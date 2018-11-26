<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/12
  Time: 20:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <title>滴答办公系统-资源列表</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/media/layui/css/layui.css" media="all">
    <script src="${pageContext.request.contextPath}/media/js/jquery.min.js"></script>
</head>
<script type="text/javascript">
    //点击下一页按钮，跳转到指定页码
    function goPage(pageIndex){
        var pageSize=$("#pageSize").val();
        $("#permissionForm").attr("action","${pageContext.request.contextPath}/menu/list/"+pageIndex+"?pageSize="+pageSize);
        $("#permissionForm").submit(); //提交表单参数
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
        $("#permissionForm").attr("action","${pageContext.request.contextPath}/menu/list/1?pageSize="+pageSize);
        $("#permissionForm").submit(); //提交表单参数
    }

</script>
<body>
<form action="${pageContext.request.contextPath}/menu/list/list/1" method="post" id="permissionForm"></form>
<div class="layui-container">
    <div class="layui-btn-group">
        <button class="layui-btn layui-btn-norma" onclick="addAuth()">
            <i class="layui-icon">&#xe654;</i>添加权限
        </button>
    </div>
</div>
<div class="layui-container">
    <table class="layui-table" id="tbdata" lay-filter="tbop">
        <thead>
        <tr>
            <td>序号</td>
            <td>名称</td>
            <td>页面路径</td>
            <td>图标</td>
            <td>级别</td>
            <td>操作</td>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${pageBean.list}" var="p">
            <tr>
                <td>${p.id}</td>
                <td>${p.name}</td>
                <td>${p.url}</td>
                <td>${p.icon}</td>
                <td>
                        ${p.parentid==0?'一级菜单':'二级菜单'}
                </td>
                <td>
                    <c:if test="${p.parentid==0}">
                        <a class="layui-btn layui-btn-mini" href="javascript:updateMenu1(${p.id});">编辑</a>
                    </c:if>
                    <c:if test="${p.parentid!=0}">
                        <a class="layui-btn layui-btn-mini" href="javascript:updateMenu2(${p.id});">编辑</a>
                    </c:if>
                    <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="del" onclick="deleteMenus(${p.id});">删除</a>
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
    function deleteMenus(id) {
        layui.use('table', function() {
            layer.confirm('是否确认删除菜单?', function(index) {
                $.ajax({
                    url:'${pageContext.request.contextPath}/menu/delete/'+id,
                    type:'get',
                    success:function(data){
                        if(data==true){
                            layer.msg("删除成功", {
                                icon: 6
                            });
                        }else{
                            layer.msg("删除失败", {
                                icon: 5
                            });
                        }

                    }
                })
            });
        });
    }

    var form;

    function addAuth() {
        //初始化
        layui.use('table', function() {
            form=layui.form;
            form.on('radio(level)', function (data) {
                //初始化
                changePid(data.value);//绑定事件
            });
            form.on('select(level)', function (data) {
                //初始化
                setPid(data.value);//绑定事件
            });

            layer.open({
                area: ['500px', '380px'],
                title: '权限页面新增',
                type: 1,
                content: $('#dvlay'), //这里content是一个普通的String
                btn: ['新增', '取消'],
                yes:function(index, layero){
                    $.ajax({
                        url:'${pageContext.request.contextPath}/menu/addMenu',
                        type:'get',
                        data:$("#fm_add").serialize(),
                        success:function(data){
                            if(data==true){
                                layer.msg("添加成功", {
                                    icon: 6
                                });
                            }else{
                                layer.msg("删除失败", {
                                    icon: 5
                                });
                            }
                        }
                    })
                    layer.close(index);//关闭窗口
                },
                cancel: function() {}
            });
        });
    }

    function changePid(i) {
        $("#dvl1").css("display", "block");
        form.render();
        if(i == -1) {
            $.get("${pageContext.request.contextPath}/menu/getParentMenus", null, function(arr) {
                for(i = 0; i < arr.length; i++) {
                    $("#menu_add").append("<option value=\"" + arr[i].id + "\">" + arr[i].name + "</option>");
                }
                $("#dvl1").css("display", "block");
                form.render();
            });
        } else {
            $("#dvl1").css("display", "none");
            $("#add_pid").val(0);
        }
    }

    function setPid(obj) {
        $("#add_pid").val(obj);
    }
    function update_level2_setpid(obj) {
        alert(obj);
        $("#update_pid").val(obj);
    }
    //更新1级菜单
    function updateMenu1(id) {
        //为隐藏id赋值
        $("#update1_id").val(id);
        //初始化参数
        $.ajax({
           url:'${pageContext.request.contextPath}/menu/getMenu/'+id,
           type:'get',
           success:function(data){
               $("#name_level1").val(data.name);
               $("#icon_level1").val(data.icon);
               $("#url_level1").val(data.url);
           }
        });
        layui.use('table', function() {
            form=layui.form;
            layer.open({
                area: ['500px', '380px'],
                title: '一级菜单更新',
                type: 1,
                content: $('#dvupdate1'), //这里content是一个普通的String
                btn: ['更新', '取消'],
                yes:function(index, layero){
                    $.ajax({
                        url:'${pageContext.request.contextPath}/menu/update',
                        type:'get',
                        data:$("#fm_update1").serialize(),
                        success:function(data){
                            if(data==true){
                                layer.msg("更新成功", {
                                    icon: 6
                                },function(){
                                    window.location.reload();});
                            }else{
                                layer.msg("更新失败", {
                                    icon: 5
                                },function(){
                                    window.location.reload();});
                            }
                        }
                    });
                    layer.close(index);//关闭窗口
                },
            });
        });
    }


    //更新2级菜单
    function updateMenu2(id) {
        //为隐藏id赋值
        $("#update2_id").val(id);
        layui.use('table', function() {
            form=layui.form;
            //初始化参数
            $.ajax({
                url:"${pageContext.request.contextPath}/menu/getSecondMenus/"+id,
                type:"get",
                success:function(result) {
                    $("#pid_level2").empty();
                    var arr = result.menus;
                    var data = result.parent;
                    for (i = 0; i < arr.length; i++) {
                        if (arr[i].flag == true) {
                            $("#pid_level2").append("<option value='" + arr[i].id + "' checked>" + arr[i].name + "</option>");
                            $("#pid_level2").val(arr[i].id);
                        } else {
                            $("#pid_level2").append("<option value='" + arr[i].id + "'>" + arr[i].name + "</option>");
                        }
                        form.render();
                    }
                    $("#name_level2").val(data.name);
                    $("#icon_level2").val(data.icon);
                    $("#url_level2").val(data.url);
                    $("#update_pid").val(data.parentid);
                    $("#dvl1").css("display", "block");
                }
            });
            form.on('select(level)', function (data) {
                //初始化
                update_level2_setpid(data.value);//绑定事件
            });

            layer.open({
                area: ['500px', '380px'],
                title: '二级菜单更新',
                type: 1,
                content: $('#dvupdate2'), //这里content是一个普通的String
                btn: ['更新', '取消'],
                cancel: function() {},
                yes:function (index, layero) {
                    $.ajax({
                        url:'${pageContext.request.contextPath}/menu/update',
                        type:'get',
                        data:$("#fm2_update").serialize(),
                        success:function(data){
                            if(data==true){
                                layer.msg("更新成功", {
                                    icon: 6
                                },function(){
                                    window.location.reload();});
                            }else{
                                layer.msg("更新失败", {
                                    icon: 5
                                },function(){
                                    window.location.reload();});
                            }
                        }
                    });
                    layer.close(index);//关闭窗口
                }
            });
        });
    }

</script>

</body>

</html>

<div style="display: none;margin-top: 10px;width: 480px" id="dvlay">
<form id="fm_add" class="layui-form ">
    <div class="layui-form-item">
        <label class="layui-form-label">名称：</label>
        <div class="layui-input-inline">
            <input name="name" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">图标：</label>
        <div class="layui-input-inline">
            <input name="icon" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">路径：</label>
        <div class="layui-input-inline">
            <input name="url" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">级别：</label>
        <div class="layui-input-inline">
            <input type="radio" name="pid" value="0" lay-filter="level" title="一级" checked>
            <input type="radio" name="pid" value="-1" lay-filter="level" title="二级">
        </div>
    </div>
    <input type="hidden" name="parentid" value="0" id="add_pid">
    <div class="layui-form-item" id="dvl1" style="display: none">
        <label class="layui-form-label">上级路径：</label>
        <div class="layui-input-inline">
            <select  id="menu_add" lay-filter="level">

            </select>

        </div>
    </div>
</form>
</div>


<div style="display: none;margin-top: 10px;width: 480px" id="dvupdate1">
    <form id="fm_update1" class="layui-form ">
        <input type="hidden" id="update1_id" name="id"/>
        <div class="layui-form-item">
            <label class="layui-form-label">名称：</label>
            <div class="layui-input-inline">
                <input name="name" class="layui-input" id="name_level1">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">图标：</label>
            <div class="layui-input-inline">
                <input name="icon" class="layui-input" id="icon_level1">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">路径：</label>
            <div class="layui-input-inline">
                <input name="url" class="layui-input" id="url_level1">
            </div>
        </div>
    </form>
</div>


<div style="display: none;margin-top: 10px;width: 480px" id="dvupdate2">
    <form id="fm2_update" class="layui-form ">
        <input type="hidden" id="update2_id" name="id"/>
        <div class="layui-form-item" id="dv22">
            <label class="layui-form-label">上级路径：</label>
            <div class="layui-input-inline">
                <input type="hidden" name="parentid" value="0" id="update_pid">
                <select  id="pid_level2" lay-filter="level">

                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">名称：</label>
            <div class="layui-input-inline">
                <input name="name" class="layui-input" id="name_level2">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">图标：</label>
            <div class="layui-input-inline">
                <input name="icon" class="layui-input" id="icon_level2">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">路径：</label>
            <div class="layui-input-inline">
                <input name="url" class="layui-input" id="url_level2">
            </div>
        </div>
    </form>
</div>