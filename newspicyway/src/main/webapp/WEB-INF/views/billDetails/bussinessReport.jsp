<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>营业数据统计</title>
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
<script src="<%=request.getContextPath()%>/scripts/jquery.min.js"></script>
<script
	src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
<script
	src="<%=request.getContextPath()%>/scripts/projectJs/reportChart.js"></script>
</head>
<body>
	<div class="report_head">
		<button class="btn btn-default" id="to_detail">查看明细</button>
	</div>
	<div>
		<div class="row-fluid example">
			<!--/span-->
			<div id="graphic" class="col-md-14">
				<div id="main" class="main"
					style='width: 100%; margin-left: 0; padding-left: 0; border-left-width: 0'></div>
			</div>
			<!--/span-->
		</div>
		<!--/row-->
	</div>
	<div class="report_data_div bottom-div">
		<table class="table table-list" id="tb_d">
			<thead>
				<tr>
					<th>时间</th>
					<th>应收总额</th>
					<th>实收总额</th>
					<th>折扣总额</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/report.js"></script>
	<script type="text/javascript">
		$(function() {
			initBussinessData();
			$("#to_detail").click(function(){
				orgTime();
				toDetail(0);
			});
		});
	</script>
</body>
</html>
