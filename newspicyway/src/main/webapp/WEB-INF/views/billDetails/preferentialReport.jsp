<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>优惠活动统计</title>
<link rel="shortcut icon" href="../asset/ico/favicon.png">
<link
	href="<%=request.getContextPath()%>/tools/echarts/css/font-awesome.min.css"
	rel="stylesheet">
<link
	href="<%=request.getContextPath()%>/tools/echarts/css/carousel.css"
	rel="stylesheet">
<link
	href="<%=request.getContextPath()%>/tools/echarts/css/echartsHome.css"
	rel="stylesheet">
<script src="<%=request.getContextPath()%>/tools/echarts/js/echarts.js"></script>
<script
	src="<%=request.getContextPath()%>/tools/echarts/js/codemirror.js"></script>
<script
	src="<%=request.getContextPath()%>/tools/echarts/js/javascript.js"></script>
<link
	href="<%=request.getContextPath()%>/tools/echarts/css/codemirror.css"
	rel="stylesheet">
<link href="<%=request.getContextPath()%>/tools/echarts/css/monokai.css"
	rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/prefreport.css"
	rel="stylesheet">
<!-- Fixed navbar -->
</head>
<body>
	<div class="report_head">
		<button class="btn btn-default" id="to_detail">查看明细</button>
	</div>
	<div class="bottom-div">
		<div class="row-fluid example">
			<!--/span-->
			<div class="item-type">
				<div class="nav-types-prev " id="nav-types-prev" style="visibility: hidden;">
					<i class="icon-chevron-left"></i>
				</div>
				<ul class="tab-ul nav-coup-type" id="preferential-type-first">
				</ul>
				<div class="nav-types-next " id="nav-types-next" style="visibility: hidden;">
					<i class="icon-chevron-right"></i>
				</div>
			</div>
			<div id="nums_graphic" class="col-md-14 graphic">
			<div class="p-main lf">
				<div id="nums_main" class="main"
					style='width: 100%; float: left; margin-right: 0; padding-right: 0; border-right-width: 0'>
				</div>
			</div>
			<div class="p-main rt">
				<div id="nums_trend_main" class="main"
					style='width: 100%;margin-left: 0; padding-left: 0; border-left-width: 0'>
				</div>
			</div>
			</div>
			<!--/span-->
			<div id="amount_graphic" class="col-md-14 graphic">
			<div class="p-main lf">
				<div id="amount_main" class="main"
					style='width: 100%;float: left; margin-right: 0; padding-right: 0; border-right-width: 0'>
				</div>
			</div>
			<div class="p-main rt">
				<div id="amount_trend_main" class="main"
					style='width: 100%;margin-left: 0; padding-left: 0; border-left-width: 0'>
				</div>
			</div>
			</div>
			<!--/span-->
			<div id="should_graphic" class="col-md-14 graphic">
			<div class="p-main lf">
				<div id="should_amount_main" class="main"
					style='width: 100%;float: left; margin-right: 0; padding-right: 0; border-right-width: 0'>
				</div>
			</div>
			<div class="p-main rt">
				<div id="should_amount_trend_main" class="main"
					style='width: 100%;margin-left: 0; padding-left: 0; border-left-width: 0'>
				</div>
			</div>
			</div>
			<!--/span-->
			<div id="paidin_graphic" class="col-md-14 graphic">
			<div class="p-main lf">
				<div id="paidin_amount_main" class="main"
					style='width: 100%;float: left; margin-right: 0; padding-right: 0; border-right-width: 0'>
				</div>
			</div>
			<div class="p-main rt">
				<div id="paidin_amount_trend_main" class="main"
					style='width: 100%;margin-left: 0; padding-left: 0; border-left-width: 0'>
				</div>
			</div>
			</div>
		</div>
		<!--/row-->
	</div>

	<script src="<%=request.getContextPath()%>/scripts/jquery.min.js"></script>
	<script
		src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/report.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/prefreport.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/zxx.color_exchange.js"></script>
	<script type="text/javascript">
		var type = "活动名称";
		var up_num = 0;
		$(function() {
			getPreferentialType();
			$("#to_detail").click(function(){
				orgTime();
				toDetail(2,beginTime ,endTime);
			});
			
			$(".item-type").hover(function(){
				var _this =$(this).find(".nav-coup-type");
				if(_this.children().length>10){
					_this.prev().css("visibility","visible");
					_this.next().css("visibility","visible");
				}
			},function(){
				var _this =$(this).find(".nav-coup-type");
				_this.prev().css("visibility","hidden");
				_this.next().css("visibility","hidden");
			});
			$("#nav-types-prev").click(function(event){
				if(up_num>=1){
					$(this).next(".nav-coup-type").find("li").eq(up_num-1).css("margin-left","0");
					up_num--;
				}
			});
			$("#nav-types-next").click(function(){
				var count = $(this).prev(".nav-coup-type").find("li").length;
				if(up_num<count-10){
					$(this).prev(".nav-coup-type").find("li").eq(up_num).css("margin-left","-10%");
					up_num++;
				}
			});
			/*鼠标滚动*/
		     var user_agent = navigator.userAgent;
			 var dom1 =$("#preferential-type-first")[0];
		     if(user_agent.indexOf("Firefox")!=-1){// Firefox
		    	 dom1.addEventListener("DOMMouseScroll",addEvent,!1);
		     } else if(user_agent.indexOf("MSIE")!=-1){// Firefox
		         dom1.attachEvent("onmousewheel",addEvent,!1);
		     }else{
		    	 dom1.addEventListener("mousewheel",addEvent,!1);
		     }
		});
		function addEvent(event) {
			event = event || window.event;
			var type = event.type;
			if (type == 'DOMMouseScroll' || type == 'mousewheel') {
				event.delta = (event.wheelDelta) ? event.wheelDelta / 120
						: -(event.detail || 0) / 3;
			}
			/*菜品分类*/
			var count = $("#preferential-type-first").children("li").length;
			if (event.delta > 0) {
				if (count - up_num > 10) {
					$("#preferential-type-first").find("li").eq(up_num).css("margin-left", "-10%");
					up_num++;
				}
			} else {
				if (up_num >= 1) {
					$("#preferential-type-first").find("li").eq(up_num - 1).css("margin-left", "0");
					up_num--;
				}
			}
			if (document.all) {
				event.cancelBubble = false;
				return false;
			} else {
				event.preventDefault();
			}
		}
	</script>
</body>
</html>
