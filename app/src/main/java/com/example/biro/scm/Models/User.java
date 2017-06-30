package com.example.biro.scm.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.FileInputStream;

/**
 * Created by Biro on 6/19/2017.
 */

public class User implements Parcelable {

    private String email;
    private String name;
    private String password;


    public User( )
    {

    }
    public User(Parcel in) {
        email = in.readString();
        name = in.readString();
        password = in.readString();
        type = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(name);
        dest.writeString(password);
        dest.writeInt(type);
    }
}
