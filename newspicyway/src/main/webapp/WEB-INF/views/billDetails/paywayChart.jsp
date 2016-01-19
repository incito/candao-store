<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>结算方式</title>
<link rel="shortcut icon" href="../asset/ico/favicon.png">
<link
	href="<%=request.getContextPath()%>/tools/echarts/css/font-awesome.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/report.css" />
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
</head>
<body>
	<div class="container-fluid">
		<div class="row-fluid example">
			<!--/span-->
			<div id="payway_num_graphic" class="col-md-14">
				<div id="payway_num_main" class="main"
					style='width: 50%; float: left; margin-right: 0; padding-right: 0; border-right-width: 0'></div>
				<div id="payway_money_main" class="main"
					style='width: 50%; margin-left: 0; padding-left: 0; border-left-width: 0'></div>
			</div>
			<!--/span-->
		</div>
		<!--/row-->
		<div class="report_data_div bottom-div" >
			<table class="table table-list" id="payway_data">
				<thead>
					<tr>
						<th>结算方式</th>
						<th>笔数</th>
						<th>发生金额</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
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
			initPaywayData();
		});
	</script>
</body>
</html>
