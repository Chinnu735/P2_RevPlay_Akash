

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
    if (dashLink) {
        if (user.role === 'artist') {
            dashLink.style.display = 'inline-block';
        } else {
            dashLink.style.display = 'none';
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

    // --- SPA ROUTING INTERCEPTOR ---
    document.body.addEventListener('click', async (e) => {
        const a = e.target.closest('a');
        if (!a || !a.href || a.target === '_blank' || a.hasAttribute('download')) return;

        const url = new URL(a.href);
        if (url.origin === window.location.origin && !a.hasAttribute('onclick')) {
            if (url.pathname === window.location.pathname && url.hash) return;
            if (url.pathname.startsWith('/api') || url.pathname === '/login' || url.pathname === '/register' || url.pathname === '/logout') return;

            e.preventDefault();
            navigateTo(url.pathname + url.search + url.hash);
        }
    });

    window.addEventListener('popstate', (e) => {
        navigateTo(window.location.pathname + window.location.search + window.location.hash, false);
    });
});

/**
 * SPA Navigate Function
 */
async function navigateTo(url, pushState = true) {
    try {
        const res = await fetch(url);
        if (res.status === 401 || res.status === 403) {
            window.location.replace('/login');
            return;
        }
        const html = await res.text();
        const parser = new DOMParser();
        const doc = parser.parseFromString(html, 'text/html');

        document.title = doc.title;

        const newMain = doc.querySelector('main');
        const oldMain = document.querySelector('main');
        if (newMain && oldMain) {
            oldMain.innerHTML = newMain.innerHTML;
            oldMain.className = newMain.className;
        }

        document.body.className = doc.body.className;

        const newNavLinks = doc.getElementById('navLinks');
        const oldNavLinks = document.getElementById('navLinks');
        if (newNavLinks && oldNavLinks) {
            oldNavLinks.innerHTML = newNavLinks.innerHTML;
        }

        if (pushState) {
            window.history.pushState({}, '', url);
        }

        const scripts = Array.from(doc.querySelectorAll('script:not([src])'));
        scripts.forEach(script => {
            try {
                const scriptContent = script.textContent;
                if (scriptContent.includes('const user = getUser();')) {
                    const newScript = document.createElement('script');
                    newScript.textContent = scriptContent;
                    document.body.appendChild(newScript);
                    setTimeout(() => newScript.remove(), 100);
                }
            } catch (err) {
                console.error('Error executing SPA script', err);
            }
        });

        document.getElementById('navLinks')?.classList.remove('open');

        if (window.location.hash) {
            const el = document.getElementById(window.location.hash.substring(1));
            if (el) el.scrollIntoView();
        } else {
            window.scrollTo(0, 0);
        }

    } catch (err) {
        console.error('SPA Navigation failed, falling back to hard reload', err);
        window.location.assign(url);
    }
}

/* ── API Helper ───────────────────────────────────────────────── */
async function api(url, method = 'GET', body = null) {
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), 8000); // 8 second timeout

    const opts = {
        method,
        headers: { 'Content-Type': 'application/json' },
        signal: controller.signal
    };

    const token = localStorage.getItem('token');
    if (token) {
        opts.headers['Authorization'] = 'Bearer ' + token;
    }

    if (body) opts.body = JSON.stringify(body);

    try {
        const res = await fetch(url, opts);
        clearTimeout(timeoutId);

        if (res.status === 401 || res.status === 403) {
            console.error(`Auth Error ${res.status} on ${url}`);
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            const currentPath = window.location.pathname;
            if (currentPath !== '/login' && currentPath !== '/') {
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
        clearTimeout(timeoutId);
        if (e.name === 'TypeError' && e.message === 'Failed to fetch') {
            console.error("Network or CORS error details:", e);
        } else if (e.name === 'AbortError') {
            console.error(`Request to ${url} timed out after 8s`);
        }
        throw e;
    }
}

//Toast Notifications
function showToast(message, type = 'success') {
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

document.addEventListener('click', (e) => {
    if (e.target.classList.contains('modal-backdrop')) {
        e.target.classList.remove('show');
    }
});

document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') {
        document.querySelectorAll('.modal-backdrop.show').forEach(m => m.classList.remove('show'));
        // Also close queue panel
        const queuePanel = document.getElementById('queuePanel');
        if (queuePanel && queuePanel.style.display !== 'none') {
            queuePanel.style.display = 'none';
        }
    }
});


/* ══════════════════════════════════════════════════════════════════
   MUSIC PLAYER — Full Implementation
   Features: play/pause, next/prev, seek, progress bar, shuffle,
   repeat (off/all/one), volume + mute, queue panel, keyboard shortcuts
   ══════════════════════════════════════════════════════════════════ */

const audio = new Audio();
let isPlaying = false;
let currentSongId = null;
let playlist = [];           // current playback queue
let originalPlaylist = [];   // original order before shuffle
let playlistIndex = -1;

let isShuffle = false;
let repeatMode = 0;  // 0 = off, 1 = repeat all, 2 = repeat one
let isMuted = false;
let savedVolume = 0.7;

// Format seconds to M:SS
function formatTime(sec) {
    if (!sec || isNaN(sec)) return '0:00';
    const m = Math.floor(sec / 60);
    const s = Math.floor(sec % 60);
    return `${m}:${s < 10 ? '0' : ''}${s}`;
}

/* ── CORE PLAY FUNCTION ──────────────────────────────────────── */
async function playSong(songId, title, artistName) {
    const playerBar = document.getElementById('playerBar');
    if (!playerBar) return;

    try {
        // --- Build playlist context from DOM ---
        const eventCaller = window.event ? window.event.currentTarget : null;
        if (eventCaller) {
            let container = null;
            if (eventCaller.classList.contains('song-row')) {
                container = eventCaller.closest('div[id$="List"]') || eventCaller.closest('#trackList') || eventCaller.closest('#artistSongs') || eventCaller.parentElement;
                if (container) {
                    const rows = container.querySelectorAll('.song-row');
                    const newPlaylist = [];
                    rows.forEach(row => {
                        const onclickAttr = row.getAttribute('onclick') || '';
                        const match = onclickAttr.match(/playSong\(\s*(\d+)\s*,\s*'([^']+)'\s*,\s*'([^']+)'\s*\)/);
                        if (match) {
                            newPlaylist.push({
                                id: parseInt(match[1]),
                                title: match[2].replace(/\\'/g, "'"),
                                artistName: match[3].replace(/\\'/g, "'")
                            });
                        }
                    });
                    if (newPlaylist.length > 0) {
                        playlist = newPlaylist;
                        originalPlaylist = [...playlist];
                    }
                }
            } else if (eventCaller.classList.contains('card')) {
                container = eventCaller.closest('.grid') || eventCaller.parentElement;
                if (container) {
                    const cards = container.querySelectorAll('.card');
                    const newPlaylist = [];
                    cards.forEach(card => {
                        const onclickAttr = card.getAttribute('onclick') || '';
                        const match = onclickAttr.match(/playSong\(\s*(\d+)\s*,\s*'([^']+)'\s*,\s*'([^']+)'\s*\)/);
                        if (match) {
                            newPlaylist.push({
                                id: parseInt(match[1]),
                                title: match[2].replace(/\\'/g, "'"),
                                artistName: match[3].replace(/\\'/g, "'")
                            });
                        }
                    });
                    if (newPlaylist.length > 0) {
                        playlist = newPlaylist;
                        originalPlaylist = [...playlist];
                    }
                }
            }
        }

        // Find song in playlist or fetch from API
        let song = playlist.find(s => s.id === songId);

        if (!song || !song.audioUrl) {
            const data = await api(`/api/songs/${songId}`);
            if (song) {
                song.audioUrl = data.audioUrl;
                song.coverImage = data.coverImage;
                song.genreName = data.genreName;
                song.albumName = data.albumName;
            } else {
                song = {
                    id: data.id,
                    title: data.title || title || 'Unknown',
                    artistName: data.artistName || artistName || 'Unknown Artist',
                    audioUrl: data.audioUrl,
                    coverImage: data.coverImage,
                    genreName: data.genreName,
                    albumName: data.albumName
                };
                playlist = [song];
                originalPlaylist = [...playlist];
            }
        }

        playlistIndex = playlist.findIndex(s => s.id === songId);
        if (playlistIndex === -1) playlistIndex = 0;

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
        audio.play().catch(e => console.warn('Autoplay blocked:', e));
        isPlaying = true;

        // Show player bar
        playerBar.style.display = 'flex';

        // Update UI
        document.getElementById('playerTitle').textContent = song.title;
        document.getElementById('playerArtist').textContent = song.artistName;

        const thumb = document.getElementById('playerThumb');
        if (thumb) {
            thumb.innerHTML = song.coverImage ? `<img src="${song.coverImage}" style="width:100%;height:100%;object-fit:cover;border-radius:var(--radius-xs);">` : '🎵';
        }

        updatePlayButton();
        checkFavoriteStatus(song.id);
        renderQueue();

        // Record listening history
        const user = getUser();
        if (user) {
            fetch('/api/listening-history', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + (localStorage.getItem('token') || '')
                },
                body: JSON.stringify({ userId: user.userId, songId: songId })
            }).catch(() => { });
        }

    } catch (err) {
        showToast('Failed to play song: ' + err.message, 'error');
    }
}

/* ── PLAY / PAUSE ────────────────────────────────────────────── */
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

/* ── PREVIOUS / NEXT ─────────────────────────────────────────── */
function prevSong() {
    if (playlist.length === 0) return;
    // If more than 3 seconds in, restart current song
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
        // Repeat one — restart current song
        audio.currentTime = 0;
        audio.play();
        return;
    }

    if (playlistIndex === playlist.length - 1 && repeatMode === 0) {
        // End of queue, no repeat — stop
        isPlaying = false;
        updatePlayButton();
        return;
    }

    playlistIndex = (playlistIndex + 1) % playlist.length;
    const song = playlist[playlistIndex];
    playSong(song.id, song.title, song.artistName);
}

/* ── SHUFFLE ─────────────────────────────────────────────────── */
function toggleShuffle() {
    isShuffle = !isShuffle;
    const btn = document.getElementById('shuffleBtn');
    if (btn) btn.style.color = isShuffle ? 'var(--accent)' : 'var(--text-3)';

    if (isShuffle) {
        originalPlaylist = [...playlist];
        if (playlistIndex >= 0) {
            const current = playlist[playlistIndex];
            playlist = playlist.filter((_, i) => i !== playlistIndex);
            // Fisher-Yates shuffle
            for (let i = playlist.length - 1; i > 0; i--) {
                const j = Math.floor(Math.random() * (i + 1));
                [playlist[i], playlist[j]] = [playlist[j], playlist[i]];
            }
            playlist.unshift(current);
            playlistIndex = 0;
        }
        showToast('Shuffle ON', 'success');
    } else {
        if (playlistIndex >= 0) {
            const current = playlist[playlistIndex];
            playlist = [...originalPlaylist];
            playlistIndex = playlist.findIndex(s => s.id === current.id);
        } else {
            playlist = [...originalPlaylist];
        }
        showToast('Shuffle OFF', 'success');
    }
    renderQueue();
}

/* ── REPEAT ──────────────────────────────────────────────────── */
function toggleRepeat() {
    repeatMode = (repeatMode + 1) % 3;
    const btn = document.getElementById('repeatBtn');
    if (!btn) return;

    if (repeatMode === 0) {
        btn.style.color = 'var(--text-3)';
        btn.innerHTML = '🔁';
        btn.title = 'Repeat: OFF';
        showToast('Repeat OFF', 'success');
    } else if (repeatMode === 1) {
        btn.style.color = 'var(--accent)';
        btn.innerHTML = '🔁';
        btn.title = 'Repeat: ALL';
        showToast('Repeat ALL', 'success');
    } else {
        btn.style.color = 'var(--accent)';
        btn.innerHTML = '🔂';
        btn.title = 'Repeat: ONE';
        showToast('Repeat ONE', 'success');
    }
}

/* ── SEEK ────────────────────────────────────────────────────── */
function seek(e) {
    if (!audio.duration) return;
    const rect = e.currentTarget.getBoundingClientRect();
    const pct = Math.max(0, Math.min(1, (e.clientX - rect.left) / rect.width));
    audio.currentTime = pct * audio.duration;
}

/* ── FAVORITES ───────────────────────────────────────────────── */
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
        const isFav = favs && favs.some(f => f.songId === songId);
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

/* ── ADD TO PLAYLIST ─────────────────────────────────────────── */
function openAddToPlaylistModal() {
    const user = getUser();
    if (!user) {
        showToast('Please login to use playlists', 'error');
        return;
    }
    if (!currentSongId) return;

    showModal('addToPlaylistModal');
    loadPlaylistsForModal();
}

async function loadPlaylistsForModal() {
    const listEl = document.getElementById('modalPlaylistsList');
    if (!listEl) return;

    try {
        const user = getUser();
        const data = await api(`/api/playlists/user/${user.userId}`);
        if (!data || !data.length) {
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

/* ── VOLUME & MUTE ───────────────────────────────────────────── */
function toggleMute() {
    isMuted = !isMuted;
    if (isMuted) {
        savedVolume = audio.volume;
        audio.volume = 0;
    } else {
        audio.volume = savedVolume;
    }
    updateVolumeUI();
}

function updateVolumeUI() {
    const muteBtn = document.getElementById('muteBtn');
    const fill = document.querySelector('.volume-fill');

    if (muteBtn) {
        if (isMuted || audio.volume === 0) {
            muteBtn.textContent = '🔇';
        } else if (audio.volume < 0.3) {
            muteBtn.textContent = '🔈';
        } else if (audio.volume < 0.7) {
            muteBtn.textContent = '🔉';
        } else {
            muteBtn.textContent = '🔊';
        }
    }

    if (fill) {
        fill.style.width = (isMuted ? 0 : audio.volume * 100) + '%';
    }
}

/* ── QUEUE PANEL ─────────────────────────────────────────────── */
function toggleQueuePanel() {
    const panel = document.getElementById('queuePanel');
    if (!panel) return;

    if (panel.style.display === 'none' || panel.style.display === '') {
        panel.style.display = 'flex';
        renderQueue();
    } else {
        panel.style.display = 'none';
    }
}

function renderQueue() {
    const listEl = document.getElementById('queueList');
    const countEl = document.getElementById('queueCount');
    if (!listEl) return;

    if (countEl) {
        countEl.textContent = `${playlist.length} song${playlist.length !== 1 ? 's' : ''}`;
    }

    if (playlist.length === 0) {
        listEl.innerHTML = '<div class="empty-state" style="padding:2rem;"><p>No songs in queue</p></div>';
        return;
    }

    listEl.innerHTML = playlist.map((s, i) => {
        const isCurrent = i === playlistIndex;
        return `<div class="queue-item ${isCurrent ? 'queue-item-active' : ''}" onclick="playFromQueue(${i})" style="cursor:pointer;">
            <span class="queue-item-number" style="min-width:24px;text-align:center;font-size:0.75rem;color:${isCurrent ? 'var(--accent)' : 'var(--text-4)'};">${isCurrent ? '▶' : (i + 1)}</span>
            <div class="queue-item-info" style="flex:1;min-width:0;">
                <div style="font-size:0.8rem;font-weight:${isCurrent ? '600' : '400'};color:${isCurrent ? 'var(--accent)' : 'var(--text-1)'};white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">${s.title}</div>
                <div style="font-size:0.7rem;color:var(--text-3);white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">${s.artistName || ''}</div>
            </div>
            <button class="player-btn text-muted" onclick="event.stopPropagation();removeFromQueue(${i})" style="font-size:0.7rem;opacity:0.5;cursor:pointer;" title="Remove">✕</button>
        </div>`;
    }).join('');
}

function playFromQueue(index) {
    if (index < 0 || index >= playlist.length) return;
    playlistIndex = index;
    const song = playlist[playlistIndex];
    playSong(song.id, song.title, song.artistName);
}

function removeFromQueue(index) {
    if (index < 0 || index >= playlist.length) return;
    if (index === playlistIndex) {
        showToast('Cannot remove currently playing song', 'error');
        return;
    }
    playlist.splice(index, 1);
    // Adjust playlistIndex if needed
    if (index < playlistIndex) {
        playlistIndex--;
    }
    renderQueue();
    showToast('Removed from queue');
}

function clearQueue() {
    if (playlist.length === 0) return;
    const currentSong = playlist[playlistIndex];
    playlist = currentSong ? [currentSong] : [];
    originalPlaylist = [...playlist];
    playlistIndex = 0;
    renderQueue();
    showToast('Queue cleared (keeping current song)');
}

/* ── AUDIO EVENT LISTENERS ───────────────────────────────────── */
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
    nextSong();
});

audio.addEventListener('error', () => {
    showToast('Error playing audio. Check the file URL.', 'error');
    isPlaying = false;
    updatePlayButton();
});

audio.addEventListener('volumechange', () => {
    updateVolumeUI();
});

/* ── VOLUME SLIDER INIT ──────────────────────────────────────── */
(function initVolume() {
    const volSlider = document.querySelector('.volume-slider');
    if (!volSlider) return;

    audio.volume = 0.7;
    const fill = volSlider.querySelector('.volume-fill');
    if (fill) fill.style.width = '70%';

    volSlider.addEventListener('click', (e) => {
        const rect = volSlider.getBoundingClientRect();
        const pct = Math.max(0, Math.min(1, (e.clientX - rect.left) / rect.width));
        audio.volume = pct;
        isMuted = false;
        updateVolumeUI();
    });

    // Drag support for volume
    let isDragging = false;
    volSlider.addEventListener('mousedown', (e) => {
        isDragging = true;
        const rect = volSlider.getBoundingClientRect();
        const pct = Math.max(0, Math.min(1, (e.clientX - rect.left) / rect.width));
        audio.volume = pct;
        isMuted = false;
        updateVolumeUI();
    });
    document.addEventListener('mousemove', (e) => {
        if (!isDragging) return;
        const rect = volSlider.getBoundingClientRect();
        const pct = Math.max(0, Math.min(1, (e.clientX - rect.left) / rect.width));
        audio.volume = pct;
        isMuted = false;
        updateVolumeUI();
    });
    document.addEventListener('mouseup', () => { isDragging = false; });
})();

/* ── KEYBOARD SHORTCUTS ──────────────────────────────────────── */
document.addEventListener('keydown', (e) => {
    // Space = play/pause (only if not typing in input)
    if (e.code === 'Space' && !['INPUT', 'TEXTAREA', 'SELECT'].includes(e.target.tagName)) {
        e.preventDefault();
        togglePlay();
    }
    // Arrow left/right = seek ±5s
    if (e.code === 'ArrowLeft' && audio.src && !['INPUT', 'TEXTAREA', 'SELECT'].includes(e.target.tagName)) {
        e.preventDefault();
        audio.currentTime = Math.max(0, audio.currentTime - 5);
    }
    if (e.code === 'ArrowRight' && audio.src && !['INPUT', 'TEXTAREA', 'SELECT'].includes(e.target.tagName)) {
        e.preventDefault();
        audio.currentTime = Math.min(audio.duration || 0, audio.currentTime + 5);
    }
    // M = mute toggle
    if (e.code === 'KeyM' && !['INPUT', 'TEXTAREA', 'SELECT'].includes(e.target.tagName)) {
        toggleMute();
    }
    // N = next song
    if (e.code === 'KeyN' && !['INPUT', 'TEXTAREA', 'SELECT'].includes(e.target.tagName)) {
        nextSong();
    }
    // P = previous song
    if (e.code === 'KeyP' && !['INPUT', 'TEXTAREA', 'SELECT'].includes(e.target.tagName)) {
        prevSong();
    }
    // Q = toggle queue
    if (e.code === 'KeyQ' && !['INPUT', 'TEXTAREA', 'SELECT'].includes(e.target.tagName)) {
        toggleQueuePanel();
    }
});
