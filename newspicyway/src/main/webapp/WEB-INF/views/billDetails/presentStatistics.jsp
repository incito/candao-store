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
<script src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
<script src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/scripts/global.js"></script>
<script src="<%=request.getContextPath()%>/scripts/projectJs/report.js"></script>
</head>
<body>
	<div class="ky-container">
		<div class="ky-content  content-iframe">
			<div class="report-title">
				<span>交接班统计表</span>
				<a href="Javascript:exportPresent()"><img
					src="../images/download.png" alt="" /></a>
			</div>
		</div>
		<hr />
		<div class="report-search-box">
			<div class="form-group">
				<div class="col-xs-4 long-search">
					<div class="btn-group ">
						<button type="button" class="btn btn-default active"
							onclick="changeDataType(0)">昨日</button>
						<button type="button" class="btn btn-default"
							onclick="changeDataType(2)">本月</button>
						<button type="button" class="btn btn-default"
							onclick="changeDataType(1)">上月</button>
						<button type="button" class="btn btn-default"
							onclick="changeDataType(3)">时间段</button>
					</div>
				</div>
				<div id="wdate" style="display: none">
					<div class="col-xs-2">
						<div class="input-group">
							<input type="text" class="form-control"
								aria-describedby="basic-addon1"
								onFocus="WdatePicker({startDate:'+%Y-%m-%d %H:%M:%S',dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endTime\')}',minDate:'#F{$dp.$D(\'endTime\',{y:-1});}'})"
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
								onFocus="WdatePicker({startDate:'+%Y-%m-%d %H:%M:%S',dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'beginTime\')}',maxDate:'#F{$dp.$D(\'beginTime\',{y:1, s:-1});}'})"
								id="endTime" name="endTime" readOnly="true" /> <span
								class="input-group-addon arrow-down" id="basic-addon1"><i
								class="icon-chevron-down" style="color: #000000"></i></span>
						</div>
					</div>
				</div>
				<!-- onclick="doSubmit()" -->
				<div class="col-xs-1 report-confirm-btn" id="div1">
					<button type="button" id="submit" onclick="initPresentData();"
						name="submit" class="btn btn-default">确认</button>
				</div>
			</div>
		</div>
		<div class="bottom-div">
		<div class="report-tb-div">
			<table class="ky-table table table-list click-table" id="present_statistics_data">
				<thead>
					<tr>
						<th>开业时间</th>
						<th>开业授权人</th>
						<th>结业时间</th>
						<th>结业授权人</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
		</div>
		<div class="modal fade report-details-dialog in " id="present-details-dialog" data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<div class="modal-title">
							<span>交接班详情</span>
							<img src="../images/close.png" class="img-close" data-dismiss="modal" />
						</div>
					</div>
					<div class="modal-body">
						<div>
							<input type="hidden" id="p-item-id" value="" />
							<input type="hidden" id="p-dish-type" value="" />
							<table class="ky-table table table-list report_sub_tb" id="present_sub_tb">
								<thead>
									<tr>
										<th>收银员</th>
										<th>开班时间</th>
										<th>结班时间</th>
										<th>结班授权人</th>
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
	</div>
	<script type="text/javascript">
		var searchType;
		$(function() {
			$(".long-search button").click(function() {
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
			changeDataType(0);
			initPresentData();
		});
	</script>
</body>
</html>