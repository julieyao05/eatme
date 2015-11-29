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
    private ArrayList<String> diningHallArray;
    private ArrayList<String> availableArray;
    private ArrayList<String> unAvailableArray;
    private ListView aListView;
    private ListView unaListView;


    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlist_activity);
        final Context context = this;
        // Initializing arrays
        wishListArray = new ArrayList<String>();
        todaysFoodArray = new ArrayList<String>();
        diningHallArray = new ArrayList<String>();
        availableArray = new ArrayList<String>();
        unAvailableArray = new ArrayList<String>();

        aListView = (ListView)findViewById(R.id.available_list);
        unaListView = (ListView)findViewById(R.id.unAvailable_list);

        // Showing "Remove" "nutrition value" when clicked
        AdapterView.OnItemClickListener av = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
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
                                Item name = new Item(it.split("\\|")[0].trim());
                                MenuClick.viewNutrition(name, context);
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


        aListView.setOnItemClickListener(av);
        unaListView.setOnItemClickListener(av);
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
                            String whichDiningHall = obj.getString("diningHall");
                            diningHallArray.add(whichDiningHall);
                        }

                        //Checks if any food in wishlist is currently available
                        for (int i = 0; i < wishListArray.size(); i++) {
                            int x;
                            if ((x = todaysFoodArray.indexOf(wishListArray.get(i))) != -1) {
                                availableArray.add(wishListArray.get(i) + " | "+ diningHallArray.get(x));
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

    private boolean removeFromWishlist(AdapterView<?> adv, String item) {
        try {
            ArrayAdapter<String> adapter = (ArrayAdapter) adv.getAdapter();
            adapter.remove(item);
            adapter.notifyDataSetChanged();

            // excluding the name of dininghall
            int index = item.indexOf("|");
            item = item.substring(0, index-1);

            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("WishList").whereMatches("List", item);
            List<ParseObject> objects = query.find();

            for (ParseObject entry : objects)
                entry.deleteInBackground();

            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
