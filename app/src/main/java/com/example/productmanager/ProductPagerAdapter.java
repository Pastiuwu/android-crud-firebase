package com.example.productmanager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ProductPagerAdapter extends FragmentPagerAdapter {

    private final int numOfTabs;

    public ProductPagerAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ProductListFragment();
        } else {
            return new ProductCreateFragment();
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
