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
</style>
</head>
<body>
	<div class="ky-container">
		<div class="ky-content  content-iframe">
			<div class="report-title">
				<span>营业报表</span>
				<a href="Javascript:exportBusinessReportData(0)"><img
					src="../images/download.png" alt="" /></a>
			</div>
		</div>
		<hr />
		<div class="report-search-box">
			<div class="form-group">
				<div class="col-xs-1" style="padding: 5px 0px 0px 20px;">
					<div class="btn-group ">
						时间选择
					</div>
				</div>
				<div id="wdate" style="display: block;">
					<div class="col-xs-2">
						<div class="input-group">
							<input type="text" class="form-control"
								aria-describedby="basic-addon1"
								onFocus="WdatePicker({startDate:'+%Y-%m-%d',dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endTime\')}',minDate:'#F{$dp.$D(\'endTime\',{y:-1});}'})"
								id="beginTime" name="beginTime" readOnly="true" /> <span
								class="input-group-addon arrow-down" id="basic-addon1"><i
								class="icon-chevron-down" style="color: #000000"></i></span>
						</div>
					</div>
					<div class="col-xs-1 report-search-span">至</div>
					<div class="col-xs-2">
						<div class="input-group">
							<input type="text" class="form-control"
								aria-describedby="basic-addon1"
								onFocus="WdatePicker({startDate:'+%Y-%m-%d',dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'beginTime\')}',maxDate:'#F{$dp.$D(\'beginTime\',{y:1, s:-1});}'})"
								id="endTime" name="endTime" readOnly="true" /> <span
								class="input-group-addon arrow-down" id="basic-addon1"><i
								class="icon-chevron-down" style="color: #000000"></i></span>
						</div>
					</div>
				</div>
				<div class="col-xs-1 report-confirm-btn" id="div1">
					<button type="button" id="submit" onclick="doSearchBtn();"
						name="submit" class="btn btn-default">确认</button>
				</div>
				<div class="col-xs-2 short-search">
					<div class="btn-group ">
						<button type="button" class="btn btn-default active" onclick="setshiftid(-1,this)">全天</button>
						<button type="button" class="btn btn-default"
							onclick="setshiftid(0,this)">午市</button>
						<button type="button" class="btn btn-default"
							onclick="setshiftid(1,this)">晚市</button>
						<input type="hidden" id="shiftid" name="shiftid" />
					</div>
				</div>
			</div>
		</div>
		<div class="bottom-div">
		<div class="report-tb-div">
			<table class="ky-table table table-list click-table" id="business_report_data">
				<thead>
					<tr>
						<th>门店名称</th>
						<th>门店编号</th>
						<th>JDE编号</th>
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
	</div>
	<script type="text/javascript">
		var branchId;
		var selBranchId;
		
		var beginTime;
		var endTime;
		var shiftId;
		$(function() {
			$(".short-search button").click(function() {
				$(this).parent().find("button").removeClass("active");
				$(this).addClass("active");
			});
			$(".select-box").click(function() {
				$(this).next().toggleClass("hidden");
			});
			$(".select-content-detail").click(function() {
				$(this).parent().prev().find("input").val($(this).text());
				$(this).parent().toggleClass("hidden");
			});
			var shiftId = localStorage.getItem("shiftId");
			console.log("shiftId="+shiftId);
			if(shiftId == null || shiftId == ""){
				$(".short-search").find("button").removeClass("active");
				$(".short-search").find("button").eq(0).addClass("active");
				$("#shiftid").val(-1);
			}else if(shiftId == 0){
				$(".short-search").find("button").removeClass("active");
				$(".short-search").find("button").eq(1).addClass("active");
				$("#shiftid").val(0);
			}else if(shiftId == 1){
				$(".short-search").find("button").removeClass("active");
				$(".short-search").find("button").eq(2).addClass("active");
				$("#shiftid").val(1);
			}
			var beginTime = localStorage.getItem("beginTime");
			var endTime = localStorage.getItem("endTime");
			if(beginTime != null && beginTime !=""){
				$("#beginTime").val(beginTime);
			}else{
				$("#beginTime").val(getNowDate(1).split(" ")[0]);
			}
			if(endTime != null && endTime !=""){
				$("#endTime").val(endTime);
			}else{
				$("#endTime").val(getNowDate1(1).split(" ")[0]);
			}
			$("#wdate").show();
			selBranchId = localStorage.getItem("branchId");
			initBusinessReport();
		});
	</script>
</body>
</html>