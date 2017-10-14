package com.google.firebase.codelab.friendlychat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zeevi on 10/12/2017.
 */

public class User1 extends User {

    public String mUsername;
    public String mEmail;
    public String mUserKeyToken;

    public String mUserId;
    public String mUserGivenName;
    public String mUserFamilyName;
    public String mUserDisplayName;
    public String mUserPhotoUrl;
    public String mUserPhotoUrlHighQuality;
    public String mAge;

    public String sex;
    public HashMap<String,String> profilePictures;
    public String lookingFor;
    public int radius;


    public double mLat;
    public double mLng;

    public User1(String userId, String userGivenName , String email, String userFamilyName, String userDisplayName, String userKeyToken, String userPhotoUrl, double lat, double lng ,String aUserPhotoUrlHighQuality) {
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
        this.mUserPhotoUrlHighQuality = aUserPhotoUrlHighQuality;
    }

    public ArrayList<String> getProfilePicFromMap() {
        return (ArrayList<String>)profilePictures.values();
    }
//
    public HashMap<String,String> getProfilePicMap() {
        return profilePictures;
    }


    public void setProfilePic(HashMap<String,String> profilePic) {
        this.profilePictures = (HashMap<String,String>) profilePic;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getSex() {
        return sex;
    }

    public String getLookingFor() {
        return lookingFor;
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

    public String getmAge() {
        return mAge;
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

    public User1() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User1(String userKeyToken, String username, String email) {
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

    public String getmUserPhotoUrlHighQuality() {
        return mUserPhotoUrlHighQuality;
    }

    public void setmUserPhotoUrlHighQuality(String mUserPhotoUrlHighQuality) {
        this.mUserPhotoUrlHighQuality = mUserPhotoUrlHighQuality;
    }
}