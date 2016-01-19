<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>登录</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/tenant.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/login.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/scripts/md5.js"></script>
<script type="text/javascript">
			var global_ctxPath = '<%=request.getContextPath()%>';
			var global_fromPath = '<%=request.getParameter("fromUrl") %>';
			var global_localPath = '<%=basePath%>';
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/commons.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/projectJs/login.js"></script>
<script type="text/javascript">
	$(function(){
		//获取验证码按钮
		$("#getCheckCode").click(function(){
			var validateType = $("#validateType").val();
			var validateWay = $("#validateWay").val();
			var dataObj = new Object();
			if(validateType=='1'){
				if(validateWay==null||validateWay==''){
					$("#validateWay").addClass("error").next(".pwdAlt").text("请输入正确的手机号");
					return false;
				}
				//发送手机验证码
				$.post(global_ctxPath+"/t_user/retrievePwdMobileIsExists",{mobile:validateWay}, function(existResult){
					var existData = $.parseJSON(existResult);
					if(existData.success){
						$("#checkCodeImage").modal("show");
						$("#checkCodeImage #valicodeImg").attr("src",global_ctxPath+"/t_user/retrievePwdMobilePageValicode"+"?"+Math.random());
						$("#checkCodeImage .errorTips").html("");
						$("#checkCodeImage #valicodeText").val("");
						$("#checkCodeImage #valicodeButton").unbind("click");
						$("#checkCodeImage #valicodeButton").click(function(){
							if($("#checkCodeImage #valicodeText").val()==null||$("#checkCodeImage #valicodeText").val()==''){
								$("#checkCodeImage .errorTips").html('请输入验证码');
								return false;
							}
							dataObj.valicode = $("#valicodeText").val();
							dataObj.mobile = validateWay;
							//发送手机验证码
							$.post(global_ctxPath+"/t_user/retrievePwdMobileValicode",dataObj, function(result){
								var data = $.parseJSON(result);
								if(data.success){
									//设置发送验证码按钮禁用，倒计时120秒
									$("#getCheckCode").removeClass('submitLogin').addClass('disabledbtn');
									$("#getCheckCode").attr("disabled","disbaled");
									countDownSendValicode(120);
									//清除提示信息
									$("#validateWay").removeClass("error").next(".pwdAlt").text("");
									//启用下一步按钮
									$("#goSecondButton").removeClass('disabledbtn').addClass('submitLogin');
									$("#goSecondButton").removeAttr("disabled");
									//
									$("#checkCodeImage").modal("hide");
									//弹出提示框
									$("#checkCode").modal("show");
									$("#checkCode #doSendSpan").text("手机："+validateWay);
									$("#checkCode #goEmail").hide();
									$("#checkCode #goMobile").show();
								}else{
									$("#checkCodeImage #valicodeText").addClass("error");
									$("#checkCodeImage .errorTips").html(data.msg);
									
									$("#checkCodeImage #valicodeImg").attr("src",global_ctxPath+"/t_user/retrievePwdMobilePageValicode?"+Math.random());
									$("#checkCodeImage #valicodeText").val("");
								}
							});
						});
					}else{
						if(existData.msg!=null){
							$("#validateWay").addClass("error").next(".pwdAlt").text(existData.msg);
						}else{
							$("#validateWay").addClass("error").next(".pwdAlt").text("请输入已注册的手机号");
						}
						return false;
					}
				});
			}else if(validateType=='2'){
				if(validateWay==null||validateWay==''){
					$("#validateWay").addClass("error").next(".pwdAlt").text("请输入正确的邮箱");
					return false;
				}
				dataObj.email = validateWay;
				//发送邮箱验证码
				$.post(global_ctxPath+"/t_user/retrievePwdEmailValicode",dataObj, function(result){
					var data = $.parseJSON(result);
					if(data.success){
						//设置发送验证码按钮禁用，倒计时120秒
						$("#getCheckCode").removeClass('submitLogin').addClass('disabledbtn');
						$("#getCheckCode").attr("disabled","disabled");
						countDownSendValicode(120);
						//清除提示信息
						$("#validateWay").removeClass("error").next(".pwdAlt").text("");
						//启用下一步按钮
						$("#goSecondButton").removeClass('disabledbtn').addClass('submitLogin');
						$("#goSecondButton").removeAttr("disabled");
						//弹出提示框
						$("#checkCode").modal("show");
						$("#checkCode #doSendSpan").text("邮箱："+validateWay);
						$("#checkCode #goEmail").show();
						$("#checkCode #goMobile").hide();
					}else{
						$("#validateWay").addClass("error").next(".pwdAlt").text(data.msg);
					}
				});
			}
		});
		//第一步到第二步按钮
		$("#goSecondButton").click(function(){
			var validateType = $("#validateType").val();
			var valicode = $("#valicode").val();
			if(valicode==null||valicode==''){
				$("#valicode").addClass("error").next(".pwdAlt").text("请输入验证码");
				return false;
			}
			var dataObj = new Object();
			dataObj.valicode = valicode;
			if(validateType=='1'){
				//验证手机验证码
				$.post(global_ctxPath+"/t_user/retrievePwdMobileVali",dataObj, function(result){
					var data = $.parseJSON(result);
					if(data.success){
						$("#div1").hide();
						$("#div2").show();
						$("#div3").hide();
						$("#step1").removeClass("current");
						$("#step2").addClass("current");
						$("#step3").removeClass("current");
						$("#valicode").removeClass("error").next(".pwdAlt").text("");
					}else{
						$("#valicode").addClass("error").next(".pwdAlt").text("验证码不正确");
					}
				});
			}else if(validateType=='2'){
				//验证邮箱验证码
				$.post(global_ctxPath+"/t_user/retrievePwdEmailVali",dataObj, function(result){
					var data = $.parseJSON(result);
					if(data.success){
						$("#div1").hide();
						$("#div2").show();
						$("#div3").hide();
						$("#step1").removeClass("current");
						$("#step2").addClass("current");
						$("#step3").removeClass("current");
						$("#valicode").removeClass("error").next(".pwdAlt").text("");
					}else{
						$("#valicode").addClass("error").next(".pwdAlt").text("验证码不正确");
					}
				});
			}
		});
		//第二步到第三步按钮
		$("#goThirdButton").click(function(){
			var validateType = $("#validateType").val();
			var validateWay = $("#validateWay").val();
			var valicode = $("#valicode").val();
			var newpassword = $("#r_password").val();
			var newpassword2 = $("#r_rePassword").val();
			
			if(newpassword==null||newpassword==''){
				$("#r_password").addClass("error").next(".pwdAlt").text("请输入密码");
				return false;
			}
			
			if(newpassword!=newpassword2){
				$("#r_rePassword").addClass("error").next(".pwdAlt").text("两次输入的密码不相同");
			}else{
				$("#r_rePassword").removeClass("error").next(".pwdAlt").text("");
				var url = global_ctxPath;
				var dataObj = new Object();
				dataObj.valicode = valicode;
				dataObj.password = hex_md5(newpassword2);
				if(validateType=='1'){
					dataObj.mobile = validateWay;
					url += "/t_user/retrievePwdMobile";
				}else if(validateType=='2'){
					dataObj.email = validateWay;
					url += "/t_user/retrievePwdEmail";
				}
				
				$.post(url,dataObj, function(result){
					var data = $.parseJSON(result);
					if(data.success){
						$("#div1").hide();
						$("#div2").hide();
						$("#div3").show();
						$("#step1").removeClass("current");
						$("#step2").removeClass("current");
						$("#step3").addClass("current");
						$("#r_rePassword").removeClass("error").next(".pwdAlt").text("");
						countDownClose(5);
					}else{
						if(data.msg!=null){
							$("#r_rePassword").addClass("error").next(".pwdAlt").text(data.msg);
						}else{
							$("#r_rePassword").addClass("error").next(".pwdAlt").text("操作失败");
						}
					}
				});
				
			}
		});
		//切换验证方式
		$(".validateType").change(function(){
			if($(this).val() == "1"){
				$("#div1 .validateWayLabel").text("手机号：");
			}else{
				$("#div1 .validateWayLabel").text("邮箱：");
			}
		}); 
		//所有输入框得到或失去焦点时，加上样式
		$("input").focus(function(){
			$(this).addClass("active");
		}).blur(function(){
			$(this).removeClass("active");
		});
		//输入密码框失去焦点时，验证两个密码是否相同
		$("#r_password").blur(function(){
			var $this = $(this);
			if($this.val() == ""){
				$this.addClass("error");
			}else{
				$this.removeClass("error");
			}
			var rePwd = $("#r_rePassword");
			if(rePwd.val() != "" && $this.val() != rePwd.val()){
				rePwd.addClass("error").next(".pwdAlt").text("两次输入的密码不相同");
			}else{
				rePwd.removeClass("error").next(".pwdAlt").text("");
			}
		});
		//输入密码框失去焦点时，验证两个密码是否相同
		$("#r_rePassword").blur(function(){
			var $this = $(this);
			if($this.val() == ""){
				$this.addClass("error");
			}else{
				var pwd = $("#r_password");
				if(pwd.val() != ""){
					if($this.val() != pwd.val()){
						$this.addClass("error").next(".pwdAlt").text("两次输入的密码不相同");
					}else{
						$this.removeClass("error").next(".pwdAlt").text("");
					}
				}
			}
		});
		//点击提示框确认按钮，隐藏提示框
		$("#checkCodeOk").click(function(){
			$("#checkCode").modal("hide");
		});
		//返回登录页按钮
		$("#backLogin").click(function(){
			window.location.href = global_ctxPath+"/login/index";
		});
	});
	//倒计时关闭当前页面
	function countDownClose(sec){
		$(".setTime").text(sec);
		if(sec==0){
			window.location.href = global_ctxPath+"/login/index";
			return false;
		}
		sec--;
		setTimeout('countDownClose('+sec+')',1000);
	}
	//倒计时发送验证码
	function countDownSendValicode(sec){
		$("#getCheckCode").text("已发送"+sec+"秒");
		if(sec==0){
			$("#getCheckCode").removeClass('disabledbtn').addClass('submitLogin');
			$("#getCheckCode").removeAttr("disabled");
			$("#getCheckCode").text("获取验证码");
			return false;
		}
		sec--;
		setTimeout('countDownSendValicode('+sec+')',1000);
	}
	/**
	 * 跳转邮箱地址
	 */
	function goEmail(){
		var email = $("#doSendSpan").text();
		var url = getEmailUrl(email);
		if(url!=null){
			window.open("http://"+url); 
		}else{
			alert("抱歉!未找到对应的邮箱登录地址，请自己登录邮箱查看邮件！");
		}
	}
	/**
	 * 重置验证码
	 */
	function resetValicode(){
		$("#checkCodeImage #valicodeImg").attr("src",global_ctxPath+"/t_user/retrievePwdMobilePageValicode?"+Math.random());
		$("#checkCodeImage .errorTips").html("");
		$("#checkCodeImage #valicodeText").val("");
	}
	/*
	* 跳转到登录页面
	*/
	function goLogin(){
		if(global_fromPath!=null&&global_fromPath!=''&&global_fromPath!='null'){
			window.location.href = global_fromPath+"/login/index";
		}else{
			window.location.href = global_localPath+"/login/index";
		}
	}
</script>
</head>
<body>
	<div class="ky-navbar ky-navbar-default">
		<div class="ky-navbar-header">
			<img alt="" src="../images/logo.png" />
			<p>餐道后台管理平台</p>
		</div>
	</div>
	<div class="tenant-btn btn-add">
		<a href="javascript:goLogin()" class="role" style="color: white;">登录</a>
	</div>
	<div class="ky-title"></div>
	<div class="ky-content">
		<h1 class="retrieveTitle">找回登录密码</h1>
		<div class="stepsWrap clearfix">
			<div class="stepsInner">
				<div class="steps current" id="step1">
					<span class="stepsNum">1</span>
					<label class="stepsTip">验证身份</label>
				</div>
				<div class="stepsLine">
					
				</div>
				<div class="steps" id="step2">
					<span class="stepsNum">2</span>
					<label class="stepsTip">重置密码</label>
				</div>
				<div class="stepsLine">
					
				</div>
				<div class="steps" id="step3">
					<span class="stepsNum">3</span>
					<label class="stepsTip">完成</label>
				</div>
			</div>
		</div>
		<!-- 第一步 -->
		<div class="retrieveForm" id="div1">
			<div class="row">
				<label>验证方式：</label>
				<select class="validateType" id="validateType">
					<option value="1">通过手机短信验证</option>
					<option value="2">通过邮箱验证</option>
				</select>
			</div>
			<div class="row" style="position:relative;">
				<label class="validateWayLabel">手机号：</label>
				<input type="text" class="validateWay" id="validateWay" />
				<span class="pwdAlt" style="color:red"></span>
			</div>
			<div class="row" style="position:relative;">
				<label>验证码：</label>
				<input type="text" class="checkCode" id="valicode"/>
				<span class="pwdAlt" style="color:red"></span>
				<button type="button" class="submitLogin getCheckCode" id="getCheckCode">获取验证码</button>
			</div>
			<div class="row">
				<label></label>
				<button type="button" class="submitLogin btnNext disabledbtn" id="goSecondButton" disabled="disabled">下一步</button>
			</div>
		</div>
		<!-- 第二步 -->
		<div class="retrieveForm" id="div2" style="display: none;">
			<div class="row" style="position:relative;">
				<label class="validateWayLabel">密码：</label>
				<input type="password" class="password" id="r_password"/>
				<span class="pwdAlt" style="color:red">密码长度6~16位，区分大小写</span>
			</div>
			<div class="row" style="position:relative;">
				<label class="validateWayLabel">确认密码：</label>
				<input type="password" class="rePassword" id="r_rePassword"/>
				<span class="pwdAlt" style="color:red"></span>
			</div>
			<div class="row">
				<label></label>
				<button type="button" class="submitLogin btnNext" id="goThirdButton">下一步</button>
			</div>
		</div>
		
		<!-- 第三步 -->
		<div class="retrieveForm" id="div3" style="display: none;">
			<div class="row">
				<span class="icon-repwdSuccess">新密码已生效</span>
			</div>
			<div class="row" style="text-align:center">
				<span class="setTime">5</span>
			</div>
			<div class="row">
				<label></label>
				<button type="button" class="submitLogin btnNext" id="backLogin">返回登录</button>
			</div>
		</div>
	</div>
	<!--验证码提示 -->
		<div class="modal fade tenant-dialog in " id="checkCode" data-backdrop="static" >
			<div class="modal-dialog" style="width: 360px;">
				<div class="modal-content">	
					<div class="modal-header tenantInfo">	
						提示		  
				        <img src="<%=request.getContextPath()%>/images/close.png" class="img-close" data-dismiss="modal"/>
				    </div>
					<div class="modal-body">
						<div class="row">
							<div class="allWrap">
								<div class="btn-operate">
									<div>验证码已发往<span id="doSendSpan"></span></div>
									<div id="goEmail">请<a href="javascript:goEmail()" class="goEmail">前往查看</a>,并将验证码填入输入框</div>
									<div id="goMobile">请前往查看,并将验证码填入输入框</div>
								</div>
								
								<div class="btn-operate">
									<button class="btn btn-save in-btn263" type="button" id='checkCodeOk'>确定</button>
								</div>
							</div>
						</div>						
					</div>
				</div>
			</div>
		</div>
		<!--图文验证码提示 -->
		<div class="modal fade tenant-dialog in " id="checkCodeImage" data-backdrop="static"  style="z-index:99900" >
			<div class="modal-dialog" style="width: 360px;padding-top:135px">
				<div class="modal-content">	
					<div class="modal-header tenantInfo">	
						提示		  
				        <img src="<%=request.getContextPath()%>/images/close.png" class="img-close" data-dismiss="modal">
				    </div>
					<div class="modal-body">
						<div class="row">
							<div class="allWrap">
								<div class="btn-operate">
									<div class="checkCodeTitle">请输入图中文字</div>
									<div class="checkCodeTextWrap clearfix" >
										<input type="hidden" name="account" />
										<input type="hidden" name="email" />
										<input type="hidden" name="mobile" />
										<input type="text" name="valicodeText" id="valicodeText" maxlength="25" value="" class="form-control checkCodeText">	
										<img id="valicodeImg" onclick="resetValicode()" />
									</div>
								</div>
								<div class="errorTips">
									
								</div>
								<div class="btn-operate">
									<button class="btn btn-save in-btn263" type="button" id="valicodeButton">确定</button>
								</div>
							</div>
						</div>						
					</div>
				</div>
			</div>
		</div>
</body>
</html>

