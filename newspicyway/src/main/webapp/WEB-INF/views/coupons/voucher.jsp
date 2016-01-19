<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>代金券</title>
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
	<script src="<%=request.getContextPath()%>/scripts/projectJs/voucher.js"></script>
	<script src="<%=request.getContextPath()%>/tools/calendar_diy/WdatePicker.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/jquery.validate.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/validate-plus.js"></script>

</head>
<body>
		<div class="ky-content preferential-content">
			<form action="" method="post"  class=" " name="discount-form" id="discount-form">
			<p class="preferential-class">
				<img src="../images/preferential-type3.png"/>
				<span class="preferential-name cashCoupon-name">代金券</span>
				<span class="preferential-desc">为顾客提供减免服务（例：30元代金券）</span>
			<p>
			<div class="ky-panel">
				<div class="ky-panel-title">
					券面设置
					
				</div>
				<div class="ky-panel-content ">
					
					<div class="form-group">
						<label class="control-label col-xs-2">活动名称：</label>
						<input type="hidden" name="id" id="id" value="${voucher.preferentialActivity.id }">
						<div class="col-xs-4">
							<div style="position:relative;">
								<input type="text" name="name" maxlength="15" id="name" value="${voucher.preferentialActivity.name }" class="form-control" placeholder="最多15个字"/>
							</div>
						</div>
						<label class="control-label col-xs-2">卡券颜色：</label>
						<div class="col-xs-2">
							<div class="input-group" id="color-select">
								<input id="color" name="color" value="${voucher.preferentialActivity.color }" type="hidden"/>
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
							<label class="control-label col-xs-2">减免金额：</label>
							<div class="col-xs-4">
								<div class="input-group  unit-style">
									  <input type="text" class="form-control isPrice" id="amount" value="${voucher.amount}" name="amount" aria-describedby="basic-addon1">
									   <span class="input-group-addon" id="basic-addon1"> 元</span>
								</div>								
							</div>
						</div>
					<div class="form-group">
						<label class="control-label col-xs-2">有效期：</label>
						<div class="col-xs-2">
							<div class="input-group">
							     
							        <c:choose>
								    <c:when test ="${voucher.preferentialActivity.starttime != 'null'}">
								       <input type="text" name="starttime" id="starttime"  value="${voucher.preferentialActivity.starttime.time}" readOnly onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'endtime\')}',onpicked:picked})" class="form-control bottom" aria-describedby="basic-addon1">
								    </c:when>
								   <c:otherwise>  
								       <input type="text" name="starttime" id="starttime"    readOnly onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'endtime\')}',onpicked:picked})" class="form-control bottom" aria-describedby="basic-addon1">
								   </c:otherwise>
								  </c:choose>
                                 <span class="input-group-addon arrow-down" id="basic-addon1"><i class="icon-chevron-down"></i></span>
							</div>								
						</div>
						<label class="time-label col-xs-2">—</label>
						<div class="col-xs-2">
							<div class="input-group">
							
							    <c:choose>
								    <c:when test ="${voucher.preferentialActivity.starttime != 'null'}">
								        <input type="text" name="endtime" id="endtime" value="${voucher.preferentialActivity.endtime.time}" readOnly onFocus="WdatePicker({minDate:'#F{$dp.$D(\'starttime\')}',onpicked:picked})"  class="form-control bottom"  aria-describedby="basic-addon1" >
								    </c:when>
								   <c:otherwise>  
								        <input type="text" name="endtime" id="endtime" readOnly onFocus="WdatePicker({minDate:'#F{$dp.$D(\'starttime\')}',onpicked:picked})"  class="form-control bottom"  aria-describedby="basic-addon1" >
								   </c:otherwise>
								  </c:choose>
								 
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
								<input id="selectBranchs" name="selectBranchs"  type="hidden" value=""/> 
							</label>
				
						</div>	
						<div class="col-xs-1  store-select hidden">
							<div class="input-group" id="store-select">
							  	<button type="button" class="btn btn-default store-select-add" name="addstore" id="addstore">
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
					<textarea name="activityIntroduction" id="activityIntroduction"  class="form-control">${voucher.preferentialActivity.activityIntroduction }</textarea>
				</div>
			</div>
			<div class="ky-panel">
				<div class="ky-panel-title">
					使用须知					
				</div>
				<div class="ky-panel-content  no-margin ">
					<textarea name="useNotice" id="useNotice"  class="form-control">${voucher.preferentialActivity.useNotice }</textarea>
				</div>
			</div>
			<c:choose>
				<c:when test="${preferentialId == '' or isModify == true}">
					<div class="btn-operate">
						<button class="btn btn-cancel in-btn135" onclick="window.location.href=global_Path+ '/preferential';return false;">取消</button>
						<div  class="btn-division"></div>
						<button class="btn btn-save in-btn135 preferential-btn-bgcolor" type="submit" />确认</button>
					</div>
					</form>
				</c:when>
				<c:otherwise>
				</form>
					<div class="btn-operate">
						
						<button class="btn btn-cancel in-btn135" id="btn_cancle_voucher">返回</button>
<%-- 						<c:if test="'<%=Constant.ISBRANCH%>'==N">  --%>
						<div class="btn-division"></div>
						<button class="btn btn-save in-btn135 preferential-btn-bgcolor" id="btn_to_modify">编辑</button>
<%-- 						</c:if> --%>
					</div>
				</c:otherwise>
			</c:choose>
			
		</div>
		
		<div class="modal fade storeSelect-dialog in " id="store-select-dialog">
			<div class="modal-dialog">
				<div class="modal-content">	
					<div class="modal-header addDelicon">				  
				        <img data-dismiss="modal" class="img-close" src="/newspicyway/images/close.png">
				    </div>
					<div class="modal-body">
						<div class="row store-select-title">
							<div class="col-xs-9">选择门店<font id="store-count"></font></div>
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
		
		$(parent.document.all("detail")).height(document.body.scrollHeight -130); 
			$("#btn_cancle_voucher").click(function(){
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
					amount : {
						required : true,
						isPrice:true
					}
					
				},
				messages : {
					name : {
						required : "请输入活动名称",
						maxlength : "长度不能超过15",
					},
					amount : {
						required : "请输入减免金额",
						number: true,
						isPrice:"请输入有效的减免金额"
					},
					starttime : {
						required : "请选择有效期开始日期",
					},
					endtime : {
						required : "请选择有效期结束日期",
					}
				}
			});
			var isModify = ${isModify};
			var applyAllShop = false;
			var selectShops = [];
			<c:if test="${preferentialId != ''}">
				applyAllShop = ${voucher.preferentialActivity.applyAll};
				<c:if test="${voucher.preferentialActivity.applyAll == false}">
					selectShops = ${voucher.branchs};
				</c:if>
			</c:if>
			var preferentialId = "${preferentialId}";
			if(isModify == false && preferentialId != ""){
				$("input, select, textarea").prop("disabled", true).css("disabled", "disabled").off();
				$(".add-dish-select").remove();// 去掉不参与
				$("#addstore").prop("disabled", true);
				$(".arrow-down").remove();// 去掉不参与
				$("#color-select").unbind("click").children("span").remove();
			}
			$("#btn_to_modify").click(function(){
				window.location.href = "<%=request.getContextPath()%>/preferential/toVoucher?id=${voucher.preferentialActivity.id }&isModify=true";
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
