import java.util.Optional;

class MegaBoard {
  private Board<Board<Value>> contents;
  private Board<Value> metaContents;
  
  private MegaBoard(Board<Board<Value>> contents, Board<Value> metaContents) {
    this.contents = contents;
    this.metaContents = metaContents;
  }
  
  public static MegaBoard create() {
    return new MegaBoard(Board.create(Board.create(Value.EMPTY)), Board.create(Value.EMPTY));
  }
  
  public MegaBoard move(Space board, Space space, Value value) {
    Board<Board<Value>> newContents = contents.move(board, contents.get(board).move(space, value));
    Board<Value> newMetaContents = metaContents;
    if (newMetaContents.get(board) == Value.EMPTY) {
      // Check if this board has been won
      Optional<Value> winner = newContents.get(board).getWinner(Value.EMPTY);
      if (winner.isPresent()) {
        newMetaContents = newMetaContents.move(board, winner.get());
      }
    }
    return new MegaBoard(newContents, newMetaContents);
  }
  
  public Value getMeta(Space board) {
    return metaContents.get(board);
  }
  
  public Value get(Space board, Space space) {
    return contents.get(board).get(space);
  }
  
  public Optional<Value> getWinner() {
    return metaContents.getWinner(Value.EMPTY);
  }
}