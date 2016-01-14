<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>账户管理</title>
	<!-- 引用依赖js、css  -->
	<link rel="stylesheet" href="<%=basePath%>/tools/bootstrap/css/bootstrap.css">
	<link rel="stylesheet" href="<%=basePath%>/css/common.css">
	<link rel="stylesheet" href="<%=basePath%>/css/common/city.css">
	<link rel="stylesheet" href="<%=basePath%>/css/index.css">
	<link rel="stylesheet" href="<%=basePath%>/css/tenant.css">
	<script type="text/javascript" src="<%=basePath%>/scripts/cityinit.js"></script>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/switchery/style.css"/>
	<script>
		var global_baseurl = '<%=basePath%>';
		var global_shop_admin_function_code = '<%=request.getAttribute("shop_admin_function_code")%>';//门店管理员角色权限
	</script>

	<script src="<%=basePath%>/scripts/jquery.js"></script>
	<script src="<%=basePath%>/scripts/city_arr.js"></script>
	<script src="<%=basePath%>/scripts/city_func.js"></script>
	<script src="<%=basePath%>/tools/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=basePath%>/scripts/projectJs/jquery.twbsPagination.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/jquery.validate.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/messages_zh.js"></script>
	<!-- 引入门店选择js -->
	<script src="<%=basePath%>/scripts/branchSelect.js"></script>
	<!-- 引用本页面js文件 -->
	<script src="<%=basePath%>/scripts/projectJs/account.js"></script>
	
</head>
<body>
		<div class="ky-content tenant-content">
			<!-- button样式添加-->
			<div class="tenant-btn btn-add">
				<div class="btn-group" role="group" aria-label="...">
				  <button type="button" class="btn  btn-first">新建账户</button>
				</div>
				<a href="<%=request.getContextPath()%>/t_role/roleList4HeadOffice" class="role">角色</a>
			</div>
			<!--样式更改 添加table-list -->
			<table class="table account-table table-list">
				<thead>
					<tr>
						<th class="count">序号</th>
						<th class="code">账号</th>
						<th class="name">姓名</th>
						<th class="phone">手机号码</th>
						<th class="email">邮箱</th>
						<th class="status">账户状态</th>
						<th class="opera">操作</th>
					</tr>
				</thead>
				<tbody>
					<tr>
					</tr>
				</tbody>
			</table>
			<div class="pagingWrap">
			
			</div>
		</div>
		<!--点击按钮弹出添加界面 -->
		<div class="modal fade employee-dialog in " id="employee-add" data-backdrop="static" style="z-index:90000">
			<div class="modal-dialog" style="width:500px;">
				<div class="modal-content">	
					<div class="modal-header">	
						<label>新建帐号</label>	  
				        <img src="<%=basePath%>/images/close.png" class="img-close" data-dismiss="modal">
				    </div>
					<div class="modal-body">
						<form action="" method="post"  class="form-horizontal " name="add-form" id="add-form">
							<div class="row">
								<div class="col-xs-12">
									<input type="hidden" name="id" id="id" />
									<table class="employee_add_table">
										<tr>
											<td class="labelText">账号：</td>
											<td>
												<div class="viewAccountWrap">
													<input type="text" id="account" name="account" class="viewAccount" maxlength="25" />
												</div>
												<button type="button" class="btn in-btn200" onclick="sendAccount()" id="sendAcc">发送账号信息</button>
											</td>
										</tr>
										<tr>
											<td class="labelText">姓名：</td>
											<td><input type="text" id="name" name="name" maxlength="15" /></td>
										</tr>
										<tr>
											
											<td class="labelText">邮箱：</td>
											<td><input type="text" id="email" name="email" maxlength="35" /></td>
										</tr>
										<tr>
											<td class="labelText">手机：</td>
											<td><input type="text" id="mobile" name="mobile" maxlength="11"  /></td>
										</tr>
										<tr>
											<td class="labelText">账户角色：</td>
											<td>
												<div class="accountRoleList">
													<span class="selectedText"></span>
													<i class="icon-listArr"></i>
													<ul>
													</ul>
												</div>
												<button type="button" class="btn in-btn200" id="btn_create_role" >新建角色</button>
												<input id="accountRoleList_input" type="hidden" name="accountRoleList_input" />
											</td>
										</tr>
										<tr class="shops" id="openSelectTr" style="display: none;"><td class="labelText">分店：</td><td>
											<div id="openSelectId" class="add-shop-select col-xs-1">
												<img src="/newspicyway/images/add.png" alt="">
											</div>
										</tr>
										<tr>
											<td class="labelText">账户状态：</td>
											<td>
												<input type="radio" id="statusOn" name="status" value="01" checked/><label for="statusOn">启用</label>
												<input type="radio" id="statusOff" name="status" value="02" /><label for="statusOff">禁用</label>
											</td>
										</tr>
									</table>
								</div>
							</div>	
							<div class="btn-operate" id="modify-detail" style="display:none">
								<button class="btn btn-save in-btn135 " type="button" style="width:200px" id="toModify">编辑资料</button>
							</div>		
							<div class="btn-operate" id="modify-opera" >
								<button class="btn btn-cancel in-btn135" type="button" id="modifyCancel" >取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save in-btn135 " type="submit" id="modifySubmit" >确认</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		
		<!--权限 -->
		<div class="modal fade employees-dialog in " id="employees_limite" data-backdrop="static" style="z-index:99000">
			<div class="modal-dialog" style="width: 650px;">
				<div class="modal-content">	
					<div class="modal-header tenantInfo" style="margin: 0px 20px; padding: 10px 0px;">	
						权限	  
				        <img src="<%=request.getContextPath()%>/images/close.png" class="img-close" data-dismiss="modal">
				    </div>
					<div class="modal-body" style="padding-top: 0px;">
								
					</div>
				</div>
			</div>
		</div>
		
		
		
		<!-- 确认删除框 -->
		<div class="modal fade dialog-sm in " id="deleteComfirm"  data-backdrop="static" style="z-index:90000">
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
								<button class="btn btn-cancel" id="deleteComfirmNo"  type="button" data-dismiss="modal">取消</button>
								<div class="btn-division"></div>
								<button class="btn btn-save" id="deleteComfirmOk" type="button">确认</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		
		<!--发送方式 -->
		<div class="modal fade tenant-dialog in " id="sendType" data-backdrop="static"  style="z-index:99000" >
			<div class="modal-dialog" style="width: 350px; padding-top:135px;">
				<div class="modal-content">	
					<div class="modal-header tenantInfo">	
						<span class="title">发送账户信息</span>  
				        <img src="<%=request.getContextPath()%>/images/close.png" class="img-close" data-dismiss="modal">
				    </div>
					<div class="modal-body">
						<div class="row">
							<div class="col-xs-12">
								<div class="btn-operate " id="sendTypeAccountWrap" style="display:none;">
									<i class="icon-sendSuccess"></i>创建成功，账号：<label id="sendTypeAccount">258712</label>
								</div>
								<input type="hidden" id="sendId" name="sendId" > 
								<div class="btn-operate sendType">
									<button class="btn in-btn200" id="sendAccountEmail" type="button" onclick="sendAccountWay(1)">通过邮箱发送帐号</button>
								</div>
								<div class="btn-operate sendType">
									<button class="btn in-btn200 " id="sendAccountPhone" type="button" onclick="sendAccountWay(2)">通过手机短信发送帐号</button>
								</div>
							</div>
						</div>						
					</div>
				</div>
			</div>
		</div>
		<!-- 发送提示 -->
		<div class="modal fade sendMsgState in " id="successPrompt"  style="z-index:99990" >
			<div class="modal-dialog" style="width:310px;">
				<div class="modal-content" >	
					<div class="modal-body pop-div-content">
						<p><i class="icon-success"></i></p>
						<p class="tipP"> 
							<label id="promptMsg">账号发送成功</label>
						</p>
					</div>
				</div>
			</div>
		</div>
		<!--验证码提示 -->
		<div class="modal fade tenant-dialog in " id="checkCode" data-backdrop="static"  style="z-index:99900" >
			<div class="modal-dialog" style="width: 360px;padding-top:135px">
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
									<div class="checkCodeTextWrap clearfix" >
										<input type="hidden" name="account" />
										<input type="hidden" name="email" />
										<input type="hidden" name="mobile" />
										<input type="text" name="valicodeText" id="valicodeText" maxlength="25" value="" class="form-control checkCodeText">	
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
</body>
</html>
