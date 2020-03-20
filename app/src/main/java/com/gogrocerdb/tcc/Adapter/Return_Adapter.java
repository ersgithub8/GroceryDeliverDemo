package com.gogrocerdb.tcc.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gogrocerdb.tcc.Activity.OrderDetail;
import com.gogrocerdb.tcc.AppController;
import com.gogrocerdb.tcc.Config.BaseURL;
import com.gogrocerdb.tcc.Model.My_order_detail_model;
import com.gogrocerdb.tcc.R;
import com.gogrocerdb.tcc.util.CustomVolleyJsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Return_Adapter  extends RecyclerView.Adapter<Return_Adapter.MyViewHolder> {

    private List<My_order_detail_model> modelList;
    private Context context;
    private TextView textView;
    private ArrayList<String> arrayList=new ArrayList<String>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title, tv_price, tv_qty;
        public ImageView iv_img;
        private CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_order_Detail_title);
            tv_price = (TextView) view.findViewById(R.id.tv_order_Detail_price);
            tv_qty = (TextView) view.findViewById(R.id.tv_order_Detail_qty);
            iv_img = (ImageView) view.findViewById(R.id.iv_order_detail_img);
            checkBox =(CheckBox) view.findViewById(R.id.checkBox);

        }
    }

    public Return_Adapter(Context context,List<My_order_detail_model> modelList, TextView tv_return) {
        this.modelList = modelList;
        this.textView=tv_return;
        this.context=context;
    }

    @Override
    public Return_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_return_item, parent, false);

        context = parent.getContext();

        return new Return_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Return_Adapter.MyViewHolder holder, int position) {
        final My_order_detail_model mList = modelList.get(position);

        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + mList.getProduct_image())
                .centerCrop()
                .placeholder(R.drawable.newdownload)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.iv_img);

        holder.tv_title.setText(mList.getProduct_name());
        holder.tv_price.setText(mList.getPrice());
        holder.tv_qty.setText(mList.getQty());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (holder.checkBox.isChecked()){
                    String temp= mList.getProduct_id();
                    arrayList.add(temp);

                }else {
                    String temp= mList.getProduct_id();
                    arrayList.remove(temp);
                }
            }
        });

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        makeGetreturn(mList.getSale_id(),arrayList);
//                        Toast.makeText(context,arrayList.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
    public void makeGetreturn(String sale_id,ArrayList<String> temp1) {

            String tag_json_obj = "json_product_req";
        String s1=String.valueOf(temp1);
        String replace = s1.replace("[","");
        String replace1 = replace.replace("]","");
            Map<String, String> params = new HashMap<String, String>();

            params.put("sale_id",sale_id);
            params.put("product_ids",replace1);
        Toast.makeText(context,replace1, Toast.LENGTH_SHORT).show();
            // Toast.makeText(getActivity(), Store_id, Toast.LENGTH_SHORT).show();

            CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                    BaseURL.GET_return_ORDER_URL, params, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {


                    try {


                       Boolean status = response.getBoolean("response");
                    Toast.makeText(context,String.valueOf(status), Toast.LENGTH_SHORT).show();

                        if(status) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<My_order_detail_model>>() {
                            }.getType();
//
                            Intent i=new Intent(context, OrderDetail.class);
                            context.startActivity(i);
//                            area_modelList = gson.fromJson(response.getString("data"), listType);
//
//
//                            area_adapter = new Area_Adapter(area_modelList);
//                            rv_socity.setAdapter(area_adapter);
//                            area_adapter.notifyDataSetChanged();
//                        }else{
//                            Session_management sessionManagement = new Session_management(getActivity());
//                            String area_name="choose area";
//                            String area_id= null;
//                            sessionManagement.updateArea(area_name,area_id);
//                            Toast.makeText(getActivity(), "No record found", Toast.LENGTH_SHORT).show();
                        }
//
//                    if(area_modelList.isEmpty()){
//                        if(getActivity() != null) {
//                            Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
//                        }
//                    }
////
                    } catch (JSONException e) {
                        e.printStackTrace();
//                    progressDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                        Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
                    }
                }
            });
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        }

}
