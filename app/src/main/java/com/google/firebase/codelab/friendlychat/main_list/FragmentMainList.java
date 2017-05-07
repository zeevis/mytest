package com.google.firebase.codelab.friendlychat.main_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.codelab.friendlychat.LocationController;
import com.google.firebase.codelab.friendlychat.MainListActivity;
import com.google.firebase.codelab.friendlychat.MyFirebaseInstanceIdService;
import com.google.firebase.codelab.friendlychat.R;
import com.google.firebase.codelab.friendlychat.User;
import com.google.firebase.codelab.friendlychat.wheel_controls.CustomeArrayWheelAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zeevi on 5/5/2017.
 */

public class FragmentMainList extends Fragment {
    private LinearLayoutManager mLinearLayoutManager;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<User,MessageViewHolder>
            mFirebaseAdapter;
    private FirebaseAuth mFirebaseAuth;
    //  private RecyclerView mMessageRecyclerView;
    private ArrayList<User> userArrayList;
    private LocationController locationController;
    private com.wx.wheelview.widget.WheelView mWheelViewWX;
    private View mRootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.activity_main_list, container, false);
        initData();
        return mRootView;
    }

    private void initData() {
        mWheelViewWX = (com.wx.wheelview.widget.WheelView)mRootView.findViewById(R.id.wheelViewMainListActivity);
        mWheelViewWX.setSkin(WheelView.Skin.Common);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                MyFirebaseInstanceIdService mfs = new MyFirebaseInstanceIdService();
                mfs.onTokenRefresh();

            }
        });
        locationController = new LocationController(getActivity());
        userArrayList = new ArrayList<>();

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();

        //////////////////////////wheel view///////////////////////////////////////////////
        DatabaseReference databaseReferenceUsers = mFirebaseDatabaseReference.child("usersNew");

        databaseReferenceUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userArrayList = new ArrayList<User>();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    User user = new User(postSnapshot.child("mUserId").getValue(String.class),postSnapshot.child("mUserGivenName").getValue(String.class),postSnapshot.hasChild("mEmail")?postSnapshot.child("mEmail").getValue(String.class):"",postSnapshot.child("mUserFamilyName").getValue(String.class),postSnapshot.child("mUserDisplayName").getValue(String.class),postSnapshot.child("userKeyToken").getValue(String.class),postSnapshot.child("mUserPhotoUrl").getValue(String.class),postSnapshot.child("mLat").getValue(double.class),postSnapshot.child("mLng").getValue(double.class),postSnapshot.child("mUserPhotoUrlHighQuality").getValue(String.class));
                    //  user =  postSnapshot.getValue(User.class);
                    if(postSnapshot.child("profilePic").getValue()!= null &&postSnapshot.child("profilePic").getValue() instanceof List){
                        List<String> picList =  (List<String>) postSnapshot.child("profilePic").getValue();
                        user.setProfilePic(picList);
                    }else{
                        List<String> onePicList = new ArrayList<>();
                        HashMap<String,String> userPicturesMap =  ((HashMap<String,String>)postSnapshot.child("profilePic").getValue());
                        if(userPicturesMap != null){
                            Collection<String> userPicturesList =  ((HashMap<String,String>)postSnapshot.child("profilePic").getValue()).values() ;
                            onePicList.addAll(userPicturesList);
                            user.setProfilePic(onePicList);
                        }

                    }

                    userArrayList.add(user);
                }

                mWheelViewWX.setWheelAdapter(new CustomeArrayWheelAdapter(getActivity(),userArrayList)); // 文本数据源
                mWheelViewWX.setWheelData(userArrayList);
                mWheelViewWX.setLoop(true);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ////////////////////////////////whellview end////////////////////////////////////////////

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setStackFromEnd(true);
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
}
