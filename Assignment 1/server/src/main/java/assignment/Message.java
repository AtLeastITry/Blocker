package assignment;

import java.util.Date;

public class Message {
    public Message() {
        sent = new Date();
    }
    public Date sent;
    public int type;
    public String sender;
    public String data;
}
