package com.google.firebase.codelab.friendlychat;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;

/**
 * Created by ideomobile on 10/24/16.
 */

@IgnoreExtraProperties
public class User {

    public String mUsername;
    public String mEmail;
    public String mUserKeyToken;

    public String mUserId;
    public String mUserGivenName;
    public String mUserFamilyName;
    public String mUserDisplayName;
    public String mUserPhotoUrl;

    public double mLat;
    public double mLng;

    public User(String userId, String userGivenName , String email, String userFamilyName, String userDisplayName, String userKeyToken, String userPhotoUrl, double lat, double lng ) {
       // this.mUsername = uername;
        this.mEmail = email;
        this.mUserKeyToken = userKeyToken;
        this.mUserId = userId;
        this.mUserGivenName = userGivenName;
        this.mUserFamilyName = userFamilyName;
        this.mUserDisplayName = userDisplayName;
        this.mUserPhotoUrl = userPhotoUrl;
        this.mLat = lat;
        this.mLng = lng;
    }

    public String getmUsername() {
        return mUsername;
    }

    public String getmEmail() {
        return mEmail;
    }

    public String getmUserKeyToken() {
        return mUserKeyToken;
    }

    public String getmUserId() {
        return mUserId;
    }

    public String getmUserGivenName() {
        return mUserGivenName;
    }

    public String getmUserFamilyName() {
        return mUserFamilyName;
    }

    public String getmUserDisplayName() {
        return mUserDisplayName;
    }

    public String getmUserPhotoUrl() {
        return mUserPhotoUrl;
    }

    public double getmLat() {
        return mLat;
    }

    public double getmLng() {
        return mLng;
    }

    public void writeNewUser(String userId, String userGivenName , String email, String userFamilyName, String userDisplayName, String userKeyToken, String userPhotoUrl, int lat, int lng ){
        mUserId = userId;
        mUserGivenName = userGivenName;
        mEmail = email;
        mUserFamilyName = userFamilyName;
        mUserDisplayName = userDisplayName;
        mUserPhotoUrl = userPhotoUrl;
        mUserKeyToken = userKeyToken;
        mLat = lat;
        mLng = lng;
    }

    public String getUserKeyToken() {
        return mUserKeyToken;
    }

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userKeyToken, String username, String email) {
        this.mUserKeyToken = userKeyToken;
        this.mUsername = username;
        this.mEmail = email;
    }

//    public void getValue(){
//        DataSnapshot dataSnapshot = new DataSnapshot();
//        DataSnapshot.getValue(User.class);
//
//    }

//    public User(String username, String email) {
//        this.username = username;
//        this.email = email;
//    }

}