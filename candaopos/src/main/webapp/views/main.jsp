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
	var g_isopened = false;

	var g_eatType = "EAT-IN";//堂食
	var roomtype_prev = 0;
	$(document).ready(function(){
		$("img.img-close").hover(function(){
		 	$(this).attr("src","<%=request.getContextPath()%>/images/close-active.png");	 
		},function(){
			$(this).attr("src","<%=request.getContextPath()%>/images/close-sm.png");
		});
		
		$(".exit-sys").click(function(){
			window.location = "<%=request.getContextPath()%>/views/login.jsp";
		});
		$(".menu-tab ul li").click(function(){
			nowPage = 0;
			var olddiv = $(".menu-tab ul li.active").attr("loaddiv");
			$(olddiv).addClass("hide");
			$(".menu-tab ul li").removeClass("active");
			$(this).addClass("active");
			var loaddiv = $(this).attr("loaddiv");
			$(loaddiv).removeClass("hide");
			doPage(nowPage);
		});
		/*分类向左向右按钮*/
		$(".rooms-type .nav-type-next").click(function(){
			var count = $(".rooms-type .nav-types").find( "li").length;
			if (roomtype_prev < count - 10) {
				$(".rooms-type .nav-types").find("li").eq(roomtype_prev).css("margin-left", "-10%");
				$(".rooms-type .nav-types").find("li").eq(roomtype_prev+1).click();
				roomtype_prev++;
			}
		});
		$(".rooms-type .nav-type-prev").click(function(){
			if(roomtype_prev>=1){	
				$(".rooms-type .nav-types").find("li").eq(roomtype_prev-1).css("margin-left","1%");	
				$(".rooms-type .nav-types").find("li").eq(roomtype_prev-1).click();
				roomtype_prev--;
			}
		});
		/**
		* 分类点击
		*/
		$(".rooms-type .nav-types li").click(function() {
			$(".rooms-type .nav-types li").removeClass("active");
			$(this).addClass("active");
		});
		$("ul#standard-tables li").click(function(){
			g_eatType = "EAT-IN";
			var cla = $(this).attr("class");
			if(cla == "opened"){
				g_isopened = true;
				$("#order-dialog").load("<%=request.getContextPath()%>/views/order.jsp");
				$("#order-dialog").modal("show");
			}else if(cla == "reserved"){
				
			}else{
				g_isopened = false;
				$("#order-dialog").load("<%=request.getContextPath()%>/views/order.jsp");
				$("#order-dialog").modal("show");
			}
		});

		//顶部菜单
		$(".m-member>ul>li").click(function(){
			var me = $(this);
			if(me.hasClass('J-btn-register')) {
				$("#register-dialog").load("<%=request.getContextPath()%>/views/member/register.jsp");
				$("#register-dialog").modal("show");
			}

			if(me.hasClass('J-btn-storge')) {
				window.location.href = '<%=request.getContextPath()%>/views/member/storge.jsp';
			}

			if(me.hasClass('J-btn-memberView')) {
				window.location.href = './member/view.jsp';
			}
		});
		$(document).click(function(e){
			$(".m-member.popover").hide();
			e.stopPropagation();  
		});

		//底部菜单事件绑定
		$(".foot-menu li").click(function(e){
			var me = $(this);
			if(me.hasClass("J-btn-takeout")){
				$("#J-takeout-dialog").modal("show");
				$(".take-out-list li").unbind("click").on("click",  function(){
					$(".take-out-list li").removeClass("active");
					$(this).addClass("active");
				});
			}
			if(me.hasClass("member-btns")){
				//会员
				$(".m-member.popover").toggle();
				e.stopPropagation();
			}
			if(me.hasClass('J-btn-sys')) {
				$("#sys-dialog").load("<%=request.getContextPath()%>/views/sys.jsp");
				$("#sys-dialog").modal("show");
			}
			if(me.hasClass('J-btn-rep')) {
				$("#sys-dialog").load("<%=request.getContextPath()%>/views/reporting/reporting.jsp");
				$("#sys-dialog").modal("show");
			}
			if(me.hasClass('J-btn-check')) {
				window.location.href="<%=request.getContextPath()%>/views/check/check.jsp";

			}
			if(me.hasClass('J-btn-checkout')) {
				var str ='<strong>确定要结业吗？</strong>';

				var alertModal = Modal.alert({
					cls: 'fade in',
					content:str,
					width:400,
					height:500,
					title: "结业提醒",
					btnOkTxt: '确定',
					btnOkCb: function(){
						$(".modal-alert").modal("hide");
						$("#J-btn-checkout-dialog").load("<%=request.getContextPath()%>/views/check/impower.jsp",{"title" : "结业授权"});
						$("#J-btn-checkout-dialog").modal("show");
					},
					btnCancelCb: function(){
					}
				})

			}
			if(me.hasClass('J-btn-clear')) {
				var str ='<strong>请选择倒班或结业：</strong>'+
						'<div id="cleardata" class="form-group form-group-base" style="margin-top: 20px">'+
						'<button id="clearAll" class="btn-default btn-lg btn-base btn-base-flex2 clearAll" style="margin-right: 5px">倒班</button>'+
						'<button id="completion" class="btn-default btn-lg btn-base btn-base-flex2 clearCompletion" >结业</button>'+
						'</div>'+
						'<div class="glyphicon glyphicon-info-sign" style="color: #8c8c8c;">还有未结账的餐台不能结业</div>'

				var alertModal = Modal.alert({
					cls: 'fade in',
					content:str,
					width:400,
					height:500,
					title: "清机提醒",
					hasBtns:false,
				});
				$("#cleardata button").click(function () {
					var _this = $(this);
					if(_this.hasClass("clearAll")){
						$(".modal-alert").modal("hide");
						$("#J-btn-clear-dialog").load("<%=request.getContextPath()%>/views/check/impower.jsp",{"title" : "清机授权","clearType":"倒班"});
						$("#J-btn-clear-dialog").modal("show");
					}
					if(_this.hasClass("clearCompletion")){
						$(".modal-alert").modal("hide");
						$("#J-btn-clear-dialog").load("<%=request.getContextPath()%>/views/check/impower.jsp",{"title" : "清机授权","clearType":"结业"});
						$("#J-btn-clear-dialog").modal("show");
					}

				})

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
	//外带
	function takeOut(){
		g_isopened = true;
		g_eatType = "TAKE-OUT";
		$("#order-dialog").load("<%=request.getContextPath()%>/views/order.jsp");
		$("#order-dialog").modal("show");
		$("#adddish-dialog").load(global_path+"/views/orderdish.jsp");
		$("#adddish-dialog").modal("show");
	}
	function doPage(currPage){
		var o = $(".menu-tab ul li.active").attr("loaddiv") + " li";
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
		<!--div class="fl menu-top">
			<div class="J-btn-memberView">会员查询</div><div class="J-btn-storge">会员储值</div><div class="J-btn-register">会员注册</div>
		</div-->
		<div class="menu-tab">
			<ul><li class="active" loaddiv="#standard-tables">标准台</li><li loaddiv="#coffee-tables">咖啡台</li></ul>
		</div>
		<div class="exit-sys">退出系统</div>
	</header>
	<article>
		<!--div class="tab-div">
			<ul><li class="active" loaddiv="#standard-tables">标准台</li><li loaddiv="#coffee-tables">咖啡台</li></ul>
		</div-->
		<div class="rooms-type">
			<div class="nav-type-btn nav-type-prev">
				<span class="glyphicon glyphicon-chevron-left"></span>
			</div>
			<ul class="nav-types">
				<li class="active">全部</li><li>包间 1</li><li>包间2</li><li>包间3</li><li>包间4</li><li>包间5</li><li>包间6</li><li>包间7</li><li>包间8</li><li>包间9</li><li>包间10</li><li>包间11</li><li>包间12</li><li>包间13</li><li>包间14</li><li>包间15</li><li>包间16</li><li>包间17</li><li>包间18</li>
			</ul>
			<div class="nav-type-btn nav-type-next">
				<span class="glyphicon glyphicon-chevron-right"></span>
			</div>
		</div>
		<div class="content">
			<ul id="standard-tables" class="standard tables">
				<li class="opened">110
					<div class="tb-info tb-status">￥246</div>
					<div class="tb-info meal-time">12:00</div>
					<div class="tb-info tb-person">2/2</div>
				</li>
				<!--li class="reserved">111<div class="tb-info tb-status">预</div><div class="tb-info tb-person">2人桌</div></li-->
				<li>外卖台<div class="tb-info tb-person">2人桌</div></li>
				<li class="opened">贵宾台
					<div class="tb-info tb-status">￥246</div>
					<div class="tb-info meal-time">16:00</div>
					<div class="tb-info tb-person">2/2</div>
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
			<div class="total-num active">全部（16）</div><div class="free-num">空闲（10）</div><div>就餐（11）</div>
		</div>
		<div class="foot-menu">
			<ul><li class="J-btn-takeout">外卖</li>
				<li class="J-btn-check">账单</li>
				<li class="J-btn-rep">报表</li>
				<li class="J-btn-clear">清机 / 结业</li>
				<!--li class="J-btn-checkout">结业</li-->
				<li class="member-btns">会员</li>
				<div class="m-member popover fade top in">
					<div class="arrow" style="left: 50%;"></div>
					<ul><li class="J-btn-storge">会员储值</li>
					<li class="J-btn-register">会员注册</li>
					<li class="J-btn-memberView">会员查询</li></ul>
				</div>
				<li class="J-btn-sys">系统设置</li>
			</ul>
			<div class="page"><div class="page-btn prev-btn">&#60;</div><span id="curr-page">0</span>/<span id="pages-len">0</span><div class="page-btn next-btn">&#62;</div></div>
		</div>
		<div class="form-group amount-div">
			<span class="highlight-color">总金额：</span><span class="highlight-color">1360.999</span>
			<span>&nbsp;&nbsp;已结金额：</span><span>1360.999</span>
			<span>&nbsp;&nbsp;未结金额：</span><span>0</span>
			<span>&nbsp;&nbsp;人数：</span><span>100</span>
			<span>&nbsp;&nbsp;已结单数：</span><span>100</span>
		</div>
		<div class="info"><span>店铺编号：</span><span>0012</span><span>&nbsp;登录员工：</span><span>&nbsp;收银员(008)</span><span>&nbsp;当前时间：</span><span>2016-08-19 12:00:00</span><span>&nbsp;版本号：</span><span>1.01</span></div>
	</footer>
	<div class="modal fade in main-dialog" data-backdrop="static" id="order-dialog" style="overflow: auto;">
	</div>
	<div class="modal in main-dialog" data-backdrop="static" id="adddish-dialog" style="overflow: auto;">
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
	<!-- 外卖 -->
	<div class="modal fade default-dialog J-takeout-dialog in " id="J-takeout-dialog"
	     data-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content">
	        	<div class="dialog-sm-header">
	        		<div class="modal-title" style="text-align: center;">备注桌号</div>
	                <img src="<%=request.getContextPath()%>/images/close-sm.png" class="img-close" data-dismiss="modal">
	            </div>
	            <div class="modal-body">
	            	<div class="form-group">
	            		<ul class="take-out-list">
	            			<li class="active">咖啡外卖1</li>
	            			<li>咖啡外卖2</li>
	            			<li>咖啡外卖3</li>
	            			<li>咖啡外卖4</li>
	            			<li>咖啡外卖5</li>
	            		</ul>
	            	</div>
	            	<div class="btn-operate  ">
	            		<button class="btn btn-cancel in-btn135 clear-btn" style="float: left;" type="button" data-dismiss="modal" onclick="takeOut()">外带
		                </button>
	                    <div style="text-align: right;">
	                    	<button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal">取消
		                    </button>
		                    <button class="btn btn-save in-btn135" id="" type="button" data-dismiss="modal" onclick="takeOut()">确认
		                    </button>
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
	 </div>
	 <!-- 选择挂账单位 -->
	<div class="modal fade in default-dialog" id="select-paycompany-dialog"
	     data-backdrop="static">
	    <div class="modal-dialog" style="width: 800px;">
	        <div class="modal-content">
	        	<div class="dialog-sm-header">
	        		<div class="modal-title">餐道</div>
	                <img src="<%=request.getContextPath()%>/images/close-sm.png" class="img-close" onclick="closeConfirm('select-paycompany-dialog')">
	            </div>
	            <div class="modal-body">
	            	<div style="font-size: 20px;font-weight: bold;">请选择挂账单位</div>
					<div class="form-group search-btns">
						<div class="search-btn">A</div>
						<div class="search-btn">B</div>
						<div class="search-btn">C</div>
						<div class="search-btn">D</div>
						<div class="search-btn">E</div>
						<div class="search-btn">F</div>
						<div class="search-btn">G</div>
						<div class="search-btn">H</div>
						<div class="search-btn">I</div>
						<div class="search-btn">J</div>
						<div class="search-btn">K</div>
						<div class="search-btn">L</div>
						<div class="search-btn">M</div>
						<div class="search-btn">N</div>
						<div class="search-btn">O</div>
						<div class="search-btn">P</div>
						<div class="search-btn">Q</div>
						<div class="search-btn">R</div>
						<div class="search-btn">S</div>
						<div class="search-btn">T</div>
						<div class="search-btn">U</div>
						<div class="search-btn">V</div>
						<div class="search-btn">W</div>
						<div class="search-btn">X</div>
						<div class="search-btn">Y</div>
						<div class="search-btn">Z</div>
					</div>
					<div class="form-group search">
						<span class="glyphicon glyphicon-search"></span> 
						<input type="search" class="form-control" placeholder="输入首字母过滤">
						<div class="delsearch-btn">C</div>
					</div>
					<div class="form-group paycompany-content">
						<ul>
							<li>合作单位1111111111111111111111111</li>
							<li>合作单位1111111111111111111111111</li>
							<li>合作单位1111111111111111111111111</li>
							<li>合作单位1111111111111111111111111</li>
							<li>合作单位1111111111111111111111111</li>
							<li>合作单位1111111111111111111111111</li>
							<li>合作单位1111111111111111111111111</li>
							<li>合作单位1111111111111111111111111</li>
							<li>合作单位1111111111111111111111111</li>
						</ul>
					</div>
					<div class="btn-operate  ">
						<div class="page">
							<button class="btn page-btn prev-btn">
								<span class="glyphicon glyphicon-chevron-left"></span>
							</button>
							<div class="page-info" style="display: inline-block;">
								<span id="pay-curr-page">0</span>/<span id="pay-pages-len">0</span>
							</div>
							<button class="btn page-btn next-btn">
								<span class="glyphicon glyphicon-chevron-right"></span>
							</button>
						</div>
						<div style="text-align: right;">
		                    <button class="btn btn-cancel in-btn135" type="button" onclick="closeConfirm('select-paycompany-dialog')">取消
		                    </button>
		                    <button class="btn btn-save in-btn135" id="" type="button" onclick="">确认
		                    </button>
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
	<div class="modal fade in main-dialog" data-backdrop="static" id="sys-dialog" style="overflow: auto;">
	</div>
	<div class="modal fade in dialog-normal bg-gray" data-backdrop="static" id="register-dialog" style="overflow: auto;"></div>
	<div class="modal fade in dialog-normal bg-gray" data-backdrop="static" id="J-btn-checkout-dialog" style="overflow: auto;"></div>
	<div class="modal fade in dialog-normal bg-gray" data-backdrop="static" id="J-btn-clear-dialog" style="overflow: auto;"></div>
</body>
</html>