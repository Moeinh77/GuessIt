package com.taan.hasani.moein.guess_it.game;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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
import java.util.ArrayList;
import java.util.HashMap;

public class single_Player extends AppCompatActivity {

    private EditText entered_word;
    private int number_of_trueGuess = 0;
    private Button check_bt, next_word_bt;
    private TextView word_TextView, timer, guesses_true, guesses_false;
    private String MY_PREFS_NAME = "username and password";
    private int Total_gamescore = 0;
    private ArrayList<Integer> indexlist_of_questionmarks = new ArrayList<>();
    private String incompleteWord, id, completeWord, game_ID,
            url = "http://online6732.tk/guessIt.php";
    private TextView totalScore_view;
    private SharedPreferences prefs;
    private CountDownTimer countDownTimer;
    private String category, flag__nextWord_Timer, difficulty, type, recivedTime;
    private int arraylist_i;//baraye gereftane index alamate soal az list
    private Dialog dialog;
    private int length;
    private TextView yourscore_gameEnd;
    private boolean Counter_started = false;//baraye inke agar ertebat ba net ghat shod moghe
    //khoorooj choon cancel vase timer darim age timer ro intialize nakrde bashim stopped working mide

    private int totalWords_number = 0;
    private boolean inGame = true;//baraye inke agar az bazi kharej shodim dg request nade

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        next_word_bt = (Button) findViewById(R.id.next_word_bt);
        word_TextView = (TextView) findViewById(R.id.word);
        entered_word = (EditText) findViewById(R.id.enterd_word);
        check_bt = (Button) findViewById(R.id.check);
        timer = (TextView) findViewById(R.id.timer);
        totalScore_view = (TextView) findViewById(R.id.total_score);
        Button Help = (Button) findViewById(R.id.help_bt);

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        id = prefs.getString("userID", null);

        //difficaulty va category va type ro az activity ghabl migirad
        Bundle bundle = getIntent().getExtras();
        category = bundle.getString("category");
        difficulty = bundle.getString("difficulty");
        type = bundle.getString("type");
        ///////////////////////////////////////////////////////

        newSinglePlayerGame();

        // message.setVisibility(View.INVISIBLE);

        Help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert_dialog_function_help();

            }
        });

        check_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Player_score = timer.getText().toString();
                String Player_time = Integer.toString(15 - Integer.parseInt(Player_score));

                if (!entered_word.getText().toString().equals(""))
                    //   message.setVisibility(View.VISIBLE);

                    if (entered_word.getText().toString().equals(completeWord) && !timer.getText().toString().equals("0")) {

                        countDownTimer.cancel();

                        word_TextView.setText(completeWord);
                        //  message.setText();
                        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.success);
                        mediaPlayer.start();
                        //  didItOnce = true;

                        Total_gamescore += Integer.parseInt(Player_score);//showing total score for game ending

                        setAnswer(entered_word.getText().toString(),
                                Player_time, Player_score);

                        Snackbar.make(findViewById(R.id.singlePlayerActivity), "Congratulations !!! Your guess was RIGHT !"
                                , Snackbar.LENGTH_LONG)
                                .setActionTextColor(Color.YELLOW).show();

                        number_of_trueGuess++;

                        nextWord_func();
                    } else {

                        Snackbar.make(findViewById(R.id.singlePlayerActivity), "No guess again !!!"
                                , Snackbar.LENGTH_LONG)
                                .setActionTextColor(Color.YELLOW).show();
                    }

            }
        });


        next_word_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // didItOnce = false;

                //  message.setVisibility(View.INVISIBLE);

                totalScore_view.setText("Total score :" + Total_gamescore);

                entered_word.setText("");

                if (flag__nextWord_Timer.equals("yes")) {

                    countDownTimer.cancel();

                } else {

                    sendNextWord();

                }
            }
        });

    }

    public void nextWord_func() {
        //didItOnce = false;

        totalScore_view.setText("Total score :" + Total_gamescore);

        entered_word.setText("");

        if (flag__nextWord_Timer.equals("yes")) {

            countDownTimer.cancel();

            if (timer.getText().toString() != recivedTime) {

                sendNextWord();

            } else {

                sendNextWord();
            }

        } else {

            sendNextWord();

        }
    }

    public void counterResume(int timeLeft_onDialogPause) {
        //resume the counter
        countDownTimer = new CountDownTimer(timeLeft_onDialogPause * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timer.setText("0");

                nextWord_func();
            }
        };

        countDownTimer.start();
        Counter_started = true;
        ////////////////////////////////////////////
    }

    public void alert_dialog_function_help() {

        //gereftane time e baghi mande baraye edame bad az dialog
        final int timeLeft_onDialogPause = Integer.parseInt(timer.getText().toString());
        countDownTimer.cancel();
        ///////////////////////////////////////

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.help_dialog);
        dialog.setCancelable(true);

        Button yes = (Button) dialog.findViewById(R.id.yes);
        Button no = (Button) dialog.findViewById(R.id.no);
        dialog.show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (word_TextView.getText().toString().equals(completeWord)) {
                    dialog.cancel();

                    counterResume(timeLeft_onDialogPause);

                    Toast.makeText(getApplication(), "You have all the letters",
                            Toast.LENGTH_SHORT).show();


                } else {
                    getting_qmarks_index();
                    replace_char();
                    dialog.cancel();
                    counterResume(timeLeft_onDialogPause);
                }

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                counterResume(timeLeft_onDialogPause);

            }
        });

    }

    //peyda kardane index e horoofe ؟
    public void getting_qmarks_index() {
        //hey check mikonim bebinim ke incompleet daryaft shode ya na
        if (!(length > 0)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getting_qmarks_index();
                    Toast.makeText(getApplicationContext(), "Please wait...",
                            Toast.LENGTH_SHORT).show();
                }
            }, 500);
        } else {

            //horoof  ؟ ro joda kone
            for (int i = 0; i < length; i++) {

                if (incompleteWord.charAt(i) == '؟') {

                    indexlist_of_questionmarks.add(i);

                }
            }
            ///////////////////////////////////
        }
    }
    /////////////////////////////////////////////////////

    //baz kardan alamate soal ha va jaygozari harfe asli
    public void replace_char() {

        if (indexlist_of_questionmarks.isEmpty()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    replace_char();
                    Toast.makeText(getApplicationContext(), "Please wait...",
                            Toast.LENGTH_SHORT).show();
                }
            }, 500);

        } else {
            //  Toast.makeText(getApplicationContext(),"replace char working",Toast.LENGTH_SHORT)
            //     .show();
            int i = indexlist_of_questionmarks.get(arraylist_i);
            char unlocked_char = completeWord.charAt(i);
            StringBuilder stringBuilder = new StringBuilder(incompleteWord);
            stringBuilder.setCharAt(i, unlocked_char);
            incompleteWord = stringBuilder.toString();
            word_TextView.setText(incompleteWord);
            arraylist_i++;
        }

    }
    ////////////////////////////////////////////////////

    public void newSinglePlayerGame() {

        Total_gamescore = 0;
        totalScore_view.setText("Total score : " + String.valueOf(0));
        HashMap<String, String> info = new HashMap<>();

        info.put("action", "newGame");
        info.put("mode", "singlePlayer");
        info.put("userID", id);
        info.put("type", type);

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

        if (inGame) {

            indexlist_of_questionmarks.clear();

            arraylist_i = 0;

            totalWords_number++;

            HashMap<String, String> info = new HashMap<>();

            info.put("action", "sendNextWord");
            info.put("gameID", game_ID);
            info.put("userID", id);

            word_TextView.setText("");
            //message.setText("");
            incompleteWord = "";//intialize bekhatere getting_qmarks_index()


            JSONObject jsonObject = new JSONObject(info);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {

                        if (response.getString("dataIsRight").equals("yes")) {

                            flag__nextWord_Timer = "yes";

                            incompleteWord = response.getJSONObject("word").getString("incompleteWord");

                            completeWord = response.getJSONObject("word").getString("word");

                            recivedTime = response.getJSONObject("word").getString("time");

                            word_TextView.setText(incompleteWord);

                            //////////////////
                            countDownTimer = new CountDownTimer((Integer.parseInt(recivedTime)) * 1000, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    timer.setText("" + millisUntilFinished / 1000);
                                }

                                public void onFinish() {
                                    timer.setText("0");

                                    nextWord_func();
                                }
                            };
                            ////////////////////////////////////////////
                            countDownTimer.start();
                            Counter_started = true;

                            /////////////////
                            length = word_TextView.getText().length();
                            //////////////

                            Toast.makeText(getApplicationContext(), String.valueOf(length),
                                    Toast.LENGTH_SHORT).show();
                        } else {


                            Toast.makeText(getApplicationContext(),
                                    "next word dataIsRight =no ", Toast.LENGTH_LONG).show();


                        }

                    } catch (JSONException e) {

                        alert_dialog_function_game_end();
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


    public void alert_dialog_function_game_end() {

        countDownTimer.cancel();

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.game_end_dialog);
        dialog.setCancelable(false);

        Button start = (Button) dialog.findViewById(R.id.yes);
        Button cancel = (Button) dialog.findViewById(R.id.no);
        guesses_true = (TextView) dialog.findViewById(R.id.guesses_true);
        yourscore_gameEnd = (TextView) dialog.findViewById(R.id.player_score_gameEnd);
        guesses_false = (TextView) dialog.findViewById(R.id.guesses_false);

        String endgame_score = String.valueOf(Total_gamescore);
        yourscore_gameEnd.setText(endgame_score);

        guesses_true.setText(String.valueOf(number_of_trueGuess));
        guesses_false.setText(String.valueOf(totalWords_number - number_of_trueGuess));

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                word_TextView.setText("");
                //message.setText("");
                dialog.cancel();
                newSinglePlayerGame();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        dialog.show();


    }

    @Override
    protected void onDestroy() {

        inGame = false;

        if (Counter_started)

            countDownTimer.cancel();

        super.onDestroy();
    }

}


