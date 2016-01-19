<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@page import="com.candao.www.utils.SessionUtils"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 	<%@ include file="/common/resource.jsp" %>  
	<script type="text/javascript">
		$(function(){
			//话题表格配置
				$('#tables').datagrid({
				url : global_Path + '/version/findAll.json',
				method : 'post',
				fit : false,
				title : '版本信息',
				fitColumns : true,
				singleSelect : true,
				rownumbers : true,
				remoteSort : false,
  				idField : 'type',
				loadMsg : '数据装载中......',
				columns : [ [ {
					field : 'no',
					title : '版本号',
					width : 40,
					align : 'left'
				}, {
					field : 'info',
					title : '更新介绍',
					width : 40,
					align : 'center'
				},{
					field : 'url',
					title : '下载地址',
					width : 40,
					align : 'center'
				},{
					field : 'type',
					title : '终端类型',
					width : 40,
					align : 'center',
					formatter : function(value, row, index) {
						if(value==1){
							return "安卓平板";
						}else if(value==2){
							return "安卓手机";
						}else if(value==3){
							return "苹果平板";
						}else if(value==4){
							return "苹果手机";
						}
					}
				},{
					field : 'isforce',
					title : '是否强制更新',
					width : 40,
					align : 'center',
					formatter : function(value, row, index) {
						if(value==1){
							return "是";
						}else{
							return "否";
						}
					}
				},{
					field : 'other',
					title : '操作',
					width : 40,
					align : 'center',
					formatter : function(value, row, index) {
						var modify ="<a href=\"###\" onclick=\"modify_version('" + row.type + "')\">修改</a>";
						return modify;
					}
				}
				] ]
			});
				$('#addVersionDialog').dialog({   
				    title: '修改版本信息 (*号部分为必填项)',   
				    width: 500,   
				    height: 400,   
				    closed: true,   
				    cache: false,  
				    inline : true,
				    modal: true,
					buttons : [ {
						text : '保存',
						handler :
							function (){
							    $('#no').validatebox({required: true,missingMessage:"版本号不能为空！"});
							    $('#url').validatebox({required:true,validType:"url",missingMessage:"下载地址不能为空！",invalidMessage:"请输入正确的地址"});
							    $("#no").validatebox('enableValidation');
								$("#url").validatebox('enableValidation');
							    if ($('#no').validatebox('isValid') && $('#url').validatebox('isValid')){
							    save_version();                           
								}	 
							
						}
							
					}, {
						text : '关闭',
						handler : function() {
							$('#addVersionDialog').dialog('close');
						}
					} ],
				onClose : function() {
				    $("#no").val("");
					$("#info").val("");
					$("#url").val("");
					$("#typeName").val("");
					$("#type").val("");
					$("#rd1").prop("checked",true);
					$("#rd2").prop("checked",false);
					$("#no").validatebox('disableValidation');
					$("#url").validatebox('disableValidation');
				}
				});
				
		});
		//修改查询
		//  终端类型  1： android平板  2：android手机   3:ios平板  4：ios手机
		function modify_version(type){
			$.ajax({
				type : "post",
				async : false,
				url : global_Path+"/version/findByType/"+type+".json",
				dataType : "json",
				success : function(result) {
					$("#no").val(result.no);
					$("#info").val(result.info);
					$("#url").val(result.url);
					var value=result.type;
					if(value==1){
						$("#typeName").val("安卓平板");
					}else if(value==2){
						$("#typeName").val("安卓手机");
					}else if(value==3){
						$("#typeName").val("苹果平板");
					}else if(value==4){
						$("#typeName").val("苹果手机");
					}
					$("#type").val(result.type);
					if(result.isforce==1){
						$("#rd1").attr("checked",true);
					}else{
						$("#rd1").removeAttr("checked");
						$("#rd2").prop("checked",true);
					}
					$('#addVersionDialog').dialog('open');  
				}
			}); 
		}
		function save_version(){
				var json=getVersionJson();
				$.ajax({
					type : "post",
					async : false,
					url : global_Path+"/version/save",
					contentType:'application/json;charset=UTF-8',
					data :json,
					dataType : "json",
					success : function(result) {
						$.messager.alert("提示",result.maessge);
						$('#addVersionDialog').dialog('close');  
						$('#tables').datagrid('reload');
					}
				});
		}

		//得到表单输入
		function getVersionJson(){
			var no=$("#no").val();
			var info=$("#info").val();
			var url=$("#url").val();
			var type=$("#type").val();
			var isforce=parseInt($("input[name='isforce']:checked").val());//得到radio选中的值
			var versionObject={};
			versionObject.no=no;
			versionObject.info=info;
			versionObject.url=url;
			versionObject.type=type;
			versionObject.isforce=isforce;
			return $.toJSON(versionObject);
		}
	</script>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="padding:10px 0px 0px 0px;" >
	 <table id="tables"></table>
	 </div>
	<div id="addVersionDialog">
      <table cellspacing="0" cellpadding="0" border="0" style="width: 100%">
        <tr align="left">
            <td>版本号:</td><td>
            <input type="text" id="no" name="no" style="width: 70%;" /><span style="color: red">*</span></td>
        </tr>
        <tr align="left">
            <td>更新介绍:</td><td><textarea  id="info" name="info" style="height: 100px;width: 70%;"></textarea></td>
        </tr>
        <tr align="left">
            <td>下载地址:</td><td><input type="text" id="url"  style="width: 70%;"/><span style="color: red">*</span></td>
        </tr>
        <tr align="left">
                <td>终端类型:</td><td><input type="text" id="typeName" style="width: 70%;" readonly="readonly"/>
                <input type="hidden" id="type" name="type"/>
                </td>
        </tr>
        <tr align="left">
                <td>是否强制更新:</td>
                <td>
					<input type="radio" id="rd1" name="isforce" value="1" checked/>是
                	<input type="radio" id="rd2" name="isforce" value="0"/>否
				</td>
        </tr>
        </table>
		</div>
 
</body>
</html>
