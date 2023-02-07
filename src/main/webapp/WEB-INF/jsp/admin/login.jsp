<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>管理员登录平台</title>
    <link rel="stylesheet"  href="/static/css/common/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="/static/css/common/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="/static/css/admin/util.css">
    <link rel="stylesheet" type="text/css" href="/static/css/admin/login.css">
    <script src="/static/js/common/jquery.min.js"></script>
    <script src="/static/js/admin/login_validate.js"></script>
</head>
<body>
<div class="container-fluid">
    <div class="limiter">
        <div class="container-login100">
            <div class="wrap-login100">
                <div class="login100-form-title" style="background-image: url(../../../static/img/bg-01.jpg);">
                    <span class="login100-form-title-1">登 录</span>
                </div>

                <form class="login100-form validate-form" method="post" action="/admin/login">
                    <div class="wrap-input100 validate-input m-b-26" data-validate="用户名不能为空">
                        <span class="label-input100">用户名</span>
                        <input class="input100" type="text" name="username" placeholder="请输入用户名">
                        <span class="focus-input100"></span>
                        <span id="umsg">${usernameMsg}</span>
                    </div>

                    <div class="wrap-input100 validate-input m-b-18" data-validate="密码不能为空">
                        <span class="label-input100">密码</span>
                        <input class="input100" type="password" name="password" placeholder="请输入密码">
                        <span class="focus-input100"></span>
                        <span id="pmsg">${passwordMsg}</span>
                    </div>

                    <div class="container-login100-form-btn">
                        <button class="login100-form-btn">登 录</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
