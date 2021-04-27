package pt.up.fc.progmovel.socialapp.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;
import java.util.List;

public class ChatMessageListFragment extends Fragment {
    private RecyclerView mMessagesRecyclerView;
    private ChatMessageListViewModel mMessagesViewModel;
    private MessageAdapter mMessageAdapter;
    private static final  int TEXT_RECEIVED =0;
    private static final  int TEXT_SENT =1;

    @Override
    public void onCreate(Bundle savedInstanceSate) {
        super.onCreate(savedInstanceSate);
        mMessagesViewModel = new ViewModelProvider(getActivity()).get(ChatMessageListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat_conversation, container, false);
        mMessagesRecyclerView = view.findViewById(R.id.chat_conversation_recycler_view);

        mMessagesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        final Observer<List<ChatMessage>> messagesObserver = new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(@Nullable final List<ChatMessage> chatMessageList) {
                updateUI(chatMessageList);
            }
        };

        mMessagesViewModel.getMessages().observe(getViewLifecycleOwner(), messagesObserver);

        return view;
    }

    private void updateUI(List<ChatMessage> messagesList){

        if(mMessageAdapter == null){
            mMessageAdapter = new MessageAdapter(messagesList);
            mMessagesRecyclerView.setAdapter(mMessageAdapter);
        }
        else{
            mMessageAdapter.notifyDataSetChanged();
        }
        mMessagesRecyclerView.scrollToPosition(messagesList.size()-1);

    }

    private class MessageTextReceived extends RecyclerView.ViewHolder {
        private ChatMessage mChatMessage;
        private TextView mMessage;

        public MessageTextReceived(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.chat_text_received, parent, false));

            mMessage = itemView.findViewById(R.id.message_received_holder);
        }

        public void bind(ChatMessage message) {
            mChatMessage = message;
            mMessage.setText(message.getTextMessage().toString());


        }
    }

    private class MessageTextSent extends RecyclerView.ViewHolder{
        private  ChatMessage mChatMessage;
        private  TextView mMessage;

        public MessageTextSent(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.chat_text_sent,parent,false));

            mMessage =  itemView.findViewById(R.id.message_sent_holder);
        }

        public void bind(ChatMessage message){
            mChatMessage = message;
            mMessage.setText(message.getTextMessage().toString());
        }
    }


    private class MessageAdapter extends  RecyclerView.Adapter{
        private List<ChatMessage> mChatMessageList;
        public MessageAdapter(List<ChatMessage> messages){
            mChatMessageList = messages;
        }

        @Override
        public int getItemViewType(int position){
             ChatMessage message = mChatMessageList.get(position);
            if(message.getFrom().toString().equals("me")){
                return TEXT_SENT;
            }
            else{
                return  TEXT_RECEIVED;
            }

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            switch (viewType){
                case TEXT_RECEIVED:
                    return new MessageTextReceived(layoutInflater,parent);
                case TEXT_SENT:
                    return new MessageTextSent(layoutInflater,parent);
                default:
                    return null;
            }
        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ChatMessage message = mChatMessageList.get(position);
            switch (holder.getItemViewType()) {
                case TEXT_RECEIVED:
                    MessageTextReceived messageTextReceived = (MessageTextReceived) holder;
                    messageTextReceived.bind(message);
                    break;
                case TEXT_SENT:
                    MessageTextSent messageTextSent = (MessageTextSent) holder;
                    messageTextSent.bind(message);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return mChatMessageList.size();
        }
    }





}
