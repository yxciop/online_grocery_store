$(function () {
    $("#btn1").click(function (){
        $(".type1").show();
        $(".choose").hide();
    });
    $("#btn2").click(function (){
        $(".type2").show();
        $(".choose").hide();
    });
    $("#btn3").click(function (){
        $(".type3").show();
        $(".choose").hide();
    })
    $(".return").click(function () {
        $(".choose").show();
        $(this).parent().parent().hide();
    })
    $("#addType1").click(function (){
        var stype=$("input[name=stype1]").val();
        if(stype.length!=stype.trim().length){
            alert('分类不允许空格')
        }
        if(stype.trim().length>8){
            alert("分类长度限定8字符以下");
        }else {
            $.ajax({
                url:'/admin/addType1',
                data:{'stype':stype,'idety':1},
                dataType:'json',
                success:function (data){
                    if(data.msg=='addsuccess'){
                        location.reload();
                        alert("添加成功");
                    }
                },error:function (e){
                    alert("添加失败");
                }
            })
        }
    })
    $("#addType2").click(function (){
        var stype=$("input[name=stype2]").val();
        var parentId=$("select[name=type_1]").val();
        if(parentId==0){
            alert("请选择一级分类");
        }
        if(stype.length!=stype.trim().length){
            alert('分类不允许空格')
        }
        if(stype.trim().length>8){
            alert("分类长度限定8字符以下");
        }else {
            $.ajax({
                url:'/admin/addType2||3',
                data:{'stype':stype,'idety':2,'parentId':parentId},
                dataType:'json',
                success:function (data){
                    if(data.msg=='addsuccess'){
                        location.reload();
                        alert("添加成功");
                    }
                },error:function (e){
                    alert("添加失败");
                }
            })
        }
    })
    $("#addType3").click(function (){
        var stype=$("input[name=stype3]").val();
        var parentId=$("select[name=type_2]").val();
        if(parentId==0){
            alert("请选择二级分类");
        }
        if(stype.length!=stype.trim().length){
            alert('分类不允许空格')
        }
        if(stype.trim().length>8){
            alert("分类长度限定8字符以下");
        }else {
            $.ajax({
                url:'/admin/addType2||3',
                data:{'stype':stype,'idety':3,'parentId':parentId},
                dataType:'json',
                success:function (data){
                    if(data.msg=='addsuccess'){
                        location.reload();
                        alert("添加成功");
                    }
                },error:function (e){
                    alert("添加失败");
                }
            })
        }
    })
    $(".stype1").on('change',function(){
        queryShopType($(".stype2"),$(this).val(),"二级分类");
    });
    function queryShopType(obj,parentId,ht) {
        $.ajax({
            url: '/type/query/' + parentId,
            type: 'get',
            dataType: "json",
            async:false,
            success: function (res) {
                var str="<option>"+ht+"</option>";
                for (var i = 0; i < res.length; i++) {
                    str+="<option value='"+res[i].id+"'>"+res[i].stype+"</option>";
                }
                obj.html(str);
            },
            error: function (err) {
                console.log(err)
            }
        });
    }
})