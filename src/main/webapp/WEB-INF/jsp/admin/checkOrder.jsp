<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <%@ include file="../common/static_import.jsp" %>
    <title>管理员中心</title>
    <script src="/static/js/admin/checkOrder.js"></script>
</head>
<body>
<div class="container-fluid">
    <%@ include file="../common/menu.jsp" %>
    <main>
        <section>
            <h1>${admin.username}<a href="/admin/logout">退出</a></h1>
            <div id="orderContent">
            </div>
            <ul class="pagination" id="btnlist">
            </ul>
            <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true" style="margin-top: 30px">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4 class="modal-title" id="exampleModalLabel">订单反馈信息</h4>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </main>
</div>
</body>
</html>
