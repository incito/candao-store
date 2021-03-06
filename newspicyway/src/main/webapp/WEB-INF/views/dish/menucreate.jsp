<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<link rel="stylesheet" href="../css/menu.css">
<link rel="stylesheet" href="../css/storesel.css">
<link rel="stylesheet" href="../css/common/jquery.Jcrop.min.css">

<link rel="stylesheet" href="../tools/font-awesome/css/font-awesome.css">
<script src="../scripts/jquery.js"></script>
<script src="../tools/bootstrap/js/bootstrap.min.js"></script>
<script src="../scripts/bootstrap-contextmenu.js"></script>
<script src="<%=request.getContextPath()%>/scripts/ajaxfileupload.js"></script>
<script src="../scripts/projectJs/index.js"></script>
<script src="../scripts/projectJs/menu.js"></script>
<script src="../scripts/projectJs/menuoperate.js"></script>
<script src="../scripts/projectJs/selectStore.js"></script>
<script src="../scripts/jquery.Jcrop.min.js"></script>
<script src="../scripts/global.js"></script>
<script src="../scripts/projectJs/createmenu.js"></script>
<script src="<%=request.getContextPath()%>/tools/calendar_diy/WdatePicker.js"></script>
<script src="<%=request.getContextPath()%>/tools/jquery-validation/jquery.validate.js"></script>
<script src="<%=request.getContextPath()%>/tools/jquery-validation/messages_zh.js"></script>
</head>
<body style="overflow-y: auto; height: 900px;">
	<div class="ky-container ">
		<div class="ky-content content-iframe">
			<div class="dishes-content">
				<p class="dishes-content-title">
				<font>
					<a href="Javascript:menuctrl();">菜品管理</a> > 新建菜谱</font>
					<button class="btn btn-default" type="button"
						id="menuAdd-complete-btn">创建完成</button>
				</p>
				<!--菜谱页面详情-->
				<div class="dishes-menu-content row dishmenu-content">
					<div class="menu-left col-xs-3">
						<table id="dishes_tb" class="table menu-left-dishes">

						</table>
					</div>
					<div class="menu-center col-xs-5">
						<!--菜品种类-->
						<div class="menu-type">
							<div class="nav-dishes-prev ">
								<i class="icon-chevron-left"></i>
							</div>
							<ul class="nav-dishes-menu" id="nav-dishes-scroll">

							</ul>
							<div class="nav-dishes-next ">
								<i class="icon-chevron-right"></i>
							</div>

						</div>
						<!--菜谱缩略图-->
						<div class="menu-thumb">
							<div class="nav-menu-prev">
								<i class="icon-chevron-left"></i>
							</div>
							<div class="menu-count" id="menu-count-scroll">
								<div class="menu-add" id="menu-add-btn">
									<p id="pf">加一页</p>
								</div>
							</div>
							<div class="menu-add-type hidden">
								<span></span> <em></em>
								<p>
									<img tmplid="tmpl1" src="../images/menu-detail1.png" class="menu-add-img">
									<img tmplid="tmpl2" src="../images/menu-detail2.png" class="menu-add-img">
									<img tmplid="tmpl3" src="../images/menu-detail3.png" class="menu-add-img">
									<img tmplid="tmpl4" src="../images/menu-detail4.png" class="menu-add-img">
									<img tmplid="tmpl5" src="../images/menu-detail5.png" class="menu-add-img">
								</p>
								<p>
									<img tmplid="tmpl6" src="../images/menu-detail6.png" class="menu-add-img">
									<img tmplid="tmpl7" src="../images/menu-detail7.png" class="menu-add-img">
									<img tmplid="tmpl8" src="../images/menu-detail8.png" class="menu-add-img">
									<img tmplid="tmpl9" src="../images/menu-detail9.png" class="menu-add-img">
									<img tmplid="tmpl10" src="../images/menu-detail10.png" class="menu-add-img">
								</p>
								<p>
									<img tmplid="tmpl11" src="../images/menu-detail11.png" class="menu-add-img">
									<img tmplid="tmpl12" src="../images/menu-detail12.png" class="menu-add-img">
									<img tmplid="tmpl13" src="../images/menu-detail13.png" class="menu-add-img">
									<img tmplid="tmpl14" src="../images/menu-detail14.png" class="menu-add-img">
								</p>
							</div>
							<div class="nav-menu-next"
								style="margin-right: 0; margin-top: -51px">
								<i class="icon-chevron-right"></i>
							</div>

						</div>
						<!--菜谱详情编辑-->
						<div class="menu-detail-edit" style="height: auto;" >
							<!--样式一-->
							<div class="menu-detail hidden " id="menu-detail1">
								<div class="one menu-detail-box" data-toggle="context"
									data-target="#context-menu1" ondrop="menuDrop(event,2)"
									ondragover="allowDrop(event)" id="A1">
									<div id="context-menu1">
										<ul class="dropdown-menu menu-right-tab">
											<li class="menu-right-tab1">修改菜品属性</li>
											<li class="menu-right-tab2">调整图片</li>
											<li class="menu-right-tab3">将菜品从本页中移除</li>
											<li class="menu-right-tab4">推荐</li>
										</ul>
									</div>
								</div>
							</div>

							<!--样式二-->
							<div class="menu-detail hidden" id="menu-detail2">
								<div class="row menu-detail-box" data-toggle="context"
									data-target="#context-menu2" id="B1"
									ondrop="menuDrop(event,2)" ondragover="allowDrop(event)">
									<div id="context-menu2">
										<ul class="dropdown-menu menu-right-tab">
											<li class="menu-right-tab1">修改菜品属性</li>
											<li class="menu-right-tab2">调整图片</li>
											<li class="menu-right-tab3">将菜品从本页中移除</li>
											<li class="menu-right-tab4">推荐</li>
										</ul>

									</div>

								</div>
								<div class="row menu-detail-box" data-toggle="context"
									data-target="#context-menu3" id="B2"
									ondrop="menuDrop(event,2)" ondragover="allowDrop(event)">
									<div id="context-menu3">
										<ul class="dropdown-menu menu-right-tab">
											<li class="menu-right-tab1">修改菜品属性</li>
											<li class="menu-right-tab2">调整图片</li>
											<li class="menu-right-tab3">将菜品从本页中移除</li>
											<li class="menu-right-tab4">推荐</li>
										</ul>
									</div>
								</div>
							</div>
							<!--样式三-->
							<div class="menu-detail hidden" id="menu-detail3">
								<div class="row menu-detail-box" data-toggle="context"
									data-target="#context-menu4" ondrop="menuDrop(event,2)"
									ondragover="allowDrop(event)" id="C1">
									<div id="context-menu4">
										<ul class="dropdown-menu menu-right-tab">
											<li class="menu-right-tab1">修改菜品属性</li>
											<li class="menu-right-tab2">调整图片</li>
											<li class="menu-right-tab3">将菜品从本页中移除</li>
											<li class="menu-right-tab4">推荐</li>
										</ul>

									</div>
								</div>
								<div class="row ">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu5" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="C2">
										<div id="context-menu5">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu6" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="C3">
										<div id="context-menu6">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
							</div>
							<!--样式四-->
							<div class="menu-detail hidden" id="menu-detail4">
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu7" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="D1">
										<div id="context-menu7">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu8" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="D2">
										<div id="context-menu8">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row menu-detail-box" data-toggle="context"
									data-target="#context-menu9" ondrop="menuDrop(event,2)"
									ondragover="allowDrop(event)" id="D3">
									<div id="context-menu9">
										<ul class="dropdown-menu menu-right-tab">
											<li class="menu-right-tab1">修改菜品属性</li>
											<li class="menu-right-tab2">调整图片</li>
											<li class="menu-right-tab3">将菜品从本页中移除</li>
											<li class="menu-right-tab4">推荐</li>
										</ul>
									</div>
								</div>
							</div>
							<!--样式五-->
							<div class="menu-detail hidden" id="menu-detail5">
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu10" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="E1">
										<div id="context-menu10">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu11" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="E2">
										<div id="context-menu11">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu12" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="E3">
										<div id="context-menu12">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu13" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="E4">
										<div id="context-menu13">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
							</div>
							<!--样式六-->
							<div class="menu-detail hidden" id="menu-detail6">
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu14" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="F1">
										<div id="context-menu14">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu15" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="F2">
										<div id="context-menu15">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-6 ">
										<div class="row menu-detail-box" data-toggle="context"
											data-target="#context-menu16" ondrop="menuDrop(event,2)"
											ondragover="allowDrop(event)" id="F3">
											<div id="context-menu16">
												<ul class="dropdown-menu menu-right-tab">
													<li class="menu-right-tab1">修改菜品属性</li>
													<li class="menu-right-tab2">调整图片</li>
													<li class="menu-right-tab3">将菜品从本页中移除</li>
													<li class="menu-right-tab4">推荐</li>
												</ul>

											</div>
										</div>
										<div class="row menu-detail-box" data-toggle="context"
											data-target="#context-menu17" ondrop="menuDrop(event,2)"
											ondragover="allowDrop(event)" id="F4">
											<div id="context-menu17">
												<ul class="dropdown-menu menu-right-tab">
													<li class="menu-right-tab1">修改菜品属性</li>
													<li class="menu-right-tab2">调整图片</li>
													<li class="menu-right-tab3">将菜品从本页中移除</li>
													<li class="menu-right-tab4">推荐</li>
												</ul>
											</div>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu18" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="F5">
										<div id="context-menu18">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
							</div>
							<!--样式七-->
							<div class="menu-detail hidden" id="menu-detail7">
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu19" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="G1">
										<div id="context-menu19">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu20" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="G2">
										<div id="context-menu20">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu21" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="G3">
										<div id="context-menu21">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6">
										<div class="row menu-detail-box" data-toggle="context"
											data-target="#context-menu22" ondrop="menuDrop(event,2)"
											ondragover="allowDrop(event)" id="G4">
											<div id="context-menu22">
												<ul class="dropdown-menu menu-right-tab">
													<li class="menu-right-tab1">修改菜品属性</li>
													<li class="menu-right-tab2">调整图片</li>
													<li class="menu-right-tab3">将菜品从本页中移除</li>
													<li class="menu-right-tab4">推荐</li>
												</ul>
											</div>
										</div>
										<div class="row menu-detail-box" data-toggle="context"
											data-target="#context-menu23" ondrop="menuDrop(event,2)"
											ondragover="allowDrop(event)" id="G5">
											<div id="context-menu23">
												<ul class="dropdown-menu menu-right-tab">
													<li class="menu-right-tab1">修改菜品属性</li>
													<li class="menu-right-tab2">调整图片</li>
													<li class="menu-right-tab3">将菜品从本页中移除</li>
													<li class="menu-right-tab4">推荐</li>
												</ul>
											</div>
										</div>
									</div>
								</div>
							</div>
							<!--样式八-->
							<div class="menu-detail hidden" id="menu-detail8">
								<div class="row ">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu24" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="H1">
										<div id="context-menu24">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu25" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="H2">
										<div id="context-menu25">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu26" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="H3">
										<div id="context-menu26">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu27" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="H4">
										<div id="context-menu27">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu28" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="H5">
										<div id="context-menu28">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu29" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="H6">
										<div id="context-menu29">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row ">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu30" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="H7">
										<div id="context-menu30">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu31" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="H8">
										<div id="context-menu31">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
							</div>
							<!--样式九-->
							<div class="menu-detail hidden" id="menu-detail9">
								<div class="row ">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu32" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="I1">
										<div id="context-menu32">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>

										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu33" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="I2">
										<div id="context-menu33">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu34" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="I3">
										<div id="context-menu34">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu35" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="I4">
										<div id="context-menu35">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu36" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="I5">
										<div id="context-menu36">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu37" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="I6">
										<div id="context-menu37">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row ">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu38" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="I7">
										<div id="context-menu38">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu39" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="I8">
										<div id="context-menu39">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row ">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu40" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="I9">
										<div id="context-menu40">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu41" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="I10">
										<div id="context-menu41">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu42" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="I11">
										<div id="context-menu42">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu43" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="I12">
										<div id="context-menu43">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu44" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="I13">
										<div id="context-menu44">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu45" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="I14">
										<div id="context-menu45">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
							</div>
							<!--样式十-->
							<div class="menu-detail hidden" id="menu-detail10">
								<div class="row-left row menu-detail-box" data-toggle="context" 
									data-target="#context-menu67" ondrop="menuDrop(event,2)" id="J1" isdbclick="no">
									<img class="show-pic" src="<%=request.getContextPath()%>/images/menu-detail-bg-upload-ver.png" onclick="imgtempClick(this)" >
									<input type="file" onchange="showpic(this)" style="position: absolute; filter: alpha(opacity = 0); opacity: 0; width: 0; height: 0;" size="1" id="main_img1" name="main_img1" accept="image/*" >
									<div id="context-menu67">
										<ul class="dropdown-menu menu-right-tab">
											<li class="menu-right-tab2">调整图片</li>
										</ul>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu57" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="J2">
										<div id="context-menu57">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu58" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="J3">
										<div id="context-menu58">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu59" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="J4">
										<div id="context-menu59">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu60" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="J5">
										<div id="context-menu60">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row ">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu61" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="J6">
										<div id="context-menu61">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row ">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu62" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="J7">
										<div id="context-menu62">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row ">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu63" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="J8">
										<div id="context-menu63">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row ">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu64" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="J9">
										<div id="context-menu64">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu65" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="J10">
										<div id="context-menu65">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
							</div>
							<!--样式十一-->
							<div class="menu-detail hidden" id="menu-detail11">
								<div class="row-left row menu-detail-box" data-toggle="context" 
									data-target="#context-menu68" ondrop="menuDrop(event,2)" id="K1" isdbclick="no">
									<img class="show-pic" src="<%=request.getContextPath()%>/images/menu-detail-bg-upload-ver.png" onclick="imgtempClick(this)" >
									<input type="file" onchange="showpic(this)" style="position: absolute; filter: alpha(opacity = 0); opacity: 0; width: 0; height: 0;" size="1" id="main_img2" name="main_img2" accept="image/*" >
									<div id="context-menu68">
										<ul class="dropdown-menu menu-right-tab">
											<li class="menu-right-tab2">调整图片</li>
										</ul>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu69" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="K2">
										<div id="context-menu69">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu70" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="K3">
										<div id="context-menu70">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu71" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="K4">
										<div id="context-menu71">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu72" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="K5">
										<div id="context-menu72">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row ">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu73" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="K6">
										<div id="context-menu73">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
							</div>
							<!--样式十二-->
							<div class="menu-detail hidden" id="menu-detail12">
								<div class="row1 row menu-detail-box" data-toggle="context" 
									data-target="#context-menu66" ondrop="menuDrop(event,2)" id="L1" isdbclick="no">
									<img class="show-pic" src="<%=request.getContextPath()%>/images/menu-detail-bg-upload.png" onclick="imgtempClick(this)" >
									<input type="file" onchange="showpic(this)" style="position: absolute; filter: alpha(opacity = 0); opacity: 0; width: 0; height: 0;" size="1" id="main_img" name="main_img" accept="image/*" >
									<div id="context-menu66">
										<ul class="dropdown-menu menu-right-tab">
											<li class="menu-right-tab2">调整图片</li>
										</ul>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu47" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="L2">
										<div id="context-menu47">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu48" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="L3">
										<div id="context-menu48">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu49" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="L4">
										<div id="context-menu49">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu50" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="L5">
										<div id="context-menu50">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row ">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu51" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="L6">
										<div id="context-menu51">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu52" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="L7">
										<div id="context-menu52">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row ">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu53" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="L8">
										<div id="context-menu53">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu54" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="L9">
										<div id="context-menu54">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu55" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="L10">
										<div id="context-menu55">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu56" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="L11">
										<div id="context-menu56">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
							</div>
							<!--样式十三-->
							<div class="menu-detail hidden" id="menu-detail13">
								<div class="row1 row menu-detail-box" data-toggle="context" 
									data-target="#context-menu74" ondrop="menuDrop(event,2)" id="M1" isdbclick="no">
									<img class="show-pic" src="<%=request.getContextPath()%>/images/menu-detail-bg-upload.png" onclick="imgtempClick(this)" >
									<input type="file" onchange="showpic(this)" style="position: absolute; filter: alpha(opacity = 0); opacity: 0; width: 0; height: 0;" size="1" id="main_img3" name="main_img3" accept="image/*" >
									<div id="context-menu74">
										<ul class="dropdown-menu menu-right-tab">
											<li class="menu-right-tab2">调整图片</li>
										</ul>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu75" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="M2">
										<div id="context-menu75">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu76" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="M3">
										<div id="context-menu76">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu77" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="M4">
										<div id="context-menu77">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu78" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="M5">
										<div id="context-menu78">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row ">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu79" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="M6">
										<div id="context-menu79">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu80" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="M7">
										<div id="context-menu80">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
							</div>
							<!--样式十四  九宫格-->
							<div class="menu-detail hidden" id="menu-detail14">
								<div class="row ">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu81" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="N1">
										<div id="context-menu81">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu82" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="N2">
										<div id="context-menu82">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu83" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="N3">
										<div id="context-menu83">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu84" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="N4">
										<div id="context-menu84">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu85" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="N5">
										<div id="context-menu85">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu86" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="N6">
										<div id="context-menu86">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="row ">
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu87" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="N7">
										<div id="context-menu87">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu88" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="N8">
										<div id="context-menu88">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
									<div class="col-xs-6 menu-detail-box" data-toggle="context"
										data-target="#context-menu89" ondrop="menuDrop(event,2)"
										ondragover="allowDrop(event)" id="N9">
										<div id="context-menu89">
											<ul class="dropdown-menu menu-right-tab">
												<li class="menu-right-tab1">修改菜品属性</li>
												<li class="menu-right-tab2">调整图片</li>
												<li class="menu-right-tab3">将菜品从本页中移除</li>
												<li class="menu-right-tab4">推荐</li>
											</ul>
										</div>
									</div>
								</div>
							</div>							<!--对于图片的操作-->
							<div class="menu-detail-oper hidden">
								<p class="p-remove">
									<img src="../images/menu-remove.png">
								</p>
								<p class="p-change">
									<img src="../images/menu-change.png">
								</p>
							</div>
							<div class="menu-detail-change hidden">
								<span></span> <em></em>
								<p>
									<img tmplid="tmpl1" src="../images/menu-detail1.png" class="menu-add-img">
									<img tmplid="tmpl2" src="../images/menu-detail2.png" class="menu-add-img">
									<img tmplid="tmpl3" src="../images/menu-detail3.png" class="menu-add-img">
									<img tmplid="tmpl4" src="../images/menu-detail4.png" class="menu-add-img">
									<img tmplid="tmpl5" src="../images/menu-detail5.png" class="menu-add-img">
								</p>
								<p>
									<img tmplid="tmpl6" src="../images/menu-detail6.png" class="menu-add-img">
									<img tmplid="tmpl7" src="../images/menu-detail7.png" class="menu-add-img">
									<img tmplid="tmpl8" src="../images/menu-detail8.png" class="menu-add-img">
									<img tmplid="tmpl9" src="../images/menu-detail9.png" class="menu-add-img">
									<img tmplid="tmpl10" src="../images/menu-detail10.png" class="menu-add-img">
								</p>
								<p>
									<img tmplid="tmpl11" src="../images/menu-detail11.png" class="menu-add-img">
									<img tmplid="tmpl12" src="../images/menu-detail12.png" class="menu-add-img">
									<img tmplid="tmpl13" src="../images/menu-detail13.png" class="menu-add-img">
									<img tmplid="tmpl14" src="../images/menu-detail14.png" class="menu-add-img">
								</p>
							</div>

						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="./menupro.jsp"%>
</body>
</html>
