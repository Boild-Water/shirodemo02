package com.boildwater.factory;

import java.util.LinkedHashMap;

/**
 * @author jinfei
 * @create 2019-10-22 19:59
 */
public class FilterChainDefinitionMapBuilder {

    public LinkedHashMap<String,String> buildFilterChainDefinitionMap(){
        /**
         * 注意:这里为什么要使用LinkedHashMap?
         * LinkedHashMap:保证在遍历map元素时，可以照添加的顺序实现遍历。
         * 添加到map中的顺序，对资源权限的管理会有影响，因为shiro是采用首次优先原则配置资源权限的
         * 例如：
         *      /login.jsp = anon
         *      /** = authc
         *      与
         *      /** = authc
         *      /login.jsp = anon 控制的资源权限不一样，
         *
         *      前者表示除了/login.jsp页面外，其他所有页面都必须要经过登录认证后才能访问
         *      后者表示访问所有页面都要进行登录认证。
         */
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        //这里用于访问数据库表，往map中填充信息，这里简单的配置模拟一下。
        /*
        <value>
                /login.jsp = anon
                /shiroLogin = anon
                /shiroLogout = logout

                /user.jsp = roles[user]
                /admin.jsp = roles[admin]

                /** = authc
         </value>
         */
        map.put("/login.jsp","anon");
        map.put("/shiroLogin","anon");
        map.put("/shiroLogout","logout");
        map.put("/user.jsp","authc,roles[user]");//需要认证，并且角色有user才能访问
        map.put("/admin.jsp","authc,roles[admin]");//需要认证，并且角色有admin才能访问

        /**
         * 首先要理解记住我与authc登陆认证的区别
         * 例如:
         *  记住我是利用cookie工作的
         *      登录成功后，会到达list.jsp页面，此时既可以访问user.jsp，也可以访问admin.jsp
         *      并且当关闭浏览器后，可以再次访问到list.jsp这个页面。
         *      因为:
         *          1.在ShiroController.login()方法中，设置了记住我为true
         *          2.map.put("/list.jsp","user");
         *              user过滤器，允许记住我和已经进行authc认证后再次访问
         *
         *      关闭浏览器后，虽然能访问到list.jsp页面，
         *      但是对于user.jsp admin.jsp这两个页面就不可以访问咯。因为：
         *      map.put("/user.jsp","authc,roles[user]");//需要认证，并且角色有user才能访问
         *      map.put("/admin.jsp","authc,roles[admin]");//需要认证，并且角色有admin才能访问
         *
         *      记住我是放在cookie中的，但是authc在浏览器关闭后就会失效！！！
         *
         *      如何设置cookie作用的时长?
         *          配置securityManager时，添加属性rememberMeManager.cookie.maxAge:
         *          <bean id="securityManager" class="xxx">
         *              //单位 s
         *              <property name="rememberMeManager.cookie.maxAge" value="10"></property>
         *          </bean>
         */
        map.put("/list.jsp","user");

        map.put("/**","authc");

        return map;
    }
}
