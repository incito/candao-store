<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>门店角色-表单</title>
<%-- <link href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css" rel="stylesheet"> --%>
<%-- <link rel="stylesheet" href="<%=request.getContextPath()%>/css/tenant.css"/> --%>
<%-- <script src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
<script
	src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script> --%>
	
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/jquery.validate.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/messages_zh.js"></script>
	<style type="text/css">
	#successPrompt{
		overflow:hidden;
	}
	</style>
</head>
<body>
<form id="limiteForm" name="limiteForm">
	<input id="roleId" value="${roleVO.role.id}" type="hidden" />
	<i class="icon-arr"></i>
	<div class="modal-header limiteInfo" style="border-bottom: 0px none;">
		<label>角色名称：</label> <input id="roleName" name="roleName" style="font-size:12px;" value="${roleVO.role.name }"/>
	</div>
	<div class="modal-body" style="padding: 0px;">
		<div class="limiteInner">
		<c:forEach items="${functions}" var="topfn">
			<ul style="padding-left:10px;">
				<c:forEach var="fn" items="${topfn.childFuns}">
					<li class="clearfix">
						<span class="roleTypeWrap" data-code="${fn.code}">${fn.name}</span>
						<dl class="roleDetailWrap">
							<c:forEach items="${fn.childFuns }" var="subFn">
								<dd >
									<c:choose>
										<c:when test="${!empty selectedMap[subFn.id] }">
											<input type="checkbox" checked="true" value="${subFn.id}" id="employeeRole_checkbox_${subFn.id}" data-code="${subFn.code}"/>
										</c:when>
										<c:otherwise>
											<input type="checkbox" value="${subFn.id}" id="employeeRole_checkbox_${subFn.id}" data-code="${subFn.code}"/>
										</c:otherwise>
									</c:choose>
									<label for="employeeRole_checkbox_${subFn.id}" style="cursor: pointer;">${subFn.name}</label>
								</dd>
							</c:forEach>
						</dl>
					</li>
				</c:forEach>
			</ul>
		</c:forEach>
		</div>

		<div class="btn-operate">
			<button class="btn btn-save in-btn135 " type="button" style="width: 200px" id="limiteForm_toModify">编辑</button>
		</div>
		<div class="btn-operate" id="modify-opera" style="display: none">
			<button class="btn btn-cancel in-btn135" type="button" id="limiteForm_modifyCancel" data-dismiss="modal">取消</button>
			<div class="btn-division"></div>
			<button class="btn btn-save in-btn135 " type="submit" id="limiteForm_modifySubmit">确认</button>
		</div>
	</div>
</form>

	<!-- 发送提示 -->
	<div class="modal fade sendMsgState in " id="successPrompt">
		<div class="modal-dialog" style="width: 310px;">
			<div class="modal-content">
				<div class="modal-body pop-div-content">
					<p>
						<i class="icon-success"></i>
					</p>
					<p class="tipP">
						<label id="promptMsg">操作成功</label>
					</p>
				</div>
			</div>
		</div>
	</div>


	<script type="text/javascript">
	
	var employee_background_login={};//.function_code
	//获取后台登陆模块编码
	employee_background_login.function_code="${EMPLOYEE_BACKGROUND_LOGIN_FUNCTION_CODE}";
	//获取后台登陆模块级联的模块编码
	employee_background_login.function_cascade_code="${EMPLOYEE_BACKGROUND_LOGIN_FUNCTION_CASCADE_CODE}";
	
	var isModify="${isModify}";
	var _callBackMethod="${callBackMethod}";
	
	$(function(){
		jQuery.validator.addMethod("isExistRoleName", function(value, element) {
		    var result=false;
		    var data={};
		    data.roleId=$("#limiteForm").find("#roleId").val();
		    data.roleName=$("#limiteForm").find("#roleName").val();
		    $.ajaxSetup({ async: false });
		    $.post( global_Path+ "/t_role/roleList4Store/roleNameCheck.json",data,function(json){
		    	if(json.isSuccess){
		    		result=!json.data.isExist;
		    	}else{
		    		alert(json.errorMsg);
		    	}
		    }); 
		    $.ajaxSetup({ async: true });
		    
		    return result;
		}, "门店中已经存在此角色名称！");
		
		$("#limiteForm").validate({
			rules:{
				roleName: {
					required :true,
					isExistRoleName:true
				}
				
			},
			messages: {
				/* address: {
					maxlength :"最多输入30个字符",
				} */
			},
			submitHandler : function(form) {
				//alert(1);
				_saveLimiteForm();
			}
		});
		
		

		
		if( isModify !="true" ){
			$("#limiteForm").find(" input , select ").prop("disabled",true);
		}else{
			//编辑。应该显示 取消和保存按钮
			$("#limiteForm").find("#limiteForm_toModify").hide();
			$("#limiteForm").find("#modify-opera").show();
		}
		
		//绑定事件
		$("#limiteForm").find("#limiteForm_toModify").click(function(){
			$(this).hide();
			$("#limiteForm").find("#modify-opera").show();
			$("#limiteForm").find("input").prop("disabled",false);
			//如果后台登陆的功能模块没有选中，那么处理级联的菜单功能。
			if( !$("#limiteForm").find("input[type=checkbox][data-code='"+employee_background_login.function_code+"']").is(":checked")){
				cascade(false);
			}
		}); 
		
		$("#limiteForm").find("#limiteForm_modifyCancel").click(function(){
			//点击取消按钮。尝试调用回调函数
			if( typeof(window[_callBackMethod])=="function"){
				eval(_callBackMethod+"()"); 
			}
		}); 
		
		
		//处理模块菜单级联问题。
		//登陆模块未选择，则其他几个模块不可用。
		if(employee_background_login.function_code!='' ){
			$("#limiteForm").find("input[type=checkbox][data-code='"+employee_background_login.function_code+"']").click(function(){
				cascade( $(this).is(":checked"));
			});
			 
			//默认设置为不选中
			cascade(false,false);
			
		}
		
		
		//绑定所有得checkbox，判断是否包含“编辑” 如果包含编辑的时候，获取“查看”xxx的按钮。点击编辑xxx的时候，自动选择查看xxx
		$("#limiteForm").find("input[type=checkbox]").each(function(i,obj){
			$(obj).click(function(){
				var label=$(this).next();
				var label_fn_name=label.html();
				if( label_fn_name.indexOf("编辑") >-1){
					if( $(this).is(":checked")){
						//选中的时候，应该自动关联此模块下 查看XXX的多选框
						var see_fun_name=label_fn_name.replace("编辑","查看");
						// 遍历所有这个模块的所有兄弟节点，判断是否有对应的查看;
						$(this).parent().parent().find("label").each(function(j,obj_lbl){
							if($(obj_lbl).html()==see_fun_name){
								//触发点击事件
								//$(obj_lbl).prev().trigger("click")
								$(obj_lbl).parent().find("input[type=checkbox]").prop("checked",true);
							}
						});
					}
				}
			});
		});
		
	});

	

	/*
	*级联选择：选择 后台登陆需要做的级联操作
	*@param checkStatus boolean类型。
			ture:选中了后台登陆选框 ； false:取消 
	 @param clearStatus 可选参数。默认为true
	 		true 代表禁用checkbox的时候，清除禁用的按钮的状态。
	 		false 表示禁用checkbox，但是不清除选中状态
	*/
	function cascade(checkStatus,clearStatus){
		var _clearStatus= undefined==clearStatus?true:clearStatus;
		var codes=employee_background_login.function_cascade_code.split(",");
		if(checkStatus){
			//显示可用
			$.each(codes,function(i,ele){
				var _span=$("#limiteForm").find("span[data-code='"+ele+"']");
				var _dl=$(_span).next();
				_dl.find("input[type=checkbox]").prop("disabled",false);
			});
		}else{
			//禁用
			$.each(codes,function(i,ele){
				var _span=$("#limiteForm").find("span[data-code='"+ele+"']");
				var _dl=$(_span).next();
				if(_clearStatus){
					_dl.find("input[type=checkbox]").prop("checked",false);
				}
				_dl.find("input[type=checkbox]").prop("disabled",true);
			});
		}
	}
	
	//$("#limiteForm").find("#limiteForm_modifySubmit").click(function(){
	function _saveLimiteForm(){
		var roleVO = {};
		roleVO.role={};// 存放权限具体信息
		roleVO.functions=[]; //存放选择的系统功能模块
		
		roleVO.role.id=$("#roleId").val();
		roleVO.role.name=$("#roleName").val();
		
		var fs=$("input[type=checkbox]:checked");
		$.each(fs,function(i,element){
			var o={};
			o.id=$(element).val();
			roleVO.functions.push(o);
		});
		
		//提交、
		$.ajax({
			url : global_Path+ "/t_role/roleList4Store/save",
			type : "POST",
			dataType : "json",
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify(roleVO),
			//data:stampInfo,
			success : function(data) {
				if( data.isSuccess ){
					$("#successPrompt").find("#promptMsg").html("保存成功");
					$("#successPrompt").modal("show");
					window.setTimeout(function(){
						$("#successPrompt").modal("hide");
						//点击取消按钮。尝试调用回调函数
						if( typeof(window[_callBackMethod])=="function"){
							eval(_callBackMethod+"(true)"); 
						}
					}, 1000);
				}else{
					$("#successPrompt").find("#promptMsg").html(""+data.message);
					$("#successPrompt").modal("show");
					window.setTimeout(function(){
						$("#successPrompt").modal("hide");
					}, 1000);
				}
			},
			error : function(XMLHttpRequest, textStatus,errorThrown) {
				alert(errorThrown);
			}
		});
		
		
	};
	
	

</script>
</body>
</html>