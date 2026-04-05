package com.example.camgallery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 1;

    Button capture;
    GridView grid;
    TextView imageCount;
    ArrayList<Bitmap> images = new ArrayList<>();
    ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        capture = findViewById(R.id.capture);
        grid = findViewById(R.id.grid);
        imageCount = findViewById(R.id.image_count);

        adapter = new ImageAdapter(this, images);
        grid.setAdapter(adapter);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 100);
        }

        capture.setOnClickListener(v -> {
            checkPermissionAndOpenFile();
        });

        grid.setOnItemClickListener((parent, view, position, id) -> {
            // Show details (simple)
            Toast.makeText(this, "Image " + position, Toast.LENGTH_SHORT).show();
        });

        grid.setOnItemLongClickListener((parent, view, position, id) -> {
            images.remove(position);
            adapter.notifyDataSetChanged();
            updateImageCount();
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void updateImageCount() {
        if (imageCount != null) {
            imageCount.setText("Total images: " + images.size());
        }
    }

    private void checkPermissionAndOpenFile() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            images.add(photo);
            adapter.notifyDataSetChanged();
            updateImageCount();
        }
    }
}
