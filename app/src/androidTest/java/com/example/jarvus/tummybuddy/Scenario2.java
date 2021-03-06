package com.example.jarvus.tummybuddy;

import android.support.test.InstrumentationRegistry;

import android.test.ActivityInstrumentationTestCase2;


import org.junit.Before;
import org.junit.Test;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


/**
 * Created by Min-Hsuan Yao on 12/2/15.
 * Given that there is a search bar, when I type in my favorite food or preference, then I should see if it is available today.
 */
public class Scenario2 extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mainActivity;

    public Scenario2() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        //creates and launches the activity for you if it doesn't already exist.
        mainActivity = getActivity();
    }

    @Test
    public void testSearch() throws InterruptedException {
        //Given that there is a search bar
        onView(withId(R.id.search_page)).perform(click());
        //when I type in my favorite food or preference
        onView(withId(R.id.editText1)).perform(typeText("Breakfast Burrito")).check(matches(isDisplayed()));
        //wait a bit for the text to process
        Thread.sleep(6000);
        //then I should see if it is available today
        onView(allOf(withText("Breakfast Burrito"), withParent(withId(R.id.search_list))));

    }
}
