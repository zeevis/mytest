package com.google.firebase.codelab.friendlychat.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.codelab.friendlychat.R;
import com.google.firebase.codelab.friendlychat.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by zeevi on 5/9/2017.
 */

public class SettingsFragment extends Fragment {
    private RadioGroup myRadioSexGroup;
    private RadioGroup friendRadioSexGroup;
    private DatabaseReference mFirebaseDatabaseReference;
    private String myUserId;
    private TextView radiusTextView;
    private FirebaseAuth mFirebaseAuth;
    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.activity_settings, container, false);
        initData();
        return mRootView;
    }

    private void initData() {
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        myUserId = mFirebaseAuth.getCurrentUser().getUid();
        radiusTextView = (TextView) mRootView.findViewById(R.id.radiusTextView);


        myRadioSexGroup = (RadioGroup) mRootView.findViewById(R.id.myRadioSexGroup);
        friendRadioSexGroup = (RadioGroup) mRootView.findViewById(R.id.friendRadioSexGroup);
        myRadioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.myRadioMale:
                        mFirebaseDatabaseReference.child("usersNew").child(myUserId).child("sex").setValue("male");
                        break;
                    case R.id.myRadioFemale:
                        mFirebaseDatabaseReference.child("usersNew").child(myUserId).child("sex").setValue("female");
                        break;
                }
            }
        });

        friendRadioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.friendRadioMale:
                        mFirebaseDatabaseReference.child("usersNew").child(myUserId).child("lookingFor").setValue("male");
                        break;
                    case R.id.friendRadioFemale:
                        mFirebaseDatabaseReference.child("usersNew").child(myUserId).child("lookingFor").setValue("female");
                        break;
                    case R.id.friendRadioMaleAndFemale:
                        mFirebaseDatabaseReference.child("usersNew").child(myUserId).child("lookingFor").setValue("both");
                        break;

                }
            }
        });


        final SeekBar volControl = (SeekBar) mRootView.findViewById(R.id.distanceSeekBar);
        volControl.setMax(200);
        volControl.setProgress(0);
        volControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
                mFirebaseDatabaseReference.child("usersNew").child(myUserId).child("radius").setValue(arg0.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                //audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, arg1, 0);
                radiusTextView.setText(arg1 + "");
            }
        });

        DatabaseReference databaseReference = mFirebaseDatabaseReference.child("usersNew").child(myUserId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user.getSex() != null && user.getSex().equals("male")) {
                    myRadioSexGroup.check(R.id.myRadioMale);
                } else {
                    myRadioSexGroup.check(R.id.myRadioFemale);
                }
                if (user.getLookingFor() != null && user.getLookingFor().equals("male")) {
                    friendRadioSexGroup.check(R.id.friendRadioMale);
                } else {
                    friendRadioSexGroup.check(R.id.myRadioFemale);
                }
                volControl.setProgress(user.getRadius());
                user.getLookingFor();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}

