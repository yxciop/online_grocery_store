$(function () {
    $('#updateForm').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget);
        var id= button.data('whatever');
        var modal = $(this);
        $.ajax({
            url: '/address/addressDetail',
            type:'get',
            data:{'id':id},
            dataType: "json",
            async:false,
            success: function (res) {
                modal.find('.modal-body input[name=rname]').val(res.address.rname);
                modal.find('.modal-body input[name=phonenumber]').val(res.address.phonenumber);
                modal.find('.modal-body input[name=address]').val(res.address.address);
                modal.find('.modal-body input[name=id]').val(id);
            },error:function (err) {
                console.log(err)
            }
        })
        modal.find('.modal-footer button.btn-primary').eq(0).bind('click',function () {
            var flag1=verifySpaceOrNull("手机号码",null,$("#phonenumber").val());
            var flag2=verifySpaceOrNull("姓名",null,$("#rname").val());
            var flag3=verifySpaceOrNull("地址",null,$("#address").val());
            if(flag1){
                flag1=verifyPhone($("#phonenumber").val());
            }
            if(flag2){
                flag2=verifyRealname($("#rname").val());
            }
            if(flag1&&flag2&&flag3){
                $.ajax({
                    url: '/address/updateAddress',
                    type: 'post',
                    data: $("#FormAddress").serialize(),
                    dataType: "json",
                    async:false,
                    success: function (res) {
                        if(res.msg=="修改成功"){
                            location.reload();
                        }
                        if(res.msg=='修改失败'){
                            alert(res.msg);
                        }
                    }, error: function (err) {
                        console.log(err)
                    }
                });
            }
        })
    })
})
function add(obj){
    var rname=$(obj).find("input[name=rname]").val();
    var phone=$(obj).find("input[name=phonenumber]").val();
    var address=$(obj).find("input[name=address]").val();
    var flag1=verifySpaceOrNull("手机号码",null,phone);
    var flag2=verifySpaceOrNull("姓名",null,rname);
    var flag3=verifySpaceOrNull("地址",null,address);
    if(flag1){
        flag1=verifyPhone(phone);
    }
    if(flag2){
        flag2=verifyRealname(rname);
    }
    if(flag1&&flag2&&flag3){
        $.ajax({
            url: '/address/addAddress',
            type: 'post',
            data: $(obj).serialize(),
            dataType: "json",
            success: function (res) {
                if(res.msg=="添加成功"){
                    location.reload();
                }else{
                    alert(res.msg);
                }
            }, error: function (err) {
                console.log(err)
            }
        });
    }
}
function updateIsDefault(id,buyerId){
    $.ajax({
        url: '/address/updateDefault',
        type: 'get',
        data: {'id':id,'buyerId':buyerId},
        dataType: "json",
        success: function (res) {
            if(res.msg=="设置成功"){
                location.reload();
            }else{
                alert(res.msg);
            }
        }, error: function (err) {
            console.log(err)
        }
    });
}
function del(id,buyerId){
    $.ajax({
        url: '/address/delAddress',
        type: 'get',
        data: {'id':id,'buyerId':buyerId},
        dataType: "json",
        success: function (res) {
            if(res.msg=="删除成功"){
                location.reload();
            }else{
                alert(res.msg);
            }
        }, error: function (err) {
            console.log(err)
        }
    });
}
