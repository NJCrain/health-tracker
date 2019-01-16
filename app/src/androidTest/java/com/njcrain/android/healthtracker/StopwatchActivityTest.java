package com.njcrain.android.healthtracker;

import com.njcrain.android.healthtracker.activity.FingerClickerActivity;
import com.njcrain.android.healthtracker.activity.StopwatchActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class StopwatchActivityTest {

    @Rule
    public ActivityTestRule<StopwatchActivity> mActivityRule =
            new ActivityTestRule<>(StopwatchActivity.class);
    @Test
    public void checkDisplayed() {
        onView(withId(R.id.textView2)).check(matches(withText("0:00:00.000")));
        onView(withId(R.id.startStop)).check(matches(withText("Start")));
        onView(withId(R.id.reset)).check(matches(withText("Reset")));
    }

    //TODO: Find a way to make this test work
//    @Test
//    public void testFunctionality() {
//        onView(withId(R.id.startStop)).perform(click());
////        long startTime = System.currentTimeMillis();
////        while(true) {
////            if (System.currentTimeMillis() - startTime > 5000) {
////                break;
////            }
////        }
//        onView(withId(R.id.startStop)).check(matches(withText("Stop")));
//        onView(withId(R.id.startStop)).perform(click());
//        onView(withId(R.id.textView2)).check(matches(not(withText("0:00:00.000"))));
//    }
}
