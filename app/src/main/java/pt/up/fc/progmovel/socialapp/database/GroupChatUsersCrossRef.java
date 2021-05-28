package pt.up.fc.progmovel.socialapp.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(primaryKeys = {"userID", "groupChatID"})
public class GroupChatUsersCrossRef {
    @NonNull
    private String userID;
    @NonNull
    private String groupChatID;

    public GroupChatUsersCrossRef(){}

    public GroupChatUsersCrossRef(String user, String group){
        userID = user;
        groupChatID = group;
    }

    public String getGroupChatID() {
        return groupChatID;
    }

    public String getUserID() {
        return userID;
    }

    public void setGroupChatID(String groupID) {
        this.groupChatID = groupID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
