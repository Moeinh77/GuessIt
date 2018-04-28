package com.taan.hasani.moein.guess_it.game_menu;

import android.content.Intent;
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
import com.securepreferences.SecurePreferences;
import com.taan.hasani.moein.guess_it.Gson.login_GSON;
import com.taan.hasani.moein.guess_it.appcontroller.AppController;
import com.taan.hasani.moein.volley.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {

    //  private final String MY_PREFS_NAME ="username and password" ;
    String url = "http://mamadgram.tk/guessIt.php_2";
    // Fname_editext, Lname_edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final Button signup = findViewById(R.id.signup);
        final EditText username_editext = findViewById(R.id.username_txt);
        final EditText password_editext = findViewById(R.id.password_txt);
        final EditText name_edittext = findViewById(R.id.name_txt);
        //   final EditText mobileNumber_edittext=findViewById(R.id.mobileNumber_txt);

        final HashMap<String, String> info = new HashMap<>();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = username_editext.getText().toString();
                final String password = password_editext.getText().toString();
                String name = name_edittext.getText().toString();
                // String mobileNumber=mobileNumber_edittext.getText().toString();

                info.put("action","signup");
                info.put("password",password);
                info.put("username",username);
                info.put("name", name);
                //info.put("mobileNumber",mobileNumber);

                JSONObject jsonObject=new JSONObject(info);
                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                        url, jsonObject,new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            if (response.getString("dataIsRight").equals("yes")) {

                                login(username, password);//if signed up sucessfully

                            } else {
                                Toast.makeText(getApplicationContext(), "signup unsucessful !! ", Toast.LENGTH_LONG).show();
                            }

                        }catch (JSONException e){
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


                        SecurePreferences.Editor securePreferences = new SecurePreferences(getApplicationContext()).edit();
                        securePreferences.putString("userName", username_);
                        securePreferences.putString("token", loginGson.token);
                        securePreferences.apply();

                        Intent i = new Intent(getApplicationContext(), Main_menu.class);
                        startActivity(i);
                        finish();
                        //  Toast.makeText(getApplicationContext(),
                        //          "Welcome "+username_, Toast.LENGTH_LONG).show();

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
