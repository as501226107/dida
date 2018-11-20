<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/8
  Time: 17:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>滴答办公系统-班级新增</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/media/layui/css/layui.css" media="all">
    <script type="text/javascript" src="${pageContext.request.contextPath}/media/js/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/media/js/jquery.validate.min.js"></script>
</head>
<body>
<div class="layui-container" style="margin-top: 5px">
    <form class="layui-form" id="myform" action="${pageContext.request.contextPath}/user/updatePassword" method="post">
        <div class="layui-form-item">
            <label class="layui-form-label">原始密码</label>
            <div class="layui-input-block">
                <input type="password" name="password"  lay-verify="name" autocomplete="off"
                       placeholder="请输入密码" class="layui-input">
            </div>

        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">新密码</label>
            <div class="layui-input-block">
                <input type="password" id="newpassword" name="newpassword" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">确认新密码</label>
            <div class="layui-input-block">
                <input type="password" name="confirm_password" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <input class="layui-btn"   style="margin-left: 10%" type="submit" value="确认修改">
        </div>
    </form>
</div>


<script src="${pageContext.request.contextPath}/media/layui/layui.js"></script>
<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<script>
    $(function(){
        var validate = $("#myform").validate({
            debug: true, //调试模式取消submit的默认提交功能
            //errorClass: "label.error", //默认为错误的样式类为：error
            focusInvalid: false, //当为false时，验证无效时，没有焦点响应
            onkeyup: false,
            submitHandler: function(form){   //表单提交句柄,为一回调函数，带一个参数：form

                form.submit();   //提交表单
            },

            rules:{
                password:{
                    required:true,
                    isEqualToRepass:true,
                },
                newpassword:{
                    required:true,
                    rangelength:[3,10]
                },
                confirm_password:{
                    equalTo:"#newpassword"
                }
            },
            messages:{
                password:{
                    required:"必填"
                },
                newpassword:{
                    required: "不能为空",
                    rangelength: "密码最小长度:3, 最大长度:10。"
                },
                confirm_password:{
                    equalTo:"两次密码输入不一致"
                }
            }

        });
        $.validator.addMethod("isEqualToRepass", function(value, element,
                                                      params) {
            var status;
            $.ajax({
                url:"${pageContext.request.contextPath}/user/validatePass/"+value,
                type:"post",
                async : false,//改为同步方式
                success:function (data) {
                    if(data.status=="true"){
                        status=true;
                    }else{
                        status=false;
                    }
                }
            });
            return status;
        }, "密码不匹配");
    });


</script>
</body>
</html>