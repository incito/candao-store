<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>在线支付</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/common.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/index.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/otherCoupon.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/tools/font-awesome/css/font-awesome.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/preferential.css">
<%@ include file="/common/resource.jsp"%>

<script src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
<script
	src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/scripts/projectJs/index.js"></script>
<script
	src="<%=request.getContextPath()%>/tools/calendar_diy/WdatePicker.js"></script>
<script
	src="<%=request.getContextPath()%>/tools/jquery-validation/jquery.validate.js"></script>
<script
	src="<%=request.getContextPath()%>/tools/jquery-validation/validate-plus.js"></script>
<script
	src="<%=request.getContextPath()%>/scripts/projectJs/preferential.js"></script>
<script
	src="<%=request.getContextPath()%>/scripts/projectJs/otherCouponMod.js"></script>
</head>
<body>
	<div class="ky-content preferential-content">
		<form action="" method="post" class=" " name="othercoupon-form"
			id="othercoupon-form">
			<p class="preferential-class">
				<img src="<%=request.getContextPath()%>/images/preferential-type6.png" /> <span
					class="preferential-name otherCoupon-name">更多优惠</span> <span
					class="preferential-desc">为顾客提供更为灵活的优惠方式（例：赠送菜品、灵活优免等）</span>
			<p>
			<div class="ky-panel">
				<div class="ky-panel-title">优免类型</div>
				<div class="ky-panel-content ">
					<div class="form-group">
						<label class="control-label col-xs-2"> <span
							class="required-span">*</span>优免类型：
						</label>
						<div class="col-xs-4">
							<div style="position: relative;">
								<select class="form-control" id="freetype"
									onchange="changeFreeType(this.value)">
								</select>
							</div>
						</div>
						<div class="payway-div hide">
							<label class="control-label col-xs-2">支付类型：</label>
							<div class="col-xs-2">
								<div class="input-group" style="padding-top: 6px;" id="payway-div">
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="ky-panel">
				<div class="ky-panel-title">券面设置</div>
				<div class="ky-panel-content ">
					<div class="form-group">
						<div class="name-div">
							<label class="control-label col-xs-2"><span
								class="required-span">*</span>活动名称：</label> <input type="hidden"
								name="id" id="id" value="">
							<div class="col-xs-4">
								<div style="position: relative;">
									<input type="text" name="name" maxlength="15" id="name"
										value="" class="form-control" placeholder="最多15个字">
								</div>
							</div>
						</div>
						<div class="company-div hide">
							<label class="control-label col-xs-2"><span
								class="required-span">*</span>单位名称：</label>
							<div class="col-xs-4">
								<div style="position: relative;">
									<input type="text" name="compnay-name" maxlength="15"
										id="compnay-name" value="" class="form-control"
										placeholder="IBM技术有限公司">
								</div>
							</div>
						</div>

						<label class="control-label col-xs-2">卡券颜色：</label>
						<div class="col-xs-2">
							<div class="input-group" id="color-select">
								<input id="color" name="color" value="" type="hidden"> <input
									type="text" name="colorinput" class="form-control"
									readonly="readonly" id="color-input"
									aria-describedby="basic-addon1"> <span
									class="input-group-addon arrow-down"><i
									class="icon-chevron-down"></i></span>
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
							<div class="col-xs-2  couponColor-remark">
								<span>卡券颜色将显示在收银端，便于结算时选择优惠</span>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="ky-panel">
				<div class="ky-panel-title">活动内容设置</div>
				<div class="ky-panel-content ">
					<div class="form-group coup-type-div hide">
						<label class="control-label col-xs-2">活动方式：</label>
						<div class="col-xs-4">
							<div class="input-group" style="padding-top: 6px;" id="activity-type-div">
								
							</div>
						</div>
					</div>
					<div class="form-group discount-div hide">
						<label class="control-label col-xs-2"><span
							class="required-span">*</span>整单折扣：</label>
						<div class="col-xs-4">
							<div class="input-group unit-style">
								<input type="text" name="discount" maxlength="3" id="discount"
									value="" class="form-control" aria-describedby="basic-addon1"
									data-toggle="tooltip" placeholder="八五折请输入8.5"> <span
									class="input-group-addon" id="basic-addon1">折</span>
							</div>
						</div>
						<div class="col-xs-2  couponColor-remark">
							<span>(0.0-10.0折扣)</span>
						</div>
					</div>
					<div class="internal-free-div hide">
						<div class="form-group">
							<label class="control-label col-xs-2">活动方式：</label>
							<div class="col-xs-2">
								<div class="input-group" style="padding-top: 6px;" id="cooperation-unit-div">
									
								</div>
							</div>
						</div>
						<div class="form-group intl-discount-div">
							<label class="col-xs-3 control-label"><span
								class="required-span">*</span>折扣：</label>
							<div class="col-xs-4 unit-style input-group">
								<div style="position: relative; display: table;">
									<input type="text" value="" class="form-control" required="required"
										aria-describedby="basic-addon1" id="intl_discount"
										name="intl_discount" maxlength="3" placeholder="八五折请输入8.5">
									<span class="input-group-addon" id="basic-addon1">折</span>
								</div>
							</div>
							<div class="col-xs-2  couponColor-remark">
								<span>(0.0-10.0折扣)</span>
							</div>
						</div>
						<div class="form-group intl-disamount-div hide">
							<label class="col-xs-3 control-label"><span
								class="required-span">*</span>减免金额：</label>
							<div class="col-xs-4 unit-style input-group">
								<div style="position: relative; display: table;">
									<input type="text" value="" class="form-control" required="required"
										aria-describedby="basic-addon2" id="dis_amount"
										name="dis_amount" maxlength="5" placeholder="100"> <span
										class="input-group-addon" id="basic-addon2">元</span>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-xs-2">是否可挂账：</label>
							<div class="col-xs-2">
								<div class="input-group" style="padding-top: 6px;">
									<label class="radio-inline "> <input type="radio"
										name="can_credit" value="1" checked="checked">是
									</label> <label class="radio-inline "> <input type="radio"
										name="can_credit" value="0">否
									</label>
								</div>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-xs-2">有效期：</label>
						<div class="col-xs-2">
							<div class="input-group">
								<input type="text" name="starttime" id="starttime" value=""
									aria-describedby="basic-addon1" readOnly
									onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'endtime\')}',onpicked:picked})"
									class="form-control bottom"> <span
									class="input-group-addon arrow-down" id="basic-addon1"><i
									class="icon-chevron-down"></i></span>
							</div>
						</div>
						<label class="time-label col-xs-2">—</label>
						<div class="col-xs-2">
							<div class="input-group">
								<input type="text" name="endtime" id="endtime" value=""
									aria-describedby="basic-addon1" readOnly
									onFocus="WdatePicker({minDate:'#F{$dp.$D(\'starttime\')}',onpicked:picked})"
									class="form-control bottom"> <span
									class="input-group-addon arrow-down" id="basic-addon1"><i
									class="icon-chevron-down"></i></span>
							</div>
						</div>
						<label class="control-label col-xs-2 label-special">适用门店：</label>
						<div class="col-xs-3  store-radio-style">
							<label class="radio-inline "> <input type="radio"
								name="applyAll" value="1" checked="checked">所有门店
							</label> <label class="radio-inline"> <input id="selectBranchs"
								name="selectBranchs" type="hidden" value="" /> <input
								type="radio" name="applyAll" value="0">指定门店
							</label>

						</div>
						<div class="col-xs-1  store-select hidden">
							<div class="input-group" id="store-select">
								<button type="button" class="btn btn-default store-select-add"
									id="addstore">
									<i class="icon-plus"></i>选择门店
								</button>
							</div>
							<label for="selectBranchs" id="selectBranchs_error"
								style="display: none;" class="popover-errorTips">请选择指定门店</label>
						</div>
					</div>
				</div>
			</div>
			<div class="ky-panel">
				<div class="ky-panel-title">活动介绍</div>
				<div class="ky-panel-content  no-margin">
					<textarea name="activityIntroduction" id="activityIntroduction" maxlength="500"
						class="form-control"></textarea>
				</div>
			</div>
			<div class="ky-panel">
				<div class="ky-panel-title">使用须知</div>
				<div class="ky-panel-content  no-margin ">
					<textarea name="useNotice" id="useNotice" maxlength="500" class="form-control"></textarea>
				</div>
			</div>
			<!-- 没有判断条件的情况 -->
			<div class="btn-operate addmod-btn">
				<button class="btn btn-cancel in-btn135"
					onclick="window.location.href=global_Path+ '/preferential';return false;">取消</button>
				<div class="btn-division"></div>
				<button class="btn btn-save in-btn135 preferential-btn-bgcolor"
					onclick="saveOtherCoupon();">确认</button>

			</div>
			<div class="btn-operate view-btn hide">
				<button class="btn btn-cancel in-btn135"
					id="btn_cancle_specialStamp">返回</button>
				<div class="btn-division"></div>
				<button class="btn btn-save in-btn135 preferential-btn-bgcolor"
					id="btn_to_modify">编辑</button>
			</div>
		</form>
	</div>
	<div class="modal fade " id="dish-select-dialog" aria-hidden="true">
	</div>

	<div class="modal fade storeSelect-dialog in " id="store-select-dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header addDelicon">
					<img data-dismiss="modal" class="img-close"
						src="/newspicyway/images/close.png">
				</div>
				<div class="modal-body">
					<div class="row store-select-title">
						<div class="col-xs-9">
							选择门店<font id="store-count"></font>
						</div>
						<div class="col-xs-3 pull-right">
							<label class="radio-inline"> <input type="radio"
								value="1" name="checkAll">全选
							</label> <label class="radio-inline"> <input type="radio"
								value="0" name="checkAll">全不选
							</label>
						</div>
					</div>
					<hr>
					<table class="table store-select-content">
					</table>
					<div class="btn-operate">
						<button class="btn btn-cancel in-btn135" id="store-select-cancel">取消</button>
						<div class="btn-division"></div>
						<button class="btn btn-save in-btn135 preferential-btn-bgcolor"
							id="store-select-confirm">确认</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 操作提示 -->
	<div class="modal fade sendMsgState in " id="successPrompt"
		style="z-index: 99990">
		<div class="modal-dialog" style="width: 310px;">
			<div class="modal-content">
				<div class="modal-body pop-div-content">
					<p>
						<i class="icon-success"></i>
					</p>
					<p class="tipP">
						<label id="promptMsg">保存成功</label>
					</p>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var preferentialId = "";
		var applyAllShop = false;
		var selectShops = [];
		
		var freeType = "07";//优免类型
		var activityType = "0";//手工优免活动方式
		var cooperationNnit="0";//合作单位活动方式
		var payway=false;//在线支付 支付类型
		$(function() {
			$("#btn_cancle_specialStamp").click(function() {
				window.location.href = global_Path + '/preferential';
				return false;
			});
			$("#othercoupon-form").validate({
				submitHandler : function(form) {
// 					saveOtherCoupon();
				},
				rules : {
					name : {
						required : true,
						maxlength : 15,
					},
					discount : {
						required : true,
						number : true,
						isDiscount : true
					}

				},
				messages : {
					name : {
						required : "请输入活动名称",
						maxlength : "长度不能超过15",
					},
					discount : {
						required : "请输入折扣值",
						number : "请输入合法的数字"
					},
					starttime : {
						required : "请选择有效期开始日期",
					},
					endtime : {
						required : "请选择有效期结束日期",
					}
				}
			});
			preferentialId = '${id}';
			if (preferentialId != "") {//查看或编辑
				var isModify = '${isModify}';
				var resultList = '${resultList}';
				console.log(resultList);
				var obj = JSON.parse(resultList)[0];
				$("#id").val(preferentialId);
				$("#name").val(obj.name);
				$("#starttime").val(obj.starttime);
				$("#endtime").val(obj.endtime);
				$("#activityIntroduction").val(obj.activityIntroduction);
				$("#useNotice").val(obj.useNotice);
				$("#color").val(obj.color);
				freeType = obj.type;//优免类型
				//在线支付
				$("#discount").val(obj.discount);//折扣
				payway = obj.payway;//支付方式
				//手工优免
				activityType = obj.free_reason;//手工优免活动方式
				//合作单位
				$("#compnay-name").val(obj.name);
				cooperationNnit = obj.cooperationType;//合作单位活动方式
				
				if(obj.can_credit){//是否挂账
					$("input[type='radio'][name='can_credit'][value='1']").prop("checked", true);
				}else{
					$("input[type='radio'][name='can_credit'][value='0']").prop("checked", true);
				}
				$("#intl_discount").val(obj.discount);//折扣
				$("#dis_amount").val(obj.amounts);//金额
				$("#color-input").css("background-color", $("#color").val());//颜色值
				
				applyAllShop = obj.apply_all;
				if (applyAllShop == false) {
					selectShops = obj.branchInfos;
				}
				selBranch();
				if (isModify == "true") {
					//修改
					$(".addmod-btn").removeClass("hide");
					$(".view-btn").addClass("hide");
					$("#freetype").prop("disabled", true);
				} else {
					//查看
					$(".addmod-btn").addClass("hide");
					$(".view-btn").removeClass("hide");
					$("input, select, textarea").prop("disabled", true).css("disabled", "disabled").off();
					$("#addstore").prop("disabled", true);
					$("#color-select").unbind("click").children("span").remove();
				}
			} 
			getFreeType();//优免类型
			getActivityType();//手工优免活动方式
			getCooperationNnit();//合作单位活动方式
			getPayway();//在线支付 支付类型
			changeFreeType(freeType);
			
			$("#btn_to_modify").click(function(){
				window.location.href = "<%=request.getContextPath()%>/otherCoupon/getCoupon?id="
										+ preferentialId + "&isModify=true";
			});
			$("img.img-close").hover(function() {
				$(this).attr("src", global_Path + "/images/close-active.png");
			}, function() {
				$(this).attr("src", global_Path + "/images/close-sm.png");
			});
			refreshBranchRole();
		});
		//根据当前 是分店还是总店，判断某些权限是否显示
		//分店 不能新增、编辑、删除
		function refreshBranchRole() {
			if (isbranch == 'Y') {
				$("#btn_to_modify").prev().remove();
				$("#btn_to_modify").remove();
			}
		}
	</script>
</body>
</html>
