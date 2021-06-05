package pt.up.fc.progmovel.socialapp.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import pt.up.fc.progmovel.socialapp.database.GroupChat;
import pt.up.fc.progmovel.socialapp.database.UsersWithGroupChats;
import pt.up.fc.progmovel.socialapp.util.Constants;

interface OnChatGroupListener{
    void onChatGroupClick(int position);
}

public class ChatGroupsFragment extends Fragment implements  OnChatGroupListener  {
    private ChatGroupsViewModel mGroups;
    private RecyclerView mGroupsRecyclerView;
    private GroupChatAdapter mAdapter = null;
    private OnChatGroupListener mChatGroupListener;

    @Override
    public void onCreate(Bundle savedInstanceSate){
        super.onCreate(savedInstanceSate);
        SharedPreferences preferences = requireActivity().getSharedPreferences(Constants.SHARED_PREFERENCES,Context.MODE_PRIVATE);

        String localUserId = preferences.getString(Constants.SHARED_LOCAL_USER_ID, "");

        ChatGroupsViewModelFactory chatGroupsViewModelFactory = new ChatGroupsViewModelFactory(requireActivity().getApplication(), localUserId);
        mGroups = new ViewModelProvider(requireActivity(), chatGroupsViewModelFactory).get(ChatGroupsViewModel.class);
        mChatGroupListener = this;
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
        String chatID = Objects.requireNonNull(mGroups.getChatGroups().getValue()).groupChats.get(position).getGroupChatID();

        chatActivity.putExtra(Constants.EXTRA_CHAT_ID, chatID);
        startActivity(chatActivity);

    }

    private class GroupChatHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView mChatName;
        private final ImageView mChatImage;
        private final OnChatGroupListener mChatGroupListener;


        public GroupChatHolder(LayoutInflater inflater, ViewGroup parent, OnChatGroupListener onChatGroupListener) {
            super(inflater.inflate(R.layout.chat_group, parent, false));
            mChatName = itemView.findViewById(R.id.chat_group_name);
            mChatImage = itemView.findViewById(R.id.chat_group_image);
            itemView.setOnClickListener(this);
            mChatGroupListener = onChatGroupListener;
        }

        public void bind(GroupChat groupChat){
            mChatName.setText(groupChat.getGroupName());
            mChatImage.setImageDrawable(ResourcesCompat.getDrawable(requireContext().getResources(),R.drawable.ic_baseline_android_24, null));

        }

        @Override
        public void onClick(View v) {
            mChatGroupListener.onChatGroupClick(getAdapterPosition());
        }
    }

    private class GroupChatAdapter extends RecyclerView.Adapter<GroupChatHolder>{
        private List<GroupChat> mGroupChatList;
        private final OnChatGroupListener mChatGroupListener;

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