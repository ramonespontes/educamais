package ifpb.edu.br.educamais;

public class User {

    private final String uuid;
    private final String username;
    private final String profileURL;


    public User(String uuid, String username, String profileURL) {
        this.uuid = uuid;
        this.username = username;
        this.profileURL = profileURL;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileURL() {
        return profileURL;
    }
}
