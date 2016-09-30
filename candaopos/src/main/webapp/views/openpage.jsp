<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8"/>
<meta name="viewport" content="width=device-width,initial-scale=1, user-scalable=no, minimum-scale=1.0,maximum-scale=1.0"/>
<!-- 让 IE 浏览器运行最新的渲染模式下 -->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- 让部分国产浏览器默认采用高速模式渲染页面 -->
<meta name="renderer" content="webkit">
<title>确认开业</title>
<link rel="stylesheet" href="../tools/bootstrap-3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" href="../css/common.css">
<link rel="stylesheet" href="../css/login.css">
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="../scripts/jquery-3.1.0.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="../tools/bootstrap-3.3.5/js/bootstrap.min.js"></script>
<script src="../scripts/common.js"></script>
<style type="text/css">
	article{
		background: #FFFFFF;
		padding: 200px 10%;
		top: 0;
	}
	.content{
		height: 270px;
	  	width: 80%;
	  	max-width: 700px;
	  	min-width: 560px;
	  	margin: auto auto;
	  	border: 1px solid white;
	  	padding: 50px;
	}
	.font{
		font-size: 32px;
		font-family: 黑体;
		font-weight: bold;
		text-align: center;
	}
	.btn{
		width: 240px;
  		height: 50px;
  		font-size: 24px;
  		color: black;
  		border-radius: 4px;
  		border: 1px solid rgb(189, 188, 188);
  		background: #fff;
	}
	.btn:hover{
		background: #F68D70;
		opacity: 0.5;
		color: #fff;
	}
	.btn:active, .btn:active{
		background: #FF5803;
		opacity: 1;
		color: #fff;
	}
	.confirm-dialog .dialog-sm-header{
		height: 45px;
	}
	.confirm-dialog .modal-dialog{
		width: 300px;
		margin-top: 30px;
	}
	.confirm-dialog .modal-body{
		height: 450px;
	}
	.confirm-dialog .virtual-keyboard{
		width: 100%;
	}
	.login-form {
		width: 100%;
		padding-left: 0px;
	}
	.login-form .form-control {
	  	height: 42px;
	  	padding-left: 80px;
	}
	.login-form .form-group {
	  	margin: 8px auto;
	}
	.span-user{
		line-height: 3;
	}
	.virtual-keyboard ul{
		padding-left: 0;
	}
	.virtual-keyboard li{
		height: 50px;
	}
	.confirm-dialog .btn {
  		width: 125px;
  		font-size: 14px;
  	}
  	.confirm-dialog .btn-operate  {
  		float: left;
  		width: 100%;
  		margin-top: 20px;
  	}
</style>
<script type="text/javascript">
	$.ajax({
		url: _config.interfaceUrl.RestaurantOpened + '//'+utils.storage.getter('ipaddress')+'/0/',
		method: 'GET',
		dataType:'text',
		success: function(res){
			var res = JSON.parse(res.substring(12,res.length-3));
			if(res.Data === '1') {//开业
				window.location = "../views/login.jsp";
			}
		},
		error: function(){
			widget.modal.alert({
				cls: 'fade in',
				content:'<strong>获取当日结业信息失败</strong>',
				width:500,
				height:500,
				btnOkTxt: '',
				btnCancelTxt: '确定'
			});
		}
	})
</script>
</head>
<body>
	<article>
		<div class="content">
			<div class="font">财源滚滚，生意兴隆</div>
			<div style="text-align: center; margin-top: 8%;">
				<button class="btn" id="confirm-opening-btn">确认开业</button>
			</div>
		</div>
	</article>
	<div class="modal fade in confirm-dialog" id="mg-login-dialog" data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="dialog-sm-header">
	        		<div class="modal-title"></div>
	                <img src="../images/close-sm.png" class="img-close" data-dismiss="modal">
	            </div>
				<div class="modal-body" style="padding-top: 0px;">
					<div class="login-form">
						<form action="">
							<div class="form-group" style="margin-top: 8px;">
								<span class="span-user">员工编号:</span>
								<input id="manager_num" value="" name="manager_num" type="text" class="form-control x319 in" autocomplete="off" ><!-- readonly="readonly" -->
							</div>
							<div class="form-group" style="margin-top: 8px;">
								<span class="span-user">权限密码:</span>
								<input id="perm_pwd" value="" name="perm_pwd" type="password" class="form-control x319 in" autocomplete="off" ><!-- readonly="readonly" -->
							</div>
						</form>
					</div>
					<div class="virtual-keyboard">
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
					 <div class="btn-operate  ">
	                    <button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal">取消
	                    </button>
	                    <button class="btn btn-save in-btn135 J-submit" id="" type="button" onclick="toLogin()">确认
	                    </button>
	                </div>
				</div>
			</div>
		</div>
	</div>
	<script src="../scripts/common.js"></script>
	<script src="../scripts/openpage.js"></script>
</body>
</html>