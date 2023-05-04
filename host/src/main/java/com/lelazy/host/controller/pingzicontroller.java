package com.lelazy.host.controller;

import com.lelazy.host.classes.pingzi;
import com.lelazy.host.classes.pingzireadstream;
import com.lelazy.host.service.pingziservice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;





//需要再明确del瓶子时候的操作，删除记录和文件，还有上传时候的操作
@RestController
@RequestMapping("/pingzi")
public class pingzicontroller {
    @Value(value = "${spring.resources.static-locations}")
    String externalpath;
    @Resource
    pingziservice pingziservice;
    @RequestMapping("/getrpingzi")
    public List<pingzi> getallpingzi(){
        List<pingzi> l=new ArrayList<>();
        List<pingzi> res=pingziservice.findallpingzi();
        for (int i = 0; i < 1; i++) {
            Random x=new Random();
            int rint=x.nextInt(res.size());
            l.add(res.get(rint));
            pingziservice.setstatus0(res.get(rint).getPingziid());
            res.remove(res.get(rint));
        }
        System.out.println("雪豹闭嘴");
        return l;
    }
    @RequestMapping("/set1")
    public void set1(String id){
        pingziservice.setstatus1(id);
        System.out.println("放归了+++"+id);
    }
    @RequestMapping("/set0")
    public void set0(String id){
        pingziservice.setstatus0(id);
    }
//    上传的时候进行此操作，要保存文件
    @RequestMapping("/addpingzidata")
    public void savepingzidata(String id,Integer user,int catagory){
        pingziservice.addpingzi(id, user,catagory);
//        增加保存文件的操作
    }
    @RequestMapping("/filehouzhui")
    public List<String> askfile(Integer userid,String pingziid) throws IOException, ClassNotFoundException {
        List<String> rs=new ArrayList<>();
        String path="E:/bishe/host-res/pingzi/"+userid.toString()+"/"+pingziid+"/";
//        File ff=new File("E:/bishe/host-res/pingzi/"+userid.toString()+"/"+pingziid+"/");
        pingzireadstream obread=new pingzireadstream(new FileInputStream(new File(path+pingziid+".pingzi")));
        pingzi pingzi= (com.lelazy.host.classes.pingzi) obread.readObject();
        String imghouzhui=" ",musichouzhui=" ";
//        牢记第一项是图片，第二项是音乐
        if (pingzi.getImgUri()!=null){
            System.out.println(pingzi.getImgUri());
            imghouzhui=pingzi.getImgUri().substring(pingzi.getImgUri().lastIndexOf("."));
            System.out.println(imghouzhui);
        }
        if (pingzi.getMusicUri()!=null){
            musichouzhui=pingzi.getMusicUri().substring(pingzi.getMusicUri().lastIndexOf("."));
            System.out.println(musichouzhui);
        }
        rs.add(imghouzhui);
        rs.add(musichouzhui);
        return rs;
    }
    @RequestMapping("/del")
    public  void delbyid(String id){
        pingzi pp=pingziservice.findpingzibyid(id);
        File files=new File("E:/bishe/host-res/pingzi/"+pp.getUserid()+"/"+pp.getPingziid());
        if (files.exists()){
            if (files.list().length>0){
                for (File file:files.listFiles()
                     ) {
                    file.delete();
                }
            }
            files.delete();
        }
        pingziservice.delpingzi(id);
    }
    @RequestMapping("/s1eil")
    public void  set1_every_in_list(List<pingzi> list){
        System.out.println("成功返回值");
        pingziservice.Listset1(list);
    }
}
