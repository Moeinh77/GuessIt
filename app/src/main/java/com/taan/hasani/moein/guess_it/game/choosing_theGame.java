package com.taan.hasani.moein.guess_it.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.taan.hasani.moein.volley.R;

public class choosing_theGame extends AppCompatActivity {

    private Button singlePlayer_bt, twoPlayer_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing_the_game);

        twoPlayer_bt = (Button) findViewById(R.id.twoPlayer_bt);

        twoPlayer_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(choosing_theGame.this, categories.class);
                startActivity(intent);

            }
        });

        singlePlayer_bt = (Button) findViewById(R.id.Single_Player);

        singlePlayer_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(choosing_theGame.this,single_Player.class);
                startActivity(i);
            }
        });

    }
}
