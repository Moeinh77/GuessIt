package com.taan.hasani.moein.guess_it.Service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.taan.hasani.moein.guess_it.helpingclasses.Player;

public class getInfo extends IntentService {


    public getInfo() {
        super("playerInfo Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Player player = new Player();
        //   player.getUserInfo();

        _Toast("Running");


    }

    private void _Toast(final String message) {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                _Toast(message);
            }
        }, 3000);

    }

}
