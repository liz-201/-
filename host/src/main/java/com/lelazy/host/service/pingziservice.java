package com.lelazy.host.service;

import com.lelazy.host.classes.pingzi;
import com.lelazy.host.interfaces.pingziRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class pingziservice {
    @Resource
    pingziRepository pingziRepository;
    @Transactional
    public List<pingzi> findallpingzi(){
        return pingziRepository.findenablepingzi();
    }
    @Transactional
    public void setstatus1(String id){
        pingziRepository.setstatus1(id);
    }
    @Transactional
    public void setstatus0(String id){
        pingziRepository.setstatus0(id);
    }
    @Transactional
    public void savepingzi(pingzi pingzi){
        pingziRepository.save(pingzi);
    }
    @Transactional
    public  void delpingzi(String id){
        pingziRepository.deleteByPingziid(id);
    }
    @Transactional
    public  void  addpingzi(String id,Integer uid,int catagory){
        pingziRepository.addpingzi(id,uid,catagory);
    }
    @Transactional
    public pingzi findpingzibyid(String id){
        return pingziRepository.findpingziBybottle_id(id);
    }
   @Transactional
    public void Listset1(List<pingzi> list){
       for (pingzi p:list
            ) {
           pingziRepository.setstatus1(p.getPingziid());
       }
   }
}
