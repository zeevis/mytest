package com.google.firebase.codelab.friendlychat;

import android.app.Application;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by zeevi on 1/23/2017.
 */

public class ApplicationClass extends Application{

private static ApplicationClass applicationClass;

    public static ApplicationClass getInsntance(){
        if(applicationClass == null){
            applicationClass = new ApplicationClass();
        }
        return applicationClass;
    }



    @Override
public void onCreate() {
    super.onCreate();
    //Parse SDK stuff goes here
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
}
}
