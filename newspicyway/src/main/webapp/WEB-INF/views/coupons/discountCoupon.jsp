<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>折扣券</title>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/preferential.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/font-awesome/css/font-awesome.css">
	<%@ include file="/common/resource.jsp" %>  

	<script src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
	<script src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/index.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/preferential.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/discount.js"></script>
	<script src="<%=request.getContextPath()%>/tools/calendar_diy/WdatePicker.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/jquery.validate.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/validate-plus.js"></script>

	
	<style type="text/css">
		.add-dish-select{
			border: 1px solid #ccc;
		    cursor: pointer;
		    height: 32px;
		    padding-top: 4px;
		    text-align: center;
		    vertical-align: middle;
		    width: 368px;
		    font-size:14px;
		    color:#666;
		}
		.add-dish-select img{
			width:18px;
			height:18px;
		}
	
		#selecedDishes .storesDiv li {
			width:130px;
			height:16px;
		}
		.add-dish-select:hover {
			color:rgb(0, 211, 172) !important;
		}
	</style>
</head>
<body>
		<div class="ky-content preferential-content">
			<form action="" method="post"  class=" " name="discount-form" id="discount-form">
			<p class="preferential-class">
				<img src="../images/preferential-type2.png"/>
				<span class="preferential-name discountCoupon-name">折扣券</span>
				<span class="preferential-desc">为顾客提供整单折扣服务（不包括设置的特定菜品）（例：整单8折除酒水）</span>
			<p>
			<div class="ky-panel">
				<div class="ky-panel-title">
					券面设置
				</div>
				<div class="ky-panel-content ">
					<div class="form-group">
						<label class="control-label col-xs-2">活动名称：</label>
						<input type="hidden" name="id" id="id" value="${discountTicket.preferentialActivity.id }">
						<div class="col-xs-4">
							<div style="position:relative;">
								<input type="text" name="name" maxlength="15" id="name" value="${discountTicket.preferentialActivity.name }" class="form-control" placeholder="最多15个字"/>
							</div>
						</div>
						<label class="control-label col-xs-2">卡券颜色：</label>
						<div class="col-xs-2">
							<div class="input-group" id="color-select">
								<input id="color" name="color" value="${discountTicket.preferentialActivity.color }" type="hidden"/>
								  <input type="text" name="colorinput"  class="form-control" readonly="readonly" id="color-input" aria-describedby="basic-addon1" >
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
					活动内容设置
					
				</div>
				<div class="ky-panel-content ">
					<div class="form-group">
						<label class="control-label col-xs-2">整单折扣：</label>
						<div class="col-xs-4">
							<div class="input-group unit-style">
							  	<input type="text" name="discount"  maxlength="3" id="discount" value="${discountTicket.discount}" class="form-control" aria-describedby="basic-addon1" data-toggle="tooltip" placeholder="八五折请输入8.5" >
							   	<span class="input-group-addon" id="basic-addon1">折</span>
							</div>								
						</div>
						<label class="control-label col-xs-2 label-special">不参与折扣菜品：</label>
						<div id="selecedDishes" class="col-xs-4" >
							<input type="hidden" name="dishesId" id="dishesId">
							<!-- <input type="text" name="dishesName" id="dishesName" class="form-control col-xs-2"  data-toggle="tooltip" title=""> -->
							<div class="add-dish-select col-xs-1" >
								<img alt="" src="<%=request.getContextPath()%>/images/add.png" />
							</div>
								
						</div>	
					</div>
					<div class="form-group">
						<label class="control-label col-xs-2">有效期：</label>
						<div class="col-xs-2">
							<div class="input-group">
							<script type="text/javascript">
							</script>
							<c:choose>
								<c:when test="${empty discountTicket.preferentialActivity.starttime || 'null'==discountTicket.preferentialActivity.starttime}">
									<input type="text" name="starttime" id="starttime" value="" aria-describedby="basic-addon1" readOnly onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'endtime\')}',onpicked:picked})" class="form-control bottom">
								</c:when>
								<c:otherwise>
									<input type="text" name="starttime" id="starttime" value="${discountTicket.preferentialActivity.starttime.time}" aria-describedby="basic-addon1" readOnly onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'endtime\')}',onpicked:picked})" class="form-control bottom">
								</c:otherwise>
							</c:choose>  
								   <span class="input-group-addon arrow-down" id="basic-addon1"><i class="icon-chevron-down"></i></span>
							</div>								
						</div>
						<label class="time-label col-xs-2">—</label>
						<div class="col-xs-2">
							<div class="input-group">
								<c:choose>
								<c:when test="${empty discountTicket.preferentialActivity.endtime || 'null'==discountTicket.preferentialActivity.endtime}">
									<input type="text" name="endtime" id="endtime" value="" aria-describedby="basic-addon1" readOnly onFocus="WdatePicker({minDate:'#F{$dp.$D(\'starttime\')}',onpicked:picked})" class="form-control bottom">
								</c:when>
								<c:otherwise>
									<input type="text" name="endtime" id="endtime" value="${discountTicket.preferentialActivity.endtime.time}" aria-describedby="basic-addon1" readOnly onFocus="WdatePicker({minDate:'#F{$dp.$D(\'starttime\')}',onpicked:picked})" class="form-control bottom">
								</c:otherwise>
							</c:choose> 
								  <span class="input-group-addon arrow-down" id="basic-addon1"><i class="icon-chevron-down"></i></span>
							</div>								
						</div>
						<label class="control-label col-xs-2 label-special" >适用门店：</label>
						<div class="col-xs-3  store-radio-style">
							<label class="radio-inline ">
								<input type="radio" name="applyAll" value="1" checked="checked">所有门店
							</label>
							<label class="radio-inline">
								<input id="selectBranchs" name="selectBranchs"  type="hidden" value=""/> 
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
					<textarea name="activityIntroduction" id="activityIntroduction" class="form-control">${discountTicket.preferentialActivity.activityIntroduction }</textarea>
				</div>
			</div>
			<div class="ky-panel">
				<div class="ky-panel-title">
					使用须知					
				</div>
				<div class="ky-panel-content  no-margin ">
					<textarea name="useNotice" id="useNotice"  class="form-control">${discountTicket.preferentialActivity.useNotice }</textarea>
				</div>
			</div>
			<c:if test="${preferentialId == '' or isModify == true}">
				<div class="btn-operate">
					<button class="btn btn-cancel in-btn135" onclick="window.location.href=global_Path+ '/preferential';return false;">取消</button>
					<div  class="btn-division"></div>
					<button class="btn btn-save in-btn135 preferential-btn-bgcolor" type="submit" />确认</button>
				</div>
				</form>
			</c:if>
			<c:if test="${preferentialId != '' and isModify == false}">
			</form>
				<div class="btn-operate">
					<button class="btn btn-cancel in-btn135" id="btn_cancle_specialStamp">返回</button>
<%-- 					<c:if test="'<%=Constant.ISBRANCH%>'==N">  --%>
					<div class="btn-division"></div>
					<button class="btn btn-save in-btn135 preferential-btn-bgcolor" id="btn_to_modify">编辑</button>
				
<%-- 					 </c:if> --%>
			</div>
			</c:if>
			
		</div>
		
		<div class="modal fade " id="dish-select-dialog" aria-hidden="true">
		</div>
		
		<div class="modal fade storeSelect-dialog in " id="store-select-dialog">
			<div class="modal-dialog">
				<div class="modal-content">	
					<div class="modal-header addDelicon">				  
				        <img data-dismiss="modal" class="img-close" src="/newspicyway/images/close.png">
				    </div>
					<div class="modal-body">
						<div class="row store-select-title">
							<div class="col-xs-9">选择门店<font id="store-count"></font></div>
							<div class="col-xs-3 pull-right">
								<label class="radio-inline">
									<input type="radio" value="1" name="checkAll">全选
								</label>
								<label class="radio-inline">
									<input type="radio" value="0" name="checkAll">全不选
								</label>
							</div>
						</div>
						<hr>
						<table class="table store-select-content">
						</table>
						<div class="btn-operate">
							<button class="btn btn-cancel in-btn135" id="store-select-cancel">取消</button>
							<div  class="btn-division"></div>
							<button class="btn btn-save in-btn135 preferential-btn-bgcolor" id="store-select-confirm">确认</button>
						</div>
					</div>
				</div>
			</div>
		</div>
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
		<script type="text/javascript">
			
			$(".add-dish-select").hover(function(){
				$(this).find("img").attr("src",global_Path + "/images/addhover.png");
			}, function(){
				$(this).find("img").attr("src",global_Path + "/images/add.png");
			});
			$(parent.document.all("detail")).height(document.body.scrollHeight -130); 
			$("#btn_cancle_specialStamp").click(function(){
				window.location.href=global_Path+ '/preferential';
				return false;
			});
			$("#discount-form").validate({
				submitHandler: function(form){      
				      save();     
				},
				rules : {
					name : {
						required : true,
						maxlength : 15,
					},
					discount : {
						required : true,
						number:true,
						isDiscount:true
					},
					storeselect : {
						required : true
					}
					
				},
				messages : {
					name : {
						required : "请输入活动名称",
						maxlength : "长度不能超过15",
					},
					discount : {
						required : "请输入折扣值",
						number: "请输入合法的数字"
					},
					starttime : {
						required : "请选择有效期开始日期",
					},
					storeselect : {
						required : "请选择指定门店"
					},
					endtime : {
						required : "请选择有效期结束日期",
					}
				}
			});
			
			var isModify = ${isModify};
			var noDiscountDish=[];
			var applyAllShop = false;
			var selectShops = [];
			$(".add-dish-select").click(function(){
				if(!isDishLoaded){
					$("#dish-select-dialog").load("loadDishSelect");
					isDishLoaded = true;
				} else {
					$("#dish-select-dialog").modal("show");
					$(".panel-collapse.in").removeClass("in").addClass("collapse").height("0px");
					$(".panel-collapse.collapse").eq(0).removeClass("collapse").addClass("in").height("auto");
					var updateDiscountDish = discountTicket.noDiscountDish;
					$("#dish-select-dialog").find("input[type='checkbox']").prop("checked",false);
					if( undefined != updateDiscountDish){
						$.each(updateDiscountDish, function(i, dish){
							var checkboxId;
							if(dish.unit && (dish.unitflag == false||dish.unitflag == "false")){
								checkboxId = dish.dish+"("+dish.unit+")";
							} else {
								checkboxId = dish.dish;
							}
							var dishCheck = $("#dish-select-dialog").find("input[type='checkbox'][id='dish_"+checkboxId+"']");
							dishCheck.prop("checked",true);
						});
					}
					
					generalDishTitle();
				}
				
			});
			<c:if test="${preferentialId != ''}">
				noDiscountDish = ${discountTicket.noDiscountDish};
				applyAllShop = ${discountTicket.preferentialActivity.applyAll};
				<c:if test="${discountTicket.preferentialActivity.applyAll == false}">
					selectShops = ${discountTicket.branchs};
				</c:if>
			</c:if>
			var preferentialId = "${preferentialId}";
			if(isModify == false && preferentialId != ""){
				$("input, select, textarea").prop("disabled", true).css("disabled", "disabled").off();
				$("#addstore").prop("disabled", true);
				$(".add-dish-select").off();
				$("#color-select").unbind("click").children("span").remove();
			}
			$("#btn_to_modify").click(function(){
				window.location.href = "<%=request.getContextPath()%>/preferential/toDiscountCoupon?id=${discountTicket.preferentialActivity.id }&isModify=true";
			});
			$(parent.document.all("allSearch")).css("visibility","hidden");
			$("img.img-close").hover(function(){
			 	$(this).attr("src",global_Path+"/images/close-active.png");	 
			},function(){
					$(this).attr("src",global_Path+"/images/close-sm.png");
			});
			
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
