package com.google.firebase.codelab.friendlychat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchesActivity extends AppCompatActivity {
    private LinearLayoutManager mLinearLayoutManager;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<String, MatchesActivity.MessageViewHolder>
            mFirebaseAdapter;
    private FirebaseAuth mFirebaseAuth;
    private RecyclerView mMessageRecyclerView;
    private ArrayList<User> userArrayList;
    private LocationController locationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirebaseAuth = FirebaseAuth.getInstance();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Initialize ProgressBar and RecyclerView.
        // mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.usersRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);


        // New child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<String, MatchesActivity.MessageViewHolder>(
                String.class,
                R.layout.item_message,
                MatchesActivity.MessageViewHolder.class,
                mFirebaseDatabaseReference.child("usersNew").child(mFirebaseAuth.getCurrentUser().getUid()).child("matches")) {

            @Override
            protected void populateViewHolder(final MatchesActivity.MessageViewHolder viewHolder,
                                              final String userid, int position) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("usersNew").child(userid);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        final User user =  snapshot.getValue(User.class);

                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(MatchesActivity.this, MainActivity.class);
                                intent.putExtra("intentType" ,"cameFormMeetingActivity" );
                                intent.putExtra("latToGetBackTo" ,user.getmLat());
                                intent.putExtra("lngToGetBackTo" ,user.getmLng());
                                intent.putExtra("senderIdToGetBackToo" ,user.getmUserId());
                                startActivity(intent);
                            }
                        });

                            viewHolder.messageTextView.setText(user.getmEmail());
                            viewHolder.messengerTextView.setText(user.getmUserDisplayName());
                            if (user.getmUserPhotoUrl() == null) {
                                viewHolder.messengerImageView
                                        .setImageDrawable(ContextCompat
                                                .getDrawable(MatchesActivity.this,
                                                        R.drawable.ic_account_circle_black_36dp));
                            } else {
                                Glide.with(MatchesActivity.this)
                                        .load(user.getmUserPhotoUrl())
                                        .into(viewHolder.messengerImageView);
                            }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                //  userArrayList.add(user);
//                viewHolder.messageTextView.setText(user.getmEmail());
//                viewHolder.messengerTextView.setText(user.getmUserDisplayName());
//                if (user.getmUserPhotoUrl() == null) {
//                    viewHolder.messengerImageView
//                            .setImageDrawable(ContextCompat
//                                    .getDrawable(MatchesActivity.this,
//                                            R.drawable.ic_account_circle_black_36dp));
//                } else {
//                    Glide.with(MatchesActivity.this)
//                            .load(user.getmUserPhotoUrl())
//                            .into(viewHolder.messengerImageView);
//                }
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
