package Bot.models;

public class Move {
    public InfluenceCard card;
    public Coordinates firstMove;
    public Coordinates secondMove;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (!(o instanceof Move)) {
          return false;
        }

        Move move = (Move)o;

        if (this.card == null) {
            if(move.card != null) {
                return false;
            }
        }
        else if(move.card == null) {
            return false;
        }
        else if(this.card != move.card) {
            return false;
        }

        if (this.firstMove == null) {
            if(move.firstMove != null) {
                return false;
            }
        }
        else if(move.firstMove == null) {
            return false;
        }
        else if(!this.firstMove.equals(move.firstMove)) {
            return false;
        }

        if (this.secondMove == null) {
            if(move.secondMove != null) {
                return false;
            }
        }
        else if(move.secondMove == null) {
            return false;
        }
        else if(!this.secondMove.equals(move.secondMove)) {
            return false;
        }

        return true;
      }
}