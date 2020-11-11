package com.flow.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@Accessors(chain = true)
@NoArgsConstructor
public class User {
    private String id;
    private String username;
    private String password;
    private String salt;
    //定义一个角色集合
    private List<Role> roles;
}
