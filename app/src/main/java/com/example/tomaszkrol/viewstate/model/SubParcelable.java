package com.example.tomaszkrol.viewstate;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tomasz.krol on 2016-12-16.
 */

public class SubParcelable implements Parcelable {

    public SubParcelable() {
    }

    protected SubParcelable(Parcel in) {
    }

    public static final Creator<SubParcelable> CREATOR = new Creator<SubParcelable>() {
        @Override
        public SubParcelable createFromParcel(Parcel in) {
            return new SubParcelable(in);
        }

        @Override
        public SubParcelable[] newArray(int size) {
            return new SubParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
