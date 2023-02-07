function getOrders(pageNum){
    $.ajax({
        url:'/seller/showSellerOrderList',
        data:{"pageNum":pageNum,"sellerId":$("#sellerId").val()},
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
                    if(element.status=="退款中"||element.status=="催促退款"){
                        div+="<div><button class='btn btn-warning refund' orderId='"+element.orderId+"' total='"+element.total_price+"' productid='"+element.product_id+"' oid='"+element.id+"'>同意退款</button></div>";
                        div+="<div><button class='btn btn-dark denyRefund'  oid='"+element.id+"'>拒绝退款</button></div>";
                        div+="<div><button class='btn btn-dark complaint'  o-id='"+element.id+"' buyerid='"+element.buyerId+"' orderid='"+element.orderId+"' data-toggle='modal' data-target='#complainModal'>投诉买家</button></div>";
                    }
                    if(element.status=="未发货"){
                        div+="<div><button class='btn btn-warning accept' oid='"+element.id+"'>接受订单</button></div>";
                        div+="<div><button class='btn btn-danger deny' orderId='"+element.orderId+"' total='"+element.total_price+"' oid='"+element.id+"' productid='"+element.product_id+"'>拒绝订单</button></div>";
                    }
                    if(element.status=="已接受订单"){
                        div+="<div><button class='btn btn-info bill' oid='"+element.id+"'>已发货</button></div>";
                    }
                    if(element.status=="已发货"){
                        div+="<div><button class='btn btn-info delivery' oid='"+element.id+"'>派送中</button></div>";
                    }
                    if(element.status=="送货中"){
                        div+="<div><button class='btn btn-primary reach' oid='"+element.id+"'>已送达</button></div>";
                    }
                    div+="<input type='hidden'value='"+element.id+"'name='id'>";
                    div+="<div><button type=\"button\" class=\"btn btn-primary search\" data-toggle=\"modal\" data-target=\"#exampleModal\">查看</button></div>";
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
                $(".content").html("无可处理的订单");
            }
        },error:function (e){
            console.log(e);
        }
    });
    $(".search").on('click',function () {
        var id=$(this).parent().parent().children("input[name=id]").val();
        $.ajax({
            url:"/order/orderDetail?id="+id,
            type:"get",
            dataType:'json',
            async:false,
            success:function (data) {
                $(".modal").find(".recipient").eq(0).val(data.order.recipient);
                $(".modal").find(".phonenumber").eq(0).val(data.order.phonenumber);
                $(".modal").find(".address").eq(0).val(data.order.address);
                $(".modal").find(".orderId").text(data.order.orderId);
                $(".modal").find(".msg").html(data.order.msg);
            },error:function (err) {
                console.log(err);
            }
        })
    })
    $(".refund").on('click',function () {
        var orderId=$(this).attr("orderId");
        var  total_price=parseFloat($(this).attr("total"));
        var productId=$(this).attr("productid");
        var oid=$(this).attr("oid");
        $.ajax({
            url:'/order/refund',
            type:'post',
            data:{'orderId':orderId,'total_price':total_price.toFixed(2),'productId':productId,'id':oid},
            dataType: 'json',
            success:function (data) {
                if(data.alipay_trade_refund_response.msg=="Success"){
                    alert("退款成功");
                    getOrders(1);
                }else{
                    alert("退款失败");
                }
            },error:function (e) {
                console.log(e);
            }
        })
    })
    $(".denyRefund").on('click',function () {
        var id=$(this).attr("oid");
        $.ajax({
            url:'/seller/denyRefund',
            type:'get',
            data:{'id':id},
            dataType: 'json',
            success:function (data) {
                if(data.msg=="拒绝成功"){
                    alert(data.msg);
                    getOrders(1);
                }else{
                    alert("拒绝失败");
                }
            },error:function (e) {
                console.log(e);
            }
        })
    })
    $(".accept").on('click',function () {
        var id=$(this).attr("oid");
        $.ajax({
            url:'/seller/acceptOrder',
            type:'get',
            data:{'id':id},
            dataType: 'json',
            success:function (data) {
                if(data.msg=="接受成功"){
                    getOrders(1);
                }else{
                    alert("接受失败");
                }
            },error:function (e) {
                console.log(e);
            }
        })
    })
    $(".deny").on('click',function () {
        var orderId=$(this).attr("orderId");
        var total_price=parseFloat($(this).attr("total"));
        var id=$(this).attr("oid");
        var productId=$(this).attr("productid");
        $.ajax({
            url:'/order/refund',
            type:'post',
            data:{'orderId':orderId,'total_price':total_price.toFixed(2),'id':id,'denyOrder':'拒绝订单','productId':productId},
            dataType: 'json',
            async:false,
            success:function (data) {
                if(data.alipay_trade_refund_response.msg=="Success"){
                    alert("拒绝成功");
                    getOrders(1);
                }else{
                    alert("拒绝失败");
                }
            },error:function (e) {
                console.log(e);
            }
        })
    })
    $(".bill").on('click',function () {
        var id=$(this).attr("oid");
        $.ajax({
            url:'/seller/bill',
            type:'get',
            data:{'id':id},
            dataType: 'json',
            success:function (data) {
                if(data.msg=="发货成功"){
                    getOrders(1);
                }else{
                    alert("发货失败");
                }
            },error:function (e) {
                console.log(e);
            }
        })
    })
    $(".delivery").on('click',function () {
        var id=$(this).attr("oid");
        $.ajax({
            url:'/seller/delivery',
            type:'get',
            data:{'id':id},
            dataType: 'json',
            success:function (data) {
                if(data.msg=="派送成功"){
                    getOrders(1);
                }else{
                    alert("派送失败");
                }
            },error:function (e) {
                console.log(e);
            }
        })
    })
    $(".reach").on('click',function () {
        var id=$(this).attr("oid");
        $.ajax({
            url:'/seller/reach',
            type:'get',
            data:{'id':id},
            dataType: 'json',
            success:function (data) {
                if(data.msg=="已送达"){
                    getOrders(1);
                }else{
                    alert("送达失败");
                }
            },error:function (e) {
                console.log(e);
            }
        })
    })
    $(".complaint").on('click',function () {
        $("#orderid").val($(this).attr("orderid"));
        $("#buyerId").val($(this).attr("buyerid"));
        $("#oid").val($(this).attr("o-id"))
    })
}
$(function () {
    getOrders(1);
    $("#com").on('click',function (){
        var content=$('#summernote').summernote('code');
        $.ajax({
            url:'/seller/complainOrder',
            data:{
                'id':$("#oid").val(),
                'buyerId':$("#buyerId").val(),
                'complain':content,
                'oid':$("#orderid").val()
            },
            dataType:'json',
            type:'post',
            async:false,
            success:function (data) {
                if(data.msg=="投诉成功"){
                    $("#closeWin").click();
                    getOrders(1);
                    alert(data.msg);
                }else{
                    alert(data.msg);
                }
            },error:function (e) {
                console.log(e);
            }
        })
    });
    $('#exampleModal').on('show.bs.modal', function (event) {})

})
