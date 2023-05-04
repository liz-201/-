package com.lelazy.yuanting.classes;

import android.graphics.Bitmap;

import java.util.Date;

public class user {
    Integer gamepoint;
    String username,passwd/*,uid*/;
    Integer uid;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }
/*public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }*/

    public int getGamepoint() {
        return gamepoint;
    }

    public void setGamepoint(int gamepoint) {
        this.gamepoint = gamepoint;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
