<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>我的账户</title>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/account.css"/>
	<link rel="stylesheet" href="<%=basePath%>/css/tenant.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/switchery/style.css"/>
	
	<script src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
	<script src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/commons.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/md5.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/password.js"></script>
	
	<script src="<%=request.getContextPath()%>/scripts/projectJs/myAccount.js"></script>
	<script type="text/javascript">
		var global_baseurl = '<%=basePath%>';
		var global_id = '<%=request.getAttribute("id")%>';
		var global_type = '<%=request.getAttribute("type")%>';
	</script>
</head>
<body>
	<div class="ky-content content-iframe myAccount">
		<div class="ky-panel">
			<div class="ky-panel-title">我的账户</div>
			<div class="ky-panel-content ">
				<form class="form-horizontal">
					<input type="hidden" id="id" >
					<input type="hidden" id="employeeId" >
					<div class="form-group">
					    <label class="control-label">账户名：</label>
					    <div class="col-sm-10">
					      <p class="form-control-static" id="account" ></p>
					    </div>
				  	</div>
					<div class="form-group">
					    <label class="control-label">姓名：</label>
					    <div class="col-sm-10">
					      <p class="form-control-static" id="name" ></p>
					    </div>
				  	</div>
				  	<div class="form-group">
					    <label class="control-label">性别：</label>
					    <div class="col-sm-10">
					      <p class="form-control-static" id="sex"></p>
					    </div>
				  	</div>
					<div class="form-group">
					    <label class="control-label">生日：</label>
					    <div class="col-sm-10">
					      <p class="form-control-static" id="birthdate"></p>
					    </div>
				  	</div>
					<div class="form-group">
					    <label class="control-label">所属门店：</label>
					    <div class="col-sm-10">
					      <p class="form-control-static" id="branch" ></p>
					    </div>
				  	</div>
				  	<div class="form-group">
					    <label class="control-label">工号：</label>
					    <div class="col-sm-10">
					      <p class="form-control-static" id="jobNumber"></p>
					    </div>
				  	</div>
					<div class="form-group">
					    <label class="control-label">职务：</label>
					    <div class="col-sm-10">
					      <p class="form-control-static" id="position" ></p>
					    </div>
				  	</div>
				  	<div class="form-group">
					    <label class="control-label">职务权限密码：</label>
					    <div class="col-sm-1">
					      <p class="form-control-static" id="paymentPassword" ></p>
					    </div>
					    <div class="col-sm-2">
					    </div>
					    <a href="javascript:void(0);" class="a-operate" id="rePaymentPwd">修改</a>
				  	</div>
				  	<div class="retrieveForm" style="display: none;" id="paymentPwd" data-type="rePwd">
				  		<div class="form-group">
    						<label class="col-sm-1 control-label" style="margin-top: 5px;font-size: 12px;text-align: right;width: 100px;padding-left: 0px;">原密码:</label>
    						<div class="col-sm-3" style="padding-left: 0px;">
								<input type="password" class="password" style="width: 342px;" id="r_paymentPassword_old"/>
							</div>
						</div>
						<div class="form-group">
    						<label class="col-sm-1 control-label" style="margin-top: 5px;font-size: 12px;text-align: right;width: 100px;padding-left: 0px;">密码：</label>
    						<div class="col-sm-3" style="padding-left: 0px;">
								<input type="password" class="password" id="r_paymentPassword"/>
							</div>
							<div class="col-sm-1">
								<span class="pwdAlt" style="left:0px;color:red"></span>
							</div>
						</div>
						<div class="form-group">
    						<label class="col-sm-1 control-label" style="margin-top: 5px;font-size: 12px;text-align: right;width: 100px;padding-left: 0px;">确认密码：</label>
    						<div class="col-sm-3" style="padding-left: 0px;">
								<input type="password" class="rePassword" id="r_rePaymentPassword"/>
							</div>
						</div>
						<div class="form-group">
    						<label class="col-sm-1 control-label" style="margin-top: 5px;font-size: 12px;text-align: right;width: 100px;padding-left: 0px;"></label>
    						<div class="btn-operate" id="modify-opera" >
								<button class="btn btn-cancel in-btn135" type="button">取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save in-btn135 " type="button" id="modifyPaymentPasswordSubmit" >保存</button>
							</div>	
						</div>
					</div>
				  	<div class="form-group">
					    <label class="control-label">企业名称：</label>
					    <div class="col-sm-10">
					      <p class="form-control-static" id="businessName" ></p>
					    </div>
				  	</div>
				  	<div class="form-group">
					    <label class="control-label">企业地址：</label>
					    <div class="col-sm-10">
					      <p class="form-control-static" id="address" ></p>
					    </div>
				  	</div>
				  	<div class="form-group">
					    <label class="control-label">固定电话：</label>
					    <div class="col-sm-10">
					      <p class="form-control-static" id="telephone" ></p>
					    </div>
				  	</div>
				  	
			  	</form>
			</div>
		</div>
		<div class="ky-panel">
			<div class="ky-panel-title clearfix">
				账户安全
				<div class="safeLevel">
					<label class="safeLevelNum"></label>
					<i class="icon-safeLevel"></i>
				</div>
			</div>
			<div class="ky-panel-content accountSafe">
				<div class="retrieveForm"  id="retrievePwd" style="display: none;"  data-type="rePwd">
					<div>
						<label class="title">修改密码</label>
					</div>
					<div class="form-group" style="position:relative;" >
    					<label class="col-sm-1 control-label" style="margin-top: 5px;font-size: 12px;text-align: right;width: 100px;padding-left: 0px;">原密码:</label>
    					<div class="col-sm-3" style="padding-left: 0px;">
							<input type="password" class="password" style="width: 342px;" id="r_password_old"/>
						</div>
					</div>
					<div class="form-group" style="position:relative;">
    					<label class="col-sm-1 control-label" style="margin-top: 5px;font-size: 12px;text-align: right;width: 100px;padding-left: 0px;">密码:</label>
    					<div class="col-sm-3" style="padding-left: 0px;">
							<input type="password" class="password"  id="r_password"/>
						</div>
						<div class="col-sm-1">
							<span class="pwdAlt" style="left:50px;color:red"></span>
						</div>
					</div>
					<div class="form-group" style="position:relative;">
    					<label class="col-sm-1 control-label" style="margin-top: 5px;font-size: 12px;text-align: right;width: 100px;padding-left: 0px;">确认密码:</label>
    					<div class="col-sm-3" style="padding-left: 0px;">
							<input type="password" class="rePassword" id="r_rePassword"/>
						</div>
					</div>
					<div class="form-group" style="position:relative;">
						<label class="col-sm-1 control-label" style="margin-top: 5px;font-size: 12px;text-align: right;width: 100px;padding-left: 0px;"></label>
    					<div class="btn-operate" id="modify-opera" >
							<button class="btn btn-cancel in-btn135" type="button">取消</button>
							<div  class="btn-division"></div>
							<button class="btn btn-save in-btn135 " type="button" id="modifyPasswordSubmit" >保存</button>
						</div>	
					</div>
				</div>
				<form class="form-horizontal">
					<div class="form-group">
					    <label class="control-label">密码</label>
					    <div class="col-sm-3"></div>
				     	<a href="javascript:void(0);" class="a-operate" id="rePwd">修改</a>
				  	</div>
					<div class="form-group">
					    <label class="control-label">邮箱</label>
					    <div class="col-sm-3">
					      <p class="form-control-static" id="emailSetStatusText">未设置</p>
					      <p class="form-control-static smaller" id="emailSetContentText" >设置邮箱可以大幅提升账号安全</p>
					    </div>
					    <a href="javascript:void(0);" class="a-operate" id="settingEmail">设置</a>
					    <a href="javascript:void(0);" class="a-operate" id="modifyEmail">修改</a>
				  	</div>
				  	
				  	<div class="retrieveForm margin15" style="display:none;" id="modifyEmailFirst" data-type="modifyEmail">
						<div>
							<label class="title">修改邮箱</label>
						</div>
						<div class="form-group" style="position:relative;">
    						<label class="col-sm-1 control-label" style="margin-top: 5px;font-size: 12px;text-align: right;width: 100px;padding-left: 0px;">账户密码：</label>
    						<div class="col-sm-3" style="padding-left: 0px;">
								<input type="password" class="password" style="width: 342px;" id="updateEmailPassword"/>
							</div>
						</div>
						<div class="form-group" style="position:relative;">
    						<label class="col-sm-1 control-label" style="margin-top: 5px;font-size: 12px;text-align: right;width: 100px;padding-left: 0px;"></label>
    						<div class="btn-operate" id="modify-opera" >
								<button class="btn btn-cancel in-btn135" type="button">取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save in-btn135 " type="button" id="modifyEmailFirstNext" >下一步</button>
							</div>	
						</div>
					</div>
				  	<div class="retrieveForm margin15" style="display:none;" id="retrieveEamil" data-type="settingEmail">
						<div>
							<label class="title">设置邮箱</label>
						</div>
						<div class="row">
							<label class="validateWayLabel">邮箱：</label>
							<input type="text" id="setEmail" class="validateWay"/>
						</div>
						<div class="row">
							<label>验证码：</label>
							<input type="text" class="checkCode" id="emailCheckCode"/>
							<button type="button" class="submitLogin getCheckCode" id="getEmailCheckCode">获取验证码</button>
						</div>
						<div class="row">
							<div class="btn-operate" id="modify-opera" >
								<button class="btn btn-cancel in-btn135" type="button">取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save in-btn135 " type="button" id="modifyEmailSubmit" >保存</button>
							</div>	
						</div>
					</div>
				  	
					<div class="form-group">
					    <label class="control-label">手机</label>
					    <div class="col-sm-3">
					      <p class="form-control-static" id="mobileSetStatusText">未设置</p>
					      <p class="form-control-static smaller" id="mobileSetContentText">>设置手机可以大幅提升账号安全</p>
					    </div>
					    <a href="javascript:void(0);" class="a-operate" id="settingPhone">设置</a>
					    <a href="javascript:void(0);" class="a-operate" id="modifyPhone">修改</a>
				  	</div>
				  		<div class="retrieveForm margin15" style="display:none;" id="modifyPhoneFirst" data-type="modifyPhone">
						<div>
							<label class="title">修改手机</label>
						</div>
						<div class="form-group" style="position:relative;">
    						<label class="col-sm-1 control-label" style="margin-top: 5px;font-size: 12px;text-align: right;width: 100px;padding-left: 0px;">账户密码：</label>
    						<div class="col-sm-3" style="padding-left: 0px;">
								<input type="password" class="password"  style="width: 342px;" id="updateMobilePassword"/>
							</div>
						</div>
						<div class="form-group" style="position:relative;">
    						<label class="col-sm-1 control-label" style="margin-top: 5px;font-size: 12px;text-align: right;width: 100px;padding-left: 0px;"></label>
    						<div class="btn-operate" id="modify-opera" >
								<button class="btn btn-cancel in-btn135" type="button">取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save in-btn135 " type="button" id="modifyPhoneFirstNext" >下一步</button>
							</div>	
						</div>
					</div>
				  	<div class="retrieveForm margin15" style="display:none;" id="retrievePhone" data-type="settingPhone">
						<div>
							<label class="title">设置手机</label>
						</div>
						<div class="row">
							<label class="validateWayLabel">手机：</label>
							<input type="text" class="validateWay" id="setMobile"/>
						</div>
						<div class="row">
							<label>验证码：</label>
							<input type="text" class="checkCode" id="mobileCheckCode"/>
							<button type="button" class="submitLogin getCheckCode" id="getMobileCheckCode">获取验证码</button>
						</div>
						<div class="row">
							<div class="btn-operate" id="modify-opera" >
								<button class="btn btn-cancel in-btn135" type="button">取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save in-btn135 " type="button" id="modifyMobileSubmit" >保存</button>
							</div>	
						</div>
					</div>
			  	</form>
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
								<div>验证码已发往邮箱<span id="checkCodeEmail"></span></div>
								<div>请<a href="javascript:goEmail()" class="goEmail" >前往查看</a>并将验证码填入输入框</div>
							</div>
							<div class="btn-operate">
								<button class="btn btn-save in-btn263" type="button" data-dismiss="modal">确定</button>
							</div>
						</div>
					</div>						
				</div>
			</div>
		</div>
	</div>
	<!--手机验证码提示 -->
	<div class="modal fade tenant-dialog in " id="mobileCheckCodeDiv" data-backdrop="static" >
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
								<div>验证码已发往手机<span id="checkCodeMobile"></span></div>
								<div>请将验证码填入输入框</div>
							</div>
							<div class="btn-operate">
								<button class="btn btn-save in-btn263" type="button" data-dismiss="modal">确定</button>
							</div>
						</div>
					</div>						
				</div>
			</div>
		</div>
	</div>
	<!--安全等级提示 -->
	<div class="modal fade tenant-dialog in " id="safeLevelTip" data-backdrop="static" >
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
								<div>设置密码+30，验证邮箱+30，绑定手机号+40，</div>
								<div>账户安全等着你拿100满分哦</div>
							</div>
							<div class="btn-operate">
								<button class="btn btn-save in-btn263" type="button" data-dismiss="modal">确定</button>
							</div>
						</div>
					</div>						
				</div>
			</div>
		</div>
	</div>
	 <div class="modal fade sendMsgState in " id="successPrompt"  >
			<div class="modal-dialog" style="width:310px;">
				<div class="modal-content" style="width:310px;">	
					<div class="modal-body pop-div-content">
						<p><i class="icon-success"></i></p>
						<p class="tipP"><label id="promptMsg">删除成功</label></p>
					</div>
				</div>
			</div>
		</div>
</body>
</html>
