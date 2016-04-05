<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<title>支付管理</title>
<%@ include file="/common/resource.jsp"%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/index.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/tools/font-awesome/css/font-awesome.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/common.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/index.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/payment.css" />
<script src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
<script
	src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
<script
	src="<%=request.getContextPath()%>/tools/jquery-validation/jquery.validate.js"></script>
<script
	src="<%=request.getContextPath()%>/tools/jquery-validation/validate-plus.js"></script>
<script src="<%=request.getContextPath()%>/scripts/global.js"></script>
<script src="<%=request.getContextPath()%>/scripts/projectJs/jquery.twbsPagination.js"></script>
<script src="<%=request.getContextPath()%>/scripts/projectJs/payment.js"></script>
</head>
<body>
	<div class="ky-content content-iframe">
		<div class="payment-title">
			<span>微信支付</span>
		</div>
		<hr />
		<div class="payment-btn btn-add">
			<div class="btn-group" role="group" aria-label="...">
				<button type="button" class="btn  btn-first" onclick="operaPayment('', 'add')">新增</button>
			</div>
		</div>
		<div class="payment-search">
			<div class="form-group">
				<label class="control-label col-xs-2">门店ID：</label>
				<div class="col-xs-3">
					<input type="text" id="searchbranchid" class="form-control" />
				</div>
				<label class="control-label col-xs-2">门店名称：</label>
				<div class="col-xs-3">
					<input type="text" id="searchbranchname" class="form-control" />
				</div>
				<label class="control-label col-xs-2">微信商户ID：</label>
				<div class="col-xs-3">
					<input type="text" id="searchpartner" class="form-control" />
				</div>
				<div class="col-xs-1 payment-search-btn">
					<button class="btn btn-default" type="button" onclick="doSearch()">
						<i class="icon-search"></i> 搜索
					</button>
				</div>
			</div>
		</div>
		<table class="table table-list payment-table" id="payment-table">
			<thead>
				<tr>
					<th>序号</th>
					<th>门店ID</th>
					<th>门店名称</th>
					<th>微信商户ID</th>
					<th>微信应用ID（APPID）</th>
					<th>微信应用秘钥（APPSECRET）</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody style="font-size: 12px;">
			</tbody>
		</table>
		<div class="pagingWrap"></div>
	</div>
	<!-- loading -->
	<div class="modal fade dialog in " id="prompt-dialog"
		data-backdrop="static">
		<div class="modal-dialog"
			style="margin-top: 100px; position: absolute; left: 35%; width: 250px;">
			<div class="modal-content">
				<div class="modal-body">
					<div style="text-align: center;">
						<p id="prompt-msg">处理中，请稍后...</p>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!--点击按钮弹出添加界面 -->
	<div class="modal fade payment-dialog in " id="payment-add"
		data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<div class="modal-title">
						<span>新增</span> <img
							src="<%=request.getContextPath()%>/images/close.png"
							class="img-close" data-dismiss="modal" />
					</div>
				</div>
				<div class="modal-body">
					<div class="row mt20">
						<div class="col-xs-12">
							<form action="" method="post" class="form-horizontal "
								id="add-paymentform">
								<div class="form-group">
									<label class="col-xs-3 control-label "><span
										class="required-span">*</span>门店ID：</label>
									<div class="col-xs-8">
										<input type="hidden" id="id" name="id" value=""/>
										<select class="form-control required" id="branchid"
											onchange="getBranchNameById(this.value)">
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 control-label "><span
										class="required-span">*</span>门店名称：</label>
									<div class="col-xs-8">
										<select class="form-control required" id="branchname"
											onchange="getBranchIdByName(this.value)">
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 control-label "><span
										class="required-span">*</span>微信商户ID：</label>
									<div class="col-xs-8">
										<input type="text" name="partner" id="partner" maxlength="50"
											placeholder="最多50个字符" value="" class="form-control required" />
									</div>

								</div>
								<div class="form-group">
									<label class="col-xs-3 control-label "><span
										class="required-span">*</span>微信应用ID<span
										style="text-align: center;"><br />(APPID)</span>：</label>
									<div class="col-xs-8">
										<input type="text" name="appid" id="appid" maxlength="50"
											placeholder="最多50个字符" value="" class="form-control required" />
									</div>

								</div>
								<div class="form-group">
									<label class="col-xs-3 control-label "><span
										class="required-span">*</span>微信应用秘钥：<br />(APPSECRET)</label>
									<div class="col-xs-8">
										<input type="text" name="appsecret" id="appsecret"
											maxlength="50" placeholder="最多50个字符" value=""
											class="form-control required" />
									</div>
								</div>
								<div class="btn-operate" id="add-btn">
									<button class="btn btn-cancel in-btn135" type="button"
										data-dismiss="modal">取消</button>
									<div class="btn-division"></div>
									<button class="btn btn-save in-btn135" type="submit" id="btnsave">保存</button>
								</div>

								<div class="btn-operate" id="edit-btn">
									<button class="btn btn-cancel in-btn135" type="button"
										data-dismiss="modal">关闭</button>
									<div class="btn-division"></div>
									<button class="btn btn-save in-btn135"
										type="button" id="btnedit" onclick="detail2edit()">
										编辑</button>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 确认删除框 -->
	<div class="modal fade dialog-sm in " id="deleteComfirm"
		data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<div class="dialog-sm-header">
						<span class=" ">确认删除</span> <img
							src="<%=request.getContextPath()%>/images/close-sm.png"
							class="img-close" data-dismiss="modal">
					</div>
					<form action="" method="post" class="form-horizontal " name="">
						<!-- 仅存在一个分类中-->
						<div class="dialog-sm-info">
							<p class="p1">
								<input type="hidden" id="delid" value=""/>
								<img src="<%=request.getContextPath()%>/images/del-tip.png"></i>确认删除吗?
							</p>
						</div>
						<div class="btn-operate">
							<button class="btn btn-cancel  " type="button"
								data-dismiss="modal">取消</button>
							<div class="btn-division"></div>
							<button class="btn btn-save  " id="dishes-type-save"
								type="button" onclick="doDel()">确认</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<!-- 操作提示 -->
	<div class="modal fade sendMsgState in " id="successPrompt"
		style="z-index: 99990">
		<div class="modal-dialog" style="width: 310px;margin-top: 100px;">
			<div class="modal-content">
				<div class="modal-body pop-div-content">
					<p class="tipP">
						<label id="promptMsg">删除成功！</label>
					</p>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
var isView = false;
var isModify = false;
$(document).ready(function() {
	localStorage._currentPage = 1;
	//dialog中右上角关闭按钮，鼠标经过效果
	$("img.img-close").hover(function(){
	 	$(this).attr("src", "<%=request.getContextPath()%>/images/close-active.png");	 
	},function(){
		$(this).attr("src", "<%=request.getContextPath()%>/images/close-sm.png");
	});
	getBranchNameById(null, true);
	getBranchIdByName(null, true);
	doSearch();
	refreshBranchRole();
	$("#add-paymentform").validate({
		submitHandler : function(form) {
			savePayment();
		},
		rules : {
			partner : {
				required : true,
				validCharacter: true,
				maxlength : 50
			},
			appid : {
				required : true,
				validCharacter: true,
				maxlength : 50
			},
			appsecret : {
				required : true,
				validCharacter: true,
				maxlength : 50
			}
		},
		messages : {
			partner : {
				required : "您还未输入内容",
				validCharacter: "您输入的是非法字符，请重新输入数字、字母。",
				maxlength : "您输入的内容超过50个字符，请重新输入"
			},
			appid : {
				required : "您还未输入内容",
				validCharacter: "您输入的是非法字符，请重新输入数字、字母。",
				maxlength : "您输入的内容超过50个字符，请重新输入"
			},
			appsecret : {
				required : "您还未输入内容",
				validCharacter: "您输入的是非法字符，请重新输入数字、字母。",
				maxlength : "您输入的内容超过50个字符，请重新输入"
			}
		}
	});
});
//根据当前 是分店还是总店，判断某些权限是否显示
//分店 不能新增、编辑、删除
function refreshBranchRole(){
	if(isbranch=='Y'){
		//移除手工优免
		$(".btn-add").hide();
		$("#edit-btn").find("#btnedit").hide();
		$("#edit-btn").find(".btn-division").hide();
	}
}

</script>
</html>