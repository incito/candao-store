
var tbPrinterAreaList=[];
var flag_prev =0;
$(document).ready(function(){
	/*餐台分类鼠标滚动*/	
	var dom =$("#nav-tables")[0];
    var user_agent = navigator.userAgent;
    if(user_agent.indexOf("Firefox")!=-1){// Firefox
            dom.addEventListener("DOMMouseScroll",addEvent_T,!1);

    } else if(user_agent.indexOf("MSIE")!=-1){// Firefox
             dom.attachEvent("onmousewheel",addEvent_T,!1);

    }else{
             dom.addEventListener("mousewheel",addEvent_T,!1);

    }	
	
	$("#personNum").change(function(){
		$("#personNum_tip").text("");
	});
	$("#tableName").change(function(){
		$("#tableName_tip").text("");
	});
	$("#areanameB").change(function(){
		$("#areanameB_tip").text("");
	});
	findPrinterArea();
	$("img.img-close").hover(function(){
	 	$(this).attr("src",global_Path+"/images/close-active.png");	 
	},function(){
			$(this).attr("src",global_Path+"/images/close-sm.png");
	});
	 var temp = document.getElementById('nav-tables');
	 	temp.oncontextmenu = function ()
	    {
	        return false;
	    };
	    
	    document.querySelector(".tables-right-tab").oncontextmenu=function(){
	    	return false;
	    };
	    
	showAndHidden();
	$("#add-form1").validate(
			{
				
				submitHandler : function(form) {
					var vcheck = true;
					if ($("#personNum").val().trim() == "") {
						$("#personNum_tip").text("就餐人数不能为空");
						$("#personNum").focus();
						$("#personNum").addClass("error");
						vcheck = false;
					}	
					if ($("#tableName").val().trim() == "") {
						$("#tableName_tip").text("餐台名称不能为空");
						
						$("#tableName").focus();
						$("#tableName").addClass("error");
						vcheck = false;
					}

					if($("#minp").is(":checked") && ($('#minprice').val() === '0' ||  $('#minprice').val() === '')) {
						vcheck = false;
						$('#minConsu_tip').text('必须大于0');
					}

					if($("#fixp").is(":checked") && ($('#fixprice').val() === '0' ||  $('#fixprice').val() === '')) {
						vcheck = false;
						$('#fixConsu_tip').text('必须大于0');
					}

					
					if (vcheck) {
						if(check_validate()){
							save_table();
						}else{
						}
					}
				}
	});
 	$("#add-form2").validate(
			{
				submitHandler : function(form) {
					var vcheck = true;
					if ($("#areanameB").val().trim() == "") {
						$("#areanameB_tip").text("分区名称不能为空");
						 $("#areanameB").focus();
						 $("#areanameB").addClass("error");
						vcheck = false;
					} 
					if (vcheck) {
						if(check_area()){
							save_Area();
						}else{
					}
				}

			}
	});
	$("body").click(function(){

		
		$(".tables-right-tab").addClass("hidden");
	});

	getTableTag();
	getPrinterTag();
//	getPersonNumTag();
	getTabletypeTag();

//	getbuildingNoANDTableTypeTag();
	


	$(".icon-remove").mouseover(function(){
		 $(this).show();
		
	});



//	$("button.tables-type").click(function(e){
//		$(".nav-tables li").removeClass("active");
//		$(this).parent().addClass("active");
//		$(".tables-right-tab").addClass("hidden");
//		stopEvent(window.event||event);
//	});
	


	


	
	 
		/*select */
	$(".select-box").click(function(){
		$(".select-content").toggleClass("hidden");
	});
	$(".select-multi input[type='checkbox']").click(function(){
		var text = '';
		$(".select-multi").find("input[type='checkbox']").each(function(){
			if($(this).is(":checked"))
			{
				text = text+","+$(this).parent().text();
			}
		});
		text = text.substr(1,text.length-1);
		$(".select-multi").prev(".select-box").find("input").val(text)

	});
//	$("#tables-detail-save").click(function(){
//		var name = $("input[name='tablesName']").val();
//		var code = $("input[name='tablesCode']").val();
//		var temp = '<div class="tables-detail-box" onmouseover="delDisplay(this)" onmouseout="delHidden(this)"><p class="tables-detail-name">'+name+'</p><p class="tables-detail-code">（'+code+'）</p><i class="icon-remove hidden" onclick="deltablesDetail(this)"></i></div>';
//		$(".tables-detail-add").before(temp);
//		$("#tables-detailAdd-dialog").modal("hide");
//	});
	
	

	/*菜品口味,菜品标签中添加按钮*/	
	$(".tagAdd-btn button").click(function(){
		var id = $(this).parent().attr("id");
		var num =id.substr(id.length-1,1);
		var text =$("#tagName"+num).val();
		var temp = '<div><button class="btn btn-default" onclick="addTag(this)" type="button">'+text+'</button></div>';
		$("#tables-tag-table"+num).append(temp);

	});
	/*菜品口味弹出框显示*/
	$("#tables-taste-add").click(function(){
		$("#tables-tasteAdd-dialog").modal("show");
	});
	/*菜品口味确定按钮*/
	$("#tables-taste-save").click(function(){
		var text = "";
		$(".tables-tasteAdd-dialog .tables-tag-select").find("button").each(function(){
			 text =text+$(this).text()+",";
		});
		text = text.substr(0,text.length-1);
		$("#tables-taste-add").text(text);
		$("#tables-tasteAdd-dialog").modal("hide");

	});	
	/*菜品标签弹出框显示*/
	$("#tables-label-add").click(function(){
		$("#tables-labelAdd-dialog").modal("show");

	});
	/*菜品标签确定按钮*/
	$("#tables-label-save").click(function(){
		$(".tables-labelAdd-dialog .tables-tag-select").find("button").each(function(){
			var temp = '<label class="checkbox-inline"><input type="checkbox" value="">'+$(this).text()+'</label>';
			$(".tables-checkbox").append(temp);
		});
		$("#tables-labelAdd-dialog").modal("hide");

	});
	/*添加一组计价单位*/
	$(".charge-unit-addBtn").click(function(){
		var temp = '<div class="form-group">';
		temp = temp+'<label class="col-xs-2 control-label">计价单位：</label><div class="col-xs-2"><input type="text" value="" class="form-control"></div>'
		temp = temp+'<label class="col-xs-2 control-label">价格：</label><div class="col-xs-2"><input type="text" value="" class="form-control"></div>';
		temp =temp+'<label class="col-xs-2 control-label">会员价：</label><div class="col-xs-2"><input type="text" value="" class="form-control"><div></div>';
		$(".tables-charge-unit hr").before(temp);
	
	});
	/*添加已有菜品至该分类*/
	$("#tables-other-tab3").click(function(){
		$("#tables-existAdd-dialog").modal("show");
		$(".tables-other-right").addClass("hidden");
	});
	$('.glyphicon').click(function(){
 		if($(this).hasClass('glyphicon-chevron-up')){
 			$(this).removeClass('glyphicon-chevron-up').addClass('glyphicon-chevron-down');
 		}else if($(this).hasClass('glyphicon-chevron-down')){
 			$(this).removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-up');
 		}
 	});
 	$("#tables-existAdd-dialog input[type='checkbox']").click(function(){

 		var existText = '';
 		var panelId = $(this).parent().parent().parent().attr("id");
 		var obj = $("#"+panelId).prev().find(".panel-tableName span");
 		$(this).parent().parent().find("label.checkbox-inline").find("input[type='checkbox']").each(function(){
 			if($(this).is(":checked"))
 			{
 				existText = existText+","+$.trim($(this).parent().text());
 			}
 		});
 		if(obj.next()[0].nodeName == 'SPAN')
 		{
 			obj.next().remove();
 		}
 		var temp = '<span class="tables-existAdd-checked">('+existText.substr(1,existText.length-1)+')</span>';
 		obj.after(temp);

 	});
	/*菜品分类下具体菜品添加*/
	$("#tables-detailMain-Add").click(function(){
		init_object();
		$("#tables-detailAdd-dialog").modal("show");
		$("#editTitle2").text("添加餐台");

	});
/*	$("#tables-detailOther-Add").mousedown(function(e){

		if(e.button == 2){
			$(".tables-other-right").removeClass("hidden")
		}
	
	});*/

	$(".nav-counter-next").click(function(){
		var count = $(".nav-counter").find("li").length;
		if(flag_prev<count-12){
				$(".nav-counter").find("li").eq(flag_prev).css("margin-left","-10%");
				flag_prev++;
		}

	});
	$(".nav-counter-prev").click(function(){
		if(flag_prev>=1){	
			$(".nav-counter").find("li").eq(flag_prev-1).css("margin-left","0px");
			flag_prev--;
		}
	});
	
	
	$("#tables-detailOther-Add").click(function(e){
		$(".tables-other-right").toggleClass("hidden");
	});
		/*select */
	$(".select-box").click(function(){
		$(".select-content").toggleClass("hidden");
	});
	$(".select-multi input[type='checkbox']").click(function(){
		var text = '';
		$(".select-multi").find("input[type='checkbox']").each(function(){
			if($(this).is(":checked"))
			{
				text = text+","+$(this).parent().text();
			}
		});
		text = text.substr(1,text.length-1);
		$(".select-multi").prev(".select-box").find("input").val(text)

	});
	/*双击菜品名称弹出菜品编辑框*/
	$(".counter-detail-box").dblclick(function(){
		doEdit($(this).attr("id"));

	});

	/*菜品口味,菜品标签中添加按钮*/	
	$(".tagAdd-btn button").click(function(){
		var id = $(this).parent().attr("id");
		var num =id.substr(id.length-1,1);
		var text =$("#tagName"+num).val();
		var temp = '<div><button class="btn btn-default" onclick="addTag(this)" type="button">'+text+'</button></div>';
		$("#tables-tag-table"+num).append(temp);

	});
	/*菜品口味弹出框显示*/
	$("#tables-taste-add").click(function(){
		$("#tables-tasteAdd-dialog").modal("show");
	});
	/*菜品口味确定按钮*/
	$("#tables-taste-save").click(function(){
		var text = "";
		$(".tables-tasteAdd-dialog .tables-tag-select").find("button").each(function(){
			 text =text+$(this).text()+",";
		});
		text = text.substr(0,text.length-1);
		$("#tables-taste-add").text(text);
		$("#tables-tasteAdd-dialog").modal("hide");

	});	
	/*菜品标签弹出框显示*/
	$("#tables-label-add").click(function(){
		$("#tables-labelAdd-dialog").modal("show");

	});
	/*菜品标签确定按钮*/
	$("#tables-label-save").click(function(){
		$(".tables-labelAdd-dialog .tables-tag-select").find("button").each(function(){
			var temp = '<label class="checkbox-inline"><input type="checkbox" value="">'+$(this).text()+'</label>';
			$(".tables-checkbox").append(temp);
		});
		$("#tables-labelAdd-dialog").modal("hide");

	});
	/*添加一组计价单位*/
	$(".charge-unit-addBtn").click(function(){
		var temp = '<div class="form-group">';
		temp = temp+'<label class="col-xs-2 control-label">计价单位：</label><div class="col-xs-2"><input type="text" value="" class="form-control"></div>'
		temp = temp+'<label class="col-xs-2 control-label">价格：</label><div class="col-xs-2"><input type="text" value="" class="form-control"></div>';
		temp =temp+'<label class="col-xs-2 control-label">会员价：</label><div class="col-xs-2"><input type="text" value="" class="form-control"><div></div>';
		$(".tables-charge-unit hr").before(temp);
	
	});
	/*添加已有菜品至该分类*/
	$("#tables-other-tab3").click(function(){
		$("#tables-existAdd-dialog").modal("show");
		$(".tables-other-right").addClass("hidden");
	});
	$('.glyphicon').click(function(){
 		if($(this).hasClass('glyphicon-chevron-up')){
 			$(this).removeClass('glyphicon-chevron-up').addClass('glyphicon-chevron-down');
 		}else if($(this).hasClass('glyphicon-chevron-down')){
 			$(this).removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-up');
 		}
 	});
 	$("#tables-existAdd-dialog input[type='checkbox']").click(function(){

 		var existText = '';
 		var panelId = $(this).parent().parent().parent().attr("id");
 		var obj = $("#"+panelId).prev().find(".panel-tableName span");
 		$(this).parent().parent().find("label.checkbox-inline").find("input[type='checkbox']").each(function(){
 			if($(this).is(":checked"))
 			{
 				existText = existText+","+$.trim($(this).parent().text());
 			}
 		});
 		if(obj.next()[0].nodeName == 'SPAN')
 		{
 			obj.next().remove();
 		}
 		var temp = '<span class="tables-existAdd-checked">('+existText.substr(1,existText.length-1)+')</span>';
 		obj.after(temp);

 	});
});

//----------------------------------------------------------------------------------------------

/*禁止浏览器自带的鼠标右键功能*/
window.onload = function ()
{
   
};
function delDisplay(e){	
	$(e).find("i.icon-remove").removeClass("hidden");

}
function delHidden(e){
	$(e).find("i.icon-remove").addClass("hidden");

}
function removeTag(e){
	$(e).parent().remove();
}
function addTag(e){
	var id = $(e).parent().parent().attr("id");
	var num =id.substr(id.length-1,1);
	var text = $(e).text();
	var temp = '<button class="btn btn-default" type="button" onmouseover="delDisplay(this)" onmouseout="delHidden(this)">'+text+'<i onclick="removeTag(this)" class="icon-remove hidden"></i></button>';
	$("#tables-tag-select"+num).append(temp);
	removeTag();
}



/*禁止浏览器自带的鼠标右键功能*/

function delDisplay(e){	
	$(e).find("i.icon-remove").removeClass("hidden");

}
function delHidden(e){
	$(e).find("i.icon-remove").addClass("hidden");

}
function removeTag(e){
	$(e).parent().remove();
}
function addTag(e){
	var id = $(e).parent().parent().attr("id");
	var num =id.substr(id.length-1,1);
	var text = $(e).text();
	var temp = '<button class="btn btn-default" type="button" onmouseover="delDisplay(this)" onmouseout="delHidden(this)">'+text+'<i onclick="removeTag(this)" class="icon-remove hidden"></i></button>';
	$("#tables-tag-select"+num).append(temp);
	removeTag();
}
/*菜品详情删除*/
function deltablesDetail(e)
{
	$("#tables-detailDel-dialog").modal("show");
}


function delDisplay(e){	
	$(e).find("i.icon-remove").removeClass("hidden");

}
function delHidden(e){
	$(e).find("i.icon-remove").addClass("hidden");

}
function removeTag(e){
	$(e).parent().remove();
}
function addTag(e){
	var id = $(e).parent().parent().attr("id");
	var num =id.substr(id.length-1,1);
	var text = $(e).text();
	var temp = '<button class="btn btn-default" type="button" onmouseover="delDisplay(this)" onmouseout="delHidden(this)">'+text+'<i onclick="removeTag(this)" class="icon-remove hidden"></i></button>';
	$("#tables-tag-select"+num).append(temp);
	removeTag();
}
/*菜品详情删除*/
function deltablesDetail(e)
{
	$("#tables-detailDel-dialog").modal("show");
}

/**
 * 在这个删除事件后面调下这个方法，就不会调用按钮事件
 * @param evt
 */
function stoppro(evt){
	var e=evt?evt:window.event; //判断浏览器的类型，在基于ie内核的浏览器中的使用cancelBubble  
	if (window.event) {  
		e.cancelBubble=true;  
	} else {  
	    e.preventDefault(); //在基于firefox内核的浏览器中支持做法stopPropagation  
		e.stopPropagation();  
	}  
}
/*菜品分类拖曳*/
function allowDrop(ev)
{
	ev.preventDefault();
}

function drag(ev)
{
	ev.dataTransfer.setData("Text",ev.target.id);
}
/*分类拖动*/
function drop(ev)
{
	ev.preventDefault();
	var data=ev.dataTransfer.getData("Text");
	var drag_text =document.getElementById(data).parentNode;
	var drag_num = $("#"+data).parent().prevAll().length;
	var drop_text = ev.target.parentNode;
	var drop_num = $("#"+ev.target.id).parent().prevAll().length;
	if(drag_num>drop_num)
	{
		$(drop_text).before(drag_text);

	}else{
		$(drop_text).after(drag_text);
	}
	updateAreaOrder();
}
/**
 * 更新菜品分类的顺序
 */
function updateAreaOrder(){
	var idList=[];
	$("#nav-tables li").each(function(index,item){
		 var temp={};
		  temp.areaid=$(this).attr('id');
		  temp.areaSort=index;
		  idList.push(temp);
		  
	});
//	alert(idList);
	$.ajax({
		type:"post",
		async:false,
		url : global_Path+'/tableArea/updateListOrder.json',
		contentType:'application/json;charset=UTF-8',
	    data:JSON.stringify(idList), 
		dataType : "json",
		success : function(result) {
		}
	});
}

function del() {
	
		$.ajax({
			type : "post",
			async : false,
			url : global_Path+"/table/delete/"+$("#showTableId").val()+".json",
//			url : global_Path + "/dish/delete/"+$("#showDishId").val()+".json",
			dataType : "json",
			success : function(result) {	
				$(".img-close").click();
				oneclickTableType($("#nav-tables .active").attr("id"));			
				var tableNum = $("#nav-tables .active").find("span").eq(1).text().split("(")[1].split(")")[0]-(1);
				$("#nav-tables .active").find("span").eq(1).text("("+tableNum+")");
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert(errorThrown);
			}

		});
	

}
function getTableTag(){
	
	
	$.ajax({
		type : "post",
		async : false,
		url:global_Path + '/table/getTableTag.json',
		dataType : "json",
		success : function(result) {
			
			
			$.each(result, function(i,val){  
				if(val.areaname==null){
					val.areaname="";
				}	
				$(".areaid").append("<option value ="+val.areaid+"  class='form-control myInfo-select-addrW'>"+val.areaname+"</option>");
				
				});
					

		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}

	});
}
function getPrinterTag(){
	$.ajax({
		type : "post",
		async : false,
		url:global_Path + '/table/getPrinterTag.json',
		dataType : "json",
		success : function(result) {

			$.each(result, function(i,val){  
				
				$(".printerid").append("<option value ="+val.printerid+"  class='form-control myInfo-select-addrW'>"+val.printername+"</option>");
			});
		
					

		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}

	});
}
function getPersonNumTag(){
	var result = [{'personNum':'0'},{'personNum':'2'},{'personNum':'4'},{'personNum':'6'},{'personNum':'8'},{'personNum':'10'}];
	
	
			$.each(result, function(i,val){  
				
				$(".personNum").append("<option value ="+val.personNum+"  class='form-control myInfo-select-addrW'>"+val.personNum+"人</option>");
			});
}
function getTabletypeTag(){
	var result = [{'tabletype':'0','tabletypename':'散台'},{'tabletype':'1','tabletypename':'包间'},{'tabletype':'2','tabletypename':'外卖'},{'tabletype':'3','tabletypename':'咖啡外卖'},{'tabletype':'4','tabletypename':'咖啡台'}];
	
	
			$.each(result, function(i,val){  
				
				$(".tabletype").append("<option value ="+val.tabletype+"  class='form-control myInfo-select-addrW'>"+val.tabletypename+"</option>");
			});
}

//保存餐台信息
function save_table() {
	$("#areaid").val($("#nav-tables .active").attr("id"));
	var tableInfo = {};
	$("#tableNo,#tableName,#areaid,#minprice,#fixprice,#personNum,#tableid").each(function(index) {
		tableInfo["" + $(this).attr("name") + ""] = $(this).val();
	});
	$("#tabletype option:selected,#areaid option:selected").each(function(index) {
		tableInfo["" + $(this).parent().attr("name") + ""] = $(this).val();
	});
	$.ajax({
		type:"post",
		async:false,
		url : global_Path+'/table/save',
		contentType:'application/json;charset=UTF-8',
	    data:JSON.stringify(tableInfo),
		dataType : "json",
		success : function(result) {	
			
			$(".img-close").click();
			oneclickTableType($("#nav-tables .active").attr("id"));
			var printeridHave=[];
			var printerHave=[];
			$.each(tbPrinterAreaList,function(i,item){
				if(item.areaid==$("#nav-tables .active").attr("id")&&printeridHave.indexOf(item.printerid) == -1){
					item.tableid=result.tableid;
					printeridHave.push(item.printerid);
					printerHave.push(item);
				}
			});
			if(printerHave!=""&&$("#tableid").val()==""){
				addPrinterArea(printerHave);
			}
			if($("#editTitle2").text() === "添加餐台") {
				var tableNum = $("#nav-tables .active").find("span").eq(1).text().split("(")[1].split(")")[0]-(-1);
				$("#nav-tables .active").find("span").eq(1).text("("+tableNum+")");
				console.log($("#nav-tables .active").find("span").eq(1).text().split("(")[1].split(")")[0]-(-1));
			}

		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}
	});
}
//编辑
function doEdit(id) {
	$("#tables-detailAdd-dialog").modal("show");
	$("#editTitle2").text("编辑餐台");
	$("#table-add").modal("show");
	init_object();
	$.ajax({
		type : "post",
		async : false,
		url : global_Path+"/table/findById/"+id+".json",
		dataType : "json",
		success : function(result) {
			
			$("#tableid").val(result.tableid);					
			$("#tableNo").val(result.tableNo);		
			$("#tabletype  option[value="+result.tabletype+"] ").attr("selected",true);
			$("#personNum").val(result.personNum);
			$("#areaid2").val(result.areaname);
			$("#tableName").val(result.tableName);
			$("#minpriceLable").html('<input type="checkbox" id="minp" onclick="checkit(this.checked,this.id)" >最低消费：');
			if(result.minprice==0.00){
				result.minprice="";
			}else{
//				$("#minp").attr("checked",true);
//				$("#minprice").attr("disabled",false);
				$("#minp").click();
			}
			$("#fixpriceLable").html('<input type="checkbox" id="fixp" onclick="checkit(this.checked,this.id)" >固定使用费：');
			if(result.fixprice==0.00){
				result.fixprice="";
			}else{
//				$("#fixp").attr("checked",true);
//				$("#fixprice").attr("disabled",false);
				$("#fixp").click();
			}
			$("#minprice").val(result.minprice);
			$("#fixprice").val(result.fixprice);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}
	});
}
function init_object(){
// 	tableid,tableNo,tabletype,tableName,personNum,areaid,minprice,fixprice
	$("#minprice").attr("disabled",true);
	$("#fixprice").attr("disabled",true);
	$("#minp").attr("checked",false);
	$("#fixp").attr("checked",false);
	$("#minprice").next("span").css({"color": "#CECECE"});
	$(".minpCheckboxSpan").css({"color": " #CECECE"});
	$("#fixprice").next("span").css({"color": "#CECECE"});
	$(".fixpCheckboxSpan").css({"color": " #CECECE"});
	$("#tableid").val("");
	$("#tableName_tip").text("");
	$("#personNum_tip").text("");
	$('#minConsu_tip').text("");
	$('#fixConsu_tip').text("");
	$("#tabletype").val("");
	$("#tableName").val("");
	$("#personNum").val("");
	$("#areaid").val("");
	$(".printerid").html("");
	$(".tabletype").html("");
	getPrinterTag();
	getTabletypeTag();
	$("#fixprice").val("");
	$("#tableid").val("");
    $("#tableNo").val("");
	$("#minprice").val("");
	$("#buildingNo").val("");

}

/**
 * 单击分类事件
 */
function  oneclickTableType(id){
	$(".nav-counter li").removeClass("active");
	$("#"+id).addClass("active");
	$.ajax({
		url : global_Path + "/table/getTablesByTableType/"+id+".json",
		type : "post",
		datatype : "json",
		contentType : "application/json; charset=utf-8",
		success : function(result) {
			$(".counter-detail-box").remove();
			$.each(result,function(index,item){
				$('#tables-detailMain-Add').before("<div class='counter-detail-box' id='"+item.tableid+"' onmouseover='delDisplay(this)' onmouseout='delHidden(this)'>"+
				"<p  >"+item.tableName+"</p>"+
				"<p  >("+item.personNum+"人桌)</p>"+
				"<i class='icon-remove hidden' onclick='delTablesDetail("+"&apos;"+item.tableid+"&apos;"+","+"&apos;"+item.tableName+"&apos;"+",event)'></i></div>");
				$('#'+item.tableid).dblclick(function(){
					doEdit(item.tableid);
				});
			});
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}
	});
}
/*餐台详情删除*/
function delTablesDetail(tableid,tableName,e)
{
	$("#tables-detailDel-dialog").modal("show");
	$("#showTableName").html(tableName);
	$("#showTableId").val(tableid);
}
function initArea(){
	$("#areanameB").val("");
	$("#areaidB").val("");
	$("#areanameB_tip").text("");
	$(".error").text("");
}
function addArea(){
	initArea();
	 $("#areas-detailAdd-dialog").modal("show");
	 $("#editTitle1").text("添加分区");

}
function editArea(id){
	initArea();
	$("#areas-detailAdd-dialog").modal("show");
	$("#editTitle1").text("编辑分区");
	$("#table-add").modal("show");
	var str=id;
	if(id==null || id==undefined){
		str=$(".nav-counter .active").attr("id");
	}
	$.ajax({
		type : "post",
		async : false,
		url : global_Path+"/tableArea/findById/"+str+".json",
			dataType : "json",
		success : function(result) {
			$("#areaidB").val(result.areaid);					
			$("#areanameB").val(result.areaname);		
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}
	});
}
var areaLength;
function save_Area(){
	var areaInfo = {};
	areaInfo["" + "areaname" + ""] = $("#areanameB").val();
	areaInfo["" + "areaid" + ""] = $("#areaidB").val();
	areaLength= $("#nav-tables").find("li").length+1;
	$.ajax({
		type:"post",
		async:false,
		url : global_Path+'/tableArea/save/'+areaLength+".json",
		contentType:'application/json;charset=UTF-8',
	    data:JSON.stringify(areaInfo),
		dataType : "json",
		success : function(result) {	
			window.location.reload();
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}
	});
}
function check_validate(){
	var TN = $("#tableName").val();
	$("#tableNo").val(TN);
	  	var flag=true;
	$.ajax({
		type : "post",
		async : false,
		data:{
    	    tableid:$("#tableid").val(),
    	    tableName:$("#tableName").val()
		},
		url : global_Path+"/table/validateTable.json",
		dataType : "json",
		success : function(result) {
			if(result.message=='餐台名称不能重复'){
				$("#tableName_tip").text(result.messageDetail);
				$("#tableName").focus();
				flag=false;
			}
			}
		
	});
	return flag;
} 
function check_area(){
	var TN = $("#areanameB").val();
	$("#areaNoB").val(TN);
	  	var flag=true;
	$.ajax({
		type : "post",
		async : false,
		data:{
			areaid:$("#areaidB").val(),
    	    areaname:$("#areanameB").val()
		},
		url : global_Path+"/tableArea/validateArea.json",
		dataType : "json",
		success : function(result) {
			
			if(result.message=='分区名称不能重复'){
				 $("#login").focus();
				$("#areanameB_tip").text(result.message);
				$("#areanameB").focus();
				flag=false;
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}
	});
	return flag;
} 
function delAreaAndTables(){
	$.ajax({
		type:"post",
		async:false,
		url : global_Path+"/table/deleteTablesByAreaid/"+$("#nav-tables .active").attr("id")+".json",
		dataType : "json",
		success : function(result) {	
			delArea();
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}
	});
}
function delArea(){
	$.ajax({
		type:"post",
		async:false,
		url : global_Path+"/tableArea/delete/"+$("#nav-tables .active").attr("id")+".json",
		dataType : "json",
		success : function(result) {	
			updateAreaOrder();
			window.location.reload();
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}
	});
} 
function stopEvent(e){  
    try{  
        if (window.event) {  
            window.event.cancelBubble = true;  
        } else e.stopPropagation();  
    }catch(e){  
        trace("stopEvent error="+e);  
    }  
}
function checkit(isChecked,id){
	 if(isChecked){
		 $("#"+id+"rice").attr("disabled",false);
		 $("#"+id+"rice").next("span").css({"color": "#282828"});
		 $("."+id+"CheckboxSpan").css({"color": "#282828"});
	 }
	 else{
		 $("#"+id+"rice").val('');
		 $("#"+id+"rice").attr("disabled","disabled");
		 $("#"+id+"rice").next("span").css({"color": "#CECECE"});
		 $("."+id+"CheckboxSpan").css({"color": " #CECECE"});
		 
	 }
	}
$("#tables-detailAdd-dialog").click(function(){
	 a(document.body).removeClass("modal-open");
});
$('#tables-detailAdd-dialog').modal('show');
function showAndHidden(){
	var count = $("#nav-tables").find("li").length;

	if(count> parseInt(($('#nav-tables').width() -1 )/ $('#nav-tables li').eq(0).outerWidth())){
		$(".nav-counter-prev").css({"display":"inline"});
		$(".nav-counter-next").css({"display":"inline"});
	}else{
		$(".nav-counter-prev").css({"display":"none"});
		$(".nav-counter-next").css({"display":"none"});
	}
	if(count>0){

	
		$("#tables-detailMain-Add").css({"display":"inline-block"});
		$(".tables-content-title span").text("餐台管理");
	}else{
	

		$("#tables-detailMain-Add").css({"display":"none"});
		$(".tables-content-title span").html("&nbsp;");
	}
	if(count>10){
		$("#dishTypeSelect").css({"height":"365px"});
	}
}
/*v右键弹出框*/
function doMenu(e,_this){
	if(e.button == 2){  
		$(".nav-counter li").removeClass("active");
		$(_this).addClass("active");
		$(".tables-right-tab").addClass("hidden")


		oneclickTableType($(_this).attr("id"));
		var num =0;
		$(_this).prevAll().each(function(){
			if($(_this).css("margin-left") =='0px')
			 	num++;
		});
		var left = num*99/12+'%';	
		console.log(left);
		$(".tables-right-tab").removeClass("hidden");
		$(".tables-right-tab").css("left",left);
		var area_name ="\""+$(_this).text()+"\"";
		$("li#tables-right-tab2 span").text("编辑"+area_name);
		$("li#tables-right-tab3 span").text("删除"+area_name);



	}
} 
function showDeleteArea(){
	
	$("#areas-detailDel-dialog").modal("show");
	$("#showAreaName").html($("#nav-tables .active button").text());
//	$("#showTableId").val(tableid);
}
function hideDialog(){
	$(".img-close").click();
}
function addPrinterArea(list){

	$.ajax({
		type:"post",
		async:false,
		url : global_Path+"/printerManager/addOnePrinterTable.json",
		contentType:'application/json;charset=UTF-8',
		dataType : "json",
		data:JSON.stringify(list), 
		success : function(result) {	
			
			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}
	});
}
function findPrinterArea(){
	$.ajax({
		type:"post",
		async:false,
		url : global_Path+"/printerManager/findPrinterArea.json",
		dataType : "json",
		data:{
    	    status:1,
    	    
		},
		success : function(result) {	
			tbPrinterAreaList=result.tbPrinterAreaList;

		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}
	});
}
function addEvent_T(event) {      
    event=event || window.event;
    
    var type = event.type;
    if (type == 'DOMMouseScroll' || type == 'mousewheel') {
        event.delta = (event.wheelDelta) ? event.wheelDelta / 120 : -(event.detail || 0) / 3;
    }

    var count = $("#nav-tables").children("li").length;		 
	if(event.delta >0){
		if(count-flag_prev>12)
		{
			$("#nav-tables").find("li").eq(flag_prev).css("margin-left","-14%");
			flag_prev++;
		}

	}else{ 
		if(flag_prev>=1){	
			$("#nav-tables").find("li").eq(flag_prev-1).css("margin-left","0");						
			flag_prev--;
		}
	}

	if(document.all){
    	event.cancelBubble = false;
    	return false;
    }else{
    	event.preventDefault();
    }

}
/*自定义餐台*/

var customTable={
	int:function () {
		this.isClick();
		this.getTableJson();
	},
	/*获取所以餐台数据*/
	getTableJson:function () {
		var tmpJson =[];
        $.ajax({
            url: global_Path+'/table/getTypeAndTableMap.json',
            method: 'POST',
            contentType: "application/json",
            dataType: 'json',
            success: function (res) {
                console.log(res)
                $.each(res, function (index, item) {
                    $.each(item, function (key, obj) {
                        var tmpJson1 = JSON.parse(key);
                        //debugger
                        var abc={
                            'areaid':tmpJson1.areaid,
                            'areaname':tmpJson1.areaname,
                            'areaSort':tmpJson1.areaSort,
                            'tables':obj
                        }
                        tmpJson.push(abc);
                    })
                });
            }
        })
		tableJson=tmpJson
	},
	isClick:function () {
		var that=this;
		$('#dinnerTable').click(function () {
			var me=$(this).find('span')
			if(me.text()=='自定义餐台'){
				me.text('保存');
				$('#counter-type-add,#tables-detailMain-Add').hide();
				that.isHide();
				that.areaDrag();
				that.tableDrag();
			}
			else {
				me.text('自定义餐台');
				$('#counter-type-add,#tables-detailMain-Add').show();
				that.isShow();
				that.saveTable();
				$(".nav-counter" ).sortable('disable')
				$(".nav-counter-tab" ).sortable('disable');
			}
		})
	},
	isHide:function () {
		var that=this;
		$('.counter-detail-box').each(function () {
			var me=$.trim($(this).find('p:first-child').text())
			if(me.indexOf('外卖')>-1){
				$(this).hide()
			}
		})
	},
	isShow:function () {
		var that=this;
		$('.counter-detail-box').each(function () {
			var me=$.trim($(this).find('p:first-child').text())
			if(me.indexOf('外卖')>-1){
				$(this).show()
			}
		})

	},
	/*区域拖动*/
	areaDrag:function () {
		var that=this;
		$( ".nav-counter" ).sortable({
			cursor: "move",
			items :"li",                        //只是li可以拖动
			opacity: 1,                       //拖动时，透明度为0.6
			revert: true,                       //释放时，增加动画
			start: function(event, ui) {
				(ui.item).removeAttr('onclick')
			},
			stop:function (event, ui) {
				(ui.item).attr('onclick','oneclickTableType(this.id)')
			},
			update : function(event, ui){       //更新排序之后
				var _thisItem=(ui.item).attr('id')//ui.item当前移动的元素
				var abc=$(this).sortable("toArray");//更新排序后ID的集合
				$.each(abc,function (i, value) {
					$('.ui-sortable-handle').each(function () {
						if ($(this).attr('id')==value){
							$(this).attr('areaSort',i)
						}
					})
				});
				(ui.item).attr('onclick','oneclickTableType(this.id)')
                that.setJson()


			}
		});
	},
	/*餐台拖动*/
	tableDrag:function () {
		var that=this;
		$( ".nav-counter-tab" ).sortable({
			cursor: "move",
			items :"div",                        //只是div可以拖动
			opacity: 1,                       //拖动时，透明度为0.6
			revert: true,                       //释放时，增加动画
			start: function(event, ui) {
				//(ui.item).removeAttr('onclick')
			},
			stop:function (event, ui) {
				//(ui.item).attr('onclick','oneclickTableType(this.id)')
			},
			update : function(event, ui){       //更新排序之后
				var _thisItem=(ui.item).attr('id')//ui.item当前移动的元素
				var abc=$(this).sortable("toArray");//更新排序后ID的集合
				$('.counter-detail-box').each(function (i) {
					if($(this).is(":hidden")){
						abc.splice($.inArray($(this).attr('id'),abc),1);//删除数组中隐藏的外卖咖啡台id
						$(this).attr('position',$('.counter-detail-box').length+i+1)
					}
				})
				 $.each(abc,function (i, value) {
					 $('.counter-detail-box').each(function () {
					 	if($(this).is(":hidden")){
						}
						else {
							if ($(this).attr('id')==value){
								$(this).attr('position',i)
							}
						}
					 })
				 });
				that.setJson()
			}
		});
	},
    setJson:function () {
        for(var i=0;i<tableJson.length;i++){
            $('.nav-counter li').each(function () {
                if(tableJson[i].areaid==$(this).attr('id')){
                    tableJson[i].areaSort=$(this).attr('areaSort')
                    for(var j=0;j<tableJson[i].tables.length;j++){
                        $('.counter-detail-box').each(function () {
                            if(tableJson[i].tables[j].tableid==$(this).attr('id')){
                                tableJson[i].tables[j].position=$(this).attr('position')
                            }
                        })
                    }

                }
            });
        }
        console.log(JSON.stringify(tableJson))
    },
	/*保存自定义餐台*/
	saveTable:function () {
		$.ajax({
			url: global_Path+'/table/saveSortedTypeAndTable.json',
			method: 'POST',
			contentType: "application/json",
			dataType: 'json',
			data:JSON.stringify(tableJson),
			success: function (res) {
				console.log(res)

			},
			error:function (res) {
				console.log(res)
			}
		})
		window.location.reload();
	}

}