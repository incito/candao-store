<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal fade" id="dishes-combo-dialog" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-body">

				<div class="dishes-combo-title">添加套餐</div>
				<img src="../images/close.png" class="img-close" data-dismiss="modal">
				<hr class="ky-hr">
				<form class="form-horizontal ky-form"  method="post" name=""  id="dishes-combo-dialog-form">
					<input id="combodishCreateTime" class="hidden" >
					<div class="form-group">
						<label for="" class="control-label" >套餐编号：</label>
						<div class="ky-inputW">
							<input type="text"	value="" class="form-control " id="combodishno" name="combodishno" disabled="disabled">	
							<input type="hidden" value="" class="form-control" id="combodishid">
						</div>
						<label for="" class="control-label"><span class="required-span ">*</span>套餐名称：</label>
						<div class="ky-inputW">
							<input type="text" class="form-control required" id="combotitle" name="combotitle" placeholder="" maxlength="12">
							<font color="red" id="combotitle_tip" class="error"></font>
						</div>
						<label for="" class="control-label"><span class="required-span">*</span>菜品分类：</label>
						<div class="ky-inputW">
							<div class="input-group select-box" id="combodishTypeShow">
								<input type="text" class="form-control " aria-describedby="basic-addon1" id="comboselectDishTypeName" name="comboselectDishTypeName"  disabled="disabled">
								<input type="hidden" value="" class="form-control" id="combocolumnid">
								<span class="input-group-addon arrow-down" id="basic-addon1"><i class="icon-chevron-down"></i></span>
							</div>		
							<font color="red" id="comboselectDishTypeName_tip" class="error"></font>	
							<div class="select-content hidden select-multi scrollspy-example" id="combodishTypeSelect"  style="overflow: scroll;width: 175px; ">
				    		</div>	
						</div>
					</div>
					<div class="form-group">
						<label for="" class="control-label"><span class="required-span">*</span>价格：</label>
						<div class="ky-inputW">
							<input type="text" class="form-control required" id="comboprice" name="comboprice" placeholder="">
						</div>
						<label for="" class="control-label">会员价：</label>
						<div class="ky-inputW">
							<input type="text" class="form-control" id="combovipprice" name="combovipprice" placeholder="">
						</div>
						<label for="" class="control-label">套餐标签：</label>
						<div class="ky-inputW dishes-taste " >
							<div class="btn btn-default" type="button" id="combodishes-label-add" name="combodishes-label-add"  ><i class="icon-plus-sign"></i></div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-xs-2 control-label" style="width: 120px;margin-right:10px;">参与人气推荐：</label>
						<div class="col-xs-2">
							<div class="switch demo3" >
								<input type="checkbox" id="recommendCombo"/>
								<label><i data-on="是" data-off="否"></i></label> 
							</div>
						</div>
					</div>
					<div class="form-group">
						<label for="" class="control-label">套餐介绍：</label>
						<div class="dishes-textareaW">
							<textarea name="" id="combointroduction" cols="30" rows="4" class="form-control" maxlength="100" placeholder="“腌”出来的好锅底？没错，新辣道锅底做大的特色就是“腌” —— 腌出来的浓厚醇香，腌出来的红油亮糖，腌出来的余香绵绵... ..."></textarea>
							<p class="help-block text-right">继续可以输入<span id="combocount" >100</span>字</p>
						</div>
						<div class="dishes-fileup">
							<img src="../images/combo-fileup.png" width="160px" height="100px" onclick="combotempClick(2)" id="uploadpic2">
							<input type="file" onchange="comboshowpic(2)" style="position: absolute; filter: alpha(opacity = 0); opacity: 0; width: 0px;" size="1" id="main_img2" name="main_img2" accept="image/*" >
						</div>
					</div>
					<div class="form-group">
<!-- 						<div class="dishes-comboDetail"  style="background-color: #FAFAFA;text-align: center;vertical-align: middle;"> -->
						<div class="dishes-comboDetail"  style="background-color: #FAFAFA;">
							<div class="dishes-comboDetail-title">
								套餐明细 <i id="taocanDetailid" class="glyphicon glyphicon-edit" onclick="showcombodishselect(1)"></i>
							</div>
						<span class="glyphicon glyphicon-chevron-left dishes-roll-left" style="display: none;color:#B3B3B3;" id="comboleft"></span>
							<div id="initComboDishDetail" >
								<img alt="" src="../images/comboadd.png" style="cursor: pointer;" onclick="showcombodishselect(2)">
							</div>
							<div id="selectDishShow" class="dishes-comboDetail-content" style="padding: auto;">
							</div>
						<span class="glyphicon glyphicon-chevron-right dishes-roll-right" style="display: none;color:#B3B3B3;" id="comboright"></span>
						</div>
					</div>
					<div class="btn-operate btn-operate-dishes">
						<button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal">取消</button>
						<div class="btn-division"></div>
						<button class="btn btn-save in-btn135" id="combodishes-detail-save" type="submit">确认</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<div class="modal fade"  id="combodish-select-dialog">
</div>
<div class="modal fade" id="dishes-comboEdit-dialog">
	<div class="modal-dialog">
		<div class="modal-content">
		<img src="../images/close.png" class="img-close" data-dismiss="modal">
			<div class="modal-body" style="height: 460px;overflow: auto;">
				
				<div class="dishes-comboEdit-title"><span id="modify-columnname"></span>
				
				<input type="hidden" id="modify-columnid" value=""/>
				</div>
				
				<hr class="ky-hr" />
				<div id="optionalbutton">
				</div>
<!-- 				<div class="dishes-comboEdit-box"> -->
<!-- 					<div class="checkbox"> -->
<!-- 						<label> <input type="checkbox" value=""><span -->
<!-- 							class="dishes-comboEdit-boxi">1</span>红汤梭边鱼 -->
<!-- 						</label> -->
<!-- 					</div> -->
<!-- 					<div class="dishes-panBottom"> -->
<!-- 						<span class="dishes-panBottom-name">红汤锅底</span> <span -->
<!-- 							class="dishes-panBottom-num"><input type="text" -->
<!-- 							class="form-control"><span class="dishes-unit">份</span></span> <span -->
<!-- 							class="dishes-panBottom-price">48元</span> -->
<!-- 					</div> -->
<!-- 					<div class="dishes-panBottom"> -->
<!-- 						<span class="dishes-panBottom-name">红汤锅底</span> <span -->
<!-- 							class="dishes-panBottom-num"><input type="text" -->
<!-- 							class="form-control"><span class="dishes-unit">份</span></span> <span -->
<!-- 							class="dishes-panBottom-price">48元</span> -->
<!-- 					</div> -->
<!-- 				</div> -->
					<div id="dishes-comboEdit-box-list-unselected" class="dishes-comboEdit-box-list" ></div>
					<div class="dishes-combined" style="display: none;">
						以下菜品<span id="startnum"></span>选<span id="endnum"></span>
						<button type="button" class="btn-combined pull-right"  onclick="restComboDishGroup()">重新组合</button>
					</div>
					<div id="dishes-comboEdit-box-list-selected" class="dishes-comboEdit-box-list"></div>
			</div>
			<div class="btn-operate btn-operate-dishes" style="margin-left: 5px;">
				<button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal">取消</button>
				<div class="btn-division"></div>
				<button class="btn btn-save in-btn135 btn-save" id="combodishes-selectdetail-save" onclick="savegroupselected()"
					type="button">确认</button>
			</div>
		</div>
	</div>
</div>
