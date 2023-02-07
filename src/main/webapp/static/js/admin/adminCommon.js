$(function() {

    var nav = $('nav'),
        menu = $('nav h1'),
        main = $('main'),
        open = false,
        hover = false;

    menu.on('click', function() {
        open = !open ? true : false;
        nav.toggleClass('menu-active');
        main.toggleClass('menu-active');
        nav.removeClass('menu-hover');
        main.removeClass('menu-hover');
        console.log(open);
    });
    menu.hover(
        function() {
            if (!open) {
                nav.addClass('menu-hover');
                main.addClass('menu-hover');
            }
        }, function() {
            nav.removeClass('menu-hover');
            main.removeClass('menu-hover');
        }
    );
    $("#addProductType").click(function(){
        window.location.href="/admin/addProductType";
    });
    $("#delProductType").click(function(){
        window.location.href="/admin/delProductType";
    });
    $("#checkProduct").click(function (){
        window.location.href="/admin/checkProduct";
    });
    $("#checkOrder").click(function(){
        window.location.href="/admin/checkOrder";
    });
    $("#checkBuyer").click(function(){
        window.location.href="/admin/checkBuyer";
    });
    $("#checkSeller").click(function (){
        window.location.href="/admin/checkSeller";
    });
});