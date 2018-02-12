package com.taan.hasani.moein.guess_it.helpingclasses;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.taan.hasani.moein.guess_it.Gson.gameInfo_GSON;
import com.taan.hasani.moein.guess_it.Gson.simpleRequest_GSON;
import com.taan.hasani.moein.guess_it.Gson.userInfo_GSON;
import com.taan.hasani.moein.guess_it.appcontroller.AppController;
import com.taan.hasani.moein.guess_it.game_menu.Entrance_signup_login;
import com.taan.hasani.moein.guess_it.game_menu.Main_menu;
import com.taan.hasani.moein.guess_it.gameHistory.gameHistory_object;
import com.taan.hasani.moein.volley.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Moein on 11/1/2017.
 */

public class Player {

    private Activity activity;
    private ArrayList<gameHistory_object> scoreList = new ArrayList<>();
    private String url = "http://mamadgram.tk/guessIt.php";
    private String username;
    private String role;
    private String password;
    private String id;
    private String name;//@@@@@dakhele user info dare meghdar dehi mishe
    private String MY_PREFS_NAME = "username and password";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private int Highscore;


    public Player(Activity activity_) {
        this.activity = activity_;
        prefs = activity_.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = activity_.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        id = prefs.getString("userID", null);
        password = prefs.getString("password", null);
        username = prefs.getString("username", null);
        name = prefs.getString("name", null);
    }

    private void setrole(String role) {
        editor.putString("role", role);
        editor.apply();
    }

    private void setUsername(String username) {
        editor.putString("username", username);
        editor.apply();
    }

    private void setPassword(String password) {
        editor.putString("password", password);
        editor.apply();

    }

    private void setId(String id) {
        editor.putString("userID", id);
        editor.apply();

    }

    private void setName(String name) {
        editor.putString("name", name);
        editor.apply();

    }

    ////////////////////////////////////////////////////////////
    public int getHighscore() {
        Highscore = prefs.getInt("HighScore", 0);
        return Highscore;
    }

    public void setHighscore(int highscore) {
        editor.putInt("HighScore", highscore);
        editor.apply();
    }

    public String getrole() {
        role = prefs.getString("role", null);
        return role;
    }

    public String getName() {
        name = prefs.getString("name", null);
        return name;
    }

    public String getUsername() {
        username = prefs.getString("username", null);
        return username;
    }

    public String getPassword() {
        password = prefs.getString("password", null);
        return password;
    }

    public String getId() {
        id = prefs.getString("userID", null);
        return id;
    }

    public ArrayList<gameHistory_object> getScoreList() {
        return scoreList;
    }

    //Gsonized !!!
    //hargah inra seda konim etelaat ro az server migire mirize too pref
    public void getUserInfo() {

        HashMap<String, String> info = new HashMap<>();

        info.put("action", "sendUserInformation");
        info.put("userID", id);

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    userInfo_GSON userInfo;
                    Gson gson = new Gson();
                    userInfo = gson.fromJson(response.getJSONObject("user").toString()
                            , userInfo_GSON.class);

                    setName(userInfo.name);
                    setId(userInfo.id);
                    setPassword(userInfo.password);
                    setUsername(userInfo.username);
                    setrole(userInfo.role);

                } catch (JSONException e) {
                    Toast.makeText(activity.getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity.getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    //Gsonized !!!
    public void login(final String username_, final String password_) {

        HashMap<String, String> info = new HashMap<>();

        info.put("action", "login");
        info.put("username", username_);
        info.put("password", password_);

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    simpleRequest_GSON loginGson;
                    Gson gson = new Gson();

                    loginGson = gson.fromJson(response.toString()
                            , simpleRequest_GSON.class);

                    if (loginGson.dataIsRight.equals("yes")) {

                        userInfo_GSON user;

                        user = gson.fromJson(response.getJSONObject("user").toString()
                                , userInfo_GSON.class);//gereftan etelaat user az
                        // json object userdakhel response

                        setrole(user.role);
                        setName(user.name);
                        setId(user.id);
                        setUsername(user.username);
                        setPassword(user.password);

                        //opens the gamechoose activity
                        Intent i = new Intent(activity, Main_menu.class);
                        activity.startActivity(i);
                        activity.finish();
                        Toast.makeText(activity,
                                "Welcome " + user.name, Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(activity,
                                "Wrong username or password", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity,
                        "Error login", Toast.LENGTH_LONG).show();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    //Gsonized !!!
    public void logout_of_server() {

        final HashMap<String, String> info = new HashMap<>();

        info.put("action", "logout");
        info.put("userID", id);

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Gson gson = new Gson();
                simpleRequest_GSON request;
                request = gson.fromJson(response.toString(),
                        simpleRequest_GSON.class);

                if (request.dataIsRight.equals("yes")) {

                    SharedPreferences.Editor editor = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("username", null);
                    editor.putString("password", null);
                    editor.putString("userID", null);
                    editor.putString("name", null);
                    editor.putInt("HighScore", 0);
                    editor.putString("role", null);
                    editor.apply();

                    Intent intent = new Intent(activity, Entrance_signup_login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    activity.startActivity(intent);
                    activity.finish();
                } else {
                    Toast.makeText(activity,
                            "خطایی در خروج شما رخ داد دوباره تلاش کنید...", Toast.LENGTH_LONG).show();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity,
                        "خطایی در خروج شما رخ داد دوباره تلاش کنید...", Toast.LENGTH_SHORT).show();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    //Gsonized !!!
    public void change_password_func(String Entered_oldpass,
                                     final String Entered_newpass,
                                     String Entered_repeatpass) {

        HashMap<String, String> info = new HashMap<>();

        if (!Entered_oldpass.equals(getPassword())) {
            Toast.makeText(activity,
                    "Wrong old password", Toast.LENGTH_LONG).show();

        }

        if (Entered_newpass.equals(Entered_repeatpass)) {

            info.put("action", "changePassword");
            info.put("newPassword", Entered_newpass);
            info.put("oldPassword", password);
            info.put("userID", id);

            JSONObject jsonObject = new JSONObject(info);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Gson gson = new Gson();
                    simpleRequest_GSON request;
                    request = gson.fromJson(response.toString(),
                            simpleRequest_GSON.class);

                    if (request.dataIsRight.equals("yes")) {

                        SharedPreferences.Editor editor;
                        editor = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("password", Entered_newpass);
                        editor.apply();

                        Toast.makeText(activity, "Password changed !", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(activity,
                                "There was an error in changing the pass word!!!", Toast.LENGTH_LONG).show();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity, "error listener " + error.toString(), Toast.LENGTH_LONG).show();

                }
            });

            AppController.getInstance().addToRequestQueue(jsonObjectRequest);

        } else {
            Toast.makeText(activity, "Entered passwords are not the same ", Toast.LENGTH_LONG).show();

        }

    }

    //Gsonized !!!
    public void changeUsername(final String username_) {


        HashMap<String, String> info = new HashMap<>();

        info.put("action", "changeUsername");
        info.put("username", username_);
        info.put("userID", getId());

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Gson gson = new Gson();
                simpleRequest_GSON request;
                request = gson.fromJson(response.toString(),
                        simpleRequest_GSON.class);

                if (request.dataIsRight.equals("yes")) {

                    Toast.makeText(activity,
                            "Username changed", Toast.LENGTH_LONG).show();

                    setUsername(username_);

                } else {
                    Toast.makeText(activity,
                            "username didnt change", Toast.LENGTH_LONG).show();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, error.toString(), Toast.LENGTH_LONG);

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    //Gsonized !!!
    public void changeName(final String name_) {


        HashMap<String, String> info = new HashMap<>();

        info.put("action", "changeName");
        info.put("name", name_);
        info.put("userID", getId());

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Gson gson = new Gson();
                simpleRequest_GSON request;
                request = gson.fromJson(response.toString(),
                        simpleRequest_GSON.class);

                if (request.dataIsRight.equals("yes")) {

                    Toast.makeText(activity,
                            "name changed", Toast.LENGTH_LONG).show();

                    setName(name_);

                } else {
                    Toast.makeText(activity,
                            "Couldn't change the name !", Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, error.toString(), Toast.LENGTH_LONG);

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    //Gsonized !!!
    public void app_reopening() {

        final HashMap<String, String> info = new HashMap<>();

        info.put("action", "login");
        info.put("username", getUsername());
        info.put("password", getPassword());

        final JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                simpleRequest_GSON loginGson;
                Gson gson = new Gson();

                loginGson = gson.fromJson(response.toString()
                        , simpleRequest_GSON.class);
                if (loginGson.dataIsRight.equals("yes")) {

                    userInfo_GSON user;
                    try {
                        user = gson.fromJson(response.getJSONObject("user").toString()
                                , userInfo_GSON.class);//gereftan etelaat user az
                        // json object e "user" dakhel response

                        setrole(user.role);//baraye har bar baz shodan naghshe
                        // kar bar ra update mikonad

                        //opens the gamechoose activity
                        Intent i = new Intent(activity, Main_menu.class);
                        activity.startActivity(i);
                        Toast.makeText(activity,
                                "welcome " + user.name, Toast.LENGTH_LONG).show();
                        activity.finish();
                    } catch (JSONException e) {
                        Toast.makeText(activity,
                                "login  " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(activity,
                            "Some thing went wrong login!!!", Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity,
                        "ارتباط با اینترنت پیدا نشد ...", Toast.LENGTH_LONG).show();
                activity.finish();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    //Gsonized !!!
    public void onAppExit() {

        HashMap<String, String> info = new HashMap<>();

        info.put("action", "logout");
        info.put("userID", id);
        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                simpleRequest_GSON request;
                request = gson.fromJson(response.toString(),
                        simpleRequest_GSON.class);

                if (request.dataIsRight.equals("yes")) {

                    Toast.makeText(activity,
                            "Logged out successfully ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity,
                            "There was error in logging out...", Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity,
                        "There was error in logging out...", Toast.LENGTH_LONG).show();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);


    }

    //Gsonized !!!
    public ArrayList getUserGamesInfo(String gameID) {

        HashMap<String, String> info = new HashMap<>();

        info.put("action", "sendGameInformation");
        info.put("userID", getId());
        info.put("gameID", gameID);

        JSONObject jsonObject = new JSONObject(info);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //    Toast.makeText(activity, response.toString(), Toast.LENGTH_LONG).show();

                gameInfo_GSON gameInfo;
                    Gson gson = new Gson();
                gameInfo = gson.fromJson(response.toString(), gameInfo_GSON.class);


                if (gameInfo.playerOneID != null) {

                        gameHistory_object gameHistory_object = new gameHistory_object();

                    if (id.equals(gameInfo.playerOneID)) {

                        gameHistory_object.setRivalUsername(gameInfo.playerTwoUsername);
                        gameHistory_object.setPlayerScore(gameInfo.playerOneTotalScore);
                        gameHistory_object.setRivalScore(gameInfo.playerTwoTotalScore);

                    } else if (id.equals(gameInfo.playerTwoID)) {

                        gameHistory_object.setRivalUsername(gameInfo.playerOneUsername);
                        gameHistory_object.setPlayerScore(gameInfo.playerTwoTotalScore);
                        gameHistory_object.setRivalScore(gameInfo.playerOneTotalScore);

                        }

                        scoreList.add(gameHistory_object);

                    } else {

                        activity.findViewById(R.id.message_history).setVisibility(View.VISIBLE);

                    }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

        return scoreList;
    }

}
