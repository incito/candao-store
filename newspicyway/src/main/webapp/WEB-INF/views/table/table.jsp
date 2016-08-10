<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
	<link href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css">

	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/counter.css?v=1">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/font-awesome/css/font-awesome.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/switchery/style.css"/>
	<script src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
	<script src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/index.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/tables.js?v=b"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/commons.js"></script>
<script type="text/javascript">
	var global_Path = '<%=request.getContextPath()%>';
</script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/jquery.validate.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/messages_zh.js"></script>

</head>
<body>
		<div class="ky-content content-iframe">
			<div class="counter-content">
				<p class="counter-content-title">
					<button class="btn btn-default counter-type-add counter-add"  type="button" id="counter-type-add" onclick="addArea()" ><i class="icon-plus"></i> 餐厅分区</button>
				</p>

					<div class="nav-counter-prev"  style="display: none"><i class="icon-chevron-left"></i></div>

				<ul class="nav-counter" id="nav-tables" >
					<c:forEach var="item" items="${areanames}" varStatus="i">
						<c:if test="${i.index==0}">
						<li id="${item.areaid}" class="active" onmousedown="doMenu(event,this)" onmouseover="delDisplay(this)" onmouseout="delHidden(this)"
						onclick="oneclickTableType(this.id)" ondblclick="editArea(this.id)" >
						<span>${item.areaname}</span><span>(${item.tableCount})</span>
						<i class="icon-remove hidden"  onclick="showDeleteArea(this.id)"></i>
						</li>
						</c:if>
						<c:if test="${i.index!=0}">
							 <li id="${item.areaid}" class="" onmouseover="delDisplay(this)" onmouseout="delHidden(this)"
	 						onclick="oneclickTableType(this.id)" onmousedown="doMenu(event,this)"  ondblclick="editArea(this.id)" >
							<span>${item.areaname}</span><span>(${item.tableCount})</span>
							<i class="icon-remove hidden"  onclick="showDeleteArea()"></i>
							</li>

						</c:if>

					</c:forEach>
				</ul>
					<div class="nav-counter-next"  style="display: none"><i class="icon-chevron-right"></i></div>
				<ul class="tables-right-tab right-tab hidden">
					<li id="tables-right-tab1" onclick="addArea()"><i class="icon-plus"></i><span>添加分区</span></li>
					<li id="tables-right-tab2" onclick="editArea()"><i class="icon-edit"></i><span>编辑分区</span></li>
					<li id="tables-right-tab3" onclick="showDeleteArea()"><i class="icon-minus"></i><span>删除分区</span></li>
				</ul>
				<div class="nav-counter-tab">

					<c:forEach var="item" items="${datas}" varStatus="i">
					<div class="counter-detail-box" id="${item.tableid}" onmouseover="delDisplay(this)" onmouseout="delHidden(this)" >
						<p>${item.tableName }</p>
						<p>(${item.personNum }人桌)</p>
						<i class="icon-remove hidden" onclick="delTablesDetail(${item.tableid },&apos;${item.tableName }&apos;,event)"></i>
					</div>
					</c:forEach>

					<button class="btn btn-default counter-add"  type="button" id="tables-detailMain-Add"><i class="icon-plus"></i> 添加餐台</button>
				</div>

			</div>
		</div>
		<!--添加餐台详情框 -->
			<div class="modal fade counter-dialog in " id="tables-detailAdd-dialog"  data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-body">
						<div class="counter-dialog-header">
					        <span id="editTitle2">添加餐台</span>
					        <img src="<%=request.getContextPath()%>/images/close.png" class="img-close"  data-dismiss="modal">
					    </div>
					    <hr class="ky-hr">
						<form action="" method="post" class="form-horizontal " name="add-form1" id="add-form1" >

									<input type="hidden"  name="tableid" id="tableid" >
									<div class="form-group" style="display:none;">
										<label class="col-xs-5 control-label "><span class="required-span">*</span>餐台编号：</label>
										<div class="col-xs-8">
											<input type="text" name="tableNo" id="tableNo" maxlength="200"	 class="form-control required">
											<font color="red" id="tableNo_tip" class="error"  ></font>
										</div>
									</div>


							<div class="form-group">
								<label class="col-xs-4 control-label"><span class="required-span">*</span>餐台名称：</label>
								<div class="col-xs-7">
									<input type="text" name="tableName"  id="tableName" maxlength="5" 	 class="form-control  ">
											<font color="red" id="tableName_tip" class="error"></font>
								</div>
							</div>
							<div class="form-group">
								<label class="col-xs-4 control-label"><span class="required-span">*</span>餐台类型：</label>
								<div class="col-xs-7">
									<select class="form-control myInfo-select-addrW tabletype" id="tabletype" name="tabletype">

										</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-xs-4 control-label"><span class="required-span">*</span>就餐人数：</label>
								<div class="col-xs-7">
									<div class="input-group  unit-style">
										  <input type="text" name="personNum" id="personNum"     maxlength="3" onkeyup="clearNoNum(this);"  class="form-control "></input>
										  <span class="input-group-addon" style="color: #282828;">人</span>

										</div>
										<font color="red" id="personNum_tip" class="error"></font>
								</div>
							</div>
							<div class="form-group" style="display:none;">
										<input id="areaid" name="areaid" />
							</div>
							<div class="form-group disabled-input counter-input-select">
								<label class="col-xs-4 control-label"><input type="checkbox" name="enableCheck" id="minp" onclick="checkit(this.checked,this.id)" ><span class="minpCheckboxSpan">最低消费：</span></label>
								<div class="col-xs-7">
									<div class="input-group   unit-style">

										  <input type="text" name="minprice" id="minprice"   aria-describedby="basic-addon1"      maxlength="8" onkeyup="oneDecimal(this,2,8,99999.99);"  class="form-control "></input>
										   <span class="input-group-addon">元</span>

									</div>
									<font color="red" id="minConsu_tip" class="error">必须大于0</font>
								</div>
							</div>
							<div class="form-group disabled-input counter-input-select">
								<label class="col-xs-4 control-label"><input type="checkbox"  id="fixp" onclick="checkit(this.checked,this.id)" name="enableCheck"><span class="fixpCheckboxSpan">固定使用费：</span></label>
								<div class="col-xs-7">
									<div class="input-group  unit-style ">
										  <input type="text" name="fixprice" id="fixprice"    aria-describedby="basic-addon1"    maxlength="8" onkeyup="oneDecimal(this,2,8,99999.99);"  class="form-control "></input>
										   <span class="input-group-addon">元</span>

									</div>
									<font color="red" id="fixConsu_tip" class="error">必须大于0</font>
								</div>
							</div>


							<div class="btn-operate">
								<button class="btn btn-cancel in-btn135" type="button" onclick="hideDialog()" >取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save in-btn135" id="btnsave1" >确认</button>
							</div>
						</form>
					</div>


				</div>

			</div>
		</div>

		<!--删除弹出框-->
		<div class="modal fade counter-dialog dialog-sm in counter-dialog-del" id="tables-detailDel-dialog"  data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-body">
						<div class="dialog-sm-header">
					        <span>确认删除</span>
					        <img src="<%=request.getContextPath()%>/images/close-sm.png" class="img-close"  data-dismiss="modal">
					    </div>
					    <hr class="ky-hr">
						<form action="" method="post" class="form-horizontal " name="">
						<input id="showTableId" value="" type="hidden">
							<div class="del-sm-info">
								<p class="counter-del-p1"><img src="../images/del-tip.png">确认删除该餐台？</p>
							</div>
							<div class="btn-operate">
								<button class="btn btn-cancel" type="button" data-dismiss="modal">取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save" id="counter-type-del" type="button" onclick="del()">确认</button>
							</div>

						</form>
					</div>
				</div>
			</div>

</div>


 <div class="modal fade counter-dialog in counter-dialog-del " id="areas-detailDel-dialog"  data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-body">
						<div class="counter-dialog-header">
					        <span>确认删除</span>
					        <img src="<%=request.getContextPath()%>/images/close-sm.png" class="img-close"  data-dismiss="modal">
					    </div>
					    <hr class="ky-hr">
						<form action="" method="post" class="form-horizontal " name="">
						<input id="showTableTypeId" value="" type="hidden"/>
							<div class="del-info">
								<p class="counter-del-p1"><img src="../images/del-tip.png"></i>确认删除该分区？</p>
							</div>
							<div class="btn-operate">
								<button class="btn btn-cancel" type="button" data-dismiss="modal">取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save" id="counter-type-del2" type="button" onclick="delAreaAndTables()">确认</button>
							</div>

						</form>
					</div>
				</div>
			</div>
		</div>


 		<div class="modal fade counter-dialog in" id="areas-detailAdd-dialog"  data-backdrop="static">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-body">
						<div class="counter-dialog-header">
					        <span id="editTitle1">添加分区</span>
					        <img src="<%=request.getContextPath()%>/images/close-sm.png" class="img-close"  data-dismiss="modal">
					    </div>
					    <hr class="ky-hr">
						<form action="" method="post" class="form-horizontal " name="" id="add-form2" >
						<input type="hidden" id="areaidB" name="areaNoB" />
							<div class="form-group">
								<label class="col-xs-4 control-label">分区名称：</label>
								<div class="col-xs-8">
									<input type="text" name="areanameB" id="areanameB" maxlength="5"	 class="form-control ">
											<font color="red" id="areanameB_tip" class="error"></font>
								</div>
 							</div>

							<div class="btn-operate">
								<button class="btn btn-cancel" type="button"  onclick="hideDialog()">取消</button>
								<div  class="btn-division"></div>
								<button class="btn btn-save"  id="btnsave2" >确认</button>
							</div>
						</form>
					</div>


				</div>

			</div>
		</div>




		<script>
		var count=0;
		$('.tables-roll-left').click(function(){
			var marginl = -147.5*(++count)+'px';
			$('.tables-comboList:eq(0)').css('margin-left',marginl);
		});
		$('.tables-roll-right').click(function(){
			var marginl = -147.5*(--count)+'px';
			$('.tables-comboList:eq(0)').css('margin-left',marginl);
		});


		function clearNoNum(obj) {
		    // 先把非数字的都替换掉，除了数字和.
		    obj.value = obj.value.replace(/[^\d]/g,"");
		}


		function oneDecimal(obj,num,maxlength,maxvalue) {

		    // 先把非数字的都替换掉，除了数字和.
		    obj.value = obj.value.replace(/[^\d.]/g,"");
		    // 必须保证第一个为数字而不是.
		    obj.value = obj.value.replace(/^\./g,"");
		    // 保证只连续出现一个.而没有多个.
		    //obj.value = obj.value.replace(/\.{2,}/g,".");
		    // 保证.只出现一次，而不能出现两次以上
		    obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
		    if (num !=undefined && num != null && num != "") {
		    	if (num == "2") {
		    		// 小数点后只保留3位小数
		            obj.value = obj.value.replace(/(\.\d\d)\d+/ig,"$1");
		    	} else if (num == "3") {
		    		// 小数点后只保留3位小数
		            obj.value = obj.value.replace(/(\.\d\d\d)\d+/ig,"$1");
		    	}
		    } else {
		    	// 小数点后只保留一位小数
		        obj.value = obj.value.replace(/(\.\d)\d+/ig,"$1");
		    }

		    if(parseFloat(obj.value)>maxvalue){
		    	//obj.value=maxvalue;
		    	obj.value=obj.value.substring(0,5);
		    	//$(obj).parent().find("label").show().delay(4000).hide(500);
		    }

		    // 字符串长度最大为maxlength
		    if (maxlength !=undefined && maxlength != null && maxlength != "") {
		    	if (obj.value.length > maxlength) {
		    		obj.value = obj.value.substr(0,maxlength);
		    	}
		    }
		}


		</script>
</body>
</html>