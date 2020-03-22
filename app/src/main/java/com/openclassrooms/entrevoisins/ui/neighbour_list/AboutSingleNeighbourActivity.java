package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutSingleNeighbourActivity extends AppCompatActivity {

    @BindView(R.id.ab_neighbour_about_image) ImageView mNeighbourToolbarImageView;
    //@BindView(R.id.ab_neighbour_ic_favorite) Button mIsFavorite;
    @BindView(R.id.ab_neighbour_coordinate_title) TextView mNeighbourCoordinateTitleName;
    @BindView(R.id.ab_neighbour_coordinate_adr) TextView mNeighbourAddress;
    @BindView(R.id.ab_neighbour_coordinate_phone) TextView mNeighbourPhone;
    @BindView(R.id.ab_neighbour_coordinate_web) TextView mNeighbourWebLink;
    @BindView(R.id.ab_neighbour_about_descript) TextView mNeighbourAboutTextView;

    private NeighbourApiService mApiService;
    private List<Neighbour> mNeighbours;
    String mNeighbourName; // for toolbar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_single_neighbour);
        ButterKnife.bind(this);
        mApiService = DI.getNeighbourApiService();
        Toolbar toolbar = (Toolbar) findViewById(R.id.ab_neighbour_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
