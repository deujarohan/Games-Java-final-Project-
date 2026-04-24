// ===== TIC-TAC-TOE with Minimax AI =====
(function () {
  const cells = Array.from(document.querySelectorAll(".ttt-cell"));
  const statusEl = document.getElementById("tttStatus");
  const resetBtn = document.getElementById("tttReset");
  const resultBanner = document.getElementById("tttResult");
  const resultIcon = document.getElementById("resultIcon");
  const resultText = document.getElementById("resultText");
  const winsEl = document.getElementById("tttWins");
  const drawsEl = document.getElementById("tttDraws");
  const lossesEl = document.getElementById("tttLosses");

  const WINS = [
    [0, 1, 2],
    [3, 4, 5],
    [6, 7, 8],
    [0, 3, 6],
    [1, 4, 7],
    [2, 5, 8],
    [0, 4, 8],
    [2, 4, 6],
  ];
  let board = Array(9).fill(null);
  let gameOver = false;
  let stats = { wins: 0, draws: 0, losses: 0 };

  function postResult(result) {
    const token = document
      .querySelector('meta[name="_csrf"]')
      .getAttribute("content");
    const header = document
      .querySelector('meta[name="_csrf_header"]')
      .getAttribute("content");

    fetch("/games/ttt/result", {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
        [header]: token,
      },
      body: "result=" + result,
    }).catch(() => {});
  }

  function checkWinner(b) {
    for (const [a, c, d] of WINS) {
      if (b[a] && b[a] === b[c] && b[a] === b[d]) return b[a];
    }
    return b.every(Boolean) ? "DRAW" : null;
  }

  function getWinLine(b) {
    for (const line of WINS) {
      const [a, c, d] = line;
      if (b[a] && b[a] === b[c] && b[a] === b[d]) return line;
    }
    return null;
  }

  // Minimax
  function minimax(b, isMax) {
    const w = checkWinner(b);
    if (w === "X") return -10;
    if (w === "O") return 10;
    if (w === "DRAW") return 0;
    if (isMax) {
      let best = -Infinity;
      b.forEach((v, i) => {
        if (!v) {
          b[i] = "O";
          best = Math.max(best, minimax(b, false));
          b[i] = null;
        }
      });
      return best;
    } else {
      let best = Infinity;
      b.forEach((v, i) => {
        if (!v) {
          b[i] = "X";
          best = Math.min(best, minimax(b, true));
          b[i] = null;
        }
      });
      return best;
    }
  }

  function aiMove() {
    let best = -Infinity,
      move = -1;
    board.forEach((v, i) => {
      if (!v) {
        board[i] = "O";
        const score = minimax(board, false);
        board[i] = null;
        if (score > best) {
          best = score;
          move = i;
        }
      }
    });
    return move;
  }

  function renderBoard() {
    cells.forEach((cell, i) => {
      cell.textContent = board[i] || "";
      cell.className = "ttt-cell" + (board[i] ? " " + board[i] : "");
      cell.disabled = !!board[i] || gameOver;
    });
  }

  function showResult(result) {
    resultBanner.classList.remove("hidden");
    if (result === "WIN") {
      resultIcon.textContent = "🏆";
      resultText.textContent = "You Win!";
      resultText.style.color = "var(--green)";
    } else if (result === "LOSS") {
      resultIcon.textContent = "💀";
      resultText.textContent = "AI Wins";
      resultText.style.color = "var(--red)";
    } else {
      resultIcon.textContent = "🤝";
      resultText.textContent = "Draw!";
      resultText.style.color = "var(--yellow)";
    }
  }

  function updateStats(result) {
    if (result === "WIN") stats.wins++;
    if (result === "DRAW") stats.draws++;
    if (result === "LOSS") stats.losses++;
    winsEl.textContent = stats.wins;
    drawsEl.textContent = stats.draws;
    lossesEl.textContent = stats.losses;

    // Persist to server
    fetch("/games/ttt/result", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: "result=" + result,
    }).catch(() => {});
  }

  function handleEnd(winner) {
    gameOver = true;
    const line = getWinLine(board);
    if (line) {
      line.forEach((i) => cells[i].classList.add("winning"));
    }

    let result;
    if (winner === "X") {
      result = "WIN";
      statusEl.textContent = "You win! 🎉";
    } else if (winner === "O") {
      result = "LOSS";
      statusEl.textContent = "AI wins!";
    } else {
      result = "DRAW";
      statusEl.textContent = "It's a draw!";
    }
    showResult(result);
    updateStats(result);
    cells.forEach((c) => (c.disabled = true));
  }

  function playerMove(i) {
    if (gameOver || board[i]) return;
    board[i] = "X";
    renderBoard();
    const w = checkWinner(board);
    if (w) {
      handleEnd(w);
      return;
    }

    statusEl.textContent = "AI is thinking...";
    setTimeout(() => {
      const move = aiMove();
      if (move >= 0) {
        board[move] = "O";
        renderBoard();
        const w2 = checkWinner(board);
        if (w2) {
          handleEnd(w2);
          return;
        }
        statusEl.textContent = "Your turn";
      }
    }, 250);
  }

  cells.forEach((cell, i) => {
    cell.addEventListener("click", () => playerMove(i));
  });

  resetBtn.addEventListener("click", () => {
    board = Array(9).fill(null);
    gameOver = false;
    statusEl.textContent = "Your turn";
    resultBanner.classList.add("hidden");
    renderBoard();
  });

  renderBoard();
})();
