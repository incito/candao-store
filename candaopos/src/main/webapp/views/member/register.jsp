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
	<title>会员注册</title>
	<link rel="stylesheet" href="../../tools/bootstrap-3.3.5/css/bootstrap.min.css">
	<link rel="stylesheet" href="../../css/common.css">
	<link rel="stylesheet" href="../../css/main.css">
	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="../../scripts/jquery-3.1.0.min.js"></script>
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script src="../../tools/bootstrap-3.3.5/js/bootstrap.min.js"></script>
	<script src="../../scripts/common.js"></script>
	<link rel="stylesheet" href="../../css/member.css">
	<script type="text/javascript" src="../../lib/jedate/jedate.min.js"></script>
	<link type="text/css" rel="stylesheet" href="../../lib/jedate/skin/jedate.css">
</head>
<body>
<header>
	<div class="fl">餐道</div>
	<div class="fr" onclick="goBack();" >返回</div>
</header>
<article>
	<div class="member-view"  style="width: 740px;margin:30px auto;">
			<div class="row">
				<div class="col-md-7">
						<div class="form-group form-group-base">
							<span class="form-label">手机号码:</span>
							<input value="" id="phone" name="phone"  type="text" class="form-control" autocomplete="off">
						</div>
						<div class="form-group form-group-base f-oh">
							<span class="form-label f-fl">验证码:</span>
							<input value="" name="phone" id="phoneCode" type="text" class="form-control f-fl" autocomplete="off" style="width: 320px; float: left; margin-right: 20px;">
							<button class="btn-default btn-lg btn-base btn-yellow f-fl btn-sendMsg" >发送</button>
						</div>
						<div class="form-group form-group-base" >
							<span class="form-label">姓名:</span>
							<input value="" id="nmae" name="name"  type="text" class="form-control" autocomplete="off">
						</div>
						<div class="form-group form-group-base" >
							<span class="form-label">姓别:</span>
							<div class="form-info" id="gender">
								<div class="radio-box">
									<label>
										<input type="radio" value="0" checked="checked">&nbsp;男
									</label>
									&nbsp;
									&nbsp;
									&nbsp;
									<label>
										<input type="radio"  value="1">&nbsp;女
									</label>
								</div>
							</div>

						</div>
						<div class="form-group form-group-base" >
							<span class="form-label">生日:</span>
							<input value="" id="birthday" name="birthday"  type="text" readonly="readonly" class="form-control" autocomplete="off">
						</div>
						<div class="form-group form-group-base" >
							<span class="form-label">密码:</span>
							<input value="" id="psd" name="pwd"  type="password" class="form-control" autocomplete="off">
						</div>
						<div class="form-group form-group-base" >
							<span class="form-label">确认密码:</span>
							<input value="" id="rpsd" name="repwd"  type="password" class="form-control" autocomplete="off">
						</div>
						<div class="form-group form-group-base" >
							<span class="form-label">实体会员卡:</span>
							<div class="form-info" style="padding-left:100px;">
								<button class="btn-default btn-lg btn-base">绑定实体卡</button>
								可绑定IC卡, ID卡, 磁条卡
							</div>
						</div>
						<div class="form-group form-group-base">
							<button class="btn-default btn-lg btn-base btn-base-flex2" onclick="goBack();">取消</button>
							<button class="btn-default btn-lg btn-base btn-base-flex2 btn-save">注册</button>
						</div>
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
	</article>
<footer>
	<div class="info J-sys-info"><span>店铺编号：</span><span class="branch-num">- -</span><span>&nbsp;登录员工：</span><span>&nbsp;<span class="user-info">- -</span></span><span>&nbsp;当前时间：</span><span class="time">- -</span><span>&nbsp;版本号：</span><span>1.01</span></div>
</footer>
<script src="../../scripts/member.js"></script>
<script>
	$(function(){
		//加载虚拟键盘组件
		widget.keyboard({
			target: '.virtual-keyboard-base'
		});

	});
	var start = {
		dateCell: '#birthday',//input选择框
		skinCell:"jedateorange",//橙色风格
		format: 'YYYY-MM-DD',
		festival:true,//是否显示节日
		maxDate: jeDate.now(0), //设定最大日期为当前日期
		choosefun: function(elem,datas){

		}
	};
	jeDate(start);
	var valicode=null;
	$('.btn-save').click(function () {
		member.register();
		return false//禁止表单提交
	})
	$('.btn-sendMsg').click(function () {
		var moblie=$.trim($('#phone').val());
		if(member.isPhoneNo(moblie)===true){
			if(member.isPhoneRepeat(moblie)===false){
				member.errorAlert('该手机号码已注册，请重新输入新的手机号码');
				return false
			}
			else {
				member.sendVerifyCode(moblie);
			}

		}
		else {
			member.errorAlert('请输入正确的手机号码')
		}
		return false//禁止表单提交
	});
</script>
</body>
</html>