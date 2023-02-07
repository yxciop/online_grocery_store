$(function(){
    new Swipe(document.getElementById('banner_box'), {
        speed: 500,
        auto: 3000,
        callback: function(){
            var lis = $(this.element).next("ol").children();
            lis.removeClass("on").eq(this.index).addClass("on");
        }
    });
    $("#a").Spinner({value:1, min:0, len:3, max:10000});
    $('.addshop_cat').click(function () {
        $("input[name=amount]").eq(0).val($(".Amount").val());
        if($("input[name=specification]").length>1){
            $("#fo>input[name=specification]").val($("input[name=specification]:checked").val());
        }
        $.ajax({
            url: '/cart/addCart/',
            type: 'post',
            data: $("#fo").serialize(),
            dataType: "json",
            async:false,
            success: function (res) {
                alert(res.msg);
            },error: function (err) {
                console.log(err)
            },complete:function (xhr,status) {
                var REDIRECT=xhr.getResponseHeader("REDIRECT");
                if(REDIRECT=="REDIRECT"){
                    document.location.href=xhr.getResponseHeader("CONTENTPATH");
                }
            }
        });
    });
    $(".buy").click(function () {
        var specification=$("input[name=specification]:checked").val();
        var amount=$(".Amount").val();
        window.location.href="/shop/order?product_id="
            +$("#productid").val()+"&sellerId="+$("#sellerid").val()
            +"&specification="+specification+"&amount="+amount+"&buyerId="+$("#buyerid").val();
    })
    $(".spec_select ul li em").click(function(){
        $(this).addClass("click").siblings().removeClass("click");
    })
    $('.box_list ul li').click(function(){
        var index = $('.box_list ul li').index(this);
        $(this).addClass('current').siblings('li').removeClass('current');
        $('.box_list .goods_box:eq('+index+')').show().siblings('.goods_box').hide();
    })
});