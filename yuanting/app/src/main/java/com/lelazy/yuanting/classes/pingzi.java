package com.lelazy.yuanting.classes;

import android.graphics.Bitmap;
import android.net.Uri;


import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
//瓶子的音乐文件放在用户id/瓶子id
//拟瓶子id为userid加日期（日期精确到秒）
public class pingzi implements Serializable {

    String pingziid;
//    user user;
    Integer userid;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    Date createtime;
//    Uri
    String pingzitext;
    int picposition;
    String dateformat;
    String imgUri,musicUri;
    int catagory;
    public pingzi() {
        this.imgUri=null;
        this.musicUri=null;
    }

  /*  public Uri getImgUri() {
        return imgUri;
    }

    public void setImgUri(Uri imgUri) {
        this.imgUri = imgUri;
    }

    public Uri getMusicUri() {
        return musicUri;
    }

    public void setMusicUri(Uri musicUri) {
        this.musicUri = musicUri;
    }*/
  public int getCatagory() {
      return catagory;
  }

    public void setCatagory(int catagory) {
        this.catagory = catagory;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getMusicUri() {
        return musicUri;
    }

    public void setMusicUri(String musicUri) {
        this.musicUri = musicUri;
    }

    /*public void setUser(com.lelazy.yuanting.classes.user user) {
        this.user = user;
    }*/
    public void setPingziid(String id) {
        this.pingziid=id;
    }

    public String getPingziid() {
        return pingziid;
    }

    public String getPingzitext() {
        return pingzitext;
    }

    public void setPingzitext(String pingzitext) {
        this.pingzitext = pingzitext;
    }

    public int getPicposition() {
        return picposition;
    }

    public void setPicposition(int picposition) {
        this.picposition = picposition;
    }

    public Date getCreatetime() {
        return createtime;
    }
    public String getDateformat() {
        return dateformat;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        this.dateformat=format.format(this.createtime);
    }

}
