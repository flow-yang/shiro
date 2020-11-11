package com.flow.demo.sevice;

import com.flow.demo.pojo.Perms;
import com.flow.demo.pojo.Role;
import com.flow.demo.pojo.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    //注册用户
    void register(User user);
    User findByUsername(String username);
    //    根据用户查询所有角色
    User findRolesByUserName(String username);
    //根据角色id查询权限集合
    List<Perms> findPermsByRoleId(String id);
}
