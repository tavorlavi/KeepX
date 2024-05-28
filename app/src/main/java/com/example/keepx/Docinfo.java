package com.example.keepx;

public class Docinfo {
    private String image, title, uid;
    private boolean fav = false;
    public Docinfo(){

    }
    public Docinfo(String image, String title, String uid, boolean fav) {
        this.image = image;
        this.title = title;
        this.uid = uid;
        this.fav = fav;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }

    public boolean getFav() {
        return fav;
    }
}
