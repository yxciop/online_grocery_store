$(function () {
    getOrders(1);
})
function getOrders(pageNum){
    $.ajax({
        url:'/seller/delOrderList',
        data:{"pageNum":pageNum},
        dataType:'json',
        async:false,
        success:function (data){
            var div="";
            if(data.pageInfo.list.length>0){
                $.each(data.pageInfo.list,function (index,element) {
                    div+="<div class='row'>"+
                        "<div class='col-3'>"+
                        "<div style='height: 100%'><img src='"+element.imgPath+"' style='width:100%;height:100%' /></div>"+
                        "</div>"+
                        "<div class='col-7 row'>"+
                        "<div class='text-left col-4'><span>数量:"+element.amount+"</span></div>"+
                        "<div class='text-right col-8'><span>状态:"+element.status+"</span></div>"+
                        "<div class='text-left col-4'><span>价格:¥"+element.price+"</span></div>"+
                        "<div class='text-right col-8'><span>"+element.specification+"</span></div>"+
                        "<div class='col-12'><span>总计¥:"+element.total_price+"</span></div>"+
                        "<div class='col-12'>"+element.productname+"</div>"+
                        "</div>";
                    div+="<div class='col-2'>";
                    div+="<div><button type='button' class='btn btn-primary delete' data-id='"+element.id+"'>删除</button></div>";
                    div+="</div>";
                    div+="</div>";
                })
                $(".content").html(div);
                var btns="";
                if(data.pageInfo.hasPreviousPage){
                    btns+="<li class=\"page-item\"><a onclick=\"getOrders(1)\" class=\"page-link\">首页</a></li><li class=\"prev page-item\"> <a onclick=\"getOrders("+data.pageInfo.prePage+")\" class=\"page-link\">前一页</a></li>";
                }
                $.each(data.pageInfo.navigatepageNums,function (index,element) {
                    btns+="<li  class=\"page-item pages\"><a onclick=\"getOrders("+element+")\" class=\"page-link\">"+element+"</a></li>";
                })
                if(data.pageInfo.hasNextPage){
                    btns+="<li class=\"next page-item\"><a onclick=\"getOrders("+data.pageInfo.nextPage+")\"  class=\"page-link\">后一页</a></li>";
                }
                btns+="<li class=\"page-item\"><a onclick=\"getOrders("+data.pageInfo.pages+")\"  class=\"page-link\">尾页</a></li>";
                $("#btnlist").html(btns);
                var pages=$(".pages");
                $.each(pages,function (index,element) {
                    if($(element).text()==pageNum){
                        $(element).addClass("active");
                    }
                })
            }else{
                $(".content").html("无可删除的订单");
            }
        },error:function (e){
            console.log(e);
        }
    });
    $(".delete").on('click',function () {
        var id=$(this).attr("data-id");
        $.ajax({
            url:'/seller/deleteSellerOrder',
            data:{'id':id},
            dataType: 'json',
            type:'post',
            success:function (data){
                if(data.msg=="删除成功"){
                    getOrders(1);
                }else{
                    alert(data.msg);
                }
            },error:function (err) {
                console.log(err);
            }
        })
    })
}