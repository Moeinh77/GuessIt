package com.taan.hasani.moein.volley.game_menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.taan.hasani.moein.volley.R;
import com.taan.hasani.moein.volley.game.choosing_theGame;
import com.taan.hasani.moein.volley.profile_fragments.profile;

public class Main_menu extends AppCompatActivity {

    Button profile_bt,games_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        profile_bt=(Button)findViewById(R.id.profile_bt);
        games_bt=(Button)findViewById(R.id.games_bt);

        profile_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Main_menu.this,profile.class);
                startActivity(i);
            }
        });

        games_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Main_menu.this,choosing_theGame.class);
                startActivity(i);
            }
        });
    }
}
