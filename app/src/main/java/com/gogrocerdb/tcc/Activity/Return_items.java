package com.gogrocerdb.tcc.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.gogrocerdb.tcc.Adapter.Return_Adapter;
import com.gogrocerdb.tcc.AppController;
import com.gogrocerdb.tcc.Config.BaseURL;
import com.gogrocerdb.tcc.Model.My_order_detail_model;
import com.gogrocerdb.tcc.R;
import com.gogrocerdb.tcc.util.CustomVolleyJsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Return_items extends AppCompatActivity {
    private List<My_order_detail_model> my_order_detail_modelList = new ArrayList<>();
    RecyclerView rv_return;
    TextView tv_return;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_items);
        rv_return=findViewById(R.id.return_items_rv);
        rv_return.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        String sale_id;
        sale_id=getIntent().getStringExtra("sale");
        tv_return=findViewById(R.id.btn_return);

        makeGetOrderDetailRequest(sale_id);
        Toast.makeText(this, sale_id, Toast.LENGTH_SHORT).show();
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

                Return_Adapter adapter = new Return_Adapter(Return_items.this,my_order_detail_modelList,tv_return);
                rv_return.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (my_order_detail_modelList.isEmpty()) {
                    Toast.makeText(Return_items.this, getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(Return_items.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
}
