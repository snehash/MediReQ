package com.example.sneha.medireq;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * Created by Sneha on 7/25/2015.
 */
public class Profile implements Parcelable{
    private String name, address, phone, email, emergencyContact, emergencyNumber;
    private Date lastUpdate;

    public Profile(String name, String address, String phone, String email, String emergencyContact, String emergencyNumber){
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.emergencyContact = emergencyContact;
        this.emergencyNumber = emergencyNumber;

    }

    public Profile(){
        name = null;
        address = null;
        phone = null;
        email = null;
        emergencyContact = null;
        emergencyNumber = null;

    }
    public Profile(Parcel in){
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        name = in.readString();
        address = in.readString();
        phone = in.readString();
        email = in.readString();
        emergencyContact= in.readString();
        emergencyNumber = in.readString();
        lastUpdate = (Date) in.readSerializable();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }




    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(emergencyContact);
        dest.writeString(emergencyNumber);
        dest.writeSerializable(lastUpdate);
    }

    public String getEmergencyNumber() {
        return emergencyNumber;
    }

    public void setEmergencyNumber(String emergencyNumber) {
        this.emergencyNumber = emergencyNumber;
    }

    @Override
    public String toString(){
        String str = "";
        str += "Name: " + name;
        str += "Address: " + address;
        str += "Phone: " + phone;
        str += "Email: " + email;
        str += "EmerC: " + emergencyContact;
        str += "EmerN: " + emergencyNumber;
        return str;
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Profile createFromParcel(Parcel in) {
                    return new Profile(in);
                }

                public Profile[] newArray(int size) {
                    return new Profile[size];
                }
            };
}
