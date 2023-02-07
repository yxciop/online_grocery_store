let imgsrc=new Array();
$(function (){
    var $summernote=$('#summernote').summernote({
        toolbar: [
            ['style', ['style']],
            ['font', ['bold', 'italic', 'underline','strikethrough', 'superscript', 'subscript']],
            ['color', ['color']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['height', ['height']],
            ['table', ['table']],
            ['insert', ['link', 'picture', 'hr']],
            ['view', ['fullscreen', 'codeview']]
        ],
        placeholder: '编辑界面',
        height: 300,
        focus:true,
        callbacks: {
            onImageUpload: function (files){
                uploadFile($summernote,files[0]);
            },onChange:function (target){
                var nowimgs = getSrc(target);
                // html 即变化之后的内容
                var arr1=getArrDifference(imgsrc,nowimgs);
                var arr2=new Array();
                arr2=getArrEqual(imgsrc,arr1);
                console.log("imgsec:"+imgsrc);
                console.log("nowings:"+nowimgs);
                console.log("arr1:"+arr1);
                console.log("arr2:"+arr2);
                if(arr2.length>0){
                    removeFile(arr2);
                }
            }
        }
    });

    function uploadFile($summernote,file){
        var fd=new FormData();
        fd.append("file",file);
        $.ajax({
            url:"/upload",
            data:fd,
            cache:false,
            contentType:false,
            processData:false,
            async:false,
            type:"post",
            success:function (data){
                imgsrc.push(data);
                $summernote.summernote('insertImage',data,'img');
            }
        })
    }
    function getArrDifference(arr1, arr2) {
        return arr1.concat(arr2).filter(function(v, i, arr) {
            return arr.indexOf(v) === arr.lastIndexOf(v);
        });
    }
    function getArrEqual(arr1, arr2) {
        let newArr = [];
        for (let i = 0; i < arr2.length; i++) {
            for (let j = 0; j < arr1.length; j++) {
                if(arr1[j] === arr2[i]){
                    newArr.push(arr1[j]);
                }
            }
        }
        return newArr;
    }
    function removeFile(target){
        var imgsrc=target;
        $.post('/deletePics',{
            "imgSrcs":JSON.stringify(imgsrc)
        },function (data){
            console.log(data);
        })
    }
})
function getSrc (html) {
    var imgReg = /<img.*?(?:>|\/>)/gi
    // 匹配src属性
    var srcReg = /src=[\\"]?([^\\"]*)[\\"]?/i
    var arr = html.match(imgReg)
    let imgs = new Array();
    if (arr) {
        for (let i = 0; i < arr.length; i++) {
            var src = arr[i].match(srcReg)[1]
            imgs.push(src)
        }
    }
    return imgs;
}
function setContent(html){
    imgsrc=getSrc(html);
    $('#summernote').summernote('code',html);
}
