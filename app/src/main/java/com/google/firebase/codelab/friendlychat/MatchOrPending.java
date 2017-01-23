package com.google.firebase.codelab.friendlychat;

/**
 * Created by zeevi on 1/23/2017.
 */

public class MatchOrPending {

    String userId;
    String redDot;

    public MatchOrPending(String userId, String redDot) {
        this.userId = userId;
        this.redDot = redDot;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRedDot() {
        return redDot;
    }

    public void setRedDot(String redDot) {
        this.redDot = redDot;
    }
}
