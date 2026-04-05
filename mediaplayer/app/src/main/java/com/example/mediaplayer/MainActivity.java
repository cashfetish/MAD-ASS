package com.example.mediaplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    VideoView videoView;
    EditText url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button openFile = findViewById(R.id.openFile);
        Button openUrl = findViewById(R.id.openUrl);
        Button play = findViewById(R.id.play);
        Button pause = findViewById(R.id.pause);
        Button stop = findViewById(R.id.stop);
        Button restart = findViewById(R.id.restart);

        videoView = findViewById(R.id.videoView);
        url = findViewById(R.id.url);

        // Open audio file
        openFile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("audio/*");
            startActivityForResult(intent, 1);
        });

        // Play video from URL
        openUrl.setOnClickListener(v -> {
            String videoUrl = url.getText().toString();
            if (!videoUrl.isEmpty()) {
                videoView.setVideoURI(Uri.parse(videoUrl));
                videoView.start();
            }
        });

        // Controls
        play.setOnClickListener(v -> videoView.start());

        pause.setOnClickListener(v -> videoView.pause());

        stop.setOnClickListener(v -> videoView.stopPlayback());

        restart.setOnClickListener(v -> {
            videoView.seekTo(0);
            videoView.start();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            videoView.setVideoURI(uri);
            videoView.start();
        }
    }
}
