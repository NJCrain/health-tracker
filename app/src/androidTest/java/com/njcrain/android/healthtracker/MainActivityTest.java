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
public class MainActivityTest {
    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule =
            new IntentsTestRule<>(MainActivity.class);

    @Test
    public void testClickerExerciseButton() {
        onView(withId(R.id.button)).perform(click());
        intended(hasComponent(FingerClickerActivity.class.getName()));
    }

    @Test
    public void testStopwatchButton() {
        onView(withId(R.id.button4)).perform(click());
        intended(hasComponent(StopwatchActivity.class.getName()));
    }

    @Test
    public void testImageButton() {
        onView(withId(R.id.button17)).perform(click());
        intended(hasComponent(ImageGalleryActivity.class.getName()));
    }

    @Test
    public void testExerciseLogButton() {
        onView(withId(R.id.button3)).perform(click());
        intended(hasComponent(ExerciseLogActivity.class.getName()));
    }
}
