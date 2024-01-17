package com.example.pulopo.Activity;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.pulopo.R;
import com.example.pulopo.Services.Post_Location_Service;
import com.example.pulopo.model.LocationCurrent;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.example.pulopo.databinding.ActivityMapsBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


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


    int statusLocaCurrent = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createdSevices();
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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


   }


}