package pt.up.fc.progmovel.socialapp.ui.chat;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.socialapp.R;



public class ChatGroups extends Fragment {
    private String user2 = "ea63f905-ee65-4df0-9d15-530be680c6f4";
    private String user1 = "484fcf9a-c83d-4719-b6ce-5ac642d784c3";
    private ChatGroupsViewModel mGroups;



    @Override
    public void onCreate(Bundle savedInstanceSate){
        super.onCreate(savedInstanceSate);
        mGroups = new ViewModelProvider(this).get(ChatGroupsViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_chat_groups,container,false);
        return  view;
    }


}