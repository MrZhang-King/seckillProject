<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="bootstrap/bootstrap.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
    <head>
        <title>登录页</title>
    </head>
    <body>
        <div class="container" align="center">
            <form action="login-c" method="post">
                <h2 class="form-signin-heading">${requestScope.loginResult}</h2><br>
                <h2 class="form-signin-heading">${requestScope.registerResult}</h2><br>
                <h2 class="form-signin-heading">用户登录</h2>
                <label for="inputPhone" class="sr-only">phone</label>
                <input type="number" id="inputPhone" class="form-control" name="uphone" placeholder="请输入电话号码" required autofocus>
                <label for="inputPassword" class="sr-only">Password</label>
                <input type="password" id="inputPassword" class="form-control" name="upassword" placeholder="请输入密码" required>
                <button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
            </form>
<%--            <button class="btn btn-lg btn-primary btn-block" type="button" id="registerBtn">注册</button>--%>
            <a class="btn-block" href="jumpRegister">注册</a>
        </div>
    <a href="test1">测试</a>
    </body>
</html>
