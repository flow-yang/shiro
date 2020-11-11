package com.flow.demo.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OrderController {
    @RequestMapping("/save")
//    @RequiresRoles("user")
    @RequiresPermissions("user:find:*")
    public String save(){
        System.out.println("进入方法");
        //获取主体对象
//        Subject subject= SecurityUtils.getSubject();
//        代码方式
//        if (subject.hasRole("user")){
//            System.out.println("保存订单");
//        }else{
//            System.out.println("无权访问");
//        }
        return "redirect:/login";
    }
}
