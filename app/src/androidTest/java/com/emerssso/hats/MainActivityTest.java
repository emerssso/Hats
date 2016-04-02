package com.emerssso.hats;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testShouldShowAppTitle() throws Exception {
        onView(withText("Hats"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testShouldHavePagerWithTwoChildren() throws Exception {
        onView(withId(R.id.tab_layout))
                .check(matches(hasDescendant(withText(R.string.fragment_title_hats))))
                .check(matches(hasDescendant(withText(R.string.fragment_title_history))));

        onView(withText(R.string.fragment_title_hats))
                .check(matches(isSelected()));

        onView(withId(R.id.view_pager))
                .check(matches(hasDescendant(withId(R.id.add_hat))))
                .perform(swipeLeft())
                .check(matches(hasDescendant(withText(R.string.coming_soon))));

        onView(withText(R.string.fragment_title_history))
                .check(matches(isSelected()));
    }
}
