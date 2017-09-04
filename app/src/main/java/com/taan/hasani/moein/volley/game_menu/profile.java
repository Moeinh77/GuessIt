package com.taan.hasani.moein.volley.game_menu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.taan.hasani.moein.volley.R;

public class profile extends AppCompatActivity {

    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        info=(TextView)findViewById(R.id.info);

       // info.setText();
    }
}
