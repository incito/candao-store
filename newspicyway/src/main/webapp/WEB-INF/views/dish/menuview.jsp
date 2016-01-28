<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>菜谱管理</title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="../tools/bootstrap/css/bootstrap.css" rel="stylesheet">
<link rel="stylesheet" href="../css/common.css">
<link rel="stylesheet" href="../css/index.css">
<link rel="stylesheet" href="../css/dishes.css">
<link rel="stylesheet" href="../css/menu.css?v=5">
<link rel="stylesheet" href="../css/storesel.css">
<link rel="stylesheet" href="../tools/font-awesome/css/font-awesome.css">
<script src="../scripts/jquery.js"></script>
<script src="../tools/bootstrap/js/bootstrap.min.js"></script>
<script src="../scripts/projectJs/index.js"></script>
<script src="../scripts/projectJs/menu.js"></script>
<script src="../scripts/projectJs/menuoperate.js"></script>
<script src="../scripts/projectJs/selectStore.js"></script>
<script src="../scripts/global.js"></script>
<script src="<%=request.getContextPath()%>/tools/calendar_diy/WdatePicker.js"></script>
<script src="<%=request.getContextPath()%>/tools/jquery-validation/jquery.validate.js"></script>
<script src="<%=request.getContextPath()%>/tools/jquery-validation/messages_zh.js"></script>

</head>
<body>
	<div class="ky-container ">
		<div class="ky-content content-iframe">
			<div class="dishes-content">
				<input type="hidden" id="menuId_param" value="${menuId }">
				<p class="dishes-content-title">
					<font><a href="Javascript:menuctrl();">菜品管理</a> > <span class="menuname_span"></span> > 查看菜谱</font>
					<button class="btn btn-default" type="button" id="menuAdd-edit-btn">编辑菜谱</button>
				</p>
				<div class="dishes-menu-content row">
					<div class="menu-left col-xs-4 menu-left-view">
					<!-- 
						<button class="btn btn-default" id="menuDetail-view-btn">菜品信息查看</button>
						 -->
						<table class="table menu-left-desc ">
							<tr>
								<td class="td1">菜谱名称：</td>
								<td><span class="menuname_span"></span><img
									src="../images/menu-edit-pen.png" class="hidden"></td>
							</tr>
							<tr>
								<td class="td1">适用门店：</td>
								<td><span class="branch_span" style="cursor: pointer;" title=""></span>
								<img src="../images/menu-edit-pen.png" class="hidden">
								</td>
							</tr>
							<tr>
								<td class="td1">启用时间：</td>
								<td><span class="effecttime_span"></span><img
									src="../images/menu-edit-pen.png" class="hidden"></td>
							</tr>

						</table>

					</div>
					<div class="menu-center col-xs-5">
						<div class="menu-type">
							<div class="nav-dishes-prev">
								<i class="icon-chevron-left"></i>
							</div>
							<ul class="nav-dishes-menu" id="nav-dishes-scroll">

							</ul>
							<div class="nav-dishes-next">
								<i class="icon-chevron-right"></i>
							</div>
						</div>
						<div class="menu-detail-view" id="menu-detail-view" onmousemove="displayCoord(this, event);"></div>
					</div>
					<div class="menu-page col-xs-5">
						<span class="current_page"></span>/<span class="total_page"></span>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!--信息查看-->
	<div class="modal fade menuDetail-view-dialog in "
		id="menuDetail-view-dialog" data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title">菜品信息</h4>
					<img src="../images/close.png" class="img-close"
						data-dismiss="modal">
				</div>
				<div class="modal-body">
					<div class="row">
						<div class="col-xs-9 div-left">
							<p>
								<span class="sp1">菜品编号:</span><span class="sp2">2002</span>
							</p>
							<p>
								<span class="sp1">菜品名称:</span><span class="sp2">2002</span> <span
									class="sp1">库存状态:</span><span class="sp2"><img
									src="../images/menu-state-on.png"></span>
							</p>
							<!--另一种状态的图片 menu-state-off.png-->
							<p>
								<span class="sp1">菜品分类:</span><span class="sp2">饮品</span>
							</p>
							<p>
								<span class="sp1">单位:</span><span class="sp2">杯/扎</span>
							</p>
							<p>
								<span class="sp1">价格:</span><span class="sp2">12</span> <span
									class="sp1">会员价:</span><span class="sp2">12</span>
							</p>

							<p>
								<span class="sp1">其他:</span><span class="sp2">特色新品</span>
							</p>
						</div>
						<div class="col-xs-3 div-right">
							<img src="../images/dish1.png">
						</div>
					</div>
					<div class="row">
						<p>
							<span class="sp1">菜品介绍:</span><span class="sp2-intro">当你准备演讲的时候，永远在桌子上放一瓶水。当你突然想不起来该说啥的时候，喝口水。听众不会感到任何异常的。当你准备演讲的时候，永远在桌子上放一瓶水。当你突然想不起来该说啥的时候，喝口水。听众不会感到任何异常的。当你准备演讲的时候，永远在桌子上放一瓶水。当你突然想不起来该说啥的时候，喝口水。听众不会感到任何异常的。</span>
						</p>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="./menupro.jsp"%>
	<!-- 菜谱版式 -->
	<div class="hidden" id="hide_tmpl">
		<!-- 样式一 -->
		<div class="menu-detail hidden " id="menu-detail1">
			<div class="one menu-detail-box" id="A1">
				<img src="">
				<div class="menu-desc">
					<p>
						<span></span>
					</p>
					<p>
						<span></span>
					</p>
					<p>
						<span></span>
					</p>
				</div>
			</div>
		</div>
		<!--样式二-->
		<div class="menu-detail hidden" id="menu-detail2">
			<div class="row menu-detail-box" id="B1">
				<img src="">
				<div class="menu-desc">
					<p>
						<span></span>
					</p>
					<p>
						<span></span>
					</p>
					<p>
						<span></span>
					</p>
				</div>
			</div>
			<div class="row menu-detail-box" id="B2">
				<img src="">
				<div class="menu-desc">
					<p>
						<span></span>
					</p>
					<p>
						<span></span>
					</p>
					<p>
						<span></span>
					</p>
				</div>
			</div>

		</div>
		<!--样式三-->
		<div class="menu-detail" id="menu-detail3">
			<div class="row menu-detail-box" id="C1">
				<img src="">
				<div class="menu-desc">
					<p>
						<span></span>
					</p>
					<p>
						<span></span>
					</p>
					<p>
						<span></span>
					</p>
				</div>
			</div>
			<div class="row ">
				<div class="col-xs-6 menu-detail-box" id="C2">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="C3">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
		</div>
		<!--样式四-->
		<div class="menu-detail hidden" id="menu-detail4">
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="D1">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="D2">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row menu-detail-box" id="D3">
				<img src="">
				<div class="menu-desc">
					<p>
						<span></span>
					</p>
					<p>
						<span></span>
					</p>
					<p>
						<span></span>
					</p>
				</div>
			</div>
		</div>
		<!--样式五-->
		<div class="menu-detail hidden" id="menu-detail5">
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="E1">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="E2">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="E3">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="E4">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
		</div>
		<!--样式六-->
		<div class="menu-detail hidden" id="menu-detail6">
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="F1">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="F2">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6 ">
					<div class="row menu-detail-box" id="F3">
						<img src="">
						<div class="menu-desc">
							<p>
								<span></span>
							</p>
							<p>
								<span></span>
							</p>
							<p>
								<span></span>
							</p>
						</div>
					</div>
					<div class="row menu-detail-box" id="F4">
						<img src="">
						<div class="menu-desc">
							<p>
								<span></span>
							</p>
							<p>
								<span></span>
							</p>
							<p>
								<span></span>
							</p>
						</div>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="F5">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
		</div>
		<!--样式七-->
		<div class="menu-detail hidden" id="menu-detail7">
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="G1">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="G2">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="G3">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6">
					<div class="row menu-detail-box" id="G4">
						<img src="">
						<div class="menu-desc">
							<p>
								<span></span>
							</p>
							<p>
								<span></span>
							</p>
							<p>
								<span></span>
							</p>
						</div>
					</div>
					<div class="row menu-detail-box" id="G5">
						<img src="">
						<div class="menu-desc">
							<p>
								<span></span>
							</p>
							<p>
								<span></span>
							</p>
							<p>
								<span></span>
							</p>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--样式八-->
		<div class="menu-detail hidden" id="menu-detail8">
			<div class="row ">
				<div class="col-xs-6 menu-detail-box" id="H1">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="H2">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>

			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="H3">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="H4">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>

			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="H5">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="H6">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row ">
				<div class="col-xs-6 menu-detail-box" id="H7">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="H8">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
		</div>
		<!--样式九-->
		<div class="menu-detail hidden" id="menu-detail9">
			<div class="row ">
				<div class="col-xs-6 menu-detail-box" id="I1">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="I2">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="I3">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="I4">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="I5">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="I6">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row ">
				<div class="col-xs-6 menu-detail-box" id="I7">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="I8">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row ">
				<div class="col-xs-6 menu-detail-box" id="I9">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="I10">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="I11">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="I12">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="I13">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="I14">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
		</div>
		<!--样式十-->
		<div class="menu-detail hidden" id="menu-detail10">
			<div class="row-left row menu-detail-box" id="J1">
				<img class="show-pic" src="">
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="J2">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="J3">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="J4">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="J5">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="J6">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="J7">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="J8">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="J9">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="J10">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			
		</div>
		<!--样式十一-->
		<div class="menu-detail hidden" id="menu-detail11">
			<div class="row-left row menu-detail-box" id="K1">
				<img class="show-pic" src="">
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="K2">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="K3">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="K4">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="K5">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="K6">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
		</div>
		<!--样式十二-->
		<div class="menu-detail hidden" id="menu-detail12">
			<div class="row1 row menu-detail-box" id="L1">
				<img class="show-pic" src="">
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="L2">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="L3">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="L4">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="L5">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row ">
				<div class="col-xs-6 menu-detail-box" id="L6">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="L7">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row ">
				<div class="col-xs-6 menu-detail-box" id="L8">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="L9">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="L10">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="L11">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
		</div>
		<!--样式十三-->
		<div class="menu-detail hidden" id="menu-detail13">
			<div class="row1 row menu-detail-box" id="M1">
				<img class="show-pic" src="">
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="M2">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="M3">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="M4">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="M5">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row ">
				<div class="col-xs-6 menu-detail-box" id="M6">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="M7">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
		</div>
		<!--样式十四  九宫格-->
		<div class="menu-detail hidden" id="menu-detail14">
			<div class="row ">
				<div class="col-xs-6 menu-detail-box" id="N1">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="N2">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="N3">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6 menu-detail-box" id="N4">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="N5">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="N6">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
			<div class="row ">
				<div class="col-xs-6 menu-detail-box" id="N7">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="N8">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
				<div class="col-xs-6 menu-detail-box" id="N9">
					<img src="">
					<div class="menu-desc">
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
						<p>
							<span></span>
						</p>
					</div>
				</div>
			</div>
		</div>
	</div>
	<input type="text" class="hidden " value="${menuId}" id="menuidMenu"   >
	<script type="text/javascript">
		$("#menuDetail-view-btn").click(function() {
			$("#menuDetail-view-dialog").modal("show");
		});
	</script>
</body>
</html>

