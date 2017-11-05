package com.taan.hasani.moein.guess_it.helpingclasses;

import android.app.Activity;
import android.content.Context;
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


public class Functions {

    private String url = "http://online6732.tk/guessIt.php";
    private String game_ID;
    private Boolean gameset_flag;
    private Activity activity;

    public Functions(Activity activity) {

        this.activity = activity;
    }

    public String f_newSinglePlayerGame(final String id, final String type) {


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

                    //    Toast.makeText(getApplicationContext(),
                    //          response.toString(), Toast.LENGTH_LONG).show();

                    if (response.getString("dataIsRight").equals("yes")) {

                        game_ID = response.getString("gameID");

                    } else {

                        Toast.makeText(activity, " sth went wrong... trying again"
                                , Toast.LENGTH_SHORT).show();
                        game_ID = "";
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
        return game_ID;


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

                        Toast.makeText(activity, response.toString(), Toast.LENGTH_LONG).show();
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

