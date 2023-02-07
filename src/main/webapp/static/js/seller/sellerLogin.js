$(function () {
    $("#head_portrait_file").change(function () {
        // 获取上传文件对象
        var file = $(this)[0].files[0];
        // 读取文件URL
        var reader = new FileReader();
        reader.readAsDataURL(file);
        // 阅读文件完成后触发的事件
        reader.onload = function () {
            // 读取的URL结果：this.result
            $("#head_portrait").attr("src", this.result);
        }
    });
    var inputs=$(".form-style");
    $.each(inputs,function(index,element) {
        $(element).bind('input propertychange',function(event){
            $(element).next().text("");
            $(element).prev().text("");
        });
    });
    $("#log").click(function () {
        var username=$("input[name=username]").eq(0).val();
        var password=$("input[name=password]").eq(0).val();
        var flag1=verifySpaceOrNull("用户名",$("#logU"),username);
        var flag2=verifySpaceOrNull("密码",$("#logP"),password);
        if(flag1&&flag2){
            $("#logForm").attr("method","post");
            $("#logForm").attr("action","/seller/login");
            $("#logForm").submit();
        }
    })
    $("#register").click(function () {
        var username=$("input[name=username]").eq(1).val();
        var password=$("input[name=password]").eq(1).val();
        var store_name=$("input[name=store_name]").val();
        var phonenumber=$("input[name=phonenumber]").val();
        var address=$("input[name=address]").val();
        var realname=$("input[name=realname]").val();
        var file=$("#head_portrait_file");
        var flag1=verifySpaceOrNull("用户名",$("#username_msg"),username);
        var flag2=verifySpaceOrNull("密码",$("#password_msg"),password);
        var flag3=verifySpaceOrNull("店铺名称",$("#storeName_msg"),store_name);
        var flag4=verifySpaceOrNull("手机号码",$("#phone_msg"),phonenumber);
        var flag5=verifySpaceOrNull("地址",$("#address_msg"),address);
        var flag6=verifySpaceOrNull("姓名",$("#realname_msg"),realname);
        var flag7=verifySpaceOrNull("店铺头像",$("#img_msg"),file.val());
        if(flag1){
            flag1=verifyDataLength(username,"用户名",5,20,$("#username_msg"));
        }
        if(flag3){
            flag3=verifyDataLength(store_name,"店铺名称",4,10,$("#storeName_msg"));
        }
        if(flag4){
            flag4=verifyPhone(phonenumber);
            if(!flag4){
                $("#phone_msg").text("手机号码格式不正确");
            }
        }
        if(flag6){
            flag6=verifyRealname(realname);
            if(!flag6){
                $("#realname_msg").text("请输入真实姓名");
            }
        }
        if(flag7){
            flag7=verifyPic(file.val());
            if(!flag7){
                $("#img_msg").text("图片文件格式不正确");
            }else{
                flag7=checkPicSize(file);
                if(!flag7){
                    $("#img_msg").text("图片文件大小不能超过5M");
                }
            }
        }
        if(flag1&&flag2&&flag3&&flag4&&flag5&&flag5&&flag6&&flag7){
            $("#reg").attr("method","post");
            $("#reg").attr("action","/seller/register");
            $("#reg").submit();
        }
    })
    var msg=$("#msg").text();
    if(msg=="") {
        if($("#username_msg").text()!=""||$("#storeName_msg").text()!=""||$("#phone_msg").text()!=""){
            $('#login').click();
        }
    }else{
        alert("注册成功");
    }
    if($("#login_msg").text()=="登录成功"){
        alert("登录成功");
    }
})
