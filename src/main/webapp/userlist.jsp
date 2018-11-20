<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/15
  Time: 14:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>滴答办公系统-用户列表</title>
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
            $("#userRoleForm").attr("action","${pageContext.request.contextPath}/userRole/list/"+pageIndex+"?pageSize="+pageSize);
            $("#userRoleForm").submit(); //提交表单参数
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
            $("#userRoleForm").attr("action","${pageContext.request.contextPath}/userRole/list/1?pageSize="+pageSize);
            $("#userRoleForm").submit(); //提交表单参数
        }

    </script>
</head>
<body>
<form id="userRoleForm" action="${pageContext.request.contextPath}/userRole/list/1">
    <div class="layui-container">
        <div class="layui-row" style="margin-top: 10px">
            <div class="layui-col-xs3" style="margin-right: 20px">
                <div class="layui-form-item layui-form-text">
                    <label class="layui-form-label">姓名：</label>
                    <div class="layui-input-block">
                        <input type="text" id="username" name="username" class="layui-input" placeholder="用户姓名">
                    </div>
                </div>
            </div>
            <div class="layui-col-xs3" style="margin-right: 20px">
                <div class="layui-form-item layui-form-text">
                    <label class="layui-form-label">用户：</label>
                    <div class="layui-input-block">
                        <select class="layui-input" id="fg" name="locked">
                            <option value="-1">全部</option>
                            <option value="0">启用</option>
                            <option value="1">禁用</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="layui-col-xs2">
                <div class="layui-form-item">
                    <div class="layui-input-block">
                        <button class="layui-btn" onclick="searchData()"><i class="layui-icon layui-icon-search">搜索</i></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<div class="layui-container">
    <table class="layui-table" id="tbdata" lay-filter="tbop">
        <thead>
        <tr>
            <td>序号</td>
            <td>工号</td>
            <td>姓名</td>
            <td>职位</td>
            <td>状态</td>
            <td>操作</td>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${pageBean.list}" var="u">
            <tr>
                <td>${u.id}</td>
                <td>
                    ${u.no}
                </td>
                <td>${u.uname}</td>
                <td>
                    <c:forEach items="${u.roles}" var="role">
                        ${role.name}
                    </c:forEach>
                </td>
                <td><span style="${u.locked=='0'?'color: #1E9FFF':'color:red'}"  id="span_${u.id}">${u.locked=='0'?'启用':'禁用'}</span></td>
                <td><a class="layui-btn    layui-btn-mini" href="#" onclick="showRoles(${u.id});">编辑</a>
                    <a class="layui-btn  ${u.locked=='0'?'layui-btn-danger':''}  layui-btn-mini" name="${u.locked}" onclick="changeState(this,${u.id});"  lay-event="detail">${u.locked=='0'?'禁用':'启用'}</a>
                    <a class="layui-btn  layui-btn-mini"
                       lay-event="del" onclick="deleteCourse(${u.id});">删除</a>
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
    function deleteCourse(id){
        layui.use('table', function() {
            layer.confirm('是否确认删除学生?',function(index) {
                $.ajax({
                    url:"${pageContext.request.contextPath}/userRole/delete/"+id,
                    type:"post",
                    success:function (data) {
                        if(data==true){
                            layer.msg("删除成功", {
                                icon: 6
                            });
                        }else{
                            layer.msg("删除失败", {
                                icon: 5
                            });
                        }
                    },
                });
            });
        });
    }

    //启用禁用
    function changeState(obj,id){
        var status=$(obj).attr("name")==0?1:0;//置换一下
        console.log(status);
        //调转关系
        var statusStr=status==1?'禁用':'启用';
        var result=status==1?'启用':'禁用';
        layui.use('table', function() {
            console.log(obj)
            layer.confirm("确认"+statusStr+"吗?",function() {
                $.ajax({
                    url:"${pageContext.request.contextPath}/userRole/changeStatus",
                    data:"locked="+status+"&id="+id,
                    type:"post",
                    success:function (data) {
                        if(data==true){
                            $(obj).attr("name",status);
                            $(obj).html(result);
                            $("#span_"+id).html(statusStr);
                            //当状态变为禁用得时候
                            if(status==1){
                                $(obj).removeClass("layui-btn-danger");
                                $("#span_"+id).css("color","red")

                            }else{
                                $(obj).addClass("layui-btn-danger");
                                $("#span_"+id).css("color","#1E9FFF")
                            }
                            layer.msg(statusStr+"成功", {
                                icon: 6
                            });
                        }else{
                            layer.msg(statusStr+"失败", {
                                icon: 5
                            });
                        }
                    },
                });
                layer.close(index);//关闭窗口
            });
        });
    }

    var form;

    function showRoles(id){
        $("#update_id").val(id);
        //清空缓存
        $("#update_role").empty();
        layui.use('table', function() {
            form=layui.form;
            //初始化参数
            $.ajax({
                url:"${pageContext.request.contextPath}/userRole/pageToUpdate/"+id,
                type:"get",
                success:function(data){
                    var user=data.user;
                    var roles=data.roles;
                    //初始化数据
                    //工号
                    $("#update_no").val(user.no);
                    //姓名
                    $("#update_name").val(user.uname);
                    //初始化数据
                    $(roles).each(function(index,role){
                        if(role.flag==true){
                            $("#update_role").append(" <input type='checkbox' name='rids' value='"+role.id+"' title='"+role.name+"' checked>");
                        }else{
                            $("#update_role").append(" <input type='checkbox' name='rids' value='"+role.id+"' title='"+role.name+"'>");
                        }
                    });
                    form.render();
                }
            })
            layer.open({
                area: ['500px', '480px'],
                title: '用户编辑',
                type: 1,
                content: $('#dvlay'), //这里content是一个普通的String
                btn: ['更新', '取消'],
                yes:function(index, layero){
                    //添加数据
                    $.ajax({
                        url:"${pageContext.request.contextPath}/userRole/update",
                        data:$("#update_userrole").serialize(),
                        type:"post",
                        success:function (data) {
                            if(data==true){
                                layer.msg("更新成功", {
                                    icon: 6
                                });
                            }else{
                                layer.msg("更新失败", {
                                    icon: 5
                                });
                            }
                        },
                    });
                    layer.close(index);//关闭窗口
                },
                cancel: function() {}
            });
        });
    }

</script>


</body>
</html>

<div style="display: none;margin-top: 10px;width: 480px" id="dvlay">
    <form id="update_userrole" class="layui-form layui-form-pane" >
        <input type="hidden" name="id" id="update_id"/>
        <div class="layui-form-item" pane >
            <label class="layui-form-label">工号：</label>
            <div class="layui-input-inline">
                <input id="update_no" name="no" disabled class="layui-input">
            </div>
        </div>
        <div class="layui-form-item" pane >
            <label class="layui-form-label">姓名：</label>
            <div class="layui-input-inline">
                <input id="update_name" name="uname"  class="layui-input">
            </div>
        </div>
        <div class="layui-form-item" pane>
            <label class="layui-form-label">职位：</label>
            <div class="layui-input-inline" id="update_role">


            </div>
        </div>
    </form>
</div>