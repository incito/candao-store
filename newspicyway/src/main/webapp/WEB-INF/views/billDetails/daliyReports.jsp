<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ include file="/common/taglibs.jsp"%>
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
	href="<%=request.getContextPath()%>/css/report.css" />
<script src="<%=request.getContextPath()%>/scripts/projectJs/report.js"></script>
<style>
.trname:hover {
	background: #E6E6E6
}

.btn:hover {
	background: #ff6c77
}
</style>
</head>
<body>
	<div class="ky-container">
		<div class="ky-content  content-iframe">
			<div class="report-title">
				<span>营业数据明细表</span>
				<a href="Javascript:exportReportDaliy()" ><img
					src="../images/download.png" alt="" /></a>
			</div>
		</div>
		<hr />
		<div class="report-search-box">
			<div class="form-group">
				<div class="col-xs-4 long-search">
					<div class="btn-group ">
						<button type="button" class="btn btn-default active"
							onclick="changeDataType(0,this)">今日</button>
						<button type="button" class="btn btn-default"
							onclick="changeDataType(2,this)">本月</button>
						<button type="button" class="btn btn-default"
							onclick="changeDataType(1,this)">上月</button>
						<button id="period_time" type="button" class="btn btn-default"
							onclick="changeDataType(3,this)">时间段</button>
					</div>
				</div>
				<div id="wdate" style="display: none">
					<div class="col-xs-2">
						<div class="input-group">
							<input type="text" class="form-control"
								aria-describedby="basic-addon1"
								onFocus="WdatePicker({startDate:'+%Y-%m-%d %H:%M:%S',dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endTime\')}',minDate:'#F{$dp.$D(\'endTime\',{y:-1});}'})"
								id="beginTime" name="beginTime" value="" readOnly="true" /> <span
								class="input-group-addon arrow-down" id="basic-addon1"><i
								class="icon-chevron-down" style="color: #000000"></i></span>
						</div>
					</div>
					<div class="col-xs-1 report-search-span">至</div>
					<div class="col-xs-2">
						<div class="input-group">
							<input type="text" class="form-control"
								aria-describedby="basic-addon1"
								onFocus="WdatePicker({startDate:'+%Y-%m-%d %H:%M:%S',dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'beginTime\')}',maxDate:'#F{$dp.$D(\'beginTime\',{y:1});}'})"
								id="endTime" name="endTime" value="" readOnly="true" /> <span
								class="input-group-addon arrow-down" id="basic-addon1"><i
								class="icon-chevron-down" style="color: #000000"></i></span>
						</div>
					</div>
				</div>
				<div class="col-xs-1 report-confirm-btn" id="div1">
					<button type="button" onclick="reloadChart()" class="btn btn-default submitClass">确认</button>
				</div>
				<div class="col-xs-2 short-search" style="float: right;">
					<div class="btn-group">
						<button type="button" class="btn btn-default active" onclick="setshiftid(-1,this)">全天</button>
						<button type="button" class="btn btn-default"
							onclick="setshiftid(0,this)">午市</button>
						<button type="button" class="btn btn-default"
							onclick="setshiftid(1,this)">晚市</button>
						<input type="hidden" id="shiftid" name="shiftid" value="" />
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="daliyChart"></div>
	<script type="text/javascript">
		var shiftid;
		var searchType;
		$(function() {
			$(".long-search button").click(function() {
				$(this).parent().find("button").removeClass("active");
				$(this).addClass("active");
			});
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
			//
			var dateType = '${dateType}';
			if(dateType == null || dateType ==""){
				changeDataType(0);
			}else{
				var begin = '${beginTime}';
				var end = '${endTime}';
				if(dateType == "0"){
					begin += " 00:00:00";
					end += " 23:59:59";
				}else if(dateType == "1"){
					begin += "-01 00:00:00";
					var mon = end.split("-")[1];
					if(mon == 2){
						end += "-29 23:59:59";
					}else if(mon == 4 || mon == 6 || mon == 9 || mon == 11){
						end += "-30 23:59:59";
					}else{
						end += "-31 23:59:59";
					}
				}else if(dateType == "2"){
					begin += "-01-01 00:00:00";
					end += "-12-31 23:59:59";
				}
				$("#beginTime").val(begin);
				$("#endTime").val(end);
				$(".long-search button").removeClass("active");
				$("#period_time").addClass("active");
				$("#wdate").show();
			}
			setshiftid(-1);
			reloadChart();
		});
		function reloadChart() {
			shiftid = $("#shiftid").val();
			$("#daliyChart").load(global_Path + "/daliyReports/daliyChart");
		}
	</script>
</body>
</html>