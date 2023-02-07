<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <%@ include file="../common/static_import.jsp" %>
    <title>删除商品分类</title>
    <link rel="stylesheet" href="/static/css/admin/delType.css">
    <script src="/static/js/admin/delType.js"></script>
</head>
<body>
<div class="container-fluid">
    <%@ include file="../common/menu.jsp" %>
    <main>
        <section>
            <h1>${admin.username}<a href="/admin/logout">退出</a></h1>
            <h2>一级分类列表</h2>
            <table cellspacing="1" class="table">
                <thead>
                <tr class="row">
                    <th scope="col" class="col-3">id</th>
                    <th scope="col" class="col-3">分类名称</th>
                    <th scope="col" class="col-3">父级分类id</th>
                    <th scope="col" class="col-3">有无子分类</th>
                </tr>
                </thead>
                <tbody id="parentTab">

                </tbody>
            </table>
            <h2>二三级分类列表</h2>
            <table cellspacing="1" class="table" id="test">
                <thead>
                <tr class="row">
                    <th scope="col" class="col-lg-2 col-md-4 col-6">id</th>
                    <th scope="col" class="col-lg-2 col-md-4 col-6">分类名称</th>
                    <th scope="col" class="col-lg-2 col-md-4 col-6">父级分类id</th>
                    <th scope="col" class="col-lg-2 col-md-4 col-6">有无子分类</th>
                    <th scope="col" class="col-lg-2 col-md-4 col-6">父级分类名称</th>
                    <th scope="col" class="col-lg-2 col-md-4 col-6">操作</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
            <ul class="pagination" id="btnlist">
            </ul>
        </section>
    </main>
</div>


</body>
</html>
