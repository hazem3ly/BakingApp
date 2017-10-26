package com.example.android.bakingapp.fragments;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.test.TestActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertNotNull;


@RunWith(AndroidJUnit4.class)
public class MainFragmentTest {

    @Rule
    public ActivityTestRule<TestActivity> activityTestRule =
            new ActivityTestRule<>(TestActivity.class);


    private TestActivity testActivity;

    private IdlingResource mIdlingResource;
    private MainFragment mainFragment;


    @Before
    public void registerIdlingResource() {
// get reference to temp activity
        testActivity = activityTestRule.getActivity();
        // find container view to add fragment in
        FrameLayout frameLayout = (FrameLayout) testActivity.findViewById(R.id.container);
        assertNotNull(frameLayout);

        mainFragment = new MainFragment();
        // start fragment transaction
        testActivity.getSupportFragmentManager().beginTransaction()
                .add(frameLayout.getId(), mainFragment).commitAllowingStateLoss();

        // geting reference to idilingresource object
        mIdlingResource = mainFragment.getIdlingResource();
        // Registers any resource that needs to be synchronized with Espresso
        Espresso.registerIdlingResources(mIdlingResource);

    }

    @Test
    public void idlingResourceTest() {

        // Added a sleep statement to match the app's execution delay.
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        View mainFragmentView = mainFragment.getView();
        RecyclerView recyclerView = mainFragmentView.findViewById(R.id.recipe_recycler_view);
        assertNotNull(recyclerView);

        if (recyclerView.getAdapter().getItemCount() > 0) {
            onView(withId(recyclerView.getId())).check(matches(isDisplayed()));
            onView(withId(recyclerView.getId())).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        } else if (recyclerView.getVisibility() == View.GONE){
            TextView errorText = mainFragmentView.findViewById(R.id.no_data);
            onView(withId(errorText.getId())).check(matches(isDisplayed()));
            onView(withId(errorText.getId())).check(matches(withText(R.string.connection_error)));
        }


    }

    // Remember to unregister resources when not needed to avoid malfunction.
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

}