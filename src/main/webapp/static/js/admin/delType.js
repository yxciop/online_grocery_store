$(function () {
    showTable(1);

})
function showTable(pageNum){
    $.ajax({
        url:'/admin/queryTypeList',
        data:{"pageNum":pageNum},
        dataType:'json',
        success:function (data){
            var table="";
            $.each(data.pageInfo.list,function (index,element) {
                var o="";
                if(element.soncode==1){
                    o="有";
                }else {
                    o="无";
                }
                table+="<tr class=\"row\"><td class=\"col-lg-2 col-md-4 col-6\">"+element.id+"</td><td class=\"col-lg-2 col-md-4 col-6\">"+element.stype+"</td> <td class=\"col-lg-2 col-md-4 col-6\">"+element.parentId+"</td> <td class=\"col-lg-2 col-md-4 col-6\">"+o+"</td> <td class=\"col-lg-2 col-md-4 col-sm-6 col-6\">"+element.parentType+"</td> <td class=\"col-lg-2 col-md-4 col-sm-6 col-6\"><button class=\"btn btn-success\" onclick=\"del("+element.id+","+element.parentId+","+element.soncode+")\">删除</button></td>";
            })
            $("#test>tbody").html(table);
            var btns="";
            if(data.pageInfo.hasPreviousPage){
                btns+="<li class=\"page-item\"><a onclick=\"showTable(1)\" class=\"page-link\">首页</a></li><li class=\"prev page-item\"> <a onclick=\"showTable("+data.pageInfo.prePage+")\" class=\"page-link\">&lt;</a></li>";
            }
            $.each(data.pageInfo.navigatepageNums,function (index,element) {
                btns+="<li  class=\"page-item pages\"><a onclick=\"showTable("+element+")\" class=\"page-link\">"+element+"</a></li>";
            })
            if(data.pageInfo.hasNextPage){
                btns+="<li class=\"next page-item\"><a onclick=\"showTable("+data.pageInfo.nextPage+")\"  class=\"page-link\">&gt;</a></li>";
            }
            btns+="<li class=\"page-item\"><a onclick=\"showTable("+data.pageInfo.pages+")\"  class=\"page-link\">尾页</a></li>";
            $("#btnlist").html(btns);
            var pages=$(".pages");
            $.each(pages,function (index,element) {
                if($(element).text()==pageNum){
                    $(element).addClass("active");
                }
            })
            var parents="";
            $.each(data.parentList,function (index,element) {
                parents+="<tr class=\"row\"><td class=\"col-3\">"+element.id+"</td><td class=\"col-3\">"+element.stype+"</td><td class=\"col-3\">"+element.parentId+"</td><td class=\"col-3\">";
                if(element.soncode==0){
                    parents+="无"
                }else{
                    parents+="有";
                }
                parents+="</td></tr>";
                $("#parentTab").html(parents);
            })

        },error:function (e){
            console.log(e);
        }
    });
}
function del(id,parentId,soncode){
    if(confirm("是否确认删除")){
        $.ajax({
            url:'/admin/delType',
            data:{"id":id,"parentId":parentId,"soncode":soncode},
            dataType: 'json',
            success:function (data){
                alert(data.msg);
                if(data.msg=="删除成功"){
                    showTable(1);
                }
            }
        });
    }
}