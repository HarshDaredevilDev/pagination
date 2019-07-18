package com.example.com.zylatest;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TaskOne extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView pageNumber;
    PageAdapter adapter;
    ArrayList<Model> list;

    String nextUrl;
    String url1="";
    String lastUrl="";
    int page;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_one);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        pageNumber=(TextView)findViewById(R.id.page_number);
        list=new ArrayList<>();
        adapter=new PageAdapter(list);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);
        final RequestQueue requestQueue;

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        Network network = new BasicNetwork(new HurlStack());

        requestQueue = new RequestQueue(cache, network);
        RequestQueue queue = Volley.newRequestQueue(this);

        requestQueue.start();


        String url="http://address.app.13.126.61.89.xip.io/api/v1/address/pincode";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // textView.setText("Response: " + response.toString());
                        int pageCounter=1;

                        try {
                            JSONObject obj=response.getJSONObject("meta");
                            JSONObject link=response.getJSONObject("links");
                            JSONObject last=link.getJSONObject("last");
                            JSONObject next=link.getJSONObject("next");


                            Log.e("total pages",obj.getString("total"));
                            Log.e("total pages",obj.getString("page"));
                            Log.e("total pages",last.getString("href"));
                            nextUrl=next.getString("href");

                            lastUrl=last.getString("href");
                            JSONArray arr=response.getJSONArray("items");
                            for(int i=0;i<arr.length();i++)
                            { Model model=new Model();

                                JSONObject pin=arr.getJSONObject(i);
                                model.setPinCode(pin.getString("pincode"));
                                model.setName(pin.getString("district"));

                                list.add(model);
                                adapter.notifyDataSetChanged();
                            }

                            page=obj.getInt("page");
                            pageNumber.setText("fetching page.."+page);
                            url1="http://address.app.13.126.61.89.xip.io/api/v1/"+next.getString("href");
                            Log.e("total pages","url1 is"+url1);




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.e("total pages",error.toString());

                    }
                });

        requestQueue.add(jsonObjectRequest);



        final Handler handler1 = new Handler();
        final int delay = 3000;

        handler1.postDelayed(new Runnable(){
            public void run(){
                if(!url1.equalsIgnoreCase(lastUrl)) {
                    JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest
                            (Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    int pageCounter = 1;

                                    try {
                                        JSONObject obj = response.getJSONObject("meta");
                                        JSONObject link = response.getJSONObject("links");
                                        JSONObject last = link.getJSONObject("last");
                                        JSONObject next1 = link.getJSONObject("next");

                                        url1 = "http://address.app.13.126.61.89.xip.io/api/v1/" + next1.getString("href");
                                        // url1="";

                                        page = obj.getInt("page");
                                        Log.e("total pages", next1.getString("href"));
                                        Log.e("total pages", url1);
                                        JSONArray arr = response.getJSONArray("items");
                                        pageNumber.setText("fetched page.."+page);

                                        for (int i = 0; i < arr.length(); i++) {
                                            Model model = new Model();

                                            JSONObject pin = arr.getJSONObject(i);
                                            model.setPinCode(pin.getString("pincode"));
                                            model.setName(pin.getString("district"));

                                            list.add(model);

                                            adapter.notifyDataSetChanged();
                                        }

                                        recyclerView.smoothScrollToPosition(list.size());

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO: Handle error
                                    Log.e("total pages", error.toString());

                                }
                            });

                    requestQueue.add(jsonObjectRequest1);

                }else{
                    Toast.makeText(getApplicationContext(), "end", Toast.LENGTH_SHORT).show();


                }
                handler1.postDelayed(this, delay);
            }
        }, delay);




    }
}
