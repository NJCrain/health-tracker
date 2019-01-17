package com.njcrain.android.healthtracker;

import com.njcrain.android.healthtracker.activity.ImageGalleryActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

//TODO: Add the implementation of a withDrawable matcher from https://medium.com/@dbottillo/android-ui-test-espresso-matcher-for-imageview-1a28c832626f for better testing
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ImageGalleryActivityTest {

    @Rule
    public ActivityTestRule<ImageGalleryActivity> mActivityRule =
            new ActivityTestRule<>(ImageGalleryActivity.class);


    @Test
    public void checkDisplayed() {
        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
        onView(withId(R.id.captionText)).check(matches(withText("This could be you in 500 button clicks")));
        onView(withId(R.id.imageLocation)).check(matches(withText("1/3")));
        onView(withId(R.id.next)).check(matches(withText("Next")));
        onView(withId(R.id.prev)).check(matches(withText("Prev")));
    }

    @Test
    public void testFunctionality() {
        onView(withId(R.id.next)).perform(click());
        onView(withId(R.id.captionText)).check(matches(withText("This Seagull used the app, and look at them now!")));
        onView(withId(R.id.imageLocation)).check(matches(withText("2/3")));

        onView(withId(R.id.prev)).perform(click());
        onView(withId(R.id.captionText)).check(matches(withText("This could be you in 500 button clicks")));
        onView(withId(R.id.imageLocation)).check(matches(withText("1/3")));

        onView(withId(R.id.prev)).perform(click());
        onView(withId(R.id.captionText)).check(matches(withText("Here's a cute puppy for inspiration")));
        onView(withId(R.id.imageLocation)).check(matches(withText("3/3")));

        onView(withId(R.id.next)).perform(click());
        onView(withId(R.id.captionText)).check(matches(withText("This could be you in 500 button clicks")));
        onView(withId(R.id.imageLocation)).check(matches(withText("1/3")));
    }
}
