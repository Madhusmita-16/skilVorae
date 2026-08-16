/**
 * Assessment Quiz Engine (Timer, Navigator & Submit)
 */
let userAnswers = {};
let timerInterval = null;

function initQuiz(assessmentId, courseId, timeLimitMinutes) {
  let secondsRemaining = timeLimitMinutes * 60;
  const timerDisplay = document.getElementById('timer-display');

  timerInterval = setInterval(() => {
    secondsRemaining--;
    const mins = Math.floor(secondsRemaining / 60);
    const secs = secondsRemaining % 60;
    
    if (timerDisplay) {
      timerDisplay.innerText = `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
    }

    if (secondsRemaining <= 0) {
      clearInterval(timerInterval);
      showToast('Time expired! Submitting quiz automatically...', 'error');
      submitQuiz(assessmentId, courseId);
    }
  }, 1000);
}

function selectOption(questionId, optionId, element) {
  userAnswers[questionId] = optionId;

  // Update option styles in same question container
  const parent = element.closest('.option-list');
  parent.querySelectorAll('.option-item').forEach(el => el.classList.remove('selected'));
  element.classList.add('selected');

  // Update question navigator button
  const navBtn = document.getElementById(`nav-btn-${questionId}`);
  if (navBtn) {
    navBtn.classList.add('answered');
  }
function scrollToQuestion(questionId) {
  const card = document.getElementById(`q-card-${questionId}`);
  if (card) {
    card.scrollIntoView({ behavior: 'smooth', block: 'center' });
  }
}

async function submitQuiz(assessmentId, courseId) {
  if (timerInterval) clearInterval(timerInterval);

  const submitBtn = document.getElementById('submit-quiz-btn');
  if (submitBtn) submitBtn.disabled = true;

  try {
    const response = await fetch('/api/assessments/submit', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        assessmentId: assessmentId,
        answers: userAnswers
      })
    });

    const data = await response.json();
    if (data.success && data.data) {
      window.location.href = `/assessments/${courseId}/results/${data.data.attemptId}`;
    } else {
      showToast(data.message || 'Error submitting assessment', 'error');
      if (submitBtn) submitBtn.disabled = false;
    }
  } catch (err) {
    console.error(err);
    showToast('Failed to connect to assessment server', 'error');
    if (submitBtn) submitBtn.disabled = false;
  }
}
