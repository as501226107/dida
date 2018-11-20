<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/8
  Time: 15:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>滴答办公系统-学员更新</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/media/layui/css/layui.css" media="all">
    <script type="text/javascript" src="${pageContext.request.contextPath}/media/js/jquery.min.js"></script>
</head>
<body>
<div class="layui-container" style="margin-top: 5px">
    <form class="layui-form" action="${pageContext.request.contextPath}/student/update" method="post">
        <input type="hidden" name="id" value="${student.id}"/>
        <div class="layui-form-item">
            <label class="layui-form-label">学员学号</label>
            <div class="layui-input-block">
                <input type="text" value="${student.no}" name="no" lay-verify="name" autocomplete="off"
                       placeholder="请输入学号" id="no1" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">学员姓名</label>
            <div class="layui-input-block">
                <input type="text" value="${student.name}" name="name" lay-verify="name" autocomplete="off"
                       placeholder="请输入姓名" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">所属班级</label>
            <div class="layui-input-block">
                <select name="gid" id="cds">
                    <option value="-1">--请选择班级--</option>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">性别</label>
            <div class="layui-input-block">
                <input type="radio" name="sex" value="男" title="男" ${student.sex=='男'?'checked':''}>
                <input type="radio" name="sex" value="女" title="女" ${student.sex=='女'?'checked':''}>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">邮箱</label>
            <div class="layui-input-inline">
                <input type="text" name="email" value="${student.email}" lay-verify="required"
                       placeholder="请输入有效" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">手机号</label>
            <div class="layui-input-inline">
                <input type="text" name="phone" value="${student.phone}" lay-verify="required"
                       placeholder="请输入手机号" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">QQ</label>
            <div class="layui-input-inline">
                <input type="text" name="qq" value="${student.qq}" lay-verify="required"
                       placeholder="请输入QQ" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">身份证号</label>
            <div class="layui-input-inline">
                <input type="text" name="cardno" value="${student.cardno}" lay-verify="required"
                       placeholder="请输入身份证号" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">毕业学校</label>
            <div class="layui-input-inline">
                <input type="text" name="school" value="${student.school}" lay-verify="required"
                       placeholder="请输入毕业学校" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">学历</label>
            <div class="layui-input-inline">
                <select name="education">
                    <option>初中</option>
                    <option>高中</option>
                    <option>专科</option>
                    <option>本科</option>
                    <option>硕士</option>
                    <option>博士</option>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">出生日期</label>
            <div class="layui-input-inline">
                <input type="text" value="${student.birthday}" name="birthday" id="date1" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">入学日期</label>
            <div class="layui-input-inline">
                <input type="text" name="createdate" value="${student.createdate}" id="date2" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <input class="layui-btn"  style="margin-left: 10%" id="btn1" type="submit"
                   value="确认更新">
        </div>
    </form>
</div>


<script src="${pageContext.request.contextPath}/media/layui/layui.js"></script>
<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<script>
    var form;
    layui.use(
        [ 'form','upload', 'layedit', 'laydate' ],
        function() {
            form = layui.form, layer = layui.layer, layedit = layui.layedit, laydate = layui.laydate;
            var upload = layui.upload;
            //日期
            laydate.render({
                elem : '#date1'
            });
            laydate.render({
                elem : '#date2'
            });
            initData();
        });
    //初始化数据
    function initData() {
        $.get("${pageContext.request.contextPath}/grade/lists",null,function(arr){
            for(i=0;i<arr.length;i++){
                $("#cds").append("<option value='"+arr[i].id+"'>"+arr[i].name+"</option>");
            }
            form.render("select");
            $("#cds").val(${student.gid});
            /*点击对应的下拉框*/
            var select = 'dd[lay-value=' + ${student.gid} + ']';
            $('#cds').siblings("div.layui-form-select").find('dl').find(select).click();
        })
    }
</script>
</body>
</html>
