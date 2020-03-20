package com.gogrocerdb.tcc.Activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.gogrocerdb.tcc.R;
import com.gogrocerdb.tcc.UpdateLocationService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class demo extends AppCompatActivity {

    static demo instance;
    LocationRequest locationRequest;
    TextView textView;
    FusedLocationProviderClient fusedLocationProviderClient;
    public static demo getInstance(){
        return instance;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        textView =(TextView)findViewById(R.id.demo);

//        Dexter.withActivity(demo.this)
//                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse response) {
//
//                        updateLocation();
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse response) {
//                        Toast.makeText(demo.this, "you must accept the location ", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//
//                    }
//                }).check();

    }



//    private void updateLocation() {
//
//        buildLocationRequest();
//        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
//        fusedLocationProviderClient.requestLocationUpdates(locationRequest,getPendingIntent());
//
//    }
//
//    private PendingIntent getPendingIntent() {
//        Intent intent = new Intent(this, UpdateLocationService.class);
//        intent.setAction(UpdateLocationService.Action_process);
//        return PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//    }
//
//    public void updateToast(final String value)
//    {
//        demo.this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                textView.setText(value);
//                // Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//
//    private void buildLocationRequest() {
//
//        locationRequest = new LocationRequest();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(5000);
//        locationRequest.setFastestInterval(3000);
//        locationRequest.setSmallestDisplacement(10f);
//
//
//
//    }
}
