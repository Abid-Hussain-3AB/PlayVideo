package com.example.playvideo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.google.android.exoplayer2.util.Log;

import java.util.ArrayList;

public class MP3activity extends AppCompatActivity {
    static  ArrayList<ModelVideo> Mp3List= new ArrayList<>();
    RecyclerView AudioRc;
    static AudioAdapter audioAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_p3activity);
        AudioRc=findViewById(R.id.AudioRc);
        AudioRc.setLayoutManager(new GridLayoutManager(this,2));
        audioAdapter=new AudioAdapter(this,Mp3List);
        AudioRc.setAdapter(audioAdapter);
        loadVideo();
    }
    private void loadVideo() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                String[] projection = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DURATION,MediaStore.Audio.Media.RELATIVE_PATH};
                String sortorder = MediaStore.Audio.Media.DATE_ADDED + " DESC ";
                Cursor cursor = getApplication().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortorder);
                if (cursor != null) {
                    int idcolumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
                    int titlecolumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                    int derationcolumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
                    int pathcolumn=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.RELATIVE_PATH);
                    while (cursor.moveToNext()) {
                        long id = cursor.getLong(idcolumn);
                        String path = cursor.getString(pathcolumn);
                        String title = cursor.getString(titlecolumn);
                        int duration = cursor.getInt(derationcolumn);
                        Uri data = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
                        String DurationFormat;
                        int sec = (duration % 60000 / 1000);
                        int min = (duration / 60000) % 60000;
                        int hrs = (duration / 3600000);
                        if (hrs == 0) {
                            DurationFormat = String.format("%02d:%02d:%02d", hrs, min, sec);
                        } else {
                            DurationFormat = String.format("%02d:%02d", min, sec);

                        }
                        Mp3List.add(new ModelVideo(id, path, data, title, DurationFormat));
                       // Log.e("Path", path);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                audioAdapter.notifyItemInserted(Mp3List.size() - 1);
                            }
                        });
                    }
                }
            }
        }.start();

    }

}