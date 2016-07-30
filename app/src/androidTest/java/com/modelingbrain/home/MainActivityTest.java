package com.modelingbrain.home;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.modelingbrain.home.model.ContentManagerModel;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    // TODO: 7/30/16 create automatic test for all version of android
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
    public void sizeOfModels() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).check(matches(allOf(
                isDisplayed()
        )));
        onView(withId(R.id.recycler_view))
                .perform( RecyclerViewActions
                        .actionOnItemAtPosition(ContentManagerModel.getListChooseModel().size()-1, click()));
        onView(withText("Name:")).perform(pressBack());
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