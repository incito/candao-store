<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/resource.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/ajaxfileupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/json2.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/tools/calendar_diy/WdatePicker.js"></script>
<script src="<%=request.getContextPath()%>/tools/bootstrap/datatables/jquery.js"></script>
<script src="<%=request.getContextPath()%>/tools/bootstrap/datatables/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/tools/bootstrap/datatables/jquery.dataTables.js"></script>
<script src="<%=request.getContextPath()%>/tools/bootstrap/datatables/dataTables.bootstrap.js"></script>
<script src="<%=request.getContextPath()%>/scripts/projectJs/creditReport.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/font-awesome/css/font-awesome.css" />
<link href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css" rel="stylesheet" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/font-awesome/css/font-awesome.css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/report.css" />
</head>
<body>
	<div class="ky-container">
		<div class="ky-content  content-iframe">
			<div class="report-title">
				<span>挂账明细统计表</span>
				<a href="Javascript:exportCreditReport();"><img
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
					<label class="col-xs-1 control-label" style="width:100px;">挂账单位:</label>
					<div class="col-xs-2">
						<div class="input-group select-box">
							<select style="width: 150px; height: 30px;" class="form-control"
							    id="billName">
						    </select>
						</div>
					</div>
					<label class="col-xs-1 control-label">清算标志:</label>
					<div class="col-xs-4">
						<div class="input-group select-box">
						<select style="width: 150px; height: 30px;" class="form-control"
							id="clearStatus">
							<option value="0">全部</option>
							<option value="1">已清算</option>
							<option value="2">未清算</option>
						</select>
					</div>
					</div>
					<div class="col-xs-1 report-confirm-btn" id="div1">
					<button type="button" id="submit" onclick="getCreditData()"
						name="submit" class="btn btn-default">确认</button>
					</div>
				</div>
			</div>
		</div>
		<form id="creditForm" method="post" style="display:none;">
			<input id="_beginTime" name="beginTime" type="hidden"/>
			<input id="_endTime" name="endTime" type="hidden"/>
			<input id="_billName" name="billName" type="hidden"/>
			<input id="_clearStatus" name="clearStatus" type="hidden"/>
			<input id="_searchType" name="searchType" type="hidden"/>
		</form>
		<div class="report-tb-div bottom-div">
			<table class="ky-table table table-striped table-bordered table-hover datatable table-list click-table" id="credit-tb">
				<thead>
					<tr>
						<th nowrap="nowrap">挂账单位</th>
						<th nowrap="nowrap">挂账单数（单）</th>
						<th nowrap="nowrap">挂账总额（元）</th>
						<th nowrap="nowrap">已结金额（元）</th>
						<th nowrap="nowrap">未结金额（元）</th>
						<th nowrap="nowrap">最长挂账时间（天）</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
	    </div>
		<div class="modal fade report-details-dialog in " id="credit-dialog" data-backdrop="static">
		<div class="modal-dialog" style="width:1300px;">
			<div class="modal-content">
				<div class="modal-header">
					<div class="modal-title">
						<span>挂账明细表</span><span id="p_begintime"></span>至<span id="p_endtime"></span>
						<img src="../images/close.png" class="img-close" data-dismiss="modal" />
					</div>
				</div>
				<div class="modal-body" style="width:1200px;">
					<div>
						<input type="hidden" id="p_gzdw" value="" />
						<input type="hidden" id="p_gzds" value="" />
						<input type="hidden" id="p_gzze" value="" />
						<input type="hidden" id="p_wjje" value="" />
						<div class="p-desc">
						    <span class="span-desc">挂账单位:</span>
						    <span id="gzdw"></span>&nbsp;&nbsp;&nbsp;
						    <span class="span-desc p-margin-left">挂账单数（单）:</span>
						    <span id="gzds"></span>&nbsp;&nbsp;&nbsp;
						    <span class="span-desc p-margin-left">挂账金额（元）:</span>
						    <span id="gzze"></span>&nbsp;&nbsp;&nbsp;
						<table class="ky-table table table-list report_sub_tb click-table" id="credit-details-tb">
							<thead>
								<tr>
									<th nowrap="nowrap">订单时间</th>
									<th nowrap="nowrap">订单编号</th>
									<th nowrap="nowrap">挂账金额（元）</th>
									<th nowrap="nowrap">已结金额（元）</th>
									<th nowrap="nowrap">未结金额（元）</th>
									<th nowrap="nowrap">清算标志</th>
									<th nowrap="nowrap">清算时间</th>
									<th nowrap="nowrap">备注信息</th>
									<th nowrap="nowrap">操作</th>
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
	<div class="modal fade report-details-dialog in " id="credit-history-dialog" data-backdrop="static">
		<div class="modal-dialog" style="width:1000px;">
			<div class="modal-content">
				<div class="modal-header">
					<div class="modal-title">
						<span id="p_title"></span>
						<img src="../images/close.png" class="img-close" data-dismiss="modal" onclick="javascript:closedCredit();"/>
					</div>
				</div>
				<div class="modal-body" style="width:900px;">
					<div>
					    <span id="creaditname" style="display:none;"></span>
					    <span id="orderId" style="display:none;"></span>
						<table class="ky-table table table-list report_sub_tb click-table" id="credit-details-tb-1" style="text-align:left;">
							<tbody>
							</tbody>
						</table>
						<span>结算历史</span>
						<table class="ky-table table table-list report_sub_tb click-table" id="credit-details-tb-2">
							<tbody>
							</tbody>
						</table>
						<span id="p_credit"></span>
						<table class="ky-table table table-list report_sub_tb click-table" id="credit-details-tb-3">
							<tbody>
							    <tr>
							        <td>结算时间</td>
							        <td>
							            <input type="text" class="form-control"
										aria-describedby="basic-addon1"
										onFocus="WdatePicker({startDate:'+%Y-%m-%d %H:%M:%S',dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'beginTime\')}'})"
										id="jieshushijian" name="beginTime" readOnly="true" value="new Date"/>
									</td>
							        <td>结算金额</td>
							        <td>
							            <input onkeyup='clearNoNum(this)' ondragenter="return false" onpaste="return !clipboardData.getData('text').match(/D/)" id="jiesuajine" type="text"/>
							        </td>
							        <td>优免金额</td>
							        <td>
							            <input onkeyup='clearNoNum(this)' ondragenter="return false" onpaste="return !clipboardData.getData('text').match(/D/)" id="youmianjine" type="text"/>
							        </td>
							    </tr>
							    <tr>
							        <td>备注信息</td>
							        <td colspan="5">
							            <textarea rows="8" cols="105" id="remark"></textarea>
							        </td>
							    </tr>
							</tbody>
						</table>
					</div>
					<div class="btn-operate btn-operate-dishes">
						<button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal" onclick="javascript:submitCredit();" id="_submitBtn" style="display:none;">确定</button>
						<button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal" onclick="javascript:closedCredit();">关闭</button>
					</div>
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
	<jsp:include page="reckoning.jsp" />
	<script type="text/javascript">
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
			setDzdw();
			changeDataType(0);
			getCreditData();
		});

		function clearNoNum(obj) {
			obj.value = obj.value.replace(/[^\d.]/g, "");//清除“数字”和“.”以外的字符
			obj.value = obj.value.replace(/^\./g, "");//验证第一个字符是数字而不是.
			obj.value = obj.value.replace(/\.{2,}/g, ".");//只保留第一个. 清除多余的.
			obj.value = obj.value.replace(".", "$#$").replace(/\./g,"").replace("$#$", ".");
		}
	</script>
</body>
</html>