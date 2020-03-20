package com.gogrocerdb.tcc.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.gogrocerdb.tcc.Activity.LogInActivity;
import com.gogrocerdb.tcc.Adapter.My_Order_Adapter;
import com.gogrocerdb.tcc.AppController;
import com.gogrocerdb.tcc.Config.BaseURL;
import com.gogrocerdb.tcc.MainActivity;
import com.gogrocerdb.tcc.Model.My_order_model;
import com.gogrocerdb.tcc.R;
import com.gogrocerdb.tcc.util.CustomVolleyJsonArrayRequest;
import com.gogrocerdb.tcc.util.CustomVolleyJsonRequest;
import com.gogrocerdb.tcc.util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends Fragment {
    private static String TAG = Home.class.getSimpleName();
    private RecyclerView rv_myorder;
    private List<My_order_model> my_order_modelList = new ArrayList<>();
    private Session_management sessionManagement;
    String get_id;
    TextView tv_receiving,tv_commission;

    public Home() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,
                container, false);
        sessionManagement = new Session_management(getActivity());
        get_id = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
        rv_myorder = (RecyclerView) view.findViewById(R.id.rv_myorder);
        rv_myorder.setLayoutManager(new LinearLayoutManager(getActivity()));
        tv_receiving= view.findViewById(R.id.receiving);
        tv_commission= view.findViewById(R.id.commission);
        makejson_commission();
        makeGetOrderRequest();



        return view;
    }

    private void makeGetOrderRequest() {
        String tag_json_obj = "json_socity_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("d_id", get_id);

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                BaseURL.GET_ORDER_URL, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                 try {

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject obj = response.getJSONObject(i);
                        String saleid = obj.getString("sale_id");
                        String placedon = obj.getString("on_date");
                        String timefrom = obj.getString("delivery_time_from");
                        String timeto=obj.getString("delivery_time_from");
                        String item = obj.getString("total_items");
                        String ammount = obj.getString("total_amount");
                        String status = obj.getString("status");

                        String society = obj.getString("socity_name");
                        String house = obj.getString("house_no");
                        String rename = obj.getString("receiver_name");
                        String renumber = obj.getString("receiver_mobile");
                        String deliveryAddress = obj.getString("delivery_address");
                        String storelat = obj.getString("store_lat");
                        String storelong = obj.getString("store_lang");
                       // Toast.makeText(getActivity(), deliveryAddress, Toast.LENGTH_SHORT).show();
                        My_order_model my_order_model = new My_order_model();
                        my_order_model.setSocityname(society);
                        my_order_model.setHouse(house);
                        my_order_model.setRecivername(rename);
                        my_order_model.setRecivermobile(renumber);
                           my_order_model.setDelivery_time_from(timefrom);
                               my_order_model.setSale_id(saleid);
                               my_order_model.setOn_date(placedon);
                               my_order_model.setDelivery_time_to(timeto);
                              my_order_model.setTotal_amount(ammount);
                                 my_order_model.setStatus(status);
                                   my_order_model.setTotal_items(item);
                                   my_order_model.setDelivery_address(deliveryAddress);
                                   my_order_model.setStore_lat(storelat);
                                   my_order_model.setStore_lang(storelong);
                             my_order_modelList.add(my_order_model);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                My_Order_Adapter myPendingOrderAdapter = new My_Order_Adapter(my_order_modelList);
                rv_myorder.setAdapter(myPendingOrderAdapter);
                myPendingOrderAdapter.notifyDataSetChanged();


                if (my_order_modelList.isEmpty()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
    private void makejson_commission() {
       // String tag_json_obj = "json_socity_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_name", get_id);



        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET, BaseURL.COMMISSION + get_id
                , params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String Commision=response.getString("total_commission");
                    String reciving =response.getString("total_reciving");
                    //Toast.makeText(getActivity(), String.valueOf(response), Toast.LENGTH_SHORT).show();

//                    Toast.makeText(getActivity(), Commision, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getActivity(), reciving, Toast.LENGTH_SHORT).show();
                    tv_commission.setText(Commision);
                    tv_receiving.setText(reciving);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }




}
