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
				<span>服务员考核统计表</span>
				<a href="Javascript:exportWaiterAssess(0)"><img
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
				<div class="col-xs-1 report-confirm-btn" id="div1">
					<button type="button" id="submit" onclick="getWaiterAssessData();"
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
		<div class="report-tb-div bottom-div" style="overflow-x: auto;">
			<table class="ky-table table table-striped table-bordered table-hover datatable table-list click-table" id="waiter-assess-tb">
				<thead>
					<tr>
						<th>服务员编号</th>
						<th>服务员姓名</th>
						<th>开台数</th>
						<th>结算人数</th>
						<th>应收总额</th>
						<th>实收总额</th>
						<th>应收人均</th>
						<th>实收人均</th>

						<!-- <th>实收/现金</th>
						<th>实收/银行卡</th>
						<th>实收/抹零</th>
						<th>实收/会员卡消费</th>
						<th>实收/会员积分消费</th>
						<th>实收/挂帐</th>
						<th>实收/微信支付</th>
						<th>实收/支付宝支付</th> -->
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
	</div>
	<div class="modal fade report-details-dialog in " id="waiter-assess-dialog" data-backdrop="static">
		<div class="modal-dialog" style="width:1000px;">
			<div class="modal-content">
				<div class="modal-header">
					<div class="modal-title">
						<span>服务员考核明细</span>
						<a href="Javascript:exportWaiterAssess(1);"><img src="../images/download.png" alt="" style="float: right;" /></a>
						<img src="../images/close.png" class="img-close" data-dismiss="modal" />
					</div>
				</div>
				<div class="modal-body">
					<div style="width: 950px;max-height: 500px;overflow-x: scroll;">
						<input type="hidden" id="p_userid" value="" />
						<table class="ky-table table table-list report_sub_tb" id="waiterassess-details-tb">
							<thead>
								<tr>
									<th nowrap="nowrap">订单号</th>
									<th nowrap="nowrap">台号</th>
									<th nowrap="nowrap">就餐人数</th>
									<th nowrap="nowrap">应收</th>
									<th nowrap="nowrap">实收</th>

									<%-- <th nowrap="nowrap">实收/现金</th>
									<th nowrap="nowrap">实收/银行卡</th>
									<th nowrap="nowrap">实收/抹零</th>
									<th nowrap="nowrap">实收/会员卡消费</th>
									<th nowrap="nowrap">实收/会员积分消费</th>
									<th nowrap="nowrap">实收/挂帐</th>
									<th nowrap="nowrap">实收/微信支付</th>
									<th nowrap="nowrap">实收/支付宝支付</th> --%>
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
	<script src="<%=request.getContextPath()%>/tools/bootstrap/datatables/jquery.js"></script>
	<script src="<%=request.getContextPath()%>/tools/bootstrap/datatables/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/tools/bootstrap/datatables/jquery.dataTables.js"></script>
	<script src="<%=request.getContextPath()%>/tools/bootstrap/datatables/dataTables.bootstrap.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/report.js"></script>
	<script type="text/javascript">
		var shiftid;
		var searchType;
		var oTable=null;
		var beginTime;
		var endTime;
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
			setshiftid(-1);
			changeDataType(0);
			getWaiterAssessData();
		});
	</script>
</body>
</html>