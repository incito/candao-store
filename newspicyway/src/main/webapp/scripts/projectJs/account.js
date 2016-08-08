/**
 * 总店用户管理 JS
 * @author lishoukun
 * @date 2015/04/22
 */
var global_listurl = 	"t_headuser/queryUserPage";//分页列表URL
var global_saveurl = 	"t_headuser/saveUser";//保存URL
var global_updatestatusurl = 	"t_user/updateStatus";//更新用户状态
var global_detailurl = 	"t_headuser/getUserById";//详细URL
var global_delurl = 	"t_headuser/deleteUserById";//删除URL
var global_roleurl = 	"t_role/roleFunctionCodeList4HeadOffice.json";//总店角色URL
var global_branchurl = 	"shopMg/getall.json";//门店URL
var global_valicode = 	"t_user/sendAccountByMailValicode";//验证码URL
var global_emailsend = 	"t_user/sendAccountByMail";//邮箱发送URL
var global_mobilesend = 	"t_user/sendAccountByMobile";//手机发送URL
var global_roleFuncionCodeObj = new Object();
var global_roleArray = new Array();//已选择的角色数组
var global_page = 1;
var global_rows = 10;
var global_queryParam = {
		page : global_page,
		rows : global_rows
};
var timer;
var sendcounttimer;
var branchSelectObj = new BranchSelect();//选择门店对象
$(function(){
	
	$(".modal").on("shown.bs.modal", function(){
		var count = $(".modal:visible").length;
		$("div.modal-backdrop").eq(count - 1).css("z-index", (parseInt($(this).css("z-index"))-1));
	});
	
	$("#employee-add").on("hide.bs.modal", function(){
		clearInterval(timer);
	});
	/*
	 * 列表页面
	 */
	//添加用户按钮
	$(".btn-first").click(function(){
		add();
	});
	/*
	 * 添加编辑页面
	 */
	// 前往编辑按钮
	$("#toModify").click(function(){
		toUpdate();
	});
	// 取消
	$("#modifyCancel").click(function(){
		cancel();
	});
	$("img.img-close").hover(function(){
	 	$(this).attr("src",global_baseurl+"/images/close-active.png");	 
	},function(){
		$(this).attr("src",global_baseurl+"/images/close-sm.png");
	});
	//保存按钮
//	$("#modifySubmit").click(function(){
//		saveUser();
//	});
	
	$.validator.setDefaults({ ignore: "" }); 
	jQuery.validator.addMethod("mobile", function(value, element) {
	    var length = value.length;
	    var mobile =  /^(((1[0-9]{1})|(1[0-9]{1}))+\d{9})$/
	    return this.optional(element) || (length == 11 && mobile.test(value));
	}, "请输入11位手机号");
	
	jQuery.validator.addMethod("requiredTo", function(value, element) {
		return !($("#email").val() == "" && $(element).val() == "");
	}, "手机和邮箱至少填写一个");
	jQuery.validator.addMethod("accountRoleList_input", function(value, element) {
		return !($("#accountRoleList_input").val() == "");
	}, "请至少选择一个角色!");
	$("#add-form").validate({
		rules:{
			account: {
				required : true,
				maxlength :25,
			},
			name : {
				required : true,
				
			},
			mobile : {
				requiredTo : true,
				mobile : true
			},
			email : {
				email : true
			},
			accountRoleList_input:{
				accountRoleList_input:true
			}
			
		},
		messages: {
			account: {
				maxlength :"最多输入25个字符",
			},
			email : {
				email : "请输入正确的邮箱"
			},
		},
		submitHandler : function(form) {
			saveUser();
		}
	});
	
	//选择角色切换按钮
	$(".selectedText").click(function(){
		$(this).next("i").toggleClass("reverse");
		$(this).siblings("ul").toggle();
	});
	//初始化选择门店对象
	branchSelectObj.init("openSelectTr","openSelectId");
	$("#openSelectId").hover(function(){
		$(this).find(".popover").show();
	}, function(){
		$(this).find(".popover").hide();
	});
	//初始化各项数据
	initTable();
	initRole();
	
	//绑定 创建角色按钮
	$("#btn_create_role").click(function(){
		
		// 调用弹出层，加载页面
		$("#employees_limite").find(".modal-body").html("");
		$("#employees_limite").find(".modal-body").load(global_baseurl+"/t_role/roleList4HeadOffice/edit?id=&isModify=true&callBackMethod=afterCreateRole",function(){
			$(".icon-arr").css("display","none"); //隐藏一个页面中的 小图标。
			$("#employees_limite").modal("show");
		});
		
		
	});
	
//	$("#sendAcc").click(function(){
//		//如果邮箱或者手机号号没填的话，不能进行以下操作。
//		if($("#email").val()==null||$("#email").val()==''||$("#mobile").val()==null||$("#mobile").val()==''){
//			return false;
//		}
//	});
});

/*
 * 创建角色后调用的 回调的函数。用作本页面处理数据。
 	当打开创建角色窗口，点击的是【取消】按钮，则调用 afterCreateRole();
	如果点击保存角色，保存成功后会调用 afterCreateRole(true);
 */
function afterCreateRole(isRefresh){
	 $("#employees_limite").modal("hide");
	// if( isRefresh ){
		 //刷新角色列表。
		 initRole();
		 
	// }
}

//查看权限
function openLimite() {
	//获取select的权限id
	var roleid = '';
	$.each(global_roleArray, function(i, v) { 
		if(roleid==''){
			roleid += v.roleId;
		}else{
			roleid += ','+v.roleId;
		}
	}); 
	$("#employees_limite").find(".modal-body").load(global_baseurl+"/t_role/roleList4Store/detail?scope=02&id="+roleid+"",function(){
		$("#employees_limite").find(".icon-arr").hide();
		$("#employees_limite").find(".limiteInfo").hide();
		$("#employees_limite").find(".btn-operate").hide();
		$("#employees_limite").modal("show");
		
	});
	
}


/**
 * 初始化角色下拉列表
 */
function initRole(){
	//初始化列表
	$.get(global_baseurl+global_roleurl,"", function(dataList){
		
		var selectedArray=global_roleArray.slice(0);; //复制一份数组。一般用作第二次加载的时候临时存储上一次选择的数据
		
		$(".accountRoleList ul").empty();
		$.each(dataList, function(i, v) { 
			//将功能code放到本地缓存Array中
			global_roleFuncionCodeObj[v.id] = v.functions;
			
			var html = 	'<li>';
			html	+=  '<input type="checkbox" id="role_'+v.id+'" name="role" value="'+v.id+'" /> <label for="role_'+v.id+'">'+v.name+'</label>';
			html	+=  '</li>';
			$(".accountRoleList ul").append(html);
		}); 
		
		$(".accountRoleList li").click(function(){
			global_roleArray = new Array();
			var text = "";
			var isShopAdmin = false;
			$(".accountRoleList").find(":checked").each(function(i, v){
				//组织文字
				text += $(this).next().text();
				if(i < $(".accountRoleList").find(":checked").length - 1){
					text += ",";
				}
				//将id放入集合中
				var obj = new Object();
				obj.roleId = $(this).val();
				global_roleArray.push(obj);
				//判断当前角色的权限中是否有门店管理员的权限
				var funList = global_roleFuncionCodeObj[$(this).val()];
				for(var k=0;k<funList.length;k++){
					if(funList[k].code==global_shop_admin_function_code){
						isShopAdmin = true;
						break;
					}
				}
			});
			$(".selectedText").text("").text(text);
			//判断是否有角色
			if(text!=null&&text!=''){
				if($("#seeLimite").length<1){
					$(".accountRoleList").width("173px");
					$(".accountRoleList").find(".selectedText").width("163px");
					var str = '<button type="button" class="btn in-btn200" id="seeLimite" onclick="openLimite()">查看权限</button>';
					$(".accountRoleList").after(str);
				}
			}else{
				$(".accountRoleList").width("260px");
				$(".accountRoleList").find(".selectedText").width("250px");
				$("#seeLimite").remove();
				$(".accountRoleList").parents("table.employee_add_table").find("tr.bAccount").remove();
			}
			//判断是否门店管理员
			if(isShopAdmin){
				branchSelectObj.showCombo();
			}else{
				branchSelectObj.hideCombo();
				branchSelectObj.resetCombo();
				branchSelectObj.resetDialog();
			}
			
			//更新  accountRoleList_input
			$("#accountRoleList_input").val($(".accountRoleList").find(":checked").val() );
			$("#add-form"). valid();//调用校验
		});
		
		 //还原已经选择的权限角色
		 $.each(selectedArray, function(i , element ){
			$(".accountRoleList").find("input[type=checkbox][value='"+element.roleId+"']").prop("checked",true);
		 } );
		 
	});
}
/**
 * 加载列表
 */
function initTable(){
	//初始化列表
	$.post(global_baseurl+global_listurl, global_queryParam, function(result){
		$(".table-list tbody").empty();
		var data = $.parseJSON(result).data;
		var dataList = data.rows;
		$.each(dataList, function(i, v) { 
			//计算编号
			var baseNumber = (data.current - 1 )*data.pageSize;
			var html = 	'<tr>';
			html 	+=	'<td>'+(baseNumber+i+1)+'</td>';
			html 	+=	'<td>'+v.account+'</td>';
			html 	+=	'<td>'+v.name+'</td>';
			html 	+=	'<td>'+v.mobile+'</td>';
			html 	+=	'<td>'+v.email+'</td>';
			html 	+=	'<td>';
			if(v.status=='01'){
				html 	+=	'<div class="switch demo3">';
				html 	+=	'<input type="checkbox" checked value="'+v.id+'" ">';
				html 	+=	'<label><i data-on="启用" data-off="停用"></i></label>';
				html 	+=	'</div>';
			}else{
				html 	+=	'<div class="switch demo3">';
				html 	+=	'<input type="checkbox" value="'+v.id+'" >';
				html 	+=	'<label><i data-on="启用" data-off="停用"></i></label>';
				html 	+=	'</div>';
			}
			html 	+=	'</td>';
			html 	+=	'<td class="td-last">';
			html 	+=	'	<div class="operate">';
			html 	+=	'		<a href="javascript:detail(\''+v.id+'\')">查看</a>';
			html 	+=	'		<a href="javascript:update(\''+v.id+'\')">修改</a>';
			html 	+=	'		<a href="javascript:del(\''+v.id+'\',\''+v.name+'\')">删除</a>';
			if((v.email!=null&&v.email!='')||(v.mobile!=null&&v.mobile!='')){
				html 	+=	'		<a href="javascript:sendAccount(\''+v.account+'\',\''+v.email+'\',\''+v.mobile+'\',\''+v.id+'\')">发送账号</a>';
			}
			
			html 	+=	'	</div>';
			html 	+=	'</td>';
			html 	+=	'</tr>';
			
			$(".table-list tbody").append(html);
		}); 
		//分页实现,只有在无页数的时候才会加载分页条
		if(parseInt(data.pageCount) > 1 && $("ul.paging").length == 0){
			if(data.pageCount < global_queryParam.page){
				global_queryParam.page = data.pageCount;
			}
			$(".pagingWrap").html('<ul class="paging clearfix"></ul>');
    		$(".pagingWrap .paging").twbsPagination({
				totalPages: data.pageCount,
				visiblePages: 7,
    			startPage : parseInt(global_queryParam.page),
				first: '...',
    			prev : '<',
    			next : '>',
		        last: '...',
		        onPageClick: function (event, page){
		        	global_queryParam.page = page;
		        	initTable();
		        }
			});
		}else if(parseInt(data.pageCount) <= 1){
			$(".pagingWrap").empty();
		}
		
		$(".switch input[type=checkbox]").click(function(){
			var isCheck = $(this).prop("checked");
			if(isCheck){
				changeStatus($(this).val(),"01");
			}else{
				changeStatus($(this).val(),"02");
			}
		});
	});
	
}

/**
 * 保存用户方法
 */
function saveUser(){
	var dataObj = getFormData();
	//提交数据
	$.post(global_baseurl+global_saveurl, dataObj, function(result){
		var data = $.parseJSON(result);
		if(data.success){
			if($("#employee-add").attr("data-type") == "new"){
				//以下几句有效代码，是将发送帐号窗口改为发送账户信息，显示创建帐号。。。那一行，将id保存到sendType sendId隐藏域中（用于发送账户后计时）
				$("#sendType .title").text("发送账户信息");
				$("#sendTypeAccountWrap").show();
				$("#sendType input[name='sendId']").val(data.data.id);
				sendAccountSecond();
			}else{
				sendMsg(true, '保存成功!');
			}
			$("#employee-add").modal("hide");
			initTable();
		}else{
			if(data.msg!=null&&data.msg!=''){
				var str = '<label for="account" class="error" style="display: block;">'+data.msg+'</label>';
				$("#account").addClass("error").after(str);
//				sendMsg(false,data.msg);
			}else{
				sendMsg(false,'保存失败!');
			}
		}
	});
}
/**
 * 切换启用状态
 * @param id
 */
function changeStatus(id,status){
	var dataObj = new Object();
	dataObj.id = id;
	dataObj.status = status;
	//提交数据
	$.post(global_baseurl+global_updatestatusurl, dataObj, function(result){
		var data = $.parseJSON(result);
		if(data.success){
			sendMsg(true,'操作成功!');
		}else{
			if(data.msg!=null&&data.msg!=''){
				sendMsg(false,data.msg);
			}else{
				sendMsg(false,'操作失败!');
			}
		}
	});
}
/**
 * 添加用户
 */
function add(){
	$("#sendAcc").hide().siblings().removeClass("viewAccountWrap");
	$("#modify-detail").hide();
	$("#modify-opera").show();
	$("#employee-add").modal("show").attr("data-type","new");
	$("#employee-add").find(".modal-header label").text("新建账户");
	$(".shops").hide();
	resetForm();
	enabledForm();
	$("label.error").remove();// 清除之前的验证提未信息。
	$("input, select").removeClass('error');
}
/**
 * 取消
 */
function cancel(){
	var dialog = $("#employee-add");
	dialog.modal("hide");
}
/**
 * 前往编辑用户
 */
function toUpdate(){
	$("#sendAcc").show().siblings("div").addClass("viewAccountWrap");
	$("#modify-detail").hide();
//	$(".selectedText").click(function(){
//		$(this).next("i").toggleClass("reverse");
//		$(this).siblings("ul").toggle();
//	});
	$("#modify-opera").show();
	$("#employee-add").attr("data-type","old");
	$("#employee-add").find(".modal-header label").text("修改账户");
	enabledForm();
}
/**
 * 查看用户详细
 * @param id
 */
function detail(id){
	$("label.error").remove();// 清除之前的验证提未信息。
	$("input, select").removeClass('error');
	$.get(global_baseurl+global_detailurl+"/"+id,"", function(result){
		var data = $.parseJSON(result).data;
		$("#sendAcc").attr("data-id",data.id).show().siblings("div").addClass("viewAccountWrap");
		
		$("#modify-detail").show();
		$("#modify-opera").hide();
		
		$("#employee-add").find(".modal-header label").text("查看账户");
		
		resetForm();
		$("#id").val(data.id);
		setFormData(data);
		disabledForm();
		var num = localStorage.getItem(data.id);
		if(num != null){
			var curSeconds = getCurSeconds();
			var num = 120 - (curSeconds - num);
			$("#sendAcc").prop("disabled", true);
			timer = setInterval(function(){
				$("#sendAcc").text("已发送 " + num);
				if(num < 1){
					localStorage.removeItem(data.id);
					$("#sendAcc").prop("disabled", false);
					$("#sendAcc").text("发送账号信息");
					clearInterval(timer);
				}
				num--;
			}, 1000);
		}else{
			$("#sendAcc").prop("disabled", false);
			$("#sendAcc").text("发送账号信息");
		}
		$("#employee-add").modal("show").attr("data-type","old");
	});
}

/**
 * 更新用户详细
 * @param id
 */
function update(id){
	$("label.error").remove();// 清除之前的验证提未信息。
	$("input, select").removeClass('error');
	$.get(global_baseurl+global_detailurl+"/"+id,"", function(result){
		var data = $.parseJSON(result).data;
		var num = localStorage.getItem(data.id);
		if(num != null){
			var curSeconds = getCurSeconds();
			var num1 = 120 - (curSeconds - num);
			$("#sendAcc").prop("disabled", true);
			timer = setInterval(function(){
				$("#sendAcc").text("已发送 " + num1);
				if(num1 < 1){
					localStorage.removeItem(data.id);
					$("#sendAcc").prop("disabled", false);
					$("#sendAcc").text("发送账号信息");
					clearInterval(timer);
				}
				num1--;
			}, 1000);
		}else{
			$("#sendAcc").prop("disabled", false);
			$("#sendAcc").text("发送账号信息");
		}
		$("#sendAcc").show().siblings("div").addClass("viewAccountWrap");
		$("#toModify").parent().hide();
		$("#modify-opera").show();
		$("#employee-add").modal("show").attr("data-type","old");
		$("#employee-add").find(".modal-header label").text("修改账户");
		
		resetForm();
		$("#id").val(data.id);
		setFormData(data);
		enabledForm();
	});
}

/**
 * 删除用户
 * @param id
 */
function del(id,name){
	//显示删除提示框
	$("#deleteComfirm").modal("show");
	$("#deleteComfirm #showName").text(name);
	//同意删除
	$("#deleteComfirm #deleteComfirmOk").unbind("click").click(function(){
		$("#deleteComfirm").modal("hide");
		$.get(global_baseurl+global_delurl+"/"+id,"", function(result){
			var data = $.parseJSON(result);
			if(data.success){
				sendMsg(true,'删除成功!',function(){
					if($(".table-list tbody tr").length == 1){
						if(global_queryParam.page != 1){
							global_queryParam.page = parseInt(global_queryParam.page) - 1;
						}
					}
					if(global_queryParam.page == ""){
						global_queryParam.page = "1";
					}
					
					initTable();
				});
			}else{
				if(data.msg!=null&&data.msg!=''){
					sendMsg(false,data.msg);
				}else{
					sendMsg(false,'删除失败!',function(){
						initTable();
					});
				}
			}
		});
	});
	//同意删除
//	$("#deleteComfirmNo").add(".img-close").unbind("click").click(function(){
//		$("#deleteComfirm").modal("hide");
//	});
}
/*
 * 打开门店选择窗口
 */
function openSelectShop (){
	if(!$(".add-shop-select").hasClass("disabled")){
		$("#store-select-dialog").modal("show");
	}
}
/**
 * 选择门店
 */
function selectShop(){
	global_branchArray = [];
	var text='';
	var ul = $("<ul/>").addClass("storesDiv");
	$("#store-select-dialog :checkbox:checked").each(function(i, v){
			text += $(this).next().text();
			if(text != ''){
				text += ",";
			}
			var obj = new Object();
			obj.branchId = $(this).val();
			obj.branchName = $(this).next().text();
			global_branchArray.push(obj);
			ul.append("<li>"+obj.branchName+"</li>")
	});
	
	if(global_branchArray.length > 0){
		$(".add-shop-select").find("div.popover").remove();
		var top = ileft = iwidth ="";
		if(global_branchArray.length >= 3){
			iwidth = "460px";
			ileft = "-70px";
			
		}
		var div = $("<div>").addClass("popover fade bottom in").css({
			width : iwidth,
			top : "30px",
			left: ileft
		}).append('<div class="arrow" style="left: 50%;"></div>');
		div.append(ul);
		$(".add-shop-select").text("已选中"+global_branchArray.length + "家店").append(div);
	}else{
		$(".add-shop-select").html('<img src="/newspicyway/images/add.png" alt=""> ').next(".popover").remove();
	}
	
	$("#store-select-dialog").modal("hide");
	
	
	/*
	//如果没有选择，则将图标显示为图片
	if(text==''){
		$('.add-shop-select').html('<img src="/newspicyway/images/add.png" alt="">');
	}else{
		if(text.length>30){
			text = text.substring(0,30)+'...';
		}
		$('.add-shop-select').html(text);
	}
	$("#store-select-dialog").modal("hide");*/
}
/*
 * 打开发送方式窗口
 */
function sendAccount(account,email,mobile,id){
	$("#sendType .title").text("发送方式");
	$("#sendTypeAccountWrap").hide();
	sendAccountSecond(account,email,mobile,id);
}
/**
 * 发送帐号，第二步
 * @param account
 * @param email
 * @param mobile
 * @param id
 */
function sendAccountSecond(account,email,mobile,id){
	$("#sendType input[name='sendId']").val("");
	if(id!=null){
		$("#sendType input[name='sendId']").val(id);
	}
	if(email!=null){
		$("#checkCode input[name='email']").val(email);
	}else{
		$("#checkCode input[name='email']").val($("#email").val());
	}
	if(mobile!=null){
		$("#checkCode input[name='mobile']").val(mobile);
	}else{
		$("#checkCode input[name='mobile']").val($("#mobile").val());
	}
	if(account!=null){
		$("#checkCode input[name='account']").val(account);
	}else{
		$("#checkCode input[name='account']").val($("#account").val());
	}
	
	if($("#checkCode input[name='account']").val()!=null&&$("#checkCode input[name='account']").val()!=''){
		$("#sendType").modal("show");
		$("#sendTypeAccount").text($("#account").val());
		
		var num = localStorage.getItem(id);
		var curSeconds = getCurSeconds();
		var num1 = 120 - (curSeconds - num);
		if(num1 > 0){
			$("#sendAccountEmail").attr("disabled","disabled");
			$("#sendAccountPhone").attr("disabled","disabled");
		}else{
			initSendBtn();
		}
	}
}
/**
 * 获取当前秒数
 * @returns
 */
function getCurSeconds(){
	var myDate = new Date();
	var curSeconds = myDate.getHours() * 3600 + myDate.getMinutes() * 60 + myDate.getSeconds();
	return curSeconds;
}
/**
 * 判断邮箱验证还是手机验证可用
 */
function initSendBtn(){
	//判断是否有邮箱，如果没有邮箱，则不能用邮箱发送
	if($("#checkCode input[name='email']").val()!=null&&$("#checkCode input[name='email']").val()!=''){
		$("#sendAccountEmail").removeAttr("disabled");
	}else{
		$("#sendAccountEmail").attr("disabled","disabled");
	}
	//判断是否有手机，如果没有手机，则不能用手机发送
	if($("#checkCode input[name='mobile']").val()!=null&&$("#checkCode input[name='mobile']").val()!=''){
		$("#sendAccountPhone").removeAttr("disabled");
	}else{
		$("#sendAccountPhone").attr("disabled","disabled");
	}
}
/**
 * 发送账号成功之后，等待120秒
 */
function sendTimeOut_fun(){
	$("#sendAccountEmail").prop("disabled", true);
	$("#sendAccountPhone").prop("disabled", true);
	sendcounttimer = setInterval(function(){
		initSendBtn();
		clearInterval(sendcounttimer);
	}, 120000);
}
/*
 * 发送方式点击，打开验证码窗口
 */
function sendAccountWay(type){
	var curSeconds = getCurSeconds();
	if($("#sendType input[name='sendId']").val()!=null&&$("#sendType input[name='sendId']").val()!=''){
		localStorage.setItem($("#sendType input[name='sendId']").val(), curSeconds);
	}else{
		//创建、编辑账号的时候 使用
		$("#sendAcc").prop("disabled", true);
		var id = $("#id").val();
		var num = 120;
		localStorage.setItem(id, curSeconds);
		timer = setInterval(function(){
			$("#sendAcc").text("已发送 " + num);
			if(num < 1){
				localStorage.removeItem(id);
				$("#sendAcc").prop("disabled", false);
				$("#sendAcc").text("发送账号信息");
				clearInterval(timer);
			}
			num--;
		}, 1000);
	}
	
	if(type==1){
		var url = global_baseurl + global_emailsend;
		var dataObj = new Object();
		dataObj.account = $("#checkCode input[name='account']").val();
		dataObj.email = $("#checkCode input[name='email']").val();
		//提交数据
		$.post(url, dataObj, function(result){
			var data = $.parseJSON(result);
			if(data.success==true){
				var msg = '';
				if(data.msg!=null){
					msg = data.msg;
				}else{
					msg = '帐号发送成功！';
				}
				$("#sendType").modal("hide");
				sendTimeOut_fun();
				sendMsg(true,msg);
			}else{
				var msg = '';
				if(data.msg!=null){
					msg = data.msg;
				}else{
					msg = '帐号发送失败！请重发';
				}
				sendMsg(false,msg);
			}
		});
	}else if(type==2){
		$("#checkCode").modal("show");
		$("#valicodeImg").attr("src",global_valicode+"?"+Math.random());
		$("#checkCode .errorTips").html("");
		$("#valicodeText").val("").removeClass("error");
		$("#valicodeButton").unbind("click");
		$("#valicodeButton").click(function(){
			var url = global_baseurl + global_mobilesend;
			var dataObj = new Object();
			dataObj.account = $("#checkCode input[name='account']").val();
			if($("#valicodeText").val()==null||$("#valicodeText").val()==''){
				$("#checkCode .errorTips").html('请输入验证码');
				return false;
			}
			dataObj.valicode = $("#valicodeText").val();
			dataObj.mobile = $("#checkCode input[name='mobile']").val();
			//提交数据
			$.post(url, dataObj, function(result){
				var data = $.parseJSON(result);
				if(data.success==true){
					var msg = '';
					if(data.msg!=null){
						msg = data.msg;
					}else{
						msg = '帐号发送成功！';
					}
					$("#checkCode").modal("hide");
					$("#sendType").modal("hide");
					sendTimeOut_fun();
					
					sendMsg(true,msg,function(){
						$("#checkCode .errorTips").html('');
					});
				}else{
					var msg = '';
					if(data.msg!=null){
						msg = data.msg;
					}else{
						msg = '图文验证码错误';
					}
					$("#checkCode .errorTips").html(msg);
					//重置验证码
					$("#valicodeImg").attr("src",global_valicode+"?"+Math.random());
					$("#valicodeText").val("").addClass("error");;
				}
			});
		});
	}
}
/**
 * 重置验证码
 */
function resetValicode(){
	$("#valicodeImg").attr("src",global_valicode+"?"+Math.random());
	$("#checkCode .errorTips").html("");
	$("#valicodeText").val("").removeClass("error");
}
/**
 * 重置表单
 */
function resetForm(){
	$("#id").val("");
	$("#add-form")[0].reset();
	$(".selectedText").text("");
	
	branchSelectObj.resetCombo();
	branchSelectObj.resetDialog();
	global_roleArray = new Array();
}
/**
 * 禁用表单
 */
function disabledForm(){
	$("#account").attr("disabled","disabled");  
	$("#name").attr("disabled","disabled"); 
	$("#email").attr("disabled","disabled"); 
	$("#mobile").attr("disabled","disabled"); 
	$(".labelText").attr("disabled","disabled"); 
	//禁用角色下拉列表
	$(".accountRoleList input[type=checkbox]").attr("disabled","disabled"); 

	//禁用门店选择
	$(".add-shop-select").addClass("disabled");
	// 禁用新建角色
	$("#addRole").prop("disabled", true);
//	//禁用确定选择门店按钮
//	$("#store-select-confirm").attr("disabled","disabled"); 
//	//禁用全选和全不选单选框
//	$("#checkAll").attr("disabled","disabled"); 
//	$("#noAll").attr("disabled","disabled"); 
//	//禁用所有门店复选框
//	$("#store-select-dialog input[name='branch']").prop('disabled',"disabled");

	//启用禁用
	$("#statusOn").attr("disabled","disabled");
	$("#statusOff").attr("disabled","disabled");
}
/**
 * 启用表单
 */
function enabledForm(){
	$("#account").removeAttr("disabled");  
	$("#name").removeAttr("disabled"); 
	$("#email").removeAttr("disabled"); 
	$("#mobile").removeAttr("disabled"); 
	$(".labelText").removeAttr("disabled"); 
	//角色下拉列表
	$(".accountRoleList input[type=checkbox]").removeAttr("disabled"); 

	// 启用门店选择
	$(".add-shop-select").removeClass("disabled");
	
	// 启用新建角色
	$("#addRole").prop("disabled", false);
	
//	//确定选择门店按钮
//	$("#store-select-confirm").removeAttr("disabled"); 
//	//全选和全不选单选框
//	$("#checkAll").removeAttr("disabled"); 
//	$("#noAll").removeAttr("disabled"); 
//	//所有门店复选框
//	$("#store-select-dialog input[name='branch']").removeAttr('disabled',"disabled");

	//启用禁用
	$("#statusOn").removeAttr("disabled");
	$("#statusOff").removeAttr("disabled");
}
/**
 * 获取表单数据
 * @returns 
 */
function getFormData(){
	//获取表单数据
	var dataArray = $("#add-form").serializeArray();
	//拼凑提交数据
	var dataObj = new Object();
	$.each(dataArray, function(i, v) { 
		if(!(v.name in dataObj)){
			dataObj[v.name] = v.value;
		}
	});
	dataObj['rolesJson'] = JSON.stringify(global_roleArray);
	dataObj['branchsJson'] = branchSelectObj.getJsonData();
	return dataObj;
}
/**
 * 设置表单数据
 */
function setFormData(data){
	$("#account").val(data.account);
	$("#name").val(data.name);
	$("#email").val(data.email);
	$("#mobile").val(data.mobile);
	
	//如果当前用户的角色id中有门店管理员id相同的，则显示门店框
	global_roleArray = data.roles;
	var isShopAdmin = false;
	var text = '';
	$.each(data.roles,function(i, v){
		$(".accountRoleList [name='role'] ").each(function(ii, vv){
			if($(this).val()==v.roleId){
				$(this).prop("checked",true);
				if(text!=''){
					text += ',';
				}
				text += $(this).next().text();
			}
		});
		//判断当前角色的权限中是否有门店管理员的权限
		var funList = global_roleFuncionCodeObj[v.roleId];
		if(funList!=null){
			for(var k=0;k<funList.length;k++){
				if(funList[k].code==global_shop_admin_function_code){
					isShopAdmin = true;
					break;
				}
			}
		}
		//更新accountRoleList_input，这是为了验证
		$("#accountRoleList_input").val(v.roleId);
	});
	$(".selectedText").text("").text(text);
	//判断是否有角色
	if(text!=null&&text!=''){
		if($("#seeLimite").length<1){
			$(".accountRoleList").width("173px");
			$(".accountRoleList").find(".selectedText").width("163px");
			var str = '<button type="button" class="btn in-btn200" id="seeLimite" onclick="openLimite()">查看权限</button>';
			$(".accountRoleList").after(str);
		}
	}else{
		$(".accountRoleList").width("260px");
		$(".accountRoleList").find(".selectedText").width("250px");
		$("#seeLimite").remove();
		$(".accountRoleList").parents("table.employee_add_table").find("tr.bAccount").remove();
	}
	//判断是否门店管理员
	if(isShopAdmin){
		branchSelectObj.showCombo();
	}else{
		branchSelectObj.hideCombo();
	}
	
	branchSelectObj.setData(data.branchs);

	//为门店id赋值
	global_branchArray = data.branchs;
	var branchText = '';
	$.each(data.branchs,function(i, v){
		if(branchText!=''){
			branchText += ',';
		}
		branchText += v.branchName;
		$("#store-select-dialog input[name='branch'][value='"+v.branchId+"']").prop('checked',true);
	});
	
	
	
	if(global_branchArray.length > 0){
		var ul = $("<ul/>").addClass("storesDiv");
		
		$.each(global_branchArray, function(i, v){
			ul.append("<li>"+v.branchName+"</li>")
		});
		$(".add-shop-select").find("div.popover").remove();
		var top = ileft = iwidth ="";
		if(global_branchArray.length >= 3){
			iwidth = "460px";
			ileft = "-70px";
			
		}
		var div = $("<div>").addClass("popover fade bottom in").css({
			width : iwidth,
			top : "30px",
			left: ileft
		}).append('<div class="arrow" style="left: 50%;"></div>');
		div.append(ul);
		$(".add-shop-select").text("已选中"+global_branchArray.length + "家店").append(div);
	}else{
		$(".add-shop-select").html('<img src="/newspicyway/images/add.png" alt=""> ').next(".popover").remove();
	}
	
	
/*	//如果没有选择，则将图标显示为图片
	if(branchText==''){
		$('.add-shop-select').html('<img src="/newspicyway/images/add.png" alt="">');
	}else{
		if(branchText.length>28){
			branchText = branchText.substring(0,28)+'...';
		}
		$('.add-shop-select').html(branchText);
	}*/

	if(data.status=="01"){
		$("#statusOn").prop("checked",true);
	}else if(data.status=="02"){
		$("#statusOff").prop("checked",true);
	}
}
/**
 * 发送消息
 * @param isOk 是否成功
 * @param msg
 * @param callback
 * @returns
 */
function sendMsg(isOk,msg,callback){
	if(isOk){
		$("#successPrompt .modal-body i").removeClass("icon-fail").addClass("icon-success");
	}else{
		$("#successPrompt .modal-body i").removeClass("icon-success").addClass("icon-fail");
	}
	$("#successPrompt").find("#promptMsg").parent().html('<label id="promptMsg"></label>');
	var msgArray = msg.split("！");
	if(msgArray.length>1){
		for(var i=0;i<msgArray.length;i++){
			if(i==0){
				$("#successPrompt").find("#promptMsg").html(msgArray[0]+"！\n");
			}else if(i==msgArray.length-1){
				$("#successPrompt").find("#promptMsg").parent().append('<label>'+msgArray[i]+'</label>');
			}else{
				$("#successPrompt").find("#promptMsg").parent().append('<label>'+msgArray[i]+'！</label>');
			}
		}
	}else{
		$("#successPrompt").find("#promptMsg").html(msg);
	}
	
	$("#successPrompt").modal("show");
	if(callback!=null){
		window.setTimeout(callback, 1000);
		window.setTimeout('$("#successPrompt").modal("hide")', 1000);
	}else{
		window.setTimeout('$("#successPrompt").modal("hide")', 1000);
	}
}

/**
* 供主页面查询调用
* text 查询的内容
*/
function searchDataFromMain(text){
	global_queryParam.searchText = text;
	initTable();
}