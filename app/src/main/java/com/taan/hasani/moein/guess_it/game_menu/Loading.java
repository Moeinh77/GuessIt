package com.taan.hasani.moein.guess_it.game_menu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.taan.hasani.moein.guess_it.helpingclasses.Player;
import com.taan.hasani.moein.volley.R;
import com.taan.hasani.moein.guess_it.appcontroller.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Loading extends AppCompatActivity {

    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.Loading_intro);
//        LottieDrawable drawable = new LottieDrawable();
//        LottieComposition.Factory.fromAssetFileName(getApplicationContext(),
//                "hello-world.json", (composition) -> {
//            drawable.setComposition(composition);
//        });

        player = new Player(this);

        int TIME_OUT = 500;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (player.getUsername() != null && player.getPassword() != null) {

                    player.app_reopening();

                } else {
                    Intent intent=new Intent(Loading.this,Entrance_signup_login.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, TIME_OUT);

    }
}
