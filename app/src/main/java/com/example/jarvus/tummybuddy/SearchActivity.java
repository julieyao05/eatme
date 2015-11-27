package com.example.jarvus.tummybuddy;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 2015-11-23.
 */
public class SearchActivity extends Activity {

    EditText editSearch;
    ListView listView;
    private ArrayList<String> mItems;
    private ArrayList<String> vegetarian;
    private ArrayList<String> vegan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        final Context context = this;
        editSearch = (EditText) findViewById(R.id.editText1);
        listView = (ListView) findViewById(R.id.search_list);
        vegetarian = new ArrayList<String>();
        vegan = new ArrayList<String>();

        //
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                final int pos = position;

                PopupMenu popup = new PopupMenu(context, arg1);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Object o = listView.getItemAtPosition(pos);
                        Item it = new Item((String)o);
                        switch (item.getItemId()) {
                            case R.id.viewNutr:
                                MenuClick.viewNutrition(it, context);
                                return true;
                            case R.id.trackItem:
                                MenuClick.addToTracker(it, context);
                                return true;
                            case R.id.wishlist:
                                MenuClick.addToWishlist(it, context);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.inflate(R.menu.display_menu);
                popup.show();
            }
        });

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("DiningHall");
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    //Toast.makeText(ParseListActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                }
                for (ParseObject obj : list) {
                    String menu = obj.getString("menu");
                    mItems.add(menu);
                    String[] tags = obj.getString("tags").split(",");
                    for (String t : tags) {
                        if (t.equals("vegetarian"))
                            vegetarian.add(menu);
                        if (t.equals("vegan"))
                            vegan.add(menu);
                    }
                }
            }
        });

        mItems = new ArrayList<String>();
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mItems));

        editSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<String> temp = new ArrayList<String>();
                int textlength = editSearch.getText().length();
                temp.clear();
                if (editSearch.getText().toString().equalsIgnoreCase("vegetarian")) {
                    temp = vegetarian;
                } else if (editSearch.getText().toString().equalsIgnoreCase("vegan")) {
                    temp = vegan;
                } else {
                    for (int i = 0; i < mItems.size(); i++) {
                        if (textlength <= mItems.get(i).length()) {
                            if (mItems.get(i).matches("(?i:.*" + editSearch.getText().toString() + ".*)")) {
                                temp.add(mItems.get(i));
                            }
                        }
                    }
                }
                ArrayAdapter<String> aa;
                if (temp.size() == 0) {
                    temp.add("No Items Found");
                    aa = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_list_item_1, temp) {
                        @Override
                        public boolean isEnabled(int position) {
                            return false;
                        }
                    };
                } else {
                    aa = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_list_item_1, temp);
                }


                listView.setAdapter(aa);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
    }

}
