<%--
  Created by IntelliJ IDEA.
  User: keli
  Date: 2018/8/13
  Time: 17:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" autoFlush="false" %>
<html>
<head>
    <title>用户注册</title>
</head>
<body>
<h1>用户注册</h1>
<form action="registUserServlet" method="post">
    <table width="600" border="1">
        <tr>
            <td>用户名</td>
            <td><input type="text" name="username"></td>
        </tr>
        <tr>
            <td>密码</td>
            <td><input type="password" name="password"></td>
        </tr>
        <tr>
            <td>昵称</td>
            <td><input type="text" name="nickname"></td>
        </tr>
        <tr>
            <td>邮箱</td>
            <td><input type="text" name="email"></td>
        </tr>
        <tr>
            <td colspan="2"> <input type="submit" value="注册"> </td>
        </tr>
    </table>
</form>
</body>
</html>
