package pt.up.fc.progmovel.socialapp.ui.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.socialapp.R;

import pt.up.fc.progmovel.socialapp.util.Constants;

public class VideoActivity extends AppCompatActivity {
    private VideoView mVideoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mVideoView = findViewById(R.id.videoView);

        Intent intent = getIntent();
        String path = intent.getStringExtra(Constants.EXTRA_VIDEO_URI);
        Uri uri = Uri.parse(path);
        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(mVideoView);
        mVideoView.setMediaController(controller);
        mVideoView.setVideoURI(uri);

    }
}