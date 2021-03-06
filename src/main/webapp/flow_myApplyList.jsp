<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/19
  Time: 22:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>滴答办公系统-审批流程管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/media/layui/css/layui.css" media="all">
    <script src="${pageContext.request.contextPath}/media/js/jquery.min.js"></script>
</head>
<body>
<div class="layui-container">
    <table class="layui-table" id="tbdata" lay-filter="tbop">
        <thead>
        <tr>
            <td>序号</td>
            <td>标题</td>
            <td>申请人</td>
            <td>申请时间</td>
            <td>当前状态</td>
            <td>操作</td>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${myApplies}" var="my">
            <tr>
                <td>${my.id}</td>
                <td>${my.remark}</td>
                <td>${sessionScope.user.uname}</td>
                <td>${my.applydate}</td>
                <c:if test="${my.status=='通过审批'}">
                    <td style="color:green">${my.status}</td>
                </c:if>
                <c:if test="${my.status=='审批中'}">
                    <td style="color:blue">${my.status}</td>
                </c:if>
                <c:if test="${my.status=='未通过'}">
                    <td style="color:red">${my.status}</td>
                </c:if>
                <td><a class="layui-btn layui-btn-mini" href="${pageContext.request.contextPath}/process/pageToApplyInfo/${my.id}">查看申请信息</a>
                    <a class="layui-btn layui-btn-mini" href="${pageContext.request.contextPath}/process/flowRecord/${my.id}">查看流转记录</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

</div>
<script src="${pageContext.request.contextPath}/media/layui/layui.js"></script>

<script type="text/javascript">
    function deletePdList(){
        layui.use('table', function() {
            layer.confirm('是否确认删除该流程图?',function(index) {
                layer.msg("删除成功", {icon : 6});
                layer.msg("删除失败", {icon : 5});
            });
        });
    }
</script>


</body>
</html>