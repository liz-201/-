package com.lelazy.host.controller;

import com.lelazy.host.classes.user;
import com.lelazy.host.service.userservice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.jws.WebParam;
//乐理
@RestController
@RequestMapping("/user")
public class usercontroller {
    @Resource
    userservice userservice;
    @RequestMapping("/signup")
    public Integer signup(String name,String pwd){
        user x=new user();
        x.setUsername(name);
        x.setPasswd(pwd);
        userservice.saveuser(x);
        System.out.println(x.getUid());
        return x.getUid();
    }
    @RequestMapping("/login")
    public int login(Integer uid,String username,String pwd){
        user x=userservice.findbyid(uid).get();
        System.out.println(username+","+pwd);
        if (x.getUsername().equals(username)&&x.getPasswd().equals(pwd)){
            System.out.println(1);
            return 1;
        }
        System.out.println(0);
        return 0;
    }
    @RequestMapping("/changemess")
    public void changemess(Integer uid,String name){
        user x=userservice.findbyid(uid).get();
        userservice.Changeusermess(x,name);
        System.out.println("执行了修改名字");
    }
    @RequestMapping("/changepwd")
    public Integer changepwd(Integer uid,String username,String newpwd){
        user x=userservice.findbyid(uid).get();
        System.out.println("执行了修改密码");
        if (x.getUsername().equals(username)){
            userservice.Changeuserpwd(x,newpwd);
            return 1;
        }
        return 0;
    }
}
