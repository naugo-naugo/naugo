package com.naugo.naugo.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Drivers implements Parcelable {
    private String name, username, password, price, depart_from,depart_to, departing_arrive, departing_time, driversID, phone, boatname;
    private Long rating;

    public Drivers() {
    }

    protected Drivers(Parcel in) {
        name = in.readString();
        username = in.readString();
        password = in.readString();
        price = in.readString();
        depart_from = in.readString();
        depart_to = in.readString();
        departing_time = in.readString();
        departing_arrive = in.readString();
        phone = in.readString();
        boatname = in.readString();

        driversID = in.readString();
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readLong();
        }
    }

    public static final Creator<Drivers> CREATOR = new Creator<Drivers>() {
        @Override
        public Drivers createFromParcel(Parcel in) {
            return new Drivers(in);
        }

        @Override
        public Drivers[] newArray(int size) {
            return new Drivers[size];
        }
    };


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public String getDriversID() {
        return driversID;
    }

    public void setDriversID(String driversID) {
        this.driversID = driversID;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getDeparting_arrive() {
        return departing_arrive;
    }

    public void setDeparting_arrive(String departing_arrive) {
        this.departing_arrive = departing_arrive;
    }

    public String getDeparting_time() {
        return departing_time;
    }

    public void setDeparting_time(String departing_time) {
        this.departing_time = departing_time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(price);
        dest.writeString(depart_from);
        dest.writeString(depart_to);
        dest.writeString(departing_time);
        dest.writeString(departing_arrive);
        dest.writeString(driversID);
        dest.writeString(boatname);
        if (rating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(rating);
        }
    }
}
