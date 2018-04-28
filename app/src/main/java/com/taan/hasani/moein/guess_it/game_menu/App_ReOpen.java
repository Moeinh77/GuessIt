package com.taan.hasani.moein.guess_it.game_menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.taan.hasani.moein.guess_it.Gson.login_GSON;
import com.taan.hasani.moein.guess_it.Gson.simpleRequest_GSON;
import com.taan.hasani.moein.guess_it.Gson.userInfo_GSON;
import com.taan.hasani.moein.guess_it.Service.getInfo;
import com.taan.hasani.moein.guess_it.appcontroller.AppController;
import com.taan.hasani.moein.guess_it.helpingclasses.Player;
import com.taan.hasani.moein.volley.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class App_ReOpen extends AppCompatActivity {

    private Player player;
    static public Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        activity = this;

        player = new Player();

        int TIME_OUT = 1000;

        Intent intent = new Intent(this, getInfo.class);
        startService(intent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (player.getToken() != null) {

                    Intent i = new Intent(App_ReOpen.this, Main_menu.class);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(),
                            "your user ### " + player.getuserName(), Toast.LENGTH_LONG).show();
                    finish();

                } else {
                    Intent intent = new Intent(App_ReOpen.this, Entrance_signup_login.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, TIME_OUT);

    }


    //Gsonized !!!
//    public void autoLogin() {
//
//        String url = "http://mamadgram.tk/guessIt_2.php";
//
//        final HashMap<String, String> info = new HashMap<>();
//
//        info.put("action", "login");
//        info.put("username", player.getUsername());
//        info.put("password", player.getPassword());
//
//        final JSONObject jsonObject = new JSONObject(info);
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
//                url, jsonObject, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                login_GSON loginGson;
//                Gson gson = new Gson();
//
//                loginGson = gson.fromJson(response.toString()
//                        , login_GSON.class);
//                if (loginGson.dataIsRight.equals("yes")) {
//
//                        //setrole(user.role);//baraye har bar baz shodan naghshe
//                        // kar bar ra update mikonad
//
//                        //opens the gamechoose activity
//                        Intent i = new Intent(App_ReOpen.this, Main_menu.class);
//                        startActivity(i);
//                        Toast.makeText(getApplicationContext(),
//                                "welcome " , Toast.LENGTH_LONG).show();
//                        finish();
//
//                } else {
//                    Toast.makeText(getApplicationContext(),
//                            "Some thing went wrong login!!!", Toast.LENGTH_LONG).show();
//                }
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(activity,
//                        "ارتباط با اینترنت پیدا نشد ...", Toast.LENGTH_LONG).show();
//                finish();
//
//            }
//        });
//
//        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
//
//    }


}
