package pt.up.fc.progmovel.socialapp.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.socialapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import pt.up.fc.progmovel.socialapp.database.SocialAppRepository;
import pt.up.fc.progmovel.socialapp.database.User;
import pt.up.fc.progmovel.socialapp.ui.chat.ChatInputFragment;
import pt.up.fc.progmovel.socialapp.util.Constants;

public class HomeFragment extends Fragment {
    private TextView userName;
    private String userId;
    private SocialAppRepository mSocialAppRepository;
    private User user;


    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       /* homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);*/
        //        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        SharedPreferences preferences = requireActivity().getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        userId = preferences.getString(Constants.SHARED_LOCAL_USER_ID, "");

        mSocialAppRepository = new SocialAppRepository(requireActivity().getApplication());
        user = mSocialAppRepository.getUserFromId(userId);
        userName =  view.findViewById(R.id.home_user);
        userName.setText(user.getName());


        return view;
    }
}