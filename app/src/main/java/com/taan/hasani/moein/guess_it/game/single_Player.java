package com.taan.hasani.moein.guess_it.game;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
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
import com.taan.hasani.moein.volley.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class single_Player extends AppCompatActivity {

    private EditText entered_word;
    private Button check_bt, next_word_bt;
    private TextView word_TextView, message, timer;
    private String MY_PREFS_NAME = "username and password";
    private int Total_gamescore = 0;

    private String incompleteWord, id, completeWord, game_ID,
            url = "http://online6732.tk/guessIt.php";

    private SharedPreferences prefs;
    private String recivedTime;
    private CountDownTimer countDownTimer;
    private int spent_time;
    private String flag__nextWord_Timer;
    private String category;
    private String difficulty;
    boolean didItOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_player);

        next_word_bt = (Button) findViewById(R.id.next_word_bt);
        message = (TextView) findViewById(R.id.message);
        word_TextView = (TextView) findViewById(R.id.word);
        entered_word = (EditText) findViewById(R.id.enterd_word);
        check_bt = (Button) findViewById(R.id.check);
        timer = (TextView) findViewById(R.id.timer);
        final TextView totalScore_view = (TextView) findViewById(R.id.total_score);


        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        id = prefs.getString("userID", null);

        newSinglePlayerGame();

        //difficaulty va category ro az activity ghabl migirad
        Bundle bundle = getIntent().getExtras();
        category = bundle.getString("category");
        difficulty = bundle.getString("difficulty");
        ///////////////////////////////////////////////////////

        message.setVisibility(View.INVISIBLE);

        check_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String Player_score = timer.getText().toString();
                String Player_time = Integer.toString(15 - Integer.parseInt(Player_score));

                if (!entered_word.getText().toString().equals(""))
                    message.setVisibility(View.VISIBLE);

                if (entered_word.getText().toString().equals(completeWord) && didItOnce == false && !timer.getText().toString().equals("0")) {


                    countDownTimer.cancel();

                    word_TextView.setText(completeWord);
                    message.setText("Congratulations !!! Your guess was RIGHT !");
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.success);
                    mediaPlayer.start();
                    didItOnce = true;

                    Total_gamescore += Integer.parseInt(Player_score);//showing total score for game ending

                    setAnswer(entered_word.getText().toString(),
                            Player_time, Player_score);

                    Snackbar.make(findViewById(R.id.singlePlayerActivity), "you scored : " + Player_score, Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.YELLOW).show();
                } else if (entered_word.getText().toString().equals(completeWord)
                        && didItOnce) {

                    message.setText("Please press the Next word button ");

                } else if (timer.getText().toString().equals("0")) {

                    Toast.makeText(getApplicationContext(), "Your time is up!", Toast.LENGTH_LONG).show();

                } else {

                    message.setText("No,Guess again !");

                }

            }
        });


        next_word_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                didItOnce = false;

                message.setVisibility(View.INVISIBLE);

                totalScore_view.setText("Toatal score :" + Total_gamescore);

                entered_word.setText("");

                if (flag__nextWord_Timer.equals("yes")) {

                    countDownTimer.cancel();

                    if (timer.getText().toString() != recivedTime) {
                        spent_time = 0;

                        sendNextWord();

                    } else {
                        spent_time = Integer.parseInt(recivedTime) - Integer.parseInt(timer.getText().toString());

                        sendNextWord();
                    }

                } else {

                    sendNextWord();

                }
            }
        });

    }

    public void newSinglePlayerGame() {
        HashMap<String, String> info = new HashMap<>();

        info.put("action", "newGame");
        info.put("userID", id);
        info.put("mode", "singlePlayer");

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {

                    //    Toast.makeText(getApplicationContext(),
                    //          response.toString(), Toast.LENGTH_LONG).show();
                    game_ID = response.getString("gameID");

                    if (response.getString("dataIsRight").equals("yes")) {
                        setGameSettings();
                    } else {

                        Toast.makeText(getApplicationContext(), " data is right=no ,sth went wrong..."
                                , Toast.LENGTH_SHORT).show();
                        newSinglePlayerGame();
                    }


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "newSinglePlayerGame " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "*newSinglePlayerGame**Volley  :" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void setGameSettings() {
        HashMap<String, String> info = new HashMap<>();

        info.put("action", "setGameSetting");
        info.put("userID", id);
        info.put("gameID", game_ID);
        info.put("level", difficulty);
        try {
            info.put("categories", URLEncoder.encode(category, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(getApplicationContext(), "UnsupportedEncodingException", Toast.LENGTH_SHORT).show();
        }

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getString("dataIsRight").equals("yes")) {

                        sendNextWord();

                    } else {

                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

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
        info.put("gameID", game_ID);
        info.put("userID", id);

        word_TextView.setText("");
        message.setText("");
        // timer.setText("");

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                // Toast.makeText(getApplicationContext(),
                //         response.toString(), Toast.LENGTH_LONG).show();

                try {

                    if (response.getString("dataIsRight").equals("yes")) {

                        flag__nextWord_Timer = "yes";

                        incompleteWord = response.getJSONObject("word").getString("incompleteWord");
                        //  .getBytes("ISO-8859-1"), "UTF-8");

                        completeWord = response.getJSONObject("word").getString("word");
                        //     .getBytes("ISO-8859-1"), "UTF-8");

                        recivedTime = response.getJSONObject("word").getString("time");

                        ////////////////////////////////////////////
                        countDownTimer = new CountDownTimer((Integer.parseInt(recivedTime) - spent_time) * 1000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                timer.setText("" + millisUntilFinished / 1000);
                            }

                            public void onFinish() {
                                timer.setText("0");
                                setAnswer(entered_word.getText().toString(),
                                        "0", "0");
                                Toast.makeText(getApplicationContext(), "Time's Up!", Toast.LENGTH_SHORT).show();
                            }
                        };
                        ////////////////////////////////////////////
                        countDownTimer.start();

                        word_TextView.setText(incompleteWord);

                    } else {


                        Toast.makeText(getApplicationContext(),
                                "next word dataIsRight =no ", Toast.LENGTH_LONG).show();


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
                        "sendNextWord***Volley  :" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);


    }

    public void setAnswer(String entered_word, String player_time,
                          String player_score) {

        HashMap<String, String> info = new HashMap<>();
        HashMap<String, String> answer_hashmap = new HashMap<>();
        /////////////////////////
        answer_hashmap.put("time", player_time);
        answer_hashmap.put("score", player_score);
        answer_hashmap.put("answer", entered_word);

        JSONObject answer = new JSONObject(answer_hashmap);

        /////////////////////////
        info.put("action", "setAnswer");
        info.put("gameID", game_ID);
        info.put("userID", id);
        info.put("answer", answer.toString());
        /////////////////////////

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //  Toast.makeText(getApplicationContext(),
                //         "setAnswer response  :" + response.toString(), Toast.LENGTH_LONG).show();

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


    @Override
    protected void onDestroy() {
        countDownTimer.cancel();

        super.onDestroy();
    }
}


