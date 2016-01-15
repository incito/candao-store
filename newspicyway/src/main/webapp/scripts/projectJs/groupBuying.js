/**
 * 团购的脚本
 */

$(document).ready(function() {
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
	//所有门店与选中门店的切换
	$("input[name='applyAll']").click(function(){
		if($(this).val()=='1'){
			$("div.store-select").addClass("hidden");
			$("#store-select-input").val("");
		}else{
			$("div.store-select").removeClass("hidden");
		}

	});

	$.validator.addMethod("isTokenamount", function(value, element) {  
	    var elem = $("#token_amount");
	    return ( parseInt(elem.val()) <= parseInt($(element).val()) );
	}, "记账金额不能大于抵用金额");
	
	//提交表单的时候，进行数据的有效性检验
	$("#groupBuying-form").validate({
		submitHandler : function(form) {
			//TODO 需要完成校验功能;
			saveGroupBuying();
		},

		rules : {
			name : {
				required : true,
				maxlength : 15,
			},
			billamount : {
				required : true,
				number:true,
				isTokenamount:true,
			},
			tokenamount : {
				required : true,
				number:true,
			},
			storeselect : {
				required : true
			}
			
		},
		messages : {
			name : {
				required : "请输入活动名称",
				maxlength : "长度不能超过15",
			},
			billamount : {
				required : "请输入记账金额",
				number: "请输入合法的数字"
			},
			tokenamount : {
				required : "请输入抵用金额",
				number: "请输入合法的数字"
			},
			starttime : {
				required : "请选择有效期开始日期",
			},
			storeselect : {
				required : "请选择指定门店"
			},
			endtime : {
				required : "请选择有效期结束日期",
			},
			billamount:{
				isPrice:"请输入有效的记账金额"
			},
			tokenamount:{
				isPrice:"请输入有效的抵用金额"
			}
		}
	});
		
		
});

function hex(x) {
	return ("0" + parseInt(x).toString(16)).slice(-2);
}

//保存表单
function saveGroupBuying() {
	
	var groupBuying={};
	groupBuying.activity={};//优惠对象
	groupBuying.branchs=[]; //存放所有门店
	groupBuying.groupon={}; //团购的线管内容
	//取页面值，封装进json
	groupBuying.activity.id=$("#id").val();
	groupBuying.activity.name=$("#name").val();
	groupBuying.activity.color=$("#color").val() == "" ? "#9cc05b" : $("#color").val();
	//groupBuying.activity.groupType=$("input[type=radio][name=groupType]:checked").val();
	groupBuying.activity.endtime=$("#endtime").val();
	groupBuying.activity.starttime=$("#starttime").val();
	groupBuying.activity.activityIntroduction=$("#activityIntroduction").val();
	groupBuying.activity.useNotice=$("#useNotice").val();
	
	groupBuying.groupon.bill_amount=$("#bill_amount").val();
	groupBuying.groupon.token_amount=$("#token_amount").val();
	
	//获取是否适用全部门面
	groupBuying.activity["applyAll"]=$("input[name=applyAll]:checked").val()=="1"?true:false;
	//如果不是 适用所有门店，则需要获取指定的门店列表。
	if( !groupBuying.activity["applyAll"] ){
		var allBranchs=$("#select_branchs").val().split(",");
		$.each(allBranchs, function(i, obj) {
			if(obj != ""){
				groupBuying.branchs.push({"branch":obj});
			}
			
		});
	}
	
	$.ajax({
		url : global_Path+ "/preferential/saveGrouponTicket.json",
		type : "POST",
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(groupBuying),
		//data:stampInfo,
		success : function(data) {
			if(data.isSuccess){
				$("#promptMsg").html("保存成功");
				$("#successPrompt").modal("show");
				window.setTimeout(function(){
					$("#successPrompt").modal("hide");
					window.location.href=global_Path+ '/preferential';
				}, 1000);
				
			}else {
				var str = '<label for="name" class="popover-errorTips" style="display: block;">'+data.message+'</label>';
				$("#name").addClass("popover-errorTips").after(str);
				/*if(data.message){
					$("#promptMsg").html(data.message);
				} else {
					$("#promptMsg").html("保存失败");
				}
				$("#successPrompt").modal("show");
				window.setTimeout(function(){
					$("#successPrompt").modal("hide");
				}, 1000);*/
			}
		},
		error : function(XMLHttpRequest, textStatus,errorThrown) {
			alert(errorThrown);
		}
	});
	
	
}

