package com.google.firebase.codelab.friendlychat.wheel_controls;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.codelab.friendlychat.R;
import com.google.firebase.codelab.friendlychat.User;
import com.wx.wheelview.adapter.BaseWheelAdapter;

import java.util.ArrayList;

/**
 * Created by zeevi on 4/22/2017.
 */

public class CustomeArrayWheelAdapter extends BaseWheelAdapter<User> {
    private LayoutInflater mLayoutInflater;
    Context mContext;
    private ArrayList<User> mUserArrayList;
    public CustomeArrayWheelAdapter(Context context,ArrayList<User> aUserArrayList) {
        super();
        mContext = context;
        mUserArrayList = aUserArrayList;
    }




    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {

        View view;
        if(convertView == null) {

            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view =  mLayoutInflater
                    .inflate(R.layout.whhel_base_layout, parent, false);
        }
        else {
            view = convertView;
        }
        ArrayList<String> profilePictures =  (ArrayList<String>) mUserArrayList.get(position).getProfilePic();
        ArrayList<String> picurlsList = new ArrayList<>();
        picurlsList.add(mUserArrayList.get(position).getmUserPhotoUrl());

        if(profilePictures != null && profilePictures.size() > 0){
            picurlsList.addAll(profilePictures);
        }

        ViewPager viewPager = (ViewPager)view. findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(mContext,picurlsList));

        return view;
    }


}
