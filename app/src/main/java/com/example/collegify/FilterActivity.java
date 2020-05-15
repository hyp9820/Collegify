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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collegify.Adapters.CollegeListAdapter;
import com.example.collegify.Adapters.SubCategoryAdapter;
import com.example.collegify.Lists.AllClgList;
import com.example.collegify.Lists.ClgCategories;
import com.example.collegify.Lists.CollegeList;
import com.example.collegify.Lists.common;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SubCategoryAdapter filteradapter;
    private CollegeListAdapter Cfilteradapter;

    private NavigationView navigationView;

    List<ClgCategories> filteredList;
    List<CollegeList> filteredList1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);


        final String userinput = getIntent().getStringExtra("userinput");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(userinput);
        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //allCourseList = new ArrayList<>();
        //allCollegeList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        applyfilters(userinput);

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
    public void applyfilters(String userinput){
        /*Bundle bundle = getIntent().getExtras();
        allCourseList = bundle.getParcelableArrayList("AllCourses");
        allCollegeList = bundle.getParcelableArrayList("AllColleges");*/

        filteredList1 = new ArrayList<>();
        filteredList = new ArrayList<>();

        for (AllClgList temp : common.allCourseList) {
            if (temp.getName().toLowerCase().contains(userinput.toLowerCase())) {
                // || temp.getName1().toLowerCase().contains(user_input.toLowerCase())
                //user_input = temp.getName();

                filteredList.add(new ClgCategories(temp.getName(), temp.getImage()));
                //Toast.makeText(getApplicationContext(), user_input, Toast.LENGTH_LONG).show();
            }

        }
        for (AllClgList temp : common.allCollegeList) {

            if (temp.getName().toLowerCase().contains(userinput.toLowerCase())|| temp.getName1().toLowerCase().contains(userinput.toLowerCase())) {
                //|| temp.getName1().toLowerCase().contains(userinput.toLowerCase())
                //userinput = temp.getName();
                filteredList1.add(new CollegeList(temp.getName()));
                //  filteredList1.add(new CollegeList(temp.getName(), temp.getImage()));

            }

        }
        Toast.makeText(getApplicationContext(), "Search Finished", Toast.LENGTH_LONG).show();

        if(filteredList == null || filteredList.isEmpty()){

            if(filteredList1 == null || filteredList1.isEmpty()) {
                TextView message = (TextView) findViewById(R.id.text);
                message.setText("No result found");
            }
            else {
                Cfilteradapter = new CollegeListAdapter(FilterActivity.this, filteredList1);
                recyclerView.setAdapter(Cfilteradapter);
            }
        } else{
            filteradapter = new SubCategoryAdapter(FilterActivity.this, filteredList);
            recyclerView.setAdapter(filteradapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String user_input = query.trim();
                applyfilters(user_input);
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
        if (item.getItemId() == R.id.search) {

        }
        return super.onOptionsItemSelected(item);

    }
}
