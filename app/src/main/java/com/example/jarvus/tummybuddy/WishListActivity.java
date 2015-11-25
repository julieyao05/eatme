package com.example.jarvus.tummybuddy;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 2015-11-24.
 */
public class WishListActivity extends Activity {

    private ArrayList<String> wishListArray;
    private ArrayList<String> todaysFoodArray;
    private ArrayList<String> availableArray;
    private ArrayList<String> unAvailableArray;
    private ListView aListView;
    private ListView unaListView;


    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishist_activity);

        // Initializing arrays
        wishListArray = new ArrayList<String>();
        todaysFoodArray = new ArrayList<String>();
        availableArray = new ArrayList<String>();
        unAvailableArray = new ArrayList<String>();

        aListView = (ListView)findViewById(R.id.available_list);
        unaListView = (ListView)findViewById(R.id.unAvailable_list);

        // Retrieving data from "wishList"
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("WishList");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    //Toast.makeText(ParseListActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                }
                for (ParseObject obj : list) {
                    String wishList = obj.getString("List");
                    wishListArray.add(wishList);
                }

                // Retrieving data from "DiningHall"
                ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("DiningHall");
                query2.setLimit(1000);
                query2.findInBackground(new FindCallback<ParseObject>() {

                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        if (e != null) {
                            //Toast.makeText(ParseListActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                        }
                        for (ParseObject obj : list) {
                            String todaysFood = obj.getString("menu");
                            todaysFoodArray.add(todaysFood);
                        }

                        //Checks if any food in wishlist is currently available
                        for (int i = 0; i < wishListArray.size(); i++) {
                            if (todaysFoodArray.indexOf(wishListArray.get(i)) != -1) {
                                availableArray.add(wishListArray.get(i));
                            } else {
                                unAvailableArray.add(wishListArray.get(i));
                            }
                        }
                        //updating listviews
                        aListView.setAdapter(new ArrayAdapter<String>(WishListActivity.this, android.R.layout.simple_list_item_1, availableArray));
                        unaListView.setAdapter(new ArrayAdapter<String>(WishListActivity.this, android.R.layout.simple_list_item_1, unAvailableArray));
                    }
                });
            }
        });
    }
}
