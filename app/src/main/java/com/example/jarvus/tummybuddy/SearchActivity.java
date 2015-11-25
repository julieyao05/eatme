package com.example.jarvus.tummybuddy;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        editSearch = (EditText)findViewById(R.id.editText1);
        listView = (ListView)findViewById(R.id.search_list);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("DiningHall");
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseObject>(){

            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e != null){
                    //Toast.makeText(ParseListActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                }
                for(ParseObject obj : list){
                    String menu = obj.getString("menu");
                    mItems.add(menu);
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

                for (int i = 0; i < mItems.size(); i++){
                    if (textlength <= mItems.get(i).length()){
                        if(editSearch.getText().toString().equalsIgnoreCase(
                                (String) mItems.get(i).subSequence(0, textlength))){
                            temp.add(mItems.get(i));
                        }
                    }
                }
                listView.setAdapter(new ArrayAdapter<String>(SearchActivity.this,android.R.layout.simple_list_item_1, temp));
            }

            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        });
    }
}
