package Bot.ai;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Set;

import Bot.util.Client;
import Bot.util.Config;
import Bot.models.Coordinates;
import Bot.models.Game;
import Bot.models.InfluenceCard;
import Bot.models.Player;

public class Agent {
    private final Client _client = new Client(Config.SERVER);

    public Agent() throws URISyntaxException {}

    private Player _player;
    private Game _game;
    private ArrayList<String> _avaliableGameNames;
    private ArrayList<Coordinates> _coordinates;

    private Set<InfluenceCard> getPlayerCards() {
        return this._game.getPlayerCards(this._player.id);
    }

    public void run() {

    }
}