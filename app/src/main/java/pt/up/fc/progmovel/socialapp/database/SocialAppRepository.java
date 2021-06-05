package pt.up.fc.progmovel.socialapp.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SocialAppRepository {
    private DatabaseDao databaseDao;

    public SocialAppRepository(Application application) {
        SocialAppDatabase database = SocialAppDatabase.getDatabase(application);
        databaseDao = database.chatDao();
    }
    public User getUser(String name){
        User user = null;
        try {
            user=  new GetUserAsyncTask(databaseDao).execute(name).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<User> getUsers(){
        List<User> users = null;
        try {
            users= new GetUsersAsyncTask(databaseDao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return  users;
    }
    public void insertChatMessage(ChatMessage message) {
        String chatId = message.getTo();
        new InsertChatMessageAsyncTask(databaseDao).execute(message);
        GroupChatMessagesCrossRef ref = new GroupChatMessagesCrossRef(chatId,message.getChatMessageID());
        new InsertGroupChatMessagesCrossRefAsyncTask(databaseDao).execute(ref);

    }

    public void insertGroupChat(GroupChat groupChat, List<User> users) {
        new InsertGroupChatAsyncTask(databaseDao).execute(groupChat);

        for(User user: users){
            GroupChatUsersCrossRef ref = new GroupChatUsersCrossRef(user.getUserId(), groupChat.getGroupChatID());
            new InsertGroupChatUsersCrossRefAsyncTask(databaseDao).execute(ref);
        }
    }

    public void insertGroupChatMessagesCrossRef(GroupChatMessagesCrossRef groupChatMessagesCrossRef) {
        new InsertGroupChatMessagesCrossRefAsyncTask(databaseDao).execute(groupChatMessagesCrossRef);
    }

    public void insertGroupChatUsersCrossRef(GroupChatUsersCrossRef groupChatUsersCrossRef) {
        new InsertGroupChatUsersCrossRefAsyncTask(databaseDao).execute(groupChatUsersCrossRef);
    }

    public LiveData<GroupChatWithMessages> getMessagesOfGroupChat(String ID) {
        LiveData<GroupChatWithMessages> chatMessages = databaseDao.getGroupChatWithMessages(ID);

        return chatMessages;
    }

    public LiveData<List<ChatMessage>> getChatMessages(){

        return  databaseDao.getChatMessages();
    }

    public LiveData<UsersWithGroupChats> getGroupsFromUser(String ID){
        LiveData<UsersWithGroupChats> groups;
        return databaseDao.getUsersWithGroupChats(ID);
    }

    private static class InsertChatMessageAsyncTask extends AsyncTask<ChatMessage, Void, Void> {
        private DatabaseDao databaseDao;

        private InsertChatMessageAsyncTask(DatabaseDao dao) {
            databaseDao = dao;
        }

        @Override
        protected Void doInBackground(ChatMessage... chatMessages) {
            databaseDao.insertChatMessage(chatMessages[0]);
            return null;
        }
    }

    private static class InsertGroupChatAsyncTask extends AsyncTask<GroupChat, Void, Void> {
        private DatabaseDao databaseDao;


        public InsertGroupChatAsyncTask(DatabaseDao dao) {
            databaseDao = dao;
        }

        @Override
        protected Void doInBackground(GroupChat... groupChats) {
            databaseDao.insertGroupChat(groupChats[0]);
            return null;
        }
    }

    private static class InsertGroupChatUsersCrossRefAsyncTask extends AsyncTask<GroupChatUsersCrossRef, Void, Void> {
        private DatabaseDao databaseDao;


        public InsertGroupChatUsersCrossRefAsyncTask(DatabaseDao dao) {
            databaseDao = dao;
        }

        @Override
        protected Void doInBackground(GroupChatUsersCrossRef... groupChatUsersCrossRefs) {
            databaseDao.insertGroupChatUsersCrossRef(groupChatUsersCrossRefs[0]);
            return null;
        }
    }

    private static class InsertGroupChatMessagesCrossRefAsyncTask extends AsyncTask<GroupChatMessagesCrossRef, Void, Void> {
        private DatabaseDao databaseDao;

        public InsertGroupChatMessagesCrossRefAsyncTask(DatabaseDao dao) {
            databaseDao = dao;
        }


        @Override
        protected Void doInBackground(GroupChatMessagesCrossRef... groupChatMessagesCrossRefs) {
            databaseDao.insertGroupChatMessagesCrossRef(groupChatMessagesCrossRefs[0]);
            return null;
        }
    }
    private static class GetUsersAsyncTask extends AsyncTask<Void,Void,List<User>>{
        private DatabaseDao databaseDao;
        public GetUsersAsyncTask(DatabaseDao dao){
            databaseDao = dao;
        }

        @Override
        protected List<User> doInBackground(Void... voids) {
            return databaseDao.getUsers();
        }
    }

    private static class GetUserAsyncTask extends AsyncTask<String,Void,User>{
        private DatabaseDao databaseDao;
        public GetUserAsyncTask(DatabaseDao dao){
            databaseDao = dao;
        }

        @Override
        protected User doInBackground(String... strings) {
            return  databaseDao.getUser(strings[0]);
        }
    }

}
