package pt.up.fc.progmovel.socialapp.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(primaryKeys = {"userID", "groupID"})
public class GroupChatUsersCrossRef {
    @NonNull
    private String userID;
    @NonNull
    private String groupID;

    public GroupChatUsersCrossRef(){}

    public String getGroupID() {
        return groupID;
    }

    public String getUserID() {
        return userID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
