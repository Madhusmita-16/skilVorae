/**
 * SkilVorae Chart.js Analytics Engine
 */

function initActivityChart(weeklyData) {
  const ctx = document.getElementById('activityChart');
  if (!ctx) return;

  new Chart(ctx, {
    type: 'line',
    data: {
      labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
      datasets: [{
        label: 'Lessons Completed',
        data: weeklyData || [2, 4, 3, 5, 2, 6, 4],
        borderColor: '#7C3AED',
        backgroundColor: 'rgba(124, 58, 237, 0.12)',
        borderWidth: 3,
        fill: true,
        tension: 0.4,
        pointBackgroundColor: '#C7F36B',
        pointBorderColor: '#7C3AED',
        pointRadius: 5
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { display: false }
      },
      scales: {
        y: {
          beginAtZero: true,
          grid: { color: 'rgba(124, 58, 237, 0.08)' },
          ticks: { stepSize: 1, color: '#64748B' }
        },
        x: {
          grid: { display: false },
          ticks: { color: '#64748B' }
        }
      }
    }
  });
}

function initInstructorChart(monthlyEnrollment, labels) {
  const ctx = document.getElementById('instructorEnrollmentChart');
  if (!ctx) return;

  new Chart(ctx, {
    type: 'bar',
    data: {
      labels: labels || ['Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug'],
      datasets: [{
        label: 'Monthly Enrollments',
        data: monthlyEnrollment || [12, 19, 25, 32, 45, 58, 72],
        backgroundColor: '#7C3AED',
        borderRadius: 8,
        hoverBackgroundColor: '#8B5CF6'
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: { legend: { display: false } },
      scales: {
        y: { beginAtZero: true, grid: { color: 'rgba(0,0,0,0.05)' } },
        x: { grid: { display: false } }
      }
    }
  });
}

function initAdminUserGrowthChart(growthData, labels) {
  const ctx = document.getElementById('adminGrowthChart');
  if (!ctx) return;

  new Chart(ctx, {
    type: 'line',
    data: {
      labels: labels || ['Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug'],
      datasets: [{
        label: 'Total Registered Users',
        data: growthData || [120, 180, 240, 310, 420, 580, 750],
        borderColor: '#7C3AED',
        backgroundColor: 'rgba(124, 58, 237, 0.1)',
        fill: true,
        tension: 0.35,
        borderWidth: 3
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: { legend: { display: false } },
      scales: {
        y: { beginAtZero: true },
        x: { grid: { display: false } }
      }
    }
  });
}

function initAdminCategoryChart() {
  const ctx = document.getElementById('adminCategoryChart');
  if (!ctx) return;

  new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: ['Programming', 'Full Stack', 'Cloud & DevOps', 'Cybersecurity', 'Automation', 'Data & AI'],
      datasets: [{
        data: [25, 20, 18, 15, 12, 10],
        backgroundColor: ['#7C3AED', '#38BDF8', '#F59E0B', '#10B981', '#EC4899', '#C7F36B'],
        borderWidth: 2
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { position: 'right' }
      }
    }
  });
}
