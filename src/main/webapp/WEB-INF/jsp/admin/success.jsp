<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <%@ include file="../common/static_import.jsp" %>
    <title>管理员中心</title>
</head>
<body>
<div class="container-fluid">
    <%@ include file="../common/menu.jsp" %>
    <main>
        <section>
            <h1>${admin.username}<a href="/admin/logout">退出</a></h1>
            <c:forEach items="${products}" var="product">
                <p>店铺＂${product.store}＂上架了商品:${product.title},未审核.<a href="/admin/checkProduct">审核</a></p>
            </c:forEach>
        </section>
    </main>
</div>
</body>
</html>
