<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>优惠管理-特价券</title>
	<meta name="description" content="">
	<meta name="keywords" content="">
	<link href="<%=request.getContextPath()%>/tools/bootstrap/css/bootstrap.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/preferential.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/tools/font-awesome/css/font-awesome.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/json2.js"></script>
	<%@ include file="/common/resource.jsp" %>  

	<script src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
	<script src="<%=request.getContextPath()%>/tools/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/scripts/projectJs/index.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/jquery.validate.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/messages_zh.js"></script>
	<script src="<%=request.getContextPath()%>/tools/calendar_diy/WdatePicker.js"></script>
	<script src="<%=request.getContextPath()%>/tools/jquery-validation/validate-plus.js"></script>
	<style type="text/css">
		.add-dish-select{
			border: 1px solid #ccc;
		    cursor: pointer;
		    height: 32px;
		    padding-bottom: 2px;
		    padding-top: 2px;
		    text-align: center;
		    vertical-align: middle;
		    width: 368px;
		}
		.add-dish-select img{
			height: 18px;
		    margin-top: 4px;
		    width: 18px;
		}
		.p_0.isPrice {
			width:60px;
			height: 23px;
		}
		/* .selected-dish-list div{
			background-color:#f8f8f8;
		} */
		.selected-dish-list{
			border-bottom: 1px solid #dfdfdf;
		}
		
		.selected-dish-list .p_0{
			padding-top: 0px;
			padding-bottom: 0px;
			margin-top:-2px;
		}
		.selected-dish-list .glyphicon{
			margin-right:25px;
			top:0px;
		}
		.selected-dish-list .col-xs* .input-group{
			height:30px;
		}
		.selected-dish-list .input-group-addon{
			height:23px;
		}
		.selected-dish-list .h24{
			/* height:30px; */
			padding-left: 15px;
			font-size:12px;
		}
		
		
		/** 操作按钮移动到上面方才显示 **/
		.selected-dish-list .operate{
			height:30px;
			display: none;
			min-width: 100px
		}
		
		 .selected-dish-list:HOVER  div{
			background-color: rgb(215,215,215)
		}
		.selected-dish-list:HOVER .operate{
			display: block;
		} 
		
		.selected-dish-list .clear{
			display:none;
		}
		.selected-dish-list .input-group-addon{
			width:35%;
			text-align: left;
		}
		
		
		.ky-addDishes-empty{
			float: right;
			margin-top: 0;
			margin-bottom: 0;
		}
		.ky-addDishes-content .checkbox-inline{
			margin:0;
		}
		.ky-addDishes-content .panel-heading{
			padding-top: 5px;
			padding-bottom: 5px;
		}
		.ky-addDishes-content .panel-title{
			font-size: 14px;
		}
		.ky-addDishes-content .dish-label{
			font-size:12px;
		}
		.store-count{
			color:red;
		}
		
		#dish-selected-list {
			border:1px solid #dfdfdf;
			padding-left: 0;
    		padding-right: 0;
    		margin-left: 15px;
    		line-height: 30px;
    		color:#505050;
		}
		#dish-selected-list .selected-dish-list:last-child{
			border-bottom:0;
		}
	</style>

</head>
<body>
	<div class=" ky-content preferential-content">
		<form action="" method="post"  class=" " name="add-form" id="add-form">
			<input id="id" name="id" type="hidden" value="${specialStampVO.activity.id }" />
			<input id="color" name="color"  type="hidden" value="${specialStampVO.activity.color }" />
			<input id="selectBranchs" name="selectBranchs"  type="hidden" value=""/> 
			<p class="preferential-class">
				<img src="<%=request.getContextPath()%>/images/preferential-type1.png" /> <span
					class="preferential-name specialStamp-name">特价券</span> <span
					class="preferential-desc">为某一菜品提供特价服务（例：川味香肠特价8元）</span>
			<p>
			<div class="ky-panel">
				<div class="ky-panel-title">券面设置</div>
				<div class="ky-panel-content ">
					<div class="form-group">
						<label class="control-label col-xs-2">活动名称：</label>
						<div class="col-xs-4">
							<div style="position:relative;">
								<input type="text" class="form-control" name="name" id="name" value="${specialStampVO.activity.name }" placeholder="最多15个字"  maxlength="15"/>
							</div>
						</div>
						<label class="control-label col-xs-2">卡券颜色：</label>
						<div class="col-xs-2">
							<div class="input-group" id="color-select">
								<input type="text" class="form-control" readonly="readonly" 
									id="color-input" name="colorinput" aria-describedby="basic-addon1" style="background-color:${specialStampVO.activity.color};"> <span
									class="input-group-addon arrow-down"><i
									class="icon-chevron-down"></i></span>
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
						<div class="col-xs-2 couponColor-remark">
							<span>卡券颜色将显示在收银端,便于结算时选择优惠。</span>
						</div>
					</div>
				</div>
			</div>
			<div class="ky-panel">
				<div class="ky-panel-title">活动内容设置</div>
				<div class="ky-panel-content ">
					<div class="form-group" style="${null==specialStampVO.stamps || specialStampVO.stamps.size()<1 ?'':'display:none'}">
						<label class="control-label col-xs-2">特价菜品：</label>
						<div class="col-xs-4">
							<div class="add-dish-select">
								<img alt="" src="<%=request.getContextPath()%>/images/add.png" width="18" height="18" />
							</div>
						</div>
					</div>
					<div class="form-group" style="${null!=specialStampVO.stamps && specialStampVO.stamps.size()>0 ?'':'display:none'} ">
						<label class="control-label col-xs-2">特价菜品：</label>
						<div class="col-xs-9"  id="dish-selected-list">
							<c:forEach items="${specialStampVO.stamps }" var="stamp">
								<div class="selected-dish-list clearfix">
								<c:choose>
									<c:when test='${(stamp.unit==null or stamp.unit=="") or (stamp.unitflag==true or stamp.unitflag=="true") }'>
										<div class="col-xs-6 h24">${stamp.dish_title }</div>
										<div class="input-group col-xs-3 h24">
									    	<input class="form-control p_0 isPrice h24" name="${stamp.dish }" dish-id="${stamp.dish }" data-dish="${stamp.dish }"  data-dish-title="${stamp.dish_title}" value="${stamp.price}" unit="${stamp.unit}"  unitflag="${stamp.unitflag}" type="text">
									    	<span class=" " style="padding-top:0px;padding-bottom:0px;height:auto" id="basic-addon1"> 元/${stamp.unit}</span>
									    </div>
									</c:when>
									<c:otherwise>
										<div class="col-xs-6 h24">${stamp.dish_title}(${stamp.unit})</div>
										<div class="input-group col-xs-3 h24" >
									    	<input class="form-control p_0 isPrice" name="${stamp.dish }(${stamp.unit})" dish-id="${stamp.dish }" data-dish="${stamp.dish }(${stamp.unit})"  data-dish-title="${stamp.dish_title}" value="${stamp.price}" unit="${stamp.unit}" unitflag="${stamp.unitflag}" type="text">
									    	<span class=" " style="padding-top:0px;padding-bottom:0px;height:auto" id="basic-addon1"> 元/${stamp.unit}</span>
									    </div>
									</c:otherwise>
								</c:choose>
								  	
								    <div class="col-xs-3 h24" style="text-align:left;padding:0px 10px; height:30px">
								    	<div class="operate"> 
									    	<span class="glyphicon glyphicon-plus" aria-hidden="true" onclick="loadDish()"></span>
									        <span class="glyphicon glyphicon-remove" aria-hidden="true" onclick="deleteDish(this)"></span>
									        <div class="clear"></div>
								        </div>
								    </div>
								</div>
								 
							</c:forEach>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-xs-2">有效期：</label>
						<div class="col-xs-2">
							<div class="input-group">
								<input name="starttime" id="starttime" readOnly onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'endtime\')}',onpicked:picked})" class="form-control bottom" type="text"
									aria-describedby="basic-addon1" value=" <fmt:formatDate value='${specialStampVO.activity.starttime }'  pattern='yyyy-MM-dd'/>" >
									<span class="input-group-addon arrow-down" id="basic-addon1" >
										<i class="icon-chevron-down"></i>
									</span>
							</div>
						</div>
						<label class="time-label col-xs-2">—</label>
						<div class="col-xs-2">
							<div class="input-group">
								<input name="endtime" id="endtime" readOnly onFocus="WdatePicker({minDate:'#F{$dp.$D(\'starttime\')}',onpicked:picked})" class="form-control bottom" type="text" class="form-control"
									aria-describedby="basic-addon1" value=" <fmt:formatDate value='${specialStampVO.activity.endtime }'  pattern='yyyy-MM-dd'/>" > <span
									class="input-group-addon arrow-down" id="basic-addon1"><i
									class="icon-chevron-down"></i></span>

							</div>
						</div>
						<label class="control-label col-xs-2">适用门店：</label>
						<div class="col-xs-3  store-radio-style">
							<label class="radio-inline "> <input type="radio"
								name="applyAll" value="1" checked="checked">所有门店
							</label> <label class="radio-inline"> <input type="radio"
								name="applyAll" value="0">指定门店

							</label>

						</div>
						<div class="col-xs-1  store-select hidden">
							<div class="input-group" id="store-select">
								<button type="button" class="btn btn-default store-select-add" id="addstore">
									<i class="icon-plus"></i>选择门店
								</button>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="ky-panel">
				<div class="ky-panel-title">活动介绍</div>
				<div class="ky-panel-content  no-margin">
					<textarea name="activityIntroduction" id="activityIntroduction" class="form-control">${specialStampVO.activity.activityIntroduction }</textarea>
					
				</div>
			</div>
			<div class="ky-panel">
				<div class="ky-panel-title">使用须知</div>
				<div class="ky-panel-content  no-margin ">
					<textarea name="useNotice" id="useNotice" class="form-control">${specialStampVO.activity.useNotice }</textarea>
				</div>
			</div>
			<div class="btn-operate">
				<button class="btn btn-cancel in-btn135" id="btn_cancle_specialStamp">取消</button>
				<div class="btn-division"></div>
				<button type="submit" class="btn btn-save in-btn135 preferential-btn-bgcolor" >确认</button>
			</div>
		</form>
	</div>


	<!--点击按钮弹出添加门店界面 -->
	<div class="modal fade storeSelect-dialog in " id="store-select-dialog">
			<div class="modal-dialog">
				<div class="modal-content">	
					<div class="modal-header addDelicon">				  
				        <img data-dismiss="modal" class="img-close" src="/newspicyway/images/close.png">
				    </div>
					<div class="modal-body">
						<div class="row store-select-title">
							<div class="col-xs-9">选择门店<font id="store-count"></font></div>
							<div class="col-xs-3 pull-right">
								<label class="radio-inline">
									<input type="radio" value="1" name="store">全选
								</label>
								<label class="radio-inline">
									<input type="radio" value="0" name="store">全不选
								</label>
							</div>
						</div>
						<hr>
						<table class="table store-select-content">
						</table>
						<div class="btn-operate">
							<button class="btn btn-cancel in-btn135" data-dismiss="modal">取消</button>
							<div  class="btn-division"></div>
							<button class="btn btn-save in-btn135 preferential-btn-bgcolor" id="store-select-confirm">确认</button>
						</div>
					</div>
				</div>
			</div>
	</div>
	
	<div class="modal fade " id="dish-select-dialog" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header addDelicon">				  
				        <img data-dismiss="modal" class="img-close" src="/newspicyway/images/close.png">
				    </div>
				<div class="modal-body">
					<div class="ky-addDishes-title">
						<span style="font-size:16px">添加特价菜品<font id="dish-count"></font></span>
						<div class="col-xs-3 pull-right">
							<label class="radio-inline">
								<input type="radio" name="dish-radio-uncheck" value="1" class="checkAll">全选
							</label>
							<label class="radio-inline">
								<input type="radio" name="dish-radio-uncheck" value="0" class="checkAll">全不选
							</label>
						</div>
					</div>
					<hr class="ky-hr">
					<div class="ky-addDishes-content" style="height: 400px;overflow: auto;">
						<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
						</div>
					</div>
					<div class="ky-addDishes-footer">
						<div class="btn-operate">
							<button class="btn btn-cancel in-btn135" data-dismiss="modal">取消</button>
							<div  class="btn-division"></div>
							<button class="btn btn-save in-btn135 addDishes-btn-bgcolor" id="dish-select-confirm">确认</button>
						</div>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
	<div class="modal fade .dishes-detailDel-dialog in " id="successPrompt"  >
		<div class="modal-dialog" style="width:310px;">
			<div class="modal-content" style="width:310px;">	
				<div class="modal-body pop-div-content" style="height:210px;">
					<br/>
						<p class="tipP"> <i class="icon-ok"></i><label id="promptMsg">保存成功</label></p>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		
		//--begin 如果存在主键，那么需要处理还原已有的数据
	
		//-- end 
		$(".add-dish-select").hover(function(){
			$(this).find("img").attr("src",global_Path + "/images/addhover.png");
		}, function(){
			$(this).find("img").attr("src",global_Path + "/images/add.png");
		});
		$("#dish-selected-list").parent().height($("#dish-selected-list").height()); //防止在编辑优惠的时候，有优惠菜品的时候，会错乱布局
		
		$(document).ready(function() {
			
			$("#store-select").hover(function(){
				$(this).find(".popover").show();
			}, function(){
				$(this).find(".popover").hide();
			});
			
			$("#color-select").click(function(event){
				 $(".color-select-box").toggleClass("hidden");
			});


			$(".color-select-box span").click(function(e){
				$("#color-input").css("background-color",$(this).css("background-color"));
				$(".color-select-box").toggleClass("hidden");
				var rgb = $(this).css("background-color");
				rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
		       rgb= "#" + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]);
				$("#color").val(rgb);
				$("#color").parent().find("div.popover").remove();


			});
			function hex(x) {
				return ("0" + parseInt(x).toString(16)).slice(-2);
			}
			//绑定菜品选择
			$(".add-dish-select").click(function(){
				loadDish()
			});
			//如果是编辑 优惠券，还原门店信息
			reBuildBranchs();
			
			//页面取消按钮，跳转到列表页面
			$("#btn_cancle_specialStamp").click(function(){
				window.location.href=global_Path+ '/preferential';
				return false;
			});
			
			//提交表单的时候，进行数据的有效性检验
			$("#add-form").validate({
				submitHandler : function(form) {
					//TODO 需要完成校验功能;
					save_specialStamp();
				},

				rules : {
					name : {
						required : true,
						maxlength : 15,
					},
					amount : {
						required : true,
						number:true
					},
					storeselect : {
						required : true
					}
					
				},
				messages : {
					name : {
						required : "请输入活动名称",
						maxlength : "长度不能超过15",
					},
					amount : {
						required : "请输入减免金额",
						number: "请输入合法的数字"
					},
					starttime : {
						required : "请选择有效期开始日期",
					},
					storeselect : {
						required : "请选择指定门店"
					},
					endtime : {
						required : "请选择有效期结束日期",
					}
				}
			});
				
				/*选择指定门店时*/
				$("input[name='applyAll']").click(function(){
					if($(this).val()=='1'){
						$("div.store-select").addClass("hidden");
						$("#store-select-input").val("");
					}else{
						$("div.store-select").removeClass("hidden");
					}
			
				});
	
				// 保存特价票
				function save_specialStamp() {
					var stampInfo = {};
					stampInfo.activity={};// 存放优惠券信息
					stampInfo.stamps=[]; //存放优惠的菜品
					stampInfo.branchs=[];//存放选中的门店
					//获取填写的表单内容
					/*
					$("#add-form :text,#id,#add-form :hidden , #add-form textarea").each(
						function(index) {
						// console.log("each:"+$(this).attr("name"));
							if( undefined!=$(this).attr("name") &&  $(this).attr("name")!=""){
								stampInfo.activity["" + $(this).attr("name")+ ""] = $(this).val();
							}
						}
					);
					*/
					stampInfo.activity.id=$("#id").val();
					stampInfo.activity.name=$("#name").val();
					stampInfo.activity.color=$("#color").val() == "" ? "#9cc05b" : $("#color").val();
					stampInfo.activity.starttime=$("#starttime").val();
					stampInfo.activity.endtime=$("#endtime").val();
					stampInfo.activity.activityIntroduction=$("#activityIntroduction").val();
					stampInfo.activity.useNotice=$("#useNotice").val();
					
					//获取是否适用全部门面
					stampInfo.activity["applyAll"]=$("input[name=applyAll]:checked").val()=="1"?true:false;
					//如果不是 适用所有门店，则需要获取指定的门店列表。
					if( !stampInfo.activity["applyAll"] ){
						var allBranchs=$("#selectBranchs").val().split(",");
						$.each(allBranchs, function(i, obj) {
							if(obj != ""){
								stampInfo.branchs.push({"branch":obj});
							}
						});
					}
					
					//获取优惠的菜品
					var allDishs=$("#dish-selected-list").find("input");
					if(allDishs.length>0){
						var stampJson={};
						$.each(allDishs, function(i, obj) {
							
							stampJson={};
							stampJson.dish= $(obj).attr("dish-id");
							stampJson.dish_title=$(obj).attr("data-dish-title");
							stampJson.price= $(obj).val();
							stampJson.unit=$(obj).attr("unit");
							stampJson.unitflag=$(obj).attr("unitflag");
							stampJson.sequence=i;
							stampInfo.stamps.push(stampJson);
						})
					}
					
										
					
					$.ajax({
						url : global_Path+ "/preferential/specialStamp/save",
						type : "POST",
						dataType : "json",
						contentType : "application/json; charset=utf-8",
						data : JSON.stringify(stampInfo),
						//data:stampInfo,
						success : function(data) {
							if(data.result){
								$("#promptMsg").html("保存成功");
								$("#successPrompt").modal("show");
								window.setTimeout(function(){
									$("#successPrompt").modal("hide");
									window.location.href=global_Path+ '/preferential';
								}, 1000);
								
							}else {
								var str = '<label for="name" class="popover-errorTips" style="display: block;">'+data.message+'</label>';
								$("#name").addClass("popover-errorTips").after(str);
							/* 	if(data.message){
									$("#promptMsg").html(data.message);
								} else {
									$("#promptMsg").html("保存失败");
								}
								$("#successPrompt").modal("show");
								window.setTimeout(function(){
									$("#successPrompt").modal("hide");
								}, 1000); */
							}
						},
						error : function(XMLHttpRequest, textStatus,errorThrown) {
							alert(errorThrown);
						}
					});
				}
				
				//弹出的选择门店 相关代码
				/*弹框*/
				
				$("#store-select").click(function(){
					$("#store-select-dialog").modal("show");
					//从数据库加载所有门面
					$.getJSON(global_Path+"/shopMg/getall.json", function(json){
						$("table.store-select-content").html();
						var html="<tr>";
						$.each(json, function(i, obj) {
							html=html+" <td>"
								+"<input type='checkbox' value="+obj.branchid+"><span>"+obj.branchname+"</span>"
								+"</td>";
							if( (i+1)%4==0){//因为计数从0开始，所以要加一个才能显示正常
								html+="</tr><tr>"
							}
						});
						html=html+"</tr>"
						$("table.store-select-content").html(html);
						//如果当前已经有选择的门店，需要将选择的门店，重新在页面显示为选中的状态
						//if( $("#selectBranchs").val() != ""){
							$.each( $("#selectBranchs").val().split(","),function(i,obj){
								$("table.store-select-content input[type='checkbox'][value='"+obj+"']").prop("checked", true);
							});
							
							uploadStoreCount($("table.store-select-content").find("input[type='checkbox']:checked").length);//$("#store-count").text($("table.store-select-content").find("input[type='checkbox']:checked").length);
						//}
						//重新初始化 门店点击事件
						$("table.store-select-content input[type='checkbox']").click(function(){
							var branchLen = $("table.store-select-content").find("input[type='checkbox']:checked").length;
							if(branchLen == 0){
								$(".store-select-title input[type=radio][name=store][value='0']").prop("checked", true);
								$(".store-select-title input[type=radio][name=store][value='1']").prop("checked", false);
							} else if(branchLen == $("table.store-select-content").find("input[type='checkbox']").length ){
								$(".store-select-title input[type=radio][name=store][value='0']").prop("checked", false);
								$(".store-select-title input[type=radio][name=store][value='1']").prop("checked", true);
							} else {
								$(".store-select-title input[type=radio][name=store][value='0']").prop("checked", false);
								$(".store-select-title input[type=radio][name=store][value='1']").prop("checked", false);
							}
							uploadStoreCount(branchLen);
						});
						//选中门店名字，也可以发生变化
						// 点击文字选中
						$("table.store-select-content span").click(function(){
							$(this).prev("input[type='checkbox']").trigger("click");
						});
						
						//取消全选与全不选的选中状态
						$("input[name='store']").prop("checked",false);
						
						// 判断一下全选与全不选
						var branchLen = $("table.store-select-content").find("input[type='checkbox']:checked").length;
						if(branchLen == 0){
							$(".store-select-title input[type=radio][name=store][value='0']").prop("checked", true);
							$(".store-select-title input[type=radio][name=store][value='1']").prop("checked", false);
						} else if(branchLen == $("table.store-select-content").find("input[type='checkbox']").length ){
							$(".store-select-title input[type=radio][name=store][value='0']").prop("checked", false);
							$(".store-select-title input[type=radio][name=store][value='1']").prop("checked", true);
						} else {
							$(".store-select-title input[type=radio][name=store][value='0']").prop("checked", false);
							$(".store-select-title input[type=radio][name=store][value='1']").prop("checked", false);
						}
						uploadStoreCount(branchLen);
						
					});
				});
				/*点击全选与非全选按钮*/
				$("input[name='store']").click(function(){
					if($(this).val()=='1')
					{
						num = $("table.store-select-content td").length;
						$("table.store-select-content").find("input[type='checkbox']").each(function(i,ch){
							ch.checked = true;
						});
			
						uploadStoreCount( $("table.store-select-content").find("input[type='checkbox']:checked").length );
			
					}else{
						num=0;
						$("table.store-select-content").find("input[type='checkbox']").each(function(i,ch){
							ch.checked = false;
						});
						uploadStoreCount( $("table.store-select-content").find("input[type='checkbox']:checked").length );
					}
			
				});
				
				/*点击确认按钮*/
				$("#store-select-confirm").click(function(){
					var selectedBranch = $("table.store-select-content").find("input:checked[type='checkbox']");
					
					if(selectedBranch.length > 0){
						$("#store-select").find("div.popover").remove();
						var selectBranchs=[];
						var ul = $("<ul/>").addClass("storesDiv");
						$.each(selectedBranch,function(i,obj){
							selectBranchs.push($(this).val());
							var name = $(this).next("span").text();
							ul.append("<li>"+name+"</li>")
						});
						var top = ileft = iwidth ="";
						if(selectedBranch.length >= 3){
							iwidth = "460px";
							ileft = "-155px";
							
						}
						var div = $("<div>").addClass("popover fade bottom in").css({
							width : iwidth,
							top : "38px",
							left: ileft
						}).append('<div class="arrow" style="left: 50%;"></div>');
						div.append(ul);
						$("#selectBranchs").val( selectBranchs.join(","));
						$("#store-select").append(div);
						$("#addstore").text("已选中"+selectedBranch.length + "家店").addClass("selectBranch");
					}else{
						$("#selectBranchs").val("");
						$("#addstore").html('<i class="icon-plus"></i>选择门店 ').removeClass("selectBranch").next(".popover").remove();
					}
					
					$("#store-select-dialog").modal("hide");
					
					
				});
				
		});
		
		//将弹出的选择门店的层中，更新选择的门店数量的方法抽象出来。当选择的门店为0个的时候，标题显示“选择门店”，否则显示“ 选择门店（已选1家店）”
		
		function uploadStoreCount(count){
			if( count !=0){
				$("#store-count").parent().html("选择门店（已选<font id='store-count'>"+count+"</font>家店）");
			}else{
				$("#store-count").parent().html("选择门店<font id='store-count'></font>");
			}
		}
		
		function loadDish(){
			//显示的modal层
			$("#dish-select-dialog").modal("show");
			$("#dish-select-dialog #accordion").html("数据正在加载中......");
			
			//解析获取到的菜品/菜品分类，然后显示在 弹出层中。
			$.getJSON(global_Path+"/preferential/getTypeAndDishList.json", function(json){
				var html="";
				var tmpJson={};
				var i = 0;
				$.each(json, function(key,obj) {
					tmpJson=JSON.parse(key);
					html +="<div class='panel panel-default'>      "
							+"	<div class='panel-heading' role='tab' id='headingFour_"+tmpJson.id+"'>      "
							+"		<div class='panel-title' data-toggle='collapse' data-parent='#accordion' href='#collapseFour_"+tmpJson.id+"' aria-expanded='true' aria-controls='collapseFour_"+tmpJson.id+"'  style='cursor:pointer'>      "
							+"			<span>"+ tmpJson.itemdesc+"</span> <span class='dish-label'></span>     "
							+"			<a data-toggle='collapse' data-parent='#accordion' href='#collapseFour_"+tmpJson.id+"' aria-expanded='true' aria-controls='collapseFour_"+tmpJson.id+"' class='pull-right'>      "
							+"			 <i class='glyphicon glyphicon-chevron-down'></i>      "
							+"			</a>      "
							+"		</div>      "
							+"	</div>      ";
						if(i == 0){
							html +=	"	<div id='collapseFour_"+tmpJson.id+"' class='panel-collapse in' role='tabpanel' aria-labelledby='headingFour_"+tmpJson.id+"' style='height:auto;'>      "
						}else{
							
							html += "	<div id='collapseFour_"+tmpJson.id+"' class='panel-collapse collapse' role='tabpanel' aria-labelledby='headingFour_"+tmpJson.id+"'>      "
						}
							html +="		<div class='panel-body'>      ";
					//计算该菜品分类是否存在菜品。如果存在，则遍历，并显示。
					if( obj.length>0){
						$.each(obj,function(i,dishObj){
							var checkboxId;
							var checkboxContent;
							if(dishObj.unit && dishObj.unitflag == 0){
								checkboxId = dishObj.dishid+"("+dishObj.unit+")";
								checkboxContent = dishObj.title+"("+dishObj.unit+")";
							} else {
								checkboxId = dishObj.dishid;
								checkboxContent = dishObj.title;
							}
							html += "<label class='checkbox-inline col-xs-3'> <input type='checkbox' id='dish_"
								+checkboxId+"' value='"+dishObj.dishid+"' data-title='"+dishObj.title
								+"' unit='"+dishObj.unit+"' unitflag='"+(dishObj.unitflag==1)+"'>" +checkboxContent+"</label>";
						});
					}
					html+=  "		</div>      "
							+"	</div>      "
							+"</div>      ";
							
					i ++;
				});
				
				
				$("#dish-select-dialog #accordion").html(html);
				//绑定反选功能
				/* $("#dish-select-dialog #dish-radio-uncheck").click(function(){
					$("#dish-select-dialog #accordion").find("input[type='checkbox']").prop("checked",false);
					updateSelectedDish();
				});
				 */
				$(":radio.checkAll").click(function(){
					if($(this).val() == "1"){
						$("#dish-select-dialog #accordion").find("input[type=checkbox]").prop("checked",true);
					}else{
						$("#dish-select-dialog #accordion").find("input[type=checkbox]").prop("checked",false);
					}
					updateSelectedDish();
				});
				
				
				//设置当前的反选功能按钮为不选中。防止编辑菜品的时候，点击过一次 全不选，再次打开菜品 modal的时候，全不选按钮是选中的
				$("#dish-select-dialog #dish-radio-uncheck").prop("checked",false);
				
				//每个菜品选中的时候，需要更新选中菜品数量以及菜品分类的文字
				$.each($("#dish-select-dialog #accordion").find("input[type='checkbox']"), function(i,obj){
					$(this).click(function(){
						$("#dish-select-dialog #dish-radio-uncheck").prop("checked",false);
						if($(this).prop("checked")){
							$("#dish-select-dialog").find("input[type='checkbox'][id='"+$(this).attr("id")+"']").prop("checked", true);
						} else {
							$("#dish-select-dialog").find("input[type='checkbox'][id='"+$(this).attr("id")+"']").prop("checked", false);
						}
						updateSelectedDish();
					});
				});
				
				//绑定确定按钮
				$("#dish-select-dialog #dish-select-confirm").click(function(){
					var checkedDishs=$("#dish-select-dialog #accordion").find("input[type=checkbox]:checked");
					var selectedDishs=[];
					$.each(checkedDishs,function(i,obj){
						var d={};
						d.dish=$(obj).val();
						d.dish_title=$(obj).attr("data-title");
						d.unit=$(obj).attr("unit");
						d.unitflag=$(obj).attr("unitflag");
						//防止不同分类中的同一菜品重复添加。
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
					//使用选择的菜品，构建一个显示在页面表单中的列表。
					buildDishListInView(selectedDishs);
					$("#dish-select-dialog").modal("hide");
				});
				
				//还原原来已经选择的菜品（如果有的话）
				var dishs=getHaveSelectDishsInView();
				$.each(dishs,function(i,obj){
					$("#dish-select-dialog").find("input[type=checkbox][id='dish_"+obj+"']").prop("checked",true);
				});
				
				updateSelectedDish();
				
				function updateSelectedDish(){
					//1.更新选中的数量
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
						$("#dish-count").html("");
					} else {
						$("#dish-count").html("(已选"+selectedDishs.length+"菜品 )");
					} 
					
					//2.更新分类 dish-label
					$.each( $("#dish-select-dialog #accordion").find(".panel"), function(i,obj){
						var dish_array=[];
						var dishs= $(this).find("input[type=checkbox]:checked");
						$.each(dishs,function(d){
							if($(this).attr("unit") && ($(this).attr("unitflag") == "false" || $(this).attr("unitflag") == false)){
								dish_array.push( $(this).attr("data-title")+"("+$(this).attr("unit")+")" );
							} else {
								dish_array.push( $(this).attr("data-title") );
							}
							
						});
						var dish_label=dish_array.join("、");
						if( dish_label.length>15){
							dish_label=dish_label.substring(0,15)+"...";
						}
						if( dish_label!=""){
							dish_label="("+dish_label+")";
						}
						
						$(this).find(".dish-label").html(dish_label);
					});
					//更新全选与全不选按钮的状态。当不是全选或者全不选的时候，清除这两个单选按钮的选中状态。
					$("input[type='radio'][name='dish-radio-uncheck']").prop("checked",false);
					if( checkedDishs.length == $("#dish-select-dialog #accordion").find("input[type='checkbox']").length ){
						//当前为全选。
						$("input[type='radio'][name='dish-radio-uncheck'][value='1']").prop("checked",true); 
					}else if( checkedDishs.length == 0 ){
						//当前为不全选。
						$("input[type='radio'][name='dish-radio-uncheck'][value='0']").prop("checked",true); 
					}
					
				}
			});
			
			//创建选中的 菜品列表，显示在页面中，并且可以填写金额
			/**
			*@param selectedDishs 代表选中的 菜品。数组中，存放json,用json的方式传递菜品数据
			* [
			*	{dish:"id",dish_title:"菜品名称"},
			*	{....}
			* ]
			**/
			function buildDishListInView(selectedDishs){
				var dishListDiv=$("#dish-selected-list");
				dishListDiv.parent().show();
				
				//如果选择了菜品，则不在显示 添加的大按钮，添加操作由 每行菜品的小添加按钮调用
				if( selectedDishs.length>0){
					$(".add-dish-select").parent().parent().hide();
				}else{
					$(".add-dish-select").parent().parent().show();
					dishListDiv.parent().hide();
				}
				var html=""; //新选择的菜品列表。用来在原有列表基础上进行添加的。
				//构建菜品之前，需要考虑 选择的菜品是否已经显示在 优惠菜品列表中。如果已经存在，那么需要保留（主要是价格需要保留），
				
				$.each(selectedDishs,function(i,dish){
					var dishTitle = dish.dish_title;
					var dataDish = dish.dish;
					if(dish.unit && (dish.unitflag == "false" || dish.unitflag == false)){
						dishTitle += "("+dish.unit+")";
						dataDish += "("+dish.unit+")";
					}
					//找到匹配的，然后放入
					var price =dishListDiv.find("input[data-dish='"+dataDish+"']").val();
					dish.price= undefined==price?"":price;
					selectedDishs[i]=dish;
				});
				
				
				$.each(selectedDishs,function(i,dish){
					var dishTitle = dish.dish_title;
					var dataDish = dish.dish;
					if(dish.unit && (dish.unitflag == "false" || dish.unitflag == false)){
						dishTitle += "("+dish.unit+")";
						dataDish += "("+dish.unit+")";
					}
					
					html+="<div class='selected-dish-list clearfix'>"
						+"     <div class='col-xs-6 h24'>"+dishTitle+"</div> "
						+" 		<div class='input-group col-xs-3 h24'>       "
						+" 			<input type='text' name='"+dataDish+"' class='form-control p_0  isPrice' data-dish='"+dataDish+"' dish-id='"+dish.dish+"' data-dish-title='"+dish.dish_title+"' unit='"+dish.unit+"' unitflag='"+dish.unitflag+"' value='"+dish.price+"'>  "
						+" 			<span class='' style='padding-top:0px;padding-bottom:0px;height:auto' id='basic-addon1'> 元/"+dish.unit+"</span>       "
						+"		</div> "
						+"     <div class='col-xs-3 h24' style='text-align:left;padding:0px 10px;height:30px;'> " 
						+"			<div class='operate'>  "
						+" 			<span class='glyphicon glyphicon-plus' aria-hidden='true'></span>"
						+" 			<span class='glyphicon glyphicon-remove' aria-hidden='true'></span>"
						+"			<div class='clear'></div>"
						+"			</div>"
						+"	   </div> "
						+" </div>";
					
				});
				dishListDiv.html(html);
				dishListDiv.parent().height(dishListDiv.height());
				//设置 所有的菜品列表中的删除按钮可以 删除某一行菜品
				dishListDiv.find(".glyphicon-remove").click(function(){
					deleteDish($(this));
				});
				
				//所有的 +按钮，都可以打开添加面板
				dishListDiv.find(".glyphicon-plus").click(function(){
					loadDish();
				});
				
			}
			
			/**
			 *获取已经选择的，并且显示在页面中的 菜品id，用于重新选择菜品的时候，还原应选择的菜品.
			 @return Array
			**/
			function getHaveSelectDishsInView(){
				var dishListDiv=$("#dish-selected-list");
				var dishs=[];
				var dishList=dishListDiv.find("input");
				$.each(dishList, function(i, obj) {
					dishs.push( $(obj).attr("data-dish") );
				})
				return dishs;
			}
			
			
			
			
		}
		
		function deleteDish(obj){
				$(obj).parent().parent().parent().remove();
				var dishListDiv=$("#dish-selected-list");
				dishListDiv.parent().height(dishListDiv.height());
				//判断是否还有菜品，如果没有菜品，则重新显示 大的增加按钮
				if( $("#dish-selected-list").find("input").length<1 ){
					$(".add-dish-select").parent().parent().show();
					dishListDiv.parent().hide();
				}
		}
		
		//还原优惠券门店信息的方法。在编辑 优惠券的时候，根据ID是否存在进行调用
		function reBuildBranchs(){
			if( $("#id").val() !="" ){
				var applyAll=${null!=specialStampVO.activity ? specialStampVO.activity.applyAll:true}; //是否应用所有门店
				var branch_ids="${branch_ids}"; //如果是优惠部分门店，这里则是所有优惠的门店的ID，用逗号分割
				var branch_names="${branch_names}"; //优惠门店的名称
				if( !applyAll ){
					$("input[name=applyAll][value=0]").prop("checked",true);
					$("div.store-select").removeClass("hidden");
					$("#selectBranchs").val(branch_ids);
					var branchArray=[];
					if(branch_names != ""){
						branchArray=branch_names.split("、");
					}
					
					if(branchArray.length > 0){
						var ul = $("<ul/>").addClass("storesDiv");
						$.each(branchArray,function(i,v){
							var name = v;
							ul.append("<li>"+name+"</li>")
						});
						var top = ileft = iwidth ="";
						if(branchArray.length >= 3){
							iwidth = "460px";
							ileft = "-155px";
							
						}
						var div = $("<div>").addClass("popover fade bottom in").css({
							width : iwidth,
							top : "38px",
							left: ileft
						}).append('<div class="arrow" style="left: 50%;"></div>');
						div.append(ul);
						$("#store-select").append(div);
						$("#addstore").text("已选中"+branchArray.length + "家店").addClass("selectBranch");
					}
					
				}else{
					$("input[name=applyAll][value=1]").prop("checked",true);
				}
			}
		}
		$(parent.document.all("allSearch")).css("visibility","hidden");
		$("img.img-close").hover(function(){
		 	$(this).attr("src",global_Path+"/images/close-active.png");	 
		},function(){
				$(this).attr("src",global_Path+"/images/close-sm.png");
		});
	</script>
</body>
</html>
 

