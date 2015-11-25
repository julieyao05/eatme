package com.example.jarvus.tummybuddy;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Leo on 2015-11-23.
 */
public class CounterActivity extends Activity{

    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter_activity);

        int counter = 0;
        //counter_activity = getIntent().getIntExtra(MainActivity.COUNTER, MENU_ERROR);

        if(counter == 0){
            calories_counter();
        }else if(counter == 1){
            price_counter();
        }else{
            wishList();
        }
    }

    void calories_counter(){

    }

    void price_counter(){

    }

    void wishList(){

    }

}
