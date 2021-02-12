package com.example.playvideo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Intent;
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

import java.net.URI;
import java.util.ArrayList;

import static com.example.playvideo.MainActivity.videolist;
import static com.example.playvideo.VideoFolderAdapter.videofolderlist;

public class PlayActivity extends AppCompatActivity {
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

  /*  @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
        case MotionEvent.ACTION_DOWN:
        test=false;
        if (event.getX() < (sWidth / 2)) {
            intLeft = true;
            intRight = false;
        } else if (event.getX() > (sWidth / 2)) {
            intLeft = false;
            intRight = true;
        }
        int upperLimit = (sHeight / 4) + 100;
        int lowerLimit = ((sHeight / 4) * 3) - 150;
        if (event.getY() < upperLimit) {
            intBottom = false;
            intTop = true;
        } else if (event.getY() > lowerLimit) {
            intBottom = true;
            intTop = false;
        } else {
            intBottom = false;
            intTop = false;
        }
        seekSpeed = (TimeUnit.MILLISECONDS.toSeconds(exoPlayer.getDuration()) * 0.1);
        diffX = 0;
        calculatedTime = 0;
        seekDur = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(diffX) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diffX)),
                TimeUnit.MILLISECONDS.toSeconds(diffX) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diffX)));

        //TOUCH STARTED
        baseX = event.getX();
        baseY = event.getY();
        break;
        case MotionEvent.ACTION_MOVE:
        screen_swipe_move=true;
        root.setVisibility(View.GONE);
        diffX = (long) (Math.ceil(event.getX() - baseX));
        diffY = (long) Math.ceil(event.getY() - baseY);
        double brightnessSpeed = 0.05;
        if (Math.abs(diffY) > MIN_DISTANCE) {
            tested_ok = true;
        }
        if (Math.abs(diffY) > Math.abs(diffX)) {
            if (intLeft) {
                cResolver = getContentResolver();
                window = getWindow();
                try {
                    Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                    brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
                int new_brightness = (int) (brightness - (diffY * brightnessSpeed));
                if (new_brightness > 250) {
                    new_brightness = 250;
                } else if (new_brightness < 1) {
                    new_brightness = 1;
                }
                double brightPerc = Math.ceil((((double) new_brightness / (double) 250) * (double) 100));
                brightnessBarContainer.setVisibility(View.VISIBLE);
                brightness_center_text.setVisibility(View.VISIBLE);
                brightnessBar.setProgress((int) brightPerc);
                if (brightPerc < 30) {
                    brightnessIcon.setImageResource(R.drawable.ic_bright_min);
                    brightness_image.setImageResource(R.drawable.ic_bright_min);
                } else if (brightPerc > 30 && brightPerc < 80) {
                    brightnessIcon.setImageResource(R.drawable.ic_bright_med);
                    brightness_image.setImageResource(R.drawable.ic_bright_med);
                } else if (brightPerc > 80) {
                    brightnessIcon.setImageResource(R.drawable.ic_bright_max);
                    brightness_image.setImageResource(R.drawable.ic_bright_max);
                }
                brigtness_perc_center_text.setText(" " + (int) brightPerc);
                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, (new_brightness));
                WindowManager.LayoutParams layoutpars = window.getAttributes();
                layoutpars.screenBrightness = brightness / (float) 255;
                window.setAttributes(layoutpars);
            }else if (intRight) {
                vol_center_text.setVisibility(View.VISIBLE);
                mediavolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                double cal = (double) diffY * ((double)maxVol/(double)(device_height*4));
                int newMediaVolume = mediavolume - (int) cal;
                if (newMediaVolume > maxVol) {
                    newMediaVolume = maxVol;
                } else if (newMediaVolume < 1) {
                    newMediaVolume = 0;
                }
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newMediaVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                double volPerc = Math.ceil((((double) newMediaVolume / (double) maxVol) * (double) 100));
                vol_perc_center_text.setText(" " + (int) volPerc);
                if (volPerc < 1) {
                    volIcon.setImageResource(R.drawable.ic_volume_mute);
                    vol_image.setImageResource(R.drawable.ic_volume_mute);
                    vol_perc_center_text.setVisibility(View.GONE);
                } else if (volPerc >= 1) {
                    volIcon.setImageResource(R.drawable.ic_volume);
                    vol_image.setImageResource(R.drawable.ic_volume);
                    vol_perc_center_text.setVisibility(View.VISIBLE);
                }
                volumeBarContainer.setVisibility(View.VISIBLE);
                volumeBar.setProgress((int) volPerc);
            }
        }else if (Math.abs(diffX) > Math.abs(diffY)) {
            if (Math.abs(diffX) > (MIN_DISTANCE + 100)) {
                tested_ok = true;
                root.setVisibility(View.VISIBLE);
                seekBar_center_text.setVisibility(View.VISIBLE);
                onlySeekbar.setVisibility(View.VISIBLE);
                (exoPlayer.getCurrentPosition() + (calculatedTime)));
            }
        }
        break;
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
        screen_swipe_move=false;
        tested_ok = false;

        seekBar_center_text.setVisibility(View.GONE);
        brightness_center_text.setVisibility(View.GONE);
        vol_center_text.setVisibility(View.GONE);
        brightnessBarContainer.setVisibility(View.GONE);
        volumeBarContainer.setVisibility(View.GONE);
        onlySeekbar.setVisibility(View.VISIBLE);
        root.setVisibility(View.VISIBLE);
        calculatedTime = (int) (exoPlayer.getCurrentPosition() + (calculatedTime));
        exoPlayer.seekTo(calculatedTime);
        break;
        return super.onTouchEvent(event);
    }




    }

   */
}