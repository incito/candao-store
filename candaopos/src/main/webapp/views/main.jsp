<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8"/>
<meta name="viewport" content="width=device-width,initial-scale=1, user-scalable=no, minimum-scale=1.0,maximum-scale=1.0"/>
<!-- 让 IE 浏览器运行最新的渲染模式下 -->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- 让部分国产浏览器默认采用高速模式渲染页面 -->
<meta name="renderer" content="webkit">
<title>主页</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/bootstrap-3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="<%=request.getContextPath()%>/scripts/jquery-3.1.0.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="<%=request.getContextPath()%>/tools/bootstrap-3.3.5/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/scripts/common.js"></script>
<script src="<%=request.getContextPath()%>/scripts/page.js"></script>
<script src="<%=request.getContextPath()%>/scripts/main.js"></script>
<script type="text/javascript">
	var global_path = "<%=request.getContextPath()%>";
	var nowPage = 0;
	var isopened = false;
	$(document).ready(function(){
		$("img.img-close").hover(function(){
		 	$(this).attr("src","<%=request.getContextPath()%>/images/close-active.png");	 
		},function(){
			$(this).attr("src","<%=request.getContextPath()%>/images/close-sm.png");
		});
		
		$(".exit-sys").click(function(){
			window.location = "<%=request.getContextPath()%>/views/login.jsp";
		});
		$(".tab-div ul li").click(function(){
			nowPage = 0;
			var olddiv = $(".tab-div ul li.active").attr("loaddiv");
			$(olddiv).addClass("hide");
			$(".tab-div ul li").removeClass("active");
			$(this).addClass("active");
			var loaddiv = $(this).attr("loaddiv");
			$(loaddiv).removeClass("hide");
			doPage(nowPage);
		});
		$("ul#standard-tables li").click(function(){
			var cla = $(this).attr("class");
			if(cla == "opened"){
				isopened = true;
				$("#order-dialog").load("<%=request.getContextPath()%>/views/order.jsp");
				$("#order-dialog").modal("show");
			}else if(cla == "reserved"){
				
			}else{
				isopened = false;
				$("#order-dialog").load("<%=request.getContextPath()%>/views/order.jsp");
				$("#order-dialog").modal("show");
			}
		});

		//顶部菜单
		$(".menu-top>div").click(function(){
			var me = $(this);
			if(me.hasClass('J-btn-register')) {
				$("#register-dialog").load("<%=request.getContextPath()%>/views/member/register.jsp");
				$("#register-dialog").modal("show");
			}
		}).click();


		//底部菜单事件绑定
		$(".foot-menu li").click(function(){
			var me = $(this);
			if(me.hasClass('J-btn-sys')) {
				$("#sys-dialog").load("<%=request.getContextPath()%>/views/sys.jsp");
				$("#sys-dialog").modal("show");
			}
		});


		$(".page .prev-btn").click(function(){
			if($(this).hasClass("disabled")){
				return false;
			}
			doPage(nowPage-1);
		});
		$(".page .next-btn").click(function(){
			if($(this).hasClass("disabled")){
				return false;
			}
			doPage(nowPage+1);
		});
		doPage(nowPage);
	});
	function doPage(currPage){
		var o = $(".tab-div ul li.active").attr("loaddiv") + " li";
		var options = {
				obj : o,
				listNum : 40,
				currPage : currPage,
				totleNums : $(o).length,
				curPageObj : "#curr-page",
				pagesLenObj : "#pages-len",
				prevBtnObj : ".page .prev-btn",
				nextBtnObj : ".page .next-btn"
			};
		nowPage = page(options);
	}
</script>
</head>
<body>
	<header>
		<div class="fl">餐道</div>
		<div class="fl menu-top">
			<div>会员查询</div><div>会员储值</div><div class="J-btn-register">会员注册</div>
		</div>
		<div class="fr exit-sys">退出系统</div>
	</header>
	<article>
		<div class="tab-div">
			<ul><li class="active" loaddiv="#standard-tables">标准台</li><li loaddiv="#coffee-tables">咖啡台</li></ul>
		</div>
		<div class="content">
			<ul id="standard-tables" class="standard tables">
				<li class="opened">110
					<div class="tb-info tb-status">开</div>
					<div class="tb-info meal-time">12:00</div>
					<div class="tb-info tb-person">2人桌/2人</div>
				</li>
				<li class="reserved">111<div class="tb-info tb-status">预</div><div class="tb-info tb-person">2人桌</div></li>
				<li>外卖台<div class="tb-info tb-person">2人桌</div></li>
				<li class="opened">贵宾台
					<div class="tb-info tb-status">开</div>
					<div class="tb-info meal-time">16:00</div>
					<div class="tb-info tb-person">2人桌/2人</div>
				</li>
				<li>112<div class="tb-info tb-person">2人桌</div></li><li>113<div class="tb-info tb-person">2人桌</div></li>
				<li>114<div class="tb-info tb-person">4人桌</div></li><li>115<div class="tb-info tb-person">2人桌</div></li>
				<li>116<div class="tb-info tb-person">4人桌</div></li><li>116<div class="tb-info tb-person">4人桌</div></li>
				<li>116<div class="tb-info tb-person">4人桌</div></li><li>116<div class="tb-info tb-person">4人桌</div></li>
				<li>116<div class="tb-info tb-person">4人桌</div></li><li>116<div class="tb-info tb-person">4人桌</div></li>
				<li>116<div class="tb-info tb-person">4人桌</div></li><li>116<div class="tb-info tb-person">4人桌</div></li>
				<li>116<div class="tb-info tb-person">4人桌</div></li><li>116<div class="tb-info tb-person">4人桌</div></li>
				<li>116<div class="tb-info tb-person">4人桌</div></li><li>116<div class="tb-info tb-person">4人桌</div></li>
				<li>116<div class="tb-info tb-person">4人桌</div></li><li>116<div class="tb-info tb-person">4人桌</div></li>
				<li>116<div class="tb-info tb-person">4人桌</div></li><li>116<div class="tb-info tb-person">4人桌</div></li>
				<li>116<div class="tb-info tb-person">4人桌</div></li><li>116<div class="tb-info tb-person">4人桌</div></li>
				<li>116<div class="tb-info tb-person">4人桌</div></li><li>116<div class="tb-info tb-person">4人桌</div></li>
				<li>116<div class="tb-info tb-person">4人桌</div></li><li>116<div class="tb-info tb-person">4人桌</div></li>
				<li>116<div class="tb-info tb-person">4人桌</div></li><li>116<div class="tb-info tb-person">4人桌</div></li>
				<li>116<div class="tb-info tb-person">4人桌</div></li><li>116<div class="tb-info tb-person">4人桌</div></li>
				<li>116<div class="tb-info tb-person">4人桌</div></li><li>116<div class="tb-info tb-person">4人桌</div></li>
				<li>116<div class="tb-info tb-person">4人桌</div></li><li>116<div class="tb-info tb-person">4人桌</div></li>
				<li>116<div class="tb-info tb-person">4人桌</div></li><li>116<div class="tb-info tb-person">4人桌</div></li>
				<li>116<div class="tb-info tb-person">5人桌</div></li><li>116<div class="tb-info tb-person">6人桌</div></li>
			</ul>
			<ul id="coffee-tables" class="hide coffee tables">
				<li>1台<div class="tb-info tb-person">2人桌</div></li><li>2台<div class="tb-info tb-person">2人桌</div></li>
				<li>3台<div class="tb-info tb-person">2人桌</div></li>
			</ul>
		</div>
	</article>
	<footer>
		<div class="table-nums">
			<span class="free-num">空闲（10）</span><span>就餐（11）</span>
		</div>
		<div class="foot-menu">
			<ul><li>外卖</li><li>账单</li><li>报表</li><li>清机</li><!--li>结业</li--><li>会员</li><li class="J-btn-sys">系统设置</li></ul>
			<div class="page"><div class="page-btn prev-btn">&#60;</div><span id="curr-page">0</span>/<span id="pages-len">0</span><div class="page-btn next-btn">&#62;</div></div>
		</div>
		<div class="info"><span>店铺编号：</span><span>0012</span><span>&nbsp;登录员工：</span><span>&nbsp;收银员(008)</span><span>&nbsp;当前时间：</span><span>2016-08-19 12:00:00</span><span>&nbsp;版本号：</span><span>1.01</span></div>
	</footer>
	<div class="modal fade in main-dialog" data-backdrop="static" id="order-dialog" style="overflow: auto;">
	</div>
	<div class="modal fade in main-dialog" data-backdrop="static" id="adddish-dialog" style="overflow: auto;">
	</div>
	<div class="modal fade in main-dialog" data-backdrop="static" id="sys-dialog" style="overflow: auto;">
	</div>
	<div class="modal fade in dialog-normal bg-gray" data-backdrop="static" id="register-dialog" style="overflow: auto;">
	</div>
	<!-- 提示用dialog -->
	<div class="modal fade dialog-sm confirm-dialog in" id="tips-dialog"
	     data-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content">
	        	<div class="dialog-sm-header">
	        		<div class="modal-title"></div>
	                <img src="<%=request.getContextPath()%>/images/close-sm.png" class="img-close" onclick="closeConfirm('tips-dialog')">
	            </div>
	            <div class="modal-body">
	            	<p class="p1" style="text-align: center; padding-top: 20px; font-size: 16px;" id="tips-msg"></p>
	            	<div class="btn-operate  ">
	                    <button class="btn btn-save in-btn135" id="" type="button" onclick="closeConfirm('tips-dialog')">确认
	                    </button>
	                </div>
	            </div>
	        </div>
	    </div>
	 </div>
</body>
</html>