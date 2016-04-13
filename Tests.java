import java.util.Optional;

class Tests {
  public static void basicBoard() {
    Board<Value> board = Board.create(Value.EMPTY);
    for (Space space : Space.values()) {
      assert (board.get(space) == Value.EMPTY)
          : String.format("Space %s is %s, expected empty", space, board.get(space));
    }
    for(Space space : Space.values()) {
      board = board.move(space, Value.O);
      assert(board.get(space) == Value.O)
          : String.format("Space %s is %s, expected O", space, board.get(space));
    }
    assert(board.getWinner(Value.EMPTY).equals(Optional.of(Value.O)));
  }
  
  public static void megaBoard() {
    MegaBoard board = MegaBoard.create();
    for (Space a : Space.values()) {
      for (Space b : Space.values()) {
        assert (board.get(a, b) == Value.EMPTY)
            : String.format("Space %s is %s, expected empty", a, b, board.get(a, b));
      }
    }
    for(Space a : Space.values()) {
      for(Space b : Space.values()) {
        board = board.move(a, b, Value.O);
        assert(board.get(a, b) == Value.O)
            : String.format("Space %s/%s in %s, expected O", a, b, board.get(a, b));
      }
    }
    assert(board.getWinner().equals(Optional.of(Value.O)));
  }
}