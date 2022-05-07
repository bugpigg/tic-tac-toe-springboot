let turns = [["#", "#", "#"], ["#", "#", "#"], ["#", "#", "#"]];
let turn = 'X';
let gameOn = false;

function playerTurn(turn, id) {
  if (gameOn && playerType === turn) {
    let spotTaken = $("#" + id).text();
    if (spotTaken === "#") {
      makeAMove(playerType, id.split("_")[0], id.split("_")[1]);
    }
  }
}

function makeAMove(type, xCoordinate, yCoordinate) {
  let nextTurn = ''
  if (playerType === 'X') {
    nextTurn = 'O'
  } else {
    nextTurn = 'X'
  }
  $.ajax({
    url: url + "/game/gameplay",
    type: 'POST',
    dataType: "json",
    contentType: "application/json",
    data: JSON.stringify({
      "type": type,
      "coordinateX": xCoordinate,
      "coordinateY": yCoordinate,
      "gameId": gameId,
      "turn": nextTurn
    }),
    success: function (data) {
      gameOn = false;
      displayResponse(data);
    },
    error: function (error) {
      console.log(error);
    }
  })
}

function displayResponse(data) {
  let board = data.board;
  turn = data.turn;
  for (let i = 0; i < board.length; i++) {
    for (let j = 0; j < board[i].length; j++) {
      if (board[i][j] === 1) {
        turns[i][j] = 'X'
      } else if (board[i][j] === 2) {
        turns[i][j] = 'O'
      }
      let id = i + "_" + j;
      $("#" + id).text(turns[i][j]);
    }
  }
  if (data.winner != null) {
    alert("Winner is " + data.winner);
  }
  gameOn = true;
}

$(".tic").click(function () {
  let slot = $(this).attr('id');
  playerTurn(turn, slot);
});

function reset() {
  turns = [["#", "#", "#"], ["#", "#", "#"], ["#", "#", "#"]];
  $(".tic").text("#");
}

$("#reset").click(function () {
  reset();
});
