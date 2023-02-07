$(function(){
    $("#sub").on('click',function(){
        var password=$("#password").val();
        var confirmPassword=$("#confirmPassword").val();
        var flag1=verifySpaceOrNull("密码",$("#msg"),password);
        if(password==confirmPassword){
            if(flag1){
                $("#update").attr("action","/seller/updatePassword");
                $("#update").attr("method","post");
                $("#update").submit();
                alert("修改成功");
            }
        }else{
            alert("请确保密码相符合");

        }
    });
});