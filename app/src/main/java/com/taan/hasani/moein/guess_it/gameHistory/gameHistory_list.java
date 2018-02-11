package com.taan.hasani.moein.guess_it.gameHistory;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.taan.hasani.moein.guess_it.Gson.userInfo_GSON;
import com.taan.hasani.moein.guess_it.appcontroller.AppController;
import com.taan.hasani.moein.guess_it.helpingclasses.Player;
import com.taan.hasani.moein.volley.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class gameHistory_list extends Fragment {

    String url = "http://mamadgram.tk/guessIt.php";
    ListView listView;
    private TextView message;
    ArrayList<gameHistory_object> scoreList;//*************
    HashMap<String, String> info = new HashMap<>();
    Player player;

    public gameHistory_list() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View games_info = inflater.inflate(R.layout.fragment_games_history, container, false);
        listView = (ListView) games_info.findViewById(R.id.listView);
        // progressBar = (ProgressBar) games_info.findViewById(R.id.progressBar2);

        player = new Player(getActivity());

        get_user_games_ids();
        message = (TextView) games_info.findViewById(R.id.message_history);
        message.setVisibility(View.INVISIBLE);

        return games_info;
    }

    public void get_user_games_ids() {
        info.put("action", "sendUserInformation");
        info.put("userID", player.getId());
        final JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    userInfo_GSON userInfo;
                    Gson gson = new Gson();
                    userInfo = gson.fromJson(response.getJSONObject("user").toString()
                            , userInfo_GSON.class);

                    scoreList = new ArrayList<>();

                    String recived_games_id = userInfo.games;
                    //   Toast.makeText(getActivity(),userInfo.username,Toast.LENGTH_LONG).show();

                    String[] array = recived_games_id.split(", ");

                    //id hame bazi ha dar array hast inja
                    // miaym har khoone az array ro mifresim be oon yeki
                    //function ta etelaat ro bar asas id biare va hamaro bokone ye string ta too ye view bezarim
                    for (int i = 0; i < array.length; i++) {
                        player.getUserGamesInfo(array[i]);
                    }
                    ////////////////////////
                    scoreList = player.getScoreList();

                    startUpTheList(array.length);

                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "history list" + e.toString(), Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "history " + error.toString(), Toast.LENGTH_LONG).show();
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
            //progressBar.setVisibility(View.INVISIBLE);
            listView.setAdapter(listViewAdapter_gamesInfo);
            listViewAdapter_gamesInfo.notifyDataSetChanged();
        }

    }
}

