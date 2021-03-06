package com.google.firebase.codelab.friendlychat;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by ideomobile on 11/12/16.
 */
public class LocationController {

    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 0;
    private DatabaseReference mFirebaseDatabaseReference;

    Context context;
    Geocoder geocoder;
    String bestProvider;
    List<Address> user = null;
    double lat;
    double lng;
    LocationManager lm;
    Criteria criteria;

    public Interfaces.basicListener getBasicListenerLocationChanged() {
        return basicListenerLocationChanged;
    }

    public void setBasicListenerLocationChanged(Interfaces.basicListener basicListenerLocationChanged) {
        this.basicListenerLocationChanged = basicListenerLocationChanged;
    }

    private Interfaces.basicListener basicListenerLocationChanged;

    public LocationController(Context context) {
        this.context = context;
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Context activeContext = context!= null?context:ApplicationClass.getInsntance().getApplicationContext();
        lm = (LocationManager) activeContext.getSystemService(Context.LOCATION_SERVICE);
        startPresentingLocation();
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void startPresentingLocation() {
         criteria = new Criteria();
        bestProvider = lm.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions((Activity)context,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
        Location location = lm.getLastKnownLocation(bestProvider);

        if (location == null){
            Toast.makeText(context,"Location Not found", Toast.LENGTH_LONG).show();
        }else{
            geocoder = new Geocoder(context);
            try {
                user = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                lat=(double)user.get(0).getLatitude();
                lng=(double)user.get(0).getLongitude();
                System.out.println(" DDD lat: " +lat+",  longitude: "+lng);

            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        lm.requestLocationUpdates(bestProvider, 2000, 10, locationListener);
        if(location !=null ) {
            lng = location.getLongitude();
            lat = location.getLatitude();
        }
    }


    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            lng = location.getLongitude();
            lat = location.getLatitude();
            bestProvider = lm.getBestProvider(criteria, false);
            FirebaseUser  firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            if(firebaseUser != null){
                String userid = firebaseUser.getUid();
                mFirebaseDatabaseReference.child("usersNew").child(userid).child("mLat")
                        .setValue(lat);

                mFirebaseDatabaseReference.child("usersNew").child(userid).child("mLng")
                        .setValue(lng);
                if(basicListenerLocationChanged != null){
                    basicListenerLocationChanged.onSuccess();

                }
            }



        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };



}
