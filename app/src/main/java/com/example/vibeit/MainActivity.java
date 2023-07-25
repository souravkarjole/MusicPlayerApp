package com.example.vibeit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView noMusicTextView;
    ArrayList<AudioModel> songsList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    LinearLayout btnShare;

    private static final int PICK_SONG_REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShare = findViewById(R.id.btnShare);
        recyclerView = findViewById(R.id.recycler_view);
        noMusicTextView = findViewById(R.id.no_songs_list);


        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFilePicker();
            }
        });

        if(!checkPermission()){
            requestPermission();
        }

        String[] projection = {
          MediaStore.Audio.Media.TITLE,
          MediaStore.Audio.Media.DATA,
          MediaStore.Audio.Media.DURATION
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,null);

        while(cursor.moveToNext()){
            AudioModel songData = new AudioModel(cursor.getString(1),cursor.getString(0),cursor.getString(2));
            songsList.add(songData);
        }

        if(songsList.size() == 0){
            noMusicTextView.setVisibility(View.VISIBLE);
        }else{
            initRecyclerView();
        }
    }

    /*
        It creates an intent to open
     */
    private void startFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("audio/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_SONG_REQUEST_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_SONG_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<Uri> selectedSongUris = new ArrayList<>();

            // Check if multiple songs are selected
            if (data.getClipData() != null) {
                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri songUri = item.getUri();
                    if (songUri != null) {
                        selectedSongUris.add(songUri);
                    }
                }
            } else {
                // Only one song is selected
                Uri songUri = data.getData();
                if (songUri != null) {
                    selectedSongUris.add(songUri);
                }
            }

            if (!selectedSongUris.isEmpty()) {
                shareSongs(selectedSongUris);
            }
        }
    }

    private void shareSongs(List<Uri> songUris) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.setType("audio/*");
        /*
            This step is necessary because the Intent mechanism allows data to be shared between
            different components or apps on Android devices using the concept of extras.

            The "Intent.EXTRA_STREAM" extra key is a standard Android constant that is used to
            indicate that the data being attached to the intent is a stream of data (e.g., a file).
            The value associated with this key should be a ParcelableArrayList containing Uri objects representing
            the data to be shared.

            In summary, putParcelableArrayListExtra() is used to attach the list of selected song URIs to the Intent,
            making it available to other apps when the sharing action is performed.
         */
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, new ArrayList<>(songUris));
        startActivity(Intent.createChooser(shareIntent, "Share Songs"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(recyclerView != null){
            recyclerView.setAdapter(new Adapter_MUSIC(getApplicationContext(),songsList));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android_large___1_activity.mediaPlayer.stop();
        android_large___1_activity.mediaPlayer.release();
        Intent serviceIntent = new Intent(this,MusicForegroundService.class);
        stopService(serviceIntent);
    }

    void initRecyclerView(){
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new Adapter_MUSIC(getApplicationContext(),songsList));
    }
    boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED;
    }



    void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "READ PERMISSION IS REQUIRED!", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        }
    }
}