// 재예약 동시성 측정
// 사용법: node reschedule.cjs <id들(쉼표구분)> <newTime> [반복]
// 같은 환자의 서로 다른 예약 id들을 '같은 새 시간'으로 동시에 재예약 → 중복이 생기는지 확인
const B = 'http://localhost:8080/api/v1';
const ids = process.argv[2].split(',');
const NEW = process.argv[3];
const REPEAT = +(process.argv[4] || 10); // id마다 몇 번씩 쏠지
(async () => {
  const r = await fetch(B + '/auth/login', { method: 'POST', headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email: 'guardian@docto.local', password: 'Docto1234!' }) });
  const cookie = r.headers.getSetCookie().map(c => c.split(';')[0]).join('; ');
  const send = (id) => fetch(`${B}/appointments/${id}/reschedule`, { method: 'POST',
    headers: { 'Content-Type': 'application/json', Cookie: cookie },
    body: JSON.stringify({ newTime: NEW }) }).then(async x => [x.status, (await x.text()).slice(0, 90)]);
  const jobs = [];
  for (let i = 0; i < REPEAT; i++) for (const id of ids) jobs.push(send(id));
  const res = await Promise.all(jobs);
  const tally = {}; for (const [s, t] of res) { const k = s + ' ' + (s === 200 ? '' : t); tally[k] = (tally[k] || 0) + 1; }
  console.log(JSON.stringify(tally, null, 1));
})();
