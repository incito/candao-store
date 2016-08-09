//将弹出的选择门店的层中，更新选择的门店数量的方法抽象出来。当选择的门店为0个的时候，标题显示“选择门店”，否则显示“ 选择门店（已选1家店）”
function uploadStoreCount(){
	var count=$("table.store-select-content").find("input[type='checkbox']:checked").length;
	if( count !=0){
		$("#store-count").parent().html("选择门店（已选<font id='store-count'>"+count+"</font>家店）");
	}else{
		$("#store-count").parent().html("选择门店<font id='store-count'></font>");
	}
	//更新全选与全不选的按钮状态
	var branchLen = $("table.store-select-content").find("input[type='checkbox']:checked").length;
	if(branchLen == 0){
		$("input[name='checkAll'][value='0']").prop("checked", true);
		$("input[name='checkAll'][value='1']").prop("checked", false);
	} else if(branchLen == $("table.store-select-content").find("input[type='checkbox']").length ){
		$("input[name='checkAll'][value='0']").prop("checked", false);
		$("input[name='checkAll'][value='1']").prop("checked", true);
	} else {
		$("input[name='checkAll'][value='0']").prop("checked", false);
		$("input[name='checkAll'][value='1']").prop("checked", false);
	}
	
}
function loadBranch(){
	createBranch();
	$("#store-select-dialog").modal("show");
}
function loadSelectStore(){
	//清除选中状态/重置状态
	$("table.store-select-content input[type='checkbox']").prop("checked", false);
	//如果当前已经有选择的门店，需要将选择的门店，重新在页面显示为选中的状态
	if( $("#selectBranchs").val() != ""){
		$.each( $("#selectBranchs").val().split(","),function(i,obj){
			num++;
			$("table.store-select-content input[type='checkbox'][value='"+obj+"']").prop("checked", true);
		});
	}else{
		$("table.store-select-content").find("input[type='checkbox']").each(function(i,ch){
			ch.checked = false;
		});
	}
	uploadStoreCount();
}
function createBranch(){
	//从数据库加载所有门面
	$.getJSON(global_Path+"/shopMg/getall.json", function(json){
		$("table.store-select-content").html();
		var html="<tr>";
		$.each(json, function(i, obj) {
			html=html+" <td>"
				+"<input type='checkbox' value="+obj.branchid+"><span>"+obj.branchname+"</span>"
				+"</td>";
			if( (i+1)%4==0){//因为计数从0开始，所以要加一个才能显示正常
				html+="</tr><tr>";
			}
		});
		html=html+"</tr>";
		$("table.store-select-content").html(html);
		num = 0;
		loadSelectStore();
		//重新初始化 门店点击事件
		$("table.store-select-content input[type='checkbox']").click(function(){
			uploadStoreCount();
		});
		//选中门店名字，也可以发生变化
		// 点击文字选中
		$("table.store-select-content span").click(function(){
			$(this).prev("input[type='checkbox']").trigger("click");
		});
		
		//绑定确定按钮。当点击确定的时候，保存选择的门店。
		$("#store-select-confirm").click(function(){
			var selectedStore=$("table.store-select-content").find("input[type='checkbox']:checked");
			var ids=new Array();
			var names = new Array();
			selectedStore.each(function(){
				ids.push( $(this).val() );
				names.push($(this).next().text());
			});
			$("#selectBranchs").val( ids.join(",") );
			$("#selectBranchNames").val( names.join(",") );
			
			showSelectStoreDiv();
			$("#store-select-dialog").modal("hide");
		});
		/*
		$("#store-select").hover(function(){
			$(this).find(".popover").show();
		}, function(){
			$(this).find(".popover").hide();
		});
		*/
		/*点击取消按钮*/
		$("#store-select-cancel").click(function(){
			$("#store-select-dialog").modal("hide");
		});
	});
	/*点击全选与非全选按钮*/
	$("input[name='checkAll']").click(function(){
		if($(this).val()=='1'){
			$("table.store-select-content").find("input[type='checkbox']").each(function(i,ch){
				ch.checked = true;
			});
			uploadStoreCount();
		}else{
			$("table.store-select-content").find("input[type='checkbox']").each(function(i,ch){
				ch.checked = false;
			});
			uploadStoreCount();
		}
	});
}
//鼠标停留按钮显示已选择门店div
function showSelectStoreDiv(branchnames){
	if(operate == "create" || branchnames == null || branchnames==""){
		var selectedBranch = $("table.store-select-content").find("input:checked[type='checkbox']");
		$("#store-select").find("div.popover").remove();
		if(selectedBranch.length > 0){
			var ul = $("<ul/>").addClass("storesDiv");
			$.each(selectedBranch,function(i){
				var name = $(this).next("span").text();
				ul.append("<li>"+name+"</li>");
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
			$("#store-select").append(div);
			$("#addstore").text("已选中"+selectedBranch.length + "家店").addClass("selectBranch");
			$("#selectBranchs_error").css("display", "none");
		}else{
			$("#addstore").html('<i class="icon-plus-sign"></i>').removeClass("selectBranch").next(".popover").remove();
		}
	}else{
		var selectBranchs = $("#selectBranchs").val();
		var selectBranchs_arr = selectBranchs.split(",");
		var branchnames_arr = branchnames.split(",");
		if(selectBranchs_arr.length > 0){
			$("#store-select").find("div.popover").remove();
			var ul = $("<ul/>").addClass("storesDiv");
			$.each(selectBranchs_arr,function(i){
				var name = branchnames_arr[i];
				ul.append("<li>"+name+"</li>");
			});
			var ileft = iwidth ="";
			if(selectBranchs_arr.length >= 3){
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
			$("#addstore").text("已选中"+selectBranchs_arr.length + "家店").addClass("selectBranch");;
		}
	}
	$("#store-select").hover(function(){
		$(this).find(".popover").show();
	}, function(){
		$(this).find(".popover").hide();
	});
	
}