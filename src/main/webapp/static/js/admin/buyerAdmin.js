$(function () {
    showBuyer(1);
})
function showBuyer(pageNum){
    $.ajax({
        url:'/admin/queryAbnormalBuyer',
        data:{"pageNum":pageNum},
        dataType:'json',
        async:false,
        success:function (data){
            if(data.pageInfo.list.length<=0){
                $("#buyerTable>tbody").html("无异常买家");
            }else{
                var table="";
                $.each(data.pageInfo.list,function (index,element) {
                    table+="<tr class='row'>" +
                        "<td class='col-3'>"+element.id+"</td>"+
                        "<td class='col-3'>"+element.username+"</td>"+
                        "<td class='col-3'>"+element.status+"</td>";
                    table+="<td class='col-3'>"
                    if(element.status=="异常"){
                        table+="<button class='btn btn-warning warn' buyerid='"+element.id+"'>警告</button>";
                        table+="<button class='btn btn-primary recover' buyerid='"+element.id+"'>恢复正常</button>";
                    }else if(element.status=="警告"){
                        table+="<button class='btn btn-danger suspend' buyerid='"+element.id+"'>封号</button>";
                    }else if(element.status=="封号"){
                        table+="<button class='btn btn-success resolve' buyerid='"+element.id+"'>解封</button>";
                    }
                    table+="<input type='hidden' name='msg' value='"+element.msg+"' />";
                    table+="<button class='btn btn-info search'  type='button' data-toggle='modal' data-target='#exampleModal'>查看</button>";
                    table+="</td>"+
                        "</tr>";
                })
                $("#buyerTable>tbody").html(table);
                var btns="";
                if(data.pageInfo.hasPreviousPage){
                    btns+="<li class=\"page-item\"><a onclick=\"showBuyer(1)\" class=\"page-link\">首页</a></li><li class=\"prev page-item\"> <a onclick=\"showBuyer("+data.pageInfo.prePage+")\" class=\"page-link\">&lt;</a></li>";
                }
                $.each(data.pageInfo.navigatepageNums,function (index,element) {
                    btns+="<li  class=\"page-item pages\"><a onclick=\"showBuyer("+element+")\" class=\"page-link\">"+element+"</a></li>";
                })
                if(data.pageInfo.hasNextPage){
                    btns+="<li class=\"next page-item\"><a onclick=\"showBuyer("+data.pageInfo.nextPage+")\"  class=\"page-link\">&gt;</a></li>";
                }
                btns+="<li class=\"page-item\"><a onclick=\"showBuyer("+data.pageInfo.pages+")\"  class=\"page-link\">尾页</a></li>";
                $("#btnlist").html(btns);
                var pages=$(".pages");
                $.each(pages,function (index,element) {
                    if($(element).text()==pageNum){
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
        var buyerId=$(this).attr("buyerid");
        $.ajax({
            url:'/admin/warnBuyer',
            data:{'buyerId':buyerId},
            dataType: 'json',
            type:'get',
            success:function (data) {
                if(data.msg=="警告成功"){
                    showBuyer(1);
                }else{
                    alert("警告失败");
                }
            },error:function (data) {
                alert("警告失败");
            }
        })
    })
    $(".recover").on('click',function () {
        var buyerId=$(this).attr("buyerid");
        $.ajax({
            url:'/admin/recoverBuyer',
            data:{'buyerId':buyerId},
            dataType: 'json',
            type:'get',
            success:function (data) {
                if(data.msg=="恢复成功"){
                    showBuyer(1);
                }else{
                    alert("恢复失败");
                }
            },error:function (data) {
                alert("恢复失败");
            }
        })
    })
    $(".suspend").on('click',function () {
        var buyerId=$(this).attr("buyerid");
        $.ajax({
            url:'/admin/suspendBuyer',
            data:{'buyerId':buyerId},
            dataType: 'json',
            type:'get',
            success:function (data) {
                if(data.msg=="封号成功"){
                    showBuyer(1);
                }else{
                    alert("封号失败");
                }
            },error:function (data) {
                alert("封号失败");
            }
        })
    })
    $(".resolve").on('click',function () {
        var buyerId=$(this).attr("buyerid");
        $.ajax({
            url:'/admin/resolveBuyer',
            data:{'buyerId':buyerId},
            dataType: 'json',
            type:'get',
            success:function (data) {
                if(data.msg=="解封成功"){
                    showBuyer(1);
                }else{
                    alert("解封失败");
                }
            },error:function (data) {
                alert("解封失败");
            }
        })
    })
}