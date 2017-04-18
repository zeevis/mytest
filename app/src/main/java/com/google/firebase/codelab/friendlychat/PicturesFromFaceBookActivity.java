package com.google.firebase.codelab.friendlychat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by zeevi on 4/17/2017.
 */

public class PicturesFromFaceBookActivity extends AppCompatActivity {
    ArrayList<String> mPicUrlList;
    private GridView mGridViewFacebookAlbums;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_faceook_albums_activity_layout);
        mGridViewFacebookAlbums = (GridView)findViewById(R.id.gridViewFacebookAlbums);
        mPicUrlList = getIntent().getStringArrayListExtra("picture_facebook_list");
        mGridViewFacebookAlbums.setAdapter(new FacebookImageAdapter(this,mPicUrlList));
    }
}
