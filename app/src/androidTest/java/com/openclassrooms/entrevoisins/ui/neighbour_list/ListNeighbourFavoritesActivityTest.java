package com.openclassrooms.entrevoisins.ui.neighbour_list;


import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewPickUpItem.childAtPosition;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNull.notNullValue;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class ListNeighbourFavoritesActivityTest {

    private final int mPosition = 0;
    private String mNeighbourName;
    @Rule
    public ActivityTestRule<ListNeighbourActivity> mActivityTestRule = new ActivityTestRule<>(ListNeighbourActivity.class);


    @Before
    public void setUp() {
        ListNeighbourActivity activity = mActivityTestRule.getActivity();
        assertThat(activity, notNullValue());

        // Setup ApiService for do some test on Favorites list of neighbours.
        NeighbourApiService mApiService = DI.getNeighbourApiService();
        List<Neighbour> neighbours = mApiService.getNeighbours();
        mNeighbourName = neighbours.get(mPosition).getName();
    }

    /**
     * Check if TextView on AboutSingleNeighbourActivity display name of neighbour
     */
    @Test
    public void listNeighbourFavoritesActivityTest() {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.list_neighbours),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(mPosition, click()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.ab_neighbour_coordinate_title), withText(mNeighbourName),
                        childAtPosition(
                                allOf(withId(R.id.ab_neighbour_coordinate_layout),
                                        childAtPosition(
                                                withId(R.id.ab_neighbour_coordinate_cardV),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText(mNeighbourName)));
    }
}
