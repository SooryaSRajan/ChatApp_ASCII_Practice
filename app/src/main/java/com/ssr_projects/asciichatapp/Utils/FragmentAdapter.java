package com.ssr_projects.asciichatapp.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;

import com.ssr_projects.asciichatapp.Models.HomePageFragmentModel;

import java.util.ArrayList;

public class FragmentAdapter extends FragmentStatePagerAdapter {

    ArrayList<HomePageFragmentModel> homePageFragmentModelArrayList;

    public FragmentAdapter(@NonNull FragmentManager fm, int behavior, ArrayList<HomePageFragmentModel> homePageFragmentModelArrayList) {
        super(fm, behavior);
        this.homePageFragmentModelArrayList = homePageFragmentModelArrayList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return homePageFragmentModelArrayList.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return homePageFragmentModelArrayList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return homePageFragmentModelArrayList.get(position).getTitle();

    }
}
