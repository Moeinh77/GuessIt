package com.taan.hasani.moein.volley.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.taan.hasani.moein.volley.R;

public class choosing_theGame extends AppCompatActivity {

    private Button singleplayer_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing_the_game);

        singleplayer_bt=(Button)findViewById(R.id.Single_Player);
        singleplayer_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(choosing_theGame.this,game_info.class);
                startActivity(i);
            }
        });

    }
}
