package com.example.playvideo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Intent;
import android.media.VolumeAutomation;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.khizar1556.mkvideoplayer.MKPlayer;

import java.net.URI;
import java.util.ArrayList;

import static com.example.playvideo.MainActivity.videolist;
import static com.example.playvideo.VideoFolderAdapter.videofolderlist;

public class PlayActivity extends AppCompatActivity {
    private enum VolumeState {ON, OFF}
    private VolumeState volumeState;
  private   PlayerView playerView;
   private SimpleExoPlayer player;
    long videoId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        initializeView();
        videoId=getIntent().getExtras().getLong("videoId");


    }
    private void initializeView()
    {
        playerView=findViewById(R.id.ViewPlayer);
    }
    private void initializePlyer()
    {
        player=new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        Uri videoUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,videoId);
        MediaSource mediaSource =buildMediaSource(videoUri);
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);

    }
    private MediaSource buildMediaSource(Uri uri)
    {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,getString(R.string.app_name));
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);

    }
    private void relasePlayer()
    {
        if (player!=null)
        {
            player.release();
            player=null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(Util.SDK_INT>=24)
        {
            initializePlyer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.SDK_INT<24||player==null)
        {
            initializePlyer();
        }
    }

    @Override
    protected void onPause() {
        if (Util.SDK_INT<24)
        {
            relasePlayer();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (Util.SDK_INT>=24)
        {
            relasePlayer();
        }
        super.onStop();
    }

}