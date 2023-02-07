<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="../common/static_import.jsp" %>
    <title>审核商品</title>
    <link rel="stylesheet" href="/static/css/admin/checkProduct.css">
    <script src="/static/js/admin/checkProduct.js"></script>
</head>
<body>
<div class="container-fluid">
    <%@ include file="../common/menu.jsp" %>
    <main>
        <section>
            <h1>${admin.username}<a href="/admin/logout">退出</a></h1>
            <table cellspacing="1" class="table" id="test">
                <thead>
                <tr class="row">
                    <th scope="col" class="col-lg-2 col-md-4 col-6">标题</th>
                    <th scope="col" class="col-lg-2 col-md-4 col-6">价格</th>
                    <th scope="col" class="col-lg-2 col-md-4 col-6">库存</th>
                    <th scope="col" class="col-lg-2 col-md-4 col-6">创建时间</th>
                    <th scope="col" class="col-lg-2 col-md-4 col-6">所属店铺</th>
                    <th scope="col" class="col-lg-2 col-md-4 col-6">操作</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
            <ul class="pagination" id="btnlist">
            </ul>
        </section>
        <div class="checkWindow">
        </div>
    </main>
</div>
</body>
</html>
