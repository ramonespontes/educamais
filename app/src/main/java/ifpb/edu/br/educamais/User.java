package ifpb.edu.br.educamais;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{

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

    protected User(Parcel in) {
        uuid = in.readString();
        username = in.readString();
        profileURL = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileURL() {
        return profileURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeString(username);
        dest.writeString(profileURL);
    }
}
