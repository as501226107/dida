<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/14
  Time: 18:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>滴答办公系统-角色列表</title>
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
            $("#roleForm").attr("action","${pageContext.request.contextPath}/role/list/"+pageIndex+"?pageSize="+pageSize);
            $("#roleForm").submit(); //提交表单参数
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
            $("#roleForm").attr("action","${pageContext.request.contextPath}/role/list/1?pageSize="+pageSize);
            $("#roleForm").submit(); //提交表单参数
        }

    </script>
</head>
<body>
<div class="layui-container">
    <div class="layui-btn-group">
        <button class="layui-btn layui-btn-norma" onclick="addRole()">
            <i class="layui-icon">&#xe654;</i>添加角色
        </button>
    </div>
</div>
<div class="layui-container">
    <form id="roleForm" action="${pageContext.request.contextPath}/role/list/1" method="post"></form>
    <table class="layui-table" id="tbdata" lay-filter="tbop">
        <thead>
        <tr>
            <td>序号</td>
            <td>角色名称</td>
            <td>角色备注</td>
            <td>操作</td>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${pageBean.list}" var="r">
            <tr>
                <td>${r.id}</td>
                <td>${r.name}</td>
                <td>${r.remark}</td>
                <td>
                    <a class="layui-btn layui-btn-mini" href="#" onclick="updateRole(${r.id});">编辑</a>
                    <a class="layui-btn layui-btn-danger layui-btn-mini"
                       lay-event="del" onclick="deleteCourse(${r.id});">删除</a>
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
            layer.confirm('是否确认删除该角色?',function(index) {
                $.ajax({
                    url:"${pageContext.request.contextPath}/role/delete/"+id,
                    type:"post",
                    success:function (data) {
                        if(data==true){
                            layer.msg("删除成功", {
                                icon: 6
                            },function(){
                                window.location.reload();}
                            );
                        }else{
                            layer.msg("删除失败", {
                                icon: 5
                            },function() {
                                window.location.reload();
                            });
                        }
                    },
                });
            });
        });
    }

    var form;

    function updateRole(id){
        $("#menus_update").empty();
        $("#permissions_update").empty();
        //为id赋值
        $("#role_update_id").val(id);

        //点击确定请求
        layui.use('table', function() {
            form=layui.form;
            //为子菜单添加时间
            form.on('checkbox(level)', function (data) {
                parentunion(data);//绑定事件
            });
            //为父菜单添加时间
            form.on('checkbox(level1)', function (data) {
                parentunion1(data);//绑定事件
            });
            $.ajax({
                url:"${pageContext.request.contextPath}/role/pagaToUpdate/"+id,
                type:"get",
                success:function (data) {
                    //清空当前的列表
                    $("#permissions_update").empty();
                    var menus=data.menus;
                    var permissions=data.permissions;
                    var role=data.role;
                    //初始化角色姓名和备注
                    $("#update_name").val(role.name);
                    $("#update_remark").val(role.remark);
                    $(menus).each(function(index,m){
                        //1.判断父子菜单
                        if(m.parentid==0){//父菜单
                            //判断父菜单选中
                            if(m.flag==true){
                                $("#menus_update").append("<input type='checkbox' id='parent_"+m.id+"' lay-filter='level1'  name='rids' value='"+m.id+"' title='"+m.name+"' checked>")
                            }else{
                                $("#menus_update").append("<input type='checkbox' id='parent_"+m.id+"' lay-filter='level1' name='rids' value='"+m.id+"' title='"+m.name+"'>")
                            }
                            $(menus).each(function(index1,ms){
                                if(ms.parentid==m.id){
                                    if(ms.flag==true){
                                        $("#menus_update").append("<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input class='son_"+ms.parentid+"'  type='checkbox' name='rids' lay-filter='level' value='"+ms.id+"' title='"+ms.name+"' checked>")
                                    }else{
                                        $("#menus_update").append("<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input class='son_"+ms.parentid+"'  type='checkbox' name='rids' lay-filter='level' value='"+ms.id+"' title='"+ms.name+"'>")
                                    }
                                }
                            });
                        }
                    });
                    //生成权限checkbox
                    $(permissions).each(function(index,p){
                        if(p.flag==true){
                            $("#permissions_update").append("<input type='checkbox' name='rids' value='"+p.id+"' title='"+p.name+"' checked>");
                        }else{
                            $("#permissions_update").append("<input type='checkbox' name='rids' value='"+p.id+"' title='"+p.name+"'>");
                        }
                    });
                    layui.form.render();

                }
            });
            layer.open({
                area: ['500px', '480px'],
                title: '更新角色',
                type: 1,
                content: $('#dvlay'), //这里content是一个普通的String
                btn: ['更新', '取消'],
                cancel: function() {},
                yes:function(index){
                    //添加数据
                    $.ajax({
                        url:"${pageContext.request.contextPath}/role/update",
                        data:$("#fm_roleupdate").serialize(),
                        type:"post",
                        success:function (data) {
                            if(data==true){
                                layer.msg("更新成功", {
                                    icon: 6
                                },function(){
                                    window.location.reload();}
                                );
                            }else{
                                layer.msg("更新失败", {
                                    icon: 5
                                },function(){
                                    window.location.reload();
                                });
                            }
                        },
                    });
                    layer.close(index);//关闭窗口
                }
            });
        });
    }

    function addRole(){
        $("#menus_add").empty();
        $("#permissions_add").empty();
        layui.use('table', function() {
            form=layui.form;
            form.on('checkbox(level)', function (data) {
                parentunion(data);//绑定事件
            });
            //为父菜单添加事件
            form.on('checkbox(level1)', function (data) {
                parentunion1(data);//绑定事件
            });
            //初始化下拉列表
            $.ajax({
                url:"${pageContext.request.contextPath}/role/pagaToAdd",
                type:"get",
                success:function (data) {
                    $("#menus_add").empty();
                    var menus=data.menus;
                    var permissions=data.permissions;
                    $(menus).each(function(index,m){
                        //1.判断父子菜单
                        if(m.parentid==0){//父菜单
                            $("#menus_add").append("<input id='parent_"+m.id+"' lay-filter='level1'  type='checkbox' name='rids'  value='"+m.id+"' title='"+m.name+"'>")
                            $(menus).each(function(index1,ms){
                                if(ms.parentid==m.id){
                                    $("#menus_add").append("<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input class='son_"+ms.parentid+"'  type='checkbox' lay-filter='level' name='rids' value='"+ms.id+"' title='"+ms.name+"'>")
                                }
                            });
                        }
                    });
                    //生成权限checkbox
                    $(menus).each(function(index,p){
                        $("#permissions_add").append("<input type='checkbox' name='rids' value='"+p.id+"' title='"+p.name+"'>");
                    });
                    layui.form.render();
                }
            })
            layer.open({
                area: ['500px', '480px'],
                title: '添加角色',
                type: 1,
                content: $('#dvlay_add'), //这里content是一个普通的String
                btn: ['更新', '取消'],
                yes:function(index){
                    //添加数据
                    $.ajax({
                        url:"${pageContext.request.contextPath}/role/add",
                        data:$("#fm_roleadd").serialize(),
                        type:"post",
                        success:function (data) {
                            if(data==true){
                                layer.msg("添加成功", {
                                    icon: 6
                                },function(){
                                    window.location.reload();
                                });
                            }else{
                                layer.msg("添加成功", {
                                    icon: 5
                                },function(){
                                    window.location.reload();
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
    //子元素点击事件
    function parentunion(obj){
        var parent_id=$(obj.elem).attr("class");
        var str=new String(parent_id).substring("son_".length);
        $("#parent_"+str).prop("checked",true);
        layui.form.render();
    }
    //父元素点击事件
    //当父元素取消点击，子元素一同取消
    function parentunion1(obj){
        //父元素选中状态
        var parentChecked= $(obj.elem).prop("checked")
        if(parentChecked==false){
            var parent_id=$(obj.elem).attr("id");
            var str=new String(parent_id).substring("parent_".length);
            var son_class="son_"+str;
            //获得子元素
            var sons=$("."+son_class);
            $(sons).each(function(index,son){
                $(son).prop("checked",false);
            });
            layui.form.render();
        }


    }
</script>


</body>
</html>


<div style="display: none;margin-top: 10px;width: 480px" id="dvlay">
    <form id="fm_roleupdate" class="layui-form layui-form-pane" >
        <input type="hidden" id="role_update_id" name="id"/>
        <div class="layui-form-item" pane >
            <label class="layui-form-label">角色名称：</label>
            <div class="layui-input-inline">
                <input id="update_name" name="name" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item" pane >
            <label class="layui-form-label">角色备注：</label>
            <div class="layui-input-inline">
                <input id="update_remark" name="remark" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item" pane>
            <label class="layui-form-label">菜单：</label>
            <div class="layui-input-inline" id="menus_update">

            </div>
        </div>
        <div class="layui-form-item" pane>
            <label class="layui-form-label">权限：</label>
            <div class="layui-input-inline" id="permissions_update">

            </div>
        </div>
    </form>
</div>



<div style="display: none;margin-top: 10px;width: 480px" id="dvlay_add">
    <form id="fm_roleadd" class="layui-form layui-form-pane" >
        <div class="layui-form-item" pane >
            <label class="layui-form-label">角色名称：</label>
            <div class="layui-input-inline">
                <input id="name_add" name="name" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item" pane >
            <label class="layui-form-label">角色备注：</label>
            <div class="layui-input-inline">
                <input id="remark_add" name="remark" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item" pane>
            <label class="layui-form-label">菜单：</label>
            <div class="layui-input-inline" id="menus_add">

            </div>
        </div>
        <div class="layui-form-item" pane>
            <label class="layui-form-label">权限：</label>
            <div class="layui-input-inline" id="permissions_add">

            </div>
        </div>
    </form>
</div>