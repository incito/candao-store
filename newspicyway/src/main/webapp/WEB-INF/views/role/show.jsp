<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 	<%@ include file="/common/resource.jsp" %>  
	<script type="text/javascript">
		$(function(){
			//话题表格配置
			$('#tables').datagrid({
				url : global_Path + '/role/page.json',
				method : 'post',
				fit : false,
				title : '所有角色',
				fitColumns : true,
				singleSelect : true,
				pagination : true,
				rownumbers : true,
				pageSize : 10,
				pageList : [ 10, 20, 30 ],
				remoteSort : false,
  				idField : 'roleid',
				loadMsg : '数据装载中......',
				columns : [ [ {
					field : 'rolename',
					title : '角色名称',
					width : 50,
					align : 'left'
				}, {
					field : 'roledesc',
					title : '角色描述',
					width : 50,
					align : 'center'
				},{
					field : 'roletype',
					title : '角色类型',
					width : 50,
					align : 'center',
					formatter : function(value, row, index) {
						if(value==1){
							return "系统角色";
						}else if(value==2){
							return "用户定义";
						}
					}
				},{
					field : 'status',
					title : '状态',
					width : 50,
					align : 'center',
					formatter : function(value, row, index) {
						if(value==1){
							return "启用";
						}else{
							return "禁用";
						}
					}
				},{
					field : 'roleid',
					title : '操作',
					width : 40,
					align : 'center',
					formatter : function(value, row, index) {
						var rowstatus;
						if(row.status==1){
							rowstatus='禁用';
						}else{
							rowstatus="启用";
						}
						var modify ="<a href=\"###\" onclick=\"modify_object('" + value + "')\">修改</a>";
						var del = "&nbsp;&nbsp;<a href=\"###\" onclick=\"del_object('" + value+"','"+row.status + "')\">"+rowstatus+"</a>";
						var role = "&nbsp;&nbsp;<a href=\"###\" onclick=\"add_resource_role('" + value + "')\">授权</a>";
						return modify+del+role;
					}
				}
				] ],
				toolbar : '#toolbar_t'
			});
			//分页参数配置
			$('#tables').datagrid('getPager').pagination({
				displayMsg : '当前显示从{from}到{to}共{total}记录',
				beforePageText : '第',
				afterPageText : '页 共 {pages} 页',
				onBeforeRefresh : function(pageNumber, pageSize) {
					$(this).pagination('loading');
					$(this).pagination('loaded');
				}
			});
			
			$('#addObjectDialog').dialog({   
			    title: '增加角色 (*号部分为必填项)',   
			    width: 400,   
			    height: 300,   
			    closed: true,   
			    cache: false,  
			    inline : true,
			    modal: true,
				buttons : [ {
					text : '保存',
					handler : 
						function(){
						 $('#rolename').validatebox({required: true,validType:'maxLength[1,10]',missingMessage:"角色名称不能为空！"});
						 $('#roledesc').validatebox({required: true,validType:'maxLength[1,50]',missingMessage:"角色描述不能为空！"});
						 $("#rolename").validatebox('enableValidation');
						 $("#roledesc").validatebox('enableValidation');
						 if($("#rolename").validatebox("isValid") && $("#roledesc").validatebox("isValid")){
							 save_object();
						 }						
					}
						
				
				}, {
					text : '关闭',
					handler : function() {
						$('#addObjectDialog').dialog('close');
					}
				} ],
			onClose : function() {
				$("#roleid").val("");
				$("#rolename").val("");
				$("#roledesc").val("");
				$("#rd1").prop("checked",true);
				$("#rd2").prop("checked",false);
				$("#roletype").combobox("clear");
			    $('#roletype').combobox('setValues','2');
 		        $("#rolename").validatebox('disableValidation');
				$("#roledesc").validatebox('disableValidation');
				// $("#usertype option[text='普通用户']").attr("selected", true);
			}
			});
			
			$('#usertype').combobox({
			    url:global_Path + '/user/getUserTat.json',
			    valueField:'id',
			    textField:'itemdesc',
			    panelHeight : 'auto',
			    loadFilter : function(row){
			    	return row;
				}
			});
			
			$('#roleDialog').dialog({   
			    title: '选择用户角色 ',   
			    width: 500,   
			    height: 400,   
			    closed: true,   
			    cache: false,  
			    inline : true,
			    modal: true,
				buttons : [ {
					text : '保存',
					handler : role_save
				}, {
					text : '关闭',
					handler : function() {
						roleId="";
						$("#roleDialog").dialog('close');
					}
				} ],
			onClose : function() {
			}
			});
			
			
		});
		
		function add_object(){
			$('#addObjectDialog').dialog('open');  
		}
		
		function save_object(){
			var check=check_validate(); 
			if(check){
				var json=getUseJson();
//     				alert(json);
				$.ajax({
					type : "post",
					async : false,
					url : global_Path+"/role/save",
					contentType:'application/json;charset=UTF-8',
					data :json,
					dataType : "json",
					success : function(result) {
						$.messager.alert("提示",result.maessge);
						$('#addObjectDialog').dialog('close');  
						$('#tables').datagrid('reload');
					}
				});
			}
		}
		//增加用户认证
		function check_validate(){
			var username=$("#rolename").val();
			if($.trim(username)==""){
				$.messager.alert("提示","请填写角色名");
				return false;
			}
			var fullname=$("#roledesc").val();
			if($.trim(fullname)==""){
				$.messager.alert("提示","请填写角色描述");
				return false;
			}
			return true;
		}
		//得到表单输入
		function getUseJson(){
			var roleid=$("#roleid").val();
			var rolename=$("#rolename").val();
			var roledesc=$("#roledesc").val();
			var roletype=parseInt($("#roletype").combobox('getValue')); 
			var status=parseInt($("input[name='status']:checked").val());//得到radio选中的值
			var userObject={};
			userObject.roleid=roleid;
			userObject.rolename=rolename;
			userObject.roledesc=roledesc;
			userObject.roletype=roletype;
			userObject.status=status;
			return $.toJSON(userObject);
		}
		//修改查询
		function modify_object(userid){
			$.ajax({
				type : "post",
				async : false,
				url : global_Path+"/role/findById/"+userid+".json",
				dataType : "json",
				success : function(result) {
					$("#roleid").val(result.roleid);
					$("#rolename").val(result.rolename);
					$("#roledesc").val(result.roledesc);
					$('#roletype').combobox('select', result.roletype);
					if(result.status==1){
						//$("#rd1").attr("checked",true);
					}else{
						$("#rd1").removeAttr("checked");
						$("#rd2").prop("checked",true);
					}
					$('#addObjectDialog').dialog('open');  
				}
			});
		}
		
		function del_object(userid,status){
			$.ajax({
				type : "post",
				async : false,
				url : global_Path+"/role/delete/"+userid+"/"+status+".json",
				dataType : "json",
				success : function(result) {
// 					alert(result);
					$('#tables').datagrid('reload');
				}
			});
		}
		
		var roleId;
	    function add_resource_role(roleid){
	    	roleId=roleid;
	    	//getRoleDatagrid();
			$.ajax({
				type : "post",
				async : false,
				url : global_Path+"/resource/getTreeNode",
				dataType : "json",
				success : function(result) {
					$('#treeNode').tree({
		                 checkbox:true,  
  		                 cascadeCheck:false,
 	   	     	    	 data:[result.treeNode]
 		 	    	});
				}
			});
			
			$.ajax({
				type : "post",
				async : false,
				url : global_Path+"/resource/getRoleResource/"+roleid+".json",
				dataType : "json",
				success : function(result) {
					for(var i in result){
// 						alert(result[i].resourceid);
						var node = $('#treeNode').tree('find', result[i].resourceid);
						$('#treeNode').tree('check',node.target);
					}
				}
			});
			
	    	$("#roleDialog").dialog('open');
	    }
	    
	    //加载用户角色信息
	    function getRoleDatagrid(){
	    	$('#roleTable').datagrid({
	    		url : global_Path + '/user/page.json',
				method : 'post',
				fit : false,
				fitColumns : true,
// 				singleSelect : true,
				pagination : true,
				rownumbers : true,
				pageSize : 10,
				pageList : [ 10, 20, 30 ],
				remoteSort : false,
  				idField : 'userid',
				loadMsg : '数据装载中......',
				columns : [ [ 
				{
					field : 'uesrid',
					title : '选择',
					width : 50,
					align : 'left',
					checkbox: true
				}, {
					field : 'username',
					title : '用户名',
					width : 50,
					align : 'left'
				}, {
					field : 'password',
					title : '密码',
					width : 50,
					align : 'center'
				}
				]]
			});
	    }
	    
	    function role_save(){
// 	    	alert(roleId);
	    	var checked=$('#treeNode').tree('getChecked');
	    	var rosourceIds=[];
	    	for(var i in checked){
// 	    		alert(checked[i].id);
	    		rosourceIds.push(checked[i].id);
	    	}
	    	var json={};
	    	json.roleId=roleId;
	    	json.resourceIds=rosourceIds;
	    	$.ajax({
				type : "post",
				async : false,
				url : global_Path+"/resource/addResourceRole.json",
				data : $.toJSON(json),
				contentType:'application/json;charset=UTF-8',
				dataType : "json",
				success : function(result) {
					$.messager.alert("提示",result);
    					$("#roleDialog").dialog('close');
				}
			});
	    }
	</script>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="padding:10px 0px 0px 0px;">
	 <table id="tables"></table>
		<div id="toolbar_t" class="info">
			<table cellspacing="0" cellpadding="0" border="0" style="width:100%">
				<tr>
				    <td align="left" width="60">
				    <a href="###" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="add_object()">创建角色</a>
					</td>
				</tr>
			</table>
		</div>
		
	<div id="addObjectDialog">
      <table cellspacing="0" cellpadding="0" border="0" style="width: 100%">
        <tr align="left">
            <td>角色名称:</td><td><input type="hidden" id="roleid" />
            <input type="text" id="rolename" name="rolename"  /><span style="color: red">*</span></td>
        </tr>
        <tr align="left">
                <td>角色描述:</td><td><input type="text" id="roledesc" /><span style="color: red">*</span></td>
        </tr>
        <tr align="left">
                <td>角色类型:</td>
                <td>
                 <select id="roletype" class="easyui-combobox" name="roletype" style="width:155px;">  
			       <option value="1">系统角色 </option>  
			       <option value="2" selected="selected">用户定义</option>  
                  </select>  
                </td>
        </tr>
        <tr align="left">
            <td>角色状态:</td>	
            <td>
                                   启用：<input type="radio" id="rd1" name="status" value="1" checked/>
                                   禁用：<input type="radio" id="rd2" name="status" value="0"/>
            </td>
        </tr>
        </table>
		</div>
		
	  <div id="roleDialog">
       <!--  <table id="roleTable"></table>  -->
<ul id="treeNode" ></ul>  
      </div>
</body>
</html>
