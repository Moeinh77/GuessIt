package com.taan.hasani.moein.guess_it.gameHistory;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.taan.hasani.moein.volley.R;

import java.util.ArrayList;

/**
 * Created by Moein on 9/27/2017.
 */

public class listViewAdapter_gamesInfo extends ArrayAdapter<gameHistory_object> {

    private int Layoutresource;//adress e row e nemoone
    private Activity activity;//activity ee ke list dar an ast
    private ArrayList<gameHistory_object> score_list = new ArrayList<>();

    public listViewAdapter_gamesInfo(Activity act, int resource, ArrayList<gameHistory_object> data) {
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
    public gameHistory_object getItem(int position) {
        return score_list.get(position);
    }

    @Override
    public int getPosition(gameHistory_object item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        Viewholder holder;

        if (row == null || (row.getTag() == null)) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            row = inflater.inflate(Layoutresource, null);//inflate kardane row e nemoone

            holder = new Viewholder();

            //intialize kardane view hay mojood dar view nemoone
            holder.rivalUsername = (TextView) row.findViewById(R.id.rival_username);
            holder.rivalScore = (TextView) row.findViewById(R.id.rivalScore);
            holder.playerScore = (TextView) row.findViewById(R.id.playerScore);

            ///////////////////////////////////////////////////

            row.setTag(holder);

        } else {
            holder = (Viewholder) row.getTag();
        }

        //gereftan etelaat az object ke dadim va set kardan an dar view nemoone ee ke sakhtim
        holder.gameHistoryObject = getItem(position);

        holder.playerScore.setText(holder.gameHistoryObject.getPlayerScore());
        holder.rivalScore.setText(holder.gameHistoryObject.getRivalScore());
        holder.rivalUsername.setText(holder.gameHistoryObject.getRivalUsername());
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

        gameHistory_object gameHistoryObject;
        TextView playerScore;
        TextView rivalUsername;
        TextView rivalScore;
    }
}
