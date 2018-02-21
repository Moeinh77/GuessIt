package com.taan.hasani.moein.guess_it.profile_activity;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.taan.hasani.moein.guess_it.game_menu.addNewWord;
import com.taan.hasani.moein.guess_it.helpingclasses.Player;
import com.taan.hasani.moein.volley.R;


public class profile extends Fragment {

    String profilePicture;
    EditText username_edittext, name_edittext;//bara inke final nashe
    Button logout, Edit_username_bt;
    private EditText old_password, new_password, repeat_password;
    private ImageView profile_pic, AddNewWord;
    private Player player;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View account_info_ = inflater.inflate(R.layout.fragment_profile, container, false);

        player = new Player(getActivity());//intialize e class Player

        player.getUserInfo();//hame etelaat ra update konad

        AddNewWord = account_info_.findViewById(R.id.addWord);
        name_edittext = (EditText) account_info_.findViewById(R.id.FirstName);
        username_edittext = (EditText) account_info_.findViewById(R.id.username);
        logout = (Button) account_info_.findViewById(R.id.logout_bt);
        old_password = (EditText) account_info_.findViewById(R.id.old_password);
        new_password = (EditText) account_info_.findViewById(R.id.new_password);
        repeat_password = (EditText) account_info_.findViewById(R.id.repeat_password);
        Button save_bt = (Button) account_info_.findViewById(R.id.save_bt);
        profile_pic = (ImageView) account_info_.findViewById(R.id.profile_pic);
        Edit_username_bt = (Button) account_info_.findViewById(R.id.edit_bt);
        Button Edit_name_bt = (Button) account_info_.findViewById(R.id.editname_bt);

        name_edittext.setText(player.getName());
        username_edittext.setText(player.getUsername());

        AddNewWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), addNewWord.class);
                startActivity(i);
            }
        });

        Edit_name_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.changeName(name_edittext.getText().toString());
            }
        });

        Edit_username_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.changeUsername(username_edittext.getText().toString());
            }
        });

        save_bt.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           if (!old_password.getText().toString().equals("") && !new_password.getText().toString().equals("") && !repeat_password.getText().toString().equals("")) {

                                               player.change_password_func(
                                                       old_password.getText().toString(),
                                                       new_password.getText().toString(),
                                                       repeat_password.getText().toString());


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


                                player.logout_of_server();

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

    ////////////////////////////////////////////////////
    private Bitmap getRoundedBitmap(Bitmap bitmap) {
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
