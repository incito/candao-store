<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width,initial-scale=1, user-scalable=no, minimum-scale=1.0,maximum-scale=1.0" />
<!-- 让 IE 浏览器运行最新的渲染模式下 -->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- 让部分国产浏览器默认采用高速模式渲染页面 -->
<meta name="renderer" content="webkit">
<title>订单</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/orderdish.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/order.css">
<script src="<%=request.getContextPath()%>/scripts/page.js"></script>
<script type="text/javascript">

var nowPage1 = 0;//已选择菜品分页
var nowPage2 = 0;//已选择优惠分页
var nowPage3 = 0;//待选优惠分页

	$(document).ready(function(){
		$("img.img-close").hover(function(){
		 	$(this).attr("src","<%=request.getContextPath()%>/images/close-active.png");	 
		},function(){
			$(this).attr("src","<%=request.getContextPath()%>/images/close-sm.png");
		});
		
		//点击加菜
		$("#add-dish").click(function(){
			$("#adddish-dialog").load("<%=request.getContextPath()%>/views/orderdish.jsp");
			$("#adddish-dialog").modal("show");
		});
		
		//已点菜上一页
		$(".dish-oper-btns .prev-btn").click(function() {
			if ($(this).hasClass("disabled")) {
				return false;
			}
			page1(nowPage1 - 1);
		});
		//已点菜下一页
		$(".dish-oper-btns .next-btn").click(function() {
			if ($(this).hasClass("disabled")) {
				return false;
			}
			page1(nowPage1 + 1);
		});
		//已选优惠上一页
		$(".preferential-oper-btns .prev-btn").click(
				function() {
					if ($(this).hasClass("disabled")) {
						return false;
					}
					page2(nowPage2 - 1);
				});
		//已选优惠下一页
		$(".preferential-oper-btns .next-btn").click(
				function() {
					if ($(this).hasClass("disabled")) {
						return false;
					}
					page2(nowPage2 + 1);
				});
		//优惠上一页
		$(".main-div .prev-btn").click(function() {
			if ($(this).hasClass("disabled")) {
				return false;
			}
			page3(nowPage3 - 1);
		});
		//优惠下一页
		$(".main-div .next-btn").click(function() {
			if ($(this).hasClass("disabled")) {
				return false;
			}
			page3(nowPage3 + 1);
		});
		
		$(".tab-payment ul li").click(function(){
			$(".tab-payment ul li").removeClass("active");
			$(this).addClass("active");
			
			$(".paytype-input").addClass("hide");
			var targetId = $(this).attr("target");
			$(targetId).removeClass("hide");
		});
		initPreferentialType();
		initOrderDish();
	});
	//已点菜品分页
	function page1(currPage) {
		nowPage1 = loadPage({
			obj : "#order-dish-table tbody tr",
			listNum : 6,
			currPage : currPage,
			totleNums : $("#order-dish-table tbody tr").length,
			curPageObj : "#order-modal #curr-page1",
			pagesLenObj : "#order-modal #pages-len1",
			prevBtnObj : "#order-modal .dish-oper-btns .prev-btn",
			nextBtnObj : "#order-modal .dish-oper-btns .next-btn",
			callback : function() {
				$("#order-dish-table tbody tr").not(".hide").eq(0).addClass(
						"selected");
			}
		});
	}
	//已选优惠分页
	function page2(currPage) {
		nowPage2 = loadPage({
			obj : "#sel-preferential-table tbody tr",
			listNum : 6,
			currPage : currPage,
			totleNums : $("#sel-preferential-table tbody tr").length,
			curPageObj : "#order-modal #curr-page2",
			pagesLenObj : "#order-modal #pages-len2",
			prevBtnObj : "#order-modal .preferential-oper-btns .prev-btn",
			nextBtnObj : "#order-modal .preferential-oper-btns .next-btn"
		});
	}
	//优惠分页
	function page3(currPage) {
		nowPage3 = loadPage({
			obj : ".preferentials-content .preferential-info",
			listNum : 16,
			currPage : currPage,
			totleNums : $(".preferentials-content .preferential-info").length,
			curPageObj : "#order-modal #curr-page3",
			pagesLenObj : "#order-modal #pages-len3",
			prevBtnObj : "#order-modal .main-div .prev-btn",
			nextBtnObj : "#order-modal .main-div .next-btn"
		});
	}
	function initOrderDish(){
		for(var i=0; i< 14;i++){
			var tr = "";
			tr = "<tr><td>菜品"+i+"</td><td>1</td><td>49</td></tr>";
			
			$("#order-dish-table tbody").prepend(tr);
		}
		page1(0);
	}
	//优惠分类
	function initPreferentialType() {
		var htm = '';
		for (var i = 0; i < 20; i++) {
			var cla = "";
			if (i == 0)
				cla = "active";
			htm += '<li class="'+cla+'">优惠' + i + '</li>';
		}
		$(".nav-types").html(htm);
		initPreferential();
		$(".nav-types li").click(function() {
			$(".nav-types li").removeClass("active");
			$(this).addClass("active");
			initPreferential();
		});
	}
	//通过分类获取菜品信息
	function initPreferential() {
		var htm = '';
		for (var i = 0; i < 20; i++) {
			htm += '<div class="preferential-info" dishid="dish-id-'+i+'" dishname="菜品'+i+'" price="49">'
					+ '<div class="dish-name">优惠活动' + i + '</div>' + '</div>';
		}
		$(".main-div .preferentials-content").html(htm);
		page3(nowPage3);
		$(".preferentials-content .preferential-info").click(
				function() {
					var tr = "";
					var dishname = $(this).attr("dishname");
					var price = $(this).attr("price");
					tr = "<tr><td>" + dishname + "</td><td>1</td><td>" + price
							+ "</td></tr>";

					$("#sel-preferential-table tbody").prepend(tr);
					$("#sel-preferential-table tbody tr").removeClass(
							"selected");
					page2(nowPage2);

					//选中已选优惠
					$(".sel-preferential-table tbody tr").click(
							function() {
								$(".sel-preferential-table tbody tr")
										.removeClass("selected");
								$(this).addClass("selected");
							});
				});
	}
	function changeKeyboard(type){
		if(type == "num"){
			$("#num-keyboard").removeClass("hide");
			$("#letter-keyboard").addClass("hide");
		}else if(type == "letter"){
			$("#num-keyboard").addClass("hide");
			$("#letter-keyboard").removeClass("hide");
		}
	}
</script>
</head>
<body>
	<div class="modal-dialog" id="order-modal"
		data-backdrop="static">
		<div class="modal-content">
			<div class="modal-body">
				<header>
					<div class="fl">餐道</div>
					<div class="fr close-win" data-dismiss="modal">关闭</div>
				</header>
				<article>
					<div class="content">
						<div class="left-div">
							<div class="order-info">
								<div>
									账单号：<span>E210234782737478</span>
								</div>
								<div>
									桌号：<span>002</span>&nbsp;&nbsp;&nbsp;&nbsp;人数：<span>6</span>
								</div>
							</div>
							<div class="dish-sel">
								<table class="table display-table sel-dish-table"
									id="order-dish-table">
									<thead>
										<tr>
											<th width="60%">菜品名称</th>
											<th width="20%">数量</th>
											<th>金额</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
							<hr class="lf-hr">
							<div class="total-amount">
								消费：￥<span id="amount">548</span>
							</div>
							<div class="preferential-sel">
								<table class="table display-table sel-dish-table"
									id="sel-preferential-table">
									<thead>
										<tr>
											<th width="60%">优惠方式</th>
											<th width="20%">数量</th>
											<th>金额(元)</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
							<hr class="discount-hr">
							<div class="discount-total-amount">
								优惠总额：￥<span id="discount-amount">40</span>
							</div>
							<hr class="lf-hr">
							<div class="total-amount">
								应收：￥<span id="should-amount">548</span>
							</div>
						</div>
						<div class="oper-div">
							<div class="dish-oper-btns btns">
								<div class="oper-btn prev-btn">
									<span class="glyphicon glyphicon-chevron-up"></span>
								</div>
								<div class="page-info">
									<span id="curr-page1">0</span>/<span id="pages-len1">0</span>
								</div>
								<div class="oper-btn next-btn">
									<span class="glyphicon glyphicon-chevron-down"></span>
								</div>
								<div class="oper-btn" id="add-dish">
									<span class="glyphicon glyphicon-plus"></span>
								</div>
								<div class="oper-btn" id="back-dish">
									<span class="glyphicon glyphicon-minus"></span>
								</div>
							</div>
							<div class="preferential-oper-btns btns">
								<div class="oper-btn prev-btn">
									<span class="glyphicon glyphicon-chevron-up"></span>
								</div>
								<div class="page-info">
									<span id="curr-page2">0</span>/<span id="pages-len2">0</span>
								</div>
								<div class="oper-btn next-btn">
									<span class="glyphicon glyphicon-chevron-down"></span>
								</div>
								<div class="oper-btn" id="del-pref">
									<span class="glyphicon glyphicon-minus"></span>
								</div>
								<div class="oper-btn" id="clear-pref">
									<span>C</span>
								</div>
							</div>
						</div>
						<div class="main-div">
							<div class="dish-type">
								<div class="nav-type-prev">
									<span class="glyphicon glyphicon-chevron-left"></span>
								</div>
								<ul class="nav-types"></ul>
								<div class="nav-type-next nav-type">
									<span class="glyphicon glyphicon-chevron-right"></span>
								</div>
							</div>
							<div class="preferentials">
								<div class="preferentials-content"></div>
								<div class="page-btns">
									<div class="page-btn prev-btn">
										<span class="glyphicon glyphicon-chevron-up"></span>
									</div>
									<div class="page-info">
										<span id="curr-page3">0</span>/<span id="pages-len3">0</span>
									</div>
									<div class="page-btn next-btn">
										<span class="glyphicon glyphicon-chevron-down"></span>
									</div>
								</div>
							</div>
							<hr style="border: 1px solid #D3D3D3;">
							<div class="tab-payment">
								<ul><li class="active" target="#cash">现金支付</li><li>银行卡</li><li target="#membership-card">会员卡</li><li target="#this-card">挂账支付</li><li target="#pay-treasure">支付宝</li><li>微信支付</li></ul>
							</div>
							<div class="pay-div">
								<div class="paytype-input cash" id="cash">
									<span>金额：</span> <input type="text"
										class="form-control cash-input">
								</div>
								<div class="paytype-input membership-card hide"
									id="membership-card">
									<input type="text" class="form-control">
								</div>
								<div class="paytype-input this-card hide" id="this-card">
									<input type="text" class="form-control">
								</div>
								<div class="paytype-input pay-treasure hide" id="pay-treasure"></div>
								<div class="virtual-keyboard num-virtual-keyboard hide" id="num-keyboard">
									<ul>
										<li>1</li><li>2</li><li>3</li>
									</ul>
									<ul>
										<li>4</li><li>5</li><li>6</li>
									</ul>
									<ul>
										<li>7</li><li>8</li><li>9</li>
									</ul>
									<ul>
										<li>.</li><li>0</li><li>00</li>
									</ul>
									<ul>
										<li>←</li><li onclick="changeKeyboard('letter')">字母</li><li>确定</li>
									</ul>
								</div>
								<div class="virtual-keyboard letter-virtual-keyboard" id="letter-keyboard">
									<ul>
										<li>A</li><li>B</li><li>C</li><li>D</li><li>E</li><li>F</li>
									</ul>
									<ul>
										<li>G</li><li>H</li><li>I</li><li>J</li><li>K</li><li>L</li>
									</ul>
									<ul>
										<li>M</li><li>N</li><li>O</li><li>P</li><li>Q</li><li>R</li>
									</ul>
									<ul>
										<li>S</li><li>T</li><li>U</li><li>V</li><li>W</li><li>X</li>
									</ul>
									<ul>
										<li>Y</li><li>Z</li><li>←</li><li onclick="changeKeyboard('num')">数字</li><li>确定</li>
									</ul>
								</div>
							</div>
						</div>
						<footer>
							<div class="info">
								<span>店铺编号：</span><span>0012</span><span>&nbsp;登录员工：</span><span>&nbsp;收银员(008)</span><span>&nbsp;当前时间：</span><span>2016-08-19
									12:00:00</span><span>&nbsp;版本号：</span><span>1.01</span>
							</div>
						</footer>
					</div>
				</article>
			</div>
		</div>
	</div>
</body>
</html>