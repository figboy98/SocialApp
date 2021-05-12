package pt.up.fc.progmovel.socialapp.ui.chat;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.UUID;

@Entity(primaryKeys = {"groupChatID","chatMessageID"})
public class GroupChatMessagesCrossRef {
    @NonNull
    private String groupChatID;
    @NonNull
    private String chatMessageID;

    public GroupChatMessagesCrossRef(){}

    public GroupChatMessagesCrossRef(String chatID, String messageID){
        groupChatID = chatID;
        chatMessageID = messageID;
    }

    public String getGroupChatID() {
        return groupChatID;
    }

    public String getChatMessageID() {
        return chatMessageID;
    }

    public void setGroupChatID(String groupChatID) {
        this.groupChatID = groupChatID;
    }

    public void setChatMessageID(String chatMessageID) {
        this.chatMessageID = chatMessageID;
    }
}
