<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/9
  Time: 14:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>滴答办公系统-部门更新</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/media/layui/css/layui.css" media="all">
</head>
<body>

<div class="layui-container" style="margin-top: 5px">
    <form class="layui-form" action="${pageContext.request.contextPath}/depart/update" method="post">

                <input type="hidden" name="id" value="${depart.id}" readonly lay-verify="name" autocomplete="off"
                       class="layui-input">


        <div class="layui-form-item">
            <label class="layui-form-label">部门名称</label>
            <div class="layui-input-block">
                <input type="text" name="name" value="${depart.name}" lay-verify="name" autocomplete="off"
                       placeholder="请输入部门名称" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">创立日期</label>
            <div class="layui-input-block">
                <input type="text" name="createtime" value="${depart.createtime}" placeholder="请输入创立日期" id="date" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <shiro:hasPermission name="depart:add">
                <input class="layui-btn" style="margin-left: 10%" type="submit" value="确认更新">
            </shiro:hasPermission>
        </div>
    </form>
</div>


<script src="${pageContext.request.contextPath}/media/layui/layui.js"></script>
<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<script>
    layui.use(
        [ 'form', 'laydate' ],
        function() {
            var form = layui.form, layer = layui.layer, laydate = layui.laydate;

            //日期
            laydate.render({
                elem : '#date'
            });


            //自定义验证规则
            form.verify({
                title : function(value) {
                    if (value.length < 5) {
                        return '标题至少得5个字符啊';
                    }
                },
                pass : [ /(.+){6,12}$/, '密码必须6到12位' ],
                content : function(value) {
                    layedit.sync(editIndex);
                }
            });
        });
</script>
</body>
</html>