package com.taan.hasani.moein.guess_it.Leader_board;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.taan.hasani.moein.guess_it.appcontroller.AppController;
import com.taan.hasani.moein.guess_it.game.categories_singlePlayer;
import com.taan.hasani.moein.guess_it.game.categories_twoPlayer;
import com.taan.hasani.moein.volley.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class leader_board_fragment extends Fragment {

    private TextView scores_names, scores_score, yourPlace, wordScore_textview;
    private String MY_PREFS_NAME = "username and password";
    final HashMap<String, String> info = new HashMap<>();
    String url = "http://online6732.tk/guessIt.php";

    public leader_board_fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View leader = inflater.inflate(R.layout.fragment_leader_board_fragment, container, false);


        scores_names = (TextView) leader.findViewById(R.id.scores_p1_frag);
        scores_score = (TextView) leader.findViewById(R.id.scores_p2_frag);
        yourPlace = (TextView) leader.findViewById(R.id.your_place_frag);
        wordScore_textview = (TextView) leader.findViewById(R.id.wordScore_frag);
        // numbers_textview=(TextView) findViewById(R.id.numbers);

        get_scores();

        return leader;
    }

    public void get_scores() {

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
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

                    String scores_names_fromJsonArray = "";
                    String scores_score_fromJsonArray = "";
                    String wordScore = "";
                    // String numbers="";

                    for (int i = 0; i < jsonArray_scores.length(); i++) {

                        scores_names_fromJsonArray += "\n\n" +
                                jsonArray_scores.getJSONObject(i).getString("position") + ". " +
                                jsonArray_scores.getJSONObject(i).getString("username") + "\t\t" + "\n";


                        scores_score_fromJsonArray += "\n\n" +
                                jsonArray_scores.getJSONObject(i).getString("totalScore") + "\n";


                        wordScore += "\n\n" + "  Score : " + "\n";
                    }

                    scores_names.setText(scores_names_fromJsonArray);

                    scores_score.setText(scores_score_fromJsonArray);

                    wordScore_textview.setText(wordScore);


                    yourPlace.setText(response.getString("yourPosition"));

                } catch (JSONException e) {

                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                }

            }

        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        "get_scores $$$" + error.toString(), Toast.LENGTH_LONG).show();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }
}
