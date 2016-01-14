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
				url : global_Path+"/branchshop/page.json",
				method : 'post',
				fit : false,
				fitColumns : true,
				singleSelect : true,
				pagination : true,
				rownumbers : true,
				pageSize : 10,
				pageList : [ 10, 20, 30 ],
				remoteSort : false,
  				idField : 'id',
				loadMsg : '数据装载中......',
				columns : [ [{
					field : 'shopname',
					title : '名称',
					width : 10,
					align : 'left',
				},{
					field : 'areaid',					
					title : '所属大区',
					width : 10,
					align : 'center',
				},{
					field : 'address',
					title : '地址',
					width : 10,
					align : 'center',
				},{
					field : 'storemanager',					
					title : '店长',
					width : 10,
					align : 'center',
				},{
					field : 'mobilephone',					
					title : '电话',
					width : 10,
					align : 'center',
				},{
					field : 'id',
					title : '操作',
					width : 10,
					align : 'center',
					formatter : function(value, row, index) {
						var modify ="<a href=\"###\" onclick=\"modify_parterner('" + index + "')\">查看/修改</a>";
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
			 $('#myForm').form({  
			        url:global_Path+'/branchshop/save',
			        onSubmit:function(){  
			            return true;  
			        },  
			        success:function(result){ 
			        	result = $.parseJSON(result);
			            alert(result.message); 
						$('#dishShow').datagrid('reload');
			    		$('#addDishDialog').dialog('close');
			        }  
			    }); 
			$('#addDishDialog').dialog({   
			    title: '门店管理(*号部分为必填项)',   
			    width:650,   
			    height: 300,   
			    closed: true,   
			    cache: false,  
			    inline : true,
			    modal: true,
				buttons : [ {
					text : '保存', 
					handler : 
					function(){
						$('#myForm').submit();
					}						
				}, {
					text : '取消',
					handler : function() {
						$('#addDishDialog').dialog('close');
						$('#myForm').form('clear');
					}
				}, {
					text : '重置',
					handler :function(){
						$('#myForm').form('clear');
					}
				}
				],
			onClose : function() {
				$('#myForm').form('clear');
			}
			});	
// 			$('#type').combobox({
// 			    url:global_Path + '/datadictionary/getDatasByType/COUPONSCATEGORY.json',
// 			    valueField:'id',
// 			    textField:'itemDesc',
// 			    panelHeight : 'auto',
// 			    loadFilter : function(row){
// 			    	return row;
// 				}
// 			});
});
//------------------------------------------------------------------------------------------------------------		
//------------------------------------------------------------------------------------------------------------
		function del_dish(id){
			$.messager.confirm("分店管理","确认删除此数据吗？",function(r){
				if (r){
					$.ajax({
						type : "post",
						async : false,
						url : global_Path+"/delete/delete/"+id+".json",
						dataType : "json",
						success : function(result) {							
							$('#dishShow').datagrid('reload');
							$.messager.alert('分店管理',result,'info',null);
						}
					});
				}
			});
		}
		function modify_parterner(index){
		    $('#dishShow').datagrid('selectRow',index);
	    	var row = $('#dishShow').datagrid('getSelected');  
			$('#addDishDialog').dialog('open'); 
			if(row){
				$("#myForm").form("load", row);
			}
		}

		
		function add_datadictionary(){
			$('#addDishDialog').dialog('open');  
		}
		function keywordsSearch(value){
			var searchshopname= $("#searchshopname").val();
			var searchaddress= $("#searchaddress").val();
        	 var searchareaid=$("#searchareaid").combobox('getValue');
        	 $('#dishShow').datagrid('load',{
					"shopname": searchshopname,
					"address": searchaddress,
					"areaid": searchareaid
				});
	    }
	</script>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="padding: 10px 10px;">
		<table id="dishShow"></table>  
		<div id="toolbar_datadictionary" class="info">
			<table cellspacing="0" cellpadding="0" border="0" style="width: 100%">
				<tr style="border-bottom: 1px solid #9A9691;">
					<td align="left" width="400" colspan="3"><font style="font-size: 18px; color: #000; font-weight: 700;">门店管理</font></td>
				    <td align="left"  width="80" style="padding-right: 80px;"><input type="button" onclick="add_datadictionary()" value="新增门店" class="xld_addbutton" style="width: 100px;"></input></td>
				</tr>
				<tr>
					<td align="left" width="25%" >门店名称:<input id="searchshopname"/></td>
				    <td align="left" width="25%">所属大区：<input class="easyui-combobox" id="searchareaid" name="searchareaid" 
						data-options="panelHeight:'auto',valueField: 'label',textField: 'value',data: [{label: '1',value: '北京大区'},
						{label: '2',value: '华中大区'},{label: '3',value: '西北大区'}]" /></td>
					<td align="left" width="25%" >地址:<input id="searchaddress"/></td>
				    <td align="left" width="25%"><a href="###" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="keywordsSearch();">搜索</a></td>
				</tr>
			</table>
		</div>
	 	<div id="addDishDialog">
	 	<form id="myForm" method="post">
			<input type="hidden" id="id" name="id" />
			<table cellspacing="0" cellpadding="0" border="0" style="width: 100%">
				<tr align="left">
					<td>门店名称:</td>
					<td><input type="text" id="shopname" name="shopname" />
					</td>
					<td>所属大区:</td>
					<td><input class="easyui-combobox" id="areaid" name="areaid" 
						data-options="panelHeight:'auto',valueField: 'label',textField: 'value',data: [{label: '1',value: '北京大区'},
 						{label: '2',value: '华中大区'},{label: '3',value: '西北大区'}]" /> </td> 
				</tr>
				<tr align="left">
					<td>地&nbsp;&nbsp;&nbsp;&nbsp;址:</td>
					<td><input type="text" id="province" name="province" />
					</td>
					<td>详细地址:</td>
<!-- 					<td><input type="text" id="address" name="address" placeholder="详细地址"/> -->
					<td><input type="text" id="address" name="address" />
					</td>
				</tr>
				<tr align="left">
					<td>店&nbsp;&nbsp;&nbsp;&nbsp;长:</td>
					<td><input type="text" id="storemanager" name="storemanager" />
					</td>
					<td>固定电话:</td>
					<td><input type="text" id="telphone" name="telphone" />
					</td>
				</tr>
				<tr align="left">
					<td>手&nbsp;&nbsp;&nbsp;&nbsp;机:</td>
					<td><input type="text" id="mobilephone" name="mobilephone" />
					</td>
					<td>传&nbsp;&nbsp;&nbsp;&nbsp;真:</td>
					<td><input type="text" id="fax" name="fax" />
					</td>
				</tr>
			</table>
		 </form>
		</div> 
	</div>
</body>
</html>
