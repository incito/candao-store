<%@page import="com.candao.www.utils.SessionUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Welcome</title>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/css/easyui/metro/easyui.css" />
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/css/common/common.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/ztree/zTreeStyle.css"
	type="text/css" />
<script type="text/javascript"
	src="<%=request.getContextPath()%>/scripts/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/scripts/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/scripts/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript">
			var global_ctxPath = "<%=request.getContextPath()%>";
</script>
<script type="text/javascript">
	$(function() {
		$('#user_info_win').dialog({
			title : '个人信息',
			width : 250,
			height : 180,
			cache : false,
			modal : true,
			closed : true,
			inline : true,
			buttons : [ {
				text : '关闭',
				handler : function() {
					$('#user_info_win').dialog('close');
				}
			} ]
		});

		$('#modify_password_dialog').dialog({
			title : '修改密码',
			width : 300,
			height : 180,
			cache : false,
			modal : true,
			closed : true,
			inline : true,
			buttons : [ {
				text : '保存',
				handler : update_user_password
			}, {
				text : '关闭',
				handler : function() {
					$('#modify_password_dialog').dialog('close');
				}
			} ]
		});

	});

	function format_time(t) {
		return t >= 10 ? t : '0' + t;
	}

	function modify_user_info() {
		$.messager.progress();
		$.ajax({
			type : 'post',
			async : false,
			cache : false,
			url : global_ctxPath + '/current',
			dataType : 'json',
			success : function(data) {
				$.messager.progress('close');
				$('#username').val(data.username);
				$('#fullname').val(data.fullname);
				$('#phone').val(data.phone);
				$('#user_info_win').dialog('open');
			}
		});
	}

	function user_password_info() {
		$('#modify_password_dialog').dialog('open');
	}
	//增加用户认证
	function check_validate() {
		var password = $("#password").val();
		if ($.trim(password) == "") {
			alert("请填写新密码");
			return false;
		}
		var passwordcheck = $("#passwordcheck").val();
		if ($.trim(passwordcheck) == "") {
			alert("请填写确认密码");
			return false;
		}
		if ($.trim(passwordcheck) != $.trim(password)) {
			alert("两次输入密码不匹配");
			return false;
		}
		return true;
	}

	function update_user_password() {
		var check = check_validate();
		if (check) {
			$.ajax({
				type : 'post',
				async : false,
				url : global_ctxPath + '/user/updatePassword',
				data : {
					userid : $('#userid').val(),
					password : $('#password').val()
				},
				dataType : 'text',
				success : function(data) {
					alert(data);
					if (data == '修改成功') {
						$('#modify_password_dialog').dialog('close');
					}
				}
			});
		}
	}
</script>
</head>
<body class="easyui-layout">
<!-- 	<input type="hidden" id="userid" -->
<%-- 		value="<%=SessionUtils.getCurrentUser().getUserid()%>" /> --%>
	<div data-options="region:'north',border:false" class="topbg"
		style="height: 70px; padding: 10px 10px 10px 10px">
		<ul>
			<li style="display: inline; width: 50%;"><a
				style="text-decoration: none; cursor: pointer;"
				href="<%=request.getContextPath()%>/login/index"><span
					style="font-size: 28px; color: #1E1E1E; font-weight: 700;">餐道后台管理平台</span></a>
			</li>
			<li style="display: inline; float: right;"><a
				href="javascript:void(0)" onclick="user_password_info()"> <img
					id="u14_img" class="img"
					src="<%=request.getContextPath()%>/images/u14.png" /></a> <span
				style="color: #666; font-weight: 400;">${currentUser.fullname}</span><a
				style="text-decoration: none; cursor: pointer;"
				href="<%=request.getContextPath()%>/login/logout"><span
					style="color: #666; font-weight: 400;">【退出】</span></a></li>
		</ul>
	</div>
	<div
		data-options="region:'west',title:'所有菜单',split:false,collapsible:true"
		style="width: 200px; overflow-x: hidden;">
		<menus:leftMenu />
	</div>
	<div data-options="region:'center',border:false,cache:false">
		<iframe id="mainframe" name="mainframe" src="" frameborder="0"
			height="100%" width="100%" allowTransparency="true" align="center"
			scrolling="auto"></iframe>
	</div>
	<div id="user_info_win" class="info">
		<table style="padding: 5px;">
			<tr>
				<th>登陆名:</th>
				<td><input id="username" type="text" readonly="readonly" /></td>
			</tr>
			<tr>
				<th>用户姓名:</th>
				<td><input id="fullname" type="text" /></td>
			</tr>
			<tr>
				<th>联系电话:</th>
				<td><input id="phone" type="text" /></td>
			</tr>

		</table>
	</div>

	<div id="modify_password_dialog">
		<table style="padding: 5px;">
			<tr>
				<th>原始密码:</th>
				<td><input id="oldPassword" type="password" readonly="readonly"
					value="<%=SessionUtils.getCurrentUser().getPassword()%>" /></td>
			</tr>
			<tr>
				<th>新密码:</th>
				<td><input id="password" type="password" /><span
					style="color: red">*</span></td>
			</tr>
			<tr>
				<th>密码确认:</th>
				<td><input id="passwordcheck" type="password" /><span
					style="color: red">*</span></td>
			</tr>
		</table>
	</div>
</body>
</html>