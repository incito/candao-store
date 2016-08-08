$(document).ready(function(){

	/*优惠信息主页面*/
	$("#nav-preferential-main  li").click(function(){
		$("ul.nav-preferential li").removeClass("active");
		$(this).addClass("active");
		var left= ($(this).prevAll("li").length*14+7)+'%';
		$(this).find("em").css("left",left);
		
		localStorage.currentType = $(this).attr("data-type");
		localStorage.currentPage = "1";
		findData($(this).attr("data-type"), 1);

	});
/*	$("#nav-preferential-main  li").hover(function(){
		var left= ($(this).prevAll("li").length*14+7)+'%';
		$(this).find("em").css("left",left);
		
	});*/
	$(".btn-second").click(function(){
		$("#preferential-add").modal("show")

	});
	$(".btn-first").click(function(){
		$("#preferential-add").modal("show")

	});
		/*颜色选择*/
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
	/*选择指定门店时*/
	$("input[name='applyAll']").click(function(){
		if($(this).val()=='1'){
			$("div.store-select").addClass("hidden");
		}else{
			$("div.store-select").removeClass("hidden");
			
		}

	});
	/*弹框*/
	var num =0;
//	$("#store-select").click(function(){
//		$("#store-select-dialog").modal("show");
//
//	});
	/*点击全选与非全选按钮*/
	$("input[name='store']").click(function(){
		if($(this).val()=='1')
		{
			num = $("table.store-select-content td").length;
			$("table.store-select-content").find("input[type='checkbox']").each(function(i,ch){
				ch.checked = true;
			});
			$("#store-count").text(num);
		}else{
			num=0;
			$("table.store-select-content").find("input[type='checkbox']").each(function(i,ch){
				ch.checked = false;
			});
			$("#store-count").text(num);
		}
	});
	/*点击checkbox*/
	$("table.store-select-content input[type='checkbox']").click(function(){
		if($(this).is(":checked")){
			num++;
		} else {
			num--;
		}
		$("#store-count").text(num);
	});
	// 点击文字选中
	$("table.store-select-content span").click(function(){
		var obj = $(this).prev("input").each(function(i,ch){
			if(ch.checked == false)
			{
				ch.checked = true;
				num++;
			}else{
				ch.checked =false;
				num--;
			}
		});
		$("#store-count").text(num);
	});

	/*点击确认按钮*/
	$("#store-select-confirm").click(function(){
		createSelectBranchesText();
		$("#store-select-dialog").modal("hide");
	});
	
	/*点击取消按钮*/
	$("#store-select-cancel").click(function(){
		$("#store-select-dialog").modal("hide");
	});

	/*团购券 选择团购券*/
	$("input[name='groupType']").click(function(){
		var temp = $(this).val();
		$("div[id*='type']").addClass("hidden");
		$("#type"+temp).removeClass("hidden");


	});
	
	/*更多优惠*/
	$("#nav-preferential-other li").click(function(){
		$("ul.nav-preferential li").removeClass("active");
		$(this).addClass("active");
		var id= $(this).attr("id").substr($(this).attr("id").length-1,1);
		$("div[id*='other-preferential-content']").addClass("hidden");
		$("#other-preferential-content"+id).removeClass("hidden");
		var left= ($(this).prevAll("li").length*50+25)+'%';
		$(this).find("em").css("left",left);


	});
	$("#nav-preferential-other li").hover(function(){
		var left= ($(this).prevAll("li").length*50+25)+'%';
		$(this).find("em").css("left",left);


	});
	/*select */
	$(".select-box").click(function(){
		$(".select-content").toggleClass("hidden");
	});
	$(".select-content-detail").click(function(){
		$(this).parent().prev().find("input").val($(this).text());
		$(this).parent().toggleClass("hidden");

	});
	// 已选门店
	$("#store-select").hover(function(){
		$(this).find(".popover").show();
	}, function(){
		$(this).find(".popover").hide();
	});
	
});
function buildStores(){
	var div = $("<div>").addClass("popover fade bottom in").css({
		display : "block",
		
	}).append('<div class="arrow" style="left: 50%;"></div>');
	return div;
}
function createSelectBranchesText(){
	var storeName= "";
	var selectedBranch = $("table.store-select-content").find("input:checked[type='checkbox']");
	if(selectedBranch.length > 0){
		$("#store-select").find("div.popover").remove();
		var ul = $("<ul/>").addClass("storesDiv");
		$.each(selectedBranch,function(i){
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
		$("#store-select").append(div);
		$("#addstore").text("已选中"+selectedBranch.length + "家店").addClass("selectBranch");;
	}else{
		$("#addstore").html('<i class="icon-plus"></i>选择门店 ').removeClass("selectBranch").next(".popover").remove();
	}
}

Date.prototype.format = function(format) {
	if( this == 'Invalid Date'){ //当时间对象不是一个正确的时间对象的时候，返回空字符串
		return "";
	}
    var o = {
        "M+": this.getMonth() + 1,
        // month
        "d+": this.getDate(),
        // day
        "h+": this.getHours(),
        // hour
        "m+": this.getMinutes(),
        // minute
        "s+": this.getSeconds(),
        // second
        "q+": Math.floor((this.getMonth() + 3) / 3),
        // quarter
        "S": this.getMilliseconds()
        // millisecond
    };
    if (/(y+)/.test(format) || /(Y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
};

function timestampformat(timestamp) {
	return (new Date(timestamp)).format("YYYY-MM-dd");

} 
function deletePreferntial(id, i, name){
	$("#deleteComfirm").modal("show");
	$("#showName").text(name).attr("data-id", id).attr("tr-id", i);
	
}
function doDel(){
	var id = $("#showName").attr("data-id");
	var trid = $("#showName").attr("tr-id");
	$.post(contextPath+"/preferential/delete.json",{"id":id},function(result){
		$("#deleteComfirm").modal("hide");
		if(result.isSuccess){
			$("#promptMsg").html("删除成功");
			$("#successPrompt").modal("show");
			var type = $("#nav-preferential-main").children(".active").attr("data-type");
			var page = $(".page.active").text();
			if($("tbody tr").length == 1){
				if(page != "1" && page !=''){
					page = parseInt(page) - 1;
				}
			}
			if(page == ""){
				page = "1";
			}
			findData(type, page, null);
			window.setTimeout(function(){
				$("#successPrompt").modal("hide");
			}, 1000);
		}else{
			$("#successPrompt .modal-body i").removeClass("icon-success").addClass("icon-fail");
			$("#promptMsg").html("删除失败");
			$("#successPrompt").modal("show");
			window.setTimeout(function(){
				$("#successPrompt").modal("hide");
				$("#successPrompt .modal-body i").removeClass("icon-fail").addClass("icon-success");
			}, 1000);
		}
	},"json");
}

function operaPreferntial(id, type, subType, viewType){
	var isModify = true;
	if(viewType == "look"){
		isModify = false;
	}
	var url;
	if(type == "01"){
		url =  contextPath+"/preferential/toSpecialStamp?id="+id+"&isModify="+isModify;
	} else if(type == "02") {
		url = contextPath+"/preferential/toDiscountCoupon?id="+id+"&isModify="+isModify;
	} else if(type == "03") {
		url = contextPath+"/preferential/toVoucher?id="+id+"&isModify="+isModify;
	} else if(type == "04") {
		url = "";
	} else if(type == "05") {
		url =  contextPath+"/preferential/toGroupBuying?id="+id+"&isModify="+isModify;
	} else if(type == "06" && subType == "0601") {
		/** 
		if(viewType == "look"){
			url = contextPath+"/preferential/toOtherCoupon?id="+id+"&isDetail=true&subType="+subType;
		} else{
			url = contextPath+"/preferential/toOtherCoupon?id="+id+"&isDetail=false&subType="+subType;
		}
		**/
		url = contextPath+"/preferential/toOtherCoupon?id="+id+"&isDetail=false&subType="+subType;
	} else if(type == "06" && subType == "0602") {
		url = contextPath+"/preferential/toOtherCoupon?id="+id+"&isModify="+isModify+"&subType="+subType;
	} else if(type == "07" || type == "08" || type == "09"){
		url = contextPath+"/otherCoupon/getCoupon?id="+id+"&isModify="+isModify;
	}
	window.location.href = url;
}

