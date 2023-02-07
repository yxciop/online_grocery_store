$(document).ready(function (){
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
    var inputs=$('.form-control');
    $.each(inputs,function(index,element) {
        $(element).bind('input propertychange',function(event){
            $(element).next().text("");
        });
    });
    $("#btn1").bind('click',function () {
        var phonenumber=$("input[name=phonenumber]").val();
        var address=$("input[name=address]").val();
        var storename=$("input[name=store_name]").val();
        var file=$("#head_portrait_file");
        var flag1=verifySpaceOrNull("手机号码",$("#phoneMsg"),phonenumber);
        var flag2=verifySpaceOrNull("地址",$("#addressMsg"),address);
        var flag3=verifySpaceOrNull("商店名称",$("#storeNameMsg"),storename);
        var flag4=true;
        if(file.val().length<0){
            flag4=verifySpaceOrNull("商品图片",$("#imageMsg"),file.val());
            if(flag4){
                flag4=verifyPic(file.val());
                if(flag4){
                    flag4=checkPicSize(file);
                    if(!flag4){
                        $("#imageMsg").text("商品图片大小必须小于5MB");
                    }
                }else{
                    $("#imageMsg").text("商品图片文件格式不正确");
                }
            }
        }
        if(flag1){
            flag1=verifyPhone(phonenumber);
            if(!flag1){
                $("#phoneMsg").text("手机号码格式不正确");
            }
        }
        if(flag3){
            flag3=verifyDataLength(storename,"商店名称",4,10,$("#storeNameMsg"));
        }
        if(flag1&&flag2&&flag3&&flag4){
            updateInfo();
        }
    });
    function updateInfo(){
        var formdata=new FormData(document.getElementById("info"));
        $.ajax({
            url:'/seller/updateInfomation',
            type:'post',
            contentType:false,
            data:formdata,
            processData:false,
            async:false,
            success:function(result){
                $("#phoneMsg").text(result.phone_msg);
                $("#storeNameMsg").text(result.storeName_msg);
                if(result.msg=="修改成功"){
                    window.location.href="/seller/toUpdateInfo";
                }
            },
            error:function(err){
                console.log(err)
            }
        });
    }
})