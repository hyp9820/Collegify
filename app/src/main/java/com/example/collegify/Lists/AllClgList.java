package com.example.collegify.Lists;
/*
 * Created by Hitanshu on 21-03-2019.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AllClgList implements Parcelable {
    @SerializedName("name")
    public String name;

    @SerializedName("name1")
    private String name1;

    @SerializedName("image")
    private String image;

    public AllClgList(String name, String name1, String image) {
        this.name = name;
        this.name1 = name1;
        this.image = image;
    }
    AllClgList()
    {}

    protected AllClgList(Parcel in) {
        name = in.readString();
        name1 = in.readString();
        image = in.readString();
    }

    public static final Creator<AllClgList> CREATOR = new Creator<AllClgList>() {
        @Override
        public AllClgList createFromParcel(Parcel in) {
            return new AllClgList(in);
        }

        @Override
        public AllClgList[] newArray(int size) {
            return new AllClgList[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getName1() {
        return name1;
    }

    public String getImage() {
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(name1);
        dest.writeString(image);
    }
}
