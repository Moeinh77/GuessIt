package com.taan.hasani.moein.volley;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    String url = "http://online6732.tk/guessIt.php";
    private String password;
    private String username;
    private String name;
    private Button login_bt;
    private Button signup_bt;
    private Button gameinfo_bt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameinfo_bt=(Button)findViewById(R.id.game_info);
        login_bt=(Button)findViewById(R.id.login);
        signup_bt=(Button)findViewById(R.id.signup);

        signup_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_up();
            }
        });

        gameinfo_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game_info();
            }
        });

        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }


    public void game_info(){
        HashMap<String, String> info = new HashMap<String, String>();


        info.put("action","newGame");
        info.put("catagory","ورزشی");
        info.put("player","Moein");
        info.put("mode","singlePlayer");


        JSONObject jsonObject=new JSONObject(info);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,
                url, jsonObject,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Toast.makeText(getApplicationContext(),
                            response.getString("responseData"),Toast.LENGTH_LONG).show();
                }catch (JSONException e){
                    Toast.makeText(getApplicationContext(),
                            "Failed",Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "error",Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void login(){

        Intent i=new Intent(MainActivity.this,Login.class);
        startActivity(i);
    }

    public void sign_up(){

        HashMap<String, String> info = new HashMap<String, String>();

        username="Moein";
        password="&&password__";
        name="Moeinhasani";

        info.put("action","signup");
        info.put("password",password);
        info.put("username",username);
        info.put("name",name);

        JSONObject jsonObject=new JSONObject(info);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,
                url, jsonObject,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Toast.makeText(getApplicationContext(),
                            response.getString("responseData"),Toast.LENGTH_LONG).show();
                }catch (JSONException e){
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
}



