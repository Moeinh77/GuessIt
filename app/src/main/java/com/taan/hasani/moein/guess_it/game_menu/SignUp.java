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
import com.taan.hasani.moein.guess_it.appcontroller.AppController;
import com.taan.hasani.moein.volley.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {

    private final String MY_PREFS_NAME ="username and password" ;
    String url = "http://online6732.tk/guessIt.php";
    private String password, Lname, Fname, username, nickName;
    private Button signup;
    private EditText username_editext, Nname_edittext, password_editext, Fname_editext, Lname_edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signup=(Button)findViewById(R.id.signup);
        username_editext=(EditText)findViewById(R.id.username);
        password_editext=(EditText)findViewById(R.id.password);
        Fname_editext = (EditText) findViewById(R.id.FirstName);
        Lname_edittext = (EditText) findViewById(R.id.lastName);
        Nname_edittext = (EditText) findViewById(R.id.nickname);

        final HashMap<String, String> info = new HashMap<>();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username=username_editext.getText().toString();
                password=password_editext.getText().toString();
                Fname = Fname_editext.getText().toString();
                Lname = Lname_edittext.getText().toString();
                nickName = Nname_edittext.getText().toString();

                info.put("action","signup");
                info.put("password",password);
                info.put("username",username);
                info.put("firstName", Fname);
                info.put("lastName", Lname);
                info.put("name", nickName);

                JSONObject jsonObject=new JSONObject(info);
                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,
                        url, jsonObject,new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            if (response.getString("dataIsRight").toString().equals("yes")) {
                                save_user_and_pass(username, password);
                                Intent intent = new Intent(SignUp.this, Loading.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                    response.getString("responseData"),Toast.LENGTH_LONG).show();
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

    //highscore default ham injast
    public void save_user_and_pass(String username__,String password__){

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("username", username__);
        editor.putString("password", password__);
        editor.putInt("HighScore", 0);
        editor.apply();
//        Toast.makeText(getApplicationContext(),
//                "user :"+username__+"pass :"+password__,Toast.LENGTH_LONG).show();

    }
}
