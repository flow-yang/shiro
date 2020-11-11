package com.flow.demo.realm;

import com.flow.demo.pojo.Perms;
import com.flow.demo.pojo.Role;
import com.flow.demo.pojo.User;
import com.flow.demo.sevice.UserService;
import com.flow.demo.sevice.UserServiceImp;
import com.flow.demo.utils.ApplicationContextUtils;
import com.oracle.xmlns.internal.webservices.jaxws_databinding.SoapBindingParameterStyle;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 这两个方法是在用户进行登录的时候，我们设计规则来判断用户是否能够登录并且登陆之后有哪些操作权限
 */
public class CustomerRealm  extends AuthorizingRealm {
//
    @Autowired
    UserService userService;
    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取身份信息
        String primaryPrincipal=(String) principalCollection.getPrimaryPrincipal();
        //根据主身份信息获取角色 和 权限信息
        System.out.println("调用授权验证："+primaryPrincipal);
        User user=userService.findRolesByUserName(primaryPrincipal);
//        授权角色信息
        if (!CollectionUtils.isEmpty(user.getRoles())){
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            user.getRoles().forEach(role -> {
                simpleAuthorizationInfo.addRole(role.getName());

                //权限信息
                List<Perms> perms = userService.findPermsByRoleId(role.getId());
                if (!CollectionUtils.isEmpty(perms)){
                    perms.forEach(perms1 ->{
                        simpleAuthorizationInfo.addStringPermission(perms1.getName());
                    });
                }
            });
            return simpleAuthorizationInfo;
        }
//        if ("xiaochen".equals(primaryPrincipal)){//满足条件后进行授权
//            SimpleAuthorizationInfo simpleAuthorizationInfo= new SimpleAuthorizationInfo();
//            simpleAuthorizationInfo.addRole("admin");
////            simpleAuthorizationInfo.addRole("admin");
//            simpleAuthorizationInfo.addStringPermission("user:find:*");
//
//            return simpleAuthorizationInfo;
//        }
        return null;
    }

    /**
     * 认证，思路：从传入的token拿到即将要登录的用户数据，然后根据token中即将登录的用户信息去数据库中找相关用户，然后将数据库中找到的用户信息
     * 返回给AuthenticationInfo用以验证
     * @param token  是通过contrler中得来的，即当前正尝试登录的用户
     * @return
     * @throws AuthenticationException
     */
    @Override
    //因为这个两个方法是在.login中，我们将它覆写了，所以这里的token是从login获取得来的
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("==========================");
        //获取主体，即用户登录名
        String principal=(String) token.getPrincipal();
        //这里之前是假设从数据中已经取出了数据
//        if ("root".equals(principal)){
//            //这里传入的前两个参数来自于数据库中。
//            //        这块对比逻辑是先对比username，但是username肯定是相等的，所以真正对比的是password。
////        从这里传入的credentials（这里是从数据库获取的）和token（filter中登录时生成的）中的password做对比，如果相同就允许登录，不相同就抛出异常。
//            return new SimpleAuthenticationInfo(principal,"123",this.getName());
//        }

        //在工厂中获取service对象
//        UserService userServiceImp= (UserService) ApplicationContextUtils.getBean("userServiceImp");
        //从数据库中找到用户，并获取用户
        User user=userService.findByUsername(principal);
        System.out.println("数据库中的："+user);
        if (!ObjectUtils.isEmpty(user)){
            //这里把盐也返回了
            return new SimpleAuthenticationInfo(user.getUsername(),user.getPassword(),ByteSource.Util.bytes(user.getSalt()),this.getName());
        }
        return null;
    }
}
