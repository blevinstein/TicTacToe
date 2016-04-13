import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

class Board<T> {
  private HashMap<Space, T> contents;
  
  public static <T> Board<T> create(T value) {
    HashMap<Space, T> contents = new HashMap<Space, T>();
    for (Space space : Space.values()) {
      contents.put(space, value);
    }
    return new Board(contents);
  }
  
  protected Board(HashMap<Space, T> contents) {
    this.contents = contents;
  }
  
  public T get(Space space) {
    return contents.get(space);
  }
  
  public Board move(Space space, T value) {
    HashMap<Space, T> newContents = new HashMap<Space, T>(contents);
    newContents.put(space, value);
    return new Board(newContents);
  }
  
  @Override public String toString() {
    return contents.toString();
  }
  
  @Override public int hashCode() {
    return contents.hashCode();
  }
  
  @Override public boolean equals(Object o) {
    if (o instanceof Board) {
      Board other = (Board) o;
      for (Space space : Space.values()) {
        if (!get(space).equals(other.get(space))) {
          return false;
        }
      }
      return true;
    }
    return false;
  }
  
  public Optional<T> getWinner(T ignoreValue) {
    for (List<Space> winGroup : getWinGroups()) {
      T candidate = get(winGroup.get(0));
      boolean candidateWon = true;
      for (Space space : winGroup) {
        T value = get(space);
        if (!value.equals(candidate) || value.equals(ignoreValue)) {
          candidateWon = false;
        }
      }
      if (candidateWon) {
        return Optional.of(candidate);
      }
    }
    return Optional.empty();
  }
  
  public static List<List<Space>> getWinGroups() {
    List<List<Space>> winGroups = new ArrayList<>();
    // Add rows and columns
    for (int i = 0; i < 3; i++) {
      List<Space> rowGroup = new ArrayList<>();
      List<Space> colGroup = new ArrayList<>();
      for (int j = 0; j < 3; j++) {
        rowGroup.add(new Space(i, j));
        colGroup.add(new Space(j, i));
      }
      winGroups.add(rowGroup);
      winGroups.add(colGroup);
    }
    List<Space> diagOne = new ArrayList<>();
    diagOne.add(new Space(0, 0));
    diagOne.add(new Space(1, 1));
    diagOne.add(new Space(2, 2));
    winGroups.add(diagOne);
    List<Space> diagTwo = new ArrayList<>();
    diagTwo.add(new Space(0, 2));
    diagTwo.add(new Space(1, 1));
    diagTwo.add(new Space(2, 0));
    winGroups.add(diagTwo);
    return winGroups;
  }
}