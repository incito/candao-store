<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 	<%@ include file="/common/resource.jsp" %>  
 	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/ajaxfileupload.js"></script>
 	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/datagrid-groupview.js"></script>
 	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/json2.js"></script>
 	<script type="text/javascript" src="<%=request.getContextPath()%>/tools/calendar_diy/WdatePicker.js"></script>
    
    <link rel="stylesheet" href="<%=request.getContextPath()%>/tools/font-awesome/css/font-awesome.css"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/report.css"/>
 	<link href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css" rel="stylesheet"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/preferential.css"/>
	<script type="text/javascript">
		function submitUrl(){
			var beginTime = $("#beginTime").val();
			//var beginTime = "2015-04-02 00:00";
			var endTime = $("#endTime").val();
			location.href = global_Path + "/daliyReports/getDayReportListC?beginTime="+beginTime+"&endTime="+endTime+"&shiftid="+"";
		}
	</script>
</head>
<body onload="gethiddenId(3,this);submitUrl();">
               <div id="wdate" style="display:">
					   <div>
							<div>
								<input type="hidden" id="beginTime" name="beginTime"  readOnly="true"/>
							</div>
						</div>	
						<div>	
							<div>
								<input type="hidden" id="endTime" name="endtTme" readOnly="true"/>
							</div>
					 </div>
		        </div>
</body>
    