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
<link rel="stylesheet" href="../css/orderdish.css">
<script src="../scripts/page.js"></script>
<script src="../scripts/addDish.js"></script>
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
									账单号：<span class="J-order-id"></span>
								</div>
								<div>
									桌号：<span class="J-table-no"></span>&nbsp;&nbsp;&nbsp;&nbsp;人数：<span class="J-person-no"></span>
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
							<div>
								<hr class="lf-hr">
								<div class="total-amount">
									合计金额：￥<span id="total-amount">0</span>
								</div>
							</div>
							<div class="note-div hide">
								<hr class="lf-hr">
								<div class="total-amount">
									全单备注：<span id="order-note"></span>
								</div>
							</div>
							
						</div>
						<div class="oper-div">
							<div class="btns">
								<button class="btn oper-btn prev-btn">
									<span class="glyphicon glyphicon-chevron-up"></span>
								</button>
								<div class="page-info">
									<span id="curr-page">0</span>/<span id="pages-len">0</span>
								</div>
								<button class="btn oper-btn next-btn">
									<span class="glyphicon glyphicon-chevron-down"></span>
								</button>
								<button class="btn oper-btn disabled" onclick="add()">
									<span class="glyphicon glyphicon-plus"></span>
								</button>
								<button class="btn oper-btn disabled" onclick="reduct()">
									<span class="glyphicon glyphicon-minus"></span>
								</button>
								<button class="btn oper-btn disabled" onclick="updateNum()">
									<span>数量</span>
								</button>
								<button class="btn oper-btn disabled" onclick="singleNote()">
									<span>备注</span>
								</button>
							</div>
						</div>
						<div class="main-div">
							<div class="form-group dish-type">
								<div class="nav-type-prev nav-dishtype-prev">
									<span class="glyphicon glyphicon-chevron-left"></span>
								</div>
								<ul class="nav-types nav-dish-types"></ul>
								<div class="nav-type-next nav-type nav-dishtype-next">
									<span class="glyphicon glyphicon-chevron-right"></span>
								</div>
							</div>
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
								<span class="glyphicon glyphicon-search"></span> <input
									type="search" class="form-control" placeholder="输入菜品首字母">
								<div class="btn-clear">C</div>
							</div>
							<div class="form-group dishes">
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
								<button class="btn disabled gua-dan hide" onclick="guadan()">挂单</button>
								<button class="btn disabled give-dish" onclick="giveFood()">赠菜</button>
								<button class="btn disabled" onclick="allNote()">全单备注</button>
								<button class="btn disabled" onclick="clearSelected()">清空</button>
								<button class="btn disabled place-order" onclick="placeOrder()">下单</button>
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
	                <img src="../images/close-sm.png" class="img-close" onclick="closeConfirm('clear-confirm-dialog')">
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
	                <img src="../images/close-sm.png" class="img-close" onclick="closeConfirm('placeorder-confirm-dialog')">
	            </div>
	            <div class="modal-body">
	            	<!-- 仅存在一个分类中-->
	                <div class="dialog-sm-info">
	                    <p class="p1">餐台【<span id="tableno">002</span>】确定下单吗？
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
	<!-- 更新购物车单个菜品数量 -->
	<div class="modal fade default-dialog in " id="updatenum-dialog"
	     data-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content">
	        	<div class="dialog-sm-header">
	        		<div class="modal-title">菜品数量设置</div>
	                <img src="../images/close-sm.png" class="img-close" onclick="closeConfirm('updatenum-dialog')">
	            </div>
	            <div class="modal-body">
	            	<!-- 仅存在一个分类中-->
	                <div class="dialog-sm-info">
	                	<input type="hidden" id="dish-id" value="">
	                    <div class="form-group dishname">菜品名称：<span id="dish-name"></span></div>
	                    <div class="form-group">
	                    	<span class="inpt-span">菜品数量:</span>
	                    	<input type="text" class="form-control padding-left" id="num">
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
	                    <button class="btn btn-save in-btn135" id="" type="button" onclick="doUpdateNum()">确认
	                    </button>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
	<!-- 备注 -->
	<div class="modal fade default-dialog in " id="note-dialog"
	     data-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content">
	        	<div class="dialog-sm-header">
	        		<div class="modal-title">餐道</div>
	                <img src="../images/close-sm.png" class="img-close" onclick="closeConfirm('note-dialog')">
	            </div>
	            <div class="modal-body">
	            	<!-- 仅存在一个分类中-->
	                <div class="dialog-sm-info">
	                	<input type="hidden" id="dish-id" value="">
	                	<input type="hidden" id="note-type" value="">
	                	<input type="hidden" id="dish-type" value="">
	                    <div id="dish-info"><p class="p1"><span id="note-dishname"></span> <span id="note-price" style="margin-left: 100px;">0</span>元</p></div>
	                    <div id="taste" class="taste">
	                    	<h6>选择口味</h6>
	                    	<ul><li>口味1</li><li>口味2</li><li>口味3</li><li>口味4</li></ul>
	                    </div>
	               		<h6>忌口</h6>
	               		<div class="form-group"><div class="avoid">少辣</div><div class="avoid">少盐</div></div>
	               		<div class="form-group"><span class="inpt-span">其他忌口：</span><input type="text" id="dish-note" class="form-control padding-left" onclick="inputNote()"></div>
	                </div>
	                <div class="btn-operate  ">
	                    <button class="btn btn-cancel in-btn135" type="button" onclick="closeConfirm('note-dialog')">取消
	                    </button>
	                    <button class="btn btn-save in-btn135" id="" type="button" onclick="doNote()">确认
	                    </button>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
	<!--挂单 -->
	<div class="modal fade in default-dialog guadan-dialog" data-backdrop="static" id="guadan-dialog">
	    <div class="modal-dialog">
	        <div class="modal-content">
	        	<div class="dialog-sm-header">
	        		<div class="modal-title" style="text-align: center;">挂单</div>
	                <img src="../images/close-sm.png" class="img-close" onclick="closeConfirm('guadan-dialog')" >
	            </div>
	            <div class="modal-body">
	            	<div style=" display: inline-block;width: 100%;">
		            	<div class="col-xs-6" style="padding: 0;">
		            		<div class="form-group" style="display: inline-block;">
		            			<span class="inpt-span">订餐单位:</span>
		            			<input type="text" class="form-control" style="width: 80%; display: inline-block;">
		            			<button class="btn sel-btn" onclick="selPayCompany()">选择</button>
		            		</div>
		            		<div class="form-group">
		            			<span class="inpt-span">联系人:</span>
		            			<input type="text" class="form-control">
		            		</div>
		            		<div class="form-group">
		            			<span class="inpt-span">联系电话:</span>
		            			<input type="text" class="form-control">
		            		</div>
		            		<div class="btn-operate ">
			                    <button class="btn btn-cancel in-btn135" type="button" onclick="closeConfirm('guadan-dialog')" >取消
			                    </button>
			                    <button class="btn btn-save in-btn135" id="" type="button" onclick="">确认挂单
			                    </button>
	                		</div>
		            	</div>
		            	<div class="col-xs-6" style="float: right; padding: 0 11px;">
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
	            </div>
	        </div>
	    </div>
	</div>
	 <!-- 赠菜 -->
	 <div class="modal fade default-dialog in " id="givefood-dialog"
	     data-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content">
	        	<div class="dialog-sm-header">
	        		<div class="modal-title">餐道</div>
	                <img src="../images/close-sm.png" class="img-close" onclick="closeConfirm('givefood-dialog')">
	            </div>
	            <div class="modal-body">
	            	<!-- 仅存在一个分类中-->
	                <div class="dialog-sm-info">
	                	<input type="hidden" id="dish-id" value="">
	                	<input type="hidden" id="dish-type" value="">
	               		<h6>赠菜原因</h6>
	               		<div class="form-group"><span class="inpt-span">其他赠菜原因：</span><input type="text" id="givefood-reason" class="form-control padding-left" onclick="inputReason()"></div>
	                </div>
	                <div class="btn-operate  ">
	                    <button class="btn btn-cancel in-btn135" type="button" onclick="closeConfirm('givefood-dialog')">取消
	                    </button>
	                    <button class="btn btn-save in-btn135" id="" type="button" onclick="doGive()">确认
	                    </button>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
	<!-- 输入赠菜原因 -->
	<div class="modal fade default-dialog input-dialog in " id="reasoninput-dialog"
	     data-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content">
	        	<div class="dialog-sm-header">
	        		<div class="modal-title">餐道</div>
	                <img src="../images/close-sm.png" class="img-close" onclick="closeConfirm('reasoninput-dialog')">
	            </div>
	            <div class="modal-body">
	            	<div class="fl ">其他赠菜原因：</div>
	            	<div class="fr">还可以输入<span id="reason-count">20</span>字</div>
	            	<textarea class="form-control" maxlength="20" rows="5" cols="80" id="reason-inp" onkeyup="changeReaCount()"></textarea>
	            	<div class="btn-operate  ">
	                    <button class="btn btn-cancel in-btn135 clear-btn disabled" style="float: left;" type="button" onclick="clearReasonInput()">清空
	                    </button>
	                    <div style="text-align: right;">
	                    	<button class="btn btn-cancel in-btn135" type="button" onclick="closeConfirm('reasoninput-dialog')">取消
		                    </button>
		                    <button class="btn btn-save in-btn135" id="" type="button" onclick="changeReason()">确认
		                    </button>
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
	 </div>
	 <!-- 套餐 -->
	 <div class="modal fade default-dialog in " id="combodish-dialog"
	     data-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content">
	        	<div class="dialog-sm-header">
	        		<div class="modal-title">餐道</div>
	                <img src="../images/close-sm.png" class="img-close" onclick="closeConfirm('combodish-dialog')">
	            </div>
	            <div class="modal-body">
	            	<div class="fl dish-div">
	            		<!-- 仅存在一个分类中-->
		            	<div class="dishname" id="dishname">套餐</div>
		            	<div class="combo-group">
		            		<h4 class="group-title">凉菜（3选1）已选0</h4> 
		            		<div class="form-group">
		            			<div class="col-xs-7">开胃三拼(例)</div>
		            			<div class="col-xs-5"><input class="form-control num-inp" type="text"></div>
		            		</div>
		            		<div class="form-group">
		            			<div class="col-xs-7">川青口水鸡(例)</div>
		            			<div class="col-xs-5"><input class="form-control num-inp" type="text"></div>
		            		</div>
		            		<div class="form-group">
		            			<div class="col-xs-7">水晶猪耳(例)</div>
		            			<div class="col-xs-5"><input class="form-control num-inp" type="text"></div>
		            		</div>
		            		<h4 class="group-title">凉菜（3选1）已选0</h4> 
		            		<div class="form-group">
		            			<div class="col-xs-7">开胃三拼(例)</div>
		            			<div class="col-xs-5"><input class="form-control num-inp" type="text"></div>
		            		</div>
		            		<h4 class="group-title">凉菜（3选1）已选0</h4> 
		            		<div class="form-group">
		            			<div class="col-xs-7">开胃三拼(例)</div>
		            			<div class="col-xs-5"><input class="form-control num-inp" type="text"></div>
		            		</div>
		            		<h4 class="group-title">凉菜（3选1）已选0</h4> 
		            		<div class="form-group">
		            			<div class="col-xs-7">开胃三拼(例)</div>
		            			<div class="col-xs-5"><input class="form-control num-inp" type="text"></div>
		            		</div>
		            		<h4 class="group-title">凉菜（3选1）已选0</h4> 
		            		<div class="form-group">
		            			<div class="col-xs-7">开胃三拼(例)</div>
		            			<div class="col-xs-5"><input class="form-control num-inp" type="text"></div>
		            		</div>
		            		<h4 class="group-title">凉菜（3选1）已选0</h4> 
		            		<div class="form-group">
		            			<div class="col-xs-7">开胃三拼(例)</div>
		            			<div class="col-xs-5"><input class="form-control num-inp" type="text"></div>
		            		</div>
		            	</div>
		                <div class="dialog-sm-info">
		                	<input type="hidden" id="dish-id" value="">
		                	<input type="hidden" id="dish-type" value="">
		               		<h6 style="font-weight: bold; font-size: 16px">忌口</h6>
		               		<div class="form-group"><div class="avoid">少辣</div><div class="avoid">少盐</div></div>
		               		<div class="form-group"><span class="inpt-span">其他忌口：</span><input type="text" id="combodish-note" class="form-control padding-left" onclick="inputNote()"></div>
		                </div>
	            	</div>
	            	<div class="fr btns-div">
	            		<div class="num-btns">
	            			<div class="num-btn">1</div>
	            			<div class="num-btn">2</div>
	            			<div class="num-btn">3</div>
	            			<div class="num-btn">4</div>
	            			<div class="num-btn">5</div>
	            			<div class="num-btn">6</div>
	            			<div class="num-btn">7</div>
	            			<div class="num-btn">8</div>
	            			<div class="num-btn">9</div>
	            			<div class="num-btn">0</div>
	            			<div class="num-btn">.</div>
	            		</div>
	            		<div class="btn-operate  ">
		                    <button class="btn btn-cancel in-btn135" type="button" onclick="closeConfirm('combodish-dialog')">取消
		                    </button>
		                    <button class="btn btn-save in-btn135" id="" type="button" onclick="">确认
		                    </button>
		                </div>
	            	</div>
	                
	            </div>
	        </div>
	    </div>
	</div>
	<!-- 鱼锅 -->
	 <div class="modal fade default-dialog in " id="fishpotdish-dialog"
	     data-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content">
	        	<div class="dialog-sm-header">
	        		<div class="modal-title">餐道</div>
	                <img src="../images/close-sm.png" class="img-close" onclick="closeConfirm('fishpotdish-dialog')">
	            </div>
	            <div class="modal-body">
	            		<!-- 仅存在一个分类中-->
		            <div class="dialog-sm-info">
		              	<input type="hidden" id="dish-id" value="">
		                <input type="hidden" id="dish-type" value="">
		                <div id="taste" class="taste">
	                    	<h6 style="font-weight: bold; font-size: 16px">选择口味</h6>
	                    	<ul><li>口味1</li><li>口味2</li><li>口味3</li><li>口味4</li></ul>
	                    </div>
	                    <div class="fishpot">
		            		<h6 style="font-weight: bold; font-size: 16px">锅底</h6>
	                    	<div class="form-group"><div class="col-xs-7 fish">锅底</div></div>
	                    	
	                    	<h6 style="font-weight: bold; font-size: 16px">鱼</h6>
	                    	<div class="form-group">
	                    		<div class="col-xs-7 fish">小小鱼</div>
	                    		<div class="num-oper col-xs-5">
	                    			<button class="btn btn-default num-oper-btn">-</button>
	                    			<input type="text" class="form-control num-inp" min="0">
	                    			<button class="btn btn-default num-oper-btn">+</button>
	                    		</div>
	                    	</div>
	                    	<div class="form-group">
	                    		<div class="col-xs-7 fish">大大鱼</div>
	                    		<div class="num-oper col-xs-5">
	                    			<button class="btn btn-default num-oper-btn">-</button>
	                    			<input type="text" class="form-control num-inp" min="0">
	                    			<button class="btn btn-default num-oper-btn">+</button>
	                    		</div>
	                    	</div>
		            	</div>
		               	<h6 style="font-weight: bold; font-size: 16px">忌口</h6>
		               	<div class="form-group"><div class="avoid">少辣</div><div class="avoid">少盐</div></div>
		               	<div class="form-group"><span class="inpt-span">其他忌口：</span><input type="text" id="combodish-note" class="form-control padding-left" onclick="inputNote()"></div>
		            </div>
	            	<div class="btn-operate  ">
	            		<button class="btn btn-cancel in-btn135" type="button" onclick="closeConfirm('fishpotdish-dialog')">取消
		                </button>
		                <button class="btn btn-save in-btn135" id="" type="button" onclick="">确认
		                </button>
		            </div>
	            </div>
	        </div>
	    </div>
	</div>
	<!-- 输入备注 -->
	<div class="modal fade default-dialog input-dialog in " id="noteinput-dialog"
	     data-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content">
	        	<div class="dialog-sm-header">
	        		<div class="modal-title">餐道</div>
	                <img src="../images/close-sm.png" class="img-close" onclick="closeConfirm('noteinput-dialog')">
	            </div>
	            <div class="modal-body">
	            	<div class="form-group">
		            	<input type="hidden" id="type" value="">
		            	<div class="fl ">其他忌口：</div>
		            	<div class="fr">还可以输入<span id="note-count">20</span>字</div>
	            	</div>
	            	<div class="form-group">
	            		<textarea class="form-control" maxlength="20" rows="5" cols="80" id="note-inp" onkeyup="changeCount()"></textarea>
	            	</div>
	            	<div class="btn-operate  ">
	                    <button class="btn btn-cancel in-btn135 clear-btn disabled" style="float: left;" type="button" onclick="clearNoteInput()">清空
	                    </button>
	                    <div style="text-align: right;">
	                    	<button class="btn btn-cancel in-btn135" type="button" onclick="closeConfirm('noteinput-dialog')">取消
		                    </button>
		                    <button class="btn btn-save in-btn135" id="" type="button" onclick="changeNote()">确认
		                    </button>
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
	 </div>
</body>
</html>