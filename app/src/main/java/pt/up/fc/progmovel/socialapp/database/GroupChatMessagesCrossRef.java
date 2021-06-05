package pt.up.fc.progmovel.socialapp.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.jetbrains.annotations.NotNull;

@Entity(primaryKeys = {"groupChatID","chatMessageID"})
public class GroupChatMessagesCrossRef {
    @NonNull
    private final String groupChatID;
    @NonNull
    private final String chatMessageID;


    public GroupChatMessagesCrossRef(@NotNull String chatID, @NotNull String messageID) {
        groupChatID = chatID;
        chatMessageID = messageID;
    }

    @NotNull
    public String getGroupChatID() {
        return groupChatID;
    }

    @NotNull
    public String getChatMessageID() {
        return chatMessageID;
    }

    /*public void setGroupChatID(@NotNull String groupChatID) {
        this.groupChatID = groupChatID;
    }

    public void setChatMessageID(@NotNull String chatMessageID) {
        this.chatMessageID = chatMessageID;
    }
}*/
}
