/**
 * Course Player & Real-time Progress Sync
 */
async function markLessonComplete(courseId, lessonId) {
  const btn = document.getElementById('mark-complete-btn');
  if (btn) btn.disabled = true;

  try {
    const response = await fetch(`/api/progress/${courseId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        lessonId: lessonId,
        completed: true
      })
    });

    const data = await response.json();
    if (data.success) {
      showToast('Progress saved!', 'success');

      // Update sidebar lesson item UI
      const lessonItem = document.getElementById(`lesson-item-${lessonId}`);
      if (lessonItem) {
        lessonItem.classList.add('completed');
        const icon = lessonItem.querySelector('.lesson-status-icon');
        if (icon) icon.innerHTML = '[Done]';
      }

      // Update progress bar
      const fill = document.getElementById('course-progress-fill');
      const text = document.getElementById('course-progress-text');
      if (fill) fill.style.width = `${data.data}%`;
      if (text) text.innerText = `${data.data}% Completed`;

      if (btn) {
        btn.innerText = 'Completed';
        btn.classList.remove('btn-accent');
        btn.classList.add('btn-outline');
      }
    } else {
      showToast(data.message || 'Failed to update progress', 'error');
      if (btn) btn.disabled = false;
    }
  } catch (err) {
    console.error(err);
    showToast('Error syncing progress with server', 'error');
    if (btn) btn.disabled = false;
  }
}
