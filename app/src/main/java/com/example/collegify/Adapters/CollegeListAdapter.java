package com.example.collegify.Adapters;
/*
 * Created by Hitanshu on 15-03-2019.
 */

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.collegify.CollegeActivity;
import com.example.collegify.Lists.CollegeList;
import com.example.collegify.R;

import java.util.List;

public class CollegeListAdapter extends RecyclerView.Adapter<CollegeListAdapter.MyViewHolder> {

    private Context myContext;
    private List<CollegeList> mData;

    //Constructors
    public CollegeListAdapter(Context myContext, List<CollegeList> mData) {
        this.myContext = myContext;
        this.mData = mData;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        View view;
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        view = mInflater.inflate(R.layout.cardview_item, parent,false);
        return new CollegeListAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final CollegeListAdapter.MyViewHolder myViewHolder, int i) {

        myViewHolder.category.setText(mData.get(i).getClgCatName());
        //Picasso.get().load(mData.get(i).getClgCatBg()).into(myViewHolder.catbg);
        myViewHolder.catbg.setAlpha(190);
        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ClgName = myViewHolder.category.getText().toString();
                Intent intent = new Intent(myContext, CollegeActivity.class);
                intent.putExtra("collegenm", ClgName);
                myContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView category;
        ImageView catbg;
        CardView cardView;

        public MyViewHolder(View itemView){
            super(itemView);

            category = (TextView) itemView.findViewById(R.id.category);
            catbg = (ImageView) itemView.findViewById(R.id.categorybg);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
        }

    }
}
