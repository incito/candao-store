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
	<div class="ky-container schedule-container">
		<div class="ky-content  content-iframe">
			<div class="report-title">
				<span>排班参考统计表</span>
				<a href="Javascript:exportScheduleReport()"><img
					src="../images/download.png" alt="" /></a>
			</div>
		</div>
		<hr/>
		<div class="report-search-box">
			<div class="form-group">
				<div class="col-xs-4 long-search">
					<div class="btn-group ">
						<button type="button" class="btn btn-default active"
							onclick="controlCheckbox(0,this)">今日</button>
						<button type="button" class="btn btn-default"
							onclick="controlCheckbox(2,this)">本月</button>
						<button type="button" class="btn btn-default"
							onclick="controlCheckbox(1,this)">上月</button>
						<button type="button" class="btn btn-default"
							onclick="controlCheckbox(3,this)">时间段</button>
					</div>
				</div>
				<div id="wdate" style="display: none">
					<div class="col-xs-2">
						<div class="input-group">
							<input type="text" class="form-control"
								aria-describedby="basic-addon1"
								onFocus="WdatePicker({startDate:'+%Y-%m-%d %H:%M:%S',dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endTime\')}',minDate:'#F{$dp.$D(\'endTime\',{M:-1});}'})"
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
								onFocus="WdatePicker({startDate:'+%Y-%m-%d %H:%M:%S',dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'beginTime\')}',maxDate:'#F{$dp.$D(\'beginTime\',{M:1});}'})"
								id="endTime" name="endTime" readOnly="true" /> <span
								class="input-group-addon arrow-down" id="basic-addon1"><i
								class="icon-chevron-down" style="color: #000000"></i></span>
						</div>
					</div>
				</div>
				<div class="col-xs-1 report-confirm-btn" id="div1">
					<button type="button" id="submit" onclick="getScheduleReport()"
						name="submit" class="btn btn-default">确认</button>
				</div>
				<!--div class="col-xs-2 short-search">
					<div class="btn-group ">
						<button type="button" class="btn btn-default active" onclick="setshiftid(-1,this)">全天</button>
						<button type="button" class="btn btn-default"
							onclick="setshiftid(0,this)">午市</button>
						<button type="button" class="btn btn-default"
							onclick="setshiftid(1,this)">晚市</button>
						<input type="hidden" id="shiftid" name="shiftid" />
					</div>
				</div-->
			</div>
			<div class="form-group">
				<label class="col-xs-1 control-label">周时间选择:</label>
				<div class="checkbox-div">
					<div class="input-group">
						<label class="checkbox-inline">
							<input type="checkbox" value="0" name="weeky" disabled="disabled" />周一
						</label>
						<label class="checkbox-inline">
							<input type="checkbox" value="1" name="weeky"/>周二
						</label>
						<label class="checkbox-inline">
							<input type="checkbox" value="2" name="weeky"/>周三
						</label>
						<label class="checkbox-inline">
							<input type="checkbox" value="3" name="weeky"/>周四
						</label>
						<label class="checkbox-inline">
							<input type="checkbox" value="4" name="weeky"/>周五
						</label>
						<label class="checkbox-inline">
							<input type="checkbox" value="5" name="weeky"/>周六
						</label>
						<label class="checkbox-inline">
							<input type="checkbox" value="6" name="weeky"/>周日
						</label>
					</div>
				</div>
				<div class="time-interval">
					<label class="col-xs-1 control-label">时间间隔设置：</label>
					<div class="input-group">
						<select id="dateinterval">
							<option value="5">5 min</option>
							<option value="10">10min</option>
							<option value="15">15min</option>
							<option value="20">20min</option>
							<option value="25">25min</option>
							<option value="30">30min</option>
							<option value="60">1hours</option>
						</select>
					</div>
				</div>
			</div>
		</div>
		<div class="bottom-div">
			<div class="frozen-tb-div" id="frozenDiv">
				<table class="schedule-table ky-table table table-list " id="frozen-schedule-Tb">
					<thead>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			<div class="report-tb-div scroll-tb-div">
				<table class="schedule-table ky-table table table-list" id="schedule-tb">
					<thead>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div class="modal fade in " id="nodataPrompt"  data-backdrop="static">
 		<div class="modal-dialog" style="width:310px;">
 			<div class="modal-content" style="width:310px;margin-top: 60px;">
 				<div class="modal-body pop-div-content">
 				<br/> 
 					<p class=" "><label id="promptMsg">亲，没有查到数据！</label></p>
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
 	<script src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
	<script src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/report.js"></script>
	<script type="text/javascript">
		var shiftid;
		var searchType;
		var beginTime;
		var endTime;
		var week;
		var dateinterval;
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
			controlCheckbox(0);
			$("#dateinterval").val(30);
			getScheduleReport();
		});
		//选择查询类型控制下面周时间选择
		function controlCheckbox(type, o){
			changeDataType(type, o);
			if(type == 0){
				$(".checkbox-div .checkbox-inline").find("input").attr("disabled", "disabled");
				$(".checkbox-div .checkbox-inline").find("input").each(function(){
					$(this).attr("checked", false);
				});
			}else{
				$(".checkbox-div .checkbox-inline").find("input").removeAttr("disabled");
			}
		}
	</script>
</body>
</html>