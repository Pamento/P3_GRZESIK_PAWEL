package com.openclassrooms.entrevoisins.neighbour_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity;
import com.openclassrooms.entrevoisins.ui.neighbour_list.MyNeighbourRecyclerViewAdapter;
import com.openclassrooms.entrevoisins.utils.DeleteViewAction;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.nthChildOf;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.RecyclerViewMatcher;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Test class for List of favorites neighbours
 */
@RunWith(AndroidJUnit4.class)
public class NeighboursFavoritesListTest {

    /** the Activity of the Target application */
    private ListNeighbourActivity mActivity;

    /** the {@link RecyclerView}'s resource id */
    private int resId = R.id.list_neighbours;

    /** the {@link RecyclerView} */
    private RecyclerView mRecyclerView;

    /** and it's item count */
    private int itemCount = 0;

    /**
     * such a {@link ActivityTestRule} can be used eg. for Intent.putExtra(),
     * alike one would pass command-line arguments to regular run configurations.
     * this code runs before the {@link com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourFragment} is being started.
     **/
    @Rule
    public ActivityTestRule<ListNeighbourActivity> mActivityRule = new ActivityTestRule<ListNeighbourActivity>(ListNeighbourActivity.class) {

        @Override
        protected Intent getActivityIntent() {
            Intent intent = new Intent();
            Bundle extras = new Bundle();
            intent.putExtras(extras);
            return intent;
        }
    };

    @Before
    public void setUpTest() {

        /* obtaining the Activity from the ActivityTestRule */
        this.mActivity = this.mActivityRule.getActivity();

        /* obtaining handles to the Ui of the Activity */
        this.mRecyclerView = this.mActivity.findViewById(this.resId);
        this.itemCount = this.mRecyclerView.getAdapter().getItemCount();
    }

    @Test
    public void RecyclerViewTest() {
        if(this.itemCount > 0) {
            for(int i=0; i < this.itemCount; i++) {

                /* clicking the item */
                onView(withId(this.resId))
                        .perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));

                /* check if the ViewHolder is being displayed */
                onView(new RecyclerViewMatcher(this.resId)
                        .atPosition(i))
                        .check(matches(isDisplayed()));

                /* checking for the text of the first one item */
                if(i == 0) {
                    onView(new RecyclerViewMatcher(this.resId)
                            .atPosition(0))
                            .check(matches(withText("Caroline")));
                }

            }
        }
    }
}
