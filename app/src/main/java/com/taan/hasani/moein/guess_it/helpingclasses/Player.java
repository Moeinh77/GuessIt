package com.taan.hasani.moein.guess_it.helpingclasses;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.taan.hasani.moein.guess_it.appcontroller.AppController;
import com.taan.hasani.moein.guess_it.game_menu.Entrance_signup_login;
import com.taan.hasani.moein.guess_it.game_menu.Loading;
import com.taan.hasani.moein.guess_it.game_menu.Login;
import com.taan.hasani.moein.guess_it.game_menu.Main_menu;
import com.taan.hasani.moein.guess_it.profile.gameHistory_object;
import com.taan.hasani.moein.volley.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.taan.hasani.moein.volley.R.id.message;


/**
 * Created by Moein on 11/1/2017.
 */

public class Player {

    private Activity activity;
    private ArrayList<gameHistory_object> scoreList = new ArrayList<>();
    private String url = "http://online6732.tk/guessIt.php";
    //agar jaee lazem shod mishe az inja avord choon public e

    private String username;
    private String password;
    private String id;
    private String name;//@@@@@dakhele user info dare meghdar dehi mishe
    private String MY_PREFS_NAME = "username and password";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;


    public Player(Activity activity_) {
        this.activity = activity_;
        prefs = activity_.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = activity_.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        id = prefs.getString("userID", null);
        password = prefs.getString("password", null);
        username = prefs.getString("username", null);
        name = prefs.getString("name", null);


    }

    public void setUsername(String username) {
        editor.putString("username", username);
        editor.apply();
    }

    public void setPassword(String password) {
        editor.putString("password", password);
        editor.apply();

    }

    public void setId(String id) {
        editor.putString("userID", id);
        editor.apply();

    }

    public void setName(String name) {
        editor.putString("name", name);
        editor.apply();

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
                    //Toast.makeText(activity,
                    //         response.toString(),Toast.LENGTH_LONG).show();
                    if (response.getString("dataIsRight").equals("yes")) {

                        String id = response.getString("userID");
                        String name = response.getString("name");

                        setName(name);
                        setId(id);
                        setUsername(username_);
                        setPassword(password_);

                        //opens the gamechoose activity
                        Intent i = new Intent(activity, Main_menu.class);
                        activity.startActivity(i);
                        activity.finish();
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

    public void logout_of_server() {

        final HashMap<String, String> info = new HashMap<>();

        info.put("action", "logout");
        info.put("userID", id);

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    if (response.getString("dataIsRight").equals("yes")) {

                        SharedPreferences.Editor editor = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("username", null);
                        editor.putString("password", null);
                        editor.putString("userID", null);
                        editor.putString("name", null);
                        editor.putInt("HighScore", 0);
                        editor.apply();

                        Intent intent = new Intent(activity, Entrance_signup_login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(intent);
                        activity.finish();
                    } else {
                        Toast.makeText(activity,
                                "خطایی در خروج شما رخ داد دوباره تلاش کنید...", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
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

                    try {

                        if (response.getString("dataIsRight").equals("yes")) {

                            SharedPreferences.Editor editor;
                            editor = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("password", Entered_newpass);
                            editor.apply();

                            Toast.makeText(activity, "Password changed", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(activity,
                                    response.getString("dataIsRight"), Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
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

                try {


                    if (response.getString("dataIsRight").equals("yes")) {

                        Toast.makeText(activity,
                                "Username changed", Toast.LENGTH_LONG).show();

                        setUsername(username_);

                    } else {
                        Toast.makeText(activity,
                                "username didnt change", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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


    public void app_reopening() {

        final HashMap<String, String> info = new HashMap<>();

        String url = "http://online6732.tk/guessIt.php";

        info.put("action", "login");
        info.put("username", getUsername());
        info.put("password", getPassword());

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getString("dataIsRight").equals("yes")) {


                        Toast.makeText(activity, "Welcome " + getUsername(), Toast.LENGTH_LONG).show();

                        Intent i = new Intent(activity, Main_menu.class);
                        activity.startActivity(i);
                        activity.finish();
                    } else {
                        Toast.makeText(activity,
                                "Error in login", Toast.LENGTH_LONG).show();
                        activity.finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity,
                            e.toString(), Toast.LENGTH_LONG).show();

                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity,
                        "No internet connection found ...", Toast.LENGTH_LONG).show();
                activity.finish();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }


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
                try {

                    if (response.getString("playerOneTotalScore") != null) {

                        gameHistory_object gameHistory_object = new gameHistory_object();

                        if (id.equals(response.getString("playerOneID"))) {

                            String Playerscores = response.getString("playerOneTotalScore");
                            String Rivalscore = response.getString("playerTwoTotalScore");

                            gameHistory_object.setRivalUsername(response.getString("playerTwoUsername"));
                            gameHistory_object.setPlayerScore(Playerscores);
                            gameHistory_object.setRivalScore(Rivalscore);

                        } else {

                            String Playerscores = response.getString("playerTwoTotalScore");
                            String Rivalscore = response.getString("playerTwoTotalScore");

                            gameHistory_object.setRivalUsername(response.getString("playerOneUsername"));
                            gameHistory_object.setPlayerScore(Playerscores);
                            gameHistory_object.setRivalScore(Rivalscore);

                        }

                        scoreList.add(gameHistory_object);

                    } else {

                        activity.findViewById(R.id.message_history).setVisibility(View.VISIBLE);

                    }

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

        return scoreList;
    }


    //hargah inra seda konim etelaat ro az server migire mirize too pref
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

                    String name = new String(response.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                    String username = new String(response.getString("username").getBytes("ISO-8859-1"), "UTF-8");
                    String password = new String(response.getString("password").getBytes("ISO-8859-1"), "UTF-8");
                    String id = new String(response.getString("id").getBytes("ISO-8859-1"), "UTF-8");

                    setName(name);
                    setId(id);
                    setPassword(password);
                    setUsername(username);

                } catch (JSONException e) {
                    Toast.makeText(activity.getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException e) {
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

}
