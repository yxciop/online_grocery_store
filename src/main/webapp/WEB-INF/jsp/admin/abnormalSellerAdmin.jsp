<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <%@ include file="../common/static_import.jsp" %>
    <title>异常卖家处理界面</title>
    <script src="/static/js/admin/sellerAdmin.js"></script>
</head>
<body>
<div class="container-fluid">
    <%@ include file="../common/menu.jsp" %>
    <main>
        <section>
            <h1>${admin.username}<a href="/admin/logout">退出</a></h1>
            <table class="table table-hover" id="sellerTable">
                <thead>
                   <tr class="row">
                       <th class="col-3">用户id</th>
                       <th class="col-3">用户名</th>
                       <th class="col-3">状态</th>
                       <th class="col-3">操作</th>
                   </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
            <ul class="pagination" id="btnlist">
            </ul>
            <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true" style="margin-top: 30px">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4 class="modal-title" id="exampleModalLabel">卖家状态信息</h4>
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
