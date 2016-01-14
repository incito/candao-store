<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 	<%@ include file="/common/resource.jsp" %>  
 	
	<script type="text/javascript">
		$(function(){
			//话题表格配置
			$('#datadictionaryShow').datagrid({
				url : global_Path + '/datadictionary/page.json',
				method : 'post',
				fit : false,
				title : '所有用户',
				fitColumns : true,
				singleSelect : true,
				pagination : true,
				rownumbers : true,
				pageSize : 10,
				pageList : [ 10, 20, 30 ],
				remoteSort : false,
  				idField : 'id',
				loadMsg : '数据装载中......',
				columns : [ [ 
					{
						field : 'typename',					
						title : '项目名称',
						width : 50,
						align : 'center',
						
					},         
				  {
					field : 'itemid',
					title : '状态编号',
					width : 50,
					align : 'left'
				},{
					field : 'itemDesc',					
					title : '状态描述',
					width : 50,
					align : 'center',
				}, {
					field : 'itemSort',
					title : '状态顺序',
					width : 50,
					align : 'center'
				}
				,{
					field : 'id',
					title : '操作',
					width : 40,
					align : 'center',
					formatter : function(value, row, index) {
						var modify ="<a href=\"###\" onclick=\"modify_datadictionary('" + value + "')\">查看/修改</a>";
						var del = "&nbsp;&nbsp;<a href=\"###\" onclick=\"del_datadictionary('" + value + "')\">删除</a>";
					    return modify+del;
					}
				}
				] ],
				toolbar : '#toolbar_datadictionary'
			});
			//分页参数配置
			$('#datadictionaryShow').datagrid('getPager').pagination({
				displayMsg : '当前显示从{from}到{to}共{total}记录',
				beforePageText : '第',
				afterPageText : '页 共 {pages} 页',
				onBeforeRefresh : function(pageNumber, pageSize) {
					$(this).pagination('loading');
					$(this).pagination('loaded');
				}
			});
			
			$('#addDataDictionaryDialog').dialog({   
			    title: '增加数据字典 (*号部分为必填项)',   
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
						    $('#itemid').validatebox({required: true,missingMessage:"不能为空！"});
						    $('#itemDesc').validatebox({required:true,missingMessage:"不能为空！"});
							$('#itemSort').validatebox({required:true,missingMessage:"不能为空！"});
							$("#itemid").validatebox('enableValidation');
							$("#itemDesc").validatebox('enableValidation');
							$("#itemSort").validatebox('enableValidation');
						    if ($('#itemid').validatebox('isValid') && $('#itemDesc').validatebox('isValid')&& $('#itemSort').validatebox('isValid')){
							save_datadictionary();	                           
							}														
					}						
				}, {
					text : '取消',
					handler : function() {
						$('#addDataDictionaryDialog').dialog('close');
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
			
			
			$('#type').combobox({
			    url:global_Path + '/datadictionary/getTypeandTypename.json',
			    valueField:'type',
			    textField:'typename',
			    panelHeight : 'auto',
			    loadFilter : function(row){
			    	return row;
				},
				onSelect: function(record){
// 					alert(record.typename);
					if(record.typename=="其他"){
						$("#typenameTr2").show();
						$("#typeTr2").show();
					}else{
						$("#typenameTr2").hide();
						$("#typeTr2").hide();
					}
				}
			});
			$('#searchType').combobox({
				url:global_Path + '/datadictionary/getTypeandTypename.json',
// 				data:$('#type').combobox("getData"),
			    valueField:'type',
			    textField:'typename',
			    panelHeight : 'auto',
			    loadFilter : function(row){
			    	return row;
				}
			});
			$("#typenameTr2").show();
			$("#typeTr2").show();
	});
//---------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------
		function onSelectFunction(){
	
	}

		
		function init_object(){
			$("#id").val("");
			$("#type").combobox('setValue','');	
			$("#itemid").val("");
			$("#itemDesc").val("");
			$("#itemSort").val("");
			$("#typename2").val("");
			$("#type2").val("");
			$("#typenameTr2").hide();
			$("#typeTr2").hide();
		   
		}
		
		function add_datadictionary(){
			
			$('#addDataDictionaryDialog').dialog('open');  
			init_object();		
		}
		
		function save_datadictionary(){
			var json=getDataDictionaryJson();
			
			if($("#id").val()!=null && $("#id")!=""){
				$.ajax({
					type:"post",
					async:false,
					url : global_Path+'/datadictionary/save',
					contentType:'application/json;charset=UTF-8',
				    data:json, 
					dataType : "json",
					success : function(result) {								
						$('#datadictionaryShow').datagrid('reload');
						$('#addDataDictionaryDialog').dialog('refresh'); 
						$('#addDataDictionaryDialog').dialog('close');
						$.messager.alert('数据字典',result.maessge,'info',null);						
					}
				});
			}	
		}

		//修改查询
		function modify_datadictionary(id){
			$.ajax({
				type : "post",
				async : false,
				url : global_Path+"/datadictionary/findById/"+id+".json",
				dataType : "json",
				success : function(result) {
					
					$("#id").val(result.id);					
					$("#type").combobox('select',result.type);
					$("#itemid").val(result.itemid);
					$("#itemDesc").val(result.itemDesc);
					$("#itemSort").val(result.itemSort);
					
// 					alert($("#type").combobox('select',result.type));
// 					alert(result.typename);
// 					alert($("#type").combobox('getText'));
					$('#addDataDictionaryDialog').dialog('open');  
				}
			});
		}

		function del_datadictionary(id){
			$.messager.confirm("数据字典","确认删除此数据吗？",function(r){
				if (r){
					$.ajax({
						type : "post",
						async : false,
						url : global_Path+"/datadictionary/delete/"+id+".json",
						dataType : "json",
						success : function(result) {							
							$('#datadictionaryShow').datagrid('reload');
							$.messager.alert('数据字典',result,'info',null);
						}
					});
				}
			});
		}


		function getDataDictionaryJson(){
		var id = $("#id").val();
		var itemid = $("#itemid").val();
		var itemDesc = $("#itemDesc").val();
		var itemSort = $("#itemSort").val();
		var type = $("#type").combobox('getValue');
		var typename = $("#type").combobox('getText');
		if($("#typename2" ).val()!=""){
			
			type=$("#type2").val();
			typename=$("#typename2" ).val();
		}
		var datadictionaryObject={};
		datadictionaryObject.id=id;
		//id与dictid之间的转换
		datadictionaryObject.type=type;
		datadictionaryObject.itemid=itemid;
		datadictionaryObject.itemDesc=itemDesc;
		datadictionaryObject.itemSort=itemSort;
		datadictionaryObject.typename=typename;
		return $.toJSON(datadictionaryObject);
		}
		
		
		function keywordsSearch(){
		
			var type=$("#searchType").combobox('getValue');
    	        if(type=='全部'){
    	        	type='';
       	    	}
	    	$('#datadictionaryShow').datagrid('load',{
	    		
				"type": type,
				
		});
	   
	    	
	}			
	</script>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="padding: 10px 0px 0px 0px;">
		<table id="datadictionaryShow"></table>
		<div id="toolbar_datadictionary" class="info">
			<table cellspacing="0" cellpadding="0" border="0" style="width: 100%">
				<tr>
					<td align="left" width="400"><a href="###"class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="add_datadictionary()">创建项目</a></td>
					<td align="right" width="60">项目名称：</td>
					<td width="120"><input class="searchType" id="searchType" name="searchType"  style="height:25px;width: 100px" /></td>
					<td width="80"><a href="###" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="keywordsSearch();">查询</a></td>
				</tr>
			</table>
		</div>
	 	<div id="addDataDictionaryDialog" style="top:1px;">
			<input type="hidden" id="id" name="id" />
			<table cellspacing="0" cellpadding="0" border="0" style="width: 100%">
				<tr align="left" id="typeTr">
					<td>项目名称:</td>
					<td><input type="text" id="type" name="type" />
					<span style="color: red">*</span></td>
				</tr >
				<tr align="left" id="typeTr2">
					<td>项目英文名:</td>
					<td><input type="text" id="type2" name="type2" />
					<span style="color: red">*</span></td>
				</tr>
				<tr align="left" id="typenameTr2">
					<td>项目名称:</td>
					<td><input type="text" id="typename2" name="typename2" />
					<span style="color: red">*</span></td>
				</tr>
				<tr align="left">
					<td>状态编号:</td>
					<td><input type="text" id="itemid" name="itemid"/><span style="color: red">*</span></td>
				</tr>
				<tr align="left">
					<td>状态描述:</td>
					<td><input type="text" id="itemDesc" name="itemDesc" /><span style="color: red">*</span></td>        
				</tr>
				<tr align="left">
					<td>状态顺序:</td>
					<td><input type="text" id="itemSort" name="itemSort" /><span style="color: red">*</span></td>        
				</tr>

			</table>
		</div> 
	</div>
</body>
</html>
