package pt.up.fc.progmovel.socialapp.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity
public class GroupChat {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String groupChatID;
    private String groupName;

    public GroupChat(){}
    public GroupChat(String name){
        groupChatID = UUID.randomUUID().toString();
        groupName = name;
    }

    public String getGroupChatID(){
        return groupChatID;
    }

    public String getGroupName(){return groupName;}

    public void setGroupChatID(String groupChatID) {
        this.groupChatID = groupChatID;
    }
    public void setGroupName(String name){
        groupName = name;
    }
}
