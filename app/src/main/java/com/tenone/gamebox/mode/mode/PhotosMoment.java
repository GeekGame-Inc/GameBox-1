package com.tenone.gamebox.mode.mode;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Eddy on 2018/1/4.
 */

public class PhotosMoment implements Parcelable {
    public String content;
    public ArrayList<String> photos;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeStringList(this.photos);
    }

    public PhotosMoment() {
    }

    public PhotosMoment(String content, ArrayList<String> photos) {
        this.content = content;
        this.photos = photos;
    }

    protected PhotosMoment(Parcel in) {
        this.content = in.readString();
        this.photos = in.createStringArrayList();
    }

    public static final Creator<PhotosMoment> CREATOR = new Creator<PhotosMoment>() {
        @Override
        public PhotosMoment createFromParcel(Parcel source) {
            return new PhotosMoment(source);
        }

        @Override
        public PhotosMoment[] newArray(int size) {
            return new PhotosMoment[size];
        }
    };
}
