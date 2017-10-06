package com.taan.hasani.moein.guess_it.game_menu;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.taan.hasani.moein.guess_it.Leader_board.Leader_Board;
import com.taan.hasani.moein.guess_it.appcontroller.AppController;
import com.taan.hasani.moein.guess_it.game.categories_singlePlayer;
import com.taan.hasani.moein.guess_it.game.choosing_theGame;
import com.taan.hasani.moein.guess_it.game.single_Player;
import com.taan.hasani.moein.guess_it.profile.profile;
import com.taan.hasani.moein.volley.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Main_menu extends AppCompatActivity {

    Button profile_bt, games_bt, Leader_board_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        profile_bt = (Button) findViewById(R.id.profile_bt);
        games_bt = (Button) findViewById(R.id.games_bt);
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
                Intent i = new Intent(Main_menu.this, profile.class);
                startActivity(i);
            }
        });

        games_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main_menu.this, choosing_theGame.class);
                startActivity(i);
            }
        });
    }


    @Override
    public void onBackPressed() {

//        AlertDialog.Builder alertB=new AlertDialog.Builder(getApplicationContext());
//        alertB.setTitle("Exit");
//        alertB.setIcon(R.drawable.exiticon);
//        alertB.setPositiveButton("بلی", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//
//                finish();
//            }
//        });
//
//        alertB.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        alertB.setMessage("آیا می خواهید از بازی خارج شوید؟");
//
//        alertB.create().show();
        alert_dialog_function();
    }


    public void alert_dialog_function() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.exit_dialog);
        dialog.setCancelable(true);

        dialog.setTitle("Exit");


        Button yes = (Button) dialog.findViewById(R.id.yes);
        Button no = (Button) dialog.findViewById(R.id.no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


        dialog.show();

    }


    @Override
    protected void onDestroy() {

        SharedPreferences prefs = getSharedPreferences("username and password", MODE_PRIVATE);
        String id = prefs.getString("userID", null);

        final HashMap<String, String> info = new HashMap<>();
        final String url = "http://online6732.tk/guessIt.php";

        info.put("action", "logout");
        info.put("userID", id);
        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("dataIsRight").equals("yes")) {

                        Log.v("", response.getString("dataIsRight"));

                        Toast.makeText(getApplicationContext(),
                                "Logged out successfully ", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "There was error in logging out...", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "There was error in logging out...", Toast.LENGTH_LONG).show();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

        super.onDestroy();

    }



}
