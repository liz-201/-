package com.lelazy.host.interfaces;

import com.lelazy.host.classes.pingzi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface pingziRepository extends JpaRepository<pingzi,String> {
    @Query(value = "select b.bottle_id,b.bottle_author_id,b.bottle_catagroy from bottle b where b.bottle_status !=0",nativeQuery = true)
    public List<pingzi> findenablepingzi();
    @Modifying
    @Query(value = "update bottle set bottle_status =1 where bottle_id=?1",nativeQuery = true)
    public void setstatus1(String pingziid);
    @Modifying
    @Query(value = "update bottle set bottle_status =0 where bottle_id=?1",nativeQuery = true)
    public void setstatus0(String pingziid);
    @Modifying
    public void deleteByPingziid(String pingziid);
    @Modifying
    @Query(value = "insert into bottle values(?1,?2,1,?3)",nativeQuery = true)
    public void addpingzi(String pingziid,Integer uid,int catagory);
    @Query(value = "select b.bottle_id,b.bottle_author_id,b.bottle_catagroy from bottle b where b.bottle_id=?1",nativeQuery = true)
    public pingzi findpingziBybottle_id(String id);
}
