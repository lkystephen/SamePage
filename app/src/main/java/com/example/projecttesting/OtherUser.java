package com.example.projecttesting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by edmundlee on 10/1/15.
 */
public class OtherUser implements Parcelable {
    public boolean hasLoc;
    private boolean hasLoc_old;
    public String fbid;
    public String userId;
    public String username;
    public double lat;
    public double longitude;
    public double lat_old;
    public double longitude_old;
    public long timestamp;
    public long timestamp_old;


    public OtherUser (String fbid_input, String uid_input, String username_input){
        hasLoc = false;
        hasLoc_old = false;
        fbid = fbid_input;
        userId = uid_input;
        username = username_input;
    }
    public OtherUser (String fbid_input, String uid_input, String username_input, double lat_input, double long_input){
        hasLoc = true;
        hasLoc_old = false;
        fbid = fbid_input;
        userId = uid_input;
        username = username_input;
        lat = lat_input;
        longitude = long_input;
    }

    public OtherUser (String fbid_input, String uid_input, String username_input, double lat_input, double long_input, double lat_in_old, double long_in_old, long ts, long ts_old){
        hasLoc = true;
        hasLoc_old = true;
        fbid = fbid_input;
        userId = uid_input;
        username = username_input;
        lat = lat_input;
        longitude = long_input;
        lat_old = lat_in_old;
        longitude_old = long_in_old;
        timestamp = ts;
        timestamp_old = ts_old;
    }


    protected OtherUser(Parcel in) {
        hasLoc = in.readByte() != 0;
        hasLoc_old = in.readByte() != 0;
        fbid = in.readString();
        userId = in.readString();
        username = in.readString();
        if(hasLoc){
            lat = in.readDouble();
            longitude = in.readDouble();
        }
        if (hasLoc_old) {
            lat_old = in.readDouble();
            longitude_old = in.readDouble();
            timestamp = in.readLong();
            timestamp_old = in.readLong();
        }
    }


    public String toString(){
        String tmp = new String();
        tmp = "\nfbid: "+ fbid +
                "\nname: "+username;
        if (userId == null){
            // do nth then
        } else {
            tmp = tmp + "\nuid: "+userId;
        }
        if (hasLoc) {
            tmp = tmp + "\nlat: "+lat +"\nlong: "+longitude;
        }
        if (hasLoc_old) {
            tmp = tmp + "\nlat_o: "+lat_old +"\nlong_o: "+longitude_old+ "\ntimestamp: "+timestamp +"\ntimestamp_old: "+timestamp_old;
        }
        return tmp;
    }

    public static final Creator<OtherUser> CREATOR = new Creator<OtherUser>() {
        @Override
        public OtherUser createFromParcel(Parcel in) {
            return new OtherUser(in);
        }

        @Override
        public OtherUser[] newArray(int size) {
            return new OtherUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (hasLoc ? 1 : 0));
        parcel.writeByte((byte) (hasLoc_old ? 1 : 0));
        parcel.writeString(fbid);
        parcel.writeString(userId);
        parcel.writeString(username);
        if (hasLoc) {
            parcel.writeDouble(lat);
            parcel.writeDouble(longitude);
        }
        if (hasLoc_old) {
            parcel.writeDouble(lat_old);
            parcel.writeDouble(longitude_old);
            parcel.writeLong(timestamp);
            parcel.writeLong(timestamp_old);
        }
    }
}
