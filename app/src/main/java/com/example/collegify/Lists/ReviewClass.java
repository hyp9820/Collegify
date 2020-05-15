package com.example.collegify.Lists;
/*
 * Created by Hitanshu on 17-04-2019.
 */

import android.widget.TextView;

public class ReviewClass {

    private String userid;
    private String review;

    public ReviewClass(String userid, String review) {
        this.userid = userid;
        this.review = review;
    }

    public String getUserid() {
        return userid;
    }

    public String getReview() {
        return review;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
