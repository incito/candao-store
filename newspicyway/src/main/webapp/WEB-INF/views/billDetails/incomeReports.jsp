<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 	<%@ include file="/common/resource.jsp" %>  
 	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/ajaxfileupload.js"></script>
 	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/datagrid-groupview.js"></script>
 	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/json2.js"></script>
 	<script type="text/javascript" src="<%=request.getContextPath()%>/tools/calendar_diy/WdatePicker.js"></script>
 
	<script type="text/javascript">
		$(function(){
			var d = new Date();
			var month = d.getMonth()+1;
			if(d.getMonth()+1<10){
				month = "0"+month;
			}
			var day = d.getDate();
			if(d.getDate()<10){
				day = "0"+day;
			}
			$("#dateTime").val(d.getFullYear()+'-'+month+'-'+day);	
			$('#dayReportShow').datagrid({
				url : global_Path + "/daliyReports/getDayReportList2.json",
				singleSelect : false,
				collapsible : true,
				rownumbers : true,
				fitColumns : true,
				method : 'post',
				fit : false,
				view : groupview,
				remoteSort : false,
				loadMsg : '数据装载中......',
				groupField : 'baseCom',
//				onClickCell : onClickCell,
//				idField : 'dishid',
				groupFormatter : function(value, rows) {
					return value + '(' + rows.length + ')';
				},
				columns : [ [{
					field : 'selfcom',
					title : '名称',
					width : 20,
					align : 'left',
				},{
					field : 'objvalue',
					title : '金额',
					width : 20,
					align : 'left',
				}] ],
				onLoadSuccess : function(data) {
// 					 $('#dayReportShow').datagrid('collapseGroup');				
				},
			});					
});

		function searchDate(){
			var val="";
			var value = $("#dateTime").val();
			var str= new Array();   			  
			str=value.split("-"); 
			for(var i=0;i<str.length;i++){
				val+=str[i];
			}
			$('#dayReportShow').datagrid('load',{
				 "dateTime":val
			}); 
		}
		
		function exportReport(){
			var val="";
			var value = $("#dateTime").val();
			var str= new Array();   			  
			str=value.split("-"); 
			for(var i=0;i<str.length;i++){
				val+=str[i];
			}
			
			location.href=global_Path + "/daliyReports/exprotReport2/"+val+".json";
		}
		
		function exportReports(){
			location.href=global_Path + "/daliyReports/exportxls.json";
		}
		
	</script>
</head>
<body class="easyui-layout" data-options="fit:true">
<div data-options="region:'center',border:false" style="padding: 10px 10px;">
		
		<div id="toolbar_datadictionary" class="info">
			<table cellspacing="0" cellpadding="0" border="0" style="width: 100%">
				<tr style="border-bottom: 1px solid #9A9691;">
					<td align="left" width="300"><font
						style="font-size: 18px; color: #000; font-weight: 700;">营业日报表</font></td>
					<td align="right" width="260">
					请选择日期：<input type="text" id="dateTime" name="dateTime" class="Wdate" readonly onFocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd'})"/>
					<input type="button" onclick="searchDate(); " value="查询" class="xld_addbutton" />
					<input type="button" onclick="exportReport(); " value="导出报表" class="xld_addbutton" />
					</td>
				</tr>
			</table>
		</div>	
	    <table id="dayReportShow"></table>
	</div>
	
</body>
</html>