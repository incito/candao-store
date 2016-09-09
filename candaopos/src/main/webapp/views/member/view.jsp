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
	<title>会员查询</title>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/bootstrap-3.3.5/css/bootstrap.min.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="<%=request.getContextPath()%>/scripts/jquery-3.1.0.min.js"></script>
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script src="<%=request.getContextPath()%>/tools/bootstrap-3.3.5/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/common.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/page.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/main.js"></script>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/member.css">
</head>
<body>


<header>
	<div class="fl">餐道</div>
	<div class="fr" onclick="goBack();" >返回</div>
</header>
<article>
	<div class="member-view"  style="width: 740px;margin:0 auto;">
		<div class="row">
			<div class="col-md-12">
				<div class="form-group form-group-base search-box mt20" >
					<input value="" name="name"  type="text" placeholder="输入会员卡号/手机号" class="form-control form-control-sm form-control-search" autocomplete="off">
					<button class="btn-base btn-yellow btn-base-sm btn-search">搜索</button>
				</div>
			</div>
		</div>

		<div class="block-shadow block-radius ">
			<ul class="member-info-list">
				<li>卡号：<span>827375738</span></li>
				<li>手机号码：<span>827375738</span></li>
				<li>姓名：<span>张晓东</span></li>
				<li>会员卡等级：<span>储值卡</span></li>
				<li>生日：<span>87/02/09</span></li>
				<li>性别：<span>女</span></li>
				<li>余额：<span>88.00</span></li>
				<li>积分：<span>88</span></li>
				<li>卡状态：<span>正常</span></li>
			</ul>
		</div>

		<ul class="member-op-list">
			<li><a href="./storge.jsp">会员储值</a></li>
			<li class="J-add-card">新增实体卡</li>
			<li  class="J-modify-base">修改基本信息</li>
			<li class="J-modify-pwd">修改消费密码</li>
			<li class="J-modify-card">修改卡号</li>
			<li class="J-modify-phone">修改手机号</li>
			<li class="J-report-loss">会员挂失</li>
			<li class="J-cancellation">会员注销</li>
		</ul>

		<div class="virtual-keyboard-box f-pr">
			<div class="virtual-keyboard-base virtual-keyboard-base-4" style="position: absolute;top:0;right:0px;">
				<ul>
					<li>1</li><li>2</li><li>3</li><li>←</li>
				</ul>
				<ul>
					<li>4</li><li>5</li><li>6</li>
				</ul>
				<ul>
					<li>7</li><li>8</li><li>9</li>
				</ul>
				<ul>
					<li>.</li><li>0</li><li>00</li>
				</ul>
				<div class="virtual-keyboard-ok">确定</div>
			</div>
		</div>
	</div>
</article>
<footer class="footer-min">
	<div class="info"><span>店铺编号：</span><span>0012</span><span>&nbsp;登录员工：</span><span>&nbsp;收银员(008)</span><span>&nbsp;当前时间：</span><span>2016-08-19 12:00:00</span><span>&nbsp;版本号：</span><span>1.01</span></div>
</footer>
<div class="modal fade in dialog-normal bg-gray" data-backdrop="static" id="modify-base-dialog" style="overflow: auto;"></div>
<div class="modal fade in dialog-normal bg-gray" data-backdrop="static" id="modify-pwd-dialog" style="overflow: auto;"></div>
<div class="modal fade in dialog-normal bg-gray" data-backdrop="static" id="modify-phone-dialog" style="overflow: auto;"></div>
<div class="modal fade in dialog-normal bg-gray" data-backdrop="static" id="modify-cancellation-dialog" style="overflow: auto;"></div>
<script>
	$(function(){
		$('.member-op-list li').on('click', function(){
			var me = $(this);
			if(me.hasClass('J-modify-base')) {
				$("#modify-base-dialog").load("<%=request.getContextPath()%>/views/member/modifyBase.jsp");
				$("#modify-base-dialog").modal("show");
			}

			if(me.hasClass('J-modify-phone')) {
				$("#modify-phone-dialog").load("<%=request.getContextPath()%>/views/member/modifyPhone.jsp");
				$("#modify-phone-dialog").modal("show");
			}

			if(me.hasClass('J-modify-pwd')) {
				$("#modify-pwd-dialog").load("<%=request.getContextPath()%>/views/member/modifyPwd.jsp");
				$("#modify-pwd-dialog").modal("show");
			}

			if(me.hasClass('J-cancellation')) {
				var tpl = '<strong>是否要注销[李提付]?</strong>'
				var cancellationModal = Modal.alert({
					cls: 'fade in',
					content:tpl,
					width:500,
					height:500,
					btnOkCb: function(){
						Modal.alert({
							cls: 'fade in',
							content:'<strong>注销成功</strong>',
							width:500,
							height:500,
							hasBtns: false
						});
						cancellationModal.hide();
					},
					btnCancelCb: function(){
					}
				});
			}

			if(me.hasClass('J-report-loss')) {
				var tpl = '<strong>是否要挂失[李提付]?</strong>'
				var lossModal = Modal.alert({
					cls: 'fade in',
					content:tpl,
					width:500,
					height:500,
					btnOkCb: function(){
						Modal.alert({
							cls: 'fade in',
							content:'<strong>挂失成功</strong>',
							width:500,
							height:500,
							hasBtns: false
						});
						lossModal.hide();
					},
					btnCancelCb: function(){
					}
				});
			}


			if(me.hasClass('J-add-card')) {
				var tpl =
						'<strong>修改卡号-请刷卡</strong>' +
						'<br/><br/>' +
						'<i>正在读取会员卡信息......</i>';
				var addCardModal = Modal.alert({
					title:'新增实体卡',
					cls: 'fade in',
					content:tpl,
					width:500,
					height:500,
					btnOkCb: function(){
					},
					btnCancelCb: function(){
					}
				});
			}

			if(me.hasClass('J-modify-card')) {
				var tpl =
						'<strong>修改卡号-请刷卡</strong>' +
						'<br/><br/>' +
						'<i>正在读取会员卡信息......</i>';
				var modifyCardModal = Modal.alert({
					title:'修改卡号',
					cls: 'fade in',
					content:tpl,
					width:500,
					height:500,
					btnOkCb: function(){
					},
					btnCancelCb: function(){
					}
				});
			}
		});

	})
</script>
</body>
</html>