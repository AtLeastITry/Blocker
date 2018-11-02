package Bot.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Bot.deserializers.GameDeserializer;
import Bot.models.Game;

public class JsonHelper {
    public static Gson GSON = new GsonBuilder()
                                .registerTypeAdapter(Game.class, new GameDeserializer())
                                .create();
}