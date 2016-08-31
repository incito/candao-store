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
<title>订单</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/orderdish.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/order.css">
<script src="<%=request.getContextPath()%>/scripts/page.js"></script>
<script src="<%=request.getContextPath()%>/scripts/order.js"></script>
<script type="text/javascript">
</script>
</head>
<body>
	<div class="modal-dialog main-modal-dialog" id="order-modal"
		data-backdrop="static" >
		<div class="modal-content">
			<div class="modal-body">
				<header>
					<div class="fl">餐道</div>
					<div class="fr close-win" data-dismiss="modal">关闭</div>
				</header>
				<article>
					<div class="content">
						<input type="hidden" id="isopened" value="">
						<div class="left-div">
							<div class="order-info">
								<div>
									账单号：<span>E210234782737478</span>
								</div>
								<div>
									桌号：<span>002</span>&nbsp;&nbsp;&nbsp;&nbsp;人数：<span>6</span>
								</div>
							</div>
							<div class="dish-sel">
								<table class="table display-table sel-dish-table"
									id="order-dish-table">
									<thead>
										<tr>
											<th width="60%">菜品名称</th>
											<th width="20%">数量</th>
											<th>金额</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
							<hr class="lf-hr">
							<div class="total-amount">
								消费：￥<span id="amount">548</span>
							</div>
							<div class="preferential-sel">
								<table class="table display-table sel-dish-table"
									id="sel-preferential-table">
									<thead>
										<tr>
											<th width="60%">优惠方式</th>
											<th width="20%">数量</th>
											<th>金额(元)</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
							<hr class="discount-hr">
							<div class="discount-total-amount">
								优惠总额：￥<span id="discount-amount">40</span>
							</div>
							<hr class="lf-hr">
							<div class="total-amount">
								应收：￥<span id="should-amount">548</span>
							</div>
							<div class="operate-btns">
								<div>预结单</div>
								<div>反结</div>
								<div onclick="takeOrder()">点菜</div>
								<div>开钱箱</div>
								<div>更多</div>
							</div>
						</div>
						<div class="oper-div">
							<div class="dish-oper-btns btns">
								<div class="oper-btn prev-btn">
									<span class="glyphicon glyphicon-chevron-up"></span>
								</div>
								<div class="page-info">
									<span id="curr-page1">0</span>/<span id="pages-len1">0</span>
								</div>
								<div class="oper-btn next-btn">
									<span class="glyphicon glyphicon-chevron-down"></span>
								</div>
								<div class="oper-btn" id="add-dish">
									<span class="glyphicon glyphicon-plus"></span>
								</div>
								<div class="oper-btn" id="back-dish">
									<span class="glyphicon glyphicon-minus"></span>
								</div>
							</div>
							<div class="preferential-oper-btns btns">
								<div class="oper-btn prev-btn">
									<span class="glyphicon glyphicon-chevron-up"></span>
								</div>
								<div class="page-info">
									<span id="curr-page2">0</span>/<span id="pages-len2">0</span>
								</div>
								<div class="oper-btn next-btn">
									<span class="glyphicon glyphicon-chevron-down"></span>
								</div>
								<div class="oper-btn" id="del-pref">
									<span class="glyphicon glyphicon-minus"></span>
								</div>
								<div class="oper-btn" id="clear-pref">
									<span>C</span>
								</div>
							</div>
						</div>
						<div class="main-div">
							<div class="dish-type">
								<div class="nav-type-prev nav-pretype-prev">
									<span class="glyphicon glyphicon-chevron-left"></span>
								</div>
								<ul class="nav-types nav-pref-types"></ul>
								<div class="nav-type-next nav-type nav-pretype-next">
									<span class="glyphicon glyphicon-chevron-right"></span>
								</div>
							</div>
							<div class="preferentials">
								<div class="preferentials-content"></div>
								<div class="page-btns">
									<div class="page-btn prev-btn">
										<span class="glyphicon glyphicon-chevron-up"></span>
									</div>
									<div class="page-info">
										<span id="curr-page3">0</span>/<span id="pages-len3">0</span>
									</div>
									<div class="page-btn next-btn">
										<span class="glyphicon glyphicon-chevron-down"></span>
									</div>
								</div>
							</div>
							<hr style="border: 1px solid #D3D3D3;">
							<div class="tab-payment">
								<ul><li class="active" target="#cash">现金支付</li><li target="#bank-card">银行卡</li><li target="#membership-card">会员卡</li><li target="#this-card">挂账支付</li><li target="#pay-treasure">支付宝</li><li target="#wechat-pay">微信支付</li></ul>
							</div>
							<div class="pay-div">
								<!-- 现金支付 -->
								<div class="paytype-input cash" id="cash">
									<div>
										<span>金额：</span>
										<input type="text" class="form-control">
									</div>
								</div>
								<!-- 银行卡支付 -->
								<div class="paytype-input bank-card hide" id="bank-card">
									<div style="display: inline-flex;">
										<input type="text" class="form-control bank-type" placeholder="银行类型" disabled="disabled">
										<div class="selbank-btn">选择银行</div>
									</div>
									<div>
										<span>银行卡号:</span>
										<input type="text" class="form-control" >
									</div>
									<div>
										<span>金额:</span>
										<input type="text" class="form-control" >
									</div>
								</div>
								<!-- 会员卡支付 -->
								<div class="paytype-input membership-card hide"
									id="membership-card">
									<div style="display: inline-flex;">
										<input type="text" class="form-control card-number" placeholder="卡号">
										<div class="login-btn">登录</div>
									</div>
									<div>
										<span>刷卡金额:</span>
										<input type="text" class="form-control" >
									</div>
									<div>
										<span>使用积分:</span>
										<input type="text" class="form-control" >
									</div>
									<div>
										<span>会员密码:</span>
										<input type="text" class="form-control" >
									</div>
									<div><div class="register-btn">注册</div></div>
								</div>
								<!-- 挂账支付 -->
								<div class="paytype-input this-card hide" id="this-card">
									<div style="display: inline-flex;">
										<input type="text" class="form-control payment-unit" placeholder="挂账单位" disabled="disabled">
										<div class="sel-btn">选择</div>
									</div>
									<div>
										<span>挂账金额:</span>
										<input type="text" class="form-control" >
									</div>
								</div>
								<!-- 支付宝支付 -->
								<div class="paytype-input pay-treasure hide" id="pay-treasure">
									<div>
										<span>支付宝:</span>
										<input type="text" class="form-control" >
									</div>
									<div>
										<span>金额:</span>
										<input type="text" class="form-control" >
									</div>
								</div>
								<!-- 微信支付 -->
								<div class="paytype-input wechat-pay hide" id="wechat-pay">
									<div>
										<span>微信号:</span>
										<input type="text" class="form-control" >
									</div>
									<div>
										<span>金额:</span>
										<input type="text" class="form-control" >
									</div>
								</div>
								<div class="virtual-keyboard num-virtual-keyboard" id="num-keyboard">
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
										<li>.</li><li>0</li><li>00</li>
									</ul>
									<ul>
										<li>←</li><li onclick="changeKeyboard('letter')">字母</li><li class="ok-btn">确定</li>
									</ul>
								</div>
								<div class="virtual-keyboard letter-virtual-keyboard hide" id="letter-keyboard">
									<ul>
										<li>A</li><li>B</li><li>C</li><li>D</li><li>E</li><li>F</li>
									</ul>
									<ul>
										<li>G</li><li>H</li><li>I</li><li>J</li><li>K</li><li>L</li>
									</ul>
									<ul>
										<li>M</li><li>N</li><li>O</li><li>P</li><li>Q</li><li>R</li>
									</ul>
									<ul>
										<li>S</li><li>T</li><li>U</li><li>V</li><li>W</li><li>X</li>
									</ul>
									<ul>
										<li>Y</li><li>Z</li><li>←</li><li onclick="changeKeyboard('num')">数字</li><li class="ok-btn">确定</li>
									</ul>
								</div>
							</div>
						</div>
					</div>
				</article>
				<footer>
					<div class="info">
						<span>店铺编号：</span><span>0012</span><span>&nbsp;登录员工：</span><span>&nbsp;收银员(008)</span><span>&nbsp;当前时间：</span><span>2016-08-19
						12:00:00</span><span>&nbsp;版本号：</span><span>1.01</span>
					</div>
				</footer>
			</div>
		</div>
	</div>
	<!--开台 -->
	<div class="modal fade in open-dialog" data-backdrop="static" id="open-dialog">
	    <div class="modal-dialog">
	        <div class="modal-content">
	        	<div class="dialog-sm-header">
	        		<div class="modal-title">开台</div>
	                <img src="<%=request.getContextPath()%>/images/close-sm.png" class="img-close" data-dismiss="modal">
	            </div>
	            <div class="modal-body">
	            	<div style="padding: 13px; float: left;">
		            	<div class="hori-lf-div">
		            		<div>
		            			<span>服务员编号:</span>
		            			<input type="text" class="form-control">
		            		</div>
		            		<div>
		            			<span>桌号:</span>
		            			<input type="text" class="form-control tableno">
		            		</div>
		            		<div>
		            			<span>就餐人数(男):</span>
		            			<input type="text" class="form-control personnum">
		            		</div>
		            		<div>
		            			<span>就餐人数(女):</span>
		            			<input type="text" class="form-control personnum">
		            		</div>
		            		<div>
		            			<span>餐具数量:</span>
		            			<input type="text" class="form-control">
		            		</div>
		            	</div>
		            	<div class="hori-rt-div">
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
		            	</div>
	            	</div>
	            	<div class="age-type">
	            		<div>儿童</div><div>青年</div><div>中年</div><div>老年</div>
	            	</div>
	                <div class="btn-operate ">
	                    <button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal">取消
	                    </button>
	                    <button class="btn btn-save in-btn135" id="" type="button" onclick="confirmOpen()">确认开台
	                    </button>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
</body>
</html>