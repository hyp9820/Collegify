package com.example.collegify.Adapters;
/*
 * Created by Hitanshu on 17-04-2019.
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
import com.example.collegify.Lists.ReviewClass;
import com.example.collegify.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    private Context myContext;
    private List<ReviewClass> mData;

    //Constructors
    public ReviewAdapter(Context myContext, List<ReviewClass> mData) {
        this.myContext = myContext;
        this.mData = mData;
    }
    @NonNull
    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        View view;
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        view = mInflater.inflate(R.layout.review_item, parent,false);
        return new ReviewAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewAdapter.MyViewHolder myViewHolder, int i) {

        myViewHolder.user.setText(mData.get(i).getUserid() + ":");
        myViewHolder.review.setText(mData.get(i).getReview());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView user;
        TextView review;

        public MyViewHolder(View itemView){
            super(itemView);

            user = (TextView) itemView.findViewById(R.id.userid);
            review = (TextView) itemView.findViewById(R.id.review);

        }

    }
}

