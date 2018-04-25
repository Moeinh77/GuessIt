package com.taan.hasani.moein.guess_it.helpingclasses;

import android.app.Activity;
import android.os.Handler;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.taan.hasani.moein.guess_it.appcontroller.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;


public class gameplayFunctions {

    private String url = "http://mamadgram.tk/guessIt.php";
    private Boolean gameset_flag;
    private Activity activity;

    private Player player;

    public gameplayFunctions(Activity activity) {

        this.activity = activity;
        player = new Player();

    }

    // public String getGame_ID() {
//        return game_ID;
//    }


    public void addWordtoDB(final String completeWord, String new_incompleteWord
            , JSONObject recievedWord_Jsonobj) {

        HashMap<String, String> info = new HashMap<>();

        try {
            recievedWord_Jsonobj.put("incompleteWord", new_incompleteWord.trim());
            recievedWord_Jsonobj.put("word", completeWord.trim());
            info.put("word", recievedWord_Jsonobj.toString());
            // Toast.makeText(activity, recievedWord_Jsonobj.toString(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /////////////////////////
        info.put("action", "addWord");
        info.put("username", player.getUsername());//$$$$$$$$$$$$$$$$$$$$$$$$  changed from userId to username $$$$$$$$$$$$$$$$$$$$$$$$

        /////////////////////////

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(activity,
//                       response.toString(), Toast.LENGTH_LONG).show();

                Toast.makeText(activity,
                        "\"" + completeWord + "\"" + "با موفقیت اضافه شد !", Toast.LENGTH_LONG).show();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity,
                        "addword***Volley  :" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void setAnswer(String game_ID, String entered_word, String player_time,
                          String player_score, String turn) {


        HashMap<String, String> info = new HashMap<>();
        HashMap<String, String> answer_hashmap = new HashMap<>();
        /////////////////////////
        answer_hashmap.put("time", player_time);
        answer_hashmap.put("score", player_score);
        answer_hashmap.put("answer", entered_word);
        answer_hashmap.put("myTurn", turn);

        JSONObject answer = new JSONObject(answer_hashmap);

        /////////////////////////
        info.put("action", "setAnswer");
        info.put("gameID", game_ID);
        info.put("username", player.getUsername());
        info.put("answer", answer.toString());
        /////////////////////////

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                // Toast.makeText(getApplicationContext(),
                //       "setAnswer response  :" + response.toString(), Toast.LENGTH_SHORT).show();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity,
                        "setAnswer***Volley  :" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }


    public void f_newSinglePlayerGame(final String id, final String type) {

        final HashMap<String, String> info = new HashMap<>();

        info.put("action", "newGame");
        info.put("mode", "singlePlayer");
        info.put("userID", id);
        info.put("type", type);

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getString("dataIsRight").equals("yes")) {

                        //game_ID = response.getString("gameID");

                    } else {

                        Toast.makeText(activity, " sth went wrong... ", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    Toast.makeText(activity,
                            "newSinglePlayerGame " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity,
                        "*newSinglePlayerGame**Volley  :" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);


    }

    public Boolean f_setGameSettings(String id, String game_ID,
                                     String difficulty, String category) {

        HashMap<String, String> info = new HashMap<>();

        info.put("action", "setGameSetting");
        info.put("userID", id);
        info.put("gameID", game_ID);
        info.put("level", difficulty);
        try {
            info.put("categories", URLEncoder.encode(category, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(activity, "UnsupportedEncodingException", Toast.LENGTH_SHORT).show();
        }

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getString("dataIsRight").equals("yes")) {

                        gameset_flag = true;

                    } else {

                        Toast.makeText(activity, "settings ***" + response.toString(),
                                Toast.LENGTH_LONG).show();

                        gameset_flag = false;
                    }

                } catch (JSONException e) {
                    Toast.makeText(activity,
                            e.toString(), Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity,
                        "setGameSetting***Volley  :" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

        return gameset_flag;

    }

}

