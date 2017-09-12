package com.taan.hasani.moein.guess_it.player_class;

import android.content.SharedPreferences;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Moein on 9/7/2017.
 */

public class player {

    private final String MY_PREFS_NAME="username and password";
    final HashMap<String, String> info = new HashMap<>();
    String username,password,id,photo;

//
//    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//        editor.putString("usename", username__);
//        editor.putString("password", password__);
//        editor.putString("userID",id__);
//        editor.apply();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
