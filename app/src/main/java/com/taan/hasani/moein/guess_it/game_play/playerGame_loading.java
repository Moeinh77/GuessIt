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
import com.google.gson.Gson;
import com.taan.hasani.moein.guess_it.Gson.makingGame_GSON;
import com.taan.hasani.moein.guess_it.Gson.simpleRequest_GSON;
import com.taan.hasani.moein.guess_it.appcontroller.AppController;
import com.taan.hasani.moein.guess_it.helpingclasses.Player;
import com.taan.hasani.moein.volley.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class playerGame_loading extends Activity {

    private boolean inGame = true;//baraye inke agr az safe sakhte bazi
    // do nafare kharej shod digar false beshe va request nadahad

    private Player player;
    private String url = "http://mamadgram.tk/guessIt.php";
    private String gamedID, category, type;
    private TextView status_textview;//status sakhte bazi ra gozaresh midahad

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_loading);

        status_textview = (TextView) findViewById(R.id.status_textview);

        //inGame = true;

        player = new Player();

        //difficaulty va category ro az activity ghabl migirad ke activity category hast
        Bundle bundle = getIntent().getExtras();
        category = bundle.getString("category");
        type = bundle.getString("type");
        //Toatalwords = bundle.getInt("totalwordsnumber");
        ///////////////////////////////////////////////////////
        newTwoPlayerGame();

    }

    //GSONIZED !!!
    public void newTwoPlayerGame() {

        status_textview.setText("در حال ساخت بازی...");

        HashMap<String, String> info = new HashMap<>();

        info.put("action", "newGame");
        info.put("username", player.getuserName());//$$$$$$$$$$$$$$$$$$$$$$$$  changed from userId to username $$$$$$$$$$$$$$$$$$$$$$$$
        info.put("mode", "twoPlayer");
        info.put("type", type);

        if (inGame) {//agar az safe ijad bazi kharej shode digar request nadahad
            JSONObject jsonObject = new JSONObject(info);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    url, jsonObject, new Response.Listener<JSONObject>() {


                @Override
                public void onResponse(JSONObject response) {

                    Gson gson = new Gson();
                    makingGame_GSON makingGame;
                    makingGame = gson.fromJson(response.toString(),
                            makingGame_GSON.class);

                    gamedID = makingGame.gameID;//bayad ba intent montaghel beshe

                    if (gamedID.equals("-1")) {

                        isMyGameReady();

                    } else {

                        setGameSettings();
                        Toast.makeText(getApplicationContext(),
                                gamedID, Toast.LENGTH_SHORT).show();
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
    }

    //GSONIZED !!!
    public void isMyGameReady() {
        //  Toast.makeText(getApplicationContext(),"is my game",Toast.LENGTH_SHORT).show();

        status_textview.setText("جستجو برای حریف...");

        HashMap<String, String> info = new HashMap<>();

        info.put("action", "isMyGameReady");
        info.put("username", player.getuserName());//$$$$$$$$$$$$$$$$$$$$$$$$  changed from userId to username $$$$$$$$$$$$$$$$$$$$$$$$


        if (inGame) {//agar az safe ijad bazi kharej shode digar request nadahad
            //   Toast.makeText(getApplicationContext(),"is my game",Toast.LENGTH_SHORT).show();

            JSONObject jsonObject = new JSONObject(info);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    url, jsonObject, new Response.Listener<JSONObject>() {


                @Override
                public void onResponse(JSONObject response) {


                    Gson gson = new Gson();
                    final makingGame_GSON makingGame;
                    makingGame = gson.fromJson(response.toString(),
                            makingGame_GSON.class);

                    if (makingGame.gameID.equals("-1")) {
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                isMyGameReady();

                            }
                        }, 3000);

                    } else {
                        gamedID = makingGame.gameID;
                        setGameSettings();
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
    }

    //GSONIZED !!!
    public void setGameSettings() {

        HashMap<String, String> info = new HashMap<>();

        info.put("action", "setGameSetting");
        info.put("username", player.getuserName());//$$$$$$$$$$$$$$$$$$$$$$$$  changed from userId to username $$$$$$$$$$$$$$$$$$$$$$$$

        info.put("gameID", gamedID);
        info.put("categories", category);

        if (inGame) {//agar az safe ijad bazi kharej shode digar request nadahad
            JSONObject jsonObject = new JSONObject(info);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    url, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    Gson gson = new Gson();
                    simpleRequest_GSON request;
                    request = gson.fromJson(response.toString(),
                            simpleRequest_GSON.class);

                    if (request.dataIsRight.equals("yes")) {

                        Intent intent = new Intent(playerGame_loading.this, two_player.class);
                        intent.putExtra("gamedID", gamedID);
                        startActivity(intent);
                        finish();

                    } else {

                        //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();

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

    }

    private void stopsearch() {

        HashMap<String, String> info = new HashMap<>();

        info.put("action", "stopSearchForFriend");
        info.put("username", player.getuserName());//$$$$$$$$$$$$$$$$$$$$$$$$  changed from userId to username $$$$$$$$$$$$$$$$$$$$$$$$

        info.put("gameID", gamedID);

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                inGame = false;


                Gson gson = new Gson();
                simpleRequest_GSON request;
                request = gson.fromJson(response.toString(),
                        simpleRequest_GSON.class);

                // Toast.makeText(getApplicationContext(),request.dataIsRight,Toast.LENGTH_LONG).show();
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
        // handler.removeCallbacksAndMessages(null);

        stopsearch();
        super.onDestroy();
    }


}
