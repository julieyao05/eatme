package com.example.jarvus.tummybuddy;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
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
    private ListView priceList;
    final Context context = this;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter_activity);
        final Context context = this;

        // call price_counter() and calories_counter()
        price_counter();
        calrories_counter();
    }

    void price_counter(){

        //Initializing priceArray and carlriesList
        priceArray = new ArrayList<String>();
        priceList = (ListView)findViewById(R.id.price_counter);


        TextView tmp = (TextView)findViewById(R.id.price_text);
        tmp.append("First = ");
        // Showing "Remove" "nutrition value" when clicked
        AdapterView.OnItemClickListener av = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                TextView tmp = (TextView)findViewById(R.id.price_text);
                tmp.append("Second = ");
                PopupMenu popup = new PopupMenu(context, arg1);

                final String it = (String) arg0.getItemAtPosition(position);
                final AdapterView adv = arg0;
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.remove:
                                return removeFromWishlist(adv, it);
                            case R.id.viewNutrWish:
                                MenuClick.viewNutrition(new Item(it), context);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.inflate(R.menu.remove_menu);
                popup.show();
            }
        };

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
                    priceArray.add("$" + tmpPrice);
                    //priceArray.add(todaysFood);
                }
                tmpPrice = "Total : $" + Double.toString(totalPrice);
                priceArray.add(tmpPrice);

                priceList.setAdapter(new ArrayAdapter<String>(CounterActivity.this, android.R.layout.simple_list_item_1, priceArray));

            }
        });
    }

    void calrories_counter(){
    }

    private boolean removeFromWishlist(AdapterView<?> adv, String item) {
        try {
            ArrayAdapter<String> adapter = (ArrayAdapter) adv.getAdapter();
            adapter.remove(item);
            adapter.notifyDataSetChanged();

            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Counter").whereMatches("Price", item);
            List<ParseObject> objects = query.find();

            for (ParseObject entry : objects)
                entry.deleteInBackground();

            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
