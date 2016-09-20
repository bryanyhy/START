package ambiesoft.start.model.dataclass;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bryanyhy on 4/9/2016.
 */
public class User implements Parcelable {

    private String key;
    private String email;
    private String username;
    private String category;
    private String desc;

    public User() {}

    public User(String key, String email, String username, String category, String desc) {
        this.key = key;
        this.email = email;
        this.username = username;
        this.category = category;
        this.desc = desc;
    }

    public User(String email, String username, String category, String desc) {
        this.email = email;
        this.username = username;
        this.category = category;
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    protected User(Parcel in) {
        key = in.readString();
        email = in.readString();
        username = in.readString();
        category = in.readString();
        desc = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(email);
        dest.writeString(username);
        dest.writeString(category);
        dest.writeString(desc);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}