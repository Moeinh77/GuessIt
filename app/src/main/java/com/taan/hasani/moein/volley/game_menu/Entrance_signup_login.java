package com.taan.hasani.moein.volley.game_menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.taan.hasani.moein.volley.R;

public class Entrance_signup_login extends AppCompatActivity {

    private Button login_bt;
    private Button signup_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_bt = (Button) findViewById(R.id.login);
        signup_bt = (Button) findViewById(R.id.signup);


        //////////////////////////////

        signup_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_up();
            }
        });


        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }


    public void login() {

        Intent i = new Intent(Entrance_signup_login.this, Login.class);
        startActivity(i);
        finish();
    }

    public void sign_up() {

        Intent i = new Intent(Entrance_signup_login.this, SignUp.class);
        startActivity(i);
        finish();

    }


}



