var isBranchLoaded = false;
$(document).ready(function() {
	$("#addstore").click(function(){
		if(!isBranchLoaded){
			loadBranch();
			isBranchLoaded = true;
		} else {
			loadSelectStore();
			$("#store-select-dialog").modal("show");
		}
	});
});
function selBranch(){
	if(applyAllShop){
		$("input[type='radio'][name=applyAll][value='1']").attr("checked",true);
	} else {
		$("input[type='radio'][name=applyAll][value='0']").attr("checked",true);
		$("div.store-select").removeClass("hidden");
		createBranch();
		var selectedId = "";
		if(selectShops.length > 0){
			var ul = $("<ul/>").addClass("storesDiv");
			$.each(selectShops, function(i, shop){
				var storeName = shop.branch_name;
				selectedId += shop.branch;
				ul.append("<li>"+storeName+"</li>");
				if(i < selectShops.length -1){
					selectedId +=",";
				}
			});
			$("#selectBranchs").val(selectedId);
			var top = "", ileft = "", iwidth ="";
			if(selectShops.length >= 3){
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
			$("#addstore").text("已选中"+selectShops.length + "家店").addClass("selectBranch");
		}else{
			$("#addstore").html('<i class="icon-plus"></i>选择门店 ').removeClass("selectBranch").next(".popover").remove();
		}
	}
}
function cancel(){
	$("#othercoupon-form").reset();
}

var othercouponInfo ={};
/**
 * 保存信息
 * @returns {Boolean}
 */
function saveOtherCoupon(){
	if(!$("#othercoupon-form").valid()){
		return false;
	}
	othercouponInfo.type = $("#freetype").val();
	othercouponInfo.id=$("#id").val();
	othercouponInfo.color=$("#color").val() == "" ? "#9cc05b" : $("#color").val();
	othercouponInfo.apply_all=$("input[name='applyAll']:checked").val();
	if(freeType == "07"){//手工优免
		othercouponInfo.name=$("#name").val();
		othercouponInfo.free_reason=$("input[name='free_reason']:checked").val();
	}else if(freeType == "08"){//合作单位
		othercouponInfo.discount=$("#intl_discount").val();
		othercouponInfo.can_credit=$("input[name='can_credit']:checked").val();
		othercouponInfo.free_reason=$("input[name='intl_free_reason']:checked").val();
		othercouponInfo.amount=$("#dis_amount").val();
		othercouponInfo.name=$("#compnay-name").val();
	}if(freeType == "09"){//在线支付
		othercouponInfo.name=$("#name").val();
		othercouponInfo.discount=$("#discount").val();
		othercouponInfo.payway = $("input[name='payway']:checked").val();
	}
	if(othercouponInfo.apply_all == 0){
		var branchInfos = [];
		$.each($("table.store-select-content").find("input:checked[type='checkbox']"),function(i,obj){
			var branch = {};
			branch.branchId=$(this).val();
			branch.branchName=$(this).next().html();
			branchInfos.push(branch);
		});
		othercouponInfo.branchInfos = JSON.stringify(branchInfos);
		//检测是否选择了门店
		if(branchInfos == null || branchInfos.length<=0){
			$("#selectBranchs_error").css("display", "");
			return false;
		}else{
			$("#selectBranchs_error").css("display", "none");
		}
	}
	othercouponInfo.starttime=$("#starttime").val();
	othercouponInfo.endtime=$("#endtime").val();
	othercouponInfo.activityIntroduction=$("#activityIntroduction").val();
	othercouponInfo.useNotice=$("#useNotice").val();
	console.log(JSON.stringify(othercouponInfo));
	$.post(global_Path+"/otherCoupon/addOtherCoupon.json", othercouponInfo, 
			function(result){
		console.log(result);
		var code = result.code;
		if(code == 1){
			$("#promptMsg").html("保存成功");
			$("#successPrompt").modal("show");
			window.setTimeout(function(){
				$("#successPrompt").modal("hide");
				window.location.href=global_Path+ "/preferential";
			}, 1000);
		}
	},'json');
}
/**
 * 打开选择门店窗口
 */
function loadBranch(){
	createBranch();
	$("#store-select-dialog").modal("show");
}
/**
 * 获取门店信息
 */
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
			selectedStore.each(function(){
				ids.push( $(this).val() );
			});
			$("#selectBranchs").val( ids.join(",") );
			//隐藏提示信息
			$("#selectBranchs_error").css("display", "none");
		});
		
	});
	/*点击全选与非全选按钮*/
	$("input[name='checkAll']").click(function(){
		var num;
		if($(this).val()=='1'){
			num = $("table.store-select-content td").length;
			$("table.store-select-content").find("input[type='checkbox']").each(function(i,ch){
				ch.checked = true;
			});
			uploadStoreCount();
		}else{
			num=0;
			$("table.store-select-content").find("input[type='checkbox']").each(function(i,ch){
				ch.checked = false;
			});
			uploadStoreCount( );
		}
	});
	
}

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

function loadSelectStore(){
	//清除选中状态/重置状态
	$("table.store-select-content input[type='checkbox']").prop("checked", false);
	//如果当前已经有选择的门店，需要将选择的门店，重新在页面显示为选中的状态
	if( $("#selectBranchs").val() != ""){
		$.each( $("#selectBranchs").val().split(","),function(i,obj){
			num++;
			$("table.store-select-content input[type='checkbox'][value='"+obj+"']").prop("checked", true);
		});
	}
	uploadStoreCount();
}

//切换优免类型
function changeFreeType(value) {
	freeType = value;
	if (value == "07") {
		//手工优免
		$(".discount-div").addClass("hide");
		$(".internal-free-div").addClass("hide");
		$(".payway-div").addClass("hide");
		$(".company-div").addClass("hide");
		$(".coup-type-div").removeClass("hide");
		$(".name-div").removeClass("hide");
	} else if (value == "08") {
		//内部优免
		$(".coup-type-div").addClass("hide");
		$(".discount-div").addClass("hide");
		$(".payway-div").addClass("hide");
		$(".name-div").addClass("hide");
		$(".internal-free-div").removeClass("hide");
		$(".company-div").removeClass("hide");
		controlIntlHide(cooperationNnit);
	} else if (value == "09") {
		//在线支付
		$(".coup-type-div").addClass("hide");
		$(".internal-free-div").addClass("hide");
		$(".company-div").addClass("hide");
		$(".name-div").removeClass("hide");
		$(".discount-div").removeClass("hide");
		$(".payway-div").removeClass("hide");
	}
}
//切换内部优免的活动类型
function selIntlFree(obj) {
	var value = $(obj).find("input").val();
	controlIntlHide(value);
}
//切换内部优免的活动类型 显示隐藏不同的属性
function controlIntlHide(value) {
	if (value == 0) {
		//折扣
		$(".intl-disamount-div").addClass("hide");
		$(".intl-discount-div").removeClass("hide");
	} else if (value == 1) {
		//减免
		$(".intl-discount-div").addClass("hide");
		$(".intl-disamount-div").removeClass("hide");
	} else if (value == 2) {
		$(".intl-discount-div").addClass("hide");
		$(".intl-disamount-div").addClass("hide");
	}
}
/**
* 获取优免类型
*/
function getFreeType(){
	$.getJSON(global_Path+"/otherCoupon/getTypeDict.json", function(result){
		console.log(result);
		var code = result.code;
		if(code == 1){
			var resultList = result.resultList;
			if(resultList != null && resultList.length>0){
				var option = "";
				$.each(resultList, function(i, data){
					option += '<option value="'+data.code+'">'+data.name+'</option>';
				});
				$("#freetype").html(option);
				if(freeType == null || freeType == ""){
					freeType = "07";
				}
				$("#freetype").val(freeType);
			}
		}
	});
}
//支付类型
function getPayway(){
	$.getJSON(global_Path+"/otherCoupon/getPaywayType.json", function(result){
		console.log(result);
		var code = result.code;
		if(code == 1){
			var resultList = result.resultList;
			if(resultList != null && resultList.length>0){
				var radioHtm = "";
				$.each(resultList, function(i, data){
					radioHtm += '<label class="radio-inline ">';
					radioHtm += '<input type="radio" name="payway" value="'+data.code+'">'+data.name;
					radioHtm += '</label>';
				});
				$("#payway-div").html(radioHtm);
				if(payway){
					$("input[type='radio'][name=payway][value='1']").prop("checked", true);
				}else{
					$("input[type='radio'][name=payway][value='0']").prop("checked", true);
				}
			}
		}
	});
}
//手动优免活动方式
function getActivityType(){
	$.getJSON(global_Path+"/otherCoupon/getActivityType.json", function(result){
		console.log(result);
		var code = result.code;
		if(code == 1){
			var resultList = result.resultList;
			if(resultList != null && resultList.length>0){
				var radioHtm = "";
				$.each(resultList, function(i, data){
					radioHtm += '<label class="radio-inline ">';
					radioHtm += '<input type="radio" name="free_reason" value="'+data.code+'">'+data.name;
					radioHtm += '</label>';
				});
				$("#activity-type-div").html(radioHtm);
				if(activityType == null || activityType == ""){
					activityType = "0";
				}
				$("input[type='radio'][name=free_reason][value='"+activityType+"']").prop("checked", true);
			}
		}
	});
}
//合作单位活动方式
function getCooperationNnit(){
	$.getJSON(global_Path+"/otherCoupon/getCooperationNnit.json", function(result){
		console.log(result);
		var code = result.code;
		if(code == 1){
			var resultList = result.resultList;
			if(resultList != null && resultList.length>0){
				var radioHtm = "";
				$.each(resultList, function(i, data){
					radioHtm += '<label class="radio-inline " onclick="selIntlFree(this)">';
					radioHtm += '<input type="radio" name="intl_free_reason" value="'+data.code+'">'+data.name;
					radioHtm += '</label>';
				});
				$("#cooperation-unit-div").html(radioHtm);
				if(cooperationNnit == null || cooperationNnit == ""){
					cooperationNnit = "0";
				}
				$("input[type='radio'][name=intl_free_reason][value='"+cooperationNnit+"']").prop("checked", true);
			}
		}
	});
}