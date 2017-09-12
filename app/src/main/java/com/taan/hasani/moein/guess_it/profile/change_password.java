package com.taan.hasani.moein.guess_it.profile;

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
import com.taan.hasani.moein.guess_it.appcontroller.AppController;

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
                                           if (!old_password.getText().toString().equals("") && !new_password.getText().toString().equals("") && !repeat_password.getText().toString().equals("")) {
                                               change_password_func();

                                           } else {

                                               Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_LONG).show();
                                           }
                                       }
                                   }
        );
    }

    public void change_password_func() {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final String user_id = prefs.getString("userID", null);
        String oldpassowrd_from_sharedprefs = prefs.getString("password", null);
        newPassword_string = new_password.getText().toString();
        oldPassword_string = old_password.getText().toString();


        if (!old_password.getText().toString().equals(oldpassowrd_from_sharedprefs)){
            Toast.makeText(getApplicationContext(),
                    "Wrong old passwprd",Toast.LENGTH_LONG).show();
        }else
        if (newPassword_string.equals(repeat_password.getText().toString())) {

            info.put("action", "changePassword");
            info.put("newPassword", newPassword_string);
            info.put("oldPassword", oldpassowrd_from_sharedprefs);
            info.put("userID", user_id);

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

                            Toast.makeText(getApplicationContext(), "Password changed", Toast.LENGTH_LONG).show();

                            finish();
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
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG);

                }
            });

            AppController.getInstance().addToRequestQueue(jsonObjectRequest);

        } else {
            Toast.makeText(getApplicationContext(), "Entered passwords are not the same ", Toast.LENGTH_LONG).show();

        }

    }

}

