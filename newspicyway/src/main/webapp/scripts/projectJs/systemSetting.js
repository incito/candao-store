$(function() {
$("#test").click(function(){
	$.ajax({
		url:global_Path+"/padinterface/getSystemSetData.json",
		type : "post",
		datatype : "json",
		data:{ "type":"ROUNDING"},
//		contentType : "application/json; charset=utf-8",
		success : function(data) {
		}});
	});
	$.widget("ui.timespinner", $.ui.spinner, {
		options : {
			// seconds
			step : 60 * 1000,
			// hours
			page : 60
		},
		_parse : function(value) {
			if (typeof value === "string") {
				// already a timestamp
				if (Number(value) == value) {
					return Number(value);
				}
				return +Globalize.parseDate(value);
			}
			return value;
		},

		_format : function(value) {
			return Globalize.format(new Date(value), "t");
		}
	});

	Globalize.culture('de-DE');
	$(".time").timespinner();
	doGet();
	initComplaint();
	initResponsetime();
	getAllGifts();
	
	// 点击营业时间编辑
	$("#editBussiness").click(function() {
		$(this).addClass("hide");
		$("#saveBussiness").removeClass("hide");
		$("#retrieveBuss").find("div.edit_info").removeClass("hide");
		$("#retrieveBuss").find("div.show_info").addClass("hide");
		$(".time-label").css("margin-left","-5");
	});
	// 点击服务密码 编辑
	$("#editPadPwd").click(function() {
		$(this).addClass("hide");
		$("#savePadPwd").removeClass("hide");
		$("#retrievePwd").find("div.edit_info").removeClass("hide");
		$("#retrievePwd").find("div.show_info").addClass("hide");
	});

	// 点击餐具设置 编辑
	$("#editDishes").click(function() {
		$(this).addClass("hide");
		$("#saveDishes").removeClass("hide");
		$("#retrieveDish").find("div.edit_info").removeClass("hide");
		$("#retrieveDish").find("div.show_info").addClass("hide");
	});
	// 点击服务员响应时间 编辑
	$("#editTimes").click(function() {
		$(this).addClass("hide");
		$("#saveTimes").removeClass("hide");
		$("#retrieveResponsetime").find("div.edit_info").removeClass("hide");
		$("#retrieveResponsetime").find("div.show_info").addClass("hide");
	});
	// 点击忌口设置 编辑
	$("#editAvoid").click(function() {
		$(this).addClass("hide");
		$("#saveAvoid").removeClass("hide");
		$("#avoid-list .avoid-li").attr("disabled", false);
		if($("#avoid-list .avoid-li").not("#avoid-add").length < 5){
			//最多只能添加5个忌口
			$("#avoid-add").removeClass("hide");
		}
	});
	
	// 点击退菜原因设置 编辑
	$("#editReturn").click(function() {
		$(this).addClass("hide");
		$("#saveReturn").removeClass("hide");
		$("#reason-list .avoid-li").attr("disabled", false);
		if($("#reason-list .avoid-li").not("#reason-add").length < 5){
			//最多只能添加5个退菜原因
			$("#reason-add").removeClass("hide");
		}
	});
	//点击投诉原因 编辑
	$("#editComplaint").click(function() {
		$(this).addClass("hide");
		$("#saveComplaint").removeClass("hide");
		$("#complaint-list .avoid-li").attr("disabled", false);
	});
	
	$("#reason-add").click(function() {
		$("#reason_name").val("");
		$(".addreason-input-div").removeClass("hide");
		$("#reason-add").addClass("hide");
	});

	$("#avoid-add").click(function() {
		$("#avoid_name").val("");
		$(".addavoid-input-div").removeClass("hide");
		$("#avoid-add").addClass("hide");
	});
	
	
	// 点击呼叫服务员类型设置 编辑
	$("#editRounding").click(function() {
		$(this).addClass("hide");
		$("#saveRounding").removeClass("hide");
		$("#rounding").attr("disabled", false);
		$("#accuracy").attr("disabled", false);
	
	});
	// 点击零头处理方式 编辑
	$("#editCalltype").click(function() {
		$(this).addClass("hide");
		$("#saveCalltype").removeClass("hide");
		$("#calltype-list .avoid-li").attr("disabled", false);
		if($("#calltype-list .avoid-li").not("#calltype-add").length < 5){
			//最多只能添加5个退菜原因
			$("#calltype-add").removeClass("hide");
		}
	});
	$("#calltype-add").click(function() {
		$("#calltype_name").val("");
		$(".addcalltype-input-div").removeClass("hide");
		$("#calltype-add").addClass("hide");
	});
	

	/**
	 * 修改营业时间
	 */
	$("#saveBussiness").click(function() {
		var list = [];
		var allobj = {
				dictid : $("#bussallid").val(),
				item_desc : "全天",
				begin_time : $("#all_begintime_input").val(),
				date_type : $("#all_date_type").val(),
				end_time : $("#all_endtime_input").val()
		};
		list.push(allobj);
		var lunchobj = {
				dictid : $("#busslunchid").val(),
				item_desc : "午市",
				begin_time : $("#lunch_begintime_input").val(),
				date_type : $("#lunch_date_type").val(),
				end_time : $("#lunch_endtime_input").val()
		};
		list.push(lunchobj);
		var dinnerobj = {
				dictid : $("#bussdinnerid").val(),
				item_desc : "晚市",
				begin_time : $("#dinner_begintime_input").val(),
				date_type : $("#dinner_date_type").val(),
				end_time : $("#dinner_endtime_input").val()
		};
		list.push(dinnerobj);
		var type = $("#busstype").val();
		doPost(type, list, function(data){
			$("#saveBussiness").addClass("hide");
			$("#editBussiness").removeClass("hide");
			$("#retrieveBuss").find("div.edit_info").addClass("hide");
			$("#retrieveBuss").find("div.show_info").removeClass("hide");
			$(".time-label").css("margin-left","-33");
			initData(data, 0);
		});
	});
	/**
	 * 修改密码
	 */
	$("#savePadPwd").click( function() {
		var obj = {
				dictid : $("#passid").val(),
				item_desc : $("#service_pass").val()
			};
		var type = $("#passtype").val();
		doPost(type, obj, function(data){
			$("#savePadPwd").addClass("hide");
			$("#editPadPwd").removeClass("hide");
			$("#retrievePwd").find("div.edit_info").addClass("hide");
			$("#retrievePwd").find("div.show_info").removeClass("hide");
			initData(data, 1);
		});
	});
	
	/**
	 * 修改忌口设置
	 */
	$("#saveAvoid").click(function() {
		var type = $("#avoidtype").val();
		var avoidArr = [];
		$("#avoid-list .avoid-li").not("#avoid-add").each(function(){
			var dictid = $(this).attr("dictid");
			var text = $(this).text();
			var avoid = {
					dictid: dictid,
					item_desc: text,
					itemSort:$(this).index()
			};
			avoidArr.push(avoid);
		});
		doPost(type, avoidArr, function(data){
			$("#avoid-list").find(".dishTasteUl").last().attr("dictid",data.SPECIAL[data.SPECIAL.length-1].dictid);
			$("#saveAvoid").addClass("hide");
			$("#editAvoid").removeClass("hide");
			$("#avoid-list .avoid-li").attr("disabled", true);
			$("#avoid-add").addClass("hide");
			$(".addavoid-input-div").addClass("hide");
		});
	});
	/**
	 * 修改退菜原因
	 */
	$("#saveReturn").click(function() {
		var type = $("#reasontype").val();
		var reasonArr = [];
		$("#reason-list .avoid-li").not("#reason-add").each(function(){
			var dictid = $(this).attr("dictid");
			var text = $(this).text();
			var reason = {
					dictid: dictid,
					item_desc: text,
					itemSort:$(this).index()
			};
			reasonArr.push(reason);
		});
		doPost(type, reasonArr, function(data){
			$("#reason-list").find(".dishTasteUl").last().attr("dictid",data.RETURNDISH[data.RETURNDISH.length-1].dictid);
			$("#saveReturn").addClass("hide");
			$("#editReturn").removeClass("hide");
			$("#reason-list .avoid-li").attr("disabled", true);
			$("#reason-add").addClass("hide");
			$(".addreason-input-div").addClass("hide");
		});
	});
	/**
	 * 修改投诉原因
	 */
	$("#saveComplaint").click(function(){
		var type = $("#complaintstype").val();
		var reasonArr = [];
		$("#complaint-list .avoid-li").not("#complaint-add").each(function(){
			var dictid = $(this).attr("dictid");
			var text = $(this).text();
			var reason = {
					dictid: dictid,
					itemDesc: text,
					itemSort:$(this).index()
			};
			reasonArr.push(reason);
		});
		if(reasonArr == null || reasonArr.length==0){
			$("#saveComplaint").addClass("hide");
			$("#editComplaint").removeClass("hide");
			return false;
		}
		$.ajax({
			type: "post",
			dataType : "json",
			url : global_Path + "/system/updateComplainDicData",
			contentType: "application/json;charset=UTF-8",
		    data: formatJson(type, reasonArr),
			success : function(result) {
				if (result.code == "SUCCESS") {
//					$("#complaint-list").find(".dishTasteUl").last().attr("dictid",data.RETURNDISH[data.RETURNDISH.length-1].dictid);
					$("#saveComplaint").addClass("hide");
					$("#editComplaint").removeClass("hide");
					$("#complaint-list .avoid-li").attr("disabled", true);
					$("#complaint-add").addClass("hide");
					$(".addreason-input-div").addClass("hide");
				}else{
					alert("保存失败");
				}
			}
		});
	});
	/**
	 * 修改服务员响应时间
	 */
	$("#saveTimes").click(function(){
		var type = $("#responsetimetype").val();
		var reasonArr = [];
		var reason1 = {
				dictid: $("#paytimeid").val(),
				itemDesc: $("#pay-time").val()
		};
		reasonArr.push(reason1);
		var reason2 = {
				dictid: $("#complaintstimeid").val(),
				itemDesc: $("#complaints-time").val()
		};
		reasonArr.push(reason2);
		var reason3 = {
				dictid: $("#calltimeid").val(),
				itemDesc: $("#call-time").val()
		};
		reasonArr.push(reason3);
		if(reasonArr == null || reasonArr.length==0){
			$("#saveTimes").addClass("hide");
			$("#editTimes").removeClass("hide");
			return false;
		}
		$.ajax({
			type: "post",
			dataType : "json",
			url : global_Path + "/system/updateComplainDicData",
			contentType: "application/json;charset=UTF-8",
		    data: formatJson(type, reasonArr),
			success : function(result) {
				if (result.code == "SUCCESS") {
					$("#saveTimes").addClass("hide");
					$("#editTimes").removeClass("hide");
					$("#retrieveResponsetime").find("div.edit_info").addClass("hide");
					$("#retrieveResponsetime").find("div.show_info").removeClass("hide");
					initResponsetime();
				}else{
					alert("保存失败");
				}
			}
		});
	});
	/**
	 * 修改呼叫服务员类型
	 */
	$("#saveCalltype").click(function() {
		var type = $("#calltype").val();
		var calltypeArr = [];
		$("#calltype-list .avoid-li").not("#calltype-add").each(function(){
			var dictid = $(this).attr("dictid");
			var text = $(this).text();
			var calltype = {
					dictid: dictid,
					item_desc: text,
					itemid:$(this).index(),
					itemSort:$(this).index()
			};
			calltypeArr.push(calltype);
		});
		doPost(type, calltypeArr, function(data){
			$("#calltype-list").find(".dishTasteUl").last().attr("dictid",data.CALLTYPE[data.CALLTYPE.length-1].dictid);
			$("#saveAvoid").addClass("hide");
			$("#saveCalltype").addClass("hide");
			$("#editCalltype").removeClass("hide");
			$("#calltype-list .avoid-li").attr("disabled", true);
			$("#calltype-add").addClass("hide");
			$(".addcalltype-input-div").addClass("hide");
		});
	});
	/**
	 * 修改零头处理方式
	 */
	$("#saveRounding").click(function() {
		var typeRounding = $("#Rounding").val();
		var typeAccuracy = $("#Accuracy").val();
		var roundingArr = [];
		var accuracyArr = [];
		$(this).addClass("hide");
		$("#editRounding").removeClass("hide");
		$("#rounding").attr("disabled", "disabled");
		$("#accuracy").attr("disabled", "disabled");
		var dictidRounding = $("#rounding").attr("dictid");
		var itemidRounding = $("#rounding").val();
		var textRounding = $("#rounding option:selected").text();
		var rounding = {
				dictid: dictidRounding,
				itemid:itemidRounding,
			};
		roundingArr.push(rounding);

		doPost(typeRounding, roundingArr, function(data){

		});
		var dictidAccuracy = $("#accuracy").attr("dictid");
		var itemidAccuracy = $("#accuracy").val();
		var textAccuracy = $("#accuracy option:selected").text();
		var accuracy = {
				dictid: dictidAccuracy,
				itemid:itemidAccuracy,

		};
		accuracyArr.push(accuracy);
		
		doPost(typeAccuracy, accuracyArr, function(data){

		});
	});
	
	/**
	 * 点击免费、收费
	 */
	$("input[name='isFree']").click(function(){
		checkIsFree($(this).val());
	});
	function saveDishes(){
		var obj = {
				dictid : $("#dishid").val(),
				status : $("input[name='isFree']:checked").val(),
				price : $("#price").val(),
				member_price: $("#vipprice").val()
			};
		var type = $("#dishtype").val();
		doPost(type, obj, function(data){
			$("#saveDishes").addClass("hide");
			$("#editDishes").removeClass("hide");
			$("#retrieveDish").find("div.edit_info").addClass("hide");
			$("#retrieveDish").find("div.show_info").removeClass("hide");
			initData(data, 2);
		});
	}
	$("#tableware_form").validate({
		submitHandler : function(form) {
			var vcheck = true;
			if (vcheck) {
				var price = $("#price").val();
				var vipprice = $("#vipprice").val();
				if(price == 0){
					var err=$("<label class='error' id='city_la'></label>").html("请填写大于0的数字");
					$("#price").parent().append(err);
					$("#price").addClass("error");
					vcheck = false;
				}
				if(vipprice != null && vipprice != ""){
					if(vipprice == 0 || parseFloat(vipprice) >= parseFloat(price)){
						var msg = "";
						if(vipprice == 0){
							msg = "请填写大于0的数字";
						}else if(parseFloat(vipprice) >= parseFloat(price)){
							msg = "会员价应小于原价";
						}
						var err=$("<label class='error' id='city_la'></label>").html(msg);
						$("#vipprice").parent().append(err);
						$("#vipprice").addClass("error");
						vcheck = false;
					}
				}else{
					vcheck = true;
				}
			}
			if(vcheck){
				saveDishes();
			}
		}
	});
	/**
	 * 礼物设置
	 */
	$("#editGifts").click(function(){
		$("#dish-select-dialog").load("loadDishSelect");
		$("#dish-select-dialog").modal("show");
	});


	//编辑 保存按钮切换
	false && $('.setup_div_box .setup-mt>.btn').click(function(){
		
		if(me.hasClass('btn-edit')) {
			me.addClass('btn-submit').removeClass('btn-edit');
			me.text('保存');
			$selects.removeAttr("disabled");
			$inputs.removeAttr("disabled").removeClass('disabled');
			
			//社交功能单独处理
			if($parent.hasClass('setup_div_social')) {
				$('.seat-item-op').hide();
			}
		} else {
			me.addClass('btn-edit').removeClass('btn-submit');
			me.text('编辑');
			$selects.attr({"disabled":"disabled"});
			$inputs.attr({"disabled":"disabled"}).addClass('disabled');
			$btns.hide();
			$btns.show();
			if($parent.hasClass('setup_div_social')) {
				$('.seat-item-op').show();
			}
		}
	});
	
	//互动礼品设置
	$(".setup_div_social .J-btn-op").click(function(){
		var me = $(this);
		var $parent = me.parents('.setup_div_box');
		var $selects = $parent.find('.form-group select');
		var $inputs = $parent.find('.form-group input');
		var $btns = $parent.find('.form-group .btn');
		var $editGifts = $('#editGifts');
		var $seatOp = $('.seat-item-op');
		if(me.hasClass('btn-submit')) {
			$.ajax({
				type: "POST",
				dataType : "json",
				url : global_Path + "/padinterface/saveorupdate",
				contentType: "application/json;charset=UTF-8",
			    data: $.parseJSON({
			    	"vipstatus" : $('select[name=vipstatus]').val() === '0' ? true : false,
			    	"viptype" : $('select[name=viptype]').val()
			    }),
				success : function(result) {
					if (result.code == "0") {
						me.addClass('btn-edit').removeClass('btn-submit');
						me.text('编辑');
						$inputs.attr({"disabled":"disabled"}).addClass('disabled');
						$selects.attr({"disabled":"disabled"});
						$seatOp.hide();
						$editGifts.hide();
					}
				}
			});
		} else {
			me.addClass('btn-submit').removeClass('btn-edit');
			me.text('保存');
			$selects.removeAttr("disabled");
			$inputs.removeAttr("disabled").removeClass('disabled');
			$editGifts.show();
			$seatOp.show();
		}
		
	});
	
	//座位图上传
	$(".setup_div_social .J-btn-del").click(function(){
		$(this).parents('.seat-item').remove();
	})
	
	
	
	
	//会员设置
	$(".setup_div_member .J-btn-op").click(function(){
		var me = $(this);
		var $parent = me.parents('.setup_div_box');
		var $selects = $parent.find('.form-group select');
		if(me.hasClass('btn-submit')) {
			$.ajax({
				type: "GET",
				dataType : "json",
				url : global_Path + "/padinterface/saveorupdate",
			    data: {
			    	"vipstatus" : $('select[name=vipstatus]').val() === '0' ? true : false,
			    	"viptype" : $('select[name=viptype]').val()
			    },
				success : function(result) {
					if (result.code == "0") {
						me.addClass('btn-edit').removeClass('btn-submit');
						me.text('编辑');
						$selects.attr({"disabled":"disabled"});
						
					}
				}
			});
		} else {
			me.addClass('btn-submit').removeClass('btn-edit');
			me.text('保存');
			$selects.removeAttr("disabled");
		}
	});
	
	
	$('select[name=vipstatus],select[name=social]').change(function(){
		var me = $(this);
		var $target = me.parents('.setup_div');
		if(me.val() === '0') {
			$target.addClass('active');
		} else {
			$target.removeClass('active');
		}
	})
	
	
	//其他操作
	$(".setup_div_other .J-btn-op").click(function(){
		var me = $(this);
		var $parent = me.parents('.setup_div_box');
		var $selects = $parent.find('.form-group select');
		var $inputs = $parent.find('.form-group input');
		var $btns = $parent.find('.form-group .btn');
		if(me.hasClass('btn-submit')) {
			
			//验证 
			if(!/^[1-9]*[1-9][0-9]*$/.test($.trim($('input[name=adtimes]').val()))) {
				$('.yy-time-tip').show().text('请输入1-11位正整数');
				return false;
			}
			$.ajax({
				type: "POST",
				dataType : "json",
				url : global_Path + "/padinterface/saveorupdate",
			    data: {
			    	"clickimagedish" : $('select[name=clickimagedish]').val() === '0' ? true : false,
			    	"onepage" : $('select[name=onepage]').val() === '0' ? true : false,
			    	"newplayer" : $('select[name=newplayer]').val() === '0' ? true : false,
			    	"chinaEnglish" : $('select[name=chinaEnglish]').val() === '0' ? true : false,
			    	"indexad" : $('select[name=indexad]').val() === '0' ? true : false,
			    	"invoice" : $('select[name=invoice]').val() === '0' ? true : false,
			    	"hidecarttotal" : $('select[name=hidecarttotal]').val() === '0' ? true : false,
			    	"adtimes" : $('input[name=adtimes]').val(),
			    	"waiterreward" : $('select[name=waiterreward]').val() === '0' ? true : false
			    },
				success : function(result) {
					if (result.code == "0") {
						me.addClass('btn-edit').removeClass('btn-submit');
						me.text('编辑');
						$selects.attr({"disabled":"disabled"});
						$inputs.attr({"disabled":"disabled"}).addClass('disabled');
						
						
					}
				}
			});
		} else {
			me.addClass('btn-submit').removeClass('btn-edit');
			me.text('保存');
			$selects.removeAttr("disabled");
			$inputs.removeAttr("disabled").removeClass('disabled');
		}
		
	})
	
	//统计操作
	$(".setup_div_total .J-btn-op").click(function(){
		var me = $(this);
		var $parent = me.parents('.setup_div_box');
		var $selects = $parent.find('.form-group select');
		var $inputs = $parent.find('.form-group input[type=text]');
		var $btns = $parent.find('.form-group .btn');
		var flag = true;//校验标准 
		if(me.hasClass('btn-submit')) {
			
			//验证 
			$inputs.each(function(i,v){
				if($.trim($(v).val()).length == 0) {
					flag = false;
					$(v).next().show();
				}
			});
			if(!flag) {
				return false;
			} 
			
			$.ajax({
				type: "POST",
				dataType : "json",
				url : global_Path + "/padinterface/saveorupdate",
			    data: {
			    	"youmengappkey" : $('input[name=youmengappkey]').val(),
			    	"youmengchinnal" : $('input[name=youmengchinnal]').val(),
			    	"bigdatainterface" : $('input[name=bigdatainterface]').val(),
			    },
				success : function(result) {
					if (result.code == "0") {
						me.addClass('btn-edit').removeClass('btn-submit');
						me.text('编辑');
						$inputs.attr({"disabled":"disabled"}).addClass('disabled');
						
					}
				}
			});
		} else {
			me.addClass('btn-submit').removeClass('btn-edit');
			me.text('保存');
			$inputs.removeAttr("disabled").removeClass('disabled');
		}
	});
	
	

});
/**
 * 编辑保存按钮切换
 */

function showImg(obj,thumb){
	var me = $(obj);
	var $parent = me.parents('.seat-item');
	var $seatImg = $parent.find('.seat-img');
	var idx = $parent.index()+1;
	var seatImgUrl = getObjectURL(me[0].files[0]);
	$parent.find("#setimgurl" + idx).val(seatImgUrl);
	$seatImg.attr("src",seatImgUrl);
}


/**
 * 获取投诉原因
 */
function initComplaint(){
	$.get(global_Path + "/system/getComplainDicList.json", function(result) {
		if(result.code == "SUCCESS"){
			var data = result.data;
			initData(data, 7);
		}
	},'json');
}
/**
 * 服务响应时间获取
 */
function initResponsetime(){
	$.get(global_Path + "/clientcomplain/getTimeSet.json", function(result) {
		if(result.code == "001"){
			var data = result.data;
			initData(data, 8);
		}
	},'json');
}
/**
 * 获取所有礼物
 */
function getAllGifts(){
	$.get(global_Path + "/social/getGiftList.json", function(result){
		if(result.flag == 1){
			var data = result.data;
			if(data!=null && data.length>0){
				showAllGifts(data);
			}
		}else{
			$("#gifts-div").text(result.desc);
		}
	}, 'json');
}
/**
 * 显示已设置的礼物
 * @param selectedDishs
 */
function showAllGifts(data){
	console.log(data);
	haveSelected = new Array();
	var htm = "";
	$.each(data, function(i, item){
		var obj = {dish: item.giftNo, dish_title: item.giftName, unit: item.giftUnit};
		haveSelected.push(obj);
		var giftname = (i+1)+"、"+item.giftName+"("+item.giftUnit+")";
		console.log(giftname);
		htm += "<div class='one-gift'>"+giftname+"</div>";
	});
	$("#gifts-div").html(htm);
}
function checkValue(e){
	$(e).removeClass("error");
	$(e).parent().find("label.error").remove();
	var value = $(e).val();
	var price = $("#price").val();
	var vipprice = $("#vipprice").val();
	if(value!= null && value !="" && value == 0){
		var err=$("<label class='error' id='city_la'></label>").html("请填写大于0的数字");
		$(e).parent().append(err);
		$(e).addClass("error");
	}
	if(price !="" && vipprice != "" && parseFloat(vipprice) >= parseFloat(price)){
		var err=$("<label class='error' id='city_la'></label>").html("会员价应小于原价");
		$(e).parent().append(err);
		$(e).addClass("error");
	}
}
function checkIsFree(value){
	if(value == "0"){
		$(".price_pro").addClass("hide");
		$(".vipprice_pro").addClass("hide");
	}else{
		$(".price_pro").removeClass("hide");
		$(".vipprice_pro").removeClass("hide");
	}
}
/**
 * 发送请求
 * @param type
 * @param obj
 * @param callback
 */
function doPost(type, obj, callback){
	$.ajax({
		type: "post",
		dataType : "json",
		url : global_Path + "/system/insertDate",
		contentType: "application/json;charset=UTF-8",
	    data: formatJson(type, obj),
		success : function(result) {
			if (result.code == "SUCCESS") {
				callback(result.data);
			}else{
				alert("保存失败");
			}
		}
	});
}
function formatJson(type, obj){
	var json = {type: type, data: obj};
	return JSON.stringify(json);
}
function delDisplay(e) {
	$(e).find("i.icon-remove").removeClass("hidden");
}
function delHidden(e) {
	$(e).find("i.icon-remove").addClass("hidden");
}
/**
 * 编辑忌口、退菜原因
 * @param e
 */
function editItem(e){
	var dictid = $(e).attr("dictid");
	var type = $(e).attr("type");
	var text = $(e).text();
	$next = $(e).next();
	$(e).remove();
	var inp = '<div class="col-xs-2 no-left-padding">';
	if(type == "COMPLAINT"){
		inp += '<input type="text" dictid="'+dictid+'" _type="'+type+'" class="form-control" id="complaint_name" value="'+text+'" maxlength="4" required="required" onblur="updateItem(this);" /></div>';
	}else{
		inp += '<input type="text" dictid="'+dictid+'" _type="'+type+'" class="form-control" id="reason_name" value="'+text+'" maxlength="5" required="required" onblur="updateItem(this);" /></div>';
	}
	$next.before(inp);
}
function updateItem(e){
	var dictid = $(e).attr("dictid");
	var type = $(e).attr("_type");
	var text = dellrTrim($(e).val());
	if (text != null && text != "") {
		$next = $(e).parent().next();
		$(e).parent().remove();
		var htm = itemHtm(dictid, text, type, false);
		$next.before(htm);
	}
	
}
/**
 * 删除一个忌口、退菜原因
 * @param e
 */
function delItem(e){
	var dictid = $(e).parent().attr("dictid");
	if(dictid == null || dictid == ""){
		var $o = $(e).parent().parent();
		$(e).parent().remove();
		showAddBtn($o);
	}else{
		$.post(global_Path+"/system/deleteDate", {dictid: dictid}, function(result){
			if(result.code == "SUCCESS"){
				var $o = $(e).parent().parent();
				$(e).parent().remove();
				showAddBtn($o);
			}
		},'json');
	}
}
/**
 * 显示添加按钮
 * @param $o
 */
function showAddBtn($o){
	if($o.attr("id") == "reason-list"){
		if($("#addreason-input").hasClass("hide") && $o.find(".avoid-li").not("#reason-add").length < 5){
			$("#reason-add").removeClass("hide");
		}
	}else if($o.attr("id") == "avoid-list"){
		if($("#addavoid-input").hasClass("hide") && $o.find(".avoid-li").not("#avoid-add").length < 5){
			$("#avoid-add").removeClass("hide");
		}
	}
}
function itemHtm(dictid, itemdesc, type, dis) {
	var disable = "";
	if(dis){
		disable = "disabled";
	}
	var htm = "";
	if(type == "COMPLAINT"){
		htm += '<div '+disable+' dictid="'
			+ dictid
			+ '" type="'
			+ type
			+ '" ondblclick="editItem(this)" class="avoid-li btn btn-default canClick dishTasteUl">'
			+ itemdesc
			+ '</div>';
	}else{
		htm += '<div '+disable+' dictid="'
			+ dictid
			+ '" type="'
			+ type
			+ '" ondblclick="editItem(this)" onmouseout="delHidden(this)" onmouseover="delDisplay(this)" class="avoid-li btn btn-default canClick dishTasteUl">'
			+ itemdesc
			+ '<i class="icon-remove hidden" onclick="delItem(this)"></i>'
			+ '</div>';
	}
	return htm;
}
/**
 * 添加一个忌口
 */
function avoidSave() {
	var avoid_name = dellrTrim($("#avoid_name").val());
	if (avoid_name != null && avoid_name != "") {
		$(".addavoid-input-div").addClass("hide");
		var htm = itemHtm("", avoid_name, $("#avoidtype").val(), false);
		$("#avoid-add").before(htm);

		if ($("#avoid-list").find(".avoid-li").not(".add-reason").length < 5) {
			$("#avoid-add").removeClass("hide");
		}
	}
}
/**
 * 添加一个退菜原因
 */
function returnDishSave() {
	var reason_name = dellrTrim($("#reason_name").val());
	if (reason_name != null && reason_name != "") {
		$(".addreason-input-div").addClass("hide");
		var htm = itemHtm('', reason_name, $("#reasontype").val(), false);
		$("#reason-add").before(htm);

		if ($("#reason-list").find(".avoid-li").not(".add-reason").length < 5) {
			$("#reason-add").removeClass("hide");
		}
	}
}
/**
 * 添加一个投诉原因
 */
function complaintSave() {
	var complaint_name = dellrTrim($("#complaint_name").val());
	if (complaint_name != null && complaint_name != "") {
		$(".addcomplaint-input-div").addClass("hide");
		var htm = itemHtm('', complaint_name, $("#complaintstype").val(), false);
		$("#complaint-add").before(htm);
		/*
		if ($("#reason-list").find(".avoid-li").not(".add-reason").length < 5) {
			$("#reason-add").removeClass("hide");
		}*/
	}
}
/**
 * 添加一个呼叫服务员类型原因
 */
function CalltypeSave() {
	var calltype_name = dellrTrim($("#calltype_name").val());
	if (calltype_name != null && calltype_name != "") {
		$(".addcalltype-input-div").addClass("hide");
		var htm = itemHtm('', calltype_name, $("#calltype").val(), false);
		$("#calltype-add").before(htm);

		if ($("#calltype-list").find(".avoid-li").not(".add-calltype").length < 5) {
			$("#calltype-add").removeClass("hide");
		}
	}
}

function initData(data, type){
	if(type == 0 || type == null){
		//营业时间
		var busslist = data.BIZPERIODDATE;
		$.each(busslist, function(i, item){
			var itemDesc = item.itemDesc;
			var datetype = item.date_type;
			var begintime = item.begin_time;
			var endtime = item.end_time;
			var dictid = item.dictid;
			if(itemDesc == "全天"){
				$("#bussallid").val(dictid);
				$("#all_begintime_info").text(begintime);
				$("#all_endtime_info").text(endtime);
				$("#all_datetype_info").text(datetype=="T"?"今日":"次日");
				
				$("#all_begintime_input").val(begintime);
				$("#all_endtime_input").val(endtime);
				$("#all_date_type").val(datetype);
			}else if(itemDesc == "午市"){
				$("#busslunchid").val(dictid);
				$("#lunch_begintime_info").text(begintime);
				$("#lunch_endtime_info").text(endtime);
				$("#lunch_datetype_info").text(datetype=="T"?"今日":"次日");
				
				$("#lunch_begintime_input").val(begintime);
				$("#lunch_endtime_input").val(endtime);
				$("#lunch_date_type").val(datetype);
			}else if(itemDesc == "晚市"){
				$("#bussdinnerid").val(dictid);
				$("#dinner_begintime_info").text(begintime);
				$("#dinner_endtime_info").text(endtime);
				$("#dinner_datetype_info").text(datetype=="T"?"今日":"次日");
				
				$("#dinner_begintime_input").val(begintime);
				$("#dinner_endtime_input").val(endtime);
				$("#dinner_date_type").val(datetype);
			}
		});
	}
	if(type == 1 || type == null){
		//服务密码
		var passlist = data.PASSWORD;
		$.each(passlist, function(i, item){
			var pass = item.itemDesc;
			$(".password p").text(encrypt(pass));
			$("#passid").val(item.dictid);
			$("#service_pass").val(pass);
		});
	}
	if(type == 2 || type == null){
		//餐具设置
		var tablewarelist = data.DISHES;
		$.each(tablewarelist, function(i, item){
			var price = item.price;
			var vipprice = item.member_price;
			var status = item.status;//0免费；1收费
			if(status == 0){
				$(".isFree").find("p").text("免费");
			}else{
				$(".isFree").find("p").text("收费");
			}
			$("input[name='isFree'][value="+status+"]").prop("checked",true);
			
			$(".price p").text(price+"元");
			var vipprice_p = vipprice;
			if(vipprice !=null && vipprice != ""){
				vipprice_p = vipprice + "元";
			}
			$(".vipprice p").text(vipprice_p);
			$("#dishid").val(item.dictid);
			$("#price").val(price);
			$("#vipprice").val(vipprice);
			checkIsFree(status);
		});
	}
	
	if(type == 3 || type == null){
		//忌口设置
		var speciallist = data.SPECIAL;
		var len = speciallist.length;
		$.each(speciallist, function(i, item){
			var itemDesc = item.itemDesc;
			var htm = itemHtm(item.dictid, itemDesc, $("#avoidtype").val(), true);
			$("#avoid-add").before(htm);
			if(len >= 5){
				$("#avoid-add").addClass("hide");
			}
		});
	}
	if(type == 4 || type == null){
		//退菜原因设置
		var returnlist = data.RETURNDISH;
		var len = returnlist.length;
		$.each(returnlist, function(i, item){
			var itemDesc = item.itemDesc;
			var htm = itemHtm(item.dictid, itemDesc, $("#reasontype").val(), true);
			$("#reason-add").before(htm);

			if (len>=5) {
				$("#reason-add").addClass("hide");
			}
		});
	}
	if(type == 5 || type == null){
		//呼叫服务员设置
		var calltypelist = data.CALLTYPE;
		if(calltypelist != null && calltypelist != ""){
			var len = calltypelist.length;
			$.each(calltypelist, function(i, item){
				var itemDesc = item.itemDesc;
				var htm = itemHtm(item.dictid, itemDesc, $("#calltype").val(), true);
				$("#calltype-add").before(htm);

				if (len>=5) {
					$("#calltype-add").addClass("hide");
				}
			});
		}
	}
	if(type == 6 || type == null){
		//零头处理方式
		var roundinglist = data.ROUNDING;
		var accuracylist = data.ACCURACY;
		if(roundinglist!=null && roundinglist.length!=0 && accuracylist!=null && accuracylist.length!=0){
			$("#rounding").attr("dictid",roundinglist[0].dictid);
			$("#accuracy").attr("dictid",accuracylist[0].dictid);
			
		}
		getRoundingTag();
		getAccuracyTag();
		if(roundinglist!=null && roundinglist.length!=0&&accuracylist!=null && accuracylist.length!=0){
		$("#rounding  option[value="+roundinglist[0].itemid+"] ").attr("selected",true);
		$("#accuracy  option[value="+accuracylist[0].itemid+"] ").attr("selected",true);
		$("#rounding").attr("dictid",roundinglist[0].dictid);
		$("#accuracy").attr("dictid",accuracylist[0].dictid);
		}
		$("#rounding").change(function(){
			if($(this).val()==0){
				$("#accuracyDiv").addClass("hidden");
			}else{
				$("#accuracyDiv").removeClass("hidden");
			}
		});
		if($("#rounding").val()==0){
			$("#accuracyDiv").addClass("hidden");
		}
	}
	if(type == 7){
		//投诉原因设置
		var complaintlist = data;
		$.each(complaintlist, function(i, item){
			var itemDesc = item.itemDesc;
			var htm = itemHtm(item.dictid, itemDesc, $("#complaintstype").val(), true);
			$("#complaint-add").before(htm);
		});
	}
	if(type == 8){
		//餐具设置
		var tablewarelist = data;
		$.each(tablewarelist, function(i, item){
			var itemid = item.itemid;
			var dictid = item.dictid;
			var itemValue = item.item_value;
			if(itemid == 3){
				//呼叫服务员响应时间
				$(".call-time p").text(itemValue+"秒");
				$("#call-time").val(itemValue);
				$("#calltimeid").val(dictid);
			}else if(itemid == 2){
				//买单
				$(".pay-time p").text(itemValue+"秒");
				$("#pay-time").val(itemValue);
				$("#paytimeid").val(dictid);
			}else if(itemid == 1){
				//投诉
				$(".complaints-time p").text(itemValue+"秒");
				$("#complaints-time").val(itemValue);
				$("#complaintstimeid").val(dictid);
			}
		});
	}
	
	if(type == null){
		var imgs = data.PADIMG;
		var logoImg;
		var bgImg;
		$(imgs).each(function(){
			if(this.itemid == 1){
				logoImg = this;
			}
			if(this.itemid == 2){
				bgImg = this;
			}
			if(logoImg != null){
				$("#defaultlogo").attr("src",img_Path + logoImg.item_value);
			}
			if(bgImg != null){
				$("#def_background").attr("src",img_Path + bgImg.item_value);
			}
		});
	}
}
/**
 * 查询数据
 */
function doGet(type){
	$.get(global_Path + "/system/getalllist.json", function(result) {
		if(result.code == "SUCCESS"){
			var data = result.data;
			initData(data, null);
		}
	},'json');
	
	doGetPadData();
}
/**
 * 查询pad设数据,并初始化
 */
function doGetPadData(){
	$.get(global_Path + "/padinterface/getconfiginfos", function(result) {
		if(result.code == "0"){
			var data = result.data;
			var $seatItem = $('.seat-item');
			
			//社交
			$('select[name=social]').val(data.social ? "0" :"1");
			
			if(data.social) {
				$('.setup_div_social').addClass('active');
			};
			
			//data.seatImagename = [];
			//
			
			data.seatImagename = ['111111111'];
			data.seatImagefiles = ['http://layznet.iteye.com/images/status/offline.gif']
			
			//data.seatImagename = ['111111111','222222222222222'];
			//data.seatImagefiles = ['http://layznet.iteye.com/images/status/offline.gif','http://www.iteye.com/images/user-logo-thumb.gif?1448702469']
			
			if(data.seatImagename.length == 0) {
				$seatItem.eq(0).show();
			} else if(data.seatImagename.length == 1){
				$seatItem.show();
				$seatItem.eq(0).find('input[name=seatname1]').val(data.seatImagename[0]);
				$seatItem.eq(0).find('.seat-img').attr('src',data.seatImagefiles[0]);
			} else {
				$seatItem.show();
				$seatItem.eq(0).find('input[name=seatname1]').val(data.seatImagename[0]);
				$seatItem.eq(1).find('.seat-img').attr('src',data.seatImagename[1]);
			}
			
			//会员设置
			$('select[name=vipstatus]').val(data.vipstatus ? "0" :"1");
			$('select[name=viptype]').val(data.viptype);
			if(data.vipstatus) {
				$('.setup_div_member').addClass('active');
			};
			
			//其他设置
			$('select[name=clickimagedish]').val(data.clickimagedish ? "0" :"1");
			$('select[name=onepage]').val(data.onepage ? "0" :"1");
			$('select[name=newplayer]').val(data.newplayer ? "0" :"1");
			$('select[name=chinaEnglish]').val(data.chinaEnglish ? "0" :"1");
			$('select[name=indexad]').val(data.indexad ? "0" :"1");
			$('select[name=invoice]').val(data.invoice ? "0" :"1");
			$('select[name=hidecarttotal]').val(data.hidecarttotal ? "0" :"1");
			$('select[name=waiterreward]').val(data.waiterreward ? "0" :"1");
			$('input[name=adtimes]').val(data.adtimes);
			
			
			//统计设置
			$('input[name=youmengappkey]').val(data.youmengappkey);
			$('input[name=youmengchinnal]').val(data.youmengchinnal);
			$('input[name=bigdatainterface]').val(data.bigdatainterface);
		}
	},'json');
}
/**
 * 密码用*代替
 * @param pass
 * @returns {String}
 */
function encrypt(pass){
	var _pass = "";
	for(var j=0;j<pass.length;j++){
		_pass += "*";
	}
	return _pass;
}
/**
 * 去掉字符串前后空格
 * @param str
 * @returns
 */
function dellrTrim(str){
	return str.replace(/(^\s*)|(\s*$)/g, "");
}
/**
 * 去掉字符串前面空格
 * @param str
 * @returns
 */
function dellTrim(str){
	return str.replace(/(^\s*)/g, "");
}
/**
 * 去掉字符串后面空格
 * @param str
 * @returns
 */
function delrTrim(str){
	return str.replace(/(\s*$)/g, "");
}
/**
 * 小时:分钟
 * 这种格式输入框控制
 * @param obj
 */
function checkTimeFormat(obj){
	var value = $(obj).val();
	if(value != null && value != ""){
		if(value.indexOf(":")==0){
			//第一个字符输入冒号
			value = "00"+value;
		}else if(value.indexOf(":") < 0){
			if(value.length > 2){
				//大于两位数,截前两位
				value = value.substring(0, 2);
			}else{
				if(value.length == 1){
					//小时的最高位最大为2（0～23）
					if(value > 2){
						value = "";
					}
				}else{
					if(parseInt(value) > 23){
						//小时不能大于23
						value = value.substring(0, 1);
					}
				}
			}
		}else{
			var arr = value.split(":");
			var before = arr[0];
			if(before.length == 1){
				before = "0"+before;
			}
			var after = arr[1];
			if(after != null && after != ""){
				if(after.length == 1){
					if(after > 5){
						after = "";
					}
				}else if(after.length == 2){
					if(parseInt(after)>59){
						after = after.substring(0, 1);
					}
				}else{
					after = after.substring(0, 2);
				}
			}
			value = before + ":"+after;
		}
	}
	$(obj).val(value);
}
function getRoundingTag(){
	var result = [{'rounding':'0','roundingname':'不处理'},{'rounding':'1','roundingname':'四舍五入'},{'rounding':'2','roundingname':'抹零'}];
	
	
	$.each(result, function(i,val){  
		
		$(".rounding").append("<option value ="+val.rounding+"  class='form-control myInfo-select-addrW'>"+val.roundingname+"</option>");
	});
}
function getAccuracyTag(){
	var result = [{'accuracy':'0','accuracyname':'分'},{'accuracy':'1','accuracyname':'角'},{'accuracy':'2','accuracyname':'元'}];
	
	
	$.each(result, function(i,val){  
		$(".accuracy").append("<option value ="+val.accuracy+"  class='form-control myInfo-select-addrW'>"+val.accuracyname+"</option>");
	});
}

/**
 * 获取路径
 * @param file
 * @returns
 */
function getObjectURL(file) {
	if(file == null){
		return;
	}
    var url = null ;
    if (window.createObjectURL!=undefined) { // basic
        url = window.createObjectURL(file) ;
    } else if (window.URL!=undefined) { // mozilla(firefox)
        url = window.URL.createObjectURL(file) ;
    } else if (window.webkitURL!=undefined) { // webkit or chrome
        url = window.webkitURL.createObjectURL(file) ;
    }
    return url ;
}

$.extend($.validator.messages, {
    required: "这是必填字段",
    remote: "请修正此字段",
    email: "请输入有效的电子邮件地址",
    url: "请输入有效的网址",
    date: "请输入有效的日期",
    dateISO: "请输入有效的日期 (YYYY-MM-DD)",
    number: "请输入有效的数字",
    digits: "只能输入数字",
    creditcard: "请输入有效的信用卡号码",
    equalTo: "你的输入不相同",
    extension: "请输入有效的后缀",
    maxlength: $.validator.format("最多可以输入 {0} 个字符"),
    minlength: $.validator.format("最少要输入 {0} 个字符"),
    rangelength: $.validator.format("请输入长度在 {0} 到 {1} 之间的字符串"),
    range: $.validator.format("请输入范围在 {0} 到 {1} 之间的数值"),
    max: $.validator.format("请输入不大于 {0} 的数值"),
    min: $.validator.format("请输入不小于 {0} 的数值")
});
