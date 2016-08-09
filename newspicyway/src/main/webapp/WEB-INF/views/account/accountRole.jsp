<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>角色权限</title>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/tenant.css"/>
	

	<script src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
	<script src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
	
</head>
<body>
		<div class="ky-content tenant-content">
			<!--  
			<span style="font-size : large;font-weight:bold ; ">
				<span style="cursor: pointer;" onclick="window.location.href='<%=request.getContextPath()%>/t_headuser/account';">员工管理 </span>| 
			</span>
			<span style="font-size : medium ;">
				角色权限
			</span>
			-->
			<h3 class="title">
				<a href="<%=request.getContextPath()%>/t_headuser/account">账户管理</a> | 角色权限
			</h3>
			<!-- button样式添加-->
			<div class="tenant-btn btn-add">
				<div class="btn-group" role="group" aria-label="...">
				  <button type="button" class="btn  btn-first">新建角色</button>
				</div>
			</div>
			<div class="roleList" id="roleList" style="height:auto">
				<p>角色名称</p>
				<ul>
					
					<c:forEach items="${roleList }" var="role" varStatus="st" >
						<li data-id="${role.id}">
							<span class="num">${st.index+1 }</span>
							<span class="roleName" style="width:120px;cursor: pointer;">${role.name }</span>
							<div class="delete">×</div>
						</li>
					</c:forEach> 
					
				</ul>
			</div>
			<!-- 点击角色 -->
			<div class="modal-dialog" style="width: 650px; display:none;" id="roleLimite" >
				<div class="modal-content" style="padding: 0 20px;">	
				</div>
			</div>
		</div>
		<!--新建角色-->
		<div class="modal fade employees-dialog in " id="employees_limite" data-backdrop="static">
			<div class="modal-dialog" style="width: 650px;">
				<div class="modal-content">	
					<div class="modal-header tenantInfo" style="margin: 0px 20px; padding: 10px 0px;">	
						新建角色	  
				        <img src="<%=request.getContextPath()%>/images/close.png" class="img-close" data-dismiss="modal">
				    </div>
				    <div id="load_div"  style="padding: 0 20px;">
				    </div>
					
				</div>
			</div>
		</div>
		
		<!-- 发送提示 -->
		<div class="modal fade sendMsgState in " id="successPrompt"  >
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
		<div class="modal fade tenant-dialog in " id="checkCode" data-backdrop="static" style="">
			<div class="modal-dialog" style="width: 360px;">
				<div class="modal-content">	
					<div class="modal-header tenantInfo">	
						<label>角色名称：</label>	  
						<input type="text" class="roleNameText"/>
				    </div>
					<div class="modal-body">
						<div class="row">
							<div class="allWrap">
								<div class="btn-operate">
									<div class="checkCodeTitle">请输入图中文字</div>
									<div class="checkCodeTextWrap clearfix">
										<input type="text" name="name" id="name" maxlength="25" value="" class="form-control checkCodeText">	
										<span class="checkCoedImg"></span>
									</div>
								</div>
								<div class="errorTips">
									图文验证码错误
								</div>
								<div class="btn-operate">
									<button class="btn btn-save in-btn263" type="button" >确定</button>
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
							<p class="p1"><img src="<%=request.getContextPath()%>/images/del-tip.png">确认删除角色“<span id="showName"></span>”吗?</p>
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
		
			<!-- 删除成功失败对话框 -->
	<div class="modal fade dialog-sm in " id="successPrompt2">
		<div class="modal-dialog" style="width: 310px;">
			<div class="modal-content">
				<div class="modal-body ">
					<div class="dialog-sm-header">				  
				        <span class=" ">提示</span>
				        <img src="<%=request.getContextPath()%>/images/close-sm.png" class="img-close" data-dismiss="modal">
				    </div>
				
					<p>
						<i class="icon-success"></i>
					</p>
					<p class="tipP">
						<label id="promptMsg">操作成功</label>
					</p>
					
					<div class="btn-operate">
						<button class="btn btn-cancel  " type="button" data-dismiss="modal">取消</button>
						<div  class="btn-division"></div>
						<button class="btn btn-save" type="button" data-dismiss="modal"  >确认</button>
					</div>
				</div>
			</div>
		</div>
	</div>
		
		<script type="text/javascript">
		$(function(){
			// 点击新建角色
			$(".btn-first").click(function(){
				$("#roleLimite").find(".modal-content").html("<div style='min-height: 100px;text-align: center; font-size: 24px; vertical-align: middle;margin-top: 35px;'>数据加载中..... </div>");
				$("#roleLimite").hide();
				
				$("#employees_limite").modal("show");
 				$("#employees_limite").find("#load_div").css("min-height","100px");
				$("#employees_limite").find("#load_div").load( global_Path+"/t_role/roleList4HeadOffice/edit?id=&isModify=true&callBackMethod=afterCreateRole&t="+(new Date().getTime()) , function(){
					$(".icon-arr").css("display","none");
				}); 
				
			});
			
			// 角色列表点击，弹出详细
			$("#roleList li").each(function(i,ele){
				// 角色列表点击，弹出详细
				$(ele).find(".roleName").click(function(){
					$(ele).addClass("active").siblings().removeClass("active");
					$("#roleLimite").show().find("input").prop("disabled",true);
					//load
					$("#roleLimite").find(".modal-content").html("<div style='min-height: 100px;text-align: center; font-size: 24px; vertical-align: middle;margin-top: 35px;'>数据加载中..... </div>");
					$("#roleLimite").find(".modal-content").load( global_Path+"/t_role/roleList4HeadOffice/edit?id="+$(ele).attr("data-id")+"&isModify=false&callBackMethod=afterUpdateRole&t="+(new Date().getTime()),function(){
						
						$(".icon-arr").css({
							top : $(ele).position().top + 8 + "px"
						});
					});
					
				});
				$(ele).find(".delete").unbind("click");
				$(ele).find(".delete").click(function(){
					$("#deleteComfirm").modal("show");
					var name=$(ele).find("span.roleName").html();
					$("#showName").text(name).attr("data-id", $(ele).attr("data-id"));
					
					//绑定删除 确认框中得 确定按钮事件
					$("#deleteComfirm").find("#deleteComfirm-type-save").unbind("click").click(function(){
						$("#deleteComfirm").modal("hide");  
						 $.post( global_Path+ "/t_role/delete", { id: $(ele).attr("data-id") },function(data){
							 if(data.isSuccess){
								$("#successPrompt2").find("#promptMsg").html("保存成功");
								$("#successPrompt2").modal("show");
								window.setTimeout(function(){
									$("#successPrompt2").modal("hide");
									window.location.href=window.location.href;
								}, 1000);
							}else{
									$("#successPrompt2").find("#promptMsg").html(""+data.message);
									$("#successPrompt2").modal("show");
									/* window.setTimeout(function(){
										$("#successPrompt2").modal("hide");
									}, 1000); */
							}
						 }, 'json'); 
					});
				});
				
			});
			
			var arrOld = [];
			// 编辑
			$("#toModify").click(function(){
				var old = $("#limiteForm").find(":checked");
				old.each(function(){
					arrOld.push($(this).attr("id"));
				});
				$(this).hide();
				$("#modify-opera").show();
				$("#roleLimite").find("input").prop("disabled",false);
			});
			// 取消
			$("#modifyCancel").click(function(){
				$("#roleLimite").find("input").prop("checked",false);
				for (var i = 0; i < arrOld.length; i++) {
					$("#"+arrOld[i]).prop("checked",true);
				}
				$(this).parent().hide();
				$("#toModify").show();
				$("#roleLimite").find("input").prop("disabled",true);
			});
		});
		
		
		
		function afterUpdateRole(isRefresh){
			$("#roleLimite").hide();
			if(isRefresh){ //只有点击确认保存了 权限，才会要求强制刷新
				window.location.href=window.location.href;
			}
		}
		
		function afterCreateRole(isRefresh){
			$("#employees_limite").modal("hide");
			if( isRefresh){
				window.location.href=window.location.href;
			}
		}
	</script>
	
	
</body>
</html>
