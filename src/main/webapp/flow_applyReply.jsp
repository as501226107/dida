<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <title>滴答办公系统-审批处理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/media/layui/css/layui.css" ${pageContext.request.contextPath}/media="all">
    <script src="${pageContext.request.contextPath}/media/js/jquery.min.js"></script>
</head>

<body>
<div class="layui-container" style="margin-top: 5px">
    <form class="layui-form" action="${pageContext.request.contextPath}/process/approve" method="post">

        <!-- 申请信息ID -->
        <input type="hidden" name="applyId" value="${apply.id}"/>

        <!-- 任务ID -->
        <input type="hidden" name="taskId" value="${taskId}"/>

        <!-- 审批状态    approval=true 同意    -->
        <input type="hidden" id="approval" name="approval" value="true">

        <div class="layui-form-item">
            <label class="layui-form-label">申请类型</label>
            <div class="layui-input-block">
                <div class="layui-form-mid layui-word-aux">
                    ${apply.type}
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">申请时间</label>
            <div class="layui-input-block">
                <div class="layui-form-mid layui-word-aux">
                    ${apply.applydate}
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">申请原因</label>
            <div class="layui-input-block">
                <div class="layui-form-mid layui-word-aux">
                    ${apply.remark}
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">请假天数</label>
            <div class="layui-input-block">
                <div class="layui-form-mid layui-word-aux">
                    ${apply.days}
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">审批信息</label>
            <div class="layui-input-block">
                <div class="layui-col-md12">
                    <textarea name="comment" placeholder="请输入审批意见" class="layui-textarea"></textarea>
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <input class="layui-btn" style="margin-left: 10%" id="btn" type="submit" value="同意">
            <input class="layui-btn layui-btn-danger" onclick="$('#approval').val('false')" id="btn1" type="submit" value="驳回">
            <input class="layui-btn layui-btn-primary" onclick="javascript:history.go(-1);" id="btn2" type="button" value="返回">
        </div>
    </form>
</div>
<script src="${pageContext.request.contextPath}/media/layui/layui.js"></script>
<script>
</script>
</body>

</html>