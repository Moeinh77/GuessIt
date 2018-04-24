package com.taan.hasani.moein.guess_it.game_play;

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
import com.taan.hasani.moein.guess_it.Gson.makingGame_GSON;
import com.taan.hasani.moein.guess_it.Gson.recievedWord_GSON;
import com.taan.hasani.moein.guess_it.Gson.simpleRequest_GSON;
import com.taan.hasani.moein.guess_it.appcontroller.AppController;
import com.taan.hasani.moein.guess_it.helpingclasses.gameplayFunctions;
import com.taan.hasani.moein.guess_it.helpingclasses.Player;
import com.taan.hasani.moein.volley.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class single_Player extends AppCompatActivity {

    private EditText entered_word;
    private int number_of_trueGuess;
    private TextView word_TextView, timer;
    private int Total_gamescore = 0;
    private ArrayList<Integer> indexlist_of_questionmarks = new ArrayList<>();
    private String incompleteWord, id, completeWord, game_ID,
            url = "http://mamadgram.tk/guessIt.php";
    private TextView totalScore_view;
    private CountDownTimer countDownTimer;
    private String category, type, recivedTime;
    private int index;//baraye gereftane index alamate soal az list
    private Dialog dialog;
    private TextView wordnumber;
    private boolean inGame = true;//baraye inke agar az bazi kharej shodim dg request nade
    private int currentword_number;
    private int Totalwords;//tedad kole kalamt
    private gameplayFunctions functions;
    private Player player;
    private Button Edit;
    private JSONObject recievedWord_Jsonobj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        player = new Player(this);
        functions = new gameplayFunctions(this);
        Button next_word_bt = (Button) findViewById(R.id.next_word_bt);
        word_TextView = (TextView) findViewById(R.id.word);
        entered_word = (EditText) findViewById(R.id.enterd_word);
        Button check_bt = (Button) findViewById(R.id.check);
        timer = (TextView) findViewById(R.id.timer);
        totalScore_view = (TextView) findViewById(R.id.total_score);
        Button Help = (Button) findViewById(R.id.help_bt);

        Edit = (Button) findViewById(R.id.edit);//**************
        Edit.setVisibility(View.INVISIBLE);

        wordnumber = (TextView) findViewById(R.id.wordnumber);
        id = player.getId();

        //difficaulty va category va type ro az activity ghabl migirad
        Bundle bundle = getIntent().getExtras();
        category = bundle.getString("category");
        // difficulty = bundle.getString("difficulty");
        type = bundle.getString("type");
        Totalwords = bundle.getInt("totalwordsnumber");
        ///////////////////////////////////////////////////////

        newSinglePlayerGame();

        Help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert_dialog_function_help();

            }
        });

        check_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (timer != null) {
                    String Player_score = timer.getText().toString();
                    String Player_time = Integer.toString(15 - Integer.parseInt(Player_score));//***jaygozini 15 ba meghdar dar yafti az server

                    if (!entered_word.getText().toString().equals(""))
                        //   message.setVisibility(View.VISIBLE);

                        if (entered_word.getText().toString()
                                .trim().equalsIgnoreCase(completeWord)) {

                            countDownTimer.cancel();

                            word_TextView.setText(completeWord);
                            //  message.setText();
                            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.success);
                            mediaPlayer.start();
                            //  didItOnce = true;

                            Total_gamescore += Integer.parseInt(Player_score);//showing total score for game ending

                            //setAnswer*******
                            functions.setAnswer(game_ID, entered_word.getText().toString(),
                                    Player_time, Player_score, "yes");

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
            }
        });


        next_word_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                totalScore_view.setText("Total score :" + Total_gamescore);

                entered_word.setText("");

                countDownTimer.cancel();

                sendNextWord();

            }
        });


    }

    //*****************************
    private void editor() {

        if (player.getrole().equals("admin"))
            Edit.setVisibility(View.VISIBLE);

        countDownTimer.cancel();
        word_TextView.setText(completeWord);


        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexlist_of_questionmarks.clear();

                String newIncomlpeteword = entered_word.getText().toString();

                //age zabane farsi ro khastim edit konim alamat soal haro dors kone
                ////////////////////////////////////////////////////
                if (!completeWord.matches("[A-Za-z ]+?")) {

                    for (int i = 0; i < newIncomlpeteword.length(); i++) {
                        if (newIncomlpeteword.charAt(i) == '?')
                            indexlist_of_questionmarks.add(i);
                    }
                    ////////////////////////////////////////////////////

                    StringBuilder stringBuilder = new StringBuilder(newIncomlpeteword);

                    for (int i = 0; i < indexlist_of_questionmarks.size(); i++) {

                        //  Toast.makeText(getApplicationContext(),
                        //        String.valueOf(indexlist_of_questionmarks.get(i)),
                        // Toast.LENGTH_LONG).show();
                        stringBuilder.setCharAt(indexlist_of_questionmarks.get(i), '؟');

                    }

                    newIncomlpeteword = stringBuilder.toString();
                }
                ////////////////////////////////////////////////////

                if (completeWord.length() == newIncomlpeteword.length())

                    functions.addWordtoDB(completeWord, newIncomlpeteword,
                            recievedWord_Jsonobj);

                else
                    Toast.makeText(getApplicationContext(),
                            "به نظر در وارد کردن کلمه اشتباهی کرده اید..",
                            Toast.LENGTH_SHORT).show();
            }
        });
    }
    //*****************************

    public void nextWord_func() {

        totalScore_view.setText("Total score :" + Total_gamescore);

        entered_word.setText("");

        countDownTimer.cancel();

        sendNextWord();
    }

    public void counterResume(int timeLeft_onDialogPause) {
        //resume the counter
        countDownTimer = new CountDownTimer(timeLeft_onDialogPause * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText(String.valueOf(millisUntilFinished / 1000));//***chaneged from +""
            }

            public void onFinish() {
                timer.setText("0");
                nextWord_func();
            }
        };

        countDownTimer.start();
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
    public void getting_qmarks_index(final String incompleteWord) {
        //hey check mikonim bebinim ke incompleet daryaft shode ya na
        if (!(completeWord.length() > 0) && inGame) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getting_qmarks_index(incompleteWord);
                    Toast.makeText(getApplicationContext(), "Please wait...",
                            Toast.LENGTH_SHORT).show();
                }
            }, 500);
        } else {

            //horoof  alamate soal ro joda kone
            for (int i = 0; i < completeWord.length(); i++) {

                if (completeWord.matches("[A-Za-z ]+?")) {

                    if (incompleteWord.charAt(i) == '?') {

                        indexlist_of_questionmarks.add(i);

                    }
                } else {
                    if (incompleteWord.charAt(i) == '؟') {

                        indexlist_of_questionmarks.add(i);

                    }
                }

            }
            ///////////////////////////////////
        }
    }
    /////////////////////////////////////////////////////

    //baz kardan alamate soal ha va jaygozari harfe asli
    public void replace_char() {

        if (indexlist_of_questionmarks.isEmpty() && inGame) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getting_qmarks_index(incompleteWord);
                    replace_char();
                    Toast.makeText(getApplicationContext(), "indexlist_of_questionmarks.isEmpty",
                            Toast.LENGTH_SHORT).show();
                }
            }, 500);

        } else {
            //  Toast.makeText(getApplicationContext(),"replace char working",Toast.LENGTH_SHORT)
            //     .show();
            int i = indexlist_of_questionmarks.get(index);
            char unlocked_char = completeWord.charAt(i);
            StringBuilder stringBuilder = new StringBuilder(incompleteWord);
            stringBuilder.setCharAt(i, unlocked_char);
            incompleteWord = stringBuilder.toString();
            word_TextView.setText(incompleteWord);
            index++;
        }

    }
    ////////////////////////////////////////////////////

    //gsonized!!!
    public void newSinglePlayerGame() {

        currentword_number = 0;//kalame hara az avl beshmarad
        number_of_trueGuess = 0;
        Total_gamescore = 0;
        totalScore_view.setText("Total score : " + String.valueOf(0));

        //Functions gameclass = new Functions(context);

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

                makingGame_GSON makingGame;
                Gson gson = new Gson();
                makingGame = gson.fromJson(response.toString(), makingGame_GSON.class);

                game_ID = makingGame.gameID;

                if (makingGame.dataIsRight.equals("yes")) {
                    setGameSettings();
                } else {

                    Toast.makeText(getApplicationContext(), " single player data is right=no ,sth went wrong..."
                            , Toast.LENGTH_SHORT).show();
                    newSinglePlayerGame();
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

    //gsonized!!!
    public void setGameSettings() {

        HashMap<String, String> info = new HashMap<>();

        info.put("action", "setGameSetting");
        info.put("userID", id);
        info.put("gameID", game_ID);

        info.put("categories", category);

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                simpleRequest_GSON request;
                Gson gson = new Gson();
                request = gson.fromJson(response.toString(), simpleRequest_GSON.class);

                if (request.dataIsRight.equals("yes")) {

                    sendNextWord();

                } else {

                    Toast.makeText(getApplicationContext(), "setsettings data is right no single player", Toast.LENGTH_LONG).show();

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

    //gsonized!!!
    public void sendNextWord() {

        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor();
            }
        });

        if (inGame) {

            currentword_number++;
////////////////////////////////////////////////////////////////////////////////
            if (currentword_number == Totalwords + 1)
                wordnumber.setText(Totalwords + "/" + Totalwords);
            else
                wordnumber.setText(currentword_number + "/" + Totalwords);
////////////////////////////////////////////////////////////////////////////////
            indexlist_of_questionmarks.clear();

            index = 0;

            HashMap<String, String> info = new HashMap<>();

            info.put("action", "sendNextWord");
            info.put("gameID", game_ID);
            info.put("userID", id);

            word_TextView.setText("");

            incompleteWord = "";//intialize bekhatere getting_qmarks_index()

            JSONObject jsonObject = new JSONObject(info);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    simpleRequest_GSON request;
                    Gson gson = new Gson();
                    request = gson.fromJson(response.toString(), simpleRequest_GSON.class);

                    try {
                        recievedWord_GSON recievedWord = gson.fromJson(response.getJSONObject("word").toString(), recievedWord_GSON.class);

                        //      Toast.makeText(getApplicationContext(),
                        //              response.toString(),Toast.LENGTH_LONG).show();

                        if (request.dataIsRight.equals("yes")) {

                            Boolean lastWordCheck = recievedWord.word.equals("outOfWords");

                            if (!lastWordCheck)//agar be outofwords nareside bood
                            {
                                recievedWord_Jsonobj = response.getJSONObject("word");

                                //   Toast.makeText(getApplicationContext(),
                                //          response.toString(),Toast.LENGTH_LONG).show();

                                incompleteWord = recievedWord.incompleteWord;

                                completeWord = recievedWord.word;

                                recivedTime = recievedWord.time;

                                word_TextView.setText(incompleteWord);

                                //////////////////joda kardane alamat hay soal
                                getting_qmarks_index(incompleteWord);
                                //////////////////

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

                                /////////////////
                                //length = word_TextView.getText().length();
                                //////////////

//                            Toast.makeText(getApplicationContext(), String.valueOf(length),
//                                    Toast.LENGTH_SHORT).show();
                            } else if (recievedWord.word.equals("outOfWords"))//agar kalamt tamam shod
                                //outofwords miad ke bad dialog ro neshan midim
                                alert_dialog_function_game_end();

                        } else {

                            Toast.makeText(getApplicationContext(),
                                    "next word dataIsRight =no ", Toast.LENGTH_LONG).show();

                        }

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.toString(),
                                Toast.LENGTH_SHORT).show();
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

    public void alert_dialog_function_game_end() {

        if (countDownTimer != null)
        countDownTimer.cancel();

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.game_end_singleplayer_dialog);
        dialog.setCancelable(false);

        Button start = (Button) dialog.findViewById(R.id.yes);
        Button cancel = (Button) dialog.findViewById(R.id.no);
        TextView guesses_true = (TextView) dialog.findViewById(R.id.guesses_true);
        TextView yourscore_gameEnd = (TextView) dialog.findViewById(R.id.player_score_gameEnd);
        TextView guesses_false = (TextView) dialog.findViewById(R.id.guesses_false);
        TextView newHighScore_view = (TextView) dialog.findViewById(R.id.newHighscore);

        newHighScore_view.setVisibility(View.INVISIBLE);

        String endgame_score = String.valueOf(Total_gamescore);
        yourscore_gameEnd.setText(endgame_score);


        //dar soorat zadane highscore jadid
        if (Total_gamescore > player.getHighscore()) {

            //save kardane highscore e jadid
            player.setHighscore(Total_gamescore);
            //////////////////////////////////

            newHighScore_view.setVisibility(View.VISIBLE);//neshan dadan payame highscore

            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.success);
            mediaPlayer.start();

        }
        /////////////////////////////////////////////////////////

        guesses_true.setText(String.valueOf(number_of_trueGuess));
        guesses_false.setText(String.valueOf(Totalwords - number_of_trueGuess));

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                word_TextView.setText("");
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
    public void onBackPressed() {

        AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        alertDialogBuilder.setTitle("ترک بازی");
        alertDialogBuilder
                .setMessage("آیا میخواهید از بازی را ترک کنید ؟")
                .setCancelable(false)
                .setPositiveButton("بلی", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        finish();

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

        inGame = false;


        if (countDownTimer != null)

            countDownTimer.cancel();

        super.onDestroy();
    }

}


