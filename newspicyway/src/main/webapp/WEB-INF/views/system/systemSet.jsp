<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/resource.jsp"%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/scripts/datagrid-groupview.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/scripts/json2.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/tools/calendar_diy/WdatePicker.js"></script>
<script src="<%=request.getContextPath()%>/scripts/jquery.mousewheel.js"></script>
<script src="<%=request.getContextPath()%>/scripts/globalize.js"></script>
<script
	src="<%=request.getContextPath()%>/scripts/globalize.culture.de-DE.js"></script>

<script
	src="<%=request.getContextPath()%>/scripts/jquery-ui-1.10.4.custom.js"></script>

<script
	src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
<script
	src="<%=request.getContextPath()%>/tools/jquery-validation/jquery.validate.js"></script>
<script
	src="<%=request.getContextPath()%>/tools/jquery-validation/messages_zh.js"></script>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/jquery-ui.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/tools/font-awesome/css/font-awesome.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/common.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/index.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/tools/font-awesome/css/font-awesome.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/system.css" />
</head>


<body>
	<div style="margin: 10px 10px 200px 10px;">
		<div class="setup_div">
			<div style="height: 30px;">
				<div class="system-setup-title" style="">营业时间管理</div>
				<button type="button" class="btn btn-default" id="editBussiness">编辑</button>
				<button type="button" class="btn btn-default hide"
					id="saveBussiness">保存</button>
			</div>
			<hr style="margin: 5px 0px;" />
			<div class="modal-body" style="padding-top: 0px;">
				<div class="form-horizontal " id="retrieveBuss">
					<div class="form-group">
						<input type="hidden" id="busstype" value="BIZPERIODDATE" /> <input
							type="hidden" id="bussallid" value="" /> <label
							class="col-xs-2 control-label">全天：</label>
						<div class="col-xs-1">
							<div class="show_info">
								<p id="all_begintime_info"></p>
							</div>
							<div class="edit_info hide">
								<input type="text" value="" class="form-control time"
									required="required" id="all_begintime_input" name="time" maxlength="15" onkeyup="this.value= this.value.match(/\d+(:)?(\d+)?/) ? this.value.match(/\d+(:)?(\d+)?/)[0] : '';checkTimeFormat(this)" />
							</div>
						</div>
						<label class="time-label col-xs-2">—</label>
						<div class="col-xs-2 show_info">
							<p class="center">
								<label id="all_datetype_info"></label><label
									id="all_endtime_info"></label>
							</p>
						</div>
						<div class="col-xs-2 ">
							<div class="edit_info hide">
								<select id="all_date_type" class="form-control"
									style="display: inline-block; width: 45%;"><option
										value="T">今日</option>
									<option value="N">次日</option></select> <input type="text" value=""
									class="form-control time" required="required" id="all_endtime_input" onkeyup="this.value= this.value.match(/\d+(:)?(\d+)?/) ? this.value.match(/\d+(:)?(\d+)?/)[0] : '';checkTimeFormat(this)"
									name="" maxlength="15" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<input type="hidden" id="busslunchid" value="" /> <label
							class="col-xs-2 control-label">午市：</label>
						<div class="col-xs-1">
							<div class="show_info">
								<p id="lunch_begintime_info"></p>
							</div>
							<div class="edit_info hide">
								<input type="text" value="" class="form-control time"
									required="required" id="lunch_begintime_input" name="" maxlength="15" onkeyup="this.value= this.value.match(/\d+(:)?(\d+)?/) ? this.value.match(/\d+(:)?(\d+)?/)[0] : '';checkTimeFormat(this)" />
							</div>
						</div>
						<label class="time-label col-xs-2">—</label>
						<div class="col-xs-2 show_info">
							<p>
								<label id="lunch_datetype_info"></label><label id="lunch_endtime_info"></label>
							</p>
						</div>
						<div class="col-xs-2 ">
							<div class="edit_info hide">
								<select id="lunch_date_type" class="form-control"
									style="display: inline-block; width: 45%;"><option
										value="T">今日</option>
									<option value="N">次日</option></select> <input type="text" value=""
									class="form-control time" required="required" id="lunch_endtime_input" onkeyup="this.value= this.value.match(/\d+(:)?(\d+)?/) ? this.value.match(/\d+(:)?(\d+)?/)[0] : '';checkTimeFormat(this)"
									name="" maxlength="15" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<input type="hidden" id="bussdinnerid" value="" /> <label
							class="col-xs-2 control-label">晚市：</label>
						<div class="col-xs-1">
							<div class="show_info">
								<p id="dinner_begintime_info"></p>
							</div>
							<div class="edit_info hide">
								<input type="text" value="" class="form-control time" onkeyup="this.value= this.value.match(/\d+(:)?(\d+)?/) ? this.value.match(/\d+(:)?(\d+)?/)[0] : '';checkTimeFormat(this)"
									required="required" id="dinner_begintime_input" name="" maxlength="15" />
							</div>
						</div>
						<label class="time-label col-xs-2">—</label>
						<div class="col-xs-2 show_info">
							<p>
								<label id="dinner_datetype_info"></label><label id="dinner_endtime_info"></label>
							</p>
						</div>
						<div class="col-xs-2 ">
							<div class="edit_info hide">
								<select id="dinner_date_type" class="form-control"
									style="display: inline-block; width: 45%;"><option
										value="T">今日</option>
									<option value="N">次日</option></select> <input type="text" value="" onkeyup="this.value= this.value.match(/\d+(:)?(\d+)?/) ? this.value.match(/\d+(:)?(\d+)?/)[0] : '';checkTimeFormat(this)"
									class="form-control time" required="required" id="dinner_endtime_input"
									name="" maxlength="15" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="setup_div">
			<div style="height: 30px;">
				<div class="system-setup-title" style="">服务密码</div>
				<button type="button" id="editPadPwd" class="btn btn-default">编辑</button>
				<button type="button" id="savePadPwd" class="btn btn-default hide">保存</button>
			</div>
			<hr style="margin: 5px 0px;" />
			<div class="modal-body" style="padding-top: 0px;">
				<div class="form-horizontal " id="retrievePwd">
					<div class="form-group">
						<input type="hidden" id="passtype" value="PASSWORD" /> <input
							type="hidden" id="passid" value="" /> <label
							class="col-xs-2 control-label">PAD服务密码：</label>
						<div class="col-xs-2">
							<div class="show_info password">
								<p class="left"></p>
							</div>
							<div class="edit_info hide">
								<input type="password" value="" class="form-control"
									required="required" id="service_pass" name="" maxlength="15" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="setup_div">
			<form action="" method="post" class="form-horizontal " name="" id="tableware_form">
			<div style="height: 30px;">
				<div class="system-setup-title">餐具设置</div>
				<button type="button" id="editDishes" class="btn btn-default">编辑</button>
				<button type="submit" id="saveDishes" class="btn btn-default hide">保存</button>
			</div>
			<hr style="margin: 5px 0px;" />
			<div class="modal-body" style="padding-top: 0px;">
				<div class="form-horizontal " id="retrieveDish">
					<div class="form-group">
						<input type="hidden" id="dishtype" value="DISHES" /> <input
							type="hidden" id="dishid" value="" /> <label
							class="col-xs-2 control-label">是否收费：</label>
						<div class="col-xs-2">
							<div class="show_info isFree">
								<p class="left"></p>
							</div>
							<div class="edit_info hide">
								<label class="radio-inline"> <input type="radio"
									name="isFree" value="0" checked="checked" />免费
								</label> <label class="radio-inline"><input type="radio"
									name="isFree" value="1" />收费 </label>
							</div>
						</div>
					</div>
					<div class="form-group price_pro">
						<label class="col-xs-2 control-label">价格：</label>
						<div class="col-xs-2">
							<div class="show_info price">
								<p class="left"></p>
							</div>
							<div class="edit_info hide">
								<input type="text" value="" class="form-control"
									required="required" id="price" name="price" maxlength="10"
									onkeyup="this.value= this.value.match(/\d+(\.\d{0,2})?/) ? this.value.match(/\d+(\.\d{0,2})?/)[0] : '';checkValue(this)" />
							</div>
						</div>
					</div>
					<div class="form-group vipprice_pro">
						<label class="col-xs-2 control-label">会员价：</label>
						<div class="col-xs-2">
							<div class="show_info vipprice">
								<p class="left"></p>
							</div>
							<div class="edit_info hide">
								<input type="text" value="" class="form-control" id="vipprice" name="vipprice"
									maxlength="10" onkeyup="this.value= this.value.match(/\d+(\.\d{0,2})?/) ? this.value.match(/\d+(\.\d{0,2})?/)[0] : ''; checkValue(this);" />
							</div>
						</div>
					</div>
				</div>
			</div>
			</form>
		</div>

		<div class="setup_div">
			<div style="height: 30px;">
				<div class="system-setup-title" style="">忌口设置</div>
				<button type="button" id="editAvoid" class="btn btn-default">编辑</button>
				<button type="button" id="saveAvoid" class="btn btn-default hide">保存</button>
			</div>
			<hr style="margin: 5px 0px;" />
			<div class="form-horizontal " id="">
				<div class="form-group">
					<input type="hidden" id="avoidtype" value="SPECIAL" />
					<div class="avoid-tag-table col-xs-10" id="avoid-list"
						style="overflow: auto;">
						<div class="add-reason avoid-li btn btn-default hide" id="avoid-add">
							<i class="icon-plus"></i>添加忌口
						</div>
						<div class="addavoid-input-div hide" id="addavoid-input">
							<div class="col-xs-3 no-left-padding">
								<input type="text" class="form-control" id="avoid_name" name=""
									maxlength="5" required="required" onblur="avoidSave();" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="setup_div">
			<div style="height: 30px;">
				<div class="system-setup-title" style="">退菜原因设置</div>
				<button type="button" id="editReturn" class="btn btn-default">编辑</button>
				<button type="button" id="saveReturn" class="btn btn-default hide">保存</button>
			</div>
			<hr style="margin: 5px 0px;" />
			<div class="form-horizontal " id="">
				<div class="form-group">
					<input type="hidden" id="reasontype" value="RETURNDISH" />
					<div class="avoid-tag-table col-xs-10" id="reason-list"
						style="overflow: auto;">
						<div class="add-reason avoid-li btn btn-default hide" id="reason-add">
							<i class="icon-plus"></i>添加原因
						</div>
						<div class="addreason-input-div hide" id="addreason-input">
							<div class="col-xs-3 no-left-padding">
								<input type="text" class="form-control" id="reason_name" name=""
									maxlength="5" required="required" onblur="returnDishSave();" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="setup_div">
			<div style="height: 30px;">
				<div class="system-setup-title" style="">投诉原因</div>
				<button type="button" id="editComplaint" class="btn btn-default">编辑</button>
				<button type="button" id="saveComplaint" class="btn btn-default hide">保存</button>
			</div>
			<hr style="margin: 5px 0px;" />
			<div class="form-horizontal " id="">
				<div class="form-group">
					<input type="hidden" id="complaintstype" value="COMPLAINT" />
					<div class="avoid-tag-table col-xs-10" id="complaint-list"
						style="overflow: auto;">
						<div class="add-reason avoid-li btn btn-default hide" id="complaint-add">
							<i class="icon-plus"></i>添加原因
						</div>
						<div class="addcomplaint-input-div hide" id="complaint-input">
							<div class="col-xs-3 no-left-padding">
								<input type="text" class="form-control" id="complaint_name" name=""
									maxlength="4" required="required" onblur="complaintSave();" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--div class="setup_div">
			<div style="height: 30px;">
				<div class="system-setup-title" style="">呼叫服务类型设置</div>
				<button type="button" id="editCalltype" class="btn btn-default">编辑</button>
				<button type="button" id="saveCalltype" class="btn btn-default hide">保存</button>
			</div>
			<hr style="margin: 5px 0px;" />
			<div class="form-horizontal " id="">
				<div class="form-group">
					<input type="hidden" id="calltype" value="CALLTYPE" />
					<div class="avoid-tag-table col-xs-10" id="calltype-list"
						style="overflow: auto;">
						<div class="add-calltype avoid-li btn btn-default hide" id="calltype-add">
							<i class="icon-plus"></i>添加原因
						</div>
						<div class="addcalltype-input-div hide" id="addcalltype-input">
							<div class="col-xs-3 no-left-padding">
								<input type="text" class="form-control" id="calltype_name" name=""
									maxlength="5" required="required" onblur="CalltypeSave();" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div-->
		<div class="setup_div">
			<div style="height: 30px;">
				<div class="system-setup-title" style="">零头处理方式</div>
				<br>&nbsp;&nbsp;&nbsp;&nbsp;<font color="red" size="1">(编辑保存后需重启POS方能生效)</font>
				<button type="button" id="editRounding" class="btn btn-default">编辑</button>
				<button type="button" id="saveRounding" class="btn btn-default hide">保存</button>
			</div>
			<hr style="margin: 5px 0px;" />
			<!--div class="avoid-tag-table col-xs-10" -->
			<div class="form-horizontal " id="">
				<div class="form-group">
					<input type="hidden" id="complaintstype" value="COMPLAINT" />
					<div class="avoid-tag-table col-xs-10" style="overflow: auto;">
						<input type="hidden" id="Rounding" value="ROUNDING" />
						<input type="hidden" id="Accuracy" value="ACCURACY" />		
						<label class="system-setup-title">零头处理方式：</label>
						<div class="col-xs-3">
							<select class="form-control myInfo-select-addrW rounding" id="rounding" dictid="" name="rounding" disabled="disabled" >
									  
							</select>	
						</div>
						<div class="" id="accuracyDiv">
							<label class="system-setup-title" id="accuracySet">零头精确度：</label>
							<div class="col-xs-3">
								<select class="form-control myInfo-select-addrW accuracy" id="accuracy" dictid="" name="accuracy" disabled="disabled">
									  
								</select>	
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="setup_div">
			<form action="" method="post" class="form-horizontal " name="" id="">
			<div style="height: 30px;">
				<div class="system-setup-title">服务响应时间设置</div>
				<button type="button" id="editTimes" class="btn btn-default">编辑</button>
				<button type="button" id="saveTimes" class="btn btn-default hide">保存</button>
			</div>
			<hr style="margin: 5px 0px;" />
			<div class="modal-body" style="padding-top: 0px;">
				<div class="form-horizontal " id="retrieveResponsetime">
					<div class="form-group">
						<input type="hidden" id="responsetimetype" value="RESPONSETIME" /> 
						<input type="hidden" id="calltimeid" value="" />
						<input type="hidden" id="paytimeid" value="" />
						<input type="hidden" id="complaintstimeid" value="" />
					</div>
					<div class="form-group">
						<label class="col-xs-2 control-label">投诉：</label>
						<div class="col-xs-2">
							<div class="show_info complaints-time">
								<p class="left"></p>
							</div>
							<div class="edit_info hide">
								<input type="text" value="" class="form-control"
									id="complaints-time" name="complaints-time" required="required" maxlength="10" onkeyup="this.value= this.value.match(/\d+(\.\d{0,2})?/) ? this.value.match(/\d+(\.\d{0,2})?/)[0] : ''"/>
								<div class="unit">秒</div>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-xs-2 control-label">买单：</label>
						<div class="col-xs-2">
							<div class="show_info pay-time">
								<p class="left"></p>
							</div>
							<div class="edit_info hide">
								<input type="text" value="" class="form-control" 
									id="pay-time" name="pay-time" required="required" maxlength="10" onkeyup="this.value= this.value.match(/\d+(\.\d{0,2})?/) ? this.value.match(/\d+(\.\d{0,2})?/)[0] : ''" />
								<div class="unit">秒</div>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-xs-2 control-label">呼叫服务员：</label>
						<div class="col-xs-2">
							<div class="show_info call-time">
								<p class="left"></p>
							</div>
							<div class="edit_info hide">
								<input type="text" value="" class="form-control" 
									id="call-time" name="call-time" required="required" maxlength="10" onkeyup="this.value= this.value.match(/\d+(\.\d{0,2})?/) ? this.value.match(/\d+(\.\d{0,2})?/)[0] : ''" />
								<div class="unit">秒</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			</form>
		</div>
		<div class="setup_div setup_div_box setup_div_social">
			<form action="" method="post" class="form-horizontal" name="" id="">
				<div class="setup-mt" style="height: 30px;">
					<div class="system-setup-title">互动礼品设置</div>
					<button type="button"  class="btn btn-default btn-edit J-btn-op">编辑</button>
				</div>
				<hr style="margin: 15px 0px;" />
				<div class="form-group setup-mc">
					<label class="col-xs-2 control-label">社交功能：</label>
						<div class="col-xs-3">
							<select class="form-control" disabled  name="social">	  
								<option value="0" class="form-control" selected="selected">启用</option>
								<option value="1" class="form-control">关闭</option>
							</select>
						</div>
				</div>

				<div class="form-group form-group-seat">
					<label class="col-xs-2 control-label">上传座位图：</label>
					<div class="col-xs-10">
							<div class="seat-item f-dn seat-item-default">
								<div class="seat-item-pic ">
									<input type="hidden" id="seatImagefiles" name="seatImagefiles" value="">
									<img src="../images/upload-img.png" class="seat-img" class="upload-default">
								</div>
								<div class="seat-item-op f-dn">
									<a href="javascript:void(0);" class="f-fl J-rUpload">重新上传</a>
									<a href="javascript:void(0);" class="f-fr J-btn-del">删除</a>
									<input type="file" onchange="showImg(this)"  size="1"  class="seatImgBtn" id="seatImgIpt1" name="seatImgIpt0" accept="image/*">
								</div>
								<div class="seat-item-name">
									<input type="text" value="" name="seatname1" disabled class="form-control disabled seatname" required="required" />
								</div>
								<div class="seat-item-tip f-dn">请输入作为名称</div>
							</div>
							<div class="seat-item  seat-item-default f-dn">
								<div class="seat-item-pic">
									<input type="hidden" id="setimgurl2" name="setimgurl2" value="">
									<img src="../images/upload-img.png" class="seat-img">
								</div>
								<div class="seat-item-op f-dn">
									<a href="javascript:void(0);" class="f-fl J-rUpload">重新上传</a>
									<a href="javascript:void(0);" class="f-fr J-btn-del">删除</a>
									<input type="file" onchange="showImg(this)"  size="1"  class="seatImgBtn" id="seatImgIpt2" name="seatImgIpt1" accept="image/*">
								</div>
								<div class="seat-item-name">
									<input type="text" value="" name="seatname2" disabled class="form-control disabled seatname" required="required" />
								</div>
								<div class="seat-item-tip f-dn">请输入作为名称</div>
							</div>
							<div class="f-cb"></div>
							<div class="seatimg-rule">
					1.上传图片支持jpg 和 png格式<br/>
					2.上传图片最佳尺寸1536*2048px<br/>
					3. 上传图片大小不能超过2M
				</div>
						</div>
				</div>
				
				

				<div class="form-group form-group-gifts">
					<label class="col-xs-2 control-label">添加互动礼品：</label>
					<div class="col-xs-10">
						<button type="button" id="editGifts" class="btn btn-default store-gifts-add" style="float:left;display:none;" name="addGifts">
							<i class="icon-plus"></i>添加
						</button>
						<div class="f-cb"></div>
						<div id="gifts-div" class="modal-body"></div>
					</div>
				</div>
				
			</form>
		</div>
		
		
		<div class="setup_div setup_div_box setup_div_member">
			<form action="" method="post" class="form-horizontal" name="" id="">
				<div class="setup-mt" style="height: 30px;">
					<div class="system-setup-title">会员设置</div>
					<button type="button" class="btn btn-default btn-edit J-btn-op">编辑</button>
				</div>
				<hr style="margin: 15px 0px;" />
				<div class="form-group">
					<label class="col-xs-2 control-label">启用会员：</label>
						<div class="col-xs-3">
							<select class="form-control" disabled  name="vipstatus">	  
								<option value="0" class="form-control" selected="selected">启用</option>
								<option value="1" class="form-control">关闭</option>
							</select>
						</div>
				</div>

				<div class="form-group viptype-box">
					<label class="col-xs-2 control-label">会员类型：</label>
						<div class="col-xs-3">
							<select class="form-control" disabled  name="viptype">	  
								<option value="1" class="form-control" selected="selected">餐道会员</option>
								<option value="2" class="form-control">雅座会员</option>
							</select>
						</div>
				</div>
			</form>
		</div>
		
		
		<div class="setup_div setup_div_box setup_div_other">
			<form action="" method="post" class="form-horizontal" name="" id="form-other">
				<div class="setup-mt" style="height: 30px;">
					<div class="system-setup-title">其他设置</div>
					<button type="button" class="btn btn-default btn-edit J-btn-op">编辑</button>
				</div>
				<hr style="margin: 15px 0px;" />
				<div class="form-group">
					<div class="col-xs-6">
						<label class="col-xs-4 control-label">点图点菜：</label>
						<div class="col-xs-8">
							<select class="form-control" disabled name="clickimagedish">	  
								<option value="0" class="form-control">启用</option>
								<option value="1" class="form-control">关闭</option>
							</select>
						</div>
					</div>
					<div class="col-xs-6">
						<label class="col-xs-4 control-label">一页菜谱：</label>
						<div class="col-xs-8">
							<select class="form-control" disabled name="onepage">	  
								<option value="0" class="form-control">启用</option>
								<option value="1" class="form-control">关闭</option>
							</select>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-xs-6">
						<label class="col-xs-4 control-label">新手引导：</label>
						<div class="col-xs-8">
							<select class="form-control" disabled name="newplayer">	  
								<option value="0" class="form-control">启用</option>
								<option value="1" class="form-control">关闭</option>
							</select>
						</div>
					</div>
					<div class="col-xs-6">
						<label class="col-xs-4 control-label">中英文国际化：</label>
						<div class="col-xs-8">
							<select class="form-control" disabled name="chinaEnglish">	  
								<option value="0" class="form-control">启用</option>
								<option value="1" class="form-control">关闭</option>
							</select>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-xs-6">
						<label class="col-xs-4 control-label">首页广告：</label>
						<div class="col-xs-8">
							<select class="form-control" disabled name="indexad">	  
								<option value="0" class="form-control">启用</option>
								<option value="1" class="form-control">关闭</option>
							</select>
						</div>
					</div>
					<div class="col-xs-6">
						<label class="col-xs-4 control-label">开发票：</label>
						<div class="col-xs-8">
							<select class="form-control" disabled name="invoice">	  
								<option value="0" class="form-control">启用</option>
								<option value="1" class="form-control">关闭</option>
							</select>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-xs-6">
						<label class="col-xs-4 control-label">隐藏购物车总价：</label>
						<div class="col-xs-8">
							<select class="form-control" disabled name="hidecarttotal">	  
								<option value="0" class="form-control" selected="selected">启用</option>
								<option value="1" class="form-control">关闭</option>
							</select>
						</div>
					</div>
					<div class="col-xs-6">
						<label class="col-xs-4 control-label"></label>
						<div class="col-xs-8 yy-time">
							<label class="control-label col-xs-5">进入异业营销时间：</label>
							<div class=" col-xs-7">
								<input type="text" value="" maxlength="11" oninput="if(value.length>11)value=value.slice(0,11)" class="form-control disabled" disabled name="adtimes" required="required" /><span class="unit">s</span>
							</div>
							<div class="c-red f-fr yy-time-tip f-dn"></div>
							
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-xs-6">
						<label class="col-xs-4 control-label">服务员打赏：</label>
						<div class="col-xs-8 f-pr">
							<select class="form-control" name="waiterreward" disabled>	  
								<option value="0" class="form-control" selected="selected">启用</option>
								<option value="1" class="form-control">关闭</option>
							</select>
							<div class="f-fr c-red" style="position:absolute;bottom:-20px;right:15px;">（目前打赏功能仅支持Pad端）</div>
						</div>
					</div>
					<div class="col-xs-6">
					</div>
				</div>
				
				<!-- <div class="form-group">
					<div class="col-xs-6">
						<label class="col-xs-4 control-label">打赏金额：</label>
						<div class="col-xs-8">
							<div class="c-red mt10">5元</div>
						</div>
					</div>
					<div class="col-xs-6">
					</div>
				</div> -->
				

				
			</form>
		</div>
		
		
		<div class="setup_div setup_div_box setup_div_total" id="form-total">
			<form action="" method="post" class="form-horizontal " name="" id="">
			<div class="setup-mt" style="height: 30px;">
				<div class="system-setup-title">统计设置</div>
				<button type="button" class="btn btn-default btn-edit J-btn-op">编辑</button>
			</div>
			<hr style="margin: 5px 0px;">
			<div class="modal-body" style="padding-top: 0px;">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-xs-2 control-label"><span class="c-red">*</span>友盟应用秘钥(APPKEY)：</label>
						<div class="col-xs-3">
							<div class="edit_info f-pr">
								<input type="text" value="" name="youmengappkey" maxlength="100" oninput="if(value.length>100)value=value.slice(0,100)" class="form-control disabled" disabled placeholder="">
								<div class="f-fr c-red tip f-dn" style="position:absolute;bottom:-20px;:15px;">不能为空</div>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-xs-2 control-label"><span class="c-red">*</span>友盟渠道号(HANNEL)：</label>
						<div class="col-xs-3">
							<div class="edit_info f-pr">
							<input type="text" value="" name="youmengchinnal" maxlength="100" oninput="if(value.length>100)value=value.slice(0,100)" class="form-control disabled" disabled placeholder="">
							<div class="f-fr c-red tip f-dn" style="position:absolute;bottom:-20px;:15px;">不能为空</div>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-xs-2 control-label"><span class="c-red">*</span>大数据接口地址：</label>
						<div class="col-xs-3">
							<div class="edit_info f-pr">
								<input type="text" value="" name="bigdatainterface" class="form-control disabled" maxlength="100" oninput="if(value.length>100)value=value.slice(0,100)" disabled placeholder="">
								<div class="f-fr c-red tip f-dn" style="position:absolute;bottom:-20px;:15px;">不能为空</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			</form>
		</div>
		
		<!-- -------------------------------------PAD端图片设置------------------------------------- -->
		
		<div class="setup_div clear deflogo">
			<form action="" method="post" class="form-horizontal " name="" id="tableware_form">
				<div style="height: 30px;">
					<div class="system-setup-title">LOGO图片设置</div>
				</div>
				<hr style="margin: 5px 0px;" />
				<div class="modal-body" style="padding-top: 0px;">
					<img src='../images/defaultlogo.png' id="defaultlogo" />
	                <div class="tag">
	                    <div class="arrow">
	                        <em></em><span></span>
	                    </div>
	                    <span class="tagspan">更改LOGO请登录总店后台修改</span>
	                </div>
				</div>
			</form>
		</div>
		
		
		<div class="setup_div clear defBackground">
			<form action="" method="post" class="form-horizontal " name="" id="tableware_form">
				<div style="height: 30px;">
					<div class="system-setup-title">PAD启动背景图片设置</div>
				</div>
				<hr style="margin: 5px 0px;" />
				<div class="modal-body" style="padding-top: 0px;">
					<img src='../images/def_background.png' id="def_background" style="margin:0;padding:0;border:1px solid #ddd;float:left"/>
	                <div class="bgtag">
	                    <div class="arrow" >
	                        <em></em><span></span>
	                    </div>
	                    <span class="tagspan1">更改PAD背景图请登录总店后台修改</span>
	                </div>
				</div>
			</form>
		</div>
		
	<!-- --------------------------PAD端图片设置结束----------------------------------------- -->				
		
	</div>
	<div class="modal fade " id="dish-select-dialog" aria-hidden="true">
	</div>
	<script src="<%=request.getContextPath()%>/scripts/global.js"></script>
	<script
		src="<%=request.getContextPath()%>/scripts/projectJs/systemSetting.js"></script>
	<script>
	/**
	 * 显示已选择的礼物在系统设置页面中
	 */
	function saveSelectedGifts(selectedDishs,type){
		if(selectedDishs!=null && selectedDishs.length>0){
			saveGiftsToPage(selectedDishs);
			if(type === 'save') {
				saveGifts(selectedDishs);
			}
			
		}
	}
	/**
	 * 保存礼物
	 */
	function saveGifts(selectedDishs){
		$.post(global_Path+"/social/saveGift", {data: JSON.stringify(selectedDishs)}, function(result){
			console.log(result);
			if(result.flag == 1){
				//saveGiftsToPage(selectedDishs);
			}else{
				
			}
		},'json');
	}

	function saveGiftsToPage(selectedDishs){
		haveSelected = new Array();
		var htm = "";
		$.each(selectedDishs, function(i, item){
			haveSelected.push(item);
			var giftname = (i+1)+"、"+item.dish_title+"("+item.unit+")";
			console.log(giftname);
			htm += "<div class='one-gift git-overflow'>"+giftname+"</div>";
		});
		$("#gifts-div").html(htm);
	}
	/**
	 * 生成菜品选择分类的显示内容
	 */
	function generalDishTitle(){
		var checkedDishs=$("#dish-select-dialog #accordion").find("input[type='checkbox']:checked");
		var selectedDishs=[];
		if(checkedDishs.length > 0){
			$.each(checkedDishs,function(i,obj){
				//防止不同分类中的同一菜品重复添加。
				var d={};
				d.dish=$(obj).val();
				d.unit=$(obj).attr("unit");
				var hasSelected = false;
				$.each(selectedDishs, function(j, dish){
					if(dish.dish==d.dish && dish.unit==d.unit){
						hasSelected = true;
						return false;
					}
				});
				if(!hasSelected){
					selectedDishs.push(d);
				}
			});
		}
		if(selectedDishs.length <= 0){
			allselected = 0;
			$("#select-font").text("");
		} else {
			allselected = selectedDishs.length;
			$("#select-font").text("(已选"+selectedDishs.length+"种菜品)");
		}
		$.each( $("#dish-select-dialog #accordion").find(".panel"), function(i,panel){
			var dish_array=[];
			var dishs= $(this).find("input[type=checkbox]:checked");
			if(dishs.length > 0){
				if(dishs.length < $(this).find("input[type=checkbox]").length){
					$(this).find("img").attr("alt", "1");
					$(this).find("img").attr("src", global_Path+ "/images/sub_select.png");
					var panelTitle = "";
					$.each(dishs, function(i){
						panelTitle += $(this).parent().text();
						if(panelTitle.length >= 15){
							panelTitle = panelTitle.substring(0,15)+"...";
							return false;
						}
						if(i < dishs.length -1){
							panelTitle +="、";
						}
						
					});
					panelTitle = "(" + panelTitle + ")";
					$(this).find(".dish-label").html(panelTitle);
					dishs.attr("itemtype","");
					dishs.attr("itemdesc","");
					dishs.attr("item_select", "");
				} else {
					$(this).find("img").attr("alt", "2");
					$(this).find("img").attr("src", global_Path+ "/images/all_select.png");
					$(this).find(".dish-label").html("");
					dishs.attr("itemtype",$(this).find("span").attr("itemtype"));
					dishs.attr("itemdesc",$(this).find("span").text());
					dishs.attr("item_select", true);
				}
			} else {
				$(this).find("img").attr("alt", "0");
				$(this).find("img").attr("src", global_Path+ "/images/none_select.png");
				$(this).find(".dish-label").html("");
			}
		});
		
		//更新全选与全不选按钮的状态。当不是全选或者全不选的时候，清除这两个单选按钮的选中状态。
		$("input[type='checkbox'][name='dish-radio-uncheck']").prop("checked",false);
		if( checkedDishs.length == 0 ){
			//当前为不全选。
			$("input[type='checkbox'][name='dish-radio-uncheck'][value='0']").prop("checked",true); 
		}else if( checkedDishs.length == $("#dish-select-dialog #accordion").find("input[type='checkbox']").length ){
			//
		}
		
	}

	function changImg(){
		$('#saveLOGO').removeClass('hide');
		$('#editLOGO').hide();
		$('#defaultlogo').attr('src','../images/uplogo.png');
		
		$('#defaultlogo').css("border","1px dashed #ddd")
		$('#defaultlogo').click(function(){
			alert("jjjj");
			
		})
	}
	</script>
	
	
</body>
</html>