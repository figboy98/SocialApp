package pt.up.fc.progmovel.socialapp.ui.chat;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity
public class GroupChat {
    @PrimaryKey(autoGenerate = false)
    private UUID groupChatID;
    private String groupName;

}
