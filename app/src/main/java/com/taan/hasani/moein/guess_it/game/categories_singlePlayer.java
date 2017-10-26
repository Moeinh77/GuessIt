package com.taan.hasani.moein.guess_it.game;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.taan.hasani.moein.volley.R;

public class categories_singlePlayer extends AppCompatActivity {

    Button varzeshi, english, bazigar, film;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        varzeshi = (Button) findViewById(R.id.varzeshi);
        english = (Button) findViewById(R.id.english);
        bazigar = (Button) findViewById(R.id.bazigar);
        film = (Button) findViewById(R.id.film);

        //////////////////////////
        varzeshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert_dialog_function("1");

            }
        });
        //////////////////////////
        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert_dialog_function("2");

            }
        });
        //////////////////////////
        film.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert_dialog_function("3");

            }
        });
        //////////////////////////
        bazigar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert_dialog_function("4");

            }
        });
        //////////////////////////

    }

    public void alert_dialog_function(final String category) {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.difficulty_dialog);
        dialog.setCancelable(true);

        final RadioButton fast_RadioButton = (RadioButton) dialog.findViewById(R.id.fast);
        final RadioButton slow_RadioButton = (RadioButton) dialog.findViewById(R.id.slow);

        Button start = (Button) dialog.findViewById(R.id.start);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int totalwordsnumber = 13;
                String rIsChecked = "false";
                String type = "fast";

                if (fast_RadioButton.isChecked()) {

                    type = "fast";
                    rIsChecked = "true";
                    totalwordsnumber = 13;
                }
                if (slow_RadioButton.isChecked()) {

                    type = "slow";
                    rIsChecked = "true";
                    totalwordsnumber = 7;

                }


                Intent i = new Intent(categories_singlePlayer.this, single_Player.class);

                if (rIsChecked.equals("true")) {

                    i.putExtra("category", category);
                    i.putExtra("type", type);
                    i.putExtra("totalwordsnumber", totalwordsnumber);

                    startActivity(i);
                    dialog.cancel();

                } else {
                    Toast.makeText(getApplicationContext(), "حالت بازی را انتخاب نکردید", Toast.LENGTH_LONG).show();
                }


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


        dialog.show();

    }
}
