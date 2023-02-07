function verifyPhone(phone) {
    var phoneRegex = /^1(3[0-9]|4[5,7]|5[0,1,2,3,5,6,7,8,9]|6[2,5,6,7]|7[0,1,7,8]|8[0-9]|9[1,8,9])\d{8}$/;
    var flag=phoneRegex.test(phone);
    if(!flag){
        alert("请输入正确的手机号");
    }
    return flag;
}
function verifyRealname(realname){
    var realnameRegex= /^(([一-龥]{2,8})|([a-zA-Z]{2,16}))$/;
    var flag=realnameRegex.test(realname);
    if(!flag){
        alert("请输入正确的名字格式");
    }
    return flag;
}
function verifySpaceOrNull(dataType,obj,str){
    var length=str.length;
    if(length>0){
        if(str.trim().length<length){
            alert(dataType+"不允许存在空格");
            if(obj!=null){
                obj.text(dataType+"不能有空格");
            }
            return false;
        }
    }else{
        alert(dataType+"不能为空");
        if(obj!=null){
            obj.text(dataType+"不能为空");
        }
        return false;
    }
    return true;
}
function verifyPic(path){
    if(path.length!=0){
        var extStart = path.lastIndexOf('.'),
            ext = path.substring(extStart, path.length).toUpperCase();
        if (ext !== '.PNG' && ext !== '.JPG' && ext !== '.JPEG' && ext !== '.GIF') {
            alert("图片文件格式不正确!");
            return false;
        }
    }
    return  true;
}
function checkPicSize(fileObj){
    var filesize = fileObj[0].files[0].size;
    if(filesize>5*1024*1024){
        alert("图片文件大小不能超过5M");
        fileObj.val("");
        return false;
    }
    return true;
}
function verifyDataLength(data,dataType,small,large,obj){
    if(data.length>large||data.length<small){
        alert(dataType+"长度必须在"+small+"到"+large+"之间");
        if(obj!=null){
            obj.text(dataType+"长度必须在"+small+"到"+large+"之间");
        }
        return false;
    }
    return true;
}
function verifyPrice(price,obj) {
    var priceReg = /(^[1-9]\d*(\.\d{1,2})?$)|(^0(\.\d{1,2})?$)/;
    if (!priceReg.test(price)){
        alert("请输入正确的商品价格格式:整数或者保留两位小数");
        if(obj!=null){
            obj.text("商品价格格式不正确");
        }
        return false;
    }else{
        return true;
    }
}
