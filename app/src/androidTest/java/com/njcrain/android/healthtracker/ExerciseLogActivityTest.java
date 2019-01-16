package com.njcrain.android.healthtracker;


import android.text.format.DateFormat;

import com.njcrain.android.healthtracker.activity.ExerciseLogActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.Random;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExerciseLogActivityTest {
    @Rule
    public ActivityTestRule<ExerciseLogActivity> mActivityRule =
            new ActivityTestRule<>(ExerciseLogActivity.class);

    @Test
    public void checkDisplayed() {
        onView(withId(R.id.exerciseList)).check(matches(isDisplayed()));
        onView(withId(R.id.exerciseTitle)).check(matches(withHint("Exercise")));
        onView(withId(R.id.description)).check(matches(withHint("Description")));
        onView(withId(R.id.quantity)).check(matches(withHint("Quantity")));
        onView(withId(R.id.button5)).check(matches(withText("Add Exercise")));
    }

    @Test
    public void testAddExercise() {
        Random random = new Random();

        onView(withId(R.id.exerciseTitle)).perform(typeText("Test"));
        onView(withId(R.id.description)).perform(typeText("just testing"));
        onView(withId(R.id.quantity)).perform(typeText(Integer.toString(random.nextInt())));
        onView(withId(R.id.button5)).perform(click());

        Date now = new Date();
        String timestamp = DateFormat.format("M/d/yy h:mma", now).toString();

        //Parts of how I setup this specific test came from https://stackoverflow.com/questions/22965839/espresso-click-by-text-in-list-view
        onData(anything()).inAdapterView(withId(R.id.exerciseList)).atPosition(0).check(matches(withText("Test: just testing - " + timestamp)));

    }
}
