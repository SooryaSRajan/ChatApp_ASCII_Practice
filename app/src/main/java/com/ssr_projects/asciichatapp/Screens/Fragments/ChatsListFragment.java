package com.ssr_projects.asciichatapp.Screens.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssr_projects.asciichatapp.R;


public class ChatsListFragment extends Fragment {

    public ChatsListFragment() {
    }


    public static ChatsListFragment newInstance() {
        return new ChatsListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chats_list, container, false);
    }
}