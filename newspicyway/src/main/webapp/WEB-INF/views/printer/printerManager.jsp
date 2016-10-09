<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
	<link href="../tools/bootstrap/css/bootstrap.css" rel="stylesheet">
	<link rel="stylesheet" href="../css/common.css">
	<link rel="stylesheet" href="../css/index.css">
	<link rel="stylesheet" href="../css/print.css">


	<link rel="stylesheet" href="../tools/font-awesome/css/font-awesome.css">
	<script src="../scripts/jquery.js"></script>
	<script src="../tools/bootstrap/js/bootstrap.min.js"></script>
	<script src="../scripts/projectJs/index.js"></script>
	<script src="../scripts/global.js"></script>
	<script src="../scripts/projectJs/print.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/jquery.validate.js"></script>
	<script type="text/javascript">
		var global_Path = '<%=request.getContextPath()%>';
	</script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/messages_zh.js"></script>
</head>
<body>
		<div class="ky-contenter">
			<div class="print-content" id="print-content">
		
<!-- 					<div class="print-detail-box"  onmouseover="showPrintDel(this)" onmouseout="displayPrintDel(this)" ondblclick="editPrintBox(this)"> -->
<!-- 						<p class="print-img" ><img src="../images/print.png"></p> -->
<!-- 						<p>厨房打印</p> -->
<!-- 						<i class="icon-remove hidden" onclick="delPrintBox(this)"></i> -->
<!-- 					</div> -->
					
					<button class="printConfig-btn btn btn-default" id="printConfig-add"   type="button" ><i class="icon-plus"></i> 打印配置</button>
			</div>
	
		</div>

	
		<!--添加打印配置 -->
	<div class="modal fade printConfig-add-dialog in " id="printConfig-add-dialog"  data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content">	
				<div class="modal-header">				  
			        <div class="modal-title">添加打印配置</div>
			        <img src="../images/close.png" class="img-close"  data-dismiss="modal">
			    </div>
				<div class="modal-body">			
					<form action="" id="add-form-printConfig" method="post" class="form-horizontal " name="">
						<input type="hidden" id="printerid" name="printerid" />
						<div class="form-group"> 
							<label class="col-xs-5 control-label "><span class="required-span">*</span>打印配置名称：</label>
							<div class="col-xs-7">
								
								<input type="text"	value="" class="form-control required" name="printConfig-name" id="printConfig-name" maxlength="15">	
								<font color="red" id="printConfig-name_tip" class="error"></font>
							</div>
							
						</div>
						
						<div class="form-group">
							<label class="col-xs-5 control-label"><span class="required-span">*</span>打印配置IP：</label>
							<div class="col-xs-7">
								<input type="text"	value="" class="form-control  required" name="ipAddress" id="ipAddress">	
								<font color="red" id="ipAddress_tip" class="error"></font>
							</div>
							
						</div>
						<div class="form-group">
							<label class="col-xs-5 control-label"><span class="required-span">*</span>打印配置端口：</label>
							<div class="col-xs-7">
								<input type="text"	value="" class="form-control digits required" name="port" id="port" max="65535" min="1">	
<!-- 								<font color="red" id="port_tip" class="error"></font> -->
							</div>
							
						</div>
						<div class="form-group">
							<label class="col-xs-5 control-label"><span class="required-span">*</span>打印单据：</label>
							<div class="col-xs-7">
								<select class="form-control" id="print-bill" name="print-bill">
									<option value="1">厨打单</option>
									<option value="2">客用单</option>
									<%--<option value="3">预结单</option>--%>
									<option value="4">结账单</option>
								</select>
							</div>
						</div>
						<div class="form-group" id="print-font">
							<label class="col-xs-5 control-label"><span class="required-span">*</span>字体大小：</label>
							<div class="col-xs-7">
								<select class="form-control" id="fontSize" name="fontZise">
									<option value="1">标准</option>
									<option value="2">小字体</option>
									<option value="3">大字体</option>
								</select>
							</div>
						</div>
						<div class="form-group hidden" id="print-note">
							<label class="col-xs-10 " style="text-align:left;padding-left:35px">POS单据</label>
                            <div class="col-xs-10" style="width: 100%;">
							<div>
								预结单、结账单、会员消费客户联和商户联、重印客用单、清机单、发票单、会员储值客户联和商户联、品项销售、小费统计、营业数据明细
							</div>
                                </div>
						</div>
						<!--已选择内容的样式-->
					 	<div class="form-group" id="print-area">
							<label class="col-xs-5 control-label "><span class="required-span">*</span>打印区域：</label>
							<div class="col-xs-7" id="div-print-area-add">
<!-- 							<a href="#" id="example" class="btn btn-success" rel="popover">hover for popover</a> -->
<!-- 								<button  class="btn btn-default required" type="button" id="print-area-add"></button>		 -->
								 <button type="button" class="btn btn-default required " data-html="true" title=""  id="print-area-add"
							      data-placement="bottom" rel="popover"    >
							     </button>
								<font color="red" id="print-area-add_tip" class="error"></font>
							</div>
						</div>	 
						<div class="form-group " id="print-dishes">
							<label class="col-xs-5 control-label "><span class="required-span">*</span>打印菜品：</label>
							<div class="col-xs-7" id="div-print-dishes-add">
<!-- 								<button title="" class="btn btn-default required" type="button" id="print-dishes-add"></button>		 -->
								<button type="button" class="btn btn-default required " data-html="true" title=""  id="print-dishes-add"
							     data-container=""  data-toggle="popover" data-placement="bottom" 
							     data-content=""></button>
							     <div class="popover fade bottom in" style="top: 30px; left: -145px; display: none;">

								    <div class="arrow"></div>
								    <h3 class="popover-title" style="display: none;"></h3>
								    <div class="popover-content">
								        <div class="tableOrdiah-detail-box"></div>
								        <div class="tableOrdiah-detail-box"></div>
								        <div class="tableOrdiah-detail-box"></div>
								    </div>
								
								</div>
								<font color="red" id="print-dishes-add_tip" class="error"></font>
							</div>
						</div>
						<div class="form-group hidden" id="print-groupdishes" style="margin: auto;width: 88%;">
							<div class="col-xs-2 group-div" style="text-align: left; margin: auto; width: 130px;padding-top: 5px;" groupid="1">
								<button type="button" style="font-size: 13px;" class="btn btn-default required " data-html="true" title=""
							     data-container=""  data-toggle="popover" data-placement="bottom" 
							     data-content="" onclick="showGroupDialog(this)">+添加组合</button>
							     <div class="popover fade bottom in" style="top: 30px; left: -145px; display: none;">

								    <div class="arrow"></div>
								    <h3 class="popover-title" style="display: none;"></h3>
								    <div class="popover-content">
								        <div class="tableOrdiah-detail-box"></div>
								        <div class="tableOrdiah-detail-box"></div>
								        <div class="tableOrdiah-detail-box"></div>
								    </div>
								
								</div>
								<font color="red" id="print-groupdishes-add_tip" class="error"></font>
							</div>
						</div>		
								
						<div class="btn-operate  " id="add-btn">
								<button class="btn btn-cancel in-btn135" type="button" data-dismiss="modal" >取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save in-btn135 " id="submitButton" type="submit" >确认</button>
						
						</div>
					</form>
				</div>
				

			</div>
			
		</div>
	</div>
	<!--删除弹出框-->
	<div class="modal fade dialog-sm in"  data-backdrop="static" id="printConfig-del-dialog" >
		<div class="modal-dialog">
			<div class="modal-content">				
				<div class="modal-body">
					<div class="dialog-sm-header">				  
				        <span>确认删除</span>
				        <img src="../images/close-sm.png" class="img-close"  data-dismiss="modal">
			  	 	 </div>				
					<form action="" method="post" class="form-horizontal " name="">
						<div class="dialog-sm-info">
							<p class="p1"><img src="../images/del-tip.png">确认删除该打印配置吗？</p>	
						</div>
						<div class="btn-operate">
							<button class="btn btn-cancel" type="button" data-dismiss="modal">取消</button>
							<div  class="btn-division"></div>
							<button class="btn btn-save" id="deletePrinterButton" type="button" onclick="deletePrinter()">确认</button>
						</div>

					</form>
				</div>
			</div>
		</div>
	</div>
	<!--确认打印餐台弹出框-->
	<div class="modal fade dialog-sm in"  data-backdrop="static" id="printTables-confirm-dialog" >
		<div class="modal-dialog">
			<div class="modal-content">				
				<div class="modal-body">
					<div class="dialog-sm-header">				  
				        <span>确认删除</span>
				        <img src="../images/close-sm.png" class="img-close"  data-dismiss="modal">
			  	 	 </div>				
					<form action="" method="post" class="form-horizontal " name="">
						<div class="dialog-sm-info">
							<p class="p1"><img src="../images/del-tip.png">确认删除该打印配置吗？</p>	
						</div>
						<div class="btn-operate">
							<button class="btn btn-cancel" type="button" data-dismiss="modal">取消</button>
							<div  class="btn-division"></div>
							<button class="btn btn-save" id="confirmPrintTables" type="button" onclick="changeIPFlag()">确认</button>
						</div>

					</form>
				</div>
			</div>
		</div>
	</div>



		<!--打印区域弹出框-->
		<div class="modal fade " data-backdrop="static" id="printArea-add-dialog">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-body">
						<div class="ky-print-title">
							
							<span>打印区域 <font >(已选<font id="table-count">0</font>个餐台 )</font></span>
							<span class="ky-print-empty">
								<label class="radio-inline">
									<input type="radio" name="table-radio-check" id="table-radio-check" value="1" >全选</label>
								<label class="radio-inline">
									<input type="radio" name="table-radio-uncheck" id="table-radio-uncheck" value="0" >全不选</label>
							</span>
							<img src="../images/close.png" class="img-close" data-dismiss="modal">

						</div>
						<hr class="ky-hr">
						<div class="ky-print-content" style="height: 400px;overflow: auto;">
							<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">

							</div>
						</div>
						<div class="ky-print-footer">
							<div class="btn-operate">
								<button class="btn btn-cancel in-btn135" data-dismiss="modal">取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save in-btn135" id="print-area-confirm">确认</button>
							</div>
						</div>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
		<!-- /.modal-dialog -->
		</div>
		
		<!--打印菜品弹出框-->
		<div class="modal fade " data-backdrop="static" id="printDishes-add-dialog">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-body">
						<div class="ky-print-title">
							<span>选择菜品 <font >(已选<font id="dish-count">0</font>个菜品 )</font></span>
							<span class=" ky-print-empty">
								<label class="radio-inline">
									<input type="radio" name="selectRadio" id="dishes-radio-check" value="" >全选</label>
								<label class="radio-inline">
									<input type="radio" name="selectRadio" id="dishes-radio-uncheck" value="" >全不选</label>
							</span>
							<img src="../images/close.png" class="img-close" data-dismiss="modal">

						</div>
						<hr class="ky-hr">
						<div class="ky-print-content" style="height: 400px;overflow: auto;">
							<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">

							</div>
						</div>
						<div class="ky-print-footer">
							<div class="btn-operate">
								<button class="btn btn-cancel in-btn135" data-dismiss="modal">取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save in-btn135" id="print-dishes-confirm">确认</button>
							</div>
						</div>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
		<!-- /.modal-dialog -->
		</div>
		
		<!--菜品组合弹出框-->
		<div class="modal fade " data-backdrop="static" id="printGroup-add-dialog">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-body">
						<div class="ky-print-title">
							<span>选择组合<font >(已选<font id="group-count">0</font>个菜品 )</font></span>
							<span class=" ky-print-empty">
								<label class="radio-inline">
									<input type="radio" name="selectRadio" id="group-radio-check" value="" >全选</label>
								<label class="radio-inline">
									<input type="radio" name="selectRadio" id="group-radio-uncheck" value="" >全不选</label>
							</span>
							<img src="../images/close.png" class="img-close" data-dismiss="modal">

						</div>
						<hr class="ky-hr">
						<div class="ky-print-content" style="height: 400px;overflow: auto;">
							<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">

							</div>
						</div>
						<div class="ky-print-footer">
							<div class="btn-operate">
								<button class="btn btn-cancel in-btn135" data-dismiss="modal">取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save in-btn135" id="print-groupdishes-confirm">确认</button>
							</div>
						</div>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
		<!-- /.modal-dialog -->
		</div>

</body>

</html>