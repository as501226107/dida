<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/7
  Time: 15:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>滴答办公系统-班级列表</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/media/layui/css/layui.css" media="all">
    <script src="${pageContext.request.contextPath}/media/js/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/media/layui/layui.js"></script>
    <script type="text/javascript">
        //点击下一页按钮，跳转到指定页码
        function goPage(pageIndex){
            var pageSize=$("#pageSize").val();
            $("#gradeForm").attr("action","${pageContext.request.contextPath}/grade/list/"+pageIndex+"?pageSize="+pageSize);
            $("#gradeForm").submit(); //提交表单参数
        }
        //跳转到某个页面
        function tiao(){
            var pageIndex=$("#tiao").val();
            //1.判断输入是否为空
            if(pageIndex==''){
                alert("页码不能为空");
                return false;
            }
            //2.判断输入的是否为正确的数字
            var test=/^(\d{1,})$/;
            var bool=test.test(pageIndex);
            if(bool==false){
                alert("请输入正确的页码");
                return false;
            }
            goPage(pageIndex);
        }
        //下拉框调整每页显示数
        function bian(){
            var pageSize=$("#pageSize").val();
            $("#gradeForm").attr("action","${pageContext.request.contextPath}/grade/list/1?pageSize="+pageSize);
            $("#gradeForm").submit(); //提交表单参数
        }

    </script>
</head>
<body>
<form action="${pageContext.request.contextPath}/grade/list/1" method="post" id="gradeForm"></form>
<div class="layui-container">
    <table class="layui-table" id="tbdata" lay-filter="tbop">
        <thead>
        <tr>
            <td>id</td>
            <td>班级名称</td>
            <td>班级人数</td>
            <td>学科名称</td>
            <td>周数</td>
            <td>位置</td>
            <td>开班日期</td>
            <td>操作</td>
        </tr>
        </thead>
        <tbody>
            <c:forEach items="${pageBean.list}" var="g" varStatus="index">
                <tr>
                    <td>${g.id}</td>
                    <td>${g.name}</td>
                    <td>${g.count}</td>
                    <td>${g.course.name}</td>
                    <td>${g.week}</td>
                    <td>${g.location}</td>
                    <td>${g.createdate}</td>
                    <td>
                        <shiro:hasPermission name="grade:update">
                            <a class="layui-btn layui-btn-mini" href="${pageContext.request.contextPath}/grade/pageToUpage/${g.id}">编辑</a>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="grade:delete">
                            <a class="layui-btn layui-btn-danger layui-btn-mini"
                               lay-event="del" onclick="deleteCourse(${g.id});">删除</a>
                        </shiro:hasPermission>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <div class="layui-box layui-laypage layui-laypage-default" id="layui-laypage-1">
        <c:if test="${hasPre==true}">
            <a href="#" onclick="goPage(${pageBean.pageIndex-1})" class="layui-laypage-prev" data-page="0">
                <i class="layui-icon">&lt;</i>
            </a>
        </c:if>
        <c:if test="${hasPre==false}">
            <a href="#"  class="layui-laypage-prev layui-disabled" data-page="0">
                <i class="layui-icon">&lt;</i>
            </a>
        </c:if>
        <c:forEach begin="${pageBean.startNum}" end="${pageBean.endNum}" step="1" var="i">
            <c:if test="${pageBean.pageIndex==i}">
                <span style="color:#009688;font-weight: bold;">${i}</span>
            </c:if>
            <c:if test="${pageBean.pageIndex!=i}">
                <a href="#" onclick="goPage(${i})">${i}</a>
            </c:if>
            </c:forEach>
        <c:if test="${hasNext==true}">
            <a href="#" onclick="goPage(${pageBean.pageIndex+1})" class="layui-laypage-next" data-page="2">
                <i class="layui-icon">&gt;</i>
            </a>
        </c:if>
        <c:if test="${hasNext==false}">
            <a href="#" o class="layui-laypage-next layui-disabled" data-page="2">
                <i class="layui-icon">&gt;</i>
            </a>
        </c:if>
        <span class="layui-laypage-skip">到第
							   <input id="tiao" type="text" min="1" value="${pageBean.pageIndex}" class="layui-input">页
								<button type="button" class="layui-laypage-btn" onclick="tiao()">确定</button>
							</span>
        <span class="layui-laypage-count">共 ${pageBean.totalCount} 条</span>
        <span class="layui-laypage-limits">
							    <select lay-ignore="" id="pageSize" onchange="bian();">
							        <option value="5">5 条/页</option>
									<option value="10">10 条/页</option>
									<option value="15">15 条/页</option>
									<option value="20">20 条/页</option>
							</select>
							</span>
    </div>
</div>



<script type="text/javascript">
    function deleteCourse(id){
        layui.use('table', function() {
            layer.confirm('是否确认删除班级?',function() {
                location.href="${pageContext.request.contextPath}/grade/delete/"+id;
            });
        });
    }

</script>

</body>
</html>