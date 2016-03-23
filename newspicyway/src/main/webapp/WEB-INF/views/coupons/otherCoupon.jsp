<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>其它优惠</title>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/preferential.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/font-awesome/css/font-awesome.css">
	<%@ include file="/common/resource.jsp" %>  
	<script src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
	<script src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/index.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/jquery.twbsPagination.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/autoComplete.js"></script>
	<script src="<%=request.getContextPath()%>/tools/calendar_diy/WdatePicker.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/jquery.validate.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/validate-plus.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/otherCoupon.js"></script>

	<style type="text/css">
		.autoComplete {
			border-top : "1px solid #dfdfdf";
			left:15px !important;
			top: 30px !important;
		}
		.auto_item {
			width:185px;
		}
	</style>
	<style type="text/css">
		.dishes-type-add:hover {
			color:rgb(0, 211, 172) !important;
			background-color: #fff !important;
			border-color: #cccccc !important;
		}
		.btn-save {
		    background: none repeat scroll 0 0 #8cc253;
		}
		.iconwrap {
			position:relative;
			margin-right:10px;
			margin-top:10px;
		}
		.icon-remove {
			background-image: none;
		    height: 14px;
		    position: absolute;
		    right: 2px;
		    text-align: center;
		    top: 6px;
		    width: 14px;
		    color: #999;
		    
		}
		.icon-remove:hover {
			background-color:#000;
		    text-decoration: none;
		    color:#fff;
		    border-radius:50%;
		}
		.modal-header {
		     border-bottom: 1px solid rgb(229,229,229); 
		     padding:15px; 
		     margin-top:0px; 
		}
		.searchIcon {
			background-color: #fff;
		    border-bottom-left-radius: 15px;
		    border-right: medium none;
		    border-top-left-radius: 15px;
		}
		#innerfree_searcher {
			border-bottom-right-radius: 15px;
		    border-left: medium none;
		    border-top-right-radius: 15px;
		    box-shadow: none;
		}
		.btn-add .dishes-type-add {
			border-radius: 0;
   			color: #c8c8c8;
   			height: 32px;
    		width: 120px;
    		
    		
		}
		.falseArrow {
			border-left: 1px solid #dfdfdf;
		    height: 30px;
		    position: absolute;
		    right: 16px;
		    top: 0;
		    width: 37px;
	    }
	    #addReason {
	    	background-color:#fff;
	    	color:#c8c8c8;
	    }
	    .table-list th{
	   	 	color:#282828;
	    }
	    .control-label {
	    	font-weight: normal;
		    padding-top: 4px;
		    text-align: right;
		    font-size:14px;
	    }
	    #innerfree-add-dialog .form-group{
	    	margin-bottom:25px;
	    }
	    #reasonContent {
	    	line-height:40px;
	    }
	    .reasonLabel {
	    	float: left;
		    min-width: 80px;
		    width: 8%;
	    }
	    .reasonList {
	     	float: left;
    		width: 90%;
	    }
	    h4 {
	    	font-size:16px;
	    	font-family:Microsoft YaHei;
	    }
	    
	    .preferential-btn{
	    	min-height: 20px;
	    }
	</style>
</head>

<body>
		<div class="ky-content preferential-content">
			<p class="preferential-class">
				<img src="../images/preferential-type6.png"/>
				<span class="preferential-name otherCoupon-name">更多优惠 </span>
				<span class="preferential-desc">为顾客提供更为灵活的优惠方式（例：赠送菜品、灵活优免等）</span>
			<p>
			<ul class="nav-preferential other-preferential " id="nav-preferential-other">
				<li class="active" id="other-preferentail-type1">
					<span class="top"></span>
					<em></em>
					<a >手工优免</a>
					<span class="bottom"></span>
				</li>
				<li id="other-preferential-type2">
					<span class="top"></span>
					<em></em>
					<a>内部优免</a>
					<span class="bottom"></span>

				</li>	
			</ul>	
			<!--手工优免-->
			<div id="other-preferential-content1">
				<div  class="manual-free-box">
					<p id="reasonContent">
						<span class="reasonLabel">优免原因：</span>
						<span class="reasonList">
							<input type="hidden" id="handfree_preferential" value="${preferential}"/>
<%-- 							<c:if test="'<%=Constant.ISBRANCH%>'==N"> --%>
							<button id="addReason" class="btn btn-default dishes-type-add" onclick="showAddReason()">
								<i class="icon-plus"></i>
								添加原因
							</button>
<%-- 							</c:if> --%>
						</span>
					</p>
				</div>
				
			</div>
			<!--添加手工优免原因 -->
			<div class="modal fade  in " id="reason-add-dialog"  data-backdrop="static">
				<div class="modal-dialog">
					<div class="modal-content">	
						<div class="modal-header">				  
					        <h4 class="modal-title"><span>编辑</span>手工优免</h4>
					        <img src="../images/close.png" class="img-close"  data-dismiss="modal">
					    </div>
						<div class="modal-body">			
							<form id="addReasonForm" action=""  method="post" class="form-horizontal " name="">
								<div class="form-group">
									<label class="col-xs-3 control-label">优免原因：</label>
									<div class="col-xs-6">
										<div style="position:relative;">
											<input type="text"	value="" class="form-control" id="newReason" name="newReason" placeholder="最多5个字" maxlength="5">	
										</div>
									</div>
								</div>
	
								<div class="btn-operate ">
									<button class="btn btn-cancel in-btn135" id="reason-cancle" type="button" onclick="cancelAdd()">取消</button>
									<div  class="btn-division"></div>
									<button class="btn btn-save in-btn135" id="reason-save" type="submit">确认</button>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>

			<!-- 内部优免 -->
			<div id="other-preferential-content2" class="hidden">
				<div class="preferential-btn btn-add" >
					<div id="btn-group-innerfree-add" class="btn-group" role="group" aria-label="..." onclick="showInnerfreeReason()">
<%-- 					<c:if test="'<%=Constant.ISBRANCH%>'==N"> --%>
					  <button type="button" class="btn btn-default dishes-type-add " >
					  
					  <i class="icon-plus"></i>
					  内部优免
					   
					  </button>
<%-- 					 </c:if> --%>
					</div>
					
					<div class="btn-group" style="float:right; margin-left:20px;margin-bottom:20px;">
						<div class="input-group">
						  <span class="input-group-addon searchIcon" id="basic-addon1"><i class="icon-search" style="background:url();"></i></span>
						  <input id="innerfree_searcher" type="text" class="form-control "  placeholder="搜索" aria-describedby="basic-addon1">
						</div>
					</div>
				</div>					
				<table class="table  table-list" id="innerfree-table">
					<thead>
						<tr>
							<th style="width:10%">序号</th>
							<th style="width:30%">单位名称</th>
							<th style="width:10%">折扣额</th>
							<th style="width:10%">是否可挂账</th>
							<th style="width:30%">有效期</th>
<%-- 							<c:if test="'<%=Constant.ISBRANCH%>'==N"> --%>
							<th style="width:10%">操作</th>
<%-- 							</c:if> --%>
						</tr>
					</thead>
					<tbody>
					
					</tbody>
				</table>
				<div class="pagingWrap">
			
				</div>
			</div>
		</div>
		
		<!-- 添加内部优免弹出层 -->
		<div class="modal fade  in " id="innerfree-add-dialog"  data-backdrop="static" aria-hidden="true">
				<input id="id" type="hidden" />
				<input id="preferential"  type="hidden"/>
				<div class="modal-dialog">
					<div class="modal-content">	
						<div class="modal-header">				  
					        <h4 class="modal-title"><span>编辑</span>内部优免</h4>
					        <img src="../images/close.png" class="img-close"  data-dismiss="modal">
					    </div>
						<div class="modal-body">			
							<form action=""  method="post" class="form-horizontal " id="add-form">
								
								<div class="form-group">
									<label class="col-xs-3 control-label">单位名称：</label>
									<div class="col-xs-8">
										<div style="position:relative;">
											<input type="text"	value="" class="form-control" name="company_name" id="company_name" placeholder="最多15个字" maxlength="15">	
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 control-label">折扣额：</label>
									<div class="col-xs-8 unit-style input-group">
										<div style="position:relative;display: table;">
											<input type="text"	value="" class="form-control" aria-describedby="basic-addon1" id="discount" name="discount" maxlength="3" placeholder="8.5">	
											<span class="input-group-addon" id="basic-addon1">折</span>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 control-label">是否可挂账：</label>
									<div class="col-xs-8">
										<select  class="form-control" id="can_credit" name="can_credit" style="width:340px">
										  <option value="1">是</option>
										  <option value="0">否</option>
										</select>
										<span class="input-group-addon arrow-down falseArrow">
											<i class="icon-chevron-down"></i>
										</span>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-3 control-label">有效期：</label>
		
									<div class="col-xs-4">
										<div class="input-group">
											<input name="starttime" id="starttime" readOnly
												onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'endtime\')}',onpicked:picked})"
												class="form-control bottom" type="text"
												aria-describedby="basic-addon1" value=""> <span
												class="input-group-addon arrow-down" >
												<i class="icon-chevron-down"></i>
											</span>
										</div>
									</div>
									<label class="time-label col-xs-1">—</label>
									<div class="col-xs-4">
										<div class="input-group">
											<input name="endtime" id="endtime" readOnly
												onFocus="WdatePicker({minDate:'#F{$dp.$D(\'starttime\')}',onpicked:picked})"
												class="form-control bottom" type="text" class="form-control"
												aria-describedby="basic-addon1" value=""> <span
												class="input-group-addon arrow-down"><i
												class="icon-chevron-down"></i></span>
		
										</div>
									</div>
								</div>

								<div class="btn-operate " style="margin-top:45px;">
									<button class="btn btn-cancel in-btn135" id="innerfree-cancle" type="button" data-dismiss="modal">取消</button>
									<div  class="btn-division"></div>
									<button type="submit" class="btn btn-save in-btn135" id="innerfree-save"  >确认</button>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		<!-- /.modal -->
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
							<p class="p1"><img src="<%=request.getContextPath()%>/images/del-tip.png"></i>确认删除“<span id="showName"></span>”吗?</p>
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
		</div><!-- end delete  -->
			<!-- 确认删除框 -->
		<div class="modal fade dialog-sm in " id="deleteInnerComfirm"  data-backdrop="static">
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
							<p class="p1"><img src="<%=request.getContextPath()%>/images/del-tip.png"></i>确认删除“<span id="showInnerName"></span>”吗?</p>
							</div>
							<div class="btn-operate">
								<button class="btn btn-cancel  " type="button" data-dismiss="modal"   >取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save  " id="dishes-type-save" type="button" onclick="doDelInner()">确认</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div><!-- end delete  -->
	
		<script type="text/javascript">
			$(parent.document.all("detail")).height(document.body.scrollHeight); 
			var isDetail = ${isDetail};
	
			//根据当前 是分店还是总店，判断某些权限是否显示
			//分店 不能新增、编辑、删除
			function refreshBranchRole(){
				if(isbranch=='Y'){ 
					//移除手工优免
					$(".reasonList").find("div.btn-default").prop("ondblclick","");//取消双击事件
					$(".reasonList").find("div.btn-default").find("a.icon-remove").remove();
					$(".reasonList").find("#addReason").hide(); 
					$(".reasonList").find("#addReason").prop("onclick",""); 
					//移除内部优免
					$("#other-preferential-content2").find("#btn-group-innerfree-add").remove();
					$("#other-preferential-content2").find("#innerfree-table").find("tbody a.deleteBtn").remove();
					$("#other-preferential-content2").find("#innerfree-table").find("tbody a.editBtn").remove();
				}
			}
			
			 refreshBranchRole(); 
		</script>
</body>
</html>
