package com.taan.hasani.moein.guess_it.Leader_board;

import android.app.MediaRouteButton;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.taan.hasani.moein.guess_it.gameHistory.listViewAdapter_gamesInfo;
import com.taan.hasani.moein.guess_it.helpingclasses.Player;
import com.taan.hasani.moein.volley.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class leader_board_fragment extends Fragment {

    private ArrayList<leaderBoard_object> arrayList = new ArrayList<>();
    private Player player;
    private TextView yourPlace;
    final HashMap<String, String> info = new HashMap<>();
    String url = "http://online6732.tk/guessIt.php";
    private ListView listView;
    private ProgressBar progressBar;

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
        progressBar = (ProgressBar) leader.findViewById(R.id.progressBar_leaderboard);
        listView = (ListView) leader.findViewById(R.id.listview_leaderboard);
        yourPlace = (TextView) leader.findViewById(R.id.your_place_frag);

        player = new Player(getActivity());

        get_scores();


        return leader;
    }

    public void get_scores() {

        info.put("action", "sendListOfTopUsers");
        info.put("userID", player.getId());

        JSONObject jsonObject = new JSONObject(info);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray_scores = response.getJSONArray("listOfTopUsers");


                    for (int i = 0; i < jsonArray_scores.length(); i++) {

                        String place = jsonArray_scores.getJSONObject(i).getString("position");
                        String username = jsonArray_scores.getJSONObject(i).getString("username");
                        String score = jsonArray_scores.getJSONObject(i).getString("totalScore");

                        leaderBoard_object object = new leaderBoard_object();
                        object.setPlayer_place(place);
                        object.setPlayer_score(score);
                        object.setPlayer_username(username);

                        arrayList.add(object);

                    }

                    yourPlace.setText(response.getString("yourPosition"));


                    ListViewAdapter_leaderboard listViewAdapter_gamesInfo = new ListViewAdapter_leaderboard
                            (getActivity(), R.layout.leaderboard_row, arrayList);
                    progressBar.setVisibility(View.INVISIBLE);
                    listView.setAdapter(listViewAdapter_gamesInfo);
                    listViewAdapter_gamesInfo.notifyDataSetChanged();

                } catch (JSONException e) {

                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();

                }

            }

        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        "leaderBoard $$$" + error.toString(), Toast.LENGTH_LONG).show();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }
}
