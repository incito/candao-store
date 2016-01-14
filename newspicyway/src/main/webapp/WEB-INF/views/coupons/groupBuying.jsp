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
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/jquery.validate.js"></script>
	<script src="<%=request.getContextPath()%>/tools/calendar_diy/WdatePicker.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/groupBuying.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/validate-plus.js"></script>
</head>
<body>
	<form action="" method="post"  class=" " name="groupBuying-form" id="groupBuying-form">
		<input id="id" value="${grouponVO.activity.id }" type="hidden" />
		<input id="color" value="${grouponVO.activity.color }"  type="hidden" /> 
		<input id="select_branchs" value="${branch_ids}"  type="hidden" />
		<div class="ky-content preferential-content">
			<p class="preferential-class">
				<img src="../images/preferential-type5.png"/>
				<span class="preferential-name groupBuying-name">团购券</span>
				<span class="preferential-desc">为顾客提供团购服务（例：美团88抵100）</span>
			<p>
			<div class="ky-panel">
				<div class="ky-panel-title">
					券面设置
				</div>
				<div class="ky-panel-content ">
					
					<div class="form-group">
						<label class="control-label col-xs-2">活动名称：</label>
						<div class="col-xs-4">
							<div style="position:relative;">
								<input id="name" name="name" type="text" class="form-control" placeholder="最多15个字" value="${grouponVO.activity.name}"  maxlength="15" />
							</div>
						</div>
						<label class="control-label col-xs-2">卡券颜色：</label>
						<div class="col-xs-2">
							<div class="input-group" id="color-select">
								  <input type="text" class="form-control" readonly="readonly" id="color-input" name="colorinput" aria-describedby="basic-addon1"  style="background-color:${grouponVO.activity.color};">
								   <span class="input-group-addon arrow-down" ><i class="icon-chevron-down"></i></span>
							</div>	
							 <div class="color-select-box hidden">
						   		<div class="row">
						   			<div class="col-xs-4">
						   				<span id="color1"></span>
						   			</div>
						   			<div class="col-xs-4">
						   				<span id="color2"></span>
						   			</div>
						   			<div class="col-xs-4">
						   				<span id="color3"></span>
						   			</div>
						   		</div>
						   		 <div class="row">
						   			<div class="col-xs-4">
						   				<span id="color4"></span>
						   			</div>
						   			<div class="col-xs-4">
						   				<span id="color5"></span>
						   			</div>
						   			<div class="col-xs-4">
						   				<span id="color6"></span>
						   			</div>
						   		</div>
						   		<div class="row">
						   			<div class="col-xs-4">
						   				<span id="color7"></span>
						   			</div>
						   			<div class="col-xs-4">
						   				<span id="color8"></span>
						   			</div>
						   			<div class="col-xs-4">
						   				<span id="color9"></span>
						   			</div>
						   		</div>
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
						<input type="radio" name="groupType" value="1" checked>抵用券
					</label>
					<label class="radio-inline groupBuying-radio-style">
						<input type="radio" name="groupType" value="2" disabled="disabled">套餐券
					</label>
				</div>
				<div class="ky-panel-content ">
					<div class="form-group" id="type1">
						<label class="control-label col-xs-2">记账金额：</label>
						<div class="col-xs-4">
							<div class="input-group  unit-style">
								  <input id="token_amount" name="tokenamount" value="${grouponVO.groupon.token_amount }" type="text" class="form-control isPrice" aria-describedby="basic-addon1">
								   <span class="input-group-addon" id="basic-addon1"> 元</span>
							</div>								
						</div>	
						<label class="control-label col-xs-2">抵用金额：</label>
						<div class="col-xs-4">
							<div class="input-group  unit-style">
								  <input id="bill_amount" name="billamount" value="${grouponVO.groupon.bill_amount}" type="text" class="form-control isPrice" aria-describedby="basic-addon1">
								   <span class="input-group-addon" id="basic-addon1"> 元</span>
							</div>								
						</div>	
					</div>
					<div class="form-group hidden" id="type2">
						<label class="control-label col-xs-2">套餐内容：</label>
						<div class="col-xs-6">
							<input type="text" class="form-control">
						</div>			
					</div>
					<div class="form-group">
						<label class="control-label col-xs-2">有效期：</label>
						<div class="col-xs-2">
							<div class="input-group">
								  <input type="text" name="starttime" id="starttime" value="" aria-describedby="basic-addon1" readOnly onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'endtime\')}',onpicked:picked})" class="form-control bottom">
								   <span class="input-group-addon arrow-down" id="basic-addon1"><i class="icon-chevron-down"></i></span>
							</div>								
						</div>
						<label class="time-label col-xs-2">至</label>
						<div class="col-xs-2">
							<div class="input-group">
								  <input type="text" name="endtime" id="endtime" value="" aria-describedby="basic-addon1" readOnly onFocus="WdatePicker({minDate:'#F{$dp.$D(\'starttime\')}',onpicked:picked})"  class="form-control bottom" >
								  <span class="input-group-addon arrow-down" id="basic-addon1"><i class="icon-chevron-down"></i></span>
							</div>								
						</div>
						
						<label class="control-label col-xs-2">适用门店：</label>
						<div class="col-xs-3  store-radio-style">
							<label class="radio-inline ">
								<input type="radio" name="applyAll" value="1" checked="checked">所有门店
							</label>
							<label class="radio-inline">
								<input type="radio" name="applyAll" value="0">指定门店
							</label>
						</div>	
						<div class="col-xs-1  store-select hidden">
							<div class="input-group" id="store-select">
							  	<button type="button" class="btn btn-default store-select-add" id="addstore">
									<i class="icon-plus"></i>选择门店
								</button>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="ky-panel">
				<div class="ky-panel-title">
					活动介绍					
				</div>
				<div class="ky-panel-content  no-margin">
					<textarea id="activityIntroduction" class="form-control">${grouponVO.activity.activityIntroduction }</textarea> 
				</div>
			</div>
			<div class="ky-panel">
				<div class="ky-panel-title">
					使用须知					
				</div>
				<div class="ky-panel-content  no-margin ">
					<textarea id="useNotice" class="form-control">${grouponVO.activity.useNotice }</textarea> 
				</div>
			</div>
			<div class="btn-operate">
				<button class="btn btn-cancel in-btn135" onclick="window.location.href=global_Path+ '/preferential';return false;" >取消</button>
				<div  class="btn-division"></div>
				<button type="submit"  class="btn btn-save in-btn135 preferential-btn-bgcolor">确认</button>
			</div>
		</div>
	</form>
	<!-- /.modal -->
	<div class="modal fade .dishes-detailDel-dialog in " id="successPrompt"  >
		<div class="modal-dialog" style="width:310px;">
			<div class="modal-content" style="width:310px;">	
				<div class="modal-body pop-div-content" style="height:210px;">
					<br/>
						<p class="tipP"> <i class="icon-ok"></i><label id="promptMsg">保存成功</label></p>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="selectBranch.jsp"></jsp:include>	
		
	<script type="text/javascript">
			$(parent.document.all("detail")).height(document.body.scrollHeight -130); 
			$("#addstore").selectBranchPlugin({
        		'dialog_id': 'store-select-dialog',
		        'target_id':'select_branchs'
        	});
			// 已选门店
			$("#store-select").hover(function(){
				$(this).find(".popover").show();
			}, function(){
				$(this).find(".popover").hide();
			});
	</script>

	<c:if test="${isModify}">
	<script  type="text/javascript">
		var starttime="<fmt:formatDate value='${grouponVO.activity.starttime }' pattern='yyyy-MM-dd' />";
		var endtime="<fmt:formatDate value='${grouponVO.activity.endtime }' pattern='yyyy-MM-dd' />";
		var applyAllShop = ${ null!=grouponVO.activity?grouponVO.activity.applyAll:'true'};
		$("#starttime").val(starttime);
		$("#endtime").val(endtime);
		if(applyAllShop){
			$("input[name='applyAll'][value=1]").prop("checked",true);
			$("div.store-select").addClass("hidden");
		}else{
			$("input[name='applyAll'][value=0]").prop("checked",true);
			$("div.store-select").removeClass("hidden");
		}
		$(parent.document.all("allSearch")).css("visibility","hidden");
	</script>
	</c:if>

</body>
</html>
