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
<!--link
	href="<%=request.getContextPath()%>/tools/echarts/css/bootstrap.css"
	rel="stylesheet"-->
<link
	href="<%=request.getContextPath()%>/tools/echarts/css/carousel.css"
	rel="stylesheet">
<link
	href="<%=request.getContextPath()%>/tools/echarts/css/echartsHome.css"
	rel="stylesheet">
<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
<script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->
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
</head>
<body>
	<div class="report_head">
		<button class="btn btn-default" id="to_detail">查看明细</button>
	</div>
	<div class="bottom-div">
		<div class="row-fluid example">
			<!--/span-->
			<div id="sale_count_graphic" class="col-md-14">
				<div id="sale_count_top_main" class="main"
					style='width: 50%; float: left; margin-right: 0; padding-right: 0; border-right-width: 0'>
				</div>
				<div id="sale_count_main" class="main"
					style='width: 50%; margin-left: 0; padding-left: 0; border-left-width: 0'>
				</div>
			</div>
			<div id="amount_graphic" class="col-md-14">
				<div id="saleamount_top_main" class="main"
					style='width: 50%; float: left; margin-right: 0; padding-right: 0; border-right-width: 0'>
				</div>
				<div id="saleamount_trend_main" class="main"
					style='width: 50%; margin-left: 0; padding-left: 0; border-left-width: 0'>
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
		$(function() {
			initItemData();
			$("#to_detail").click(function(){
				orgTime();
				toDetail(1,beginTime ,endTime);
			});
		});
	</script>
</body>
</html>
