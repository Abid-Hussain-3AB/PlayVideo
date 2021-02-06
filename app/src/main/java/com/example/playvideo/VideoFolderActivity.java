package com.example.playvideo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.google.android.exoplayer2.util.Log;

import java.util.ArrayList;

public class VideoFolderActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    VideoFolderAdapter videoFolderAdapter;
    String MfolderName;
    ArrayList<ModelVideo> modelVideoArrayList= new ArrayList<>();
    ArrayList<ModelVideo> videofolderlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_folder);
        recyclerView=findViewById(R.id.FolderAct);
        MfolderName=getIntent().getStringExtra("FNAME");
        loadVideos(MfolderName);

        if (MfolderName!=null)
        {

            modelVideoArrayList =videofolderlist;
        }

        videoFolderAdapter=new VideoFolderAdapter(this,modelVideoArrayList);
        recyclerView.setAdapter(videoFolderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
    }
    public void loadVideos(String folder) {
        new Thread() {
            @Override
            public void run() {
                super.run();

                String[] projection = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DURATION,MediaStore.Video.Media.RELATIVE_PATH,MediaStore.Video.Media.DATA};
                String selection = MediaStore.Video.Media.DATA + " like?";
                String[] selectionArgs= new String[]{"%" + folder + "%"};
                String sortorder = MediaStore.Video.Media.DATE_ADDED + " DESC ";


                Cursor cursor = getApplication().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, sortorder);
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
                        videofolderlist.add(new ModelVideo(id, path, data, title, DurationFormat));


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                videoFolderAdapter.notifyItemInserted(videofolderlist.size());
                            }
                        });
                    }
                }
            }
        }.start();

    }
}