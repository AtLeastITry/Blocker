package Bot.models;

public class Coordinates {
    public int x;
    public int y;

    public Coordinates() {}

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (!(o instanceof Coordinates)) {
          return false;
        }

        Coordinates coordinates = (Coordinates)o;
        
        if (this.x != coordinates.x) {
            return false;
        }

        if (this.y != coordinates.y) {
            return false;
        }

        return true;
      }
}