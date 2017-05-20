package com.google.firebase.codelab.friendlychat.wheel_controls;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.daprlabs.cardstack.SwipeDeck;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.codelab.friendlychat.LocationController;
import com.google.firebase.codelab.friendlychat.MyFirebaseInstanceIdService;
import com.google.firebase.codelab.friendlychat.NotificationController;
import com.google.firebase.codelab.friendlychat.R;
import com.google.firebase.codelab.friendlychat.User;
import com.wx.wheelview.adapter.BaseWheelAdapter;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TimerTask;

/**
 * Created by zeevi on 4/22/2017.
 */

public class CustomeArrayWheelAdapter extends BaseWheelAdapter<User> {
    private LayoutInflater mLayoutInflater;
    Context mContext;
    private ArrayList<User> mUserArrayList;
    private LocationController mLocationController;

    public CustomeArrayWheelAdapter(Context context,ArrayList<User> aUserArrayList) {
        super();
        mContext = context;
        mUserArrayList = aUserArrayList;
        mLocationController = new LocationController(mContext);
    }




    @Override
    public View bindView(final int position, View convertView, ViewGroup parent) {

        View view;
        if(convertView == null) {

            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view =  mLayoutInflater
                    .inflate(R.layout.whhel_base_layout, parent, false);
        }
        else {
            view = convertView;
        }
        User currentUser = mUserArrayList.get(position);
        ArrayList<String> profilePictures =  (ArrayList<String>) currentUser.getProfilePic();
       final ArrayList<String> picurlsList = new ArrayList<>();
        String HqPROFILEpIC = currentUser.getmUserPhotoUrlHighQuality();
        if(HqPROFILEpIC != null && !HqPROFILEpIC.isEmpty()){
            picurlsList.add(currentUser.getmUserPhotoUrlHighQuality());
        }else{
            picurlsList.add(currentUser.getmUserPhotoUrl());
        }


        if(profilePictures != null && profilePictures.size() > 0){
            picurlsList.addAll(profilePictures);
        }

//        ViewPager viewPager = (ViewPager)view. findViewById(R.id.viewpager);
//        viewPager.setAdapter(new CustomPagerAdapter(mContext,picurlsList));

////////////////////////////////
        final SwipeDeck cardStack = (SwipeDeck) view.findViewById(R.id.swipe_deck);
        Button buttonSwipeLeft  = (Button) view.findViewById(R.id.buttonSwipeLeft);
        Button buttonMeetingRequest = (Button) view.findViewById(R.id.buttonMeetingRequest);
        Button buttonSwipeRight = (Button) view.findViewById(R.id.buttonSwipeRight);
        TextView mTextViewNameAge = (TextView) view.findViewById(R.id.textViewWheelLayoutNameAge);


        final SwipeDeckAdapter adapter = new SwipeDeckAdapter(picurlsList, mContext);
        cardStack.setAdapter(adapter);
        cardStack.setHardwareAccelerationEnabled(true);
        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                Log.i("MainActivity", "card was swiped left, position in adapter: " + position);
//                Collections.rotate(picurlsList, -1);
//             //  adapter.notifyDataSetChanged();
//                cardStack.setSelection(0);
            //    cardStack.setSelection((position + 1)%picurlsList.size());

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        Collections.rotate(picurlsList, -1);
                        adapter.notifyDataSetChanged();
                        cardStack.setSelection(0);
                    }
                }, 200);


            }

            @Override
            public void cardSwipedRight(int position) {
                Log.i("MainActivity", "card was swiped right, position in adapter: " + position);
                //Collections.rotate(picurlsList, -1);
                //adapter.notifyDataSetChanged();
           //     cardStack.setSelection((position + 1)%picurlsList.size());
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        Collections.rotate(picurlsList, -1);
                        adapter.notifyDataSetChanged();
                        cardStack.setSelection(0);
                    }
                }, 200);

            }

            @Override
            public void cardsDepleted() {
                Log.i("MainActivity", "no more cards");
             //cardStack.setSelection(0);

            }

            @Override
            public void cardActionDown() {
                Log.i("MainActivity", "action down");
            }

            @Override
            public void cardActionUp() {
                Log.i("MainActivity", "action up");
            }

        });


        ////////////////////////
        String age = currentUser.getmAge();
        boolean hasAge = age!= null && !age.isEmpty();
        String name = currentUser.getmUserGivenName();
        mTextViewNameAge.setText(hasAge?name + ", "+age:name);

        buttonSwipeLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardStack.swipeTopCardLeft(200);
            }
        });


        buttonSwipeRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardStack.swipeTopCardRight(200);
            }
        });

        buttonMeetingRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                double lat = mLocationController.getLat();
                double lng = mLocationController.getLng();

                NotificationController notificationController = new NotificationController(mContext);
                ArrayList<String> regIds = new ArrayList<String>();
                regIds.add(mUserArrayList.get(position).getmUserKeyToken());
                JSONArray regArray = new JSONArray(regIds);
                notificationController.sendMessage(regArray, lat + ":" + MyFirebaseInstanceIdService.DEVICE_TOKEN, lng + ":" + FirebaseAuth.getInstance().getCurrentUser().getUid(), null, "locationNotification");

            }
        });


        return view;
    }


}
