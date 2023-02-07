$(function () {
    $("#btn1").click(function (){
        var  text = $('#summernote').summernote('code');
        if(text=="<p><br></p>"){
            alert("请编辑内容");
        }else{
            $.post('/seller/addmsg',{
                "msg":text
            },function (data){
                alert("申诉成功");
            })
        }
    });
})