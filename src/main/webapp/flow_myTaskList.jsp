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
            <td>申请类型</td>
            <td>申请人</td>
            <td>申请原因</td>
            <td>申请时间</td>
            <td>操作</td>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${myTasks}" var="t">
        <tr>
            <td>${t.apply.id}</td>
            <td>${t.apply.type}</td>
            <td>${t.user.uname}</td>
            <td>${t.apply.remark}</td>
            <td>${t.apply.applydate}</td>
            <td><a class="layui-btn layui-btn-mini" href="${pageContext.request.contextPath}/process/pageToApplyReply?taskId=${t.task.id}&apply_id=${t.apply.id}">审批处理</a>
                <a class="layui-btn layui-btn-mini" href="${pageContext.request.contextPath}/process/showPng?applyId=${t.apply.id}">查看流转记录</a>
            </td>
        </tr>
        </c:forEach>


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