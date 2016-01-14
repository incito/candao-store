<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>礼品券</title>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/preferential.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/font-awesome/css/font-awesome.css">

	<script src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
	<script src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/index.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/preferential.js"></script>
</head>
<body>
		<div class="ky-content preferential-content">
			<p class="preferential-class">
				<img src="../images/preferential-type4.png"/>
				<span class="preferential-name giftCoupon-name">礼品券</span>
				<span class="preferential-desc">为顾客提供消费赠品服务（例：整单满200送1.25L雪碧）</span>
			<p>
			<div class="ky-panel">
				<div class="ky-panel-title">
					券面设置
					
				</div>
				<div class="ky-panel-content ">
					
					<div class="form-group">
						<label class="control-label col-xs-2">活动名称：</label>
						<div class="col-xs-4">
							<div style="position:relative;">
								<input type="text" class="form-control" placeholder="最多15个字"/>
							</div>
						</div>
						<label class="control-label col-xs-2">卡券颜色：</label>
						<div class="col-xs-2">
							<div class="input-group" id="color-select">
								  <input type="text" class="form-control" readonly="readonly" id="color-input" aria-describedby="basic-addon1" >
								   <span class="input-group-addon arrow-down"><i class="icon-chevron-down"></i></span>
							
							</div>	
							 <div class="color-select-box hidden">
						   		<div class="row">
						   			<div class="col-xs-4">
						   				<span id="color1"></span>
						   			</div>
						   			<div class="col-xs-4">
						   				<span id="color2"></span>
						   			</div>
						   			<div class="col-xs-4">
						   				<span id="color3"></span>
						   			</div>
						   		</div>
						   		 <div class="row">
						   			<div class="col-xs-4">
						   				<span id="color4"></span>
						   			</div>
						   			<div class="col-xs-4">
						   				<span id="color5"></span>
						   			</div>
						   			<div class="col-xs-4">
						   				<span id="color6"></span>
						   			</div>
						   		</div>
						   		<div class="row">
						   			<div class="col-xs-4">
						   				<span id="color7"></span>
						   			</div>
						   			<div class="col-xs-4">
						   				<span id="color8"></span>
						   			</div>
						   			<div class="col-xs-4">
						   				<span id="color9"></span>
						   			</div>
						   		</div>
						   	
						   </div>
		
						</div>	
						<div class="col-xs-2  couponColor-remark"><span>卡券颜色将显示在收银端，便于结算时选择优惠</span></div>
			
						
					</div>
			
			
				</div>
			</div>
			<div class="ky-panel">
				<div class="ky-panel-title">
					活动内容设置
					
				</div>
				<div class="ky-panel-content ">
					<div class="form-group" style="position:relative">
						<label class="control-label col-xs-2">赠送菜品：</label>
						<div class="col-xs-4">				
							<div class="input-group select-box">
								<input type="text" class="form-control" aria-describedby="basic-addon1">
								<span class="input-group-addon arrow-down" id="basic-addon1"><i class="icon-chevron-down"></i></span>
							</div>		
							  <div class="select-content hidden">
					               <span class="select-content-detail">土贡梅煎</span><br/>
					               <span class="select-content-detail">22222222222222222</span><br/>
					          </div>			
						</div>				
				
					</div>
					<div class="form-group">
						<label class="control-label col-xs-2">有效期：</label>
						<div class="col-xs-2">
							<div class="input-group">
								  <input type="text" class="form-control" aria-describedby="basic-addon1">
								   <span class="input-group-addon arrow-down" id="basic-addon1"><i class="icon-chevron-down"></i></span>

							</div>								
						</div>
						<div class="col-xs-2">
							<div class="input-group">
								  <input type="text" class="form-control" aria-describedby="basic-addon1">
								   <span class="input-group-addon arrow-down" id="basic-addon1"><i class="icon-chevron-down"></i></span>

							</div>								
						</div>
						<label class="control-label col-xs-2">适用门店：</label>
						<div class="col-xs-3  store-radio-style">
							<label class="radio-inline ">
								<input type="radio" name="storeRadio" value="1">所有门店
							</label>
							<label class="radio-inline">
								<input type="radio" name="storeRadio" value="0">指定门店

							</label>
				
						</div>	
						<div class="col-xs-1  store-select hidden">
							<div class="input-group" id="store-select">
								  <input type="text" class="form-control" readonly="readonly" id="store-select-input" aria-describedby="basic-addon1">
								   <span class="input-group-addon arrow-down"><i class="icon-chevron-down"></i></span>

							</div>
						</div>
					</div>
			
						
		
			
				</div>
			</div>
			<div class="ky-panel">
				<div class="ky-panel-title">
					活动介绍					
				</div>
				<div class="ky-panel-content  no-margin">
					<textarea class="form-control">
						
					</textarea>
		
			
				</div>
			</div>
			<div class="ky-panel">
				<div class="ky-panel-title">
					使用须知					
				</div>
				<div class="ky-panel-content  no-margin ">
					<textarea class="form-control">
						
					</textarea>
		
			
			
				</div>
			</div>
			<div class="btn-operate">
				<button class="btn btn-cancel in-btn135">取消</button>
				<div  class="btn-division"></div>
				<button class="btn btn-save in-btn135 preferential-btn-bgcolor">确认</button>
			</div>
		</div>
		<div class="modal fade storeSelect-dialog in " id="store-select-dialog">
			<div class="modal-dialog">
				<div class="modal-content">	
					<div class="modal-body">
						<div class="row store-select-title">
							<div class="col-xs-9">选择门店（已选<font id="store-count">0</font>家店）</div>
							<div class="col-xs-3 pull-right">
								<label class="radio-inline">
									<input type="radio" value="1" name="store">全选
								</label>
								<label class="radio-inline">
									<input type="radio" value="0" name="store">全不选
								</label>
							</div>
						</div>
						<hr>
						<table class="table store-select-content">
							<tr>
								<td><input type="checkbox"><span>北京新辣道新会点新会店新会店</span></td>
								<td><input type="checkbox"><span>北京新辣道新会点新会店新会店</span></td>
								<td><input type="checkbox"><span>北京新辣道新会点新会店新会店</span></td>
								<td><input type="checkbox"><span>北京新辣道新会点新会店新会店</span></td>
							</tr>
							<tr>
								<td><input type="checkbox"><span>北京新辣道新会点新会店新会店</span></td>
								<td><input type="checkbox"><span>北京新辣道新会点新会店新会店</span></td>
								<td><input type="checkbox"><span>北京新辣道新会点新会店新会店</span></td>
								<td><input type="checkbox"><span>北京新辣道新会点新会店新会店</span></td>
							</tr>
							<tr>
								<td><input type="checkbox"><span>北京新辣道新会点新会店新会店</span></td>
								<td><input type="checkbox"><span>北京新辣道新会点新会店新会店</span></td>
								<td><input type="checkbox"><span>北京新辣道新会点新会店新会店</span></td>
								<td><input type="checkbox"><span>北京新辣道新会点新会店新会店</span></td>
							</tr>
				
				
						</table>
						<div class="btn-operate">
							<button class="btn btn-cancel in-btn135">取消</button>
							<div  class="btn-division"></div>
							<button class="btn btn-save in-btn135 preferential-btn-bgcolor" id="store-select-confirm">确认</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<script type="text/javascript">
			$(parent.document.all("detail")).height(document.body.scrollHeight-130); 
			$(parent.document.all("allSearch")).css("visibility","hidden");
		</script>
</body>
</html>
