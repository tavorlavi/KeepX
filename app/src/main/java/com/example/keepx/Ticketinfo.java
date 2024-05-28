package com.example.keepx;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;

public class Ticketinfo {
    private String title, location, time, date, qr, uid;
    private String image;
    private boolean fav = false;



    public Ticketinfo(){

    }
    public String  getImage(){
        return this.image;
    }
    public String getTitle(){
        return this.title;
    }
    public String getLocation() {
        return location;
    }
    public String getTime(){
        return this.time;
    }
    public String getDate(){
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImage(String image) {
        this.image =image;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public boolean getFav(){
        return this.fav;
    }
    public void setFav(boolean f){
        this.fav = f;
    }

}
