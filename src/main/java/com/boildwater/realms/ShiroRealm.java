package com.boildwater.realms;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.HashSet;
import java.util.Set;

/**
 * @author jinfei
 * @create 2019-10-22 9:26
 */
public class ShiroRealm extends AuthorizingRealm {

    //授权的方法(此时，用户已经经过认证，登陆成功。)
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        //1.从PrincipalCollection中获取登陆用户的信息
        Object principal = principalCollection.getPrimaryPrincipal();

        //2.利用登陆的用户的信息来获取当前用户的角色或者权限(可能需要查询数据库)
        Set<String> roles = new HashSet<>();
        roles.add("user");
        if ("admin".equals(principal)){
            roles.add("admin");
        }

        //3.创建SimpleAuthorizationInfo对象，并设置其roles属性，返回
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);

        return info;
    }

    //认证的方法
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        System.out.println("my firstRealm...");

        //1.把authenticationToken转化为UsernamePasswordToken
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;

        //2.从UsernamePasswordToken中来过去username
        String username = usernamePasswordToken.getUsername();

        //3.调用数据库的方法，从数据库中查询username对应的用户记录
        System.out.println("从数据库中获取username：" + username + " 所对应的信息");

        //4.若用户不存在，则可以抛出UnknownAccountException异常
        if ("heihei".equals(username)) {//模拟
            System.out.println("用户不存在！");
            throw new UnknownAccountException("用户不存在！");
        }

        //5.根据用户的信息情况，决定是否要抛出其他的AuthenticationException异常(例如用户是否被锁定)
        if ("haha".equals(username)) {//模拟
            System.out.println("用户被锁定！");
            throw new LockedAccountException("用户被锁定！");
        }

        //6.根据用户的情况构建AuthenticationInfo对象并返回。
        /**
         * 以下信息是从数据库中获取的
         *  1) principal:认证的实体信息，可以是username，也可以是数据表对应的用户的实体类对象
         *  2) credentials:从数据表中获取的密码
         *  3) realmName:当前realm对象的name，调用父类的getName()方法即可
         *  4) credentialsSalt:指定盐值，目的在于迫使经过MD5加密后，不同用户的密码即使相同，
         *      但是加密后的密码也不应该相同，这里的做法就是将用户名作为“盐”参与到MD5生成密码运算中
         *      更进一步提高密码的安全性。
         */
        Object principal = username;
        Object credentials = "123456";
        String realmName = getName();
        ByteSource credentialsSalt = ByteSource.Util.bytes(username);

        /**
         * 不使用盐值之前的做法
         */
//        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal,credentials,realmName);
//        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, method1(credentials),realmName);

        /**
         * 使用盐值 将username作为盐值
         */
        SimpleAuthenticationInfo info =
                new SimpleAuthenticationInfo(principal, method2(credentials,credentialsSalt), credentialsSalt, realmName);

        return info;
    }

    /**
     * 测试MD5加密
     */
    public static void main(String[] args) {
        Object credentials ="123456";//加密前明文密码
        System.out.println(method1(credentials));

        //不同用户名，虽然密码一样，但是设置了盐值之后，加密结果也不一致
        System.out.println(method2("123456",ByteSource.Util.bytes("admin")));
        System.out.println(method2("123456",ByteSource.Util.bytes("root")));
    }

    /**
     * 没有指定盐值的MD5加密
     */
    public static Object method1(Object credentials){

        String hashAlgorithmName = "MD5";//加密算法
        Object credentials1 = credentials;//加密前明文密码
        Object salt = null;//盐值
        int hashIterations = 1024;//加密次数

        Object result = new SimpleHash(hashAlgorithmName,credentials1,salt,hashIterations);
        return result;
    }

    /**
     * 指定盐值的MD5加密
     *  存在的问题，发现如果相同的密码经过加密后，生成的明文是相同的，这就存在一个问题
     *  如果不同的用户(不同的username)即使密码相同，经过加密后也应该保证明文不一样，
     *  这样的安全性是不是更高呢？
     */
    public static Object method2(Object credentials,Object salt){

        String hashAlgorithmName = "MD5";//加密算法
        Object credentials1 = credentials;//加密前明文密码
        Object salt1 = salt;//盐值
        int hashIterations = 1024;//加密次数

        Object result = new SimpleHash(hashAlgorithmName,credentials1,salt1,hashIterations);
        return result;
    }

}
