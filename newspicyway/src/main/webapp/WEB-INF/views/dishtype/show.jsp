<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 	<%@ include file="/common/resource.jsp" %>  
 	
	<script type="text/javascript">
		$(function(){
			//话题表格配置
			$('#datadictionaryShow').treegrid({    
				url : global_Path+"/dishtype/findAllNote/0.json",
				method : 'post',
// 				fit : false,
				fitColumns : true,
				rownumbers : false,
				remoteSort : false,
  				idField : 'id',
  				animate: false,
  				treeField:'itemid',
  				collapsible: true,
				loadMsg : '数据装载中......',
				columns : [ [{
					field : 'itemid',
					title : '编号',
					width : 50,
					align : 'left',
				}, {
					field : 'itemdesc',
					title : '分类名称',
					width : 50,
					align : 'center'
				},{
					field : 'remark',					
					title : '打印口',
					width : 50,
					align : 'center',
				},{
					field : 'depthnum',					
					title : '分类级别',
					width : 50,
					align : 'center',
					formatter:function(value, row, index){  
                        if (value) {  
                            switch (value) {  
                                case '1':  
                                    return '一级分类';  
                                    break;  
                                case '2':  
                                    return '二级分类';  
                                    break;  
                                case '3':  
                                    return '三级分类';  
                                    break;  
                                case '4':  
                                    return '四级分类';  
                                    break;  
                                case '5':  
                                    return '五级分类';  
                                    break;  
                                default :  
                                    return '未知';  
                                    break;  
                            }  
                        }  
                    },
				},{
					field : 'fitemDesc',					
					title : '上级分类',
					width : 50,
					align : 'center',
					formatter:function(value, row, index){
						if(value!=null&&value!=""){return value;}else{
							return "-";
						}
					},
				},{
					field : 'itemsort',					
					title : '排序',
					width : 50,
					align : 'center',
				},{
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
				onLoadSuccess:function(row, data){
					$('#datadictionaryShow').treegrid('collapseAll');
				},
				
				toolbar : '#toolbar_datadictionary'
			});
			
			$('#addDataDictionaryDialog').dialog({   
			    title: '菜品分类 (*号部分为必填项)',   
			    width: 500,   
			    height: 400,   
			    closed: true,   
			    cache: false,  
			    inline : true,
			    modal: true,
				buttons : [ {
					text : '保存',
					handler : 
					function(){
						    $('#itemid').validatebox({required: true,validType:'maxLength[1,10]',missingMessage:"分类编号不能为空！"});
						    $('#itemdesc').validatebox({required:true,validType:'maxLength[1,10]',missingMessage:"分类名称不能为空！"});
						    $('#itemSort').validatebox({required:true,validType:'number',missingMessage:"排序不能为空！"});
							$("#itemid").validatebox('enableValidation');
							$("#itemdesc").validatebox('enableValidation');
						    if ($('#itemid').validatebox('isValid') && $('#itemdesc').validatebox('isValid')&& $('#itemSort').validatebox('isValid')){
								save_datadictionary();	                           
							}														
					}						
				}, {
					text : '取消',
					handler : function() {
						$('#addDataDictionaryDialog').dialog('close');
						init_data();
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
			$('#remark').combobox({
			    url:global_Path + '/dish/getPrintersList.json',
			    valueField:'printerid',
			    textField:'printername',
			    panelHeight : 'auto',
			    loadFilter : function(row){
			    	return row;
				}
			});
		});
//-----------------------------------------------------------------------------------------------------		
//-----------------------------------------------------------------------------------------------------		
		function getcombox(id){
			$('#fid').combobox({
			    url:global_Path + '/dishtype/getDataDictionaryTag.json',
			    valueField:'id',
			    textField:'itemdesc',
			    panelHeight : 'auto',
			    loadFilter : function(row){
			    	var objectList=[];
			    	$.each(row,function(index,item){
			    		if(item.id!=id){
			    			objectList.push(item);
			    		}
			    	});
			    	return objectList;
				}
			});
		}
		function init_data(){
			$("#id").val("");
			$("#fid").combobox('setValue','0');
			$("#remark").combobox('setValue','');
			$("#isShow").combobox('setValue','1');
//             $("#itemdesc").validatebox('disableValidation');
            $("#itemdesc").val("");
// 			$("#itemid").validatebox('disableValidation');
			$("#itemid").val("");
// 			$("#itemSort").validatebox('disableValidation');
			$("#itemSort").val("");
		}
		
		function save_datadictionary(){
			var json=getDataDictionaryJson();
				$.ajax({
					type:"post",
					async:false,
					url : global_Path+'/dishtype/save',
					contentType:'application/json;charset=UTF-8',
				    data:json, 
					dataType : "json",
					success : function(result) {								
						$('#datadictionaryShow').treegrid('reload');
						$('#addDataDictionaryDialog').dialog('refresh'); 
						$('#addDataDictionaryDialog').dialog('close');
						$.messager.alert('菜品分类',result.maessge,'info',null);						
					}
				});
			init_data();
		}
		//修改查询
		function modify_datadictionary(id){
			getcombox(id);
			$.ajax({
				type : "post",
				async : false,
				url : global_Path+"/dishtype/findById/"+id+".json",
				dataType : "json",
				success : function(result) {
					$("#id").val(result.id);					
					$("#itemid").val(result.itemid);				
					$("#itemSort").val(result.itemsort);				
					$("#itemdesc").val(result.itemdesc);
// 					var comdata=$('#fid').combobox('getData');
// 					var flag=false;
// 					if(comdata.length>0){
// 					   for (var i=0;i<comdata.length;i++) {
						   	
// 		                     if (comdata[i].id == result.fid) {
// 		                    	 flag=true;
// 		                     }
// 		                 }
// 					}
// 					if(flag){
// 						$('#fid').combobox('select', result.fid);
// 					}else{
// 						$('#fid').combobox('select','0');
// 					}
					$('#fid').combobox('select', result.fid);
					$('#remark').combobox('select',result.remark);
					$('#isShow').combobox('select',result.isShow);
					$('#addDataDictionaryDialog').dialog('open');  
				}
			});
		}

		function del_datadictionary(id){
			$.messager.confirm("菜品分类","确认删除此数据吗？",function(r){
				if (r){
					$.ajax({
						type : "post",
						async : false,
						url : global_Path+"/dishtype/delete/"+id+".json",
						dataType : "json",
						success : function(result) {							
							$('#datadictionaryShow').treegrid('reload');
							$.messager.alert('菜品分类',result,'info',null);
						}
					});
				}
			});
		}
		
		function getDataDictionaryJson(){
			var id = $("#id").val();
			var itemid = $("#itemid").val();
			var itemSort = $("#itemSort").val();
			var itemdesc = $("#itemdesc").val();
			var fid =$("#fid").combobox('getValue');	
			var remark =$("#remark").combobox('getText');	
			var isShow =$("#isShow").combobox('getValue');	
			var datadictionaryObject={};
			datadictionaryObject.id=id;
			datadictionaryObject.itemid=itemid;
			datadictionaryObject.itemsort=itemSort;
			datadictionaryObject.itemdesc=itemdesc;
			datadictionaryObject.fid=fid;
			datadictionaryObject.remark=remark;
			datadictionaryObject.isShow=isShow;
			return $.toJSON(datadictionaryObject);
		}
		function add_datadictionary(){
			getcombox();
			$('#addDataDictionaryDialog').dialog('open');  
			init_data();
		}
	</script>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="padding: 10px 10px;">
<!-- 		<ul id="datadictionaryShow"></ul> -->
		<table id="datadictionaryShow" class="easyui-treegrid"></table>  
		<div id="toolbar_datadictionary" class="info">
			<table cellspacing="0" cellpadding="0" border="0" style="width: 100%">
				<tr>
					<td align="left" width="400"><font style="font-size: 18px; color: #000; font-weight: 700;">菜品分类管理</font></td>
				    <td width="80"><input type="button" onclick="add_datadictionary()" value="新增分类" class="xld_addbutton"></input></td>
				</tr>
			</table>
		</div>
	 	<div id="addDataDictionaryDialog" style="top:1px;">
			<input type="hidden" id="id" name="id" />
			<table cellspacing="0" cellpadding="0" border="0" style="width: 100%">
				<tr align="left">
					<td>分类编号:</td>
					<td><input type="text" id="itemid" name="itemid" />
					<span style="color: red">*</span></td>
				</tr>
				<tr align="left">
					<td>分类名称:</td>
					<td><input type="text" id="itemdesc" name="itemdesc" />
					<span style="color: red">*</span></td>
				</tr>
				<tr align="left">
					<td>上级分类:</td>
					<td><input id="fid" name="fid"/></td>
				</tr>
				<tr align="left">
					<td>打印口:</td>
					<td>
						<input id="remark" name="remark"/>
					</td>
				</tr>
				<tr align="left">
					<td>排序:</td>
					<td><input type="text" id="itemSort" name="itemSort" />
					<span style="color: red">*</span>
					</td> 
				</tr>
				<tr align="left">
					<td>是否在pad端展示:</td>
					<td><input class="easyui-combobox" id="isShow" name="isShow"
						data-options="panelHeight:'auto',valueField: 'label',textField: 'value',data: [{label: '0',value: '否'},{label: '1',value: '是'}]" /> 
					</td>
				</tr>
			</table>
		</div> 
	</div>
</body>
</html>
