package com.example.playvideo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class MP3PlayActivity extends AppCompatActivity {
    private PlayerView playerView;
    private SimpleExoPlayer player;
    long videoId;
    long AudioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        initializeView();
        AudioId=getIntent().getExtras().getLong("MP3");

    }
    private void initializeView()
    {
        playerView=findViewById(R.id.ViewPlayer);
    }
    private void initializePlyer()
    {
        player=new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        Uri audioUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,AudioId);
        MediaSource mediaSou = buildMediaSource(audioUri);
        player.prepare(mediaSou);
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