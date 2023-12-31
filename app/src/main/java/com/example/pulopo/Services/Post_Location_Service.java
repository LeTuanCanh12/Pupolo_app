package com.example.pulopo.Services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.pulopo.Activity.LoginActivity;
import com.example.pulopo.Utils.UserUtil;
import com.example.pulopo.model.Users;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Post_Location_Service extends Service {
    Handler mHandler = new Handler();
    private LocationManager locationManager;

    double myLatLocation =0.0;
    double myLongLocation = 0.0;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Post_Location_Service() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getLocationCurrent();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("My Family");
                Users users = new Users(UserUtil.UID.toString().trim(),UserUtil.userName.toString().trim(),myLatLocation,myLongLocation);
                myRef.child(users.getUID()).setValue(users);
                mHandler.postDelayed(this, 1000);
            }
        }, 1000);
        return START_NOT_STICKY;

    }

    private void getLocationCurrent() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }else {
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    myLongLocation = location.getLongitude();
                    myLatLocation = location.getLatitude();

                }
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
       return  null;
    }
}