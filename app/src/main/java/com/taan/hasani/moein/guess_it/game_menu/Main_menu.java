package com.taan.hasani.moein.guess_it.game_menu;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.taan.hasani.moein.guess_it.Leader_board.leader_board_fragment;
import com.taan.hasani.moein.guess_it.game_play.choosing_games_fragment;
import com.taan.hasani.moein.guess_it.helpingclasses.Player;
import com.taan.hasani.moein.guess_it.profile_activity.account_settings_and_info;
import com.taan.hasani.moein.guess_it.gameHistory.gameHistory_list;
import com.taan.hasani.moein.volley.R;

import java.util.ArrayList;
import java.util.List;

public class Main_menu extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewpager;
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        player = new Player(this);
        ////////////////////////

        viewpager = (ViewPager) findViewById(R.id.viewpager_mainmenu);
        setupViewPager(viewpager);

        tabLayout = (TabLayout) findViewById(R.id.tabs_mainmenu);

        tabLayout.setupWithViewPager(viewpager);

        tabLayout.getTabAt(0).setIcon(R.drawable.manuser);
        tabLayout.getTabAt(1).setIcon(R.drawable.history1);
        tabLayout.getTabAt(2).setIcon(R.drawable.game);
        tabLayout.getTabAt(3).setIcon(R.drawable.winnermedal);

        viewpager.setCurrentItem(2);//baraye ijad e tab e default
    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (player.getrole().equals("admin")) {

            switch (item.getItemId()) {
                case R.id.add:
                    startActivity(new Intent(this, addNewWord.class));
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        } else {

            Toast.makeText(getApplicationContext(), "متاسفانه اجازه دسترسی به این قسمت را ندارید...",
                    Toast.LENGTH_LONG).show();
        }
        return false;
    }

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


    //setting tabs
    private void setupViewPager(ViewPager viewPager) {
        Main_menu.ViewPagerAdapter adapter = new
                Main_menu.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new account_settings_and_info(), "");
        adapter.addFragment(new gameHistory_list(), "");
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

        player.onAppExit();
        super.onDestroy();

    }
}
