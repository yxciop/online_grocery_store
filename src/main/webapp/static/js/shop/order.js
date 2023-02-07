$(function () {
    $('#addAddress').on('show.bs.modal', function (event) {
        var modal = $(this);
        modal.find('.modal-footer button.btn-primary').eq(0).bind('click',function () {
            $.ajax({
                url: '/address/addAddress',
                type: 'post',
                data: $("#FormAddress").serialize(),
                dataType: "json",
                async:false,
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
        })
    })
})
function pay(){
    if($("body").find("#idArray").length>0){
        var obj1=$("#idArray").val();
        $("input[name=idArray]").val(obj1);
        $("#payForm").attr('method','post');
        $("#payForm").attr('action','/buyer/buy');
        $("#payForm").submit();
    }else{
        $("input[name=productid]").val($("#productid").val());
        $("#payForm").attr('method','post');
        $("#payForm").attr('action','/buyer/buy');
        $("#payForm").submit();
    }
}