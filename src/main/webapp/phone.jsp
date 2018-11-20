<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>jQuery全国城市天气预报API查询代码</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}media/css/iconfont.css" />
<style>
	*{margin:0;padding:0;}
	ul,li{list-style: none;}
	a{text-decoration: none;}
	html,body{width:100%;height:100%;background:#CCE8E9;}
	input{border:none;outline:none;}
	.clearfix:after { content: "";height: 0;line-height: 0;display: block; clear: both;}
	.clearfix {zoom: 1;}
	.wrap{width:600px;min-height:300px;position:fixed;left:50%;top:50%;margin-left:-300px;margin-top:-150px;}
	.wrap .header{width:100%;height:40px;position:relative;line-height:40px;border:1px solid #fff;border-radius:4px;}
	.wrap .header .intCity{width:80%;height:40px;line-height:40px;font-size:16px;text-indent: 10px;}
	.wrap .header .seachBtn{width:19%;height:40px;line-height:40px;font-size:16px;color:#fff;text-align: center;background:#00BFFF;font-weight:600;cursor:pointer;}
	.wrap .left{width:200px;min-height:300px;float:left;text-align: left;padding-top:20px;}
	.wrap .left li{height:40px;line-height:40px;font-size:16px;color:#fff;}
	.wrap .left li i{font-size:22px;color:yellow;}
	.wrap .left li .span2{margin-left:20px;}
	.wrap .left li .cityName{font-size:20px;}
	
	.wrap .right{width:400px;text-align: center;float:right;}
	.wrap .right ul{margin-top:20px;color:#fff;font-size:16px;}
	.wrap .right .data1{width:50%;float:left;}
	.wrap .right .data2{width:50%;float:left;}
	.wrap .right .data3{width:100%;}
	input{outline:none;border:none;height:30px;}
</style>
</head>
<body><script src="${pageContext.request.contextPath}/demos/googlegg.js"></script>
<div class="wrap clearfix">
	<form action="${pageContext.request.contextPath}/util/getLocation">
		<div class="header">
			<input class="intCity" name="phone" type="text" placeholder="Please enter the city" value="13635418613">
			<input class="seachBtn" type="submit" value="Seach">
		</div>
	</form>
	<h3>${phone}</h3>
<script src="${pageContext.request.contextPath}/media/js/jquery.min.js"></script>

<script src="http://webapi.amap.com/subway?v=1.0&key=a6414d65323db9e2718b31e525a1337d&callback=cbk"></script>

	
	

</body>
</html>

