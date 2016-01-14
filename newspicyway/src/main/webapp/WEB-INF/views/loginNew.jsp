<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@page import="com.candao.common.utils.PropertiesUtils"%>
<%
	String isbranch = PropertiesUtils.getValue("isbranch");
	String host = PropertiesUtils.getValue("cloud.host");
	String webRoot = PropertiesUtils.getValue("cloud.webroot");
	String webPath = "http://" + host + "/"+ webRoot;
	String localPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title>登录</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/login.css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery.min.js"></script>
	<script type="text/javascript">
			var global_ctxPath = '<%=request.getContextPath()%>';
			var global_isbranch = '<%=isbranch %>';
			var global_cloudPath = '<%=webPath %>';
			var global_localPath = '<%=localPath %>';
	</script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/projectJs/login.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/md5.js"></script>
	<script type="text/javascript">
	$(function() {
		$("#j_username").focus().keydown(function(event) {
			if (event.which == 13) {
				$("#j_password_show").focus();
			}
		});
	
		$("#j_password_show").keydown(function(event) {
			if (event.which == 13) {
				submitForm();
				//$("#j_captcha").focus();
			}
		});
	
		/* $("#j_captcha").keydown(function(event) {
			if (event.which == 13) {
				submitForm();
			}
		}); */
	
		if (self != top) {
	
			window.parent.location.href = global_ctxPath + "/login/login";
		}
		var l="${j_username}";
		//如果账号输入框不为空，则聚焦到密码框
		if( $("#j_username").val() != ''){
			$("#j_password_show").focus();
		}
	});
	
	function changeValidate() {
		$("#validateCode").attr("src",
				global_ctxPath + "/login/captcha?abc=" + Math.random());
	}
	
	function newtrim(text) {
		var strTrim = text.replace(/(^\s*)|(\s*$)/g, "");
		strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, "");
		var strf = strTrim;
		strf = strf.replace(/(^\s*)|(\s*$)/g, "");
		strf = strf.replace(/^[\s　\t]+|[\s　\t]+$/, "");
		return strf;
	}
	
	function submitForm() {
		var unv = $('#j_username').val() != "";
		var j_password_show=$("#j_password_show").val();
		var password = $('#j_password').val();
		var pwv = $('#j_password_show').val() != "";
		
		if (!unv) {
			$('#j_username').focus().parent().addClass("error");
			return false;
		}else{
			$('#j_username').parent().removeClass("error");
		}
		if (!pwv) {
			$('#j_password_show').focus().parent().addClass("error");
			
			return false;
		}else{
			$('#j_password_show').parent().removeClass("error");
		}
		/* if (!cap) {
			$('#j_captcha').focus();
			return false;
		} */
		var aaa=hex_md5(j_password_show);
		$('#j_password').val(hex_md5(j_password_show));
		$("#j_loginform").submit();
	}
	
	function closeMsg() {
		$("#error_msg").hide();
	}
	/**
	* 跳转到找回密码页面
	*/
	function goRePwd(){
		if(global_isbranch=='Y'){
			window.location.href = global_cloudPath+"/t_user/retrievePwd?fromUrl="+global_localPath;
		}else{
			window.location.href = global_ctxPath+"/t_user/retrievePwd";
		}
	}
	</script>
</head>
<body>
	<div class="ky-navbar ky-navbar-default">
		<div class="ky-navbar-header">
			<img alt="" src="../images/logo.png"/>
			<p>餐道后台管理平台</p>
		</div>
	</div>
	<div class="ky-title"></div>
	<div class="ky-content">
		<img src="../images/bg.jpg" class="contentBj" alt="" width="1024px" height="362px"/>
		<div class="loginWrap main">
			<form method="post"
			action="${ pageContext.request.contextPath}/login/login"
			class="form-signin" id="j_loginform">
				<h2 class="header">登录</h2>
				<c:if test="${not empty message }">
					<div class="alert alert-warning"
						id="error_msg">
						<button type="button" class="close" data-dismiss="alert"
							aria-hidden="true" onclick="closeMsg();">&times;</button>
						${message }
					</div>
				</c:if>
				<div class="row">
					<div class="textWrap clearfix">
						<span class="icon-user"></span>
						<input type="text" name="username"
							id="j_username" class="input-block-level" placeholder="手机号/邮箱/账号"
							tabindex="1" onblur="this.value=newtrim(this.value);" value="${j_username}"/>
					</div>
				</div>
				<div class="row">
					<div class="textWrap clearfix">
						<span class="icon-pwd"></span>
						<input type="password"
							class="input-block-level" name="password_show" id="j_password_show"
							tabindex="2" placeholder="密码" />
						<input type="hidden"
							class="input-block-level" name="password" id="j_password" />
					</div>
				</div>
				<div class="row">
					<div class="forgetPwd">
						<a href="javascript:goRePwd()">忘记密码？</a>
					</div>
				</div>
				<div class="row">
					<button type="button" class="submitLogin" tabindex="3" onclick="submitForm();">登录</button>
				</div>
			</form>
		</div>
	</div>
	<div class="footer">
		<p><a href="###">关于餐道</a> | <a href="###">联系我们</a></p>
		<p>© 2015 candaochina.com 版权所有</p>
	</div>
</body>
</html>

