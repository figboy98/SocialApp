package pt.up.fc.progmovel.socialapp.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ChatRepository {
    private ChatDao chatDao;

    public ChatRepository(Application application) {
        ChatDatabase database = ChatDatabase.getDatabase(application);
        chatDao = database.chatDao();
    }
    public User getUser(String name){
        User user = null;
        try {
            user=  new GetUserAsyncTask(chatDao).execute(name).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<User> getUsers(){
        List<User> users = null;
        try {
            users= new GetUsersAsyncTask(chatDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  users;
    }
    public void insertChatMessage(ChatMessage message, String chatId) {
        new InsertChatMessageAsyncTask(chatDao).execute(message);
        GroupChatMessagesCrossRef ref = new GroupChatMessagesCrossRef(chatId,message.getChatMessageID());
        new InsertGroupChatMessagesCrossRefAsyncTask(chatDao).execute(ref);

    }

    public void insertGroupChat(GroupChat groupChat, List<User> users) {
        new InsertGroupChatAsyncTask(chatDao).execute(groupChat);

        for(User user: users){
            GroupChatUsersCrossRef ref = new GroupChatUsersCrossRef(user.getUserID(), groupChat.getGroupChatID());
            new InsertGroupChatUsersCrossRefAsyncTask(chatDao).execute(ref);
        }
    }

    public void insertGroupChatMessagesCrossRef(GroupChatMessagesCrossRef groupChatMessagesCrossRef) {
        new InsertGroupChatMessagesCrossRefAsyncTask(chatDao).execute(groupChatMessagesCrossRef);
    }

    public void insertGroupChatUsersCrossRef(GroupChatUsersCrossRef groupChatUsersCrossRef) {
        new InsertGroupChatUsersCrossRefAsyncTask(chatDao).execute(groupChatUsersCrossRef);
    }

    public LiveData<GroupChatWithMessages> getMessagesOfGroupChat(String ID) {
        LiveData<GroupChatWithMessages> chatMessages = chatDao.getGroupChatWithMessages(ID);

        return chatMessages;
    }

    public LiveData<List<ChatMessage>> getChatMessages(){

        return  chatDao.getChatMessages();
    }

    public LiveData<UsersWithGroupChats> getGroupsFromUser(String ID){
        LiveData<UsersWithGroupChats> groups;
        return chatDao.getUsersWithGroupChats(ID);
    }

    private static class InsertChatMessageAsyncTask extends AsyncTask<ChatMessage, Void, Void> {
        private ChatDao chatDao;

        private InsertChatMessageAsyncTask(ChatDao dao) {
            chatDao = dao;
        }

        @Override
        protected Void doInBackground(ChatMessage... chatMessages) {
            chatDao.insertChatMessage(chatMessages[0]);
            return null;
        }
    }

    private static class InsertGroupChatAsyncTask extends AsyncTask<GroupChat, Void, Void> {
        private ChatDao chatDao;


        public InsertGroupChatAsyncTask(ChatDao dao) {
            chatDao = dao;
        }

        @Override
        protected Void doInBackground(GroupChat... groupChats) {
            chatDao.insertGroupChat(groupChats[0]);
            return null;
        }
    }

    private static class InsertGroupChatUsersCrossRefAsyncTask extends AsyncTask<GroupChatUsersCrossRef, Void, Void> {
        private ChatDao chatDao;


        public InsertGroupChatUsersCrossRefAsyncTask(ChatDao dao) {
            chatDao = dao;
        }

        @Override
        protected Void doInBackground(GroupChatUsersCrossRef... groupChatUsersCrossRefs) {
            chatDao.insertGroupChatUsersCrossRef(groupChatUsersCrossRefs[0]);
            return null;
        }
    }

    private static class InsertGroupChatMessagesCrossRefAsyncTask extends AsyncTask<GroupChatMessagesCrossRef, Void, Void> {
        private ChatDao chatDao;

        public InsertGroupChatMessagesCrossRefAsyncTask(ChatDao dao) {
            chatDao = dao;
        }


        @Override
        protected Void doInBackground(GroupChatMessagesCrossRef... groupChatMessagesCrossRefs) {
            chatDao.insertGroupChatMessagesCrossRef(groupChatMessagesCrossRefs[0]);
            return null;
        }
    }
    private static class GetUsersAsyncTask extends AsyncTask<Void,Void,List<User>>{
        private ChatDao chatDao;
        public GetUsersAsyncTask(ChatDao dao){
            chatDao = dao;
        }

        @Override
        protected List<User> doInBackground(Void... voids) {
            return chatDao.getUsers();
        }
    }

    private static class GetUserAsyncTask extends AsyncTask<String,Void,User>{
        private ChatDao chatDao;
        public GetUserAsyncTask(ChatDao dao){
            chatDao = dao;
        }

        @Override
        protected User doInBackground(String... strings) {
            return  chatDao.getUser(strings[0]);
        }
    }

}
