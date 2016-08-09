<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<style type="text/css">
		.dishTypeUnSelected{
			 border:1px solid black;
		}
	
	</style>
 	<%@ include file="/common/resource.jsp" %>  
 	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/ajaxfileupload.js"></script>
 	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/datagrid-groupview.js"></script>
 	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/json2.js"></script>
 	<script type="text/javascript" src="<%=request.getContextPath()%>/tools/calendar_diy/WdatePicker.js"></script>
 	
	<script type="text/javascript">
	var Index="";
	var End="";
	
		$(function(){
		
			
			//判断页面是第一次加载
			var flag=0;
		
			//话题表格配置
			
			$('#dishShow').datagrid({    
				url : global_Path+"/salesSummary/page.json",
// 				url : global_Path+"/dish/findById/"+id+".json",
				method : 'post',
				
				fit : false,
// 				title : '所有用户',
				fitColumns : false,
// 				scrollbarSize :0,
				singleSelect : true,
				pagination : true,
				rownumbers : true,
				showFooter: true,
				pageSize : 10,
				pageList : [ 10, 20, 30 ],
				remoteSort : false,
  				idField : 'settledId',
				loadMsg : '数据装载中......',
// 				groupField:'fitemDesc',
// 				groupFormatter:function(value,rows){
// 				return value + '(' + rows.length + ')';
// 				},
				columns : [ [
						             
				{
					field : 'opendate',
					title : '日期',
					width : 100,
					align : 'left',
				}
// 				,{
// 					field : 'receivable',
// 					title : '主营业应收',
// 					width : 100,
// 					align : 'center',
// 				}
				,{
					field : 'mainIncome',
					title : '主营业实收',
					width : 100,
					align : 'center',
				},{
					field : 'cardIncome',					
					title : '刷卡',
					width : 100,
					align : 'center',
				},{
					field : 'vipIncome',					
					title : '会员卡',
					width : 100,
					align : 'center',
					
				},{
					field : 'debtIncome',					
					title : '挂账',
					width : 100,
					align : 'center',
				},
				{
					field : 'cashIncome',					
					title : '现金',
					width : 100,
					align : 'center',
				}
				,
				{
					field : 'free',					
					title : '优免',
					width : 100,
					align : 'center',
				}
				,
				{
					field : 'maling',					
					title : '抹零',
					width : 100,
					align : 'center',
				}
				
				,
				{
					field : 'vipIntegral',					
					title : '会员积分消费',
					width : 100,
					align : 'center',
				}
				,
				{
					field : 'yazuoCoupon',					
					title : '雅座优惠券',
					width : 100,
					align : 'center',
				}
				,
				{
					field : 'debtIncome2',					
					title : '挂帐2',
					width : 100,
					align : 'center',
				}
				,
				{
					field : 'vipValue',					
					title : '会员储值',
					width : 100,
					align : 'center',
				}
				,
				{
					field : 'coupon',					
					title : '优惠券',
					width : 100,
					align : 'center',
				}
				,
				{
					field : 'discount',					
					title : '折扣',
					width : 100,
					align : 'center',
				}
				,
				{
					field : 'vipGive',					
					title : '会员卡增送',
					width : 100,
					align : 'center',
				}
				
				,
				{
					field : 'debtAdjustment',					
					title : '挂账调整',
					width : 100,
					align : 'center',
				}
				,
				{
					field : 'vipSales',					
					title : '会员卡销售',
					width : 100,
					align : 'center',
				}
				
				] ],
				onBeforeLoad:function(){
					
					
				},
				
				toolbar : '#toolbar_datadictionary'
			});
			//分页参数配置
			$('#dishShow').datagrid('getPager').pagination({
				displayMsg : '当前显示从{from}到{to}共{total}记录',
				beforePageText : '第',
				afterPageText : '页 共 {pages} 页',
				onBeforeRefresh : function(pageNumber, pageSize) {
					$(this).pagination('loading');
					$(this).pagination('loaded');
				}
			});
// 		gethiddenId(0);	
		$("#wdate").hide();
		$("#button1").hide();
		
});
//------------------------------------------------------------------------------------------------------------		
//------------------------------------------------------------------------------------------------------------
   
		function gethiddenId(date){
// 			alert($("#begintime").val());
		
			if(date==4){
				--date;
				
			Index=$("#begintime").val();
			End=$("#endtime").val();
			if(Index>End){
				alert("开始日期不能大于截止日期");
			}
			
			}else{
				selectTime(date);
			}
			if(date==3){
				
				$("#wdate").show();
				$("#button1").show();
			}else{
				$("#wdate").hide();
				$("#button1").hide();
			}
			$("#begintime").val(Index);
			$("#endtime").val(End);
			$("#dishTypeTr td").attr("class","dishTypeUnSelected");
			$("#"+date).attr("class","dishTypeSelected");
			$('#dishShow').datagrid('load',{
				"begintime":Index,
				"endtime":End

			}
			
			);
// 			alert(Index+"---"+End);
		}
			function  selectTime(date){

			$.ajax({
				type : "post",
				async : false,
				url : global_Path+"/billDetails/indexAndEnd/"+date+".json",

				dataType : "json",
				success : function(result) {
					
					Index=result.begintime;
					End=result.endtime;
					if($("#begintime").val()==""){
						$("#begintime").val(Index);
						$("#endtime").val(End);
					}
// 					alert(result.index);
				}
			});

		}
		function exportReports(){
			
			var begintime=$("#begintime").val();
			var endtime=$("#endtime").val();
			
			location.href=global_Path + "/salesSummary/exportxlsD?begintime="+begintime+"&endtime="+endtime;
		}
		
		
			
	</script>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="padding: 10px 10px;">
		<table id="dishShow"></table>  
		<div id="toolbar_datadictionary" class="info">
			<table cellspacing="0" cellpadding="0" border="0" style="width: 100%;height:50px;" >
				<tr style="border-bottom: 1px solid #9A9691;">
					<td align="left" width="400" ><font style="font-size: 25px; color: #000; font-weight: 700;">报表分析</font><font style="font-size: 18px;" >  报表明细</font >
				    </td>
				    <td align="right" width="300">
				    <input type="button" onclick="exportReports(); " value="导出" class="xld_addbutton" />
					</td>
				</tr>
			
			</table>
			
			
			
			
			<table cellspacing="0" cellpadding="0"  style="width:1000px;margin: 10px 0px; border:0px solid #9A9691; " id="selectItem">
				<tr style="height:32px;" id="dishTypeTr" >
					<td id="0" class="dishTypeSelected" align="center" class="today" onclick="gethiddenId(0)" style="width:100px;">今日</td>
					<td id="1" class="dishTypeUnSelected" align="center" class="thisWeek" onclick="gethiddenId(1)" style="width:100px;">本周</td>
					<td id="2" class="dishTypeUnSelected" align="center" class="thisMonth" onclick="gethiddenId(2)" style="width:100px;">本月</td>
					<td id="3" class="dishTypeUnSelected" align="center" class="timeSlot" onclick="gethiddenId(3)" style="width:100px;">时间段</td>
					
					<td id="wdate" style="border-style:none;background-color:white;" align="right" >
					<input class="Wdate"  onFocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd'})"
						id="begintime" name="begintime"  />
					&nbsp;&nbsp;至: 
					<input class="Wdate" onFocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd'})"
						 id="endtime" name="endtime" />
					</td>
					<td  style="border-style:none;background-color:white;"><button id="button1"  onclick="gethiddenId(4)">确定</button></td>
				</tr>
				
			</table>
			
			
			
		</div>
	
	</div>
	
	
	
</body>
</html>
