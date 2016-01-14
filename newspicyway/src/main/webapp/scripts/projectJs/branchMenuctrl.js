var operate = "";
var modmenu = null;
$(document).ready( function() {
	$("#dish_ctrl").click( function() {
		toDish();
	});
	var sorttype = $("input[type='radio']:checked").val();
	initMenuList(sorttype);
	
	//选择排序方式
	$("input:radio[name='sorttype']").click(function(){
		initMenuList($(this).val());
	});
});
//初始化菜谱列表
function initMenuList(sorttype){
	$.post(global_Path+"/menu/getMenuList.json", {sorttype: sorttype}, function(menulist){
		var htm = "";
		if(menulist.length>0){
			$.each(menulist, function(index, menu){
				htm += '<div class="menuControl-list-sigle" menuid="'+menu.menuid+'">';
				var status = menu.status;
				if(status == 1){
					htm += '<img class="pop_pic" src="../images/menuControl-on.png" alt="">';
				}else if(status == 2){
					htm += '<img class="pop_pic" src="../images/menuControl-timingon.png" alt="">';
				}
				var effecttime = menu.effecttime;
				if(status == 0){
					effecttime = "不启用";
				}else{
					effecttime = effecttime.substring(0, effecttime.length-2);
				}
				//门店
				var branchnames = "";
				var branchMap = menu.branchMap;
				if(branchMap!=null && branchMap.length>0){
					$.each(branchMap, function(i, branch){
						branchnames += branch.branchname +",";
					});
				}
				if(branchnames.endsWith(",")){
					branchnames = branchnames.substring(0, branchnames.length-1);
				}
				var sub_branchnames = "";
				if(branchnames.length>15){
					sub_branchnames = branchnames.substring(0, 15)+".......("+branchMap.length+")";
				}else{
					sub_branchnames = branchnames;
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
					;
				htm += '</div>';
			});
		}else{
			toDish();
		}
		$(".menuControl-list").html(htm);
		$(".menuControl-list-sigle").hover(function() {
			$(this).find(".menuControl-list-operate").show();
		}, function() {
			$(this).find(".menuControl-list-operate").hide();
		});
		
		$(".menuControl-list-sigle").not(".menuControl-list-operate").click(function(){
			viewMenu($(this).attr("menuid"));
		});
		//修改菜谱属性
		$(".edit_menu").click(function(event){
			operate = "edit";
			var menuid = $(this).parent().attr("menuid");
			if($(this).parent().attr("status") == 1){
				$('#menuControl-prompt-modal').modal('show');
				$('#menuControl-ok').click(function() {
					$('#menuControl-prompt-modal').modal('hide');
					editPage(menuid);
				});
			}else{
				editPage(menuid);
			}
			event.stopPropagation();
		});
		
		//复制菜谱
		$(".copy_menu").click(function(event){
			operate = "copy";
			var menuid = $(this).parent().attr("menuid");
			editPage(menuid);
			event.stopPropagation();
		});
		//删除菜谱
		$(".del_menu").click(function(event){
			$("#modal_confirmdel").modal("show");
			var menuname = $(this).parent().attr("menuname");
			$("#showName").html(menuname);
			var menuid = $(this).parent().attr("menuid");
			$("#dodel_btn").click(function(){
				$.post(global_Path+"/menu/deleteMenuById/"+menuid+".json", function(json){
					initMenuList();
					$("#modal_confirmdel").modal("hide");
				});
			});
			event.stopPropagation();
		});
	});
}
function editPage(menuid){
	$("#menuAdd-complete-dialog").modal("show");
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
			if(menu.status == 0){
				$("#menuAdd-complete-dialog #effecttime").val("");
				$("#effecttime_div").addClass("hidden");
				$("input:radio[name='status']").eq(0).prop("checked",false);
				$("input:radio[name='status']").eq(1).prop("checked",true);
			}else{
				$("#effecttime_div").removeClass("hidden");
				$("input:radio[name='status']").eq(1).prop("checked",false);
				$("input:radio[name='status']").eq(0).prop("checked",true);
				var effecttime = menu.effecttime.substring(0, menu.effecttime.length-2);
				$("#menuAdd-complete-dialog #effecttime").val(effecttime);
			}
		}
		
		$("#menuAdd-complete-dialog #addmenu-name").val(menuname);
		
		//选择门店
		var branchids = "";
		var branchnames = "";
		console.log(json);
		$.each(menu.branchMap, function(n, branch){
			branchids += branch.branchid+",";
			branchnames += branch.branchname +",";
		});
		branchids = branchids.substring(0, branchids.length-1);
		$("#menuAdd-complete-dialog #selectBranchs").val(branchids);
		$("#menuAdd-complete-dialog #selectBranchNames").val(branchnames);
		showSelectStoreDiv(branchnames.substring(0, branchnames.length-1));
	});
}
function getMenuById(menuid, callback){
	$.getJSON(global_Path+"/menu/getMenuById/"+menuid+".json", function(json){
		callback(json);
	});
}
//进入菜品管理
function toDish(){
	$(parent.document.all("detail")).attr("src",global_Path+"/dish/branchDindex");
	$("#allSearch").css("visibility","hidden");
}