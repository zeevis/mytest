package com.google.firebase.codelab.friendlychat.splash;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.codelab.friendlychat.LocationController;
import com.google.firebase.codelab.friendlychat.MainListActivity;
import com.google.firebase.codelab.friendlychat.R;
import com.google.firebase.codelab.friendlychat.SignInActivity;
import com.google.firebase.codelab.friendlychat.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final String TAG = "SplashActivity";

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void updateUI(FirebaseUser currentUser){
        if(currentUser != null){
            LocationController locationController = new LocationController(SplashActivity.this);
            double lat = locationController.getLat();
            double lng = locationController.getLng();

            writeNewUserFacebook(lat,lng);
        }else{
            LoginManager.getInstance().logOut();
        }
    }

    private void writeNewUserFacebook(final double lat,final double lng) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userFaceBookId = FirebaseAuth.getInstance().getCurrentUser().getProviderData().get(1).getUid();
        String highQualityProfilePicture = "https://graph.facebook.com/" + userFaceBookId + "/picture?type=large&redirect=true&width=600&height=600";
        final User user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getDisplayName(), firebaseUser.getDisplayName(), FirebaseInstanceId.getInstance().getToken(), firebaseUser.getPhotoUrl().toString(), lat, lng, highQualityProfilePicture);

        mDatabase.child("usersNew").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(firebaseUser.getUid())) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("mLat", lat);
                    map.put("mLng", lng);
                    map.put("userKeyToken", user.getmUserKeyToken());
                    map.put("mUserPhotoUrlHighQuality", user.getmUserPhotoUrlHighQuality());
                    mDatabase.child("usersNew").child(user.getmUserId()).updateChildren(map);
                } else {
                    mDatabase.child("usersNew").child(user.getmUserId()).setValue(user);
                }

                startActivity(new Intent(SplashActivity.this, MainListActivity.class));
                SplashActivity.this.finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    }
