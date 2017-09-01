package com.taan.hasani.moein.volley;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    String url = "http://online6732.tk/guessIt.php";
    private String password;
    private String username;
    private Button login;
    private EditText username_editext,password_editext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login=(Button)findViewById(R.id.login);
        username_editext=(EditText)findViewById(R.id.username);
        password_editext=(EditText)findViewById(R.id.password);


        HashMap<String, String> info = new HashMap<String, String>();

        username="Moein";
        password="&&password__";
        info.put("action","login");
        info.put("password",password);
        info.put("username",username);

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
