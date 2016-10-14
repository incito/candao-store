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
	<link rel="stylesheet" href="../tools/bootstrap-3.3.5/css/bootstrap.min.css">
	<link rel="stylesheet" href="../css/common.css">
	<link rel="stylesheet" href="../css/main.css">
	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="../scripts/jquery-3.1.0.min.js"></script>
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script src="../tools/bootstrap-3.3.5/js/bootstrap.min.js"></script>
	<script src="../scripts/common.js"></script>
	<link rel="stylesheet" href="../css/orderdish.css">
	<link rel="stylesheet" href="../css/order.css">
</head>
<body id="order" style="overflow: scroll">

<%
	String orderid = request.getParameter("orderid");
	String personnum = request.getParameter("personnum");
	String tableno =  java.net.URLDecoder.decode(request.getParameter("tableno") , "UTF-8");
%>
<input type="hidden" value="<%=orderid%>" name="orderid">
<input type="hidden" value="<%=personnum%>" name="personnum">
<input type="hidden" value="<%=tableno%>" name="tableno">
	<div id="order-modal"
		data-backdrop="static" >
		<header>
			<div class="fl">餐道</div>
			<div class="fr close-win" onclick="window.location.href = './main.jsp'">关闭</div>
		</header>
		<div class="content">
			<input type="hidden" id="isopened" value="">
			<div class="left-div">
				<div class="order-info">
					<div>
						账单号：<span class="J-order-id"><%=orderid%></span>
					</div>
					<div>
						桌号：<span class="J-table-no"><%=tableno%></span>&nbsp;&nbsp;&nbsp;&nbsp;人数：<span class="J-person-no"><%=personnum%></span>
					</div>
				</div>
				<div class="dish-sel">
					<table class="table display-table sel-dish-table"
						   id="order-dish-table">
						<thead>
						<tr>
							<th width="40%">菜品名称</th>
							<th width="20%">数量</th>
							<th>单位</th>
							<th>小计</th>
						</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div>
					<hr class="lf-hr">
					<div class="total-amount">
						消费：￥<span id="amount">0.00</span>
						<div class="tip"><hr class="lf-hr">小费：￥<span id="tip-amount">0.00</span></div>
					</div>
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
				<div>
					<hr class="discount-hr">
					<div class="discount-total-amount">
						优惠总额：￥<span id="discount-amount">40</span>
					</div>
				</div>
				<div>
					<hr class="lf-hr">
					<div class="total-amount">
						应收：￥<span id="should-amount">0.00</span>
					</div>
				</div>
				<div class="operate-btns" style="padding-left: 11px;">
					<div onclick="Order.printPay(1)">预结单</div>
					<div onclick="Order.takeOrder()">点菜</div>
					<div onclick="Order.openCash()">开钱箱</div>
					<div class="show-more">更多</div>
				</div>
				<div class="more-oper hide">
					<ul>
						<li id="backDishAll" onclick="Order.backDish(1)">整单退菜</li>
						<li id="reprintOrder" onclick="Order.printPay(3)">重印客用单</li>
						<li onclick="Order.cancelOrder()">取消订单</li>
						<li id="consumInfo" onclick="Order.consumInfo()">零头不处理</li>
					</ul>
				</div>
			</div>
			<div class="oper-div">
				<div class="dish-oper-btns btns">
					<div class="oper-btn btn prev-btn">
						<span class="glyphicon glyphicon-chevron-up"></span>
					</div>
					<div class="page-info">
						<span id="curr-page1">0</span>/<span id="pages-len1">0</span>
					</div>
					<div class="oper-btn btn next-btn">
						<span class="glyphicon glyphicon-chevron-down"></span>
					</div>
					<div class="oper-btn btn" id="back-dish">
						退菜
					</div>
					<div class="oper-btn btn disabled" id="weigh-dish">
						称重
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
					<button class="btn oper-btn disabled" id="del-pref">
						<span class="glyphicon glyphicon-minus"></span>
					</button>
					<button class="btn oper-btn disabled" id="clear-pref">
						<span>C</span>
					</button>
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
						<div class="form-group">
							<span>金额：</span>
							<input type="text" class="form-control J-pay-val" iptType="cash">
						</div>
						<div class="form-group the-change-div">
							找零：<span class="the-change-span">0.00</span>
						</div>
					</div>
					<!-- 银行卡支付 -->
					<div class="paytype-input bank-card hide" id="bank-card">
						<div class="form-group" style="display: inline-flex;">
							<input type="text" class="form-control bank-type" name="banktype" placeholder="银行类型" disabled="disabled">
							<button class="btn selbank-btn J-bank-sel">选择银行</button>
						</div>
						<div class="form-group">
							<span>银行卡号:</span>
							<input type="text" class="form-control">
						</div>
						<div class="form-group">
							<span>金额:</span>
							<input type="text" class="form-control J-pay-val"  iptType="bank">
						</div>
					</div>
					<!-- 会员卡支付 -->
					<div class="paytype-input membership-card hide"
						 id="membership-card">
						<div class="form-group" style="display: inline-flex;">
							<input type="text" class="form-control card-number" placeholder="卡号">
							<button class="btn login-btn disabled">登录</button>
						</div>
						<div class="form-group">
							<span>刷卡金额:</span>
							<input type="text" class="form-control  J-pay-val" iptType="member-cash">
						</div>
						<div class="form-group">
							<span>使用积分:</span>
							<input type="text" class="form-control J-pay-val" iptType="member-jf" >
						</div>
						<div class="form-group">
							<span>会员密码:</span>
							<input type="text" class="form-control" >
						</div>
						<!--div class="form-group"><button class="btn register-btn">注册</button></div-->
						<div class="form-group" style="color: #FF5803;">储值金额：<b id="StoreCardBalance"></b></div>
						<div class="form-group" style="color: #FF5803;">积分余额：<b id="IntegralOverall"></b></div>
					</div>
					<!-- 挂账支付 -->
					<div class="paytype-input this-card hide" id="this-card">
						<div class="form-group" style="display: inline-flex;">
							<input type="text" class="form-control payment-unit" placeholder="挂账单位" disabled="disabled">
							<button class="btn sel-btn J-selCompany" >选择</button>
						</div>
						<div class="form-group">
							<span>挂账金额:</span>
							<input type="text" class="form-control J-pay-val" iptType="debitAmount" >
						</div>
					</div>
					<!-- 支付宝支付 -->
					<div class="paytype-input pay-treasure hide" id="pay-treasure">
						<div class="form-group">
							<span>支付宝:</span>
							<input type="text" class="form-control" >
						</div>
						<div class="form-group">
							<span>金额:</span>
							<input type="text" class="form-control J-pay-val" iptType="alipay">
						</div>
					</div>
					<!-- 微信支付 -->
					<div class="paytype-input wechat-pay hide" id="wechat-pay">
						<div class="form-group">
							<span>微信号:</span>
							<input type="text" class="form-control" >
						</div>
						<div class="form-group">
							<span>金额:</span>
							<input type="text" class="form-control J-pay-val" ipttype="wpay" >
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
							<li>←</li><li class="btn-action" onclick="Order.changeKeyboard('letter')">字母</li><li class="btn-action ok-btn" onclick="Order.doSettlement()">确定</li>
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
							<li>Y</li><li>Z</li><li>←</li><li class="btn-action " onclick="Order.changeKeyboard('num')">数字</li><li class="btn-action ok-btn" onclick="Order.doSettlement()">确定</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<footer>
			<div class="info">
				<span>店铺编号：</span><span>0012</span><span>&nbsp;登录员工：</span><span>&nbsp;收银员(008)</span><span>&nbsp;当前时间：</span><span>2016-08-19
						12:00:00</span><span>&nbsp;版本号：</span><span>1.01</span>
			</div>
		</footer>
	</div>
	<!-- 填写优惠信息设置 -->
	<div class="modal fade default-dialog in input-num-dialog" id="coupnum-dialog"
	     data-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content">
	        	<div class="dialog-sm-header">
	        		<div class="modal-title">优惠券</div>
	                <img src="../images/close-sm.png" class="img-close"  onclick="closeConfirm('coupnum-dialog')">
	            </div>
	            <div class="modal-body">
	                <div class="dialog-sm-info">
	                	<input type="hidden" id="preferential">
	                	<input type="hidden" id="pref-disrate">
	                	<input type="hidden" id="pref-type">
						<input type="hidden" id="pref-num">

	                	<input type="hidden" id="pref-name">
	                	<input type="hidden" id="pref-price">

	                    <div class="form-group coupname">团购券</div>
	                    <div class="form-group">
	                    	<span class="inpt-span">使用数量:</span>
	                    	<input type="text"   onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"   class="form-control J-pref-ipt padding-left">
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
	                </div>
	                <div class="btn-operate">
	                    <button class="btn btn-cancel in-btn135" type="button" onclick="closeConfirm('coupnum-dialog')">取消
	                    </button>
	                    <button class="btn btn-save in-btn135"  type="button" onclick="Order.addPref(this)">确认
	                    </button>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>

	<!-- 赠菜 -->
	<div class="modal fade default-dialog in coupnum-cus-give" id="givedish-dialog"
		 data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="dialog-sm-header">
					<div class="modal-title">赠菜</div>
					<img src="../images/close-sm.png" class="img-close" onclick="closeConfirm('givedish-dialog')">
				</div>
				<div class="modal-body">
					<div class="dialog-sm-info">
						请选择赠菜:
					</div>
					<ul class="give-dish-list">
					</ul>
					<div class="btn-operate  ">
						<button class="btn btn-cancel in-btn135" onclick="closeConfirm('givedish-dialog')" type="button">取消
						</button>
						<button class="btn btn-save in-btn135" onclick="Order.addPref(this)" type="button" >确认
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 退菜 -->
	 <div class="modal fade default-dialog in " id="backfood-dialog"
	     data-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content">
	        	<div class="dialog-sm-header">
	        		<div class="modal-title">餐道</div>
	                <img src="../images/close-sm.png" class="img-close" onclick="closeConfirm('backfood-dialog')">
	            </div>
	            <div class="modal-body">
	            	<!-- 仅存在一个分类中-->
	                <div class="dialog-sm-info">
	               		<h6>退菜原因</h6>
	               		<div class="form-group"><div class="avoid">客户要求</div><div class="avoid">菜品质量</div><div class="avoid">用餐服务</div></div>
	               		<div class="form-group"><span class="inpt-span">其他退菜原因：</span><input type="text" id="backfood-reason" class="form-control padding-left"></div>
	                </div>
	                <div class="btn-operate  ">
	                    <button class="btn btn-cancel in-btn135" type="button" onclick="closeConfirm('backfood-dialog')">取消
	                    </button>
	                    <button class="btn btn-save in-btn135" type="button">确认
	                    </button>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
	<!-- 输入退菜原因 -->
	<div class="modal fade default-dialog input-dialog in " id="backreasoninput-dialog"
	     data-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content">
	        	<div class="dialog-sm-header">
	        		<div class="modal-title">餐道</div>
	                <img src="../images/close-sm.png" class="img-close" onclick="closeConfirm('backreasoninput-dialog')">
	            </div>
	            <div class="modal-body">
	            	<div class="fl ">其他退菜原因：</div>
	            	<div class="fr">还可以输入<span id="backreason-count">20</span>字</div>
	            	<textarea class="form-control" maxlength="20" rows="5" cols="80" id="backreason-inp"></textarea>
	            	<div class="btn-operate  ">
	                    <button class="btn btn-cancel in-btn135 clear-btn disabled J-clear" style="float: left;" type="button">清空
	                    </button>
	                    <div  style="text-align: right;">
	                    	<button class="btn btn-cancel in-btn135" type="button" onclick="closeConfirm('backreasoninput-dialog')">取消
		                    </button>
		                    <button class="btn btn-save in-btn135"  type="button">确认
		                    </button>
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
	 </div>
	<!-- 退菜数量 -->
	<div class="modal fade default-dialog in input-num-dialog" id="backfoodnum-dialog"
	 data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="dialog-sm-header">
					<div class="modal-title">退菜</div>
					<img src="../images/close-sm.png" class="img-close"  onclick="closeConfirm('backfoodnum-dialog')">
				</div>
				<div class="modal-body">
					<div class="dialog-sm-info">
						<div class="form-group">
							<span class="inpt-span">退菜数量:</span>
							<input type="text" id="backDishNumIpt"  onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"    class="form-control padding-left">
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
					</div>
					<div class="btn-operate">
						<button class="btn btn-cancel in-btn135" type="button" onclick="closeConfirm('backfoodnum-dialog')">取消
						</button>
						<button class="btn btn-save in-btn135"  type="button">确认
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade in dialog-normal bg-gray" data-backdrop="static" id="backfood-right" style="overflow: auto;"></div>
	 <!-- 称重-->
	<div class="modal fade in default-dialog input-num-dialog" id="weight-dialog"
	     data-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content">
	        	<div class="dialog-sm-header">
	        		<div class="modal-title">称重</div>
	                <img src="../images/close-sm.png" class="img-close" onclick="closeConfirm('weight-dialog')">
	            </div>
	            <div class="modal-body">
	            	<!-- 仅存在一个分类中-->
	                <div class="dialog-sm-info">
	                    <div class="form-group coupname"><span id="coup-name">称重菜品</span></div>
	                    <div class="form-group">
	                    	<span class="inpt-span">称重数量:</span>
	                    	<input type="text" class="form-control padding-left" id="weight-num">
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
	                </div>
	                <div class="btn-operate  ">
	                    <button class="btn btn-cancel in-btn135" type="button" onclick="closeConfirm('weight-dialog')">取消
	                    </button>
	                    <button class="btn btn-save in-btn135"  type="button" onclick="">确认
	                    </button>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
	<!-- 选择银行 -->
	<div class="modal fade in default-dialog" id="select-bank-dialog"
	     data-backdrop="static">
	    <div class="modal-dialog" style="width: 800px;">
	        <div class="modal-content">
	        	<div class="dialog-sm-header">
	        		<div class="modal-title">餐道</div>
	                <img src="../images/close-sm.png" class="img-close" data-dismiss="modal">
	            </div>
	            <div class="modal-body">
	            	<div style="font-size: 20px;font-weight: bold;">请选择银行</div>
	            	<div class="bank-icon">
	            	</div>
	                <div class="btn-operate  ">
	                    <button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal">取消
	                    </button>
	                    <button class="btn btn-save in-btn135" id="" type="button">确认
	                    </button>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>

	<!-- 挂账单位 -->
	<div class="modal fade in default-dialog" id="selCompany-dialog"
		 data-backdrop="static">
		<div class="modal-dialog" style="width: 800px;">
			<div class="modal-content">
				<div class="dialog-sm-header">
					<div class="modal-title">挂账单位</div>
					<img src="../images/close-sm.png" class="img-close" data-dismiss="modal">
				</div>
				<div class="modal-body">
					<div class="form-group search-btns">
						<div class="search-btn">A</div>
						<div class="search-btn">B</div>
						<div class="search-btn">C</div>
						<div class="search-btn">D</div>
						<div class="search-btn">E</div>
						<div class="search-btn">F</div>
						<div class="search-btn">G</div>
						<div class="search-btn">H</div>
						<div class="search-btn">I</div>
						<div class="search-btn">J</div>
						<div class="search-btn">K</div>
						<div class="search-btn">L</div>
						<div class="search-btn">M</div>
						<div class="search-btn">N</div>
						<div class="search-btn">O</div>
						<div class="search-btn">P</div>
						<div class="search-btn">Q</div>
						<div class="search-btn">R</div>
						<div class="search-btn">S</div>
						<div class="search-btn">T</div>
						<div class="search-btn">U</div>
						<div class="search-btn">V</div>
						<div class="search-btn">W</div>
						<div class="search-btn">X</div>
						<div class="search-btn">Y</div>
						<div class="search-btn">Z</div>
					</div>
					<div class="form-group search J-search">
						<span class="glyphicon glyphicon-search"></span> <input type="search" class="form-control" placeholder="输入首字母">
						<div class="btn-clear">C</div>
					</div>
					<ul class="campany-icon clearfix">
					</ul>
					<div class="page" id="J-company-pager"></div>
					<div class="btn-operate  ">
						<button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal">取消
						</button>
						<button class="btn btn-save in-btn135"  type="button">确认
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>


	<script type="text/javascript" src="../lib/md5.js"></script>
	<script src="../scripts/order.js"></script>
</body>
</html>