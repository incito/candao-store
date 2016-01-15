var selectedAreas=[];
var selectedDishs=[];
var findTableids=[];
var findTablenames=[];
var findDishids=[];
var findDishnames=[];
var getAreaslistTag=[];
var areaidListStatus0=[];
var areaidListStatus1=[];
var getDishTypeslistTag=[];
var dishTypeIdListTag=[];
var flagIpAddress=true;
var findTDid=[];
var findTDname=[];
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
			if(!f_check_IP()){
				$("#ipAddress_tip").text("请输入格式正确的IP");
				vcheck = false;
			}
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
		$("#print-dishes-add_tip").text("");
		var checkedDishs=$("#printDishes-add-dialog #accordion").find("input[type=checkbox]:checked");
		findDishids=[];
		findDishnames=[];
		$.each(checkedDishs,function(i,obj){
			if(findDishids.indexOf($(obj).val())==-1){
				findDishids.push($(obj).val());
				findDishnames.push($(obj).next("span").text());
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
	});
});
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
				}
			});
			}
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
			port:$("#port").val()
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
	getAreaslistTag=[];
	areaidListStatus0=[];
	areaidListStatus1=[];
	getDishTypeslistTag=[];
	dishTypeIdListTag=[];
	$("#print-area").removeClass("hidden");
	$("#print-dishes").removeClass("hidden");
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
		$("#print-area").removeClass("hidden");
		$("#print-dishes").removeClass("hidden");
	}else if(text ==='2')
	{
		$("#print-area").removeClass("hidden");
		$("#print-dishes").addClass("hidden");

	}else{
		$("#print-area").addClass("hidden");
		$("#print-dishes").addClass("hidden");
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