<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
response.addHeader("P3P","CP=CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>员工管理</title>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/tenant.css"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/switchery/style.css"/>

	<script src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
	<script src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/tools/calendar_diy/WdatePicker.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/commons.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/jquery.twbsPagination.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/jquery.validate.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/messages_zh.js"></script>
	<style>
	/* 针对验证 员工角色的 错误信息进行书写样式表*/
		.accountRoleList label.error{
			
		}

		label.error{
			font-size:12px;
		}

		.employees-table tbody tr td {
			padding-left:40px;
		}
		.employees-table tbody tr td:nth-of-type(7) {
			padding-left:0;
			text-align:center;
		}
		.employees-table tbody tr td:nth-of-type(8) {
			text-align:center;
			padding-left:0;
		}

	</style>
</head>
<body>
	<input type="hidden" value="${page.pageSize}" id="pagesize" /> 
	<input type="hidden" value="${page.current }" id="current" />
		<div class="ky-content tenant-content">
			<!-- button样式添加-->
			<div class="employees-btn btn-add">
				<%-- <div class="btn-group" role="group" aria-label="...">
				  <button type="button" class="btn  btn-first">新建员工</button>
				</div>
				<a href="<%=request.getContextPath()%>/t_role/roleList4Store/" class="role">角色</a> --%>
				<c:choose>
					<c:when test="${hasStore}">
						<div class="btn-group" role="group" aria-label="...">
						  <button type="button" class="btn  btn-first">新建员工</button>
						</div>
						<a href="<%=request.getContextPath()%>/t_role/roleList4Store/" class="role">角色</a>
					</c:when>
					<c:otherwise>
						<div class="alert alert-danger" role="alert">当前无默认门店信息，请完善默认门店信息！</div>
						
					</c:otherwise>
				</c:choose>
			</div>
			<!--样式更改 添加table-list -->
			<table class="table employees-table table-list">
				<thead>
					<tr>
						<th class="count">序号</th>
						<th class="name">姓名</th>
						<th class="number">工号</th>
						<th class="sex">性别</th>
						<th class="title">职务</th>
						<th class="call">联系电话</th>
						<th class="off">账户状态</th>
						<th class="opera">操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${page.rows }" var="e" varStatus="st" >
						<tr>
							<td>${ (page.current-1)*page.pageSize + (st.index+1) }</td>
							<td>${e.user.name}</td>
							<td>${e.jobNumber}</td>
							<td>
								<c:choose>
									<c:when test="${e.sex =='1'}">男</c:when>
									<c:when test="${e.sex == '0'}">女</c:when>
									<c:otherwise>
										${e.sex }
									</c:otherwise>
								</c:choose>
							
							</td>
							<td>${e.position }</td>
							<td>${e.user.mobile }</td>
							<td>
								<div class="switch demo3" data-user-id="${e.userId }">
									<c:choose>
										<c:when test="${e.user.status eq '01' }">
											<input type="checkbox" checked="checked" />
										</c:when>
										<c:otherwise>
											<input type="checkbox"   />
										</c:otherwise>
									</c:choose>
									<label><i data-on="启用" data-off="停用"></i></label>
								</div>
							</td>
							<td class="td-last">
								<div class="operate">
									<a href="javascript:void(0)" onclick="viewData('${e.id}')">查看</a>
									<a href="javascript:void(0)" onclick="modifyData('${e.id}')">修改</a>
									<a href="javascript:void(0)" onclick="deleteData('${e.id}', ${st.index+1 }, '${e.user.name }')">删除</a>

									<c:if test="${ not empty needSendAccountEmployeesMap[ e.id ]}">
										<a href="javascript:void(0)" onclick="sendAccount('${e.user.account}', '${e.user.email}', '${e.user.mobile}', '${e.id }')">发送账号</a>
									 </c:if> 

								</div>
							</td>
						</tr>
					</c:forEach>
				
					<!--  
					<tr>
						<td>1</td>
						<td>张三</td>
						<td>100010</td>
						<td>男</td>
						<td>收银员</td>
						<td>18600711055</td>
						<td>
							<div class="switch demo3">
								<input type="checkbox">
								<label><i data-on="启用" data-off="停用"></i></label>
							</div>
						</td>
						<td class="td-last">
							<div class="operate">
								<a href="javascript:void(0)" onclick="viewData()">查看</a>
								<a href="javascript:void(0)" onclick="modifyData()">修改</a>
								<a href="javascript:void(0)" onclick="deleteData('100010', 1, '收银员')">删除</a>
								<a href="javascript:void(0)" onclick="sendAccount();">发送账号</a>
							</div>
						</td>
					</tr>
					-->
				</tbody>
			</table>
			<div class="pagingWrap">
			
			</div>
		</div>
		<!--点击按钮弹出添加界面 -->
		<div class="modal fade employee-dialog in " id="employee-add" data-backdrop="static" >
			<div class="modal-dialog" style="width:500px;">
				<div class="modal-content">	
					<div class="modal-header tenantInfo">	
						<label>新建员工</label>		  
				        <img src="<%=request.getContextPath()%>/images/close.png" class="img-close" data-dismiss="modal">
				    </div>
					<div class="modal-body">
						<form action="" method="post"  class="form-horizontal " name="add-form" id="add-form">
							<input type="hidden" id="employee_id" value="" />
							<input type="hidden" id="user_id" value="" />
							
							
							<h3 class="typeTitle">基础信息</h3>
							<div class="row">
								<div class="col-xs-12">
									<table class="employee_add_table">
										<tr>
											<td class="labelText"><span class="required-span">*</span>姓名：</td>
											<td><input type="text" id="user_name" name="name" maxlength="15"/></td>
											<td class="labelText">性别：</td>
											<td>
												<input type="radio" id="sexMale" name="sex" value="1" checked="checked" /><label for="sexMale" style="font-size:12px;">男</label>
												<input type="radio" id="sexFemale" name="sex" value="0"/><label for="sexFemale" style="font-size:12px;">女</label>
											</td>
										</tr>
										<tr>
											<td class="labelText"><span class="required-span">*</span>工号：</td>
											<td><input type="text" id="job_number" name="number"/></td>
											<td class="labelText">职务：</td>
											<td><input type="text" id="position" name="position"/></td>
										</tr>
										<tr>
											<td class="labelText">联系电话：</td>
											<td><input type="text" id="mobile" name="mobile" oninput="$('#email').valid()"/></td>
											<td class="labelText">邮箱：</td>
											<td><input type="text" id="email" name="email" oninput="$('#mobile').valid()" maxlength="35"/></td>
										</tr>
										<tr>
											<td class="labelText"><span class="required-span">*</span>出生日期：</td>
											<td><input type="text" class="Wdate" id="birthdate" name="birthdate" readOnly onFocus="WdatePicker( {onpicked:function(){$(this).valid();}});"/>
											</td>
											<td></td>
											<td></td>
										</tr>
										<tr>
											<td class="labelText">家庭住址：</td>
											<td colspan="3"><input type="text" id="home_address" maxlength="25" name="address"/></td>
										</tr>
									</table>
								</div>
							</div>			
							<div class="employee_add_line"></div>			
							<h3 class="typeTitle">授权信息</h3>
							<div class="row">
								<div class="col-xs-12">
									<table class="employee_add_table">
										<tr>
											<td class="labelText"><span class="required-span">*</span>角色权限：</td>
											<td colspan="3">
												<!-- <select class="role_select">
													<option></option>
													<option value="1">店长</option>
													<option value="2">收银员</option>
												</select> -->
												
												<div class="accountRoleList">
													
													<span class="selectedText"></span>
													<i class="icon-listArr"></i>
													<input type="hidden" id="accountRoleList_input" name="accountRoleList_input" />
													<ul>
														<li><input type="checkbox" /><label>abc</label></li>
														<li><input type="checkbox" /><label>abc</label></li>
													</ul>
												</div>
												
												<button type="button" class="btn in-btn200" id="btn_create_role">新建角色</button>
											</td>
										</tr>
									</table>
								</div>
							</div>			
							<div class="btn-operate">
								<button class="btn btn-save in-btn135 " type="button" style="width:200px" id="toModify">编辑资料</button>
							</div>		
							<div class="btn-operate" id="modify-opera" style="display:none">
								<button class="btn btn-cancel in-btn135" type="button" id="modifyCancel" >取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save in-btn135 " type="submit" > 确认</button>
							</div>	
						</form>
					</div>
				</div>
			</div>
		</div>
		
		<!--权限 -->
		<div class="modal fade employees-dialog in " id="employees_limite" data-backdrop="static" >
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
								<button class="btn btn-save  " id="deleteComfirm-type-save" type="button"  >确认</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div><!-- end delete  -->
<!-- 		 <div class="modal fade .dishes-detailDel-dialog in sendMsgState" id="successPrompt_del"  >
			<div class="modal-dialog" style="width:310px;">
				<div class="modal-content" style="width:310px;">	
					<div class="modal-body pop-div-content" style="height:210px;">
						<p><i class="icon-success"></i></p>
							<p class="tipP"> <i class="icon-ok"></i><label id="promptMsg">删除成功</label></p>
					</div>
				</div>
			</div>
		</div> -->
		<!--发送方式 -->
		<div class="modal fade tenant-dialog in " id="sendType" data-backdrop="static" >
			<div class="modal-dialog" style="width: 350px;">
				<div class="modal-content">	
					<div class="modal-header tenantInfo">	
						<span class="title">发送方式</span> 
				        <img src="<%=request.getContextPath()%>/images/close.png" class="img-close" data-dismiss="modal">
				    </div>
					<div class="modal-body">
						<div class="row">
							<div class="col-xs-12">
								<input type="hidden" id="emp_id"/>
								<div class="btn-operate " id="sendTypeAccountWrap" style="display:none;">
									<i class="icon-sendSuccess"></i>创建成功，账号：<label id="sendTypeAccount">258712</label>
								</div>
								<div class="btn-operate sendType">
									<button class="btn in-btn200" id="sendAccountEmail" type="button" onclick="sendAccountWay(1)">通过邮箱发送</button>
								</div>
								<div class="btn-operate sendType">
									<button class="btn in-btn200 " id="sendAccountPhone"  type="button" onclick="sendAccountWay(2)">通过手机短信发送</button>
								</div>
							</div>
						</div>						
					</div>
				</div>
			</div>
		</div>
		<!-- 发送提示 -->
<!-- 		<div class="modal fade sendMsgState in " id="successPrompt"  style="z-index:99999" >
			<div class="modal-dialog" style="width:310px;">
				<div class="modal-content" >	
					<div class="modal-body pop-div-content">
						<p><i class="icon-success"></i></p>
						<p class="tipP"> <label id="promptMsg">账号发送成功</label></p>
					</div>
				</div>
			</div>
		</div> -->
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
		
		<!-- /.modal -->
		<div class="modal fade .dishes-detailDel-dialog in sendMsgState" id="successPrompt"  >
			<div class="modal-dialog" style="width:310px;">
				<div class="modal-content" style="width:310px;">	
					<div class="modal-body pop-div-content" style="height:210px;">
						<p><i class="icon-success"></i></p>
							<p class="tipP"> <i class="icon-ok"></i><label id="promptMsg">保存成功</label></p>
					</div>
				</div>
			</div>
		</div>
		<!-- 临时表单 -->
		<form id="tmpForm" action="<%=request.getContextPath()%>/t_employeeUser/list" method="post" >
			
		</form>
		
		
		<script type="text/javascript">
			var sendcounttimer;
			$(function(){
				$("img.img-close").hover(function(){
				 	$(this).attr("src",global_Path+"/images/close-active.png");	 
				},function(){
					$(this).attr("src",global_Path+"/images/close-sm.png");
				});
				$.validator.setDefaults({ ignore: "" }); 
				jQuery.validator.addMethod("mobile", function(value, element) {
				    var length = value.length;
				    var mobile =  /^(((1[0-9]{1})|(1[0-9]{1}))+\d{9})$/;
				    return this.optional(element) || (length == 11 && mobile.test(value));
				}, "请输入11位手机号");
				
				jQuery.validator.addMethod("isExistJobNumber", function(value, element) {
				    var result=false;
				    var data={};
				    data.jobNumber=value;
				    data.uid=$("#add-form").find("#user_id").val();
				    $.ajaxSetup({ async: false });
				    $.post( global_Path+ "/t_employeeUser/checkEmployeeJobNumber.json",data,function(json){
				    	if(json.isSuccess){
				    		result=!json.data.isExist;
				    	}else{
				    		alert(json.errorMsg);
				    	}
				    },'json'); 
				    $.ajaxSetup({ async: true});
				    
				    return result;
				}, "门店中已经存在此工号！");
				jQuery.validator.addMethod("isExistBackAccount", function(value, element) {
				    var result=false;
				    var data={};
				    data.backAccount=value;
				    data.uid=$("#add-form").find("#user_id").val();
				    $.ajaxSetup({ async: false });
				    $.post( global_Path+ "/t_employeeUser/checkEmployeeBackAccount.json",data,function(json){
				    	if(json.isSuccess){
				    		result=!json.data.isExist;
				    	}else{
				    		alert(json.errorMsg);
				    	}
				    },'json'); 
				    $.ajaxSetup({ async: true });
				    
				    return result;
				}, "系统中已经存在此后台账号！");
				
				jQuery.validator.addMethod("isExistEmail", function(value, element) {
					//email值为空的时候，不做唯一性校验
				    var result=false;
				    if( value!=''){
				    	var data={};
					    data.email=value;
					    data.uid=$("#add-form").find("#user_id").val();
					    $.ajaxSetup({ async: false });
					    $.post( global_Path+ "/t_user/validateEmail.json",data,function(json){
					    	if(json.isSuccess){
					    		result=!json.data.isExist;
					    	}else{
					    		alert(json.errorMsg);
					    	}
					    },'json'); 
					    $.ajaxSetup({ async: true });
				    }else{
				    	result=true;
				    }
				    return result;
				}, "系统中已经存在该邮箱！");
				
				jQuery.validator.addMethod("isExistMobile", function(value, element) {
					//email值为空的时候，不做唯一性校验
				    var result=false;
				    if( value!=''){
				    	var data={};
					    data.mobile=value;
					    data.uid=$("#add-form").find("#user_id").val();
					    $.ajaxSetup({ async: false });
					    $.post( global_Path+ "/t_user/validateMobile.json",data,function(json){
					    	if(json.isSuccess){
					    		result=!json.data.isExist;
					    	}else{
					    		alert(json.errorMsg);
					    	}
					    },'json'); 
					    $.ajaxSetup({ async: true });
				    }else{
				    	result=true;
				    }
				    return result;
				}, "系统中已经存在该手机号！");
				
				jQuery.validator.addMethod("requiredTo", function(value, element) {
					//调用验证方法，刷新验证信息
					/* $("#email").valid();
					$("#mobile").valid(); */
					return !($("#email").val() == "" && $("#mobile").val() == "");
				}, "手机和邮箱至少填写一个");
				
				jQuery.validator.addMethod("accountRoleList_input", function(value, element) {
					return !($("#accountRoleList_input").val() == "");
				}, "请至少选择一个角色!");
				
				$("#add-form").validate({
					rules:{
						address: {
							maxlength :30
						},
						name : {
							required : true,
							maxlength :5,
						},
						number : {
							required : true,
							isExistJobNumber:true,
						},
						mobile : {
							mobile : true,
							requiredTo:true,
							isExistMobile:true
						},
						email : {
							email : true,
							requiredTo:true,
							isExistEmail:true
						},
						birthdate : {
							required : true
						},
						position : {
							maxlength :10
						},
						back_account:{
							required : true,
							maxlength:30,
							isExistBackAccount:true
						},
						accountRoleList_input:{
							accountRoleList_input:true
						}
						
					},
					messages: {
						address: {
							maxlength :"最多输入30个字符",
						},
						mobile : {
							
						},
						email : {
							email : "请输入正确的邮箱"
						}
					},
					submitHandler : function(form) {
						saveEmployee();
					}
				});
				
				/* $(".role_select").change(function(){
					if($(this).val() != ""){
						if($("#seeLimite").length < 1){
							$(this).width("173px");
							var str = '<button type="button" class="btn in-btn200" id="seeLimite" onclick="openLimite()">查看权限</button>';
							$(this).after(str);
							$(this).parents("table.employee_add_table").append('<tr class="bAccount"><td class="labelText">后台账号：</td><td colspan="3"><input type="text"/></td></tr>');
						}
					}else{
						$(this).width("260px");
						$("#seeLimite").remove();
						$(this).parents("table.employee_add_table").find("tr.bAccount").remove();
					} 
				}); */
				$(".btn-first").click(function(){
					
					$("#toModify").parent().hide();
					$("#modify-opera").show();
					$("#employee-add").modal("show").attr("data-type","new");
					//$("#employee-add").find(".modal-header label").text("新建账户");
					//重置表单
					//火狐重置表单不会处理hidden的input
					$("#employee-add").find("input[type=hidden]").val('');
					$("#add-form")[0].reset();
					
					$(".accountRoleList").find("input[type=hidden]").val('');
					loadRoleList(); 
					uploadAccountRoleListCheckedInfo();
					$("label.error").remove();// 清除之前的验证提未信息。
					$("input, select").removeClass('error');
					
					$("#employee-add").modal("show").find("input, select").prop("disabled", false);
				});
				// 编辑
				$("#toModify").click(function(){
					$(this).parent().hide();
					$("#modify-opera").show();
					$("#employee-add").attr("data-type","view").find("input, select").prop("disabled",false);
					$("#employee-add").find(".modal-header label").text("修改账户");
				});
				// 取消
				$("#modifyCancel").click(function(){
					var dialog = $("#employee-add");
					if(dialog.attr("data-type") == "new"){
						dialog.modal("hide");
					}else{
						$(this).parent().hide();
						
						$("#toModify").parent().show();
						dialog.find("input, select").prop("disabled",true);
						dialog.find(".modal-header label").text("查看账户");
						
					}
				});
				
				
				//绑定启用与禁用
				$(".switch").each(function(i,ele){
					//点击的时候，改变内部checkbox的值
					var ele_i= $(ele).find("i");
					var ele_checkbox=$(ele).find("input[type=checkbox]");
					var checkedStatus=false;
					$(ele_checkbox).click( function(){
						//alert($(ele_checkbox).prop("checked") );
						
						$.post( global_Path+ "/t_employeeUser/changeUserStatus", { id: $(ele).attr("data-user-id"), status: $(ele_checkbox).prop("checked")  },function(data){
							
							if(data.isSuccess){
								$("#successPrompt").find("#promptMsg").html("保存成功");
								$("#successPrompt").modal("show");
								window.setTimeout(function(){
									$("#successPrompt").modal("hide");
									//重新加载当前页面，并包含分页信息
									window.location.href=global_Path+ '/t_employeeUser/list?current='+$("#current").val()+"&pagesize="+$("#pagesize").val();
								}, 1000);
							}else{
								$("#successPrompt").find("#promptMsg").html(data.message);
								$("#successPrompt").modal("show");
								window.setTimeout(function(){
									$("#successPrompt").modal("hide");
									//重新加载当前页面，并包含分页信息
									//window.location.href=global_Path+ '/t_employeeUser/list?current='+$("#current").val()+"&pagesize="+$("#pagesize").val();
								}, 1000);
							}
					    });
						
					});
				});
				
				
				//分页
				if( '${page.total}' > 10 ){
					$(".pagingWrap").html('<ul class="paging clearfix">');
	        		$(".paging").twbsPagination({
						totalPages: '${page.pageCount}',
	        			visiblePages: 7,
	        			startPage : parseInt('${page.current}'),
	        			first: '...',
	        			prev : '<',
	        			next : '>',
				        last: '...',
				        onPageClick: function (event, page){
				        	window.location.href=global_Path+ '/t_employeeUser/list?current='+page+"&pagesize="+$("#pagesize").val();
				        }
					});
	        		
				}else {
					$(".pagingWrap").empty();
				}
				
				//绑定 创建角色按钮
				$("#btn_create_role").click(function(){
					//alert("创建角色");
					// 调用弹出层，加载页面
					$("#employees_limite").find(".modal-body").html("");
					$("#employees_limite").find(".modal-body").load(global_Path+"/t_role/roleList4Store/edit?id=&isModify=true&callBackMethod=callBackMethod",function(){
						$(".icon-arr").css("display","none"); //隐藏一个页面中的 小图标。
						$("#employees_limite").modal("show");
					});
					
				});
				
				//处理选择角色列表的方法
				$(".selectedText").click(function(){
					$(this).next("i").toggleClass("reverse");
					$(this).siblings("ul").toggle();
				});
				
				
			});
			// 查看
			function viewData (id){
				loadEmployeeUser(id);
				
				$("#employee-add").attr("data-type","view").modal("show").find("input, select").prop("disabled", true);
				$("#employee-add").find(".modal-header label").text("查看账户");
				$("#toModify").parent().show();
				$("#modify-opera").hide();
				
			}
			// 修改
			function modifyData (id){
				$("#employee-add").attr("data-type","new").modal("show").find("input, select").prop("disabled", false);
				$("#employee-add").find(".modal-header label").text("修改账户");
				$("#toModify").parent().hide();
				$("#modify-opera").show();
				$(".send").show();
				$(".viewAccount").width("200px");
				
				loadEmployeeUser(id);
				
			}
			// 查看权限
			function openLimite() {
				//获取select的权限id
				var roleid=$(".accountRoleList").find("input[type=hidden]").val();
				$("#employees_limite").find(".modal-body").load(global_Path+"/t_role/roleList4Store/detail?id="+roleid+"",function(){
					$("#employees_limite").find(".icon-arr").hide();
					$("#employees_limite").find(".limiteInfo").hide();
					$("#employees_limite").find(".btn-operate").hide();
					$("#employees_limite").modal("show");
					
				});
				
			}
			// 删除
			function deleteData(id, i, name) {
				$("#deleteComfirm").modal("show");
				$("#showName").text(name).attr("data-id", id).attr("tr-id", i);
				
				//绑定删除 确认框中得 确定按钮事件
				$("#deleteComfirm").find("#deleteComfirm-type-save").click(function(){
					//alert( $("#deleteComfirm").find("#showName").attr("data-id") );
					$.post( global_Path+ "/t_employeeUser/delete", { id: $("#deleteComfirm").find("#showName").attr("data-id") },function(data){
						$("#deleteComfirm").modal("hide");
						if(data.isSuccess){
							$("#successPrompt").find("#promptMsg").html("删除成功");
							$("#successPrompt").modal("show");
							window.setTimeout(function(){
								$("#successPrompt").modal("hide");
								//重新加载当前页面，并包含分页信息
								var current = 1;
								if($(".employees-table tbody tr").length == 1){
									current = parseInt($("#current").val()) - 1;
									if(current < 1){
										current = 1;
									}
								}else{
									current = parseInt($("#current").val()) ;
								}
								window.location.href=global_Path+ '/t_employeeUser/list?current='+current+"&pagesize="+$("#pagesize").val();
							}, 1000);
						}else{
							$("#successPrompt").find("#promptMsg").html("删除失败");
							$("#successPrompt").modal("show");
							window.setTimeout(function(){
								$("#successPrompt").modal("hide");
								//重新加载当前页面，并包含分页信息
								//window.location.href=global_Path+ '/t_employeeUser/list?current='+$("#current").val()+"&pagesize="+$("#pagesize").val();
							}, 1000);
						}
				    });
					
				});
				
			}
			/*
			 * 打开发送方式窗口
			 */
			function sendAccount(account,email,mobile,id){
				if(id != null && id != ""){
					$("#emp_id").val(id);
				}
				//如果来源是创建账户后提示，则弹出框标题为发送帐号信息
				if(account!=null&&email!=null&&mobile!=null){
					$("#sendType .title").text("发送方式");
				}else{
					$("#sendType .title").text("发送账户信息");
				}
				if(email!=null){
					$("#checkCode input[name='email']").val(email);
				}else{
					$("#checkCode input[name='email']").val($("#email").val());
				}
				if(mobile!=null){
					$("#checkCode input[name='mobile']").val(mobile);
				}else{
					$("#checkCode input[name='mobile']").val($("#mobile").val());
				}
				if(account!=null){
					$("#checkCode input[name='account']").val(account);
				}else{
					$("#checkCode input[name='account']").val($("#account").val());
				}
				if($("#checkCode input[name='account']").val()!=null&&$("#checkCode input[name='account']").val()!=''){
					$("#sendType").modal("show");
					if($("#tenant-add").attr("data-type") == "old"){
						$("#sendTypeAccountWrap").hide();
					}
					$("#sendTypeAccount").text($("#account").val());
					
					var num = localStorage.getItem(id);
					var curSeconds = getCurSeconds();
					var num1 = 120 - (curSeconds - num);
					if(num1 > 0){
						$("#sendAccountEmail").attr("disabled","disabled");
						$("#sendAccountPhone").attr("disabled","disabled");
					}else{
						initSendBtn();
					}
				}else{
					sendMsg(false,"该员工没有帐号，无法发送！");
				}
			}
			/**
			 * 获取当前秒数
			 * @returns
			 */
			function getCurSeconds(){
				var myDate = new Date();
				var curSeconds = myDate.getHours() * 3600 + myDate.getMinutes() * 60 + myDate.getSeconds();
				return curSeconds;
			}
			/**
			 * 判断邮箱验证还是手机验证可用
			 */
			function initSendBtn(){
				//判断是否有邮箱，如果没有邮箱，则不能用邮箱发送
				if($("#checkCode input[name='email']").val()!=null&&$("#checkCode input[name='email']").val()!=''){
					$("#sendAccountEmail").removeAttr("disabled");  
				}else{
					$("#sendAccountEmail").attr("disabled","disabled");  
				}
				//判断是否有手机，如果没有手机，则不能用手机发送
				if($("#checkCode input[name='mobile']").val()!=null&&$("#checkCode input[name='mobile']").val()!=''){
					$("#sendAccountPhone").removeAttr("disabled");  
				}else{
					$("#sendAccountPhone").attr("disabled","disabled");  
				}
			}
			/**
			 * 发送账号成功之后，等待120秒
			 */
			function sendTimeOut_fun(){
				$("#sendAccountEmail").prop("disabled", true);
				$("#sendAccountPhone").prop("disabled", true);
				sendcounttimer = setInterval(function(){
					initSendBtn();
					clearInterval(sendcounttimer);
				}, 4000);
			}
			/*
			 * 发送方式点击，打开验证码窗口
			 */
			function sendAccountWay(type){
				var curSeconds = getCurSeconds();
				if($("#sendType #emp_id").val() != null && $("#sendType #emp_id").val() != ''){
					localStorage.setItem($("#sendType #emp_id").val(), curSeconds);
				}
				if(type==1){
					var url = global_Path + "/t_user/sendAccountByMail";
					var dataObj = new Object();
					dataObj.account = $("#checkCode input[name='account']").val();
					dataObj.email = $("#checkCode input[name='email']").val();
					//提交数据
					$.post(url, dataObj, function(result){
						var data = $.parseJSON(result);
						if(data.success==true){
							var msg = '';
							if(data.msg!=null){
								msg = data.msg;
							}else{
								msg = '帐号发送成功！';
							}
							$("#sendType").modal("hide");
							sendTimeOut_fun();
							sendMsg(true,msg);
						}else{
							var msg = '';
							if(data.msg!=null){
								msg = data.msg;
							}else{
								msg = '帐号发送失败！请重发';
							}
							sendMsg(false,msg);
						}
					});
				}else if(type==2){
					$("#checkCode").modal("show");
					$("#valicodeImg").attr("src",global_Path+"/t_user/sendAccountByMailValicode?"+Math.random());
					$("#checkCode .errorTips").html("");
					$("#valicodeText").val("");
					$("#valicodeButton").unbind("click");
					$("#valicodeButton").click(function(){
						var url = global_Path + "/t_user/sendAccountByMobile";
						var dataObj = new Object();
						dataObj.account = $("#checkCode input[name='account']").val();
						if($("#valicodeText").val()==null||$("#valicodeText").val()==''){
							$("#checkCode .errorTips").html('请输入验证码');
							return false;
						}
						dataObj.valicode = $("#valicodeText").val();
						dataObj.mobile = $("#checkCode input[name='mobile']").val();
						//提交数据
						$.post(url, dataObj, function(result){
							var data = $.parseJSON(result);
							if(data.success==true){
								var msg = '';
								if(data.msg!=null){
									msg = data.msg;
								}else{
									msg = '帐号发送成功！';
								}
								$("#sendType").modal("hide");
								$("#checkCode").modal("hide");
								sendTimeOut_fun();
								sendMsg(true,msg,function(){
									$("#checkCode .errorTips").html('');
								});
							}else{
								var msg = '';
								if(data.msg!=null){
									msg = data.msg;
								}else{
									msg = '图文验证码错误';
								}
								$("#checkCode .errorTips").html(msg);
								//重置验证码
								$("#valicodeImg").attr("src",global_Path+"/t_user/sendAccountByMailValicode?"+Math.random());
								$("#valicodeText").val("");
							}
						});
					});
				}
			}
			/**
			 * 重置验证码
			 */
			function resetValicode(){
				$("#valicodeImg").attr("src",global_Path+"/t_user/sendAccountByMailValicode?"+Math.random());
				$("#checkCode .errorTips").html("");
				$("#valicodeText").val("");
			}
			
			
			
		</script>
		<script type="text/javascript">
		
			/*
			保存员工账号
			*/
			function saveEmployee(){
				//对象
				var employee={};
				employee.user={};
				employee.roles=[];
				employee.id=$("#employee_id").val();
				employee.user.id=$("#user_id").val();
				employee.user.name=$("#user_name").val();
				employee.userId=$("#user_id").val();
				employee.sex=$('input[name="sex"]:checked').val();
				employee.jobNumber=$("#job_number").val();
				employee.position=$("#position").val();
				employee.user.mobile=$("#mobile").val();
				employee.user.email=$("#email").val();
				employee.birthdate=$("#birthdate").val();
				employee.homeAddress=$("#home_address").val();
				/* employee.role.id=$(".role_select").val(); */
				//遍历获取role
				var selectedRoles=$(".accountRoleList").find("input[type=hidden]").val();
				$.each( selectedRoles.split(",") , function(i,obj){
					employee.roles.push({id:obj});
				}); 
				if( $("#back_account").length>0 ){
					employee.user.account=$("#back_account").val();
				}
				
				//禁用保存按钮
				$("#add-form").find(".btn-save").prop("disabled", true);
				$.ajax({
					url : global_Path+ "/t_employeeUser/save",
					type : "POST",
					dataType : "json",
					contentType : "application/json; charset=utf-8",
					data : JSON.stringify(employee),
					//data:stampInfo,
					success : function(data) {
						$("#add-form").find(".btn-save").prop("disabled", false);
						if(data.isSuccess){
							$("#successPrompt").find("#promptMsg").html("保存成功");
							$("#successPrompt").modal("show");
							window.setTimeout(function(){
								$("#employee-add").modal("hide");
								$("#successPrompt").modal("hide");
								//重新加载当前页面，并包含分页信息
								window.location.href=global_Path+ '/t_employeeUser/list?current='+$("#current").val()+"&pagesize="+$("#pagesize").val();
							}, 1000);
						}else{
							$("#successPrompt").find("#promptMsg").html(data.message);
							$("#successPrompt").modal("show");
							window.setTimeout(function(){
								$("#successPrompt").modal("hide");
							}, 1000);
						}
						
						
					},
					error : function(XMLHttpRequest, textStatus,errorThrown) {
						alert(errorThrown);
						$("#add-form").find(".btn-save").prop("disabled", false);
					}
				});
				
			}
		
			/**
				加载权限角色列表
			**/
			function loadRoleList(){
				 $.get(global_Path+ "/t_role/roleList4Store.json", function(result){
					   var roleList=result.roleList;
					   var sel=$(".accountRoleList").find("ul");
					   var hiddenInput=$(".accountRoleList").find("input[type=hidden]");
					   var selectedText=$(".accountRoleList").find(".selectedText");
					   var oldSelectRoleIds=$(hiddenInput).val();
					   $(sel).empty();
					   //sel.append("<option value=''>---请选择---</option>");
					   $.each(roleList,function(i,ele){
						   sel.append("<li><input type='checkbox' id='accountRole_"+ele.id+"' value='"+ele.id+"' /> <label for='accountRole_"+ele.id+"'>"+ele.name+"</label></li>");
					   });
					 	//绑定其中的checkbox事件
					   $.each( $(sel).find("input[type=checkbox]") , function(i,ele){
							$(ele).click( function(){
								uploadAccountRoleListCheckedInfo();
								$("#add-form"). valid();
							} );
					   });
					 	
					   //如果原来存在选中的 role的时候，在重新加载完毕权限列表后，进行还原选中状态
					   $.each( oldSelectRoleIds.split(","),function(index,element){
						   $(sel).find("input[type=checkbox][value='"+element+"']").prop("checked",true);
					   });
					   
				 });
				 
				
			}
			
			/*
			*更新选中的权限信息
			*/
			function uploadAccountRoleListCheckedInfo(){
				var hiddenInput=$(".accountRoleList").find("input[type=hidden]");
				var selectedText=$(".accountRoleList").find(".selectedText");
				var sel=$(".accountRoleList").find("ul");
				//获取所有得选中的checkbox
				var allCheckedBox= $(sel).find("input[type=checkbox]:checked");
				var selectRoleArray=new Array();
				var selectRoleTextArray=new Array();
				$.each(allCheckedBox,function(index,obj){
					selectRoleArray.push( $(obj).val() );
					selectRoleTextArray.push( $(obj).next().html() );
				});
				//保存选中的值到  input ，截取文本用于显示
				$(hiddenInput).val( selectRoleArray.join(","));
				var sText=selectRoleTextArray.join(",");
				$(selectedText).html( sText);
				
				//根据权限变化，显示查看按钮
				if($(hiddenInput).val() != ""){
					if($("#seeLimite").length < 1){
						$(".accountRoleList").width("173px");
						$(".accountRoleList").find(".selectedText").width("163px");
						var str = '<button type="button" class="btn in-btn200" id="seeLimite" onclick="openLimite()">查看权限</button>';
						$(".accountRoleList").after(str);
						//$(".accountRoleList").parents("table.employee_add_table").append('<tr class="bAccount"><td class="labelText">后台账号：</td><td colspan="3"><input type="text"/></td></tr>');
					}
					//检查角色权限
					$.get(global_Path+ "/t_role/roleList4Store/checkEmployeeRole.json",{ids:$(hiddenInput).val()}, function(json){
						if( json.isSuccess){
							if( json.data.needAccount){
								var _old_account=$(".accountRoleList").parents("table.employee_add_table").find("tr.bAccount").find("#back_account").val();
								$(".accountRoleList").parents("table.employee_add_table").find("tr.bAccount").remove();
								$(".accountRoleList").parents("table.employee_add_table").append('<tr class="bAccount"><td class="labelText">后台账号：</td><td colspan="3"><input type="text" id="back_account" name="back_account" value="'+(undefined==_old_account?'':_old_account)+'" /></td></tr>');
								
							}else{
								$(".accountRoleList").parents("table.employee_add_table").find("tr.bAccount").remove();
							}
						}else{
							alert(json.errorMsg);
						}
					}, 'json');
					
				}else{
					$(".accountRoleList").width("260px");
					$(".accountRoleList").find(".selectedText").width("250px");
					$("#seeLimite").remove();
					$(".accountRoleList").parents("table.employee_add_table").find("tr.bAccount").remove();
				} 
				
				//$("#add-form"). valid();
			}
			
			
			function loadEmployeeUser(id){
				$.ajaxSetup({ async: false });
				$.get(global_Path+ "/t_employeeUser/get",{id:id}, function(result){
					   var employee=result;
					   //将对象赋值到form 
						$("#employee_id").val(employee.id );
						$("#user_id").val(employee.user.id);
						$("#user_name").val(employee.user.name);
						$('input[name="sex"][value="'+employee.sex+'"]').prop("checked",true);
						$("#job_number").val(employee.jobNumber);
						$("#position").val(employee.position);
						$("#mobile").val(employee.user.mobile);
						$("#email").val(employee.user.email);
						$("#birthdate").val( (new Date( employee.birthdate )).format("yyyy-MM-dd") );
						$("#home_address").val(employee.homeAddress);
						/*$(".role_select").val(employee.role.id); */
						//如果角色存在那么应该显示 查看权限按钮。
						/* $(".role_select").change(); */
						
						var roles=employee.roles; //ARRAY
						var selectedRoleArray=new Array();
						$.each(roles , function(i,ele){
							if(null!=ele && undefined!=ele){
								selectedRoleArray.push( ele.id );
							}
						});
						$(".accountRoleList").find("input[type=hidden]").val(selectedRoleArray.join(","));
						//如果原来存在选中的 role的时候，在重新加载完毕权限列表后，进行还原选中状态
						$.each( selectedRoleArray,function(index,element){
							$(".accountRoleList").find("input[type=checkbox][value='"+element+"']").prop("checked",true);
						});

						loadRoleList(); 
						uploadAccountRoleListCheckedInfo();
						/* $("label.error").remove();// 清除之前的验证提未信息。
						$("input, select").removeClass('error'); */
						//将账号信息显示在 文本框中
						if($(".accountRoleList").parents("table.employee_add_table").find("tr.bAccount").length>0 ){
							$(".accountRoleList").parents("table.employee_add_table").find("tr.bAccount input").val(employee.user.account);
						}
						$("#add-form"). valid();//需要再次调用验证，否则账号这个地方会显示 要求账号必填。
				 }, 'json');
				
				$.ajaxSetup({ async: true });
			}
			
			
			function callBackMethod(isRefresh){
				$("#employees_limite").modal("hide");
				if(isRefresh){ //只有点击确认保存了 权限，才会要求强制刷新
					$.ajaxSetup({ async: false });
					loadRoleList();	
					uploadAccountRoleListCheckedInfo();
					$("#add-form"). valid();
					$.ajaxSetup({ async: true });
				}
			}
			
			/**
			 * 发送消息
			 * @param isOk 是否成功
			 * @param msg
			 * @param callback
			 * @returns
			 */
			 function sendMsg(isOk,msg,callback){
					if(isOk){
						$("#successPrompt .modal-body i").removeClass("icon-fail").addClass("icon-success");
					}else{
						$("#successPrompt .modal-body i").removeClass("icon-success").addClass("icon-fail");
					}
					$("#successPrompt").find("#promptMsg").parent().html('<label id="promptMsg"></label>');
					var msgArray = msg.split("！");
					if(msgArray.length>1){
						for(var i=0;i<msgArray.length;i++){
							if(i==0){
								$("#successPrompt").find("#promptMsg").html(msgArray[0]+"！\n");
							}else if(i==msgArray.length-1){
								$("#successPrompt").find("#promptMsg").parent().append('<label>'+msgArray[i]+'</label>');
							}else{
								$("#successPrompt").find("#promptMsg").parent().append('<label>'+msgArray[i]+'！</label>');
							}
						}
					}else{
						$("#successPrompt").find("#promptMsg").html(msg);
					}
					
					$("#successPrompt").modal("show");
					if(callback!=null){
						if(isOk){
							window.setTimeout(callback, 1000);
							window.setTimeout('$("#successPrompt").modal("hide")', 1000);
						}else{
							window.setTimeout(callback, 1000);
						}
					}else{
						if(isOk){
							window.setTimeout('$("#successPrompt").modal("hide")', 1000);
						}
					}
				}
			/**
			* 供主页面查询调用
			* text 查询的内容
			*/
			function searchDataFromMain(text){
				var dataObj = new Object();
				dataObj.current = 1;
				dataObj.pagesize = $("#pagesize").val();
				dataObj.searchText = text;
				
				//var form = $("<form method='post'></form>");
			    //form.attr({"action":global_allPath+ "/t_employeeUser/list"});
			    var form = $("#tmpForm");
			    for (arg in dataObj)
			    {
			        var input = $("<input type='hidden'>");
			        input.attr({"name":arg});
			        input.val(dataObj[arg]);
			        form.append(input);
			    }
			    form.submit();
			}
		</script>
</body>
</html>
