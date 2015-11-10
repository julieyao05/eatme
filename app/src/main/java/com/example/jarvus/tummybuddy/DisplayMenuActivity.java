package com.example.jarvus.tummybuddy;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.util.Date;

public class DisplayMenuActivity extends Activity {
    public static final int MENU_ERROR = -1;
    public static final int MENU_SIXTY_FOUR = 0;
    public static final int MENU_CANYON_VISTA = 1;
    public static final int MENU_CAFE_VENTANAS = 2;
    public static final int MENU_CLUB_MED = 3;
    public static final int MENU_FOODWORX = 4;
    public static final int MENU_GOODYS = 5;
    public static final int MENU_PINES = 6;
    public static final int MENU_ROOTS = 7;
    public static final int MENU_BISTRO = 8;

   // public static final String hour_type1 = String.format("Mon - Thurs: %s am - %s pm \n Fri - Sun: %s am - % pm");
    public static final String hour_type1 = "Mon - Thurs: %s \n Fri - Sun: %s ";
    public static final String hour_type2 = "Mon - Thurs: %s \n Fri: %s | Sat - Sun: %s ";
    public static final String hour_type3 = "Mon - Fri: %s \n Sat - Sun: %s";

    private TextView data_text;
    private String url_data;
    private int time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_menu);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        //LinearLayout wholePage = (LinearLayout) findViewById(R.id.whole_page);
        //wholePage.setMovementMethod(new ScrollingMovementMethod());
        //data_text.setMovementMethod(new ScrollingMovementMethod());

        // Set variable as menu error by default
        int menu = getIntent().getIntExtra(MainActivity.EXTRA_DINING_HALL, MENU_ERROR);
        String menuName = getString(R.string.error_loading);
        TextView textView = (TextView) findViewById(R.id.menu_name);

        // Set hours of each dinning hall
        String dining_hours = getString(R.string.error_loading);
        TextView hour_text = (TextView) findViewById(R.id.hours);

        // Set the current date
        String currentDate = DateFormat.getDateInstance().format(new Date());
        TextView date_text = (TextView) findViewById(R.id.date);

        /*
        // Create drop down menu
        Spinner dropdown = (Spinner) findViewById(R.id.spinner1);
        final String[] items1 = new String[]{"Breakfast"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        dropdown.setAdapter(adapter);

        dropdown = (Spinner) findViewById(R.id.spinner2);
        final String[] items2 = new String[]{"Lunch"};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        dropdown.setAdapter(adapter);

        dropdown = (Spinner) findViewById(R.id.spinner3);
        final String[] items3 = new String[]{"Dinner"};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items3);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                items1[0] = "Coming Soon";
                items2[0] = "Coming Soon";
                items3[0] = "Coming Soon";
                //selectedItem = items[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        */

        if(menu == MENU_SIXTY_FOUR) {
            menuName = getString(R.string.button_sixty_four);
            dining_hours = String.format(hour_type1, "10 am - 9 pm", "10 am - 8 pm");
            url_data = "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=64";
        }
        else if (menu == MENU_CANYON_VISTA) {
            menuName = getString(R.string.button_canyon);
            dining_hours = String.format(hour_type2, "7:30 am - 9 pm", "7:30 am - 8 pm", "10 am - 8 pm");
            url_data = "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=24";
        }
        else if (menu == MENU_CAFE_VENTANAS) {
            menuName = getString(R.string.button_ventanas);
            dining_hours = String.format(hour_type2, "7:30 am - 9 pm", "7:30 am - 8 pm", "10 am - 8 pm");
            url_data = "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=18";
        }
        else if (menu == MENU_CLUB_MED) {
            menuName = getString(R.string.button_med);
            dining_hours = String.format(hour_type3, "7:30 am - 2 pm", "Closed");
            url_data = "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=15";
        }
        else if (menu == MENU_FOODWORX) {
            menuName = getString(R.string.button_foodworx);
            dining_hours = String.format(hour_type2, "7:30 am - 10 pm", "7:30 am - 8 pm", "10 am - 8 pm");
            url_data = "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=11";
        }
        else if (menu == MENU_GOODYS) {
            menuName = getString(R.string.button_goodys);
            dining_hours = String.format(hour_type3, "8 am - 10 pm", "11 am - 10 pm");
            url_data = "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=06";
        }
        else if (menu == MENU_PINES) {
            menuName = getString(R.string.button_pines);
            dining_hours = String.format(hour_type2, "7:30 am - 9 pm", "7:30 am - 8 pm", "10 am - 8 pm");
            url_data = "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=01";
        }
        else if (menu == MENU_ROOTS) {
            menuName = getString(R.string.button_roots);
            dining_hours = String.format(hour_type3, "11 am - 8 pm", "Closed");
            url_data = "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=32";
        }
        else if (menu == MENU_BISTRO) {
            menuName = getString(R.string.button_bistro);
            dining_hours = String.format(hour_type3, "11 am - 9 pm", "Closed");
            url_data = "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=27";
        }

        textView.setText(menuName);
        hour_text.setText(dining_hours);
        date_text.setText(currentDate);

        // Parsing data when clicking the button
        Button btn = (Button) findViewById(R.id.data_button);
        data_text = (TextView) findViewById(R.id.textData);
        //data_text.setMovementMethod(new ScrollingMovementMethod());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                (new ParseURL()).execute(new String[]{url_data});
            }
        });

        // Set the text view as the activity layout
    }

    public void onBackPressed() {
        finish();
    }

    private class ParseURL extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings){

            StringBuffer buffer = new StringBuffer();

            try{
                Document doc = Jsoup.connect(strings[0]).get();

                // Get title
                String title = doc.title();

                // Get body
                Elements menuList = doc.select("td.menuList");

                // append appropriate menu based on the value of "time" which is set by
                // among breakfast, lunch or dinner button
                if(time == 0){
                    buffer.append(menuList.get(0).text());
                }
                else if(time == 1){
                    buffer.append(menuList.get(1).text());
                }
                else{
                    buffer.append(menuList.get(2).text());
                }

            }catch(Throwable t){
                t.printStackTrace();
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            data_text.setText(s);
        }
    }
}
