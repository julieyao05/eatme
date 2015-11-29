package com.example.jarvus.tummybuddy;

import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by Minh on 11/26/15.
 */
public class MenuClick {
    protected static void addToTracker(Item it, Context context) {
        //Implement (use it.getName() to get the name of the item to do a query on)
        Toast.makeText(context, it.getName() + " Added To Tracker", Toast.LENGTH_SHORT).show();
    }

    protected static void addToWishlist(Item it, Context context) {
        //Implement (use it.getName() to get the name of the item to do a query on)

        final String clickedItem = it.getName();
        final String clickedDiningHall = it.getDiningHall();
        final Context wishListContext = context;

        ParseQuery<ParseObject> wishListQuery = new ParseQuery<ParseObject>("WishList");
        wishListQuery.setLimit(1000);
        wishListQuery.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> list, ParseException e) {
                boolean isDuplicate = false;
                if (e != null) {
                    //Toast.makeText(ParseListActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                }
                for (ParseObject obj : list) {
                    String wishList = obj.getString("List");
                    if(wishList.equals(clickedItem)){
                        isDuplicate = true;
                    }
                }
                if(isDuplicate == true){
                    Toast.makeText(wishListContext, clickedItem + " is already in your WishList! ", Toast.LENGTH_SHORT).show();
                }else {
                    // connecting to database
                    ParseObject wishListObj = new ParseObject("WishList");

                    wishListObj.put("List", clickedItem);
                    wishListObj.put("DiningHall", clickedDiningHall);

                    wishListObj.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                        }
                    });
                    Toast.makeText(wishListContext, clickedItem + " Added To WishList, ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected static void viewNutrition(Item it, Context context) {
        Toast.makeText(context, "Loading " + it.getName() + " Nutrition Facts", Toast.LENGTH_SHORT).show();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("DiningHall");
        query.whereEqualTo("menu", it.getName());

        try {
            List<ParseObject> objects = query.find();
            if (objects != null && objects.size() > 0) {
                for (ParseObject dealsObject : objects) {
                    String uid = (String) dealsObject.get("distinct_id");
                    if (uid.matches("[-+]?\\d*\\.?\\d+")) {
                        it.setID(uid);
                    }
                }
            }
        } catch (ParseException e) {
            Log.d("distinct_id", "Error: " + e.getMessage());
        }

        if (it.hasID()) {
            try {
                String nutri_link = "http://hdh.ucsd.edu/DiningMenus/nutritionfacts.aspx?i=" + it.getID();
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(nutri_link));
                context.startActivity(myIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "No application can handle this request."
                        + " Please install a web browser", Toast.LENGTH_SHORT).show();
                //e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "Nutrition Facts Unavailable", Toast.LENGTH_SHORT).show();
        }

    }

    public static class ParseApplication extends Application {

        @Override
        public void onCreate() {
            super.onCreate();

            Parse.enableLocalDatastore(this);
            Parse.initialize(this, "2s0GrgbG5sY5OMVcemUzWe4TYLz86tLfRMp8ISTa", "wEjMG8yFL5GjR01PIzSLIpMlDJH5ghHXRPeoTmXm");


/*
            ParseObject diningObj = new ParseObject("DiningHall");
            ParseQuery<ParseObject> query = ParseQuery.getQuery("DiningHall");
            try{
                List<ParseObject> objects = query.find();
                if(objects.size() != 0) {
                    for (ParseObject entry : objects) {
                        entry.deleteInBackground();
                    }
                }
                DataBase db = new DataBase();
                db.collectData(diningObj);
            } catch(com.parse.ParseException e) {
                e.printStackTrace();
            }*/
        } // end of onCreate()
    } // end of ParseApplication
}
