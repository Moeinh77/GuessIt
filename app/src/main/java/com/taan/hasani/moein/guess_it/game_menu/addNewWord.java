package com.taan.hasani.moein.guess_it.game_menu;

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
import com.taan.hasani.moein.volley.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class addNewWord extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String category;
    JSONObject word_;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_word);
        spinner = (Spinner) findViewById(R.id.spinner);

        spinner_menu();

        final Functions_ functions_ = new Functions_(this);
        Button send = (Button) findViewById(R.id.send);
        final EditText completeWord = (EditText) findViewById(R.id.completeword);
        final EditText incompleteWord = (EditText) findViewById(R.id.incompleteword);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (completeWord.getText().toString().length()
                        == incompleteWord.getText().toString().length()) {
                    word_ = new JSONObject();
                    try {
                        word_.put("category", category);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    functions_.addWordtoDB(completeWord.getText().toString(),
                            incompleteWord.getText().toString(), word_);

                    completeWord.setText("");
                    incompleteWord.setText("");

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
        categories.add("فیلم ایرانی");
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
