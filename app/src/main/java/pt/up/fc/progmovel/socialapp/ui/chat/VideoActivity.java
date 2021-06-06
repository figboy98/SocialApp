package pt.up.fc.progmovel.socialapp.ui.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
        //getExternalMediaDirs()
        Uri uri = Uri.parse(path);
        path = uri.getPath();
        String finalPath="";


        Cursor cursor = null;
            try {
                String[] proj = { MediaStore.Video.Media.DATA };
                cursor = getContentResolver().query(uri,  proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                 finalPath = cursor.getString(column_index);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(mVideoView);
        mVideoView.setMediaController(controller);
        mVideoView.setVideoPath(path);
       // mVideoView.setVideoURI(uri);

    }
}