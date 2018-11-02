package Bot.deserializers;

import Bot.models.Game;
import Bot.models.InfluenceCard;
import Bot.models.UserPlayer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

public class GameDeserializer implements JsonDeserializer<Game> {
    private static final Gson GSON = new GsonBuilder().create();

    @Override
    public Game deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Game game = new Game();
        
        game.board = GSON.fromJson(jsonObject.get("_board").getAsString(), new TypeToken<int[][]>(){}.getType());
        game.influenceCards = GSON.fromJson(jsonObject.get("_influenceCards").getAsString(), new TypeToken<Map<Integer, Set<InfluenceCard>>>(){}.getType());
        game.players = GSON.fromJson(jsonObject.get("_userPlayers").getAsString(), new TypeToken<Map<Integer, Set<UserPlayer>>>(){}.getType());
        game.inProgress = jsonObject.get("_inProgress").getAsBoolean();
        game.finished = jsonObject.get("_gameFinished").getAsBoolean();
        game.name = jsonObject.get("name").getAsString();
        game.playerTurn = jsonObject.get("_playerTurn").getAsInt();

        return game;
    }
}