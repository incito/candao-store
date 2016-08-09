// 新增或编辑标识
var win_flag = 'add';// edit
var searchBranchName = "";
//隐藏城市选择框
function hideCityDiv(){
	$('.citySelect').css({'display':''});
}
function showDiv(){
	$("#shop-add").modal("show");
	$("#add-btn").show();
	$("#btnsave").removeAttr("disabled" );
	clearAddFrom();
	win_flag = 'add';
	$("label.error").remove();// 清除之前的验证提未信息。
	hideCityDiv();
	$("#edit-btn").hide();//隐藏编辑按钮
	canwrite();
	$("input").removeClass('error');
	$("select").removeClass('error');
}
$(document).ready(function() {
			console.log($("#branchid").val());
			$("img.img-close").hover(function(){
				 	$(this).attr("src",global_Path+"/images/close-active.png");	 
			},function(){
					$(this).attr("src",global_Path+"/images/close-sm.png");
			});
			
			
			$.validator.setDefaults({
				errorPlacement:function(error,element){
					if(element.is("select") && (element.attr("id")=="province" 
						|| element.attr("id")=="city" 
							|| element.attr("id")=="region")){
						 $(element).parent().parent().append(error);
					}else{
						$(element).parent().append(error);
					}
				}
				});
			
			jQuery.validator.addMethod("mobile", function(value, element) {
			    var length = value.length;
			    var mobile =  /^(((1[0-9]{1})|(1[0-9]{1}))+\d{9})$/
			    return this.optional(element) || (length == 11 && mobile.test(value));
			}, "请输入11位手机号");
			
			jQuery.validator.addMethod("phone", function(value, element) {
			    var length = value.length;
			    var flag =true;
			    for(var i=0;i<value.length;i++){
			    	if("0123456789+-".indexOf(value.charAt(i))==-1){
			    		flag=false;
			    		break;
			    	}
			    }
			    return this.optional(element) ||  flag;
			}, "最多为15位数字或 + 、-号");
			
			$(".btn-second").click(function(){
				showDiv();
			});

			$(".btn-first").click(function(){
				showDiv();
			});

			$("#add-form").validate(
					{
						submitHandler : function(form) {
							$("#branchid").attr("disabled","disabled");
							var vcheck = true;
//							if ($("#province").val() == "") {
//								$("#address_tip").html("省 不能为空");
//								vcheck = false;
//							} else {
								//$("#address_tip").html("");
								if ($("#city").val() == "") {
									var err=$("<label class='error' id='city_la'></label>").html("必填信息");
									$("#city").parent().parent().append(err);
									$("#city").addClass("error");
									vcheck = false;
								} else {
									//$("#address_tip").html("");
									if ($("#region").val() == ""
											&& $("#city").val() != "3200"
											&& $("#city").val() != "3300"
											&& $("#city").val() != "3400"
											&& $("#region option").size() > 1) {
										var err=$("<label class='error' id='city_la'></label>").html("必填信息");
										$("#region").parent().parent().append(err);
										$("#region").addClass("error");
										vcheck = false;
									} else {
//										$("#address_tip").html("");
//										if ($("#address").val() == "") {
//											$("#address_tip").html("街道地址不能为空");
//											$("#address").focus();
//											vcheck = false;
//										} else {
//											$("#address_tip").html("");
//										}
									}
								}
							//}

								if (vcheck&&check_sameShopname()) {
									console.log("checked");
									var query={"branchname":$("#branchname").val()};
									 
									if($.trim($("#region option:selected").text())!=""){
										query["region"]=$("#region option:selected").text();
									}else{
										
										if($.trim($("#city option:selected").text())==""){
											query["city"]=$("#city option:eq(0)").text();
										}else{
											query["city"]=$("#city option:selected").text();
										}
										
									}
									query["branchid"]=$("#branchid").val();
									$.ajax({
										url : global_Path + "/shopMg/isRepeatShopName",
										type : "post",
										datatype : "json",
										contentType : "application/json; charset=utf-8",
										data : JSON.stringify(query),
										success : function(obj) {
											if (obj.status == "1") {// there is alread have the same name shop.
												$("#sameCityPrompt").css("display","block");
												$("#branchname").focus();
											}else if(obj.status=="0"){
												// there is not the same shop name.
												$("#sameCityPrompt").css("display","none");
												save_shop();
											}
										},
										error : function(XMLHttpRequest, textStatus, errorThrown) {
											alert(errorThrown);
										}
		
									});
							
							}
								
//							if (vcheck) {
//								save_shop();
//							}
								
								
						}
					});

		});

// 编辑门店时，初始化地址信息。
function initAddressInfo() {
	if ($("#province_").size() == 1) {
		if ($("#city_").val() != "") {
			if ($("#province_").val() == "") {
				selectedByText("province", $("#city_").val());
			} else {
				selectedByText("province", $("#province_").val());
			}
			$("#province").change();

			if ($("#city_").val() != "") {
				selectedByText("city", $("#city_").val());
				$("#city").change();

				if ($("#region_").val() != "") {
					selectedByText("region", $("#region_").val());
					$("#region").change();
				}
			}
		}
	}
}

function hideDialog() {
	$("#shop-add").modal("hide");
}

function readonly(){
	$("#add-form :text,select").attr("disabled",true);
}

function canwrite(){
	$("#add-form :text,select").removeAttr("disabled" );
}

function detail2edit(){
	canwrite();
	$("#add-btn").show();
	$("#edit-btn").hide();//隐藏编辑按钮
	$("#btnsave").removeAttr("disabled");
}
// 编辑
function doEdit(id, flag) {
	console.log("doEdit");
	hideCityDiv();
	$("label.error").remove();// 清除之前的验证提未信息。
	win_flag = 'edit';
	clearAddFrom();//清空弹出框，先。
	if (flag == "detail") {// 表示查看详情
		$("#add-btn").hide();
		$("#edit-btn").show();//show编辑按钮
		$("#btnsave").attr("disabled",true);
		readonly();
	} else {
		$("#add-btn").show();
		$("#edit-btn").hide();//隐藏编辑按钮
		$("#btnsave").removeAttr("disabled");
		canwrite();
	}
	$("#branchid").attr("disabled","disabled");
	$("#shop-add").modal("show");
	$.ajax({
		url : global_Path + "/shopMg/get.json",
		type : "post",
		datatype : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify({
			"id" : id
		}),
		success : function(obj) {
			var objObj=eval("("+obj+")")
//			var str=JSON.stringify(arr); 
			if (objObj.msg == "0") {
				data2Form(objObj.data);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}

	},'json');
}

function data2Form(data) {
	for ( var key in data) {
		$("#add-form input[name='" + key + "']").val(data[key]);
	}
	// 编辑门店时，初始化地址信息。
	if (win_flag == 'edit') {
		initAddressInfo();
	}
}
function del(id) {
	hideCityDiv();
	if (confirm("确定删除该门店？")) {
		$.ajax({
			url : global_Path + "/shopMg/del",
			type : "post",
			datatype : "json",
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify({
				"branchid" : id
			}),
			success : function(data) {
				if (data.msg == "0") {
					alert('删除成功');
					$("#query").submit();
				} else {
					alert('删除失败');
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert(errorThrown);
			}

		});
	}

}

function jumpPage(cur) {
	$("#current").val(cur);
	console.log($("#searchBranchName").val());
//	searchBranchName = $("#searchBranchName").val();
	$("#searchBranchName").val(searchBranchName);
	console.log($("#searchBranchName").val());
	$("form:first").submit();
	//submit后不能改变val值？
}

function doQuery() {
	$("#current").val("1");
	searchBranchName = $("#searchBranchName").val();
}

 
// 新增或编辑、标识
var win_flag = 'add';// edit

//隐藏城市选择框
function hideCityDiv(){
	$('.citySelect').css({'display':''});
} 

// 编辑门店时，初始化地址信息。
function initAddressInfo() {
	if ($("#province_").size() == 1) {
		if ($("#city_").val() != "") {
			if ($("#province_").val() == "") {
				selectedByText("province", $("#city_").val());
			} else {
				selectedByText("province", $("#province_").val());
			}
			$("#province").change();

			if ($("#city_").val() != "") {
				selectedByText("city", $("#city_").val());
				$("#city").change();

				if ($("#region_").val() != "") {
					selectedByText("region", $("#region_").val());
					$("#region").change();
				}
			}
		}
	}
}

function hideDialog() {
	if (win_flag == 'edit') {
		$("#modal_confirmCancel").modal("show");
	}else{
		$("#shop-add").modal("hide");
	}
	
}

function readonly(){
	$("#add-form :text,select").attr("disabled",true);
}

function canwrite(){
	$("#add-form :text,select").removeAttr("disabled" );
}

function detail2edit(){
	canwrite();
	$("#add-btn").show();
	$("#edit-btn").hide();//隐藏编辑按钮
	$("#btnsave").removeAttr("disabled");
}
// 保存门店信息
function save_shop() {
	var shopInfo = {};
	$("#add-form input").each(function(index) {
		shopInfo["" + $(this).attr("name") + ""] = $(this).val();
	});
	//console.log(JSON.stringify(shopInfo));
	$.ajax({
		url : global_Path + "/shopMg/addShop",
		type : "post",
		datatype : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(shopInfo),
		success : function(data) {

			if (data.msg == "0") {
				$(".confirm-box").removeClass("hidden");
				$("#promptMsg").html("保存成功");
				window.setTimeout(function(){
					//$("#successPrompt").modal("hide");
					$("#query").submit();
				}, 1000);
				
				
			} else {
				$("#promptMsg").html("保存失败");
				$(".confirm-box").removeClass("hidden");
				window.setTimeout(function(){
					//$("#successPrompt").modal("hide");
					$("#query").submit();
				}, 1000);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}

	});
}

var id="";// 选中预删除店ID
function doDel() {
		$.ajax({
			url : global_Path + "/shopMg/del",
			type : "post",
			datatype : "json",
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify({
				"branchid" : id
			}),
			success : function(data) {
				canDel_cancel();//隐藏删除确认框
				if (data.msg == "0") {
					//$("#successPrompt").modal("show");
					//$("#promptMsg").html("删除成功");
					//window.setTimeout(function(){
					//	$("#successPrompt").modal("hide");
						$("#query").submit();
					//}, 1000);
				} else {
					$("#promptMsg").html("删除失败");
					$(".confirm-box").removeClass("hidden");
					window.setTimeout(function(){
						 
						$("#query").submit();
					}, 1000);
				}
				
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert(errorThrown);
			}

		});
	 

}



function clearAddFrom(){
	$("#add-form :text,#add-form :hidden:not('option')").val("");
	$("#city").empty();
	$("#region").empty();
	$("input").removeClass('error');
	$("select").removeClass('error');
}

function showCanDel(id1,shopname){
	id=id1;
	$("#candel").modal("show");
	$("#showName").html(shopname);
	
	hideCityDiv();
}

function canDel_cancel(){
	$("#candel").modal("hide");
}

function confirmCancel(){
	$("#shop-add").modal("hide");
}

function showOpr(_this){
	$(_this).css("background","#f4f4f4");
	$(_this).find("div.operate").show();
}

function hideOpr(_this){
	$(_this).css("background","#ffffff");
	$(_this).find("div.operate").hide();
}
function check_sameShopname(){
	var id = $("#branchid").val();
	var branchname = $("#branchname").val();
	var flag=true;
	$.ajax({
		type : "post",
		async : false,
		contentType : "application/json; charset=utf-8",
		data:JSON.stringify({
			branchid:id,
			branchname:branchname
		
		}),
		url : global_Path+"/shopMg/validateShop.json",
		dataType : "json",
		success : function(result) {
			console.log(result);
			if(result=='门店名称不能重复'){
			var str = '<label for="branchname" class="error" style="display: block;">'+result+'</label>';
			$("#branchname").addClass("error").after(str);
//			$("#branchname").focus();
			flag=false;
			}
		}
			
	});
	return flag;
} 