

//Auth Helpers
function getUser() {
    try {
        return JSON.parse(localStorage.getItem('user'));
    } catch {
        return null;
    }
}

// Global Logout — clears session on server + client storage, then redirects to login
async function logout() {
    try {
        await fetch('/logout', { method: 'POST', credentials: 'same-origin' });
    } catch (e) { /* ignore network errors — still clear local data */ }
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    localStorage.removeItem('rp_playback');
    window.location.replace('/');
}

// Injects Logout button into .nav-actions on every page (called from DOMContentLoaded)
function initNavLogout() {
    const navActions = document.querySelector('.nav-actions');
    if (!navActions || document.getElementById('logoutBtn')) return;
    const btn = document.createElement('button');
    btn.id = 'logoutBtn';
    btn.className = 'btn btn-ghost btn-sm';
    btn.textContent = 'Logout';
    btn.style.cssText = 'margin-left:0.5rem;font-size:0.8rem;padding:0.3rem 0.8rem;border:1px solid var(--border-2);border-radius:var(--radius-sm);cursor:pointer;color:var(--text-2);background:transparent;transition:all 0.2s;';
    btn.onmouseenter = () => { btn.style.color = 'var(--danger,#ff4d4d)'; btn.style.borderColor = 'var(--danger,#ff4d4d)'; };
    btn.onmouseleave = () => { btn.style.color = 'var(--text-2)'; btn.style.borderColor = 'var(--border-2)'; };
    btn.onclick = logout;
    navActions.appendChild(btn);
}

//Auto-init: Handle Navbar Links & Badges
document.addEventListener('DOMContentLoaded', () => {
    const user = getUser();
    const path = window.location.pathname;
    const publicPages = ['/', '/login', '/register', '/forgot-password'];

    // Global protection: redirect unauthenticated users away from protected pages
    if (!user && !publicPages.includes(path)) {
        window.location.replace('/login');
        return;
    }

    // Stop execution of remaining navbar scripts if no user (e.g. on public pages)
    if (!user) return;

    const dashLink = document.getElementById('dashboardLink');
    if (dashLink && user.role !== 'artist') {
        dashLink.style.display = 'none';
    }

    // Set navbar avatar — profile image or first letter of username
    const navAvatar = document.getElementById('userAvatar');
    if (navAvatar) {
        if (user.profilePicture) {
            // If it doesn't start with / or http, assume it's a filename in /uploads/
            let picUrl = user.profilePicture;
            if (!picUrl.startsWith('/') && !picUrl.startsWith('http')) {
                picUrl = '/uploads/' + picUrl;
            }
            // Add cache busting
            const cacheBust = picUrl.includes('?') ? '&v=' : '?v=' + Date.now();
            navAvatar.innerHTML = `<img src="${picUrl}${cacheBust}" data-retries="0" style="width:100%;height:100%;object-fit:cover;border-radius:50%;" onerror="if(this.dataset.retries < 5){ this.dataset.retries++; setTimeout(()=>this.src='${picUrl}${cacheBust}', 500); } else { this.parentElement.textContent='${(user.displayName || user.username || 'U').charAt(0).toUpperCase()}'; }">`;
        } else {
            navAvatar.textContent = (user.displayName || user.username || 'U').charAt(0).toUpperCase();
        }
    }

    if (user.role === 'artist') {
        const avatar = document.getElementById('userAvatar');
        if (avatar && avatar.parentElement && !document.getElementById('navArtistBadge')) {
            const badge = document.createElement('span');
            badge.id = 'navArtistBadge';
            badge.className = 'badge badge-accent';
            badge.textContent = 'ARTIST';
            badge.style.marginRight = '0.75rem';
            badge.style.fontSize = '0.65rem';
            badge.style.transform = 'translateY(-1px)';
            avatar.parentElement.insertBefore(badge, avatar);
        }
    }

    // Restore music playback from previous page
    restorePlayback();

    // Inject Logout button into navbar header
    initNavLogout();
});

/* ── API Helper ───────────────────────────────────────────────── */
async function api(url, method = 'GET', body = null) {
    const opts = {
        method,
        headers: { 'Content-Type': 'application/json' }
    };

    const token = localStorage.getItem('token');
    if (token) {
        opts.headers['Authorization'] = 'Bearer ' + token;
    }

    if (body) opts.body = JSON.stringify(body);

    try {
        const res = await fetch(url, opts);

        if (res.status === 401 || res.status === 403) {
            console.error(`Auth Error ${res.status} on ${url}`);
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            if (window.location.pathname !== '/login' && window.location.pathname !== '/') {
                window.location.replace('/login');
            }
            return null;
        }

        if (!res.ok) {
            const err = await res.json().catch(() => ({}));
            throw new Error(err.message || `HTTP ${res.status}`);
        }

        const text = await res.text();
        return text ? JSON.parse(text) : null;
    } catch (e) {
        console.error(`API Error on ${url}:`, e);
        throw e;
    }
}

/* ── Unified UI Components ─────────────────────────────────────── */
window.ui = {
    formatDuration(seconds) {
        if (!seconds) return '--:--';
        const m = Math.floor(seconds / 60);
        const s = Math.floor(seconds % 60);
        return `${m}:${s.toString().padStart(2, '0')}`;
    },
    songRow(s, i, showNumber = true) {
        if (!s.duration) {
            setTimeout(() => fixMissingDuration(s.id), 500);
        }
        const safeTitle = (s.title || '').replace(/'/g, "\\'");
        const safeArtist = (s.artistName || '').replace(/'/g, "\\'");
        return `
            <div class="song-row" onclick="playSong(${s.id}, '${safeTitle}', '${safeArtist}')">
                ${showNumber ? `<span class="song-number">${i + 1}</span>` : ''}
                <span class="song-play-icon">▶</span>
                <div class="song-thumb" style="overflow:hidden;border-radius:4px;background:var(--bg-3);">
                    ${(() => {
                let ci = s.coverImage;
                if (ci && !ci.startsWith('/') && !ci.startsWith('http')) ci = '/uploads/' + ci;
                return ci ? `<img src="${ci}" style="width:100%;height:100%;object-fit:cover;" onerror="this.src='/images/default-song.png'">` : '🎵';
            })()}
                </div>
                <div class="song-info">
                <div class="song-title">${s.title || 'Unknown'}</div>
                <div class="song-artist">${s.artistName || 'Unknown Artist'}</div>
            </div>
            <div class="song-actions" style="display:flex;align-items:center;gap:1rem;">
                <span class="song-duration" id="dur-${s.id}" style="color:var(--text-3);font-size:0.85rem;font-variant-numeric:tabular-nums;">${ui.formatDuration(s.duration)}</span>
                <button class="btn-add-playlist" onclick="event.stopPropagation();openAddToPlaylistModal(${s.id})" title="Add to Playlist">+</button>
            </div>
        </div>`;
    },
    card(item, type = 'song') {
        const safeTitle = (item.title || item.name || '').replace(/'/g, "\\'");
        const safeArtist = (item.artistName || '').replace(/'/g, "\\'");
        let icon = '🎵';
        let url = '#';
        if (type === 'album') { icon = '◉'; url = `/album/${item.id}`; }
        else if (type === 'podcast') { icon = '🎙️'; url = `/podcast/${item.id}`; }
        else if (type === 'artist') { icon = '👤'; url = `/artist/${item.id}`; }

        const onclick = type === 'song' ? `onclick="playSong(${item.id || item.songId}, '${safeTitle}', '${safeArtist}')" style="cursor:pointer;"` : `href="${url}"`;
        const img = item.coverImage ? `<img src="${item.coverImage}" style="width:100%;height:100%;object-fit:cover;" onerror="this.style.display='none';this.parentElement.innerHTML='${icon}'">` : icon;

        return `
            <${type === 'song' ? 'div' : 'a'} ${onclick} class="card">
                <div class="card-img" style="background:var(--bg-1);display:flex;align-items:center;justify-content:center;font-size:2.25rem;color:var(--text-4);overflow:hidden;border-radius:var(--radius-sm);">
                    ${(() => {
                let ci = item.coverImage || item.profilePicture;
                if (ci && !ci.startsWith('/') && !ci.startsWith('http')) ci = '/uploads/' + ci;
                return ci ? `<img src="${ci}" style="width:100%;height:100%;object-fit:cover;" onerror="this.style.display='none';this.parentElement.innerHTML='${icon}'">` : icon;
            })()}
                </div>
                <div class="card-body">
                    <div class="card-title">${item.title || item.name || ''}</div>
                    <div class="card-subtitle">${item.artistName || ''} ${item.playedAt ? ' · ' + new Date(item.playedAt).toLocaleDateString() : ''}</div>
                </div>
            </${type === 'song' ? 'div' : 'a'}>`;
    },
    empty(icon, message) {
        return `<div class="empty-state"><div class="empty-icon">${icon}</div><p>${message}</p></div>`;
    }
};

//Toast Notifications
function showToast(message, type = 'success') {
    // Remove existing
    document.querySelectorAll('.toast').forEach(t => t.remove());

    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.textContent = message;
    document.body.appendChild(toast);

    requestAnimationFrame(() => toast.classList.add('show'));
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

/* ── Modal Helpers ────────────────────────────────────────────── */
function showModal(id) {
    document.getElementById(id).classList.add('show');
}

function hideModal(id) {
    document.getElementById(id).classList.remove('show');
}

// Close modal on backdrop click
document.addEventListener('click', (e) => {
    if (e.target.classList.contains('modal-backdrop')) {
        e.target.classList.remove('show');
    }
});

// Close modal on Escape
document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') {
        document.querySelectorAll('.modal-backdrop.show').forEach(m => m.classList.remove('show'));
    }
});


//MUSIC PLAYER — Full HTML5 Audio Implementation


const audio = new Audio();
let isPlaying = false;
let currentSongId = null;
let playlist = [];      // queue of song objects {id, title, artistName, audioUrl}
let originalPlaylist = []; // Keeps track of original queue before shuffling
let playlistIndex = -1;

let isShuffle = false;
let repeatMode = 0; // 0 = off, 1 = repeat all, 2 = repeat one

// Save playback state to localStorage (called on beforeunload and periodically)
function savePlaybackState() {
    if (!currentSongId || !audio.src) {
        localStorage.removeItem('rp_playback');
        return;
    }
    const currentSong = playlist.find(s => s.id === currentSongId) || {};
    localStorage.setItem('rp_playback', JSON.stringify({
        songId: currentSongId,
        title: currentSong.title || '',
        artistName: currentSong.artistName || '',
        audioUrl: currentSong.audioUrl || audio.src,
        coverImage: currentSong.coverImage || '',
        currentTime: audio.currentTime || 0,
        volume: audio.volume,
        wasPlaying: isPlaying,
        playlist: playlist.slice(0, 50), // cap to avoid quota
        playlistIndex: playlistIndex
    }));
}

// Restore playback from localStorage on page load
function restorePlayback() {
    try {
        const saved = JSON.parse(localStorage.getItem('rp_playback'));
        if (!saved || !saved.audioUrl) return;

        const playerBar = document.getElementById('playerBar');
        if (!playerBar) return;

        // Restore state
        currentSongId = saved.songId;
        if (saved.playlist && saved.playlist.length) {
            playlist = saved.playlist;
            originalPlaylist = [...saved.playlist];
            playlistIndex = saved.playlistIndex || 0;
        }

        // Show player bar immediately
        playerBar.style.display = 'flex';
        document.getElementById('playerTitle').textContent = saved.title || 'Unknown';
        document.getElementById('playerArtist').textContent = saved.artistName || '';
        const thumb = document.getElementById('playerThumb');
        if (thumb) {
            thumb.innerHTML = saved.coverImage ? `<img src="${saved.coverImage}" style="width:100%;height:100%;object-fit:cover;border-radius:var(--radius-xs);">` : '🎵';
        }

        // Update volume UI
        const volFill = document.querySelector('.volume-fill');
        if (volFill) volFill.style.width = ((saved.volume || 0.7) * 100) + '%';

        // Set up audio — currentTime MUST be set after loadedmetadata
        audio.preload = 'auto';
        audio.volume = saved.volume || 0.7;
        audio.src = saved.audioUrl;

        const resumeTime = saved.currentTime || 0;
        const shouldPlay = saved.wasPlaying;

        // Wait for metadata to load before seeking and playing
        const onMetaLoaded = () => {
            audio.removeEventListener('loadedmetadata', onMetaLoaded);
            if (resumeTime > 0 && resumeTime < audio.duration) {
                audio.currentTime = resumeTime;
            }
            if (shouldPlay) {
                audio.play().then(() => {
                    isPlaying = true;
                    updatePlayButton();
                }).catch(() => {
                    isPlaying = false;
                    updatePlayButton();
                });
            } else {
                isPlaying = false;
                updatePlayButton();
            }
        };
        audio.addEventListener('loadedmetadata', onMetaLoaded);

        // Fallback: if metadata is already loaded (cached), fire immediately
        if (audio.readyState >= 1) {
            onMetaLoaded();
        }

        isPlaying = false;
        updatePlayButton();
        checkFavoriteStatus(saved.songId);
    } catch (e) {
        console.error('Failed to restore playback:', e);
    }
}

// Save state before page unload
window.addEventListener('beforeunload', savePlaybackState);

function updatePlaylistFromDOM(currentSongId) {
    // Don't rebuild playlist if music is already playing (search results can disrupt)
    if (isPlaying && playlist.length > 0 && playlist.some(s => s.id === currentSongId)) {
        playlistIndex = playlist.findIndex(s => s.id === currentSongId);
        return true;
    }
    const rows = document.querySelectorAll('[onclick^="playSong("]');
    if (rows.length === 0) return false;

    let newPlaylist = [];
    let newIndex = -1;
    let seenIDs = new Set();

    rows.forEach((row) => {
        const match = row.getAttribute('onclick').match(/playSong\((\d+),\s*'([^']*)',\s*'([^']*)'/)
        if (match) {
            const id = parseInt(match[1]);
            const title = match[2].replace(/\\'/g, "'");
            const artist = match[3].replace(/\\'/g, "'");
            if (!seenIDs.has(id)) {
                seenIDs.add(id);
                newPlaylist.push({ id, title, artistName: artist });
                if (id === currentSongId) newIndex = newPlaylist.length - 1;
            }
        }
    });

    if (newPlaylist.length > 0 && newIndex !== -1) {
        playlist = newPlaylist;
        playlistIndex = newIndex;
        originalPlaylist = [...newPlaylist];
        isShuffle = false;
        const btn = document.getElementById('shuffleBtn');
        if (btn) {
            btn.style.color = 'var(--text-3)';
            btn.innerHTML = `🔀 <span style="font-size:0.6rem;vertical-align:super;">OFF</span>`;
        }
        return true;
    }
    return false;
}

// Format seconds to M:SS
function formatTime(sec) {
    if (!sec || isNaN(sec)) return '0:00';
    const m = Math.floor(sec / 60);
    const s = Math.floor(sec % 60);
    return `${m}:${s < 10 ? '0' : ''}${s}`;
}

// Core play function — fetches song data from API, loads audio, plays
async function playSong(songId, title, artistName, fromListOrAudioUrl = true, paramCoverImage = null) {
    const playerBar = document.getElementById('playerBar');
    if (!playerBar) return;

    try {
        let fromList = true;
        let directAudioUrl = null;
        if (typeof fromListOrAudioUrl === 'string') {
            fromList = false;
            directAudioUrl = fromListOrAudioUrl.trim();
        } else {
            fromList = fromListOrAudioUrl;
        }

        if (fromList) updatePlaylistFromDOM(songId);

        let song = playlist.find(s => s.id === songId);

        if (!song || !song.audioUrl) {
            if (directAudioUrl) {
                if (!song) {
                    song = { id: songId, title: title || 'Unknown', artistName: artistName || 'Unknown Artist' };
                    playlist.push(song);
                    playlistIndex = playlist.length - 1;
                }
                song.audioUrl = (directAudioUrl.startsWith('http') || directAudioUrl.startsWith('/')) ? directAudioUrl : '/uploads/' + directAudioUrl;
                song.coverImage = paramCoverImage;
            } else {
                // Fetch from API to get audioUrl
                const data = await api(`/api/songs/${songId}`);
                if (!song) {
                    song = {
                        id: data.id,
                        title: data.title || title || 'Unknown',
                        artistName: data.artistName || artistName || 'Unknown Artist'
                    };
                    playlist.push(song);
                    playlistIndex = playlist.length - 1;
                }
                song.audioUrl = (data.audioUrl.startsWith('http') || data.audioUrl.startsWith('/')) ? data.audioUrl : '/uploads/' + data.audioUrl;
                song.coverImage = data.coverImage;
            }
        } else {
            playlistIndex = playlist.indexOf(song);
        }

        // Update title/artist overrides if provided
        if (title) song.title = title;
        if (artistName) song.artistName = artistName;

        if (!song.audioUrl) {
            showToast('No audio file for this song', 'error');
            return;
        }

        // Load and play
        currentSongId = song.id;
        audio.src = song.audioUrl;
        audio.load();
        audio.play();
        isPlaying = true;

        // Show player bar
        playerBar.style.display = 'flex';

        // Update UI
        document.getElementById('playerTitle').textContent = song.title;
        document.getElementById('playerArtist').textContent = song.artistName;

        // Handle default music thumb picture
        const thumb = document.getElementById('playerThumb');
        if (thumb) {
            thumb.innerHTML = song.coverImage ? `<img src="${song.coverImage}" style="width:100%;height:100%;object-fit:cover;border-radius:var(--radius-xs);">` : '🎵';
        }

        updatePlayButton();
        checkFavoriteStatus(song.id);

        // Record listening history
        const user = getUser();
        if (user) {
            api('/api/listening-history', 'POST', { userId: user.userId, songId: songId })
                .catch(err => console.error("Failed to insert history", err));
        }

    } catch (err) {
        showToast('Failed to play song: ' + err.message, 'error');
    }
}

function togglePlay() {
    if (!audio.src) return;
    if (isPlaying) {
        audio.pause();
    } else {
        audio.play();
    }
    isPlaying = !isPlaying;
    updatePlayButton();
}

function updatePlayButton() {
    const btn = document.getElementById('playPauseBtn');
    if (btn) btn.textContent = isPlaying ? '⏸' : '▶';
}

// Seek — click on progress bar to jump to position
function seek(e) {
    if (!audio.duration) return;
    const track = e.currentTarget;
    const rect = track.getBoundingClientRect();
    const pct = Math.max(0, Math.min(1, (e.clientX - rect.left) / rect.width));
    audio.currentTime = pct * audio.duration;
}

// Queue UI
function showQueue() {
    const modal = document.getElementById('queueModal');
    if (!modal) return;
    const list = document.getElementById('queueList');

    if (playlist.length === 0) {
        list.innerHTML = '<div class="empty-state"><div class="empty-icon">🎵</div><p>Queue is empty. Play a song first.</p></div>';
    } else {
        list.innerHTML = playlist.map((s, i) => `
            <div onclick="playFromQueue(${i})" style="display:flex;align-items:center;gap:0.75rem;padding:0.65rem 0.75rem;cursor:pointer;border-radius:var(--radius-sm);transition:background 0.15s;${i === playlistIndex ? 'background:var(--accent-dim);' : ''}"
                 onmouseenter="this.style.background='var(--bg-3)'" onmouseleave="this.style.background='${i === playlistIndex ? 'var(--accent-dim)' : ''}'">
                <span style="width:24px;text-align:center;font-size:0.75rem;color:${i === playlistIndex ? 'var(--accent)' : 'var(--text-4)'};">${i === playlistIndex ? '▶' : (i + 1)}</span>
                <div style="flex:1;min-width:0;">
                    <div style="font-size:0.85rem;font-weight:${i === playlistIndex ? '600' : '400'};color:${i === playlistIndex ? 'var(--accent)' : 'var(--text-1)'};white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">${s.title || 'Unknown'}</div>
                    <div style="font-size:0.7rem;color:var(--text-3);white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">${s.artistName || ''}</div>
                </div>
            </div>
        `).join('');
    }

    showModal('queueModal');
}

function playFromQueue(index) {
    if (index < 0 || index >= playlist.length) return;
    playlistIndex = index;
    const song = playlist[index];
    hideModal('queueModal');
    playSong(song.id, song.title, song.artistName);
}

function prevSong() {
    if (playlist.length === 0) return;
    if (audio.currentTime > 3) {
        audio.currentTime = 0;
        return;
    }
    playlistIndex = (playlistIndex - 1 + playlist.length) % playlist.length;
    const song = playlist[playlistIndex];
    playSong(song.id, song.title, song.artistName);
}

function nextSong() {
    if (playlist.length === 0) return;

    if (repeatMode === 2) {
        // Repeat one
        audio.currentTime = 0;
        audio.play();
        return;
    }

    if (playlistIndex === playlist.length - 1 && repeatMode === 0) {
        // End of list and no repeat
        isPlaying = false;
        updatePlayButton();
        return;
    }

    playlistIndex = (playlistIndex + 1) % playlist.length;
    const song = playlist[playlistIndex];
    playSong(song.id, song.title, song.artistName);
}

function toggleShuffle() {
    isShuffle = !isShuffle;
    const btn = document.getElementById('shuffleBtn');
    if (btn) {
        btn.style.color = isShuffle ? 'var(--accent)' : 'var(--text-3)';
        btn.innerHTML = `🔀 <span style="font-size:0.6rem;vertical-align:super;">${isShuffle ? 'ON' : 'OFF'}</span>`;
    }

    if (isShuffle) {
        originalPlaylist = [...playlist];
        if (playlistIndex >= 0) {
            const current = playlist[playlistIndex];
            playlist = playlist.filter((_, i) => i !== playlistIndex);
            for (let i = playlist.length - 1; i > 0; i--) {
                const j = Math.floor(Math.random() * (i + 1));
                [playlist[i], playlist[j]] = [playlist[j], playlist[i]];
            }
            playlist.unshift(current);
            playlistIndex = 0;
        }
    } else {
        if (playlistIndex >= 0) {
            const current = playlist[playlistIndex];
            playlist = [...originalPlaylist];
            playlistIndex = playlist.findIndex(s => s.id === current.id);
        } else {
            playlist = [...originalPlaylist];
        }
    }
}

function toggleRepeat() {
    repeatMode = (repeatMode + 1) % 3;
    const btn = document.getElementById('repeatBtn');
    if (!btn) return;

    if (repeatMode === 0) {
        btn.style.color = 'var(--text-3)';
        btn.innerHTML = '🔁 <span style="font-size:0.6rem;vertical-align:super;">OFF</span>';
    } else if (repeatMode === 1) {
        btn.style.color = 'var(--accent)';
        btn.innerHTML = '🔁 <span style="font-size:0.6rem;vertical-align:super;">ALL</span>';
    } else {
        btn.style.color = 'var(--accent)';
        btn.innerHTML = '🔂 <span style="font-size:0.6rem;vertical-align:super;">1</span>';
    }
}

async function checkFavoriteStatus(songId) {
    const user = getUser();
    const btn = document.getElementById('favoriteBtn');
    if (!btn) return;

    if (!user) {
        btn.style.color = 'var(--text-3)';
        btn.textContent = '♡';
        return;
    }

    try {
        const isFav = await api(`/api/favorites/check?userId=${user.userId}&songId=${songId}`);
        btn.style.color = isFav ? 'var(--accent)' : 'var(--text-3)';
        btn.textContent = isFav ? '♥' : '♡';
    } catch (e) { }
}

async function toggleFavorite() {
    const user = getUser();
    if (!user) {
        showToast('Please login to favorite songs', 'error');
        return;
    }
    if (!currentSongId) return;

    const btn = document.getElementById('favoriteBtn');
    if (btn.disabled) return;
    btn.disabled = true;

    const isFav = btn.textContent.includes('♥');

    try {
        if (isFav) {
            await api(`/api/favorites/user/${user.userId}/song/${currentSongId}`, 'DELETE');
            btn.style.color = 'var(--text-3)';
            btn.textContent = '♡';
            showToast('Removed from favorites');
        } else {
            await api('/api/favorites', 'POST', { userId: user.userId, songId: currentSongId });
            btn.style.color = 'var(--accent)';
            btn.textContent = '♥';
            showToast('Added to favorites');
        }
    } catch (e) {
        showToast((isFav ? 'Failed to unfavorite' : 'Failed to favorite'), 'error');
    }
    btn.disabled = false;
}

function openAddToPlaylistModal(songId) {
    const user = getUser();
    if (!user) {
        showToast('Please login to use playlists', 'error');
        return;
    }
    if (songId) currentSongId = songId;
    if (!currentSongId) {
        showToast('No song selected', 'error');
        return;
    }

    showModal('addToPlaylistModal');
    loadPlaylistsForModal();
}

async function loadPlaylistsForModal() {
    const listEl = document.getElementById('modalPlaylistsList');
    if (!listEl) return;

    try {
        const user = getUser();
        const data = await api(`/api/playlists/user/${user.userId}`);
        if (!data.length) {
            listEl.innerHTML = '<p class="text-muted" style="font-size:0.85rem;">You have no playlists yet.</p>';
            return;
        }

        listEl.innerHTML = data.map(p => `
            <div class="song-row" style="cursor:pointer;" onclick="addCurrentSongToPlaylist(${p.id})">
                <div class="song-thumb" style="background:var(--bg-3);display:flex;align-items:center;justify-content:center;font-size:1rem;">📋</div>
                <div class="song-info">
                    <div class="song-title">${p.name}</div>
                    <div class="song-artist">${p.privacy}</div>
                </div>
                <span class="text-accent">+</span>
            </div>
        `).join('');
    } catch (e) { }
}

async function addCurrentSongToPlaylist(playlistId) {
    if (!currentSongId) return;
    try {
        await api('/api/playlist-songs', 'POST', {
            playlistId: playlistId,
            songId: currentSongId,
            orderIndex: 0
        });
        showToast('Song added to playlist!', 'success');
        hideModal('addToPlaylistModal');
    } catch (e) {
        showToast('Failed or already in playlist', 'error');
    }
}

async function createPlaylist(e) {
    if (e) e.preventDefault();
    const user = getUser();
    if (!user) return;
    try {
        const newPlaylist = await api('/api/playlists', 'POST', {
            userId: user.userId,
            name: document.getElementById('playlistName').value.trim(),
            description: document.getElementById('playlistDesc').value.trim(),
            privacy: document.getElementById('playlistPrivacy').value
        });
        hideModal('createPlaylistModal');
        // If we are on playlists page, update it
        if (typeof loadMyPlaylists === 'function') loadMyPlaylists();
        // Clear form
        document.getElementById('playlistName').value = '';
        document.getElementById('playlistDesc').value = '';
        showToast('Playlist created!', 'success');

        // Auto-select if opened from Add to Playlist modal
        if (document.getElementById('addToPlaylistModal').classList.contains('show') || currentSongId) {
            setTimeout(() => addCurrentSongToPlaylist(newPlaylist.id), 500);
        }
    } catch (e) { showToast('Failed to create playlist', 'error'); }
}

function seek(e) {
    if (!audio.duration) return;
    const rect = e.currentTarget.getBoundingClientRect();
    const pct = (e.clientX - rect.left) / rect.width;
    audio.currentTime = pct * audio.duration;
}

// Audio event listeners
audio.addEventListener('timeupdate', () => {
    const fill = document.getElementById('progressFill');
    const currentEl = document.getElementById('currentTime');
    if (fill && audio.duration) {
        fill.style.width = (audio.currentTime / audio.duration * 100) + '%';
    }
    if (currentEl) {
        currentEl.textContent = formatTime(audio.currentTime);
    }
    // Periodically save playback state (every ~5 seconds)
    if (Math.floor(audio.currentTime) % 5 === 0) {
        savePlaybackState();
    }
});

audio.addEventListener('loadedmetadata', () => {
    const totalEl = document.getElementById('totalTime');
    if (totalEl) {
        totalEl.textContent = formatTime(audio.duration);
    }
});

audio.addEventListener('ended', () => {
    // Logic handled by nextSong
    nextSong();
});

audio.addEventListener('error', () => {
    showToast('Error playing audio. Check the file URL.', 'error');
    isPlaying = false;
    updatePlayButton();
});

// Volume control — initialized after DOM is ready
function initVolume() {
    const volSlider = document.querySelector('.volume-slider');
    if (!volSlider) return;

    audio.volume = 0.7;
    const fill = volSlider.querySelector('.volume-fill');
    if (fill) fill.style.width = '70%';

    function setVolume(e) {
        const rect = volSlider.getBoundingClientRect();
        const pct = Math.max(0, Math.min(1, (e.clientX - rect.left) / rect.width));
        audio.volume = pct;
        if (fill) fill.style.width = (pct * 100) + '%';
        // Update speaker icon
        const icon = volSlider.parentElement.querySelector('span');
        if (icon) icon.textContent = pct === 0 ? '🔇' : pct < 0.5 ? '🔉' : '🔊';
    }

    // Click to set volume
    volSlider.addEventListener('click', setVolume);

    // Drag to adjust volume
    let dragging = false;
    volSlider.addEventListener('mousedown', (e) => {
        dragging = true;
        setVolume(e);
        e.preventDefault();
    });
    document.addEventListener('mousemove', (e) => {
        if (dragging) setVolume(e);
    });
    document.addEventListener('mouseup', () => { dragging = false; });

    // Mute toggle on speaker icon click
    const speakerIcon = volSlider.parentElement.querySelector('span');
    if (speakerIcon) {
        let savedVolume = 0.7;
        speakerIcon.style.cursor = 'pointer';
        speakerIcon.addEventListener('click', () => {
            if (audio.volume > 0) {
                savedVolume = audio.volume;
                audio.volume = 0;
                if (fill) fill.style.width = '0%';
                speakerIcon.textContent = '🔇';
            } else {
                audio.volume = savedVolume;
                if (fill) fill.style.width = (savedVolume * 100) + '%';
                speakerIcon.textContent = savedVolume < 0.5 ? '🔉' : '🔊';
            }
        });
    }
}

// Run initVolume when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initVolume);
} else {
    initVolume();
}

// Keyboard shortcuts
document.addEventListener('keydown', (e) => {
    // Space = play/pause (only if not typing in input)
    if (e.code === 'Space' && !['INPUT', 'TEXTAREA', 'SELECT'].includes(e.target.tagName)) {
        e.preventDefault();
        togglePlay();
    }
    // Arrow left/right = seek ±5s
    if (e.code === 'ArrowLeft' && audio.src) {
        audio.currentTime = Math.max(0, audio.currentTime - 5);
    }
    if (e.code === 'ArrowRight' && audio.src) {
        audio.currentTime = Math.min(audio.duration || 0, audio.currentTime + 5);
    }
});

/* ── Genre Filter — global so it works from onclick attrs after SPA nav ── */
async function filterByGenre(id, name) {
    // If we are on home page, filter inline; otherwise navigate to home with genre param
    const searchResults = document.getElementById('searchResults');
    const list = document.getElementById('searchList');
    if (!searchResults || !list) {
        spaNavigate('/home?genre=' + id);
        return;
    }
    try {
        const data = await api('/api/songs/genre/' + id);
        searchResults.classList.remove('hidden');
        const h2 = document.querySelector('#searchResults .section-header h2');
        if (h2) h2.textContent = name || 'Genre Results';
        list.innerHTML = data && data.length
            ? data.map((s, i) => ui.songRow(s, i)).join('')
            : ui.empty('🎵', 'No songs in this genre.');
        searchResults.scrollIntoView({ behavior: 'smooth' });
    } catch (e) {
        list.innerHTML = ui.empty('❌', 'Could not load genre.');
    }
}

/* ── Universal Search (no PJAX — normal navigation) ────────────── */
function handleUniversalSearch(query) {
    if (!query || query.trim().length === 0) return;
    if (window.location.pathname === '/home') {
        const hs = document.getElementById('searchInput');
        if (hs) {
            hs.value = query;
            if (typeof debounceSearch === 'function') debounceSearch();
        }
    } else {
        spaNavigate('/home?q=' + encodeURIComponent(query));
    }
}

/* ── Custom Mini-SPA Router (Seamless Audio) ────────────── */
// Intercept clicks on same-origin links to prevent hard reloads and keep audio playing
document.addEventListener('click', e => {
    // Find closest anchor tag
    const a = e.target.closest('a');
    if (!a || !a.href) return;

    // Ignore external links, new tabs, or links asking to be ignored
    if (a.target === '_blank' || a.host !== window.location.host || a.hasAttribute('download')) return;

    // Ignore hash links
    if (a.getAttribute('href').startsWith('#')) return;

    // Prevent default hard navigation
    e.preventDefault();
    spaNavigate(a.href);
});

// Handle browser Back/Forward buttons
window.addEventListener('popstate', () => {
    spaNavigate(window.location.href, false);
});

async function spaNavigate(url, pushState = true) {
    try {
        // Optional loading indictor logic could go here
        const response = await fetch(url);
        if (!response.ok) {
            if (response.status === 401) { window.location.href = '/login'; return; }
            throw new Error('Failed to load page');
        }

        const html = await response.text();

        // Parse the fetched HTML
        const parser = new DOMParser();
        const doc = parser.parseFromString(html, 'text/html');

        // Extract main content and title
        const newMain = doc.querySelector('main');
        if (!newMain) {
            // fallback to hard reload if the target page structure is radically different
            window.location.href = url;
            return;
        }

        document.title = doc.title;

        // Swap out the main content
        const currentMain = document.querySelector('main');
        if (currentMain) {
            currentMain.innerHTML = newMain.innerHTML;
            currentMain.className = newMain.className; // sync classes (e.g. app-bg vs nothing)
        }

        // Update active states in Navbar
        document.querySelectorAll('.nav-links a').forEach(link => {
            if (link.getAttribute('href') === new URL(url).pathname) {
                link.classList.add('active');
            } else {
                link.classList.remove('active');
            }
        });

        // Re-initialize specific page scripts safely
        if (typeof initPage === 'function') {
            // Because initPage is often declared globally in inline <script> tags on the HTML templates,
            // we need to extract and evaluate inline scripts within the new <main> or the page itself.
            const newScripts = doc.querySelectorAll('script:not([src])');
            newScripts.forEach(script => {
                if (script.textContent.includes('initPage')) {
                    try {
                        // Execute the new page's inline JS logic in global scope so functions like loadTrending wire up
                        const fn = new Function(script.textContent);
                        fn();
                    } catch (err) { console.error('SPA Script execution error', err); }
                }
            });
        }

        // Push state to browser history
        if (pushState) {
            window.history.pushState({}, doc.title, url);
        }

        // Scroll to top
        window.scrollTo(0, 0);

    } catch (e) {
        console.error('SPA Navigation Error:', e);
        window.location.href = url; // Fallback to hard reload on error
    }
}
