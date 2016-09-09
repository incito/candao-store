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
	<title>修改手机号</title>
</head>
<body>
<div class="modal-dialog" style="width: 760px;">
	<div class="modal-content">
		<div class="modal-header">
			<div class="fl">修改手机号</div>
			<div class="fr close-win" data-dismiss="modal">x</div>
		</div>
		<div class="modal-body">
			<div class="row">
				<div class="col-md-7">
					<form action="">
						<div class="form-group form-group-base">
							<span class="form-label">手机号:</span>
							<input value="" name="phone"  type="text" class="form-control" autocomplete="off">
						</div>
						<div class="form-group form-group-base" >
							<span class="form-label f-fl">验证码:</span>
							<input value="" name="phone"  type="text" class="form-control f-fl" autocomplete="off" style="width: 320px; float: left; margin-right: 20px;">
							<button class="btn-default btn-lg btn-base btn-yellow f-fl">发送</button>
						</div>
						<div class="form-group form-group-base" style="margin-top:120px;">
							<button class="btn-default btn-lg btn-base btn-base-sm btn-yellow" style="width: 100%;">保存修改</button>
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
</body>
</html>