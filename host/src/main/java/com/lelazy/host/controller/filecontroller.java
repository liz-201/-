package com.lelazy.host.controller;

import com.lelazy.host.classes.pingzi;
import com.lelazy.host.classes.pingzireadstream;
import javassist.expr.Instanceof;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class filecontroller {
    @Value("${fileuploadpos}")
    private String posi;
    @Autowired
    pingzicontroller pingzicontroller;
//    @RequestMapping(value = "/file",method = RequestMethod.POST)
    @PostMapping("/uploadfile")
    public void uploadfiles(Integer uid, String filename, MultipartFile file) throws IOException {
        String pingziid=filename.substring(0,filename.lastIndexOf("."));
        File external=new File(posi+"/"+uid.toString());
        if (!external.exists()){
            System.out.println("用户已经存在"+external.getAbsolutePath());
            external.mkdir();
        }
        File filedir=new File(external.getAbsolutePath()+"/"+pingziid);
        if (!filedir.exists()){
            filedir.mkdir();
        }
        File filebody=new File(filedir.getAbsolutePath()+"/"+filename);
        if (filename.substring(filename.lastIndexOf(".")).equals(".pingzi")){
            try {
                pingzireadstream pingzireadstream=new pingzireadstream(file.getInputStream());
                ObjectOutputStream of=of=new ObjectOutputStream(new FileOutputStream(filebody));
                pingzi pingzi= (com.lelazy.host.classes.pingzi) pingzireadstream.readObject();
                of.writeObject(pingzi);
                pingzireadstream.close();
                of.close();
                pingzicontroller.savepingzidata(pingziid,pingzi.getUserid(),pingzi.getCatagory());
                System.out.println("写入瓶子成功");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            BufferedOutputStream bou=new BufferedOutputStream(new FileOutputStream(filebody));
            BufferedInputStream bin=new BufferedInputStream(file.getInputStream());
            int x;
            while ((x= bin.read())!=-1){
                bou.write(x);
                bou.flush();
            }
            bou.close();
            bin.close();
        }
        System.out.println("他是文件："+filename);
        System.out.println("他的大小："+file.getSize());
    }
    @PostMapping("/uploadsingle")
    public void getsingle(String id,String filename,MultipartFile file) throws Exception {
        String pingziid=filename.substring(0,filename.lastIndexOf("."));
        File external=new File(posi+"/"+id);
        if (!external.exists()){
            System.out.println("用户已经存在"+external.getAbsolutePath());
            external.mkdir();
        }
        File filedir=new File(external.getAbsolutePath()+"/"+pingziid);
        if (!filedir.exists()){
            filedir.mkdir();
        }
        File filebody=new File(filedir.getAbsolutePath()+"/"+filename);
        BufferedOutputStream bou=new BufferedOutputStream(new FileOutputStream(filebody));
        BufferedInputStream bin=new BufferedInputStream(file.getInputStream());
        int x;
        while ((x= bin.read())!=-1){
            bou.write(x);
            bou.flush();
        }
        bou.close();
        bin.close();
        System.out.println("他是文件："+filename);
        System.out.println("他的大小："+file.getSize());
    }
}
