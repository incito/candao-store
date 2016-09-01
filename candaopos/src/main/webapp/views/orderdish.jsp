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
<title>点菜</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/orderdish.css">
<script src="<%=request.getContextPath()%>/scripts/page.js"></script>
<script src="<%=request.getContextPath()%>/scripts/addDish.js"></script>
<script type="text/javascript">
</script>
</head>
<body>
	<div class="modal-dialog main-modal-dialog" id="adddish-modal" data-backdrop="static" >
		<div class="modal-content">
			<div class="modal-body">
				<header>
					<div class="fl">餐道</div>
					<div class="fr close-win" id="close-adddish-dialog" data-dismiss="modal">关闭</div>
				</header>
				<article>
					<div class="content">
						<div class="left-div">
							<div class="order-info">
								<div>
									账单号：<span>E210234782737478</span>
								</div>
								<div>
									桌号：<span>002</span>&nbsp;&nbsp;&nbsp;&nbsp;人数：<span>6</span>
								</div>
							</div>
							<div class="dish-info">
								<table class="table display-table sel-dish-table"
									id="sel-dish-table">
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
								消费：￥<span id="total-amount">0</span>
							</div>
						</div>
						<div class="oper-div">
							<div class="btns">
								<div class="oper-btn prev-btn">
									<span class="glyphicon glyphicon-chevron-up"></span>
								</div>
								<div class="page-info">
									<span id="curr-page">0</span>/<span id="pages-len">0</span>
								</div>
								<div class="oper-btn next-btn">
									<span class="glyphicon glyphicon-chevron-down"></span>
								</div>
								<div class="oper-btn" onclick="add()">
									<span class="glyphicon glyphicon-plus"></span>
								</div>
								<div class="oper-btn" onclick="reduct()">
									<span class="glyphicon glyphicon-minus"></span>
								</div>
								<div class="oper-btn" onclick="updateNum()">
									<span>数量</span>
								</div>
								<div class="oper-btn">
									<span>备注</span>
								</div>
							</div>
						</div>
						<div class="main-div">
							<div class="dish-type">
								<div class="nav-type-prev nav-dishtype-prev">
									<span class="glyphicon glyphicon-chevron-left"></span>
								</div>
								<ul class="nav-types nav-dish-types"></ul>
								<div class="nav-type-next nav-type nav-dishtype-next">
									<span class="glyphicon glyphicon-chevron-right"></span>
								</div>
							</div>
							<div class="search-btns">
								<div>A</div>
								<div>B</div>
								<div>C</div>
								<div>D</div>
								<div>E</div>
								<div>F</div>
								<div>G</div>
								<div>H</div>
								<div>I</div>
								<div>J</div>
								<div>K</div>
								<div>L</div>
								<div>M</div>
								<div>N</div>
								<div>O</div>
								<div>P</div>
								<div>Q</div>
								<div>R</div>
								<div>S</div>
								<div>T</div>
								<div>U</div>
								<div>V</div>
								<div>W</div>
								<div>X</div>
								<div>Y</div>
								<div>Z</div>
							</div>
							<div class="search">
								<span class="glyphicon glyphicon-search"></span> <input
									type="search" class="form-control" placeholder="输入菜品名">
								<div class="delsearch-btn">C</div>
							</div>
							<div class="dishes">
								<div class="dishes-content"></div>
								<div class="page-btns">
									<div class="page-btn prev-btn">
										<span class="glyphicon glyphicon-chevron-up"></span>
									</div>
									<div class="page-info">
										<span id="curr-page1">0</span>/<span id="pages-len1">0</span>
									</div>
									<div class="page-btn next-btn">
										<span class="glyphicon glyphicon-chevron-down"></span>
									</div>
								</div>
							</div>
							<div class="main-oper-btns">
								<div onclick="giveFood()">赠菜</div>
								<div onclick="allNote()">全单备注</div>
								<div onclick="clearSelected()">清空</div>
								<div class="place-order" onclick="placeOrder()">下单</div>
							</div>
						</div>
					</div>
				</article>
				<footer>
					<div class="info">
						<span>店铺编号：</span><span>0012</span><span>&nbsp;登录员工：</span><span>&nbsp;收银员(008)</span><span>&nbsp;当前时间：</span>
						<span>2016-08-19 12:00:00</span><span>&nbsp;版本号：</span><span>1.01</span>
					</div>
				</footer>
			</div>
		</div>
	</div>
	<!-- 清空确认框 -->
	<div class="modal fade dialog-sm confirm-dialog in " id="clear-confirm-dialog"
	     data-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content">
	        	<div class="dialog-sm-header">
	        		<div class="modal-title"></div>
	                <img src="<%=request.getContextPath()%>/images/close-sm.png" class="img-close" onclick="closeConfirm('clear-confirm-dialog')">
	            </div>
	            <div class="modal-body">
	            	<!-- 仅存在一个分类中-->
	                <div class="dialog-sm-info">
	                    <p class="p1">确定要清空已选菜品吗？
	                    </p>
	                </div>
	                <div class="btn-operate  ">
	                    <button class="btn btn-cancel in-btn135" type="button" onclick="closeConfirm('clear-confirm-dialog')">取消
	                    </button>
	                    <button class="btn btn-save in-btn135" id="" type="button" onclick="doClear()">确认
	                    </button>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
	<!-- 下单确认框 -->
	<div class="modal fade dialog-sm confirm-dialog in " id="placeorder-confirm-dialog"
	     data-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content">
	        	<div class="dialog-sm-header">
	        		<div class="modal-title"></div>
	                <img src="<%=request.getContextPath()%>/images/close-sm.png" class="img-close" onclick="closeConfirm('placeorder-confirm-dialog')">
	            </div>
	            <div class="modal-body">
	            	<!-- 仅存在一个分类中-->
	                <div class="dialog-sm-info">
	                    <p class="p1">餐台【<sapn id="tableno">002</sapn>】确定下单吗？
	                    </p>
	                </div>
	                <div class="btn-operate  ">
	                    <button class="btn btn-cancel in-btn135" type="button" onclick="closeConfirm('placeorder-confirm-dialog')">取消
	                    </button>
	                    <button class="btn btn-save in-btn135" id="" type="button" data-dismiss="modal" onclick="doPlaceOrder()">确认
	                    </button>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
	<!-- 是否选菜确认框 -->
	<div class="modal fade dialog-sm confirm-dialog in " id="nodish-confirm-dialog"
	     data-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content">
	        	<div class="dialog-sm-header">
	        		<div class="modal-title"></div>
	                <img src="<%=request.getContextPath()%>/images/close-sm.png" class="img-close" onclick="closeConfirm('nodish-confirm-dialog')">
	            </div>
	            <div class="modal-body">
	                <div class="dialog-sm-info">
	                    <p class="p1" id="confirm-msg">请先选择菜品。</p>
	                </div>
	                <div class="btn-operate  ">
	                    <button class="btn btn-save in-btn135" id="" type="button" onclick="closeConfirm('nodish-confirm-dialog')">确认
	                    </button>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
	<!-- 更新购物车单个菜品数量 -->
	<div class="modal fade dialog-sm confirm-dialog in " id="updatenum-dialog"
	     data-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content">
	        	<div class="dialog-sm-header">
	        		<div class="modal-title">菜品数量设置</div>
	                <img src="<%=request.getContextPath()%>/images/close-sm.png" class="img-close" onclick="closeConfirm('updatenum-dialog')">
	            </div>
	            <div class="modal-body">
	            	<!-- 仅存在一个分类中-->
	                <div class="dialog-sm-info">
	                    <div class="form-group dishname">菜品名称：<span id="dish-name"></span></div>
	                    <div class="form-group">
	                    	<span class="inpt-span">菜品数量:</span>
	                    	<input type="text" class="form-control">
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
	                    <button class="btn btn-cancel in-btn135" type="button" onclick="closeConfirm('updatenum-dialog')">取消
	                    </button>
	                    <button class="btn btn-save in-btn135" id="" type="button" onclick="">确认
	                    </button>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
</body>
</html>