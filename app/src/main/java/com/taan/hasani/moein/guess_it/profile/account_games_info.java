package com.taan.hasani.moein.guess_it.profile;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.taan.hasani.moein.volley.R;
import com.taan.hasani.moein.guess_it.appcontroller.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;


public class account_games_info extends Fragment {


    HashMap<String, String> info = new HashMap<>();
    String url = "http://online6732.tk/guessIt.php";
    private TextView games_info_textview;
    String recived_games_id;
    String game_scores_string = "", scores = "";
    String[] array = null;


    public account_games_info() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        get_user_games_ids();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View games_info = inflater.inflate(R.layout.fragment_account_games_info, container, false);
        games_info_textview = (TextView) games_info.findViewById(R.id.games_info);
        games_info_textview.setBackgroundColor(Color.parseColor("#BABABA")); // set any custom color as background color



        //
        //games_info_textview.setText(games_array[0]);

        return games_info;
    }
    ////////////////////////////////////////////////////

    ////////////////////////////////////////////////////
    private void getUserGamesInfo(String gameID) {
        //   final String scores = new String();

        final String MY_PREFS_NAME = "username and password";
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final String id = prefs.getString("userID", null);


        info.put("action", "sendGameInformation");
        info.put("userID", id);
        info.put("gameID", gameID);

        JSONObject jsonObject = new JSONObject(info);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if (id.equals(response.getString("playerOneID"))) {

                        scores += "\n" + response.getString("playerOneTotalScore");

                    } else {

                        scores += "\n" + response.getString("playerTwoTotalScore");

                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }

                games_info_textview.setMovementMethod(new ScrollingMovementMethod());
                games_info_textview.setText(scores);


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        // return scores;
    }

    ///////////////////////////////////////////////////////

    public String[] get_user_games_ids() {

        //getting id for the player
        final String MY_PREFS_NAME = "username and password";
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String id = prefs.getString("userID", null);
        //////////////////////////////////////////
        info.put("action", "sendUserInformation");
        info.put("userID", id);
        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    recived_games_id = new String(response.getString("games").getBytes("ISO-8859-1"), "UTF-8");
                    //Snackbar.make(getView(), recived_games_id, Snackbar.LENGTH_LONG).show();

                    array=recived_games_id.split(", ");

                    //id hame bazi ha dar array hast inja miaym har khoone az array ro mifresim be oon yeki
                    //function ta etelaat ro bar asas id biare va hamaro bokone ye string ta too ye view bezarim
                    for (int i=0;i<array.length;i++) {
                        // game_scores_string +=
                        getUserGamesInfo(array[i]);
                    }
                     ////////////////////////
                    Toast.makeText(getActivity().getApplication(), scores, Toast.LENGTH_SHORT).show();



                } catch (JSONException e) {
                    Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException e) {
                    Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

        return array;
    }


}
