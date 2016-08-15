<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>Examples</title>
	<meta name="description" content="">
	<meta name="keywords" content="">
	<link href="../tools/bootstrap/css/bootstrap.css" rel="stylesheet">
	<link rel="stylesheet" href="../css/index.css">

	<link rel="stylesheet" href="../css/fishpot.css">
	<link rel="stylesheet" href="../css/common.css">
	<link rel="stylesheet" href="../tools/font-awesome/css/font-awesome.css">
	<link rel="stylesheet" href="../css/jquery.mCustomScrollbar.css" />
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/switchery/style.css"/>
	<link rel="stylesheet" href="../css/dishes.css">
	<script src="../scripts/jquery.js"></script>
	<script src="../tools/bootstrap/js/bootstrap.min.js"></script>
	<script src="../scripts/projectJs/dishes.js?v=2"></script>
	<script src="../scripts/projectJs/combodish.js?v=1"></script>
	<script src="../scripts/projectJs/fishPot.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/json2.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/ajaxfileupload.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/commons.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/jquery.validate.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/messages_zh.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/jquery.twbsPagination.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/autoComplete.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/jquery.mCustomScrollbar.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/jquery.mousewheel.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/global.js"></script>

</head>
<body>
<!-- from中必须删除	type="button"才能用valicate -->
	<div class="ky-container ">
		<div class="ky-content content-iframe">
			<div class="dishes-content">
				<p class="dishes-content-title">
					<button class="btn dishes-type-add red-btn"  type="button" id="dishes-menu-ctrl">菜谱
						<img class="icon_img" src="<%=request.getContextPath()%>/images/menu_icon.png" />
					</button>
					<button class="btn btn-default dishes-type-add"  type="button" id="dishes-type-add"><i class="icon-plus"></i> 菜品分类</button>
				</p>


				<c:if test="${navDishesLength>10}">
				<div class="nav-dishes-prev"><i class="icon-chevron-left"></i></div>

				</c:if>
				<c:if test="${navDishesLength<=10}">
				<div class="nav-dishes-prev" style="display:none;"><i class="icon-chevron-left"></i></div>
				</c:if>
				<ul class="nav-dishes" id="nav-dishes">
					<c:forEach var="item" items="${listType}" varStatus="i">
					<c:if test="${i.index==0}">

					<li class="nav-dishes-type active " value="${item.itemdesc}" draggable="true" ondragstart="drag(event)"
					ondrop="drop(event)" ondragover="allowDrop(event)" id="${item.id}" ondblclick="editTableTypeShow(this)"
					onmouseover="delDisplay(this)" onmouseout="delHidden(this)" onclick="oneclickDishType(this.id,this)" maxlength="0">
					<span>${item.itemdesc}</span><span>(${item.countDish})</span>
					<i class="icon-remove hidden"  onclick="delDishType('${item.id}','${item.itemdesc}',event)"></i>
					</li>
					</c:if>
					<c:if test="${i.index!=0}">
						<li class="nav-dishes-type" value="${item.itemdesc}"  draggable="true" ondragstart="drag(event)"
						  ondrop="drop(event)" ondragover="allowDrop(event)" id="${item.id}" ondblclick="editTableTypeShow(this)"
						  onmouseover="delDisplay(this)" onmouseout="delHidden(this)" onclick="oneclickDishType(this.id,this)">
						  <span>${item.itemdesc}</span><span>(${item.countDish})</span>
						  <i class="icon-remove hidden"  onclick="delDishType('${item.id}','${item.itemdesc}',event)"></i>
						</li>
					</c:if>
				</c:forEach>
				</ul>

				<c:if test="${navDishesLength>10}">
				<div class="nav-dishes-next"><i class="icon-chevron-right"></i></div>
				</c:if>
				<c:if test="${navDishesLength<=10}">
				<div class="nav-dishes-next" style="display:none;"><i class="icon-chevron-right"></i></div>
				</c:if>
				<ul class="dishes-right-tab right-tab hidden">
					<li id="dishes-right-tab1"><i class="icon-plus "></i><span>添加菜品分类</span></li>
					<li id="dishes-right-tab2" class="dishesRightTab2"><i class="icon-edit"></i><span>编辑菜品分类</span></li>
					<li id="dishes-right-tab3"><i class="icon-minus "></i><span>删除菜品分类</span></li>
				</ul>
				<div class="nav-dishes-tab"  >
				   <c:forEach var="item" items="${listdish}" varStatus="i">
					<div class="dishes-detail-box"  onmouseover="delDisplay(this)" id="${item.dishid}" data-dishtype="${item.dishtype}" onmouseout="delHidden(this)">
						<p class="dishes-detail-name">${item.title}
						</p>
						<i class="icon-remove hidden" onclick="delDishesDetail('${item.dishid}','${item.title}',event)"></i>
					</div>
					</c:forEach>
					<c:if test="${navDishesLength>0}">
					</c:if>
					<span id="before-dishes-add"></span>
					<div id="dishDetailList" class="btn-group dishes-detail-add "  role="group" aria-label="..." style="font-size: 16px;">
					  <button type="button" class="btn btn-default" id="dishes-detailMain-Add"><i class="icon-plus"></i> 菜品</button>
					  <button type="button" class="btn btn-default" id="dishes-detailOther-Add"><i class="icon-plus"></i> 其他</button>
					  	<ul class="dishes-other-right right-tab hidden">
							<li id="dishes-other-tab1" ><i class="icon-plus "></i><span>添加套餐</span></li>
							<li id="dishes-other-tab2" ><i class="icon-plus "></i><span>添加鱼锅</span></li>
							<li id="dishes-other-tab3" ><i class="icon-arrow-right"></i><span>添加已有菜品至该分类</span></li>
						</ul>

					</div>


					<!---菜谱页面入口-->
					<div class="menu-enter">
						<img src="../images/menu-enter.png">
					</div>

				</div>

			</div>
		</div>

	</div>
		<!--添加菜品分类框   （未使用valicate）-->
		<div class="modal fade dishes-add-dialog in " id="dishes-add-dialog"  data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
				        <div class="modal-title">添加分类</div>
				        <img src="../images/close.png" class="img-close"  onclick="initDishType()">
				    </div>
				    <hr class="dishes-dialog-hr">
					<div class="modal-body">
						<form action="" method="post" class="form-horizontal " name="" id="addtypeForm">
							<div class="form-group">
								<label class="col-xs-3 control-label">分类名称：</label>
								<div class="col-xs-5">
									<input type="text" class="hidden" id="dishTypeAddType">
									<input type="text"	value="" class="form-control" id="dish-type" maxlength="5" required>
									<input type="hidden" value="" class="form-control" id="dish-type-id">
									<label class="error" style="display: none;" id="emptywarntip"> 分类名称不能为空</label>
									<label class="error" style="display: none;" id="warntip"> 该分类已经存在</label>
								</div>

							</div>


							<div class="btn-operate  ">
								<button class="btn btn-cancel in-btn135" id="dishes-type-cancle" type="button" data-dismiss="modal">取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save in-btn135" id="dishes-type-save" type="button">确认</button>
							</div>
						</form>
					</div>


				</div>

			</div>
		</div>
		<!--添加菜品详情框 -->
		<div class="modal fade dishes-detailAdd-dialog in " id="dishes-detailAdd-dialog"  data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
				        <div class="modal-title">添加菜品</div>
				         <img src="<%=request.getContextPath()%>/images/close.png" class="img-close"  data-dismiss="modal">
				    </div>
				    <hr class="dishes-dialog-hr" style="margin: 10px 10px;">
					<div class="modal-body" style="padding-top: 0px;">
						<form action="" method="post" class="form-horizontal " name="" id="add-form-dishes">

							<div class="form-group">

								<label class="col-xs-2 control-label"><span class="required-span">*</span>菜品编号：</label>
								<div class="col-xs-2 ">
									<input type="text"	value="" class="form-control " id="dishno" name="dishno" disabled="disabled" >
									<input type="hidden"	value="" class="form-control" id="dishid">
									<input type="hidden" value="" class="form-control" id="unitflag">
								</div>

								<label class="col-xs-2 control-label"><span class="required-span">*</span>菜品名称：</label>
								<div class="col-xs-2">
									<input type="text"	value="" class="form-control " required="required" id="title" name="title" maxlength="12">
									<font color="red" id="title_tip" class="error"></font>
								</div>
								<label class="col-xs-2 control-label"><span class="required-span">*</span>菜品分类：</label>
								<div class="col-xs-2 input-widthl">
									<div class="input-group select-box" id="dishTypeShow">
										<input type="text" class="form-control required" aria-describedby="basic-addon1" id="selectDishTypeName" name="selectDishTypeName"  disabled=true >
										<input type="hidden" value="" id="selectDishTypeId">
										<span class="input-group-addon arrow-down" id="basic-addon1"><i class="icon-chevron-down" style="color:#000000"></i></span>

									</div>
									<font color="red" id="selectDishTypeName_tip" class="error"></font>
									<div class="select-content hidden select-multi scrollspy-example" id="dishTypeSelect"  >
						    		</div>
								</div>
							</div>
							<div class="dishes-charge-unit" id="adddishDiv">
								<div class="form-group dishChargeUnit"  >
									<label class="col-xs-2 control-label"><span class="required-span">*</span>计价单位：</label>
									<div class="col-xs-2">
									<div  id="divunit1"  onclick="showUnitHistory('unit1')">
										<input type="text"	aria-describedby="basic-addon1" class="form-control required" id="unit1" name="unit1"
											AUTOCOMPLETE="off" placeholder="只能输入汉字和英文"  onkeyup="value=value.replace(/[^\a-\z\A-\Z\u4E00-\u9FA5]/g,'')" 	>
									</div>
									  <div class="select-content hidden select-multi" id="Selectunit1">
						    		  </div>
									</div>
									<label class="col-xs-2 control-label"><span class="required-span">*</span>价格：</label>
									<div class="col-xs-2">
										<input type="text"	value="" class="form-control required" id="price1"  name="price1"
										onkeyup="this.value= this.value.match(/\d+(\.\d{0,2})?/) ? this.value.match(/\d+(\.\d{0,2})?/)[0] : ''" placeholder="最多两位小数的数字">
									</div>
									<label class="col-xs-2 control-label">会员价：</label>
									<div class="col-xs-2 input-widthl">
										<input type="text"	value="" class="form-control" id="vipprice1" name="vipprice1"
											onkeyup="this.value= this.value.match(/\d+(\.\d{0,2})?/) ? this.value.match(/\d+(\.\d{0,2})?/)[0] : ''" placeholder="最多两位小数的数字">
									</div>
									<i class="icon-remove hidden iclass" style="display:block;padding-top:8px;" id="hiddenidi" onclick="removeOtherUnit(this)"  style="display:block;padding-top:8px;" onmouseover="removeStyle11(this)" onmouseout="removeStyle22(this)"></i>
								</div>
								<hr>
								<div class="form-group">
									<div id="addUnitDiv">
										<span class="charge-unit-addBtn">
										<i class="icon-plus-sign"></i>添加一组计价单位
										</span>

									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-xs-2 control-label">菜品口味：</label>
								<div class="col-xs-2 dishes-taste">
									<div class="btn btn-default" type="button" id="dishes-taste-add"><i class="icon-plus-sign"></i></div>
								</div>
								<label class="col-xs-2 control-label">菜品标签：</label>
<!-- 								<div class="col-xs-4 dishes-checkbox"> -->
<!-- 									<label class="checkbox-inline"><input type="checkbox"	value="" >  <span>推荐</span></label> -->
<!-- 									<label class="checkbox-inline"><input type="checkbox"	value="" >	<span>特色</span></label> -->
<!-- 									<label class="checkbox-inline"><input type="checkbox"	value="" >	<span>新品</span></label> -->
<!-- 								</div> -->
								<div class="col-xs-2 dishes-taste">
								<div class="btn btn-default" type="button" id="dishes-label-add"><i class="icon-plus-sign"></i></div>
<!-- 									<button type="button" id="dishes-label-add"><i class="icon-plus"></i> 自定义</button> -->
								</div>
							</div>
						<div class="form-group">
							<label class="col-xs-2 control-label">现场称重：</label>
							<div class="col-xs-2 dishes-taste">
								<div class="switch demo3" >
									<input type="checkbox" id="weigh"/>
									<label><i data-on="是" data-off="否"></i></label>
								</div>
							</div>
							<label class="col-xs-2 control-label" style="width: 100px;">参与人气推荐：</label>
							<div class="col-xs-2">
								<div class="switch demo3" >
									<input type="checkbox" id="recommend"/>
									<label><i data-on="是" data-off="否"></i></label>
								</div>
							</div>
						</div>
						<div class="form-group">
								<label class="col-xs-2 control-label">菜品介绍：</label>
								<div class="col-xs-7 dishes-introduce">
									<textarea class="form-control" id="introduction" maxlength="100" placeholder="“腌”出来的好锅底？没错，新辣道锅底做大的特色就是“腌” —— 腌出来的浓厚醇香，腌出来的红油亮糖，腌出来的余香绵绵... ..."></textarea>
									<p class="dishes-introduce-tip">继续可以输入<span id="count" >100</span>字</p>
								</div>
								<div class="col-xs-3 dishes-upload-box">
									<img src="../images/upload-img.png" width="100%" height="180px" onclick="tempClick()" id="uploadpic">
									<input type="file" onchange="showpic()" style="position: absolute; filter: alpha(opacity = 0); opacity: 0; width: 0px;" size="1" id="main_img" name="main_img" accept="image/*" >
								</div>
							</div>
							<div class="btn-operate  " id="add-btn">
								<button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal" >取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save in-btn135 " id=""  >确认</button>
<!-- 								dishes-detail-save -->
							</div>

							<input type="hidden" value="1">
						</form>
					</div>


				</div>

			</div>
		</div>
		<!--添加菜品口味 -->
		<div class="modal fade dishes-tasteAdd-dialog in " id="dishes-tasteAdd-dialog" data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
				        <div class="modal-title">菜品口味</div>
				        <img src="<%=request.getContextPath()%>/images/close.png" class="img-close" data-dismiss="modal">
				    </div>
				    <hr class="dishes-dialog-hr">
					<div class="modal-body">
						<form action="" method="post" class="form-horizontal " name="">
						<input type="hidden" value="" id="dishes-tasteAdd-dialog-flag">
							<p class="dishes-tag-select" id="dishes-tag-select1">
<!-- 								<button class="btn btn-default" type="button" onmouseover="delDisplay(this)" onmouseout="delHidden(this)">微辣1<i class="icon-remove hidden"  id="1" onclick="removeTag(this)"></i></button> -->
<!-- 								<button class="btn btn-default" type="button"  onmouseover="delDisplay(this)" onmouseout="delHidden(this)">微辣2<i class="icon-remove hidden" id="2" onclick="removeTag(this)"></i></button> -->
<!-- 								<button class="btn btn-default" type="button" onmouseover="delDisplay(this)" onmouseout="delHidden(this)">微辣3<i class="icon-remove hidden "  onclick="removeTag(this)"></i></button> -->
<!-- 								<button class="btn btn-default" type="button" onmouseover="delDisplay(this)" onmouseout="delHidden(this)">微辣4<i class="icon-remove hidden"  onclick="removeTag(this)"></i></button> -->
							</p>
<!-- 							<hr class="dishes-dialog-hr"> -->
							<div class="dishes-tag-box">
								<div class="form-group">
									<div class="col-xs-6">
										<input type="text" class="form-control" id="tagName1" placeholder="自定义口味" name="tagName" maxlength="5" onkeyup="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')" >
									</div><div class="col-xs-2 tagAdd-btn" id="tagAdd-btn1"><button class="btn btn-default" type="button" >添加</button>
									</div>
									<div class="col-xs-4 tagAdd-tip" style="display: none;" id="warntip1">
										<i class="icon-warning-sign"></i>该菜品口味已经存在
									</div>
									<div class="col-xs-4 tagAdd-tip" style="display: none;" id="emptywarntip1">
										<i class="icon-warning-sign"></i>菜品口味不能为空
									</div>
								</div>
								<ul class="dishes-tag-table" id="dishes-tag-table1" >
									<li></li>
<!-- 									<div><button  onclick="addTag(this)" class="btn btn-default" type="button" >微辣</button></div> -->
<!-- 									<div><button  onclick="addTag(this)" class="btn btn-default" type="button">微辣</button></div>									 -->
<!-- 									<div><button  onclick="addTag(this)" class="btn btn-default" type="button">微辣</button></div>									 -->
<!-- 									<div><button  onclick="addTag(this)" class="btn btn-default" type="button">微辣</button></div>					 -->
								</ul>
							</div>
							<div class="btn-operate  " >
								<button class="btn btn-cancel in-btn135" type="button" onclick ="closeModal('dishes-tasteAdd-dialog')" >取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save in-btn135" id="dishes-taste-save" type="button" onclick ="closeModal('dishes-tasteAdd-dialog')" >确认</button>
							</div>
						</form>
					</div>


				</div>

			</div>
		</div>
		<!--添加菜品标签 -->

		<div class="modal fade dishes-labelAdd-dialog in " id="dishes-labelAdd-dialog" data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
				        <div class="modal-title">菜品标签</div>
				        <img src="<%=request.getContextPath()%>/images/close.png" class="img-close" data-dismiss="modal">
				    </div>
				    <hr class="dishes-dialog-hr">
					<div class="modal-body">
						<form action="" method="post" class="form-horizontal " name="">
						<input type="hidden" value="" id="dishes-labelAdd-dialog-flag">
							<p class="dishes-tag-select" id="dishes-tag-select2">
<!-- 								<button class="btn btn-default" type="button" onmouseover="delDisplay(this)" onmouseout="delHidden(this)">推荐<i class="icon-remove hidden"  id="1" onclick="removeTag(this)"></i></button> -->
<!-- 								<button class="btn btn-default" type="button"  onmouseover="delDisplay(this)" onmouseout="delHidden(this)">特色<i class="icon-remove hidden" id="2" onclick="removeTag(this)"></i></button> -->

							</p>
<!-- 							<hr class="dishes-dialog-hr"> -->
							<div class="dishes-tag-box">
								<div class="form-group">
									<div class="col-xs-6">
										<input type="text" class="form-control" id="tagName2" placeholder="自定义标签" maxlength="5" name="labelName" onkeyup="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')" >
									</div>
									<div class="col-xs-2 tagAdd-btn" id="tagAdd-btn2">
										<button class="btn btn-default" type="button">添加</button>
									</div>
									<div class="col-xs-4 tagAdd-tip" style="display: none;" id="warntip2">
										<i class="icon-warning-sign"></i>该菜品标签已经存在
									</div>
									<div class="col-xs-4 tagAdd-tip" style="display: none;" id="emptywarntip2">
										<i class="icon-warning-sign"></i>菜品标签不能为空
									</div>
								</div>
								<div class="dishes-tag-table" id="dishes-tag-table2" >
<!-- 									<div><button  onclick="addTag(this)" class="btn btn-default" type="button">推荐</button></div> -->
<!-- 									<div><button  onclick="addTag(this)" class="btn btn-default" type="button">特色</button></div>							 -->
<!-- 									<div><button  onclick="addTag(this)" class="btn btn-default" type="button">新品</button></div>							 -->

								</div>
							</div>



							<div class="btn-operate  rate-dishes">
								<button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal" onclick ="closeModal('dishes-labelAdd-dialog')">取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save in-btn135" id="dishes-label-save" type="button" onclick ="closeModal('dishes-labelAdd-dialog')">确认</button>
							</div>
						</form>
					</div>


				</div>

			</div>
		</div>
		<!--删除单个菜品弹出框-->
		<div class="modal fade dialog-sm dishes-detailDel-dialog in " id="dishes-detailDel-dialog"  data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">
				<div class="modal-body">
					<div class="dialog-sm-header">
				        <span>确认删除</span>
				        <img src="<%=request.getContextPath()%>/images/close-sm.png" class="img-close" data-dismiss="modal">
				    </div>
<!-- 				    <hr class="dishes-dialog-hr"> -->

						<form action="" method="post" class="form-horizontal " name="">
							<!-- 仅存在一个分类中-->
							<div class="dialog-sm-info">
							<p class="p1"><img src="<%=request.getContextPath()%>/images/del-tip.png">确认删除“<span id="showDishName"></span>”吗?
							<input id="showDishId" value="" type="hidden">
							</p>
							</div>
							<div class="btn-operate  ">
								<button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal">取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save in-btn135" id="dishes-type-save" type="button" onclick="confirmDelDish(1)">确认</button>
							</div>

						</form>

					</div>
				</div>
			</div>
		</div>
		<!-- 删除的多个菜品的弹出框 -->
		<div class="modal fade dishes-detailDel-dialog in " id="mutdishes-detailDel-dialog"  data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
				        <div class="modal-title">确认删除</div>
				        <img src="<%=request.getContextPath()%>/images/close.png" class="img-close" data-dismiss="modal">
				    </div>
				    <hr class="dishes-dialog-hr">
					<div class="modal-body">
						<form action="" method="post" class="form-horizontal " name="">
							<!-- 仅存在一个分类中-->
							<input id="showmulDishId" value="" type="hidden">
							<p class="dishes-del-p1" >
							<input type="radio" id="selectall" name="del" value="1" />彻底删除“<span></span>”
							</p>
							<p class="dishes-del-p1" >
							<input type="radio" id="onlyone" name="del" value="2" checked="checked" />在“<span></span>”分类中删除“<span></span>”
							</p>
							<div class="btn-operate  ">
								<button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal">取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save in-btn135" id="dishes-type-save" type="button" onclick="confirmDelDish(2)">确认</button>
							</div>

						</form>
					</div>
				</div>
			</div>
		</div>
		<div class="modal fade dialog-sm dishes-detailDel-dialog in " id="dishtype-detailDel-dialog"  data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">
				<div class="modal-body">
					<div class="dialog-sm-header">
				        <div class="modal-title">确认删除</div>
				        <img src="<%=request.getContextPath()%>/images/close.png" class="img-close" data-dismiss="modal">
				    </div>


						<form action="" method="post" class="form-horizontal " name="">
							<!-- 仅存在一个分类中-->
							<div class="dialog-sm-info">
							<p class="p1"><img src="<%=request.getContextPath()%>/images/del-tip.png">确认删除“<span id="showDishTypeName"></span>”吗?
							<input id="showDishTypeId" value="" type="hidden" />
							</p>
							</div>
							<div class="btn-operate  ">
								<button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal">取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save in-btn135" id="dishes-type-save" type="button" onclick="confirmDelDishType()">确认</button>
							</div>

						</form>
					</div>
				</div>
			</div>
		</div>
		<!--删除菜品口味弹出框  -->
		<div class="modal fade dialog-sm dishTasteType-detailDel-dialog in " id="dishTasteType-detailDel-dialog"  data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">
				<div class="modal-body">
					<div class="dialog-sm-header">
				        <div class="modal-title">确认删除</div>
				        <img src="<%=request.getContextPath()%>/images/close.png" class="img-close" data-dismiss="modal">
				    </div>


						<form action="" method="post" class="form-horizontal " name="">
							<!-- 仅存在一个分类中-->
							<div class="dialog-sm-info">
							<p class="p1"><img src="<%=request.getContextPath()%>/images/del-tip.png">确认删除“<span id="showDishTasteName"></span>”吗?
							<input id="showDishTasteId" value="" type="hidden" />
							</p>
							</div>
							<div class="btn-operate  ">
								<button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal" onclick ="closeModal('dishTasteType-detailDel-dialog')">取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save in-btn135" id="dishes-type-save" type="button" onclick="confirmDelDishTasteAndLabel()">确认</button>
							</div>

						</form>
					</div>
				</div>
			</div>
		</div>
		<!--添加已有菜品至该分类-->
		<div class="modal fade dishes-existAdd-dialog" data-backdrop="static" id="dish-select-dialog">
		</div>

		<!--信息查看-->
		<div class="modal fade menuDetail-view-dialog in " id="menuDetail-view-dialog"  data-backdrop="static" >
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
				        <div class="modal-title">菜品信息</div>
				        <img src="../images/close.png" class="img-close"  data-dismiss="modal">
				    </div>
					<div class="modal-body">
						<div class="row">
							<div class="col-xs-9 div-left">
							<input type="hidden"	value="" class="form-control" id="menuViewDishid">
<!-- 								<input type="hidden" value="" class="form-control" id="menuViewUnitflag"> -->
								<input type="hidden" value="" class="form-control" id="menuidInDish">
								<p><span class="sp1">菜品编号:</span><span class="sp2" id="menuViewDishno"></span></p>

								<p><span class="sp1">菜品名称:</span><span class="sp2" id="menuViewTitle"></span>
									</p>
								<p>
									<span class="sp1">库存状态:</span><span class="sp2" id="menuViewStatus"></span>
								</p>
									<!--另一种状态的图片 menu-state-off.png-->
								<p><span class="sp1">菜品分类:</span><span class="sp2" id="menuViewDishType"></span></p>
								<p><span class="sp1">单位:</span><span class="sp2" id="menuViewUnit"></span></p>
								<p><span class="sp1">价格:</span><span class="sp2" id="menuViewPrice"></span>
									<span class="sp1">会员价:</span><span class="sp2" id="menuViewVipPrice"></span></p>

								<p><span class="sp1">其他:</span><span class="sp2" id="tasteAndLabel"></span></p>
							</div>
							<div class="col-xs-3 div-right">
								<img id="div-right-img" src="">
							</div>
						</div>
						<div class="row">
							<p ><span class="sp1">菜品介绍:</span><span class="sp2-intro" id="menuViewIntroduction"></p>
						</div>



					</div>


				</div>

			</div>
		</div>
		<!--删除单个菜品弹出框-->
		<div class="modal fade dialog-sm  in " id="notNullDialog"  data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-body">
					    <div>
							<form action="" method="post" class="form-horizontal " name="">
								<!-- 仅存在一个分类中-->
								<div class="dialog-sm-info">
								<p class="p1"><img src="<%=request.getContextPath()%>/images/del-tip.png"><span id="showTypeName"></span>
								<input id="showDishId" value="" type="hidden">
								</p>
								</div>
							</form>

						</div>
					</div>
				</div>
			</div>
		</div>
	<!--菜品被菜谱选中不能删除弹出框  -->
		<div class="modal fade dialog-sm cannotDeleteDialog in " id="cannotDeleteDialog"  data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">
				<div class="modal-body">
					<div class="dialog-sm-header">
				        <div class="modal-title">提示</div>
				        <img src="<%=request.getContextPath()%>/images/close.png" class="img-close" data-dismiss="modal">
				    </div>


						<form action="" method="post" class="form-horizontal " name="">
							<div class="dish_cannotDiv"><img src="<%=request.getContextPath()%>/images/dish_cannot.png" ></div>
							<!-- 仅存在一个分类中-->
							<div class="dialog-sm-info">
							<p class="p1"><span id="showCannotDeleteName"></span>
							<input id="" value="" type="hidden" />
							</p>
							</div>
							<div class="btn-operate  ">
								<button class="btn btn-save in-btn135" type="button" data-dismiss="modal" onclick ="closeModal('cannotDeleteDialog')">确认</button>
<!-- 								<div  class="btn-division"></div> -->
<!-- 								<button class="btn btn-save in-btn135" id="dishes-type-save" type="button" onclick="confirmDelDishTasteAndLabel()">确认</button> -->
							</div>

						</form>
					</div>
				</div>
			</div>
		</div>
		<%@ include file="./comboDish.jsp"%>
		<%@ include file="./fishandhotdiv.jsp"%>

		<script>
		var flag_combo=0;
		$('.dishes-roll-left').click(function(){

			if(flag_combo>=1){
				var marginl = -147.5*(--flag_combo)+'px';
				$('.dishes-comboList:eq(0)').css('margin-left',marginl);

			}
// 			if(flag_combo==0){
// 				$('.dishes-roll-left').css({"color":"white"});;
// 			}



		});
		$('.dishes-roll-right').click(function(){
			var count = $("#selectDishShow").find(".dishes-comboList").length;

			if(flag_combo<count-5){
			var marginl = -147.5*(++flag_combo)+'px';
			$('.dishes-comboList:eq(0)').css('margin-left',marginl);
			}
			console.log("inner"+flag_combo);
		});


		</script>
</body>
</html>
