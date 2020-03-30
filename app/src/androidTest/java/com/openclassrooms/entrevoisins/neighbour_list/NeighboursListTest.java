
package com.openclassrooms.entrevoisins.neighbour_list;

import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.contrib.ViewPagerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.ui.neighbour_list.AboutSingleNeighbourActivity;
import com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity;
import com.openclassrooms.entrevoisins.utils.DeleteViewAction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewEachItemAssertion.atPosition;


/**
 * Test class for list of neighbours
 */
@RunWith(AndroidJUnit4.class)
public class NeighboursListTest {

    private int ITEMS_COUNT;
    /**
     * the {@link android.support.v7.widget.RecyclerView}'s resource id
     */
    private final int resId = R.id.list_neighbours;

    private int FAVORITES_COUNT;
    private List<Neighbour> mFavoritesList;


    @Rule
    public ActivityTestRule<ListNeighbourActivity> mActivityRule = new ActivityTestRule<>(ListNeighbourActivity.class);

    @Before
    public void setUp() {
        ListNeighbourActivity activity = mActivityRule.getActivity();
        assertThat(activity, notNullValue());
        // Setup ApiService for do some test on Favorites list of neighbours.
        NeighbourApiService mApiService = DI.getNeighbourApiService();
        this.ITEMS_COUNT = mApiService.getNeighbours().size();
        this.mFavoritesList = mApiService.getFavoritesNeighbours();
        this.FAVORITES_COUNT = mFavoritesList.size();
    }

    /**
     * We ensure that our recyclerview is displaying at least on item
     */
    @Test
    public void myNeighboursList_shouldNotBeEmpty() {
        // First scroll to the position that needs to be matched and click on it.
        onView(allOf(withId(this.resId), isDisplayed()))
                .check(matches(hasMinimumChildCount(1)));
    }

    /**
     * When we delete an item, the item is no more shown
     */
    @Test
    public void myNeighboursList_deleteAction_shouldRemoveItem() {
        // Given : We remove the element at position 2
        onView(allOf(withId(this.resId), isDisplayed()))
                .check(withItemCount(ITEMS_COUNT));
        // When perform a click on a delete icon
        onView(allOf(withId(this.resId), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));
        // Then : the number of element is 11
        onView(allOf(withId(this.resId), isDisplayed()))
                .check(withItemCount(ITEMS_COUNT - 1));
    }

    /**
     * We ensure that our List of favorites neighbours has only the neighbour checked as favorites
     */
    @Test
    public void myNeighboursFavoritesList_has_only_favorites() {
        // Get second view from pagerView, but it's not work has wanted.
        onView(withId(R.id.container))
                .perform(ViewPagerActions.scrollRight());

        onView(allOf(withId(this.resId), isDisplayed()))
                .check(withItemCount(FAVORITES_COUNT));
        // For each element of the list of favorites neighbours check if match that one who is displayed
        for (int i = 0; i < FAVORITES_COUNT; i++) {
            String neighbourName = mFavoritesList.get(i).getName();
            onView(allOf(withId(this.resId), isDisplayed()))
                    .perform(RecyclerViewActions.scrollToPosition(i))
                    .check(matches(atPosition(i, hasDescendant(withText(neighbourName)))));
        }
    }

    /**
     * Check if second activity: AboutSingleNeighbourActivity is launched after click on item
     * from RecyclerView list
     */
    @Test
    public void myNeighboursList_onClickItem_shouldOpen_newActivity() {
        // Init espresso extension for checking the other activity (start register).
        Intents.init();

        // Click on item of RecyclerView.
        onView(allOf(withId(R.id.list_neighbours), isDisplayed()))
                .perform(click());
        // Check if other activity has launched.
        intended(hasComponent(AboutSingleNeighbourActivity.class.getName()));
        // Check BackUp button to go back on the list
        pressBack();
        // Stop register of intents.
        Intents.release();
    }
}