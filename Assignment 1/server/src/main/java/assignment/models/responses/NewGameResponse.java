package assignment.models.responses;

public class NewGameResponse extends Response{
    public String gameName;

    public NewGameResponse(boolean success, String gameName) {
        super(success);
        this.gameName = gameName;
    }
}
