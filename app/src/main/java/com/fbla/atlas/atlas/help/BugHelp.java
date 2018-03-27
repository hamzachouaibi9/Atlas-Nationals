package com.fbla.atlas.atlas.help;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.VideoView;

import com.fbla.atlas.atlas.R;


public class BugHelp extends AppCompatActivity {

    VideoView video;
    String video_URL = "https://firebasestorage.googleapis.com/v0/b/atlas-c1067.appspot.com/o/video_help%2Fbug.mp4?alt=media&token=a6a0c0e6-f203-436d-91ee-274347b22b19";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bug_help);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        video = (VideoView) findViewById(R.id.bug_video);
        Uri uri = Uri.parse(video_URL);
        video.setVideoURI(uri);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        progressDialog.setCancelable(false);

        video.requestFocus();
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressDialog.dismiss();
                mp.setLooping(true);
                mp.setVolume(0f, 0f);
                video.start();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        return super.onOptionsItemSelected(item);
    }

}
