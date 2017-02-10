package com.google.firebase.codelab.friendlychat;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.WheelViewAdapter;

/**
 * Created by zeevi on 2/9/2017.
 */

public class MyHorizontalArrayWheelAdapter implements WheelViewAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<String> mUserPhotosList;

    public MyHorizontalArrayWheelAdapter(Context context, List<String> userArrayList) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mUserPhotosList = userArrayList;
    }

    @Override
    public int getItemsCount() {
        if(mUserPhotosList == null){
            return 0;
        }
        return mUserPhotosList.size();
    }

    @Override
    public View getItem(int i, View view, ViewGroup viewGroup) {
        View view1 = mInflater.inflate(R.layout.vertical_wheel_view,viewGroup,false);
        String photoUrl = mUserPhotosList.get(i);
        ImageView mainPhoto = (ImageView) view1.findViewById(R.id.mainImageView);
//        LinearLayout horizontalWheelLinearLayout = (LinearLayout) view1.findViewById(R.id.horizontalWheelView);
//        WheelView horizontalWheelView = new WheelView(mContext);

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
