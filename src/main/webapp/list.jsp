<%--
  Created by IntelliJ IDEA.
  User: jinfe
  Date: 2019/10/22
  Time: 9:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--导入shiro标签库--%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>List</h1>

    Welcome:<shiro:principal></shiro:principal><br><br>

    <%--如果登录的用户含有admin角色--%>
    <shiro:hasRole name="admin">
        <a href="admin.jsp">admin</a><br><br>
    </shiro:hasRole>

    <%--如果登录的用户含有user角色--%>
    <shiro:hasRole name="user">
    <a href="user.jsp">user</a><br><br>
    </shiro:hasRole>

    <a href="shiroLogout">注销</a><br><br>
</body>
</html>
