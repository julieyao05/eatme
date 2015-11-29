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
import java.util.List;

/**
 * Created by Leo on 2015-11-23.
 */
public class CounterActivity extends Activity{

    private ArrayList<String> priceArrayString;
    private ArrayList<Double> priceArrayDouble;
    private ListView priceListView;
    final Context context = this;
    private TextView totalPriceTextView;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter_activity);
        final Context context = this;

        totalPriceTextView = (TextView)findViewById(R.id.total_price);

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
                            case R.id.remove:
                                return removeFromPriceCounter(adv, it);
                            default:
                                return false;
                        }
                    }
                });
                popup.inflate(R.menu.remove_menu);
                popup.show();
            }
        };
        priceListView.setOnItemClickListener(av);
    }

    void calrories_counter(){
    }

    void showPriceList(){

        // connecting to database
        ParseQuery<ParseObject> priceQuery = new ParseQuery<ParseObject>("Counter");
        priceQuery.setLimit(1000);
        priceQuery.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    //Toast.makeText(ParseListActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                }
                String tmpPrice;
                for (ParseObject obj : list) {
                    String priceString = obj.getString("Price");
                    priceString = priceString.replaceAll("[$()]", "");
                    double priceDouble = Double.parseDouble(priceString);
                    priceArrayDouble.add(priceDouble);

                    DecimalFormat format = new DecimalFormat("0.00");
                    String formattedPrice = format.format(priceDouble);
                    priceArrayString.add("$" + formattedPrice);
                    //priceArrayString.add(todaysFood);
                }

                priceListView.setAdapter(new ArrayAdapter<String>(CounterActivity.this, android.R.layout.simple_list_item_1, priceArrayString));
                totalPriceTextView.setText(calculatingTotalPrice(""));
            }
        });
    }

    private boolean removeFromPriceCounter(AdapterView<?> adv, final String item) {


        //removing from the listView
        ArrayAdapter<String> adapter = (ArrayAdapter) adv.getAdapter();
        adapter.remove(item);

        // removing from total
        totalPriceTextView.setText(calculatingTotalPrice(item));

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Counter");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    //Toast.makeText(ParseListActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                }
                for (ParseObject obj : list) {
                    String price = obj.getString("Price");
                    if(price.equals(item)){
                        obj.deleteInBackground();
                        break;
                    }
                }
            }
        });
        return true;
    }

    String calculatingTotalPrice(String remove){

        Double totalPrice = 0.0;
        if(!remove.equals("")){
            remove = remove.replaceAll("[$]", "");
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
}
