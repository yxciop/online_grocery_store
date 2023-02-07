$(function () {
    showSeller(1);
})
function showSeller(pageNum){
    $.ajax({
        url:'/admin/queryAbnormalSeller',
        data:{"pageNum":pageNum},
        dataType:'json',
        async:false,
        success:function (data){
            if(data.pageInfo.list.length<=0){
                $("#sellerTable>tbody").html("无异常卖家");
            }else {
                var table = "";
                $.each(data.pageInfo.list, function (index, element) {
                    table += "<tr class='row'>" +
                        "<td class='col-3'>" + element.id + "</td>" +
                        "<td class='col-3'>" + element.username + "</td>" +
                        "<td class='col-3'>" + element.status + "</td>";
                    table += "<td class='col-3'>"
                    if (element.status == "异常") {
                        table += "<button class='btn btn-warning warn' sellerid='" + element.id + "'>警告</button>";
                        table += "<button class='btn btn-primary recover' sellerid='" + element.id + "'>恢复正常</button>";
                    } else if (element.status == "警告") {
                        table += "<button class='btn btn-danger suspend' sellerid='" + element.id + "'>封号</button>";
                    } else if (element.status == "封号") {
                        table += "<button class='btn btn-success resolve' sellerid='" + element.id + "'>解封</button>";
                    }
                    table += "<input type='hidden' name='msg' value='" + element.msg + "' />";
                    table += "<button class='btn btn-info search'  type='button' data-toggle='modal' data-target='#exampleModal'>查看</button>";
                    table += "</td>" +
                        "</tr>";
                })
                $("#sellerTable>tbody").html(table);
                var btns = "";
                if (data.pageInfo.hasPreviousPage) {
                    btns += "<li class=\"page-item\"><a onclick=\"showSeller(1)\" class=\"page-link\">首页</a></li><li class=\"prev page-item\"> <a onclick=\"showSeller(" + data.pageInfo.prePage + ")\" class=\"page-link\">&lt;</a></li>";
                }
                $.each(data.pageInfo.navigatepageNums, function (index, element) {
                    btns += "<li  class=\"page-item pages\"><a onclick=\"showSeller(" + element + ")\" class=\"page-link\">" + element + "</a></li>";
                })
                if (data.pageInfo.hasNextPage) {
                    btns += "<li class=\"next page-item\"><a onclick=\"showSeller(" + data.pageInfo.nextPage + ")\"  class=\"page-link\">&gt;</a></li>";
                }
                btns += "<li class=\"page-item\"><a onclick=\"showSeller(" + data.pageInfo.pages + ")\"  class=\"page-link\">尾页</a></li>";
                $("#btnlist").html(btns);
                var pages = $(".pages");
                $.each(pages, function (index, element) {
                    if ($(element).text() == pageNum) {
                        $(element).addClass("active");
                    }
                })
            }
        },error:function (e){
            console.log(e);
        }
    });
    $(".search").on('click',function () {
        var msg=$(this).prev().val();
        $(".modal-body").html(msg);
    })
    $(".warn").on('click',function () {
        var sellerId = $(this).attr("sellerid");
        $.ajax({
            url: '/admin/warnSeller',
            data: {'sellerId': sellerId},
            dataType: 'json',
            type: 'get',
            success: function (data) {
                if (data.msg == "警告成功") {
                    showSeller(1);
                } else {
                    alert("警告失败");
                }
            }, error: function (data) {
                alert("警告失败");
            }
        })
    })
    $(".recover").on('click',function () {
        var sellerId = $(this).attr("sellerid");
        $.ajax({
            url: '/admin/recoverSeller',
            data: {'sellerId': sellerId},
            dataType: 'json',
            type: 'get',
            success: function (data) {
                if (data.msg == "恢复成功") {
                    showSeller(1);
                } else {
                    alert("恢复失败");
                }
            }, error: function (data) {
                alert("恢复失败");
            }
        })
    })
    $(".suspend").on('click',function () {
        var sellerId = $(this).attr("sellerid");
        $.ajax({
            url: '/admin/suspendSeller',
            data: {'sellerId': sellerId},
            dataType: 'json',
            type: 'get',
            success: function (data) {
                if (data.msg == "封号成功") {
                    showSeller(1);
                } else {
                    alert("封号失败");
                }
            }, error: function (data) {
                alert("封号失败");
            }
        })
    })
    $(".resolve").on('click',function () {
        var sellerId = $(this).attr("sellerid");
        $.ajax({
            url: '/admin/resolveSeller',
            data: {'sellerId': sellerId},
            dataType: 'json',
            type: 'get',
            success: function (data) {
                if (data.msg == "解封成功") {
                    showSeller(1);
                } else {
                    alert("解封失败");
                }
            }, error: function (data) {
                alert("解封失败");
            }
        })
    })
}