package com.naugo.naugo.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class TimeSlot implements Parcelable {
    private String name, phone, driversID, depart_from, depart_to, boatname;
    private Long rating;

    public TimeSlot() {
    }

    protected TimeSlot(Parcel in) {
        name = in.readString();
        phone = in.readString();
        driversID = in.readString();
        depart_from = in.readString();
        depart_to = in.readString();
        boatname = in.readString();
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readLong();
        }
    }

    public static final Creator<TimeSlot> CREATOR = new Creator<TimeSlot>() {
        @Override
        public TimeSlot createFromParcel(Parcel in) {
            return new TimeSlot(in);
        }

        @Override
        public TimeSlot[] newArray(int size) {
            return new TimeSlot[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDriversID() {
        return driversID;
    }

    public void setDriversID(String driversID) {
        this.driversID = driversID;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public String getDepart_from() {
        return depart_from;
    }

    public void setDepart_from(String depart_from) {
        this.depart_from = depart_from;
    }

    public String getDepart_to() {
        return depart_to;
    }

    public void setDepart_to(String depart_to) {
        this.depart_to = depart_to;
    }

    public String getBoatname() {
        return boatname;
    }

    public void setBoatname(String boatname) {
        this.boatname = boatname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(depart_from);
        dest.writeString(depart_to);
        dest.writeString(boatname);

        dest.writeString(driversID);
        if (rating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(rating);
        }
    }
}
