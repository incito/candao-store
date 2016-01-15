var operate = "";
var modmenu = null;
var _copysrcobj = null;
var shopTotle = 0;
$(document).ready( function() {
	$("#dish_ctrl").unbind("click").click( function() {
		toDish();
	});
	getAllStore();
	var sorttype = $("input[type='radio']:checked").val();
	initMenuList(sorttype);
	
	//选择排序方式
	$("input:radio[name='sorttype']").unbind("click").click(function(){
		initMenuList($(this).val());
	});
});
//初始化菜谱列表
function initMenuList(sorttype){
	var htm = '';
	$.post(global_Path+"/menu/getMenuList.json", {sorttype: sorttype}, function(menulist){
		console.log(menulist);
		if(menulist.length>0){
			$.each(menulist, function(index, menu){
				htm += combMenusigle(menu);
			});
		}else{
			toDish();
		}
		$(".menuControl-list").html(htm);
		menuAction();
	});
}
/**
 * 菜谱上动作
 */
function menuAction(){
	$(".menuControl-list-sigle").hover(function() {
		$(this).find(".menuControl-list-operate").show();
	}, function() {
		$(this).find(".menuControl-list-operate").hide();
	});
	
	$(".menuControl-list-sigle").not(".menuControl-list-operate").unbind("click").click(function(){
		viewMenu($(this).attr("menuid"));
	});
	
	$(".edit_menu").hover(function(){
		$(this).attr("src", "../images/menuControl-edit-active.png");
	}, function(){
		$(this).attr("src", "../images/menuControl-edit.png");
	});
	$(".del_menu").hover(function(){
		$(this).attr("src", "../images/menuControl-delete-active.png");
	}, function(){
		$(this).attr("src", "../images/menuControl-delete.png");
	});
	$(".copy_menu").hover(function(){
		$(this).attr("src", "../images/menuControl-copy-active.png");
	}, function(){
		$(this).attr("src", "../images/menuControl-copy.png");
	});
	//复制菜谱
	$(".copy_menu").unbind("click").click(function(event){
//		ajaxLoading();
		operate = "copy";
		var menuid = $(this).parent().attr("menuid");
		$("#menuid").val(menuid);
		_copysrcobj = $(this).parent().parent();
		editPage(menuid);
		event.stopPropagation();
	});
	//删除菜谱
	$(".del_menu").unbind("click").click(function(event){
		$("#modal_confirmdel").modal("show");
		var menuname = $(this).parent().attr("menuname");
		$("#showName").html(menuname);
		var menuid = $(this).parent().attr("menuid");
		$("#dodel_btn").unbind("click").click(function(){
			$.post(global_Path+"/menu/deleteMenuById/"+menuid+".json", function(json){
				initMenuList($("input[type='radio']:checked").val());
				$("#modal_confirmdel").modal("hide");
			});
		});
		event.stopPropagation();
	});
	//修改菜谱属性
	$(".edit_menu").unbind("click").click(function(event){
		operate = "edit";
		var menuid = $(this).parent().attr("menuid");
		$("#menuid").val(menuid);
		if($(this).parent().attr("status") == 1){
			$('#menuControl-prompt-modal').modal('show');
			$('#menuControl-ok').unbind("click").click(function() {
				$('#menuControl-prompt-modal').modal('hide');
				editPage(menuid);
			});
		}else{
			editPage(menuid);
		}
		event.stopPropagation();
	});
}
/**
 * 组合每个菜谱在页面展示信息
 * @param menu
 * @returns {String}
 */
function combMenusigle(menu){
	var htm = "";
	htm += '<div class="menuControl-list-sigle" menuid="'+menu.menuid+'">';
	var status = menu.status;
	if(status == 1){
		htm += '<img class="pop_pic" src="../images/menuControl-on.png" alt="">';
	}else if(status == 2){
		htm += '<img class="pop_pic" src="../images/menuControl-timingon.png" alt="">';
	}
	var effecttime = menu.effecttime;
	if(status == 4){
		effecttime = "不启用";
	}else{
		if(effecttime!=null && effecttime!="" && effecttime!=undefined){
			effecttime = effecttime.substring(0, effecttime.length-2);
		}else{
			effecttime = "";
		}
	}
	//门店
	var branchnames = "";
	var branchMap = menu.branchMap;
	var sub_branchnames = "";
	var flag = menu.flag;
	if(flag == "1"){
		sub_branchnames = "全部";
	}else{
		if(branchMap!=null && branchMap.length>0 ){
			$.each(branchMap, function(i, branch){
				branchnames += branch.branchname +",";
			});
			
			if(branchnames != null && branchnames != ""){
				branchnames = branchnames.substring(0, branchnames.length-1);
			}
			if(branchnames.length>15){
				sub_branchnames = branchnames.substring(0, 15)+".......("+branchMap.length+")";
			}else{
				sub_branchnames = branchnames;
			}
		}
	}
	
	htm += '<div class="menuControl-list-content">'
		+ '<p class="menuControl-list-name">'
		+ menu.menuname
		+ '</p>'
		+ '<p>适用门店：'
		+ '<div style="overflow: hidden;cursor: pointer;" title="'+branchnames+'">'
		+ sub_branchnames
		+'</div>'
		+ '</p>'
		+ '</div>'
		+ '<div class="menuControl-list-contentBottom">'
		+ '<p>启用时间<br>'
		+ effecttime
		+ '</p>'
		+ '</div>'
		+ '<div class="menuControl-list-operate" style="display: none" menuid="'+menu.menuid+'" menuname="'
		+ menu.menuname+'" status="'+menu.status+'">'
		+ '<img class="edit_menu" src="../images/menuControl-edit.png" alt="" title="编辑">'
		+ '<img src="../images/menuControl-copy.png" alt="" class="menuControl-img-margin copy_menu" title="复制">';
	if(status != 1){
		htm += '<img class="del_menu" src="../images/menuControl-delete.png" alt="" title="删除">';
	}
	htm += '</div></div>';
	return htm;
}
/**
 * 获取全部门店数量 待用
 */
function getAllStore(){
	$.getJSON(global_Path+"/shopMg/getall.json", function(json){
		if(json != null && json != ""){
			shopTotle = json.length;
		}
	});
}
/**
 * 进入菜谱属性修改页面
 * @param menuid
 */
function editPage(menuid){
	clearProForm();
	if(operate == "copy"){
		$("#menuAdd-complete-dialog .modal-title").text("复制菜谱");
	}else{
		$("#menuAdd-complete-dialog .modal-title").text("菜谱属性设置");
	}
	getMenuById(menuid, function(json){
		modmenu = json;
		var menu = json.menu;
		//菜谱属性信息回填
		var menuname = menu.menuname;
		if(operate == "copy"){
			menuname = "副本_"+menuname;
			$("#menuAdd-complete-dialog #effecttime").val("");
			$("#effecttime_div").addClass("hidden");
			$("input:radio[name='status']").eq(0).prop("checked",false);
			$("input:radio[name='status']").eq(1).prop("checked",true);
		}else{
			if(menu.status == 4){
				$("#menuAdd-complete-dialog #effecttime").val("");
				$("#effecttime_div").addClass("hidden");
				$("input:radio[name='status']").eq(0).prop("checked",false);
				$("input:radio[name='status']").eq(1).prop("checked",true);
			}else{
				$("#effecttime_div").removeClass("hidden");
				$("input:radio[name='status']").eq(0).prop("checked",true);
				$("input:radio[name='status']").eq(1).prop("checked",false);
				var effecttime = menu.effecttime;
				if(effecttime!=null && effecttime != ""){
					effecttime = effecttime.substring(0, effecttime.length-2);
					$("#menuAdd-complete-dialog #effecttime").val(effecttime);
				}
			}
		}
		
		$("#menuAdd-complete-dialog #addmenu-name").val(menuname);
		
		//选择门店
		var branchids = "";
		var branchnames = "";
		$.each(menu.branchMap, function(n, branch){
			branchids += branch.branchid+",";
			branchnames += branch.branchname +",";
		});
		branchids = branchids.substring(0, branchids.length-1);
		$("#menuAdd-complete-dialog #selectBranchs").val(branchids);
		$("#menuAdd-complete-dialog #selectBranchNames").val(branchnames);
		showSelectStoreDiv(branchnames.substring(0, branchnames.length-1));
		$("#menuAdd-complete-dialog").modal("show");
	});
}
/**
 * 通过menuid获取单个menu
 * @param menuid
 * @param callback
 */
function getMenuById(menuid, callback){
	$.getJSON(global_Path+"/menu/getMenuById/"+menuid+".json", function(json){
		callback(json);
	});
}
/**
 * 复制后放在原菜谱后面
 * @param menuid
 */
function afterCopy(menuid){
	getMenuById(menuid, function(data){
		var htm = combMenusigle(data.menu);
		_copysrcobj.after(htm);
		menuAction();
	});
}