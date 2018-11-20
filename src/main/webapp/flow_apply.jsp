<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>滴答办公系统-流程审批</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/media/layui/css/layui.css" ${pageContext.request.contextPath}/media="all">
    <script type="text/javascript" src="${pageContext.request.contextPath}/media/js/jquery.min.js"></script>
</head>
<body>
<div class="layui-container" style="margin-top: 5px">
    <form class="layui-form layui-form-pane" action="${pageContext.request.contextPath}/process/apply" method="post" >
        <div class="layui-form-item">
            <label class="layui-form-label">起始日期</label>
            <div class="layui-input-block">
                <input type="text" name="startdate" class="layui-input mydate">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">结束日期</label>
            <div class="layui-input-block">
                <input type="text" name="enddate" class="layui-input mydate">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">涉及天数</label>
            <div class="layui-input-inline">
                <input type="number" name="days" class="layui-input">
            </div>
            <div class="layui-form-mid layui-word-aux">天</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">操作类型</label>
            <div class="layui-input-block">
                <select name="type" >
                    <option value="-1">--请选择类型--</option>
                    <option value="事假">事假</option>
                    <option value="病假">病假</option>
                    <option value="婚假">婚假</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">审批人</label>
            <div class="layui-input-block">
                <select name="excuteId"  id="cds">
                    <option value="-1">--请选择领导--</option>
                           <option value="${teacher.id}">${teacher.uname}</option>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">说明信息</label>
            <div class="layui-input-block">
                <input name="remark" placeholder="请输入原因" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <input class="layui-btn" style="margin-left: 10%" id="btn1" type="submit" value="我要申请">
        </div>
    </form>
</div>
<script src="${pageContext.request.contextPath}/media/layui/layui.js"></script>
<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<script>
    var form;
    layui
        .use(
            [ 'form', 'laydate' ],
            function() {
                form = layui.form, laydate = layui.laydate;
                //日期
                lay('.mydate').each(function(){
                    laydate.render({
                        elem: this
                    });
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
                initData();
            });
    //初始化数据
    function initData() {
        $.get("departall.do",null,function(arr){
            for(i=0;i<arr.length;i++){
                $("#cds").append("<option value='"+arr[i].id+"'>"+arr[i].name+"</option>");
            }
            form.render("select");
        });
    }
</script>
</body>
</html>