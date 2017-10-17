package com.taan.hasani.moein.guess_it.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.taan.hasani.moein.volley.R;

public class choosing_games_fragment extends Fragment {

    Button twoPlayer_bt, singlePlayer_bt;

    public choosing_games_fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View choosing_game = inflater.inflate(R.layout.fragment_choosing_games, container, false);

        twoPlayer_bt = (Button) choosing_game.findViewById(R.id.twoPlayer_bt);

        twoPlayer_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), categories_twoPlayer.class);
                startActivity(intent);

            }
        });

        singlePlayer_bt = (Button) choosing_game.findViewById(R.id.Single_Player);

        singlePlayer_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), categories_singlePlayer.class);
                startActivity(i);
            }
        });

        return choosing_game;
    }


}
