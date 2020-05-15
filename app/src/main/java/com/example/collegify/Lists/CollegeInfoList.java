package com.example.collegify.Lists;
/*
 * Created by Hitanshu on 15-03-2019.
 */

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CollegeInfoList {

    @SerializedName("name")
    private String ClgName;

    @SerializedName("addr")
    private String ClgAddr;

    @SerializedName("link")
    private String website;

    @SerializedName("lat")
    private double latitude;

    @SerializedName("long")
    private double longitude;

    @SerializedName("contact")
    private String phonecall;

    @SerializedName("course")
    private List<String> courses;

    public String getClgName() {
        return ClgName;
    }

    public String getClgAddr() {
        return ClgAddr;
    }

    public String getWebsite() {
        return website;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public List<String> getCourses() {
        return courses;
    }
}
