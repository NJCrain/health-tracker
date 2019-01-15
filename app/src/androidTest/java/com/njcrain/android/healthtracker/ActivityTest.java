package com.njcrain.android.healthtracker;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ActivityTest {
    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule =
            new IntentsTestRule<>(MainActivity.class);

    @Test
    public void testMain() {
        onView(withId(R.id.button)).perform(click());
        intended(hasComponent(FingerClickerActivity.class.getName()));
//        onView(withId(R.id.button4)).check(matches(withText("Clicker Exercise")));
//        onView(withId(R.id.button17)).check(matches(withText("Clicker Exercise")));
//        onView(withId(R.id.button3)).check(matches(withText("Clicker Exercise")));
    }
}
