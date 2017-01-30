package com.google.firebase.codelab.friendlychat;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {


    private RadioGroup myRadioSexGroup;
    private RadioGroup friendRadioSexGroup;
    private DatabaseReference mFirebaseDatabaseReference;
    private String myUserId;
    private TextView radiusTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        myUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        radiusTextView = (TextView) findViewById(R.id.radiusTextView);


        myRadioSexGroup = (RadioGroup)findViewById(R.id.myRadioSexGroup);
        friendRadioSexGroup = (RadioGroup)findViewById(R.id.friendRadioSexGroup);
        myRadioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
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
                switch (i){
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



        SeekBar volControl = (SeekBar)findViewById(R.id.distanceSeekBar);
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


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
