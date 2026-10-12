// 정원 초과 확정 / 타 병원 확정 가능 여부 확인
const B = 'http://localhost:8080/api/v1';
const login = async (email) => { const r = await fetch(B + '/auth/login', { method: 'POST', headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ email, password: 'Docto1234!' }) }); return r.headers.getSetCookie().map(c => c.split(';')[0]).join('; '); };
const call = async (cookie, method, path, body) => { const r = await fetch(B + path, { method, headers: { 'Content-Type': 'application/json', Cookie: cookie },
  body: body && JSON.stringify(body) }); return [r.status, (await r.text()).slice(0, 120)]; };
(async () => {
  const TIME = process.argv[2];
  const g = await login('guardian@docto.local'), h = await login('hospital@docto.local'), h2 = await login('hospital2@docto.local');
  for (const pid of [1, 2]) console.log('request patient', pid, await call(g, 'POST', '/appointments', { hospitalId: 1, doctorId: 2, userId: 2, patientId: pid,
    symptom: '정원 테스트', question: '', appointmentTime: TIME, appointmentType: 'SCHEDULED', paymentType: 'ONSITE' }));
  const ids = process.argv.slice(3);
  console.log('hospital1 confirm', ids[0], (await call(h, 'PATCH', `/appointments/${ids[0]}/status`, { status: 'CONFIRMED' }))[0]);
  console.log('hospital1 confirm', ids[1], await call(h, 'PATCH', `/appointments/${ids[1]}/status`, { status: 'CONFIRMED' }));
  console.log('hospital2(other) set status of', ids[2], await call(h2, 'PATCH', `/appointments/${ids[2]}/status`, { status: 'CANCELED' }));
})();
