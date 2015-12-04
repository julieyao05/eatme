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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Leo on 2015-11-23.
 */
public class CounterActivity extends Activity{

    private ArrayList<String> priceArrayString;
    private ArrayList<Double> priceArrayDouble;
    private ListView priceListView;
    private TextView totalPriceTextView;
    private HashMap<String, String> priceHashMap;

    private ArrayList<String> caloriesArrayString;
    private ArrayList<Integer> caloreisArrayInt;
    private ListView caloriesListView;
    private TextView totalCaloriesTextView;
    private HashMap<String, String> caloriesHashMap;

    private ArrayList<ArrayList<String>> idArray;

    ParseQuery<ParseObject> removeQuery;
    public boolean flag;
    final Context context = this;

    TextView tmp123;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter_activity);

        tmp123 = (TextView)findViewById(R.id.calories_text);

        final Context context = this;
        priceHashMap = new HashMap<String,String>();
        caloriesHashMap = new HashMap<String, String>();

        totalPriceTextView = (TextView)findViewById(R.id.total_price);
        totalCaloriesTextView = (TextView)findViewById(R.id.total_calories);

        // call price_counter() and calories_counter()
        price_counter();
        calrories_counter();
    }

    void price_counter(){

        //Initializing priceArrayString and carlriesList
        priceArrayString = new ArrayList<String>();
        priceArrayDouble = new ArrayList<Double>();
        priceListView = (ListView)findViewById(R.id.price_counter);

        // Show price added to counter
        showPriceList();

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
                            case R.id.remove_c:
                                return removeFromCounter(adv, it, false);
                            default:
                                return false;
                        }
                    }
                });
                popup.inflate(R.menu.remove_counter);
                popup.show();
            }
        };
        priceListView.setOnItemClickListener(av);
    }

    void calrories_counter(){
        //Initializing priceArrayString and carlriesList
        caloriesArrayString = new ArrayList<String>();
        caloreisArrayInt = new ArrayList<Integer>();
        caloriesListView = (ListView)findViewById(R.id.calories_counter);

        // Show price added to counter
        showCaloriesList();

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
                            case R.id.remove_c:
                                return removeFromCounter(adv, it, true);
                            default:
                                return false;
                        }
                    }
                });
                popup.inflate(R.menu.remove_counter);
                popup.show();
            }
        };
        caloriesListView.setOnItemClickListener(av);
    }

    void showCaloriesList(){

        // connecting to database
        ParseQuery<ParseObject> caloriesQuery = new ParseQuery<ParseObject>("Calories");
        caloriesQuery.setLimit(1000);
        caloriesQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    //Toast.makeText(ParseListActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                }
                caloriesListView.setAdapter(new ArrayAdapter<String>(CounterActivity.this, android.R.layout.simple_list_item_1, caloriesArrayString));
                for (ParseObject obj : list) {
                    String caloriesString = obj.getString("Calories");
                    String itemString = obj.getString("Item");
                    String idString = obj.getString("Id");
                    caloriesHashMap.put(idString, itemString);
                    Integer caloriesInt;
                    try {
                        caloriesInt = Integer.parseInt(caloriesString);
                    } catch (NumberFormatException ex) {
                        caloriesInt = 0;
                    }
                    caloreisArrayInt.add(caloriesInt);

                    caloriesArrayString.add(caloriesInt + "\n(" + itemString + ")");
                }

                caloriesListView.setAdapter(new ArrayAdapter<String>(CounterActivity.this, android.R.layout.simple_list_item_1, caloriesArrayString));
                totalCaloriesTextView.setText(calculatingTotalCalories(""));

            }
        });
    }
    void showPriceList(){
        // connecting to database
        ParseQuery<ParseObject> priceQuery = new ParseQuery<ParseObject>("Counter");
        priceQuery.setLimit(1000);

        priceQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    //Toast.makeText(ParseLi6stActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                }

                for (ParseObject obj : list) {
                    String priceString = obj.getString("Price");
                    String itemString = obj.getString("Item");
                    String idString = obj.getString("Id");
                    priceHashMap.put(idString, itemString);
                    priceString = priceString.replaceAll("[$()\\s]", "");
                    double priceDouble;
                    try {
                        priceDouble = Double.parseDouble(priceString);
                    } catch (NumberFormatException ex) {
                        priceDouble = 0.00;
                    }
                    priceArrayDouble.add(priceDouble);

                    DecimalFormat format = new DecimalFormat("0.00");
                    String formattedPrice = format.format(priceDouble);

                    priceArrayString.add("$" + formattedPrice + "\n(" + itemString + ")");
                }

                priceListView.setAdapter(new ArrayAdapter<String>(CounterActivity.this, android.R.layout.simple_list_item_1, priceArrayString));
                totalPriceTextView.setText(calculatingTotalPrice(""));
            }
        });


    }

    private boolean removeFromCounter(AdapterView<?> adv, final String item, boolean isCalories) {

        //removing from the listView
        ArrayAdapter<String> adapter = (ArrayAdapter) adv.getAdapter();
        adapter.remove(item);

        // retrieving only food item. (removing names of dining hall
        String copyIt = item;
        int tmpIndex = copyIt.indexOf("(");
        copyIt = copyIt.substring(tmpIndex+1, copyIt.length() - 1);

        // subtracting from totalPrice
        final String onlyPriceOrCalories = (copyIt.replaceAll("[(a-zA-Z)]", "")).trim();

        if(isCalories){
            // deleting selected calories fron database
            removeQuery = new ParseQuery<ParseObject>("Calories");
            removeQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    String calories = "";
                    outerLoop:
                    for (ParseObject obj : list) {
                        String id = obj.getString("Id");
                        for (String key : caloriesHashMap.keySet()) {
                            if (key.equals(id)) {
                                calories = obj.getString("Calories");
                                try {
                                    obj.delete();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                                break outerLoop;
                            }
                        }
                    }
                    totalCaloriesTextView.setText(calculatingTotalCalories(calories));
                }
            });
        }else{
            // deleting selected price from database
            removeQuery = new ParseQuery<ParseObject>("Counter");
            removeQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    String price = "";
                    String calories = "";
                    outerLoop:
                    for (ParseObject obj : list) {
                        String id = obj.getString("Id");
                        for (String key : priceHashMap.keySet()) {
                            if (key.equals(id)) {
                                price = obj.getString("Price");
                                price = price.replaceAll("[()]", "");
                                calories = obj.getString("Calories");
                                try {
                                    obj.delete();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                                break outerLoop;
                            }
                        }
                    }
                    totalPriceTextView.setText(calculatingTotalPrice(price));
                }
            });
        }
        return true;
    }

    String calculatingTotalPrice(String remove){

        Double totalPrice = 0.0;
        if(!remove.equals("")){
                remove = remove.replaceAll("[^\\d.]", "");
                remove = remove.trim();
                double removePrice = Double.parseDouble(remove);
                priceArrayDouble.remove(removePrice);
        }

        //calculating total price
        for(int i=0; i<priceArrayDouble.size(); i++){
            totalPrice += priceArrayDouble.get(i);
        }
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String stringTotalPrice = "Total : " + formatter.format(totalPrice);

        return stringTotalPrice;
    }

    String calculatingTotalCalories(String remove){

        int totalCalories = 0;

       //tmp123.append("\nremove : " + remove);

        if(!remove.equals("")){
            for(int i=0; i<caloreisArrayInt.size(); i++){
                String tmpString = caloreisArrayInt.get(i).toString();
                if(tmpString.equals(remove)){
                    caloreisArrayInt.remove(i);
                }
            }
        }
        //calculating total price
        for(int i=0; i<caloreisArrayInt.size(); i++){
            totalCalories += caloreisArrayInt.get(i);
        }

        String totalCaloriesString = String.valueOf(totalCalories);

        return totalCaloriesString;
    }

    public void clearPriceCount(View view) {
        ArrayAdapter<String> adapter = (ArrayAdapter) priceListView.getAdapter();
        priceArrayDouble.clear();
        priceArrayString.clear();
        adapter.clear();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Counter");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> list, ParseException e) {
            if(e == null)
                ParseObject.deleteAllInBackground(list);
            }
        });
        totalPriceTextView.setText(calculatingTotalPrice(""));
    }

    public void clearCalorieCount(View view) {
        ArrayAdapter<String> adapter = (ArrayAdapter) caloriesListView.getAdapter();
        caloreisArrayInt.clear();
        caloriesArrayString.clear();
        adapter.clear();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Calories");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> list, ParseException e) {
            if(e == null)
                ParseObject.deleteAllInBackground(list);
            }
        });
        totalCaloriesTextView.setText(calculatingTotalCalories(""));
    }
}
