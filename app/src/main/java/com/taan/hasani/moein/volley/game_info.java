package com.taan.hasani.moein.volley;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Random;

public class game_info extends AppCompatActivity {

    private Button get_info_bt;
    HashMap<String, String> info = new HashMap<String, String>();
    String url = "http://online6732.tk/guessIt.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);

        get_info_bt=(Button)findViewById(R.id.getinfo);

        get_info_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.put("action","newGame");
                info.put("category","ورزشی");
                info.put("player","1");
                info.put("mode","singlePlayer");

                JSONObject jsonObject=new JSONObject(info);
                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,
                        url, jsonObject,new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray=new JSONArray(response.getString("words"));
                            //  jsonArray.getJSONObject(0).getString("incompleteWord");

                            Random rand = new Random();

                            int  i = rand.nextInt(2);

                            String name_fa = new String( jsonArray.getJSONObject(i).getString("incompleteWord")
                                    .getBytes("ISO-8859-1"), "UTF-8");
                            Toast.makeText(getApplicationContext(),
                                    name_fa,Toast.LENGTH_LONG).show();
                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(),
                                    "Json Execption",Toast.LENGTH_LONG).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),
                                error.toString(),Toast.LENGTH_LONG).show();
                    }
                });

                AppController.getInstance().addToRequestQueue(jsonObjectRequest);
            }
        });

    }
}
