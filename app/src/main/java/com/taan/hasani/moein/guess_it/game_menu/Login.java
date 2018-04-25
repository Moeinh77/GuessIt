package com.taan.hasani.moein.guess_it.game_menu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.taan.hasani.moein.guess_it.Gson.login_GSON;
import com.taan.hasani.moein.guess_it.Gson.simpleRequest_GSON;
import com.taan.hasani.moein.guess_it.appcontroller.AppController;
import com.taan.hasani.moein.guess_it.helpingclasses.Player;
import com.taan.hasani.moein.volley.R;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;

public class Login extends AppCompatActivity {

    Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login_bt = (Button) findViewById(R.id.login);
        final EditText username_editext = (EditText) findViewById(R.id.username);
        final EditText password_editext = (EditText) findViewById(R.id.password);
        player = new Player();

        login_bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String username=username_editext.getText().toString();
                String password=password_editext.getText().toString();

                login(username, password);

            }
        });

    }

    public void login(final String username_, final String password_) {

        String url = "http://mamadgram.tk/guessIt_2.php";

        HashMap<String, String> info = new HashMap<>();

        info.put("action", "login");
        info.put("username", username_);
        info.put("password", password_);

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    login_GSON loginGson;
                    Gson gson = new Gson();

                    loginGson = gson.fromJson(response.toString()
                            , login_GSON.class);

                    if (loginGson.dataIsRight.equals("yes")) {

                        Toast.makeText(getApplicationContext(),
                                "Response :" + loginGson.token, Toast.LENGTH_SHORT).show();
                        //  userInfo_GSON user;

                        // user = gson.fromJson(response.getJSONObject("user").toString()
                        //          , userInfo_GSON.class);//gereftan etelaat user az
                        // json object userdakhel response

                        //   setrole(user.role);
                        //   setName(user.name);
                        //   setUsername(user.username);
                        //   setPassword(user.password);

                        //opens the gamechoose activity

                        player.setToken(loginGson.token);
                        Toast.makeText(getApplicationContext(),
                                "Response :" + player.getToken(), Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(getApplicationContext(), Main_menu.class);
                        startActivity(i);
                        finish();
                        Toast.makeText(getApplicationContext(),
                                "Welcome ", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Wrong username or password", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "Error login", Toast.LENGTH_LONG).show();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }
}
