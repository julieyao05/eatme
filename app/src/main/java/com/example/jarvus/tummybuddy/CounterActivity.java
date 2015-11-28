package com.example.jarvus.tummybuddy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 2015-11-23.
 */
public class CounterActivity extends Activity{

    private ArrayList<String> priceArray;
    private ListView caloriesList;
    private TextView conterText;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter_activity);

        // Price counter
        final Button button = (Button) findViewById(R.id.price_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                calories_counter();
            }
        });

    }

    void calories_counter(){

        //Initializing priceArray and carlriesList
        priceArray = new ArrayList<String>();
        caloriesList = (ListView)findViewById(R.id.price_counter);

        // connecting to database
        ParseQuery<ParseObject> priceQuery = new ParseQuery<ParseObject>("Counter");
        priceQuery.setLimit(1000);
        priceQuery.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    //Toast.makeText(ParseListActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                }
                double totalPrice = 0.0;
                String tmpPrice;
                for (ParseObject obj : list) {
                    String priceString = obj.getString("Price");
                    priceString = priceString.replaceAll("[$()]", "");
                    double priceDouble = Double.parseDouble(priceString);
                    totalPrice += priceDouble;
                    tmpPrice = Double.toString(priceDouble);
                    priceArray.add("$"+tmpPrice);
                    //priceArray.add(todaysFood);
                }
                tmpPrice = "Total : $"+Double.toString(totalPrice);
                priceArray.add(tmpPrice);

                caloriesList.setAdapter(new ArrayAdapter<String>(CounterActivity.this, android.R.layout.simple_list_item_1, priceArray));

            }
        });
    }

    void price_counter(){
    }

}
