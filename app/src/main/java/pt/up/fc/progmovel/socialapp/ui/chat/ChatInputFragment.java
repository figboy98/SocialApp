package pt.up.fc.progmovel.socialapp.ui.chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.socialapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import androidx.lifecycle.Observer;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;


public class ChatInputFragment extends Fragment {
    private ChatMessage mChatMessage;
    private EditText mInputMessage;
    private ImageButton mSendButton;
    private ImageButton mImagesButton;
    private ImageButton mVideosButton;
    //private RecyclerView mImagesOrVideosInput;
    private ArrayList<Uri> mImagesOrVideosList = new ArrayList<Uri>();
   // private ImagesVideoInputAdapter mAdapter;
    private final int GET_IMAGE_CODE = 1;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mChatMessage = new ChatMessage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_input, container, false);

        mInputMessage = view.findViewById(R.id.chat_message_input);
        mSendButton = view.findViewById(R.id.send_image_button);
        mImagesButton = view.findViewById(R.id.image_input_button);
        mVideosButton = view.findViewById(R.id.video_input_button);

        mImagesButton.setOnClickListener(new ImagesButtonClickListener("image/*"));
        mVideosButton.setOnClickListener(new ImagesButtonClickListener("video/*"));

        /*
        mImagesOrVideosInput = view.findViewById(R.id.recycler_view_images_videos_input);
        mImagesOrVideosInput.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new ImagesVideoInputAdapter(mImagesOrVideosList);
        mImagesOrVideosInput.setAdapter(mAdapter);*/

        return view;
    }


    private class ImagesButtonClickListener implements View.OnClickListener{
        private String mType;
        public ImagesButtonClickListener(String type){
            mType = type;
        }

        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(),
                new ActivityResultCallback<List<Uri>>() {
                    @Override
                    public void onActivityResult(List<Uri> result) {

                        if(result.size()>0){
                            mImagesOrVideosList = (ArrayList)result;
                        }

                    }
                });

        @Override
        public void onClick(View v) {
            mGetContent.launch(mType);
        }
    }

   /* private class ImagesVideosInput extends RecyclerView.ViewHolder{

        private ImageView mImageVideo;

        public ImagesVideosInput(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.fragment_chat_input,parent,false));
            mImageVideo = itemView.findViewById(R.id.input_image_video);
        }

        public void bind(Uri uri){

            mImageVideo.setImageURI(uri);
        }

    }

    private class ImagesVideoInputAdapter extends RecyclerView.Adapter<ImagesVideosInput>{
        private ArrayList<Uri> imagesList;

        public ImagesVideoInputAdapter(ArrayList<Uri> list){

            imagesList = list;
        }

        @NonNull
        @Override
        public ImagesVideosInput onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return  new ImagesVideosInput(layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ImagesVideosInput holder, int position) {
            Uri uri = imagesList.get(position);
            holder.bind(uri);


        }

        @Override
        public int getItemCount() {
            return imagesList.size();
        }
    }*/


}