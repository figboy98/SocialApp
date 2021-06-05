package pt.up.fc.progmovel.socialapp.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialapp.R;

import java.util.List;
import java.util.Objects;

import pt.up.fc.progmovel.socialapp.database.ChatMessage;
import pt.up.fc.progmovel.socialapp.database.GroupChatWithMessages;
import pt.up.fc.progmovel.socialapp.util.Constants;
interface OnMessageListener{
    void onMessageClick(int position);
}

public class  ChatMessageListFragment extends Fragment implements OnMessageListener {
    private RecyclerView mMessagesRecyclerView;
    private ChatMessageListViewModel mMessagesViewModel;
    private MessageAdapter mMessageAdapter;
    private OnMessageListener mOnMessageListener;
    private static final  int TEXT_RECEIVED =0;
    private static final  int TEXT_SENT =1;
    private static final int IMAGE_RECEIVED =2;
    private static final int IMAGE_SENT =3;
    private static final int VIDEO_SENT = 4;
    private static  final int VIDEO_RECEIVED =5;
    private String mLocalUserId;
    private View view;


    @Override
    public void onCreate(Bundle savedInstanceSate) {
        super.onCreate(savedInstanceSate);

        String chatID = null;
        if (getArguments() != null) {
            chatID = getArguments().getString(Constants.EXTRA_CHAT_ID);
        }
        mOnMessageListener = this;
        SharedPreferences preferences = requireActivity().getSharedPreferences(Constants.SHARED_PREFERENCES,Context.MODE_PRIVATE);

        mLocalUserId = preferences.getString(Constants.SHARED_LOCAL_USER_ID, "");

        ChatMessageListViewModelFactory chatMessageListViewModelFactory = new ChatMessageListViewModelFactory(requireActivity().getApplication(), chatID);
        mMessagesViewModel = new ViewModelProvider(requireActivity(), chatMessageListViewModelFactory).get(ChatMessageListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat_conversation, container, false);
        mMessagesRecyclerView = view.findViewById(R.id.chat_conversation_recycler_view);

        mMessagesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final Observer<GroupChatWithMessages> messagesObserver = groupChatWithMessages -> {
            if(groupChatWithMessages !=null){
                List<ChatMessage> orderedMessages = groupChatWithMessages.getSortedMessages();
                updateUI(orderedMessages);
            }
        };

       mMessagesViewModel.getMessages().observe(getViewLifecycleOwner(), messagesObserver);
        this.view = view;
        return view;
    }

    private void updateUI(List<ChatMessage> messagesList){

        if(mMessageAdapter == null){
            mMessageAdapter = new MessageAdapter(messagesList,mOnMessageListener);
            mMessagesRecyclerView.setAdapter(mMessageAdapter);
        }
        else{
            mMessageAdapter.setChatMessageList(messagesList);
        }
        mMessagesRecyclerView.scrollToPosition(messagesList.size());
    }

    @Override
    public void onMessageClick(int position) {
        /*Intent videoActivity = new Intent(requireContext(), VideoViewActivity.class);


        String path = uri.getPath();

        videoActivity.putExtra(Constants.EXTRA_VIDEO_URI, path );
        startActivity(videoActivity);*/
//        Uri uri = Uri.parse(Objects.requireNonNull(mMessagesViewModel.getMessages().getValue()).chatMessages.get(position).getTextMessage());
//
//        Intent mediaIntent = new Intent();
//        mediaIntent.setAction(Intent.ACTION_VIEW);
//        mediaIntent.setData(uri);
//        if(mediaIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager())!=null){
//            startActivity(mediaIntent);
//        }

    }

    private static class MessageTextReceived extends RecyclerView.ViewHolder {
        private final TextView mMessage;

        public MessageTextReceived(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.chat_text_received, parent, false));

            mMessage = itemView.findViewById(R.id.text_received_holder);
        }

        public void bind(ChatMessage message) {
            mMessage.setText(message.getTextMessage());


        }
    }

    private static class MessageTextSent extends RecyclerView.ViewHolder{
        private final TextView mMessage;

        public MessageTextSent(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.chat_text_sent,parent,false));

            mMessage =  itemView.findViewById(R.id.text_sent_holder);
        }

        public void bind(ChatMessage message){
            mMessage.setText(message.getTextMessage());
        }
    }

    private class MessageImageSent extends  RecyclerView.ViewHolder{
        private final ImageView mImage;

        public MessageImageSent(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.chat_image_sent,parent,false));
            mImage = itemView.findViewById(R.id.image_sent_holder);
        }

        public void bind(ChatMessage message){

            Uri imageUri = Uri.parse(message.getTextMessage());
            //Picasso.get().load(imageUri).resize(2000,2000).onlyScaleDown().centerCrop().into(mImage);
            Glide.with(view).asBitmap().fitCenter().load(imageUri).into(mImage);


        }
    }

    private class MessageImageReceived extends  RecyclerView.ViewHolder{
        private final ImageView mImage;

        public MessageImageReceived(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.chat_image_received,parent,false));
            mImage = itemView.findViewById(R.id.image_received_holder);
        }

        public void bind(ChatMessage message){
            Uri imageUri = Uri.parse(message.getTextMessage());
            Glide.with(view).asBitmap().fitCenter().load(imageUri).into(mImage);

        }
    }

    private class MessageVideoReceived extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView mVideo;
        private final OnMessageListener mOnClickListener;

        public MessageVideoReceived(LayoutInflater inflater, ViewGroup parent, OnMessageListener onClickListener){
            super(inflater.inflate(R.layout.chat_video_received,parent,false));
            mVideo = itemView.findViewById(R.id.video_received_holder);
            itemView.setOnClickListener(this);
            mOnClickListener = onClickListener;
        }
        public void bind(ChatMessage message){
            Uri videoUri = Uri.parse(message.getTextMessage());
            Glide.with(view)
                    .asBitmap()
                    .load(videoUri)
                    .placeholder(R.drawable.default_image)
                    .into(mVideo);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onMessageClick(getAdapterPosition());
        }
    }

    private class MessageVideoSent extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView mVideo;
        private final OnMessageListener mOnClickListener;


        public MessageVideoSent(LayoutInflater inflater, ViewGroup parent, OnMessageListener onMessageListener){
            super(inflater.inflate(R.layout.chat_video_sent,parent,false));
            mVideo = itemView.findViewById(R.id.video_sent_holder);
            itemView.setOnClickListener(this);
            mOnClickListener = onMessageListener;
        }
        public void bind(ChatMessage message){
            Uri videoUri = Uri.parse(message.getTextMessage());
            Glide.with(view)
                    .asBitmap()
                    .load(videoUri)
                    .into(mVideo);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onMessageClick(getAdapterPosition());

        }
    }
    private class MessageAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List<ChatMessage> mChatMessageList;
        private OnMessageListener mOnMessageListener;
        public MessageAdapter(List<ChatMessage> messages, OnMessageListener onMessageListener) {
            mChatMessageList = messages;
            mOnMessageListener =onMessageListener;
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

            switch (type) {
                case "text":
                    if (from.equals(mLocalUserId)) {
                        return TEXT_SENT;
                    } else {
                        return TEXT_RECEIVED;
                    }
                case "image":
                    if (from.equals(mLocalUserId)) {
                        return IMAGE_SENT;
                    } else {
                        return IMAGE_RECEIVED;
                    }
                case "video":
                    if (from.equals(mLocalUserId)) {
                        return VIDEO_SENT;
                    } else {
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
                    return new MessageTextReceived(layoutInflater, parent);
                case TEXT_SENT:
                    return new MessageTextSent(layoutInflater, parent);
                case IMAGE_RECEIVED:
                    return  new MessageImageReceived(layoutInflater,parent);
                case IMAGE_SENT:
                    return new MessageImageSent(layoutInflater,parent);
                case VIDEO_RECEIVED:
                    return new MessageVideoReceived(layoutInflater,parent,mOnMessageListener);
                case VIDEO_SENT:
                    return new MessageVideoSent(layoutInflater,parent,mOnMessageListener);
            }
            return new MessageTextSent(layoutInflater, parent);
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
