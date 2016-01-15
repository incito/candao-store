<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>品项销售统计</title>
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
<!-- Fixed navbar -->
<style type="text/css">
	.main1{
		width: 50%; 
		float: left; 
		margin-right: 0; 
		padding-right: 0; 
		border-right-width: 0;
	}
	.main2{
		width: 50%; 
		float: left; 
		margin-left: 0; 
		padding-left: 0;
		border-left-width: 0;
	}
	.main3{
		width: 100%; 
		float: left; 
		margin-right: 0; 
		padding-right: 0; 
		border-right-width: 0;
		padding-bottom: 100px;
	}
</style>
</head>
<body>
	<div class="report_head">
		<button class="btn btn-default" id="to_detail">查看明细</button>
	</div>
	<div class="bottom-div">
		<div class="row-fluid example">
			<!--/span-->
			<div id="sale_count_graphic" class="col-md-14">
				<div class="item-type">
					<div class="nav-types-prev " id="nav-types-prev1" style="visibility: hidden;">
						<i class="icon-chevron-left"></i>
					</div>
					<ul class="tab-ul nav-dish-type" id="dish-type-first">
					</ul>
					<div class="nav-types-next " id="nav-types-next1" style="visibility: hidden;">
						<i class="icon-chevron-right"></i>
					</div>
				</div>
				<div id="sale_count_top_main" class="main1 main" style=''>
				</div>
				<div id="sale_count_main" class="main2 main" style=''>
				</div>
			</div>
			<div id="amount_graphic" class="col-md-14">
				<div class="item-type">
					<div class="nav-types-prev " id="nav-types-prev2" style="visibility: hidden;">
						<i class="icon-chevron-left"></i>
					</div>
					<ul class="tab-ul nav-dish-type" id="dish-type-sec">
					</ul>
					<div class="nav-types-next " id="nav-types-next2" style="visibility: hidden;">
						<i class="icon-chevron-right"></i>
					</div>
				</div>
				<div id="saleamount_top_main" class="main1 main" style=''>
				</div>
				<div id="saleamount_trend_main" class="main2 main" style=''>
				</div>
			</div>
			<div id="thousand_graphic" class="col-md-14">
				<div class="item-type">
					<div class="nav-types-prev " id="nav-types-prev3" style="visibility: hidden;">
						<i class="icon-chevron-left"></i>
					</div>
					<ul class="tab-ul nav-dish-type" id="dish-type-third">
					</ul>
					<div class="nav-types-next " id="nav-types-next3" style="visibility: hidden;">
						<i class="icon-chevron-right"></i>
					</div>
				</div>
				<div id="thousand_times_main" class="main3 main" style=''>
				</div>
			</div>
			<!--/span-->
		</div>
		<!--/row-->
	</div>
	<script src="<%=request.getContextPath()%>/scripts/jquery.min.js"></script>
	<script
		src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
	<script
		src="<%=request.getContextPath()%>/scripts/projectJs/reportChart.js"></script>
	<script
		src="<%=request.getContextPath()%>/scripts/projectJs/report.js"></script>
	<script type="text/javascript">
		var up_num1 = 0;
		var up_num2 = 0;
		var up_num3 = 0;
		$(function() {
			getItemsType();
			$("#to_detail").click(function(){
				orgTime();
				toDetail(1,beginTime ,endTime);
			});
			scrollClick();
		});
	</script>
</body>
</html>
