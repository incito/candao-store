$(function(){
	// 添加租户
	$(".btn-first").click(function(){
		showAdd();
	});
	page = 1;
//	if(window.localStorage){
//		page = typeof(localStorage.currentPage) == "undefined" ? "1" : localStorage.currentPage;
//	}else{
//		page = "1";
//	}
	findData(page, null);
	$("#tenant-add").on("hide.bs.modal", function(){
		clearInterval(timer);
	})
	$("#sendAcc").click(function(){
		//如果邮箱或者手机号号没填的话，不能进行以下操作。
		if($("#email").val()==null||$("#email").val()==''||$("#mobile").val()==null||$("#mobile").val()==''){
			return false;
		}
		
		var $this = $(this);
		var myDate = new Date();
		var curSeconds = myDate.getHours() * 3600 + myDate.getMinutes() * 60 + myDate.getSeconds();
		$this.prop("disabled", true);
		var id = $("#id").val();
		var num = 120;
		localStorage.setItem(id, curSeconds)
		timer = setInterval(function(){
			$this.text("已发送 " + num);
			if(num < 1){
				localStorage.removeItem(id);
				$this.prop("disabled", false);
				$this.text("发送账号信息");
				clearInterval(timer);
			}
			num--;
		}, 1000)
	});
});
var timer;
function findData(subPage, id,searchText){
	var dataObj = new Object();
	dataObj.page = subPage;
	dataObj.rows = 10;
	dataObj.searchText = searchText;
	
	$.ajax({
		url:contextPath+"/tenant/page.json",
		data:dataObj,
		type:"post",
		dataType:"json",
		success:function(data){
			var table = $("table.tenant-table");
			var dataBlock = table.find("tbody");
			var str = "", classToggle, starttime, endtime;
			$.each(data.rows, function(i, v){
				classToggle = i % 2 == 0 ? "odd" : "";
				str += '<tr>';
				str	+= '<td>'+(i+1)+'</td>';
				str	+= '<td>'+v.account+'</td>';
				str	+= '<td>'+v.businessName+'</td>';
				str	+= '<td>'+v.name+'</td>';
				if(v.email!=null){
					str	+= '<td>'+v.email+'</td>';
				}else{
					str	+= '<td></td>';
				}
				if(v.mobile!=null){
					str	+= '<td>'+v.mobile+'</td>';
				}else{
					str	+= '<td></td>';
				}
				str	+= '<td>';
				if(v.areaCode!=null){
					str	+= v.areaCode+'-';
				}
				if(v.telephone!=null){
					str	+= v.telephone;
				}
				str	+= '</td>';
				str	+= '<td>';
				if(v.province!=null){
					str	+= v.province;
				}
				if(v.city!=null){
					str	+= v.city;
				}
				if(v.region!=null){
					str	+= v.region;
				}
				str	+= v.address+'</td>';
				str	+= '<td class="td-last">';
				str	+= '<div class="operate">';
				str	+= '	<a href="javascript:void(0)" onclick="operaTenant(\''+v.id+'\',\'look\')">查看</a>';
				str	+= '	<a href="javascript:void(0)" onclick="operaTenant(\''+v.id+'\',\'modify\')">修改</a>';
				str	+= '	<a href="javascript:void(0)" class="deleteBtn" onclick="deleteTenant(\''+v.id+'\',\''+i+'\',\''+v.businessName+'\')">删除</a>';
				if((v.email!=null&&v.email!='')||(v.mobile!=null&&v.mobile!='')){
					str	+= '	<a href="javascript:sendAccount(\''+v.account+'\',\''+v.email+'\',\''+v.mobile+'\',\''+v.id+'\')">发送账号</a>';
				}
				str	+= '</div>';
				str	+= '</td>';
				str	+= '</tr>';
			})
			dataBlock.empty().append(str);
			if(data.total > 10 && $("ul.paging").length == 0){
				$(".pagingWrap").html('<ul class="paging clearfix">');
        		$(".paging").twbsPagination({
					totalPages: data.pageCount,
        			visiblePages: 7,
        			startPage : parseInt(subPage),
        			first: '...',
        			prev : '<',
        			next : '>',
			        last: '...',
			        onPageClick: function (event, page){
			        	localStorage.currentPage = page;
			        	findData(page, null);
			        }
				});
        		
			}else if(data.total <= 10){
				$(".pagingWrap").empty();
			}
		}
	});
}

//显示新增租户窗口
function showAdd(){
	$("#add-form")[0].reset();
	//如果表单项被禁用的情况，将表单项放开
	$("#add-form").find("input").removeAttr("disabled");
	$("#add-form").find("select").removeAttr("disabled");
	
	$("#tenant-add").modal("show");
	$("label.error").remove();
	$(".error").removeClass("error");
	$("div.tenantInfo span").text("新建租户");
	$("#accountDiv").css("display", "none");
	$("#add-btn").css("display", "block");
	$("#edit-btn").css("display", "none");
	$("#btnsave").unbind("click").click({"isAdd":true},saveTenant);
	
	
}

//查看编辑租户信息
function operaTenant(id, viewType){
	var url = contextPath+"/tenant/get.json?id="+id;
	$("label.error").remove();
	$(".error").removeClass("error");
	$.post(url,function(result){
		if(result.isSuccess){
			var user = result.user;
			$("p.codeLabel").text(user.account);
			$("#name").val(user.name);
			$("#id").val(user.id);
			$("#email").val(user.email);
			$("#mobile").val(user.mobile);
			$("#areaCode").val(user.areaCode);
			$("#telephone").val(user.telephone);
			$("#businessName").val(user.businessName);
			$("#province_").val(user.province);
			$("#city_").val(user.city);
			$("#region_").val(user.region);
//			$("input[type='hidden'][name='province']").val(user.province);
			$("#address").val(user.address);
			$("#tenant-add").modal("show");
			$("#accountDiv").css("display", "block");
			initAddressInfo();
			if(viewType == "look"){ //查看
				$("#add-btn").css("display", "none");
				$("#edit-btn").css("display", "block");
				$("div.tenantInfo span").text("查看租户信息");
				$("#add-form").find("input").attr("disabled","disabled");
				$("#add-form").find("select").attr("disabled","disabled");
			}else{ // 编辑
				$("#add-btn").css("display", "block");
				$("#edit-btn").css("display", "none");
				$("div.tenantInfo span").text("编辑租户信息");
				$("#add-form").find("input").removeAttr("disabled");
				$("#add-form").find("select").removeAttr("disabled");
				$("#btnsave").unbind("click").click({"isAdd":false},saveTenant);
			} 
			var num = localStorage.getItem(id);
			console.log(num);
			if(num != null){
				var myDate = new Date();
				var curSeconds = myDate.getHours() * 3600 + myDate.getMinutes() * 60 + myDate.getSeconds();
				var num = 120 - (curSeconds - num);
				$("#sendAcc").prop("disabled", true)
				timer = setInterval(function(){
					$("#sendAcc").text("已发送 " + num);
					if(num < 1){
						localStorage.removeItem(id)
						$("#sendAcc").prop("disabled", false)
						$("#sendAcc").text("发送账号信息");
						clearInterval(timer);
					}
					num--;
				}, 1000);
			}else{
				$("#sendAcc").prop("disabled", false)
				$("#sendAcc").text("发送账号信息");
			}
		} else {
			$("#promptMsg").html(result.message);
			$("#successPrompt").modal("show");
			window.setTimeout(function(){
				$("#successPrompt").modal("hide");
			}, 1000);
		}
	},"json");
	
	
}

function saveTenant(event){
	if(!$("#add-form").valid()){
		return false;
	}
	
	var url, oper;
	var data = $("#add-form").serialize();
	if(event.data.isAdd == true){
		url = contextPath+"/tenant/add.json";
		oper = "添加";
	} else{
		url = contextPath+"/tenant/save.json";
		oper = "保存";
		data+= "&account="+$("p.codeLabel").text();
	}
	
	$.ajax({
		url:url,
		data:data,
		type:"post",
		dataType:"json",
		success:function(data){
			if(data.isSuccess){
				//新增时发送账号
				if(event.data.isAdd == true){
					var text = $("#successAccount").text();
					$("#sendTypeAccountWrap").show();
					$("#successAccount").text(data.account);
					$("p.codeLabel").text(data.account);
					sendAccount();
				} else {
					$("#promptMsg").html(oper + "成功");
					$("#successPrompt").modal("show");
					window.setTimeout(function(){
						$("#successPrompt").modal("hide");
					}, 1000);
				}
				$("#tenant-add").modal("hide");
			} else {
				
				var str = '<label for="mobile" class="error" style="display: block;">'+data.message+'</label>';
				$("#mobile").addClass("error").after(str);
//				$("#promptMsg").html(data.message);
//				$("#successPrompt").modal("show");
//				window.setTimeout(function(){
//					$("#successPrompt").modal("hide");
//				}, 1000);
			}
			
			
			findData(page, null);
		},
		error:function(){
			$("#promptMsg").html(oper + "失败");
			$("#successPrompt").modal("show");
			window.setTimeout(function(){
				$("#successPrompt").modal("hide");
			}, 1000);
		}
	});
	return false;
}


function detail2edit(){
	$("#add-form").find("input").removeAttr("disabled");
	$("#add-form").find("select").removeAttr("disabled");
	$("#add-btn").css("display", "block");
	$("#edit-btn").css("display", "none");
	$("div.tenantInfo span").text("编辑租户信息");
	$("#btnsave").unbind("click").click({"isAdd":false},saveTenant);
}

	
// 删除
function deleteTenant(id, i, name) {
	$("#deleteComfirm").modal("show");
	$("#showName").text(name).attr("data-id", id).attr("tr-id", i);
}

function doDel(){
	$("#deleteComfirm").modal("hide");
	$.post(contextPath+"/tenant/delete.json?id="+$("#showName").text(name).attr("data-id"),function(result){
		if(result.isSuccess){
			
			$("#promptMsg").html("删除成功");
		} else {

			$("#promptMsg").html(result.message);
		}
		$("#successPrompt").modal("show");
		window.setTimeout(function(){
			$("#successPrompt").modal("hide");
		}, 1000);
		
		var page = $(".page.active").text();
		if($("tbody tr").length == 1){
			if(page != "1"){
				page = parseInt(page) - 1;
			}
		}
		if(page == ""){
			page = "1";
		}
		findData(page, null);
	});
}

/*
 * 打开发送方式窗口
 */
function sendAccount(account,email,mobile, id){
	//如果来源是创建账户后提示，则弹出框标题为发送帐号信息
	if(account!=null&&email!=null&&mobile!=null){
		$("#sendType .title").text("发送方式");
	}else{
		$("#sendType .title").text("发送账户信息");
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
		$("#checkCode input[name='account']").val($("p.codeLabel").text());
	}
	if(id!=null){
		var myDate = new Date();
		var curSeconds = myDate.getHours() * 3600 + myDate.getMinutes() * 60 + myDate.getSeconds();
		localStorage.setItem(id, curSeconds);
	}
	if($("#checkCode input[name='account']").val()!=null&&$("#checkCode input[name='account']").val()!=''){
		$("#sendType").modal("show");
		if($("#tenant-add").attr("data-type") == "old"){
			$("#sendTypeAccountWrap").hide();
		}else if($("#tenant-add").attr("data-type") == "new"){
			$("#sendTypeAccountWrap").show();
		}
		$("p.codeLabel").text($("#checkCode input[name='account']").val());
		
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
}



////通过邮箱发送账户
//function sendMail(){
//	$.post(contextPath+"/tenant/sendMail.json?email="+$("#email").val()+"&account="+$("p.codeLabel").text(),function(result){
//		if(result.isSuccess){
//			$("#promptMsg").html("账号发送成功");
//			$("#sendType").modal("hide");
//		} else {
//			$("#promptMsg").html("账号发送失败！请重发");
//		}
//		$("#successPrompt").modal("show");
//		window.setTimeout(function(){
//			$("#successPrompt").modal("hide");
//		}, 1000);
//		$("#deleteComfirm").modal("hide");
//	});
//}

/*
 * 发送方式点击，打开验证码窗口
 */
function sendAccountWay(type){
	if(type==1){
		var url = contextPath + "/t_user/sendAccountByMail";
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
		$("#valicodeImg").attr("src",contextPath+"/t_user/sendAccountByMailValicode?"+Math.random());
		$("#checkCode .errorTips").html("");
		$("#valicodeText").val("");
		$("#valicodeButton").unbind("click");
		$("#valicodeButton").click(function(){
			var url = contextPath + "/t_user/sendAccountByMobile";
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
					$("#sendType").modal("hide");
					$("#checkCode").modal("hide");
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
					$("#valicodeImg").attr("src",contextPath+"/t_user/sendAccountByMailValicode?"+Math.random());
					$("#valicodeText").val("");
				}
			});
		});
	}
}
/**
 * 重置验证码
 */
function resetValicode(){
	$("#valicodeImg").attr("src",contextPath+"/t_user/sendAccountByMailValicode?"+Math.random());
	$("#checkCode .errorTips").html("");
	$("#valicodeText").val("");
}

//查看编辑时，初始化地址信息。
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
	findData("1", null,text);
}
