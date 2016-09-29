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
	<script src="../scripts/projectJs/posprint.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/jquery.validate.js"></script>
	<script type="text/javascript">
		var global_Path = '<%=request.getContextPath()%>';
	</script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/messages_zh.js"></script>
</head>
<body>
	<div class="ky-contenter">
		<div class="print-content" id="print-content">
			<button class="printConfig-btn btn btn-default"
				id="pos-printConfig-add" type="button">
				<i class="icon-plus"></i> 新增POS
			</button>
		</div>
	</div>
	<!--添加POS -->
	<div class="modal fade printConfig-add-dialog in "
		id="pos-printConfig-add-dialog" data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<div class="modal-title">新增POS</div>
					<img src="../images/close.png" class="img-close"
						data-dismiss="modal">
				</div>
				<div class="modal-body">
					<form action="" id="add-form-posprintConfig" method="post"
						class="form-horizontal " name="">
						<input type="hidden" id="deviceid" name="deviceid" />
						<div class="form-group">
							<label class="col-xs-5 control-label "><span
								class="required-span">*</span>POS ID：</label>
							<div class="col-xs-7">
								<input type="text" value="" class="form-control required"
									name="posid" id="posid" maxlength="15">
								<font color="red" id="posid_tip" class="error"></font>
							</div>

						</div>

						<div class="form-group">
							<label class="col-xs-5 control-label"><span
								class="required-span">*</span>POS 名称：</label>
							<div class="col-xs-7">
								<input type="text" value="" class="form-control  required"
									name="posname" id="posname"> 
								<font color="red" id="posname_tip" class="error"></font>
							</div>
						</div>
						<!--已选择内容的样式-->
						<div class="form-group" id="print-area">
							<label class="col-xs-5 control-label "><span
								class="required-span">*</span>配置打印机：</label>
							<div class="col-xs-7" id="div-printer-add">
								<button type="button" class="btn btn-default required "
									data-html="true" title="" id="printer-add"
									data-placement="bottom" rel="popover"></button>
								<font color="red" id="printer-add_tip" class="error"></font>
							</div>
						</div>
						<div class="btn-operate  " id="add-btn">
							<button class="btn btn-cancel in-btn135" type="button"
								data-dismiss="modal">取消</button>
							<div class="btn-division"></div>
							<button class="btn btn-save in-btn135 " id="submitButton"
								type="submit">确认</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<!--删除弹出框-->
	<div class="modal fade dialog-sm in" data-backdrop="static"
		id="posprint-del-dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<div class="dialog-sm-header">
						<span>确认删除</span> <img src="../images/close-sm.png"
							class="img-close" data-dismiss="modal">
					</div>
					<form action="" method="post" class="form-horizontal " name="">
						<div class="dialog-sm-info">
							<p class="p1">
								<img src="../images/del-tip.png">确认删除该POS机吗？
							</p>
						</div>
						<div class="btn-operate">
							<button class="btn btn-cancel" type="button" data-dismiss="modal">取消</button>
							<div class="btn-division"></div>
							<button class="btn btn-save" id="deletePrinterButton"
								type="button" onclick="deletePosPrinter()">确认</button>
						</div>

					</form>
				</div>
			</div>
		</div>
	</div>
	<!--配置打印机弹出框-->
	<div class="modal fade " data-backdrop="static" id="printer-add-dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<div class="ky-print-title">

						<span>配置打印机 <font>(已选<font id="table-count">0</font>个打印机
								)
						</font></span> <span class="ky-print-empty"> <label class="radio-inline">
								<input type="radio" name="table-radio-check"
								id="table-radio-check" value="1">全选
						</label> <label class="radio-inline"> <input type="radio"
								name="table-radio-uncheck" id="table-radio-uncheck" value="0">全不选
						</label>
						</span> <img src="../images/close.png" class="img-close"
							data-dismiss="modal">

					</div>
					<hr class="ky-hr">
					<div class="ky-print-content"
						style="height: 400px; overflow: auto;">
						<div class="panel-group" id="accordion" role="tablist"
							aria-multiselectable="true">
							<div class='panel panel-default'>
								<div class='panel-collapse collapse in' role='tabpanel' >
									<div class='panel-body'>
									</div>
								</div>
							</div>	
						</div>
					</div>
					<div class="ky-print-footer">
						<div class="btn-operate">
							<button class="btn btn-cancel in-btn135" data-dismiss="modal">取消</button>
							<div class="btn-division"></div>
							<button class="btn btn-save in-btn135" id="print-area-confirm">确认</button>
						</div>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!--提示弹出框-->
	<div class="modal fade dialog-sm in" data-backdrop="static"
		id="tips-dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<div class="dialog-sm-header">
						<span>提示</span> <img src="../images/close-sm.png"
							class="img-close" data-dismiss="modal">
					</div>
					<form action="" method="post" class="form-horizontal " name="">
						<div class="dialog-sm-info">
							<p class="p1">
								<span id="tips-msg"></span>
							</p>
						</div>
						<div class="btn-operate">
							<button class="btn btn-save" type="button"  data-dismiss="modal">确认</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>