package com.gogrocerdb.tcc.Activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.franmontiel.localechanger.LocaleChanger;
import com.gogrocerdb.tcc.MainActivity;
import com.gogrocerdb.tcc.MyLocationService;
import com.gogrocerdb.tcc.R;
import com.gogrocerdb.tcc.UpdateLocationService;
import com.gogrocerdb.tcc.util.Session_management;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class Splash extends AppCompatActivity {

//    static Splash instance;
//    LocationRequest locationRequest;
//    TextView textView;
//    FusedLocationProviderClient fusedLocationProviderClient;
//    public static Splash getInstance(){
//        return instance;
//    }


    public static final int MY_PERMISSIONS_REQUEST_WRITE_FIELS = 102;
    private AlertDialog dialog;
    private Session_management sessionManagement;
    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        sessionManagement = new Session_management(Splash.this);
        //textView=(TextView)findViewById(R.id.broadcast);

//        Dexter.withActivity(Splash.this)
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
//                        Toast.makeText(Splash.this, "you must accept the location ", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//
//                    }
//                }).check();







        Thread background = new Thread() {
            public void run() {

                try {
                    sleep(2 * 1000);
                    checkAppPermissions();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        background.start();
    }

//    private void updateLocation() {
//
//        buildLocationRequest();
//        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
//        fusedLocationProviderClient.requestLocationUpdates(locationRequest,getPendingIntent());
//
//    }

//    private PendingIntent getPendingIntent() {
//        Intent intent = new Intent(this, UpdateLocationService.class);
//        intent.setAction(UpdateLocationService.Action_process);
//        return PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//    }
//
//    public void updateToast(final String value)
//    {
//        Splash.this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                textView.setText(value);
//               // Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


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

    public void checkAppPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_NETWORK_STATE)
                        != PackageManager.PERMISSION_GRANTED
                ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.INTERNET) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_NETWORK_STATE)) {
                go_next();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.INTERNET,
                                android.Manifest.permission.ACCESS_NETWORK_STATE
                        },
                        MY_PERMISSIONS_REQUEST_WRITE_FIELS);
            }
        } else {
            go_next();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_FIELS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                go_next();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
                builder.setMessage("App required some permission please enable it")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                openPermissionScreen();
                            }
                        })
                        .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                dialog.dismiss();
                            }
                        });
                dialog = builder.show();
            }
            return;
        }
    }

    public void go_next() {
        if (sessionManagement.isLoggedIn()) {
            Intent startmain = new Intent(Splash.this, MainActivity.class);
            startActivity(startmain);
        } else {
            Intent startmain = new Intent(Splash.this, LogInActivity.class);
            startActivity(startmain);
        }
        finish();
    }


    public void openPermissionScreen() {
        // startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", Splash.this.getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
