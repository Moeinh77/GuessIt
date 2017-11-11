package com.taan.hasani.moein.guess_it.game_play;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.taan.hasani.moein.volley.R;

public class categories_twoPlayer extends AppCompatActivity {

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
        dialog.setTitle("This is my custom dialog box");
        dialog.setCancelable(true);

        final RadioButton easy_RadioButton = (RadioButton) dialog.findViewById(R.id.slow);
        final RadioButton hard_RadioButton = (RadioButton) dialog.findViewById(R.id.fast);


        final Button start = (Button) dialog.findViewById(R.id.start);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String difficulty = "2";
                String rIsChecked = "false";


                if (easy_RadioButton.isChecked()) {
                    difficulty = "1";//easy_RadioButton.getText().toString();
                    rIsChecked = "true";

                }
                if (hard_RadioButton.isChecked()) {
                    difficulty = "3";
                    rIsChecked = "true";

                }

                Intent i = new Intent(categories_twoPlayer.this, two_player.class);

                if (rIsChecked.equals("true")) {

                    i.putExtra("category", category);
                    i.putExtra("type", difficulty);//ferestadane noe bazi


                    startActivity(i);
                    dialog.cancel();
                } else {
                    Toast.makeText(getApplicationContext(), "درجه سطحی را انتخاب کنید", Toast.LENGTH_LONG).show();
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
