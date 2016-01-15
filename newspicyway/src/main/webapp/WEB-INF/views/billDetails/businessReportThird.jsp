<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/resource.jsp"%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/scripts/ajaxfileupload.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/scripts/datagrid-groupview.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/scripts/json2.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/tools/calendar_diy/WdatePicker.js"></script>
<script src="<%=request.getContextPath()%>/scripts/global.js"></script>
<script src="<%=request.getContextPath()%>/scripts/projectJs/report.js"></script>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/tools/font-awesome/css/font-awesome.css" />
<link
	href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css"
	rel="stylesheet" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/common.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/index.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/preferential.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/tools/font-awesome/css/font-awesome.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/report.css" />
<style>
.trname:hover {
	background: #E6E6E6
}
.bottom-div {
	padding-bottom: 100px;
	float: left;
	width: 100%;
	margin-right: 20px;
}
.report-tb-div{
	width: 99%;
	margin-right: 20px;
	overflow: auto;
}
.ky-table {
	width: 1500px;
}
table {
  max-width: 200%;
}
</style>
</head>
<body>
	<div class="ky-container">
		<div class="ky-content  content-iframe">
			<div class="report-title">
				<button type="button" onclick="javascript:history.go(-1);" class="btn btn-default">返回</button>
				<a href="Javascript:exportBusinessReportOrders()"><img
					src="../images/download.png" alt="" /></a>
			</div>
		</div>
		<hr />
	</div>
	<div class="bottom-div">
		<div class="report-tb-div">
			<table class="ky-table table table-list click-table" id="business_third_report_data">
				<thead>
					<tr>
						<th>订单号</th>
						<th>开台时间</th>
						<th>结账时间</th>
						<th>应收</th>
						<th>实收</th>
						<th>折扣</th>
						<th>现金</th>
						<th>刷工行卡</th>
						<th>刷他行卡</th>
						<th>微信</th>
						<th>支付宝</th>
						<th>挂账</th>
						<th>会员消费净值</th>
						<th>会员消费虚增</th>
						<th>会员积分消费</th>
						<th>会员优惠券</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
	</div>
	<jsp:include page="reckoning.jsp" />
	<script src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		var branchId;
		var shiftId;
		var currDate;
		$(function() {
			branchId = localStorage.getItem("branchId");
			shiftId = localStorage.getItem("shiftId");
			currDate = localStorage.getItem("currDate");
			initBusinessReportThird();
		});
	</script>
</body>
</html>