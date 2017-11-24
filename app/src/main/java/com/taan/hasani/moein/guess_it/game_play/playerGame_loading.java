package com.taan.hasani.moein.guess_it.game_play;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.taan.hasani.moein.guess_it.appcontroller.AppController;
import com.taan.hasani.moein.guess_it.helpingclasses.Player;
import com.taan.hasani.moein.volley.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class playerGame_loading extends Activity {

    private boolean inGame;
    private Player player;
    private String url = "http://online6732.tk/guessIt.php";
    private String gamedID, category, type;
    private TextView status_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_loading);

        status_textview = (TextView) findViewById(R.id.status_textview);

        inGame = true;
        player = new Player(this);

        //difficaulty va category ro az activity ghabl migirad
        Bundle bundle = getIntent().getExtras();
        category = bundle.getString("category");
        type = bundle.getString("type");
        //Toatalwords = bundle.getInt("totalwordsnumber");
        ///////////////////////////////////////////////////////
        newTwoPlayerGame();

    }

    public void newTwoPlayerGame() {

        //status_textview.setText("در حال ساخت بازی...");

        HashMap<String, String> info = new HashMap<>();

        info.put("action", "newGame");
        info.put("userID", player.getId());
        info.put("mode", "twoPlayer");
        info.put("type", type);

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {

                    // Toast.makeText(getApplicationContext(),
                    //        "newSingle #"+response.toString(), Toast.LENGTH_SHORT).show();

                    if (response.getString("gameID").equals("-1")) {

                        isMyGameReady();

                    } else {

                        gamedID = response.getString("gameID");//bayad ba intent montaghel beshe

                        setGameSettings();
                        Toast.makeText(getApplicationContext(),
                                gamedID, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "newTwoPlayerGame " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "*newTwoPlayerGame**Volley  :" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void isMyGameReady() {

        status_textview.setText("جستجو برای حریف...");

        HashMap<String, String> info = new HashMap<>();

        info.put("action", "isMyGameReady");
        info.put("userID", player.getId());

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {

                    //    Toast.makeText(getApplicationContext(),
                    //            "isMyGameReady #"+response.toString(), Toast.LENGTH_SHORT).show();
                    if (inGame) {
                        if (response.getString("gameID").equals("-1")) {


                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    isMyGameReady();
                                }
                            }, 3000);


                        } else {
                            gamedID = response.getString("gameID");
                            setGameSettings();
                        }
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "isMyGameReady***Volley  :" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    public void setGameSettings() {

        HashMap<String, String> info = new HashMap<>();

        info.put("action", "setGameSetting");
        info.put("userID", player.getId());
        info.put("gameID", gamedID);
        info.put("categories", category);

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getString("dataIsRight").equals("yes")) {

                        Intent intent = new Intent(playerGame_loading.this, two_player.class);
                        intent.putExtra("gamedID", gamedID);
                        startActivity(intent);
                        finish();

                    } else {

                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "setGameSetting " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "setGameSetting***Volley  :" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);


    }

    @Override
    protected void onDestroy() {

        inGame = false;
        super.onDestroy();
    }
}
