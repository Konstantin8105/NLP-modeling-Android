package com.modelingbrain.home;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@SuppressWarnings("ALL")
public class RotateTest {
    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void rotateTest() {
        rotateScreen(Configuration.ORIENTATION_PORTRAIT);
        onView(withId(R.id.fab)).check(matches(isDisplayed()));
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).perform(click());
        rotateScreen(Configuration.ORIENTATION_LANDSCAPE);
        onView(withText("Name:")).check(matches(isDisplayed()));
        rotateScreen(Configuration.ORIENTATION_PORTRAIT);
        onView(withText("Name:")).perform(pressBack());
        onView(withId(R.id.fab)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(click());
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
    }

    private void rotateScreen(int orientationNext) {
        Context context = InstrumentationRegistry.getTargetContext();
        int orientation = context.getResources().getConfiguration().orientation;

        if (orientation != orientationNext) {
            Activity activity = main.getActivity();
            activity.setRequestedOrientation(
                    (orientationNext == Configuration.ORIENTATION_PORTRAIT) ?
                            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
}
