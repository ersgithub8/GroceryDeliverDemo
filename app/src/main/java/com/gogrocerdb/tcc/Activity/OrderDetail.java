package com.gogrocerdb.tcc.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.franmontiel.localechanger.LocaleChanger;
import com.gogrocerdb.tcc.Adapter.My_order_detail_adapter;
import com.gogrocerdb.tcc.AppController;
import com.gogrocerdb.tcc.Config.BaseURL;
import com.gogrocerdb.tcc.MainActivity;
import com.gogrocerdb.tcc.Model.My_order_detail_model;
import com.gogrocerdb.tcc.R;
import com.gogrocerdb.tcc.util.ConnectivityReceiver;
import com.gogrocerdb.tcc.util.CustomVolleyJsonArrayRequest;
import com.gogrocerdb.tcc.util.CustomVolleyJsonRequest;
import com.gogrocerdb.tcc.util.Session_management;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetail extends AppCompatActivity {
    public TextView tv_orderno, tv_status, tv_date, tv_time, tv_price, tv_item, relativetextstatus, tv_tracking_date;
    private String sale_id;

    private RecyclerView rv_detail_order;
    private List<My_order_detail_model> my_order_detail_modelList = new ArrayList<>();
    RelativeLayout pickup_location,dropoff_location,cancel,order_canceled,Mark_Delivered,picked_store,return_items;
    public String stats;
    Session_management session_management;
    String getuserid;
    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.orderdetail));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetail.this, MainActivity.class);
                startActivity(intent);
            }
        });


        rv_detail_order = (RecyclerView) findViewById(R.id.product_recycler);
        rv_detail_order.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_detail_order.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 0));
        tv_orderno = (TextView) findViewById(R.id.tv_order_no);
        tv_status = (TextView) findViewById(R.id.tv_order_status);
        relativetextstatus = (TextView) findViewById(R.id.status);
        cancel = (RelativeLayout) findViewById(R.id.btn_mark_cancel);
        picked_store = (RelativeLayout) findViewById(R.id.btn_mark_picked);
        order_canceled=(RelativeLayout)findViewById(R.id.order_canceled);
        tv_tracking_date = (TextView) findViewById(R.id.tracking_date);
        tv_date = (TextView) findViewById(R.id.tv_order_date);
        tv_time = (TextView) findViewById(R.id.tv_order_time);
        tv_price = (TextView) findViewById(R.id.tv_order_price);
        return_items = (RelativeLayout) findViewById(R.id.btn_mark_return);
        tv_item = (TextView) findViewById(R.id.tv_order_item);
        pickup_location = (RelativeLayout) findViewById(R.id.pick_location_rev);
        dropoff_location=(RelativeLayout)findViewById(R.id.dropoff_location_rev);
        session_management = new Session_management(getApplicationContext());
//        Intent i =getIntent();
//        String s = i.getStringExtra("userid");


        getuserid = session_management.getUserDetails().get(BaseURL.KEY_ID);

pickup_location.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


        String storeLat=getIntent().getStringExtra("store_lat");
        String storelang= getIntent().getStringExtra("store_lang");
        if(storeLat.isEmpty()||storelang.isEmpty()){
            Toast.makeText(OrderDetail.this, "Can't perform this operation.", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(OrderDetail.this,MapsActivity.class);
        intent.putExtra("customerLat",storeLat);
        intent.putExtra("customerLang",storelang);

        startActivity(intent);

    }
});
        dropoff_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String deliveryAddress=getIntent().getStringExtra("delivery_address");
                if(deliveryAddress.isEmpty()){
                    Toast.makeText(OrderDetail.this, "Can't perform this operation.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String[] values = deliveryAddress.split(",");
                String customerLat = values[0];
                String customerLang = values[1];
              //  Toast.makeText(OrderDetail.this, customerLat, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OrderDetail.this,MapsActivity.class);
                intent.putExtra("customerLat",customerLat);
                intent.putExtra("customerLang",customerLang);
                startActivity(intent);

            }
        });


        sale_id = getIntent().getStringExtra("sale_id");
        if (ConnectivityReceiver.isConnected()) {
            makeGetOrderDetailRequest(sale_id);
        } else {
            Toast.makeText(getApplicationContext(), "Network Issue", Toast.LENGTH_SHORT).show();
        }
        String placed_on = getIntent().getStringExtra("placedon");
        String time = getIntent().getStringExtra("time");
        String item = getIntent().getStringExtra("item");
        String amount = getIntent().getStringExtra("ammount");
        stats = getIntent().getStringExtra("status");
        String deliveryAddress=getIntent().getStringExtra("delivery_address");

       // Toast.makeText(this, deliveryAddress, Toast.LENGTH_SHORT).show();
        Mark_Delivered = (RelativeLayout) findViewById(R.id.btn_mark_delivered);
        picked_store = (RelativeLayout) findViewById(R.id.btn_mark_picked);
        Mark_Delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetail.this, GetSignature.class);
                intent.putExtra("sale", sale_id);
                startActivity(intent);

            }
        });
        return_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetail.this, Return_items.class);
                intent.putExtra("sale", sale_id);
                startActivity(intent);

            }
        });

        picked_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dropoff_location.setVisibility(View.VISIBLE);
                pick_store(sale_id);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                canceled(sale_id);

            }
        });
//        if (stats.equals("0")) {
//            tv_status.setText(getResources().getString(R.string.pending));
//            relativetextstatus.setText(getResources().getString(R.string.pending));
//        } else
            if (stats.equals("1")) {
            tv_status.setText(getResources().getString(R.string.confirm));
            relativetextstatus.setText(getResources().getString(R.string.confirm));
        } else if (stats.equals("2")) {
            tv_status.setText(getResources().getString(R.string.outfordeliverd));
                //Mark_Delivered.setVisibility(View.VISIBLE);
                picked_store.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);

            relativetextstatus.setText(getResources().getString(R.string.outfordeliverd));
        }else if (stats.equals("3")) {
                tv_status.setText("Picked From Store");
                Mark_Delivered.setVisibility(View.VISIBLE);
                return_items.setVisibility(View.VISIBLE);
                picked_store.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                dropoff_location.setVisibility(View.VISIBLE);
                relativetextstatus.setText(getResources().getString(R.string.delivered));
            }else if (stats.equals("4")) {
            tv_status.setText(getResources().getString(R.string.delivered));
                picked_store.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                return_items.setVisibility(View.GONE);
                Mark_Delivered.setVisibility(View.GONE);
                dropoff_location.setVisibility(View.VISIBLE);
           // Mark_Delivered.setVisibility(View.VISIBLE);
            relativetextstatus.setText(getResources().getString(R.string.delivered));
        }else if (stats.equals("5")) {
                tv_status.setText("Order Canceled");
                picked_store.setVisibility(View.GONE);
                Mark_Delivered.setVisibility(View.GONE);
                return_items.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                order_canceled.setVisibility(View.VISIBLE);
                // Mark_Delivered.setVisibility(View.VISIBLE);
                relativetextstatus.setText("Order Canceled");
            }

        tv_orderno.setText(sale_id);
        tv_date.setText(placed_on);
        tv_time.setText(time);
        tv_item.setText(item);
        tv_tracking_date.setText(placed_on);
        tv_price.setText(getResources().getString(R.string.currency) + amount);


    }

    private void canceled(String sale_id) {

        String tag_json_obj = "json_delete_order_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("sale_id", sale_id);
        params.put("status","5");
        params.put("cancelfrom","Deliveryboy");
        params.put("cancel_person_id", getuserid);
        // params.put("user_id", user_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.DELETE_ORDER_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String msg = response.getString("message");
                        Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT).show();

                        // ((MainActivity) getApplicationContext()).onBackPressed();
                        Intent i = new Intent(OrderDetail.this,MainActivity.class);
                        startActivity(i);
                        order_canceled.setVisibility(View.VISIBLE);
                        picked_store.setVisibility(View.GONE);
                        cancel.setVisibility(View.GONE);


                    } else {
                        String error = response.getString("error");
                        Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }
    private void pick_store(String sale_id) {

        String tag_json_obj = "json_delete_order_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("sale_id", sale_id);
        params.put("status","3");

        // params.put("user_id", user_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.DELETE_ORDER_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String msg = response.getString("message");
                        Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT).show();

                        // ((MainActivity) getApplicationContext()).onBackPressed();
                        Intent i = new Intent(OrderDetail.this,MainActivity.class);
                        startActivity(i);
                        Mark_Delivered.setVisibility(View.VISIBLE);
                        picked_store.setVisibility(View.GONE);
                        cancel.setVisibility(View.GONE);


                    } else {
                        String error = response.getString("error");
                        Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }

    private void makeGetOrderDetailRequest(String sale_id) {
        String tag_json_obj = "json_order_detail_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("sale_id", sale_id);

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                BaseURL.OrderDetail, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<My_order_detail_model>>() {
                }.getType();

                my_order_detail_modelList = gson.fromJson(response.toString(), listType);

                My_order_detail_adapter adapter = new My_order_detail_adapter(my_order_detail_modelList);
                rv_detail_order.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (my_order_detail_modelList.isEmpty()) {
                    Toast.makeText(OrderDetail.this, getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(OrderDetail.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


}
