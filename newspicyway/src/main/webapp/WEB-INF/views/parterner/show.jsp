<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 	<%@ include file="/common/resource.jsp" %>  
	<script type="text/javascript">
		$(function(){
			//话题表格配置
			$('#dishShow').datagrid({    
				url : global_Path+"/parterner/page.json",
				method : 'post',
				fit : false,
				fitColumns : true,
				singleSelect : true,
				pagination : true,
				rownumbers : true,
				pageSize : 10,
				pageList : [ 10, 20, 30 ],
				remoteSort : false,
  				idField : 'parternerId',
				loadMsg : '数据装载中......',
				columns : [ [{
					field : 'code',
					title : '客户编号',
					width : 10,
					align : 'left',
				},{
					field : 'name',					
					title : '客户名称',
					width : 10,
					align : 'center',
				},{
					field : 'typename',
					title : '客户类型',
					width : 10,
					align : 'center',
				},{
					field : 'relaperson',					
					title : '联系人',
					width : 10,
					align : 'center',
				},{
					field : 'address',					
					title : '地址',
					width : 10,
					align : 'center',
				},{
					field : 'status',					
					title : '是否可以挂账',
					width : 10,
					align : 'center',
					formatter:function(value, row, index){  
						if(value=="0"){
							return "否";
						}else{
							return "是";
						}
					}
				},{
					field : 'parternerId',
					title : '操作',
					width : 10,
					align : 'center',
					formatter : function(value, row, index) {
						var modify ="<a href=\"###\" onclick=\"modify_parterner('" + value + "')\">查看/修改</a>";
						var del = "&nbsp;&nbsp;<a href=\"###\" onclick=\"del_dish('" + value + "')\">删除</a>";
					    return modify+del;
					}
				}
				] ],
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
			$('#addDishDialog').dialog({   
			    title: '合作单位管理(*号部分为必填项)',   
			    width:650,   
			    height: 380,   
			    closed: true,   
			    cache: false,  
			    inline : true,
			    modal: true,
				buttons : [ {
					text : '保存', 
					handler : 
					function(){//dishno,title
								
					    $('#code').validatebox({required: true,validType:'maxLength[1,10]',missingMessage:"客户编号不能为空！"});
					    $('#name').validatebox({required:true,validType:'maxLength[1,10]',missingMessage:"客户名称不能为空！"});
						$('#telephone').validatebox({required:false,validType:'phone'});
						$("#code").validatebox('enableValidation');
						$("#name").validatebox('enableValidation');
						$("#telephone").validatebox('enableValidation');
					    if ($('#name').validatebox('isValid') && $('#code').validatebox('isValid')&& $('#telephone').validatebox('isValid')){
					    	save_parterner();                           
						}	
					}						
				}, {
					text : '取消',
					handler : function() {
						init_data();
						$('#addDishDialog').dialog('close');
					}
				}, {
					text : '重置',
					handler :function(){
						init_data();
					}
				}
				],
			onClose : function() {
				init_data();
			}
			});	
			$('#type').combobox({
			    url:global_Path + '/datadictionary/getDatasByType/COUPONSCATEGORY.json',
			    valueField:'id',
			    textField:'itemDesc',
			    panelHeight : 'auto',
			    loadFilter : function(row){
			    	return row;
				}
			});
});
//------------------------------------------------------------------------------------------------------------		
//------------------------------------------------------------------------------------------------------------
		function init_data(){
			$("#id").val("");
			$("#code").val("");
			$("#name").val("");
			$("#telephone").val("");
			$("#telephone").validatebox('disableValidation');
			$("#code").validatebox('disableValidation');
			$("#name").validatebox('disableValidation');
			$("#relaperson").val("");
			$("#address").val("");
//     	    $("#status").combobox('select','0');
    	    $("#status").combobox('setValue','0');
    	    $("#type").combobox('setValue','');
		}
		function del_dish(id){
			$.messager.confirm("合作单位管理","确认删除此数据吗？",function(r){
				if (r){
					$.ajax({
						type : "post",
						async : false,
						url : global_Path+"/parterner/delete/"+id+".json",
						dataType : "json",
						success : function(result) {							
							$('#dishShow').datagrid('reload');
							$.messager.alert('合作单位管理',result,'info',null);
						}
					});
				}
			});
		}
		function getparternerJson(){
			var id = $("#id").val();
			var code = $("#code").val();
			var name = $("#name").val();
			var telephone = $("#telephone").val();
			var relaperson = $("#relaperson").val();
			var address = $("#address").val();
			var status = $("#status").combobox('getValue');
			var type = $("#type").combobox('getValue');
			var parterner={};
			parterner.parternerId=id;
			parterner.code=code;
			parterner.type=type;
			parterner.name=name;
			parterner.telephone=telephone;
			parterner.relaperson=relaperson;
			parterner.address=address;
			parterner.status=status;
			//id与dictid之间的转换
			return $.toJSON(parterner);
			}
		function save_parterner(){
			if($("#type").combobox('getValue')==''){
				alert("客户类型不能为空");	
				return;
			}
			var json=getparternerJson();
				$.ajax({
					type:"post",
					async:false,
					url : global_Path+'/parterner/save',
					contentType:'application/json;charset=UTF-8',
				    data:json, 
					dataType : "json",
					success : function(result) {	
						alert(result.message);
						$('#dishShow').datagrid('reload');
			    		$('#addDishDialog').dialog('close');
			    		init_data(); 					
					}
				});
		}
		//修改查询
		function modify_parterner(id){
			$.ajax({
				type : "post",
				async : false,
				url : global_Path+"/parterner/findById/"+id+".json",
				dataType : "json",
				success : function(result) {
					$("#id").val(result.parternerId);					
					$("#code").val(result.code);					
					$("#name").val(result.name);					
					$("#telephone").val(result.telephone);					
					$("#relaperson").val(result.relaperson);					
					$("#address").val(result.address);	
					$("#type").combobox('setValue',result.type);
					$("#status").combobox('setValue',result.status);
					$('#addDishDialog').dialog('open');  
				}
			});
		}

		
		function add_datadictionary(){
			$('#addDishDialog').dialog('open');  
			init_data();		
		}
		function getlist(value){
			$('#dishShow').datagrid('load',{
				 "name":value
			});
	    }
	</script>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="padding: 10px 10px;">
		<table id="dishShow"></table>  
		<div id="toolbar_datadictionary" class="info">
			<table cellspacing="0" cellpadding="0" border="0" style="width: 100%">
				<tr>
					<td align="left" width="400" ><font style="font-size: 18px; color: #000; font-weight: 700;">合作单位管理</font></td>
				    <td align="right" width="80"><input type="button" onclick="add_datadictionary()" value="新增合作单位" class="xld_addbutton" style="width: 100px;"></input></td>
				    <td align="center" width="80"><input id="dishNameSearch" class="easyui-searchbox" style="width:150px;"  data-options="searcher:getlist"  prompt="合作单位搜索" /></td>
				</tr>
			</table>
		</div>
	 	<div id="addDishDialog">
			<input type="hidden" id="id" name="id" />
			<table cellspacing="0" cellpadding="0" border="0" style="width: 100%">
				<tr align="left">
					<td>客户编号:</td>
					<td><input type="text" id="code" name="code" />
					<span style="color: red">*</span></td>
				</tr>
				<tr align="left">
					<td>客户名称:</td>
					<td><input type="text" id="name" name="name" />
					<span style="color: red">*</span></td>
				</tr>
				<tr align="left">
					<td>客户类型:</td>
					<td><input type="text" id="type" name="type" />
					</td>
				</tr>
				<tr align="left">
					<td>联系人:</td>
					<td><input type="text" id="relaperson" name="relaperson" />
					</td>
				</tr>
				<tr align="left">
					<td>客户电话:</td>
					<td><input type="text" id="telephone" name="telephone" />
					</td>
				</tr>
				<tr align="left">
					<td>联系地址:</td>
					<td><input type="text" id="address" name="address" />
					</td>
				</tr>
				<tr align="left">
					<td>是否可以挂账:</td>
					<td><input class="easyui-combobox" id="status" name="status"
						data-options="panelHeight:'auto',valueField: 'label',textField: 'value',data: [{label: '0',value: '否',selected:true},{label: '1',value: '是'}]" /> 
					</td>
				</tr>
			</table>
		</div> 
	</div>
</body>
</html>
