package com.boildwater.controller;

import com.boildwater.service.ShiroService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author jinfei
 * @create 2019-10-22 10:49
 */
@Controller
public class ShiroController {

    @Autowired
    private ShiroService shiroService;

    /**
     * 演示如何利用shiro在service层获取到session信息
     *  有时候这个功能还是很好用的
     */
    @RequestMapping("/shiroSession")
    public String testShiroSerssion(HttpServletRequest request){

        HttpSession session = request.getSession();
        session.setAttribute("hello","shiro");
        System.out.println(session);

        shiroService.testShiroSession();

        return "redirect:/list.jsp";
    }

    @RequestMapping("testShiroAnnotation")
    public String testShiroAnnotation(){
        shiroService.testMethod();
        return "redirect:/list.jsp";
    }

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
