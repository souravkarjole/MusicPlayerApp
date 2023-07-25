package com.example.vibeit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.mtp.MtpDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_MUSIC extends RecyclerView.Adapter<Adapter_MUSIC.ViewHolder>{

    private ArrayList<AudioModel> songList;
    private Context context;

    public Adapter_MUSIC(Context context,ArrayList<AudioModel> songList) {
        this.songList = songList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.set_song_name.setText(songList.get(position).getTitle());

        if (MyMediaPlayerInstance.currentIndex == position) {
            holder.curr_PlayingSong.setVisibility(View.VISIBLE);
        } else {
            holder.curr_PlayingSong.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyMediaPlayerInstance.getInstance().reset();
                MyMediaPlayerInstance.currentIndex = position;

                Intent intent = new Intent(context,android_large___1_activity.class);
                intent.putExtra("LIST",songList);
                intent.putExtra("POS",holder.getAdapterPosition());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView set_image_View;
        TextView set_song_name;
        ImageView curr_PlayingSong;

        public ViewHolder( View itemView) {
            super(itemView);

            set_image_View = itemView.findViewById(R.id.music_image);
            set_song_name = itemView.findViewById(R.id.song_Name);
            curr_PlayingSong = itemView.findViewById(R.id.curr_PlayingSong);
        }
    }
}
