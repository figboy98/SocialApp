package pt.up.fc.progmovel.socialapp.database;

import android.bluetooth.BluetoothAdapter;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String userId;
    private String name;

    public User(String name, String userId) {
        this.userId = userId;
        this.name = name;
    }

    public String getUserId(){
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setUserID(String userID) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
