package com.lelazy.host.classes;

/*import android.graphics.Bitmap;
import android.net.Uri;*/


import jdk.nashorn.internal.ir.annotations.Ignore;
import org.w3c.dom.Text;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
//瓶子的音乐文件放在用户id/瓶子id
//拟瓶子id为userid加日期（日期精确到秒）
@Entity
@Table(name = "bottle")
public class pingzi implements Serializable {
    @Id
    @Column(name = "bottle_id")
    String pingziid;
    @Column(name = "bottle_author_id")
    Integer userid;
    /*@ManyToOne
                @Transient
        user user;*/
    @Transient
    Date createtime;
//    Uri
@Transient
    String pingzitext;
    @Transient
    int picposition;
    @Transient
    String dateformat;
    @Transient
    String imgUri;
    @Transient
    String musicUri;
    @Column(name = "bottle_catagroy")
    int catagory;
    public int getCatagory() {
        return catagory;
    }

    public void setCatagory(int catagory) {
        this.catagory = catagory;
    }
/*public user getUser() {
        return user;
    }*/

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

    /*public void setUser(user user) {
        this.user = user;
    }*/
    public void setPingziid(String id) {
        this.pingziid=id;
    }

    public String getPingziid() {
        return pingziid;
    }

    public static class pingzimaneger {
        String[] getpingzitext() {
            return null;
        }
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
    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        this.dateformat=format.format(this.createtime);
    }
}
