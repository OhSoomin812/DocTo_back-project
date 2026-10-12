// 동시 예약 경쟁 조건 측정: 같은 환자·같은 시간 예약을 N건 동시에 요청
const N = +(process.argv[2] || 20), TIME = process.argv[3] || '2026-10-08T10:00:00';
(async () => {
  const r = await fetch('http://localhost:8080/api/v1/auth/login', { method: 'POST', headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email: 'guardian@docto.local', password: 'Docto1234!' }) });
  const cookie = r.headers.getSetCookie().map(c => c.split(';')[0]).join('; ');
  const body = JSON.stringify({ hospitalId: 1, doctorId: 1, userId: 2, patientId: 1, symptom: '동시성 테스트',
    question: '', appointmentTime: TIME, appointmentType: 'SCHEDULED', paymentType: 'ONSITE' });
  const send = () => fetch('http://localhost:8080/api/v1/appointments', { method: 'POST',
    headers: { 'Content-Type': 'application/json', Cookie: cookie }, body }).then(async x => [x.status, (await x.text()).slice(0, 160)]);
  if (process.argv[4] === 'seq') { for (let i = 0; i < 2; i++) console.log(await send()); return; }
  const res = await Promise.all(Array.from({ length: N }, send));
  const tally = {}; for (const [s, t] of res) { const k = s + ' ' + (s === 200 ? '' : t); tally[k] = (tally[k] || 0) + 1; }
  console.log(JSON.stringify(tally, null, 1));
})();
