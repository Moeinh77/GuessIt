package com.taan.hasani.moein.guess_it.game;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.taan.hasani.moein.volley.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class two_player extends AppCompatActivity {

    private String MY_PREFS_NAME = "username and password",
            url = "http://online6732.tk/guessIt.php", id, completeWord, incompleteWord;
    private SharedPreferences prefs;
    private String gamedID;
    private TextView word, message;
    private EditText entered_word;
    private Button check_bt, nextWord_bt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_player);

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        id = prefs.getString("userID", null);

        newTwoPlayerGame();

        check_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (entered_word.getText().toString().equals(completeWord)) {

                    word.setText(completeWord);
                    message.setText("Congratulations !!! Your guess was RIGHT !");

                } else {

                    message.setText("No,Guess again !");

                }

                setAnswer(entered_word.getText().toString());

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

                    if (response.getString("gameID").equals("-1")) {

                        isMyGameReady();

                    } else {

                        gamedID = response.getString("gameID");

                        setGameSettings();

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

                    if (response.getString("gameID").equals("-1")) {
                        isMyGameReady();

                    } else {

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

        String categories = "ورزشی,ورزشی,ورزشی,ورزشی,ورزشی,ورزشی,ورزشی,ورزشی,ورزشی,ورزشی";
        try {

            JSONArray catagories_jsonarray = new JSONArray(categories);
            info.put("categories", catagories_jsonarray.toString());

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error in jsonarray ", Toast.LENGTH_LONG).show();
        }

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

                        Toast.makeText(getApplicationContext(), "There wae an error with setting the game settings", Toast.LENGTH_LONG).show();

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

                try {

                    incompleteWord = new String(response.getString("incompleteWord")
                            .getBytes("ISO-8859-1"), "UTF-8");

                    completeWord = new String(response.getString("word")
                            .getBytes("ISO-8859-1"), "UTF-8");

                    word.setText(incompleteWord);



                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            e.toString(), Toast.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
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


    public void setAnswer(String entered_word) {

        HashMap<String, String> info = new HashMap<>();
        HashMap<String, String> answer_hashmap = new HashMap<>();
        JSONObject answer = new JSONObject(answer_hashmap);
        /////////////////////////
        answer_hashmap.put("time", "15");
        answer_hashmap.put("score", "15");
        answer_hashmap.put("answer", entered_word);
        /////////////////////////
        info.put("action", "sendNextWord");
        info.put("gameID", gamedID);
        info.put("userID", id);
        info.put("answer", answer.toString());
        /////////////////////////

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


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
