package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 * Unit test on Neighbour service
 */
@RunWith(JUnit4.class)
public class NeighbourServiceTest {

    private NeighbourApiService service;

    @Before
    public void setup() {
        service = DI.getNewInstanceApiService();
    }

    @Test
    public void getNeighboursWithSuccess() {
        List<Neighbour> neighbours = service.getNeighbours();
        List<Neighbour> expectedNeighbours = DummyNeighbourGenerator.DUMMY_NEIGHBOURS;
        assertThat(neighbours, IsIterableContainingInAnyOrder.containsInAnyOrder(Objects.requireNonNull(expectedNeighbours.toArray())));
    }

    @Test
    public void deleteNeighbourWithSuccess() {
        Neighbour neighbourToDelete = service.getNeighbours().get(0);
        service.deleteNeighbour(neighbourToDelete);
        assertFalse(service.getNeighbours().contains(neighbourToDelete));
    }

    @Test
    public void getFavoritesNeighboursWithSuccess() {
        List<Neighbour> favorites = service.getFavoritesNeighbours();
        assertThat(favorites, IsIterableContainingInAnyOrder.containsInAnyOrder(Objects.requireNonNull(DummyNeighbourGenerator.DUMMY_NEIGHBOURS.stream()
                .filter(Neighbour::isFavorite).toArray())));
    }

    @Test
    public void changeFavoriteState() {
        // Get first neighbour form list
        boolean neighbour1 = service.getNeighbours().get(0).isFavorite();
        String neighbour1Str = Boolean.toString(neighbour1);

        // Change the favorite state of this neighbour via Service function
        service.favoriteStateOfNeighbour(service.getNeighbours().get(0), false);

        // Get again the same neighbour
        boolean neighbour2 = service.getNeighbours().get(0).isFavorite();
        String neighbour2Str = Boolean.toString(neighbour2);

        // Must not equal the neighbor state of its first state
        assertThat(neighbour2Str, is(not(neighbour1Str)));
    }
}
