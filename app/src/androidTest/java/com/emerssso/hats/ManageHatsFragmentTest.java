package com.emerssso.hats;

import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.espresso.util.TreeIterables;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class ManageHatsFragmentTest {

    public static final String HAT_NAME_1 = "Hat1";
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Perform action of waiting for a specific view id.
     */
    public static ViewAction waitText(final String waitTextId, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for a specific view with string <" + waitTextId + "> during " + millis + " millis.";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;
                final Matcher<View> viewMatcher = withText(waitTextId);

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            return;
                        }
                    }

                    uiController.loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);

                // timeout happens
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }

    @Test
    public void testShouldAddHatToList() throws Exception {
        onView(withId(R.id.add_hat))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withText(R.string.dialog_add_hat_instructions))
                .check(matches(isDisplayed()));

        onView(withId(R.id.field_hat_name))
                .check(matches(isDisplayed()))
                .perform(typeText(HAT_NAME_1));

        onView(withText(R.string.save_caps))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withText(R.string.dialog_add_hat_instructions))
                .check(doesNotExist());

        onView(isRoot()).perform(waitText(HAT_NAME_1, TimeUnit.SECONDS.toMillis(5)));

        onView(withId(R.id.hats_list))
                .check(matches(isDisplayed()))
                .check(matches(hasDescendant(withText(HAT_NAME_1))));
    }
}
