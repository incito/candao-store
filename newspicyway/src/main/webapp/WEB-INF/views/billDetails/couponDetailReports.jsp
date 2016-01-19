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
<!-- 
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/preferential.css" /> -->
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/tools/font-awesome/css/font-awesome.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/report.css" />
<script src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
<script
	src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/scripts/projectJs/report.js"></script>

</head>
<body>
	<div class="ky-container">
		<div class="ky-content  content-iframe">
			<div class="report-title">
				<span>优惠活动明细表</span> <a href="Javascript:exportReports(0)"><img
					src="../images/download.png" alt="" /></a>
			</div>
		</div>
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
								id="beginTime" name="beginTime" value="" readOnly="true" />
							<span class="input-group-addon arrow-down" id="basic-addon1"><i
								class="icon-chevron-down" style="color: #000000"></i></span>
						</div>
					</div>
					<div class="col-xs-1 report-search-span">至</div>
					<div class="col-xs-2">
						<div class="input-group">
							<input type="text" class="form-control"
								aria-describedby="basic-addon1"
								onFocus="WdatePicker({startDate:'+%Y-%m-%d %H:%M:%S',dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'beginTime\')}',maxDate:'#F{$dp.$D(\'beginTime\',{y:1, s:-1});}'})"
								id="endTime" name="endTime" value="" readOnly="true" />
							<span class="input-group-addon arrow-down" id="basic-addon1"><i class="icon-chevron-down"
								style="color: #000000"></i></span>
						</div>
					</div>
				</div>
				<div class="col-xs-2 short-search">
					<div class="btn-group ">
						<button type="button" class="btn btn-default active" onclick="setshiftid(-1,this)">全天</button>
						<button type="button" class="btn btn-default"
							onclick="setshiftid(0,this)">午市</button>
						<button type="button" class="btn btn-default"
							onclick="setshiftid(1,this)">晚市</button>
						<input type="hidden" id="shiftid" name="shiftid"
							value="" />
					</div>
				</div>
				<div></div>
			</div>
			<div class="form-group">
				<label class="col-xs-1 control-label">活动名称:</label>
				<div class="col-xs-2">
					<div class="input-group select-box">
						<select style="width: 150px; height: 30px;" class="form-control" id="bankcardno">
						</select>
					</div>
				</div>
				<label class="col-xs-1 control-label">结算方式:</label>
				<div class="col-xs-2">
					<div class="input-group select-box">
						<select style="width: 150px; height: 30px;" class="form-control" id="settlementWay">
						</select>
					</div>
				</div>
				<label class="col-xs-1 control-label">活动类型:</label>
				<div class="col-xs-2">
					<div class="input-group select-box">
						<select style="width: 150px; height: 30px;" class="form-control" id="type">
						</select>
					</div>
				</div>
				<div class="col-xs-2">
					<div class="col-xs-1 report-confirm-btn" id="div1">
						<button type="button" onclick="searchData();" class="btn btn-default submitClass">确认</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="report-tb-div bottom-div">
		<table class="ky-table table table-list" id="coupon_tb">
			<thead>
				<tr>
					<th>活动名称</th>
					<th>活动类型</th>
					<th>结算方式</th>
					<th>笔数</th>
					<th>发生金额</th>
					<th>拉动应收</th>
					<th>拉动实收</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
<div class="modal fade report-details-dialog in " id="coupon-details-dialog" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<div class="modal-title">
					<span>优惠活动详情</span>
					<a href="Javascript:exportReports(1)"><img src="../images/download.png" alt="" style="float: right;" /></a>
					<img src="../images/close.png" class="img-close" data-dismiss="modal" />
				</div>
			</div>
			<div class="modal-body">
				<div>
					<input type="hidden" id="p-coupon-id" value="" />
					<input type="hidden" id="p-coupon-payway" value="" />
					<input type="hidden" id="p-type-id" value="" />
					<div class="p-desc"><span class="span-desc">优惠活动名称：</span><span id="coupon-name"></span>&nbsp;&nbsp;<span class="span-desc p-margin-left">结算方式：</span><span id="payway-name"></span></div>
					<table class="ky-table table table-list report_sub_tb" id="coupon_sub_tb">
						<thead>
							<tr>
								<th>发生时间</th>
								<th>订单号</th>
								<th>结算金额</th>
								<th>笔数</th>
								<th>发生金额</th>
								<th>拉动应收</th>
								<th>拉动实收</th>
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
	<script type="text/javascript">
		var shiftid;
		var settlementWay;
		var bankcardno;
		var type;
		var searchType;
		$(document).ready(function() {
			$("img.img-close").hover(function(){
			 	$(this).attr("src", "<%=request.getContextPath()%>/images/close-active.png");	 
			},function(){
				$(this).attr("src", "<%=request.getContextPath()%>/images/close-sm.png");
			});
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
			
			setshiftid(-1);//默认全天
			initCouponData();

			//获取查询条件内容
			getActiviy();
			getPayway();
			getActiviyType();
		});
		function exportReports(f) {
			if(compareBeginEndTime()){
				var beginTime = $("#beginTime").val();
				var endTime = $("#endTime").val();
//				var shiftid = $("#shiftid").val();
//				var settlementWay = $("#settlementWay").val();
//				var bankcardno = transcoding($("#bankcardno").val());
//				var type = $("#type").val();
				
				if (beginTime == null || "" == beginTime) {
					var d = new Date();
					var month = d.getMonth() + 1;
					if (d.getMonth() + 1 < 10) {
						month = "0" + month;
					}
					var day = d.getDate();
					if (d.getDate() < 10) {
						day = "0" + day;
					}
					beginTime = d.getFullYear() + '-' + month + '-' + day + ' 00:00:00';
				}
		
				if (endTime == null || "" == endTime) {
					var d = new Date();
					var month = d.getMonth() + 1;
					if (d.getMonth() + 1 < 10) {
						month = "0" + month;
					}
					var day = d.getDate();
					if (d.getDate() < 10) {
						day = "0" + day;
					}
					var day = d.getDate();
					if (d.getDate() < 10) {
						day = "0" + day;
					}
					var hours = d.getHours();
					if (d.getHours() < 10) {
						hours = "0" + hours;
					}
					var minutes = d.getMinutes();
					if (d.getMinutes() < 10) {
						minutes = "0" + minutes;
					}
		
					var second = d.getSeconds();
					if (d.getSeconds() < 10) {
						second = "0" + second;
					}
		
					endTime = d.getFullYear() + '-' + month + '-' + day + ' ' + hours
							+ ":" + minutes + ":" + second;
				}
				
				var payway = "";
				var ptype = "";
				var pname = "";
				if(f == 1){
					//pname = transcoding($("#p-coupon-id").val());
					pname = encodeURI(encodeURI($("#p-coupon-id").val()));
					payway = $("#p-coupon-payway").val();
					ptype = $("#p-type-id").val();
				}else{
					payway = "null";
					ptype= "null";
					pname = "null";
				}
				/*
				location.href = global_Path + "/preferentialAnalysisCharts/exportReportCouDetail/"
						+ settlementWay + "/" + beginTime + "/" + endTime + "/"
						+ shiftid + "/" + bankcardno + "/" + type + "/"+codes+".json";*/
				location.href = global_Path + "/preferentialAnalysisCharts/exportReportCouDetail/"
					+ settlementWay + "/" + beginTime + "/" + endTime + "/"
					+ shiftid + "/" + bankcardno + "/" + type + "/"+payway+"/"+ptype+"/"+pname+"/"+searchType+".json";
			}
		}
	</script>
</body>
</html>