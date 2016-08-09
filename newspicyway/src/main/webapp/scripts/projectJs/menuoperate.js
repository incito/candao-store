var isDishLoaded = false;
var isBranchLoaded = false;
var maxCount = 100;  // 菜品介绍可以输入的最大值
var intervalProcess=null;
$(document).ready( function() {
	if($("#only_show").val() == "true"){
		
	}else{
		if($("#menuadd_complete_form") && $("#menuadd_complete_form") != undefined && $("#menuadd_complete_form") != null){
			$("#menuadd_complete_form").validate({
				submitHandler : function(form) {
					
					if (check_validate()) {
						
						var selectBranchs = $("#selectBranchs").val();
						if(selectBranchs==null || selectBranchs == ""){
							$("#selectBranchs_error").css("display", "block");
						}else{
							$("#selectBranchs_error").css("display", "none");
							saveMenu();
						}
					}else{
						$("#sameNameError").css("display", "block");
						$("#addmenu-name").addClass("error");
					}
				}
			});
		}
	}
	
	$("#dishintroduction").on('keyup', function() {
 	    var len = this.value.length;
 	     $("#count").html(maxCount-len);
 	});
	$("input").keyup(function(){
		$(this).removeClass('error');
	});
	
	//选择门店
	$("#addstore").click(function(){
		//参考discount.js
		if(!isBranchLoaded){
			loadBranch();
			isBranchLoaded = true;
		} else {
			loadSelectStore();
			$("#store-select-dialog").modal("show");
		}
	});
	//取消保存
	$("#addmenu-cancel").click(function(){
		if(operate == "create"){
			clearProForm();
		}
		$("#menuAdd-complete-dialog").modal("hide");
	});
	$("#modal_confirm_save .img-close").click(function(){
		//清空计时器
		clearIntervalProcess(intervalProcess);
	});
	$("#modal_confirm_save .btn-cancel").click(function(){
		//清空计时器
		clearIntervalProcess(intervalProcess);
	});
	//确定保存
	$("#dosave_btn").click(function(){
		//清空计时器
		clearIntervalProcess(intervalProcess);
		$("#modal_confirm_save").modal("hide");
		doSave();
	});
	//点击更换菜谱状态
	$("#menuAdd-complete-dialog input[name='status']").click(function(){
		changeMenuStatus($(this).val());
	});
	//点击保存编辑
	$("#menuEdit-complete-btn").click(function(){
		doEdit();
	});
	$("#addstore").hover(function(){
		$(this).find(".icon-plus-sign").css("color", "red");
	}, function(){
		$(this).find(".icon-plus-sign").css("color", "#969696");
	});
});
/**
 * 清空编辑菜谱属性form值
 */
function clearProForm(){
	$("#addmenu-name").removeClass("error");
	$("#sameNameError").css("display", "none");
	$("#menuAdd-complete-dialog input").not("input[name='status']").val("");
	$("#menuAdd-complete-dialog #addstore").html('<i class="icon-plus-sign"></i>').removeClass("selectBranch").next(".popover").remove();
	$("#store-select").find("div.popover").remove();
}
/**
 * 表单验证
 */
var days=0,hours=0,mins=0,secs=0;
function saveMenu(){
	if($("#menuAdd-complete-dialog input[name='status']:checked").val() == 4){//原来是0
		doSave();
	}else{
		var effecttime = $("#effecttime").val();
		var s = ((new Date(effecttime.replace(/-/g,"/"))) - (new Date()));
		var floor = Math.abs(s/1000/60/60/24);
		days = Math.floor(floor);
		$("#how-days").text(days);//天
		var hoursFloor = Math.abs((floor-days)*24);
		hours = Math.floor(hoursFloor);
		$("#how-hours").text(hours);//时
		var minsFloor = Math.abs((hoursFloor-hours)*60);
		mins = Math.floor(minsFloor);
		$("#how-minutes").text(mins);//分
		var secsFloor = Math.abs((minsFloor-mins)*60);
		secs = Math.floor(secsFloor);
		$("#how-seconds").text(secs);//秒
		
		//计时器
		intervalProcess = setInterval("myInterval()",1000);//1000为1秒钟
		$("#menuAdd-complete-dialog").modal("hide");
		$("#modal_confirm_save").modal("show");
	}
}
function myInterval(){
	if(secs > 0){
		secs = secs - 1;
		$("#how-seconds").text(secs);
	}else if(secs == 0){
		//秒数减完，减分钟
		if(mins > 0){
			secs = 59;
			mins = mins - 1;
			$("#how-minutes").text(mins);//分
			$("#how-seconds").text(secs);//秒
		}else if(mins == 0){
			//分钟减完，减小时
			if(hours > 0){
				hours = hours - 1;
				mins = 59;
				secs = 59;
				$("#how-hours").text(hours);//时
				$("#how-minutes").text(mins);//分
				$("#how-seconds").text(secs);//秒
			}else if(hours == 0){
				//小时减完，减天数
				if(days > 0){
					days = days -1;
					hours = 23;
					mins = 59;
					secs = 59;
					$("#how-days").text(days);//天
					$("#how-hours").text(hours);//时
					$("#how-minutes").text(mins);//分
					$("#how-seconds").text(secs);//秒
				}
			}
		}
	}
 }
/**
 * 切换菜谱状态
 * @param status
 */
function changeMenuStatus(status){
	$("#menuAdd-complete-dialog input[name='status'][value="+status+"]").prop("checked",true);
	if(status == 4){
		$("#menuAdd-complete-dialog #effecttime").val("");
		$("#menuAdd-complete-dialog #effecttime_div").addClass("hidden");
	}else{
		$("#menuAdd-complete-dialog #effecttime_div").removeClass("hidden");
	}
}
/**
 * 菜谱编辑提交
 */
function doEdit(){
	var url = global_Path+"/menu/savemenu";
	var templlist = getTmplListJson();
	var menulist = {
		menu: menuobj,
	    menuBranchlist: menubrachobj["branch"],
	    templatelist: templlist
	};
	doPost(url, menulist);
}
/**
 * 菜谱创建、复制提交
 */
function doSave(){
	var url = "";
	var menulist = getMenuJson();
	if(operate == "copy"){
		url = global_Path+"/menu/copyMenu/"+modmenu.menu.menuid+".json";
	}else{
		url = global_Path+"/menu/savemenu";
	}
	doPost(url, menulist);
}
/**
 * 保存菜谱api
 * @param url
 * @param menulist
 */
function doPost(url, menulist){
	$("#alert_msg").html("");
	$.ajax({
		type: "post",
		dataType : "json",
		url : url,
		contentType: "application/json;charset=UTF-8",
	    data: JSON.stringify(menulist),
		success : function(result) {
			if(result.message == "添加成功" || result.message == "修改成功"){
				menuctrl();
			}else if(result.message == "复制成功" ){
				afterCopy(result.menuid);
				$("#menuAdd-complete-dialog").modal("hide");
			}else if(result.message == "修改失败"){
				$("#menuadd-prompt-modal #prop-msg").text(result.message);
				$("#menuadd-prompt-modal").modal("show");
			}else{
				//显示错误信息
				$("#alert_msg").html("<span>"+result.message+"</span>");
			}
		}
	});
}
//进入菜谱管理页面
function menuctrl(){
	$(parent.document.all("detail")).attr("src",global_Path + "/menu/menucontrol");
	$("#allSearch").css("visibility","hidden");
}
//进入门店菜谱管理页面
function menuBranchCtrl(){
	$(parent.document.all("detail")).attr("src",global_Path + "/menu/branchDindex");
	$("#allSearch").css("visibility","hidden");
}
//进入菜品管理
function toDish(){
	$(parent.document.all("detail")).attr("src",global_Path+"/dish/zdindex");
	$("#allSearch").css("visibility","hidden");
}
//进入菜谱查看页面
function viewMenu(menuid){
	$(parent.document.all("detail")).attr("src",global_Path + "/menu/viewmenu?menuId="+ menuid +"");
	$("#allSearch").css("visibility","hidden");
}
//组合菜谱json数据
function getMenuJson(){
	var status = $("#menuAdd-complete-dialog input[name='status']:checked").val();
	var effecttime = "";
	if(status == 2||status ==1){
		effecttime = $("#effecttime").val();
	}
	var menuobj = {
			menuname: $("#addmenu-name").val(),
    		effecttime: effecttime,
    		status: status
	};
	if(operate == "edit"){
		menuobj.menuid = modmenu.menu.menuid;
	}
	menuBranchList = [];
	var selectBranchs = $("#menuAdd-complete-dialog #selectBranchs").val();
	var arr = selectBranchs.split(",");
	for(var i=0; i<arr.length; i++){
		var menuBranch = {};
		menuBranch.branchid = arr[i];
		menuBranchList.push(menuBranch);
	}
	var templlist = [];
	if(operate == "create"){
		templlist = getTmplListJson();
	}else{
		templlist = modmenu.templatelist;
	}
	var menulist = {
    	menu: menuobj,
    	menuBranchlist: menuBranchList,
    	templatelist: templlist
    };
	return menulist;
}
/**
 * 组合模板数据
 * @returns {Array}
 */
function getTmplListJson(){
	var templlist = [];
	for(var key in tmpllist){
		var tmpl = tmpllist[key];
		var detaillist = [];
		var list1 = tmpl.detaillist;
		for(var key1 in list1){
			var detail = list1[key1];
			delete detail["dishtype"];
			var dishunitlist = [];
			var list2 = detail.dishunitlist;
			for(var key2 in list2){
				var dishunit = list2[key2];
				dishunitlist.push(dishunit);
			}
			detail.dishunitlist = dishunitlist;
			detaillist.push(detail);
		}
		tmpl.detaillist = detaillist;
		var columnsort = columnSortMap.get(tmpl.columnid);
		tmpl.columnsort = columnsort;
		templlist.push(tmpl);
	}
	return templlist;
}
/**
 * 菜谱名称不能重复
 * 
 */
function check_validate(){
	var flag=true;
	$.ajax({
		type : "post",
		async : false,
		data:{
    	    menuid:$("#menuidMenu").val(),
    	    menuname:$("#addmenu-name").val()
		},
		url : global_Path+"/menu/validateMenu.json",
		dataType : "json",
		success : function(result) {
			if(result.message=='菜谱名称不能重复'){
//				$("#tableName_tip").text(result.messageDetail);
//				$("#tableName").focus();
				flag=false;
			}
			}
		
	});
	return flag;
} 