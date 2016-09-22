package com.modelingbrain.home;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.openDrawer;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void things() {
        onView(withId(R.id.fab)).check(matches(isDisplayed()));
    }

    @Test
    public void things2() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
    }

    @Test
    public void things3() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.recycler_view)).perform(swipeUp());
    }

    @Test
    public void things4() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.recycler_view)).perform(swipeUp());
        onView(withId(R.id.recycler_view)).perform(pressBack());
    }

    @Test
    public void things5() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).perform(click());
    }

    @Test
    public void things6() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withText("Model SCORE")).perform(click());
    }

    @Test
    public void things7() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).perform(pressBack());
    }

    @Test
    public void rotate1() {
        rotateScreen(Configuration.ORIENTATION_PORTRAIT);
        onView(withId(R.id.fab)).perform(click());
        rotateScreen(Configuration.ORIENTATION_LANDSCAPE);
        onView(withId(R.id.recycler_view)).perform(pressBack());
        rotateScreen(Configuration.ORIENTATION_PORTRAIT);
    }

    @Test
    public void rotate2() {
        rotateScreen(Configuration.ORIENTATION_PORTRAIT);
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withText("Model SCORE")).perform(click());
        rotateScreen(Configuration.ORIENTATION_LANDSCAPE);
        onView(withText("Name:")).check(matches(isDisplayed()));
        rotateScreen(Configuration.ORIENTATION_PORTRAIT);
        onView(withText("Name:")).perform(pressBack());
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

    @Test
    public void navigation() {
        openDrawer(R.id.drawer_layout);
        onView(withText(R.string.nav_folder)).perform(click());
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
    }

    @Test
    public void checkButtonBackFromModel() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withText("Model T.O.T.E.")).perform(click());
        onView(withText("Name:")).check(matches(isDisplayed()));
        onView(withText("Name:")).perform(pressBack());
    }
}