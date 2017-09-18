package com.taan.hasani.moein.guess_it.game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.taan.hasani.moein.guess_it.appcontroller.AppController;
import com.taan.hasani.moein.guess_it.game_menu.Entrance_signup_login;
import com.taan.hasani.moein.guess_it.game_menu.Loading;
import com.taan.hasani.moein.volley.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class two_player extends AppCompatActivity {

    private String MY_PREFS_NAME = "username and password",
            url = "http://online6732.tk/guessIt.php", id, completeWord, incompleteWord;
    private SharedPreferences prefs;
    private String gamedID;
    private TextView word, message, timer;
    private EditText entered_word;
    private Button check_bt, nextWord_bt;
    private CountDownTimer countDownTimer;
    private String recivedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_player);

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        id = prefs.getString("userID", null);

        check_bt = (Button) findViewById(R.id.check_bt);
        nextWord_bt = (Button) findViewById(R.id.nextWord_bt);
        word = (TextView) findViewById(R.id.word);
        message = (TextView) findViewById(R.id.message);
        entered_word = (EditText) findViewById(R.id.enteredWord);
        timer = (TextView) findViewById(R.id.timer);


        newTwoPlayerGame();

        ////////////

        ////////////

        check_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Player_time = timer.getText().toString();
                // String Player_score=15-timer.getText().toString();

                setAnswer(entered_word.getText().toString(), Player_time);

                if (entered_word.getText().toString().equals(completeWord)) {
                    countDownTimer.cancel();

                    word.setText(completeWord);
                    message.setText("Congratulations !!! Your guess was RIGHT !");
                } else {

                    message.setText("No,Guess again !");

                }


            }
        });

        nextWord_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendNextWord();

            }
        });



    }


    public void newTwoPlayerGame() {
        HashMap<String, String> info = new HashMap<>();

        info.put("action", "newGame");
        info.put("userID", id);
        info.put("mode", "twoPlayer");

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {

                    Toast.makeText(getApplicationContext(),
                            response.toString(), Toast.LENGTH_LONG).show();

                    if (response.getString("gameID").equals("-1")) {

                        isMyGameReady();

                    } else {

                        gamedID = response.getString("gameID");

                        setGameSettings();
                        Toast.makeText(getApplicationContext(),
                                gamedID, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "newTwoPlayerGame " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "*newTwoPlayerGame**Volley  :" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void isMyGameReady() {
        HashMap<String, String> info = new HashMap<>();

        info.put("action", "isMyGameReady");
        info.put("userID", id);

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {

                    Toast.makeText(getApplicationContext(),
                            response.toString(), Toast.LENGTH_SHORT).show();

                    if (response.getString("gameID").equals("-1")) {

                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                isMyGameReady();
                            }
                        }, 3000);

                    } else {
                        Toast.makeText(getApplicationContext(),
                                response.getString("gameID"), Toast.LENGTH_SHORT).show();
                        setGameSettings();

                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            e.toString(), Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "isMyGameReady***Volley  :" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    public void setGameSettings() {
        HashMap<String, String> info = new HashMap<>();

        // String [] categories = {"ورزشی","ورزشی","ورزشی","ورزشی","ورزشی","ورزشی","ورزشی","ورزشی","ورزشی","ورزشی","ورزشی"};

        // JSONArray catagories_jsonarray = new JSONArray(Arrays.asList(categories));

        //    try {

        try {
            info.put("categories", URLEncoder.encode("1", "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //  }

        //  } catch (UnsupportedEncodingException e) {
        //      e.printStackTrace();
        //  }


        info.put("action", "setGameSetting");
        info.put("userID", id);
        info.put("gameID", gamedID);

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getString("dataIsRight").equals("yes")) {

                        sendNextWord();

                    } else {

                        Toast.makeText(getApplicationContext(), "dataIsRight=no, There was an error with setting the game settings", Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            e.toString(), Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "setGameSetting***Volley  :" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);


    }

    public void sendNextWord() {

        HashMap<String, String> info = new HashMap<>();

        info.put("action", "sendNextWord");
        info.put("gameID", gamedID);
        info.put("userID", id);

        word.setText("");
        message.setText("");

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                // Toast.makeText(getApplicationContext(),
                //         response.toString(), Toast.LENGTH_LONG).show();
                try {


                    incompleteWord = response.getJSONObject("word").getString("incompleteWord");
                    //  .getBytes("ISO-8859-1"), "UTF-8");

                    completeWord = response.getJSONObject("word").getString("word");
                    //     .getBytes("ISO-8859-1"), "UTF-8");

                    recivedTime = response.getJSONObject("word").getString("time");


                    word.setText(incompleteWord);
                    ////////////////////////////////////////////
                    countDownTimer = new CountDownTimer(Integer.parseInt(recivedTime) * 1000, 1000) {

                        public void onTick(long millisUntilFinished) {

                            timer.setText("" + millisUntilFinished / 1000);

                        }

                        public void onFinish() {
                            timer.setText("done!");
                        }
                    };
                    countDownTimer.start();
                    ////////////////////////////////////////////


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            e.toString(), Toast.LENGTH_LONG).show();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "sendNextWord***Volley  :" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);


    }


    public void setAnswer(String entered_word, String player_time) {

        HashMap<String, String> info = new HashMap<>();
        HashMap<String, String> answer_hashmap = new HashMap<>();
        /////////////////////////
        answer_hashmap.put("time", player_time);
        answer_hashmap.put("score", "not set yet");
        answer_hashmap.put("answer", entered_word);

        JSONObject answer = new JSONObject(answer_hashmap);

        /////////////////////////
        info.put("action", "setAnswer");
        info.put("gameID", gamedID);
        info.put("userID", id);
        info.put("answer", answer.toString());
        /////////////////////////

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Toast.makeText(getApplicationContext(),
                        "setAnswer response  :" + response.toString(), Toast.LENGTH_LONG).show();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "setAnswer***Volley  :" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

}
