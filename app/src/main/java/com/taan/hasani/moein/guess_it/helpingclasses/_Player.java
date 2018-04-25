package com.taan.hasani.moein.guess_it.helpingclasses;

import android.app.Activity;
import android.content.SharedPreferences;

import com.taan.hasani.moein.guess_it.game_menu.Main_menu;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Moein on 4/24/2018.
 */

public class _Player {

    private String url = "http://mamadgram.tk/guessIt_2.php";
    private String username;
    private String role;
    private String password;
    private String id;
    private String name;
    private String MY_PREFS_NAME = "username and password";
    private int Highscore;
    private Activity activity;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;


    public _Player() {

        //  activity=Main_menu.activity;

    }

    private void writeInSP() {


        prefs = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

    }


}
