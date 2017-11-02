package com.taan.hasani.moein.guess_it.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.taan.hasani.moein.guess_it.appcontroller.AppController;
import com.taan.hasani.moein.guess_it.game_menu.Entrance_signup_login;
import com.taan.hasani.moein.volley.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;


public class account_settings_and_info extends Fragment {

    String url = "http://online6732.tk/guessIt.php";
    String name, username, profilePicture;
    TextView name_textview;
    EditText username_edittext;
    Button logout, Edit_username_bt;
    private EditText old_password, new_password, repeat_password;
    private final String MY_PREFS_NAME = "username and password";
    private String newPassword_string;
    private ImageView profile_pic;
    //oldPassword_string;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View account_info_ = inflater.inflate(R.layout.fragment_account_settings_info, container, false);

        name_textview = (TextView) account_info_.findViewById(R.id.FirstName);
        username_edittext = (EditText) account_info_.findViewById(R.id.username);
        logout = (Button) account_info_.findViewById(R.id.logout_bt);
        old_password = (EditText) account_info_.findViewById(R.id.old_password);
        new_password = (EditText) account_info_.findViewById(R.id.new_password);
        repeat_password = (EditText) account_info_.findViewById(R.id.repeat_password);
        Button save_bt = (Button) account_info_.findViewById(R.id.save_bt);
        name_textview.setVisibility(View.INVISIBLE);
        profile_pic = (ImageView) account_info_.findViewById(R.id.profile_pic);
        Edit_username_bt = (Button) account_info_.findViewById(R.id.edit_bt);

        getUserInfo();

        Edit_username_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUsername();
            }
        });

        save_bt.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           if (!old_password.getText().toString().equals("") && !new_password.getText().toString().equals("") && !repeat_password.getText().toString().equals("")) {
                                               change_password_func();

                                           } else {

                                               Toast.makeText(getActivity(), "Please fill all the fields", Toast.LENGTH_LONG).show();
                                           }
                                       }
                                   }
        );

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.myDialog));
                alertDialogBuilder.setTitle("Logout");
                alertDialogBuilder
                        .setMessage("آیا میخواهید از اکانت خود خارج شوید ؟")
                        .setCancelable(false)
                        .setPositiveButton("بلی", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                logout_of_server();


                            }
                        })
                        .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });

                alertDialogBuilder.setIcon(R.drawable.logout);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });


        return account_info_;
    }


    public void change_password_func() {

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final String user_id = prefs.getString("userID", null);
        String oldpassowrd_from_sharedprefs = prefs.getString("password", null);
        newPassword_string = new_password.getText().toString();
//        oldPassword_string = old_password.getText().toString();

        HashMap<String, String> info = new HashMap<>();

        if (!old_password.getText().toString().equals(oldpassowrd_from_sharedprefs)) {
            Toast.makeText(getActivity(),
                    "Wrong old passwprd", Toast.LENGTH_LONG).show();
        } else if (newPassword_string.equals(repeat_password.getText().toString())) {

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


                            SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("password", newPassword_string);
                            editor.apply();

                            Toast.makeText(getActivity(), "Password changed", Toast.LENGTH_LONG).show();

                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(),
                                    response.getString("dataIsRight"), Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG);

                }
            });

            AppController.getInstance().addToRequestQueue(jsonObjectRequest);

        } else {
            Toast.makeText(getActivity(), "Entered passwords are not the same ", Toast.LENGTH_LONG).show();

        }

    }

    private void logout_of_server() {

        final HashMap<String, String> info = new HashMap<>();
        String url = "http://online6732.tk/guessIt.php";

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String id = prefs.getString("userID", null);


        info.put("action", "logout");
        info.put("userID", id);

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    if (response.getString("dataIsRight").equals("yes")) {

                        SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("username", null);
                        editor.putString("password", null);
                        editor.putString("userID", null);
                        editor.apply();

                        Intent intent = new Intent(getActivity(), Entrance_signup_login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(),
                                "حطایی در خروج شما رخ داد دوباره تلاش کنید...", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        "خطایی در خروج شما رخ داد دوباره تلاش کنید...", Toast.LENGTH_SHORT).show();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);


    }

    private void getUserInfo() {
        //getting id for the player
        final String MY_PREFS_NAME = "username and password";
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String id = prefs.getString("userID", null);
        //////////////////////////////////////////
        HashMap<String, String> info = new HashMap<>();


        info.put("action", "sendUserInformation");
        info.put("userID", id);

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    name = new String(response.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                    username = new String(response.getString("username").getBytes("ISO-8859-1"), "UTF-8");
//                    games = new String(response.getString("games").getBytes("ISO-8859-1"), "UTF-8");
                    //profilePicture = new String(response.getString("profilePicture").getBytes("ISO-8859-1"), "UTF-8");
                    name_textview.setText(name);
                    name_textview.setVisibility(View.VISIBLE);

                    username_edittext.setText(username);


                } catch (JSONException e) {
                    Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException e) {
                    Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }


    private void changeUsername() {


        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final String user_id = prefs.getString("userID", null);
        HashMap<String, String> info = new HashMap<>();

        final String username_ = username_edittext.getText().toString();

        info.put("action", "changeUsername");
        info.put("username", username_);
        info.put("userID", user_id);

        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {


                    if (response.getString("dataIsRight").equals("yes")) {


                        Toast.makeText(getActivity(),
                                "Username changed", Toast.LENGTH_LONG).show();

                        SharedPreferences.Editor editor = getActivity().
                                getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("username", username_);
                        editor.apply();


                    } else {
                        Toast.makeText(getActivity(),
                                "username didnt change", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG);

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    ////////////////////////////////////////////////////
    public Bitmap getRoundedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
////////////////////////////////////////////////////
}
