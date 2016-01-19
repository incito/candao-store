<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>优惠管理-特价券</title>
	<meta name="description" content="">
	<meta name="keywords" content="">
	<link href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/preferential.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/font-awesome/css/font-awesome.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/json2.js"></script>
	<%@ include file="/common/resource.jsp" %>  

	<script src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
	<script src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/index.js"></script>
	<!-- <script src="<%=request.getContextPath()%>/scripts/projectJs/preferential.js"></script> -->

	<style type="text/css">
		.add-dish-select{
			border: 1px solid #ccc;
		    cursor: pointer;
		    height: 32px;
		    padding-bottom: 2px;
		    padding-top: 2px;
		    text-align: center;
		    vertical-align: middle;
		    width: 368px;
		}
		.add-dish-select img{
			height: 18px;
		    margin-top: 4px;
		    width: 18px;
		}
		.p_0.isPrice {
			width:80px;
			height: 23px;
		}
		/* .selected-dish-list div{
			background-color:#f8f8f8;
		} */
		.selected-dish-list{
			border-bottom: 1px solid #dfdfdf;
		}
		
		.selected-dish-list .p_0{
			padding-top: 0px;
			padding-bottom: 0px;
			margin-top:-2px;
		}
		.selected-dish-list .glyphicon{
			margin-right:25px;
			top:0px;
		}
		.selected-dish-list .col-xs* .input-group{
			height:30px;
		}
		.selected-dish-list .input-group-addon{
			height:23px;
		}
		.selected-dish-list .h24{
			/* height:30px; */
			padding-left: 15px;
			font-size:12px;
		}
		
		
		/** 操作按钮移动到上面方才显示 **/
		.selected-dish-list .operate{
			height:30px;
			display: none;
			min-width: 100px
		}
		
		 .selected-dish-list:HOVER  div{
			background-color: rgb(215,215,215)
		}
		.selected-dish-list:HOVER .operate{
			display: block;
		} 
		
		.selected-dish-list .clear{
			display:none;
		}
		.selected-dish-list .input-group-addon{
			width:35%;
			text-align: left;
		}
		
		
		.ky-addDishes-empty{
			float: right;
			margin-top: 0;
			margin-bottom: 0;
		}
		.ky-addDishes-content .checkbox-inline{
			margin:0;
		}
		.ky-addDishes-content .panel-heading{
			padding-top: 5px;
			padding-bottom: 5px;
		}
		.ky-addDishes-content .panel-title{
			font-size: 14px;
		}
		.ky-addDishes-content .dish-label{
			font-size:12px;
		}
		.addDishes-btn-bgcolor{
			background-color: #ff5500;
		}
		.store-count{
			color:red;
		}
		
		#dish-selected-list {
			border:1px solid #dfdfdf;
			padding-left: 0;
    		padding-right: 0;
    		margin-left: 15px;
    		line-height: 30px;
    		color:#505050;
		}
		#dish-selected-list .selected-dish-list:last-child{
			border-bottom:0;
		}
	</style>

</head>
<body>
	<div class=" ky-content preferential-content">
		<form action="" method="post"  class=" " name="add-form" id="add-form">
			<p class="preferential-class">
				<img src="<%=request.getContextPath()%>/images/preferential-type1.png" /> <span
					class="preferential-name specialStamp-name">特价券</span> <span
					class="preferential-desc">为某一菜品提供特价服务</span>
			<p>
			<div class="ky-panel">
				<div class="ky-panel-title">券面设置</div>
				<div class="ky-panel-content " style="min-height:30px;">

					<div class="form-group">
						<label class="control-label col-xs-2">活动名称：</label>
						<div class="col-xs-4">
							<input type="text" name="name" maxlength="15" id="name" value="${specialStampVO.activity.name }" class="form-control" disabled="disabled"/>
						</div>
						<label class="control-label col-xs-2">卡券颜色：</label>
						<div class="col-xs-2">
							
							<div class="input-group" id="color-select">
								  <input type="text" name="colorinput" style="background-color:${specialStampVO.activity.color};" class="form-control" readonly="readonly" id="color-input"  >
							</div>	
						</div>
						<div class="col-xs-2 couponColor-remark">
							<span>卡券颜色将显示在收银端,便于结算时选择优惠。</span>
						</div>
					</div>
				</div>
			</div>
			<div class="ky-panel">
				<div class="ky-panel-title">活动内容设置</div>
				<div class="ky-panel-content ">
					
					<div class="form-group clearfix" >
						<label class="control-label col-xs-2">特价菜品：</label>
						<div class="col-xs-9"  id="dish-selected-list">
							<c:forEach items="${specialStampVO.stamps }" var="stamp">
								<div class="selected-dish-list clearfix">
								<c:choose>
									<c:when test='${(stamp.unit==null or stamp.unit=="") or (stamp.unitflag==true or stamp.unitflag=="true") }'>
										<div class="col-xs-6 h24">${stamp.dish_title }</div>
									</c:when>
									<c:otherwise>
										<div class="col-xs-6 h24">${stamp.dish_title}(${stamp.unit})</div>
									</c:otherwise>
								</c:choose>
										<div class="col-xs-4" style="height:30px;">
								    	<label style=" font-weight: normal;margin-bottom: 0; width: 80px;" >${stamp.price}</label>
								    	<span style="color: #333;font-size: 14px;" > 元/${stamp.unit}</span>
								    </div>
								    <div class="col-xs-2" style="text-align:right;padding:0px 10px;">
								    	<span style='width:100%'>&nbsp;</span>
								    </div>
								</div>
								 
							</c:forEach>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-xs-2">有效期：</label>
						<div class="col-xs-2">
							<div class="input-group">
								  <input type="text" name="starttime" id="starttime" value="<fmt:formatDate value='${specialStampVO.activity.starttime }'  pattern='yyyy-MM-dd'/>" readOnly class="form-control bottom">
							</div>								
						</div>
						<label class="time-label col-xs-2">—</label>
						<div class="col-xs-2">
							<div class="input-group">
								  <input type="text" name="endtime" id="endtime" value="<fmt:formatDate value='${specialStampVO.activity.endtime }'  pattern='yyyy-MM-dd'/>" readOnly  class="form-control bottom"  >
							</div>								
						</div>
						<label class="control-label col-xs-2">适用门店：</label>
						<div class="col-xs-3  store-radio-style">
							<c:choose>
								<c:when test="${specialStampVO.activity.applyAll }">
									<label class="radio-inline ">
										<input type="radio" name="applyAll" value="1" checked="checked" disabled="disabled">所有门店
									</label>
									<label class="radio-inline">
										<input type="radio" name="applyAll" value="0" disabled="disabled">指定门店
									</label>
									</div>	
								</c:when>
								<c:otherwise>
									<label class="radio-inline ">
										<input type="radio" name="applyAll" value="1" disabled="disabled" >所有门店
									</label>
									<label class="radio-inline">
										<input type="radio" name="applyAll" value="0" checked="checked" disabled="disabled">指定门店
									</label>
									</div>	
									<div class="col-xs-6  store-select ">
										<div class="input-group" id="store-select">
											<button type="button" class="btn btn-default store-select-add" id="addstore">
												<i class="icon-plus"></i>选择门店
											</button>
										</div>
									</div>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			
			<div class="ky-panel">
				<div class="ky-panel-title">活动介绍</div>
				<div class="ky-panel-content  no-margin" >
					<textarea name="activityIntroduction" id="activityIntroduction" class="form-control" disabled="disabled">${specialStampVO.activity.activityIntroduction }</textarea>
				</div>
			</div>
			<div class="ky-panel">
				<div class="ky-panel-title">使用须知</div>
				<div class="ky-panel-content  no-margin " >
					<textarea name="useNotice" id="useNotice" class="form-control" disabled="disabled">${specialStampVO.activity.useNotice }</textarea>
				</div>
			</div>
		</form>
			<div class="btn-operate">
				
				<button class="btn btn-cancel in-btn135" id="btn_cancle_specialStamp">返回</button>
<%-- 				<c:if test="'<%=Constant.ISBRANCH%>'==N">  --%>
				<div class="btn-division"></div>
				<button class="btn btn-save in-btn135 preferential-btn-bgcolor" id="btn_to_modify">编辑</button>
<%-- 				</c:if> --%>
			</div>
	</div>
	<script type="text/javascript">
		
	//$("#dish-selected-list").parent().height($("#dish-selected-list").height()); //防止在编辑优惠的时候，有优惠菜品的时候，会错乱布局
	
		$("#btn_cancle_specialStamp").click(function(){
			window.location.href=global_Path+ '/preferential';
			return false;
		});
		var branch_names="${branch_names}"; //优惠门店的名称
		var branchArray=[];
		if(branch_names != ""){
			branchArray=branch_names.split("、");
		}
		
		if(branchArray.length > 0){
			var ul = $("<ul/>").addClass("storesDiv");
			$.each(branchArray,function(i,v){
				var name = v;
				ul.append("<li>"+name+"</li>")
			});
			var top = ileft = iwidth ="";
			if(branchArray.length >= 3){
				iwidth = "460px";
				ileft = "-155px";
				
			}
			var div = $("<div>").addClass("popover fade bottom in").css({
				width : iwidth,
				top : "38px",
				left: ileft
			}).append('<div class="arrow" style="left: 50%;"></div>');
			div.append(ul);
			$("#store-select").append(div);
			$("#addstore").text("已选中"+branchArray.length + "家店").addClass("selectBranch");
		}
		// 已选门店
		$("#store-select").hover(function(){
			$(this).find(".popover").show();
		}, function(){
			$(this).find(".popover").hide();
		});
		
		$("#btn_to_modify").click(function(){
			window.location.href = "<%=request.getContextPath()%>/preferential/toSpecialStamp?id=${specialStampVO.activity.id }&isModify=true";
		});
		$(parent.document.all("allSearch")).css("visibility","hidden");
		
		
		//根据当前 是分店还是总店，判断某些权限是否显示
		//分店 不能新增、编辑、删除
		function refreshBranchRole(){
			if(isbranch=='Y'){ 
				$("#btn_to_modify").prev().remove();
				$("#btn_to_modify").remove();
			}
		}
		
		refreshBranchRole();
	</script>
</body>
</html>
 

