<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/resource.jsp"%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/scripts/datagrid-groupview.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/scripts/json2.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/tools/calendar_diy/WdatePicker.js"></script>
	
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/index.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/preferential.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/tools/font-awesome/css/font-awesome.css" />
<link rel="stylesheet" 
	href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css"/>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/common.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/report.css" />
<script src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
<script src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/scripts/projectJs/report.js"></script>
<script src="<%=request.getContextPath()%>/scripts/global.js"></script>
<script src="<%=request.getContextPath()%>/scripts/ajaxfileupload.js"></script>
</head>
<body>
	<div class="ky-container">
		<div class="ky-content  content-iframe">
			<div class="report-title">
				<span>营业分析</span>
			</div>

			<div class="report-search-box">
				<div class="form-group">
					<div class="col-xs-4 long-search auto-width">
						显示类型：
						<div class="btn-group ">
							<button type="button" class="btn btn-default active"
								onclick="changeType(0)" value="D">日</button>
							<button type="button" class="btn btn-default"
								onclick="changeType(1)" value="M">月</button>
							<!--button type="button" class="btn btn-default"
								onclick="changeType(2)" value="Y">年</button-->
						</div>
					</div>
					<div id="wdate">
						<div class="col-xs-2">
							<div class="input-group">
								<input type="text" class="form-control"
									aria-describedby="basic-addon1"
									onFocus="WdatePicker({startDate:'+%Y-%m-%d',dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endTime\')}',minDate:'#F{$dp.$D(\'endTime\',{d:-30});}'})"
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
									onFocus="WdatePicker({startDate:'+%Y-%m-%d',dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'beginTime\')}',maxDate:'#F{$dp.$D(\'beginTime\',{d:30});}'})"
									id="endTime" name="endTime" value="" readOnly="true" /> <span
									class="input-group-addon arrow-down" id="basic-addon1"><i
									class="icon-chevron-down" style="color: #000000"></i></span>
							</div>
						</div>
					</div>
					<div class="col-xs-1 report-confirm-btn" id="div1">
						<button type="button" onclick="showData();" name="button"
							class="btn btn-default">确认</button>
					</div>
				</div>
			</div>

			<ul id="report_tab" class="nav nav-tabs">
				<li class="active"><a href="#saleRe" data-toggle="tab">营业数据统计</a></li>
				<li><a href="#itemRe" data-toggle="tab">品项销售统计</a></li>
				<li><a href="#couponsRe" data-toggle="tab">优惠活动统计</a></li>
			</ul>
			<div id="myTabContent" class="tab-content">
				<div class="tab-pane fade in active" id="saleRe"></div>
				<div class="tab-pane fade" id="itemRe"></div>
				<div class="tab-pane fade" id="couponsRe"></div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var myDate;
		var dateType = 0;
		var activiyType = "";
		var endTime = "";
		var beginTime = "";
		$(function() {
			$(".long-search button").click(function() {
				$(this).parent().find("button").removeClass("active");
				$(this).addClass("active");
			});
			$(".select-content-detail").click(function() {
				$(this).parent().prev().find("input").val($(this).text());
				$(this).parent().toggleClass("hidden");
			});
			$("#report_tab li a").on('shown.bs.tab', function(e) {
				// 获取已激活的标签页的名称
				clearDiv();
				orgTime();
				var divid = $(e.target).attr("href");
				tabActive(divid);
			});
			myDate = new Date();
			changeType(0);
			showData(true);
		});
		//确认点击
		function showData(isFirst){
			orgTime(isFirst);
			var tabId = $("#report_tab li.active").find("a").attr("href");
			tabActive(tabId);
		}
		//切换tab
		function tabActive(divid) {
			var path = "";
			if (divid == "#saleRe") {
				path = "bussinessReport";
			} else if (divid == "#itemRe") {
				path = "itemReport";
			} else if (divid == "#couponsRe") {
				path = "preferentialReport";
			}
			$(divid).load(global_Path + "/daliyReports/" + path);
		}
		//根据开始时间计算结束时间，通过结束时间计算开始时间
		function orgTime(isFirst){
			var begin = $("#beginTime").val();
			var end = $("#endTime").val();
			if(begin == null && end == null){
				alert("请至少选择一个开始时间或结束时间");
				return;
			}
			if(begin == null || begin == ""){
				var date = getNewDate(end, dateType);
				//当开始日期为空时，默认查7天、6个月的数据
				beginTime = getBeginTime_default(date);
			}else{
				beginTime = begin;
			}
			if(end == null || end == ""){
				var date = getNewDate(begin, dateType);
				//当结束日期为空时，默认查7天、6个月的数据
				endTime = getEndTime_default(date);
			}else{
				endTime = end;
			}
		}
		//切换显示类型（日、月、年）
		function changeType(type) {
			dateType = type;
			//初始化页面进来的时候，前面日期默认为空，后面结束日期默认为当前日期
			endTime = getEndTime();
			$("#beginTime").val("");
			$("#endTime").val(endTime);
			timeFormat(type);
		}
		//初始化进入页面的时候，默认查7天的数据
		function getBeginTime_default(date){
			console.log(date.getMonth());
			var begintt = "";
			if (dateType == 0) {
				//日
				date.setDate(date.getDate() - 6);//7天
//				date.setDate(date.getDate() - 30);//31天
				begintt = date.getFullYear() + "-" + formatMonDay(date.getMonth() + 1) + "-"
						+ formatMonDay(date.getDate());
			} else if (dateType == 1) {
				//月
				date.setMonth((date.getMonth()+1) - 6, 1);//6个月
//				date.setMonth(date.getMonth() + 1 - 12);//12个月
				begintt = date.getFullYear() + "-" + formatMonDay(date.getMonth()+1);
			} else {
				//年
//				date.setFullYear(date.getFullYear() + 1 - 10);
//				begintt = date.getFullYear()+1;
			}
			console.log(begintt);
			return begintt;
		}
		function getEndTime_default(date) {
			var end = "";
			if (dateType == 0) {
				date.setDate(date.getDate() + 6);
//				date.setDate(date.getDate() + 30);
				end = date.getFullYear() + "-" + formatMonDay(date.getMonth() + 1) + "-" + formatMonDay(date.getDate());
				
			} else if (dateType == 1) {
				date.setMonth(date.getMonth() + 6, 0);
//				date.setMonth(date.getMonth() + 12, 0);
				end = date.getFullYear() + "-" + formatMonDay(date.getMonth() + 1);
			} else {
//				date.setFullYear(date.getFullYear() + 10, 0, 0);
//				end = date.getFullYear()+1;
			}
			return end;
		}
		//获取开始时间(暂时没用)
		function getBeginTime(date) {
			var begintt = "";
			var date = new Date();
			if (dateType == 0) {
				date.setDate(date.getDate() -1);//当前日期的前一天
				begintt = date.getFullYear() + "-" + formatMonDay(date.getMonth() + 1) + "-"
						+ formatMonDay(date.getDate());
				
			} else if (dateType == 1) {
				begintt = date.getFullYear() + "-" + formatMonDay(date.getMonth()+1);
			} else {
				begintt = date.getFullYear();
			}
			return begintt;
		}
		//获取结束时间
		function getEndTime() {
			var end = "";
			var date = new Date();
			if (dateType == 0) {
				//当前日期-日
				date.setDate(date.getDate());
				end = date.getFullYear() + "-" + formatMonDay(date.getMonth() + 1) + "-" + formatMonDay(date.getDate());
				
			} else if (dateType == 1) {
				//当前日期-月
				end = date.getFullYear() + "-" + formatMonDay(date.getMonth() + 1);
			} else {
				//当前日期-年
				end = date.getFullYear();
			}
			return end;
		}
		//小于10的补零
		function formatMonDay(num){
			if(num<10){
				num = "0"+num;
			}
			return num;
		}
		//格式化时间
		function timeFormat() {
			if (dateType == 0) {
				$("#beginTime")
						.attr(
								"onFocus",
								"WdatePicker({startDate:'+%Y-%m-%d',dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\\'endTime\\')}',minDate:'#F{$dp.$D(\\'endTime\\',{d:-30});}'})");
				$("#endTime")
						.attr(
								"onFocus",
								"WdatePicker({startDate:'+%Y-%m-%d',dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\\'beginTime\\')}',maxDate:'#F{$dp.$D(\\'beginTime\\',{d:30});}'})");
			} else if (dateType == 1) {
				$("#beginTime")
						.attr(
								"onFocus",
								"WdatePicker({startDate:'+%Y-%m',dateFmt:'yyyy-MM',maxDate:'#F{$dp.$D(\\'endTime\\')}',minDate:'#F{$dp.$D(\\'endTime\\',{M:-11});}'})");
				$("#endTime")
						.attr(
								"onFocus",
								"WdatePicker({startDate:'+%Y-%m',dateFmt:'yyyy-MM',minDate:'#F{$dp.$D(\\'beginTime\\')}',maxDate:'#F{$dp.$D(\\'beginTime\\',{M:11});}'})");
			} else if (dateType == 2) {
				$("#beginTime")
						.attr(
								"onFocus",
								"WdatePicker({startDate:'+%Y',dateFmt:'yyyy',maxDate:'#F{$dp.$D(\\'endTime\\')}',minDate:'#F{$dp.$D(\\'endTime\\',{y:-9});}'})");
				$("#endTime")
						.attr(
								"onFocus",
								"WdatePicker({startDate:'+%Y',dateFmt:'yyyy',minDate:'#F{$dp.$D(\\'beginTime\\')}',maxDate:'#F{$dp.$D(\\'beginTime\\',{y:9});}'})");
			}
		}
		function clearDiv() {
			$("#saleRe").html("");
			$("#itemRe").html("");
			$("#couponsRe").html("");
		}
	</script>
</body>
</html>