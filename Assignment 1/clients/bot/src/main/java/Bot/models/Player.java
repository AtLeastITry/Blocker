package Bot.models;

public class Player {
    public int id;
    public InfluenceCard selectedCard;
    public InfluenceCard[] powerUps;
    
    public Boolean isHost() {
        return id == 1;
    }

    public String username() {
        return "player " + this.id;
    }

    public Player() {}

    public Player(int id) {
        this.id = id;
    }
}