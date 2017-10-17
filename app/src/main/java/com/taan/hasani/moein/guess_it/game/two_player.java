package com.taan.hasani.moein.guess_it.game;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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

public class two_player extends AppCompatActivity {

    private String MY_PREFS_NAME = "username and password",
            url = "http://online6732.tk/guessIt.php", id, completeWord, incompleteWord;
    private SharedPreferences prefs;
    private String gamedID;
    private TextView word, message, timer, player2_textview, player1_textview;
    private EditText entered_word;
    private Button check_bt, nextWord_bt;
    private CountDownTimer countDownTimer;
    private String recivedTime, category, difficulty;
    private String turn,
            flag__nextWord_Timer = null;//baraye inke check konim aya vared next word shode
    //agar shode bashad timer be karoftade
    private int spent_time = 0;
    private boolean inGame = true;
    private boolean flag_counter;//baraye inke dar onDestroy error rokh nade age timer rah nayoftade bood


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
        player2_textview = (TextView) findViewById(R.id.rivalscore);
        player1_textview = (TextView) findViewById(R.id.yourscore);

        //difficaulty va category ro az activity ghabl migirad
        Bundle bundle = getIntent().getExtras();
        category = bundle.getString("category");
        difficulty = bundle.getString("difficulty");
        ///////////////////////////////////////////////////////

        message.setVisibility(View.INVISIBLE);
        word.setVisibility(View.INVISIBLE);

        newTwoPlayerGame();

        check_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (turn.equals("notmyturn")) {

                    Toast.makeText(getApplicationContext(), "Not your turn", Toast.LENGTH_SHORT).show();

                } else if (!timer.getText().toString().equals("")) {

                    String Player_score = timer.getText().toString();
                    String Player_time = Integer.toString(15 - Integer.parseInt(Player_score));
                    String myturn;

                    if (timer.getText().toString().equals("0")) {

                        Toast.makeText(getApplicationContext(), "Your time is up", Toast.LENGTH_SHORT).show();

                    } else if (!entered_word.getText().toString().equals("")) {

                        message.setVisibility(View.VISIBLE);

                        if (entered_word.getText().toString().equals("")) {

                            //   Toast.makeText(getApplicationContext(),"Please wait...",Toast.LENGTH_SHORT).show();

                        } else if (entered_word.getText().toString().equals(completeWord)) {

                            countDownTimer.cancel();

                            myturn = "no";

                            word.setText(completeWord);
                            message.setText("Congratulations !!! Your guess was RIGHT !");
                            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.success);
                            mediaPlayer.start();

                            setAnswer(entered_word.getText().toString(),
                                    Player_time, Player_score, myturn);
                            //baraye check kardan nobat
                            sendNextWord();
                            ////////////////////////////

                        } else {

                            message.setText("No,Guess again !");
                            myturn = "yes";
                            setAnswer(entered_word.getText().toString(),
                                    Player_time, Player_score, myturn);

                        }

                        if (timer.getText().toString().equals("0")) {
                            myturn = "no";
                            Toast.makeText(getApplicationContext(), "Times up!", Toast.LENGTH_SHORT).show();
                            setAnswer(entered_word.getText().toString(),
                                    Player_time, Player_score, myturn);
                        }


                    }
                }

            }
        });

        nextWord_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                message.setVisibility(View.INVISIBLE);


                if (flag__nextWord_Timer.equals("yes")) {

                    countDownTimer.cancel();

                    if (timer.getText().toString() != recivedTime) {

                        spent_time = Integer.parseInt(recivedTime) - Integer.parseInt(timer.getText().toString());

                        sendNextWord();

                    } else {

                        sendNextWord();
                    }

                } else {

                    sendNextWord();

                }

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
                            response.toString(), Toast.LENGTH_SHORT).show();

                    if (response.getString("gameID").equals("-1")) {

                        isMyGameReady();

                    } else {

                        gamedID = response.getString("gameID");

                        setGameSettings();
                        Toast.makeText(getApplicationContext(),
                                gamedID, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "newTwoPlayerGame " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "*newTwoPlayerGame**Volley  :" + error.toString(), Toast.LENGTH_SHORT).show();
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
                            response.getString("responseData"), Toast.LENGTH_SHORT).show();
                    if (inGame) {
                        if (response.getString("gameID").equals("-1")) {


                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    isMyGameReady();
                                }
                            }, 3000);


                        } else {
                            gamedID = response.getString("gameID");
                            setGameSettings();
                        }
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "isMyGameReady***Volley  :" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    public void setGameSettings() {

        HashMap<String, String> info = new HashMap<>();

        info.put("action", "setGameSetting");
        info.put("userID", id);
        info.put("gameID", gamedID);
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

                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "setGameSetting***Volley  :" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);


    }

    public void sendNextWord() {

        gameInfo_during();

        HashMap<String, String> info = new HashMap<>();

        info.put("action", "sendNextWord");
        info.put("gameID", gamedID);
        info.put("userID", id);

        entered_word.setText("");
        word.setText("");
        message.setText("");
        timer.setText("");

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getString("dataIsRight").equals("yes")) {

                        turn = "myturn";

                        flag__nextWord_Timer = "yes";

                        incompleteWord = response.getJSONObject("word").getString("incompleteWord");

                        completeWord = response.getJSONObject("word").getString("word");

                        recivedTime = response.getJSONObject("word").getString("time");

                        ////////////////////////////////////////////
                        countDownTimer = new CountDownTimer((Integer.parseInt(recivedTime) - spent_time) * 1000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                timer.setText(String.valueOf(millisUntilFinished / 1000));
                                flag_counter = true;

                            }

                            public void onFinish() {
                                timer.setText("0");
                                setAnswer(entered_word.getText().toString(),
                                        "0", "0", "no");
                                spent_time = 0;
                                // check kardane nobat
                                sendNextWord();
                                ////////////////////////

                            }
                        };
                        ////////////////////////////////////////////
                        countDownTimer.start();

                        word.setVisibility(View.VISIBLE);
                        word.setText(incompleteWord);

                    } else {
                        message.setVisibility(View.INVISIBLE);
                        word.setVisibility(View.INVISIBLE);

                        turn = "notmyturn";
                        if (inGame) {
                            //Toast.makeText(getApplicationContext(),
                            //        "It's not your turn yet, Please wait...", Toast.LENGTH_SHORT).show();
                            timer.setText("Please wait...");
                            //ferestadan request baraye inke bebinim nobateman shode ya na
                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    sendNextWord();
                                }
                            }, 1000);
                            //////////////////////////////////////////
                        }

                    }

                } catch (JSONException e) {
                    gameInfo_gameEnd(gamedID);
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "sendNextWord***Volley  :" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);


    }

    public void setAnswer(String entered_word, String player_time,
                          String player_score, String myturn) {

        HashMap<String, String> info = new HashMap<>();
        HashMap<String, String> answer_hashmap = new HashMap<>();
        /////////////////////////
        answer_hashmap.put("time", player_time);
        answer_hashmap.put("score", player_score);
        answer_hashmap.put("answer", entered_word);
        answer_hashmap.put("myTurn", myturn);

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

                // Toast.makeText(getApplicationContext(),
                //       "setAnswer response  :" + response.toString(), Toast.LENGTH_SHORT).show();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "setAnswer***Volley  :" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void gameInfo_during() {

        final String MY_PREFS_NAME = "username and password";
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final String id = prefs.getString("userID", null);

        HashMap<String, String> info = new HashMap<>();

        info.put("action", "sendGameInformation");
        info.put("userID", id);
        info.put("gameID", gamedID);

        JSONObject jsonObject = new JSONObject(info);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    String your_score = response.getString("playerOneTotalScore");

                    String rival_score = response.getString("playerTwoTotalScore");

                    if (id.equals(response.getString("playerOneID"))) {

                        player2_textview.setText("امتیاز " + response.getString("playerTwoID") + " : " + rival_score);
                        player1_textview.setText("امتیاز شما : " + your_score);

                        player2_textview.setTextColor(Color.RED);
                        player1_textview.setTextColor(Color.GREEN);

                    } else if (id.equals(response.getString("playerTwoID"))) {

                        player2_textview.setText("امتیاز شما : " + rival_score);
                        player1_textview.setText("امتیاز " + response.getString("playerOneID") + " : " + your_score);

                        player2_textview.setTextColor(Color.GREEN);
                        player1_textview.setTextColor(Color.RED);
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

    }


    private void gameInfo_gameEnd(String gameID) {

        HashMap<String, String> info = new HashMap<>();

        final String MY_PREFS_NAME = "username and password";
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME,
                MODE_PRIVATE);
        final String id = prefs.getString("userID", null);

        info.put("action", "sendGameInformation");
        info.put("userID", id);
        info.put("gameID", gameID);

        JSONObject jsonObject = new JSONObject(info);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //   try {

                Toast.makeText(getApplicationContext(), response.toString(), Toast
                        .LENGTH_LONG).show();
//                    if (id.equals(response.getString("playerOneID"))) {
//
//                        String Playerscores = response.getString("playerOneTotalScore");
//                        //     Rivalscore = response.getString("playerTwoTotalScore");
//
//                      //  yourscore_gameEnd.setText(Total_gamescore);
//
//                    } else {
//
//                        String Playerscores = response.getString("playerTwoTotalScore");
//                        //   Rivalscore = response.getString("playerTwoTotalScore");
//
//                        //yourscore_gameEnd.setText(Playerscores);
//
//                    }


//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }


    @Override
    protected void onDestroy() {
        inGame = false;

        if (flag_counter) {
            countDownTimer.cancel();
        }
        super.onDestroy();
    }
}
