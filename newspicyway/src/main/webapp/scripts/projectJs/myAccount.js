var global_getHeadUserUrl = 't_headuser/getUserById';//获取总店员工
var global_getBusinUserUrl = 'tenant/getUserById';//获取企业用户
var global_getEmployeeUserUrl = 't_employeeUser/getUserById';//获取门店员工

var global_getsafescoreurl = 't_user/computeSafeScore';
var global_updatePwdUrl = 't_user/updatePassword';

var global_setEmailValicode = 't_user/setEmailValicode';//设置邮箱验证码
var global_setEmail = 't_user/setEmail';//设置邮箱

var global_validatePassword = 't_user/validatePassword';//验证密码

var global_setMobileValicode = 't_user/setMobileValicode';//设置手机验证码
var global_setMobile = 't_user/setMobile';//设置手机
$(function(){
	init();
	initPassword();
	initSafeScore();
	//打开修改密码
	$("#rePwd").click(function(){
		$(this).parents(".form-group").hide();
		$("#retrievePwd").find(":text").val("");
		$("#retrievePwd").find(":password").val("");
		$("#retrievePwd").slideDown(function(){
			$("#retrievePwd").find(".pwdAlt").text("密码长度6~16位，区分大小写");//再显示文字
		});
	});
	//修改密码
	$("#modifyPasswordSubmit").click(function(){
		//该处需要验证非空
		var oldPassword = $("#r_password_old").val();
		if(oldPassword==null||oldPassword==''){
			sendMsg(false,"请输入密码！");
			return false;
		}
		var newpassword = $("#r_password").val();
		var newpassword2 = $("#r_rePassword").val();
		if(newpassword==null||newpassword==''){
			sendMsg(false,"请输入新密码！");
			return false;
		}
		if(newpassword!=newpassword2){
			sendMsg(false,"两次密码输入不一致！");
			return false;
		}
		var dataObj = new Object();
		dataObj.id = global_id;
		dataObj.oldPwd = hex_md5(oldPassword);
		dataObj.newPwd = hex_md5(newpassword);
		//提交修改密码
		$.post(global_baseurl+global_updatePwdUrl,dataObj, function(result){
			var data = $.parseJSON(result);
			if(data.success){
				var msg = "操作成功！";
				if(data.msg!=null&&data.msg!=''){
					msg = data.msg;
				}
				sendMsg(true,msg,function(){
					$("#retrievePwd").find(".pwdAlt").text("");//将文字清除掉
					$("#retrievePwd").slideUp(function(){
						$("#"+$(this).attr("data-type")).parent().show();
					});
					init();
				});
			}else{
				var msg = "操作失败！";
				if(data.msg!=null&&data.msg!=''){
					msg = data.msg;
				}
				sendMsg(false,msg);
			}
		});
	});
	//打开设置邮箱
	$("#settingEmail").click(function(){
		$(this).parents(".form-group").hide();
		$("#retrieveEamil").slideDown();
		$("#retrieveEamil").find(":text").val("");
		$("#retrieveEamil").find(":password").val("");
	});
	//获取邮箱验证码
	$("#getEmailCheckCode").click(function(){
		var setEmail = $("#setEmail").val();
		if(setEmail==null||setEmail==''){
			sendMsg(false,"请输入正确的邮箱");
			return false;
		}
		//发送邮箱验证码
		var dataObj = new Object();
		dataObj.email = setEmail;
		$.get(global_baseurl+global_setEmailValicode,dataObj, function(result){
			var data = $.parseJSON(result);
			if(data.success){
				$("#checkCodeEmail").text(setEmail);
				$("#checkCode").modal("show");
			}else{
				sendMsg(false,data.msg);
			}
		});
	});
	//设置邮箱
	$("#modifyEmailSubmit").click(function(){
		var setEmail = $("#setEmail").val();
		var emailCheckCode = $("#emailCheckCode").val();
		var dataObj = new Object();
		dataObj.email = setEmail;
		dataObj.valicode = emailCheckCode;
		
		//提交设置邮箱
		$.post(global_baseurl+global_setEmail+"/"+global_id,dataObj, function(result){
			var data = $.parseJSON(result);
			if(data.success){
				var msg = "操作成功！";
				if(data.msg!=null&&data.msg!=''){
					msg = data.msg;
				}
				sendMsg(true,msg,function(){
					$("#retrieveEamil").slideUp(function(){
						$("#"+$(this).attr("data-type")).parent().show();
						
					});
					init();
				});
			}else{
				var msg = "操作失败！";
				if(data.msg!=null&&data.msg!=''){
					msg = data.msg;
				}
				sendMsg(false,msg);
			}
		});
	});
	// 修改邮箱
	$("#modifyEmail").click(function(){
		$(this).parents(".form-group").hide();
		$("#modifyEmailFirst").slideDown();
		$("#modifyEmailFirst").find(":text").val("");
		$("#modifyEmailFirst").find(":password").val("");
	});
	// 修改邮箱下一步
	$("#modifyEmailFirstNext").click(function(){
		var pwd = $("#updateEmailPassword").val();
		if(pwd==null||pwd==''){
			sendMsg(false,"请输入密码！");
			return false;
		}
		var dataObj = new Object();
		dataObj.id = global_id;
		dataObj.password = hex_md5(pwd);
		//验证密码
		$.post(global_baseurl+global_validatePassword,dataObj, function(result){
			var data = $.parseJSON(result);
			if(data.success){
				$("#modifyEmailFirst").slideUp();
				$("#retrieveEamil").slideDown();
				$("#retrieveEamil").find(":text").val("");
				$("#retrieveEamil").find(":password").val("");
			}else{
				sendMsg(false,"密码不正确！",function(){});
			}
		});
	});
	// 设置手机
	$("#settingPhone").click(function(){
		$(this).parents(".form-group").hide();
		$("#retrievePhone").slideDown();
		$("#retrievePhone").find(":text").val("");
		$("#retrievePhone").find(":password").val("");
	});
	//获取手机验证码
	$("#getMobileCheckCode").click(function(){
		var setMobile = $("#setMobile").val();
		if(setMobile==null||setMobile==''){
			sendMsg(false,"请输入正确的 手机号");
			return false;
		}
		//发送邮箱验证码
		var dataObj = new Object();
		dataObj.mobile = setMobile;
		$.get(global_baseurl+global_setMobileValicode,dataObj, function(result){
			var data = $.parseJSON(result);
			if(data.success){
				$("#checkCodeMobile").text(setMobile);
				$("#mobileCheckCodeDiv").modal("show");
			}else{
				sendMsg(false,data.msg);
			}
		});
	});
	//设置手机
	$("#modifyMobileSubmit").click(function(){
		var setMobile = $("#setMobile").val();
		var mobileCheckCode = $("#mobileCheckCode").val();
		var dataObj = new Object();
		dataObj.mobile = setMobile;
		dataObj.valicode = mobileCheckCode;
		
		//提交设置手机
		$.post(global_baseurl+global_setMobile+"/"+global_id,dataObj, function(result){
			var data = $.parseJSON(result);
			if(data.success){
				var msg = "操作成功！";
				if(data.msg!=null&&data.msg!=''){
					msg = data.msg;
				}
				sendMsg(true,msg,function(){
					$("#retrievePhone").slideUp(function(){
						$("#"+$(this).attr("data-type")).parent().show();
					});
					init();
				});
			}else{
				var msg = "操作失败！";
				if(data.msg!=null&&data.msg!=''){
					msg = data.msg;
				}
				sendMsg(false,msg);
			}
		});
	});
	// 修改手机
	$("#modifyPhone").click(function(){
		$(this).parents(".form-group").hide();
		$("#modifyPhoneFirst").slideDown();
		$("#modifyPhoneFirst").find(":text").val("");
		$("#modifyPhoneFirst").find(":password").val("");
	});
	// 修改手机下一步
	$("#modifyPhoneFirstNext").click(function(){
		var pwd = $("#updateMobilePassword").val();
		if(pwd==null||pwd==''){
			sendMsg(false,"请输入密码！",function(){
			});
		}
		var dataObj = new Object();
		dataObj.id = global_id;
		dataObj.password = hex_md5(pwd);
		//验证密码
		$.post(global_baseurl+global_validatePassword,dataObj, function(result){
			var data = $.parseJSON(result);
			if(data.success){
				$("#modifyPhoneFirst").hide();
				$("#retrievePhone").show();
				$("#retrievePhone").find(":text").val("");
				$("#retrievePhone").find(":password").val("");
			}else{
				sendMsg(false,"密码不正确！",function(){});
			}
		});
	});
	//所有取消按钮事件，隐藏面板
	$(".accountSafe .btn-cancel").click(function(){
		$(this).parents(".retrieveForm").find(".pwdAlt").text("");//将文字清除掉
		$(this).parents(".retrieveForm").slideUp(function(){
			$("#"+$(this).attr("data-type")).parent().show();
		});
	});
	//所有输入框事件，获取焦点添加样式，失去焦点删除样式
	$("input").focus(function(){
		$(this).addClass("active");
	}).blur(function(){
		$(this).removeClass("active");
	});
	// 安全等级事件，弹出安全等级帮助
	$(".icon-safeLevel").click(function(){
		$("#safeLevelTip").modal("show");
	});
	//打开修改职务权限密码
	$("#rePaymentPwd").click(function(){
		$("#paymentPwd").find(":text").val("");
		$("#paymentPwd").find(":password").val("");
		$("#paymentPwd").slideDown(function(){
			$("#paymentPwd").find(".pwdAlt").text("密码长度6位，区分大小写");//再显示文字
		});
	});
	//取消修改职务权限密码，隐藏面板
	$("#paymentPwd .btn-cancel").click(function(){
		$(this).parents(".retrieveForm").find(".pwdAlt").text("");//清除文字
		$(this).parents(".retrieveForm").slideUp(function(){
			$("#"+$(this).attr("data-type")).parent().show();
		});
	});
	//修改职务权限密码
	$("#modifyPaymentPasswordSubmit").click(function(){
		//该处需要验证非空
		var oldPassword = $("#r_paymentPassword_old").val();
		if(oldPassword==null||oldPassword==''){
			sendMsg(false,"请输入密码！");
			return false;
		}
		if(oldPassword.length!=6){
			sendMsg(false,"密码长度不正确！");
			return false;
		}
		var newpassword = $("#r_paymentPassword").val();
		var newpassword2 = $("#r_rePaymentPassword").val();
		if(newpassword==null||newpassword==''||newpassword2==null||newpassword2==''){
			sendMsg(false,"请输入新密码！");
			return false;
		}
		if(newpassword.length!=6||newpassword2.length!=6){
			sendMsg(false,"密码长度不正确！");
			return false;
		}
		if(newpassword!=newpassword2){
			sendMsg(false,"两次密码输入不一致！");
			return false;
		}
		var dataObj = new Object();
		dataObj.id = $("#employeeId").val();
		dataObj.oldPwd = hex_md5(oldPassword);
		dataObj.newPwd = hex_md5(newpassword);
		//提交修改密码
		$.post(global_baseurl+"t_employeeUser/updatePaymentPassword",dataObj, function(result){
			var data = $.parseJSON(result);
			if(data.success){
				var msg = "操作成功！";
				if(data.msg!=null&&data.msg!=''){
					msg = data.msg;
				}
				sendMsg(true,msg,function(){
					$("#paymentPwd").find(".pwdAlt").text("");//清除文字
					$("#paymentPwd").slideUp(function(){
						$("#"+$(this).attr("data-type")).parent().show();
					});
					init();
				});
			}else{
				var msg = "操作失败！";
				if(data.msg!=null&&data.msg!=''){
					msg = data.msg;
				}
				sendMsg(false,msg);
			}
		});
	});
});

function init(){
	if(global_type=='01'){
		initBusinUser();
	}else if(global_type=='02'){
		initHeadUser();
	}else if(global_type=='03'){
		initEmployeeUser();
	}
}
/**
 * 初始化密码输入框
 */
function initPassword(){
	$('#r_password').password();
	$('#r_rePassword').password();
	$('#r_paymentPassword').password();
	$('#r_rePaymentPassword').password();
}
/**
 * 初始化门店员工
 */
function initEmployeeUser(){
	$.get(global_baseurl+global_getEmployeeUserUrl+"/"+global_id,"", function(result){
		var data = $.parseJSON(result).data;
		//隐藏其他项
		$("#businessName").parent().parent().hide();
		$("#address").parent().parent().hide();
		$("#telephone").parent().parent().hide();
		
		$("#id").val(data.user.id);
		$("#employeeId").val(data.id);
		$("#account").text(data.user.account);
		$("#name").text(data.user.name);
		if(data.sex==1){
			$("#sex").text("男");
		}else if(data.sex==0){
			$("#sex").text("女");
		}
		if(data.birthdate!=null&&data.birthdate!=''){
			$("#birthdate").text((new Date(parseInt(data.birthdate,10))).format("yyyy-MM-dd"));
		}
		$("#jobNumber").text(data.jobNumber);
		$("#position").text(data.position);
		if(data.paymentPassword!=null&&data.paymentPassword!=''){
//			//此处原先是根据职务权限密码长度，动态显示*长度的，现已取消
//			var psize = new String(data.paymentPassword).length;
//			var nt = '';
//			for(var i=0;i<psize;i++){
//				nt += '*';
//			}
			$("#paymentPassword").text("******");
		}else{
			$("#paymentPassword").parent().parent().hide();
		}
		
//		if(data.branchs!=null&&data.branchs!=''){
//			var branchText = '';
//			$.each(data.branchs,function(i, v){
//	    		if(branchText!=''){
//	    			branchText += ',';
//	    		}
//	    		branchText += v.branchName;
//	    	});
//			$("#branch").text(branchText);
//		}
		
		$("#branch").html(data.branchName);
		
		if(data.user.email!=null&&data.user.email!=''){
			$("#settingEmail").hide();
			$("#modifyEmail").show();
			$("#emailSetStatusText").html(hideStringCorePart(data.user.email,4,"@"));
			$("#emailSetContentText").html("已设置，可通过邮箱找回密码");
		}else{
			$("#settingEmail").show();
			$("#modifyEmail").hide();
			$("#emailSetStatusText").html("未设置");
			$("#emailSetContentText").html("设置邮箱可以大幅提升账号安全");
		}
		
		if(data.user.mobile!=null&&data.user.mobile!=''){
			$("#settingPhone").hide();
			$("#modifyPhone").show();
			$("#mobileSetStatusText").html(hideStringCorePart(data.user.mobile,4));
			$("#mobileSetContentText").html("已设置，可通过手机找回密码");
		}else{
			$("#settingPhone").show();
			$("#modifyPhone").hide();
			$("#mobileSetStatusText").html("未设置");
			$("#mobileSetContentText").html("设置手机可以大幅提升账号安全");
		}
	});
}
/**
 * 初始化总店用户
 */
function initHeadUser(){
	$.get(global_baseurl+global_getHeadUserUrl+"/"+global_id,"", function(result){
		var data = $.parseJSON(result).data;
		//隐藏其他项
		$("#businessName").parent().parent().hide();
		$("#address").parent().parent().hide();
		$("#telephone").parent().parent().hide();
		$("#position").parent().parent().hide();
		$("#paymentPassword").parent().parent().hide();
		$("#jobNumber").parent().parent().hide();
		$("#birthdate").parent().parent().hide();
		$("#sex").parent().parent().hide();
		
		$("#id").val(data.id);
		$("#account").text(data.account);
		$("#name").text(data.name);
		
//		//普通账户不需要显示门店信息
//		if(data.branchs!=null&&data.branchs!=''){
//			var branchText = '';
//			$.each(data.branchs,function(i, v){
//	    		if(branchText!=''){
//	    			branchText += ',';
//	    		}
//	    		branchText += v.branchName;
//	    	});
//			$("#branch").text(branchText);
//		}
		$("#branch").parent().parent().hide();
		
		if(data.email!=null&&data.email!=''){
			$("#settingEmail").hide();
			$("#modifyEmail").show();
			$("#emailSetStatusText").html(hideStringCorePart(data.email,4,"@"));
			$("#emailSetContentText").html("已设置，可通过邮箱找回密码");
		}else{
			$("#settingEmail").show();
			$("#modifyEmail").hide();
			$("#emailSetStatusText").html("未设置");
			$("#emailSetContentText").html("设置邮箱可以大幅提升账号安全");
		}
		
		if(data.mobile!=null&&data.mobile!=''){
			$("#settingPhone").hide();
			$("#modifyPhone").show();
			$("#mobileSetStatusText").html(hideStringCorePart(data.mobile,4));
			$("#mobileSetContentText").html("已设置，可通过手机找回密码");
		}else{
			$("#settingPhone").show();
			$("#modifyPhone").hide();
			$("#mobileSetStatusText").html("未设置");
			$("#mobileSetContentText").html("设置手机可以大幅提升账号安全");
		}
	});
}
/**
 * 初始化企业用户
 */
function initBusinUser(){
	$.get(global_baseurl+global_getBusinUserUrl+"/"+global_id,"", function(result){
		var data = $.parseJSON(result).data;
		//隐藏其他
		$("#branch").parent().parent().hide();
		$("#position").parent().parent().hide();
		$("#paymentPassword").parent().parent().hide();
		$("#jobNumber").parent().parent().hide();
		$("#birthdate").parent().parent().hide();
		$("#sex").parent().parent().hide();
		
		
		$("#id").val(data.id);
		$("#account").text(data.account);
		$("#name").text(data.name);
		
		if(data.sex==1){
			$("#sex").text("男");
		}else if(data.sex==0){
			$("#sex").text("女");
		}
		if(data.birthdate!=null&&data.birthdate!=''){
			$("#birthdate").text((new Date(parseInt(data.birthdate,10))).format("yyyy-MM-dd"));
		}
		$("#jobNumber").text(data.jobNumber);
		$("#position").text(data.position);
		
		$("#businessName").text(data.businessName);
		var businessAddress = '';
		if(data.province!=null&&data.province!=''){
			businessAddress += data.province;
		}
		if(data.city!=null&&data.city!=''){
			businessAddress += data.city;
		}
		if(data.region!=null&&data.region!=''){
			businessAddress += data.region;
		}
		if(data.address!=null&&data.address!=''){
			businessAddress += data.address;
		}
		$("#address").text(businessAddress);
		
		var telephone = '';
		if(data.areaCode!=null&&data.areaCode!=''){
			telephone += data.areaCode;
		}
		if(data.telephone!=null&&data.telephone!=''){
			telephone += '-'+data.telephone;
		}
		$("#telephone").text(telephone);
		
		if(data.email!=null&&data.email!=''){
			$("#settingEmail").hide();
			$("#modifyEmail").show();
			$("#emailSetStatusText").html(hideStringCorePart(data.email,4,"@"));
			$("#emailSetContentText").html("已设置，可通过邮箱找回密码");
		}else{
			$("#settingEmail").show();
			$("#modifyEmail").hide();
			$("#emailSetStatusText").html("未设置");
			$("#emailSetContentText").html("设置邮箱可以大幅提升账号安全");
		}
		
		if(data.mobile!=null&&data.mobile!=''){
			$("#settingPhone").hide();
			$("#modifyPhone").show();
			$("#mobileSetStatusText").html(hideStringCorePart(data.mobile,4));
			$("#mobileSetContentText").html("已设置，可通过手机找回密码");
		}else{
			$("#settingPhone").show();
			$("#modifyPhone").hide();
			$("#mobileSetStatusText").html("未设置");
			$("#mobileSetContentText").html("设置手机可以大幅提升账号安全");
		}
	});
}

/**
 * 初始化安全等级
 */
function initSafeScore(){
	//获取安全分数
	$.get(global_baseurl+global_getsafescoreurl+"/"+global_id,"", function(result){
		var data = $.parseJSON(result).data;
		$(".safeLevelNum").text("安全等级："+data);
	});
}

/**
 * 发送消息
 * @param isOk 是否成功
 * @param msg
 * @param callback
 * @returns
 */
function sendMsg(isOk,msg,callback){
	if(isOk){
		$("#successPrompt .modal-body i").removeClass("icon-fail").addClass("icon-success");
	}else{
		$("#successPrompt .modal-body i").removeClass("icon-success").addClass("icon-fail");
	}
	$("#successPrompt").find("#promptMsg").parent().html('<label id="promptMsg"></label>');
	var msgArray = msg.split("！");
	if(msgArray.length>1){
		for(var i=0;i<msgArray.length;i++){
			if(i==0){
				$("#successPrompt").find("#promptMsg").html(msgArray[0]+"！\n");
			}else if(i==msgArray.length-1){
				$("#successPrompt").find("#promptMsg").parent().append('<label>'+msgArray[i]+'</label>');
			}else{
				$("#successPrompt").find("#promptMsg").parent().append('<label>'+msgArray[i]+'！</label>');
			}
		}
	}else{
		$("#successPrompt").find("#promptMsg").html(msg);
	}
	$("#successPrompt").modal("show");
	if(callback!=null){
		window.setTimeout(callback, 1000);
		window.setTimeout('$("#successPrompt").modal("hide")', 1000);
	}else{
		window.setTimeout('$("#successPrompt").modal("hide")', 1000);
	}
}
/**
 * 跳转邮箱地址
 */
function goEmail(){
	var email = $("#checkCodeEmail").text();
	var url = getEmailUrl(email);
	if(url!=null){
		window.open("http://"+url); 
	}else{
		sendMsg(false,"抱歉!未找到对应的邮箱登录地址，请自己登录邮箱查看邮件！");
	}
}