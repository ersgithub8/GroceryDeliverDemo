package com.gogrocerdb.tcc;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.franmontiel.localechanger.LocaleChanger;
import com.gogrocerdb.tcc.Activity.Policy;
import com.gogrocerdb.tcc.Config.BaseURL;
import com.gogrocerdb.tcc.Fonts.CustomTypefaceSpan;
import com.gogrocerdb.tcc.Fragment.Home;
import com.gogrocerdb.tcc.NetworkConnectivity.NoInternetConnection;
import com.gogrocerdb.tcc.util.ConnectivityReceiver;
import com.gogrocerdb.tcc.util.CustomVolleyJsonRequest;
import com.gogrocerdb.tcc.util.Session_management;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener {




    private TextView textView;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    //textView.append("\n" +intent.getExtras().get("coordinates"));
                    String cor = (String)intent.getExtras().get("coordinates");
//                    Object latt = intent.getExtras().get("lat");
//                    String langg = intent.getStringExtra("lang");


                    String[] values = cor.split(",");
                    String langgg= values[0];
                    String lattt=values[1];

                   // Toast.makeText(context,lattt+langgg, Toast.LENGTH_SHORT).show();
                    Location_Change_of_Deliveryboy(lattt,langgg);
                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(broadcastReceiver != null){
            registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
        }
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    String cor = (String)intent.getExtras().get("coordinates");

                    String[] values = cor.split(",");
                    String langgg= values[0];
                    String lattt=values[1];

                   // Toast.makeText(context,lattt+langgg, Toast.LENGTH_SHORT).show();
                    Location_Change_of_Deliveryboy(lattt,langgg);
                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(broadcastReceiver != null){
//            unregisterReceiver(broadcastReceiver);
//        }

        if(broadcastReceiver != null){
            registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
        }
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    String cor = (String)intent.getExtras().get("coordinates");

                    String[] values = cor.split(",");
                    String langgg= values[0];
                    String lattt=values[1];

                  //  Toast.makeText(context,lattt+langgg, Toast.LENGTH_SHORT).show();
                    Location_Change_of_Deliveryboy(lattt,langgg);
                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
    }


    public static final String Action_process="com.gogrocerdb.tcc";
    static MainActivity instance;
    LocationRequest locationRequest;

    public Session_management sessionManagement;
    FusedLocationProviderClient fusedLocationProviderClient;
    public static MainActivity getInstance(){
        return instance;
    }

    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageView iv_profile;
    private Menu nav_menu;
    private TextView tv_name, tv_city;
    ImageView imageView;
    TextView mTitle;
    Toolbar toolbar;
    int padding = 0;
    private Bitmap bitmap;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences= getSharedPreferences("lan", Context.MODE_PRIVATE);
         sessionManagement = new Session_management(MainActivity.this);
        textView=(TextView)findViewById(R.id.demo2);

        Intent ii =new Intent(getApplicationContext(),GPS_Service.class);
        startService(ii);

        if(!runtime_permissions())
        {

    }
//        Dexter.withActivity(MainActivity.this)
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
//                        Toast.makeText(MainActivity.this, "you must accept the location ", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//
//                    }
//                }).check();

        editor = sharedPreferences.edit();

        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);

            if (view instanceof TextView) {
                TextView textView = (TextView) view;

                Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "Font/Bold.otf");
                textView.setTypeface(myCustomFont);
            }


        }
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu m = navigationView.getMenu();
        for (
                int i = 0; i < m.size(); i++)

        {
            MenuItem mi = m.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            applyFontToMenuItem(mi);
        }

        sessionManagement = new

                Session_management(MainActivity.this);
        View headerView = navigationView.getHeaderView(0);
        navigationView.getBackground().setColorFilter(0x80000000, PorterDuff.Mode.MULTIPLY);
        navigationView.setNavigationItemSelectedListener(this);
        nav_menu = navigationView.getMenu();
        View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
        iv_profile = (ImageView) header.findViewById(R.id.iv_header_img);

        tv_name = (TextView) header.findViewById(R.id.tv_header_name);

        tv_city = (TextView) header.findViewById(R.id.tv_city);
        updateHeader();
        sideMenu();



        if (savedInstanceState == null) {
            Fragment fm = new Home();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contentPanel, fm, "Home_fragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
        getFragmentManager().
                addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        try {

                            InputMethodManager inputMethodManager = (InputMethodManager)
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                            Fragment fr = getFragmentManager().findFragmentById(R.id.contentPanel);
                            final String fm_name = fr.getClass().getSimpleName();
                            Log.e("backstack: ", ": " + fm_name);
                            if (fm_name.contentEquals("Home_fragment")) {
                                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                                toggle.setDrawerIndicatorEnabled(true);
                                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                                toggle.syncState();

                            } else if (fm_name.contentEquals("My_order_fragment") ||
                                    fm_name.contentEquals("Thanks_fragment")) {
                                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                                toggle.setDrawerIndicatorEnabled(false);
                                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                toggle.syncState();
                                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Fragment fm = new Home();
                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                                .addToBackStack(null).commit();
                                    }
                                });
                            } else {
                                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                                toggle.setDrawerIndicatorEnabled(false);
                                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                toggle.syncState();
                                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        onBackPressed();
                                    }
                                });
                            }

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                });

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
//        MainActivity.this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                textView.setText(value);
//                // Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
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

    public void updateHeader() {
        if (sessionManagement.isLoggedIn()) {
            String getname = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
            String getcity = sessionManagement.getUserDetails().get(BaseURL.KEY_city);

            tv_name.setText(getname);
            tv_city.setText(getcity);

        }
    }



    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "Font/Bold.otf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


    public void sideMenu() {
        if (sessionManagement.isLoggedIn()) {
            nav_menu.findItem(R.id.nav_log_out).setVisible(true);
        } else {
//            tv_name.setText(getResources().getString(R.string.btn_login));
//            tv_name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent i = new Intent(MainActivity.this, LogInActivity.class);
//                    startActivity(i);
//                }
//            });
            nav_menu.findItem(R.id.nav_log_out).setVisible(false);

        }
    }


    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fm = null;
        Bundle args = new Bundle();
        if (id == R.id.nav_order) {
            Fragment fma = new Home();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contentPanel, fma, "Home_fragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();

        }
        else if (id== R.id.Dashboard){
            Fragment fma = new Home();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contentPanel, fma, "Home_fragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();

        }
        else if (id== R.id.nav_order1){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi friends i am using ." + "Kitchen Basket Please download it");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        }
        else if (id== R.id.nav_order2){
            Intent i = new Intent(MainActivity.this, Policy.class);
            startActivity(i);
        }
        else if (id== R.id.nav_order3){
            Toast.makeText(MainActivity.this,"Review Us on Play store",Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.nav_log_out) {
            sessionManagement.logoutSession();
            Intent i = new Intent(getApplicationContext(),GPS_Service.class);
            stopService(i);
            finish();
        }
        if (fm != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                    .addToBackStack(null).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {

        if (!isConnected) {
            Intent intent = new Intent(MainActivity.this, NoInternetConnection.class);
            startActivity(intent);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.language, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_language) {
            openLanguageDialog();
        }


        return super.onOptionsItemSelected(item);
    }


    private void openLanguageDialog() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_language, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        TextView lEnglish = v.findViewById(R.id.l_english);
        TextView lSpanish = v.findViewById(R.id.l_arabic);
        final AlertDialog dialog = builder.create();

        lEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocaleChanger.setLocale(Locale.ENGLISH);
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                editor.putString("language", "english");
                editor.apply();

                recreate();
                dialog.dismiss();
            }
        });
        lSpanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocaleChanger.setLocale(new Locale("ar", "ARABIC"));
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                editor.putString("language", "spanish");
                editor.apply();

                recreate();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


//    UpdateLocationService receiver = new UpdateLocationService() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals("my.action"))
//            {
//                String lat = intent.getExtras().getString("lat");
//                String lang = intent.getExtras().getString("long");
//                Location_Change_of_Deliveryboy(lat,lang);
//            }
//        }
//    };



    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){

            }else {
                runtime_permissions();
            }
        }
    }



    public void Location_Change_of_Deliveryboy(String latitude, String longitude1) {
        Session_management session_management = new Session_management(MainActivity.this);
        String user_id = session_management.getUserDetails().get(BaseURL.KEY_ID);
        String tag_json_obj = "json_delete_order_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("lat", latitude);
        params.put("lang",longitude1);
        params.put("user_id", user_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.DEL_LOCATION, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
}
