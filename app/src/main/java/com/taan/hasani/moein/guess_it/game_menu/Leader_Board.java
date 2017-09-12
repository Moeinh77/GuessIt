package com.taan.hasani.moein.guess_it.game_menu;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.taan.hasani.moein.guess_it.appcontroller.AppController;
import com.taan.hasani.moein.volley.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Leader_Board extends AppCompatActivity {

    private TextView scores;
    private String MY_PREFS_NAME = "username and password";
    final HashMap<String, String> info = new HashMap<>();
    String url = "http://online6732.tk/guessIt.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader__board);

        scores = (TextView) findViewById(R.id.scores);


        get_scores();


    }

    public void get_scores() {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String userID = prefs.getString("userID", null);

        info.put("action", "sendListOfTopUsers");
        info.put("userID", userID);

        JSONObject jsonObject = new JSONObject(info);
//        JSONArray jsonArray;
//
//        try {
//            jsonArray=new JSONArray(info);
//            JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.POST, url, jsonArray, new Response.Listener<JSONArray>() {
//                @Override
//                public void onResponse(JSONArray response) {
//
//
//
//
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                }
//            });
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray_scores = response.getJSONArray("listOfTopUsers");

                    String scores_fromJsonArray = "";

//                    for (int i = 0; i < jsonArray_scores.length(); i++) {
//
//                        scores_fromJsonArray = jsonArray_scores.getJSONObject(i).getString("position") + "\t" +
//                                jsonArray_scores.getJSONObject(i).getString("username") + "\t" +
//                                jsonArray_scores.getJSONObject(i).getString("totalScore") + "\n";
//
//                    }

                    scores.setText(jsonArray_scores.toString());


                } catch (JSONException e) {

                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }

            }

        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "get_scores $$$" + error.toString(), Toast.LENGTH_LONG).show();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }
}
