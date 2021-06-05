package pt.up.fc.progmovel.socialapp.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(primaryKeys = {"userId", "groupChatID"})
public class GroupChatUsersCrossRef {
    @NonNull
    private String userId;
    @NonNull
    private String groupChatID;

    public GroupChatUsersCrossRef(){}

    public GroupChatUsersCrossRef(String user, String group){
        userId = user;
        groupChatID = group;
    }

    public String getGroupChatID() {
        return groupChatID;
    }

    public String getUserId() {
        return userId;
    }

    public void setGroupChatID(String groupID) {
        this.groupChatID = groupID;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
