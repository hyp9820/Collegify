package com.example.collegify.Lists;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

/*
 * Created by Hitanshu on 01-03-2019.
 */

public class ClgCategories {

    @SerializedName("name")
    private String ClgCatName;

    @SerializedName("image")
    private String ClgCatBg;


    //Constructor

    public ClgCategories(String clgCatName, String clgCatBg) {
        ClgCatName = clgCatName;
        ClgCatBg = clgCatBg;
    }


    //Getters

    public String getClgCatName() {
        return ClgCatName;
    }

    public String getClgCatBg() {
        return ClgCatBg;
    }

    //Setters

    public void setClgCatName(String clgCatName) {
        ClgCatName = clgCatName;
    }

    public void setClgCatBg(String clgCatBg) {
        ClgCatBg = clgCatBg;
    }

}
