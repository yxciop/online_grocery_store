$(function () {
    showTable(1);
})
function Format(datetime,fmt) {
    if (parseInt(datetime)==datetime) {
        if (datetime.length==10) {
            datetime=parseInt(datetime)*1000;
        } else if(datetime.length==13) {
            datetime=parseInt(datetime);
        }
    }
    datetime=new Date(datetime);
    var o = {
        "M+" : datetime.getMonth()+1,                 //月份
        "d+" : datetime.getDate(),                    //日
        "h+" : datetime.getHours(),                   //小时
        "m+" : datetime.getMinutes(),                 //分
        "s+" : datetime.getSeconds(),                 //秒
        "q+" : Math.floor((datetime.getMonth()+3)/3), //季度
        "S"  : datetime.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (datetime.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}
function showTable(pageNum){
    $.ajax({
        url:'/admin/showProductList',
        data:{"pageNum":pageNum},
        dataType:'json',
        async:false,
        success:function (data){
            var table="";
            if(data.pageInfo.list.length>0){
                $.each(data.pageInfo.list,function (index,element) {
                    table+="<tr class=\"row\"><td class=\"col-lg-2 col-md-4 col-6\">"+element.title+"</td><td class=\"col-lg-2 col-md-4 col-6\">"+element.price+"</td> <td class=\"col-lg-2 col-md-4 col-6\">"+element.stock+"</td> <td class=\"col-lg-2 col-md-4 col-6\">"+Format(element.modifyTime,"yyyy-MM-dd hh:mm:ss")+"</td> <td class=\"col-lg-2 col-md-4 col-sm-6 col-6\">"+element.store+"</td> <td class=\"col-lg-2 col-md-4 col-sm-6 col-6\"><button class=\"btn btn-success\" onclick=\"check("+element.id+")\">审核</button></td>";
                })
                $("#test>tbody").html(table);
            }else{
                $("#test>tbody").html("无需要审核的商品");
            }
            var btns="";
            if(data.pageInfo.hasPreviousPage){
                btns+="<li class=\"page-item\"><a onclick=\"showTable(1)\" class=\"page-link\">首页</a></li><li class=\"prev page-item\"> <a onclick=\"showTable("+data.pageInfo.prePage+")\" class=\"page-link\">前一页</a></li>";
            }
            $.each(data.pageInfo.navigatepageNums,function (index,element) {
                btns+="<li  class=\"page-item pages\"><a onclick=\"showTable("+element+")\" class=\"page-link\">"+element+"</a></li>";
            })
            if(data.pageInfo.hasNextPage){
                btns+="<li class=\"next page-item\"><a onclick=\"showTable("+data.pageInfo.nextPage+")\"  class=\"page-link\">后一页</a></li>";
            }
            btns+="<li class=\"page-item\"><a onclick=\"showTable("+data.pageInfo.pages+")\"  class=\"page-link\">尾页</a></li>";
            $("#btnlist").html(btns);
            var pages=$(".pages");
            $.each(pages,function (index,element) {
                if($(element).text()==pageNum){
                    $(element).addClass("active");
                }
            })
        },error:function (e){
            console.log(e);
        }
    });
}
function check(id){
    $.ajax({
        url:'/admin/queryDetail',
        data:{"id":id},
        dataType:'json',
        success:function (data){
            var str="";
            str+="<span class=\"text-warning\">标题:"+data.product.title+"</span><br>";
            str+="<span class=\"text-success\">分类:"+data.firstType+"&gt;"+data.secondType+"&gt;"+data.thirdType+"</span><br>";
            str+="<div>商品主图:<img src=\""+data.product.imgPath+"\" width=\"200px\" height=\"200px\" style='border:1px solid red'></div>";
            str+="<span class=\"text-danger\">价格:¥"+data.product.price+"</span><br>";
            str+="<span class=\"text-muted\">库存:"+data.product.stock+"</span><br>";
            str+="<div>详情:<br>"+data.product.description+"</div><br>";
            str+="<button class=\"btn btn-info\" onclick=\"access("+data.product.id+")\">审核通过</button>";
            str+="<button class=\"btn btn-info\" onclick=\"$('textarea[name=msg]').show();\">审核不通过</button><br>";
            str+="<textarea  name=\"msg\"  style='display: none' required placeholder='说明原因' cols='20' rows='10'></textarea><button class='btn btn-warning' onclick='setProductMsg("+data.product.id+")'>提交</button>";
            str+="<button class=\"btn btn-success\" onclick=\"$('.checkWindow').hide();\">关闭</button>";
            $(".checkWindow").html(str);
            $(".checkWindow").show();
        },error:function (e){
            console.log(e);
        }
    });
}
function access(id){
    $.ajax({
        url:'/admin/updateProductStatus',
        data:{"id":id},
        dataType:'json',
        success:function (data){
            alert(data.msg);
            if(data.msg=='审核通过'){
                $(".checkWindow").hide();
                showTable(1);
            }
        },error:function (e){
            console.log(e);
        }
    });
}
function setProductMsg(id){
    $.ajax({
        url:'/admin/setProductMsg',
        data:{"id":id,"msg":$("textarea[name=msg]").eq(0).val()},
        dataType:'json',
        success:function (data){
            alert(data.msg);
            if(data.msg=='发送原因成功'){
                $(".checkWindow").hide();
                showTable(1);
            }
        },error:function (e){
            console.log(e);
        }
    });
}