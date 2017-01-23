package com.google.firebase.codelab.friendlychat;

import android.app.Application;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by zeevi on 1/23/2017.
 */

public class ApplicationClass extends Application{


    @Override
public void onCreate() {
    super.onCreate();
    //Parse SDK stuff goes here
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
}
}