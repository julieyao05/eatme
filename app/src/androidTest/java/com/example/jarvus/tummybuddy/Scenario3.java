package com.example.jarvus.tummybuddy;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;


/**
 * Created by Min-Hsuan Yao on 12/3/15.
 */
public class Scenario3 extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity displayActivity;

    public Scenario3() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        //creates and launches the activity for you if it doesn't already exist.
        displayActivity = getActivity();
    }

    @Test
    public void testOptions() throws InterruptedException {
        Thread.sleep(3000);

        //when click a dining hall
        onView(withId(R.id.sixty_four)).perform(click());
        Thread.sleep(3000);
        //and click a meal button
        onView(withId(R.id.button2)).perform(click());
        Thread.sleep(3000);
        //should see a list of food options displayed
        onData(instanceOf(String.class)).inAdapterView(allOf(withId(android.R.id.list), isDisplayed()));

    }
}
