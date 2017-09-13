package com.taan.hasani.moein.guess_it.game_menu;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
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

    private TextView scores, yourPlace;
    private String MY_PREFS_NAME = "username and password";
    final HashMap<String, String> info = new HashMap<>();
    String url = "http://online6732.tk/guessIt.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader__board);

        scores = (TextView) findViewById(R.id.scores);
        yourPlace = (TextView) findViewById(R.id.your_place);

        get_scores();

    }

    public void get_scores() {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String userID = prefs.getString("userID", null);

        info.put("action", "sendListOfTopUsers");
        info.put("userID", userID);

        JSONObject jsonObject = new JSONObject(info);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray_scores = response.getJSONArray("listOfTopUsers");

                    String scores_fromJsonArray = "";

                    for (int i = 0; i < jsonArray_scores.length(); i++) {

                        scores_fromJsonArray += "\n\n" + jsonArray_scores.getJSONObject(i).getString("position") + ". " +
                                jsonArray_scores.getJSONObject(i).getString("username") + "\t\t\t" + "Score : " + "\t\t" +
                                jsonArray_scores.getJSONObject(i).getString("totalScore") + "\n" + "________________________________________________";

                    }

                    scores.setText(scores_fromJsonArray);
                    scores.setMovementMethod(new ScrollingMovementMethod());


                    yourPlace.setText(response.getString("yourPosition"));

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
