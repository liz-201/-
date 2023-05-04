package com.lelazy.host.interfaces;

import com.lelazy.host.classes.user;
import org.springframework.data.repository.CrudRepository;

public interface userRepository extends CrudRepository<user,Integer> {
}
