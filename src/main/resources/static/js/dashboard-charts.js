/**
 * SkilVorae Chart.js Initialization
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
        borderColor: '#3B82F6',
        backgroundColor: 'rgba(59, 130, 246, 0.08)',
        borderWidth: 3,
        fill: true,
        tension: 0.35,
        pointBackgroundColor: '#0F172A',
        pointRadius: 4
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
          grid: { color: '#E2E8F0' },
          ticks: { stepSize: 1 }
        },
        x: {
          grid: { display: false }
        }
      }
    }
  });
}
