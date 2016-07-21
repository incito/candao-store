var selectedAreas=[];
var selectedDishs=[];
var findTableids=[];
var findTablenames=[];
var findDishids=[];
var findDishnames=[];
var findDishidsNew=[];
var getAreaslistTag=[];
var areaidListStatus0=[];
var areaidListStatus1=[];
var getDishTypeslistTag=[];
var dishTypeIdListTag=[];
var flagIpAddress=true;
var findTDid=[];
var findTDname=[];

var curGroupObjDiv=null;
var findGroupDishidMap=new HashMap();
var findGroupDishnameMap=new HashMap();
var groupid=1;
$(document).ready(function(){
	$("#printConfig-name").change(function(){
		  $("#printConfig-name_tip").text("");
		});
	$("input").keyup(function(){
		  $(this).next(".error").text("");
	});
//	$("#printConfig-add-dialog").modal("show");
//	$("#print-area-add").popover( {trigger:'hover',content:popoverLoadTable});
//	$("#print-dishes-add").popover( {trigger:'click',content:popoverLoadDish});
//	$("#print-dishes-add").hover(function(){
//		$(this).next(".popover").show();
//	}, function(){
//		$(this).next(".popover").hide();
//	});

	  $.ajax({
			type:"post",
			async:false,
			url : global_Path+"/printerManager/find.json",
			contentType:'application/json;charset=UTF-8',
			dataType : "json",
			success : function(result) {
				 $.each(result, function(i,val){  
					 $("#printConfig-add").before('<div class="print-detail-box" id='+val.printerid+'  onmouseover="showPrintDel(this)" onmouseout="displayPrintDel(this)" ondblclick="editPrintBox(this)"><p class="print-img" ><img src="../images/print.png"></p><p id="printernameShow">'+substrControl(val.printername,18)+'</p>	<i class="icon-remove hidden" onclick="delPrintBox(this)"></i></div>');
				  });
			}
		});
	$("#add-form-printConfig").validate({
		submitHandler : function(form) {
			var vcheck = true;

			if ($("#printConfig-name").val().trim() == "") {

				$("#printConfig-name_tip").text("必填信息");
				vcheck = false;
			} 
//			if ($("#ipAddress").val().trim() == "") {
//
//				$("#ipAddress_tip").text("必填信息");
//				vcheck = false;
//			} 
			/**可以配置备用打印机，通过逗号隔开，所以去掉IP校验**/
//			if(!f_check_IP()){
//				$("#ipAddress_tip").text("请输入格式正确的IP");
//				vcheck = false;
//			}
			
//			if ($("#port").val().trim() == "") {
//
//				$("#port_tip").text("必填信息");
//				vcheck = false;
//			} 
			if($("#print-bill").val()==1||$("#print-bill").val()==2){
				if($("#print-area-add").find("i").attr("class")=="icon-plus-sign"){
					$("#print-area-add_tip").text("打印区域不能为空");
					vcheck = false;
				}
			}
			if($("#print-bill").val()==1){
				if($("#print-dishes-add").find("i").attr("class")=="icon-plus-sign"){
					$("#print-dishes-add_tip").text("打印菜品不能为空");
					vcheck = false;
				}
			}
			if (vcheck) {
				if(check_same_printerName()
						&&check_same_IpAddress()
						){
				clickFormAddPrintConfig();
				
				}else{
				}
			}
		}
	});
	/*打印配置添加弹出框*/
	$("#printConfig-add").click(function(){
		initPrinter();
		$(".modal-title").text("添加打印配置");
		$("#printConfig-add-dialog").modal("show");
	});
	/*打印单据选择*/
	$("#print-bill").change(function(){
		printerBillChange($("#print-bill").val());
		
	
	});


	//绑定反选功能
	$("#printArea-add-dialog #table-radio-uncheck").click(function(){
		$("#printArea-add-dialog #accordion").find("input[type=checkbox]").prop("checked",false);
		$("#printArea-add-dialog #accordion").find("img").attr("alt", "0");
		$("#printArea-add-dialog #accordion").find("img").attr("src", global_Path+ "/images/none_select.png");
		$("#printArea-add-dialog #accordion").find(".dish-label").html('');
		checkedBoxLength("#printArea-add-dialog #table-count");
		$("#printArea-add-dialog #table-radio-check").attr("checked",false);
	});
	//绑定全选功能
	$("#printArea-add-dialog #table-radio-check").click(function(){
		$("#printArea-add-dialog #accordion").find("input[type=checkbox]").prop("checked",true);
		$("#printArea-add-dialog #accordion").find("img").attr("alt", "2");
		$("#printArea-add-dialog #accordion").find("img").attr("src", global_Path+ "/images/all_select.png");
		$("#printArea-add-dialog #accordion").find(".dish-label").html('');
		checkedBoxLength("#printArea-add-dialog #table-count");
		$("#printArea-add-dialog #table-radio-uncheck").attr("checked",false);
		generalDishTitle("printArea-add-dialog");
	});
	//绑定反选功能
	
	$("#printDishes-add-dialog #dishes-radio-uncheck").click(function(){
		$("#printDishes-add-dialog #accordion").find("input[type=checkbox]").prop("checked",false);
		$("#printDishes-add-dialog #accordion").find("img").attr("alt", "0");
		$("#printDishes-add-dialog #accordion").find("img").attr("src", global_Path+ "/images/none_select.png");
		$("#printDishes-add-dialog #accordion").find(".dish-label").html('');
		checkedBoxLength("#printDishes-add-dialog #dish-count");
		$("#printDishes-add-dialog #dishes-radio-check").attr("checked",false);
	});
	//绑定全选功能
	$("#printDishes-add-dialog #dishes-radio-check").click(function(){
		$("#printDishes-add-dialog #accordion").find("input[type=checkbox]").prop("checked",true);
		$("#printDishes-add-dialog #accordion").find("img").attr("alt", "2");
		$("#printDishes-add-dialog #accordion").find("img").attr("src", global_Path+ "/images/all_select.png");
		$("#printDishes-add-dialog #accordion").find(".dish-label").html('');
		checkedBoxLength("#printDishes-add-dialog #dish-count");
		$("#printDishes-add-dialog #dishes-radio-uncheck").attr("checked",false);
		generalDishTitle("printDishes-add-dialog");
	});
	
//绑定反选功能
	
	$("#printGroup-add-dialog #group-radio-uncheck").click(function(){
		$("#printGroup-add-dialog #accordion").find("input[type=checkbox]").prop("checked",false);
		$("#printGroup-add-dialog #accordion").find("img").attr("alt", "0");
		$("#printGroup-add-dialog #accordion").find("img").attr("src", global_Path+ "/images/none_select.png");
		$("#printGroup-add-dialog #accordion").find(".dish-label").html('');
		checkedBoxLength("#printGroup-add-dialog #group-count");
		$("#printGroup-add-dialog #group-radio-check").attr("checked",false);
	});
	//绑定全选功能
	$("#printGroup-add-dialog #group-radio-check").click(function(){
		$("#printGroup-add-dialog #accordion").find("input[type=checkbox]").prop("checked",true);
		$("#printGroup-add-dialog #accordion").find("img").attr("alt", "2");
		$("#printGroup-add-dialog #accordion").find("img").attr("src", global_Path+ "/images/all_select.png");
		$("#printGroup-add-dialog #accordion").find(".dish-label").html('');
		checkedBoxLength("#printGroup-add-dialog #group-count");
		$("#printGroup-add-dialog #group-radio-uncheck").attr("checked",false);
		generalDishTitle("printGroup-add-dialog");
	});
	
	/*打印区域按钮点击*/
	$("#print-area-add").click(function(){
		$("#printArea-add-dialog").modal("show");
		$("printArea-add-dialog #accordion").html("数据正在加载中......");
		
		//解析获取到的菜品/菜品分类，然后显示在 弹出层中。
		$.getJSON(global_Path+"/table/getTypeAndTableMap.json", function(json){
			var html="";
			var tmpJson={};
			$.each(json, function(index,item) {
			$.each(item, function(key,obj) {
				tmpJson=JSON.parse(key);
				
				html +="<div class='panel panel-default'>      "
						+"	<div class='panel-heading clearfix' role='tab'  id='headingFour_"+tmpJson.areaid+"' >      "
						+"		<div style='width:18px;float:left'><img alt='0' src='"+global_Path+ "/images/none_select.png' panelCheckedStatus='' style='width:15px;height:15px'></div>"
						+"		<div class='panel-title' style='width:470px;float:left' data-toggle='collapse' data-parent='#printArea-add-dialog #accordion' href='#collapseFour_"+tmpJson.areaid+"' aria-expanded='true' aria-controls='collapseFour'>      "
						+"			<span>"+ tmpJson.areaname+"</span>      "
						+			"<span class='dish-label'></span>"
						+"			<a  class='pull-right'>      "
						+"			 <i class='glyphicon glyphicon-chevron-down'></i>      "
						+"			</a>      "
						+"		</div>      "
						+"	</div>      "
						+"	<div id='collapseFour_"+tmpJson.areaid+"' class='panel-collapse collapse' role='tabpanel' aria-labelledby='headingFour_"+tmpJson.areaid+"'>      "
						+"		<div class='panel-body'>      ";
				//计算该菜品分类是否存在餐台。如果存在，则遍历，并显示。
				if( obj.length>0){
					$.each(obj,function(i,tableObj){
						var checkboxId; 
						var checkboxContent;
						
						
							checkboxId = tableObj.tableid;
							checkboxContent = tableObj.tableNo;
							html += "<label class='checkbox-inline col-xs-3'><input type='checkbox' id='table_"
								+checkboxId+"'  value='"+tableObj.tableid+"' data-title='"+tableObj.tableNo
								+"' code='"+tableObj.dishno+"'><span>"+substrControl(checkboxContent,12)+"</span></label>";

						
					});
				}
				html+=  "		</div>      "
						+"	</div>      "
						+"</div>      ";
					
			});
			});
			
			
			$("#printArea-add-dialog #accordion").html(html);
			$("#printArea-add-dialog #accordion").children(":first").find(".panel-collapse").removeClass("collapse");
			$("#printArea-add-dialog #accordion").children(":first").find(".panel-collapse").removeClass("in");
//			$("#printArea-add-dialog #accordion").children(":first").find(".panel-title").click();
			
			//在餐台分类绑定选择框点击事件
			$(".panel").find("img").click(function(){
				var panelCheckedStatus = $(this).attr("alt");
				if(panelCheckedStatus == 0 || panelCheckedStatus == 1){
					$(this).parent().parent().parent().find("input[type=checkbox]").prop("checked", true);
				} else {
					$(this).parent().parent().parent().find("input[type=checkbox]").prop("checked", false);
				}
				$("#printArea-add-dialog #table-radio-uncheck").attr("checked",false);
				generalDishTitle("printArea-add-dialog");
				
				checkedBoxLength("#printArea-add-dialog #table-count");
			});
			
			$("#printArea-add-dialog input[type='checkbox']").click(function(){
				var selectedDish = parseInt($("#printArea-add-dialog #table-count").text());
				if($(this).prop("checked")){
					selectedDish++;
					$("#printArea-add-dialog").find("input[type='checkbox'][id='"+$(this).attr("id")+"']").prop("checked", true);
				} else {
					selectedDish--;
					$("#printArea-add-dialog").find("input[type='checkbox'][id='"+$(this).attr("id")+"']").prop("checked", false);
				}
				if(selectedDish==0){
					$("#printArea-add-dialog #table-count").parent().css("display","none");
					
				}else{
					$("#printArea-add-dialog #table-count").parent().css("display","inline");
					
				}
				$("#printArea-add-dialog #table-count").text(selectedDish);
				
//				$("#printArea-add-dialog #table-radio-uncheck").attr("checked",false);
//				$("#printArea-add-dialog #table-radio-check").attr("checked",false);
				if($("#printArea-add-dialog #accordion").find("input[type=checkbox]:checked").length==0){
					$("#printArea-add-dialog #table-radio-uncheck").click();
					$("#printArea-add-dialog #table-radio-check").attr("checked",false);
				}else if($("#printArea-add-dialog #accordion").find("input[type=checkbox]:checked").length==$("#printArea-add-dialog #accordion").find("input[type=checkbox]").length){
					$("#printArea-add-dialog #table-radio-uncheck").attr("checked",false);
					$("#printArea-add-dialog #table-radio-check").click();
				}else{
					$("#printArea-add-dialog #table-radio-uncheck").attr("checked",false);
					$("#printArea-add-dialog #table-radio-check").attr("checked",false);
				}
				generalDishTitle("printArea-add-dialog");
			});
			if($("#printArea-add-dialog #accordion").find("input[type=checkbox]:checked").length==0){
				$("#printArea-add-dialog #table-radio-uncheck").click();
				$("#printArea-add-dialog #table-radio-check").attr("checked",false);
			}else if($("#printArea-add-dialog #accordion").find("input[type=checkbox]:checked").length==$("#printArea-add-dialog #accordion").find("input[type=checkbox]").length){
				$("#printArea-add-dialog #table-radio-uncheck").attr("checked",false);
				$("#printArea-add-dialog #table-radio-check").click();
			}else{
				$("#printArea-add-dialog #table-radio-uncheck").attr("checked",false);
				$("#printArea-add-dialog #table-radio-check").attr("checked",false);
			}
			$.each(findTableids, function(key,obj) {
				$("#table_"+obj).click();
			});
//			$.each(areaidListStatus1, function(key,obj) {
//				if($("#headingFour_"+obj).children(":first").children(":first").attr("alt")!=2){
//					
//					$("#headingFour_"+obj).children(":first").children(":first").click();
//					
//				}
//				
//			});
			checkedBoxLength("#printArea-add-dialog #table-count");
		});
	});
	
	/*打印菜品按钮点击*/
	$("#print-dishes-add").click(function(){
		
		$("#printDishes-add-dialog #dishes-radio-check").attr("checked",false);
		$("#printDishes-add-dialog #dishes-radio-uncheck").attr("checked",false);
		$("#printDishes-add-dialog").modal("show");
		$("#printDishes-add-dialog #accordion").html("数据正在加载中......");
		
		//解析获取到的菜品/菜品分类，然后显示在 弹出层中。
		$.getJSON(global_Path+"/dish/getTypeAndDishMap.json", function(json){
			var html="";
			var tmpJson={};
			var haveDishId=[];
			$(".nav-dishes-tab .dishes-detail-box").each(function(){
				haveDishId.push($(this).attr("id"));
			});
			$.each(json, function(index,item) {
			$.each(item, function(key,obj) {
				tmpJson=JSON.parse(key);
				if($("li.nav-dishes-type.active").attr('id')!=tmpJson.id){
				html +="<div class='panel panel-default'>      "
						+"	<div class='panel-heading clearfix' role='tab' id='headingDish_"+tmpJson.id+"'>      "
						+"		<div style='width:18px;float:left'><img alt='0' src='"+global_Path+ "/images/none_select.png' panelCheckedStatus='' style='width:15px;height:15px'></div>"
						+"		<div class='panel-title' style='width:470px;float:left' data-toggle='collapse' data-parent='#printDishes-add-dialog #accordion' href='#collapseDish_"+tmpJson.id+"' aria-expanded='true' aria-controls='collapseFour'>      "
						+"			<span>"+ tmpJson.itemdesc+"</span>      "
						+			"<span class='dish-label'></span>"
						+"			<a  class='pull-right'>      "
						+"			 <i class='glyphicon glyphicon-chevron-down'></i>      "
						+"			</a>      "
						+"		</div>      "
						+"	</div>      "
						+"	<div id='collapseDish_"+tmpJson.id+"' class='panel-collapse collapse' role='tabpanel' aria-labelledby='headingDish_"+tmpJson.id+"'>      "
						+"		<div class='panel-body'>      ";
				//计算该菜品分类是否存在菜品。如果存在，则遍历，并显示。
				if( obj.length>0){
					$.each(obj,function(i,dishObj){
						var checkboxId;
						var checkboxContent;
						
							checkboxId = dishObj.dishid;
							checkboxContent = dishObj.title;
						html += "<label class='checkbox-inline col-xs-3'><input type='checkbox' id='dish_"
								+checkboxId+"' value='"+dishObj.dishid+"' data-title='"+dishObj.title
								+"' code='"+dishObj.dishno+"'><span>"+substrControl(checkboxContent,12)+"</span></label>";
						
					});
				}
				html+=  "		</div>      "
						+"	</div>      "
						+"</div>      ";
				}		
			});
			});
			
			
			$("#printDishes-add-dialog #accordion").html(html);
			$("#printDishes-add-dialog #accordion").children(":first").find(".panel-collapse").removeClass("collapse");
			$("#printDishes-add-dialog #accordion").children(":first").find(".panel-collapse").removeClass("in");
//			$("#printDishes-add-dialog #accordion").children(":first").find(".panel-title").click();
//			//在餐台分类绑定选择框点击事件
//			$(".panel").find("img").click(function(){
//				var panelCheckedStatus = $(this).attr("alt");
//				if(panelCheckedStatus == 0 || panelCheckedStatus == 1){
//					$(this).parent().parent().parent().find("input[type=checkbox]").prop("checked", true);
//				} else {
//					$(this).parent().parent().parent().find("input[type=checkbox]").prop("checked", false);
//				}
//				$("#printArea-add-dialog #table-radio-uncheck").attr("checked",false);
//				generalDishTitle();
//				$("#printArea-add-dialog #dish-count").text($("#printArea-add-dialog input[type='checkbox']:checked").length);
//			});
			
			//在菜品分类绑定选择框点击事件
			$(".panel").find("img").click(function(){
				var panelCheckedStatus = $(this).attr("alt");
				if( panelCheckedStatus == 1){
					$(this).parent().parent().parent().find("input[type=checkbox]").attr("checked",false);
				}
				$(this).parent().parent().parent().find("input[type=checkbox]").click();
				$("#printDishes-add-dialog #dish-radio-uncheck").attr("checked",false);
				generalDishTitle("printDishes-add-dialog");
				checkedBoxLength("#printDishes-add-dialog #dish-count");
				
			});
			
			
			$("#printDishes-add-dialog input[type='checkbox']").click(function(){
				var selectedDish = parseInt($("#dish-count").text());
				if($(this).prop("checked")){
					selectedDish++;
					$("#printDishes-add-dialog").find("input[type='checkbox'][id='"+$(this).attr("id")+"']").prop("checked", true);
				} else {
					selectedDish--;
					$("#printDishes-add-dialog").find("input[type='checkbox'][id='"+$(this).attr("id")+"']").prop("checked", false);
				}
				if(selectedDish==0){
					$("#printDishes-add-dialog #dish-count").parent().css("display","none")
				}else{
					$("#printDishes-add-dialog #dish-count").parent().css("display","inline")
				}
				$("#printDishes-add-dialog #dish-count").text(selectedDish);
				if($("#printDishes-add-dialog #accordion").find("input[type=checkbox]:checked").length==0){
					$("#printDishes-add-dialog #dishes-radio-uncheck").click();
					$("#printDishes-add-dialog #dishes-radio-check").attr("checked",false);
				}else if($("#printDishes-add-dialog #accordion").find("input[type=checkbox]:checked").length==$("#printDishes-add-dialog #accordion").find("input[type=checkbox]").length){
					$("#printDishes-add-dialog #dishes-radio-uncheck").attr("checked",false);
					$("#printDishes-add-dialog #dishes-radio-check").click();
				}else{
					$("#printDishes-add-dialog #dishes-radio-uncheck").attr("checked",false);
					$("#printDishes-add-dialog #dishes-radio-check").attr("checked",false);
				}
				
				generalDishTitle("printDishes-add-dialog");
			});
			$.each(findDishids, function(key,obj) {
				$("#dish_"+obj).click();
			});
			if(findDishids.length==0){
				$("#printDishes-add-dialog #dishes-radio-uncheck").click();
				$("#printDishes-add-dialog #dishes-radio-check").attr("checked",false);
			}else if($("#printDishes-add-dialog #accordion").find("input[type=checkbox]:checked").length==$("#printDishes-add-dialog #accordion").find("input[type=checkbox]").length){
				$("#printDishes-add-dialog #dishes-radio-uncheck").attr("checked",false);
				$("#printDishes-add-dialog #dishes-radio-check").click();
			}
//			$.each(areaidListStatus1, function(key,obj) {
//				if($("#headingFour_"+obj).children(":first").children(":first").attr("alt")!=2){
//					
//					$("#headingFour_"+obj).children(":first").children(":first").click();
//					
//				}
//				
//			});
			checkedBoxLength("#printDishes-add-dialog #dish-count");
			
		});
		
	});
	/*打印配置内容添加*/
	$("#printConfig-save").click(function(){
		var text = $("#printConfig-name").val();
		var html = '<div class="print-detail-box" onmouseover="showPrintDel(this)" onmouseout="displayPrintDel(this)" ondblclick="editPrintBox(this)">';
			html +='<p class="print-img"><img src="../images/print.png"></p>';
			html += '<p id="printernameShow">'+substrControl(text,10)+'</p><i class="icon-remove hidden" onclick="delPrintBox(this)"></i></div>';
		$("#printConfig-add").before(html);
		$("#printConfig-add-dialog").modal("hide");
	});
	//绑定确定按钮
	$("#printArea-add-dialog #print-area-confirm").click(function(){
		checkedBoxLength("#printArea-add-dialog #table-count");
		$("#print-area-add_tip").text("");
//		findTableAndDish("#printArea-add-dialog");
		findTableids=findTDid;
		findTablenames=findTDname;
//		if(!jQuery.isEmptyObject(findTableids)){
//		$("#print-area-add").text("已选中"+findTableids.length + "个餐台");
//		}else{
//		$("#print-area-add").html('<i class="icon-plus-sign"></i>');
//		}
		
		var checkedAreas=$("#printArea-add-dialog #accordion").find(".panel-heading img");
		getAreaslistTag=[];
		areaidListStatus0=[];
		areaidListStatus1=[];
		$.each(checkedAreas,function(i,obj){
			if($(obj).attr("alt")==1||$(obj).attr("alt")==2){
				getAreaslistTag.push($(obj).parent().siblings().children(":first").text());
				areaidListStatus0.push($(obj).parent().parent().attr("id").split("_")[1]);
				
			}
			if($(obj).attr("alt")==2){
				areaidListStatus1.push($(obj).parent().parent().attr("id").split("_")[1]);
			}
		});
		$("#printArea-add-dialog").modal("hide");
		showSelectStoreDiv(findTablenames,"#div-print-area-add");
	});
	//绑定确定按钮
	$("#printDishes-add-dialog #print-dishes-confirm").click(function(){
		var oldDishIds = findDishids.slice();//将已有的数据赋给一个新的数组
		$("#print-dishes-add_tip").text("");
		var checkedDishs=$("#printDishes-add-dialog #accordion").find("input[type=checkbox]:checked");
		findDishids=[];
		findDishnames=[];
		findDishidsNew=[];
		$.each(checkedDishs,function(i,obj){
			if(findDishids.indexOf($(obj).val())==-1){
				findDishids.push($(obj).val());
				findDishnames.push($(obj).next("span").text());
				findDishidsNew.push($(obj).val());
			}
			
		});
		if(!jQuery.isEmptyObject(findDishids)){
			$("#print-dishes-add").text("已选中"+findDishids.length + "个菜品");
		}else{
			$("#print-dishes-add").html('<i class="icon-plus-sign"></i>');
		}
		
		
		
		var checkedDishes=$("#printDishes-add-dialog #accordion").find(".panel-heading img");
		var showDishtypes=[];
		$.each(checkedDishes,function(i,obj){
			if($(obj).attr("alt")==1||$(obj).attr("alt")==2){
				showDishtypes.push($(obj).parent().siblings().children(":first").text());
			}
		});
//		if(!jQuery.isEmptyObject(showDishtypes)){
//			$("#print-dishes-add").text(showDishtypes.join(','));
//		}else{
//			$("#print-dishes-add").html('<i class="icon-plus-sign"></i>');
//		}


		$("#printDishes-add-dialog").modal("hide");
		showSelectStoreDiv(findDishnames,"#div-print-dishes-add");
		if(findDishids.length>0){

			$.post(global_Path+"/printerManager/getDishOfPrinter.json", {//getTypeAndDishMap //printerManager/getDishOfPrinter
				dishids: JSON.stringify(findDishids)
			}, function(json) {
				var delGroupIdsArray = [];
				var GroupDishIds = [];
				if (json != null) {
					$.each(json, function (index, item) {
						$.each(item, function (key, obj) {
							if (obj.length > 0) {
								$.each(obj, function (i, dishObj) {
									GroupDishIds.push(dishObj.dishid);
								});
							}
						});
					});
					if(findGroupDishidMap.size() > 0) {
						findGroupDishidMap.each(function(i,v){
							var finded = false;
							$.each(v,function(key,value){
								if(GroupDishIds.indexOf(value) === -1) {
									delGroupIdsArray.push(i);
									finded = true;
									return false;
								}
								if(finded) {
									return false;
								}
							})
						})
					}
					if(delGroupIdsArray.length>0) {
						$.each(delGroupIdsArray, function(i,v){
							findGroupDishidMap.remove(v);
							findGroupDishnameMap.remove(v);
						});
						reInitGroupDiv();
					}
					console.info(delGroupIdsArray);
				}
			});
		}else{
			$("#print-groupdishes").addClass("hidden");
			clearGroup();
		}
	});
	//绑定确定按钮
	$("#printGroup-add-dialog #print-groupdishes-confirm").click(function(){
		$("#print-groupdishes-add_tip").text("");
		var checkedDishs=$("#printGroup-add-dialog #accordion").find("input[type=checkbox]:checked");
		var findGroupDishids=[];
		var findGroupDishnames=[];
		$.each(checkedDishs,function(i,obj){
			if(findGroupDishids.indexOf($(obj).val())==-1){
				findGroupDishids.push($(obj).val());
				findGroupDishnames.push($(obj).next("span").text());
			}
		});
		var group = $(curGroupObjDiv).attr("groupid");
		if(findGroupDishids!=null && findGroupDishids.length>0){
			findGroupDishidMap.put(group, findGroupDishids);
			findGroupDishnameMap.put(group, findGroupDishnames);
		}else{
			//若将选择的自合删除 则要清空map中的值
			findGroupDishidMap.remove(group);
			findGroupDishnameMap.remove(group);
			reInitGroupDiv();
		}
		
		var checkedDishes=$("#printGroup-add-dialog #accordion").find(".panel-heading img");
		var showDishtypes=[];
		$.each(checkedDishes,function(i,obj){
			if($(obj).attr("alt")==1||$(obj).attr("alt")==2){
				showDishtypes.push($(obj).parent().siblings().children(":first").text());
			}
		});

		$("#printGroup-add-dialog").modal("hide");
		showSelectStoreDivGroup(findGroupDishnames);
	});
});
/**
 * 将数组中的value转换为int
 * @param key
 * @returns
 */
function keyValueToInt(key){
	var newKey = [];
	for(var i=0; i<key.length; i++){
		newKey.push(parseInt(key[i]));
	}
	return newKey.sort();
}
/**
 * 重新组合
 */
function reInitGroupDiv(){
	$("#print-groupdishes .group-div").remove();
	initGroupDiv();
	var keys = findGroupDishidMap.keySet();
	var map = new HashMap();
	var mapname = new HashMap();
	keys = keyValueToInt(keys);
	$.each(keys, function(i, key){
		var values = findGroupDishidMap.get(key);
		var newKey = i+1;
		map.put(newKey, values);
		
		var names = findGroupDishnameMap.get(key);
		mapname.put(newKey, names);
	});
	console.log(map.keySet());
	console.log(map.values());
	
	var newGroups = map.keySet();
	newGroups = keyValueToInt(newGroups);
	groupid = newGroups[newGroups.length-1] || 0 + 1;//取最后一个加1
	var lastDiv = $("#print-groupdishes").find(".group-div").eq(0);
	$("#print-groupdishes").find(".group-div").eq(0).attr("groupid", groupid);
	
	findGroupDishidMap = new HashMap();
	findGroupDishnameMap = new HashMap();
	$.each(newGroups, function(i, group){
		findGroupDishidMap.put(group, map.get(group));
		findGroupDishnameMap.put(group, mapname.get(group));
		
		var branchnames = mapname.get(group);
		if(branchnames.length > 0){
			var	groupdiv = $('<div class="col-xs-2 group-div" style="text-align: left; margin: auto; width: 130px;padding-top: 5px;" groupid="'+group+'">'
					+'<button type="button" style="font-size: 13px;" class="btn btn-default selectBranch required " data-html="true" title=""'
					+'ata-container=""  data-toggle="popover" data-placement="bottom" '
					+'ata-content="" onclick="showGroupDialog(this)">组合'+DX(group)+'</button></div>');
			$(lastDiv).before(groupdiv);
			var ul = $("<ul/>").addClass("storesDiv");
			$.each(branchnames,function(i,item){
				
				ul.append("<li>"+item+"</li>");
			});
			var ileft = iwidth ="";
			if(branchnames.length >= 3){
				iwidth = "460px";
				ileft = "-155px";
			}
			var div = $("<div>").addClass("popover fade bottom in").css({
				width : iwidth,
				top : "38px",
				left: ileft
			}).append('<div class="arrow" style="left: 50%;"></div>');
			div.append(ul);
			groupdiv.append(div);
		}
	});
	$(".group-div").hover(function(){
		$(this).find(".popover").show();
	}, function(){
		$(this).find(".popover").hide();
	});
}
//打开选择组合dialog
function showGroupDialog(obj){
	curGroupObjDiv=$(obj).parent();
	$("#printGroup-add-dialog #group-radio-check").attr("checked", false);
	$("#printGroup-add-dialog #group-radio-uncheck").attr("checked", false);
	$("#printGroup-add-dialog").modal("show");
	$("#printGroup-add-dialog #accordion").html("数据正在加载中......");
	
	var group = $(curGroupObjDiv).attr("groupid");
	var curValues = findGroupDishidMap.get(group);
	if(curValues!=null && curValues.length>0){
		findDishidsNew = findDishids.slice();
	}
	var keys = findGroupDishidMap.keySet();
	$.each(keys, function(i, key){
		if(key != group){
			var values = findGroupDishidMap.get(key);
			$.each(values, function(j, value){
				findDishidsNew.remove(value);
			});
		}
	});
	//解析获取到的菜品/菜品分类，然后显示在 弹出层中。
	$.post(global_Path+"/printerManager/getDishOfPrinter.json", {//getTypeAndDishMap //printerManager/getDishOfPrinter
		dishids: JSON.stringify(findDishidsNew)
	}, function(json){
		console.log(json);
		var html="";
		var tmpJson={};
		var haveDishId=[];
		$(".nav-dishes-tab .dishes-detail-box").each(function(){
			haveDishId.push($(this).attr("id"));
		});
		if(json!=null && json.length>0){
			$.each(json, function(index,item) {
				$.each(item, function(key,obj) {
					tmpJson=JSON.parse(key);
					if($("li.nav-dishes-type.active").attr('id')!=tmpJson.id){
					html +="<div class='panel panel-default'>      "
							+"	<div class='panel-heading clearfix' role='tab' id='gheadingDish_"+tmpJson.id+"'>      "
							+"		<div style='width:18px;float:left'><img alt='0' src='"+global_Path+ "/images/none_select.png' panelCheckedStatus='' style='width:15px;height:15px'></div>"
							+"		<div class='panel-title' style='width:470px;float:left' data-toggle='collapse' data-parent='#printGroup-add-dialog #accordion' href='#gcollapseDish_"+tmpJson.id+"' aria-expanded='true' aria-controls='collapseFour'>      "
							+"			<span>"+ tmpJson.itemdesc+"</span>      "
							+			"<span class='dish-label'></span>"
							+"			<a  class='pull-right'>      "
							+"			 <i class='glyphicon glyphicon-chevron-down'></i>      "
							+"			</a>      "
							+"		</div>      "
							+"	</div>      "
							+"	<div id='gcollapseDish_"+tmpJson.id+"' class='panel-collapse collapse' role='tabpanel' aria-labelledby='gheadingDish_"+tmpJson.id+"'>      "
							+"		<div class='panel-body'>      ";
					//计算该菜品分类是否存在菜品。如果存在，则遍历，并显示。
					if( obj.length>0){
						$.each(obj,function(i,dishObj){
							var checkboxId;
							var checkboxContent;
							
								checkboxId = dishObj.dishid;
								checkboxContent = dishObj.title;
							html += "<label class='checkbox-inline col-xs-3'><input type='checkbox' id='gdish_"
									+checkboxId+"' value='"+dishObj.dishid+"' data-title='"+dishObj.title
									+"' code='"+dishObj.dishno+"'><span>"+substrControl(checkboxContent,12)+"</span></label>";
							
						});
					}
					html+=  "		</div>      "
							+"	</div>      "
							+"</div>      ";
					}		
				});
			});
			$("#printGroup-add-dialog #accordion").html(html);
			$("#printGroup-add-dialog #accordion").children(":first").find(".panel-collapse").removeClass("collapse");
			$("#printGroup-add-dialog #accordion").children(":first").find(".panel-collapse").removeClass("in");
		}else{
			$("#printGroup-add-dialog #accordion").html("没有可选择的菜品");
		}
		
		//在菜品分类绑定选择框点击事件
		$(".panel").find("img").click(function(){
			var panelCheckedStatus = $(this).attr("alt");
			if( panelCheckedStatus == 1){
				$(this).parent().parent().parent().find("input[type=checkbox]").attr("checked",false);
			}
			$(this).parent().parent().parent().find("input[type=checkbox]").click();
			$("#printGroup-add-dialog #group-radio-uncheck").attr("checked",false);
			generalDishTitle("printGroup-add-dialog");
			checkedBoxLength("#printGroup-add-dialog #group-count");
			
		});
		
		
		$("#printGroup-add-dialog input[type='checkbox']").click(function(){
			var selectedDish = parseInt($("#group-count").text());
			if($(this).prop("checked")){
				selectedDish++;
				$("#printGroup-add-dialog").find("input[type='checkbox'][id='"+$(this).attr("id")+"']").prop("checked", true);
			} else {
				selectedDish--;
				$("#printGroup-add-dialog").find("input[type='checkbox'][id='"+$(this).attr("id")+"']").prop("checked", false);
			}
			if(selectedDish==0){
				$("#printGroup-add-dialog #group-count").parent().css("display","none");
			}else{
				$("#printGroup-add-dialog #group-count").parent().css("display","inline");
			}
			$("#printGroup-add-dialog #group-count").text(selectedDish);
			if($("#printGroup-add-dialog #accordion").find("input[type=checkbox]:checked").length==0){
				$("#printGroup-add-dialog #group-radio-uncheck").click();
				$("#printGroup-add-dialog #group-radio-check").attr("checked",false);
			}else if($("#printGroup-add-dialog #accordion").find("input[type=checkbox]:checked").length==$("#printGroup-add-dialog #accordion").find("input[type=checkbox]").length){
				$("#printGroup-add-dialog #group-radio-uncheck").attr("checked",false);
				$("#printGroup-add-dialog #group-radio-check").click();
			}else{
				$("#printGroup-add-dialog #group-radio-uncheck").attr("checked",false);
				$("#printGroup-add-dialog #group-radio-check").attr("checked",false);
			}
			
			generalDishTitle("printGroup-add-dialog");
		});
		var group = $(curGroupObjDiv).attr("groupid");
		var findGroupDishids = findGroupDishidMap.get(group);
		if(findGroupDishids!=null && findGroupDishids.length>0){
			$.each(findGroupDishids, function(key,obj) {
				$("#gdish_"+obj).click();
			});
		}
		if(findGroupDishids!=null && findGroupDishids.length==0){
			$("#printGroup-add-dialog #group-radio-uncheck").click();
			$("#printGroup-add-dialog #group-radio-check").attr("checked",false);
		}else if($("#printGroup-add-dialog #accordion").find("input[type=checkbox]:checked").length==$("#printGroup-add-dialog #accordion").find("input[type=checkbox]").length){
			$("#printGroup-add-dialog #group-radio-uncheck").attr("checked",false);
			$("#printGroup-add-dialog #group-radio-check").click();
		}
		checkedBoxLength("#printGroup-add-dialog #group-count");
		
	});
}
//-------------------------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------------------
function showPrintDel(e)
{
	$(e).find("i.icon-remove").removeClass("hidden");
}
function displayPrintDel(e){
	$(e).find("i.icon-remove").addClass("hidden");
}

function editPrintBox(e){
	$(".modal-title").text("编辑打印配置");
	initPrinter();
	$("#printConfig-add-dialog").modal("show");
	$.ajax({
		type : "post",
		async : false,
		url : global_Path+"/printerManager/findById/"+$(e).attr("id")+".json",
		dataType : "json",
		success : function(result) {
			$("#printerid").val(result.printerid);
			
			$("#printConfig-name").val(result.printername);
			$("#print-bill").find("option[value="+result.printertype+"]").attr("selected",true);
			printerBillChange(result.printertype);
			$("#ipAddress").val(result.ipaddress);
			$("#port").val(result.port);
			//使用printerNo标记字体大小
			var fontSize = result.printerNo;
			$("#fontSize").val(fontSize == null || fontSize == "" ? 1 : fontSize);
			if($("#print-bill").val()==1||$("#print-bill").val()==2){
			$.each( result.areaslistTag, function(i,item){
				areaidListStatus0.push(item.areaid);
				getAreaslistTag.push(item.areaname);
				if(item.status==1){
					areaidListStatus1.push(item.areaid);
				}
			});
			$.each( result.tbPrinterAreaList, function(i,item){
				if(findTableids.indexOf(item.tableid)==-1){
					findTableids.push(item.tableid);
					findTablenames.push(item.tableName);
				}
			});
			}
			if($("#print-bill").val()==1){
				$.each( result.dishTypeslistTag, function(i,item){
					dishTypeIdListTag.push(item.id);
					getDishTypeslistTag.push(item.itemDesc);
					
				});
				
				$.each( result.tbPrinterDetailList, function(i,item){
					if(findDishids.indexOf(item.dishid)==-1){
						findDishids.push(item.dishid);
						findDishnames.push(item.title);
						findDishidsNew.push(item.dishid);
					}
				});
				$.each( result.groupDishList, function(i, item){//groupSequences
//					var key = result.groupSequences[i];
					var key = parseInt(item.groupid);
					var values = item.values;
					var findGroupDishids = [];
					var findGroupDishnames = [];
					$.each( values, function(j, obj){
						findGroupDishids.push(obj.dishid);
						findGroupDishnames.push(obj.title);
					});
					if(findGroupDishidMap.get(key) ==null || findGroupDishidMap.get(key).size()<=0){
						findGroupDishidMap.put(key, findGroupDishids);
						findGroupDishnameMap.put(key, findGroupDishnames);
					}
				});
				showAllSelectStoreDivGroup();
			}

			//打印区域 初始化
			$("#printArea-add-dialog input[type='checkbox']").each(function(){
				var me = $(this);
				if(me.prop("checked") === true) {
					me.prop({"checked":false})
				}
			});
			$.each(findTableids, function(key,obj) {
				$("#table_"+obj).click();
			});

			//打印菜品初始化
			$("#printDishes-add-dialog input[type='checkbox']").each(function(){
				var me = $(this);
				if(me.prop("checked") === true) {
					me.prop({"checked":false})
				}
			});
			$.each(findDishids, function(key,obj) {
				$("#dish_"+obj).click();
			});


			if(!jQuery.isEmptyObject(findTableids)){
//				$("#print-area-add").html(getAreaslistTag.join(","));
				$("#print-area-add").text("已选中"+findTableids.length + "个餐台");
				
			}	
			if(!jQuery.isEmptyObject(findDishids)){
//				$("#print-dishes-add").html(getDishTypeslistTag.join(","));
				$("#print-dishes-add").text("已选中"+findDishids.length + "个菜品");
			}
		}
	});
	showSelectStoreDiv(findTablenames,"#div-print-area-add");
	showSelectStoreDiv(findDishnames,"#div-print-dishes-add");
	
}
function delPrintBox(e){
	$("#printConfig-del-dialog").modal("show");
	$("#printerid").val($(e).parent().attr("id"));

}
function clickFormAddPrintConfig(){
	$.ajax({
		type : "post",
		async : false,
		url: global_Path+"/printerManager/save.json",  
		
		dataType : "json",
		
		data :{
			printerid:$("#printerid").val(),
			printername:$("#printConfig-name").val(),
			printertype:$("#print-bill").val(),
			ipaddress:$("#ipAddress").val(),
			port:$("#port").val(),
			printerNo:$("#fontSize").val()
		},
		
		success : function(result) {
			$("#printerid").val(result.printerid);
//			$.each(selectedAreas);
			if($("#print-bill").val()==1||$("#print-bill").val()==2){
			var checkedtables=$("#printArea-add-dialog #accordion").find("input[type=checkbox]:checked");
			 selectedAreas=[];
			$.each(checkedtables,function(i,obj){
				var a={};
				a.areaid=$(obj).parent().parent().parent().attr("id").split("_")[1];
				a.printerid=$("#printerid").val();
				a.tableid=$(obj).val();
				var imgAlt = $(obj).parents(".panel").find("img").attr("alt");
				if(imgAlt=="2"){
					a.status=1;
				}else if(imgAlt=="0"||imgAlt=="1"){
					a.status=0;
				}
				selectedAreas.push(a);
			});
			addTablesToPrinter(selectedAreas);
			}
			if($("#print-bill").val()==1){
			var checkedDishs=$("#printDishes-add-dialog #accordion").find("input[type=checkbox]:checked");
			selectedDishs=[];
			$.each(checkedDishs,function(i,obj){
				var d={};
				d.dishid=$(obj).val();
				d.printerid=$("#printerid").val();
				d.status=1;
				d.columnid=$(obj).parent().parent().parent().attr("id").split("_")[1];
				var imgAlt = $(obj).parents(".panel").find("img").attr("alt");
				if(imgAlt=="2"){
					d.status=1;
				}else if(imgAlt=="0"||imgAlt=="1"){
					d.status=0;
				}
					selectedDishs.push(d);
			});
			
			//使用选择的菜品，构建一个显示在页面表单中的列表。
			addDishesToPrinter(selectedDishs);
			}
			//保存菜品组合
			if($("#print-bill").val()==1){
				//使用选择的菜品，构建一个显示在页面表单中的列表。
				addGroupDishesToPrinter();
			}
			var text = $("#printConfig-name").val();
			if(result.maessge=="添加成功"){
				
				var html = '<div id='+result.printerid+' class="print-detail-box" onmouseover="showPrintDel(this)" onmouseout="displayPrintDel(this)" ondblclick="editPrintBox(this)">';
					html +='<p class="print-img"><img src="../images/print.png"></p>';
					html += '<p id="printernameShow">'+substrControl(text,18)+'</p><i class="icon-remove hidden" onclick="delPrintBox(this)"></i></div>';
				$("#printConfig-add").before(html);
			}else if(result.maessge=="修改成功"){
				$("#"+$("#printerid").val()+" #printernameShow").text(substrControl(text,18));
			}
		}
	});

	$("#printConfig-add-dialog").modal("hide");
	alert("新增打印机或者修改打印机IP/端口后，需要重启门店服务。");
	
}
function substrControl(dishTitle,titleLength){
	dishTitleLength="";
	dishTitleLength = getStrLength(dishTitle);
	if(dishTitleLength<=titleLength){
		return dishTitle;
	}
	for(var i=titleLength/2;i<titleLength;i++){
		dishTitleLength = getStrLength(dishTitle.substr(0,i));
		if(dishTitleLength>titleLength)	{
			return dishTitle.substr(0,i-1)+"...";
		}
	}
	
	
}
//中文字符判断
function getStrLength(str) { 
    var len = str.length; 
    var reLen = 0; 
    for (var i = 0; i < len; i++) {        
        if (str.charCodeAt(i) < 27 || str.charCodeAt(i) > 126) { 
            // 全角    
            reLen += 2; 
        } else { 
            reLen++; 
        } 
    } 
    return reLen;    
}

function addTablesToPrinter(selectedAreas){
	if(!jQuery.isEmptyObject(selectedAreas)){

// 	ajax插入数据库
	$.ajax({
		type:"post",
		async:false,
		url : global_Path+'/printerManager/addPrinterTables.json',
		contentType:'application/json;charset=UTF-8',
	    data:JSON.stringify(selectedAreas), 
		dataType : "json",
		success : function(result) {
		}
	});
	
	
	}
}
function addDishesToPrinter(selectedDishs){
	if(!jQuery.isEmptyObject(selectedDishs)){

// 	ajax插入数据库
	$.ajax({
		type:"post",
		async:false,
		url : global_Path+'/printerManager/addPrinterDishes.json',
		contentType:'application/json;charset=UTF-8',
	    data:JSON.stringify(selectedDishs), 
		dataType : "json",
		success : function(result) {
		}
	});
	
	
	}
}
//保存菜品组合
function addGroupDishesToPrinter(){
	if(findGroupDishidMap !=null && findGroupDishidMap.size()>0){
		var printerid = $("#printerid").val();
		var keys = findGroupDishidMap.keySet();
		var list = [];
		$.each(keys, function(i, key){
			var dishid = findGroupDishidMap.get(key);
			var obj = {
					printerid: printerid,
					groupsequence: key,
					dishid: dishid
			};
			list.push(obj);
		});
		
		$.ajax({
			type:"post",
			async:false,
			url : global_Path+'/printerManager/addGroupDishes.json',
			contentType:'application/json;charset=UTF-8',
		    data:JSON.stringify(list), 
			dataType : "json",
			success : function(result) {
			}
		});
	}
}
/**
 * 生成菜品选择分类的显示内容
 */
function generalDishTitle(addDialog){
	
	$.each( $("#"+addDialog+" #accordion").find(".panel"), function(i,panel){
		
		var dishs= $(this).find("input[type=checkbox]:checked");
		if(dishs.length > 0){
			if(dishs.length < $(this).find("input[type=checkbox]").length){
				$(this).find("img").attr("alt", "1");
				$(this).find("img").attr("src", global_Path+ "/images/sub_select.png");
			} else {
				$(this).find("img").attr("alt", "2");
				$(this).find("img").attr("src", global_Path+ "/images/all_select.png");
			}
			var panelTitle ='';
			$.each(dishs, function(i){
				panelTitle += $(this).parent().text();
				if(panelTitle.length >= 15){
					panelTitle = panelTitle.substring(0,15)+"...";
					return false;
				}
				if(i < dishs.length -1){
					panelTitle +=",";
				}
				
			});
			panelTitle = "(" + panelTitle + ")";
			if($(this).find("img").attr("alt")==1){
				$(this).find(".dish-label").html(panelTitle);
			}else{
				$(this).find(".dish-label").html("");
			}
			
			
		} else {
			$(this).find("img").attr("alt", "0");
			$(this).find("img").attr("src", global_Path+ "/images/none_select.png");
			//报错定位不准确，要控制变量
			$(this).find(".dish-label").html("");
		}
		
	});
	

}
function dishLabelText(){
	
}
function initPrinter(){
	$("#printerid").val("");
	$("#printConfig-name").val("");
	$("#ipAddress").val("");
	$("#port").val("");
	$("#fontSize").val("1");
	$("#printConfig-name").removeClass("error");
	$("#ipAddress").removeClass("error");
	$("#port").removeClass("error");
	$("#print-area-add").val("");
	$("#print-bill").html("");
	getPrintBillTag();
	$("#print-area-add").html('<i class="icon-plus-sign"></i>');
	$("#print-dishes-add").html('<i class="icon-plus-sign"></i>');
	selectedAreas=[];
	selectedDishs=[];
	findTableids=[];
	findTablenames=[];
	findDishids=[];
	findDishnames=[];
	findDishidsNew=[];//findDishids的拷贝（组合中删除元素的时候使用）
	findGroupDishidMap = new HashMap();
	findGroupDishnameMap = new HashMap();
	getAreaslistTag=[];
	areaidListStatus0=[];
	areaidListStatus1=[];
	getDishTypeslistTag=[];
	dishTypeIdListTag=[];
	$("#print-area").removeClass("hidden");
	$("#print-dishes").removeClass("hidden");
	clearGroup();
	initGroupDiv();
	$("#print-groupdishes").addClass("hidden");
	$(".error").text("");
	$(".popover").remove();
	
}
function getPrintBillTag(){
	var result = [{'value':'1','printBillName':'厨打单'},{'value':'2','printBillName':'客用单'},{'value':'3','printBillName':'预结单'},{'value':'4','printBillName':'结账单'},{'value':'5','printBillName':'称重单'},{'value':'6','printBillName':'并台单'},{'value':'7','printBillName':'换台单'}];
	
	
			$.each(result, function(i,val){  
				
				$("#print-bill").append("<option value ="+val.value+"  class='form-control myInfo-select-addrW'>"+val.printBillName+"</option>");
			});
}
function deletePrinter(){
	
		$.ajax({
			type : "post",
			async : false,
			url : global_Path+"/printerManager/delete/"+$("#printerid").val()+".json",
			dataType : "json",
			success : function(result) {
			window.location.reload();
			}
		});
}
function printerBillChange(text){
	
	if(text =='1'){
		$("#print-font").removeClass("hidden");
		$("#print-area").removeClass("hidden");
		$("#print-dishes").removeClass("hidden");
		$("#print-groupdishes").removeClass("hidden");
	}else if(text ==='2')
	{
		$("#print-font").removeClass("hidden");
		$("#print-area").removeClass("hidden");
		$("#print-dishes").addClass("hidden");
		$("#print-groupdishes").addClass("hidden");
	}else{
		$("#print-font").addClass("hidden");
		$("#print-area").addClass("hidden");
		$("#print-dishes").addClass("hidden");
		$("#print-groupdishes").addClass("hidden");
	}
}
function check_same_printerName(){
	var printerid = $("#printerid").val();
	var printername = $("#printConfig-name").val();
	var flag=true;
	$.ajax({
		type : "post",
		async : false,
		data:{
			printerid:printerid,
			printername:printername,
		
		},
		url : global_Path+"/printerManager/validatePrinter.json",
		dataType : "json",
		success : function(result) {
			if(result.messagePrintername=='打印机名称不能重复'){
			
			$("#printConfig-name_tip").text(result.messagePrintername);
			$("#printConfig-name").focus();
				
			
			flag=false;
						
			}
					
			
		}
			
	});
	return flag;
} 

function check_same_IpAddress(){
	var printerid = $("#printerid").val();
	var printertype = $("#printertype").val();
	var tableAndPrinternameList=[];
	var dishAndPrinternameList=[];
	$.each(findTableids,function(i,item){
		tableAndPrinternameList.push({"tableid":item,"printerid":printerid,"printertype":printertype});
	});
	$.each(findDishids,function(i,item){
		dishAndPrinternameList.push({"dishid":item,"printerid":printerid,"printertype":printertype});
	});
	flagIpAddress=true;
	var list=[];
	list.push(tableAndPrinternameList);
	list.push(dishAndPrinternameList);
	
	$.ajax({
		type : "post",
		async : false,
		url : global_Path+"/printerManager/check_same_IpAddress",
		dataType : "json",
		contentType:'application/json;charset=UTF-8',
		data:
			JSON.stringify(list)
			
		,
		success : function(result) {
			if(result.message=='打印机IP不能重复'){
			
			$("#ipAddress_tip").text(result.message);
			$("#ipAddress").focus();
			flagIpAddress=false;
			$("#printTables-confirm-dialog").modal("show");			
			}
		}
	});
	return flagIpAddress;
}
function changeIPFlag(){
	flagIpAddress = true;
}

function preLoad(names){
	var html='';
	$.each(names, function(i,item){
		html += '<div class="tableOrdiah-detail-box"  >'+item+'</div>';
		
	});
	
	return html;
	
}
function popoverLoadTable(){
	return preLoad(findTablenames);
}
function popoverLoadDish(){
	return preLoad(findDishnames);
}
function checkedBoxLength(addDialog){
	findTDid=[];
	findTDname=[];
	findTDid=findTableAndDish(addDialog.split(" ")[0])[0];
	findTDname=findTableAndDish(addDialog.split(" ")[0])[1];
	if(findTDid.length==0){
		$(addDialog).parent().css("display","none");
		$(addDialog).text(findTDid.length);
	}else{
		$(addDialog).parent().css("display","inline");
		$(addDialog).text(findTDid.length);
	}
	
}
function f_check_IP(){
   var ip = document.getElementById('ipAddress').value;
   var re=/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/;//正则表达式   
   if(re.test(ip)||ip=="")   
   {   
       if( RegExp.$1<256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256||ip==""){
    	   return true;
       } 
          
   }   
  
   return false;    
}
function findTableAndDish(addDialog){
	var checkedtables=$(addDialog).find("input[type=checkbox]:checked");
	var list=[];
	var tableidAndDishidList=[];
	var tablenameAndDishnameList=[];
	$.each(checkedtables,function(i,obj){
		if(tableidAndDishidList.indexOf($(obj).val())==-1){
			tableidAndDishidList.push($(obj).val());
			tablenameAndDishnameList.push($(obj).next("span").text());
		}
	});
	list.push(tableidAndDishidList);
	list.push(tablenameAndDishnameList);
	return list;
}

//鼠标停留按钮显示已选择div
function showSelectStoreDiv(branchnames,selectType){
		if(branchnames.length > 0){
			$(selectType).find("div.popover").remove();
			var ul = $("<ul/>").addClass("storesDiv");
			$.each(branchnames,function(i,item){
				
				ul.append("<li>"+item+"</li>");
			});
			var ileft = iwidth ="";
			if(branchnames.length >= 3){
				iwidth = "460px";
				ileft = "-155px";
				
			}
			var div = $("<div>").addClass("popover fade bottom in").css({
				width : iwidth,
				top : "38px",
				left: ileft
			}).append('<div class="arrow" style="left: 50%;"></div>');
			div.append(ul);
			$(selectType).append(div);
			if(selectType=="#div-print-area-add"){
				$("#print-area-add").text("已选中"+branchnames.length + "个餐台").addClass("selectBranch");
				
			}else if(selectType=="#div-print-dishes-add"){
				$("#print-dishes-add").text("已选中"+branchnames.length + "个菜品").addClass("selectBranch");
			}
			
		}else{
			if(selectType=="#div-print-area-add"){
				$("#print-area-add").html('<i class="icon-plus-sign"></i>');
			}else if(selectType=="#div-print-dishes-add"){
				$("#print-dishes-add").html('<i class="icon-plus-sign"></i>');
			}
			$(selectType).find(".popover").remove();
		}

	$(selectType).hover(function(){
		$(this).find(".popover").show();
	}, function(){
		$(this).find(".popover").hide();
	});
	
}
//鼠标停留按钮显示已选择div---菜品组合
function showAllSelectStoreDivGroup(){
	if(findGroupDishidMap!=null && findGroupDishidMap.size()>0){
		var keys = findGroupDishidMap.keySet();
		//当前最后一个组合id
		keys = keyValueToInt(keys);//排序
		groupid = keys[keys.length-1]+1;//取最后一个加1
		var lastDiv = $("#print-groupdishes").find(".group-div").eq(0);
		$("#print-groupdishes").find(".group-div").eq(0).attr("groupid", groupid);
		$.each(keys, function(i, key){
//			var dishids = findGroupDishidMap.get(key);
			var branchnames = findGroupDishnameMap.get(key);
			if(branchnames.length > 0){
				var	groupdiv = $('<div class="col-xs-2 group-div" style="text-align: left; margin: auto; width: 130px;padding-top: 5px;" groupid="'+key+'">'
						+'<button type="button" style="font-size: 13px;" class="btn btn-default selectBranch required " data-html="true" title=""'
						+'ata-container=""  data-toggle="popover" data-placement="bottom" '
						+'ata-content="" onclick="showGroupDialog(this)">组合'+DX(key)+'</button></div>');
				$(lastDiv).before(groupdiv);
				var ul = $("<ul/>").addClass("storesDiv");
				$.each(branchnames,function(i,item){
					
					ul.append("<li>"+item+"</li>");
				});
				var ileft = iwidth ="";
				if(branchnames.length >= 3){
					iwidth = "460px";
					ileft = "-155px";
				}
				var div = $("<div>").addClass("popover fade bottom in").css({
					width : iwidth,
					top : "38px",
					left: ileft
				}).append('<div class="arrow" style="left: 50%;"></div>');
				div.append(ul);
				groupdiv.append(div);
			}
		});
	}
	$(".group-div").hover(function(){
		$(this).find(".popover").show();
	}, function(){
		$(this).find(".popover").hide();
	});
	
}
//鼠标停留按钮显示已选择div---菜品组合
function showSelectStoreDivGroup(branchnames){
	var group = $(curGroupObjDiv).attr("groupid");
		if(branchnames.length > 0){
			$(curGroupObjDiv).find("div.popover").remove();
			var ul = $("<ul/>").addClass("storesDiv");
			$.each(branchnames,function(i,item){
				
				ul.append("<li>"+item+"</li>");
			});
			var ileft = iwidth ="";
			if(branchnames.length >= 3){
				iwidth = "460px";
				ileft = "-155px";
			}
			var div = $("<div>").addClass("popover fade bottom in").css({
				width : iwidth,
				top : "38px",
				left: ileft
			}).append('<div class="arrow" style="left: 50%;"></div>');
			div.append(ul);
			$(curGroupObjDiv).append(div);
			$(curGroupObjDiv).find("button").text("组合"+DX(group)).addClass("selectBranch");
			
		}else{
			if(group < groupid){
				$(curGroupObjDiv).remove();
			}else{
				$(curGroupObjDiv).find("button").text('+添加组合');
				$(curGroupObjDiv).find(".popover").remove();
			}
		}
		var ids=findGroupDishidMap.get(groupid);
		if(ids!=null && ids.length>0){
			groupid++;
			var htm = '<div class="col-xs-2 group-div" style="text-align: left; margin: auto; width: 130px;padding-top: 5px;" groupid="'+groupid+'">'
				+'<button type="button" style="font-size: 13px;" class="btn btn-default required " data-html="true" title=""'
				+'data-container=""  data-toggle="popover" data-placement="bottom" '
				+'data-content="" onclick="showGroupDialog(this)">+添加组合</button>'
				+'<div class="popover fade bottom in" style="top: 30px; left: -145px; display: none;">'
				+'<div class="arrow"></div>'
				+'<h3 class="popover-title" style="display: none;"></h3>'
				+'<div class="popover-content">'
				+'<div class="tableOrdiah-detail-box"></div>'
				+'<div class="tableOrdiah-detail-box"></div>'
				+'<div class="tableOrdiah-detail-box"></div>'
				+'</div>'
				+'</div><font color="red" id="print-groupdishes-add_tip" class="error"></font></div>';
			$(curGroupObjDiv).parent().append(htm);
		}
	$(curGroupObjDiv).hover(function(){
		$(this).find(".popover").show();
	}, function(){
		$(this).find(".popover").hide();
	});
	
}
function initGroupDiv(){
//	groupid=1;
//	findGroupDishidMap = new HashMap();
//	findGroupDishnameMap = new HashMap();
	var htm = '<div class="col-xs-2 group-div" style="text-align: left; margin: auto; width: 130px;padding-top: 5px;" groupid="1">'
		+'<button type="button" style="font-size: 13px;" class="btn btn-default required " data-html="true" title=""'
		+'data-container=""  data-toggle="popover" data-placement="bottom" '
		+'data-content="" onclick="showGroupDialog(this)">+添加组合</button>'
		+'<div class="popover fade bottom in" style="top: 30px; left: -145px; display: none;">'
		+'<div class="arrow"></div>'
		+'<h3 class="popover-title" style="display: none;"></h3>'
		+'<div class="popover-content">'
		+'<div class="tableOrdiah-detail-box"></div>'
		+'<div class="tableOrdiah-detail-box"></div>'
		+'<div class="tableOrdiah-detail-box"></div>'
		+'</div>'
		+'</div><font color="red" id="print-groupdishes-add_tip" class="error"></font></div>';
	$("#print-groupdishes").html(htm);
}
function clearGroup(){
	$("#print-groupdishes .group-div").remove();
	groupid=1;
	findGroupDishidMap = new HashMap();
	findGroupDishnameMap = new HashMap();
}
function DX(n) {
	if (!/^(0|[1-9]\d*)(\.\d+)?$/.test(n))
		return "数据非法";
	var arr1=['1','2','3','4','5','6','7','8','9','10'];
	var arr2=['一','二','三','四','五','六','七','八','九','十'];
	var dxnum=n;
	if(n<=10){
		var index = arr1.indexOf(n+"");
		dxnum = arr2[index];
	}else if(n<100){
		var sw = (n+"").charAt(0);
		if(sw == 1){
			sw = "十";
		}else{
			var index = arr1.indexOf(sw);
			sw = arr2[index];
		}
		
		var gw = (n+"").charAt(1);
		var index = arr1.indexOf(gw);
		gw = arr2[index];
		dxnum = sw+gw;
	}
	return dxnum;
}
Array.prototype.indexOf = function(val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == val) return i;
    }
    return -1;
};
Array.prototype.remove = function(val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }
};