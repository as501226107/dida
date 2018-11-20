<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/20
  Time: 19:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <title>滴答办公系统-申请信息</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/media/layui/css/layui.css" media="all">
    <script src="${pageContext.request.contextPath}/media/js/jquery.min.js"></script>
</head>

<body>
<div class="layui-container" style="margin-top: 5px">
        <div class="layui-form-item">
            <label class="layui-form-label">申请人</label>
            <div class="layui-input-block">
                <input type="text" disabled name="startdate" class="layui-input mydate" value="${myTask.user.uname}">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">起始日期</label>
            <div class="layui-input-block">
                <input type="text" disabled name="startdate" class="layui-input mydate" value="${myTask.apply.startdate}">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">结束日期</label>
            <div class="layui-input-block">
                <input type="text" disabled name="enddate" class="layui-input mydate" value="${myTask.apply.enddate}">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">涉及天数</label>
            <div class="layui-input-inline">
                <input  name="days" disabled class="layui-input" value="${myTask.apply.days}天"/>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">操作类型</label>
            <div class="layui-input-block">
                <input  name="days" disabled class="layui-input" value="${myTask.apply.type}天"/>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">说明信息</label>
            <div class="layui-input-block">
                <input name="remark" disabled placeholder="请输入原因" disabled value="${myTask.apply.remark}" class="layui-input"/>
            </div>
        </div>


<script src="${pageContext.request.contextPath}//layui/layui.js"></script>
<script>
</script>
</body>

</html>