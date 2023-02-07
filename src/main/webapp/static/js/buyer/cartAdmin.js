$(document).ready(function() {
    /*
     * 计算购物车中每一个产品行的金额小计
     *
     * 参数 row 购物车表格中的行元素tr
     *
     */
    function getSubTotal(row) {
        var price = parseFloat($(row).find(".selling-price").data("bind"));
        var qty = parseInt($(row).find(":text").val());
        var result = price * qty;
        $(row).find(".selling-price").text($.formatMoney(price, 2));
        $(row).find(".subtotal").text($.formatMoney(result, 2)+"总价").data("bind", result.toFixed(2));
    };
    queryCart();
    function queryCart(){
        var str="";
        $.ajax({
            url: '/cart/queryBuyerCart',
            type: 'get',
            dataType: "json",
            data:{'buyerId':$("#buyerId").val()},
            async:false,
            success: function (res) {
                var cartListList=res.cartListList;
                if(cartListList.length>0){
                    for(var i=0;i<cartListList.length;i++){
                        var cartList=cartListList[i];
                        str+="<tr class='row'><td class='col-12'>"+cartList[0].store_name+"&nbsp;商家地址:"+cartList[0].address+"</td></tr>";
                        for(var j=0;j<cartList.length;j++){
                            var cart=cartList[j];
                            str+="<tr class=\"row\"  id=\""+cart.id+"\">" +
                                "<td class=\"col-1\"><input type=\"checkbox\" class=\"check-one check\" name=\"ids\" value=\""+cart.id+"\" /></td>" +
                                "<td class=\"col-3\"><img src=\""+cart.imgPath+"\" style=\"width: 100%;height: 170px\"></td>"+
                                "<td class=\"col-8\"><div><span>"+cart.title+"</span></div>";
                            if(cart.specification!=""&&cart.specification!=null) {
                                str += "<div><select name=\"specification\"><option selected>" + cart.specification + "</option>";
                                var dimensionArray = cart.dimension.split(',');
                                for (var k = 0; k < dimensionArray.length; k++) {
                                    if (dimensionArray[k] != cart.specification) {
                                        str += "<option>" + dimensionArray[k] + "</option>";
                                    }
                                }
                                str += "</select></div>";
                            }
                            str+="<div style=\"width: 100%;height: 30px\">" +
                                "    <div style=\"float:left\">" +
                                "           <span class=\"selling-price number small-bold-red\" data-bind=\""+cart.price+"\">"+cart.price+"</span>" +
                                "    </div>" +
                                "    <div style=\"float:right;\">" +
                                "           <span class=\"input-group-addon minus\" style=\"display:inline-block;width: 20px;height: 30px;background-color: red;\">-</span>" +
                                "           <input type=\"text\" class=\"number\" value=\""+cart.amount+"\" style=\"width: 80px\"  />" +
                                "           <span class=\"input-group-addon plus\" style=\"display:inline-block;width: 20px;height: 30px;background-color: red;\">+</span>" +
                                "     </div>" +
                                "  </div>" +
                                "  <div class=\"subtotal number small-bold-red\"></div>" +
                                "  <span class=\"delete btn btn-xs btn-warning\">删除</span>";
                            str+="</td></tr>";
                        }
                    }
                    $("#bok").html(str);
                }else{
                    $("#bok").html("无购物车信息");
                }
            },
            error: function (err) {
                console.log(err)
            }
        });
    }
    /*
     * 计算购物车中产品的累计金额
     */
    function getTotal() {
        var qtyTotal = 0;
        var itemCount = 0;
        var priceTotal = 0;
        $(cartTable).find("tr").each(function() {
            getSubTotal(this);
            if ($(this).find(":checkbox").prop("checked") == true) {
                itemCount++;
                qtyTotal += parseInt($(this).find(":text").val());
                priceTotal += parseFloat($(this).find(".subtotal").data("bind"));
            }
        });
        $("#itemCount").text(itemCount).data("bind", itemCount);
        $("#qtyCount").text(qtyTotal).data("bind", qtyTotal);
        $("#priceTotal").text($.formatMoney(priceTotal, 2)).data("bind", priceTotal.toFixed(2));
    };

    var cartTable = $("#cartTable");

    getTotal();

    function updateAmount(obj){
        var amount= parseInt(obj.val());
        var cartId=parseInt(obj.parent().parent().parent().parent().attr("id"));
         $.ajax({
            url: '/cart/updateAmount',
            type: 'get',
            data:{'id':cartId,'amount':amount},
            dataType: "json",
            success: function (res) {
                console.log("数量"+res.msg);
            },
            error: function (err) {
                console.log(err)
            }
        });
    }

    //为每一个勾选框指定单击事件


    //为数量调整的＋ －号提供单击事件，并重新计算产品小计
    /*
     * 为购物车中每一行绑定单击事件，以及每行中的输入框绑定键盘事件
     * 根据触发事件的元素执行不同动作
     *   增加数量
     *   减少数量
     *   删除产品
     *
     */
    $(cartTable).find("tr").each(function() {
        var input = $(this).find(":text");
        var select=$(this).find("select");
        //为数量输入框添加事件，计算金额小计，并更新总计
        $(input).keyup(function() {
            var val = parseInt($(this).val());
            if (isNaN(val) || (val < 1)) { $(this).val("1"); }
            getSubTotal(
                $(this).parent().parent().parent().parent()
            ); //tr element
            getTotal();
        });
        //为数量调整按钮、删除添加单击事件，计算金额小计，并更新总计
        $(this).click(function() {
            var val = parseInt($(input).val());
            if (isNaN(val) || (val < 1)) { val = 1; }
            if ($(window.event.srcElement).hasClass("minus")) {
                if (val > 1) val--;
                input.val(val);
                updateAmount(input);
                getSubTotal(this);
            }else if ($(window.event.srcElement).hasClass("plus")) {
                if (val < 9999) val++;
                input.val(val);
                updateAmount(input);
                getSubTotal(this);
            }else if ($(window.event.srcElement).hasClass("delete")) {
                if (confirm("确定要从购物车中删除此商品？")) {
                    $.ajax({
                        url: '/cart/delete',
                        type: 'get',
                        data:{'id':$(this).attr('id')},
                        dataType:"json",
                        async:false,
                        success: function (res) {
                            if(res.msg=="删除成功"){
                                location.reload();
                            }else {
                                alert(res.msg);
                            }
                        },
                        error: function (err) {
                            console.log(err)
                        }
                    });
                }
            }
            getTotal();
        });
        $(input).bind("input propertychange",function () {
            updateAmount($(this));
        })
        $(select).change(function (){
            $.ajax({
                url: '/cart/updateSpecification',
                type: 'get',
                data:{
                    'id':$(this).parent().parent().parent().attr('id'),
                    'specification':$(this).val()
                },
                dataType: "json",
                success: function (res) {
                    console.log("规格"+res.msg);
                },
                error: function (err) {
                    console.log(err)
                }
            });
        })
    });
});
function buy(){
    var totalPrice=$("#priceTotal").text().replace(',','');
    $("input[name=total]").val(totalPrice);
    var temp=[];
    $.each($('input[type=checkbox]:checked'),function (index,element){
        if(element.checked){
            temp[index]=$(element).val();
        }
    })
    $("input[name=idArray]").val(temp);
    $("#orderList").attr('method','post');
    $('#orderList').attr('action','/shop/order');
    $('#orderList').submit();
}