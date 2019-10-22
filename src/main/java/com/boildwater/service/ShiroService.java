package com.boildwater.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

/**
 * @author jinfei
 * @create 2019-10-22 19:39
 */
@Service
public class ShiroService {


    /**
     * 利用Shiro在service层也可以轻松的获取到HttpSession对象中的属性
     */
    public void testShiroSession(){
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();

        System.out.println(session);//此session与Controller中的session不是同一个对象
        Object value = session.getAttribute("hello");
        System.out.println(value);
    }

    //表示登陆的用户必须含有admin这个角色才能调用该方法
    //需要注意的是：如果service层使用了事务，例如在该方法上加入了@Transactional注解，因为此时调用service层方法的对象是一个代理对象
    //那么资源权限管理就会无效，这时需要将该注解写在Controller方法上。
    @RequiresRoles({"admin"})
    public void testMethod(){
        System.out.println("shiroService testMethod...");
    }
}
