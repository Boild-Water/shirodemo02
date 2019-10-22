package com.boildwater.service;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Service;

/**
 * @author jinfei
 * @create 2019-10-22 19:39
 */
@Service
public class ShiroService {


    //表示登陆的用户必须含有admin这个角色才能调用该方法
    //需要注意的是：如果service层使用了事务，例如在该方法上加入了@Transactional注解，因为此时调用service层方法的对象是一个代理对象
    //那么资源权限管理就会无效，这时需要将该注解写在Controller方法上。
    @RequiresRoles({"admin"})
    public void testMethod(){
        System.out.println("shiroService testMethod...");
    }
}
