package com.example.collegify;

/*
 * Created by Hitanshu on 15-03-2019.
 */

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collegify.Adapters.CollegeListAdapter;
import com.example.collegify.Adapters.CourseRVAdapter;
import com.example.collegify.Adapters.CustomSwipeAdapter;
import com.example.collegify.Lists.CollegeInfoList;
import com.example.collegify.Lists.CollegeList;
import com.example.collegify.Lists.common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollegeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    List<CollegeInfoList> collegeInfo;
    private ApiInterface apiInterface;

    private TextView collegenm;
    private TextView collegeaddr;

    private String clgname;
    private String clgname1;
    private String clgaddr;
    private String phoneno;
    private String link;
    private double lat;
    private double lg;
    private List<String> courses;
    private List<String> images;

    ViewPager viewPager;
    CustomSwipeAdapter adapter;
    private Timer timer;
    private int current_position = 0;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CourseRVAdapter cAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collegenm = (TextView) findViewById(R.id.collegename);
        collegeaddr = (TextView) findViewById(R.id.collegeaddr);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        collegeInfo = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        final String clgnm = getIntent().getStringExtra("collegenm");
        getSupportActionBar().setTitle(clgnm);

        ////////////////////////////////////////////////////////////////////////////////////////////
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Colleges");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String name = postSnapshot.child("name").getValue(String.class);
                    if (name.toLowerCase().equals(clgnm.toLowerCase())) {
                        clgname = postSnapshot.child("name").getValue(String.class);
                        clgname1 = postSnapshot.child("name1").getValue(String.class);
                        clgaddr = postSnapshot.child("addr").getValue(String.class);
                        phoneno = postSnapshot.child("contact").getValue(String.class);
                        link = postSnapshot.child("link").getValue(String.class);

                        collegenm.setText(clgname);
                        collegeaddr.setText(clgaddr);
                        lat = postSnapshot.child("lat").getValue(double.class);
                        lg = postSnapshot.child("long").getValue(double.class);

                        courses = new ArrayList<>();
                        images = new ArrayList<>();
                        for (DataSnapshot post : postSnapshot.child("course").getChildren()) {
                            courses.add(post.getValue(String.class));
                        }

                        cAdapter = new CourseRVAdapter(courses);
                        recyclerView.setAdapter(cAdapter);

                        for (DataSnapshot post : postSnapshot.child("image").getChildren()) {
                            images.add(post.getValue(String.class));
                            Toast.makeText(getApplicationContext(), post.getValue(String.class), Toast.LENGTH_SHORT).show();
                        }
                        if (images.isEmpty()) {
                                collegenm.setTextColor(Color.parseColor("#000000"));
                        }
                        adapter = new CustomSwipeAdapter(getApplicationContext(), images);

                        viewPager.setAdapter(adapter);
                        if (images.size() != 0) {
                            createSlideshow();
                        }


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                String e = databaseError.getMessage();
                Toast.makeText(getApplicationContext(), e, Toast.LENGTH_SHORT).show();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        /*
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<CollegeInfoList>> call = apiInterface.sendClgNm(clgnm.trim());
        Toast.makeText(getApplicationContext(), clgnm.trim() + "1",Toast.LENGTH_SHORT ).show();

        collegeInfo.clear();

        call.enqueue(new Callback<List<CollegeInfoList>>() {
            @Override
            public void onResponse(Call<List<CollegeInfoList>> call, Response<List<CollegeInfoList>> response) {


                collegeInfo = response.body();

                clgname = collegeInfo.get(0).getClgName();
                clgaddr = collegeInfo.get(0).getClgAddr();
                phoneno = collegeInfo.get(0).getPhonecall();
                link = collegeInfo.get(0).getWebsite();
                courses = new ArrayList<>();
                courses = collegeInfo.get(0).getCourses();

                collegenm.setText(clgname);
                collegeaddr.setText(clgaddr);
                lat = collegeInfo.get(0).getLatitude();
                lg = collegeInfo.get(0).getLongitude();
                cAdapter = new CourseRVAdapter(courses);
                recyclerView.setAdapter(cAdapter);

                Toast.makeText(CollegeActivity.this,"In Response",Toast.LENGTH_SHORT).show();
                collegeInfo.clear();
            }

            @Override
            public void onFailure(Call<List<CollegeInfoList>> call, Throwable t) {
                String error = t.getMessage();
                collegenm.setText(error);
                Toast.makeText(CollegeActivity.this,"Error: "+error,Toast.LENGTH_SHORT).show();

            }
        });
        */
        ////////////////////////////////////////////////////////////////////////////////////////////

        Button addbk = (Button) findViewById(R.id.addbk);
        addbk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<List<BK>> call = apiInterface.initbk(clgname, common.username);
                call.enqueue(new Callback<List<BK>>() {
                    @Override
                    public void onResponse(Call<List<BK>> call, Response<List<BK>> response) {
                        String flag = response.body().get(0).getFlag();
                        if(flag.equals("added")){
                            Toast.makeText(getApplicationContext(),"Bookmark Added" ,Toast.LENGTH_SHORT ).show();
                        }
                        else if(flag.equals("removed")){
                            Toast.makeText(getApplicationContext(),"Bookmark Removed" ,Toast.LENGTH_SHORT ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<BK>> call, Throwable t) {
                        String error = t.getMessage();
                        Toast.makeText(CollegeActivity.this,"Error: "+error,Toast.LENGTH_SHORT).show();
                    }
                });
                */

                final DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users/" + common.username + "/Bookmarks");
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(clgname1).exists()) {
                            db.child(clgname1).setValue(null);
                            Toast.makeText(getApplicationContext(), "Bookmark Removed", Toast.LENGTH_SHORT).show();
                        } else {
                            //Add the bookmark
                            db.child("Sample").setValue(null);
                            db.child(clgname1).setValue(clgname);
                            Toast.makeText(getApplicationContext(), "Bookmark Added", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        String e = databaseError.getMessage();
                        Toast.makeText(getApplicationContext(), e, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //int[] image_resources= {R.drawable.seminarhall,R.drawable.lab,R.drawable.library,R.drawable.lobby};

        Button btn = (Button) findViewById(R.id.call);
        Button dirbtn = (Button) findViewById(R.id.dir);
        Button busbtn = (Button) findViewById(R.id.bus);
        Button webBtn = (Button) findViewById(R.id.website);
        Button revBtn = (Button) findViewById(R.id.review);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneno));
                startActivity(intent);
            }
        });
        dirbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                //it broadcasts the request to all apps asking can anyone provide the service
                intent.setData(Uri.parse("geo:" + lat + "," + lg + "?q=" + clgname));
                startActivity(intent);
            }
        });
        busbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                //it broadcasts the request to all apps asking can anyone provide the service
                intent.setData(Uri.parse("geo:" + lat + "," + lg + "?q=Bus Stops"));
                startActivity(intent);
            }
        });
        webBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!link.isEmpty()) {
                    Uri webaddress = Uri.parse(link);
                    Intent launchWeb = new Intent(Intent.ACTION_VIEW, webaddress);
                    //it broadcasts the request to all apps asking can anyone provide the service
                    if (launchWeb.resolveActivity(getPackageManager()) != null) {
                        startActivity(launchWeb);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Website Not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        revBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ReviewActivity.class);
                intent.putExtra("clgnm", clgname);
                startActivity(intent);
            }
        });
    }

    private void createSlideshow() {


        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (current_position == images.size()) {
                    current_position = 0;
                }
                viewPager.setCurrentItem(current_position++, true);

            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 400, 2500);
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
