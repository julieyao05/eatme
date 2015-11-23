package com.example.jarvus.tummybuddy;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Leo on 2015-11-21.
 */
public class DataBase {

    ParseObject testObject;

    public void collectData( ParseObject testObject1) {

        testObject = testObject1;


        String[] toLoad = {"http://hdh.ucsd.edu/DiningMenus/default.aspx?i=64",
                "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=24",
        "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=18",
        "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=15",
         "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=11",
         "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=06",
         "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=01",
         "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=32",
        "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=27"};

        for (String load : toLoad) {
            new ParseURL().execute(new String[]{load});
        }
    }


    public void storingIntoDatabase(String s) {

        InputStream inputStream = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        Set<String> itemSet = new HashSet<>();
        Set<String> dhallSet = new HashSet<>();

        try{
            List<ParseObject> list = new ArrayList<>();


        while ((line = reader.readLine()) != null) {
            ParseObject testObject = new ParseObject("DiningHall");
            String[] sArray = TextUtils.split(line, ":");
            if( (itemSet.add(sArray[1]))  ){
                if (sArray.length == 4) {
                    testObject.put("diningHall", sArray[0]);
                    testObject.put("menu", sArray[1]);
                    testObject.put("price", sArray[2]);
                    testObject.put("distinct_id", sArray[3]);
                } else if (sArray.length == 3) {
                    testObject.put("diningHall", sArray[0]);
                    testObject.put("menu", sArray[1]);
                    testObject.put("price", "Cost Unknown");
                    testObject.put("distinct_id", sArray[2]);
                } else if (sArray.length == 2){
                    testObject.put("diningHall", sArray[0]);
                    testObject.put("menu", sArray[1]);
                }
                else {
                    continue;
                }
                list.add(testObject);
            }
        }
            testObject.saveAllInBackground(list, new SaveCallback() {
                @Override
                public void done(ParseException e) { }
            });

        }catch(Exception e){
            e.printStackTrace();
        }

    }



    private class ParseURL extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings){

            StringBuffer buffer = new StringBuffer();

            try{
            Document doc = Jsoup.connect(strings[0]).get();
            Elements menuList = doc.select("li[style] a");


            // Get title
            String title = doc.title();

            title = title.substring(0, title.indexOf('|')).trim();

            // if you split by : and get only 3 elements it is hall : name : pageID
            // if you get four elements it is hall : name : cost : pageID

            for(int x = 0; x<menuList.size(); x++){

                buffer.append(title + ":" +
                        menuList.get(x).text().replaceAll("\u00a0(\u00a0)+", ":") + ":" +
                        menuList.get(x).attr("href").replaceAll("[^0-9]", "") +"\n");

                /*
                // Returns elements as such
                //Vegetable Tempura  ($7.00)  : vegetarian item
                //Bistro Green Salad  ($6.00)   : vegan item
                //Soy-Gingered Salmon Cake Salad  ($8.00)  : gluten free item
                Elements vegans;
                if((vegans = menuList.get(x).select("img")).size() > 0)
                    buffer.append(menuList.get(x).text() + " : " + vegans.first().attr("alt") + '\n');
                */
            }
            //    testObject.put("menu", "x is "+x);
             //           testObject.saveInBackground();
             } catch (Exception e){
                e.printStackTrace();
            }

          return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            storingIntoDatabase(s);
        //    testObject.put("menu", "test3");
            //testObject.put("price", s.length());
            //testObject.saveInBackground();

        }
    }
}
