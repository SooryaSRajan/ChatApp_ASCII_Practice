package com.ssr_projects.asciichatapp.Utils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.ssr_projects.asciichatapp.Models.ChatModel;
import com.ssr_projects.asciichatapp.R;

import java.util.ArrayList;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int CHAT_LEFT = 98736;

    private static final String TAG = ChatRecyclerAdapter.class.getName();

    ArrayList<ChatModel> chatDataArrayList;
    String userId;

    public ChatRecyclerAdapter(ArrayList<ChatModel> chatDataArrayList) {
        this.chatDataArrayList = chatDataArrayList;
        userId = FirebaseAuth.getInstance().getUid();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;

        if (viewType == CHAT_LEFT)
            viewHolder = new ChatRecyclerAdapter
                    .leftChatBoxHolder(
                    inflater.inflate(R.layout.chat_box_left, parent, false));

        else
            viewHolder = new ChatRecyclerAdapter
                    .rightChatBoxHolder(
                    inflater.inflate(R.layout.chat_box_right, parent, false));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.e(TAG, "onBindViewHolder: " + position);
        if (chatDataArrayList.get(position).getSenderId().equals(userId)) {
            ((rightChatBoxHolder) holder).messageBoxView.setText(chatDataArrayList.get(position).getMessage());
        } else {
            ((leftChatBoxHolder) holder).messageBoxView.setText(chatDataArrayList.get(position).getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return chatDataArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {

        int CHAT_RIGHT = 12312;
        if (chatDataArrayList.get(position).getSenderId().equals(userId))
            return CHAT_RIGHT;

        else
            return CHAT_LEFT;
    }

    private static class leftChatBoxHolder extends RecyclerView.ViewHolder {
        TextView messageBoxView;

        public leftChatBoxHolder(View inflate) {
            super(inflate);
            messageBoxView = inflate.findViewById(R.id.message_text_box);

        }
    }

    public static class rightChatBoxHolder extends RecyclerView.ViewHolder {
        TextView messageBoxView;

        public rightChatBoxHolder(View inflate) {
            super(inflate);
            messageBoxView = inflate.findViewById(R.id.message_text_box);
        }
    }
}
