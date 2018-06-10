package com.taan.hasani.moein.guess_it.Leader_board;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.taan.hasani.moein.guess_it.appcontroller.AppController;
import com.taan.hasani.moein.volley.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Moein on 11/29/2017.
 */

public class ListViewAdapter_leaderboard extends ArrayAdapter<leaderBoard_object> {

    private int Layoutresource;//adress e row e nemoone
    private Activity activity;//activity ee ke list dar an ast
    private ArrayList<leaderBoard_object> score_list = new ArrayList<>();
    private TextView username, name, totalscore;
    private Button rank;

    public ListViewAdapter_leaderboard(Activity act,
                                       int resource,
                                       ArrayList<leaderBoard_object> data) {
        super(act, resource, data);
        //sakhte constructor baraye estefade dar class marboot be list
        Layoutresource = resource;
        activity = act;
        score_list = data;
        notifyDataSetChanged();//taghir be ezay taghir dar etelaat list

        ///////////////////////////////////////////////////////////
    }

    @Override
    public int getCount() {
        return score_list.size();
    }

    @Override
    public leaderBoard_object getItem(int position) {
        return score_list.get(position);
    }

    @Override
    public int getPosition(leaderBoard_object item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ListViewAdapter_leaderboard.Viewholder holder;

        if (row == null || (row.getTag() == null)) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            row = inflater.inflate(Layoutresource, null);//inflate kardane row e nemoone

            holder = new ListViewAdapter_leaderboard.Viewholder();

            //intialize kardane view hay mojood dar view nemoone
            holder.playerPlace = row.findViewById(R.id.player_place);
            holder.playerUsername = row.findViewById(R.id.player_username);
            holder.playerScore = row.findViewById(R.id.player_score);

            ///////////////////////////////////////////////////

            row.setTag(holder);

        } else {
            holder = (ListViewAdapter_leaderboard.Viewholder) row.getTag();
        }

        //gereftan etelaat az object ke dadim va set kardan an dar view nemoone ee ke sakhtim
        holder.leaderBoardObject = getItem(position);

        holder.playerScore.setText(holder.leaderBoardObject.getPlayer_score());
        holder.playerUsername.setText(holder.leaderBoardObject.getPlayer_username());
        holder.playerPlace.setText(holder.leaderBoardObject.getPlayer_place());
        ///////////////////////////////////

        final Viewholder finalHolder = holder;

        //onClick
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(activity);

                //  Toast.makeText(activity,
                //                  "test", Toast.LENGTH_LONG).show();

                dialog.setContentView(R.layout.user_onclickdialog);
                dialog.setCancelable(true);

                Button close = dialog.findViewById(R.id.close_bt);

//                close.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.cancel();
//                    }
//                });

                sendUserInformationByUsername(
                        finalHolder.leaderBoardObject.getPlayer_username(), dialog);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                dialog.show();

            }
        });
        return row;
    }

    private void sendUserInformationByUsername(String username_, final Dialog dialog) {


        HashMap<String, String> info = new HashMap<>();

        info.put("action", "sendUserInformationByUsername");
        info.put("username", username_);


        JSONObject jsonObject = new JSONObject(info);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                AppController.url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getString("dataIsRight").equals("yes")) {

//                        Toast.makeText(activity,
//                                "Username changed", Toast.LENGTH_LONG).show();
                        username = dialog.findViewById(R.id.username_dilog);
                        name = dialog.findViewById(R.id.name_dialog);
                        totalscore = dialog.findViewById(R.id.totalscore_dialog);
                        rank = dialog.findViewById(R.id.rank_bt);


                        username.setText(response.getString("username"));
                        name.setText(response.getString("name"));
                        totalscore.setText(response.getString("totalScore"));
                        rank.setText(response.getString("position"));


                    } else {

                        Toast.makeText(activity,
                                response.getString("responseData"), Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, error.toString(), Toast.LENGTH_LONG);

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);


    }

    private class Viewholder {

        leaderBoard_object leaderBoardObject;
        TextView playerScore;
        TextView playerUsername;
        TextView playerPlace;
    }

}
