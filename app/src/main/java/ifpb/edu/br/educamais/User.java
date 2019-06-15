package ifpb.edu.br.educamais;

public class User {

    private  String uuid;
    private  String username;
    private  String profileURL;

    public User(){

    }


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
