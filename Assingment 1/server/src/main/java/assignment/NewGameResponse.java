package assignment;

public class NewGameResponse {
    public boolean success;
    public String gameName;

    public NewGameResponse(boolean success, String gameName) {
        this.success = success;
        this.gameName = gameName;
    }
}
