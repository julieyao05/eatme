package com.example.jarvus.tummybuddy;

import android.app.Application;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_DINING_HALL = "com.example.jarvus.tummybuddy.DINING_HALL";

    private DatabaseTable db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        (new LoadDB(this)).execute();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //loadMenu();



        //handleIntent(getIntent());

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Cursor c = db.getWordMatches(query, null);

            TextView test = (TextView) findViewById(R.id.disp);

            if(c != null)
                test.setText(c.getString(c.getColumnIndex("ITEM")));
            else
                test.setText("Search failure");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void sixtyFourClicked(View view) {
        int name = DisplayMenuActivity.MENU_SIXTY_FOUR;
        loadMenu(name);
    }

    public void canyonVistaClicked(View view) {
        int name = DisplayMenuActivity.MENU_CANYON_VISTA;
        loadMenu(name);
    }

    public void cafeVentanaClicked(View view) {
        int name = DisplayMenuActivity.MENU_CAFE_VENTANAS;
        loadMenu(name);
    }

    public void clubMedClicked(View view) {
        int name = DisplayMenuActivity.MENU_CLUB_MED;
        loadMenu(name);
    }

    public void foodWorxClicked(View view) {
        int name = DisplayMenuActivity.MENU_FOODWORX;
        loadMenu(name);
    }

    public void goodysClicked(View view) {
        int name = DisplayMenuActivity.MENU_GOODYS;
        loadMenu(name);
    }

    public void pinesClicked(View view) {
        int name = DisplayMenuActivity.MENU_PINES;
        loadMenu(name);
    }

    public void rootsClicked(View view) {
        int name = DisplayMenuActivity.MENU_ROOTS;
        loadMenu(name);
    }

    public void bistroClicked(View view) {
        int name = DisplayMenuActivity.MENU_BISTRO;
        loadMenu(name);
    }

    public void loadMenu(int menu) {
        Intent intent = new Intent(this, DisplayMenuActivity.class);

        intent.putExtra(EXTRA_DINING_HALL, menu);
        startActivity(intent);
    }
    public void setDB(DatabaseTable d) {
        this.db = d;
    }

    private class LoadDB extends AsyncTask<Void, Void, Void> {
        private Context mContext;
        private DatabaseTable db;
        public LoadDB(Context context) {
            mContext = context;
            db = null;
        }
        protected Void doInBackground(Void... params) {
            db = new DatabaseTable(mContext);
            return null;
        }

        protected void onPostExecute(Void res) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.toolbar_progress_bar);
            progressBar.setVisibility(View.GONE);
            setDB(db);
        }
    }

    public static class ParseApplication extends Application {

        @Override
        public void onCreate() {
            super.onCreate();

            Parse.enableLocalDatastore(this);
            Parse.initialize(this, "2s0GrgbG5sY5OMVcemUzWe4TYLz86tLfRMp8ISTa", "wEjMG8yFL5GjR01PIzSLIpMlDJH5ghHXRPeoTmXm");

            ParseObject diningObj = new ParseObject("DiningHall");
            ParseQuery<ParseObject> query = ParseQuery.getQuery("DiningHall");
            try{
                if(query.count() == 0){
                    DataBase db = new DataBase();
                    db.collectData(diningObj);
                }
            }catch(Exception e){}


        }
    }
}


