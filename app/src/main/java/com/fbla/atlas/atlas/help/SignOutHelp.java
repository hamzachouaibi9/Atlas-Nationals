package com.fbla.atlas.atlas.help;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;

import com.fbla.atlas.atlas.R;



public class SignOutHelp extends AppCompatActivity {

    VideoView video;
    ProgressDialog progressDialog;
    String video_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sign_out_help);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        video = (VideoView) findViewById(R.id.signout_video);
        video_URL = "https://firebasestorage.googleapis.com/v0/b/atlas-c1067.appspot.com/o/video_help%2Fsignout.mp4?alt=media&token=eddfd42b-75b5-4011-a150-f9c3dc7ba281";
        Uri uri = Uri.parse(video_URL);
        video.setVideoURI(uri);

        progressDialog = new ProgressDialog(this);
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