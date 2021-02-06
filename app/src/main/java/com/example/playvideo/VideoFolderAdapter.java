package com.example.playvideo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class VideoFolderAdapter extends RecyclerView.Adapter<VideoFolderAdapter.MyViewHolder> {
   static ArrayList<ModelVideo> videofolderlist= new ArrayList<>();
    Context context;
    VideoFolderAdapter(Context context,ArrayList<ModelVideo> videolist)
    {
        this.context=context;
        this.videofolderlist=videolist;
    }
    @NonNull
    @Override
    public VideoFolderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ModelVideo item=videofolderlist.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvDuration.setText(item.getDuration());
        Glide.with(context).load(item.getData()).into(holder.thumnail);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),PlayActivity.class);
                intent.putExtra("videoId",item.getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videofolderlist.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumnail;
        TextView tvTitle, tvDuration;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle=itemView.findViewById(R.id.tvTitle);
            tvDuration=itemView.findViewById(R.id.tvDuration);
            thumnail=itemView.findViewById(R.id.thumb_nail);

        }
    }
}
