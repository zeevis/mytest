package com.google.firebase.codelab.friendlychat;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.WheelViewAdapter;

//import com.wx.wheelview.adapter.ArrayWheelAdapter;

/**
 * Created by zeevi on 2/3/2017.
 */

public class MyArrayWheelAdapter implements WheelViewAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<User>  mUserArrayList;

    public MyArrayWheelAdapter(Context context, ArrayList<User>  userArrayList) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mUserArrayList = userArrayList;
    }

    @Override
    public int getItemsCount() {
        return mUserArrayList.size();
    }

    @Override
    public View getItem(int i, View view, ViewGroup viewGroup) {
        View view1 = mInflater.inflate(R.layout.vertical_wheel_view,viewGroup,false);
        String photoUrl = mUserArrayList.get(i).getmUserPhotoUrl();
        ImageView mainPhoto = (ImageView) view1.findViewById(R.id.mainImageView);

        Glide.with(mContext)
                .load(photoUrl)
                .into(mainPhoto);
        return view1;
    }

    @Override
    public View getEmptyItem(View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }
}
