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
        map.put("/user.jsp","roles[user]");
        map.put("/admin.jsp","roles[admin]");
        map.put("/**","authc");

        return map;
    }
}
