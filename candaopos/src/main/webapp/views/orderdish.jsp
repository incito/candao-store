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
	<link rel="stylesheet" href="../tools/bootstrap-3.3.5/css/bootstrap.min.css">
	<link rel="stylesheet" href="../css/common.css">
	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="../scripts/jquery-3.1.0.min.js"></script>
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script src="../tools/bootstrap-3.3.5/js/bootstrap.min.js"></script>
	<script src="../scripts/common.js"></script>
	<link rel="stylesheet" href="../css/orderdish.css">

</head>
<body style="overflow:auto ">
<%
	String orderid = request.getParameter("orderid");
	String personnum = request.getParameter("personnum");
	String tableno =  java.net.URLDecoder.decode(request.getParameter("tableno") , "UTF-8");
%>
<input type="hidden" value="<%=orderid%>" name="orderid">
<input type="hidden" value="<%=personnum%>" name="personnum">
<input type="hidden" value="<%=tableno%>" name="tableno">
	<div id="adddish">
		<header>
			<div class="fl logo">餐道</div>
			<div class="fr close-win"  onclick="window.location.href='../views/main.jsp';">关闭</div>
		</header>
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
				<div class="oper-div">
					<div class="btns">
						<button class="btn oper-btn prev-btn disabled">
							<span class="glyphicon glyphicon-chevron-up"></span>
						</button>
						<div class="page-info">
							<span id="curr-page">0</span>/<span id="pages-len">0</span>
						</div>
						<button class="btn oper-btn next-btn disabled">
							<span class="glyphicon glyphicon-chevron-down"></span>
						</button>
						<button class="btn oper-btn disabled" onclick="AddDish.doUpdateNum(1)">
							<span class="glyphicon glyphicon-plus"></span>
						</button>
						<button class="btn oper-btn disabled" onclick="AddDish.doUpdateNum(2)">
							<span class="glyphicon glyphicon-minus"></span>
						</button>
						<button class="btn oper-btn disabled" onclick="AddDish.updateNum()">
							<span>数量</span>
						</button>
						<button class="btn oper-btn disabled" onclick="AddDish.initNoteDialog(0);">
							<span>备注</span>
						</button>
					</div>
				</div>
			</div>
			<div class="main-div">
				<div class="form-group dish-type">
					<div class="nav-type-prev btn nav-dishtype-prev">
						<span class="glyphicon glyphicon-chevron-left"></span>
					</div>
					<ul class="nav-types nav-dish-types"></ul>
					<div class="nav-type-next btn nav-type nav-dishtype-next">
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
						<div class="page-btn btn prev-btn">
							<span class="glyphicon glyphicon-chevron-up"></span>
						</div>
						<div class="page-info">
							<span id="curr-page1">0</span>/<span id="pages-len1">0</span>
						</div>
						<div class="page-btn btn next-btn">
							<span class="glyphicon glyphicon-chevron-down"></span>
						</div>
					</div>
				</div>
				<div class="main-oper-btns">
					<button class="btn disabled gua-dan hide" onclick="AddDish.guadan()">挂单</button>
					<button class="btn disabled give-dish" onclick="AddDish.giveFood()">赠菜</button>
					<button class="btn disabled" onclick="AddDish.initNoteDialog(1)">全单备注</button>
					<button class="btn disabled" onclick="AddDish.clearSelected()">清空</button>
					<button class="btn disabled place-order" onclick="AddDish.placeOrder()">下单</button>
				</div>
			</div>
		</div>
		<footer>
			<div class="info J-sys-info"><span>店铺编号：</span><span class="branch-num">- -</span><span>&nbsp;登录员工：</span><span>&nbsp;<span class="user-info">- -</span></span><span>&nbsp;当前时间：</span><span class="time">- -</span><span>&nbsp;版本号：</span><span>1.01</span></div>
		</footer>
	</div>
	<!-- 更新购物车单个菜品数量 -->
	<div class="modal fade default-dialog in " id="updatenum-dialog"
	     data-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content">
	        	<div class="dialog-sm-header">
	        		<div class="modal-title">菜品数量设置</div>
	                <img src="../images/close-sm.png" class="img-close" data-dismiss="modal">
	            </div>
	            <div class="modal-body">
	            	<!-- 仅存在一个分类中-->
	                <div class="dialog-sm-info">
	                    <div class="form-group dishname">菜品名称：<span class="dish-name"></span></div>
	                    <div class="form-group">
	                    	<span class="inpt-span">菜品数量:</span>
	                    	<input type="text"  class="form-control padding-left J-num dish-amount" autofocus>
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
	                    <button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal">取消
	                    </button>
	                    <button class="btn btn-save in-btn135" disabled  type="button" onclick="AddDish.doUpdateNum(0)">确认
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
	                </div>
	                <div class="btn-operate  ">
	                    <button class="btn btn-cancel in-btn135" type="button" onclick="closeConfirm('note-dialog')">取消
	                    </button>
	                    <button class="btn btn-save in-btn135"  type="button" onclick="AddDish.doNote(this)">确认
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
		            			<input type="text" class="form-control payment-unit"  disabled style="width: 80%; display: inline-block;">
		            			<button class="btn sel-btn J-selCompany">选择</button>
		            		</div>
		            		<div class="form-group">
		            			<span class="inpt-span ">联系人:</span>
		            			<input type="text" class="form-control contact">
		            		</div>
		            		<div class="form-group">
		            			<span class="inpt-span ">联系电话:</span>
		            			<input type="text" class="form-control tel">
		            		</div>
		            		<div class="btn-operate ">
			                    <button class="btn btn-cancel in-btn135" type="button" onclick="closeConfirm('guadan-dialog')" >取消
			                    </button>
			                    <button class="btn btn-save in-btn135" disabled  type="button" onclick="AddDish.doOrder(2)">确认挂单
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
					<div class="form-group search-btns ">
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
	                <div class="dialog-sm-info">
	               		<h6>赠菜原因</h6>
						<div class="form-group freasons"></div>
	               		<div class="form-group"><span class="inpt-span">其他赠菜原因：</span><input type="text" id="givefood-reason" class="form-control padding-left" onclick="widget.textAreaModal({ target: $(this), note: $(this).val() }).show();"></div>
	                </div>
	                <div class="btn-operate  ">
	                    <button class="btn btn-cancel in-btn135" type="button" onclick="closeConfirm('givefood-dialog')">取消
	                    </button>
	                    <button class="btn btn-save in-btn135"  type="button" onclick="AddDish.doGiveRight()">确认
	                    </button>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
	<!-- 赠菜权限 -->
	<div class="modal fade in dialog-normal bg-gray" data-backdrop="static" id="givefood-right" style="overflow: auto;"></div>

	<!-- 临时菜 -->
	<div class="modal fade default-dialog  in" id="lsc-dialog" data-backdrop="static">
		<div class="modal-dialog" style="width: 695px;">
			<div class="modal-content">
				<div class="dialog-sm-header">
					<div class="modal-title">餐道</div>
					<img src="../images/close-sm.png" class="img-close" onclick="closeConfirm('lsc-dialog')">
				</div>
				<div class="modal-body f-oh">
					<div class="modal-body">
						<div class="hori-lf-div">
							<div>
								<span>备注菜名:</span>
								<input type="text" validType='noPecial' maxlength="20"   class="form-control note-name" autofocus="">
							</div>
							<div>
								<span>价格(元):</span>
								<input type="text" validtype="intAndFloat4" style="ime-mode: disabled;"  class="form-control price">
							</div>
							<div>
								<span>数量(份):</span>
								<input type="text" validtype="intAndFloat3" style="ime-mode: disabled;" class="form-control dishnum">
							</div>
							<div class="">
								<button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal">取消
								</button>
								<button class="btn btn-save in-btn135 J-btn-submit" type="button">确认
								</button>
							</div>
						</div>
						<div class="hori-rf-div">
							<div class="virtual-keyboard">
								<ul>
									<li>1</li>
									<li>2</li>
									<li>3</li>
								</ul>
								<ul>
									<li>4</li>
									<li>5</li>
									<li>6</li>
								</ul>
								<ul>
									<li>7</li>
									<li>8</li>
									<li>9</li>
								</ul>
								<ul>
									<li>.</li>
									<li>0</li>
									<li>←</li>
								</ul>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	 <!-- 套餐 -->
	 <div class="modal fade default-dialog combodish-dialog in" id="combodish-dialog"  data-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content">
	        	<div class="dialog-sm-header">
	        		<div class="modal-title">餐道</div>
	                <img src="../images/close-sm.png" class="img-close" onclick="closeConfirm('combodish-dialog')">
	            </div>
	            <div class="modal-body">
	            	<div class="fl dish-div">
						<div class="combo-box">
							<div class="dishname">套餐</div>
							<div class="combo-group">
							</div>
							<div class="f-cb"></div>
							<hr />
							<div class="only-group">
							</div>
						</div>
		                <div class="dialog-sm-info">
		               		<h6 style="font-weight: bold; font-size: 16px">忌口</h6>
		               		<div class="form-group avoid-box"></div>
		               		<div class="form-group"><span class="inpt-span">其他忌口：</span><input type="text" class="form-control padding-left J-note" onclick="widget.textAreaModal({ target: $(this), note: $(this).val() }).show();"></div>
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
	            	</div>
					<div class="f-cb"></div>
					<div class="btn-operate  ">
						<button class="btn btn-cancel in-btn135" type="button" onclick="closeConfirm('combodish-dialog')">取消
						</button>
						<button class="btn btn-save in-btn135 disabled" type="button">确认
						</button>
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
		               	<%--<div class="form-group"><div class="avoid">少辣</div><div class="avoid">少盐</div></div>--%>
		               	<div class="form-group"><span class="inpt-span">其他忌口：</span><input type="text" class="form-control padding-left" onclick='widget.textAreaModal({ target: $(this), note: $(this).val() }).show();'></div>
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
	<script type="text/javascript" src="../lib/md5.js"></script>
	<script type="text/javascript" src="../scripts/addDish.js"></script>
</body>
</html>