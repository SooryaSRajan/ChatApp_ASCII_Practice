package com.ssr_projects.asciichatapp.Models;

import androidx.fragment.app.Fragment;

public class HomePageFragmentModel {

    Fragment fragment;
    String title;

    public HomePageFragmentModel(Fragment fragment, String title) {
        this.fragment = fragment;
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public String getTitle() {
        return title;
    }
}
