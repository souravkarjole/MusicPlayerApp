package com.example.vibeit;

import android.media.MediaPlayer;

public class MyMediaPlayerInstance {
    static MediaPlayer mediaPlayer;

    public static MediaPlayer getInstance(){
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
        }

        return mediaPlayer;
    }

    public static int currentIndex = -1;
}
