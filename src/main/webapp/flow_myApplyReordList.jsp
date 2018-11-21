<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>滴答办公系统-我的申请流转记录</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="stylesheet" href="${pageContext.request.contextPath}/media/layui/css/layui.css" ${pageContext.request.contextPath}/media="all">
<script src="${pageContext.request.contextPath}/media/js/jquery.min.js"></script>
</head>
<body>
	<div class="layui-container">
		<table class="layui-table" id="tbdata" lay-filter="tbop">
			<thead>
				<tr>
					<td>审批人</td>
					<td>审批时间</td>
					<td>是否通过</td>
					<td>审批意见</td>
				</tr>
			</thead>
			<tbody>
			  <c:forEach items="${approves}" var="app">
				<tr>
					<td>${app.user.uname}</td>
					<td>${app.approveDate}</td>
					<td>${app.approveValue==true?'通过':'未通过'}</td>
				    <td>${app.comment}</td>
				</tr>
			   </c:forEach>
			</tbody>
		</table>
		
	<script src="${pageContext.request.contextPath}/media/layui/layui.js"></script>
	
	
</body>
</html>