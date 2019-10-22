package com.boildwater.helloworld;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Quickstart application showing how to use Shiro's API.
 *
 * @since 0.9 RC2
 */
public class Quickstart {

    private static final transient Logger log = LoggerFactory.getLogger(Quickstart.class);


    public static void main(String[] args) {

        // The easiest way to create a Shiro SecurityManager with configured
        // realms, users, roles and permissions is to use the simple INI config.
        // We'll do that by using a factory that can ingest a .ini file and
        // return a SecurityManager instance:

        // Use the shiro.ini file at the root of the classpath
        // (file: and url: prefixes load from files and urls respectively):
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();

        // for this simple example quickstart, make the SecurityManager
        // accessible as a JVM singleton.  Most applications wouldn't do this
        // and instead rely on their container configuration or web.xml for
        // webapps.  That is outside the scope of this simple quickstart, so
        // we'll just do the bare minimum so you can continue to get a feel
        // for things.
        SecurityUtils.setSecurityManager(securityManager);

        // Now that a simple Shiro environment is set up, let's see what you can do:

        // 获取当前的 Subject. 调用 SecurityUtils.getSubject();
        Subject currentUser = SecurityUtils.getSubject();

        // Do some stuff with a Session (no need for a web or EJB container!!!)
        // 测试使用 Session
        // 获取 Session: Subject#getSession()
        Session session = currentUser.getSession();
        session.setAttribute("someKey", "aValue");
        String value = (String) session.getAttribute("someKey");
        if (value.equals("aValue")) {
            System.out.println("==================================================");
            log.info("---> Retrieved the correct value! [" + value + "]");
            System.out.println("==================================================");
        }

        // 测试当前的用户是否已经被认证. 即是否已经登录.
        // 调动 Subject 的 isAuthenticated() 
        if (!currentUser.isAuthenticated()) {
        	// 把用户名和密码封装为 UsernamePasswordToken 对象
            UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
//            UsernamePasswordToken token = new UsernamePasswordToken("lonestarr123", "vespa");
//            UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa123");

            // rememberme
            token.setRememberMe(true);
            try {
            	// 执行登录. 
                currentUser.login(token);
            } 
            // 若没有指定的账户, 则 shiro 将会抛出 UnknownAccountException 异常. 
            catch (UnknownAccountException uae) {
                System.out.println("==================================================");
                log.info("----> There is no user with username of " + token.getPrincipal());
                System.out.println("==================================================");
                return;
            }
            // 若账户存在, 但密码不匹配, 则 shiro 会抛出 IncorrectCredentialsException 异常。 
            catch (IncorrectCredentialsException ice) {
                System.out.println("==================================================");
                log.info("----> Password for account " + token.getPrincipal() + " was incorrect!");
                System.out.println("==================================================");
                return; 
            } 
            // 用户被锁定的异常 LockedAccountException
            catch (LockedAccountException lae) {
                System.out.println("==================================================");
                log.info("The account for username " + token.getPrincipal() + " is locked.  " +
                        "Please contact your administrator to unlock it.");
                System.out.println("==================================================");
            }
            // ... catch more exceptions here (maybe custom ones specific to your application?
            // 所有认证时异常的父类. 
            catch (AuthenticationException ae) {
                //unexpected condition?  error?
            }
        }

        //say who they are:
        //print their identifying principal (in this case, a username):
        System.out.println("==================================================");
        log.info("----> User [" + currentUser.getPrincipal() + "] logged in successfully.");
        System.out.println("==================================================");

        //test a role:
        // 测试是否有某一个角色. 调用 Subject 的 hasRole 方法. 
        if (currentUser.hasRole("schwartz")) {
            System.out.println("==================================================");
            log.info("----> May the Schwartz be with you!");
            System.out.println("==================================================");
        } else {
            System.out.println("==================================================");
            log.info("----> Hello, mere mortal.");
            System.out.println("==================================================");
            return; 
        }

        //test a typed permission (not instance-level)
        // 测试用户是否具备某一个行为. 调用 Subject 的 isPermitted() 方法。 
        if (currentUser.isPermitted("lightsaber:weild")) {
            System.out.println("==================================================");
            log.info("----> You may use a lightsaber ring.  Use it wisely.");
            System.out.println("==================================================");
        } else {
            System.out.println("==================================================");
            log.info("Sorry, lightsaber rings are for schwartz masters only.");
            System.out.println("==================================================");
        }

        //a (very powerful) Instance Level permission:
        // 测试用户是否具备某一个行为. 
        if (currentUser.isPermitted("user:delete:zhangsan")) {
            System.out.println("==================================================");
            log.info("----> You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  " +
                    "Here are the keys - have fun!");
            System.out.println("==================================================");
        } else {
            System.out.println("==================================================");
            log.info("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
            System.out.println("==================================================");
        }

        //all done - log out!
        // 执行登出. 调用 Subject 的 Logout() 方法.
        System.out.println("==================================================");
        System.out.println("---->" + currentUser.isAuthenticated());
        System.out.println("==================================================");

        currentUser.logout();

        System.out.println("==================================================");
        System.out.println("---->" + currentUser.isAuthenticated());
        System.out.println("==================================================");

        System.exit(0);
    }
}