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

    public String username;
    public String email;
    public String userKeyToken;


    public String getUserKeyToken() {
        return userKeyToken;
    }

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userKeyToken, String username, String email) {
        this.userKeyToken = userKeyToken;
        this.username = username;
        this.email = email;
    }

//    public void getValue(){
//        DataSnapshot dataSnapshot = new DataSnapshot();
//        DataSnapshot.getValue(User.class);
//
//    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

}