package com.example.collegify;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.Toast;

import com.example.collegify.Adapters.CollegeListAdapter;
import com.example.collegify.Adapters.CourseRVAdapter;
import com.example.collegify.Adapters.CustomSwipeAdapter;
import com.example.collegify.Adapters.ReviewAdapter;
import com.example.collegify.Lists.CollegeList;
import com.example.collegify.Lists.ReviewClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    private Toolbar toolbar;
    List<ReviewClass> reviewList;


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Reviews");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reviewList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        final String clgnm = getIntent().getStringExtra("clgnm");

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Colleges");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String name =  postSnapshot.child("name").getValue(String.class);;
                    if (name.toLowerCase().equals(clgnm.toLowerCase())) {

                        DatabaseReference revref = FirebaseDatabase.getInstance().getReference("Colleges/" + postSnapshot.getKey() + "/reviews" );
                        revref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot dp: dataSnapshot.getChildren()){
                                    String uname = dp.child("user").getValue(String.class);
                                    String rev = dp.child("review").getValue(String.class);
                                    reviewList.add(new ReviewClass(uname,rev));
                                }
                                if(reviewList.isEmpty()){
                                    Toast.makeText(getApplicationContext(),"No Reviews Available" ,Toast.LENGTH_SHORT ).show();
                                } else {
                                    reviewAdapter = new ReviewAdapter(ReviewActivity.this, reviewList);
                                    recyclerView.setAdapter(reviewAdapter);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                String e = databaseError.getMessage();
                                Toast.makeText(getApplicationContext(), e, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                String e = databaseError.getMessage();
                Toast.makeText(getApplicationContext(), e, Toast.LENGTH_SHORT).show();
            }
        });

        Button giverevBtn = (Button) findViewById(R.id.grev);
        giverevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),GiveReviewActivity.class);
                intent.putExtra("clgnm",clgnm );
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
