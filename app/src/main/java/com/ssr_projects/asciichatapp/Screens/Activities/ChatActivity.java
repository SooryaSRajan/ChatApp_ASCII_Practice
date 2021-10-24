package com.ssr_projects.asciichatapp.Screens.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ssr_projects.asciichatapp.R;

import java.util.EventListener;
import java.util.Objects;

import static com.ssr_projects.asciichatapp.Utils.Constants.CHATS;
import static com.ssr_projects.asciichatapp.Utils.Constants.FRIENDS_NODE;
import static com.ssr_projects.asciichatapp.Utils.Constants.FRIEND_CHAT_KEY;
import static com.ssr_projects.asciichatapp.Utils.Constants.RECEIVER_ID;
import static com.ssr_projects.asciichatapp.Utils.Constants.RECEIVER_NAME;
import static com.ssr_projects.asciichatapp.Utils.Constants.USER_NODE;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

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
}
