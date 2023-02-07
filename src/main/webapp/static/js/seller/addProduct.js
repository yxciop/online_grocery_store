$(function () {
    $("#btn1").bind('click',function (){
        var status=confirm();
        if(status==1){
            $("#art").attr("method","post");
            $("#art").attr("action","/seller/addProduct");
            $("#art").submit();
        }
    });
    var i=0;
    $("#specification").bind('click',function () {
        i++;
        var input=$("<input type=\"text\" name=\"specification[]\" class=\"form-control\" placeholder=\"请输入规格"+i+"\" required/>");
        $("#art").append(input);
    })
})