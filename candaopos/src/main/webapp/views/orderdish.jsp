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
<title>点菜</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/orderdish.css">
<script src="<%=request.getContextPath()%>/scripts/page.js"></script>
<script type="text/javascript">

var nowPage4 = 0;//已选择菜品分页
var nowPage5 = 0;//待选择菜品分页

var activeinputele;
	$(document).ready(function(){
		$("img.img-close").hover(function(){
		 	$(this).attr("src","<%=request.getContextPath()%>/images/close-active.png");	 
		},function(){
			$(this).attr("src","<%=request.getContextPath()%>/images/close-sm.png");
		});
		$(".search input[type='search']").focus(function(event){
	        activeinputele = $(this);
		});
		//删除搜索条件
		$(".search .delsearch-btn").click(function(){
			$(".search input[type='search']").val("");
		});
		//搜索条件输入
		$(".search-btns div").click(function(){
			var keytext = $(this).text();
			if(activeinputele != null && activeinputele != undefined){
				var val = activeinputele.val();
				val = val + keytext;
				activeinputele.val(val);
				activeinputele.focus();
			}
		});
		//上一页
		$(".oper-div .prev-btn").click(function(){
			if($(this).hasClass("disabled")){
				return false;
			}
			page4(nowPage4-1);
		});
		//下一页
		$(".oper-div .next-btn").click(function(){
			if($(this).hasClass("disabled")){
				return false;
			}
			page4(nowPage4+1);
		});
		//上一页
		$(".main-div .prev-btn").click(function(){
			if($(this).hasClass("disabled")){
				return false;
			}
			page5(nowPage5-1);
		});
		//下一页
		$(".main-div .next-btn").click(function(){
			if($(this).hasClass("disabled")){
				return false;
			}
			page5(nowPage5+1);
		});
		page4(0);
		initDishType();
	});
	//已点菜品分页
	function page4(currPage ){
		nowPage4 = loadPage({
			obj: "#sel-dish-table tbody tr",
			listNum: 16,
	        currPage: currPage, 
	        totleNums: $("#sel-dish-table tbody tr").length,
	        curPageObj: "#adddish-modal #curr-page",
	        pagesLenObj: "#adddish-modal #pages-len",
	        prevBtnObj: "#adddish-modal .oper-div .prev-btn",
	        nextBtnObj: "#adddish-modal .oper-div .next-btn",
	        callback: function(){
	        	$("#sel-dish-table tbody tr").not(".hide").eq(0).addClass("selected");
	        }
		});
	}
	//菜品分页
	function page5(currPage){
		nowPage5 = loadPage({
			obj: ".dishes-content .dish-info",
			listNum: 20,
	        currPage: currPage, 
	        totleNums: $(".dishes-content .dish-info").length,
	        curPageObj: "#adddish-modal #curr-page1",
	        pagesLenObj: "#adddish-modal #pages-len1",
	        prevBtnObj: "#adddish-modal .main-div .prev-btn",
	        nextBtnObj: "#adddish-modal .main-div .next-btn"
		});
	}
	//菜品分类
	function initDishType(){
		var htm = '';
		for(var i=0; i< 20; i++){
			var cla = "";
			if(i == 0)
				cla = "active";
			htm += '<li class="'+cla+'">分类'+i+'</li>';
		}
		$(".nav-types").html(htm);
		initDishes();
		$(".nav-types li").click(function(){
			$(".nav-types li").removeClass("active");
			$(this).addClass("active");
			initDishes();
		});
	}
	//通过分类获取菜品信息
	function initDishes(){
		var htm = '';
		for(var i=0; i< 45; i++){
			htm += '<div class="dish-info" dishid="dish-id-'+i+'" dishname="菜品'+i+'" price="49">'
				+'<div class="dish-name">菜品'+i+'</div>'
				+'<hr>'
				+'<div class="dish-price">20元~999元</div>'
				+'</div>';
		}
		$(".main-div .dishes-content").html(htm);
		page5(nowPage5);
		$(".dishes-content .dish-info").click(function(){
			var tr = "";
			var dishname = $(this).attr("dishname");
			var price = $(this).attr("price");
			tr = "<tr><td>"+dishname+"</td><td>1</td><td>"+price+"</td></tr>";
			
			$("#sel-dish-table tbody").prepend(tr);
			$("#sel-dish-table tbody tr").removeClass("selected");
			page4(nowPage4);
			
			//选中已点菜品
			$("#sel-dish-table tbody tr").click(function(){
				$("#sel-dish-table tbody tr").removeClass("selected");
				$(this).addClass("selected");
			});
		});
	}
</script>
</head>
<body>
	<div class="modal-dialog main-modal-dialog" id="adddish-modal" data-backdrop="static" >
		<div class="modal-content">
			<div class="modal-body">
				<header>
					<div class="fl">餐道</div>
					<div class="fr close-win" id="close-adddish-dialog" data-dismiss="modal">关闭</div>
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
							<div class="dish-info">
								<table class="table display-table sel-dish-table"
									id="sel-dish-table">
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
								消费：￥<span id="amount">0</span>
							</div>
						</div>
						<div class="oper-div">
							<div class="btns">
								<div class="oper-btn prev-btn">
									<span class="glyphicon glyphicon-chevron-up"></span>
								</div>
								<div class="page-info">
									<span id="curr-page">0</span>/<span id="pages-len">0</span>
								</div>
								<div class="oper-btn next-btn">
									<span class="glyphicon glyphicon-chevron-down"></span>
								</div>
								<div class="oper-btn">
									<span class="glyphicon glyphicon-plus"></span>
								</div>
								<div class="oper-btn">
									<span class="glyphicon glyphicon-minus"></span>
								</div>
								<div class="oper-btn">
									<span>数量</span>
								</div>
								<div class="oper-btn">
									<span>备注</span>
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
							<div class="search-btns">
								<div>A</div>
								<div>B</div>
								<div>C</div>
								<div>D</div>
								<div>E</div>
								<div>F</div>
								<div>G</div>
								<div>H</div>
								<div>I</div>
								<div>J</div>
								<div>K</div>
								<div>L</div>
								<div>M</div>
								<div>N</div>
								<div>O</div>
								<div>P</div>
								<div>Q</div>
								<div>R</div>
								<div>S</div>
								<div>T</div>
								<div>U</div>
								<div>V</div>
								<div>W</div>
								<div>X</div>
								<div>Y</div>
								<div>Z</div>
							</div>
							<div class="search">
								<span class="glyphicon glyphicon-search"></span> <input
									type="search" class="form-control" placeholder="输入菜品名">
								<div class="delsearch-btn">C</div>
							</div>
							<div class="dishes">
								<div class="dishes-content"></div>
								<div class="page-btns">
									<div class="page-btn prev-btn">
										<span class="glyphicon glyphicon-chevron-up"></span>
									</div>
									<div class="page-info">
										<span id="curr-page1">0</span>/<span id="pages-len1">0</span>
									</div>
									<div class="page-btn next-btn">
										<span class="glyphicon glyphicon-chevron-down"></span>
									</div>
								</div>
							</div>
							<div class="main-oper-btns">
								<div>赠菜</div>
								<div>全单备注</div>
								<div>清空</div>
								<div class="place-order">下单</div>
							</div>
						</div>
					</div>
				</article>
				<footer>
					<div class="info">
						<span>店铺编号：</span><span>0012</span><span>&nbsp;登录员工：</span><span>&nbsp;收银员(008)</span><span>&nbsp;当前时间：</span>
						<span>2016-08-19 12:00:00</span><span>&nbsp;版本号：</span><span>1.01</span>
					</div>
				</footer>
			</div>
		</div>
	</div>
</body>
</html>