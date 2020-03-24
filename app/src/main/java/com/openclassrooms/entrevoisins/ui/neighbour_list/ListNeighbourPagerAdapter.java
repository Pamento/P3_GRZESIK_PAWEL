package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class ListNeighbourPagerAdapter extends FragmentPagerAdapter {


    ListNeighbourPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * getItem is called to instantiate the fragment for the given page.
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        if (position == 0) return NeighbourFragment.newInstance(position);
        else return NeighbourFragment.newInstance(position);
    }

    /**
     * get the number of pages
     * @return
     */
    @Override
    public int getCount() {
        return 2;
    }

//    @Override
//    public void notifyDataSetChanged() {
//        super.notifyDataSetChanged();
//    }
//
//    @Override
//    public int getItemPosition(@NonNull Object object) {
//        Log.d(TAG, "getItemPosition: ");
//        //return super.getItemPosition(object);
//        return POSITION_NONE;
//    }
}