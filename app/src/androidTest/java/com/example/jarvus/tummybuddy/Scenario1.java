package com.example.jarvus.tummybuddy;


import android.test.ActivityInstrumentationTestCase2;
import android.support.test.InstrumentationRegistry;


import org.junit.Before;
import org.junit.Test;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;




/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class Scenario1 extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity displayActivity;

    public Scenario1() {
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
    public void testOpTimes() throws InterruptedException {
        Thread.sleep(3000);
        //when click a dining hall
        onView(withId(R.id.sixty_four)).perform(click());
        Thread.sleep(30000);
        //should see the hours of operation displayed
        onView(withId(R.id.hours)).check(matches(isDisplayed()));

    }

}