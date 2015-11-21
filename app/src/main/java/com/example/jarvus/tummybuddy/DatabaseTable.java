package com.example.jarvus.tummybuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Created by Minh on 11/20/15.
 */
public class DatabaseTable {

    private static final String TAG = "DictionaryDatabase";

    //The columns we'll include in the dictionary table
    public static final String COL_HALL = "HALL";
    public static final String COL_ITEM = "ITEM";
    public static final String COL_COST = "COST";
    public static final String COL_ID = "ID";

    private static final String DATABASE_NAME = "DB";
    private static final String FTS_VIRTUAL_TABLE = "FTS";
    private static final int DATABASE_VERSION = 1;

    private final DatabaseOpenHelper mDatabaseOpenHelper;

    public DatabaseTable(Context context) {
        mDatabaseOpenHelper = new DatabaseOpenHelper(context);
    }

    public Cursor getWordMatches(String query, String[] columns) {
        String selection = COL_ITEM + " MATCH ?";
        String[] selectionArgs = new String[] {query+"*"};

        return query(selection, selectionArgs, columns);
    }

    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(FTS_VIRTUAL_TABLE);

        Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    private static class DatabaseOpenHelper extends SQLiteOpenHelper {

        //private final Context mHelperContext;
        private SQLiteDatabase mDatabase;

        private static final String FTS_TABLE_CREATE =
                "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
                        " USING fts3 (" +
                        COL_HALL + ", " +
                        COL_ITEM + ", " +
                        COL_COST + ", " +
                        COL_ID + ")";

        DatabaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            //mHelperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            mDatabase = db;
            mDatabase.execSQL(FTS_TABLE_CREATE);
            loadMenu();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            onCreate(db);
        }

        private void loadMenu() {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        loadWords();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }

        public StringBuffer loadMenuItems(){
            String[] toLoad = {"http://hdh.ucsd.edu/DiningMenus/default.aspx?i=64",
                    "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=24",
                    "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=18",
                    "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=15",
                    "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=11",
                    "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=06",
                    "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=01",
                    "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=32",
                    "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=27"};
            StringBuffer buffer = null;

            try {
                buffer = new StringBuffer();
                for(String load : toLoad) {
                    Document doc = Jsoup.connect(load).get();
                    Elements menuList = doc.select("li[style] a");

                    // Get title
                    String title = doc.title();
                    title = title.substring(0, title.indexOf('|')).trim();

                    // if you split by : and get only 3 elements it is hall : name : pageID
                    // if you get four elements it is hall : name : cost : pageID
                    for(int x=0; x<menuList.size(); x++){
                        buffer.append(title + ":" +
                                menuList.get(x).text().replaceAll("\u00a0(\u00a0)+", ":") + ":" +
                                menuList.get(x).attr("href").replaceAll("[^0-9]", "") +"\n");
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            return buffer;
        }

        private void loadWords() throws IOException {
            InputStream inputStream = new ByteArrayInputStream(loadMenuItems().toString().getBytes(StandardCharsets.UTF_8));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] strings = TextUtils.split(line, ":");
                    long id;
                    if (strings.length == 4) {
                        id = addItem(strings[0], strings[1], strings[2], strings[3]);

                    } else if(strings.length == 3) {
                        id = addItem(strings[0], strings[1], "Cost Unknown", strings[2]);
                    } else {
                        continue;
                    }
                    if (id < 0) {
                        Log.e(TAG, "unable to add word: " + strings[0].trim());
                    }
                }
            } finally {
                reader.close();
            }
        }

        public long addItem(String hall, String item, String cost, String id) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(COL_HALL, hall);
            initialValues.put(COL_ITEM, item);
            initialValues.put(COL_COST, cost);
            initialValues.put(COL_ID, id);

            return mDatabase.insert(FTS_VIRTUAL_TABLE, null, initialValues);
        }
    }
}
