package Bot.models;

public class Player {
    public String username;
    public int id;
    public InfluenceCard selectedCard;
    public InfluenceCard[] powerUps;

    public Player() {}

    public Player(String username, int id) {
        this.username = username;
        this.id = id;
    }
}