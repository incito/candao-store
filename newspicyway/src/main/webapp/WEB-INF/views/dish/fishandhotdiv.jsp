<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--添加火锅详情框 -->
<div class="modal fade" id="dishes-hot-dialog" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-body">
				<div class="dishes-combo-title">添加鱼锅</div>
				<img src="../images/close.png" class="img-close" data-dismiss="modal">
				<hr class="ky-hr">
				<form class="form-horizontal ky-form" id="dishes-fishpot-dialog-form" method="post" >
					<input id="fishpotCreateTime" class="hidden" >
					<div class="form-group">
						<label for="" class="control-label">鱼锅编号：</label>
						<div class="ky-inputW">
							<input type="text" value="" class="form-control " id="fishpotdishno" name="fishpotdishno" disabled="disabled"> 
							<input type="hidden" value="" class="form-control" id="fishpotdishid">
						</div>
						<label for="" class="control-label"><span class="required-span ">*</span>鱼锅名称：</label>
						<div class="ky-inputW">
							<input type="text" class="form-control required" id="fishpottitle" name="fishpottitle" placeholder="" maxlength="12">
							<font color="red" id="fishpottitle_tip" class="error"></font>
						</div>
						
						<label for="" class="control-label"><span class="required-span ">*</span>菜品分类：</label>
						<div class="ky-inputW">
							<div class="input-group select-box" id="fishpotdishTypeShow" >
								<input type="text" class="form-control required" aria-describedby="basic-addon1" id="fishpotselectDishTypeName"
								name="fishpotselectDishTypeName" disabled="disabled"	maxlength="0" >
								 <input type="hidden" value=""
									class="form-control" id="fishpotcolumnid"> <span
									class="input-group-addon arrow-down" id="basic-addon1"><i
									class="icon-chevron-down"></i></span>
							</div>
							<font color="red" id="fishpotselectDishTypeName_tip" class="error"></font>	
							<div class="select-content hidden select-multi scrollspy-example"
								id="fishpotdishTypeSelect" style="overflow: scroll; width: 175px;">
							</div>
						</div>
					</div>
					<div class="form-group">
						<label for="" class="control-label">鱼锅口味：</label>
						<div class="ky-inputW dishes-taste ">
							<div class="btn btn-default" type="button"
								id="fishpotdishes-taste-add">
								<i class="icon-plus-sign"></i>
							</div>
						</div>
						<label for="" class="control-label">鱼锅标签：</label>
						<div class="ky-inputW dishes-taste ">
							<div class="btn btn-default" type="button"
								id="fishpotdishes-label-add">
								<i class="icon-plus-sign"></i>
							</div>
						</div>
						<label class="col-xs-2 control-label" style="width: 115px;margin-right:10px;">参与人气推荐：</label>
						<div class="col-xs-2">
							<div class="switch demo3" >
								<input type="checkbox" id="recommendFish"/>
								<label><i data-on="是" data-off="否"></i></label> 
							</div>
						</div>
					</div>
					<div class="form-group">
						<label for="" class="control-label">鱼锅介绍：</label>
						<div class="dishes-textareaW">
							<textarea name="" id="fishpotintroduction" cols="30" rows="4" maxlength="100"
								class="form-control" placeholder="“腌”出来的好锅底？没错，新辣道锅底做大的特色就是“腌” —— 腌出来的浓厚醇香，腌出来的红油亮糖，腌出来的余香绵绵... ..."></textarea>
							<p class="help-block text-right">
								继续可以输入<span id="fishpotcount" >100</span>字
							</p>
						</div>
						<div class="dishes-fileup">
							<img src="../images/combo-fileup.png" width="160px"
								height="100px" onclick="combotempClick(3)" id="uploadpic3">
							<input type="file" onchange="comboshowpic(3)"
								style="position: absolute; filter: alpha(opacity = 0); opacity: 0; width: 0px;"
								size="1" id="main_img3" name="main_img3" accept="image/*">
						</div>
					</div>
					<div class="form-group">
						<div class="dishes-comboDetail" style="background-color: #FAFAFA;text-align: center;">
							<div class="dishes-comboDetail-title">
								鱼锅内容明细
							</div>
							<div id="selectFishDetail" class="fishpot-addfish" >
								<p><img alt="" src="../images/addfish.png" onclick="addfishandhot(0)">添加鱼</p>
								<ul class="fishpotli" id="fishul">
								</ul>
								<div>
									<ul class="fishoption" id="fishidUl">
										<li><img alt="" src="../images/fishpotadd.png" title="添加鱼" onclick="addfishandhot(0)"></li>
               							<li><img alt="" src="../images/fishpotedit.png" title="编辑鱼" onclick="addfishandhot(0)"></li>
										<li><img alt="" src="../images/fishpotdel.png" title="删除鱼" onclick="delfishandhot(0)"></li>
									</ul>
								</div>
							</div>
							<div id="selectHotDetail" class="fishpot-addhot">
								<p><img alt="" src="../images/addhot.png" onclick="addfishandhot(1)">添加锅</p>
								<ul class="fishpotli" id="potul">
								</ul>
								<div>
									<ul class="potoption" id="potopidUl">
										<li><img alt="" src="../images/fishpotadd.png" title="添加锅" onclick="addfishandhot(1)"></li>
										<li><img alt="" src="../images/fishpotedit.png" title="编辑锅" onclick="addfishandhot(1)"></li>
										<li><img alt="" src="../images/fishpotdel.png" title="删除锅" onclick="delfishandhot(1)"></li>
									</ul>
								</div>
							</div>
						</div>
					</div>
					<div class="btn-operate btn-operate-dishes">
						<button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal">取消</button>
						<div class="btn-division"></div>
						<button class="btn btn-save in-btn135"  type="submit">确认</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<div class="modal fade"  id="fishpotdish-select-dialog" data-backdrop="static">
</div>
<!--删除单个菜品弹出框-->
		<div class="modal fade dialog-sm dishes-detailDel-dialog in " id="fishpotdishes-detailDel-dialog"  data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">	
				<div class="modal-body">
					<div class="dialog-sm-header">				  
				        <span>确认删除</span>
				        <img src="<%=request.getContextPath()%>/images/close-sm.png" class="img-close" data-dismiss="modal">
				    </div>
						<!-- 仅存在一个分类中-->
						<div class="dialog-sm-info">
						<input id="fishpotflag" value="" type="hidden">
						<p class="p1"><img src="<%=request.getContextPath()%>/images/del-tip.png">确认删除“<span id="showpotName"></span>”吗?
						</p>
						</div>
						<div class="btn-operate">
							<button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal">取消</button>
							<div  class="btn-division"></div>
							<button class="btn btn-save in-btn135"  type="button" onclick="confirmDelfishpot(1)">确认</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- 删除的多个菜品的弹出框 -->
		<div class="modal fade dishes-detailDel-dialog in "   id="mutfishpotdishes-detailDel-dialog"  data-backdrop="static">
			<div class="modal-dialog" style="width: 400px;">
				<div class="modal-content">	
					<div class="modal-header">				  
				        <h4 class="modal-title">确认删除</h4>
				        <img src="<%=request.getContextPath()%>/images/close.png" class="img-close" data-dismiss="modal">
				    </div>
				    <hr class="dishes-dialog-hr">
					<div class="modal-body">
							<!-- 仅存在一个分类中-->
							<div>
							<ul class="fishpot-dish-title-del">
<!-- 							<li><span>1、红汤锁边鱼 48/斤  </span> <button  onclick="delthis(this)" class="button-del" unit="份" type="button" id="2">删除</button></li> -->
<!-- 							<li><span>1、红汤锁边鱼 48/斤 </span> <button  onclick="delthis(this)" class="button-del" type="button" id="">删除</button></li> -->
<!-- 							<li><span>1、红汤锁边鱼 48/斤   </span> <button  onclick="delthis(this)" class="button-del" type="button" id="">删除</button></li> -->
							</ul>
							</div>
							<div class="btn-operate">
								<button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal">取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save in-btn135" id="dishes-type-save" type="button" onclick="confirmDelfishpot(2)">确认</button>
							</div>
					</div>
				</div>
			</div>
		</div>
