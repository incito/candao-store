<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  	<%@ include file="/common/resource.jsp" %> 
<%--  	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css" type="text/css"/> --%>
 <style type="text/css">
 #printid000{
 	width:150px;
	height:20px;
 }
#tableShow td{
	width:200px;
	background-color:#FCE7E7;
	height:40px;
	text-align:center;
	font-weight:bold ;
	margin-top:20px;
	font-size:20px;
}
input{
	width:140px;
	height:13px;
}
#tableShow a{
text-decoration: none;
}
#printerid000{
	cursor:pointer;
	border:1px dashed #000;
}
img{
 cursor:pointer;
}
</style>
</head>
<body>
<div style="margin: 10px 0px 0px 10px;">
<table id="tableShow">
</table>
</div>
	<div id="addTableDialog">
		<input type="hidden" id="printerid" name="printerid" />
		<table id="table2" cellspacing="0" cellpadding="0" border="0"
			style="width: 100%">
			<tr align="left"  >
				<td>打印机编号:</td>
				<td><input type="text" id="printerNo" name="printerNo"  /><span
					style="color: red">*</span></td>
			</tr>
			<tr align="left">
				<td>打印机名称:</td>
				<td><input type="text" id="printername" name="printername" /><span
					style="color: red">*</span></td>
			</tr>
 							
			<tr align="left"  >
				<td>打印机类型:</td>
				<td><input class="easyui-combobox"  id="printertype" name="printertype" 
				data-options="panelHeight:'auto',valueField: 'label',textField: 'value',data: [{label: '1',value: '串口打印'},{label: '2',value: '并口打印'},{label: '3',value: '网络打印'}]" /></td>
			</tr>
<!-- 			<tr align="left"> -->
<!-- 				<td>创建人:</td> -->
<!-- 				<td><input type="text" id="createuserid" name="createuserid" /></td> -->
<!-- 			</tr> -->
<!-- 			<tr align="left"  > -->
<!-- 				<td>登录时间:</td> -->
<!-- 				<td><input type="text" id="inserttime" name="inserttime"  /></td> -->
<!-- 			</tr> -->
			<tr align="left">
				<td>ip地址:</td>
				<td><input type="text" id="ipaddress" name="ipaddress" /></td>
			</tr>
			<tr align="left"  >
				<td>端口:</td>
				<td><input type="text" id="port" name="port"  /></td>
			</tr>
		</table>
	</div>
	<script type="text/javascript">
 $(document).ready(function(){
	
	 var trHtml="<tr></tr>";
	 var tdHtml="<td></td>";
	 var j=-1;
	  $.ajax({
			type:"post",
			async:false,
			url : global_Path+"/printer/find.json",
			contentType:'application/json;charset=UTF-8',
			dataType : "json",
			success : function(result) {
				 $.each(result, function(i,val){  
					 if(i%5==0){
						 j++;
						$("#tableShow").append(trHtml);
						$("#tableShow tr:eq("+j+")").attr("id","tr"+(j+1));
						$("#tableShow tr:eq("+j+")").append(tdHtml);
					 }else{
						 $("#tableShow tr:eq("+j+")").append(tdHtml);
					 }
					 $("#tableShow tr:eq("+j+") td:eq("+i%5+")").attr("id",""+(val.printerid));
					 
					 if(val.printerNo!=0){

				     $("#"+val.printerid).html("<img  src=\""+global_Path+"/images/printer.jpg\" onclick=\"modify_table('"+val.printerid+"')\" \><button onclick=\"del_table('"+val.printerid+"')\"  style=\"display:none;float: right;background-color: #FCE7E7;\">&times;</button><p>"+val.printername+"</p>");
					 }else{
						 $("#"+val.printerid).html("<a >"+val.printername+"</a>");
					 }
				  });
			}
		});
	  $("#printerid000").css({backgroundColor:"white"});
	  $("#printerid000").click(function(){
		  add_table();
	  });
	  	$("#tableShow td").mouseover(function(){
			 $(this).children("button").show();
		});
		$("#tableShow td").mouseout(function(){
			 $(this).children("button").hide();
		$("#printerid000").css({backgroundColor:"white"});
	});
 });
 	$('#addTableDialog').dialog({   
	    title: '打印机管理 (*号部分为必填项)',   
	    width: 500,   
	    height: 300,   
	    closed: true,   
	    cache: false,  
	    inline : true,
	    modal: true,
		buttons : [ {
			text : '保存',
			handler : 
			function(){
				    $('#printerNo').validatebox({required: true,missingMessage:"编号不能为空！"});
// 				    $('#printername').validatebox({required:true,missingMessage:"不能为空！"});
				   // $('#createuserid').validatebox({required:true,missingMessage:"id不能为空！"});
// 				    $("#printerNo").validatebox('enableValidation');
// 					$("#printername").validatebox('enableValidation');
// 					$("#createuserid").validatebox('createuserid');
				    if ($('#printerNo').validatebox('isValid') &&f_check_IP()){
				    	
						save_table();
					}														
			}						
		}, {
			text : '取消',
			handler : function() {
				$('#addTableDialog').dialog('close');
			}
		}, {
			text : '重置',
			handler :function(){
				init_object();
				
			}
		}
		],
	onClose : function() {
		init_object();
		
	}
	});	

 //-----------------------------------------------------------------------------------------------------------------
  //-----------------------------------------------------------------------------------------------------------------
  function init_object(){
		$("#printerNo").val("");
		$("#printername").val("");
		$("#printertype").combobox('setValue','');
	
		
		$("#createuserid").val("");
		$("#inserttime").val("");
		$("#ipaddress").val("");
		$("#port").val("");
 }
   function del_table(printerid){
	   $.messager.confirm("打印机管理","确认删除此数据吗？",function(r){
			if (r){
				$.ajax({
					type : "post",
					async : false,
					url : global_Path+"/printer/delete/"+printerid+".json",
					dataType : "json",
					success : function(result) {
						
						$.messager.alert('打印机管理',result,'info',null);
						window.location.reload();
					}
				});
			}
		});
	   
	   
			
	}
   function modify_table(printerid){
		
		$.ajax({
			type : "post",
			async : false,
			url : global_Path+"/printer/findById/"+printerid+".json",
			dataType : "json",
			success : function(result) {
				$("#printerid").val(result.printerid);					
				$("#printerNo").val(result.printerNo);				
				$("#printername").val(result.printername);
				$("#printertype").combobox('select',result.printertype);
				//$("#printertype").val(result.printertype);
				$("#createuserid").val(result.createuserid);
				$("#inserttime").val(result.inserttime);
				$("#ipaddress").val(result.ipaddress);
				$("#port").val(result.port);				
				$('#addTableDialog').dialog('open');  
			}
		});
	}
 function getTableJson(){
		var printerid = $("#printerid").val();
		var printerNo = $("#printerNo").val();
		var printername = $("#printername").val();
		var printertype = $("#printertype").combobox('getValue');
		var createuserid = $("#createuserid").val();
		var inserttime = $("#inserttime").val();
		var ipaddress = $("#ipaddress").val();
		var port = $("#port").val();
		var tableObject={};
		tableObject.printerid=printerid;
		tableObject.printerNo=printerNo;
		tableObject.printername=printername;
		tableObject.printertype=printertype;
		tableObject.createuserid=createuserid;
		tableObject.inserttime=inserttime;
		tableObject.ipaddress=ipaddress;
		tableObject.port=port;
		return $.toJSON(tableObject);
	}
 
 function check_validate(){
		
	  	var flag=true;
		$.ajax({
			type : "post",
			async : false,
			data:{
				printerNo:$("#printerNo").val(),
				printerid:$("#printerid").val()
	    	    
			},
			url : global_Path+"/printer/validatePrinter.json",
			dataType : "json",
			success : function(result) {
				
				if(result.message=='打印机编号不能重复'){
					alert(result.message);
					flag=false;
					
				}
				}
			
		});
		return flag;
	} 
 
	function save_table() {
		var check=check_validate();
		if(check){
		var json = getTableJson();
			$.ajax({
				type : "post",
				async : false,
				url : global_Path + '/printer/save',
				contentType : 'application/json;charset=UTF-8',
				data : json,
				dataType : "json",
				success : function(result) {
					$.messager.alert('打印机', result.maessge, 'info', null);
					$('#addTableDialog').dialog('close');
					init_object();
					window.location.reload();
				}
			});
		}
		}
	function add_table() {
		init_object();
		$('#addTableDialog').dialog('open');
	}
	
	function f_check_IP()    
	{  var ip = document.getElementById('ipaddress').value;
	   var re=/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/;//正则表达式   
	   if(re.test(ip)||ip=="")   
	   {   
	       if( RegExp.$1<256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256||ip==""){
	    	   return true;
	       } 
	          
	   }   
	   alert("IP有误！");   
	   return false;    
	}
</script>

</body>
</html>