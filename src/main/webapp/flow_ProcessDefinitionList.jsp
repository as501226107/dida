<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/19
  Time: 15:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
            <td>流程名称</td>
            <td>最新版本</td>
            <td>说明</td>
            <td>key</td>
            <td>操作</td>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${list}" var="pro">
            <tr>
                <td>${pro.id}</td>
                <td>${pro.name}</td>
                <td>${pro.version}</td>
                <td>${pro.description}</td>
                <td>${pro.key}</td>
                <td><a class="layui-btn layui-btn-mini" href="${pageContext.request.contextPath}/process/processpng/${pro.id}">查看流程图</a>
                    <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="del" onclick="deletePdList(${pro.deploymentId});">删除</a></td>
            </tr>

        </c:forEach>


        </tbody>
    </table>

</div>
<script src="${pageContext.request.contextPath}/media/layui/layui.js"></script>

<script type="text/javascript">
    function deletePdList(id){
        layui.use('table', function() {
            layer.confirm('是否确认删除该流程图?',function(index) {
               location.href="${pageContext.request.contextPath}/process/delete/"+id;
            });
        });
    }
</script>


</body>
</html>