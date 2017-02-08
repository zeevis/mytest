package com.google.firebase.codelab.friendlychat;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
//import com.wx.wheelview.adapter.ArrayWheelAdapter;
//import com.wx.wheelview.widget.WheelView;

import org.json.JSONArray;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import kankan.wheel.widget.WheelView;

public class MainListActivity extends AppCompatActivity {
    private LinearLayoutManager mLinearLayoutManager;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<User, MessageViewHolder>
            mFirebaseAdapter;
    private FirebaseAuth mFirebaseAuth;
    private RecyclerView mMessageRecyclerView;
    private ArrayList<User> userArrayList;
    private LocationController locationController;
   // private  kankan.wheel.widget.WheelView wheelView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        //wheelView= (WheelView)findViewById(R.id.wheelViewUsers);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        //WheelView hour;//
//        WheelView wheelView = (WheelView) findViewById(R.id.wheelview);
//        wheelView.setWheelAdapter(new ArrayWheelAdapter(this)); // 文本数据源
//        wheelView.setSkin(WheelView.Skin.Common); // common皮肤
////        wheelView.setWheelData( );  // 数据集合
//
//        wheelView.setWheelAdapter(new ArrayWheelAdapter(this){
//
//        });

        // Add code to print out the key hash
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.google.firebase.codelab.friendlychat",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                MyFirebaseInstanceIdService mfs = new MyFirebaseInstanceIdService();
                mfs.onTokenRefresh();

            }
        });
        locationController = new LocationController(this);
        userArrayList = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();




        //////////////////////////wheel view///////////////////////////////////////////////




        DatabaseReference databaseReferenceUsers = mFirebaseDatabaseReference.child("usersNew");

        databaseReferenceUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                     User user =  postSnapshot.getValue(User.class);
                    userArrayList.add(user);
                }
                //wheelView.setViewAdapter(new MyArrayWheelAdapter(MainListActivity.this,userArrayList));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });










        ////////////////////////////////whellview end////////////////////////////////////////////






























     //  AppBaseDetails.getInstance().setAccount(mFirebaseAuth.getCurrentUser().);//llllllllllllllllllllllllllllll
        //in every chatopens 2 people group infirebase one of children should be the location of both users if changes both observe


        /////////////////////////////////////////////////////

     //                 2               2    1/2
        // (  ( 10 - y )   +  ( 10 - x )   )          <       200

//dist = arccos(sin(lat1) · sin(lat2) + cos(lat1) · cos(lat2) · cos(lon1 - lon2)) · R
        // Initialize ProgressBar and RecyclerView.
       // mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.usersRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        // New child entries

        mFirebaseAdapter = new FirebaseRecyclerAdapter<User, MessageViewHolder>(
                User.class,
                R.layout.item_message,
                MessageViewHolder.class,
                mFirebaseDatabaseReference.child("usersNew")) {

            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder,
                                              final User user, int position) {
               // mProgressBar.setVisibility(ProgressBar.INVISIBLE);

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Intent intent = new Intent(MainListActivity.this,MainActivity.class);
//                        intent.putExtra("userid",user.getmUserId());
//                        intent.putExtra("usertoken",user.getmUserKeyToken());
//                        intent.putExtra("userlat",user.getmLat());
//                        intent.putExtra("userlng",user.getmLng());
//                        startActivity(intent);
                        double lat = locationController.getLat();
                        double lng = locationController.getLng();

                        NotificationController notificationController = new NotificationController(MainListActivity.this);
                       // String nexus6p = "dSTD1uvHd60:APA91bHLdwLcFtmt-Ee6wEiaXSGpk7flxrD5UwNklH9uxBljYWli9X0bW1pRUOiE6fbCGD40yDqoj-xdgNsRVL-p7xvBQo0z9AF-BEDtguuhNhDMnP8-MsbNV1MqdzPQBVO9tNn4M37O";
                       // String nexusS = "dWswpCvgpyc:APA91bHdmJzphQgHeT1VvePeIhagqmltsjZ1yhQ_7FpIp-mL79fqzL8X87EiYOX7D7o7XddZ2VLe4Uo_QV8EQwe1yoOcyxYeYxYS8UjPLQm7S7KLyYYB81FobI5TunpAJCh6W1K-DEbw";
                       // String nexus5x = "c_eI-apYzKY:APA91bHzb4EEqDM2LmVaby08UF_ZH7GITl8utoL4rhwJgW76Ve5YSCb0qzOfJUQf7qnRcO3FselMT1Kz18BbafHIMoNcJL9UKCdZczO0yqyhkDQa8oXBe-WilO8GITw1jkcW7NiIkfEX";
                        ArrayList<String> regIds = new ArrayList<String>();
                        regIds.add(user.getmUserKeyToken());
                        JSONArray regArray = new JSONArray(regIds);
                        notificationController.sendMessage(regArray, lat + ":" + MyFirebaseInstanceIdService.DEVICE_TOKEN, lng + ":" +mFirebaseAuth.getCurrentUser().getUid(), null, "locationNotification");

                    }
                });

                //userArrayList.add(user);
                viewHolder.messageTextView.setText(user.getmEmail());
                viewHolder.messengerTextView.setText(user.getmUserDisplayName());
                if (user.getmUserPhotoUrl() == null) {
                    viewHolder.messengerImageView
                            .setImageDrawable(ContextCompat
                                    .getDrawable(MainListActivity.this,
                                            R.drawable.ic_account_circle_black_36dp));
                } else {
                    Glide.with(MainListActivity.this)
                            .load(user.getmUserPhotoUrl())
                            .into(viewHolder.messengerImageView);
                }
            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);







        ///////////////////////////////////////////////////











        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public TextView messengerTextView;
        public CircleImageView messengerImageView;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.crash_menu:
//                FirebaseCrash.logcat(Log.ERROR, TAG, "crash caused");
//                causeCrash();
                return true;
            case R.id.invite_menu:
                //sendInvitation();
                //return true;
            case R.id.fresh_config_menu:
                //fetchConfig();
               // return true;
            case R.id.matches_screen_menu:
                startActivity(new Intent(this, MatchesActivity.class));
                return true;
            case R.id.profile_page_menu:
                startActivity(new Intent(this, ProphileActivity.class));
                return true;
            case R.id.settings_page:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
               // mUsername = ANONYMOUS;
                startActivity(new Intent(this, SignInActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
