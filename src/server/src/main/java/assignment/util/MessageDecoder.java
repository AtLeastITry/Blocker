package assignment.util;

import assignment.models.Message;
import com.google.gson.Gson;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<Message> {
    @Override
    public Message decode(String s) throws DecodeException {
        return new Gson().fromJson(s, Message.class);
    }

    @Override
    public boolean willDecode(String s) {
        try {
            Message test = new Gson().fromJson(s, Message.class);
            return test != null;
        }
        catch (Exception e){
            return false;
        }
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
