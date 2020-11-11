package com.flow.demo.controller;

import com.flow.demo.pojo.User;
import com.flow.demo.sevice.UserService;
import com.flow.demo.sevice.UserServiceImp;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ShiroController {
    /**
     * 给定url跳转到指定页面
     */
    @Autowired
    private UserService userService;

    @RequestMapping("/register")
    public String register(){
        return "register";
    }
    /**
     * 用户认证
     * 前端传过来的数据直接封装到user对象中
     */
    @RequestMapping("/registering")
    public String registertoDatabase(User user){
        System.out.println("注册："+user.getUsername()+user.getPassword());
        try {

            userService.register(user);
            return "redirect:/login";
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("错误警告");
            return "register";
        }
    }
    /**
     * 登录
     * @return
     */
    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    /**
     * 登录时进行判断
     * @param username   前端传回来的用户名
     * @param password   前端传回的密码
     * @return
     */
    @RequestMapping("/tologin")
    public String tologin(String username,String password){
        System.out.println(username+password+"*************************");
        Subject subject=SecurityUtils.getSubject();
        UsernamePasswordToken token=new UsernamePasswordToken(username,password);
        try {
            subject.login(token);
            return "success";
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            System.out.println("用户名错误");
        }
        catch (IncorrectCredentialsException e){
            e.printStackTrace();
            System.out.println("密码错误");
            System.out.println("老是密码错误呢");
        }
        return "redirect:/login";
    }

    /**
     * 退出登录
     * @return
     */
    @RequestMapping("/logout")
    public String logout(){
    Subject subject= SecurityUtils.getSubject();
    subject.logout();  //退出用户
        return "redirect:/login";
    }
}
