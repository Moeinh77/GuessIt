package com.taan.hasani.moein.guess_it.game_menu;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.taan.hasani.moein.guess_it.Leader_board.leader_board_fragment;
import com.taan.hasani.moein.guess_it.appcontroller.AppController;
import com.taan.hasani.moein.guess_it.game.choosing_games_fragment;
import com.taan.hasani.moein.guess_it.profile.account_games_info;
import com.taan.hasani.moein.guess_it.profile.account_settings_and_info;
import com.taan.hasani.moein.guess_it.profile.change_password;
import com.taan.hasani.moein.volley.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main_menu extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewpager;

    private final String MY_PREFS_NAME = "username and password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ////////////////////////

        viewpager = (ViewPager) findViewById(R.id.viewpager_mainmenu);
        setupViewPager(viewpager);

        tabLayout = (TabLayout) findViewById(R.id.tabs_mainmenu);

        tabLayout.setupWithViewPager(viewpager);

        tabLayout.getTabAt(0).setIcon(R.drawable.profie);
        tabLayout.getTabAt(1).setIcon(R.drawable.history);
        tabLayout.getTabAt(2).setIcon(R.drawable.conroller);
        tabLayout.getTabAt(3).setIcon(R.drawable.leaderboard);

        viewpager.setCurrentItem(2);//baraye ijad e tab e default
    }

    ////////////////////////
    //menu
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
                alertDialogBuilder.setTitle("Logout");
                //alertDialogBuilder.setIcon(R.drawable.);
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

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;

            case R.id.change_password:

                Intent intent = new Intent(this, change_password.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        //respond to menu item selection

    }
    //////////////////////////////////////////////////


    public void alert_dialog_function() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.exit_dialog);
        dialog.setCancelable(true);

        dialog.setTitle("Exit");


        Button yes = (Button) dialog.findViewById(R.id.yes);
        Button no = (Button) dialog.findViewById(R.id.no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


        dialog.show();

    }

    private void logout_of_server() {

        final HashMap<String, String> info = new HashMap<>();
        String url = "http://online6732.tk/guessIt.php";

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
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

                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("username", null);
                        editor.putString("password", null);
                        editor.putString("userID", null);
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), Entrance_signup_login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "حطایی در خروج شما رخ داد دوباره تلاش کنید...", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "حطایی در خروج شما رخ داد دوباره تلاش کنید...", Toast.LENGTH_SHORT).show();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);


    }


    //setting tabs
    private void setupViewPager(ViewPager viewPager) {
        Main_menu.ViewPagerAdapter adapter = new
                Main_menu.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new account_settings_and_info(), "");
        adapter.addFragment(new account_games_info(), "");
        adapter.addFragment(new choosing_games_fragment(), "");
        adapter.addFragment(new leader_board_fragment(), "");


        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(android.support.v4.app.FragmentManager manager) {
            super(manager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(android.support.v4.app.Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    ///////////////////////////////////////////////////////////

    @Override
    public void onBackPressed() {
        alert_dialog_function();
    }

    @Override
    protected void onDestroy() {

        SharedPreferences prefs = getSharedPreferences("username and password", MODE_PRIVATE);
        String id = prefs.getString("userID", null);

        final HashMap<String, String> info = new HashMap<>();
        final String url = "http://online6732.tk/guessIt.php";

        info.put("action", "logout");
        info.put("userID", id);
        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("dataIsRight").equals("yes")) {

                        Log.v("", response.getString("dataIsRight"));

                        Toast.makeText(getApplicationContext(),
                                "Logged out successfully ", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "There was error in logging out...", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "There was error in logging out...", Toast.LENGTH_LONG).show();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

        super.onDestroy();

    }
}
