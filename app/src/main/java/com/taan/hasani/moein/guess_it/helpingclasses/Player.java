package com.taan.hasani.moein.guess_it.helpingclasses;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.securepreferences.SecurePreferences;
import com.taan.hasani.moein.guess_it.Gson.getMyInfo_GSON;
import com.taan.hasani.moein.guess_it.Gson.simpleRequest_GSON;
import com.taan.hasani.moein.guess_it.Gson.userInfo_GSON;
import com.taan.hasani.moein.guess_it.appcontroller.AppController;
import com.taan.hasani.moein.guess_it.game_menu.Entrance_signup_login;
import com.taan.hasani.moein.guess_it.game_menu.App_ReOpen;
import com.taan.hasani.moein.guess_it.gameHistory.gameHistory_object;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;


/**
 * dar zamani ke player instantiatemishavad updateMyInfo run mishavad
 *dar moghe ee ke updateMyInfo ejra mishavad hamechi set mishavad
 * natijatan digar dar zamani ke get hara run mikonim niaz bekhandan az sahred pref ndarim
 */

public class Player {

    private Activity activity;
    private ArrayList<gameHistory_object> scoreList = new ArrayList<>();
    private String url = "http://mamadgram.tk/guessIt_2.php";
    private String username;
    private String role;
    private String password;
    private String firstName;
    private String lastName;
    private String userID;
    private String token;
    private String signupTime;
    private String mobileNumber;
    private String MY_PREFS_NAME = "username and password";
    private int highscore;
    // private SharedPreferences prefs;
    // private SharedPreferences.Editor editor;
    private SecurePreferences prefs;
    private SecurePreferences.Editor editor;

    public Player() {

        this.activity = App_ReOpen.activity;

        prefs = new SecurePreferences(activity);
        editor = prefs.edit();

        // prefs = securePref;
        // editor = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        updateMyInfo();
    }

    ////////////////////////////////////////////////////////////

    private void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    private void setFirstName(String firstName) {
        this.firstName = firstName;
        // editor.putString("firstName", firstName);
        // editor.apply();
    }

    private void setLastName(String lastName) {
        this.lastName = lastName;
        // editor.putString("lastName", lastName);
        // editor.apply();
    }

    private void setUserID(String userID) {
        this.userID = userID;
        //  editor.putString("userID", userID);
        //   editor.apply();
    }

    private void setrole(String role) {
        this.role = role;
        //   editor.putString("role", role);
        //   editor.apply();
    }

    private void setPassword(String password) {
        this.password = password;//***

        editor.putString("password", password);
        editor.apply();

    }

    public void setHighscore(int highScore) {
        this.highscore = highScore;

        editor.putInt("highScore", highscore);
        editor.apply();
    }

    private void setSignupTime(String signupTime) {
        this.signupTime = role;
        //  editor.putString("signupTime", signupTime);
        //  editor.apply();
    }

    public void setToken(String token_) {
        this.token = token_;

        editor.putString("token", token_);
        editor.apply();
    }

    public void setuserName(String username_) {
        this.username = username_;

        editor.putString("userName", username_);
        editor.apply();

    }
    ////////////////////////////////////////////////////////////

    public String getuserName() {
        return prefs.getString("userName", null);
    }

    public String getToken() {
        return prefs.getString("token", null);
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getUserID() {
        return userID;
        //prefs.getString("userID", null);
    }

    public String getFirstName() {
        return firstName;
        //prefs.getString("firstName", null);
    }

    public String getLastName() {
        return lastName;
        //prefs.getString("token", null);
    }

    public int getHighscore() {
        return highscore;
        //prefs.getInt("HighScore", 0);
    }//not getting any value

    public String getrole() {
        return role;
        //prefs.getString("role", null);
    }

    private String getPassword() {
        //      password = prefs.getString("password", null);
        return password;
    }//make it secure ###
    /////////////////////////////////////////////////////////////////////

    public ArrayList<gameHistory_object> getScoreList() {
        return scoreList;
    }

    /////////////////////////////////////////////////////////////////////

    public void getUserInfo() {

        HashMap<String, String> info = new HashMap<>();

        info.put("action", "getUserInfo");
        info.put("username", "???????????");
        info.put("token", getToken());

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

                    //setName(userInfo.name);
                    setPassword(userInfo.password);
                    setuserName(userInfo.username);
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

    //new###
    private void updateMyInfo() {

        final HashMap<String, String> info = new HashMap<>();

        info.put("action", "getMyInfo");
        info.put("username", this.getuserName());
        info.put("token", this.getToken());

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Gson gson = new Gson();
                getMyInfo_GSON myInfo_gson;

                myInfo_gson = gson.fromJson(response.toString(),
                        getMyInfo_GSON.class);

                setuserName(myInfo_gson.username);
                setFirstName(myInfo_gson.firstName);
                setLastName(myInfo_gson.lastName);
                setUserID(myInfo_gson.userId);
                setMobileNumber(myInfo_gson.mobileNumber);
                setSignupTime(myInfo_gson.signupTime);//###make it secure
                //editor.putString("picture", myInfo_gson.picture);

                Toast.makeText(activity, "Update my Info" + response.toString(), Toast.LENGTH_SHORT).show();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity,
                        "failed to get your info !!!", Toast.LENGTH_SHORT).show();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    //new###
    public void setInterests() {

        final HashMap<String, String> info = new HashMap<>();

        info.put("action", "setInterests");
        info.put("username", this.getuserName());
        info.put("interests", "##################");
        info.put("token", this.getToken());

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    //new###
    public void getInterests() {

        final HashMap<String, String> info = new HashMap<>();

        info.put("action", "getInterests");
        info.put("username", this.getuserName());
        info.put("token", this.getToken());

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }


    public void logout_of_server() {

        final HashMap<String, String> info = new HashMap<>();

        info.put("action", "logout");
        info.put("username", getuserName());

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Gson gson = new Gson();
                simpleRequest_GSON request;
                request = gson.fromJson(response.toString(),
                        simpleRequest_GSON.class);

                // if (request.dataIsRight.equals("yes")) {

//                    SharedPreferences.Editor editor = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//                    editor.putString("username", null);
//                    editor.putString("password", null);
//                    editor.putString("userID", null);
//                    editor.putString("name", null);
//                    editor.putInt("HighScore", 0);
//                    editor.putString("role", null);
//                    editor.apply();

                editor.putString("token", null);
                editor.putString("username", null);
                    editor.apply();

                    Intent intent = new Intent(activity, Entrance_signup_login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    activity.startActivity(intent);
                    activity.finish();
                // } else {
                //     Toast.makeText(activity,
                //              "خطایی در خروج شما رخ داد دوباره تلاش کنید...", Toast.LENGTH_LONG).show();
                //  }

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

    public void changePassword(String Entered_oldpass,
                               final String Entered_newpass,
                               String Entered_repeatpass) {

        HashMap<String, String> info = new HashMap<>();

        if (!Entered_oldpass.equals(getPassword())) {
            Toast.makeText(activity,
                    "Wrong old password", Toast.LENGTH_LONG).show();

        }

        if (Entered_newpass.equals(Entered_repeatpass)) {

            info.put("action", "changeMyPassword");
            info.put("newPassword", Entered_newpass);
            info.put("oldPassword", password);
            info.put("username", getuserName());

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

//                        SharedPreferences.Editor editor;
//                        editor = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//                        editor.putString("password", Entered_newpass);
//                        editor.apply();

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

    public void changeUsername(final String username_) {


        HashMap<String, String> info = new HashMap<>();

        info.put("action", "changeUsername");
        info.put("username", username_);

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

                    setuserName(username_);

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

    public void changeName(final String name_) {


        HashMap<String, String> info = new HashMap<>();

        info.put("action", "changeName");
        info.put("name", name_);
        info.put("username", getuserName());

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

                    //  setName(name_);

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


    public void onAppExit() {

        HashMap<String, String> info = new HashMap<>();

        info.put("action", "logout");
        info.put("username", getuserName());
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
//    public ArrayList getUserGamesInfo(String gameID) {
//
//        HashMap<String, String> info = new HashMap<>();
//
//        info.put("action", "sendGameInformation");
//        info.put("username", getUsername());
//        info.put("gameID", gameID);
//
//        JSONObject jsonObject = new JSONObject(info);
//        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
//                url, jsonObject, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                //    Toast.makeText(activity, response.toString(), Toast.LENGTH_LONG).show();
//
//                gameInfo_GSON gameInfo;
//                    Gson gson = new Gson();
//                gameInfo = gson.fromJson(response.toString(), gameInfo_GSON.class);
//
//
//                if (gameInfo.playerOneID != null) {
//
//                        gameHistory_object gameHistory_object = new gameHistory_object();
//
//                    if (id.equals(gameInfo.playerOneID)) {
//
//                        gameHistory_object.setRivalUsername(gameInfo.playerTwoUsername);
//                        gameHistory_object.setPlayerScore(gameInfo.playerOneTotalScore);
//                        gameHistory_object.setRivalScore(gameInfo.playerTwoTotalScore);
//
//                    } else if (id.equals(gameInfo.playerTwoID)) {
//
//                        gameHistory_object.setRivalUsername(gameInfo.playerOneUsername);
//                        gameHistory_object.setPlayerScore(gameInfo.playerTwoTotalScore);
//                        gameHistory_object.setRivalScore(gameInfo.playerOneTotalScore);
//
//                        }
//
//                        scoreList.add(gameHistory_object);
//
//                    } else {
//
//                        activity.findViewById(R.id.message_history).setVisibility(View.VISIBLE);
//
//                    }
//
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
//
//        return scoreList;
//    }

}
