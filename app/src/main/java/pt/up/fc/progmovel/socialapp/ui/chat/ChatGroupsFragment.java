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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.socialapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pt.up.fc.progmovel.socialapp.database.GroupChat;
import pt.up.fc.progmovel.socialapp.database.UsersWithGroupChats;


public class ChatGroupsFragment extends Fragment {
    private String user2 = "a93de7b0-f672-4da2-8e4e-8aab0cb5bc16";
    private String user1 = "e51832df-435c-45ce-9f02-89273a837c17";
    private ChatGroupsViewModel mGroups;
    private RecyclerView mGroupsRecyclerView;
    private GroupChatAdapter mAdapter = null;
    private static final String EXTRA_CHAT_ID =  "pt.up.fc.progmovel.socialapp.extra.CHATID";



    @Override
    public void onCreate(Bundle savedInstanceSate){
        super.onCreate(savedInstanceSate);

        ChatGroupsViewModelFactory chatGroupsViewModelFactory = new ChatGroupsViewModelFactory(requireActivity().getApplication(), user1);
        mGroups = new ViewModelProvider(requireActivity(), chatGroupsViewModelFactory).get(ChatGroupsViewModel.class);
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
            mAdapter = new GroupChatAdapter(groupChatList);
            mGroupsRecyclerView.setAdapter(mAdapter);
        }

        mAdapter.setGroupChatList(groupChatList);

    }


    private class GroupChatHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private GroupChat mGroupChat;
        private TextView mChatName;
        private ImageView mChatImage;


        public GroupChatHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.chat_group, parent, false));
            mChatName = itemView.findViewById(R.id.chat_group_name);
            mChatImage = itemView.findViewById(R.id.chat_group_image);
        }

        public void bind(GroupChat groupChat){
            mGroupChat = groupChat;
            mChatName.setText(groupChat.getGroupName().toString());
            mChatImage.setImageDrawable(ResourcesCompat.getDrawable(requireContext().getResources(),R.drawable.ic_baseline_android_24, null));

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(),ChatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_CHAT_ID, mGroupChat.getGroupChatID());
            startActivity(intent);
        }
    }
    private class GroupChatAdapter extends RecyclerView.Adapter<GroupChatHolder>{
        private List<GroupChat> mGroupChatList = new ArrayList<>();

        public GroupChatAdapter(List<GroupChat> groupChatList){
            mGroupChatList = groupChatList;
        }

        @NonNull
        @NotNull
        @Override
        public GroupChatHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            return new GroupChatHolder(layoutInflater,parent);
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