

//Auth Helpers
function getUser() {
    try {
        return JSON.parse(localStorage.getItem('user'));
    } catch {
        return null;
    }
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
const ui = {
    songRow(s, i, showNumber = true) {
        const safeTitle = (s.title || '').replace(/'/g, "\\'");
        const safeArtist = (s.artistName || '').replace(/'/g, "\\'");
        return `
            <div class="song-row" onclick="playSong(${s.id}, '${safeTitle}', '${safeArtist}')">
                ${showNumber ? `<span class="song-number">${i + 1}</span>` : ''}
                <span class="song-play-icon">▶</span>
                <div class="song-thumb" style="overflow:hidden;">
                    ${s.coverImage ? `<img src="${s.coverImage}" style="width:100%;height:100%;object-fit:cover;">` : '🎵'}
                </div>
                <div class="song-info">
                    <div class="song-title">${s.title || 'Unknown'}</div>
                    <div class="song-artist">${s.artistName || 'Unknown Artist'}</div>
                </div>
                <div class="song-actions">
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
                <div class="card-img" style="background:var(--bg-3);display:flex;align-items:center;justify-content:center;font-size:2rem;color:var(--text-4);">
                    ${img}
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

function updatePlaylistFromDOM(currentSongId) {
    const rows = document.querySelectorAll('[onclick^="playSong("]');
    if (rows.length === 0) return false;

    let newPlaylist = [];
    let newIndex = -1;
    let seenIDs = new Set();

    rows.forEach((row) => {
        const match = row.getAttribute('onclick').match(/playSong\((\d+),\s*'([^']*)',\s*'([^']*)'/);
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
async function playSong(songId, title, artistName, fromList = true) {
    const playerBar = document.getElementById('playerBar');
    if (!playerBar) return;

    try {
        if (fromList) updatePlaylistFromDOM(songId);

        let song = playlist.find(s => s.id === songId);

        if (!song || !song.audioUrl) {
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
            song.audioUrl = data.audioUrl;
            song.coverImage = data.coverImage;
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
        const favs = await api(`/api/favorites/user/${user.userId}`);
        const isFav = favs.some(f => f.songId === songId);
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
            await fetch(`/api/favorites/user/${user.userId}/song/${currentSongId}`, { method: 'DELETE' });
            btn.style.color = 'var(--text-3)';
            btn.textContent = '♡';
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

// Volume control
(function initVolume() {
    const volSlider = document.querySelector('.volume-slider');
    if (!volSlider) return;

    // Set initial volume
    audio.volume = 0.7;
    const fill = volSlider.querySelector('.volume-fill');
    if (fill) fill.style.width = '70%';

    volSlider.addEventListener('click', (e) => {
        const rect = volSlider.getBoundingClientRect();
        const pct = Math.max(0, Math.min(1, (e.clientX - rect.left) / rect.width));
        audio.volume = pct;
        if (fill) fill.style.width = (pct * 100) + '%';
    });
})();

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

/* ── PJAX Implementation ────────────────────────────────────────── */
document.addEventListener('click', async (e) => {
    const a = e.target.closest('a');
    if (!a) return;

    const href = a.getAttribute('href');
    if (!href || href.startsWith('#') || href.startsWith('javascript:') ||
        a.hostname !== window.location.hostname || a.getAttribute('target') === '_blank') return;

    // Exclude explicit urls that we don't want to PJAX
    if (href.includes('/api/') || href.includes('download')) return;

    e.preventDefault();
    await navigate(href);
});

window.addEventListener('popstate', (e) => {
    navigate(window.location.pathname + window.location.search, false);
});

async function navigate(url, push = true) {
    try {
        const res = await fetch(url);
        if (!res.ok) {
            if (res.status === 401 || res.status === 403) window.location.href = '/login';
            return;
        }
        const text = await res.text();

        const parser = new DOMParser();
        const doc = parser.parseFromString(text, 'text/html');

        const newMain = doc.querySelector('main.page-content');
        const oldMain = document.querySelector('main.page-content');

        if (newMain && oldMain) {
            oldMain.innerHTML = newMain.innerHTML;
            oldMain.className = newMain.className;
            document.title = doc.title;

            if (push) history.pushState(null, '', url);

            // Re-evaluate page specific scripts
            const scripts = doc.querySelectorAll('script');
            scripts.forEach(s => {
                if (s.src && s.src.includes('app.js')) return;
                const newScript = document.createElement('script');
                if (s.src) newScript.src = s.src;
                else newScript.textContent = s.textContent;
                document.body.appendChild(newScript);
                setTimeout(() => newScript.remove(), 50); // Speed up cleanup
            });

            // Update nav active states
            document.querySelectorAll('.nav-links a').forEach(link => {
                link.classList.remove('active');
                const href = link.getAttribute('href');
                if (href === window.location.pathname || (href === '/home' && window.location.pathname === '/')) {
                    link.classList.add('active');
                }
            });

            // Re-run global init if needed
            if (typeof initPage === 'function') initPage();

            window.scrollTo(0, 0);
        } else {
            window.location.href = url;
        }
    } catch (e) {
        window.location.href = url;
    }
}

function handleUniversalSearch(query) {
    if (!query || query.trim().length === 0) return;
    if (window.location.pathname === '/home') {
        const hs = document.getElementById('searchInput');
        if (hs) {
            hs.value = query;
            if (typeof debounceSearch === 'function') debounceSearch();
        }
    } else {
        window.location.href = '/home?q=' + encodeURIComponent(query);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    if (window.location.pathname === '/home') {
        const q = new URLSearchParams(window.location.search).get('q');
        if (q) {
            setTimeout(() => {
                const hs = document.getElementById('searchInput');
                if (hs) {
                    hs.value = q;
                    if (typeof searchSongs === 'function') searchSongs();
                }
            }, 300);
        }
    }
});
