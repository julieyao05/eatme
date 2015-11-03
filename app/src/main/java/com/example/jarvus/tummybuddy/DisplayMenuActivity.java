package com.example.jarvus.tummybuddy;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class DisplayMenuActivity extends Activity {
    public static final int MENU_SIXTY_FOUR = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_menu);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        int menu = getIntent().getIntExtra(MainActivity.EXTRA_DINING_HALL, -1);

        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText("Menu num: " + menu);

        // Set the text view as the activity layout
        setContentView(textView);
    }

}
