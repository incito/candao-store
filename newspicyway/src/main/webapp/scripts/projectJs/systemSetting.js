var selectedDishsTop = [];
var logoImg;
var bgImg;

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
	//一页菜品设置编辑
	$("#onepagebut").click(function() {
		$(this).addClass("hide");
		$("#onepagesub").removeClass("hide");
		$("#onepagediv").find("div.edit_info").removeClass("hide");
		$("#onepagediv").find("div.show_info").addClass("hide");
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
			$("#avoid-list").find(".dishTasteUl").last().attr("dictid",data.JI_KOU_SPECIAL[data.JI_KOU_SPECIAL.length-1] && data.JI_KOU_SPECIAL[data.JI_KOU_SPECIAL.length-1].dictid);
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
			$("#reason-list").find(".dishTasteUl").last().attr("dictid",data.RETURNDISH[data.RETURNDISH.length-1] && data.RETURNDISH[data.RETURNDISH.length-1].dictid);
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
	
	
	/********一页菜谱保存***********/
	
	$("#onepage_form").validate({
		submitHandler : function(form) {
			var vcheck = true;
			if (vcheck) {
				var onepageisFree= $("input[name='onepageisFree']:checked").val();
				saveonepageForm(onepageisFree);
			}
		}
	});
	
	function saveonepageForm(status){
		var obj = {
				status : status,
				dictid:$("#dictidonepage").val()
			};
		var type = $("#onepagetype").val();
		doPost(type, obj, function(data){
			$("#onepagesub").addClass("hide");
			$("#onepagebut").removeClass("hide");
			$("#onepagediv").find("div.edit_info").addClass("hide");
			$("#onepagediv").find("div.show_info").removeClass("hide");
			initData(data, null);
		});
	}
	
	
	/**
	 * 礼物设置
	 */
	$("#editGifts").click(function(){
		$("#dish-select-dialog").load("loadDishSelect",function(){
			//console.info(haveSelected);
		});
		$("#dish-select-dialog").modal("show");
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
		var seatnameArr = [];
		var seatImgFilesArr = [];
		var validateFlag = true;
		var isUploadFile = true;//上传或者更新图片
		var isUpdateFile = true ;//删除或更新图片名称
		var updateObj = {};
		var updateObjLen = 0;
		var fileObj = {
            	seatImagename : seatnameArr
        };
		if(me.hasClass('btn-submit')) {

			$('.seat-item').each(function(i){
				var me = $(this);
				var $seatImgBtn = me.find('.seatImgBtn');
				var $seatname = me.find('.seatname').val();

				if(me.hasClass('hasDel')) {//删除图片
					updateObj['fileurl' + i + ""] = me.attr('src-img');
					updateObj['seatImagename' + i + ""] = '';
					return true
				}

				if(!me.hasClass('seat-item-default')) {
					if($seatImgBtn.val()) {//上传或者更新图片
						if(!$.trim($seatname).length) {//图片名字为空
							validateFlag = false;
							me.find('.seat-item-tip').show();
							return false;
						} else {
							seatnameArr.push($seatname);
							seatImgFilesArr.push('seatImgIpt' + i);
						}
					} else {//更新图片名称
						if(!$.trim($seatname).length) {//图片名字为空
							validateFlag = false;
							me.find('.seat-item-tip').show();
							return false;
						} else {
							updateObj['fileurl' + i + ""] = me.attr('img-src');
							updateObj['seatImagename' + i + ""] = $seatname;
							//seatnameArr.push($seatname);
							//seatImgFilesArr.push('seatImgIpt' + (i+1))
						}
					}

					//如果是修改图片名称
					if(typeof(me.attr('img-src')) != "undefined") {
						fileObj['fileurl' + i + ""] = me.attr('img-src');
					}
				}

});


			if(seatImgFilesArr.length == 0) {
				isUploadFile = false;
			}


			for(var v in updateObj){
				updateObjLen++;
			}
			if(updateObjLen === 0) {
				isUpdateFile = false;
			}



			//更新礼品
			if(validateFlag && selectedDishsTop !== null) {
				selectedDishsTop && saveSelectedGifts(selectedDishsTop,'save');
			}




			//上传图片
			if(validateFlag && isUploadFile) {
				//showPromp('保存图片中');
				$.ajaxFileUpload({
					url : global_Path + "/padinterface/importfile",
	                    secureuri: false, //是否需要安全协议，一般设置为false
	                    fileElementId: seatImgFilesArr, //文件上传域的ID
	                    fileFilter:'.jpg,.png',
	                    fileSize:'2097152',
	                    //dataType: 'content', //返回值类型 一般设置为json
	                    data : fileObj,
	                    success: function (data, status)  //服务器成功响应处理函数
	                    {
	                        if (typeof (data.error) != 'undefined') {
	                            if (data.error != '') {
	                            	console.info(data.error);
	                            } else {
	                                console.info(data.msg);
	                            }
	                        }
	                    },
	                    error: function (data, status, e)//服务器响应失败处理函数
	                    {
	                        console.info(e);
	                    },
		                complete: function (XMLHttpRequest, textStatus) {
		                	var result = $.parseJSON(XMLHttpRequest.responseText);

							if(result.fileurl0 !== undefined && result.fileurl0 !== '') {
								$('.seat-item').eq(0).attr({"img-src":result.fileurl0});
							}

							if(result.fileurl1 !== undefined && result.fileurl1 !== '') {
								$('.seat-item').eq(1).attr({"img-src":result.fileurl1});
							}


                			 //更新其他字段
                			validateFlag && $.ajax({
                				 type: "GET",
                				 dataType : "json",
                					url : global_Path + "/padinterface/saveorupdate",
                			     data: {
                			     	"social" : $('select[name=social]').val() === '0' ? true : false
                			     },
                			 	success : function(result) {
                			 		if (result.code == "0") {
                			 			me.addClass('btn-edit').removeClass('btn-submit');
                			 			me.text('编辑');
                			 			$inputs.attr({"disabled":"disabled"}).addClass('disabled');
                			 			$selects.attr({"disabled":"disabled"});
                			 			$seatOp.hide();
                			 			$editGifts.hide();
                			 			$('.seat-item-tip').hide();
                			 		}
                			 	}
                			 });
		        	    }
	                });
			} else {

				 //更新其他字段
				validateFlag && $.ajax({
					 type: "GET",
					 dataType : "json",
						url : global_Path + "/padinterface/saveorupdate",
				     data: {
				     	"social" : $('select[name=social]').val() === '0' ? true : false
				     },
				 	success : function(result) {
				 		if (result.code == "0") {
				 			me.addClass('btn-edit').removeClass('btn-submit');
				 			me.text('编辑');
				 			$inputs.attr({"disabled":"disabled"}).addClass('disabled');
				 			$selects.attr({"disabled":"disabled"});
				 			$seatOp.hide();
				 			$editGifts.hide();
				 			$('.seatname').hide();
    			 			$('.seatname-info').show();
    			 			$('.seat-item-tip').hide();
    			 			$(".seatname").each(function(){
    			 				var me = $(this);
    			 				me.next().text(me.val());
    			 			})
				 		}
				 	}
				 });
			}

			 if(validateFlag && isUpdateFile) {
	 				//删除图片或者更新图片名称
	 				$.ajax({
	 					 type: "POST",
	 					 dataType : "json",
	 						url : global_Path + "/padinterface/deletefile",
	 				     data: updateObj,
	 				 	success : function(result) {
	 				 		if (result.code == "0") {
	 				 			console.info("图片更新成功");
	 				 		}
	 				 	}
	 				 });
	 			}


		} else {
			me.addClass('btn-submit').removeClass('btn-edit');
			me.text('保存');
			$selects.removeAttr("disabled");
			$inputs.removeAttr("disabled").removeClass('disabled');
			$editGifts.show();
			$seatOp.show();
			$('.seatname').show();
 			$('.seatname-info').hide();
		}

	});

	//座位图删除
	$(".setup_div_social .J-btn-del").click(function(){
		var $parent = $(this).parents('.seat-item');
		$parent.addClass('seat-item-default').find('.seat-img').attr({'src':'../images/upload-img.png'});
		$parent.find('.seatname').val('');
		$parent.addClass('hasDel');

	});

	//会员设置
	$(".setup_div_member .J-btn-op").click(function(){
		var me = $(this);
		var $parent = me.parents('.setup_div_box');
		var $selects = $parent.find('.form-group select');
		var $imgDefault = $parent.find('.img-default');
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
						$imgDefault.hide();

					}
				}
			});
		} else {
			me.addClass('btn-submit').removeClass('btn-edit');
			me.text('保存');
			$selects.removeAttr("disabled");
			$imgDefault.show();
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
				$('.yy-time-tip').show().text('请输入1-10位正整数');
				return false;
			}
			jQuery.ajax({
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
						//$inputs.attr({"disabled":"disabled"}).addClass('disabled');
						$('.yy-time-tip').hide();
						$('.yy-time-info').show();
						$('.yy-time').hide();
						$('.adtimes-val').text($('input[name=adtimes]').val());
					}
				}
			});
		} else {
			me.addClass('btn-submit').removeClass('btn-edit');
			me.text('保存');
			$selects.removeAttr("disabled");
			$('.yy-time-info').hide();
			$('.yy-time').show();
			//$inputs.removeAttr("disabled").removeClass('disabled');
		}

	})

	//统计操作
	$(".setup_div_total .J-btn-op").click(function(){
		var me = $(this);
		var $parent = me.parents('.setup_div_box');
		var $selects = $parent.find('.form-group select');
		var $inputs = $parent.find('.form-group input[type=text]');
		var $btns = $parent.find('.form-group .btn');
		var $tip = $parent.find('.tip');
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
			    	"braceletgappkey" : $('input[name=braceletgappkey]').val(),
			    	"braceletchinnal" : $('input[name=braceletchinnal]').val(),
			    },
				success : function(result) {
					if (result.code == "0") {
						me.addClass('btn-edit').removeClass('btn-submit');
						me.text('编辑');
						$inputs.attr({"disabled":"disabled"}).addClass('disabled');
						$tip.hide();

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

function showImg(obj,thumb){
	var me = $(obj);
	var $parent = me.parents('.seat-item');
	var $seatImg = $parent.find('.seat-img');
	var idx = $parent.index()+1;
	var seatImgUrl = getObjectURL(me[0].files[0]);
	$parent.removeClass('seat-item-default');
	$parent.removeClass('hasDel');
	$parent.find("#seatImagefiles").val(seatImgUrl);
	$seatImg.attr("src",seatImgUrl);
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

			//设置上传图片按钮显示
			$seatItem.show();

			//设置座位图
			if(data.seatImagename !== "" && data.seatImagename.length !== 0) {
					$.each(data.seatImagename,function(i,v){
						var me = $seatItem.eq(i);
						if(v == "") return false;
						var imgUrl = 'http://' +window.location.host + global_Path + '/' + data.seatImagefileurls[i].replace(/\\/g,'/');
						me.find('input[name=seatname' + (i+1) + ']').val(data.seatImagename[i]);
						me.find('.seatname-info').text(data.seatImagename[i]);
						me.attr({'img-src':data.seatImagefileurls[i]})
						me.find('.seat-img').attr('src',imgUrl);
						me.removeClass('seat-item-default').show();
						//me.find('.seatImgBtn').val(imgUrl);
					});
			}

			//设置logo和pad背景图
			if(data.backgroudurl !== null && data.backgroudurl !== ''){
				var imgUrl = 'http://' +window.location.host + global_Path + '/' + data.backgroudurl.replace(/\\/g,'/');
				$("#backgroundDictid").attr("value",bgImg && bgImg.dictid);
				$("#def_background").attr("src",imgUrl);
				
			}
			if(data.logourl !== null && data.logourl !== ''){
				var imgUrl = 'http://' +window.location.host + global_Path + '/' + data.logourl.replace(/\\/g,'/');
				$("#defaultlogo").attr("src",imgUrl);
				$("#logoDictid").attr("value",logoImg && logoImg.dictid);
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
			$('.adtimes-val').text(data.adtimes);

			//统计设置
			$('input[name=youmengappkey]').val(data.youmengappkey);
			$('input[name=youmengchinnal]').val(data.youmengchinnal);
			$('input[name=bigdatainterface]').val(data.bigdatainterface);
			$('input[name=braceletgappkey]').val(data.braceletgappkey);
			$('input[name=braceletchinnal]').val(data.braceletchinnal);
		}
	},'json');
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
		if(result.code == "0"){
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
		if(result.code == '0'){
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
		var speciallist = data.JI_KOU_SPECIAL;
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

		$(imgs).each(function(){
			if(this.itemid == 1){
				logoImg = this;
			}
			if(this.itemid == 2){
				bgImg = this;
			}
		});
		
		//一页菜谱显示
		//var   onepaytypes=data.ONEPAGETYPE;
		//$.each(onepaytypes, function(i, item){
		//	var status = item.status;
		//	if(status == 0){
		//		$(".isFree_onepage").find("p").text("不开启");
		//	}else{
		//		$(".isFree_onepage").find("p").text("开启");
		//	}
		//	$("#dictidonepage").val(item.dictid);
		//	$("input[name='onepageisFree'][value="+status+"]").prop("checked",true);
		//});
		//
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


//裁剪图片
var jcrop_api = null;

/**
 * 销毁jcrop
 */
function destroyJcrop(){
	jcrop_api.destroy();
}


//原图高度和宽度
var originalWidth=0;
var originalHeight=0;

/**
 * 点击LOGO设置的编辑按钮
 */
function changLOGOImg(){
	$('#saveLOGO').removeClass('hide');
	$('#editLOGO').hide();
	$('#defaultlogo').attr('src','../images/uplogo.png');

	$('#defaultlogo').css("border","1px dashed #ddd")
	$('#defaultlogo').click(function(){
		imgtempClick(this);
	})
}

/**
 * 点击背景图设置的编辑按钮
 */
function changBackgroundImg(){
	$('#saveBackground').removeClass('hide');
	$('#editBackground').hide();
	$('#def_background').attr('src','../images/upbg.png');

	$('#def_background').css("border","1px dashed #ddd")
	$('#def_background').click(function(){
		imgtempClick(this);
	})
}

/**
 * 重新上传图片
 */
function reUpload(){
	var imgname = $("#imgname").val();
	var img;
	if(imgname == "logoimg"){
		img = $("#defaultlogo");
	}
	if(imgname == "backgroundimg"){
		img = $("#def_background");
	}
	imgtempClick(img);
}

/**
 * 点击图片触发点击input file上传
 */
function imgtempClick(o){
	var id = o.id;
	if(id == "defaultlogo"){
		$("#falg").attr("value","logo");
	}else if(id == "def_background"){
		$("#falg").attr("value","bg");
	}
	$(o).parent().find("input[type='file']").click();
}


function showImg2(){
	$("#menuImg-adjust-dialog .menu-img-adjust").html('<img src="" id="target-img">');
	var logoimg = getObjectURL(document.getElementById("logoimg").files[0]);
	var backgroundimg = getObjectURL(document.getElementById("backgroundimg").files[0]);
	var pobj = $("#menu-img-adjust");
	var img$ = $("#target-img");
	img$.removeAttr("style");

	if(logoimg != null){
		img$.attr("src",logoimg);
		$("#imgname").attr("value","logoimg");
		imgLoad(logoimg, calculateImg, img$, pobj);
	}else if(backgroundimg != null){
		img$.attr("src",backgroundimg);
		$("#imgname").attr("value","backgroundimg");
		imgLoad(backgroundimg, calculateImg, img$,pobj);
	}
	$("#logoimg").attr("value","");
	$("#backgroundimg").attr("value","");
	/*$("#falg").attr("value","");
	 $("#falg").attr("value","");*/
	logoimg = null;
	backgroundimg = null;
}

//load图片
var imgLoad = function (src, callback, img$, pobj) {
	var img_= document.createElement('img');
	img_.src=src;
	if (img_.complete) {
		callback(img_.width, img_.height, img$, pobj);
	} else {
		img_.onload = function (status) {
			console.log(status);
			callback(img_.width, img_.height, img$, pobj);
			img_.onload = null;
		};
		img_.onerror=function(){
			$("#menuadd-prompt-modal #prop-msg").text("图片异常或找不到该图片");
			$("#menuadd-prompt-modal").modal("show");
		};
	};
};


/**
 * 计算图片宽高
 */
var falg = null;
function calculateImg(w, h, img$, pobj){
	falg = $("#falg").val();
	var maxW = 700;
	var minW = 360;
	var maxH = 450;
	var minH = 200;
	originalWidth = w;
	originalHeight = h;
	var img_w = w;
	var img_h = h;
	if(img_w/maxW > img_h/maxH){
		if(img_w < minW ){
			img_w = minW;
		}else if(img_w > maxW ){
			img_w= maxW;
		}
		img$.width(img_w);
		var heightV = (h/w)*img_w;
		img$.height(heightV);

		$("#menuImg-adjust-dialog .modal-content").width(img_w+50);
		$("#menuImg-adjust-dialog .modal-content").height(heightV+180);
	}else{
		if(img_h < minH ){
			img_h = minH;
		}else if(img_h > maxH ){
			img_h= maxH;
		}
		img$.height(img_h);
		var widthV = (w/h)*img_h;
		img$.width(widthV);

		$("#menuImg-adjust-dialog .modal-dialog").width(widthV+50);
		$("#menuImg-adjust-dialog .modal-content").width(widthV+50);
		$("#menuImg-adjust-dialog .modal-content").height(img_h+180);
	}

	var cropW=0,croph=0;
	var K = 0;
	var G = 0;
	if(falg == "logo"){
		K = 70;
		G = 60;
	}else if(falg == "bg"){
		K = 1536;
		G = 1950;
	}else{
		K = 50;
		G = 50;
	}
	$('#menuImg-adjust-dialog').on('shown.bs.modal', function () {
		$("#target-img").Jcrop({
			aspectRatio: K/G,
			onSelect: updateCoords,
			onChange: function(c){
				cropW = c.w;
				croph = c.h;
			},
		},function(){
			jcrop_api = this;
			jcrop_api.animateTo([100, 100, 400, 300]);

			fillVal(100, 100, cropW, croph);
		});
		$("#menuImg-adjust-dialog .jcrop-holder").removeClass("hide");
		$(".jcrop-keymgr").css("width", "0px");
		$(".jcrop-keymgr").css("display", "none");
		$(".jcrop-holder").css("margin","0 auto");
		$(".jcrop-holder").css("background-color", "white");
	});
	$("#menuImg-adjust-dialog").modal("show");
	isHidden = $("#menuImg-adjust-dialog").is(":hidden");
	if(!isHidden){
		$("#target-img").Jcrop({
			aspectRatio: K/G,
			onSelect: updateCoords,
			onChange: function(c){
				cropW = c.w;
				croph = c.h;
			},
		},function(){
			jcrop_api = this;
			jcrop_api.animateTo([100, 100, 400, 300]);

			fillVal(100, 100, cropW, croph);
		});
	}
}

/**
 * 拖动裁剪框时改变参数
 * @param c
 */
function updateCoords(c){
	fillVal(c.x, c.y, c.w, c.h);
};

function fillVal(x, y, w, h){
	var o = $("#target-img");
	var width = $(o).width();
	var height = $(o).height();
	w = (originalWidth*w)/width;
	h = (originalHeight*h)/height;
	x = (originalWidth*x)/width;
	y = (originalHeight*y)/height;
	$('#menuImg-adjust-dialog #x').val(x);
	$('#menuImg-adjust-dialog #y').val(y);
	$('#menuImg-adjust-dialog #w').val(w);
	$('#menuImg-adjust-dialog #h').val(h);
}



function adjustpic(){
	var imgname = $("#imgname").val();
	var option = {
		image: "",
		oldimage: "",
		type: "",
		x: "",
		y: "",
		h: "",
		w: ""
	};
	option.image = $("#target-img").attr("src");
	option.x = $('#menuImg-adjust-dialog #x').val();
	option.y = $('#menuImg-adjust-dialog #y').val();
	option.h = $('#menuImg-adjust-dialog #h').val();
	option.w = $('#menuImg-adjust-dialog #w').val();


	console.log({
		x : option.x,
		y : option.y,
		h : option.h,
		w : option.w,
	});

	$.ajaxFileUpload({
		fileElementId: [imgname],
		url: '/newspicyway/padinterface/catImg',
		dataType: 'json',
		contentType:'application/json;charset=UTF-8',
		data : {
			x : option.x,
			y : option.y,
			h : option.h,
			w : option.w,
		},
		success: function (data, textStatus) {
		},
		complete: function (XMLHttpRequest, textStatus) {
			var result = $.parseJSON(XMLHttpRequest.responseText);
			var type = result.type;
			/*var msg = result.msg;
			 if(msg != null || msg != ""){
			 alert(msg);
			 }*/
			if(type == "logo"){
				$("#defaultlogo").attr("src",'/newspicyway/'+result.image);
				$("#logoUrl").attr("value",result.image);
			}else if(type == "bg"){
				$("#def_background").attr("src",'/newspicyway/'+result.image);
				$("#backgroundUrl").attr("value",result.image);
			}
			$("#menuImg-adjust-dialog").modal("hide");
		}
	});
}

/**
 * 保存Logo图
 */
function saveLOGOImg(){
	var dictid = $("#logoDictid").val();
	var imgUrl = $("#logoUrl").val();
	if(imgUrl == ""){
		alert("请选择图片");
		return;
	}
	$.post("../padinterface/setImg",{
		id : dictid,
		itemid : "1",
		itemDesc : "PadLOGO图",
		itemSort : "1",
		itemValue : imgUrl
	},function(data){
		console.log(data);
		window.location.href = global_Path + '/'+data;
	});
}

/**
 * 保存背景图
 */
function saveBackgroundImg(){
	var dictid = $("#backgroundDictid").val();
	var imgUrl = $("#backgroundUrl").val();
	if(imgUrl == ""){
		alert("请选择图片");
		return;
	}
	$.post("../padinterface/setImg",{
		id : dictid,
		itemid : "2",
		itemDesc : "Pad背景图",
		itemSort : "2",
		itemValue : imgUrl
	},function(data){
		console.log(data);
		window.location.href = global_Path + '/'+data;
	});
}

/**
 * @param img
 * @returns
 */
function cutPath(img){
	var image = "";
	if(img != null && img != ""){
		alert(img.split(img_Path));
		image = img.split(img_Path)[1];
		if(image.indexOf("\/") == 0){
			image = image.substring(1, image.length);
		}
	}
	return replaceEscape(image);
}

/**
 * 将路径中的\转换为/
 * @param img
 * @returns
 */
function replaceEscape(img){
	if(img!=null && img!=""){
		img = img.replace("\\", "/");
	}
	return img;
}