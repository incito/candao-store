<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<meta name="viewport"
		  content="width=device-width,initial-scale=1, user-scalable=no, minimum-scale=1.0,maximum-scale=1.0" />
	<!-- 让 IE 浏览器运行最新的渲染模式下 -->
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<!-- 让部分国产浏览器默认采用高速模式渲染页面 -->
	<meta name="renderer" content="webkit">
	<title>修改消费密码</title>
</head>
<body>
<div class="modal-dialog" style="width: 760px;">
	<div class="modal-content">
		<div class="modal-header">
			<div class="fl">修改消费密码</div>
			<div class="fr close-win" data-dismiss="modal">x</div>
		</div>
		<div class="modal-body">
			<div class="row">
				<div class="col-md-7">
					<form action="">
						<div class="form-group form-group-base">
							<span class="form-label">手机号:</span>
							<input value="" name="phone" id="changPsd" type="text" readonly="readonly" class="form-control" autocomplete="off">
						</div>
						<div class="form-group form-group-base f-oh">
							<span class="form-label f-fl">验证码:</span>
							<input value="" name="phone" id="phoneCode" type="text" class="form-control f-fl" autocomplete="off" style="width: 320px; float: left; margin-right: 20px;">
							<button class="btn-default btn-lg btn-base btn-yellow f-fl btn-sendMsg" >发送</button>
						</div>
						<div class="form-group form-group-base">
							<span class="form-label">新密码:</span>
							<input value="" id="newPsd" maxlength="6" name="phone"  type="password" class="form-control" autocomplete="off">
						</div>
						<div class="form-group form-group-base">
							<span class="form-label">确认密码:</span>
							<input value="" id="rnewPsd" maxlength="6" type="password" class="form-control" autocomplete="off">
						</div>
						<div class="form-group form-group-base">
							<button class="btn-default btn-lg btn-base btn-base-sm btn-yellow btn-changSave" style="width: 100%;">保存修改</button>
						</div>
					</form>
				</div>
				<div class="col-md-5">
					<div class="virtual-keyboard-base" style="position: absolute;top:0;right:10px;">
						<ul>
							<li>1</li><li>2</li><li>3</li>
						</ul>
						<ul>
							<li>4</li><li>5</li><li>6</li>
						</ul>
						<ul>
							<li>7</li><li>8</li><li>9</li>
						</ul>
						<ul>
							<li>.</li><li>0</li><li>←</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	var valicode=null;
	$('#modify-pwd-dialog #changPsd').val(loadMember.mobile)
	$('#modify-pwd-dialog .btn-changSave').click(function () {
		member.changePassword();
		return false//禁止表单提交
	})
	$('#modify-pwd-dialog .btn-sendMsg').click(function () {
		member.sendVerifyCode($.trim($('#changPsd').val()));
		return false//禁止表单提交
	})
</script>
</body>
</html>