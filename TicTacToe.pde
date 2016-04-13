import java.util.List;
import java.util.Map;
import java.util.Optional;

Board<Boolean> active = Board.create(true);
MegaBoard megaboard = MegaBoard.create();
boolean oturn = true; // oturn is true if it is O's turn
int side = 30;

void setup() {
  size(270, 270);
  textFont(loadFont("TimesNewRomanPSMT-48.vlw"), 24);
  textAlign(CENTER, CENTER);
  Tests.basicBoard();
  Tests.megaBoard();
}

void draw() {
  for (Space board : Space.values()) {
    Value boardValue = megaboard.getMeta(board);
    boolean boardActive = active.get(board);

    fill(255);
    stroke(0);
    strokeWeight(4);
    rect(board.x() * 3 * side, board.y() * 3 * side, (board.x() + 1) * 3 * side, (board.y() + 1) * 3 * side);
    strokeWeight(1);

    for (Space space : Space.values()) {
      Value spaceValue = megaboard.get(board, space);

      // draw background
      stroke(0, 0, 0);
      switch (boardValue) {
        case O:
          fill(0, 0, 128 + (boardActive ? 64 : 0)); // blue
          break;
        case X:
          fill(128 + (boardActive ? 64 : 0), 0, 0); // red
          break;
        case EMPTY:
          fill(128 + (boardActive ? 127 : 0)); // white
          break;
      }
      rect(board.x() * 3 * side + space.x() * side, board.y() * 3 * side + space.y() * side,
          board.x() * 3 * side + (space.x() + 1) * 30, board.y() * 3 * side + (space.y() + 1) * side);

      // draw X or O
      switch (spaceValue) {
        case O:
          noStroke();
          fill(0, 0, 255);
          text("O",
              board.x() * 3 * side + space.x() * side + side / 2,
              board.y() * 3 * side + space.y() * side + side / 2);
          break;
        case X:
          noStroke();
          fill(255, 0, 0);
          text("X",
              board.x() * 3 * side + space.x() * side + side / 2,
              board.y() * 3 * side + space.y() * side + side / 2);
          break;
      }
    }
  }

  Optional<Value> winner = megaboard.getWinner();
  if (winner.isPresent()) {
    // Overlay
    fill(0, 0, 0, 128);
    rect(0, 0, 270, 270);
    // Banner
    switch(winner.get()) {
      case O:
        fill(0, 0, 255);
        text("O's win!", 4.5 * side, 4.5 * side);
        break;
      case X:
        fill(255, 0, 0);
        text("X's win!", 4.5 * side, 4.5 * side);
    }
  }
}

boolean getbit(int bits,int n) {
  // return true if nth bit of bits is set
  return (bits & (1 << n)) > 0;
}

void mousePressed() {
  Space board = new Space(mouseX / (3 * side), mouseY / (3 * side));
  Space space = new Space(mouseX % (3 * side) / side, mouseY % (3 * side) / side);

  if (active.get(board) && megaboard.get(board, space) == Value.EMPTY) { // move is valid
    megaboard = megaboard.move(board, space, oturn ? Value.O : Value.X);
    oturn = !oturn;
  }

  boolean noMoves = true;
  for (Space s : Space.values()) {
    if (megaboard.get(space, s) == Value.EMPTY) {
      noMoves = false;
    }
  }
  if (noMoves) {
    active = Board.create(true);
  } else {
    active = Board.create(false).move(space, true);
  }
}