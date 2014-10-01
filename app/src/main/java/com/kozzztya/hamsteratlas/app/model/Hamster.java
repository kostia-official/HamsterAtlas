package com.kozzztya.hamsteratlas.app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Hamster implements Parcelable, Comparable<Hamster> {

    private String mTitle;
    private String mDescription;
    private String mImage;

    public Hamster() {
    }

    public Hamster(Parcel parcel) {
        readFromParcel(parcel);
    }

    public Hamster(String title, String description, String image) {
        mTitle = title;
        mDescription = description;
        mImage = image;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mDescription);
        dest.writeString(mImage);
    }

    protected void readFromParcel(Parcel parcel) {
        mTitle = parcel.readString();
        mDescription = parcel.readString();
        mImage = parcel.readString();
    }

    public static final Parcelable.Creator<Hamster> CREATOR = new Parcelable.Creator<Hamster>() {
        public Hamster createFromParcel(Parcel in) {
            return new Hamster(in);
        }

        public Hamster[] newArray(int size) {
            return new Hamster[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public int compareTo(Hamster another) {
        return mTitle.compareTo(another.getTitle());
    }
}
