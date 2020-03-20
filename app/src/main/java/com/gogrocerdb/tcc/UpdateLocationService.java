package com.gogrocerdb.tcc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.gogrocerdb.tcc.Config.BaseURL;
import com.gogrocerdb.tcc.util.CustomVolleyJsonRequest;
import com.gogrocerdb.tcc.util.Session_management;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateLocationService extends BroadcastReceiver {
Context context;

    public static final String Action_process="com.gogrocerdb.tcc";

    @Override
    public void onReceive(Context context, Intent intent) {
//            if (intent!=null)
//            {
//                final String action = intent.getAction();
//                if (Action_process.equals(action))
//                {
//                    LocationResult result = LocationResult.extractResult(intent);
//                    if (result!=null)
//                    {
//                        Location location =result.getLastLocation();
//                        String location_String = new StringBuilder(""+location.getLatitude())
//                                .append("/")
//                                .append(location.getLongitude())
//                                .toString();
//                        Intent i=new Intent("my.action");
//                        i.putExtra("lat",location.getLatitude());
//                        i.putExtra("long",location.getLongitude());
//                        context.sendBroadcast(intent);
//
//                        try {
//                           // MainActivity.getInstance().updateToast(location_String);
//                        }catch (Exception ex)
//                        {
//                            Toast.makeText(context, "kk"+location_String, Toast.LENGTH_SHORT).show();
//                          // Location_Change_of_Deliveryboy(location.getLatitude(),location.getLongitude());
//                          //  Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
//                           // MainActivity.getInstance().Location_Change_of_Deliveryboy(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));
//                        }
//                    }
//                }
//            }
    }


    private void Location_Change_of_Deliveryboy(double latitude, double longitude1) {
        Session_management sessionManagement = new Session_management(context);
        String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        String tag_json_obj = "json_delete_order_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("lat", String.valueOf(latitude));
        params.put("lang",String.valueOf(longitude1));
        params.put("user_id", user_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.DEL_LOCATION, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG, response.toString());

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context,"error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

}


