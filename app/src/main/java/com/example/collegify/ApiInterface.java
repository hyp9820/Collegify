package com.example.collegify;

import com.example.collegify.Lists.ClgCategories;
import com.example.collegify.Lists.CollegeInfoList;
import com.example.collegify.Lists.CollegeList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/*
 * Created by Hitanshu on 01-03-2019.
 */

public interface ApiInterface {

    @POST("RetrieveCategories.php")
    Call<List<ClgCategories>> getCategories();

    @POST("RetSubCategories.php")
    @FormUrlEncoded
    Call<List<ClgCategories>>  sendCategory(
            @Field("category") String cat
    );

    @POST("GetCourseClg.php")
    @FormUrlEncoded
    Call<List<CollegeList>> sendCourse(
            @Field("course") String course
    );

    @POST("Bookmark.php")
    @FormUrlEncoded
    Call<List<BK>> initbk(
            @Field("clg") String clgname,
            @Field("username") String usrnm
    );
}
