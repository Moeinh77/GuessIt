package com.taan.hasani.moein.volley;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    String url = "http://jsonplaceholder.typicode.com/users";
    HashMap<String, String> info = new HashMap<String, String>();
    private String password;
    private String username;
    private String name;
    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info.put("password",password);
        info.put("username",username);
        info.put("name",name);
        jsonObject=new JSONObject(info);


        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,
                url, jsonObject,new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }
}



