package pt.up.fc.progmovel.socialapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface  ChatDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     void insertChatMessage(ChatMessage message);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     void insertGroupChat(GroupChat groupChat);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     void insertGroupChatMessagesCrossRef(GroupChatMessagesCrossRef groupChatMessagesCrossRef);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     void insertGroupChatUsersCrossRef(GroupChatUsersCrossRef groupChatUsersCrossRef);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUser(User user);

    @Transaction
    @Query("SELECT * FROM GroupChat WHERE groupChatID = :groupChatID")
     LiveData<GroupChatWithMessages> getGroupChatWithMessages(String groupChatID);

    @Transaction
    @Query("SELECT * FROM User WHERE UserId =:userID ")
    LiveData<UsersWithGroupChats> getUsersWithGroupChats (String userID);

    @Query("SELECT * FROM CHATMESSAGE ")
     LiveData<List<ChatMessage>> getChatMessages();







}
