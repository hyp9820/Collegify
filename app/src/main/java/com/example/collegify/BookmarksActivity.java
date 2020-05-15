package com.example.collegify;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collegify.Adapters.CollegeListAdapter;
import com.example.collegify.Lists.CollegeList;
import com.example.collegify.Lists.common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookmarksActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    private TextView user;

    private NavigationView navigationView;
    List<CollegeList> collegeList;


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private CollegeListAdapter SubAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        navigationView = (NavigationView) findViewById(R.id.navgview);
        View headerView = navigationView.getHeaderView(0);
        user = (TextView) headerView.findViewById(R.id.user);
        user.setText(common.username);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bookmarks");
        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collegeList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        DatabaseReference mDb = FirebaseDatabase.getInstance().getReference("Users/"+common.username+"/Bookmarks");
        mDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {


                    String name = postSnapshot.getValue(String.class);
                    collegeList.add(new CollegeList(name));

                }
                SubAdapter = new CollegeListAdapter(BookmarksActivity.this, collegeList);
                recyclerView.setAdapter(SubAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                String e = databaseError.getMessage();
                Toast.makeText(getApplicationContext(), e, Toast.LENGTH_SHORT).show();
            }
        });

        navigationView = (NavigationView) findViewById(R.id.navgview);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.Home:
                        Intent HomeIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(HomeIntent);
                        finish();
                        break;
                    case R.id.Bookmarks:
                        Intent BookmarkIntent = new Intent(getApplicationContext(), BookmarksActivity.class);
                        startActivity(BookmarkIntent);
                        finish();
                        break;
                    case R.id.gmaps:
                        Intent GoogleMapsIntent = new Intent(getApplicationContext(), GoogleMapsActivity.class);
                        startActivity(GoogleMapsIntent);
                        break;
                    case R.id.ChangePassword:
                        Intent ChgPassIntent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                        startActivity(ChgPassIntent);
                        break;
                    case R.id.ChangeSecurityQuestions:
                        Intent ChgSecQuest = new Intent(getApplicationContext(),ChangeSecurityQuestionActivity.class);
                        startActivity(ChgSecQuest);
                        break;
                    case R.id.Feedback:
                        Intent FeedbackIntent = new Intent(getApplicationContext(),FeedbackActivity.class);
                        startActivity(FeedbackIntent);
                        break;
                    case R.id.Logout:
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                return false;
            }
        });

    }

    // So that the 3 line in toolbar work
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}