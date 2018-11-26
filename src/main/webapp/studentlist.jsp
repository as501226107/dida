<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/8
  Time: 14:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>滴答办公系统-学员列表</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/media/layui/css/layui.css" media="all">
    <script src="${pageContext.request.contextPath}/media/js/jquery.min.js"></script>
    <script type="text/javascript">
        //点击下一页按钮，跳转到指定页码
        function goPage(pageIndex){
            var pageSize=$("#pageSize").val();
            $("#studentForm").attr("action","${pageContext.request.contextPath}/student/list/"+pageIndex+"?pageSize="+pageSize);
            $("#studentForm").submit(); //提交表单参数
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
            $("#studentForm").attr("action","${pageContext.request.contextPath}/student/list/1?pageSize="+pageSize);
            $("#studentForm").submit(); //提交表单参数
        }

    </script>
</head>
<body>
<shiro:hasPermission name="student:query">
    <form id="studentForm" action="${pageContext.request.contextPath}/student/list/1" method="post">
        <div class="layui-container">
            <div class="layui-row" style="margin-top: 10px">
                <div class="layui-col-xs3" style="margin-right: 20px">
                    <div class="layui-form-item layui-form-text">
                        <label class="layui-form-label">姓名：</label>
                        <div class="layui-input-block">
                            <input type="text" name="name" value="${pageBean.bean.name}" id="no" class="layui-input" placeholder="学生姓名">
                        </div>
                    </div>
                </div>
                <div class="layui-col-xs3" style="margin-right: 20px">
                    <div class="layui-form-item layui-form-text">
                        <label class="layui-form-label">班级：</label>
                        <div class="layui-input-block">
                            <select id="cds" name="gid" class="layui-input" id="fg">
                                <option value="">--请输入班级--</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="layui-col-xs2">
                    <div class="layui-form-item">

                            <div class="layui-input-block">
                                <button class="layui-btn" type="submit" onclick="search()"><i class="layui-icon layui-icon-search">搜索</i></button>
                            </div>

                    </div>
                </div>
                <div class="layui-col-xs2">
                    <div class="layui-form-item">
                        <shiro:hasPermission name="student:exportExcel">
                            <div class="layui-input-block">
                                <a class="layui-btn layui-btn-mini layui-btn-mini" href="javascript:exportExcel();" lay-event="detail">导出Excel</a>
                            </div>
                        </shiro:hasPermission>
                    </div>
                </div>
            </div>
        </div>
</form>
</shiro:hasPermission>
<div class="layui-container">
    <table class="layui-table" id="tbdata" lay-filter="tbop">
        <thead>
        <tr>
            <td>学号</td>
            <td>姓名</td>
            <td>班级</td>
            <td>性别</td>
            <td>手机号</td>
            <td>邮箱</td>
            <td>操作</td>
        </tr>
        </thead>
        <tbody>
            <c:forEach items="${pageBean.list}" var="s">
                <tr>
                    <td>${s.id}</td>
                    <td>${s.name}</td>
                    <td>${s.grade.name}</td>
                    <td>${s.sex}</td>
                    <td>${s.phone}</td>
                    <td>${s.email}</td>
                    <td>
                        <shiro:hasPermission name="student:update">
                          <a class="layui-btn layui-btn-mini" href="${pageContext.request.contextPath}/student/pageToUpdate/${s.id}">编辑</a>
                        </shiro:hasPermission>
                            <a class="layui-btn layui-btn-mini layui-btn-mini" href="${pageContext.request.contextPath}/student/pageToDeatail/${s.id}" lay-event="detail">查看详情</a>
                        <shiro:hasPermission name="student:delete">
                            <a class="layui-btn layui-btn-danger layui-btn-mini"
                               lay-event="del" onclick="deleteCourse(${s.id});">删除</a>
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
<script src="${pageContext.request.contextPath}/media/layui/layui.js"></script>
<script type="text/javascript">
    //导出excel
    function exportExcel(){
        $("#studentForm").attr("action","${pageContext.request.contextPath}/student/exportExcel");
        $("#studentForm").submit();
    }
    function search(){
        $("#studentForm").attr("action","${pageContext.request.contextPath}/student/list/1");
        $("#studentForm").submit();
    }
    function deleteCourse(id){
        layui.use('table', function() {
            layer.confirm('是否确认删除学生?',function(index) {
               location.href="${pageContext.request.contextPath}/student/delete/"+id;
            });
        });
    }
    var form;
    layui.use([ 'form', 'laydate' ],
        function() {
            form = layui.form;
            initData();
        });

    //初始化数据
    function initData() {
        $.get("${pageContext.request.contextPath}/grade/lists",null,function(arr){
            for(i=0;i<arr.length;i++){
                $("#cds").append("<option value='"+arr[i].id+"'>"+arr[i].name+"</option>");
            }
            form.render("select");
            $("#cds").val("${pageBean.bean.gid}");
        })
    }

</script>
</body>
</html>