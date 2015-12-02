package com.example.jarvus.tummybuddy;

import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by Minh on 11/26/15.
 */
public class MenuClick {

    public static ParseObject priceObj;
    public static ParseObject caloriesObj;
    public static Item trackerItem;
    public static Context globalContext;
    public static String globalCal;
    public static String globalId;

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
                    if (wishList.equals(clickedItem)) {
                        isDuplicate = true;
                    }
                }
                if (isDuplicate == true) {
                    Toast.makeText(wishListContext, clickedItem + " is already in your WishList!", Toast.LENGTH_SHORT).show();
                } else {
                    // connecting to database
                    ParseObject wishListObj = new ParseObject("WishList");

                    wishListObj.put("List", clickedItem);
                    wishListObj.put("DiningHall", clickedDiningHall);

                    wishListObj.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                        }
                    });
                    Toast.makeText(wishListContext, clickedItem + " Added To WishList.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected static void addToTracker(Item it, Context context) {

        globalContext = context;
        trackerItem = it;
        final Item finalIt = it;
        final Context finalCont = context;

        priceObj = new ParseObject("Counter");
        caloriesObj = new ParseObject("Calories");

        priceObj.put("Price", it.getPrice());
        priceObj.put("Item", it.getName());
        caloriesObj.put("Item", it.getName());
        parsingCalories(finalIt, finalCont);
        try {
            synchronized (priceObj){
                priceObj.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        priceObj.put("Id", globalId);
        caloriesObj.put("Id", globalId);
        caloriesObj.put("Calories", globalCal);
        priceObj.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e4) {
                caloriesObj.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e4) {}
                });
            }
        });
        Toast.makeText(context, it.getName() + " Added To Counter! ", Toast.LENGTH_SHORT).show();
    }

    protected static void parsingCalories(Item it, Context context){

        String caloriesData;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("DiningHall");
        query.whereEqualTo("menu", it.getName());

        // check if it contains calories
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
        } catch (ParseException e1) {
            Log.d("distinct_id", "Error: " + e1.getMessage());
        }

        if (it.hasID()) {
            try {
                globalId = it.getID();
                //priceObj.put("Id", it.getID());
                String nutri_link = "http://hdh.ucsd.edu/DiningMenus/nutritionfacts.aspx?i=" + trackerItem.getID();
                (new ParseCaloriesURL()).execute(new String[]{nutri_link});
            } catch (ActivityNotFoundException e2) {
                Toast.makeText(context, "No application can handle this request."
                        + " Please install a web browser", Toast.LENGTH_SHORT).show();
            }
        } else {
            //Toast.makeText(context, "Nutrition Facts Unavailable", Toast.LENGTH_SHORT).show();
        }
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
        }
    }

    private static class ParseCaloriesURL extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings){

            StringBuffer buffer = new StringBuffer();

            final String calories;

            try{
                Document doc = Jsoup.connect(strings[0]).get();

                Elements caloriesList;

                caloriesList = doc.select("table#tblFacts span");

                String tmp = caloriesList.get(0).toString();
                tmp = tmp.replaceAll("[<a-zA-Z>]", "");
                tmp = tmp.replaceAll("\\W", "");
                buffer.append(tmp);
                calories = buffer.toString();
                globalCal = calories;
            }catch(Throwable t){
                t.printStackTrace();
            }
            synchronized (priceObj){
                priceObj.notify();
                return buffer.toString();
            }
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
        }
    }
}
