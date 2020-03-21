package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.events.DeleteNeighbourEvent;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class NeighbourFragment extends Fragment implements MyNeighbourRecyclerViewAdapter.OnNeighbourListener {

    private NeighbourApiService mApiService;
    private List<Neighbour> mNeighbours;
    private RecyclerView mRecyclerView;
    private static final String KEY_POSITION = "position";
    private static final String TAG = "___###___NeighbourFrag";


    /**
     * Create and return a new instance
     *
     * @return @{@link NeighbourFragment}
     */
    public static NeighbourFragment newInstance(int position) {
        NeighbourFragment fragment = new NeighbourFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiService = DI.getNeighbourApiService();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_neighbour_list, container, false);
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL));

        int position = Objects.requireNonNull(getArguments()).getInt(KEY_POSITION, 0);
        if (position == 0) {
            mNeighbours = mApiService.getNeighbours();
        } else {
            mNeighbours = getFavoritesNeighboursList(mApiService.getNeighbours());
        }
        return view;
    }

    /**
     * Init the List of neighbours
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initList() {

        mRecyclerView.setAdapter(new MyNeighbourRecyclerViewAdapter(mNeighbours, this));
    }

    /** @function getFavoritesNeighboursList filter the Neighbours global list by favorites */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<Neighbour> getFavoritesNeighboursList(List<Neighbour> neighbours) {
        return neighbours.stream()
                .filter(Neighbour::isFavorite).collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        initList();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * @param event Fired if the user clicks on a delete button
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Subscribe
    public void onDeleteNeighbour(DeleteNeighbourEvent event) {
        mApiService.deleteNeighbour(event.neighbour);
        initList();
    }

    @Override
    public void OnNeighbourClick(int position) {
        Log.d(TAG, "OnNeighbourClick: " + mNeighbours.get(position).getName());
        Intent intent = new Intent(getActivity(), AboutSingleNeighbourActivity.class);
        startActivity(intent);
    }
}
