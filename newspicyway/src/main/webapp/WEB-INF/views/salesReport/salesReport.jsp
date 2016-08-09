<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>Examples</title>
	<meta name="description" content="">
	<meta name="keywords" content="">
	<link href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/table.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/font-awesome/css/font-awesome.css">

	<%@ include file="/common/resource.jsp" %>

	<script src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
	<script src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/index.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/jquery.validate.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/messages_zh.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/ajaxfileupload.js"></script>

 	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/json2.js"></script>
 	<script type="text/javascript" src="<%=request.getContextPath()%>/tools/calendar_diy/WdatePicker.js"></script>
</head>
<body>

	<div class="ky-content content-iframe">
	
			<div class="report-btn btn-add">
				<div class="btn-group" role="group" aria-label="...">
				  <font style="font-size: 25px; color: #000; font-weight: 700;">报表分析</font><font style="font-size: 18px;" >  营业报表</font >

				</div>
			</div>
		

			<div class="report-search">
				<div class="form-group">

					<div>
						<button id="0" class="dishTypeUnSelected btn  btn-first today"   onclick="gethiddenId(0)" style="width:100px;margin-left;" >今日</button>
						<button id="1" class="dishTypeUnSelected btn btn-first thisWeek"   onclick="gethiddenId(1)" style="width:100px;">本周</button>
						<button id="2" class="dishTypeUnSelected btn btn-first thisMonth"   onclick="gethiddenId(2)" style="width:100px;">本月</button>
						<button id="3" class="dishTypeUnSelected btn btn-first timeSlot"   onclick="gethiddenId(3)" style="width:100px;">时间段</button>
						<font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>
					
					
					
						<button id="5" class="dishTypeUnSelected btn  btn-first allDay"   onclick="getHour(4)" style="width:100px;position:absolute; right: 600px;" >全天</button>
						<button id="6" class="dishTypeUnSelected btn btn-first afternoon"   onclick="getHour(5)" style="width:100px;position:absolute; right: 490px;  ">午市</button>
						<button id="7" class="dishTypeUnSelected btn btn-first dinner"   onclick="getHour(6)" style="width:100px;position:absolute; right: 380px;">晚市</button>
						
			
					
						<button class="btn btn-default" type="submit" onclick="gethiddenId(4)" style="width:100px;position:absolute; right: 250px;"><i class="icon-search"></i>    搜索</button>						
						<form id="query" action="" method="post" >
						</form>
					</div>
						<br/>
						<div class="col-xs-5" id="wdate" style="border-style:none;background-color:white;" align="right" >
							<input class="Wdate"  onFocus="WdatePicker({startDate:'+%Y-%m-%d %H:%M',dateFmt:'yyyy-MM-dd HH:mm'})"
								id="begintime" name="begintime"  />
							&nbsp;&nbsp;至: 
							<input class="Wdate" onFocus="WdatePicker({startDate:'+%Y-%m-%d %H:%M',dateFmt:'yyyy-MM-dd HH:mm'})"
								 id="endtime" name="endtime" />
						</div>
						<br/>
<!-- 					<label class="control-label col-xs-2">门店名称：</label> -->
<!-- 					<div class="col-xs-3"> -->
<%-- 						<input type="text"  class="form-control"  name="branchname" value="${branchname}"/> --%>
						
<!-- 					</div> -->
<!-- 					<label class="control-label col-xs-1">地址：</label> -->
<!-- 					<div class="col-xs-3"> -->
						 
<%-- 							  <input type="text" id="name" name="address"  style="cursor:default;"  readonly value="${address}" class="form-control cityOne" flag="all" placeholder="省 市 县" > --%>

								
<!-- 					</div>		 -->
					
				</div>
				
			</div>
			


			<table class="table table-list">
				
				<tbody>
				<c:forEach var="item" items="${datas}" varStatus="i">
				
					<tr id="baseComB${i.count}" style="display:none;background-color:grey;"><td id="baseComB${i.count}">${item.baseCom }</td>
						<td ></td>
					</tr>
					<tr>
								   
						<td>${item.selfcom}</td>
						<td>${item.objvalue}</td>
						
						
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<%@ include file="/common/page.jsp" %>
		</div>
</body>
<script type="text/javascript">
var begintime="";
var endtime="";
var hour=0;
var i=0;
var j=5;
$(function(){
	 $("#baseComB1").show();
	 $("#baseComB5").show();
	 $("#baseComB11").show();
	 $("#baseComB19").show();
	 $("#baseComB22").show();
	 $("#baseComB29").show();
	 $("#wdate").hide();
	 $("#button1").hide();
// 	getSelected(i,j);
});
//-----------------------------------------------------------------------------------------
 function getSelected(i,j){
		 $("#"+i).attr("class","dishTypeSelected btn  ");
		 $("#"+j).attr("class","dishTypeSelected btn  ");
	 }


function gethiddenId(date){
	getSelected(date,j);
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
// 			$("#dishTypeTr td").attr("class","dishTypeUnSelected");
// 			$("#"+date).attr("class","dishTypeSelected");
			begintime = Index;
			endtime = End;
			findSalesReport();
// 			alert(Index+"---"+End);
		}
		function getHour(h){
			
			if(h==5){
				hour=0;
			}else if(h==6){
				hour=1;
			}else if(h==7){
				hour=2;
			}
			findSalesReport();
		}
		function findSalesReport(){
			$("#query").attr("action",global_Path+"/salesReport/getDayReportList/?begintime="+begintime+"&endtime="+endtime+"&hour="+hour);
		
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
</html>