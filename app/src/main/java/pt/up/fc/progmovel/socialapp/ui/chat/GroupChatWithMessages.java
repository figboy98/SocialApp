package pt.up.fc.progmovel.socialapp.ui.chat;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class GroupChatWithMessages {
    @Embedded GroupChat groupChat;
    @Relation(
            parentColumn = "groupChatID",
            entityColumn = "chatMessageID",
            associateBy = @Junction(GroupChatMessagesCrossRef.class)
    )
    public List<ChatMessage> chatMessages;
}
