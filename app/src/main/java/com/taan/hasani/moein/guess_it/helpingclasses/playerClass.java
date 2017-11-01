package com.taan.hasani.moein.guess_it.helpingclasses;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.taan.hasani.moein.guess_it.appcontroller.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Moein on 11/1/2017.
 */

public class playerClass {

    private final String MY_PREFS_NAME = "username and password";
//    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
//    String username = prefs.getString("username", null);
//    String password = prefs.getString("password", null);
//    String id = prefs.getString("userID", null);

    private void getUserInfo() {

        HashMap<String, String> info = new HashMap<>();

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
                    name_textview.setVisibility(View.VISIBLE);

                    username_edittext.setText(username);


                } catch (JSONException e) {
                    // Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException e) {
                    //Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }

}
