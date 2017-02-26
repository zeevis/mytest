package com.google.firebase.codelab.friendlychat;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kankan.wheel.widget.WheelView;
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
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

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
        LinearLayout horizontalWheelLinearLayout = (LinearLayout) view1.findViewById(R.id.horizontalWheelView);

      //  HorizontalScrollView horizontalScrollView =( HorizontalScrollView) view1.findViewById(R.id.horizontalScrollView);
        mPager = (ViewPager) view1.findViewById(R.id.pager);



        CirclePageIndicator indicator = (CirclePageIndicator)
                view1.findViewById(R.id.indicator);



        final float density = mContext.getResources().getDisplayMetrics().density;
       // WheelView horizontalWheelView = new WheelView(mContext);
        ArrayList<String> picurlsList;
        if(mUserArrayList.get(i).getProfilePic()== null || mUserArrayList.get(i).getProfilePic().size() == 0){
             picurlsList = new ArrayList<>();
             picurlsList.add(mUserArrayList.get(i).getmUserPhotoUrl());
        }else {
           picurlsList = (ArrayList<String>)mUserArrayList.get(i).getProfilePic();
        }

        SlidingImageAdapter adapter = new SlidingImageAdapter(mContext,picurlsList);
        mPager.setAdapter(adapter);
        indicator.setViewPager(mPager);
        mPager.setCurrentItem(adapter.getCount()-1);
        adapter.notifyDataSetChanged();



        //Set circle indicator radius
        indicator.setRadius(5 * density);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
//        Timer swipeTimer = new Timer();
//        swipeTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });



//        String photoUrl = mUserArrayList.get(i).getmUserPhotoUrl();
//        ImageView mainPhoto = (ImageView) view1.findViewById(R.id.mainImageView);
//        Glide.with(mContext)
//                .load(photoUrl)
//                .into(mainPhoto);

//        horizontalWheelView.setShadowColor(R.color.transparent,R.color.transparent,R.color.transparent);
//        horizontalWheelView.setDrawingCacheBackgroundColor(mContext.getResources().getColor(R.color.transparent));
//        // wheelView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_INSET);
//
//       // horizontalWheelView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        horizontalWheelView.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
//        horizontalWheelView.setViewAdapter(new MyHorizontalArrayWheelAdapter(mContext,picurlsList));
//        horizontalWheelLinearLayout.addView(horizontalWheelView);

//        if(picurlsList== null || picurlsList.size() == 0){
//             String photoUrl = mUserArrayList.get(i).getmUserPhotoUrl();
//             ImageView mainPhoto = (ImageView) view1.findViewById(R.id.mainImageView);
//                    Glide.with(mContext)
//                .load(photoUrl)
//                .into(mainPhoto);
//        }


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
