<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="bootstrap/bootstrap.jsp"%>
<html>
    <head>
        <title>注册页</title>
        <script type="application/javascript">
            $(function () {
                $("#regForm").submit(function () {
                    // 手机号验证
                    var phoneText = $("#phoneId").val();
                    if (phoneText.length != 11) {
                        // 提示手机号不正确
                        alert("手机号不正确");
                        return false;
                    }
                    // 密码验证
                    var pwd1Text = $("#pwd1Id").val();
                    var pwd2Text = $("#pwd2Id").val();
                    if (pwd1Text != pwd2Text) {
                        // 提示手机号不正确
                        alert("密码2次不一致");
                        return false;
                    }
                    return true;
                });
            });
        </script>
    </head>
    <body>
        <div class="container">
            <h1>${requestScope.registerResult}</h1>
            <form class="form-signin" action="register-c" method="post" id="regForm">
                <h2 class="form-signin-heading">用户注册</h2>
                <label for="inputName" class="sr-only">用户名</label>
                <input id="nameId" type="text" id="inputName" class="form-control" placeholder="请输入用户名" required autofocus
                       name="name">
                <label for="inputPhone" class="sr-only">手机号</label>
                <input id="phoneId" type="text" id="inputPhone" class="form-control" placeholder="请输入手机号" required autofocus
                       name="phone">
                <label for="inputPassword1" class="sr-only">用户密码</label>
                <input id="pwd1Id" type="password" id="inputPassword1" class="form-control" placeholder="请输入密码" required
                       name="password1">
                <label for="inputPassword2" class="sr-only">确认密码</label>
                <input id="pwd2Id" type="password" id="inputPassword2" class="form-control" placeholder="请再次输入密码" required
                       name="password2">
                <button class="btn btn-lg btn-primary btn-block" type="submit">注册</button>
            </form>
        </div> <!-- /container -->
    </body>
</html>
