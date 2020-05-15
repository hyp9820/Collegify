package com.example.collegify;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collegify.Adapters.RecyclerViewAdapter;
import com.example.collegify.Lists.AllClgList;
import com.example.collegify.Lists.ClgCategories;
import com.example.collegify.Lists.common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Created by Hitanshu on 26-02-2019.
 */

public class ProfileActivity extends AppCompatActivity {

    private ActionBarDrawerToggle mToggle;

    List<ClgCategories> categoryList;

    private TextView user;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewAdapter myAdapter;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        common.e_mail = getIntent().getStringExtra("email");
        common.username = getIntent().getStringExtra("uname");

        navigationView = (NavigationView) findViewById(R.id.navgview);
        View headerView = navigationView.getHeaderView(0);
        user = (TextView) headerView.findViewById(R.id.user);
        user.setText(common.username);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");

        DrawerLayout mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        categoryList = new ArrayList<>();
        common.allCourseList = new ArrayList<>();
        common.allCollegeList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        common.allCourseList.clear();
        common.allCollegeList.clear();
        //////////////////////////////
        // /////////////////////////////////////////////////////
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("ClgSubCategories");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    for (DataSnapshot postSnapshot1 : postSnapshot.getChildren()) {
                        AllClgList allClgList = postSnapshot1.getValue(AllClgList.class);
                        String name = allClgList.getName().trim();
                        String name1 = allClgList.getName1();
                        String image = allClgList.getImage();
                        common.allCourseList.add(new AllClgList(name, name1, image));
                    }
                }
                //Toast.makeText(ProfileActivity.this,"Got Courses!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                String e = databaseError.getMessage();
                Toast.makeText(getApplicationContext(), e, Toast.LENGTH_SHORT).show();
                // ...
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference("Colleges");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String name = postSnapshot.child("name").getValue(String.class);
                    String name1 = postSnapshot.child("name1").getValue(String.class);
                    String image = "";//postSnapshot.child("image").getValue(String.class);
                    common.allCollegeList.add(new AllClgList(name, name1, image));
                }
                //Toast.makeText(ProfileActivity.this,"Got Colleges!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                String e = databaseError.getMessage();
                Toast.makeText(getApplicationContext(), e, Toast.LENGTH_SHORT).show();
            }
        });
        /////////////////////////////////////////////////////////////////////////////////////

        mDatabase = FirebaseDatabase.getInstance().getReference("ClgCategories");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String name = postSnapshot.child("name").getValue(String.class);
                    String image = postSnapshot.child("image").getValue(String.class);
                    categoryList.add(new ClgCategories(name,image ));
                }
                myAdapter = new RecyclerViewAdapter(ProfileActivity.this, categoryList); //Call the constructor
                recyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                String e = databaseError.getMessage();
                Toast.makeText(getApplicationContext(), e, Toast.LENGTH_SHORT).show();
            }
        });
        /*
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        final Call<List<ClgCategories>> call = apiInterface.getCategories();

        call.enqueue(new Callback<List<ClgCategories>>() {
            @Override
            public void onResponse(Call<List<ClgCategories>> call, Response<List<ClgCategories>> response) {

                categoryList = response.body();
                myAdapter = new RecyclerViewAdapter(ProfileActivity.this, categoryList); //Call the constructor
                recyclerView.setAdapter(myAdapter);

            }

            @Override
            public void onFailure(Call<List<ClgCategories>> call, Throwable t) {
                String error = t.getMessage();
                Toast.makeText(ProfileActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
        */


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String user_input = query.trim();
                Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
                intent.putExtra("userinput", user_input);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
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
