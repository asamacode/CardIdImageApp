package com.example.backgroundremove.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.backgroundremove.R;

import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    Button btn_capture;
    FrameLayout mFrameLayout;
    Camera camera1;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    ProgressDialog mProgressDialog;
    public static boolean previewing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mProgressDialog = new ProgressDialog(this);

        getWindow().setFormat(PixelFormat.UNKNOWN);
        surfaceView = new SurfaceView(this);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        btn_capture = (Button) findViewById(R.id.btn_capture);
        mFrameLayout = findViewById(R.id.frame_layout);

        if (getIntent() != null) {
            boolean isWhite = getIntent().getBooleanExtra("isWhite", false);
            if (isWhite)
                surfaceView.setBackgroundResource(R.drawable.bg_white);
            else
                surfaceView.setBackgroundResource(R.drawable.bg_blue);
        }

        mFrameLayout.addView(surfaceView);

        if (!previewing) {

            camera1 = Camera.open();
            if (camera1 != null) {
                try {
                    camera1.setDisplayOrientation(90);
                    camera1.setPreviewDisplay(surfaceHolder);
                    camera1.startPreview();
                    previewing = true;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        btn_capture.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (camera1 != null) {
                    camera1.takePicture(myShutterCallback, myPictureCallback_RAW, myPictureCallback_JPG);

                }
            }
        });

    }

    Camera.ShutterCallback myShutterCallback = new Camera.ShutterCallback() {

        public void onShutter() {
            // TODO Auto-generated method stub
        }
    };

    Camera.PictureCallback myPictureCallback_RAW = new Camera.PictureCallback() {

        public void onPictureTaken(byte[] arg0, Camera arg1) {
            // TODO Auto-generated method stub
        }
    };

    Camera.PictureCallback myPictureCallback_JPG = new Camera.PictureCallback() {

        public void onPictureTaken(byte[] arg0, Camera arg1) {
            // TODO Auto-generated method stub
            Bitmap bitmapPicture = BitmapFactory.decodeByteArray(arg0, 0, arg0.length);

            Bitmap correctBmp = Bitmap.createBitmap(bitmapPicture, 0, 0, bitmapPicture.getWidth(), bitmapPicture.getHeight(), null, true);
            new CompressBitmapTask().execute(correctBmp);
        }
    };

    private class CompressBitmapTask extends AsyncTask<Bitmap, Integer, Void> {

        //Write file
        String filename = "bitmap.png";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setMessage("Đang xử lý hình ảnh ...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Bitmap... bitmaps) {
            try {
                FileOutputStream stream = openFileOutput(filename, Context.MODE_PRIVATE);
                bitmaps[0].compress(Bitmap.CompressFormat.PNG, 100, stream);

                //Cleanup
                stream.close();
                bitmaps[0].recycle();
            } catch (Exception e) {
                mProgressDialog.dismiss();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressDialog.dismiss();
            Intent intent = new Intent(CameraActivity.this, EraserActivity.class);
            intent.putExtra("image_capture", filename);
            startActivity(intent);
            finish();
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
        if (previewing) {
            camera1.stopPreview();
            previewing = false;
        }

        if (camera1 != null) {
            try {
                camera1.setPreviewDisplay(surfaceHolder);
                camera1.startPreview();
                previewing = true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

        camera1.stopPreview();
        camera1.release();
        camera1 = null;
        previewing = false;

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}