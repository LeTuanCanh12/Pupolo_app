package com.example.pulopo.Services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.pulopo.Retrofit.ApiServer;
import com.example.pulopo.Retrofit.RetrofitClient;
import com.example.pulopo.Utils.LocationUtil;
import com.example.pulopo.Utils.UserUtil;
import com.example.pulopo.Utils.UtilsCommon;
import com.example.pulopo.model.LocationCurrent;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Post_Location_Service extends Service {

    private LocationManager locationManager;

    double myLatLocation = 0.0;
    double myLongLocation = 0.0;

    ApiServer apiServer;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int iduser;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Post_Location_Service() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        apiServer = RetrofitClient.getInstance(UtilsCommon.BASE_URL).create(ApiServer.class);
        iduser = LocationUtil.getReciverId();
        getLocationCurrent();
        stopSelf();
        return START_NOT_STICKY;

    }

    public void getLocationCurrent() {
        String locationText = "";
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    myLongLocation = location.getLongitude();
                    myLatLocation = location.getLatitude();
                    LocationCurrent.setLat(myLatLocation);
                    LocationCurrent.setLong(myLongLocation);
                    sendLocationToNet(myLatLocation,myLongLocation);
                }
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
            Log.e("location", locationManager.toString());
        }

    }

    private void sendLocationToNet(double myLatLocation,double myLongLocation) {
        String messLocation= String.valueOf(myLatLocation)+","+String.valueOf(myLongLocation);

        compositeDisposable.add(apiServer.sendMessChat(UserUtil.getId(), iduser, messLocation, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
        if (iduser == 1) {
            return;
        }


    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}