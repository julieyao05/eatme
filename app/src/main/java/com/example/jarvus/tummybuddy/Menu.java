package com.example.jarvus.tummybuddy;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Minh on 11/26/15.
 */
public class Menu {
    protected static void addToTracker(Item it, Context context) {
        //Implement (use it.getName() to get the name of the item to do a query on)
        Toast.makeText(context, it.getName() + " Added To Tracker", Toast.LENGTH_SHORT).show();
    }

    protected static void addToWishlist(Item it, Context context) {
        //Implement (use it.getName() to get the name of the item to do a query on)
        Toast.makeText(context, it.getName() + " Added To WishList", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(context, "No Nutrition Facts Found", Toast.LENGTH_SHORT).show();
        }

    }
}
