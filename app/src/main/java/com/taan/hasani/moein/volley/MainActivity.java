package com.taan.hasani.moein.volley;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    String url = "http://online6732.tk/guessIt.php";

    private Button login_bt;
    private Button signup_bt;
    private Button gameinfo_bt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameinfo_bt = (Button) findViewById(R.id.game_info);
        login_bt = (Button) findViewById(R.id.login);
        signup_bt = (Button) findViewById(R.id.signup);

        signup_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_up();
            }
        });

        gameinfo_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game_info();
            }
        });

        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }


    public void game_info() {
        Intent i = new Intent(MainActivity.this, game_info.class);
        startActivity(i);
    }

    public void login() {

        Intent i = new Intent(MainActivity.this, Login.class);
        startActivity(i);
    }

    public void sign_up() {

        Intent i = new Intent(MainActivity.this, SignUp.class);
        startActivity(i);

    }
}



