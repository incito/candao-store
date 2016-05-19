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
					<th>状态</th>
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
	<style>
	
		.personal-img {height:130px!important;}
		
		.uploadImg {
		    width: 130px;
	    	height: 130px;
	    	border: 1px solid #e6e6e6;
		}
		.btn-upload {
		    position: absolute;
		    filter: alpha(opacity = 0);
		    opacity: 0;
		 
		      width: 60px;
		    height: 28px;
		    left:15px;
		    top:0;
		    cursor: pointer;
		}
		.img-op {
			padding:5px 0;
			background:rgba(0,0,0,0.6);
			position:absolute;
			top:102px;
			padding:5px 2px;
			left:15px;
			width:130px;
		}
		.img-op a {
			color:#fff;
		}
		.personal-img.img-default .img-op {
			filter: alpha(opacity = 0);
    		opacity: 0;
		}
		.personal-img.img-default .btn-upload {
		    top: -100px;
		    left: 0;
		    position: absolute;
		    filter: alpha(opacity = 0);
		    opacity: 0;
		    cursor: pointer;
		       width: 130px;
		    height: 130px;
		}
		.switch {
  margin: 0 auto;
  position: relative;
}

.switch label {
  width: 100%;
  height: 100%;
  position: relative;
  display: block;
}

.switch input {
  top: 0; 
  right: 0; 
  bottom: 0; 
  left: 0;
  opacity: 0;
  z-index: 100;
  position: absolute;
  width: 100%;
  height: 100%;
  cursor: pointer;
}


/* DEMO 3 */

.switch.demo3 {
  width: 60px;
  height: 20px;
}

.switch.demo3 label {
  display: block;
  width: 100%;
  height: 100%;
  background: #a5a39d;
  border-radius: 40px;
  box-shadow:
      inset 0 3px 8px 1px rgba(0,0,0,0.2),
      0 1px 0 rgba(255,255,255,0.5);
}

.switch.demo3 label:after {
  content: "";
  position: absolute;
  z-index: -1;
  top: -8px; right: -8px; bottom: -8px; left: -8px;
  border-radius: inherit;
}

.switch.demo3 label:before {
  content: "";
  position: absolute;
  z-index: -1;
  top: -18px; right: -18px; bottom: -18px; left: -18px;
  border-radius: inherit;
  box-shadow: 0 1px 0 rgba(255,255,255,0.5);
}

.switch.demo3 label i {
  display: block;
  height: 100%;
  width: 35%;
  border-radius: inherit;
  background: silver;
  position: absolute;
  z-index: 2;
  top: 0;
  background: #b2ac9e;
  background: -moz-linear-gradient(#f7f2f6, #b2ac9e);
  background: -ms-linear-gradient(#f7f2f6, #b2ac9e);
  background: -o-linear-gradient(#f7f2f6, #b2ac9e);
  background: -webkit-gradient(linear, 0 0, 0 100%, from(#f7f2f6), to(#b2ac9e));
  background: -webkit-linear-gradient(#f7f2f6, #b2ac9e);
  background: linear-gradient(#f7f2f6, #b2ac9e);
  box-shadow:
      inset 0 1px 0 white,
      0 0 8px rgba(0,0,0,0.3),
      0 5px 5px rgba(0,0,0,0.2);
}

.switch.demo3 label i:after {
  content: "";
  position: absolute;
  left: 25%;
  top: 25%;
  width: 50%;
  height: 50%;
  background: #d2cbc3;
  background: -moz-linear-gradient(#cbc7bc, #d2cbc3);
  background: -ms-linear-gradient(#cbc7bc, #d2cbc3);
  background: -o-linear-gradient(#cbc7bc, #d2cbc3);
  background: -webkit-gradient(linear, 0 0, 0 100%, from(#cbc7bc), to(#d2cbc3));
  background: -webkit-linear-gradient(#cbc7bc, #d2cbc3);
  background: linear-gradient(#cbc7bc, #d2cbc3);
  border-radius: inherit;
}

.switch.demo3 label i:before {
  content: attr(data-off);
  font-style: normal;
  color: #fff;
  font-size: 10px;
  position: absolute;
  top: 50%;
  margin-top: -8px;
  right: -30px;
}

.switch.demo3 input:checked ~ label {
  background: #9abb82;
}

.switch.demo3 input:checked ~ label i {
  right: -1%;
}

.switch.demo3 input:checked ~ label i:before {
  content: attr(data-on);
  left: -90px;
  color: #fff;
}
	</style>
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
									<label class="col-xs-3 control-label "><span class="required-span">*</span>收款账户类型：</label>
									<div class="col-xs-8">
										<div class="radio-box">
											<span><input type="radio" name="weixintype" checked onchange="weixintypeChange(2)" class="mr5 weixintype" id="weixintype2" value="2" >微信商户</span>
	                    					<span class="ml20"><input type="radio" name="weixintype" onchange="weixintypeChange(1)" class="mr5 weixintype" id="weixintype1" value="1" >微信个人账户</span>
                    					</div>
									</div>
								</div>
								<div class="form-group personal-img img-default" style="display:none;">
									<label class="col-xs-3 control-label "><span
										class="required-span">*</span>个人账户二维码：</label>
									<div class="col-xs-8 f-pr">
										<img src="../images/upload-img.png" class="uploadImg">
										<div class="img-op f-dn" style="display:block;">
											<a href="javascript:void(0);" class="f-fl J-rUpload">重新上传</a>
											<a href="javascript:void(0);" onclick="delImg(this)" class="f-fr J-btn-del">删除</a>
											<input type="file" onchange="showImg(this)" size="1" class="btn-upload J-btn-upload" id="qrcode" name="qrcode" accept="image/*">
										</div>
										<div class="f-cb"></div>
										<span class="c-red tips f-dn">不能为空</span>
									</div>
								</div>
								
								<div class="busness" style="display:none;">
									<div class="form-group">
										<label class="col-xs-3 control-label "><span
											class="required-span">*</span>微信商户ID：</label>
										<div class="col-xs-8">
											<input type="text" name="partner" id="partner" maxlength="50"
												placeholder="最多50个字符" oninput="if(value.length>50)value=value.slice(0,50)" value="" class="form-control required" />
											<span class="c-red tips f-dn">不能为空</span>
										</div>
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label "><span
											class="required-span">*</span>微信应用ID<span
											style="text-align: center;"><br />(APPID)</span>：</label>
										<div class="col-xs-8">
											<input type="text" name="appid" id="appid" maxlength="50"
												placeholder="最多50个字符" oninput="if(value.length>50)value=value.slice(0,50)" value="" class="form-control required" />
												<span class="c-red tips f-dn">不能为空</span>
										</div>
	
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label "><span
											class="required-span">*</span>微信应用秘钥：<br />(APPSECRET)</label>
										<div class="col-xs-8">
											<input type="text" name="appsecret" id="appsecret"
												maxlength="50" oninput="if(value.length>50)value=value.slice(0,50)" placeholder="最多50个字符" value=""
												class="form-control required" />
											<span class="c-red tips f-dn">不能为空</span>
										</div>
									</div>
								</div>
								
								<div class="form-group">
									<label class="col-xs-3 control-label "><span
										class="required-span">*</span>启用状态：</label>
									<div class="col-xs-8">
											<div class="radio-box">
												<span><input type="radio" name="status" checked class="mr5" id="status1" value="1" >启用</span>
		                    					<span class="ml20"><input type="radio" name="status" class="mr5" id="status0" value="0" >禁用</span>
	                    					</div>
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