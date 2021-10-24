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

import static com.ssr_projects.asciichatapp.Utils.Constants.DATE;
import static com.ssr_projects.asciichatapp.Utils.Constants.FRIENDS_NODE;
import static com.ssr_projects.asciichatapp.Utils.Constants.LAST_MESSAGE;
import static com.ssr_projects.asciichatapp.Utils.Constants.RECEIVER_ID;
import static com.ssr_projects.asciichatapp.Utils.Constants.RECEIVER_NAME;
import static com.ssr_projects.asciichatapp.Utils.Constants.USER_NAME_NODE;
import static com.ssr_projects.asciichatapp.Utils.Constants.USER_NODE;


public class ChatsListFragment extends Fragment {

    RecyclerView recyclerView;
    UserListRecyclerViewAdapter adapter;
    ArrayList<UserModel> userModelArrayList;

    private final String TAG = getClass().getName();
    DatabaseReference databaseReference;
    DatabaseReference userDataReference;

    ValueEventListener valueEventListener;
    ValueEventListener userProfileEventListener;

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
        View view = inflater.inflate(R.layout.fragment_chats_list, container, false);

        databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(USER_NODE)
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .child(FRIENDS_NODE);

        userDataReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(USER_NODE);

        Log.d(TAG, "Chats references: " + databaseReference);

        databaseReference.keepSynced(true);

        userModelArrayList = new ArrayList<>();
        adapter = new UserListRecyclerViewAdapter(userModelArrayList, getActivity());

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        if (userProfileEventListener == null) {
            userProfileEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d(TAG, "onDataChange: User data: " + snapshot);
                    UserModel userModel = new UserModel();

                    String name = (String) snapshot.child(USER_NAME_NODE).getValue();
                    String userId = snapshot.getKey();
                    String date = (String) snapshot.child(DATE).getValue();
                    String lastMessage = (String) snapshot.child(LAST_MESSAGE).getValue();

                    if (date != null) {
                        userModel.setDate(date);
                    } else {
                        userModel.setDate("");
                    }

                    if (lastMessage != null) {
                        userModel.setLastMessage(lastMessage);
                    } else {
                        userModel.setLastMessage("");
                    }

                    if (name != null) {
                        userModel.setUserName(name);
                    } else {
                        userModel.setUserName("");
                    }

                    if (userId != null) {
                        userModel.setUserId(userId);
                    } else {
                        userModel.setUserId("");
                    }

                    userModelArrayList.add(userModel);
                    adapter.notifyItemInserted(userModelArrayList.size() - 1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
        }

        if (valueEventListener == null) {
            valueEventListener = new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userModelArrayList.clear();
                    adapter.notifyDataSetChanged();

                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Log.d(TAG, "onDataChange: Loop " + snap.getKey());
                        userDataReference.child(Objects.requireNonNull(snap.getKey()))
                                .addListenerForSingleValueEvent(userProfileEventListener);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
        }

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