package com.example.map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Double Latitude, Longitude;
    private Geocoder geocoder;
    private List<Address> addresses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        geocoder = new Geocoder ( this, Locale.ENGLISH );

        locationCallback = new LocationCallback () {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult ( locationResult );


                if(locationResult != null){
                    for(Location location : locationResult.getLocations ()){
                        Latitude = location.getLatitude ();
                        Longitude = location.getLongitude ();

                        try {
                            addresses = geocoder.getFromLocation ( Latitude, Longitude, 1 );

                            String address = addresses.get ( 0 ).getAddressLine ( 0 ) + "/n"+ addresses.get ( 0 ).getLocality ()+ "/"
                                    +addresses.get ( 0 ).getPostalCode ();
                            Toast.makeText ( MainActivity.this, address, Toast.LENGTH_LONG ).show ();

                        } catch (IOException e) {
                            e.printStackTrace ();
                        }

                    }
                }
            }
        };
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient ( this );
      CreateLocationRequst ();

    }

    private void CreateLocationRequst() {


        locationRequest = new LocationRequest ();
        locationRequest.setInterval ( 1000 );
        locationRequest.setPriority ( LocationRequest.PRIORITY_HIGH_ACCURACY );
        locationRequest.setFastestInterval ( 5000 );

        if (ActivityCompat.checkSelfPermission ( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission ( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions ( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},101 );
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates ( locationRequest, locationCallback, null );
    }

    public void Start_map(View view) {

        Intent intent = new Intent (MainActivity.this, MapsActivity.class);

        intent.putExtra ( "LAT", Latitude );
        intent.putExtra ( "LON", Longitude );

        startActivity ( intent);

    }
}