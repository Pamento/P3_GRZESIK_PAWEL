package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.events.DeleteNeighbourEvent;
import com.openclassrooms.entrevoisins.model.Neighbour;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyNeighbourRecyclerViewAdapter extends RecyclerView.Adapter<MyNeighbourRecyclerViewAdapter.ViewHolder> {

    private final List<Neighbour> mNeighbours;
    private OnNeighbourListener mOnNeighbourListener;

    MyNeighbourRecyclerViewAdapter(List<Neighbour> items, OnNeighbourListener onNeighbourListener) {
        mNeighbours = items;
        mOnNeighbourListener = onNeighbourListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_neighbour, parent, false);
        return new ViewHolder(view, mOnNeighbourListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Neighbour neighbour = mNeighbours.get(position);
        holder.mNeighbourName.setText(neighbour.getName());
        Glide.with(holder.mNeighbourAvatar.getContext())
                .load(neighbour.getAvatarUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.mNeighbourAvatar);

        holder.mDeleteButton.setOnClickListener(v -> EventBus.getDefault().post(new DeleteNeighbourEvent(neighbour)));
    }

    @Override
    public int getItemCount() {
        return mNeighbours.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.item_list_avatar)
        public ImageView mNeighbourAvatar;
        @BindView(R.id.item_list_name)
        public TextView mNeighbourName;
        @BindView(R.id.item_list_delete_button)
        public ImageButton mDeleteButton;
        OnNeighbourListener mOnNeighbourListener;

        /**
         * Add onClickListener on single view
         *
         * @param view of ViewHolder, single item on RecyclerView( NeighboursList)
         */
        ViewHolder(View view, OnNeighbourListener onNeighbourListener) {
            super(view);
            ButterKnife.bind(this, view);
            mOnNeighbourListener = onNeighbourListener;
            view.setOnClickListener(this);
        }

        /**
         * implements the @method OnClickListener and logic to do after click
         *
         * @param view not used
         */
        @Override
        public void onClick(View view) {
            mOnNeighbourListener.OnNeighbourClick(getAdapterPosition());
        }
    }

    /**
     * @interface OnNeighbourListener
     * interface for communication with Fragment with RecyclerView, whole list of neighbours
     */
    public interface OnNeighbourListener {
        void OnNeighbourClick(int position);
    }
}
