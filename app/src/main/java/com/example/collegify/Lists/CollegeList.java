package com.example.collegify.Lists;
/*
 * Created by Hitanshu on 28-03-2019.
 */

import com.google.gson.annotations.SerializedName;

public class CollegeList {

    @SerializedName("name")
    private String ClgCatName;

    public CollegeList(String clgCatName) {
        ClgCatName = clgCatName;
    }

    public String getClgCatName() {
        return ClgCatName;
    }
}
