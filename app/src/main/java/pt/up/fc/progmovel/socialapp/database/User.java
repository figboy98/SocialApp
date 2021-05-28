package pt.up.fc.progmovel.socialapp.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.UUID;
@Entity
public class User {
    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String userID;
    private final String hwdId;
    private final String name;

    public User(String hwdId, String name) {
        userID = UUID.randomUUID().toString();
        this.hwdId = hwdId;
        this.name = name;
    }

    public String getUserID(){
        return  userID;
    }

    public String getHwdId() {
        return hwdId;
    }

    public String getName() {
        return name;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}
