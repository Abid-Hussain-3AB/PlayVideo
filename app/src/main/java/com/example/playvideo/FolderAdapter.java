package com.example.playvideo;

import android.content.Context;
import android.content.Intent;
import android.os.RecoverySystem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.MyHolder> {
    private ArrayList<String> foldrName;
    private static ArrayList<ModelVideo> videosfiles;
    Context context;

    public FolderAdapter(ArrayList<String> foldrName, ArrayList<ModelVideo> videosfiles, Context context) {
        this.foldrName = foldrName;
        this.videosfiles = videosfiles;
        this.context = context;
    }

    @NonNull
    @Override
    public FolderAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.folder,parent,false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final ModelVideo item=videosfiles.get(position);
        holder.foldername.setText(foldrName.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,VideoFolderActivity.class);
                intent.putExtra("FNAME",foldrName.get(position));
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return foldrName.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView foldername;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            foldername=itemView.findViewById(R.id.folder_name);
        }
    }
}
