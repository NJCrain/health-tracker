package com.njcrain.android.healthtracker;

import com.njcrain.android.healthtracker.activity.FingerClickerActivity;

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

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FingerClickerActivityTest {

    @Rule
    public ActivityTestRule<FingerClickerActivity> mActivityRule =
            new ActivityTestRule<>(FingerClickerActivity.class);

    @Test
    public void checkClickerExercise() {
        onView(withId(R.id.textView)).check(matches(withText("Clicked 0 Times")));

        for (int i = 1; i < 10; i++) {
            onView(withId(R.id.exerciseButton)).perform(click());
            onView(withId(R.id.textView)).check(matches(withText("Clicked " + i + " times")));
        }
    }
}
