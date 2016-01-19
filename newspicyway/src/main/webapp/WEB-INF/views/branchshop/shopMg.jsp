<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>门店管理</title>
	<meta name="description" content="">
	<meta name="keywords" content="">
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery.json-2.3.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/commons.js"></script>
	<script type="text/javascript">
		var global_Path = '<%=request.getContextPath()%>';
	</script>

	<link href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common/city.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/shop.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/dishes.css">
	
	
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/font-awesome/css/font-awesome.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/json2.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/cityinit.js"></script>


	<script src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/city_arr.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/city_func.js"></script>
	<script src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/index.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/jquery.validate.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/messages_zh.js"></script>
	
	<style type="text/css">
		.addDelicon{
			min-height:0;
			padding:0;
			border-bottom:none;
		}
	</style>
</head>
<body> 
		<div class="ky-content content-iframe">
			<div class="shop-btn btn-add">
				<div class="btn-group" role="group" aria-label="...">
				  <button type="button" class="btn  btn-first">新增门店</button>
				</div>
			</div>
			<form id="query" action="<%=request.getContextPath()%>/shopMg/page" method="post" >
			<input type="hidden" name="current" id="current" value="${current}">
			<input type="hidden" name="pagesize" id="pagesize" value="18">
			<input type="hidden"  id="total" value="${total}">
			<div class="shop-search">
				<div class="form-group">
					<label class="control-label col-xs-2">门店名称：</label>
					<div class="col-xs-3">
						<input type="text" id="searchBranchName"  class="form-control"  name="branchname" value="${branchname}"/>
					</div>
					<label class="control-label col-xs-1">地址：</label>
					<div class="col-xs-3">
						 
							  <input type="text" id="name" name="address"  style="cursor:default;"  readonly value="${address}" class="form-control cityOne" flag="all" placeholder="省 市 县" >

						<jsp:include page="../../../common/citySelect.jsp" />				
					</div>		
					
					<div class="col-xs-3 shop-search-btn ">
						<button class="btn btn-default" type="submit" onclick="doQuery()"><i class="icon-search"></i>    搜索</button>						
					</div>		
				</div>
				
			</div>
			</form>


			<table class="table table-list shop-store-table">
				<thead>
					<tr>
						<th>序号</th>
						<th>门店ID</th>
						<th>名称</th>
						<th>地址</th>
						<th>店长</th>
						<th>电话</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody style="font-size: 12px;">
				<c:forEach var="item" items="${datas}" varStatus="i">
					<tr  <c:if test="${i.index%2==0}"> class="odd"</c:if>   onmouseover="showOpr(this)"  onmouseout="hideOpr(this)">
						<td>${(current-1)*pagesize+(i.index+1)}</td>
						<td class="name">${item.branchid }</td>
						<td class="name">${item.branchname }</td>
						<td class="address">${item.province} ${item.city} ${item.region} ${item.addressdetail }</td>
						<td class="manager">${item.manager }</td>
						<td class="name">${item.cellphone}/${item.telphone }</td>
						<td class="td-last">
							<div class="operate">
								<a href="javascript:void(0)" onclick="doEdit('${item.branchid}','detail')">查看</a>
								<a href="javascript:void(0)" onclick="doEdit('${item.branchid}')">修改</a>
								<a href="javascript:void(0)" onclick="showCanDel('${item.branchid}','${item.branchname}')">删除</a>
							</div>
						</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<%@ include file="/common/page.jsp" %>
		</div>

	 
		<!--点击按钮弹出添加界面 -->
		<div class="modal fade shop-dialog in " id="shop-add" data-backdrop="static" >
			<div class="modal-dialog">
				<div class="modal-content">	
					<div class="modal-header addDelicon">				  
				        <img src="<%=request.getContextPath()%>/images/close.png" class="img-close" data-dismiss="modal">
				    </div>
				    
					<div class="modal-body">
						<div class="row mt20">
							<div class="col-xs-12">
								<form action="" method="post"  class="form-horizontal " id="add-form" >
								<div class="form-group">
										<label class="col-xs-3 control-label ">门店ID：</label>
										<div class="col-xs-8">
											<input type="text" name="branchid" id="branchid"   placeholder="自动生成" value=""	 class="form-control" disabled="disabled"   />	
										</div>
									
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label "><span class="required-span">*</span>门店名称：</label>
										<div class="col-xs-8">
										
											<input type="text" name="branchname" id="branchname" maxlength="25" placeholder="* 为必填项"	value="${branchname}" class="form-control required"/>
											<label style="color:red;display:none;"  id="sameCityPrompt" >同一区/县存在相同店名</label>	
										</div>
									
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label "><span class="required-span">*</span>地址：</label>
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
											  <input type="hidden" name="province"  />
													<input type="hidden" name="city"  />
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
											<input type="text" name="addressdetail" id="addressdetail" maxlength="50"	value="" class="form-control required" placeholder="详细地址">
											<font color="red" id="address_tip"></font> 
										</div>					
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label "><span class="required-span">*</span>店长：</label>
										<div class="col-xs-8">
											<input type="text" name="manager" id="manager" maxlength="25" placeholder="最多25个字符"	value="" class="form-control required">	
										</div>
									
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label "><span class="required-span">*</span>手机：</label>
										<div class="col-xs-8">
											<input type="text" name="cellphone" id="cellphone" maxlength="11" placeholder="请输入11位手机号"	value="" class="form-control required mobile">	
										</div>
									
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label "><span class="required-span">*</span>固定电话：</label>
										<div class="col-xs-8">
											<input type="text" name="telphone"  id="telphone" maxlength="15" placeholder="最多为15位数字或 + 、-号"	value="" class="form-control required phone">	
										</div>
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label "><span class="required-span"></span>传真：</label>
										<div class="col-xs-8">
											<input type="text" name="taxphone" id="taxphone" maxlength="15" placeholder="最多为15位数字或 + 、-号"	value="" class="form-control phone ">	
										</div>
									
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label ">联系人一：</label>
										<div class="col-xs-8">
											<input type="text"	name ="contact1" id ="contact1" placeholder="最多25个字符" maxlength="25"  value="" class="form-control">	
										</div>
									
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label ">手机：</label>
										<div class="col-xs-8">
											<input type="text" name="contact1_tel" id="contact1_tel" placeholder="请输入11位手机号"	 maxlength="11" value="" class="form-control mobile">	
										</div>
									
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label ">联系人二：</label>
										<div class="col-xs-8">
											<input type="text" name="contact2" id="contact2" placeholder="最多25个字符"  maxlength="25"	value="" class="form-control">	
										</div>
									
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label ">手机：</label>
										<div class="col-xs-8">
											<input type="text" name="contact2_tel" id="contact2_tel" placeholder="请输入11位手机号"	 maxlength="11"	value="" class="form-control   mobile">	
										</div>
									</div>
									<div class="btn-operate" id="add-btn">
										<button class="btn btn-cancel in-btn135" type="button"  data-dismiss="modal" >取消</button>
										<div  class="btn-division"></div>
										<button class="btn btn-save in-btn135 shop-btn-save" type="submit" id="btnsave"  > 保存</button>
									</div>
									
									<div class="btn-operate" id="edit-btn">
										<button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal" >关闭</button>
										<div  class="btn-division"></div>
										<button class="btn btn-save in-btn135 shop-btn-save" type="button" id="btnedit" onclick="detail2edit()" > 编辑</button>
									</div>




								</form>
							</div>
						</div>						
					</div>

				</div>
				
			</div>
		</div><!--   add dish div end -->
		
		<!-- 确认删除框 -->
		<div class="modal fade dialog-sm   in " id="candel"  data-backdrop="static">
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
		
		
		
		<!-- 确认取消框 -->
		<div class="modal fade dishes-detailDel-dialog in " id="modal_confirmCancel"  data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">	
					<div class="modal-header">				  
				        <h4 class="modal-title">确认取消</h4>
<!-- 				        <img src="<%=request.getContextPath()%>/images/close.png" class="img-close" data-dismiss="modal"> -->
				    </div>
				    <hr class="dishes-dialog-hr">
					<div class="modal-body">
						<form action="" method="post" class="form-horizontal " name="">
							<!-- 仅存在一个分类中-->
							<p class="dishes-del-p1"><img src="<%=request.getContextPath()%>/images/del-warn.png"></i>确认取消当前编辑?</p>
							<div class="btn-operate btn-operate-dishes">
								<button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal"   >取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save in-btn135" id="dishes-type-save" type="button" data-dismiss="modal"  onclick="confirmCancel()">确认</button>
							</div>
						</form>
					</div>
					

				</div>
				
			</div>
		</div><!-- end delete  -->
		
		
		
		
	</div>
		
		
		
		<!-- 成功提示successPrompt -->
<!-- 		 <div class="modal fade dishes-detailDel-dialog in " id="successPrompt"  > -->
<!-- 			<div class="modal-dialog" style="width:310px;"> -->
<!-- 				<div class="modal-content" style="width:310px;">	 -->
<!-- 					<div class="modal-body pop-div-content" style="height:210px;"> -->
<!-- 						<br/> -->
<!-- 							<p class=" "> <i class="icon-ok"></i><label id="promptMsg">保存成功</label></p> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 		</div> -->
		
		
		<!-- 成功提示successPrompt -->
<!-- 		 <div class="modal fade dishes-detailDel-dialog in " id="failPrompt"  > -->
<!-- 			<div class="modal-dialog" style="width:310px;"> -->
<!-- 				<div class="modal-content" style="width:310px;">	 -->
<!-- 					<div class="modal-body pop-div-content" style="height:210px;"> -->
<!-- 						<br/> -->
<!-- 							<p class=" "> <i class="icon-ok"></i>保存失败</p> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 		</div> -->
		
		
		<div class="confirm-box  hidden">
				<div class="confirm-box-content">
					<p><img src="<%=request.getContextPath()%>/images/confirm-ok.png"/></p>
					<!--保存失败   confirm-fail.png-->
					<p id="promptMsg">保存成功</p>
				</div>
			</div>
</body>

	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/projectJs/shopMg.js"></script>
<script type="text/javascript">

/// 初始化货代地址省
initProvince();

</script>
</html>
 