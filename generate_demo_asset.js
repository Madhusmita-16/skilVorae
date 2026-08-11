const fs = require('fs');
const PImage = require('pureimage');
const GIFEncoder = require('gif-encoder-2');

const width = 960;
const height = 540;
const outPath = 'assets/navigation.gif';

const encoder = new GIFEncoder(width, height);
const out = fs.createWriteStream(outPath);
encoder.createReadStream().pipe(out);
encoder.start();
encoder.setRepeat(0);
encoder.setDelay(300);
encoder.setQuality(10);

const fontPath = 'C:/Windows/Fonts/arial.ttf';
const font = PImage.registerFont(fontPath, 'Arial');
font.loadSync();

const steps = ['Dashboard', 'Courses', 'Player', 'Certificate'];

for (let frame = 0; frame < 16; frame += 1) {
  const img = PImage.make(width, height);
  const ctx = img.getContext('2d');

  ctx.fillStyle = '#081b2e';
  ctx.fillRect(0, 0, width, height);

  ctx.fillStyle = '#152d4a';
  ctx.fillRect(40, 40, width - 80, 120);
  ctx.fillStyle = '#ffffff';
  ctx.font = '40pt Arial';
  ctx.fillText('SkilVorae Navigation Demo', 60, 100);

  ctx.fillStyle = '#ffffff';
  ctx.font = '24pt Arial';
  ctx.fillText('A quick animation of core app navigation flow', 60, 150);

  const boxWidth = 200;
  const boxHeight = 100;
  const gap = 30;
  const startX = 80;
  const y = 240;

  for (let i = 0; i < steps.length; i += 1) {
    const x = startX + i * (boxWidth + gap);
    const active = Math.floor(frame / 4) % steps.length === i;
    ctx.fillStyle = active ? '#5e9cff' : '#22456e';
    ctx.fillRect(x, y, boxWidth, boxHeight);
    ctx.fillStyle = active ? '#041c39' : '#c8dffb';
    ctx.font = '28pt Arial';
    const text = steps[i];
    const textWidth = ctx.measureText(text).width;
    ctx.fillText(text, x + (boxWidth - textWidth) / 2, y + 60);
  }

  const arrowX = startX + ((frame / 4) % steps.length) * (boxWidth + gap) + boxWidth / 2;
  ctx.strokeStyle = '#75b9ff';
  ctx.lineWidth = 8;
  ctx.beginPath();
  ctx.moveTo(arrowX, y + boxHeight + 40);
  ctx.lineTo(arrowX, y + boxHeight + 120);
  ctx.stroke();

  ctx.fillStyle = '#75b9ff';
  ctx.beginPath();
  ctx.moveTo(arrowX - 14, y + boxHeight + 120);
  ctx.lineTo(arrowX + 14, y + boxHeight + 120);
  ctx.lineTo(arrowX, y + boxHeight + 140);
  ctx.closePath();
  ctx.fill();

  encoder.addFrame(ctx);
}

encoder.finish();
out.on('close', () => {
  console.log('Generated', outPath);
});
