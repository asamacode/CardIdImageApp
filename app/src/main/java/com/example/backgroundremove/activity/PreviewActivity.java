package com.example.backgroundremove.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.example.backgroundremove.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;

public class PreviewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_position);

		Intent intent = getIntent();
		String imagePath = intent.getStringExtra("imagePath");
		Toast.makeText(getApplicationContext(), "Image is saved at " + imagePath, Toast.LENGTH_LONG).show();

		PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
		photoView.setImageURI(getImageUri(imagePath));

	}

	private Uri getImageUri(String path) {
		return Uri.fromFile(new File(path));
	}
	
}
