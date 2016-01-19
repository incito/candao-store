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
<link rel="stylesheet" href="../tools/font-awesome/css/font-awesome.css">
<link rel="stylesheet" href="../css/common.css">
<link rel="stylesheet" href="../css/index.css">
<link rel="stylesheet" href="../css/storesel.css">
<link rel="stylesheet" href="../css/menu.css">
<script src="../scripts/jquery.js"></script>
<script src="../tools/bootstrap/js/bootstrap.min.js"></script>
<script src="../scripts/projectJs/branchMenuctrl.js"></script>
<!-- <script src="../scripts/projectJs/menuoperate.js"></script> -->
<!-- <script src="../scripts/projectJs/selectStore.js"></script> -->
<script src="<%=request.getContextPath()%>/tools/calendar_diy/WdatePicker.js"></script>
</head>
<body>
	<div class="ky-container">
		<div class="ky-content content-iframe">
			<div class="menuControl-sort">
				<span class="menuControl-sort-title">菜品管理</span>
				<label class="radio-inline">
					<input type="radio" name="sorttype" value="1" checked>按状态排序
				</label>
				<label class="radio-inline">
					<input type="radio" name="sorttype" value="2">按时间排序
				</label>
				<button id="dish_ctrl" class="btn btn-menuControl-title pull-right">菜品库</button>
			</div>
			<hr class="ky-hr">
			<div class="menuControl-list">
			</div>
		</div>
	</div>
	<!-- 菜谱属性设置 -->
	<%@ include file="./menupro.jsp"%>

	<!-- 确认删除框 -->
		<div class="modal fade dialog-sm in" id="modal_confirm"  data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">	
					<div class="modal-body">
					<div class="dialog-sm-header">				  
				        <span class=" ">提示</span>
				        <img src="<%=request.getContextPath()%>/images/close-sm.png" class="img-close" data-dismiss="modal">
				    </div>
						<form action="" method="post" class="form-horizontal " name="">
							<div class="dialog-sm-info">
							<p class="p1"><img src="<%=request.getContextPath()%>/images/del-tip.png"></i>没有菜谱，跳回菜品页面？</p>
							</div>
							<div class="btn-operate">
								<button class="btn btn-cancel  " type="button" data-dismiss="modal">取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save " id="confirmok_btn" type="button" >确认</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div><!-- end delete  -->
		<!-- 确认 -->
	<div class="modal fade dialog-sm in" id="modal_confirmdel"  data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">	
					<div class="modal-body">
					<div class="dialog-sm-header">				  
				        <span class=" ">确认删除</span>
				        <img src="<%=request.getContextPath()%>/images/close-sm.png" class="img-close" data-dismiss="modal">
				    </div>
						<form action="" method="post" class="form-horizontal " name="">
							<div class="dialog-sm-info">
							<p class="p1"><img src="<%=request.getContextPath()%>/images/del-tip.png"></i>确认删除“<span id="showName"></span>”吗?</p>
							</div>
							<div class="btn-operate">
								<button class="btn btn-cancel  " type="button" data-dismiss="modal">取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save " id="dodel_btn" type="button" >确认</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
</body>
<script type="text/javascript">

//进入菜谱查看页面
function viewMenu(menuid){
	$(parent.document.all("detail")).attr("src",global_Path + "/menu/branchViewmenu?menuId="+ menuid +"");
	$("#allSearch").css("visibility","hidden");
}
</script>
</html>