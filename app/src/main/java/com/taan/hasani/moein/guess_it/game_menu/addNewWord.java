package com.taan.hasani.moein.guess_it.game_menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.taan.hasani.moein.guess_it.helpingclasses.Functions_;
import com.taan.hasani.moein.guess_it.helpingclasses.Player;
import com.taan.hasani.moein.volley.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class addNewWord extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Player player;
    private String category;
    JSONObject word_obj;
    Spinner spinner;
    String complete_word, incomplete_word;
    ArrayList<Integer> indexlist_of_questionmarks = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_word);

        player = new Player(this);

        if (!player.getrole().equals("admin")) {

            finish();
            Toast.makeText(getApplicationContext(), "متاسفانه اجازه دسترسی به این قسمت را ندارید...",
                    Toast.LENGTH_LONG).show();
        }

        spinner = (Spinner) findViewById(R.id.spinner);

        spinner_menu();

        final Functions_ functions_ = new Functions_(this);
        Button send = (Button) findViewById(R.id.send);
        final EditText completeWord_view = (EditText) findViewById(R.id.completeword);
        final EditText incompleteWord_view = (EditText) findViewById(R.id.incompleteword);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (completeWord_view.getText().toString().length()
                        == incompleteWord_view.getText().toString().length()) {
                    word_obj = new JSONObject();
                    try {
                        word_obj.put("category", category);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    complete_word = completeWord_view.getText().toString();
                    incomplete_word = incompleteWord_view.getText().toString();

                    //montabegh kardane alamate soal ba zaban******
                    ////////////////////////////////////////////////////
                    if (!complete_word.matches("[A-Za-z ]+?")) {

                        for (int i = 0; i < incomplete_word.length(); i++) {
                            if (incomplete_word.charAt(i) == '?')
                                indexlist_of_questionmarks.add(i);
                        }
                        ////////////////////////////////////////////////////

                        StringBuilder stringBuilder = new StringBuilder(incomplete_word);

                        for (int i = 0; i < indexlist_of_questionmarks.size(); i++) {

                            //  Toast.makeText(getApplicationContext(),
                            //        String.valueOf(indexlist_of_questionmarks.get(i)),
                            // Toast.LENGTH_LONG).show();
                            stringBuilder.setCharAt(indexlist_of_questionmarks.get(i), '؟');

                        }

                        incomplete_word = stringBuilder.toString();

                        ////////////////////////////////////////////////////
                        //  Toast.makeText(getApplicationContext(),
                        //         "not English", Toast.LENGTH_SHORT).show();


                    }

                    functions_.addWordtoDB(complete_word,
                            incomplete_word, word_obj);

                    completeWord_view.setText("");
                    incompleteWord_view.setText("");

                } else {
                    Toast.makeText(getApplicationContext(),
                            "به نظر در وارد کردن کلمات اشتباهی کرده اید", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void spinner_menu() {

// Spinner element
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("انگلیسی");
        categories.add("ورزشی");
        categories.add("بازیگر ایرانی");
        categories.add("فیلم و سریال ایرانی");
        categories.add("فیلم و سریال خارجی");
        categories.add("موسیقی ایرانی");
        categories.add("موسیقی خارجی");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        category = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //  Toast.makeText(parent.getContext(), "Selected: " + category, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }
}
