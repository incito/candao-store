var curWeixintype = "2";//默认为微信商户

function weixintypeChange(val){
	if(val == "1") {
		$('.personal-img').show();
		$('.busness').hide();
	} else {
		$('.personal-img').hide();
		$('.busness').show();
	}
	curWeixintype = val;
 }
/**
 * 显示提示信息
 * @param message
 */
function showPromp(message){
	if(message != null && message != ""){
		$("#promptMsg").text(message);
	}
	$("#successPrompt").modal("show");
	window.setTimeout(function(){
		$("#successPrompt").modal("hide");
	}, 1000);
}
/**
 * 微信校验
 */
function checkWeixin(save){
	$.post(global_Path+"/weixin/queryiseffective.json",{
		appid: $("#appid").val(),
		appsecret: $("#appsecret").val(),
		partner: $("#partner").val()
	}, function(result){
		console.log(result);
		if(result.error_code == 1){
			showPromp(result.message);
			return false;
		}
		save();
	},'json');
}
function savePayment(){
	checkWeixin(doSave);
}
/**
 * 保存支付信息
 */
function doSave(){
	var url = "";
	var options = {
			branchid: $("#branchid").val(),
			appid: $("#appid").val(),
			partner: $("#partner").val(),
			appsecret: $("#appsecret").val()
	};
	if(isModify){
		url = global_Path+"/weixin/updateweixinInfo.json";
		options.id = $("#id").val();
	}else{
		url = global_Path+"/weixin/addweixinInfo.json";
	}
	$.post(url, options, function(result){
		console.log(result);
		var code = result.error_code;
		if(code == 0){
			$("#payment-add").modal("hide");
			doSearch();
		}
		showPromp(result.message);
	},'json');
}
/**
 * 新增、修改、查看
 * @param id
 */
function operaPayment(obj, type){
	isModify = false;
	isView = false;
	var id = $(obj).parent().attr("id");
	if(type == "edit" || type == "view"){
		if(type == "edit"){
			isModify = true;
		}else if(type == "view"){
			isView = true;
		}
		$("#id").val(id);
		$.post(global_Path+"/weixin/queryweixinInfo.json", {
			id: id
		}, function(result){
			var code = result.error_code;
			if(code == 0){
				var obj = result.result;
				$("#id").val(obj.id);
				$("#branchid").val(obj.branchid);
				getBranchNameById(obj.branchid, false);
				$("#appid").val(obj.appid);
				$("#partner").val(obj.partner);
				$("#appsecret").val(obj.appsecret);
				$("#weixintype" + obj.weixintype).get(0).checked = true;
				weixintypeChange(obj.weixintype);
				$("#status" + obj.status).get(0).checked = true;


				if(obj.personweixinurl !== "") {
					$(".uploadImg").attr({"src":obj.personweixinurl});
					$('.personal-img').removeClass('img-default');
				}
			}
		},'json');
	}
	initDialog(type);
}
function initDialog(type){
	$("input.popover-errorTips").removeClass("popover-errorTips");
	$(".popover-errorTips").text("");
	$('#add-paymentform')[0].reset();
	if(type == "add" || type == "edit"){
		$(".payment-dialog input, select, textarea").prop("disabled", false).css("disabled", "");
		$("#add-btn").removeClass("hide");
		$("#edit-btn").addClass("hide");
		$(".img-op").show();
	}else if(type == "view"){
		$(".payment-dialog input, select, textarea").prop("disabled", true).css("disabled", "disabled");
		$("#add-btn").addClass("hide");
		$("#edit-btn").removeClass("hide");
		$(".img-op").hide();
	}
	if(type == "add"){
		weixintypeChange("2");
		$('.personal-img').addClass('img-default').find('.uploadImg').attr("src",'../images/upload-img.png');
	}
	$("#payment-add").modal("show");
}
function detail2edit(){
	isModify = true;
	isView = false;
	$(".payment-dialog input, select, textarea").prop("disabled", false).css("disabled", "").off();
	$("#add-btn").removeClass("hide");
	$("#edit-btn").addClass("hide");
}
/**
 * 确认删除
 * @param id
 */
function deletePayment(id){
	$("#delid").val(id);
	$("#deleteComfirm").modal("show");
}
/**
 * 删除
 */
function doDel(){
	$.post(global_Path+"/weixin/delete.json", {
		id: $("#delid").val()
	}, function(result){
		console.log(result);
		var code = result.error_code;
		if(code == 0){
			$("#deleteComfirm").modal("hide");
			doSearch();
		}
	},'json');
}
/**
 * 数据操作 分页查询
 */
function doSearch() {
	var page;
	if (window.localStorage) {
		page = typeof (localStorage._currentPage) == "undefined" ? "1"
				: localStorage._currentPage;
	} else {
		page = "1";
	}
	getPaymentList(page);
}
/**
 * 数据操作页面-获取门店信息
 * 
 * @param subPage
 */
function getPaymentList(subPage) {
	showPrompt();
	$.post(global_Path + "/weixin/page.json",{
		page : subPage,
		rows : 10,
		branchid : $("#searchbranchid").val(),
		branchname: $("#searchbranchname").val(),
		partner: $("#searchpartner").val()
	},function(result) {
		console.log(result);
		var tbody = "";
		if (result != null) {
			if (result.rows != null && result.rows.length > 0) {
				$.each(result.rows, function(i, row) {
					var current = parseInt(result.current);
					var nums = 10*(current-1)+(i+1);
					tbody += '<tr>'
						+ '<td>'+nums+'</td>'
						+ '<td>'+row.branchid+'</td>'
						+ '<td>'+row.branchname+'</td>'
						+ '<td>'+row.partner+'</td>'
						+ '<td>'+row.appid+'</td>'
						+ '<td>'+row.appsecret+'</td>'
						+ '<td>'+(row.status === '1' ? '启用' : '禁用')+'</td>'
						+ '<td class="td-last">'
						+ '<div class="operate" id="'+row.id+'">'
						+ '<a href="javascript:void(0)" onclick="operaPayment(this, \'view\')">查看</a>';
					if(isbranch=='N'){
						tbody += '<a href="javascript:void(0)" onclick="operaPayment(this, \'edit\')">修改</a>'
							+ '<a href="javascript:void(0)" class="deleteBtn" onclick="deletePayment(this)">删除</a>';
					}
					tbody += '</div></td>'
						+ '</tr>';
				});
			}
			$("#payment-table tbody").html(tbody);
			$(".pagingWrap").find("ul.paging").remove();
			if (result.total > 10
					&& $("ul.paging").length == 0) {
				$(".pagingWrap").html(
						'<ul class="paging clearfix">');
				$(".paging").twbsPagination({
					totalPages : result.pageCount,
					visiblePages : 7,
					startPage : parseInt(subPage),
					first : '...',
					prev : '<',
					next : '>',
					last : '...',
					onPageClick : function(event, page) {
						localStorage._currentPage = page;
						getPaymentList(page);
					}
				});

			} else if (result.total <= 10) {
				$(".pagingWrap").empty();
			}
		}
		hidePrompt();
	},'json');
	hidePrompt();
}
/**
 * show loading
 * @param msg
 */
function showPrompt(msg){
	if(msg!=null && msg!=""){
		$("#prompt-msg").text(msg);
	}
	$("#prompt-dialog").modal("show");
}
/**
 * hide Loading
 */
function hidePrompt(){
	$("#prompt-dialog").modal("hide");
}
/**
 * 根据门店Id查询门店名称
 */
function getBranchNameById(branchid, isAll){
	var option = {};
	if(branchid != null && branchid != ""){
		option.branchid = branchid;
	}
	$.post(global_Path+"/weixin/queryNameByBranchid.json", option, function(result){
		console.log(result);
		if(result!=null && result.length>0){
			if(isAll){
				var options = "";
				$.each(result, function(i, branch){
					options += '<option value="'+branch.branchname+'">'+branch.branchname+'</option>';
				});
				$("#branchname").html(options);
			}else{
				//获取某一个
				$("#branchname").val(result[0].branchname);
			}
		}
		
	},"json");
}
/**
 * 根据门店名称查询门店id
 */
function getBranchIdByName(branchname, isAll){
	var option = {};
	if(branchname != null && branchname != ""){
		option.branchname = branchname;
	}
	$.post(global_Path+"/weixin/queryBranchidByName.json", option, function(result){
		console.log(result);
		if(result!=null && result.length>0){
			if(isAll){
				var options = "";
				$.each(result, function(i, branch){
					options += '<option value="'+branch.branchid+'">'+branch.branchid+'</option>';
				});
				$("#branchid").html(options);
			}else{
				//获取某一个
				$("#branchid").val(result[0].branchid);
			}
		}
	},'json');
}