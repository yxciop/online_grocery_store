$(function () {
    $("#mainPic").change(function () {
        // 获取上传文件对象
        var file = $(this)[0].files[0];
        // 读取文件URL
        var reader = new FileReader();
        reader.readAsDataURL(file);
        // 阅读文件完成后触发的事件
        reader.onload = function () {
            // 读取的URL结果：this.result
            $("#pic").attr("src", this.result);
        }
    });
    $('#ty1').on('change',function(){
        $('#ty1').next().text("");
        $("#ty3").html("<option>三级分类</option>");
        queryShopType($("#ty2"),$(this).val(),"二级分类");
    });
    $('#ty2').on('change',function(){
        $('#ty2').next().text("");
        queryShopType($("#ty3"),$(this).val(),"三级分类");
    });
    $("#ty3").on('change',function () {
        $('#ty3').next().text("");
    })
    var inputs=$('.form-control');
    $.each(inputs,function(index,element) {
        $(element).bind('input propertychange',function(event){
            $(element).next().text("");
        });
    });
})
function confirm(){
    var  text = $('#summernote').summernote('code');
    $("#con").val(text);
    var status=0;
    var title=$("input[name=title]").val();
    var price=$("input[name=price]").val();
    var path=$("#mainPic").val();
    var detail=$("#con").val().replace("<p><br></p>","");
    var stock=$("input[name=stock]").val();
    var defFile=$("#mainPic")[0].defaultValue;
    var flag1=verifySpaceOrNull("商品标题",$("#titlemsg"),title);
    var flag2=verifySpaceOrNull("商品价格",$("#pricemsg"),price);
    var flag3=true;
    var flag4=true;
    var flag5=verifySpaceOrNull("商品详情",$("#detailMsg"),detail);
    var flag6=verifySpaceOrNull("商品库存量",$("#stockmsg"),stock);
    if(flag1){
        flag1=verifyDataLength(title,"商品标题",5,50,$("#titlemsg"));
    }
    if(flag2){
        flag2=verifyPrice(price,$("#pricemsg"));
    }
    console.log(defFile.length);
    if(defFile.length<=0){
        flag3=verifySpaceOrNull("商品图片",$("#imgmsg"),path);
        if(flag3){
            flag3=verifyPic(path);
            if(flag3){
                flag3=checkPicSize($("#mainPic"));
                if(!flag3){
                    $("#imgmsg").text("商品图片大小必须小于5MB");
                }
            }else{
                $("#imgmsg").text("商品图片文件格式不正确");
            }
        }
    }
    if($("#ty1").val()=="一级分类"){
        $("#1msg").text("请选择一级分类");
        flag4=false;
    }
    if($("#ty2").val()=="二级分类"){
        $("#2msg").text("请选择二级分类");
        flag4=false;
    }
    if($("#ty3").val()=="三级分类"){
        $("#3msg").text("请选择三级分类");
        flag4=false;
    }
    if(flag1&&flag2&&flag3&&flag4&&flag5&&flag6){
        status=1;
    }
    return status;
}
function queryShopType(obj,parentId,ht) {
    $.ajax({
        url: '/type/query/' + parentId,
        type: 'get',
        dataType: "json",
        success: function (res) {
            var str="<option>"+ht+"</option>";
            for (var i = 0; i < res.length; i++) {
                str+="<option value='"+res[i].id+"'>"+res[i].stype+"</option>";
            }
            obj.html(str);
        },
        error: function (err) {
            console.log(err)
        }
    });
}