<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/resource.jsp"%>
<link rel="stylesheet" href="${path}/tools/font-awesome/css/font-awesome.css" />
<link rel="stylesheet" href="${path}/tools/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${path}/css/common.css" />
<link rel="stylesheet" href="${path}/css/index.css" />
<link rel="stylesheet" href="${path}/tools/font-awesome/css/font-awesome.css" />
<link rel="stylesheet" href="${path}/css/report.css" />
<script type="text/javascript" src="${path}/scripts/ajaxfileupload.js"></script>
<script type="text/javascript" src="${path}/scripts/json2.js"></script>
<script type="text/javascript" src="${path}/tools/calendar_diy/WdatePicker.js"></script>
</head>
<body>
	<div class="ky-container">
		<div class="ky-content  content-iframe">
			<div class="report-title">
				<span>会员储值统计表</span>
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
							onclick="changeDataType(1,this)">上月</button>
						<button type="button" class="btn btn-default"
							onclick="changeDataType(2,this)">本月</button>
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
					<button type="button" id="submit" onclick="getMemberValueReportData();"
						name="submit" class="btn btn-default">确定</button>
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
			<div class="form-group">
				<div class="col-xs-4 long-search">
						<input class="form-control" id="cardno" placeholder="会员卡号/手机号" />
				</div>
				<%-- <label class="col-xs-1 control-label">支付方式:</label>
				<div class="col-xs-2" style="width: 150px;">
					<div class="input-group select-box">
						<select style="width: 150px; height: 30px;" class="form-control" id="payType">
							<option value="0">全部</option>
							<option value="1">现金</option>
							<option value="2">银行卡</option>
							<option value="3">在线支付</option>
						</select>
					</div>
				</div> --%>
			</div>
		</div>
		<div class="report-tb-div bottom-div">
			<table class="ky-table table table-striped table-bordered table-hover datatable table-list click-table" id="waiter-assess-tb">
				<thead>
					<tr>
						<th>操作时间</th>
						<th>储值金额</th>
						<th>工本费</th>
						<th>合计</th>
						<th>会员储值赠送</th>
						<th>操作笔数</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
	</div>
	<div class="modal fade report-details-dialog in " id="MemberValue2-assess-dialog" data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<div class="modal-title">
						<span>会员储值表详情</span>
						<a href="Javascript:exportWaiterAssess(1);"><img src="../images/download.png" alt="" style="float: right;" /></a>
						<img src="../images/close.png" class="img-close" data-dismiss="modal" />
					</div>
				</div>
				<div class="modal-body">
					<div>
						<input type="hidden" id="p_userid" value="" />
						<table class="ky-table table table-list report_sub_tb" id="waiterassess-details-tb2">
							<thead>
								<tr>
									<th nowrap="nowrap">操作时间</th>
									<th nowrap="nowrap">储值金额</th>
									<th nowrap="nowrap">工本费</th>
									<th nowrap="nowrap">合计</th>
									<th nowrap="nowrap">会员储值赠送</th>
									<th nowrap="nowrap">操作笔数</th>
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
	
	<div class="modal fade report-details-dialog in " id="MemberValue3-assess-dialog" data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content"  style="margin-left:-100px;width:800px;">
				<div class="modal-header">
					<div class="modal-title">
						<span>会员储值表详情</span>
						<a href="Javascript:exportWaiterAssess(1);"><img src="../images/download.png" alt="" style="float: right;" /></a>
						<img src="../images/close.png" class="img-close" data-dismiss="modal" />
					</div>
				</div>
				<div class="modal-body">
					<div>
						<table class="ky-table table table-list report_sub_tb" id="waiterassess-details-tb3">
							<thead>
								<tr>
									<th nowrap="nowrap">操作时间</th>
									<th nowrap="nowrap">会员号</th>
									<th nowrap="nowrap">手机号</th>
									<th nowrap="nowrap">姓名</th>
									<th nowrap="nowrap">会员等级</th>
									<th nowrap="nowrap">会员类型</th>
									<!-- <th nowrap="nowrap">支付方式</th> -->
									<th nowrap="nowrap">储值金额</th>
									<th nowrap="nowrap">储值后余额</th>
									<th nowrap="nowrap">工本费</th>
									<th nowrap="nowrap">操作人</th>
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
	<script src="${path}/tools/bootstrap/datatables/jquery.js"></script>
	<script src="${path}/tools/bootstrap/datatables/bootstrap.min.js"></script>
	<script src="${path}/tools/bootstrap/datatables/jquery.dataTables.js"></script>
	<script src="${path}/tools/bootstrap/datatables/dataTables.bootstrap.js"></script>
	<script src="${path}/scripts/projectJs/memberValue-report.js"></script>
	<script type="text/javascript">
		var shiftid;
		var searchType;
		var oTable=null;
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
			getMemberValueReportData();
		});
	</script>
</body>
</html>