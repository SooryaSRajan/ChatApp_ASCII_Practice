package com.ssr_projects.asciichatapp.Screens.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.ssr_projects.asciichatapp.MainActivity;
import com.ssr_projects.asciichatapp.Models.HomePageFragmentModel;
import com.ssr_projects.asciichatapp.R;
import com.ssr_projects.asciichatapp.Screens.Fragments.ChatsListFragment;
import com.ssr_projects.asciichatapp.Screens.Fragments.PeopleListFragment;
import com.ssr_projects.asciichatapp.Utils.FragmentAdapter;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    ViewPager viewPager;
    TabLayout tabLayout;

    FragmentAdapter fragmentAdapter;
    ArrayList<HomePageFragmentModel> homePageFragmentModelArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();

        if (homePageFragmentModelArrayList == null) {
            homePageFragmentModelArrayList = new ArrayList<>();
            homePageFragmentModelArrayList.add(new HomePageFragmentModel(ChatsListFragment.newInstance(), "Chats"));
            homePageFragmentModelArrayList.add(new HomePageFragmentModel(PeopleListFragment.newInstance(), "People"));
            fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), 0, homePageFragmentModelArrayList);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        if (viewPager.getAdapter() == null) {
            viewPager.setAdapter(fragmentAdapter);
            tabLayout.setupWithViewPager(viewPager);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            logOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Log out")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    firebaseAuth.signOut();

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                })
                .setNegativeButton("No", null)
                .create()
                .show();
    }
}