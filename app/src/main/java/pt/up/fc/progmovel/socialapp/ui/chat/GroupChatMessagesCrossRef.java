package pt.up.fc.progmovel.socialapp.ui.chat;

import androidx.room.Entity;

import java.util.UUID;

@Entity(primaryKeys = {"groupChatID","messageID"})
public class GroupChatMessagesCrossRef {
    private UUID groupChatID;
    private UUID chatMessageID;

}
