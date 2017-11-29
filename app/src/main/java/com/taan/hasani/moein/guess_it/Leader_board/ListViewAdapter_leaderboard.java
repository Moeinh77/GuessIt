package com.taan.hasani.moein.guess_it.Leader_board;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.taan.hasani.moein.volley.R;

import java.util.ArrayList;

/**
 * Created by Moein on 11/29/2017.
 */

public class ListViewAdapter_leaderboard extends ArrayAdapter<leaderBoard_object> {

    private int Layoutresource;//adress e row e nemoone
    private Activity activity;//activity ee ke list dar an ast
    private ArrayList<leaderBoard_object> score_list = new ArrayList<>();

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
            holder.playerPlace = (TextView) row.findViewById(R.id.player_place);
            holder.playerUsername = (TextView) row.findViewById(R.id.player_username);
            holder.playerScore = (TextView) row.findViewById(R.id.player_score);

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

        // final Viewholder finalHolder = holder;
//        row.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent i=new Intent(activity,Show_note.class);
//
//                Bundle mBundle=new Bundle();
//                mBundle.putSerializable("My object", finalHolder.note);
//                i.putExtras(mBundle);
//                activity.startActivity(i);
//
//            }
//        });
        return row;
    }


    public class Viewholder {

        leaderBoard_object leaderBoardObject;
        TextView playerScore;
        TextView playerUsername;
        TextView playerPlace;
    }
}
