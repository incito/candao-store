<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 	<%@ include file="/common/resource.jsp" %>  
 	
	<script type="text/javascript">
		$(function(){
			//话题表格配置
			$('#tableShow').datagrid({
				url : global_Path + '/table/page.json',
				method : 'post',
				fit : false,
				fitColumns : true,
				singleSelect : true,
				pagination : true,
				rownumbers : true,
				pageSize : 10,	
				pageList : [ 10, 20, 30 ],
				remoteSort : false,
  				idField : 'tableid',
				loadMsg : '数据装载中......',
				columns : [ [ {
					field : 'tableNo',
					title : '餐台编号',
					width : 50,
					align : 'left'
				},
				{
					field : 'tabletype',
					title : '类型',
					width : 50,
					align : 'center',
					formatter:function(value, row, index){
						if(value==0){
						return "散台";	
						}else if(value==1){
							return "包间";
						}else{
							return "未知";
						}
					},
				},{
					field : 'tableName',					
					title : '名称',
					width : 50,
					align : 'center',
				},{
					field : 'personNum',					
					title : '就餐人数',
					width : 50,
					align : 'center',
					formatter:function(value, row, index){
						return value+"人";
					},
				},{
					field : 'areaname',					
					title : '所在区域',
					width : 50,
					align : 'center',
				},{
					field : 'minprice',					
					title : '最低消费',
					width : 50,
					align : 'center',
					formatter:function(value, row, index){
						
						if(value==""||value==null){
							return "-";
						}else{
							return value;
						}
					},
				},{
					field : 'fixprice',					
					title : '固定费',
					width : 50,
					align : 'center',
					formatter:function(value, row, index){

						if(value==""||value==null){
						return "-";
						}else{
							return value;
						}
					},
				},
				{
					field : 'printername',					
					title : '客用单打印机',
					width : 50,
					align : 'center',
					
				},
				{
					field : 'tableid',
					title : '操作',
					width : 40,
					align : 'center',
					formatter : function(value, row, index) {
						var modify ="<a href=\"###\" onclick=\"modify_table('" + value + "')\">查看/修改</a>";
						var del = "&nbsp;&nbsp;<a href=\"###\" onclick=\"del_table('" + value + "')\">删除</a>";
					    return modify+del;
					}
				}
				] ],
				toolbar : '#toolbar_table'
			});
			//分页参数配置
			$('#tableShow').datagrid('getPager').pagination({
				displayMsg : '当前显示从{from}到{to}共{total}记录',
				beforePageText : '第',
				afterPageText : '页 共 {pages} 页',
				onBeforeRefresh : function(pageNumber, pageSize) {
					$(this).pagination('loading');
					$(this).pagination('loaded');
				}
			});
			
			$('#addTableDialog').dialog({   
			    title: '餐桌管理 (*号部分为必填项)',   
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
						    $('#tableNo').validatebox({required: true,validType:'maxLength[1,6]',missingMessage:"餐台编号不能为空！"});
						  
						    $('#areaid').validatebox({required:true,validType:'maxLength[1,6]',missingMessage:"不能为空！"});
						    
						    $("#tableNo").validatebox('enableValidation');
							
							$("#areaid").validatebox('enableValidation');
							
						    if ($('#tableNo').validatebox('isValid')){
						    	if($("#areaid").combobox('getValue')!=""&&$("#custPrinter").combobox('getValue')!=""){
						    		save_table();
						    	}else{
						    		alert("所在区域和客用打印机不能为空");
						    	}
								
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
			$('#searchAreaid').combobox({
				
				
			    url:global_Path + '/table/getTableTag.json',
			    valueField:'areaid',
			    textField:'areaname',
			    panelHeight : 'auto',
			    loadFilter : function(row){
			    	return row;
				}
			});
			$('#areaid').combobox({
			    url:global_Path + '/table/getTableTag.json',
			    valueField:'areaid',
			    textField:'areaname',
			    panelHeight : 'auto',
			    loadFilter : function(row){
			    	return row;
				}
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
//-----------------------------------------------------------------------------------------------------------		
//-----------------------------------------------------------------------------------------------------------

function init_object(){
// 	tableid,tableNo,tabletype,tableName,personNum,areaid,minprice,fixprice
	$("#tabletype").combobox('setValue','');
	$("#tableName").val("");
	$("#personNum").combobox('setValue','');
	$("#areaid").combobox('setValue','');
	$("#custPrinter").combobox('setValue','');
	$("#fixprice").val("");
	$("#tableid").val("");
    $("#tableNo").val("");
	$("#minprice").val("");
}
	function add_table(){
		$('#addTableDialog').dialog('open');  
		init_object();
	
		
	}
	function check_validate(){
		
 	  	var flag=true;
		$.ajax({
			type : "post",
			async : false,
			data:{
				tableNo:$("#tableNo").val(),
	    	    tableid:$("#tableid").val()
			},
			url : global_Path+"/table/validateTable.json",
			dataType : "json",
			success : function(result) {
				
				if(result.message=='餐台编号不能重复'){
					alert(result.message);
					flag=false;
					
				}
				}
			
		});
		return flag;
	} 

	function save_table(){
		var check=check_validate();
		if(check){
			var json=getTableJson();
			$.ajax({
				type:"post",
				async:false,
				url : global_Path+'/table/save',
				contentType:'application/json;charset=UTF-8',
			    data:json, 
				dataType : "json",
				success : function(result) {								
					$('#tableShow').datagrid('reload');
					$('#addTableDialog').dialog('refresh'); 
					$('#addTableDialog').dialog('close');
					$.messager.alert('餐桌',result.maessge,'info',null);	
					
				}
			});
	    }
		}

		//修改查询
		function modify_table(tableid){
		
			$.ajax({
				type : "post",
				async : false,
				url : global_Path+"/table/findById/"+tableid+".json",
				dataType : "json",
				success : function(result) {
					
					$("#tableid").val(result.tableid);					
					$("#tableNo").val(result.tableNo);				
					$("#tabletype").combobox('select',result.tabletype);
					$("#tableName").val(result.tableName);
					$("#personNum").combobox('select',result.personNum);
					$("#areaid").combobox('select',result.areaid);
					$("#custPrinter").combobox('select',result.custPrinter);
					$("#minprice").val(result.minprice);
					$("#fixprice").val(result.fixprice);
					
					$('#addTableDialog').dialog('open');  
				}
			});
		}
		function del_table(tableid){
			$.messager.confirm("餐桌管理","确认删除此数据吗？",function(r){
				if (r){
					$.ajax({
						type : "post",
						async : false,
						url : global_Path+"/table/delete/"+tableid+".json",
						dataType : "json",
						success : function(result) {							
							$('#tableShow').datagrid('reload');
							$.messager.alert('餐桌管理',result,'info',null);
						}
					});
				}
			});
		}
		function getTableJson(){
//		tableid,tableNo,tabletype,tableName,personNum,areaid,minprice,fixprice,custPrinter		
		var tableid = $("#tableid").val();
		var tableNo = $("#tableNo").val();
		var tableName = $("#tableName").val();
		var minprice = $("#minprice").val();
		var fixprice = $("#fixprice").val();
		var areaid = $("#areaid").combobox('getValue');
		var custPrinter = $("#custPrinter").combobox('getValue');
		var personNum = $("#personNum").combobox('getValue');
		var tabletype = $("#tabletype").combobox('getValue');
		
		var tableObject={};
		tableObject.tableid=tableid;
		tableObject.tableNo=tableNo;
		tableObject.tabletype=tabletype;
		tableObject.tableName=tableName;
		tableObject.personNum=personNum;
		tableObject.areaid=areaid;
		tableObject.custPrinter=custPrinter;
		tableObject.minprice=minprice;
		tableObject.fixprice=fixprice;
		return $.toJSON(tableObject);
		}
		
		function keywordsSearch(){
			var tabletype=$("#searchTabletype").combobox('getValue');
    	    var personNum=$("#searchPersonNum").combobox('getValue');
    	    var areaid=$("#searchAreaid").combobox('getValue');
    	    if(areaid=='全部'){
   	        	areaid='';
      	    }
//    	        if(areaid=='全部'){
//    	        	areaid='';
//       	    }
//    	     	if(personNum=='全部'){
//    	     	personNum='';
//    	    	}
//    	   	 	 if(tabletype=='全部'){
//    	   	 		tabletype='';
//    	   		 }
	    	$('#tableShow').datagrid('load',{
					"tabletype": tabletype,
					"personNum": personNum,
					"areaid":areaid
			});
	}	
		
	</script>
</head>
<body class="easyui-layout" data-options="fit:true"  >
	<div data-options="region:'center',border:false" style="padding: 10px 0px 0px 0px;">
		<div id="toolbar_datadictionary" class="info">
			<table cellspacing="0" cellpadding="0" border="0" style="width: 100%">
				<tr>
					<td align="left" width="400"><font style="font-size: 18px; color: #000; font-weight: 700;">餐台管理</font></td>
				    <td width="80"><input type="button" onclick="add_table()" value="新增餐台" class="xld_addbutton"></input></td>
				</tr>
			</table>
		</div>
		<table id="tableShow"></table>
		
		<div id="toolbar_table" class="info">
			<table cellspacing="0" cellpadding="0" border="0" style="width: 100%">
				<tr >
<!-- <td align="right"><a href="###"class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="add_table()">创建项目</a></td>
 -->					
				
				</tr>
				<tr>
					<!-- style="background-color: red;" -->
					<td align="right" width="80" >餐台分类：</td>
					<td width="50" ><input class="easyui-combobox" id="searchTabletype" name="searchTabletype" 
						data-options="panelHeight:'auto',valueField: 'label',textField: 'value',data: [{label: '0',value: '散台'},{label: '1',value: '包间'}]" /> 
					</td>
					<td align="right" width="50">就餐人数：</td>
					<td width="50" ><input  class="easyui-combobox"  id="searchPersonNum" name="searchPersonNum" 
					 data-options="panelHeight:'auto',valueField: 'label',textField: 'value',data: [{label: '0',value: '0人'},{label: '2',value: '2人'},{label: '4',value: '4人'},{label: '6',value: '6人'},{label: '8',value: '8人'},{label: '10',value: '10人'}]" /> 
					 </td>
					<td align="right" width="50">所在区域：</td>
					<td width="50"><input  class="searchAreaid" id="searchAreaid" name="searchAreaid"   /></td>
					
				    <td width="120"><a href="###" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="keywordsSearch();">搜索</a></td>
				</tr>
			</table>
		</div>
	 	<div id="addTableDialog" style="overflow: hidden;">
			<input type="hidden" id="tableid" name="tableid" />
			<table cellspacing="0" cellpadding="0" border="0" style="width: 100%;">
				<tr align="left">
					<td>餐台编号:</td>
					<td><input type="text" id="tableNo" name="tableNo" /><span style="color: red">*</span></td>
				</tr>
				<tr align="left">
					<td>类型:</td>
					<td><input class="easyui-combobox" id="tabletype" name="tabletype"
						data-options="panelHeight:'auto',valueField: 'label',textField: 'value',data: [{label: '0',value: '散客'},{label: '1',value: '包间'}]" /> </td>
				</tr>
				<tr align="left">
					<td>名称:</td>
					<td><input type="text" id="tableName" name="tableName" /></td>        
				</tr>
				<tr align="left">
					<td>就餐人数:</td>
					<td><input  class="easyui-combobox"  id="personNum" name="personNum" 
					 data-options="panelHeight:'auto',valueField: 'label',textField: 'value',data: [{label: '0',value: '0人'},{label: '2',value: '2人'},{label: '4',value: '4人'},{label: '6',value: '6人'},{label: '8',value: '8人'},{label: '10',value: '10人'}]" /> 
					 </td>
				</tr>
				<tr align="left">
					<td >所在区域：</td>
					<td ><input  id="areaid" name="areaid"  /></td>
				</tr>
				<tr align="left">
					<td>最低消费:</td>
					<td><input type="text" id="minprice" name="minprice" /></td>        
				</tr>
				<tr align="left">
					<td>固定费:</td>
					<td><input type="text" id="fixprice" name="fixprice" /></td>        
				</tr>
				<tr align="left">
					<td>客用单打印机:</td>
					<td><input type="text" id="custPrinter" name="custPrinter" /></td>        
				</tr>
			</table>
		</div> 
	</div>

	
</body>

</html>
