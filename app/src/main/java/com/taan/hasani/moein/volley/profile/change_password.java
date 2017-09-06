package com.taan.hasani.moein.volley.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.taan.hasani.moein.volley.R;
import com.taan.hasani.moein.volley.appcontroller.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class change_password extends AppCompatActivity {

    private EditText old_password, new_password, repeat_password;
    private Button save_bt;
    private final String MY_PREFS_NAME = "username and password";
    final HashMap<String, String> info = new HashMap<>();
    String url = "http://online6732.tk/guessIt.php";
    private String newPassword_string, oldPassword_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        old_password = (EditText) findViewById(R.id.old_password);
        new_password = (EditText) findViewById(R.id.new_password);
        repeat_password = (EditText) findViewById(R.id.repeat_password);
        save_bt = (Button) findViewById(R.id.save_bt);

        save_bt.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           if (old_password.getText() != null || new_password.getText() != null || repeat_password.getText() != null) {


                                           } else {

                                               //   Snackbar.make(R.layout.activity_change_password,"Please fill all the fields",Snackbar.LENGTH_INDEFINITE).show();
                                           }
                                       }
                                   }
        );
    }

    public void change_password_func() {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String user_id = prefs.getString("userID", null);

        info.put("action", "changePassword");
        info.put("newPassword", newPassword_string);
        info.put("oldPassword", oldPassword_string);
        info.put("userID", user_id);

        if (newPassword_string.equals(oldPassword_string)) {
            JSONObject jsonObject = new JSONObject(info);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        // Toast.makeText(getApplicationContext(),
                        //         response.getString("dataIsRight"),Toast.LENGTH_LONG).show();
                        if (response.getString("dataIsRight").equals("yes")) {

                            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("password", newPassword_string);
                            editor.apply();


                        } else {
                            Toast.makeText(getApplicationContext(),
                                    response.getString("dataIsRight"), Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
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

        }else if(){

        }
        else{
            Toast.makeText(getApplicationContext(),"Passwords are not the same",Toast.LENGTH_LONG).show();

        }
    }

}

