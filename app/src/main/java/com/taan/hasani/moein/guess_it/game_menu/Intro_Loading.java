package com.taan.hasani.moein.guess_it.game_menu;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.taan.hasani.moein.guess_it.helpingclasses.Player;
import com.taan.hasani.moein.volley.R;

public class Intro_Loading extends AppCompatActivity {

    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        player = new Player(this);

        int TIME_OUT = 500;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (player.getUsername() != null && player.getPassword() != null) {

                    player.app_reopening();

                } else {
                    Intent intent = new Intent(Intro_Loading.this, Entrance_signup_login.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, TIME_OUT);

    }
}
