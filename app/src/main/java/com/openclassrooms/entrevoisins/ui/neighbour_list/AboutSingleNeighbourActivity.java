package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutSingleNeighbourActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.ab_neighbour_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.ab_neighbour_about_image)
    ImageView mNeighbourToolbarImageView;
    @BindView(R.id.ab_neighbour_coordinate_title)
    TextView mNeighbourCoordinateTitleName;
    @BindView(R.id.ab_neighbour_coordinate_adr)
    TextView mNeighbourAddress;
    @BindView(R.id.ab_neighbour_coordinate_phone)
    TextView mNeighbourPhone;
    @BindView(R.id.ab_neighbour_coordinate_web)
    TextView mNeighbourWebLink;
    @BindView(R.id.ab_neighbour_about_descript)
    TextView mNeighbourAboutTextView;
    @BindView(R.id.ab_neighbour_coordinate_cardV) CardView mCardView1;
    @BindView(R.id.ab_neighbour_about_app_bar_cardV) CardView mCardView2;

    private Neighbour mNeighbour;
    public static final String EXTRA_NEIGHBOUR = "com.openclassrooms.entrevoisins.ui.neighbour_list.EXTRA_NEIGHBOUR";
    String mNeighbourName; // for toolbar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_single_neighbour);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.configureDesign();
    }

    void configureDesign() {
        // fill
        mNeighbourName = getIntent().getStringExtra(EXTRA_NEIGHBOUR);
        NeighbourApiService mApiService = DI.getNeighbourApiService();
        List<Neighbour> neighbours = mApiService.getNeighbours();
        for (Neighbour n : neighbours) {
            if(n.getName().equals(mNeighbourName)) mNeighbour = n;
        }
        //mNeighbour = neighbours.get(getNeighbour);
        //mNeighbourName = mNeighbour.getName();

        mCardView1.setRadius(12);
        mCardView2.setRadius(12);
        Glide.with(this).load(mNeighbour.getAvatarUrl())
                .centerCrop()
                .error(R.drawable.ic_account)
                .into(mNeighbourToolbarImageView);
        // Set Neighbour name in toolbar
        Objects.requireNonNull(this.getSupportActionBar()).setTitle(mNeighbourName);
        setFloatActiveButtonIcon();
        this.mNeighbourCoordinateTitleName.setText(mNeighbourName);
        this.mNeighbourAddress.setText(mNeighbour.getAddress());
        this.mNeighbourPhone.setText(mNeighbour.getPhoneNumber());
        String facebookLing = getString(R.string.facebook_link, mNeighbourName.toLowerCase());
        this.mNeighbourWebLink.setText(facebookLing);
        this.mNeighbourAboutTextView.setText(mNeighbour.getAboutMe());
    }

    @Override
    @OnClick(R.id.ab_neighbour_ic_favorite)
    public void onClick(View view) {
        if (!mNeighbour.isFavorite()) {
            mNeighbour.setFavorite(true);
        } else {
            mNeighbour.setFavorite(false);
        }
        setFloatActiveButtonIcon();
    }

    void setFloatActiveButtonIcon() {
        FloatingActionButton mFloatActionButton = findViewById(R.id.ab_neighbour_ic_favorite);
        if (mNeighbour.isFavorite()) {
            mFloatActionButton.setImageResource(R.drawable.ic_favorite);
        } else {
            mFloatActionButton.setImageResource(R.drawable.ic_no_favorite);
        }
    }
}
