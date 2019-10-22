package com.boildwater.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jinfei
 * @create 2019-10-22 10:49
 */
@Controller
public class ShiroController {

    @RequestMapping("/shiroLogin")
    public String login(String username,String password){

        // 获取当前的 Subject. 调用 SecurityUtils.getSubject();
        Subject currentUser = SecurityUtils.getSubject();

        // 测试当前的用户是否已经被认证. 即是否已经登录.
        // 调动 Subject 的 isAuthenticated()
        if (!currentUser.isAuthenticated()) {
            // 把用户名和密码封装为 UsernamePasswordToken 对象
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);

            // rememberme
            token.setRememberMe(true);
            try {
                // 执行登录.(这个token实际上被传入到了com.boildwater.realms.ShiroRealm.doGetAuthenticationInfo(AuthenticationToken token) )
                currentUser.login(token);
            }
            // 所有认证时异常的父类.
            catch (AuthenticationException ae) {
                System.out.println("登陆失败！");
            }
        }


        return "redirect:list.jsp";
    }
}
