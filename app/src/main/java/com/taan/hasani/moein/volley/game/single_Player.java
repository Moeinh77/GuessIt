package com.taan.hasani.moein.volley.game;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.taan.hasani.moein.volley.R;
import com.taan.hasani.moein.volley.appcontroller.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class single_Player extends AppCompatActivity {

    private EditText entered_word;
    private Button check_bt,next_word_bt;
    private TextView incomplete_TextView, message;
    private HashMap<String, String> info = new HashMap<>();
    private JSONArray jsonArray;
    private String MY_PREFS_NAME = "username and password";

    private String incompleteWord,id,completeWord,game_ID,
            url = "http://online6732.tk/guessIt.php";

    private SharedPreferences prefs ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_player);

        next_word_bt=(Button)findViewById(R.id.next_word_bt);
        message = (TextView) findViewById(R.id.message);
        incomplete_TextView = (TextView) findViewById(R.id.incompleteword);
        entered_word = (EditText) findViewById(R.id.enterd_word);
        check_bt = (Button) findViewById(R.id.check);

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        id = prefs.getString("userID", null);

        //starts the first game
        OnOpening();
        ///////////////////////

        check_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (entered_word.getText().toString().equals(completeWord)) {

                    incomplete_TextView.setText(completeWord);
                    message.setText("Congratulations !!! Your guess was RIGHT !");
//                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());
//                    alertDialogBuilder.setTitle("Congratulations !");
//                    //alertDialogBuilder.setIcon(R.drawable.);
//                    alertDialogBuilder
//                            .setMessage("Congratulations !!! Your guess was RIGHT !")
//                            .setCancelable(false)
//                            .setPositiveButton("Next word",new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog,int id) {
//
//                                    dialog.cancel();
//                                    next_Word_func();
//
//                                }
//                            })
//                            .setNegativeButton("Exit this game",new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog,int id) {
//                                    finish();
//                                    Intent i=new Intent(single_Player.this,choosing_theGame.class);
//                                    startActivity(i);
//                                }
//                            });
//
//                    AlertDialog alertDialog = alertDialogBuilder.create();
//                    alertDialog.show();

                } else {
                    message.setText("No,Guess again !");
                }
            }
        });


        next_word_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next_Word_func();

            }
        });

    }


    public void OnOpening() {

        //Log.v("", "########id: " + id);

        info.put("action", "newGame");
        info.put("category", "ورزشی");
        info.put("userID", id);
        info.put("mode", "singlePlayer");

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {
                    jsonArray = new JSONArray(response.getString("words"));

                    completeWord = new String(jsonArray.getJSONObject(0).getString("word")
                            .getBytes("ISO-8859-1"), "UTF-8");

                    incompleteWord = new String(jsonArray.getJSONObject(0).getString("incompleteWord")
                            .getBytes("ISO-8859-1"), "UTF-8");

                    incomplete_TextView.setText(incompleteWord);

                    // Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                    game_ID = response.getString("gameID");

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
                        "***Volley  :" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }


    public void next_Word_func() {

        info.put("action", "sendNextWord");
        info.put("playerOneTime", "10");
        info.put("playerOneScore", "10");
        info.put("gameID", game_ID);
        info.put("userID", id);

        entered_word.setText("");
        message.setText("");

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    // jsonArray=new JSONArray(response.getString("words"));

                    completeWord = new String(response.getString("word")
                            .getBytes("ISO-8859-1"), "UTF-8");

                    incompleteWord = new String(response.getString("incompleteWord")
                            .getBytes("ISO-8859-1"), "UTF-8");

                    incomplete_TextView.setText(incompleteWord);

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
                        "***Volley  :" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);


    }
}

