package com.google.firebase.codelab.friendlychat.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daprlabs.cardstack.SwipeDeck;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.codelab.friendlychat.CodelabPreferences;
import com.google.firebase.codelab.friendlychat.FriendlyMessage;
import com.google.firebase.codelab.friendlychat.Interfaces;
import com.google.firebase.codelab.friendlychat.LocationController;
import com.google.firebase.codelab.friendlychat.MainActivity;
import com.google.firebase.codelab.friendlychat.MyFirebaseInstanceIdService;
import com.google.firebase.codelab.friendlychat.NotificationController;
import com.google.firebase.codelab.friendlychat.R;
import com.google.firebase.codelab.friendlychat.SignInActivity;
import com.google.firebase.codelab.friendlychat.StartMapEvent;
import com.google.firebase.codelab.friendlychat.wheel_controls.SwipeDeckAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zeevi on 5/5/2017.
 */

public class FragmentMain extends Fragment implements OnMapReadyCallback
        ,GoogleApiClient.OnConnectionFailedListener {
    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 0;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private AdView mAdView;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<FriendlyMessage, MainActivity.MessageViewHolder>
            mFirebaseAdapter;

    private Button meetingRequestLayoutButton;

    private FirebaseAnalytics mFirebaseAnalytics;
    private SupportMapFragment mSupportMapFragment;
    private boolean latFriendLocationChanged = false;
    private boolean lngFriendLocationChanged = false;

    private boolean isFriendPending;
    private boolean isFriendMatch;

    private boolean isInTalkZone;

    private double latOfFriend;
    private double lngOfFriend;
    private RelativeLayout unApprovedLayoutScreen;
    private LinearLayout approvedLayoutScreen;

    // private Ma

    private Marker friendMarker;

    private Marker myMarker;

    private int masgId = 0;
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
    private String friendId;
    LocationController locationController;
    private View mRootView;
    private Intent mIntent;
    private SwipeDeck cardStack;

    public void setIntent(Intent intent) {
        this.mIntent = intent;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.activity_main, container, false);
        initData();
        return mRootView;
    }

    private void initData(){

        approvedLayoutScreen = (LinearLayout)mRootView.findViewById(R.id.linearLayoutApprovedLayoutScreen) ;

        EventBus.getDefault().register(this);
        final NotificationController notificationController = new NotificationController(getActivity());
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();


        if(mIntent != null && mIntent.getStringExtra("intentType") != null && mIntent.getStringExtra("intentType").equals("cameFormMeetingActivity")){
            latOfFriend = mIntent.getDoubleExtra("latToGetBackTo", 0);
            lngOfFriend = mIntent.getDoubleExtra("lngToGetBackTo", 0);
            friendId = mIntent.getStringExtra("senderIdToGetBackToo");
            final DatabaseReference myRefPending = FirebaseDatabase.getInstance().getReference().child("usersNew").child(mFirebaseAuth.getCurrentUser().getUid()).child("pending");

            if(mIntent.getStringExtra("pending") != null &&mIntent.getStringExtra("pending").equals("pending")){
                isFriendPending = true;

                //////////////////////////////////



                ArrayList<String> profilePictures =  mIntent.getStringArrayListExtra("senderPictureList");
                final ArrayList<String> picurlsList = new ArrayList<>();
                picurlsList.add(mIntent.getStringExtra("senderProfilePicture"));

                if(profilePictures != null && profilePictures.size() > 0){
                    picurlsList.addAll(profilePictures);
                }


                unApprovedLayoutScreen = (RelativeLayout) mRootView.findViewById(R.id.RelativeLayoutUnApprovedLayoutScreen);
                unApprovedLayoutScreen.setVisibility(View.VISIBLE);
                approvedLayoutScreen.setVisibility(View.GONE);
                 cardStack = (SwipeDeck) mRootView.findViewById(R.id.swipe_deck);
                Button buttonSwipeLeft  = (Button) mRootView.findViewById(R.id.buttonSwipeLeft);
                Button buttonApproveUser = (Button) mRootView.findViewById(R.id.buttonApproveUser);
                Button buttonSwipeRight = (Button)mRootView.findViewById(R.id.buttonSwipeRight);


                final SwipeDeckAdapter adapter = new SwipeDeckAdapter(picurlsList, getActivity(),null);
                cardStack.setAdapter(adapter);
                cardStack.setHardwareAccelerationEnabled(true);
                cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
                    @Override
                    public void cardSwipedLeft(int position) {
                        Log.i("MainActivity", "card was swiped left, position in adapter: " + position);

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

                buttonApproveUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference myRefMatches = FirebaseDatabase.getInstance().getReference().child("usersNew").child(mFirebaseAuth.getCurrentUser().getUid()).child("matches");
                        DatabaseReference myFriendMatches = FirebaseDatabase.getInstance().getReference().child("usersNew").child(friendId).child("matches");
                        myRefMatches.keepSynced(true);
                        myRefPending.keepSynced(true);
                        myFriendMatches.keepSynced(true);
                        myRefPending.child(friendId).removeValue();
                        myRefMatches.child(friendId).child("userId").setValue(friendId);
                        myRefMatches.child(friendId).child("redDot").setValue("redDot");

                        myFriendMatches.child(mFirebaseAuth.getCurrentUser().getUid()).child("userId").setValue(mFirebaseAuth.getCurrentUser().getUid());
                        myFriendMatches.child(mFirebaseAuth.getCurrentUser().getUid()).child("redDot").setValue("redDot");
                        ArrayList<String> regIds = new ArrayList<String>();
                        regIds.add( mIntent.getStringExtra("senderTokenToGetBackToo"));
                        JSONArray regArray = new JSONArray(regIds);
                        notificationController.sendMessage(regArray, locationController.getLat() + "", locationController.getLng() + ":" + mFirebaseAuth.getCurrentUser().getUid(), null, "yesIWantToMeet");

                        isFriendPending = false;
                        isFriendMatch = true;

                        unApprovedLayoutScreen.setVisibility(View.GONE);
                        approvedLayoutScreen.setVisibility(View.VISIBLE);

                        initGoogleMap();
                    }
                });



            }else {
                isFriendMatch = true;
                initGoogleMap();
            }


        }


        mFirebaseDatabaseReference.child("usersNew").child(friendId).child("mLat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                latOfFriend = snapshot.getValue(double.class);
                latFriendLocationChanged = true;
                changeMarkerPosition();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mFirebaseDatabaseReference.child("usersNew").child(friendId).child("mLng").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                lngOfFriend = snapshot.getValue(double.class);
                lngFriendLocationChanged = true;
                changeMarkerPosition();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        startActivity(new Intent(this, SignInActivity.class));


        locationController = new LocationController(getActivity());
        locationController.setBasicListenerLocationChanged(basicListenerLocationChanged);

        //Init Pushwoosh fragment
//        PushFragment.init(this);
        mAdView = (AdView) mRootView.findViewById(R.id.adView);
        meetingRequestLayoutButton = (Button) mRootView.findViewById(R.id.meetingRequestLayoutButton);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        meetingRequestLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double lat = locationController.getLat();
                double lng = locationController.getLng();


                String nexus6p = "dSTD1uvHd60:APA91bHLdwLcFtmt-Ee6wEiaXSGpk7flxrD5UwNklH9uxBljYWli9X0bW1pRUOiE6fbCGD40yDqoj-xdgNsRVL-p7xvBQo0z9AF-BEDtguuhNhDMnP8-MsbNV1MqdzPQBVO9tNn4M37O";
                String nexusS = "dWswpCvgpyc:APA91bHdmJzphQgHeT1VvePeIhagqmltsjZ1yhQ_7FpIp-mL79fqzL8X87EiYOX7D7o7XddZ2VLe4Uo_QV8EQwe1yoOcyxYeYxYS8UjPLQm7S7KLyYYB81FobI5TunpAJCh6W1K-DEbw";
                String nexus5x = "c_eI-apYzKY:APA91bHzb4EEqDM2LmVaby08UF_ZH7GITl8utoL4rhwJgW76Ve5YSCb0qzOfJUQf7qnRcO3FselMT1Kz18BbafHIMoNcJL9UKCdZczO0yqyhkDQa8oXBe-WilO8GITw1jkcW7NiIkfEX";
                ArrayList<String> regIds = new ArrayList<String>();
                regIds.add(nexus5x);
                JSONArray regArray = new JSONArray(regIds);

                notificationController.sendMessage(regArray, lat + ":" + MyFirebaseInstanceIdService.DEVICE_TOKEN, lng + ":" + mFirebaseAuth.getCurrentUser().getUid(), null, "locationNotification");
            }
        });


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                MyFirebaseInstanceIdService mfs = new MyFirebaseInstanceIdService();
                mfs.onTokenRefresh();
            }
        });


        // Initialize Firebase Remote Config.
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

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
            startActivity(new Intent(getActivity(), SignInActivity.class));
            getActivity().finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        // Set default username is anonymous.
        mUsername = ANONYMOUS;

        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .addApi(AppInvite.API)
                .addApi(AppIndex.API).build();

        // Initialize ProgressBar and RecyclerView.
        mProgressBar = (ProgressBar) mRootView.findViewById(R.id.progressBar);
        mMessageRecyclerView = (RecyclerView) mRootView.findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        String myId = mFirebaseAuth.getCurrentUser().getUid();
        final String chatGroupName;

        if(friendId.compareTo(myId) > 0){
            chatGroupName = friendId +myId;
        }else{
            chatGroupName = myId + friendId;
        }

        mFirebaseAdapter = new FirebaseRecyclerAdapter<FriendlyMessage, MainActivity.MessageViewHolder>(
                FriendlyMessage.class,
                R.layout.item_message,
                MainActivity.MessageViewHolder.class,
                mFirebaseDatabaseReference.child(chatGroupName)) {

            @Override
            protected void populateViewHolder(MainActivity.MessageViewHolder viewHolder,
                                              FriendlyMessage friendlyMessage, int position) {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                viewHolder.messageTextView.setText(friendlyMessage.getText());
                viewHolder.messengerTextView.setText(friendlyMessage.getName());
                if (friendlyMessage.getPhotoUrl() == null) {
                    viewHolder.messengerImageView
                            .setImageDrawable(ContextCompat
                                    .getDrawable(getActivity(),
                                            R.drawable.ic_account_circle_black_36dp));
                } else {
                    Glide.with(getActivity())
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


        mMessageEditText = (EditText) mRootView.findViewById(R.id.messageEditText);
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mSharedPreferences
                .getInt(CodelabPreferences.FRIENDLY_MSG_LENGTH, DEFAULT_MSG_LENGTH_LIMIT))});
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isInTalkZone && charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        mSendButton = (Button) mRootView.findViewById(R.id.sendButton);
        mSendButton.setEnabled(false);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendlyMessage friendlyMessage = new
                        FriendlyMessage(mMessageEditText.getText().toString(),
                        mUsername,
                        mPhotoUrl);
                mFirebaseDatabaseReference.child(chatGroupName)
                        .push().setValue(friendlyMessage);
                mMessageEditText.setText("");

                DatabaseReference myFriendMatches = FirebaseDatabase.getInstance().getReference().child("usersNew").child(friendId).child("matches");
                myFriendMatches.child(mFirebaseAuth.getCurrentUser().getUid()).child("redDot").setValue("redDot");

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
        if(isFriendMatch) {
            mFirebaseDatabaseReference.child("usersNew").child(mFirebaseAuth.getCurrentUser().getUid()).child("matches").child(friendId).child("redDot").removeValue();
        }
        if(isFriendPending) {
            mFirebaseDatabaseReference.child("usersNew").child(mFirebaseAuth.getCurrentUser().getUid()).child("pending").child(friendId).child("redDot").removeValue();
        }
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
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                mSupportMapFragment = SupportMapFragment.newInstance();
                fragmentTransaction.add(R.id.frameLayoutfragmentClinicGoogleMapContainer,
                        mSupportMapFragment).commit();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSupportMapFragment.getMapAsync(FragmentMain.this);
                    }
                });
            }
        }.start();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == getActivity().RESULT_OK) {
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
        Toast.makeText(getActivity(), "Google Play Services error.", Toast.LENGTH_SHORT).show();
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


    private void changeMarkerPosition(){
        if(latFriendLocationChanged && lngFriendLocationChanged) {
            final LatLng locationOfFriend = new LatLng(latOfFriend, lngOfFriend);
            if(friendMarker!= null) {
                friendMarker.setPosition(locationOfFriend);
            }
            latFriendLocationChanged = false;
            lngFriendLocationChanged = false;

            float[] results = new float[1];
            Location.distanceBetween(latOfFriend, lngOfFriend,
                    locationController.getLat(), locationController.getLng(),
                    results);



            if(results[0] < 200){
                mSendButton.setEnabled(true);
                isInTalkZone = true;
            }else {
                mSendButton.setEnabled(false);
                isInTalkZone = false;
            }
        }


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


    private Interfaces.basicListener basicListenerLocationChanged = new Interfaces.basicListener() {
        @Override
        public void onSuccess() {
            double latOfMe = locationController.getLat();
            double lngOfMe = locationController.getLng();
            final LatLng myLocationLatLng = new LatLng(latOfMe, lngOfMe);
            if(myMarker!= null) {
                myMarker.setPosition(myLocationLatLng);
            }

            float[] results = new float[1];
            Location.distanceBetween(latOfFriend, lngOfFriend,
                    latOfMe, lngOfMe,
                    results);
            if(results[0] < 200){
                mSendButton.setEnabled(true);
                isInTalkZone = true;
            }else {
                mSendButton.setEnabled(false);
                isInTalkZone = false;
            }
        }


        @Override
        public void onError() {

        }
    };


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
                    double latOfFriendTalkZone = latOfFriend;//locationController.getLat();
                    double lngOfFriendTalkZone = lngOfFriend;//locationController.getLng();

                    double latOfMe = locationController.getLat();
                    double lngOfMe = locationController.getLng();

                    final LatLng locationOfFriend = new LatLng(latOfFriendTalkZone, lngOfFriendTalkZone);
                    final LatLng locationOfMe = new LatLng(latOfMe, lngOfMe);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            friendMarker = aGoogleMap.addMarker(new MarkerOptions()
                                    .position(locationOfFriend)
                                    .title("talk zone")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                            myMarker = aGoogleMap.addMarker(new MarkerOptions()
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

                            if(results[0] < 200){
                                getActivity().runOnUiThread(new Runnable() {
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

//private SwipeDeckAdapter.OnSwipeCardListener onSwipeCardListener = new SwipeDeckAdapter.OnSwipeCardListener() {
//    @Override
//    public void swipeLeft() {
//        cardStack.swipeTopCardLeft(200);
//    }
//
//    @Override
//    public void swipeRight() {
//        cardStack.swipeTopCardRight(200);
//    }
//};

}
