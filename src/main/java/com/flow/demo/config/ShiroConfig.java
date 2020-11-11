package com.flow.demo.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.flow.demo.realm.CustomerRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;

//用来整合shiro相关的配置类
@Configuration
public class ShiroConfig {
    /**
     * 方言配置，这样就可以在前端利用shiro标签中解析出相关用户信息
     * @return
     */
    @Bean(name = "shiroDialect")
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }
//下面这两个方法使得shiro的注解生效
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }


    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor( DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager); // 这里需要注入 SecurityManger 安全管理器
        return authorizationAttributeSourceAdvisor;
    }


    //创建shiroFilter过滤器    负责拦截所有请求
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();
        //配置系统中受限和公共的资源
        HashMap filterChainDefinitionMap=new HashMap();
        //这里设置的是url，即在网址输入的内容
        filterChainDefinitionMap.put("/login","anon");   //设置为公共资源
        filterChainDefinitionMap.put("/register","anon");
//        filterChainDefinitionMap.put("/**","authc");   //需要认证和授权
        //默认认证界面路径，当访问一个资源需要认证的时候就会返回到这个认证界面
        shiroFilterFactoryBean.setLoginUrl("/login.html");   //这里的是url，会自动在controller中进行匹配
        //给filter设置安全管理器，在web环境中，安全管理器一旦创建，就会自动的给安全工具类中注入安全管理器，
        // 所以安全工具类可以调用里面的主体对象，比如在shirocontroller中调用了工具类来获取主体
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }
    //创建安全管理器
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(Realm realm){
        DefaultWebSecurityManager defaultWebSecurityManager=new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(realm);

        //给安全管理器设置realm
        return defaultWebSecurityManager;
    }
    @Bean
    public Realm getRealm(){
        CustomerRealm customerRealm=new CustomerRealm();
        //md5加密和散列加密，因为有问题。所以暂时跳过
        //修改凭证校验匹配器
//        HashedCredentialsMatcher credentialsMatcher=new HashedCredentialsMatcher();
        //设置加密算法MD5
//        credentialsMatcher.setHashAlgorithmName("MD5");
        //设置散列次数
//        credentialsMatcher.setHashIterations(1024);
//        customerRealm.setCredentialsMatcher(credentialsMatcher);

//        开启缓存管理
        customerRealm.setCacheManager(new EhCacheManager());
        customerRealm.setCachingEnabled(true); //开启全局缓存
        customerRealm.setAuthenticationCachingEnabled(true); //认证认证缓存
        customerRealm.setAuthenticationCacheName("authenticationCache");
        customerRealm.setAuthorizationCachingEnabled(true); //开启授权缓存
        customerRealm.setAuthorizationCacheName("authorizationCache");

        return customerRealm;
    }
}
