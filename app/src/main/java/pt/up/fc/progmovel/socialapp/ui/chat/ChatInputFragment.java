package pt.up.fc.progmovel.socialapp.ui.chat;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.socialapp.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pt.up.fc.progmovel.socialapp.database.ChatMessage;
import pt.up.fc.progmovel.socialapp.database.ChatRepository;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;


public class ChatInputFragment extends Fragment {
    private ChatRepository mChatRepository;
    private String mChatID;
    private EditText mInputMessage;
    // private ImagesVideoInputAdapter mAdapter;
    private final int GET_IMAGE_CODE = 1;
    private static final String EXTRA_CHATID =  "pt.up.fc.progmovel.socialapp.extra.CHATID";


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ChatMessage chatMessage = new ChatMessage();
        mChatRepository = new ChatRepository(getActivity().getApplication());
        mChatID = getArguments().getString(EXTRA_CHATID);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_input, container, false);

        mInputMessage = view.findViewById(R.id.chat_message_input);
        ImageButton sendButton = view.findViewById(R.id.send_image_button);
        ImageButton imagesButton = view.findViewById(R.id.image_input_button);
        ImageButton videosButton = view.findViewById(R.id.video_input_button);

        imagesButton.setOnClickListener(new ImagesButtonClickListener("image/*"));
        videosButton.setOnClickListener(new ImagesButtonClickListener("video/*"));

        sendButton.setOnClickListener(new SentButtonClickListener());

        /*
        mImagesOrVideosInput = view.findViewById(R.id.recycler_view_images_videos_input);
        mImagesOrVideosInput.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new ImagesVideoInputAdapter(mImagesOrVideosList);
        mImagesOrVideosInput.setAdapter(mAdapter);*/

        return view;
    }

    private class SentButtonClickListener implements  View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(mInputMessage.toString().length() > 0){
                Date date = new Date();
                ChatMessage message = new ChatMessage(mInputMessage.getText().toString(), date,"me","to","text");
                mChatRepository.insertChatMessage(message,mChatID);
            }
            mInputMessage.setText("");


        }
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
                            //private RecyclerView mImagesOrVideosInput;
                            ArrayList<Uri> imagesOrVideosList = (ArrayList) result;
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