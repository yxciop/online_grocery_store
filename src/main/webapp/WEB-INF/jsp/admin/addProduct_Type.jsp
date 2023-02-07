<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <%@ include file="../common/static_import.jsp" %>
    <title>添加商品分类</title>
    <link rel="stylesheet" href="/static/css/admin/addType.css">
    <script src="/static/js/admin/addType.js"></script>
</head>
<body>
<div class="container-fluid">
    <%@ include file="../common/menu.jsp" %>
    <main>
        <section>
            <h1>${admin.username}<a href="/admin/logout">退出</a></h1>
            <div class="choose">
                <button class="btn btn-info" id="btn1">添加一级分类</button>
                <button class="btn btn-primary" id="btn2">添加二级分类</button>
                <button class="btn btn-warning" id="btn3">添加三级分类</button>
            </div>
            <div class="type1">
                <div class="form-group">
                    <input class="form-control" type="text" placeholder="一级分类名称" name="stype1" required="required" />
                    <button class="btn btn-success" id="addType1">添加</button>
                    <button class="btn btn-warning return">返回</button>
                </div>
            </div>
            <div class="type2">
                <div class="form-group">
                    <select name="type_1" class="custom-select">
                        <option value="0">一级分类</option>
                        <c:forEach items="${types}" var="type">
                            <option value="${type.id}">${type.stype}</option>
                        </c:forEach>
                    </select>
                    <input class="form-control" type="text" placeholder="二级分类名称" name="stype2" required="required" />
                    <button class="btn btn-success" id="addType2">添加</button>
                    <button class="btn btn-warning return">返回</button>
                </div>
            </div>
            <div class="type3">
                <div class="form-group">
                    <select name="type_1"　 class="custom-select stype1">
                        <option value="0">一级分类</option>
                        <c:forEach items="${types}" var="type">
                            <option value="${type.id}">${type.stype}</option>
                        </c:forEach>
                    </select>
                    <select name="type_2" class="custom-select stype2">
                        <option value="0">二级分类</option>
                    </select>
                    <input class="form-control" type="text" placeholder="三级分类名称" name="stype3" required="required" />
                    <button class="btn btn-success" id="addType3">添加</button>
                    <button class="btn btn-warning return">返回</button>
                </div>
            </div>
        </section>
    </main>
</div>


</body>
</html>
