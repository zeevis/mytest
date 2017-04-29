package com.google.firebase.codelab.friendlychat.wheel_controls;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.codelab.friendlychat.R;

import java.util.ArrayList;

/**
 * Created by zeevi on 4/22/2017.
 */

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<String> mProfilePictures;

    public CustomPagerAdapter(Context context, ArrayList<String> aProfilePictures) {
        mContext = context;
        this.mProfilePictures = aProfilePictures;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout =  mLayoutInflater.inflate(R.layout.view_pager_example_layout, collection, false);
        ImageView imageView = (ImageView) layout.findViewById(R.id.imageViewMainListWheelSinglePictureScreen);

        Glide.with(mContext)
                .load(mProfilePictures.get(position))
                .into(imageView);

        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return mProfilePictures.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
       // CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
        return "bla bla lba";
    }

}