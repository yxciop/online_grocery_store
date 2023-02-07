$(function (){
    var pages=$('li[name=pages]');
    function getQueryString(name){
        var reg=new RegExp("(^|&)"+name+"=([^&]*)(&|$)");
        var r=window.location.search.substr(1).match(reg);
        if(r!=null) return unescape(r[2]); return null;
    }
    var pageNum=getQueryString("pageNum");
    if(pageNum==null){
        pages.eq(0).addClass("active");
    }else{
        pages.eq(pageNum-1).addClass("active");
    }
    var  tds=$("tbody>tr>td:nth-last-child(3)");
    $.each(tds,function(index,element) {
        if($(element).text()=="未审核"){
            $(element).parent().addClass("table-warning");
        }
        if($(element).text()=="审核不通过"){
            $(element).parent().addClass("table-danger");
        }
        if($(element).innerText=="审核通过"){
            $(element).parent().addClass("table-success");
        }
    });
})
function del(id){
    if(confirm("请确认是否删除")){
        window.location.href="/seller/deleteProduct?id="+id;
    }
}
function update(id){
    window.location.href="/seller/toUpdateProductInfo?id="+id;
}