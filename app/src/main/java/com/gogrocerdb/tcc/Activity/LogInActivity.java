package com.gogrocerdb.tcc.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.franmontiel.localechanger.LocaleChanger;
import com.gogrocerdb.tcc.AppController;
import com.gogrocerdb.tcc.Config.BaseURL;
import com.gogrocerdb.tcc.MainActivity;
import com.gogrocerdb.tcc.R;
import com.gogrocerdb.tcc.util.CustomVolleyJsonRequest;
import com.gogrocerdb.tcc.util.Session_management;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity {
    EditText Et_login_email;
    RelativeLayout Btn_Sign_in;
    TextView tv_login_email;
    String getemail;
    String token;
    Context context;

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
        setContentView(R.layout.activity_log_in);
        token = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("grocery");
//        String token1 = FirebaseInstanceId.getInstance().getToken();
//        String token = SharedPref.getString(LogInActivity.this,SharedPrefManager.getInstance(LogInActivity.this).getDeviceToken());
        Et_login_email = (EditText) findViewById(R.id.et_login_email);
        tv_login_email = (TextView) findViewById(R.id.tv_login_email);
        Btn_Sign_in = (RelativeLayout) findViewById(R.id.btn_Sign_in);

        getemail = Et_login_email.getText().toString();



        Btn_Sign_in.setEnabled(true);
        Btn_Sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Et_login_email.equals("")) {
                    Toast.makeText(LogInActivity.this, "Please Put Your Currect Email-Id", Toast.LENGTH_SHORT).show();
                } else {
             makejson();
                }
            }
        });


    }



    public void makejson() {

        String tag_json_obj = "json_login_req";

         String UserName = Et_login_email.getText().toString().trim();


        Map<String, String> params = new HashMap<String, String>();
        params.put("user_password", UserName);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.LOGIN, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {
                    String status = response.getString("responce");

                    if (status.contains("true")) {

                        JSONArray jsonArray=response.getJSONArray("product");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String user_id = jsonObject.getString("id");
                            String user_fullname = jsonObject.getString("user_name");
                            String user_city = jsonObject.getString("city_name");
                            String city_id = jsonObject.getString("city_id");
                            Session_management sessionManagement = new Session_management(LogInActivity.this);
                            sessionManagement.createLoginSession(user_id, user_fullname,user_city);

                            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            checkLogin(user_id, token);
                            Btn_Sign_in.setEnabled(false);

                        }
                        } else {
                        Btn_Sign_in.setEnabled(true);

                        Toast.makeText(LogInActivity.this, "Please Put Correct Number", Toast.LENGTH_SHORT).show();
                    }

                }
                catch (JSONException e)
                {
                        e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error [" + error + "]");

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
    private void checkLogin(String user_id, String token) {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_login_req";

        //showpDialog();

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("token", token);
        params.put("device","android");

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.JSON_RIGISTER_FCM, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        JSONObject obj = new JSONObject();



                        //onBackPressed();


                    }else{
                        String error = response.getString("error");
                        Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                //hidepDialog();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}