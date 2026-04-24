// ===== ROCK PAPER SCISSORS =====
(function () {
  const choices = ["rock", "paper", "scissors"];
  const emojis = { rock: "✊", paper: "🖐", scissors: "✌️" };
  const beats = { rock: "scissors", paper: "rock", scissors: "paper" };

  const playerChoiceEl = document.getElementById("playerChoice");
  const aiChoiceEl = document.getElementById("aiChoice");
  const resultTextEl = document.getElementById("rpsResultText");
  const winsEl = document.getElementById("rpsWins");
  const drawsEl = document.getElementById("rpsDraws");
  const lossesEl = document.getElementById("rpsLosses");
  const btns = document.querySelectorAll(".rps-btn");

  let stats = { wins: 0, draws: 0, losses: 0 };
  let busy = false;

  function postResult(result) {
    const token = document
      .querySelector('meta[name="_csrf"]')
      .getAttribute("content");
    const header = document
      .querySelector('meta[name="_csrf_header"]')
      .getAttribute("content");

    fetch("/games/rps/result", {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
        [header]: token,
      },
      body: "result=" + result,
    }).catch(() => {});
  }

  function play(playerChoice) {
    if (busy) return;
    busy = true;

    const aiChoice = choices[Math.floor(Math.random() * 3)];

    // Reset display
    playerChoiceEl.textContent = "⏳";
    aiChoiceEl.textContent = "⏳";
    resultTextEl.textContent = "";
    resultTextEl.className = "rps-result-text";

    setTimeout(() => {
      playerChoiceEl.textContent = emojis[playerChoice];
      playerChoiceEl.classList.add("revealed");

      setTimeout(() => {
        aiChoiceEl.textContent = emojis[aiChoice];
        aiChoiceEl.classList.add("revealed");

        let result;
        if (playerChoice === aiChoice) {
          result = "DRAW";
          resultTextEl.textContent = "Draw!";
          resultTextEl.classList.add("draw");
          stats.draws++;
        } else if (beats[playerChoice] === aiChoice) {
          result = "WIN";
          resultTextEl.textContent = "You Win!";
          resultTextEl.classList.add("win");
          stats.wins++;
        } else {
          result = "LOSS";
          resultTextEl.textContent = "AI Wins!";
          resultTextEl.classList.add("loss");
          stats.losses++;
        }

        winsEl.textContent = stats.wins;
        drawsEl.textContent = stats.draws;
        lossesEl.textContent = stats.losses;

        // Persist to server
        fetch("/games/rps/result", {
          method: "POST",
          headers: { "Content-Type": "application/x-www-form-urlencoded" },
          body: "result=" + result,
        }).catch(() => {});

        busy = false;
      }, 400);
    }, 300);
  }

  btns.forEach((btn) => {
    btn.addEventListener("click", () => {
      playerChoiceEl.classList.remove("revealed");
      aiChoiceEl.classList.remove("revealed");
      play(btn.dataset.choice);
    });
  });
})();
