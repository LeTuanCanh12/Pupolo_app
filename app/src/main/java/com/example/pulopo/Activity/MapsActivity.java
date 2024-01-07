package com.example.pulopo.Activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pulopo.R;
import com.example.pulopo.Services.Post_Location_Service;
import com.example.pulopo.Utils.UserUtil;
import com.example.pulopo.model.LocationCurrent;
import com.example.pulopo.model.Users;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.pulopo.databinding.ActivityMapsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LocationManager locationManager;
    double myLatLocation =0.0;
    double myLongLocation = 0.0;

    FirebaseDatabase database;
    DatabaseReference myRef;

    ArrayList<LocationCurrent> listLocation = new ArrayList<>();

    ArrayList<Users> listUser = new ArrayList<>();

    int statusLocaCurrent = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkStatusLogin();
        createdSevices();
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    private void checkStatusLogin() {
        if(UserUtil.statusLogin == false){
            Intent intent = new Intent(MapsActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }

    private void createdSevices() {
        Intent intentSer = new Intent(MapsActivity.this, Post_Location_Service.class);
        startService(intentSer);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        // add location dau tien cua ban
        statusLocaCurrent =0;
        mMap = googleMap;
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.child("My Family").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listUser.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Users users = snapshot.getValue(Users.class);
                    listUser.add(users);
                }
                mMap.clear();
                for(int i = 0;i< listUser.size();i++){
                    if(listUser.get(i).getUID().equals(UserUtil.UID.toString())){
                        UserUtil.userName = listUser.get(i).getUserName().toString().trim();
                        LatLng myHome =new LatLng(listUser.get(i).getLatLocation(),listUser.get(i).getLongLocation());
                        mMap.addMarker(new MarkerOptions().position(myHome).title("Bạn")).showInfoWindow();
                        if(statusLocaCurrent < 1 ) {
                           mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myHome,13.5f));
                           statusLocaCurrent++;
                       }
                    }else {

                        LatLng location = new LatLng(listUser.get(i).getLatLocation(), listUser.get(i).getLongLocation());
                        // Add a marker in Sydney and move the camera
                        mMap.addMarker(new MarkerOptions().position(location).title(listUser.get(i).getUserName()));

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Failed", "Failed to read value.", error.toException());
            }
        });

   }


}