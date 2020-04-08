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
    private String mNeighbourName; // for toolbar and across whole class AboutSingleNeighbourActivity
    private NeighbourApiService mApiService = DI.getNeighbourApiService();

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

    /**
     * Fill out the elements of the activity has present the detail about single neighbour
     * his:
     * mNeighbourToolbarImageView: avatar added by Glide(android extension)
     * mNeighbourName: name displayed in toolbar and in first ViewCard
     * physical address
     * phone number
     * facebook address
     * description about him self
     */
    void configureDesign() {
        // fill
        mNeighbourName = getIntent().getStringExtra(EXTRA_NEIGHBOUR);
        List<Neighbour> neighbours = mApiService.getNeighbours();
        for (Neighbour n : neighbours) {
            if(n.getName().equals(mNeighbourName)) mNeighbour = n;
        }

        // style for CardView: add corner Radius
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

    /**
     * Click to switch between favorite or non favorite state of neighbour
     * @param view not used
     */
    @Override
    @OnClick(R.id.ab_neighbour_ic_favorite)
    public void onClick(View view) {
        if (!mNeighbour.isFavorite()) {
            mApiService.favoriteStateOfNeighbour(mNeighbour,true);
        } else {
            mApiService.favoriteStateOfNeighbour(mNeighbour,false);
        }
        setFloatActiveButtonIcon();
    }

    /**
     * Manage the FloatingActionButton with sign of the favorite
     *
     * empty star if is not a favorite neighbour
     * full star if it is a favorite neighbour
     */
    void setFloatActiveButtonIcon() {
        FloatingActionButton mFloatActionButton = findViewById(R.id.ab_neighbour_ic_favorite);
        if (mNeighbour.isFavorite()) {
            mFloatActionButton.setImageResource(R.drawable.ic_favorite);
        } else {
            mFloatActionButton.setImageResource(R.drawable.ic_no_favorite);
        }
    }
}
