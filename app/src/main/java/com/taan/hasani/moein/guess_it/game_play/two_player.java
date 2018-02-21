package com.taan.hasani.moein.guess_it.game_play;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.taan.hasani.moein.guess_it.Gson.gameInfo_GSON;
import com.taan.hasani.moein.guess_it.Gson.recievedWord_GSON;
import com.taan.hasani.moein.guess_it.Gson.simpleRequest_GSON;
import com.taan.hasani.moein.guess_it.appcontroller.AppController;
import com.taan.hasani.moein.guess_it.helpingclasses.gameplayFunctions;
import com.taan.hasani.moein.guess_it.helpingclasses.Player;
import com.taan.hasani.moein.volley.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class two_player extends AppCompatActivity {

    private String url = "http://mamadgram.tk/guessIt.php", completeWord,
            incompleteWord, gamedID, recivedTime;
    private int number_of_trueGuess;
    private TextView word, message, timer, player2_textview, player1_textview;
    private EditText entered_word;
    private CountDownTimer countDownTimer;
    private String turn;
    // private int spent_time = 0;
    //  private boolean imIngame = true;//agar player bazira tark kard
    //

    private boolean word_loaded;//baraye jelo giri az crash dar soorat zadan next ya check
    //agar kalame load nashode bashad
    //private int currentword_number;
    private int Toatalwords = 0;//tedad kalamte har bazi ke az server miad
    String Rivalscore_gameEnd, Playerscore_gameEnd;
    private String status = " ";//baraye inke dar shoorooe bazi ke hanooz daryaft nashode status be nullptr nakhorim

    private Player player;//etelaat player
    private gameplayFunctions gamefuncs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_player);

        Button check_bt = (Button) findViewById(R.id.check_bt);
        //Button nextWord_bt = (Button) findViewById(R.id.nextWord_bt);
        word = (TextView) findViewById(R.id.word);
        message = (TextView) findViewById(R.id.message);
        entered_word = (EditText) findViewById(R.id.enteredWord);
        timer = (TextView) findViewById(R.id.timer);
        player2_textview = (TextView) findViewById(R.id.rivalscore);
        player1_textview = (TextView) findViewById(R.id.yourscore);
        player = new Player(this);
        gamefuncs = new gameplayFunctions(this);

        //gameID ra az playerGame_loading migirad
        Bundle bundle = getIntent().getExtras();
        gamedID = bundle.getString("gamedID");
        ///////////////////////////////////////////////////////

        message.setVisibility(View.INVISIBLE);
        word.setVisibility(View.INVISIBLE);

        number_of_trueGuess = 0;
        //   currentword_number = 0;

        gameInfo();

        sendNextWord();

        check_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (word_loaded) {

                    if (turn.equals("notmyturn")) {

                        Toast.makeText(getApplicationContext(), "لطفا صبر کنید...", Toast.LENGTH_SHORT).show();

                    } else if (!timer.getText().toString().equals("")) {

                        String Player_score = timer.getText().toString();
                        String Player_time = Integer.toString(15 - Integer.parseInt(Player_score));
                        String myturn;

                        if (!entered_word.getText().toString().equals("")) {

                            if (entered_word.getText().toString().trim().equalsIgnoreCase(completeWord)) {

                                countDownTimer.cancel();

                                number_of_trueGuess++;

                                myturn = "no";//playerdg javab dade hala bayad nobat ro avaz konim

                                word.setText(completeWord);

                                Snackbar.make(findViewById(R.id.twoPlayerActivity), "Congratulations !!! Your guess was RIGHT !"
                                        , Snackbar.LENGTH_LONG)
                                        .setActionTextColor(Color.YELLOW).show();

                                //seda movafaghat dar soorat javab doros
                                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),
                                        R.raw.success);
                                mediaPlayer.start();
                                ////////////////////////////////////////////////////////

                                gamefuncs.setAnswer(gamedID, entered_word.getText().toString(),
                                        Player_time, Player_score, myturn);

                                //baraye check kardan nobat
                                sendNextWord();
                                ////////////////////////////

                            } else {
                                Snackbar.make(findViewById(R.id.twoPlayerActivity), "No,Guess again !"
                                        , Snackbar.LENGTH_LONG)
                                        .setActionTextColor(Color.YELLOW).show();

                                myturn = "yes";//javab ghalatehamchenan nobat bazikon baghi mimanad

                                gamefuncs.setAnswer(gamedID, entered_word.getText().toString(),
                                        Player_time, Player_score, myturn);

                            }

                        }
                    }
                }
            }
        });

//        nextWord_bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (word != null)//taghie dadam words loaded ro hazf kardam
//                {
//
//                    if (flag__nextWord_Timer.equals("yes")) {
//
//                        countDownTimer.cancel();
//
//                        if (timer.getText().toString() != recivedTime) {
//
//                            spent_time = Integer.parseInt(recivedTime)
//                                    - Integer.parseInt(timer.getText().toString());
//
//                            sendNextWord();
//
//                        } else {
//
//                            sendNextWord();
//                        }
//
//                    } else {
//
//                        sendNextWord();
//
//                    }
//                }
//            }
//
//        });

    }

    //gsonized!!!
    private void sendNextWord() {

        if (!status.equals("game ended")) {

            HashMap<String, String> info = new HashMap<>();

            info.put("action", "sendNextWord");
            info.put("gameID", gamedID);
            info.put("userID", player.getId());

            entered_word.setText("");
            word.setText("");
            message.setText("");
            timer.setText("");

            JSONObject jsonObject = new JSONObject(info);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Gson gson = new Gson();
                    simpleRequest_GSON simpleRequest;
                    simpleRequest = gson.fromJson(response.toString(), simpleRequest_GSON.class);

                    if (simpleRequest.dataIsRight.equals("yes")) {
                        //dataIsRight zamani ke nobatet bashe yes miad
                        turn = "myturn";

                        try {
                            recievedWord_GSON recievedWord;

                            recievedWord = gson.fromJson(response.getJSONObject("word").toString(), recievedWord_GSON.class);
//                            //agar kalame ha tamam shode bashad dialog payan ro miare
//                            if (currentword_number == (Toatalwords + 1)) {
//
//                                gameEnd_Dialog();//send next word
//                            }
                            ///////////////////////////////////////////////////////////

                            //flag__nextWord_Timer = "yes";

                            incompleteWord = recievedWord.incompleteWord;

                            completeWord = recievedWord.word;

                            recivedTime = recievedWord.time;

                            Toast.makeText(getApplicationContext(),
                                    recievedWord.word, Toast.LENGTH_SHORT).show();

                            ////////////////////////////////////////////

                            try {//agar recieved time ee dar kar bood
                                countDownTimer = new CountDownTimer(Integer.parseInt(recivedTime) * 1000, 1000) {

                                    public void onTick(long millisUntilFinished) {
                                        timer.setText(String.valueOf(millisUntilFinished / 1000));

                                    }

                                    public void onFinish() {
                                        timer.setText("0");
                                        gamefuncs.setAnswer(gamedID, entered_word.getText().toString(),
                                                "0", "0", "no");
                                        //spent_time = 0;
                                        word_loaded = false;
                                        // check kardane nobat
                                        sendNextWord();
                                        ///////////////////////
                                    }
                                };
                            } catch (NumberFormatException e) {
                                countDownTimer = new CountDownTimer(15 * 1000, 1000) {
                                    //Integer.parseInt(recivedTime)
                                    public void onTick(long millisUntilFinished) {
                                        timer.setText(String.valueOf(millisUntilFinished / 1000));

                                    }

                                    public void onFinish() {
                                        timer.setText("0");
                                        gamefuncs.setAnswer(gamedID, entered_word.getText().toString(),
                                                "0", "0", "no");
                                        //spent_time = 0;
                                        word_loaded = false;
                                        // check kardane nobat
                                        sendNextWord();
                                        ///////////////////////
                                    }
                                };
                                ////////////////////////////////////////////
                            }

                            countDownTimer.start();

                            word.setVisibility(View.VISIBLE);
                            word.setText(incompleteWord);

                            word_loaded = true;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {

                        turn = "notmyturn";

                        message.setVisibility(View.INVISIBLE);
                        word.setVisibility(View.INVISIBLE);

                        timer.setText("لطفا صبر کنید...");

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

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),
                            "sendNextWord***Volley  :" + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });

            AppController.getInstance().addToRequestQueue(jsonObjectRequest);

        }
    }

    //gsonized!!!
    private void gameInfo() {

        HashMap<String, String> info = new HashMap<>();

        info.put("action", "sendGameInformation");
        info.put("userID", player.getId());
        info.put("gameID", gamedID);

        JSONObject jsonObject = new JSONObject(info);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                gameInfo_GSON gameInfo;
                gameInfo = gson.fromJson(response.toString(), gameInfo_GSON.class);

                status = gameInfo.status;//tamam shodan ya nashodane bazi ra mifahmad

                if (player.getId().equals(gameInfo.playerOneID)) {

                    player1_textview.setText("شما : " + gameInfo.playerOneTotalScore);

                    player2_textview.setText(gameInfo.playerTwoUsername
                            // + "امتیاز "
                            +
                            " : " + gameInfo.playerTwoTotalScore);

                    Playerscore_gameEnd = gameInfo.playerOneTotalScore;
                    Rivalscore_gameEnd = gameInfo.playerTwoTotalScore;

                } else if (player.getId().equals(gameInfo.playerTwoID)) {

                    player1_textview.setText("امتیاز شما : " + gameInfo.playerTwoTotalScore);

                    player2_textview.setText(gameInfo.playerOneUsername +
                            //"امتیاز " +
                            " : " + gameInfo.playerOneTotalScore);

                    Playerscore_gameEnd = gameInfo.playerTwoTotalScore;
                    Rivalscore_gameEnd = gameInfo.playerOneTotalScore;
                }

                if (Toatalwords == 0) {
                    Toatalwords = Integer.parseInt(gameInfo.numberOfWords);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

        //baraye inke har lahze check shavad ke harif dakhel bazi hast ya na
        if (!status.equals("game ended")) {//agar dakhel ast baz check shavad
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    gameInfo();
                }
            }, 2000);
        } else {
            gameEnd_Dialog();
        }
    }

    private void gameLeft() {

        HashMap<String, String> info = new HashMap<>();

        info.put("action", "gameStop");
        info.put("userID", player.getId());
        info.put("gameID", gamedID);


        JSONObject jsonObject = new JSONObject(info);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
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

    private void gameEnd_Dialog() {

        // if (status.equals("game ended")) {

        if (countDownTimer != null) countDownTimer.cancel();

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.game_end_twoplayer_dialog);
        dialog.setCancelable(false);

        Button cancel = (Button) dialog.findViewById(R.id.no);
        TextView guesses_true = (TextView) dialog.findViewById(R.id.guesses_true);
        TextView yourscore_gameEnd = (TextView) dialog.findViewById(R.id.player_score_gameEnd);
        TextView guesses_false = (TextView) dialog.findViewById(R.id.guesses_false);
        TextView rivalScore = (TextView) dialog.findViewById(R.id.rivalScore);
        // TextView newHighScore_view = (TextView) dialog.findViewById(R.id.newHighscore);

        // newHighScore_view.setVisibility(View.INVISIBLE);

        yourscore_gameEnd.setText(Playerscore_gameEnd);
        rivalScore.setText(Rivalscore_gameEnd);

        guesses_true.setText(String.valueOf(number_of_trueGuess));
        guesses_false.setText(String.valueOf(Toatalwords - number_of_trueGuess));

        //  int highscore = prefs.getInt("HighScore", 0);//gereftane higscore

        //dar soorat zadane highscore jadid
//        if (Total_gamescore > highscore) {
//
//            //save kardane highscore e jadid
//            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME
//                    , MODE_PRIVATE).edit();
//            editor.putInt("HighScore", Total_gamescore);
//            editor.apply();
//            //////////////////////////////////
//
//            newHighScore_view.setVisibility(View.VISIBLE);//neshan dadan payame highscore
//
//            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.success);
//            mediaPlayer.start();
//
//        }
        /////////////////////////////////////////////////////////

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dialog.show();
        //  }
// else {
//
//            if (inGame) {
//
//                new Handler().postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        gameEnd_Dialog();
//                    }
//                }, 1000);
//
//            }
//        }

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        alertDialogBuilder.setTitle("ترک بازی");
        alertDialogBuilder
                .setMessage("آیا میخواهید از بازی را ترک کنید ؟")
                .setCancelable(false)
                .setPositiveButton("بلی", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        gameLeft();

                    }
                })
                .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

        alertDialogBuilder.setIcon(R.drawable.logout);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onDestroy();
    }


}
