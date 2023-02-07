$(function () {
    $(".carousel-item").eq(0).addClass("active");
    let pages=$('li[name=pages]');
    function getQueryString(name){
        var reg=new RegExp("(^|&)"+name+"=([^&]*)(&|$)");
        var r=window.location.search.substr(1).match(reg);
        if(r!=null) return unescape(r[2]); return null;
    }
    let pageNum=getQueryString("pageNum");
    if(pageNum==null){
        pages.eq(0).addClass("active");
    }else{
        pages.eq(pageNum-1).addClass("active");
    }
})