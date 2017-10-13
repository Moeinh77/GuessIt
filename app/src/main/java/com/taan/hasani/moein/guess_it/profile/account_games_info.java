package com.taan.hasani.moein.guess_it.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.taan.hasani.moein.guess_it.appcontroller.AppController;
import com.taan.hasani.moein.volley.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;


public class account_games_info extends Fragment {


    HashMap<String, String> info = new HashMap<>();
    String url = "http://online6732.tk/guessIt.php";
    String recived_games_id;
    String Playerscores, Rivalscore;
    String[] array = null;
    ListView listView;
    ArrayList<gameHistory_object> scoreList = new ArrayList<>();//*************
    gameHistory_object gameHistory_object;
    ProgressBar progressBar;


    public account_games_info() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View games_info = inflater.inflate(R.layout.fragment_account_games_info, container, false);
        listView = (ListView) games_info.findViewById(R.id.listView);
        progressBar = (ProgressBar) games_info.findViewById(R.id.progressBar2);
        get_user_games_ids();


        return games_info;
    }

    private void getUserGamesInfo(String gameID) {

        final String MY_PREFS_NAME = "username and password";
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME,
                MODE_PRIVATE);
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

                    gameHistory_object = new gameHistory_object();
                    if (id.equals(response.getString("playerOneID"))) {

                        Playerscores = response.getString("playerOneTotalScore");
                        Rivalscore = response.getString("playerTwoTotalScore");

                        gameHistory_object.setRivalUsername(response.getString("playerTwoUsername"));
                        gameHistory_object.setPlayerScore(Playerscores);
                        gameHistory_object.setRivalScore(Rivalscore);

                    } else {

                        Playerscores = response.getString("playerTwoTotalScore");
                        Rivalscore = response.getString("playerTwoTotalScore");

                        gameHistory_object.setRivalUsername(response.getString("playerOneUsername"));
                        gameHistory_object.setPlayerScore(Playerscores);
                        gameHistory_object.setRivalScore(Rivalscore);

                    }

                    scoreList.add(gameHistory_object);//*************

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }


    public void get_user_games_ids() {

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
                    scoreList = new ArrayList<>();
                    recived_games_id = new String(response.getString("games").getBytes("ISO-8859-1"), "UTF-8");

                    array = recived_games_id.split(", ");

                    //id hame bazi ha dar array hast inja
                    // miaym har khoone az array ro mifresim be oon yeki
                    //function ta etelaat ro bar asas id biare va hamaro bokone ye string ta too ye view bezarim
                    for (int i = 0; i < array.length; i++) {
                        getUserGamesInfo(array[i]);
                    }
                    ////////////////////////


                    startUpTheList(array.length);


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

    }

    public void startUpTheList(final int n) {
        if (scoreList.size() != n) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    startUpTheList(n);
                }
            }, 1000);//choon  gereftan etelaat user ba request async ast hame hamzaman daryaft nemishavand pas
            //ma hey check mikonim ta zmani ke hame etelaat amade bashad ba list ro be listview midahim

        } else {
            listViewAdapter_gamesInfo listViewAdapter_gamesInfo = new listViewAdapter_gamesInfo
                    (getActivity(), R.layout.score_row, scoreList);
            progressBar.setVisibility(View.INVISIBLE);
            listView.setAdapter(listViewAdapter_gamesInfo);
            listViewAdapter_gamesInfo.notifyDataSetChanged();
        }

    }
}

