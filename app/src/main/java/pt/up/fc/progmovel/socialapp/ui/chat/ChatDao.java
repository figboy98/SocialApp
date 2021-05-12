package pt.up.fc.progmovel.socialapp.ui.chat;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface ChatDao {

    @Insert
    void insertChatMessage(ChatMessage message);

    @Insert
    void insertGroupChat(GroupChat groupChat);

    @Insert
    void insertGroupChatMessagesCrossRef(GroupChatMessagesCrossRef groupChatMessagesCrossRef);

    @Insert
    void insertGroupChatUsersCrossRef(GroupChatUsersCrossRef groupChatUsersCrossRef);

    @Transaction
    @Query("SELECT * FROM GroupChat")
    LiveData<GroupChatWithMessages> getGroupChatWithMessages();

    @Query("SELECT * FROM CHATMESSAGE")
    LiveData<List<ChatMessage>> getChatMessages();







}
