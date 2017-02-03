package com.google.firebase.codelab.friendlychat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {


    private RadioGroup myRadioSexGroup;
    private RadioGroup friendRadioSexGroup;
    private DatabaseReference mFirebaseDatabaseReference;
    private String myUserId;
    private TextView radiusTextView;
    private FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        myUserId = mFirebaseAuth.getCurrentUser().getUid();
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



        final SeekBar volControl = (SeekBar)findViewById(R.id.distanceSeekBar);
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
                                                                 if(user.getSex().equals("male")){
                                                                     myRadioSexGroup.check(R.id.myRadioMale);
                                                                 }else{
                                                                     myRadioSexGroup.check(R.id.myRadioFemale);
                                                                 }
                                                                 if(user.getLookingFor().equals("male")){
                                                                     friendRadioSexGroup.check(R.id.friendRadioMale);
                                                                 }else{
                                                                     friendRadioSexGroup.check(R.id.myRadioFemale);
                                                                 }
                                                                 volControl.setProgress(user.getRadius());
                                                                 user.getLookingFor();
                                                             }

                                                             @Override
                                                             public void onCancelled(DatabaseError databaseError) {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.crash_menu:
//                FirebaseCrash.logcat(Log.ERROR, TAG, "crash caused");
//                causeCrash();
                return true;
            case R.id.invite_menu:
                //sendInvitation();
                //return true;
            case R.id.fresh_config_menu:
                //fetchConfig();
                // return true;
            case R.id.matches_screen_menu:
                startActivity(new Intent(this, MatchesActivity.class));
                return true;
            case R.id.profile_page_menu:
                startActivity(new Intent(this, ProphileActivity.class));
                return true;
            case R.id.settings_page:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                // mUsername = ANONYMOUS;
                startActivity(new Intent(this, SignInActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
