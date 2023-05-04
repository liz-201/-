package com.lelazy.host.service;

import com.lelazy.host.classes.user;
import com.lelazy.host.interfaces.userRepository;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class userservice {
    @Resource
    userRepository userRepository;
    @Transactional
    public user saveuser(user x){
        return userRepository.save(x);
    }
    @Transactional
    public Optional<user> findbyid(Integer userid){
        return userRepository.findById(userid);
    }
    @Transactional
    public void Changeusermess(user x,String newname){
        x.setUsername(newname);
    }
    @Transactional
    public void Changeuserpwd(user x,String newpwd){
        x.setPasswd(newpwd);
    }

}
