
package com.openclassrooms.entrevoisins.neighbour_list;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.contrib.ViewPagerActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.ui.neighbour_list.AboutSingleNeighbourActivity;
import com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity;
import com.openclassrooms.entrevoisins.utils.DeleteViewAction;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.nthChildOf;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.core.IsNull.notNullValue;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.RecyclerViewMatcher;


/**
 * Test class for list of neighbours
 */
@RunWith(AndroidJUnit4.class)
public class NeighboursListTest {

    // This is fixed
    private static int ITEMS_COUNT = 12;
    /**
     * the {@link }'s resource id
     */
    private final int resId = R.id.list_neighbours;

    private int FAVORITES_COUNT;
    private List<Neighbour> mFavoritesList;


    @Rule
    public ActivityTestRule<ListNeighbourActivity> mActivityRule =
            new ActivityTestRule(ListNeighbourActivity.class);

    @Before
    public void setUp() {
        ListNeighbourActivity activity = mActivityRule.getActivity();
        assertThat(activity, notNullValue());
        // Setup ApiService for do some test on Favorites list of neighbours.
        NeighbourApiService mApiService = DI.getNeighbourApiService();
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
//        onView(nthChildOf(withId(R.id.container), 1))
//                .check(matches(isDisplayed()));

        onView(withId(R.id.container))
                .perform(ViewPagerActions.scrollRight());

        onView(allOf(withId(this.resId), isDisplayed()))
                .check(withItemCount(FAVORITES_COUNT));



        for (int i = 0; i < FAVORITES_COUNT; i++) {
//            onView( nthChildOf(allOf(withId(R.id.list_neighbours)), i))
//            .check(matches(withText(mFavoritesList.get(i).getName())));
//            if(i == 0) {
//                onView(new RecyclerViewMatcher(allOf(withId(this.resId)))
//                        .atPosition(i))
//                        .check(matches(withText("Caroline")));
//            }
            String neighbourName = mFavoritesList.get(i).getName();
//            onView(withText(neighbourName)).check(matches(isDisplayed()));
//            onView(allOf(withId(this.resId), isDisplayed()))
//                    .check(matches((Matcher<? super View>) neighbourName()));
            onView( allOf(withId(this.resId), isDisplayed()))
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
//
//    private ViewAssertion neighbourName() {
//        int count = 0;
//        String neighbourName = mFavoritesList.get(count).getName();
//        count++;
//        return neighbourName;
//    }
public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
//    checkNotNull(itemMatcher);
    return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
        @Override
        public void describeTo(Description description) {
            description.appendText("has item at position " + position + ": ");
            itemMatcher.describeTo(description);
        }

        @Override
        protected boolean matchesSafely(final RecyclerView view) {
            RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
            if (viewHolder == null) {
                // has no item on such position
                return false;
            }
            return itemMatcher.matches(viewHolder.itemView);
        }
    };
}
}