package com.example.backgroundremove;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.backgroundremove.activity.CameraActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startCameraWhiteActivity(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra("isWhite", true);
        startActivity(intent);
    }

    public void startCameraBlueActivity(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra("isWhite", false);
        startActivity(intent);
    }
}