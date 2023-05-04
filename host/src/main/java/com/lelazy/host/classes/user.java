package com.lelazy.host.classes;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="user")
public class user implements Serializable {
    @Nullable
    @Column(name = "gamepoint")
    Integer gamepoint;
    @Column(name = "username")
    String username;
    @Column(name = "pwd")
    String passwd;
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer uid;
    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

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

    @Override
    public String toString() {
        return "user{" +
                "gamepoint=" + gamepoint +
                ", username='" + username + '\'' +
                ", passwd='" + passwd + '\'' +
                ", uid=" + uid +
                '}';
    }
}