package Bot.models;

public class Player {
    public String username;
    public int id;
    public InfluenceCard selectedCard;
    public InfluenceCard[] powerUps;
    
    public String username() {
        return "player " + this.id;
    }

    public Player() {}

    public Player(int id) {
        this.id = id;
    }
}