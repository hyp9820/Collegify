package com.example.collegify.Adapters;

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

import com.example.collegify.ApiInterface;
import com.example.collegify.Lists.ClgCategories;
import com.example.collegify.ProfileActivity;
import com.example.collegify.R;
import com.example.collegify.SubCategory;
import com.squareup.picasso.Picasso;

import java.util.List;

/*
 * Created by Hitanshu on 26-02-2019.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context myContext;
    private List<ClgCategories> mData;
    private ApiInterface apiInterface;
    public static final String TAG = ProfileActivity.class.getSimpleName();


    //Constructor
    public RecyclerViewAdapter(Context myContext, List<ClgCategories> mData) {
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
        view = mInflater.inflate(R.layout.category_item, parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {


        myViewHolder.category.setText(mData.get(i).getClgCatName());
        Picasso.get().load(mData.get(i).getClgCatBg()).into(myViewHolder.catbg);
        myViewHolder.catbg.setAlpha(190);

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String cat = (String)mData.get(i).getClgCatName();

                Intent intent = new Intent(myContext, SubCategory.class);
                intent.putExtra("category", cat);
                myContext.startActivity(intent);

            }
        });

        //TO blur the image
        /*
         Bitmap bitmap = ((BitmapDrawable)myViewHolder.catbg.getDrawable()).getBitmap();//ImageView to bitmap
        Bitmap blurred = blurRenderScript(myContext,bit, 25);//second parameter is radius
        myViewHolder.catbg.setImageBitmap(blurred);//Assign the bitmap to imageview
        */
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
