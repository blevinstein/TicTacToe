import java.util.List;
import java.util.ArrayList;

class Space {
  private int x;
  private int y;
  public Space(int x, int y) {
    assert(0 <= x && x <= 2
        && 0 <= y && y <= 2);
    this.x = x;
    this.y = y;
  }
  public int x() { return x; }
  public int y() { return y; }
  
  public static List<Space> values() {
    ArrayList<Space> result = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        result.add(new Space(i, j));
      }
    }
    return result;
  }
  
  @Override public int hashCode() {
    return 5 * x + 37 * y;
  }
  
  @Override public boolean equals(Object o) {
    if (o instanceof Space) {
      Space other = (Space) o;
      return x == other.x && y == other.y;
    }
    return false;
  }
  
  @Override public String toString() {
    return String.format("(%d,%d)", x, y);
  }
}