package com.mwf.fuzzyquery;


import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.mwf.fuzzyquery.fakesearchview.SearchItem;

import java.util.Locale;

public class VehDataBean implements Parcelable, SearchItem {

    private String vehnumber;

    public VehDataBean(String vehnumber) {
        this.vehnumber = vehnumber;
    }

    protected VehDataBean(Parcel in) {
        vehnumber = in.readString();
    }

    public static final Creator<VehDataBean> CREATOR = new Creator<VehDataBean>() {
        @Override
        public VehDataBean createFromParcel(Parcel in) {
            return new VehDataBean(in);
        }

        @Override
        public VehDataBean[] newArray(int size) {
            return new VehDataBean[size];
        }
    };

    public String getVehnumber() {
        return vehnumber;
    }

    public void setVehnumber(String vehnumber) {
        this.vehnumber = vehnumber;
    }

    @Override
    public boolean match(CharSequence constraint) {
        if (!TextUtils.isEmpty(vehnumber)) {
            return vehnumber.toLowerCase(Locale.US)
                    .contains(constraint.toString().toLowerCase(Locale.US));
        } else {
            return false;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vehnumber);
    }
}
