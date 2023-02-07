function getFormatDate(timestamp) {
    var newDate = new Date(timestamp);
    Date.prototype.format = function (format) {
        var date = {
            'M+': this.getMonth() + 1,
            'd+': this.getDate(),
            'h+': this.getHours(),
            'm+': this.getMinutes(),
            's+': this.getSeconds(),
            'q+': Math.floor((this.getMonth() + 3) / 3),
            'S+': this.getMilliseconds()
        };
        if (/(y+)/i.test(format)) {
            format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
        }
        for (var k in date) {
            if (new RegExp('(' + k + ')').test(format)) {
                format = format.replace(RegExp.$1, RegExp.$1.length == 1
                    ? date[k] : ('00' + date[k]).substr(('' + date[k]).length));
            }
        }
        return format;
    }
    return newDate.format('yyyy-MM-dd h:m:s');
}
function  queryOrder(){
    var str="";
    $.ajax({
        url: '/buyer/queryBuyerOrder',
        type: 'get',
        dataType: "json",
        data:{'buyerId':$("#buyerId").val()},
        async:false,
        success: function (res) {
            var orderListList=res.orderListList;
            if(orderListList.length>0){
                for(var i=0;i<orderListList.length;i++) {
                    str += "<div style=\"border-bottom: 2px solid #9DC6D1;\">";
                    var orderList = orderListList[i];
                    for (var j = 0; j < orderList.length; j++) {
                        var order = orderList[j];
                        str += "<div class=\"row\" >" +
                            "<div class=\"col-4\"><img src=\"" + order.imgPath + "\" style=\"width: 100%;height: 140px\"></div>" +
                            "<div class=\"col-8\">" +
                            "<div class=\"row\">" +
                            "<div class=\"col-9\">" + order.productname + "</div>" +
                            "<div class=\"col-3\" style=\"color: red\">单价:¥" + order.price + "</div>" +
                            "</div>" +
                            "<div class=\"row\">";
                        var spec="";
                        if(order.specification!=""&&order.specification!=null){
                            spec=order.specification;
                        }else{
                            spec="无";
                        }
                        str+="<div class=\"col-8\" style=\"color: grey\">规格:" +spec+ "</div>" +
                            "<div class=\"col-4\" style=\"color: #1e7e34\">数量:" + order.amount + "</div>" +
                            "</div>" +
                            "<div class=\"row\">" +
                            "<div class=\"col-8\">" + order.status + "</div>" +
                            "<div class=\"col-4\" style=\"color: red\">总价:¥" + order.total_price + "</div>" +
                            "</div>" +
                            "<div class=\"row\">" +
                            "<input type='hidden'  value='"+order.id+"'   name=\"id\">"+
                            "<input type='hidden'  value='"+order.sellerId+"'   name=\"sellerid\">"+
                            "<input type='hidden'  value='"+order.orderId+"'   name=\"orderid\">"+
                            "<button class=\"btn btn-info more\" data-toggle=\"modal\" data-target=\"#detailModal\">更多</button>";
                        if(order.status == "已收货"||order.status=="未支付"||order.status=="退款成功"||order.status=="卖家拒绝订单") {
                            str += "<button class=\"btn btn-danger delete\">删除</button>";
                        }
                        if(order.status=="退款成功"&&order.msg!=""){
                            str+="<button class=\"btn btn-danger cancel\">撤销投诉</button>";
                        }
                        if(order.status=="退款失败"){
                            str+="<button class=\"btn btn-danger complaint\"  data-toggle=\"modal\" data-target=\"#complainModal\">投诉商家</button>";
                        }
                        if(order.status=="已送达"){
                            str += "<button class=\"btn btn-success receive\">确认收货</button>";
                        }
                        if(order.status=="未支付"){
                            str += "<a target=\"_blank\" href=\"/shop/order?amount="+order.amount+"&specification="+order.specification+"&product_id="+order.product_id+"&id="+order.id+"&status="+order.status+"\"><button class=\"btn btn-success\">去支付</button></a>";
                        }
                        str += "</div>"+
                            "</div>"+
                            "</div>";
                    }
                    str+="<div style=\"text-align: right\">下单时间:"+getFormatDate(orderList[0].createdTime)+"</div>";
                    str+="</div>";
                }
                $(".content").html(str);
            }else{
                $(".content").html("无订单数据");
            }
        },
        error: function (err) {
            console.log(err)
        }
    });
    $(".cancel").on('click',function () {
        var oid=$(this).siblings("input[name=id]").eq(0).val();
        var sellerid=$(this).siblings("input[name=sellerid]").eq(0).val();
        $.ajax({
            url:'/buyer/cancelComplain',
            type:'get',
            data:{'oid':oid,'sellerId':sellerid},
            dataType:'json',
            async:false,
            success:function (data) {
                console.log(data.msg);
                console.log(data.msg=="cancelSuccess");
                if(data.msg=="cancelSuccess"){
                    queryOrder();
                    alert("撤销成功");
                }else{
                    alert("撤销失败");
                }
            },error:function (err) {
                console.log(err)
            }
        })
    })
    $(".delete").on('click',function () {
        var id=$(this).siblings("input[name=id]").eq(0).val();
        $.ajax({
            url: '/buyer/deleteBuyerOrder',
            type: 'get',
            data:{'id':id},
            dataType: "json",
            async:false,
            success: function (res) {
                if(res.msg="删除成功"){
                    queryOrder();
                }else{
                    alert(res.msg);
                }
            },
            error: function (err) {
                console.log(err)
            }
        });
    })
    $(".complaint").on('click',function () {
        $("#orderId").val($(this).siblings("input[name=orderid]").eq(0).val());
        $("#sellerId").val($(this).siblings("input[name=sellerid]").eq(0).val());
        $("#oid").val($(this).siblings("input[name=id]").eq(0).val());
    })
    $(".receive").on('click',function () {
        var id=$(this).siblings("input[name=id]").eq(0).val();
        $.ajax({
            url: '/buyer/confirmReceive',
            type: 'get',
            data:{'id':id},
            dataType: "json",
            async:false,
            success: function (res) {
                if(res.msg="确认成功"){
                    $("#close").click();
                    queryOrder();
                }else{
                    alert(res.msg);
                }
            },
            error: function (err) {
                console.log(err)
            }
        });
    })
    $(".more").on('click',function () {
        var id=$(this).siblings("input[name=id]").eq(0).val();
        $.ajax({
            url:'/order/orderDetail',
            type:'get',
            data:{'id':id},
            dataType:'json',
            async:false,
            success:function (res) {
                $(".modal").find(".store_name").text(res.order.store_name);
                $(".modal").find(".imgPath").prop('src',res.order.imgPath);
                $(".modal").find(".title").text(res.order.productname);
                $(".modal").find(".price").text("¥"+res.order.price);
                $(".modal").find(".specification").text("规格:"+res.order.specification);
                $(".modal").find(".amount").text("数量:"+res.order.amount);
                $(".modal").find(".status").text(res.order.status);
                $(".modal").find(".total_price").text("总计:¥"+res.order.total_price);
                $(".modal").find(".recipient").eq(0).val(res.order.recipient);
                $(".modal").find(".phonenumber").eq(0).val(res.order.phonenumber);
                $(".modal").find(".address").eq(0).val(res.order.address);
                $(".modal").find(".orderId").text(res.order.orderId);
                if(res.order.msg!=null){
                    $(".modal").find(".msg").html(res.order.msg);
                }else{
                    $(".modal").find(".msg").html("");
                }
                if(res.order.status=="未发货" || res.order.status=="已收货" || res.order.status=="催促退款"){
                    var refund=$("<button id='refund' class='btn btn-primary'>退款</button>");
                    var reason=$("<input type='text' name='reason' placeholder='退款原因' style='display: none' required>");
                    $(".modal-footer").append($(refund));
                    $(".modal-footer").append($(reason));
                }
                if(res.order.status!="已收货" && res.order.status!="送货中" && res.order.status!="已送达" && res.order.status!="退款中" && res.order.status!="退款成功" && res.order.status!="退款失败"){
                    if($("body").find("#updateInfo").length>0){
                        $("#updateInfo").show();
                    }else {
                        var updateButton = $("<button class='btn badge-primary' id='updateInfo'>修改</button>");
                        $(".modal-footer").append($(updateButton));
                    }
                }else{
                    if($("body").find("#updateInfo").length>0){
                        $("#updateInfo").hide();
                    }
                }
            },error:function (err) {
                console.log(err)
            }
        })
        $("#refund").on('click',function (){
            if($("input[name=reason]").css("display")=="none"){
                $(this).remove();
                var refund=$("<button id='refund' class='btn btn-success'>退款</button>");
                $(".modal-footer").append($(refund));
                $("input[name=reason]").show();
                $("#refund").on('click',function () {
                    var reason=$("input[name=reason]").eq(0).val();
                    $.ajax({
                        url: '/buyer/refundOrder',
                        type: 'post',
                        data:{"id":id,"reason":reason},
                        dataType: "json",
                        async:false,
                        success: function (res) {
                            if(res.msg="申请成功"){
                                $("#close").click();
                                $("input[name=reason]").eq(0).val("");
                                $("input[name=reason]").eq(0).hide();
                                queryOrder();
                            }else{
                                alert(res.msg);
                            }
                        },
                        error: function (err) {
                            console.log(err)
                        }
                    });
                })
            }
        })
        $("#updateInfo").on('click',function () {
            var recipient=$(".modal").find(".recipient").eq(0).val();
            var phonenumber=$(".modal").find(".phonenumber").eq(0).val();
            var address=$(".modal").find(".address").eq(0).val();
            var flag1=verifySpaceOrNull("手机号码",null,phonenumber);
            var flag2=verifySpaceOrNull("姓名",null,recipient);
            var flag3=verifySpaceOrNull("地址",null,address);
            if(flag1){
                flag1=verifyPhone(phonenumber);
            }
            if(flag2){
                flag2=verifyRealname(recipient);
            }
            if(flag1&&flag2&&flag3){
                $.ajax({
                    url: '/buyer/updateOrderInfo',
                    type: 'post',
                    data:{
                        "id":id,
                        "recipient":recipient,
                        "phonenumber":phonenumber,
                        "address":address
                    },
                    dataType: "json",
                    async:false,
                    success: function (res) {
                        if(res.msg="修改成功"){
                            $("#close").click();
                            queryOrder();
                        }else{
                            alert(res.msg);
                        }
                    },
                    error: function (err) {
                        console.log(err)
                    }
                });
            }
        })
    })
    /*$.each($(".more"),function (index,element){
        $(element).on('click',function (){

        })
    });*/
}

$(function () {
    queryOrder();
    $('#detailModal').on('hide.bs.modal', function () {
        if($(this).find("input[name=reason]").length>0){
            $("input[name=reason]").eq(0).remove();
        }
        if($(this).find("#refund").length>0){
            $("#refund").remove();
        }
    });
    $("#com").on('click',function (){
        var content=$('#summernote').summernote('code');
        $.ajax({
            url:'/buyer/complainOrder',
            data:{
                'id':$("#oid").val(),
                'sellerId':$("#sellerId").val(),
                'complain':content,
                'oid':$("#orderId").val()
            },
            dataType:'json',
            type:'post',
            async:false,
            success:function (data) {
                if(data.msg=="投诉成功"){
                    $("#closeWin").click();
                    queryOrder();
                }else{
                    alert(data.msg);
                }
            },error:function (e) {
                console.log(e);
            }
        })
    });

})
