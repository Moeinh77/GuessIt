package com.taan.hasani.moein.guess_it.game_menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.taan.hasani.moein.guess_it.profile.profile;
import com.taan.hasani.moein.volley.R;
import com.taan.hasani.moein.guess_it.game.choosing_theGame;

public class Main_menu extends AppCompatActivity {

    Button profile_bt, games_bt, Leader_board_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        profile_bt=(Button)findViewById(R.id.profile_bt);
        games_bt=(Button)findViewById(R.id.games_bt);
        Leader_board_bt = (Button) findViewById(R.id.leader_board);

        Leader_board_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main_menu.this, Leader_Board.class);
                startActivity(i);
            }
        });

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
