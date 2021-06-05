package pt.up.fc.progmovel.socialapp.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class GroupChat {
    @PrimaryKey()
    @NonNull
    private String groupChatID;
    private String groupName;


    public GroupChat(String name, @NotNull String groupChatID){
        this.groupChatID = groupChatID;
        groupName = name;
    }

    @NotNull
    public String getGroupChatID(){
        return groupChatID;
    }

    public String getGroupName(){return groupName;}

    public void setGroupChatID(@NotNull String groupChatID) {
        this.groupChatID = groupChatID;
    }
    public void setGroupName(String name){
        groupName = name;
    }
}
