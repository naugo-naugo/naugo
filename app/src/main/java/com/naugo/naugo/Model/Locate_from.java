package com.naugo.naugo.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Locate_from implements Parcelable{
    private String name, destinationID;

    public Locate_from(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestinationID() {
        return destinationID;
    }

    public void setDestinationID(String destinationID) {
        this.destinationID = destinationID;
    }

    protected Locate_from(Parcel in) {
        name = in.readString();
        destinationID = in.readString();
    }

    public static final Creator<Locate_from> CREATOR = new Creator<Locate_from>() {
        @Override
        public Locate_from createFromParcel(Parcel in) {
            return new Locate_from(in);
        }

        @Override
        public Locate_from[] newArray(int size) {
            return new Locate_from[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(destinationID);
    }

    /*public Locate_from(){
    }

    public Locate_from(String name, String address){
        this.name = name;
    }

    public String getName() {
        return name;
    }
     */

}
