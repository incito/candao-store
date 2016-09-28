var selectedPrinters=[];
var findTableids=[];
var findTablenames=[];
var getAreaslistTag=[];
var areaidListStatus0=[];
var areaidListStatus1=[];
var findTDid=[];
var findTDname=[];

$(document).ready(function(){
	$("input").keyup(function(){
		  $(this).next(".error").text("");
	});

	initPosList();
	$("#add-form-posprintConfig").validate({
		submitHandler : function(form) {
			var vcheck = true;
			if($("#printer-add").find("i").attr("class")=="icon-plus-sign"){
				$("#printer-add_tip").text("请配置打印机");
				vcheck = false;
			}
			if (vcheck) {
				clickFormAddPrintConfig();
			}
		}
	});
	/*新增POS机弹出框*/
	$("#pos-printConfig-add").click(function(){
		initPrinter();
		$(".modal-title").text("新增POS");
		$("#pos-printConfig-add-dialog").modal("show");
	});

	//绑定反选功能
	$("#printer-add-dialog #table-radio-uncheck").click(function(){
		$("#printer-add-dialog #accordion").find("input[type=checkbox]").prop("checked",false);
		$("#printer-add-dialog #accordion").find(".dish-label").html('');
		checkedBoxLength("#printer-add-dialog #table-count");
		$("#printer-add-dialog #table-radio-check").attr("checked",false);
	});
	//绑定全选功能
	$("#printer-add-dialog #table-radio-check").click(function(){
		$("#printer-add-dialog #accordion").find("input[type=checkbox]").prop("checked",true);
		checkedBoxLength("#printer-add-dialog #table-count");
		$("#printer-add-dialog #table-radio-uncheck").attr("checked",false);
	});
	/* 配置打印机按钮点击 */
	$("#printer-add").click(function(){
		$("#printer-add-dialog").modal("show");
		$("printer-add-dialog #accordion").html("数据正在加载中......");
		
		//解析获取到的打印机数据
		// TODO -------------------------
		var html="";
		for(var i=0; i< 50; i++){
			html +=" <label class='checkbox-inline col-xs-3'><input type='checkbox' id='table_"
				+ i+"'  value='"+i+"' data-title='打印机"+i
				+"' code='"+i+"'><span>"+substrControl("打印机"+i,12)+"</span></label>";
		}
			$("#printer-add-dialog #accordion .panel-body").html(html);
			$("#printer-add-dialog #accordion").children(":first").find(".panel-collapse").addClass("in");
			
			
			$("#printer-add-dialog input[type='checkbox']").click(function(){
				var selected = parseInt($("#printer-add-dialog #table-count").text());
				if($(this).prop("checked")){
					selected++;
					$("#printer-add-dialog").find("input[type='checkbox'][id='"+$(this).attr("id")+"']").prop("checked", true);
				} else {
					selected--;
					$("#printer-add-dialog").find("input[type='checkbox'][id='"+$(this).attr("id")+"']").prop("checked", false);
				}
				if(selected==0){
					$("#printer-add-dialog #table-count").parent().css("display","none");
					
				}else{
					$("#printer-add-dialog #table-count").parent().css("display","inline");
				}
				$("#printer-add-dialog #table-count").text(selected);
				
				if($("#printer-add-dialog #accordion").find("input[type=checkbox]:checked").length==0){
					$("#printer-add-dialog #table-radio-uncheck").click();
					$("#printer-add-dialog #table-radio-check").attr("checked",false);
				}else if($("#printer-add-dialog #accordion").find("input[type=checkbox]:checked").length==$("#printer-add-dialog #accordion").find("input[type=checkbox]").length){
					$("#printer-add-dialog #table-radio-uncheck").attr("checked",false);
					$("#printer-add-dialog #table-radio-check").click();
				}else{
					$("#printer-add-dialog #table-radio-uncheck").attr("checked",false);
					$("#printer-add-dialog #table-radio-check").attr("checked",false);
				}
			});
			if($("#printer-add-dialog #accordion").find("input[type=checkbox]:checked").length==0){
				$("#printer-add-dialog #table-radio-uncheck").click();
				$("#printer-add-dialog #table-radio-check").attr("checked",false);
			}else if($("#printer-add-dialog #accordion").find("input[type=checkbox]:checked").length==$("#printer-add-dialog #accordion").find("input[type=checkbox]").length){
				$("#printer-add-dialog #table-radio-uncheck").attr("checked",false);
				$("#printer-add-dialog #table-radio-check").click();
			}else{
				$("#printer-add-dialog #table-radio-uncheck").attr("checked",false);
				$("#printer-add-dialog #table-radio-check").attr("checked",false);
			}
			$.each(findTableids, function(key,obj) {
				$("#table_"+obj).click();
			});
			checkedBoxLength("#printer-add-dialog #table-count");
	});
	
	/*打印配置内容添加*/
	$("#printConfig-save").click(function(){
		var text = $("#printConfig-name").val();
		var html = '<div class="print-detail-box" onmouseover="showPrintDel(this)" onmouseout="displayPrintDel(this)" ondblclick="editPosPrintBox(this)">';
			html +='<p class="print-img"><img src="../images/print.png"></p>';
			html += '<p id="printernameShow">'+substrControl(text,10)+'</p><i class="icon-remove hidden" onclick="delPosPrintBox(this)"></i></div>';
		$("#pos-printConfig-add").before(html);
		$("#pos-printConfig-add-dialog").modal("hide");
	});
	//绑定确定按钮
	$("#printer-add-dialog #print-area-confirm").click(function(){
		checkedBoxLength("#printer-add-dialog #table-count");
		$("#printer-add_tip").text("");
		findTableids=findTDid;
		findTablenames=findTDname;
		
		var checkedAreas=$("#printer-add-dialog #accordion").find(".panel-heading img");
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
		$("#printer-add-dialog").modal("hide");
		showSelectStoreDiv(findTablenames,"#div-printer-add");
	});
});
/**
 * 初始化列表
 */
function initPosList(){
	// TODO -------------------------
	$.ajax({
		type:"post",
		async:false,
		url : global_Path+"/printerManager/find.json",
		contentType:'application/json;charset=UTF-8',
		dataType : "json",
		success : function(result) {
			$.each(result, function(i,val){
				$("#pos-printConfig-add").before('<div class="print-detail-box" id='+val.printerid+'  onmouseover="showPrintDel(this)" onmouseout="displayPrintDel(this)" ondblclick="editPosPrintBox(this)"><p class="print-img" ><img src="../images/print.png"></p><p id="printernameShow">'+substrControl(val.printername,18)+'</p>	<i class="icon-remove hidden" onclick="delPosPrintBox(this)"></i></div>');
			});
		}
	});
}
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
//-------------------------------------------------------------------------------------------------------
/**
 * 显示删除标签
 */
function showPrintDel(e)
{
	$(e).find("i.icon-remove").removeClass("hidden");
}
function displayPrintDel(e){
	$(e).find("i.icon-remove").addClass("hidden");
}
/**
 * 编辑
 * @param e
 */
function editPosPrintBox(e){
	// TODO -------------------------
	$(".modal-title").text("编辑POS");
	initPrinter();
	$("#pos-printConfig-add-dialog").modal("show");
	$.ajax({
		type : "post",
		async : false,
		url : global_Path+"/printerManager/findById/"+$(e).attr("id")+".json",
		dataType : "json",
		success : function(result) {
			$("#printerid").val(result.printerid);

			$("#posid").val(result.printername);
			$("#posname").val(result.ipaddress);
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

			//打印区域 初始化
			$("#printer-add-dialog input[type='checkbox']").each(function(){
				var me = $(this);
				if(me.prop("checked") === true) {
					me.prop({"checked":false});
				}
			});
			$.each(findTableids, function(key,obj) {
				$("#table_"+obj).click();
			});


			if(!jQuery.isEmptyObject(findTableids)){
				$("#printer-add").text("已选中"+findTableids.length + "个餐台");

			}
		}
	});
	showSelectStoreDiv(findTablenames,"#div-printer-add");

}
/**
 * 删除POS dialog
 * @param e
 */
function delPosPrintBox(e){
	$("#posprint-del-dialog").modal("show");
	$("#printerid").val($(e).parent().attr("id"));

}
/**
 * 保存POS
 */
function clickFormAddPrintConfig(){
	// TODO -------------------------
	alert("do save");
	var checkedtables=$("#printer-add-dialog #accordion").find("input[type=checkbox]:checked");
	selectedPrinters=[];
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
		selectedPrinters.push(a);
	});
	/*
	$.ajax({
		type : "post",
		async : false,
		url: global_Path+"/printerManager/save.json",
		dataType : "json",
		data :{
			printerid:$("#printerid").val(),
			posid:$("#posid").val(),
			posname:$("#posname").val()
		},
		success : function(result) {
			$("#printerid").val(result.printerid);
			var text = $("#posname").val();
			if(result.maessge=="添加成功"){
				var html = '<div id='+result.printerid+' class="print-detail-box" onmouseover="showPrintDel(this)" onmouseout="displayPrintDel(this)" ondblclick="editPosPrintBox(this)">';
					html +='<p class="print-img"><img src="../images/print.png"></p>';
					html += '<p id="printernameShow">'+substrControl(text,18)+'</p><i class="icon-remove hidden" onclick="delPosPrintBox(this)"></i></div>';
				$("#pos-printConfig-add").before(html);
			}else if(result.maessge=="修改成功"){
				$("#"+$("#printerid").val()+" #printernameShow").text(substrControl(text,18));
			}
		}
	});*/

	$("#pos-printConfig-add-dialog").modal("hide");

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
/**
 * 初始化dialog控件
 */
function initPrinter(){
	$("#printerid").val("");
	$("#posid").val("");
	$("#posname").val("");
	$("#posid").removeClass("error");
	$("#posname").removeClass("error");
	$("#printer-add").val("");
	$("#printer-add").html('<i class="icon-plus-sign"></i>');
	selectedPrinters=[];
	findTableids=[];
	getAreaslistTag=[];
	areaidListStatus0=[];
	areaidListStatus1=[];
	$(".error").text("");
	$(".popover").remove();

}
/**
 * 确定删除POS机配置
 */
function deletePosPrinter(){
	// TODO -------------------------
	alert("do delete");
	/*
	$.ajax({
		type : "post",
		async : false,
		url : global_Path+"/printerManager/delete/"+$("#printerid").val()+".json",
		dataType : "json",
		success : function(result) {
			window.location.reload();
		}
	});*/
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
		if(selectType=="#div-printer-add"){
			$("#printer-add").text("已配置"+branchnames.length + "个打印机").addClass("selectBranch");
		}
	}else{
		if(selectType=="#div-printer-add"){
			$("#printer-add").html('<i class="icon-plus-sign"></i>');
		}
		$(selectType).find(".popover").remove();
	}

	$(selectType).hover(function(){
		$(this).find(".popover").show();
	}, function(){
		$(this).find(".popover").hide();
	});

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