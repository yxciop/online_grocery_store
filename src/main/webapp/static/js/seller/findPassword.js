$(document).ready(function (){
    var inputs=$('.form-control');
    $.each(inputs,function(index,element) {
        $(element).bind('input propertychange',function(event){
            $(element).prev().text("");
        });
    });
    $("#fi_btn").bind('click',function () {
        var username=$("input[name=username]").val();
        var phonenumber=$("input[name=phonenumber]").val();
        var realname=$("input[name=realname]").val();
        var password=$("input[name=password]").val();
        var flag1=verifySpaceOrNull("用户名",$("#fi_username"),username);
        var flag2=verifySpaceOrNull("手机号码",$("#fi_phonenumber"),phonenumber);
        var flag3=verifySpaceOrNull("姓名",$("#fi_realname"),realname);
        var flag4=verifySpaceOrNull("密码",$("#fi_newPassword"),password);
        if(flag2){
            flag2=verifyPhone(phonenumber);
            if(!flag2){
                $("#fi_phonenumber").text("手机号码格式不正确");
            }
        }
        if(flag3){
            flag3=verifyRealname(realname);
            if(!flag3){
                $("#fi_realname").text("请输入正确的姓名");
            }
        }
        if(flag1&&flag2&&flag3&&flag4){
            findAndChange();
        }
    });
    function findAndChange() {
        $.ajax({
            url: '/seller/find',
            type: 'post',
            data: $("#fi").serialize(),
            dataType: "json",
            async:false,
            success: function (result) {
                console.log(result);
                $("#fi_username").text(result.userFail);
                $("#fi_phonenumber").text(result.phoneFail);
                $("#fi_realname").text(result.nameFail);
                if (result.change_msg == "密码修改成功") {
                    alert(result.change_msg);
                    window.location.href="/seller/tologin"
                }
            },
            error: function (err) {
                console.log(err)
            }
        });
    }
})