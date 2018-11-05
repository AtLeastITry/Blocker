package Bot.models;

import java.util.Date;

public class Message<T> {
    public Date sent;
    public int type;
    public String sender;
    public T data;

    public Message(int type, String sender, T data) {
        sent = new Date();
        this.data = data;
    }
}