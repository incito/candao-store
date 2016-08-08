<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>餐道后台管理平台</title>
	<meta name="description" content="">
	<meta name="keywords" content="">
	<link href="../tools/bootstrap/css/bootstrap.css" rel="stylesheet">
	<link rel="stylesheet" href="../tools/font-awesome/css/font-awesome.css">
	<link rel="stylesheet" href="../css/common.css">
	<link rel="stylesheet" href="../css/index.css"></head>
	<script src="../scripts/jquery.js"></script>
	<script src="../tools/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript">
	var global_Path = '<%=request.getContextPath()%>';
	$(function(){
		$.ajax({
			type : "post",
			async : false,
			url : global_Path+"/login/getMenuList.json",
			dataType : "json",
			success : function(result) {
				alert(result.length);
				$.each(result,function(index,item){
					$('#menutop').append("<li><a href='"+global_Path+item.resourcespath+"' class='ky-menu-primary'>"+item.resourcesname+"</a></li> ");
				});
			}
		});	
	});
	
	
	</script>
<body>
	<div class="ky-navbar ky-navbar-default">
		<div class="ky-navbar-header">
			<img src="../images/logo.png" alt="">
			<p>餐道后台管理平台</p>
		</div>
		<div class="ky-navbar-menu">
			<ul class="ky-nav ky-nav-pills" id="menutop">
<!-- 				<li> -->
<!-- 					<a href="#" class="ky-menu-primary">门店管理</a> -->
<!-- 				</li> -->
<!-- 				<li> -->
<!-- 					<a href="#" class="ky-menu-info">菜品管理</a> -->
<!-- 				</li> -->
<!-- 				<li class="ky-dropdown"> -->
<!-- 					<a href="#" class="ky-menu-warning">餐台管理</a> -->
<!-- 					<ul class="ky-dropdown-menu ky-nav ky-nav-pills"> -->
<!-- 						<li><a href="#" class="ky-menu-warning">区域管理</a></li> -->
<!-- 						<li><a href="#" class="ky-menu-warning">菜品管理</a></li> -->
<!-- 					</ul> -->
<!-- 				</li> -->
<!-- 				<li> -->
<!-- 					<a href="#" class="ky-menu-danger">打印管理</a> -->
<!-- 				</li> -->
<!-- 				<li> -->
<!-- 					<a href="#" class="ky-menu-minor">优惠管理</a> -->
<!-- 				</li> -->
<!-- 				<li> -->
<!-- 					<a href="#" class="ky-menu-success">报表分析</a> -->
<!-- 				</li> -->
			</ul>
		</div>
	</div>
	<div class="ky-container">
		<div class="ky-title">
			<p>菜单管理</p>
			<div class="ky-title-right">
				<form class="navbar-form navbar-left" role="search">
					<div class="form-group">
						<div class="input-group">
						  <span class="input-group-addon" id="basic-addon1"><i class="icon-search"></i></span>
						  <input type="text" class="form-control" placeholder="search" aria-describedby="basic-addon1">
						</div>

					</div>
				</form>
				<img src="../images/user.png" alt="">
				<span class="ky-user">${currentUser.fullname}</span>
				<a href="<%=request.getContextPath()%>/login/logout"><img src="../images/logout.png"></a>
				</div>
		</div>
<!-- 				<iframe id="detail" src="preferential-iframeD.html " width="100%" frameBorder="0" scrolling="auto" height="704px">  -->
<!-- 		</iframe> -->
	</div>
</body>
	<script>
		$(document).ready(function(){
			$('.ky-navbar-menu > .ky-nav > li > a.ky-menu-primary').css({'background-color':'#7AC454','color':'#ffffff'});

			$('.ky-nav li a').click(function(){
				var _this = $(this);
				var bgcolor = _this.css('border-left-color');
				$('.ky-nav li a').css({'background-color':'#E8E7E4','color':'#282828'})
				_this.css({'background-color':bgcolor,'color':'#ffffff'});
				$('.ky-title').css('background-color',bgcolor);
				if(_this.parent().hasClass('ky-dropdown')){
					_this.next('ul').find('a').css({'background-color':bgcolor,'color':'#ffffff'});
				}
				if(_this.parent().parent().hasClass('ky-dropdown-menu')){
					_this.parent().parent().prev('a').css({'background-color':bgcolor,'color':'#ffffff'});
					_this.parent().parent('ul.ky-dropdown-menu').css('display','none');
				}
			});

			$('.ky-dropdown').mouseover(function(){
				$('.ky-dropdown > ul.ky-dropdown-menu').css('display','block');
				var bgcolor = $(this).children('a').css('border-left-color');
				$(this).find('.ky-dropdown-menu a').css({'background-color':bgcolor,'color':'#ffffff'});
			});

			$('.ky-dropdown').mouseout(function(){
				$('.ky-dropdown > ul.ky-dropdown-menu').css('display','none');
			});

			$('.ky-dropdown-menu a').mouseover(function(){
				var bgcolor = $(this).css('border-left-color');
				$(this).parent().parent().prev('a').css({'background-color':bgcolor,'color':'#ffffff'});
				console.log('message');
			});
		});
	</script>
</html>