<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  	<%@ include file="/common/resource.jsp" %> 
 <style type="text/css">
#tableShow td{
	width:200px;
	background-color:#CCCCCC;
	height:40px;
	border-width:1px;
	border-style:solid;
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
</style>
</head>
<body>
<div style="margin: 10px 0px 0px 10px;">
<table id="tableShow">
</table>
</div>
	<div id="addTableDialog">
		<input type="hidden" id="areaid" name="areaid" />
		<table id="table2" cellspacing="0" cellpadding="0" border="0" style="width: 100%">
			<tr align="left"  >
				<td>区域序号:</td>
				<td><input type="text" id="areaNo" name="areaNo"  /><span
					style="color: red">*</span></td>
			</tr>
			<tr align="left">
				<td>区域名称:</td>
				<td><input type="text" id="areaname" name="areaname" /></td>
			</tr>
			<tr align="left">
				<td>客用单打印机:</td>
				<td><input type="text" id="custPrinter" name="custPrinter" /></td>
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
			url : global_Path+"/tableArea/find.json",
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
					 $("#tableShow tr:eq("+j+") td:eq("+i%5+")").attr("id",""+(val.areaid));
					 
					 if(val.areaNo!=0){
				     $("#"+val.areaid).html("<a onclick=\"modify_table('"+val.areaid+"')\">"+val.areaname+"("+val.num+"桌)</a><button onclick=\"tableAvaliableStatus('"+val.areaid+"')\"  style=\"display:none;float: right;background-color: #CCCCCC;\">&times;</button>");
					 }else{
						 $("#"+val.areaid).html("<a onclick=\"add_table()\">"+val.areaname+"</a>");
					 }
				      
				  });
	
			}
		});
		$("#areaid000").css({backgroundColor:"white"});
		$("#tableShow td").mouseover(function(){
			 $(this).children("button").show();
			// $(this).css({backgroundColor:"#666666","color":"white"});
			 
		});
		$("#tableShow td").mouseout(function(){
			 $(this).children("button").hide();
			// $(this).css({backgroundColor:"#CCCCCC","color":"black"});
			 $("#areaid000").css({backgroundColor:"white"});
		 });
		
		$('#custPrinter').combobox({
		    url:global_Path + '/table/getPrinterTag.json',
		    valueField:'printerid',
		    textField:'printername',
		    panelHeight : 'auto',
		    loadFilter : function(row){
		    	return row;
			}
		});
		
		
 	});
 
 //----------------------------------------------------------------------------------------
 	$('#addTableDialog').dialog({   
	    title: '增加餐桌 (*号部分为必填项)',   
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
				    $('#areaNo').validatebox({required: true,missingMessage:"编号不能为空！"});
				    $('#areaname').validatebox({required:true,missingMessage:"不能为空！"});
				 	$("#areaNo").validatebox('enableValidation');
					$("#areaname").validatebox('enableValidation');
				    if ($('#areaNo').validatebox('isValid') && $('#areaname').validatebox('isValid')){
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
				init_tableArea();
				
			}
		}
		],
	onClose : function() {
		init_tableArea();
		
	}
 	
 	
 	
});	

 //-----------------------------------------------------------------------------------------------------------------
  //-----------------------------------------------------------------------------------------------------------------
	function init_tableArea(){
//      	$("#areaNo").validatebox('disableValidation');
// 		$("#areaname").validatebox('disableValidation');
		$("#areaNo").val("");
		$("#areaname").val("");
		$("#custPrinter").combobox('setValue','');
 	}
 
 	function tableAvaliableStatus(areaid){
//  		$.messager.confirm("区域桌数","该区域桌数必须为0才能删除",function(r){
// 			if (r){
				$.ajax({
					type : "post",
					async : false,
					url : global_Path+"/tableArea/tableAvaliableStatus/"+areaid+".json",
					dataType : "json",
					success : function(result) {
						if(result.areaid==null){
							del_table(areaid);
						}else{
							alert("该区域内存在正在使用的餐桌，不能删除。");
						}
						
						

					}
				});
// 			}
// 		});	
 	}
	function del_table(areaid){
	
				$.messager.confirm("餐桌管理","确认删除此数据？",function(r){
					if (r){
						$.ajax({
							type : "post",
							async : false,
							url : global_Path+"/tableArea/delete/"+areaid+".json",
							dataType : "json",
							success : function(result) {
						
								$.messager.alert('餐桌管理',result,'info',function(){
										window.location.reload();
								});
							}
						});
					}
				});	
		
	}
   function modify_table(areaid){
		
		$.ajax({
			type : "post",
			async : false,
			url : global_Path+"/tableArea/findById/"+areaid+".json",
			dataType : "json",
			success : function(result) {
				$("#areaid").val(result.areaid);					
				$("#areaNo").val(result.areaNo);				
				$("#areaname").val(result.areaname);
				$("#custPrinter").combobox('select',result.custPrinter);
				$('#addTableDialog').dialog('open');  
				
			}
		});
	}
 function getTableJson(){
		var areaid = $("#areaid").val();
		var areaNo = $("#areaNo").val();
		var areaname = $("#areaname").val();
		var custPrinter = $("#custPrinter").combobox('getValue');
		var tableObject={};
		tableObject.areaid=areaid;
		tableObject.areaNo=areaNo;
		tableObject.areaname=areaname;
		tableObject.custPrinter=custPrinter;
		return $.toJSON(tableObject);
	}
	function save_table() {
		var json = getTableJson();
			$.ajax({
				type : "post",
				async : false,
				url : global_Path + '/tableArea/save',
				contentType : 'application/json;charset=UTF-8',
				data : json,
				dataType : "json",
				success : function(result) {
					$.messager.alert('餐桌', result.maessge, 'info', function(){
						window.location.reload();
					});
					$('#addTableDialog').dialog('close');
					init_tableArea();
					
				}
			});
		}
	function add_table() {
		$('#addTableDialog').dialog('open');
		init_tableArea();
	}
</script>
</body>
</html>