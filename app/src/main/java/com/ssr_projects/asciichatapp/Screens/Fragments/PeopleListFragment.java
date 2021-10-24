package com.ssr_projects.asciichatapp.Screens.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssr_projects.asciichatapp.R;


public class PeopleListFragment extends Fragment {

    public PeopleListFragment() {
        // Required empty public constructor
    }

    public static PeopleListFragment newInstance() {
        return new PeopleListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_people_list, container, false);
    }
}