package com.fbla.atlas.atlas.help;


import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.fbla.atlas.atlas.R;
import com.fbla.atlas.atlas.activities.AdminMainActivity;
import com.fbla.atlas.atlas.services.PushNotificationAdminService;

public class AccountHelp extends AppCompatActivity {

    VideoView video;
    String video_URL = "https://firebasestorage.googleapis.com/v0/b/atlas-c1067.appspot.com/o/video_help%2Fviewing_account.mp4?alt=media&token=049c85e9-224f-49f6-9d5c-7d0728e782fa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_account_help);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        video = (VideoView) findViewById(R.id.account_video);
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
