package pt.up.fc.progmovel.socialapp.shared;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;
@Entity
public class User {
    @PrimaryKey(autoGenerate = false)
    private UUID userID;
    private final String hwdId;
    private final String name;

    public User(String hwdId, String name) {
        userID = UUID.randomUUID();
        this.hwdId = hwdId;
        this.name = name;
    }

    public String getHwdId() {
        return hwdId;
    }

    public String getName() {
        return name;
    }

}
