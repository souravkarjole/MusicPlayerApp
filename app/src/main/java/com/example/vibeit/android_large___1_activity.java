package com.example.vibeit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class android_large___1_activity extends Activity {


	private TextView song_title,song_starting_duration,song_total_duration;
	private ImageView pausePlay,music_Icon;
	private RelativeLayout prev_song,next_song;
	SeekBar seekBar;
	private LinearLayout batckBtn;

	ArrayList<AudioModel> songsList;
	int position;
	float x;

	public static MediaPlayer mediaPlayer = new MediaPlayer();
	private Handler handler = new Handler();


	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.android_large___1);


		song_title = findViewById(R.id.song_title);
		song_starting_duration = findViewById(R.id.start_duration);
		song_total_duration = findViewById(R.id.total_duration);
		prev_song = findViewById(R.id.skip_forward_ek2);
		next_song = findViewById(R.id.skip_forward_ek1);
		pausePlay = findViewById(R.id.pause_btn);
		music_Icon =findViewById(R.id.music_1);
		seekBar = findViewById(R.id.seekBar);
		batckBtn = findViewById(R.id.backBtn);

		song_title.setSelected(true);


		//custom code goes here

		songsList = (ArrayList<AudioModel>) getIntent().getSerializableExtra("LIST");
		position = getIntent().getIntExtra("POS",0);

		try {
			setTheSONG();
		} catch (IOException e) {
			e.printStackTrace();
		}

		updateSeekBarAndTime();

		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromuser) {
				if(mediaPlayer != null && fromuser){
					mediaPlayer.seekTo(progress);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});


		batckBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});
	}
	private void updateSeekBarAndTime() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			int currentPosition = mediaPlayer.getCurrentPosition();
			seekBar.setProgress(currentPosition);
			song_starting_duration.setText(convertToMS(currentPosition + ""));

			if(mediaPlayer.isPlaying()){
				x += 0.5;
				music_Icon.setRotation(x);
				pausePlay.setImageResource(R.drawable.pause_circle);
			}else{
				music_Icon.setRotation(0);
				pausePlay.setImageResource(R.drawable.play);

			}
		}

		// Schedule the next update after 100 milliseconds
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				updateSeekBarAndTime();
			}
		}, 30);
	}

	void setTheSONG() throws IOException {

		song_title.setText(songsList.get(MyMediaPlayerInstance.currentIndex).getTitle());
		song_total_duration.setText(convertToMS(songsList.get(MyMediaPlayerInstance.currentIndex).getDuration()));


		pausePlay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				pauseMusic();
			}
		});

		next_song.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				try {
					playNextSong();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		prev_song.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				try {
					playPrevSong();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		playMusic();

	}


	private void stopAndReleaseMediaPlayer() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}


	private void playMusic() throws IOException {
		Intent serviceIntent = new Intent(this,MusicForegroundService.class);
		serviceIntent.putExtra("SONG_NAME", songsList.get(MyMediaPlayerInstance.currentIndex).getTitle());

		startService(serviceIntent);

		stopAndReleaseMediaPlayer();
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setDataSource(songsList.get(MyMediaPlayerInstance.currentIndex).getData());
		mediaPlayer.prepare();
		mediaPlayer.start();
		seekBar.setProgress(0);
		seekBar.setMax(mediaPlayer.getDuration());
	}

	private void playNextSong() throws IOException {
		if(position == songsList.size()-1){
			return;
		}

		position++;
		MyMediaPlayerInstance.currentIndex = position;
		mediaPlayer.reset();
		setTheSONG();
	}

	private void playPrevSong() throws IOException {
		if(position == 0){
			return;
		}
		position--;
		MyMediaPlayerInstance.currentIndex = position;
		mediaPlayer.reset();
		setTheSONG();
	}

	private void pauseMusic(){
		if(mediaPlayer.isPlaying()){
			mediaPlayer.pause();
			pausePlay.setImageResource(R.drawable.play);
		}else{
			mediaPlayer.start();
			pausePlay.setImageResource(R.drawable.pause_circle);
		}
	}

	@SuppressLint("DefaultLocale")
	public static String convertToMS(String duration){
		Long millis = Long.parseLong(duration);

		return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
				                                       TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
	}
}







