package com.flow.demo.dao;

import com.flow.demo.pojo.Perms;
import com.flow.demo.pojo.Role;
import com.flow.demo.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserDao {
    void save(User user);
    User findByUsername(String username);
//    根据用户查询所有角色
    User findRolesByUserName(String username);

    //根据角色id查询权限集合
    List<Perms> findPermsByRoleId(String id);
}
