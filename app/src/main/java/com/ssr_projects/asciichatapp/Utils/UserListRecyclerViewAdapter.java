package com.ssr_projects.asciichatapp.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssr_projects.asciichatapp.Models.UserModel;
import com.ssr_projects.asciichatapp.R;

import java.util.ArrayList;

public class UserListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<UserModel> userModels;
    private static final String TAG = UserListRecyclerViewAdapter.class.getName();
    Activity activity;

    public UserListRecyclerViewAdapter(ArrayList<UserModel> userModels, Activity activity) {
        this.userModels = userModels;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new dataHolder(inflater.inflate(R.layout.user_card, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String userName = userModels.get(position).getUserName();
        String lastMessage = userModels.get(position).getLastMessage();
        String date = userModels.get(position).getDate();

        ((dataHolder) holder).userName.setText(userName);

        if (lastMessage.isEmpty()) {
            ((dataHolder) holder).date.setVisibility(View.GONE);
        } else {
            ((dataHolder) holder).date.setText(date);
        }

        if (lastMessage.isEmpty()) {
            ((dataHolder) holder).lastMessage.setText("Click to chat with " + userName);
        } else {
            ((dataHolder) holder).lastMessage.setText(lastMessage);
        }
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    public static class dataHolder extends RecyclerView.ViewHolder {

        TextView userName;
        TextView date;
        TextView lastMessage;
        ImageView profilePicture;

        public dataHolder(View inflate) {
            super(inflate);

            userName = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date_time);
            lastMessage = itemView.findViewById(R.id.last_message);
            profilePicture = itemView.findViewById(R.id.profile_picture);

            inflate.setOnClickListener(view -> Log.d(TAG, "onClick: Clicked item"));

        }
    }
}
