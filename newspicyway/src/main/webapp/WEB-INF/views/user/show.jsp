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
				url : global_Path + '/user/page.json',
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
  				idField : 'userid',
				loadMsg : '数据装载中......',
				columns : [ [ {
					field : 'username',
					title : '登录名',
					width : 50,
					align : 'left'
				}, {
					field : 'password',
					title : '密码',
					width : 50,
					align : 'center'
				},{
					field : 'fullname',
					title : '用户实名',
					width : 50,
					align : 'center'
				},{
					field : 'phone',
					title : '联系电话',
					width : 50,
					align : 'center'
				},{
					field : 'usertypename',
					title : '用户类型',
					width : 50,
					align : 'center'
				},{
					field : 'createtime',
					title : '创建时间',
					width : 50,
					align : 'center'
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
					field : 'userid',
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
						var modify ="<a href=\"###\" onclick=\"modify_user('" + value + "')\">修改</a>";
						var del = "&nbsp;&nbsp;<a href=\"###\" onclick=\"del_user('" + value+"','"+row.status + "')\">"+rowstatus+"</a>";
						var role = "&nbsp;&nbsp;<a href=\"###\" onclick=\"add_role('" + value + "')\">角色</a>";
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
			
			$('#addUserDialog').dialog({   
			    title: '增加用户 (*号部分为必填项)',   
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
						    $('#username').validatebox({required: true,validType:'maxLength[1,10]',missingMessage:"用户名不能为空！"});
						    $('#password').validatebox({required:true,missingMessage:"输入密码不能为空！"});
						    $('#passwordcheck').validatebox({required:true,validType:'equalpass["#password"]',missingMessage:"输入密码不能为空！"});
						    $('#fullname').validatebox({required: true,validType:'maxLength[1,10]',missingMessage:"用户实名不能为空！"});
						    $('#phone').validatebox({required:false,validType:'selectSorts[11]',missingMessage:"手机号码不能为空！"});					   
						    $("#username").validatebox('enableValidation');
							$("#password").validatebox('enableValidation');
							$("#passwordcheck").validatebox('enableValidation');
							$("#fullname").validatebox('enableValidation');
							$("#phone").validatebox('enableValidation');
						    if ($('#username').validatebox('isValid') && $('#password').validatebox('isValid') && $('#passwordcheck').validatebox('isValid') && $('#fullname').validatebox('isValid') && $('#phone').validatebox('isValid')){
						    save_user();                           
							}	
						
					}
						
				}, {
					text : '关闭',
					handler : function() {
						$('#addUserDialog').dialog('close');
					}
				} ],
			onClose : function() {
				$("#userid").val("");
				$("#username").val("");
				$("#password").val("");
				$("#passwordcheck").val("");
				$("#fullname").val("");
				$("#phone").val("");
				$("#usertype").val("");
				$("#userrole").val("");
				$("#rd1").prop("checked",true);
				$("#rd2").prop("checked",false);
				$("#usertype").combobox("clear");
				$("#userrole").combobox("clear");
				$("#username").validatebox('disableValidation');
				$("#password").validatebox('disableValidation');
				$("#passwordcheck").validatebox('disableValidation');
				$("#fullname").validatebox('disableValidation');
				$("#phone").validatebox('disableValidation');
// 　　　			$('#usertype').combobox('setValues',);
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
			$('#userrole').combobox({
			    url : global_Path + '/role/getRoleList.json',
			    valueField:'roleId',
			    textField:'roleName',
			    panelHeight : 'auto',
			    loadFilter : function(row){
			    	return row;
				}
			});
			
			$('#roleDialog').dialog({   
			    title: '请选择用户角色 ',   
			    width: 500,   
			    height: 400,   
			    closed: true,   
			    cache: false,  
			    inline : true,
			    modal: true,
				buttons : [ {
					text : '保存',
					handler : add_user_role
				}, {
					text : '关闭',
					handler : function() {
						$("#roleDialog").dialog('close');
					}
				} ],
			onClose : function() {
				user_id="";
				user_role="";
// 				alert(user_role);
			}
			});
			
			
		});
		
		function add_user(){
			$('#addUserDialog').dialog('open');  
		}
		
		function save_user(){
			var check=check_validate();
			if(check){
				var json=getUseJson();
//    				alert(json);
				$.ajax({
					type : "post",
					async : false,
					url : global_Path+"/user/save",
					dataType : "json",
// 					contentType:'application/json;charset=UTF-8',
					data :{
						 userrole:$("#userrole").combobox('getValue'),
						 userid:$("#userid").val(),
						 username:$("#username").val(),
						 password:$("#password").val(),
						 fullname:$("#fullname").val(),
						 phone:$("#phone").val(),
						 usertype:$("#usertype").combobox('getValue'),
						 status:parseInt($("input[name='status']:checked").val())//得到radio选中的值
						
					},
					
					success : function(result) {
						$.messager.alert("提示",result.maessge);
						$('#addUserDialog').dialog('refresh');
						$('#addUserDialog').dialog('close');  
						$('#tables').datagrid('reload');
					}
				});
			}
		}
		//增加用户认证
		function check_validate(){
			var username=$("#username").val();
			if($.trim(username)==""){
				$.messager.alert("提示","请填写用户名");
				return false;
			}
			var password=$("#password").val();
			if($.trim(password)==""){
				$.messager.alert("提示","请填写用户密码");
				return false;
			}
			var passwordcheck=$("#passwordcheck").val();
			if($.trim(passwordcheck)==""){
				$.messager.alert("提示","请填写确认密码");
				return false;
			}
			if($.trim(passwordcheck)!=$.trim(password)){
				$.messager.alert("提示","两次输入密码不匹配");
				return false;
			}
			var fullname=$("#fullname").val();
			if($.trim(fullname)==""){
				$.messager.alert("提示","请填写用户实名");
				return false;
			}
			var userrole=$("#userrole").combobox('getValue');
			if($.trim(userrole)==""){
				$.messager.alert("提示","请选择用户角色");
				return false;
			}
			return true;
		}

		//得到表单输入
		function getUseJson(){
			var userid=$("#userid").val();
			var username=$("#username").val();
			var password=$("#password").val();
			var fullname=$("#fullname").val();
			var phone=$("#phone").val();
			var usertype=$("#usertype").combobox('getValue'); 
			var status=parseInt($("input[name='status']:checked").val());//得到radio选中的值
			var userObject={};
			userObject.userid=userid;
			userObject.username=username;
			userObject.password=password;
			userObject.fullname=fullname;
			userObject.phone=phone;
			userObject.usertype=usertype;
			userObject.status=status;
			//userObject.userrole=userrole;
			return $.toJSON(userObject);
		}
		//修改查询
		function modify_user(userid){
			$.ajax({
				type : "post",
				async : false,
				url : global_Path+"/user/findById/"+userid+".json",
				dataType : "json",
				success : function(result) {
					$("#userid").val(result.userid);
					$("#username").val(result.username);
					$("#password").val(result.password);
					$("#passwordcheck").val(result.password);
					$("#fullname").val(result.fullname);
					$("#phone").val(result.phone);
					$('#usertype').combobox('select', result.usertype);
					if(result.status==1){
						//$("#rd1").attr("checked",true);
					}else{
						$("#rd1").removeAttr("checked");
						$("#rd2").prop("checked",true);
						
					}
					$('#addUserDialog').dialog('open');  
				}
			});
			$.ajax({
				type : "post",
				async : false,
				url : global_Path+"/user/getUserRoleOne/"+userid+".json",
				dataType : "json",
				success : function(result) {
					var comdata=$('#userrole').combobox('getData');
					var flag=false;
					if(comdata.length>0){
					   for (var i=0;i<comdata.length;i++) {
		                     if (comdata[i].roleId == result.roleid) {
		                    	 flag=true;
		                    	// alert(comdata[i].id);
		                     }
		                 }
					}
					if(flag){
						$('#userrole').combobox('select', result.roleid);
					}else{
						$('#userrole').combobox('select','');
					}
// 					$('#userrole').combobox('select', result.roleid);
				}
			});
		}
		
		function del_user(userid,status){
			$.ajax({
				type : "post",
				async : false,
				url : global_Path+"/user/delete/"+userid+"/"+status+".json",
				dataType : "json",
				success : function(result) {
// 					alert(result);
					$('#tables').datagrid('reload');
				}
			});
		}
		
		function get_user_role(userid){
			var userrole;
			$.ajax({
				type : "post",
				async : false,
				url : global_Path+"/user/getUserRole/"+userid+".json",
				dataType : "json",
				success : function(result) {
  					userrole= result;
				}
			});
			return userrole;
		}
		
		var user_id;
		var user_role;
	    function add_role(userid){
	    	user_id=userid;
	    	user_role=get_user_role(userid);
// 	    	alert(user_role);
	    	getRoleDatagrid();
	    	$("#roleDialog").dialog('open');	
	    }
	    
	    //加载用户角色信息
	    function getRoleDatagrid(){
	    	$('#roleTable').datagrid({
	    		url : global_Path + '/role/page.json',
				method : 'post',
				fit : false,
				fitColumns : true,
				singleSelect : true,
				pagination : true,
				rownumbers : true,
				pageSize : 5,
				pageList : [ 5, 10, 15],
				remoteSort : false,
  				idField : 'roleid',
				loadMsg : '数据装载中......',
				queryParams: {
					status: 1,
				},
				columns : [ [ 
				{
					field : 'roleid',
					title : '请选择',
					width : 50,
					align : 'left',
					checkbox: true
				}, {
					field : 'rolename',
					title : '角色名称',
					width : 50,
					align : 'left'
				}
				]],
	    	
	    	onLoadSuccess:function(){
// 	    		alert(user_role);
             $('#roleTable').datagrid('unselectAll');
	    		for(var i in user_role){
	    			for(var j in user_role[i]){
	    				$('#roleTable').datagrid('selectRecord',user_role[i][j]);
	    			};
	    		}
//	    		$('#roleTable').datagrid('selectRecord','08676e62-7f38-472e-b87a-d2fed7ba4369');
//  	    		$('#roleTable').datagrid('selectRecord','[{roleid=085a6e62-7f38-472e-b87a-d2fd87ba4369}, {roleid=08676e62-7f38-472e-b87a-d2fed7ba4369}]');
	    	}
			});
	    	//分页参数配置
			$('#roleTable').datagrid('getPager').pagination({
				displayMsg : '当前显示从{from}到{to}共{total}记录',
				beforePageText : '第',
				afterPageText : '页 共 {pages} 页',
				onBeforeRefresh : function(pageNumber, pageSize) {
					$(this).pagination('loading');
					$(this).pagination('loaded');
				}
			});
	    }
	    
	    function add_user_role(){
	    	var Selections =$('#roleTable').datagrid('getSelections');
         	 var props = "";
         	 var roleids=[];
	    	for(var i in Selections){
	    		roleids.push(Selections[i].roleid);
// 	    	var Selection=Selections[i];
// 	    	   for(var p in Selection){
// 	    	    if(typeof(Selection[p])=="function"){
// 	    	    }else{
//   	    	     props += p + "=" + Selection[p] + ";  ";
// 	    	    }
// 	    	   }
	    	}
// 	    	  alert(roles);
// 	    	   alert(props);
            var json={};
            json.userid=user_id;
            json.roleids=roleids;
			$.ajax({
				type : "post",
				async : false,
				url : global_Path+"/user/addUserRole.json",
				data : $.toJSON(json),
				contentType:'application/json;charset=UTF-8',
				dataType : "json",
				success : function(result) {
					$.messager.alert("提示",result);
    					$("#roleDialog").dialog('close');
   					    $('#tables').datagrid('reload');
				}
			});
	    }
	
	    function usernameSearch(value){
	    	$('#tables').datagrid('load',{
				"username": value
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
				  <c:if test='<%=SessionUtils.checkAllowAccessButton("/user/create")%>'>  
				    <td align="left" width="60">
				    <a href="###" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="add_user()">创建用户</a>
					</td>
				</c:if>
					<td align="right" width="160">
						关键字搜索：
					</td>
					<td width="170">
						<input id="usernameSearch" class="easyui-searchbox" style="width:220px;"   data-options="searcher:usernameSearch" prompt="请输入用户名" />
					</td>
				</tr>
			</table>
		</div>
		
	<div id="addUserDialog">
      <table cellspacing="0" cellpadding="0" border="0" style="width: 100%">
        <tr align="left">
            <td>用户名:</td><td><input type="hidden" id="userid" />
            <input type="text" id="username" name="username"  /><span style="color: red">*</span></td>
        </tr>
        <tr align="left">
            <td>用户密码:</td><td><input type="password" id="password"  /><span style="color: red">*</span></td>
        </tr>
        <tr align="left">
            <td>确认密码:</td><td><input type="password" id="passwordcheck"  /><span style="color: red">*</span></td>
        </tr>
        <tr align="left">
                <td>用户实名:</td><td><input type="text" id="fullname" /><span style="color: red">*</span></td>
        </tr>
        <tr align="left">
                <td>手机号码:</td><td><input type="text" id="phone" /></td>
        </tr>
        <tr align="left">
            <td>用户类型:</td>
            <td>
              <input id="usertype" name="usertype"  style="height:30px"/>
            </td>
        </tr>

        <tr align="left">
            <td>用户状态:</td>	
            <td>
                <input type="radio" id="rd1" name="status" value="1" checked/>启用
                <input type="radio" id="rd2" name="status" value="0"/>禁用
            </td>
        </tr>
        <tr align="left">
            <td>角色:</td>
            <td>
              <input id="userrole" name="userrole"  style="height:30px"/><span style="color: red">*</span>
            </td>
        </tr>
        </table>
		</div>
		
	  <div id="roleDialog">
<!-- 	        <div cellspacing="0" cellpadding="0" border="0" style="width:100%" align="center"> -->
<!-- 				<a href="###" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="add_user_role()">确定</a> -->
<!-- 			</div> -->
       <table id="roleTable"></table>
        	
      </div>
</body>
</html>
