// Polls /api/hits every 3 seconds and updates the counter display on every page
(function () {
  const el = document.getElementById('hitValue');
  if (!el) return;

  function fetchHits() {
    fetch('/api/hits')
      .then(r => r.json())
      .then(data => {
        el.textContent = data.hits.toLocaleString();
      })
      .catch(() => { el.textContent = '—'; });
  }

  fetchHits(); // immediate first call
  setInterval(fetchHits, 3000);
})();
