<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>团购券</title>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/preferential.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/font-awesome/css/font-awesome.css">
	<%@ include file="/common/resource.jsp" %>
	<script src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
	<script src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/index.js"></script>

</head>
<body>

		<input id="id" value="${grouponVO.activity.id }" type="hidden" />
		<input id="color" value="${grouponVO.activity.color }"  type="hidden" /> 
		<input id="select_branchs" value="${branch_ids}"  type="hidden" />
		<div class="ky-content preferential-content">
			<p class="preferential-class">
				<img src="../images/preferential-type5.png"/>
				<span class="preferential-name groupBuying-name">团购券</span>
				<span class="preferential-desc">为顾客提供团购服务</span>
			<p>
			<div class="ky-panel">
				<div class="ky-panel-title">
					券面设置
					
				</div>
				<div class="ky-panel-content ">
					
					<div class="form-group">
						<label class="control-label col-xs-2">活动名称：</label>
						<div class="col-xs-4">
							<input type="text" name="name" maxlength="15" id="name" value="${grouponVO.activity.name }" class="form-control" disabled="disabled"/>
						</div>
						<label class="control-label col-xs-2">卡券颜色：</label>
						<div class="col-xs-2">
							<div class="input-group" id="color-select">
								<input type="text" name="colorinput" style="background-color:${grouponVO.activity.color};" class="form-control" readonly="readonly" id="color-input"  >
							</div>
						</div>	
						<div class="col-xs-2  couponColor-remark"><span>卡券颜色将显示在收银端，便于结算时选择优惠</span></div>
					</div>
				</div>
			</div>
			<div class="ky-panel">
				<div class="ky-panel-title">
					活动内容设置  <span class="ky-panel-titleS">|</span>	团购类型：
					<label class="radio-inline groupBuying-radio-style">
						抵用券
					</label>
				</div>
				<div class="ky-panel-content ">
					<div class="form-group" id="type1">
						<label class="control-label col-xs-2">记账金额：</label>
						<div class="col-xs-4">
							<div class="input-group  unit-style">
								<input type="text" value="${grouponVO.groupon.token_amount}" class="form-control" disabled="disabled"/>
							   	<span class="input-group-addon" id="basic-addon1"> 元</span>
							</div>								
						</div>		
						<label class="control-label col-xs-2">抵用金额：</label>
						<div class="col-xs-4">
							<div class="input-group  unit-style">
								<input type="text" value="${grouponVO.groupon.bill_amount}" class="form-control" disabled="disabled"/>
							   	<span class="input-group-addon" id="basic-addon1"> 元</span>
							</div>								
						</div>
				
					</div>
					
					<div class="form-group">
						<label class="control-label col-xs-2">有效期：</label>
						<div class="col-xs-2">
							<div class="input-group">
								  <input type="text" name="starttime" id="starttime" value="<fmt:formatDate value='${grouponVO.activity.starttime }'  pattern='yyyy-MM-dd'/>" readOnly class="form-control bottom">
							</div>								
						</div>
						<label class="time-label col-xs-2">至</label>
						<div class="col-xs-2">
							<div class="input-group">
								  <input type="text" name="endtime" id="endtime" value="<fmt:formatDate value='${grouponVO.activity.endtime }'  pattern='yyyy-MM-dd'/>" readOnly  class="form-control bottom"  >
							</div>								
						</div>
						<label class="control-label col-xs-2">适用门店：</label>
						<div class="col-xs-3  store-radio-style">
							<c:choose>
								<c:when test="${grouponVO.activity.applyAll }">
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
					<textarea name="activityIntroduction" id="activityIntroduction" class="form-control" disabled="disabled">${grouponVO.activity.activityIntroduction }</textarea>
				</div>
			</div>
			<div class="ky-panel">
				<div class="ky-panel-title">使用须知</div>
				<div class="ky-panel-content  no-margin " >
					<textarea name="useNotice" id="useNotice" class="form-control" disabled="disabled">${grouponVO.activity.useNotice }</textarea>
				</div>
			</div>
			<div class="btn-operate">
				<button class="btn btn-cancel in-btn135" id="btn_cancle_groupon">返回</button>
<%-- 				<c:if test="'<%=Constant.ISBRANCH%>'==N">  --%>
				<div class="btn-division"></div>
				<button class="btn btn-save in-btn135 preferential-btn-bgcolor" id="btn_to_modify">编辑</button>
<%-- 				</c:if> --%>
			</div>
		</div>
	
	<script type="text/javascript">
			
	
			$(parent.document.all("detail")).height(document.body.scrollHeight-130); 
			$("#btn_cancle_groupon").click(function(){
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
				window.location.href = "<%=request.getContextPath()%>/preferential/toGroupBuying?id=${grouponVO.activity.id }&isModify=true";
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
