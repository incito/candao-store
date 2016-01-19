<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>租户管理</title>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css"/>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common/city.css"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/tenant.css"/>
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/cityinit.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/city_arr.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/city_func.js"></script>
	<script src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/jquery.validate.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/messages_zh.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/tenant.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/jquery.twbsPagination.js"></script>
</head>
<body>
		<div class="ky-content tenant-content">
			<!-- button样式添加-->
			<div class="tenant-btn btn-add">
				<div class="btn-group" role="group" aria-label="...">
				  <button type="button" class="btn  btn-first">新增租户</button>
				</div>
			</div>
			<!--样式更改 添加table-list -->
			<table class="table tenant-table table-list">
				<thead>
					<tr>
						<th class="count">序号</th>
						<th class="code">账号</th>
						<th class="companyName">企业名称</th>
						<th class="name">姓名</th>
						<th class="email">邮箱</th>
						<th class="phone">手机</th>
						<th class="call">固定电话</th>
						<th class="address">地址</th>
						<th class="opera">操作</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
			<div class="pagingWrap">
			
			</div>
		</div>
		<!--点击按钮弹出添加界面 -->
		<div class="modal fade tenant-dialog in " id="tenant-add" data-backdrop="static" >
			<div class="modal-dialog">
				<div class="modal-content">	
					<div class="modal-header tenantInfo">	
						<span>编辑租户信息</span>			  
				        <img src="<%=request.getContextPath()%>/images/close.png" class="img-close" data-dismiss="modal">
				    </div>
					<div class="modal-body">
						<div class="row">
							<div class="col-xs-12">
								<form action="" method="post"  class="form-horizontal " name="add-form" id="add-form">
									<div class="form-group" id="accountDiv">
									<input type="hidden" id="id" name="id">
										<label class="col-xs-3 control-label ">账号：</label>
										<div class="col-xs-8 clearfix">
											<p class="form-control-static codeLabel">100010</p>
											<button type="button" class="btn in-btn200 " onclick="sendAccount()" id="sendAcc" style="width:100px !important;">发送账号信息</button>
										</div>
									
									</div>
									
									<div class="form-group">
										<label class="col-xs-3 control-label ">姓名：</label>
										<div class="col-xs-8">
											<input type="text" name="name" id="name" maxlength="15" value="" class="form-control required">	
										</div>
									
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label ">邮箱：</label>
										<div class="col-xs-8">
											<input type="text" name="email" id="email" maxlength="35" value="" class="form-control ">	
										</div>
									
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label ">手机：</label>
										<div class="col-xs-8">
											<input type="text" name="mobile" id="mobile" maxlength="11" placeholder="请输入11位手机号"	value="" class="form-control  mobile">	
										</div>
									
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label ">固定电话：</label>
										<div class="col-xs-3">
											<input type="text" name="areaCode"  id="areaCode" maxlength="4" placeholder="区号"	value="" class="form-control required number">	
										</div>
										<div class="col-xs-5">
											<input type="text" name="telephone"  id="telephone" maxlength="8" placeholder="电话号"	value="" class="form-control required number">	
										</div>
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label ">企业名称：</label>
										<div class="col-xs-8">
											<input type="text" name="businessName"  id="businessName" maxlength="25" value="" class="form-control required">	
										</div>
									</div>

									<div class="form-group">
										<label class="col-xs-3 control-label ">地址：</label>
										<div class="col-xs-8 ky-select-group">
											<div class="input-group ky-xs-4">
												<select class="form-control ky-group-radius required" id="province" placeholder="省"  onchange="initCity(this)"></select>
											  <div class="input-group-btn ky-select-padding">
											     
											  </div>
											</div>
											<div class="input-group ky-xs-4">
												<select class="form-control ky-group-radius " id="city" placeholder="市" onchange="initRegion(this.value)"></select>
											  <div class="input-group-btn ky-select-padding">
											  
											  </div>
											</div>
											<div class="input-group ky-xs-4">
												<select class="form-control ky-group-radius " id="region"  placeholder="区/县"  onchange="changeRegion(this.value)"></select>
<!-- 											  <div class="input-group-btn ky-select-padding"> -->
<!-- 											    区/县 -->
<!-- 											  </div> -->
											  <input type="hidden" name="province" />
												<input type="hidden" name="city"/>
												<input type="hidden" name="region"  />
											</div>
											<input type="hidden" id="province_" name="province_" value="${agent.province}">
								          <input type="hidden" id="city_" name="city_" value="${agent.city}">
								          <input type="hidden" id="region_" name="region_" value="${agent.region}">
										</div>									
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label "></label>
										<div class="col-xs-8">
											<input type="text" name="address" id="address" maxlength="25" value="" class="form-control required" placeholder="详细街道地址">
											<font color="red" id="address_tip"></font> 
										</div>					
									</div>
									<div class="btn-operate form-group" id="add-btn">
										<button class="btn btn-cancel in-btn135" type="button"  data-dismiss="modal" >取消</button>
										<div  class="btn-division"></div>
										<button class="btn btn-save in-btn135 shop-btn-save" type="submit" id="btnsave"  > 确认</button>
									</div>
									
									<div class="btn-operate form-group" id="edit-btn">
										<label class="col-xs-3 control-label "></label>
										<div class="col-xs-8">
											<button class=" btn btn-save shop-btn-save " style="width:100%;" type="button" id="btnedit" onclick="detail2edit()" > 编辑</button>
										</div>
									</div>

								</form>
							</div>
						</div>						
					</div>
				</div>
			</div>
		</div>
		
		<!--发送方式 -->
		<div class="modal fade tenant-dialog in " id="sendType" data-backdrop="static" style="z-index:99900">
			<div class="modal-dialog" style="width: 350px;">
				<div class="modal-content">	
					<div class="modal-header tenantInfo">	
						<span class="title">发送账户信息</span>
				        <img src="<%=request.getContextPath()%>/images/close.png" class="img-close" data-dismiss="modal">
				    </div>
					<div class="modal-body">
						<div class="row">
							<div class="col-xs-12">
								<div class="btn-operate " id="sendTypeAccountWrap" style="display:none;">
									<i class="icon-sendSuccess"></i>
									<label>创建成功，账号：</label>
									<label id="successAccount"></label>
								</div>
								<div class="btn-operate sendType">
									<button class="btn in-btn200" type="button" id="sendAccountEmail" onclick="sendAccountWay(1)">通过邮箱发送帐号</button>
								</div>
								<div class="btn-operate sendType">
									<button class="btn in-btn200 " type="button" id="sendAccountPhone" onclick="sendAccountWay(2)">通过手机短信发送帐号</button>
								</div>
							</div>
						</div>						
					</div>
				</div>
			</div>
		</div>
		<!-- 发送提示 -->
		<div class="modal fade sendMsgState in " id="successPrompt" style="z-index:99999" >
			<div class="modal-dialog" style="width:310px;">
				<div class="modal-content" >	
					<div class="modal-body pop-div-content">
						<p><i class="icon-success"></i></p>
						<p class="tipP"> <label id="promptMsg">账号发送成功</label></p>
					</div>
				</div>
			</div>
		</div>
		<!--验证码提示 -->
		<div class="modal fade tenant-dialog in " id="checkCode" data-backdrop="static" style="z-index:99910">
			<div class="modal-dialog" style="width: 360px;">
				<div class="modal-content">	
					<div class="modal-header tenantInfo">	
						提示		  
				        <img src="<%=request.getContextPath()%>/images/close.png" class="img-close" data-dismiss="modal">
				    </div>
					<div class="modal-body">
						<div class="row">
							<div class="allWrap">
								<div class="btn-operate">
									<div class="checkCodeTitle">请输入图中文字</div>
									<div class="checkCodeTextWrap clearfix">
										<input type="hidden" name="account" />
										<input type="hidden" name="email" />
										<input type="hidden" name="mobile" />
										<input type="text" name="name" id="valicodeText"  id="valicodeText" maxlength="25" value="" class="form-control checkCodeText">	
										<img id="valicodeImg" onclick="resetValicode()" />
									</div>
								</div>
								<div class="errorTips">
									
								</div>
								<div class="btn-operate">
									<button class="btn btn-save in-btn263" type="button" id="valicodeButton">确定</button>
								</div>
							</div>
						</div>						
					</div>
				</div>
			</div>
		</div>
		<!-- 确认删除框 -->
		<div class="modal fade dialog-sm in " id="deleteComfirm"  data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">	
					<div class="modal-body">
						<div class="dialog-sm-header">				  
					        <span class=" ">确认删除</span>
					        <img src="<%=request.getContextPath()%>/images/close-sm.png" class="img-close" data-dismiss="modal">
					    </div>
						<form action="" method="post" class="form-horizontal " name="">
							<!-- 仅存在一个分类中-->
							<div class="dialog-sm-info">
							<p class="p1"><img src="<%=request.getContextPath()%>/images/del-tip.png">确认删除“<span id="showName"></span>”吗?</p>
							</div>
							<div class="btn-operate">
								<button class="btn btn-cancel  " type="button" data-dismiss="modal"   >取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save  " id="dishes-type-save" type="button" onclick="doDel()">确认</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div class="modal fade sendMsgState in " id="successPrompt"  >
			<div class="modal-dialog" style="width:310px;">
				<div class="modal-content" style="width:310px;">	
					<div class="modal-body pop-div-content">
						<p><i class="icon-success"></i></p>
						<p class="tipP"><label id="promptMsg">删除成功</label></p>
					</div>
				</div>
			</div>
		</div>
		<script type="text/javascript">
			var contextPath = "<%=request.getContextPath()%>";
			var page;
			// 初始化地址省
			initProvince();
			jQuery.validator.addMethod("mobile", function(value, element) {
			    var length = value.length;
			    var mobile =  /^(((1[0-9]{1})|(1[0-9]{1}))+\d{9})$/
			    return this.optional(element) || (length == 11 && mobile.test(value));
			}, "请输入11位手机号");
			jQuery.validator.addMethod("requiredTo", function(value, element) {
				return !($("#email").val() == "" && $(element).val() == "");
			}, "手机和邮箱至少填写一个");
			$("img.img-close").hover(function(){
			 	$(this).attr("src",contextPath+"/images/close-active.png");	 
			},function(){
				$(this).attr("src",contextPath+"/images/close-sm.png");
			});
			$("#add-form").validate({
				rules : {
					name : {
						required : true,
						maxlength : 15,
					},
					areaCode : {
						required : true,
						number:true,
						maxlength : 4,
					},
					mobile : {
						requiredTo : true,
						mobile : true
					},
					email : {
						email : true
					},
					telephone : {
						required : true,
						number:true,
						maxlength : 8,
					},
					businessName : {
						required : true
					},
					address : {
						required : true
					}
				},
				messages : {
					name : {
						required : "必填信息",
						maxlength : "长度不能超过15",
					},
					areaCode : {
						required : "请输入区号",
						number: "最多输入4位数字"
					},
					telephone : {
						required : "请输入电话号",
						number: "最多输入8位数字"
					},
					businessName : {
						required : "请输入企业名称"
					},
					address : {
						required : "请输入详细地址",
					},
					email : {
						email : "请输入正确的邮箱"
					},
				}
			});
		</script>
</body>
</html>
