$(document).ready(function(){
    if(!$.isEmptyObject($("#username").val())){
        $(".card-wrap").hide();
        $("#openWin").text("退出");
    }
    var cards=$('.card');
    var btns=$('.js-btn');
    var inputs=$('.form-control');
    function on_btn_click(el){
        var nextID=el.currentTarget.getAttribute('data-target');
        var next=$('#'+nextID);
        if(!next){
            return;
        }else{
            bg_change(nextID);
            view_change(next);
            return false;
        }
    }
    function bg_change(next){
        $('body').attr('class','');
        $('body').addClass('is-'+next);
    }
    function view_change(next){
        $.each(cards,function(index,element) {
            $(element).removeClass('is-show');
        });
        next.addClass('is-show');
    }
    $.each(btns,function(index,element) {
        $(element).bind('click',function(event){
            on_btn_click(event);
        });
        $(element).bind('touch',function(event){
            on_btn_click(event);
        });
    });
    $.each(inputs,function(index,element) {
        $(element).bind('input propertychange',function(event){
            $(element).prev().text("");
        });
    });
    $("#head_portrait_file").change(function () {
        // 获取上传文件对象
        var file = $(this)[0].files[0];
        // 读取文件URL
        var reader = new FileReader();
        reader.readAsDataURL(file);
        // 阅读文件完成后触发的事件
        reader.onload = function () {
            // 读取的URL结果：this.result
            $("#head_portrait").attr("src", this.result);
        }
    });
    $("#newHead_portrait_file").change(function () {
        // 获取上传文件对象
        var file = $(this)[0].files[0];
        // 读取文件URL
        var reader = new FileReader();
        reader.readAsDataURL(file);
        // 阅读文件完成后触发的事件
        reader.onload = function () {
            // 读取的URL结果：this.result
            $("#newHead_portrait").attr("src", this.result);
        }
    });
    $("#navbarDropdown").bind('mouseover',function () {
        $(this).addClass("shopType");
        $(".dropdown-menu").show();
    });
    $("#navbarDropdown").bind('click',function () {
        if($(".dropdown-menu").is(":hidden")){
            $(".dropdown-menu").show();
        }else{
            $(".dropdown-menu").hide();
        }
    })
    $("#openWin").bind('click',function () {
        if($("#openWin").text()=="登录") {
            $(".card-wrap").show();
        }else{
            logout();
        }
    })
    $("#log_btn").bind('click',function (){
        var username=$("#loginUserName").val();
        var password=$("#loginPassWord").val();
        var flag1=verifySpaceOrNull("用户名",$("#lg_username"),username);
        var flag2=verifySpaceOrNull("密码",$("#lg_password"),password);
        if(flag1&&flag2){
            login();
        }
    });
    $("#reg_btn").bind('click',function (){
        var username=$("#regUsername").val();
        var password=$("#regPassword").val();
        var phonenumber=$("#regPhonenumber").val();
        var realname=$("#regRealname").val();
        var nickname=$("#regNickname").val();
        var flag1=verifySpaceOrNull("用户名",$("#rUsernameMsg"),username);
        var flag2=verifySpaceOrNull("新密码",$("#rPasswordMsg"),password);
        var flag3=verifySpaceOrNull("手机号码",$("#rPhoneMsg"),phonenumber);
        var flag4=verifySpaceOrNull("姓名",$("#rRealnameMsg"),realname);
        var flag5=verifySpaceOrNull("昵称",$("#rNicknameMsg"),nickname);
        var flag6=true;
        if($("#head_portrait_file").val().length>0){
            flag6=verifyPic($("#head_portrait_file").val());
            if(flag6){
                flag6=checkPicSize($("#head_portrait_file"));
            }
        }
        if(flag1){
            flag1=verifyDataLength(username,"用户名",5,20,$("#rUsernameMsg"));
        }
        if(flag3){
            flag3=verifyPhone(phonenumber);
            if(!flag3){
                $("#rPhoneMsg").text("手机号码格式不正确");
            }
        }
        if(flag4){
            flag4=verifyRealname(realname);
            if(!flag4){
                $("#rRealnameMsg").text("姓名格式不正确");
            }
        }
        if(flag5){
            flag5=verifyDataLength(nickname,"昵称",4,20,$("#rNicknameMsg"));
        }
        if(flag1&&flag2&&flag3&&flag4&&flag5&&flag6){
            reg();
        }
    });
    $("#changePassword").bind('click',function () {
        var username=$("#findUserName").val();
        var password=$("#findPassword").val();
        var phonenumber=$("#findPhonenumber").val();
        var realname=$("#findRealname").val();
        var flag1=verifySpaceOrNull("用户名",$("#fUsernameMsg"),username);
        var flag2=verifySpaceOrNull("新密码",$("#fNewPasswordMsg"),password);
        var flag3=verifySpaceOrNull("手机号码",$("#fPhonenumberMsg"),phonenumber);
        var flag4=verifySpaceOrNull("姓名",$("#fRealnameMsg"),realname);
        if(flag3){
            flag3=verifyPhone(phonenumber);
            if(!flag3){
                $("#fPhonenumberMsg").text("手机号码格式不正确");
            }
        }
        if(flag4){
            flag4=verifyRealname(realname);
            if(!flag4){
                $("#fRealnameMsg").text("姓名格式不正确");
            }
        }
        if(flag1&&flag2&&flag3&&flag4){
            findAndChange();
        }
    });
    $("#updateInfo").bind('click',function (){
        var phone=$("#updatePhonenumber").val();
        var pass=$("#updatePassword").val();
        var nick=$("#updateNickname").val();
        var flag1=verifySpaceOrNull("手机号码",$("#newPhoneNumberMsg"),phone);
        var flag2=verifySpaceOrNull("密码",$("#newPasswordmsg"),pass);
        var flag3=verifySpaceOrNull("昵称",$("#newNickMsg"),nick);
        var flag4=true;
        if($("#newHead_portrait_file").val().length>0){
            flag4=verifyPic($("#newHead_portrait_file").val());
            if(flag4){
                flag4=checkPicSize($("#newHead_portrait_file"));
            }
        }
        if(flag1){
            flag1=verifyPhone(phone);
            if(!flag1){
                $("#newPhoneNumberMsg").text("手机号码格式不正确");
            }
        }
        if(flag3){
            flag3=verifyDataLength(nick,"昵称",4,20,$("#newNickMsg"));
        }
        if(flag1&&flag2&&flag3&&flag4){
            updateInfo();
        }
    })
    function login(){
        $.ajax({
            url:'/buyer/login',
            type:'post',
            dataType:'json',
            data:$("#log").serialize(),
            success:function(result){
                $("#lg_username").text(result.login_Username_msg);
                $("#lg_password").text(result.login_Password_msg);
                if(result.login_msg!=null){
                    alert(result.login_msg);
                    if(result.login_msg=="登录成功"){
                        window.location.reload();
                    }
                }
            },
            error:function(err){
                console.log(err)
            }
        });
    }
    function logout(){
        $.ajax({
            url:'/buyer/logout',
            type:'get',
            success:function(result){
                alert(result)
                location.reload();
            },
            error:function(err){
                console.log(err)
            }
        });
    }
    function updateInfo(){
        var formdata=new FormData(document.getElementById("upd"));
        $.ajax({
            url:'/buyer/updateInfo',
            type:'post',
            contentType:false,
            data:formdata,
            processData:false,
            success:function(result){
                $("#newPhoneNumberMsg").text(result.phoneNumberMsg);
                if(result.update_msg=="修改成功"){
                    alert(result.update_msg);
                    location.reload();
                }
            },
            error:function(err){
                console.log(err)
            }
        });
    }
    function findAndChange(){
        $.ajax({
            url:'/buyer/find',
            type:'post',
            dataType: "json",
            data:$("#findForm").serialize(),
            success:function(result){
                $("#fUsernameMsg").text(result.userFail);
                $("#fPhonenumberMsg").text(result.phoneFail);
                $("#fRealnameMsg").text(result.nameFail);
                if(result.change_msg=="密码修改成功"){
                    alert(result.change_msg);
                    $(".btn-login").trigger("click");
                }
            },
            error:function(err){
                console.log(err)
            }
        });
    }
    function reg(){
        var formdata=new FormData(document.getElementById("reg"));
        $.ajax({
            url:'/buyer/register',
            type:'post',
            contentType:false,
            data:formdata,
            processData:false,
            success:function(result){
                $("#rUsernameMsg").text(result.username_msg);
                $("#rPhoneMsg").text(result.phone_msg);
                if(result.reg_msg=="注册成功"){
                    alert(result.reg_msg);
                    $(".btn-login").trigger("click");
                }
            },
            error:function(err){
                console.log(err)
            }
        });
    }
    var res=queryShopType($("#dropdownMenu1"),0);
    if (res==1){
        var submenu=$(".dropdown-submenu");
        var i=0;
        $("#dropdownMenu1").bind('click',function () {
            if(i==0){
                $(".open").children('.dropdown-menu').show();
                i=1;
            }else{
                $(".open").children('.dropdown-menu').hide();
                i=0;
            }
        });
    }
    function queryShopType(obj,parentId) {
        var status=0;
       if(obj.siblings().length==0){
           $.ajax({
               url: '/type/query/' + parentId,
               type: 'get',
               dataType: "json",
               async:false,
               success: function (res) {
                   if(res.length!=0){
                       var str = "<div class='dropdown-menu'>";
                       for (var i = 0; i < res.length; i++) {
                           if(res[i].soncode==0){
                               str += "<a class='dropdown-item'  data-set='" + res[i].id + "' href='/shop/list?id="+res[i].id+"&idety="+res[i].idety+"'>"+res[i].stype+"</a>";
                           }else{
                               str += "<div class='dropdown-submenu'><a class='dropdown-item'  data-set='" + res[i].id + "' href='/shop/list?id="+res[i].id+"&idety="+res[i].idety+"'>"+res[i].stype+"</a></div>";
                           }
                       }
                       str+="</div>";
                       obj.after(str);
                       status=1;
                       var submenu=$(".dropdown-submenu");
                       $.each(submenu,function (index,element){
                           $(element).on('mouseover',function (e) {
                               queryShopType($($(this).children().get(0)),$($(this).children().get(0)).attr("data-set"));
                           })
                       })
                   }
               },
               error: function (err) {
                   console.log(err)
               }
           });
       }
        return status;
    }

    $(".carousel-inner>.carousel-item").eq(0).addClass("active");
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
    var getUrlParam = function(name) {
        var reg = new RegExp("(^|&)"+
            name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r!=null) return unescape(r[2]); return null;
    }
    var id=getUrlParam("id");
    var idety=getUrlParam("idety");
    var keyword=getUrlParam("keyword");
    var condition=getUrlParam("condition");
    var sort=decodeURI(escape(getUrlParam("sort")));
    if(id==null){
        id="";
    }
    if(idety==null){
        idety="";
    }
    if(keyword==null){
        keyword="";
    }
    if(condition=="price"&&sort=="up"){
        $("#price").find("option[value='asc']").attr("selected",true);
    }else if(condition=="price"&&sort=="down"){
        $("#price").find("option[value='desc']").attr("selected",true);
    }else if(condition=="modify"&&sort=="up"){
        $("#time").find("option[value='asc']").attr("selected",true);
    }else if(condition=="modify"&&sort=="down"){
        $("#time").find("option[value='desc']").attr("selected",true);
    }else if(condition=="sold"&&sort=="up"){
        $("#sold").find("option[value='asc']").attr("selected",true);
    }else if(condition=="sold"&&sort=="down"){
        $("#sold").find("option[value='desc']").attr("selected",true);
    }
    $("#price").change(function () {
        var value=$(this).val();
        if(value=="asc"){
            window.location.href="/shop/list?id="
                +id+"&idety="+idety+"&keyword="+keyword+
                "&condition=price&sort=up";
        }else{
            window.location.href="/shop/list?id="
                +id+"&idety="+idety+"&keyword="+keyword+
                "&condition=price&sort=down";
        }
    })
    $("#time").change(function () {
        var value=$(this).val();
        if(value=="asc"){
            window.location.href="/shop/list?id="
                +id+"&idety="+idety+"&keyword="+keyword+
                "&condition=modify&sort=up";
        }else{
            window.location.href="/shop/list?id="
                +id+"&idety="+idety+"&keyword="+keyword+
                "&condition=modify&sort=down";
        }
    })
    $("#sold").change(function () {
        var value=$(this).val();
        if(value=="asc"){
            window.location.href="/shop/list?id="
                +id+"&idety="+idety+"&keyword="+keyword+
                "&condition=sold&sort=up";
        }else{
            window.location.href="/shop/list?id="
                +id+"&idety="+idety+"&keyword="+keyword+
                "&condition=sold&sort=down";
        }
    })
})


