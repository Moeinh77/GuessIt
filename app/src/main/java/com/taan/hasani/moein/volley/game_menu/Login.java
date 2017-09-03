package com.taan.hasani.moein.volley.game_menu;

import android.app.Activity;
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
import com.taan.hasani.moein.volley.appcontroller.AppController;
import com.taan.hasani.moein.volley.R;
import com.taan.hasani.moein.volley.game.choosing_theGame;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;

public class Login extends AppCompatActivity {

    String url = "http://online6732.tk/guessIt.php";
    private Button login_bt;
    private EditText username_editext,password_editext;
    private final String MY_PREFS_NAME="username and password";
    final HashMap<String, String> info = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_bt=(Button)findViewById(R.id.login);
        username_editext=(EditText)findViewById(R.id.username);
        password_editext=(EditText)findViewById(R.id.password);


        login_bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String username=username_editext.getText().toString();
                String password=password_editext.getText().toString();


                //saving usename and password
                save_user_and_pass(username,password);
                ///////////////////////////////////

                //sending user and pass to server
                sending_info(username,password,Login.this);
                ///////////////////////////////////

            }
        });

    }

    public void save_user_and_pass(String username__,String password__){

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("usename", username__);
        editor.putString("password", password__);
        editor.apply();
        Toast.makeText(getApplicationContext(),
                "user :"+username__+"pass :"+password__,Toast.LENGTH_LONG).show();

    }

    public void sending_info(String username_, String password_, final Activity activity){

        info.put("action","login");
        info.put("username",username_);
        info.put("password",password_);

        JSONObject jsonObject=new JSONObject(info);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,
                url, jsonObject,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    // Toast.makeText(getApplicationContext(),
                    //         response.getString("dataIsRight"),Toast.LENGTH_LONG).show();
                    if(response.getString("dataIsRight").equals("yes")){
                        //opens the gamechoose activity
                        Intent i=new Intent(activity, choosing_theGame.class);
                        startActivity(i);
                    }

                    else{Toast.makeText(getApplicationContext(),
                            response.getString("dataIsRight"),Toast.LENGTH_LONG).show();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "Error login",Toast.LENGTH_LONG).show();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }

}
