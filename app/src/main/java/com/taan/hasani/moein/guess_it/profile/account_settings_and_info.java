package com.taan.hasani.moein.guess_it.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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


public class account_settings_and_info extends Fragment {

    String MY_PREFS_NAME="username and password";
    HashMap<String, String> info = new HashMap<>();
    String url = "http://online6732.tk/guessIt.php";
    String name, username, games, profilePicture;
    TextView name_textview, username_textview, games_textview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getUserInfo();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View account_info_ = inflater.inflate(R.layout.fragment_account_info, container, false);

        name_textview = (TextView) account_info_.findViewById(R.id.name);
        username_textview = (TextView) account_info_.findViewById(R.id.username);
        games_textview = (TextView) account_info_.findViewById(R.id.games);


        return account_info_;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        //saving the games
        SharedPreferences.Editor editor =
                getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("user games",games_textview.getText().toString());
        editor.apply();
        //////////////////////////////////////////



        super.onActivityCreated(savedInstanceState);
    }

    private void getUserInfo() {
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

                    name = new String(response.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                    username = new String(response.getString("username").getBytes("ISO-8859-1"), "UTF-8");
                    games = new String(response.getString("games").getBytes("ISO-8859-1"), "UTF-8");
                    profilePicture = new String(response.getString("profilePicture").getBytes("ISO-8859-1"), "UTF-8");
                    name_textview.setText(name);
                    username_textview.setText(username);
                    games_textview.setText(games);

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
}
