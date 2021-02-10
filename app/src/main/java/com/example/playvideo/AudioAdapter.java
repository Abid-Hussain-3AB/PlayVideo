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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.MyViewHolder> {
    ArrayList<ModelVideo> Audiolist= new ArrayList<>();
    Context context;

    AudioAdapter(Context context,ArrayList<ModelVideo> Audiolist)
    {
        this.context=context;
        this.Audiolist=Audiolist;

    }
    @NonNull
    @Override
    public AudioAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioAdapter.MyViewHolder holder, int position) {
        final ModelVideo item =Audiolist.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvDuration.setText(item.getDuration());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MP3PlayActivity.class);
                intent.putExtra("MP3",item.getId());
                v.getContext().startActivity(intent);
                String s=item.getTitle();
                HashMap<String,String> data = new HashMap<>();
                data.put("Video Title",s);
                FirebaseDatabase.getInstance().getReference()
                        .child("Watch History MP3")
                        .push()
                        .setValue(data)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // Toast.makeText(context, "data inserted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //  Toast.makeText(context, "Faild data", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Toast.makeText(context, "SuccessFull", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });


    }

    @Override
    public int getItemCount() {
        return Audiolist.size();
    }

   static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumNail;
        TextView tvTitle, tvDuration;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            thumNail= itemView.findViewById(R.id.thumb_nail);
            tvTitle=itemView.findViewById(R.id.tvTitle);
            tvDuration=itemView.findViewById(R.id.tvDuration);
        }
    }
}
