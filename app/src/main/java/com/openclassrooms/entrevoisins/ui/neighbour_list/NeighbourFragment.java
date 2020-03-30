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


public class NeighbourFragment extends Fragment implements MyNeighbourRecyclerViewAdapter.OnNeighbourListener {

    private NeighbourApiService mApiService;
    private List<Neighbour> mNeighbours;
    private RecyclerView mRecyclerView;
    private static final String KEY_POSITION = "position";

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
        // for each position is created new instance of NeighbourFragment
        int position = Objects.requireNonNull(getArguments()).getInt(KEY_POSITION, 0);
        initList(position);
        return view;
    }

    /**
     * Init the List of neighbours
     * and Refresh in case of change like Delete action.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initList(int position) {
        if (position == 0) {
            mNeighbours = mApiService.getNeighbours();
            mRecyclerView.setAdapter(new MyNeighbourRecyclerViewAdapter(mNeighbours, this));
        } else {
            mNeighbours = mApiService.getFavoritesNeighbours();
            mRecyclerView.setAdapter(new MyNeighbourRecyclerViewAdapter(mNeighbours, this));
        }
    }

    /**
     * It's the getter of number of page in ViewPager for make update of List after receive the change.
     * Added to page in during instantiation of fragment.
     * @return integer, the number of page.
     */
    private int getPageFromViewPager() {
        NeighbourFragment nf = this;
        int page = 0;
        if (nf.getArguments() != null) page = nf.getArguments().getInt(KEY_POSITION);
        return page;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        int pos = getPageFromViewPager();
        initList(pos);
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
        int pos = getPageFromViewPager();
        initList(pos);
    }

    /** Add the ClickListener event on each single Neighbour view in RecyclerView for both list
     * global and favorites
     * @param position give the position in the NeighboursList RecyclerView
     */
    @Override
    public void OnNeighbourClick(int position) {
        Intent intent = new Intent(getActivity(), AboutSingleNeighbourActivity.class);
        intent.putExtra(AboutSingleNeighbourActivity.EXTRA_NEIGHBOUR, mNeighbours.get(position).getName());
        startActivity(intent);
    }
}
