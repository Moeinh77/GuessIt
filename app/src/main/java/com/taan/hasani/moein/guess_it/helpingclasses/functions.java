package com.taan.hasani.moein.guess_it.helpingclasses;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;


public class functions {

    private Activity activity;

    functions(Activity activity) {

        this.activity = activity;

    }


//    public void newSinglePlayerGame() {
//
//        currentword_number = 0;//kalame hara az avl beshmarad
//        number_of_trueGuess = 0;
//        Total_gamescore = 0;
//        totalScore_view.setText("Total score : " + String.valueOf(0));
//
//        HashMap<String, String> info = new HashMap<>();
//
//        info.put("action", "newGame");
//        info.put("mode", "singlePlayer");
//        info.put("userID", id);
//        info.put("type", type);
//
//        JSONObject jsonObject = new JSONObject(info);
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
//                url, jsonObject, new Response.Listener<JSONObject>() {
//
//
//            @Override
//            public void onResponse(JSONObject response) {
//
//                try {
//
//                    //    Toast.makeText(getApplicationContext(),
//                    //          response.toString(), Toast.LENGTH_LONG).show();
//                    game_ID = response.getString("gameID");
//
//                    if (response.getString("dataIsRight").equals("yes")) {
//                        setGameSettings();
//                    } else {
//
//                        Toast.makeText(, " data is right=no ,sth went wrong..."
//                                , Toast.LENGTH_SHORT).show();
//                        newSinglePlayerGame();
//                    }
//
//
//                } catch (JSONException e) {
//                    Toast.makeText(getApplicationContext(),
//                            "newSinglePlayerGame " + e.toString(), Toast.LENGTH_LONG).show();
//                }
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(),
//                        "*newSinglePlayerGame**Volley  :" + error.toString(), Toast.LENGTH_LONG).show();
//            }
//        });
//
//        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
//    }


}
