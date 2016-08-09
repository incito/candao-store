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
<link href="<%=request.getContextPath()%>/css/prefreport.css"
	rel="stylesheet">
<style type="text/css">
	.report_data_div table {
		margin-top: 0;
	}
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row-fluid example">
			<!--/span-->
			<div id="payway_num_graphic" class="col-md-14 graphic">
				<div class="p-main lf">
					<div id="payway_num_main" class="main"
						style='width: 100%; float: left; margin-right: 0; padding-right: 0; border-right-width: 0'></div>
				</div>
				<div class="p-main rt">
					<div id="payway_money_main" class="main"
						style='width: 100%; margin-left: 0; padding-left: 0; border-left-width: 0'></div>
				</div>
			</div>
			<!--/span-->
		</div>
		<!--/row-->
		<div class="bottom-div">
		<div class="report_data_div" >
			<table class="ky-table table table-list click-table" id="payway_data">
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
	</div>
	<div class="modal fade report-details-dialog in " id="payway-details-dialog" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<div class="modal-title">
					<span>结算方式详情</span>
					<a href="Javascript:exportxlsC(1)"><img src="../images/download.png" alt="" style="float: right;" /></a>
					<img src="../images/close.png" class="img-close" data-dismiss="modal" />
				</div>
			</div>
			<div class="modal-body">
				<div>
					<input type="hidden" id="p-payway" value="" />
					<input type="hidden" id="membercardno" value="" />
					<input type="hidden" id="itemid" value="" />
					<table class="ky-table table table-list report_sub_tb" style="margin-bottom: 0px;">
						<thead>
							<tr><th width="33%">结算方式</th><th width="33%">笔数</th><th>金额</th></tr>
						</thead>
						<tbody>
							<tr><td id="payway-name"></td><td id="payway-nums"></td><td id="payway-amount"></td></tr>
						</tbody>
					</table>
					<table class="ky-table table table-list report_sub_tb click-table" id="payway_sub_tb" style="margin-top: 0px;">
						<thead>
							<tr>
								<th width="33%">时间</th>
								<th width="33%">订单号</th>
								<th>金额</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div class="btn-operate btn-operate-dishes">
					<button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="reckoning.jsp" />
	<script src="<%=request.getContextPath()%>/scripts/jquery.min.js"></script>
	<script
		src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
	<script
		src="<%=request.getContextPath()%>/scripts/projectJs/reportChart.js"></script>
	<script
		src="<%=request.getContextPath()%>/scripts/projectJs/report.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/zxx.color_exchange.js"></script>
	<script type="text/javascript">
		$(function() {
			var curTheme = "macarons";

			require.config({
				paths : {
					echarts : global_Path + '/tools/echarts/js'
				}
			});
			plaunchExample();

			function plaunchExample() {
				// 按需加载
				require([ 'echarts',
				'echarts/chart/pie'
				], function(ec) {
					echarts = ec;
				});
			}
			initPaywayData();
		});
	</script>
</body>
</html>
