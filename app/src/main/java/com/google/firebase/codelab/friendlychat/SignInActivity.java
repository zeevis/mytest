/**
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.firebase.codelab.friendlychat;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.google.firebase.codelab.friendlychat.MainActivity.MESSAGES_CHILD;


public class SignInActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private SignInButton mSignInButton;

    private GoogleApiClient mGoogleApiClient;

    private FirebaseAuth mFirebaseAuth;

    private DatabaseReference mDatabase;
    private LoginButton faceBookLoginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken;
    private ProfileTracker profileTracker;
    private FirebaseAuth.AuthStateListener mAuthListener;
// ...


    // Firebase instance variables
    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();
        // Assign fields
        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.google.firebase.codelab.friendlychat",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

// ...
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
//        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//
//            });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };



        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }

        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

            }

        };


        // Set click listeners
        mSignInButton.setOnClickListener(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Initialize FirebaseAuth
//        LoginManager.getInstance().logInWithReadPermissions(
//                this,
//                Arrays.asList("user_photos"));



        faceBookLoginButton = (LoginButton)findViewById(R.id.login_button);
       // faceBookLoginButton.setPublishPermissions(Arrays.asList("publish_actions","user_photos"));
        faceBookLoginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_friends","user_photos","user_status","albums"));


        // If using in a fragment
       // faceBookLoginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        faceBookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d(TAG, "facebook:onSuccess:" + loginResult);

                LoginManager.getInstance().logInWithReadPermissions(
                        SignInActivity.this,
                        Arrays.asList("user_photos"));

                LoginManager.getInstance().logInWithPublishPermissions(
                        SignInActivity.this,
                        Arrays.asList("publish_actions"));



                loginResult.getRecentlyGrantedPermissions();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d(TAG, "facebook:onError", exception);
            }
        });

        faceBookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(SignInActivity.this, Arrays.asList("public_profile"));
            }
        });

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                AppBaseDetails.getInstance().setAccount(account);
                firebaseAuthWithGoogle(account);

                //writeNewUser("12345678","zzzzzzz" ,"zzzzzz@gmai.com" );

            } else {
                // Google Sign In failed
                Log.e(TAG, "Google Sign In failed.");
            }
        }
    }

    private void writeNewUser(GoogleSignInAccount account, final double lat,final double lng) {
       final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
       final User user = new User( firebaseUser.getUid(),account.getGivenName() ,account.getEmail(),account.getFamilyName(),account.getDisplayName(), FirebaseInstanceId.getInstance().getToken(),account.getPhotoUrl().toString(),lat,lng );
        //User user = new User(name, email);
        //ask if user exists



        mDatabase.child("usersNew").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(firebaseUser.getUid())) {
                    HashMap<String,Object> map = new HashMap<String, Object>();
                    map.put("mLat",lat);
                    map.put("mLng",lng);
                    map.put("mUserKeyToken",user.getmUserKeyToken());
                    mDatabase.child("usersNew").child(user.getmUserId()).updateChildren(map);
                }else{
                    mDatabase.child("usersNew").child(user.getmUserId()).setValue(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




//        mDatabase.child(MESSAGES_CHILD)
//                .push().setValue(user);
//        FriendlyMessage friendlyMessage = new
//                FriendlyMessage(mMessageEditText.getText().toString(),
//                mUsername,
//                mPhotoUrl);
//        mFirebaseDatabaseReference.child(MESSAGES_CHILD)
//                .push().setValue(friendlyMessage);
//        mMessageEditText.setText("");
    }

    private void writeNewUserFacebook(final double lat,final double lng) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final User user = new User( firebaseUser.getUid(),firebaseUser.getDisplayName() ,firebaseUser.getEmail(),firebaseUser.getDisplayName(),firebaseUser.getDisplayName(), FirebaseInstanceId.getInstance().getToken(),firebaseUser.getPhotoUrl().toString(),lat,lng );
        //User user = new User(name, email);
        //ask if user exists
       // mDatabase.child("usersNew").child(user.getmUserId()).setValue(user);
//        mDatabase.child(MESSAGES_CHILD)
//                .push().setValue(user);
//        FriendlyMessage friendlyMessage = new
//                FriendlyMessage(mMessageEditText.getText().toString(),
//                mUsername,
//                mPhotoUrl);
//        mFirebaseDatabaseReference.child(MESSAGES_CHILD)
//                .push().setValue(friendlyMessage);
//        mMessageEditText.setText("");



        mDatabase.child("usersNew").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(firebaseUser.getUid())) {
                    HashMap<String,Object> map = new HashMap<String, Object>();
                    map.put("mLat",lat);
                    map.put("mLng",lng);
                    map.put("userKeyToken",user.getmUserKeyToken());
                    mDatabase.child("usersNew").child(user.getmUserId()).updateChildren(map);
                }else{
                    mDatabase.child("usersNew").child(user.getmUserId()).setValue(user);
                }

                startActivity(new Intent(SignInActivity.this, MainListActivity.class));
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }


    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGooogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle theF
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            LocationController locationController = new LocationController(SignInActivity.this);
                            double lat = locationController.getLat();
                            double lng = locationController.getLng();

                            writeNewUser(acct,lat,lng);
                            startActivity(new Intent(SignInActivity.this, MainListActivity.class));
                            finish();
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AppBaseDetails.getInstance().setUserFaceBookIdToken(token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            LocationController locationController = new LocationController(SignInActivity.this);
                            double lat = locationController.getLat();
                            double lng = locationController.getLng();

                            writeNewUserFacebook(lat,lng);

//                            startActivity(new Intent(SignInActivity.this, MainListActivity.class));
//                            finish();
                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }
}
