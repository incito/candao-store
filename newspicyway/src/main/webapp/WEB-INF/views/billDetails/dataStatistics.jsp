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

<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/easyui/icon.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/easyui/metro/easyui.css" />
	
<script src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
<script src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/scripts/projectJs/report.js"></script>
<script src="<%=request.getContextPath()%>/scripts/global.js"></script>
<script src="<%=request.getContextPath()%>/scripts/projectJs/dataAnalysis.js"></script>

<script src="<%=request.getContextPath()%>/scripts/jquery.easyui.min.js"></script>
</head>
<body>
	<div class="ky-container">
		<div class="ky-content  content-iframe">
			<div class="report-title">
				<span>详细数据统计表</span> <a href="Javascript:exportReports();"><img
					src="../images/download.png" alt="" /></a>
			</div>
		</div>
		<div class="report-search-box">
			<div class="form-group">
				<div class="col-xs-4 long-search">
					<div class="btn-group ">
						<button type="button" class="btn btn-default active"
							onclick="gethiddenId_nohms(0,this)">今日</button>
						<button type="button" class="btn btn-default"
							onclick="gethiddenId_nohms(2,this)">本月</button>
						<button type="button" class="btn btn-default"
							onclick="gethiddenId_nohms(1,this)">上月</button>
						<button id="period_time" type="button" class="btn btn-default"
							onclick="gethiddenId_nohms(3,this)">时间段</button>
					</div>
				</div>
				<div id="wdate" style="display: none">
					<div class="col-xs-2">
						<div class="input-group">
							<input type="text" class="form-control"
								aria-describedby="basic-addon1"
								onFocus="WdatePicker({startDate:'+%Y-%m-%d',dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endTime\')}',minDate:'#F{$dp.$D(\'endTime\',{M:-1});}'})"
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
								onFocus="WdatePicker({startDate:'+%Y-%m-%d',dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'beginTime\')}',maxDate:'#F{$dp.$D(\'beginTime\',{M:1});}'})"
								id="endTime" name="endTime" value="" readOnly="true" /> <span
								class="input-group-addon arrow-down" id="basic-addon1"><i
								class="icon-chevron-down" style="color: #000000"></i></span>
						</div>
					</div>
				</div>
				<div class="col-xs-2 short-search">
					<div class="btn-group ">
						<button type="button" class="btn btn-default active"
							onclick="setshiftid(-1,this)">全天</button>
						<button type="button" class="btn btn-default"
							onclick="setshiftid(0,this)">午市</button>
						<button type="button" class="btn btn-default"
							onclick="setshiftid(1,this)">晚市</button>
						<input type="hidden" id="shiftid" name="shiftid" value="" />
					</div>
				</div>
				<div></div>
			</div>
			<div class="form-group">
				<label class="col-xs-1 control-label">查询类型:</label>
				<div class="col-xs-2">
					<div class="input-group select-box">
						<select style="width: 150px; height: 30px;" class="form-control"
							id="dataType">
						</select>
					</div>
				</div>
				<label class="col-xs-1 control-label">区域:</label>
				<div class="col-xs-2">
					<div class="input-group select-box">
						<select style="width: 150px; height: 30px;" class="form-control" id="areaSel">
						</select>
					</div>
				</div>
				<div class="col-xs-2">
					<div class="col-xs-1 report-confirm-btn" id="div1">
						<button type="button" onclick="searchData();"
							class="btn btn-default submitClass">确认</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div>
		<div class="frozen-tb-div" id="frozenDiv" style="width: 100%;">
			<table class="ky-table table table-list " id="frozenTb">
				<thead><tr><th>区域</th><th>桌号/时间</th></tr></thead>
				<tbody></tbody>
			</table>
		</div>
		<div class="report-tb-div bottom-div scroll-tb-div">
			<table class="ky-table table table-list " style="margin-bottom: 2px;" id="dataStat_tb">
			</table>
		</div>
	</div>
	<script type="text/javascript">
		var shiftid;
		var areasel;
		var dataType;
		$(document).ready(function() {
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
			setshiftid(-1);
			gethiddenId_nohms(0);
			// 获取查询条件内容
			getDataType();
			getArea();
			// 初始化数据
			searchData();
		});
	</script>
</body>
</html>