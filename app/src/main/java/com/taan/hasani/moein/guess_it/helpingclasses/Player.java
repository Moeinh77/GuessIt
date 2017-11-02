package com.taan.hasani.moein.guess_it.helpingclasses;

import android.app.Activity;
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

public class Player {

    private Activity activity;


    private String url = "http://online6732.tk/guessIt.php";
    //agar jaee lazem shod mishe az inja avord choon public e

    private String username;
    private String password;
    private String id;
    private String name;//@@@@@dakhele user info dare meghdar dehi mishe


    Player(Activity activity) {
        this.activity = activity;

        String MY_PREFS_NAME = "username and password";
        SharedPreferences prefs;
        prefs = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("username", null);
        password = prefs.getString("password", null);
        id = prefs.getString("userID", null);
        name = prefs.getString("name", null);
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }


}
