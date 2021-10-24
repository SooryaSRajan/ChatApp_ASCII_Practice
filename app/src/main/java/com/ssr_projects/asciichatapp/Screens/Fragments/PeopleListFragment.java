package com.ssr_projects.asciichatapp.Screens.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ssr_projects.asciichatapp.Models.UserModel;
import com.ssr_projects.asciichatapp.R;
import com.ssr_projects.asciichatapp.Utils.UserListRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Objects;

import static com.ssr_projects.asciichatapp.Utils.Constants.USER_NAME_NODE;
import static com.ssr_projects.asciichatapp.Utils.Constants.USER_NODE;


public class PeopleListFragment extends Fragment {

    RecyclerView recyclerView;
    UserListRecyclerViewAdapter adapter;
    ArrayList<UserModel> userModelArrayList;

    private final String TAG = getClass().getName();
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;

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
        View view = inflater.inflate(R.layout.fragment_people_list, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child(USER_NODE);
        databaseReference.keepSynced(true);

        userModelArrayList = new ArrayList<>();
        adapter = new UserListRecyclerViewAdapter(userModelArrayList, getActivity());

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        if (valueEventListener == null)
            valueEventListener = new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d(TAG, "onDataChange: " + snapshot);
                    userModelArrayList.clear();
                    adapter.notifyDataSetChanged();
                    for (DataSnapshot snap : snapshot.getChildren()) {

                        Log.d(TAG, "onDataChange: " + snap);
                        Log.d(TAG, "onDataChange: " + snap.child(USER_NAME_NODE).getValue());
                        Log.d(TAG, "onDataChange: " + snap.getKey());

                        UserModel userModel = new UserModel();

                        if (!Objects.equals(snap.getKey(), FirebaseAuth.getInstance().getUid())) {
                            Log.d(TAG, "onDataChange Matched: " + snap);
                            userModel.setUserId(snap.getKey());
                            userModel.setUserName((String) snap.child(USER_NAME_NODE).getValue());
                            userModel.setLastMessage("");
                            userModel.setDate("");

                            userModelArrayList.add(userModel);
                            adapter.notifyItemInserted(userModelArrayList.size() - 1);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };


        databaseReference.addValueEventListener(valueEventListener);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (valueEventListener != null) {
            databaseReference.removeEventListener(valueEventListener);
        }

        Log.d(TAG, "onDestroyView: View Destroyed");
    }
}