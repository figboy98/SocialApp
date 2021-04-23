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

    private class MessageHolder extends RecyclerView.ViewHolder{
        private ChatMessage mChatMessage;
        private TextView mMessageReceived;
        private TextView mMessageSent;

        public MessageHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.conversation_message,parent,false));

            mMessageReceived = itemView.findViewById(R.id.receiver_message_text);
            mMessageSent = itemView.findViewById(R.id.sender_messsage_text);
        }

        public void bind(ChatMessage message){
            mChatMessage = message;
            if(message.getFrom().equals("me")){
                mMessageSent.setText(message.getTextMessage().toString());
                mMessageReceived.setVisibility(View.INVISIBLE);
            }
            else{
                mMessageReceived.setText(message.getTextMessage().toString());
                mMessageSent.setVisibility(View.INVISIBLE);
            }
        }
    }

    private class MessageAdapter extends  RecyclerView.Adapter<MessageHolder>{
        private List<ChatMessage> mChatMessageList;
        public MessageAdapter(List<ChatMessage> messages){
            mChatMessageList = messages;
        }

        @NonNull
        @Override
        public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new MessageHolder(layoutInflater,parent);

        }

        @Override
        public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
            ChatMessage message = mChatMessageList.get(position);
            holder.bind(message);

        }

        @Override
        public int getItemCount() {
            return mChatMessageList.size();
        }
    }





}
