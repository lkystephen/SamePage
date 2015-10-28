package com.example.projecttesting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by edmundlee on 10/1/15.
 */
public class OtherUser implements Parcelable {
    public boolean hasLoc;
    public String fbid;
    public String userId;
    public String username;
    public double lat;
    public double longitude;

    public OtherUser (String fbid_input, String uid_input, String username_input){
        hasLoc = false;
        fbid = fbid_input;
        userId = uid_input;
        username = username_input;
    }
    public OtherUser (String fbid_input, String uid_input, String username_input, double lat_input, double long_input){
        hasLoc = true;
        fbid = fbid_input;
        userId = uid_input;
        username = username_input;
        lat = lat_input;
        longitude = long_input;
    }

    protected OtherUser(Parcel in) {
        hasLoc = in.readByte() != 0;
        fbid = in.readString();
        userId = in.readString();
        username = in.readString();
        if(hasLoc){
            lat = in.readDouble();
            longitude = in.readDouble();
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
        parcel.writeString(fbid);
        parcel.writeString(userId);
        parcel.writeString(username);
        if (hasLoc) {
            parcel.writeDouble(lat);
            parcel.writeDouble(longitude);
        }
    }
}
