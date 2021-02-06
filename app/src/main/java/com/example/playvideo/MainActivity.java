package com.example.playvideo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static ArrayList<ModelVideo> videolist = new ArrayList<>();
    private VideoAdapter videoAdapter;
    static ArrayList<String> folderlist = new ArrayList<>();
    Button folder;
    ImageView MP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        folder = findViewById(R.id.btnFolder);
        MP=findViewById(R.id.btnMP);
        initializeView();
        checkPermision();

        folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, VideoFolders.class);
                startActivity(intent);


            }
        });
        MP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(MainActivity.this,MP3activity.class);
                startActivity(inten);
            }
        });
    }

    private void initializeView() {
        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.RcView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        videoAdapter = new VideoAdapter(this, videolist);
        recyclerView.setAdapter(videoAdapter);

    }

    private void checkPermision() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            } else {
                loadVideo();

            }

        } else {
            loadVideo();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadVideo();
            } else {
                Toast.makeText(this, "Permission was not Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadVideo() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                String[] projection = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DURATION,MediaStore.Video.Media.RELATIVE_PATH};
                String sortorder = MediaStore.Video.Media.DATE_ADDED + " DESC ";
                Cursor cursor = getApplication().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortorder);
                if (cursor != null) {
                    int idcolumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                    int titlecolumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                    int derationcolumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
                    int pathcolumn=cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RELATIVE_PATH);
                    while (cursor.moveToNext()) {
                        long id = cursor.getLong(idcolumn);
                        String path = cursor.getString(pathcolumn);
                        String title = cursor.getString(titlecolumn);
                        int duration = cursor.getInt(derationcolumn);
                        Uri data = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
                        String DurationFormat;
                        int sec = (duration % 60000 / 1000);
                        int min = (duration / 60000) % 60000;
                        int hrs = (duration / 3600000);
                        if (hrs == 0) {
                            DurationFormat = String.format("%02d:%02d:%02d", hrs, min, sec);
                        } else {
                            DurationFormat = String.format("%02d:%02d", min, sec);

                        }
                        videolist.add(new ModelVideo(id, path, data, title, DurationFormat));
                        Log.e("Path", path);
                        int slashFirstIndex = path.lastIndexOf("/");

                        String subString=path.substring(0, slashFirstIndex);

                        int index = subString.lastIndexOf("/");
                        String foldername=subString.substring(index+1, slashFirstIndex);

                        if (!folderlist.contains(foldername)) {
                            folderlist.add(foldername);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                videoAdapter.notifyItemInserted(videolist.size() - 1);
                            }
                        });
                    }
                }
            }
        }.start();

    }

}