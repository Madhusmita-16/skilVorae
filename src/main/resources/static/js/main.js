/**
 * SkilVorae Main JavaScript Utilities & UI Controllers
 */

// Toast notification helper
function showToast(message, type = 'info') {
  let container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    container.className = 'toast-container';
    document.body.appendChild(container);
  }

  const toast = document.createElement('div');
  toast.className = `toast toast-${type}`;
  toast.innerHTML = `<span>${message}</span>`;

  container.appendChild(toast);

  setTimeout(() => {
    toast.style.opacity = '0';
    toast.style.transition = 'opacity 300ms ease';
    setTimeout(() => toast.remove(), 300);
  }, 3500);
}

// Theme Toggle Logic (Light / Dark)
function updateThemeButtons(theme) {
  document.querySelectorAll('.theme-toggle-btn').forEach(btn => {
    const icon = btn.querySelector('.theme-icon');
    const label = btn.querySelector('.theme-label');
    if (theme === 'dark') {
      if (icon) icon.textContent = '☀️';
      if (label) label.textContent = 'Light';
      btn.title = 'Switch to Light Theme';
    } else {
      if (icon) icon.textContent = '🌙';
      if (label) label.textContent = 'Dark';
      btn.title = 'Switch to Dark Theme';
    }
  });
}

function initTheme() {
  const savedTheme = localStorage.getItem('skilvorae_theme') || 'light';
  document.documentElement.setAttribute('data-theme', savedTheme);
  updateThemeButtons(savedTheme);
}

function toggleTheme() {
  const currentTheme = document.documentElement.getAttribute('data-theme') === 'dark' ? 'dark' : 'light';
  const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
  document.documentElement.setAttribute('data-theme', newTheme);
  localStorage.setItem('skilvorae_theme', newTheme);
  updateThemeButtons(newTheme);
  showToast(`Switched to ${newTheme} mode`, 'info');
}

// Notification Center Logic
async function initNotifications() {
  const badge = document.getElementById('notif-badge');
  if (!badge) return;

  try {
    const res = await fetch('/api/notifications/unread-count');
    const data = await res.json();
    if (res.ok && data.data && data.data.count > 0) {
      badge.textContent = data.data.count;
      badge.style.display = 'inline-flex';
    } else {
      badge.style.display = 'none';
    }
  } catch (ignored) {}
}

async function toggleNotifDropdown() {
  const dropdown = document.getElementById('notif-dropdown');
  if (!dropdown) return;

  const isVisible = dropdown.style.display === 'block';
  if (isVisible) {
    dropdown.style.display = 'none';
    return;
  }

  dropdown.style.display = 'block';
  const listEl = document.getElementById('notif-list');
  listEl.innerHTML = '<div style="text-align: center; color: var(--text-muted); padding: 0.5rem;">Loading...</div>';

  try {
    const res = await fetch('/api/notifications');
    const data = await res.json();
    if (res.ok && data.data && data.data.length > 0) {
      listEl.innerHTML = data.data.map(n => `
        <div style="padding: 0.5rem 0; border-bottom: 1px solid var(--border-color); ${n.read ? 'opacity: 0.6;' : 'font-weight: 600;'}">
          <div style="color: var(--primary); font-weight: 700;">${n.title}</div>
          <div style="color: var(--text-secondary); margin-top: 0.15rem;">${n.message}</div>
        </div>
      `).join('');
    } else {
      listEl.innerHTML = '<div style="text-align: center; color: var(--text-muted); padding: 0.75rem;">No notifications found</div>';
    }
  } catch (err) {
    listEl.innerHTML = '<div style="text-align: center; color: var(--danger); padding: 0.75rem;">Failed to load notifications</div>';
  }
}

async function markAllNotificationsRead() {
  try {
    await fetch('/api/notifications/read-all', { method: 'POST' });
    const badge = document.getElementById('notif-badge');
    if (badge) badge.style.display = 'none';
    showToast('Notifications marked as read', 'success');
    toggleNotifDropdown();
  } catch (err) {
    showToast('Failed to update notifications', 'error');
  }
}

// Global Click listener to close dropdowns when clicking outside
document.addEventListener('click', (e) => {
  const dropdown = document.getElementById('notif-dropdown');
  const btn = document.getElementById('notif-bell-btn');
  if (dropdown && btn && !dropdown.contains(e.target) && !btn.contains(e.target)) {
    dropdown.style.display = 'none';
  }
});

// Initialization
document.addEventListener('DOMContentLoaded', () => {
  initTheme();
  initNotifications();

  const themeToggleBtn = document.getElementById('theme-toggle-btn');
  if (themeToggleBtn) {
    themeToggleBtn.addEventListener('click', toggleTheme);
  }
});
