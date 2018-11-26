<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>滴答办公系统-登录</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="format-detection" content="telephone=no">
<!-- load css -->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/media/layui/css/layui.css"
	media="all">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/media/css/login.css"
	media="all">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/media/css/verify.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/media/js/jquery-1.11.0.js"></script>

</head>
<body class="layui-bg-black">
	<div class="layui-canvs">

	</div>

	<div class="layui-layout layui-layout-login">
		<div class="layui-tab">
			<ul class="layui-tab-title">
				<li class="layui-this">普通登录</li>
				<li>二维码登录</li>
			</ul>
			<div class="layui-tab-content">
				<div class="layui-tab-item layui-show">
					<form action="${pageContext.request.contextPath}/user/login"
						  method="post">
						<input id="loginAddress" name="loginAddress" type="hidden"/>
						<h1>
							<strong>滴答办公系统登录</strong> <em>Tick-tock Office System</em>

						</h1>

						<div class="layui-user-icon larry-login">
							<input type="text" placeholder="账号" class="login_txtbx"
								   name="username" value="admin" />
						</div>
						<div class="layui-pwd-icon larry-login">
							<input type="password" placeholder="密码" name="password"
								   value="123" class="login_txtbx" />
						</div>
						<div class="feri-code">
							<div id="mpanel4"></div>
						</div>
						<div class="layui-submit larry-login">
							<input type="submit" id="btn1" disabled="disabled" value="立即登陆"
								   class="submit_btn" />
						</div>
					</form>
				</div>
				<div class="layui-tab-item">
					<img src="${pageContext.request.contextPath}/xcode/getCode" style="width: 200px;height: 200px"/>
				</div>

			</div>
		</div>


		<div class="layui-login-text">
			<p>© 2016-2018 北京滴答科技有限公司 Feri 版权所有</p>
		</div>
	</div>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/media/js/login.js"></script>
	<script type="application/javascript"
		src="${pageContext.request.contextPath}/media/js/verify.min.js"></script>
	<script  type="text/javascript"
			 src="${pageContext.request.contextPath}/media/layui/layui.js" charset="utf-8"></script>
	<script type="text/javascript">
		$(function() {
            function validateLogin(){
                var address=$("#loginAddress").val();
                $.ajax({
                    type: "POST",
                    url: "${pageContext.request.contextPath}/xcode/longConnection",
                    data: "uuid=${sessionScope.uuid}&loginAddress="+address,
                    success: function(data){

                        if(data == "wait"){
                            console.log("wait..");
                            window.setTimeout(validateLogin, "3000");
                        }else if(data=="success"){
                            console.log("success");
                            location.href="${pageContext.request.contextPath}/index.jsp";
                        }
                    }
                });
            }
            validateLogin();
			//滑动验证码
			$('#mpanel4').pointsVerify({
				defaultNum : 1, //默认的文字数量
				checkNum : 1, //校对的文字数量
				vSpace : 5, //间隔
				imgName : [ '1.jpg', '2.jpg', '3.jpg' ],
				imgSize : {
					width : '380px',
					height : '200px',
				},
				barSize : {
					width : '380px',
					height : '40px',
				},
				ready : function() {
				},
				success : function() {
					//......后续操作
                    $("#btn1").attr("disabled", false);
				},
				error : function() {
				}
			});
            //获取城市ajax
            $.ajax({
                url: 'http://api.map.baidu.com/location/ip?ak=ia6HfFL660Bvh43exmH9LrI6',
                type: 'POST',
                dataType: 'jsonp',
                success:function(data) {
                    var a=JSON.stringify(data.content.address_detail.province + data.content.address_detail.city);
                    var reg = new RegExp("\"","g");
                    //alert(a.replace(reg,""));
                    $("#loginAddress").val(a.replace(reg,""));
                }
            });
		});
	</script>
	<script>
        layui.use('element', function(){
            var $ = layui.jquery
                ,element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块

            //触发事件
            var active = {
                tabAdd: function(){
                    //新增一个Tab项
                    element.tabAdd('demo', {
                        title: '新选项'+ (Math.random()*1000|0) //用于演示
                        ,content: '内容'+ (Math.random()*1000|0)
                        ,id: new Date().getTime() //实际使用一般是规定好的id，这里以时间戳模拟下
                    })
                }
                ,tabDelete: function(othis){
                    //删除指定Tab项
                    element.tabDelete('demo', '44'); //删除：“商品管理”


                    othis.addClass('layui-btn-disabled');
                }
                ,tabChange: function(){
                    //切换到指定Tab项
                    element.tabChange('demo', '22'); //切换到：用户管理
                }
            };

            $('.site-demo-active').on('click', function(){
                var othis = $(this), type = othis.data('type');
                active[type] ? active[type].call(this, othis) : '';
            });

            //Hash地址的定位
            var layid = location.hash.replace(/^#test=/, '');
            element.tabChange('test', layid);

            element.on('tab(test)', function(elem){
                location.hash = 'test='+ $(this).attr('lay-id');
            });

        });
	</script>
</body>
</html>