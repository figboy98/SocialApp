package pt.up.fc.progmovel.socialapp.ui.chat;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;

import java.util.ArrayList;
import java.util.List;

public class  ChatMessageListFragment extends Fragment {
    private RecyclerView mMessagesRecyclerView;
    private ChatMessageListViewModel mMessagesViewModel;
    private ChatMessageListViewModelFactory mChatMessageListViewModelFactory;
    private MessageAdapter mMessageAdapter;
    private String mChatID;
    private static final  int TEXT_RECEIVED =0;
    private static final  int TEXT_SENT =1;
    private static final int IMAGE_RECEIVED =2;
    private static final int IMAGE_SENT =3;
    private static final int VIDEO_SENT = 4;
    private static  final int VIDEO_RECEIVED =5;
    private static final String EXTRA_CHATID =  "pt.up.fc.progmovel.socialapp.extra.CHATID";



    @Override
    public void onCreate(Bundle savedInstanceSate) {
        super.onCreate(savedInstanceSate);

        mChatID  = getArguments().getString(EXTRA_CHATID);
        mChatMessageListViewModelFactory = new ChatMessageListViewModelFactory(getActivity().getApplication(), mChatID);
        mMessagesViewModel = new ViewModelProvider(getActivity(), mChatMessageListViewModelFactory).get(ChatMessageListViewModel.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat_conversation, container, false);
        mMessagesRecyclerView = view.findViewById(R.id.chat_conversation_recycler_view);

        mMessagesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final Observer<GroupChatWithMessages> messagesObserver = new Observer<GroupChatWithMessages>() {
            @Override
            public void onChanged(GroupChatWithMessages groupChatWithMessages) {
                List<ChatMessage> orderedMessages = groupChatWithMessages.getSortedMessages();
                updateUI(orderedMessages);
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
            mMessageAdapter.setChatMessageList(messagesList);
        }
        mMessagesRecyclerView.scrollToPosition(messagesList.size()-1);

    }

    private class MessageTextReceived extends RecyclerView.ViewHolder {
        private ChatMessage mChatMessage;
        private TextView mMessage;

        public MessageTextReceived(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.chat_text_received, parent, false));

            mMessage = itemView.findViewById(R.id.text_received_holder);
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

            mMessage =  itemView.findViewById(R.id.text_sent_holder);
        }

        public void bind(ChatMessage message){
            mChatMessage = message;
            mMessage.setText(message.getTextMessage().toString());
        }
    }

    private class MessageImageSent extends  RecyclerView.ViewHolder{
        private ChatMessage mChatMessage;
        private ImageView mImage;

        public MessageImageSent(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.chat_image_sent,parent,false));
            mImage = itemView.findViewById(R.id.image_sent_holder);
        }

        public void bind(ChatMessage message){
            mChatMessage = message;
            //Uri imagePath = Uri.parse(message.getTextMessage());
            //mImage.setImageURI(imagePath);
            mImage.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.default_image, getContext().getTheme()));

        }
    }

    private class MessageImageReceived extends  RecyclerView.ViewHolder{
        private ChatMessage mChatMessage;
        private ImageView mImage;

        public MessageImageReceived(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.chat_image_received,parent,false));
            mImage = itemView.findViewById(R.id.image_received_holder);
        }

        public void bind(ChatMessage message){
            mChatMessage = message;
            //Uri imagePath = Uri.parse(message.getTextMessage());
            //mImage.setImageURI(imagePath);
            mImage.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.default_image, getContext().getTheme()));


        }
    }

    private class MessageVideoReceived extends RecyclerView.ViewHolder{
        private ChatMessage mChatMessage;
        private ImageView mVideo;

        public MessageVideoReceived(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.chat_video_received,parent,false));
            mVideo = itemView.findViewById(R.id.video_received_holder);
        }
        public void bind(ChatMessage message){
            mChatMessage = message;
            mVideo.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.default_image, getContext().getTheme()));
        }
    }

    private class MessageVideoSent extends RecyclerView.ViewHolder{
        private ChatMessage mChatMessage;
        private ImageView mVideo;

        public MessageVideoSent(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.chat_video_sent,parent,false));
            mVideo = itemView.findViewById(R.id.video_sent_holder);
        }
        public void bind(ChatMessage message){
            mChatMessage = message;
            mVideo.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.default_image, getContext().getTheme()));
        }
    }


    private class MessageAdapter extends  RecyclerView.Adapter{
        private List<ChatMessage> mChatMessageList = new ArrayList<>();
        public MessageAdapter(List<ChatMessage> messages) {
            mChatMessageList = messages;
        }

        public void setChatMessageList(List<ChatMessage> messages){
            mChatMessageList = messages;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position){
             ChatMessage message = mChatMessageList.get(position);
             String type = message.getType();
             String from = message.getFrom();

            if(type.equals("text")){
                if(from.equals("me")){
                    return TEXT_SENT;
                }
                else{
                    return  TEXT_RECEIVED;
                }
            }
            else if(type.equals("image")){
                if(from.equals("me")){
                    return IMAGE_SENT;
                }
                else{
                    return IMAGE_RECEIVED;
                }
            }
            else if(type.equals("video")){
                if(from.equals("me")){
                    return VIDEO_SENT;
                }
                else{
                    return VIDEO_RECEIVED;
                }
            }
            return -1;

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

            switch (viewType){
                case TEXT_RECEIVED:
                    return new MessageTextReceived(layoutInflater,parent);
                case TEXT_SENT:
                    return new MessageTextSent(layoutInflater,parent);
                case IMAGE_RECEIVED:
                    return  new MessageImageReceived(layoutInflater,parent);
                case IMAGE_SENT:
                    return new MessageImageSent(layoutInflater,parent);
                case VIDEO_RECEIVED:
                    return new MessageVideoReceived(layoutInflater,parent);
                case VIDEO_SENT:
                    return new MessageVideoSent(layoutInflater,parent);
            }
            return new MessageTextSent(layoutInflater,parent);
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
                case IMAGE_RECEIVED:
                    MessageImageReceived messageImageReceived = (MessageImageReceived)holder;
                    messageImageReceived.bind(message);
                    break;
                case IMAGE_SENT:
                    MessageImageSent messageImageSent = (MessageImageSent)holder;
                    messageImageSent.bind(message);
                    break;
                case VIDEO_RECEIVED:
                    MessageVideoReceived messageVideoReceived = (MessageVideoReceived) holder;
                    messageVideoReceived.bind(message);
                    break;
                case VIDEO_SENT:
                    MessageVideoSent messageVideoSent = (MessageVideoSent)holder;
                    messageVideoSent.bind(message);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return mChatMessageList.size();
        }
    }





}
