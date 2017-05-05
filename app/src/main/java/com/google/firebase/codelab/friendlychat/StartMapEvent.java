package com.google.firebase.codelab.friendlychat;

import android.app.usage.UsageEvents;

/**
 * Created by zeevi on 11/26/2016.
 */

public class StartMapEvent {
    public double lat;
    public double lng;

    public StartMapEvent(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
