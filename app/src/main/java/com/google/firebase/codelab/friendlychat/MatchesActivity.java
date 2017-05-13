package com.google.firebase.codelab.friendlychat;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.codelab.friendlychat.matches.BadgedDrawerArrowDrawable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchesActivity extends AppCompatActivity {
    private LinearLayoutManager mLinearLayoutManager;
    private LinearLayoutManager mLinearLayoutManagerPending;

    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<MatchOrPending, MatchesActivity.MessageViewHolder>
            mFirebaseAdapter;
    private FirebaseRecyclerAdapter<MatchOrPending, MatchesActivity.MessageViewHolder>
            mFirebaseAdapterPending;
    private FirebaseAuth mFirebaseAuth;
    private RecyclerView mMessageRecyclerView;
    private RecyclerView mMessageRecyclerViewPending;
    private TextView mTextViewArrowBack;
    private ArrayList<User> userArrayList;
    private LocationController locationController;

    private DrawerLayout mDrawerLayout;
    Toolbar toolbar;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;

    boolean toolBarWasSet;

   // private BadgedDrawerArrowDrawable mBadgedDrawerArrowDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //  mBadgedDrawerArrowDrawable = new BadgedDrawerArrowDrawable(this);
        setContentView(R.layout.activity_matches);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(!toolBarWasSet){
            toolBarWasSet = true;
            setupToolbar();
        }

        mFirebaseAuth = FirebaseAuth.getInstance();
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.usersRecyclerView);
        mMessageRecyclerViewPending = (RecyclerView) findViewById(R.id.usersRecyclerViewPending);
        mTextViewArrowBack = (TextView)findViewById(R.id.textViewArrowBack);

        mTextViewArrowBack.setOnClickListener(mTextViewArrowBackListener);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManagerPending = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mLinearLayoutManagerPending.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerViewPending.setLayoutManager(mLinearLayoutManagerPending);


        // New child entries

        mFirebaseDatabaseReference= FirebaseDatabase.getInstance().getReference();
        mFirebaseAdapterPending = new FirebaseRecyclerAdapter<MatchOrPending,MatchesActivity.MessageViewHolder>(
                MatchOrPending.class,
                R.layout.item_message,
                MatchesActivity.MessageViewHolder.class,
                mFirebaseDatabaseReference.child("usersNew").child(mFirebaseAuth.getCurrentUser().getUid()).child("pending")) {
            @Override
            protected void populateViewHolder(final MessageViewHolder viewHolder, final MatchOrPending matchOrPending, int position) {
                DatabaseReference databaseReference = mFirebaseDatabaseReference.child("usersNew").child(matchOrPending.getUserId());

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
                                intent.putExtra("senderTokenToGetBackToo" ,user.getmUserKeyToken());
                                intent.putStringArrayListExtra("senderPictureList" ,(ArrayList)user.getProfilePic());
                                intent.putExtra("senderProfilePicture" ,user.getmUserPhotoUrlHighQuality());
                                intent.putExtra("pending" ,"pending" );

                                startActivity(intent);
                                mDrawerLayout.closeDrawer(View.TEXT_ALIGNMENT_VIEW_START);
                            }
                        });

                        viewHolder.messageTextView.setText(user.getmEmail());
                        viewHolder.messengerTextView.setText(user.getmUserDisplayName());

                        if(matchOrPending.getRedDot() != null && matchOrPending.getRedDot().equals("redDot")){
                            viewHolder.redDotImageView.setVisibility(View.VISIBLE);
                            mDrawerToggle.setDrawerArrowDrawable(new BadgedDrawerArrowDrawable(MatchesActivity.this,true));
                        }else{
                            viewHolder.redDotImageView.setVisibility(View.GONE);
                            mDrawerToggle.setDrawerArrowDrawable(new BadgedDrawerArrowDrawable(MatchesActivity.this,false));
                        }
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

            }
        };
        mFirebaseAdapter = new FirebaseRecyclerAdapter<MatchOrPending, MatchesActivity.MessageViewHolder>(
                MatchOrPending.class,
                R.layout.item_message,
                MatchesActivity.MessageViewHolder.class,
                mFirebaseDatabaseReference.child("usersNew").child(mFirebaseAuth.getCurrentUser().getUid()).child("matches")) {

            @Override
            protected void populateViewHolder(final MatchesActivity.MessageViewHolder viewHolder,
                                              final MatchOrPending matchOrPending, int position) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("usersNew").child(matchOrPending.getUserId());

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
                                mDrawerLayout.closeDrawer(View.TEXT_ALIGNMENT_VIEW_START);
                            }
                        });

                        if(user != null) {
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
                        if(matchOrPending.getRedDot() != null && matchOrPending.getRedDot().equals("redDot")){
                            viewHolder.redDotImageView.setVisibility(View.VISIBLE);
                            mDrawerToggle.setDrawerArrowDrawable(new BadgedDrawerArrowDrawable(MatchesActivity.this,true));
                        }else{
                            viewHolder.redDotImageView.setVisibility(View.GONE);
                            mDrawerToggle.setDrawerArrowDrawable(new BadgedDrawerArrowDrawable(MatchesActivity.this,false));
                        }



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

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



        mFirebaseAdapterPending.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapterPending.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManagerPending.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerViewPending.scrollToPosition(positionStart);

                }
            }
        });



        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);
mMessageRecyclerViewPending.setLayoutManager(mLinearLayoutManagerPending);
        mMessageRecyclerViewPending.setAdapter(mFirebaseAdapterPending);

        setupDrawerToggle();
    }


    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public TextView messengerTextView;
        public CircleImageView messengerImageView;
        public LinearLayout redDotImageView;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
            redDotImageView = (LinearLayout) itemView.findViewById(R.id.redDotLayOut);
        }
    }

    private View.OnClickListener mTextViewArrowBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onBackPressed();
        }
    };

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
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                // mUsername = ANONYMOUS;
                startActivity(new Intent(this, SignInActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    void setupDrawerToggle(){
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
        mDrawerToggle.setDrawerArrowDrawable(new BadgedDrawerArrowDrawable(this,false));

    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
       if(this.getClass().getSimpleName().equals("MainListActivity")){
           mTextViewArrowBack.setVisibility(View.GONE);
           return true;
       }else{
           mTextViewArrowBack.setVisibility(View.VISIBLE);
           return false;
       }

    }
}
