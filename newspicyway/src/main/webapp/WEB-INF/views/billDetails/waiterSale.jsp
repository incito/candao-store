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
	href="<%=request.getContextPath()%>/tools/font-awesome/css/font-awesome.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/report.css" />
</head>
<body>
	<div class="ky-container">
		<div class="ky-content  content-iframe">
			<div class="report-title">
				<span>服务员销售统计表</span>
				<a href="Javascript:exportWaiterSale(0)"><img
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
						<button type="button" class="btn btn-default"
							onclick="changeDataType(3,this)">时间段</button>
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
			  </div>
			  <div class="form-group">
					<label class="col-xs-1 control-label" style="width:100px;">服务员姓名:</label>
					<div class="col-xs-2">
						<div class="input-group">
							<input style="width: 150px; height: 30px;" class="form-control" type="text" 
								id="waiterName">
							</input>
						</div>
					</div>
					<label class="col-xs-1 control-label">售卖菜品:</label>
					<div class="col-xs-4">
						<div class="input-group">
							<input style="width: 150px; height: 30px;" class="form-control" type="text" 
								id="dishName">
							</input>
						</div>
					</div>
					<div class="col-xs-1 report-confirm-btn" id="div1">
					<button type="button" id="submit" onclick="getWaiterSaleData();"
						name="submit" class="btn btn-default">确认</button>
					</div>
				</div>
			</div>
		</div>
		<form id="waiterSaleForm" method="post" style="display:none;">
			<input id="_beginTime" name="beginTime" type="hidden"/>
			<input id="_endTime" name="endTime" type="hidden"/>
			<input id="_waiterName" name="waiterName" type="hidden"/>
			<input id="_dishName" name="dishName" type="hidden"/>
			<input id="_searchType" name="searchType" type="hidden"/>
			<input id="_dishtype" name="dishtype" type="hidden"/>
			<input id="_dishunit" name="dishunit" type="hidden"/> 
			<input id="_userid" name="userid" type="hidden"/>
			<input id="_dishid" name="dishid" type="hidden"/>
		</form>
		<div class="report-tb-div bottom-div">
			<table class="ky-table table table-striped table-bordered table-hover datatable table-list click-table" id="waiter-sale-tb">
				<thead>
					<tr>
						<th>服务员姓名</th>
						<th>售卖菜品</th>
						<th>单位</th>
						<th>售卖数量</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
	    </div>
	<div class="modal fade report-details-dialog in " id="waiter-sale-dialog" data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<div class="modal-title">
						<span>服务员销售明细表</span>
						<a href="Javascript:exportWaiterSale(1);"><img src="../images/download.png" alt="" style="float: right;" /></a>
						<img src="../images/close.png" class="img-close" data-dismiss="modal" />
					</div>
				</div>
				<div class="modal-body">
					<div>
						<input type="hidden" id="p_userid" value="" />
						<input type="hidden" id="p_dishid" value="" />
						<input type="hidden" id="p_num" value="" />
						<input type="hidden" id="p_name" value="" />
						<input type="hidden" id="p_title" value="" />
						<input type="hidden" id="p_dishunit" value="" />
						<input type="hidden" id="p_dishtype" value="" />
						<div class="p-desc">
						    <span class="span-desc">服务员姓名:</span>
						    <span id="waiter-name"></span>&nbsp;&nbsp;&nbsp;
						    <span class="span-desc p-margin-left">售卖菜品:</span>
						    <span id="dish-name"></span>&nbsp;&nbsp;&nbsp;
						    <span class="span-desc p-margin-left">单位:</span>
						    <span id="dish-unit"></span>&nbsp;&nbsp;&nbsp;
						    <span class="span-desc p-margin-left">售卖数量:</span>
						    <span id="dish-num"></span></div>
						<table class="ky-table table table-list report_sub_tb click-table" id="waitersale-details-tb">
							<thead>
								<tr>
									<th nowrap="nowrap">时间</th>
									<th nowrap="nowrap">订单号</th>
									<th nowrap="nowrap">售卖数量</th>
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
	<div class="modal fade dialog in " id="prompt-dialog"
		data-backdrop="static">
		<div class="modal-dialog" style="margin-top: 100px; position: absolute; left: 35%;width: 250px;">
			<div class="modal-content">
				<div class="modal-body">
					<div style="text-align: center;">
						<p id="prompt-msg">处理中，请稍后...</p>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script src="<%=request.getContextPath()%>/tools/bootstrap/datatables/jquery.js"></script>
	<script src="<%=request.getContextPath()%>/tools/bootstrap/datatables/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/tools/bootstrap/datatables/jquery.dataTables.js"></script>
	<script src="<%=request.getContextPath()%>/tools/bootstrap/datatables/dataTables.bootstrap.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/report.js"></script>
	<jsp:include page="reckoning.jsp" />
	<script type="text/javascript">
		var searchType;
		var oTable=null;
		
		var beginTime;
		var endTime;
		var waiterName;
		var dishName;
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
			changeDataType(0);
			getWaiterSaleData();
		});
	</script>
</body>
</html>
