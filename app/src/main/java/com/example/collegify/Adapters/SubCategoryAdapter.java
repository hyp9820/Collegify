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
import android.widget.Toast;

import com.example.collegify.Lists.ClgCategories;
import com.example.collegify.CollegeListActivity;
import com.example.collegify.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {

    private Context myContext;
    private List<ClgCategories> mData;


    //Constructor
    public SubCategoryAdapter(Context myContext, List<ClgCategories> mData) {
        this.myContext = myContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public SubCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

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
        return new SubCategoryAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final SubCategoryAdapter.MyViewHolder myViewHolder, final int i) {


        myViewHolder.category.setText(mData.get(i).getClgCatName());
        Picasso.get().load(mData.get(i).getClgCatBg()).into(myViewHolder.catbg);
        myViewHolder.catbg.setAlpha(190);

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String subcat = myViewHolder.category.getText().toString();
                Intent intent = new Intent(myContext, CollegeListActivity.class);
                intent.putExtra("subcategory", subcat.trim());
                myContext.startActivity(intent);
                Toast.makeText(myContext, subcat, Toast.LENGTH_LONG).show();
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
