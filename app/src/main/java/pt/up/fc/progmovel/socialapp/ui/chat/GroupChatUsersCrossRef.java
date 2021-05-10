package pt.up.fc.progmovel.socialapp.ui.chat;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(primaryKeys = {"userId", "groupID"})
public class GroupChatUsersCrossRef {
    private UUID userID;
    private UUID groupID;
}
