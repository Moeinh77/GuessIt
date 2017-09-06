package com.taan.hasani.moein.volley.game;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.taan.hasani.moein.volley.appcontroller.AppController;
import com.taan.hasani.moein.volley.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Random;

public class single_Player extends AppCompatActivity {

    private EditText entered_word;
    private Button check_bt;
    private TextView incomplete_TextView,message;
    HashMap<String, String> info = new HashMap<>();
    String url = "http://online6732.tk/guessIt.php";
    JSONArray jsonArray;
    Random rand = new Random();
    int  i = rand.nextInt(2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_player);

        message=(TextView)findViewById(R.id.message);
        incomplete_TextView=(TextView)findViewById(R.id.incompleteword);
        entered_word=(EditText)findViewById(R.id.enterd_word);
        check_bt=(Button)findViewById(R.id.check);

        OnOpening();

        check_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String completeword="didnt recieve the word... :( ";
                try {
                    completeword = new String(jsonArray.getJSONObject(i).getString("word")
                            .getBytes("ISO-8859-1"), "UTF-8");
                }
                catch (JSONException je){

                }
                catch (UnsupportedEncodingException e){

                }

                if(entered_word.getText().toString().equals(completeword)){

                    message.setText("Congratulations !!! Your guess was RIGHT !");
                    incomplete_TextView.setText(completeword);

                }else{
                  message.setText("No,Guess again !");
                }
            }
        });

    }

    public void OnOpening(){

        //getting id for the player
        String MY_PREFS_NAME="username and password";
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String id = prefs.getString("userID", null);
        //////////////////////////////////////////

        Log.v("","########id: "+id);

        info.put("action","newGame");
        info.put("category","ورزشی");
        info.put("userID",id);
        info.put("mode","singlePlayer");

        JSONObject jsonObject=new JSONObject(info);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,
                url, jsonObject,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    jsonArray=new JSONArray(response.getString("words"));

                    String incompleteWord = new String( jsonArray.getJSONObject(i).getString("incompleteWord")
                            .getBytes("ISO-8859-1"), "UTF-8");

                    incomplete_TextView.setText(incompleteWord);
//                            Toast.makeText(getApplicationContext(),
//                                    incompleteWord,Toast.LENGTH_LONG).show();
                }catch (JSONException e){
                    Toast.makeText(getApplicationContext(),
                            "Json Execption",Toast.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.toString(),Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }
}

