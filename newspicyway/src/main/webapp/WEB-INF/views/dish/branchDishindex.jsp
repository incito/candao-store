<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head> 
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>Examples</title>
	<meta name="description" content="">
	<meta name="keywords" content="">
	<link href="../tools/bootstrap/css/bootstrap.css" rel="stylesheet">
	<link rel="stylesheet" href="../css/index.css">
	
<!-- 	<link rel="stylesheet" href="../css/fishpot.css"> -->
	<link rel="stylesheet" href="../css/common.css">
	<link rel="stylesheet" href="../tools/font-awesome/css/font-awesome.css">
<!-- 	<link rel="stylesheet" href="../css/jquery.mCustomScrollbar.css" /> -->
	<link rel="stylesheet" href="../css/dishes.css">
	<script src="../scripts/jquery.js"></script>
	<script src="../tools/bootstrap/js/bootstrap.min.js"></script>
	<script src="../scripts/projectJs/branchDishes.js"></script>
<!-- 	<script src="../scripts/projectJs/combodish.js?v=1"></script> -->
<!-- 	<script src="../scripts/projectJs/fishPot.js"></script> -->
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/json2.js"></script>
<%-- 	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/ajaxfileupload.js"></script> --%>
<%-- 	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/commons.js"></script> --%>
<%-- 	<script src="<%=request.getContextPath()%>/tools/jquery-validation/jquery.validate.js"></script> --%>
<%-- 	<script src="<%=request.getContextPath()%>/tools/jquery-validation/messages_zh.js"></script> --%>
<%-- 	<script src="<%=request.getContextPath()%>/scripts/projectJs/jquery.twbsPagination.js"></script> --%>
<%-- 	<script src="<%=request.getContextPath()%>/scripts/projectJs/autoComplete.js"></script> --%>
<%-- 	<script src="<%=request.getContextPath()%>/scripts/jquery.mCustomScrollbar.js"></script> --%>
<%-- 	<script src="<%=request.getContextPath()%>/scripts/jquery.mousewheel.js"></script> --%>
	<script src="<%=request.getContextPath()%>/scripts/global.js"></script>
	<style type="text/css">
		.menuControl-list-sigle{
	background: url(../images/menuControl-listBg.png) -1px -1px;
	width: 150px;
	height: 220px;
	position: relative;
	float: left;
	margin: 5px 10px;
	border-radius: 4px;
	border:1px solid #c8c8c8;
}
.menuControl-list-sigle img.pop_pic{
	margin-left: -1;
	position: absolute;
}
.menuControl-list-sigle:hover{
	background: #f6f2cc;
	border-radius: 4px;
	border:1px solid #c8c8c8;
}

.menuControl-list-content{
	text-align: center;
	margin-top: 57px;
	font-size: 12px;
}

.menuControl-list-name{
	font-size: 16px;
	margin-top: -20px;
}

.menuControl-list-contentBottom{
	position: absolute;
	top: 180px;
	text-align: center;
	width: 150px;
	font-size: 12px;
}

.menuControl-list-operate{
	background-color: #79786e;
	opacity: 0.8;
	height: 40px;
	width: 148px;
	position: absolute;
	top: 178px;
	border-bottom-left-radius: 4px;
	border-bottom-right-radius: 4px;
	text-align: center;
	padding-top: 5px;
}
.menuControl-list{
	width: 170px;
    margin: auto auto;
}
.nav-dishes-tab{
	min-height: 150px;
}
	</style>
</head>
<body>
<!-- from中必须删除	type="button"才能用valicate -->
	<div class="ky-container ">
		<div class="ky-content content-iframe">
			<div class="dishes-content">
			<input type="hidden" id="menuid" value="">
				<div class="nav-dishes-prev" style="display:none;"><i class="icon-chevron-left"></i></div>
				<ul class="nav-dishes" id="nav-dishes">
				</ul>
				<div class="nav-dishes-next" style="display:none;"><i class="icon-chevron-right"></i></div>
				
				<div class="nav-dishes-tab">
					<div id="dishDetailList" class="btn-group dishes-detail-add "  role="group" aria-label="..." style="font-size: 16px;">
					</div>
				</div>
				<div class="menuControl-list hide">
					<div class="menuControl-list-sigle">
						<img class="pop_pic" src="../images/menuControl-on.png" alt="">
						<div class="menuControl-list-content">
							<p class="menuControl-list-name"></p>
							<p>适用门店：</p>
							<div id="branchname" style="overflow: hidden; cursor: pointer;" title=""></div>
							<p></p>
						</div>
						<div class="menuControl-list-contentBottom">
							<p>
								启用时间<br>已启用
							</p>
						</div>
						<div class="menuControl-list-operate" style="display: none;"
							menuid="b72c1b66-84e0-4a06-8ca7-be0060d926c9" menuname="6.3测试菜谱"
							status="1">
							<img class="edit_menu" src="../images/menuControl-edit.png"
								alt="" title="编辑"><img
								src="../images/menuControl-copy.png" alt=""
								class="menuControl-img-margin copy_menu" title="复制"><img
								class="del_menu" src="../images/menuControl-delete.png" alt=""
								title="删除">
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
		
		
		
		<!--信息查看-->
		<div class="modal fade menuDetail-view-dialog in " id="menuDetail-view-dialog"  data-backdrop="static" >
			<div class="modal-dialog">
				<div class="modal-content">	
					<div class="modal-header">				  
				        <div class="modal-title">菜品信息</div>
				        <img src="../images/close.png" class="img-close"  data-dismiss="modal">
				    </div>
					<div class="modal-body">	
						<div class="row">
							<div class="col-xs-9 div-left">	
							<input type="hidden"	value="" class="form-control" id="menuViewDishid">	
<!-- 								<input type="hidden" value="" class="form-control" id="menuViewUnitflag"> -->
								<input type="hidden" value="" class="form-control" id="menuidInDish">
								<p><span class="sp1">菜品编号:</span><span class="sp2" id="menuViewDishno"></span></p>
								
								<p><span class="sp1">菜品名称:</span><span class="sp2" id="menuViewTitle"></span>
									<span class="sp1">库存状态:</span><span class="sp2" id="menuViewStatus"></span></p>
									<!--另一种状态的图片 menu-state-off.png-->
								<p><span class="sp1">菜品分类:</span><span class="sp2" id="menuViewDishType"></span></p>
								<p><span class="sp1">单位:</span><span class="sp2" id="menuViewUnit"></span></p>
								<p><span class="sp1">价格:</span><span class="sp2" id="menuViewPrice"></span>
									<span class="sp1">会员价:</span><span class="sp2" id="menuViewVipPrice"></span></p>
					
								<p><span class="sp1">其他:</span><span class="sp2" id="tasteAndLabel"></span></p>
							</div>
							<div class="col-xs-3 div-right">
								<img id="imgsrc" src="../images/nopic.jpg">
							</div>							
						</div>
						<div class="row">
							<p ><span class="sp1">菜品介绍:</span><span class="sp3-intro" id="menuViewIntroduction"></span></p>
						</div>
					
	
				
					</div>
					
	
				</div>
				
			</div>
		</div>

		<script>
		var count=0;
		$('.dishes-roll-left').click(function(){
			var marginl = -147.5*(++count)+'px';
			$('.dishes-comboList:eq(0)').css('margin-left',marginl);
		});
		$('.dishes-roll-right').click(function(){
			var marginl = -147.5*(--count)+'px';
			$('.dishes-comboList:eq(0)').css('margin-left',marginl);
		});
		
		
		</script>
</body>
</html>
