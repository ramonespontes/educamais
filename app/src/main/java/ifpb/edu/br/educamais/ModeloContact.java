package ifpb.edu.br.educamais;

public class ModeloContact {

    private String uuid;
    private String username;
    private String lastMesage;
    private long timestamp;
    private String photoURL;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastMesage() {
        return lastMesage;
    }

    public void setLastMesage(String lastMesage) {
        this.lastMesage = lastMesage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
