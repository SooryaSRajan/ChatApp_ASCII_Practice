package com.ssr_projects.asciichatapp.Screens.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ssr_projects.asciichatapp.Models.ChatModel;
import com.ssr_projects.asciichatapp.R;
import com.ssr_projects.asciichatapp.Utils.ChatRecyclerAdapter;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Objects;

import static com.ssr_projects.asciichatapp.Utils.Constants.CHATS;
import static com.ssr_projects.asciichatapp.Utils.Constants.DATE;
import static com.ssr_projects.asciichatapp.Utils.Constants.FRIENDS_NODE;
import static com.ssr_projects.asciichatapp.Utils.Constants.FRIEND_CHAT_KEY;
import static com.ssr_projects.asciichatapp.Utils.Constants.LAST_MESSAGE;
import static com.ssr_projects.asciichatapp.Utils.Constants.RECEIVER_ID;
import static com.ssr_projects.asciichatapp.Utils.Constants.RECEIVER_NAME;
import static com.ssr_projects.asciichatapp.Utils.Constants.TIME;
import static com.ssr_projects.asciichatapp.Utils.Constants.USER_NODE;
import static com.ssr_projects.asciichatapp.Utils.DateTime.getCurrentDate;
import static com.ssr_projects.asciichatapp.Utils.DateTime.getCurrentTime;

public class ChatActivity extends AppCompatActivity {

    Intent intent;
    String receiverUserName;
    String receiverUserId;

    DatabaseReference databaseReference;
    DatabaseReference chatIdConfigurationReference;
    DatabaseReference chatIdConfigurationReceiverUserReference;
    DatabaseReference chatReference;

    FirebaseAuth firebaseAuth;

    ValueEventListener chatIdConfigurationListener;
    ValueEventListener chatListener;

    private final String TAG = getClass().getName();

    RecyclerView recyclerView;
    ChatRecyclerAdapter adapter;
    ArrayList<ChatModel> chatModelArrayList;

    EditText chatTextBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatTextBox = findViewById(R.id.chat_edit_box);
        recyclerView = findViewById(R.id.recycler_view);
        chatModelArrayList = new ArrayList<>();
        adapter = new ChatRecyclerAdapter(chatModelArrayList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        intent = getIntent();
        receiverUserName = intent.getStringExtra(RECEIVER_NAME);
        receiverUserId = intent.getStringExtra(RECEIVER_ID);

        Log.d(TAG, "onCreate: " + receiverUserId + " " + receiverUserName);

        firebaseAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "onCreate: " + firebaseAuth.getUid());

        if (databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }

        if (chatIdConfigurationReference == null) {
            chatIdConfigurationReference = databaseReference
                    .child(USER_NODE)
                    .child(Objects.requireNonNull(firebaseAuth.getUid()))
                    .child(FRIENDS_NODE)
                    .child(receiverUserId);
        }

        if (chatIdConfigurationReceiverUserReference == null) {
            chatIdConfigurationReceiverUserReference = databaseReference
                    .child(USER_NODE)
                    .child(receiverUserId)
                    .child(FRIENDS_NODE)
                    .child(Objects.requireNonNull(firebaseAuth.getUid()));
        }

        if (chatListener == null) {
            chatListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    chatModelArrayList.clear();
                    adapter.notifyDataSetChanged();

                    for (DataSnapshot snap : snapshot.getChildren()) {
                        chatModelArrayList.add(snap.getValue(ChatModel.class));
                        adapter.notifyItemInserted(chatModelArrayList.size() - 1);
                        recyclerView.smoothScrollToPosition(chatModelArrayList.size() - 1);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
        }

        if (chatIdConfigurationListener == null) {
            chatIdConfigurationListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d(TAG, "onDataChange verifying chat thread: " + snapshot);
                    if (snapshot.child(FRIEND_CHAT_KEY).getValue() == null) {
                        Log.d(TAG, "onDataChange: Chat thread not found");
                        databaseReference
                                .child(CHATS)
                                .push()
                                .setValue(0, (error, ref) -> {
                                    Log.d(TAG, "onComplete: " + ref.getKey());
                                    chatIdConfigurationReference
                                            .child(FRIEND_CHAT_KEY)
                                            .setValue(ref.getKey());

                                    chatIdConfigurationReceiverUserReference
                                            .child(FRIEND_CHAT_KEY)
                                            .setValue(ref.getKey());

                                    chatReference = databaseReference
                                            .child(CHATS)
                                            .child(Objects.requireNonNull(ref.getKey()));

                                    chatReference.addValueEventListener(chatListener);
                                    Log.d(TAG, "onDataChange: " + chatReference);
                                });
                    } else {
                        Log.d(TAG, "onDataChange: Chat id: " +
                                snapshot.child(FRIEND_CHAT_KEY)
                                        .getValue());

                        chatReference = databaseReference
                                .child(CHATS)
                                .child((String)
                                        Objects.requireNonNull(snapshot
                                                .child(FRIEND_CHAT_KEY)
                                                .getValue()));

                        chatReference.addValueEventListener(chatListener);
                        Log.d(TAG, "onDataChange: " + chatReference);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
        }

        chatIdConfigurationReference.addListenerForSingleValueEvent(chatIdConfigurationListener);

        if (savedInstanceState != null) {
            receiverUserName = savedInstanceState.getString(RECEIVER_NAME);
            receiverUserId = savedInstanceState.getString(RECEIVER_ID);
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(receiverUserName);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(RECEIVER_NAME, receiverUserName);
        outState.putString(RECEIVER_ID, receiverUserId);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatReference.removeEventListener(chatListener);
    }

    public void sendMessage(View view) {
        String message = chatTextBox.getText().toString();
        if (message.isEmpty()) {
            Toast.makeText(ChatActivity.this, "Message empty!", Toast.LENGTH_SHORT).show();
        } else {
            ChatModel chatModel = new ChatModel();
            chatModel.setDate(getCurrentDate());
            chatModel.setTime(getCurrentTime());
            chatModel.setMessage(message);
            chatModel.setSenderId(FirebaseAuth.getInstance().getUid());
            chatModel.setReceiverId(receiverUserId);
            chatReference.push().setValue(chatModel);
            chatTextBox.setText("");
            databaseReference.child(USER_NODE)
                    .child(Objects.requireNonNull(firebaseAuth.getUid()))
                    .child(LAST_MESSAGE)
                    .setValue(message);

            databaseReference.child(USER_NODE)
                    .child(receiverUserId)
                    .child(LAST_MESSAGE)
                    .setValue(message);

            chatIdConfigurationReference
                    .child(DATE)
                    .setValue(getCurrentDate());

            chatIdConfigurationReceiverUserReference
                    .child(DATE)
                    .setValue(getCurrentDate());

            chatIdConfigurationReference
                    .child(TIME)
                    .setValue(getCurrentTime());

            chatIdConfigurationReceiverUserReference
                    .child(TIME)
                    .setValue(getCurrentTime());
        }
    }
}
