package com.openclassrooms.entrevoisins.service;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.openclassrooms.entrevoisins.model.Neighbour;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Dummy mock for the Api
 */
public class DummyNeighbourApiService implements  NeighbourApiService {

    private List<Neighbour> neighbours = DummyNeighbourGenerator.generateNeighbours();


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Neighbour> getNeighbours() {
        return neighbours;
    }
    /**
     * {@inheritDoc}
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Neighbour> getFavoritesNeighbours() {
        return neighbours.stream()
                .filter(Neighbour::isFavorite).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     * @param neighbour the single neighbour form list for witch one must by changed
     *                  his favorite state
     */
    @Override
    public void favoriteStateOfNeighbour(Neighbour neighbour, boolean isFavorite) {
        for (Neighbour n: neighbours) {
            if (n.getId() == neighbour.getId()) {
                n.setFavorite(isFavorite);
            }
        }
        System.out.println("######################################################");
        System.out.println("neighbour__ID__"+neighbour.getId());
        System.out.println("######################################################");
    }

    /**
     * {@inheritDoc}
     * @param neighbour
     */
    @Override
    public void deleteNeighbour(Neighbour neighbour) {
        neighbours.remove(neighbour);
    }

    /**
     * {@inheritDoc}
     * @param neighbour
     */
    @Override
    public void createNeighbour(Neighbour neighbour) {
        neighbours.add(neighbour);
    }
}
