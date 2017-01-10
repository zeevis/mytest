package com.google.firebase.codelab.friendlychat;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by zeevi on 1/10/2017.
 */

public class AppBaseDetails {

    protected GoogleSignInAccount account;
    private static AppBaseDetails appBaseDetails;

    /**
     * Create private constructor
     */
    private AppBaseDetails() {

    }

    /**
     * Create a static method to get instance.
     */
    public static AppBaseDetails getInstance() {
        if (appBaseDetails == null) {
            appBaseDetails = new AppBaseDetails();
        }
        return appBaseDetails;
    }

    public GoogleSignInAccount getAccount() {
        return account;
    }

    public void setAccount(GoogleSignInAccount account) {
        this.account = account;
    }
}