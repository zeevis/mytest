/**
 * Copyright Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.firebase.codelab.friendlychat;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;


import org.json.JSONArray;
import org.w3c.dom.Comment;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback
        ,GoogleApiClient.OnConnectionFailedListener {
    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 0;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private AdView mAdView;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>
            mFirebaseAdapter;

    private Button meetingRequestLayoutButton;

    private FirebaseAnalytics mFirebaseAnalytics;
    private SupportMapFragment mSupportMapFragment;

    double latOfFriend;
    double lngOfFriend;

    private Ma


    private int masgId = 0;

//
//    @Override
//    public void doOnRegistered(String registrationId)
//    {
//        //mGeneralStatus.setText(registrationId);
//    }
//
//    @Override
//    public void doOnRegisteredError(String errorId)
//    {
//        //mGeneralStatus.setText(errorId);
//    }
//
//    @Override
//    public void doOnUnregistered(final String message)
//    {
//       // mGeneralStatus.setText(message);
//    }
//
//    @Override
//    public void doOnUnregisteredError(String errorId)
//    {
//       // mGeneralStatus.setText(errorId);
//    }
//
//    @Override
//    public void doOnMessageReceive(String message)
//    {
//       // mGeneralStatus.setText(message);
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(StartMapEvent event) {

        latOfFriend = event.lat;
        lngOfFriend = event.lng;
        initGoogleMap();
    }


    @Override
    public void onMapReady(final GoogleMap aGoogleMap) {

        if (aGoogleMap != null) {

            new Thread() {
                @Override
                public void run() {
                   // Geocoder geocoder = new Geocoder(getApplicationContext());
//                    List<Address> addresses = null;
//                    try {
//                        addresses = geocoder.getFromLocationName(mClinic.getAddress(), 1);
//                    } catch (IOException aE) {
//                        Log.d(TAG, aE.getMessage());
//                    }
//                    if (addresses.size() > 0) {
//                        mLatitude = addresses.get(0).getLatitude();
//                        mLongitude = addresses.get(0).getLongitude();
//                    }


//                    double lat = locationController.getLat();
//                    double lng = locationController.getLng();
                    double latOfFriendTalkZone = latOfFriend;//locationController.getLat();
                    double lngOfFriendTalkZone = lngOfFriend;//locationController.getLng();

                    double latOfMe = locationController.getLat();
                    double lngOfMe = locationController.getLng();

                    final LatLng locationOfFriend = new LatLng(latOfFriendTalkZone, lngOfFriendTalkZone);
                    final LatLng locationOfMe = new LatLng(latOfMe, lngOfMe);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Marker friendMarker = aGoogleMap.addMarker(new MarkerOptions()
                                    .position(locationOfFriend)
                                    .title("talk zone")
                                    .icon(BitmapDescriptorFactory.defaultMarker(210.0f)));

                            Marker myMarker = aGoogleMap.addMarker(new MarkerOptions()
                                    .position(locationOfMe)
                                    .title("me")
                                    .icon(BitmapDescriptorFactory.defaultMarker(210.0f)));
                            ArrayList<Marker> markerArrayList = new ArrayList<Marker>();
                            markerArrayList.add(friendMarker);
                            markerArrayList.add(myMarker);

                            float[] results = new float[1];
                            Location.distanceBetween(locationOfFriend.latitude, locationOfFriend.longitude,
                                    locationOfMe.latitude, locationOfMe.longitude,
                                    results);
                            aGoogleMap.getUiSettings().setAllGesturesEnabled(true );

                            if(results[1] < 200){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mSendButton.setEnabled(true);

                                    }
                                });
                            }

                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            for (Marker marker : markerArrayList) {
                                builder.include(marker.getPosition());
                            }
                            LatLngBounds bounds = builder.build();

                            int padding = 0; // offset from edges of the map in pixels
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);


                           // CameraPosition cameraPosition = new CameraPosition.Builder().target(locationOfFriend).zoom(15.0f).build();
                           // CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                            aGoogleMap.animateCamera(cameraUpdate);
                        }
                    });

                }
            }.start();


        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mGoogleApiClient, getIndexApiAction());
        mGoogleApiClient.disconnect();
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

    private static final String TAG = "MainActivity";
    public static final String MESSAGES_CHILD = "messages";
    private static final int REQUEST_INVITE = 1;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 10;
    public static final String ANONYMOUS = "anonymous";
    private static final String MESSAGE_SENT_EVENT = "message_sent";
    private String mUsername;
    private String mPhotoUrl;
    private SharedPreferences mSharedPreferences;
    private GoogleApiClient mGoogleApiClient;

    private Button mSendButton;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar mProgressBar;
    private EditText mMessageEditText;
    LocationController locationController;

    // Firebase instance variables

//    @Override
//    public void onNewIntent(Intent intent)
//    {
//        super.onNewIntent(intent);
//
//        //Check if we've got new intent with a push notification
////        PushFragment.onNewIntent(this, intent);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);

        Intent intent = getIntent();
        if(intent != null && intent.getStringExtra("intentType") != null && intent.getStringExtra("intentType").equals("cameFormMeetingActivity")){
            latOfFriend = intent.getDoubleExtra("latToGetBackTo",0);
            lngOfFriend = intent.getDoubleExtra("lngToGetBackTo",0);
            initGoogleMap();
        }



//        startActivity(new Intent(this, SignInActivity.class));


        locationController = new LocationController(this);

        //Init Pushwoosh fragment
//        PushFragment.init(this);
        mAdView = (AdView) findViewById(R.id.adView);
        meetingRequestLayoutButton = (Button) findViewById(R.id.meetingRequestLayoutButton);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        meetingRequestLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double lat = locationController.getLat();
                double lng = locationController.getLng();

                NotificationController notificationController = new NotificationController(MainActivity.this);
                String nexus6p = "dSTD1uvHd60:APA91bHLdwLcFtmt-Ee6wEiaXSGpk7flxrD5UwNklH9uxBljYWli9X0bW1pRUOiE6fbCGD40yDqoj-xdgNsRVL-p7xvBQo0z9AF-BEDtguuhNhDMnP8-MsbNV1MqdzPQBVO9tNn4M37O";
                String nexusS = "dWswpCvgpyc:APA91bHdmJzphQgHeT1VvePeIhagqmltsjZ1yhQ_7FpIp-mL79fqzL8X87EiYOX7D7o7XddZ2VLe4Uo_QV8EQwe1yoOcyxYeYxYS8UjPLQm7S7KLyYYB81FobI5TunpAJCh6W1K-DEbw";
                String nexus5x = "c_eI-apYzKY:APA91bHzb4EEqDM2LmVaby08UF_ZH7GITl8utoL4rhwJgW76Ve5YSCb0qzOfJUQf7qnRcO3FselMT1Kz18BbafHIMoNcJL9UKCdZczO0yqyhkDQa8oXBe-WilO8GITw1jkcW7NiIkfEX";
                ArrayList<String> regIds = new ArrayList<String>();
                regIds.add(nexus5x);
                JSONArray regArray = new JSONArray(regIds);

                notificationController.sendMessage(regArray, lat + ":" + MyFirebaseInstanceIdService.DEVICE_TOKEN, lng + "", null, "locationNotification");
            }
        });


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                MyFirebaseInstanceIdService mfs = new MyFirebaseInstanceIdService();
                mfs.onTokenRefresh();
            }
        });

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();


        ///////////////////////////////////////


//
//        mFirebaseAuth.signOut();
//        mUsername = ANONYMOUS;


        /////////////////////////////////

        // Initialize Firebase Remote Config.
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

// Define Firebase Remote Config Settings.
        FirebaseRemoteConfigSettings firebaseRemoteConfigSettings =
                new FirebaseRemoteConfigSettings.Builder()
                        .setDeveloperModeEnabled(true)
                        .build();

// Define default config values. Defaults are used when fetched config values are not
// available. Eg: if an error occurred fetching values from the server.
        Map<String, Object> defaultConfigMap = new HashMap<>();
        defaultConfigMap.put("friendly_msg_length", 10L);

// Apply config settings and default values.
        mFirebaseRemoteConfig.setConfigSettings(firebaseRemoteConfigSettings);
        mFirebaseRemoteConfig.setDefaults(defaultConfigMap);

// Fetch remote config.
        fetchConfig();


        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Set default username is anonymous.
        mUsername = ANONYMOUS;

        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .addApi(AppInvite.API)
                .addApi(AppIndex.API).build();

        // Initialize ProgressBar and RecyclerView.
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        // New child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>(
                FriendlyMessage.class,
                R.layout.item_message,
                MessageViewHolder.class,
                mFirebaseDatabaseReference.child(MESSAGES_CHILD)) {

            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder,
                                              FriendlyMessage friendlyMessage, int position) {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                viewHolder.messageTextView.setText(friendlyMessage.getText());
                viewHolder.messengerTextView.setText(friendlyMessage.getName());
                if (friendlyMessage.getPhotoUrl() == null) {
                    viewHolder.messengerImageView
                            .setImageDrawable(ContextCompat
                                    .getDrawable(MainActivity.this,
                                            R.drawable.ic_account_circle_black_36dp));
                } else {
                    Glide.with(MainActivity.this)
                            .load(friendlyMessage.getPhotoUrl())
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


        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mSharedPreferences
                .getInt(CodelabPreferences.FRIENDLY_MSG_LENGTH, DEFAULT_MSG_LENGTH_LIMIT))});
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        mSendButton = (Button) findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendlyMessage friendlyMessage = new
                        FriendlyMessage(mMessageEditText.getText().toString(),
                        mUsername,
                        mPhotoUrl);
                mFirebaseDatabaseReference.child(MESSAGES_CHILD)
                        .push().setValue(friendlyMessage);
                mMessageEditText.setText("");


//                ValueEventListener postListener = new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        // Get Post object and use the values to update the UI
//                       // Post post = dataSnapshot.getValue(Post.class);
//                        // ...
//                        int i = 9;
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        // Getting Post failed, log a message
//                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//                        // ...
//                    }
//                };

                // mFirebaseDatabaseReference.child("usertest").addValueEventListener(postListener);
//                mFirebaseDatabaseReference.child("usertest")
//                        .push().setValue(friendlyMessage);
//                mMessageEditText.setText("");


                //final String uid = getUid();
                FirebaseDatabase.getInstance().getReference().child("users")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Get user information
                                User user = dataSnapshot.getValue(User.class);
                                String authorName = user.username;

//                                // Create new comment object
//                                String commentText = mCommentField.getText().toString();
//                                Comment comment = new Comment(uid, authorName, commentText);
//
//                                // Push the comment, it will appear in the list
//                                mCommentsReference.push().setValue(comment);
//
//                                // Clear the field
//                                mCommentField.setText(null);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


//                FirebaseMessaging fm = FirebaseMessaging.getInstance();
//                String to = "dSTD1uvHd60:APA91bHLdwLcFtmt-Ee6wEiaXSGpk7flxrD5UwNklH9uxBljYWli9X0bW1pRUOiE6fbCGD40yDqoj-xdgNsRVL-p7xvBQo0z9AF-BEDtguuhNhDMnP8-MsbNV1MqdzPQBVO9tNn4M37O"; // the notification key
//                AtomicInteger msgId = new AtomicInteger();
//                fm.send(new RemoteMessage.Builder(to)
//                        .setMessageId(msgId.toString())
//                        .addData("hello", "world")
//                        .build());


                NotificationController notificationController = new NotificationController(MainActivity.this);
                String nexus6p = "dSTD1uvHd60:APA91bHLdwLcFtmt-Ee6wEiaXSGpk7flxrD5UwNklH9uxBljYWli9X0bW1pRUOiE6fbCGD40yDqoj-xdgNsRVL-p7xvBQo0z9AF-BEDtguuhNhDMnP8-MsbNV1MqdzPQBVO9tNn4M37O";
                String nexusS = "dWswpCvgpyc:APA91bHdmJzphQgHeT1VvePeIhagqmltsjZ1yhQ_7FpIp-mL79fqzL8X87EiYOX7D7o7XddZ2VLe4Uo_QV8EQwe1yoOcyxYeYxYS8UjPLQm7S7KLyYYB81FobI5TunpAJCh6W1K-DEbw";
                ArrayList<String> regIds = new ArrayList<String>();
                regIds.add(nexus6p);
                JSONArray regArray = new JSONArray(regIds);

                notificationController.sendMessage(regArray, "hhhhhh", "ooooooo", null, "ma  kore?");

//               mFirebaseDatabaseReference.child("users").push().addValueEventListener(postListener).setValue(friendlyMessage);
//                mMessageEditText.setText("");


//                URL url = null;
//                try {
//                    url = new URL("https://fcm.googleapis.com/fcm/send\n");
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
//                HttpURLConnection client = null;
//                try {
//                    client = (HttpURLConnection) url.openConnection();
//                    client.setRequestMethod("POST");
//                    client.setRequestProperty("Key","Value");
//                    client.setDoOutput(true);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

//                RemoteMessage.Builder remoteMessage = new RemoteMessage.Builder(""+"@gcm.googleapis.com");
//                remoteMessage.ge
//
//                FirebaseMessaging fm = FirebaseMessaging.getInstance();
//                fm.subscribeToTopic("friendly_engage");
//                fm.send(new RemoteMessage.Builder("c68YiUHO8so:APA91bEOsLwBTtCN995SBsD8XEU4pV0kud6tZ4HD7TxyEN_2i9qd37c9E9WcTW2Ptv16Z8HtqHIypFmlelYXzE_Vj5UjMjxgAKRX1rRDYn9UYcZBor7eLmaGY9Ziyen173kicSZzTpRB" + "@gcm.googleapis.com")
//                        .setMessageId(Integer.toString(masgId++))
//                        .addData("my_message", "Hello World")
//                        .addData("my_action","SAY_HELLO")
//                        .setTtl(86400)
//                        .addData("to","d1ahsXr7rmc:APA91bFq2UO1AW_ZGzw3NEU76h9eQO43wvo6WIGqc0d79QbWRS1htvmXnYm_ldbdIIuTjEObV6egGkMcBsAaig-7E0V3UPBtKzdj4xU3jIkafqGsnM9qSVeMxe9xbffyLkyGokwvHffD")
//                        .build());


            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();
        // Check if user is signed in.
        // TODO: Add code to check if user is signed in.
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(mGoogleApiClient, getIndexApiAction());
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private void sendInvitation() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    private void initGoogleMap() {
        new Thread() {
            @Override
            public void run() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                mSupportMapFragment = SupportMapFragment.newInstance();
                fragmentTransaction.add(R.id.frameLayoutfragmentClinicGoogleMapContainer,
                        mSupportMapFragment).commit();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSupportMapFragment.getMapAsync(MainActivity.this);
                    }
                });
            }
        }.start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                Bundle payload = new Bundle();
                payload.putString(FirebaseAnalytics.Param.VALUE, "sent");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE,
                        payload);
                // Check how many invitations were sent and log.
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode,
                        data);
                Log.d(TAG, "Invitations sent: " + ids.length);
            } else {
                Bundle payload = new Bundle();
                payload.putString(FirebaseAnalytics.Param.VALUE, "not sent");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE,
                        payload);
                // Sending failed or it was canceled, show failure message to
                // the user
                Log.d(TAG, "Failed to send invitation.");
            }
        }
    }




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    // Fetch the config to determine the allowed length of messages.
    public void fetchConfig() {
        long cacheExpiration = 3600; // 1 hour in seconds
        // If developer mode is enabled reduce cacheExpiration to 0 so that
        // each fetch goes to the server. This should not be used in release
        // builds.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings()
                .isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Make the fetched config available via
                        // FirebaseRemoteConfig get<type> calls.
                        mFirebaseRemoteConfig.activateFetched();
                        applyRetrievedLengthLimit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // There has been an error fetching the config
                        Log.w(TAG, "Error fetching config: " +
                                e.getMessage());
                        applyRetrievedLengthLimit();
                    }
                });
    }


    /**
     * Apply retrieved length limit to edit text field.
     * This result may be fresh from the server or it may be from cached
     * values.
     */
    private void applyRetrievedLengthLimit() {
        Long friendly_msg_length =
                mFirebaseRemoteConfig.getLong("friendly_msg_length");
//        mMessageEditText.setFilters(new InputFilter[]{new
//                InputFilter.LengthFilter(friendly_msg_length.intValue())});
        Log.d(TAG, "FML is: " + friendly_msg_length);
    }


    private void causeCrash() {
        throw new NullPointerException("Fake null pointer exception");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.crash_menu:
                FirebaseCrash.logcat(Log.ERROR, TAG, "crash caused");
                causeCrash();
                return true;
            case R.id.invite_menu:
                sendInvitation();
                return true;
            case R.id.fresh_config_menu:
                fetchConfig();
                return true;
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                mUsername = ANONYMOUS;
                startActivity(new Intent(this, SignInActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
