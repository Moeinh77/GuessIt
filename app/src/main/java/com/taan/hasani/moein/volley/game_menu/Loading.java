package com.taan.hasani.moein.volley.game_menu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.taan.hasani.moein.volley.R;
import com.taan.hasani.moein.volley.appcontroller.AppController;
import com.taan.hasani.moein.volley.game.choosing_theGame;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Loading extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        final String MY_PREFS_NAME="username and password";

        //check for login
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String username = prefs.getString("usename", null);
        String password = prefs.getString("password", null);
        if (username != null &&  password!=null) {

            // Toast.makeText(getApplicationContext(),username+","+password,Toast.LENGTH_LONG).show();
            sending_info(username,password);

        }
    }


    public void sending_info(String username_, String password_){
        final HashMap<String, String> info = new HashMap<>();
        String url = "http://online6732.tk/guessIt.php";

        info.put("action","login");
        info.put("username",username_);
        info.put("password",password_);

        JSONObject jsonObject=new JSONObject(info);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,
                url, jsonObject,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    if(response.getString("dataIsRight").equals("yes")){
                        //opens the gamechoose activity
                        Intent i=new Intent(Loading.this, choosing_theGame.class);
                        startActivity(i);
                        finish();
                    }

                    else{
                        Toast.makeText(getApplicationContext(),
                                response.getString("dataIsRight"),Toast.LENGTH_LONG).show();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "Error login",Toast.LENGTH_LONG).show();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }

}
