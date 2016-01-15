<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!--创建完成弹出框-->

<div class="modal fade menuAdd-complete-dialog in "
	id="menuAdd-complete-dialog" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<div class="modal-title">菜谱属性设置</div>
				<img src="../images/close.png" class="img-close"
					data-dismiss="modal">
			</div>
			<hr class="dishes-dialog-hr">
			<div class="modal-body" style="padding-top: 0px;">
				<div class="alert_msg" id="alert_msg"></div>
				
				<form action="" method="post" class="form-horizontal " name="" id="menuadd_complete_form" style="margin-bottom: 0;">
					<div class="form-group">
						<label class="col-xs-5 control-label"><span
							class="required-span">*</span>菜谱名称：</label>
						<div class="col-xs-7">
							<input type="text" value="" class="form-control" required="required" name="addmenu-name"
								id="addmenu-name" placeholder="* 为必填项"  maxlength="15">
							<label id="sameNameError" class=" error" style="display: none;" for="addmenu-name" >菜谱名称不能重复</label>
						</div>
					</div>
					<div class="form-group">
						<label class="col-xs-5 control-label"><span
							class="required-span">*</span>适用门店：</label>
						<div class="col-xs-7 menu-shop-edit" id="store-select">
							<input id="selectBranchs" name="selectBranchs" type="hidden" value="" />
							<input id="selectBranchNames" name="selectBranchNames" type="hidden" value="" />
							<button class="btn btn-default" type="button" id="addstore">
								<i class="icon-plus-sign"></i>
							</button>
							<label id="selectBranchs_error" for="selectBranchs" class="error" style="display: none;">必选信息</label>
						</div>
					</div>
					<div class="form-group">
						<label class="col-xs-5 control-label"><span
							class="required-span">*</span>菜谱状态：</label>
						<div class="col-xs-7">
							<label class="radio-inline"> <input type="radio"
								name="status" value="2">启用
							</label> <label class="radio-inline"> <input type="radio"
								name="status" value="4">不启用
							</label>
						</div>
					</div>
					<div class="form-group" id="effecttime_div">
						<label class="col-xs-5 control-label"><span
							class="required-span">*</span>启用时间：</label>
						<div class="col-xs-7">
							<input type="text" id="effecttime" name="effecttime" required="required" value="" placeholder="* 为必填项" 
							class="form-control date-bg" readonly onFocus="WdatePicker({startDate:'+%Y-%m-%d %H:%M:%S',dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-{%d+1}'})">
						</div>
					</div>
					<div class="btn-operate btn-operate-dishes">
						<button class="btn btn-cancel" type="button" id="addmenu-cancel">取消</button>
						<div class="btn-division"></div>
						<!--button class="btn btn-save in-btn135" id="addmenu-save" type="submit">确认</button-->
						<button class="btn btn-save in-btn135" type="submit">确认</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<!-- 选择门店 -->
<div class="modal fade storeSelect-dialog in " id="store-select-dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header addDelicon">
				<img data-dismiss="modal" class="img-close"
					src="/newspicyway/images/close.png">
			</div>
			<div class="modal-body">
				<div class="row store-select-title">
					<div class="col-xs-9">
						选择门店<font id="store-count"></font>
					</div>
					<div class="col-xs-3 pull-right">
						<label class="radio-inline"> <input type="radio" value="1"
							name="checkAll">全选
						</label> <label class="radio-inline"> <input type="radio"
							value="0" name="checkAll">全不选
						</label>
					</div>
				</div>
				<hr>
				<table class="table store-select-content">
				</table>
				<div class="btn-operate">
					<button class="btn btn-cancel in-btn135" id="store-select-cancel">取消</button>
					<div class="btn-division"></div>
					<button class="btn btn-save in-btn135 preferential-btn-bgcolor"
						id="store-select-confirm">确认</button>
				</div>
			</div>
		</div>
	</div>
</div>
<!--修改菜品属性 -->
<div class="modal fade menuAttr-change-dialog in "
	id="menuAttr-change-dialog" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<div class="modal-title">菜品属性</div>
				<img src="../images/close.png" class="img-close"
					data-dismiss="modal">
			</div>
			<hr class="dishes-dialog-hr">
			<div class="modal-body" style="padding-top: 0px;">
				<form action="" method="post" class="form-horizontal " name="" id="dish_menu_prop">
					<div class="form-group">
						<label class="col-xs-2 control-label"><span
							class="required-span">*</span>菜品名称：</label>
						<div class="col-xs-3">
							<input type="hidden" value="" class="form-control" name="dishid"
								id="dishid">
							<input type="text" value="" class="form-control" required="required" name="dishname" id="dishname" maxlength="12">
						</div>
					</div>
					<div class="form-group" id="dish_price"></div>

					<div class="form-group">
						<label class="col-xs-2 control-label">菜品介绍：</label>
						<div class="col-xs-10">
							<textarea id="dishintroduction" class="form-control" maxlength="100"></textarea>
							<p class="menuAttr-tip">
								继续可以输入<span id="count">100</span>字
							</p>
						</div>

					</div>
					<div class="btn-operate btn-operate-dishes">
						<button class="btn btn-cancel in-btn135" data-dismiss="modal">取消</button>
						<div class="btn-division"></div>
						<button class="btn btn-save in-btn135" id="dishes-detail-save" type="submit">确认</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<!-- 确认框 -->
<div class="modal fade dialog-sm in menu-tip-dialog"
	data-backdrop="static" id="modal_confirm_save">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-body">
				<div class="dialog-sm-header">
					<span>提示</span> <img src="../images/close-sm.png" class="img-close"
						data-dismiss="modal">
				</div>
				<form action="" method="post" class="form-horizontal " name="">
					<p class="menu-tip-title">距离菜谱启用还有</p>
					<p class="menu-tip-content">
					<div class="menu-tip-time" id="how-days">2</div>
					<span class="menu-tip-flag">天</span>
					<div class="menu-tip-time" id="how-hours">2</div>
					<span class="menu-tip-flag"> 时</span>
					<div class="menu-tip-time" id="how-minutes">2</div>
					<span class="menu-tip-flag">分</span>
					<div class="menu-tip-time" id="how-seconds">2</div>
					<span class="menu-tip-flag">秒</span>
					</p>
					<div class="btn-operate">
						<button class="btn btn-cancel" type="button" data-dismiss="modal">取消</button>
						<div class="btn-division"></div>
						<button class="btn btn-save" type="button" id="dosave_btn">确认</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<!--提示2-->
<div class="modal fade dialog-sm in menu-tip-dialog"
	data-backdrop="static" id="menu-tip-dialog2">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-body">
				<div class="dialog-sm-header">
					<span>提示</span> <img src="../images/close-sm.png" class="img-close"
						data-dismiss="modal">
				</div>
				<form action="" method="post" class="form-horizontal " name="">
					<p class="menu-tip-title">距离菜谱启用还有</p>
					<p class="menu-tip-content">
						<button class="btn btn-default">立即启用</button>
					</p>
					<div class="btn-operate">
						<button class="btn btn-cancel" type="button" data-dismiss="modal">取消</button>
						<div class="btn-division"></div>
						<button class="btn btn-save" type="button">确认</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<!-- 图片调整弹出框 -->
<div class="modal fade menuImg-adjust-dialog in "
	id="menuImg-adjust-dialog" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<div class="modal-title">图片调整</div>
				<img src="../images/close.png" class="img-close"
					data-dismiss="modal">
			</div>
			<hr class="dishes-dialog-hr">
			<div class="modal-body">
				<div class="menu-img-adjust">
					<img src="" id="target-img">
				</div>
				<div>
					<input type="hidden" id="x" name="x" value="">
					<input type="hidden" id="y" name="y" value="">
					<input type="hidden" id="h" name="h" value="">
					<input type="hidden" id="w" name="w" value="">
					<input type="hidden" id="thumbnum" value="">
					<input type="hidden" id="dropid" value="">
				</div>
				<div class="btn-operate" style="margin: 20px 0px 0px;">
					<button class="btn btn-cancel" id="cancel-btn" type="button" data-dismiss="modal">取消</button>
					<div class="btn-division"></div>
					<button class="btn btn-save" id="adjust-save" type="button">确认</button>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 菜谱启用状态提示 -->
<div class="modal fade dialog-sm in menu-tip-dialog"
	id="menuControl-prompt-modal" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
		<div class="modal-body">
			<div class="dialog-sm-header">
				<span>提示</span>
				<img src="../images/close-sm.png" class="img-close"
						data-dismiss="modal">
			</div>
			<form action="" method="post" class="form-horizontal " name="">
				<p style="margin-top: 30px; font-size: 12px;">
					当前菜谱状态为<span class="ky-info">“已启用”</span>，确定对其进行修改？
				</p>
				<div class="btn-operate">
					<button class="btn btn-cancel" type="button" data-dismiss="modal">取消</button>
					<div class="btn-division"></div>
					<button class="btn btn-save" id="menuControl-ok" type="button">确认</button>
				</div>
			</form>
		</div>
		</div>
	</div>
</div>
<div class="modal fade dialog-sm in menu-tip-dialog"
	id="menuadd-prompt-modal" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
		<div class="modal-body">
			<div class="dialog-sm-header">
				<span>提示</span>
				<img src="../images/close-sm.png" class="img-close"
						data-dismiss="modal">
			</div>
			<form action="" method="post" class="form-horizontal " name="">
				<p id="prop-msg" style="margin-top: 30px; font-size: 12px; text-align: center;">
				</p>
				<div class="btn-operate">
					<button class="btn btn-save" data-dismiss="modal" type="button">确认</button>
				</div>
			</form>
		</div>
		</div>
	</div>
</div>
<input type="text" class="hidden " value="" id="menuid"   >