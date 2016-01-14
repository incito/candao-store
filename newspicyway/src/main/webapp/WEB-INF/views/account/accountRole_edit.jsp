<%@ page language="java" contentType="text/html; charset=UTF-8"pageEncoding="UTF-8"%>
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
		.sendMsgState{
			overflow:hidden;
		}
	</style>
	
<script type="text/javascript">
//调api获取数据
function initCouponData(){
	var name = $("#roleName").val();
	if(name!=''&& name!=null){
		$.post(global_Path + "/t_role/getRoleByName.json", {
		   name :name,
		}, function(result){
			if(result==0){
				$("#roleName1").addClass("hide");
			}else{
				$("#roleName1").removeClass("hide");
			}
		});
	} 
}
</script>
</head>
<body>
<form id="limiteForm" name="limiteForm">
	<input id="roleId" value="${roleVO.role.id}" type="hidden" />
	<i class="icon-arr"></i>
	<div class="modal-header limiteInfo" style="border-bottom: 0px none;">
		<label>角色名称：</label> <input onkeyup="initCouponData()" id="roleName" name="roleName" style="font-size:12px;" value="${roleVO.role.name }"/>
		<label id="roleName1" class="error hide">该角色已存在</label>
	</div>
	<div class="modal-body" style="padding: 0px;">
		<div class="limiteInner">
		<c:forEach items="${headOfficeFunctions}" var="topfn" varStatus="sta">
			<div class="roleNameWrap">${topfn.name }</div>
			<ul style="padding-left:10px;">
				<c:forEach var="fn" items="${topfn.childFuns}" varStatus="stat">
					<li class="clearfix">
						<span class="roleTypeWrap">${fn.name}</span>
						<dl class="roleDetailWrap">
							<c:forEach items="${fn.childFuns }" var="subFn" varStatus="state">
								<dd >
									<c:choose>
										<c:when test="${!empty selectedMap[subFn.id] }">
											<input id="checkbox${sta.count*100 + stat.count*10 + state.count}" type="checkbox" checked="checked" value="${subFn.id}" />
										</c:when>
										<c:otherwise>
											<input id="checkbox${sta.count*100 + stat.count*10 + state.count}" type="checkbox" value="${subFn.id}" />
										</c:otherwise>
									</c:choose>
									<label for="checkbox${sta.count*100 + stat.count*10 + state.count}">${subFn.name}</label>
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
			<button class="btn btn-cancel in-btn135" type="button" id="limiteForm_modifyCancel">取消</button>
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


	
	var isModify="${isModify}";
	var _callBackMethod="${callBackMethod}";
	
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
	}); 
	
	$("#limiteForm").find("#limiteForm_modifyCancel").click(function(){
		//debugger;
		//点击取消按钮。尝试调用回调函数
		if( typeof(window[_callBackMethod])=="function"){
			eval(_callBackMethod+"()"); 
		}
	}); 
	
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
	
	
/* 	$("#limiteForm").find("#limiteForm_modifySubmit").click(function(){
		
		
		
	}); */
	
	$("#limiteForm").validate({
		rules:{
			roleName: {
				maxlength :10,
				required : true,
			}
		},
		messages: {
			roleName: {
				maxlength :"最多输入10个字符",
			}
		},
		submitHandler : function(form) {
			_saveAccountRole();
		}
	});
	
	function _saveAccountRole(){
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
		
		var name = $("#roleName").val();
		if(name!=''&& name!=null){
			$.post(global_Path + "/t_role/getRoleByName.json", {
			   name :name,
			}, function(result){
				if(result==0){
					$("#roleName1").addClass("hide");
					//提交、
					$.ajax({
						url : global_Path+ "/t_role/roleList4HeadOffice/save",
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
				}else{
					
					$("#roleName1").removeClass("hide");
				}
			});
		}
		
		
	}
	
	

</script>
</body>
</html>