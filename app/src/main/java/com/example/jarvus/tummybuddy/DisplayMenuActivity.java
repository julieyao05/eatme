package com.example.jarvus.tummybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_menu);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        // Set variable as menu error by default
        int menu = getIntent().getIntExtra(MainActivity.EXTRA_DINING_HALL, MENU_ERROR);
        String menuName = getString(R.string.error_loading);
        TextView textView = (TextView) findViewById(R.id.menu_name);

        if(menu == MENU_SIXTY_FOUR)
            menuName = getString(R.string.button_sixty_four);
        else if (menu == MENU_CANYON_VISTA)
            menuName = getString(R.string.button_canyon);
        else if (menu == MENU_CAFE_VENTANAS)
            menuName = getString(R.string.button_ventanas);
        else if (menu == MENU_CLUB_MED)
            menuName = getString(R.string.button_med);
        else if (menu == MENU_FOODWORX)
            menuName = getString(R.string.button_foodworx);
        else if (menu == MENU_GOODYS)
            menuName = getString(R.string.button_goodys);
        else if (menu == MENU_PINES)
            menuName = getString(R.string.button_pines);
        else if (menu == MENU_ROOTS)
            menuName = getString(R.string.button_roots);
        else if (menu == MENU_BISTRO)
            menuName = getString(R.string.button_bistro);

        textView.setText(menuName);

        // Set the text view as the activity layout
    }

    public void onBackPressed() {
        finish();
    }
}
