package Bot.models;

import java.util.Date;

import Bot.util.JsonHelper;

public class Message<T> {
    public Date sent;
    public int type;
    public String sender;
    public String data;

    public Message(int type, String sender, T data) {
        sent = new Date();
        this.type = type;
        this.sender = sender;
        this.data = JsonHelper.GSON.toJson(data);
    }
}