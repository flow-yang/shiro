package com.flow.demo.sevice;

import com.flow.demo.dao.UserDao;
import com.flow.demo.pojo.Perms;
import com.flow.demo.pojo.Role;
import com.flow.demo.pojo.User;
import com.flow.demo.utils.SaltUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userServiceImp")
@Transactional
public class UserServiceImp implements UserService{

    @Autowired
    UserDao userDao;
    @Override
    //将注册用户的明文密码转成加密密码
    public void register(User user) {
//        处理业务调用dao
//        1 生成随机盐
        String salt= SaltUtils.getSalt(8);
        //2 将随机盐保存到数据
        user.setSalt(salt);
        //3 明文密码进行MD5+salt+hash散列
//        Md5Hash md5Hash= new Md5Hash(user.getPassword(),salt,1024);
//        user.setPassword(md5Hash.toHex());
        userDao.save(user);
    }
    @Override
    public User findByUsername(String username){
        return userDao.findByUsername(username);
    }

    @Override
    public User findRolesByUserName(String username) {
        return userDao.findRolesByUserName(username);
    }

    @Override
    public List<Perms> findPermsByRoleId(String id) {
        return userDao.findPermsByRoleId(id);
    }
}
