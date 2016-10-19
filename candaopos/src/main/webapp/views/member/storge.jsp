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
	<title>会员储值</title>
	<link rel="stylesheet" href="../../tools/bootstrap-3.3.5/css/bootstrap.min.css">
	<link rel="stylesheet" href="../../css/common.css">
	<link rel="stylesheet" href="../../css/main.css">
	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="../../scripts/jquery-3.1.0.min.js"></script>
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script src="../../tools/bootstrap-3.3.5/js/bootstrap.min.js"></script>
	<script src="../../scripts/common.js"></script>
	<link rel="stylesheet" href="../../css/member.css">
</head>
<body>


<header>
	<div class="fl">餐道</div>
	<div class="fr" onclick="goBack();" >返回</div>
</header>
<article>
	<div class="member-view"  style="width: 740px;margin:0 auto;">
		<div class="row" style="display: none">
			<div class="col-md-12">
				<div class="form-group form-group-base search-box mt20" >
					<input value="" name="name"  type="text" placeholder="输入会员卡号/手机号" class="form-control form-control-sm form-control-search" autocomplete="off">
					<button class="btn-base btn-yellow btn-base-sm btn-search">搜索</button>
				</div>
			</div>
		</div>

		<div class="coupon-box">
			<div class="prev unclick">&lt;</div>
			<div class="coupon-cnt coupon-List" style="height: 116px;overflow:hidden;">
				<%--优惠列表list--%>
			</div>
			<div class="next unclick">&gt;</div>
		</div>

		<div class="pay-type-select">
			<ul class="cnt">
				<li class="active" ChargeType="0">现金</li>
				<li ChargeType="1">银行卡</li>
			</ul>
		</div>

		<div style="margin-top:90px"></div>

		<div class="pay-type-cnt">
			<div class="pay-type-cash">
				<div class="form-group form-group-nor" >
					<span class="form-label">手机号/会员卡号:</span>
					<div class="form-group-info">
						<input value="" name="repwd" placeholder="输入手机号/会员卡号" id="rechargeMoblie"  type="text" class="form-control"  >
					</div>
				</div>
				<div class="form-group form-group-nor" >
					<span class="form-label">充值金额:</span>
					<div class="form-group-info">
						<input value="" name="repwd" id="rechargeMoney"  placeholder="100.00" type="text" class="form-control" >
					</div>
				</div>
				<div class="form-group form-group-nor" >
					<span class="form-label">赠送:</span>
					<div class="form-group-info giveMoney">
						0
					</div>
				</div>
				<button class="btn-base btn-yellow btn-base-sm btn-Save">确定充值</button>
				<button class="btn-base btn-base-sm "onclick="goBack();">取消</button>
			</div>
		</div>


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
<footer>
	<div class="info J-sys-info"><span>店铺编号：</span><span class="branch-num">- -</span><span>&nbsp;登录员工：</span><span>&nbsp;<span class="user-info">- -</span></span><span>&nbsp;当前时间：</span><span class="time">- -</span><span>&nbsp;版本号：</span><span>1.01</span></div>
</footer>
<script type="text/javascript" src="../../scripts/member.js"></script>
<script>
	//加载虚拟键盘组件
	widget.keyboard({
		target: '.virtual-keyboard-base'
	});
	member.getCouponList();
	$('.pay-type-select li').click(function () {
		$(this).addClass('active').siblings('li').removeClass('active');
	});
	$('.btn-Save').click(function () {
		member.stored_value()
	})
	$('#rechargeMoney').on('input propertychange', function(){
		var rechargeMoney=$.trim($('#rechargeMoney').val());
		var presentvalue=$.trim($('.coupon-List .active').attr('presentvalue'));//赠送比例
		var dealvalue=$.trim($('.coupon-List .active').attr('dealvalue'));//满多少赠送
		if(member.ismoney(rechargeMoney)===true && presentvalue!=''&& dealvalue!=''){
			$('.giveMoney').text(parseInt(rechargeMoney/dealvalue)*presentvalue)
		}
		else {
			$('.giveMoney').text(0)
		}
	});
	var getUrlcardMember=utils.getUrl.get('cardMember');
	if(getUrlcardMember){
		$('#rechargeMoblie').val(getUrlcardMember).attr('readonly',true)
	}
	else {
		$('#rechargeMoblie').val('').attr('readonly',false)
	}
</script>
</body>
</html>