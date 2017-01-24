package com.google.firebase.codelab.friendlychat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ProphileActivity extends AppCompatActivity {
    private final int CROPPING_IMAGE_FROM_GALERY_REQUEST_CODE = 1;
    private final int CAPTURE_IMAGE_CAMERA_REQUEST_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prophile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case CROPPING_IMAGE_FROM_GALERY_REQUEST_CODE:

                    Uri uri = data.getData();
                    if (uri == null) {
                        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                        uri = ImageUtils.getImageUri(getApplicationContext(), bitmap);
                        postImage(bitmap);
                    }
                    else{
                        ImageUtils.getBitmapFromUri(getApplicationContext(), target, uri);
                    }

                    Picasso.with(getApplicationContext()).load(uri).transform(new CircleTransform()).into(mImageViewAvatar);

                    break;

                case CAPTURE_IMAGE_CAMERA_REQUEST_CODE:
                    Picasso.with(getApplicationContext()).load(mCurrentPhotoPath).transform(new CircleTransform()).into(mImageViewAvatar);
                    ImageUtils.getBitmapFromUri(getApplicationContext(), target, Uri.parse(mCurrentPhotoPath));

                    break;
            }
        }
    }
}
