package pt.up.fc.progmovel.socialapp.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialapp.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pt.up.fc.progmovel.socialapp.database.ChatRepository;
import pt.up.fc.progmovel.socialapp.database.GroupChat;
import pt.up.fc.progmovel.socialapp.database.User;
import pt.up.fc.progmovel.socialapp.database.UsersWithGroupChats;

interface OnChatGroupListener{
    void onChatGroupClick(int position);
}

public class ChatGroupsFragment extends Fragment implements  OnChatGroupListener  {
    private String user2 = "3057508c-32cd-4dae-956c-2d22e442e6cf";
    private String user1 = "63b7ad0f-6a9d-4e26-a8fb-99bd3a9c9a7e";
    private ChatGroupsViewModel mGroups;
    private RecyclerView mGroupsRecyclerView;
    private GroupChatAdapter mAdapter = null;
    private static final String EXTRA_CHAT_ID =  "pt.up.fc.progmovel.socialapp.extra.CHAT_ID";
    private OnChatGroupListener mChatGroupListener;

    @Override
    public void onCreate(Bundle savedInstanceSate){
        super.onCreate(savedInstanceSate);
        ChatGroupsViewModelFactory chatGroupsViewModelFactory = new ChatGroupsViewModelFactory(requireActivity().getApplication(), user1);
        mGroups = new ViewModelProvider(requireActivity(), chatGroupsViewModelFactory).get(ChatGroupsViewModel.class);
        mChatGroupListener = (OnChatGroupListener) this;





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_chat_groups,container,false);
        mGroupsRecyclerView = view.findViewById(R.id.chat_groups_recycler_view);

        mGroupsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final Observer<UsersWithGroupChats> groupChatsObserver = usersWithGroupChats -> {
            if(usersWithGroupChats !=null){
                List<GroupChat> groupChatList = usersWithGroupChats.groupChats;
                updateUI(groupChatList);
            }
        };

        mGroups.getChatGroups().observe(getViewLifecycleOwner(), groupChatsObserver);
        return  view;
    }

    private void updateUI(List<GroupChat> groupChatList){
        if(mAdapter == null){
            mAdapter = new GroupChatAdapter(groupChatList,mChatGroupListener);
            mGroupsRecyclerView.setAdapter(mAdapter);
        }
        mAdapter.setGroupChatList(groupChatList);
    }

    @Override
    public void onChatGroupClick(int position) {
        Intent chatActivity = new Intent(getContext(), ChatActivity.class);
        Bundle bundle = new Bundle();
        String chatID = Objects.requireNonNull(mGroups.getChatGroups().getValue()).groupChats.get(position).getGroupChatID();
        bundle.putString(EXTRA_CHAT_ID, chatID);
        chatActivity.putExtra(EXTRA_CHAT_ID, chatID);
        //Toast.makeText(getContext(), "Chat Group clicked " + chatID, Toast.LENGTH_LONG).show();
        startActivity(chatActivity);

    }

    private class GroupChatHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private GroupChat mGroupChat;
        private TextView mChatName;
        private ImageView mChatImage;
        private OnChatGroupListener mChatGroupListener;


        public GroupChatHolder(LayoutInflater inflater, ViewGroup parent, OnChatGroupListener onChatGroupListener) {
            super(inflater.inflate(R.layout.chat_group, parent, false));
            mChatName = itemView.findViewById(R.id.chat_group_name);
            mChatImage = itemView.findViewById(R.id.chat_group_image);
            itemView.setOnClickListener(this);
            mChatGroupListener = onChatGroupListener;
        }

        public void bind(GroupChat groupChat){
            mGroupChat = groupChat;
            mChatName.setText(groupChat.getGroupName().toString());
            mChatImage.setImageDrawable(ResourcesCompat.getDrawable(requireContext().getResources(),R.drawable.ic_baseline_android_24, null));

        }

        @Override
        public void onClick(View v) {
            mChatGroupListener.onChatGroupClick(getAdapterPosition());
        }
    }

    private class GroupChatAdapter extends RecyclerView.Adapter<GroupChatHolder>{
        private List<GroupChat> mGroupChatList = new ArrayList<>();
        private OnChatGroupListener mChatGroupListener;

        public GroupChatAdapter(List<GroupChat> groupChatList, OnChatGroupListener onChatGroupListener){
            mGroupChatList = groupChatList;
            mChatGroupListener = onChatGroupListener;
        }

        @NonNull
        @NotNull
        @Override
        public GroupChatHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            return new GroupChatHolder(layoutInflater,parent, mChatGroupListener);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull GroupChatHolder holder, int position) {
            GroupChat groupChat = mGroupChatList.get(position);
            holder.bind(groupChat);

        }

        @Override
        public int getItemCount() {
            return mGroupChatList.size();
        }

        public void setGroupChatList(List<GroupChat> groupChatList) {
            mGroupChatList = groupChatList;
            notifyDataSetChanged();
        }
    }
}