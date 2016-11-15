//package com.google.firebase.codelab.friendlychat;
//
//import android.util.Log;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.Exclude;
//import com.google.firebase.database.IgnoreExtraProperties;
//import com.google.firebase.database.MutableData;
//import com.google.firebase.database.Transaction;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;
//
///**
// * Created by ideomobile on 10/24/16.
// */
//
//@IgnoreExtraProperties
//public class Post {
//
//    public String uid;
//    public String author;
//    public String title;
//    public String body;
//    public int starCount = 0;
//    public Map<String, Boolean> stars = new HashMap<>();
//
//    public Post() {
//        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
//    }
//
//    public Post(String uid, String author, String title, String body) {
//        this.uid = uid;
//        this.author = author;
//        this.title = title;
//        this.body = body;
//    }
//
//    @Exclude
//    public Map<String, Object> toMap() {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("uid", uid);
//        result.put("author", author);
//        result.put("title", title);
//        result.put("body", body);
//        result.put("starCount", starCount);
//        result.put("stars", stars);
//
//        return result;
//    }
//
//
//    private void writeNewPost(String userId, String username, String title, String body) {
//        // Create new post at /user-posts/$userid/$postid and at
//        // /posts/$postid simultaneously
//        String key = mDatabase.child("posts").push().getKey();
//        Post post = new Post(userId, username, title, body);
//        Map<String, Object> postValues = post.toMap();
//
//        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put("/posts/" + key, postValues);
//        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);
//
//        mDatabase.updateChildren(childUpdates);
//    }
//
//    private void onStarClicked(DatabaseReference postRef) {
//        postRef.runTransaction(new Transaction.Handler() {
//            @Override
//            public Transaction.Result doTransaction(MutableData mutableData) {
//                Post p = mutableData.getValue(Post.class);
//                if (p == null) {
//                    return Transaction.success(mutableData);
//                }
//
//                if (p.stars.containsKey(getUid())) {
//                    // Unstar the post and remove self from stars
//                    p.starCount = p.starCount - 1;
//                    p.stars.remove(getUid());
//                } else {
//                    // Star the post and add self to stars
//                    p.starCount = p.starCount + 1;
//                    p.stars.put(getUid(), true);
//                }
//
//                // Set value and report transaction success
//                mutableData.setValue(p);
//                return Transaction.success(mutableData);
//            }
//
//            @Override
//            public void onComplete(DatabaseError databaseError, boolean b,
//                                   DataSnapshot dataSnapshot) {
//                // Transaction completed
//                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
//            }
//        });
//    }
//
//}