$(function (){
    $("#btn1").on('click',function (){
        var status=confirm();
        if(status==1){
            $("#art").attr("method","post");
            $("#art").attr("action","/seller/updateProduct");
            $("#art").submit();
        }
    });
    $("#spec").bind('click',function () {
        $(this).before("<input class=\"form-control\"  placeholder=\"填写规格\" name=\"specification[]\">");
    })
    var type_1=$("#ty1>option");
    var type_2=$("#ty2>option");
    var type_3=$("#ty3>option");
    var first=$("#first").val();
    var second=$("#second").val();
    var third=$("#third").val();
    $.each(type_1,function (index,element){
        if($(element).val()==first){
            $(element).prop("selected",true);
        }
    });
    $.each(type_2,function (index,element){
        if($(element).val()==second){
            $(element).prop("selected",true);
        }
    });
    $.each(type_3,function (index,element){
        if($(element).val()==third){
            $(element).prop("selected",true);
        }
    });
    var description=$("#description").val();
    $("#con").val(description);
    setContent(description);
    $("#rem").on('click',function(){
        $.ajax({
            url: '/seller/setmsg',
            data:{"id":$("#pId").val(),"msg":"请审核"},
            type: 'post',
            dataType:'json',
            success: function (res) {
                if(res.result=="success"){
                    alert("提醒成功");
                }else {
                    alert("提醒失败");
                }
            },
            error: function (err) {
                console.log(err)
            }
        });
    });
})