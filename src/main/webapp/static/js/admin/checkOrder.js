$(function () {
    getOrder(1);
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
function getOrder(pageNum){
    $.ajax({
        url:'/admin/selectUndisposedOrder',
        dataType:'json',
        data:{'pageNum':pageNum},
        type:'post',
        async:false,
        success:function (data) {
            var div="";
            $.each(data.pageInfo.list,function (index,element) {
                div+="<div class='row' style='word-wrap: break-word;border:3px solid purple'>"+
                    "<div class='col-12' style='word-wrap: break-word'>"+
                    "<span class='text-primary'>订单号:<br>"+element.orderId+"</span>"+
                    "</div>"+
                    "<div class='col-12'>"+
                    "<span class='text-info'>买家:"+element.buyerAccount+"</span>"+
                    "</div>"+
                    "<div class='col-12'>"+
                    "<span class='text-muted'>卖家:"+element.sellerAccount+"</span>"+
                    "</div>"+
                    "<div class='col-12'>"+
                    "<span class='text-danger'>订单状态:"+element.status+"</span>"+
                    "</div>"+
                    "<div class='col-12'>"+
                    "<span class='text-info'>规格:"+element.specification+"</span>"+
                    "</div>"+
                    "<div class='col-6'>"+
                    "<span class='text-success'>创建时间:<br>"+Format(element.createdTime,"yyyy-MM-dd hh:mm:ss")+"</span>"+
                    "</div>"+
                    "<div class='col-6'>"+
                    "<span class='text-success'>修改时间:<br>"+Format(element.modifyTime,"yyyy-MM-dd hh:mm:ss")+"</span>"+
                    "</div>"+
                    "<div class='col-5'>"+
                    "<span class='text-danger'>总计:<br>"+element.total_price+"元"+"</span>"+
                    "</div>"+
                    "<div class='col-7'>"+
                    "<input type='hidden' name='msg'  value=\""+element.msg+"\" />";
                if(element.status=="退款中") {
                    div+="<button class='btn btn-warning urgeRefund' oid='"+element.id+"'>催促退款</button>";
                }
                if(element.status=="退款失败"||element.status=="催促退款"){
                    div+="<button class='btn btn-danger refund' orderId='"+element.orderId+"' total='"+element.total_price+"' productid='"+element.product_id+"' oid='"+element.id+"'>强制退款</button>";
                }
                if(element.status=="已接受订单"){
                    div+="<button class='btn btn-primary urgeSend' oid='"+element.id+"'>催促发货</button>";
                }
                div+="<button class='btn btn-primary search' type='button' data-toggle='modal' data-target='#exampleModal'>查看订单反馈信息</button>";
                div+="</div>"+
                    "</div>";
            })
            $("#orderContent").html(div);
            var btns="";
            if(data.pageInfo.hasPreviousPage){
                btns+="<li class=\"page-item\"><a onclick=\"getOrder(1)\" class=\"page-link\">首页</a></li><li class=\"prev page-item\"> <a onclick=\"getOrder("+data.pageInfo.prePage+")\" class=\"page-link\">前一页</a></li>";
            }
            $.each(data.pageInfo.navigatepageNums,function (index,element) {
                btns+="<li  class=\"page-item pages\"><a onclick=\"getOrder("+element+")\" class=\"page-link\">"+element+"</a></li>";
            })
            if(data.pageInfo.hasNextPage){
                btns+="<li class=\"next page-item\"><a onclick=\"getOrder("+data.pageInfo.nextPage+")\"  class=\"page-link\">后一页</a></li>";
            }
            btns+="<li class=\"page-item\"><a onclick=\"getOrder("+data.pageInfo.pages+")\"  class=\"page-link\">尾页</a></li>";
            $("#btnlist").html(btns);
            var pages=$(".pages");
            $.each(pages,function (index,element) {
                if($(element).text()==pageNum){
                    $(element).addClass("active");
                }
            })
        }
    })
    $(".search").on('click',function () {
        var msg=$(this).parent().find("input[name=msg]").val();
        $(".modal-body").html(msg);
    })
    $(".urgeRefund").on('click',function () {
        var oid = $(this).attr("oid");
        $.ajax({
            url: '/admin/urgeRefund',
            type: 'post',
            data: {'id': oid},
            dataType: 'json',
            success: function (data) {
                if (data.msg =="催促成功") {
                    alert("催促成功");
                    getOrder(1);
                } else {
                    alert("催促失败");
                }
            }, error: function (e) {
                alert("催促失败");
            }
        })
    })
    $(".urgeSend").on('click',function () {
        var oid = $(this).attr("oid");
        $.ajax({
            url: '/admin/urgeSend',
            type: 'post',
            data: {'id': oid},
            dataType: 'json',
            success: function (data) {
                if(data.msg =="催促成功") {
                    alert("催促成功");
                    getOrder(1);
                } else {
                    alert("催促失败");
                }
            }, error: function (e) {
                alert("催促失败");
            }
        })
    })
    $(".refund").on('click',function () {
        var orderId = $(this).attr("orderId");
        var total_price = parseFloat($(this).attr("total"));
        var productId = $(this).attr("productid");
        var oid = $(this).attr("oid");
        $.ajax({
            url: '/order/refund',
            type: 'post',
            data: {
                'orderId': orderId,
                'total_price': total_price.toFixed(2),
                'productId': productId,
                'id': oid
            },
            dataType: 'json',
            async:false,
            success: function (data) {
                if (data.alipay_trade_refund_response.msg == "Success") {
                    alert("退款成功");
                    getOrder(1);
                } else {
                    alert("退款失败");
                }
            }, error: function (e) {
                alert("退款失败");
            }
        })
    })
}