package com.example.playvideo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import static com.example.playvideo.MainActivity.folderlist;
import static com.example.playvideo.MainActivity.videolist;

public class VideoFolders extends AppCompatActivity {

    FolderAdapter folderAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_folders);

      recyclerView=findViewById(R.id.RvFolder);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        folderAdapter=new FolderAdapter(folderlist,videolist,this);
        recyclerView.setAdapter(folderAdapter);

    }
}