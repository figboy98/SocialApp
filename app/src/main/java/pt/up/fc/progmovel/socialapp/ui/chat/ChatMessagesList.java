package pt.up.fc.progmovel.socialapp.ui.chat;

import java.util.LinkedList;
import java.util.List;

public class ChatMessagesList {
    private LinkedList<ChatMessage> mChatMessageList;


    private ChatMessagesList(){
    }

    public  void addMessage(ChatMessage message){
        mChatMessageList.addFirst(message);
    }

    public LinkedList<ChatMessage> getChatMessageList(){
        return mChatMessageList;
    }





}
