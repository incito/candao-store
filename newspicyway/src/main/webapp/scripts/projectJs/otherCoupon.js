$(document).ready(function(){
	/*更多优惠*/
	$("ul.nav-preferential.other-preferential li").click(function(){
		$("ul.nav-preferential li").removeClass("active");
		$(this).addClass("active");
		var id= $(this).attr("id").substr($(this).attr("id").length-1,1);
		$("div[id*='other-preferential-content']").addClass("hidden");
		$("#other-preferential-content"+id).removeClass("hidden");
		var left= ($(this).prevAll("li").length*50+25)+'%';
		$(this).find("em").css("left",left);
		if(id=='1'){
			getReason();
		} else if(id == '2'){
			loadInnerFree();
		}
		
	});
	$("ul.nav-preferential.other-preferential li").mouseover(function(){
		var left= ($(this).prevAll("li").length*50+25)+'%';
		$(this).find("em").css("left",left);
	});
	
	getReason();
	
	// 自动补全
	
	$("#innerfree_searcher").autoComplete({
		delay: 300,
		showCount: 6,
		currentPage:"current",
		pageSize:"pagesize",
		nameKey:"company_name",
		url : global_Path+"/preferential/pageInnerFree.json",
		onTextChange : function(e, id){
			loadInnerFree(1,id);
		}
	});

	//隐藏头部的一个 用来搜索优惠的文本框
	$(parent.document.all("allSearch")).css("visibility","hidden");
	
	//从validate-plus.js复制过来单独定义
	$.validator.setDefaults({
		errorClass : "popover-errorTips",
		errorPlacement: function(error, element) {  
			element.parent().append(error);
			//element.parents().css("position","relative");  //注释这里防止弹出层错位
//			var classToggle = "top";
//			var top = element.position().top - 50 + "px";
//			var left = element.position().left + 50 + "px";
//			if(element.hasClass("bottom")){
//				top = element.position().top + 40 + "px";
//				left = element.position().left + 20 + "px";
//				classToggle = "bottom";
//			}
//			var div = $("<div>").addClass("popover fade "+classToggle+" in").css({
//					display : "block",
//					top : top,
//					left : left,
//				}).append('<div class="arrow" style="left: 50%;"></div>').append(error);
//			
//		    element.after(div);
		    
		},
		
	}) ;
	
	$("#addReasonForm").validate({
		submitHandler : function(form) {
			addReason();
		},

		rules : {
			newReason : {
				required : true,
			}
			
		},
		messages : {
			newReason : {
				required : "原因不能为空",
			}
			
		}
	});
	
	//绑定增加 内部优免的验证功能
	$("#add-form").validate({
		submitHandler : function(form) {
			saveInnerfree();
		},

		rules : {
			company_name : {
				required : true,
				maxlength : 15,
			},
			discount : {
				required : true,
				number:true,
				isDiscount:true
			},
			can_credit : {
				required : true
			}
			
		},
		messages : {
			company_name : {
				required : "请输入单位名称",
				maxlength : "长度不能超过15",
			},
			discount : {
				required : "请输入折扣额",
				number: "请输入合法的数字",
				isDiscount:"请输入有效的折扣额"
			},
			can_credit : {
				required : "请选择是否可以挂账"
			}
		}
	});
	
	//根据参数判断当前查看的是 手工优免还是 内部优免。
	var Request = new Object();
	Request = GetRequest();
	var subType="";
	subType=Request['subType'];
	//如果是 0602， 则是内部优免
	if( undefined != subType && subType=='0602'){
		$("ul.nav-preferential.other-preferential #other-preferential-type2").click();
	}
});

function GetRequest() {
	   var url = location.search; //获取url中"?"符后的字串
	   var theRequest = new Object();
	   if (url.indexOf("?") != -1) {
	      var str = url.substr(1);
	      strs = str.split("&");
	      for(var i = 0; i < strs.length; i ++) {
	         theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);
	      }
	   }
	   return theRequest;
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
function showAddReason(){
	$("#newReason").removeClass("popover-errorTips");
	$("label.popover-errorTips").hide();
	$("#newReason").val("").removeAttr("data-id");
	$("#reason-add-dialog").modal("show");
	//设置标题为 添加手工优免
	$("#reason-add-dialog").find(".modal-title span").html("添加");
}

function cancelAdd(){
	$("#reason-add-dialog").modal("hide");
}

function addReason(){
	var handFree = {};
	handFree.preferential = $("#handfree_preferential").val();
	handFree.freeReason = $("#newReason").val();
	if(typeof $("#newReason").attr("data-id") != "undefined"){
		handFree.id = $("#newReason").attr("data-id");
		$.ajax({
			url:global_Path+ "/preferential/updateHandFree.json",
			type : "POST",
			dataType : "json",
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify(handFree),
			success:function(result){
				if(result.isSuccess){
					$("#reason-add-dialog").modal("hide");
					$("#" + handFree.id).html(handFree.freeReason +'<a class="icon-remove hidden" onclick="removeReason(\''
							+ handFree.id + '\', this)"></a>');
					$("#promptMsg").html("修改成功");
					$("#successPrompt").modal("show");
					window.setTimeout(function(){
						$("#successPrompt").modal("hide");
					}, 1000);
				}else{
					var str = '<label for="newReason" class="popover-errorTips" style="display: block;">'+result.message+'</label>';
					$("#newReason").addClass("popover-errorTips").after(str);
				}
				//alert(result.message);
			},
		});
	}else{
		$.ajax({
			url:global_Path+ "/preferential/addHandFree.json",
			type : "POST",
			dataType : "json",
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify(handFree),
			success:function(result){
				if(result.isSuccess){
					$("#reason-add-dialog").modal("hide");
							var btn = '<div class="btn btn-default iconwrap" id="'
									+ result.id
									+ '" onmouseover="delDisplay(this)" onmouseout="delHidden(this)" ondblclick="ondbl(this)">'
									+ handFree.freeReason
									+ '<a class="icon-remove hidden" onclick="removeReason(\''+result.id+'\', this)"></a></div>';
							$("#addReason").prev().after(btn);
					$("#promptMsg").html("添加成功");
					$("#successPrompt").modal("show");
					window.setTimeout(function(){
						$("#successPrompt").modal("hide");
					}, 1000);
				}else{
					var str = '<label for="newReason" class="popover-errorTips" style="display: block;">'+result.message+'</label>';
					$("#newReason").addClass("popover-errorTips").after(str);
				}
			},
		});
	}
}

function getReason(){
	$("#reasonContent").find("div").remove();
	$.post(global_Path+ "/preferential/listHandFree.json",{"preferential" : $("#handfree_preferential").val()},function(result){
		
		if(result.length > 0){
			$("#handfree_preferential").val(result[0].preferential);
		}
		$ .each( result, function(i, v) {
			var btn = '<div class="btn btn-default iconwrap" id="' + v.id+ '"';
			if (!isDetail) {
				btn += 'onmouseover="delDisplay(this)" onmouseout="delHidden(this)" ondblclick="ondbl(this)"';
			}
			btn += '>' + v.freeReason
					+ '<a class="icon-remove hidden" onclick="removeReason(\''
					+ v.id + '\',this)"></a></div>';
			$("#addReason").prev().after(btn);
		});
		
		refreshBranchRole();
	},"json");
	
	if(isDetail){
		$("#addReason").css("display", "none");
	}
//	if(isbranch=="Y"){
//		
//		$("#addReason").hide();
//		$(".iconwrap").attr("disabled","disabled");
//	}
}

function removeReason(id, e){
	$("#deleteComfirm").modal("show");
	$("#showName").text($(e).parent().text()).attr("data-id", id);
	
}
function doDel(){
	var id = $("#showName").attr("data-id");
	$.post(global_Path+ "/preferential/delHandFree.json",{"id":id},function(result){
		$("#deleteComfirm").modal("hide");
		if(result.isSuccess){
			$("#"+id).remove();
			$("#promptMsg").html("删除成功");
			$("#successPrompt").modal("show");
			window.setTimeout(function(){
				$("#successPrompt").modal("hide");
			}, 1000);
		}else{
			$("#promptMsg").html("删除失败");
			$("#successPrompt").modal("show");
			window.setTimeout(function(){
				$("#successPrompt").modal("hide");
			}, 1000);
		}
	},"json");
}

function delDisplay(e){	
	$(e).find("a.icon-remove").removeClass("hidden");
}
function delHidden(e){
	$(e).find("a.icon-remove").addClass("hidden");
}
function ondbl(e){
	$("#newReason").removeClass("popover-errorTips");
	$("label.popover-errorTips").hide();
	$("#newReason").val($(e).text()).attr("data-id", $(e).attr("id"));
	$("#reason-add-dialog").modal("show");
	//设置标题为 编辑手工优免
	$("#reason-add-dialog").find(".modal-title span").html("编辑");
}



//显示添加内部优惠（合作单位） 弹出表单层
function showInnerfreeReason(){
	$("#innerfree-add-dialog form")[0].reset(); //防止编辑完了，接着添加的时候，会遗留上次的内容
	// id preferential 都要清空
	$("#innerfree-add-dialog").find("input").val("");
	
	$("#innerfree-add-dialog").modal("show").on("hide.bs.modal", function(){
		$("input.popover-errorTips").removeClass("popover-errorTips");
		$("label.popover-errorTips").remove();
	});
	//设置标题为 添加内部优免
	$("#innerfree-add-dialog").find(".modal-title span").html("添加");
}

function picked(){
	if($(this).val() != ""){
		$(this).parent().find("div.popover").remove();
	}
}

/**
 * 保存内部优惠（合作单位优惠）
 */
function saveInnerfree(){
	var innerfree={};
	innerfree.id=$("#innerfree-add-dialog #id").val();
	innerfree.preferential=$("#innerfree-add-dialog #preferential").val();
	innerfree.company_name=$("#company_name").val();
	innerfree.discount=$("#discount").val();
	innerfree.can_credit= $("#can_credit").val()=="1"?true:false;
	innerfree.starttime=$("#starttime").val();
	innerfree.endtime=$("#endtime").val();
	
	$.ajax({
		url:global_Path+ "/preferential/saveInnerFree.json",
		type : "POST",
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(innerfree),
		success:function(result){
			if(result.isSuccess){
				$("#promptMsg").html("保存成功");
				$("#successPrompt").modal("show");
				window.setTimeout(function(){
					$("#successPrompt").modal("hide");
				}, 1000);
				$("#innerfree-add-dialog").modal("hide");
				loadInnerFree();
			}else{
				var str = '<label for="company_name" class="popover-errorTips" style="display: block;">'+result.message+'</label>';
				$("#company_name").addClass("popover-errorTips").after(str);
			}
		},
	});
	
}

function loadInnerFree(currentPage,id){
	var dataJson={};
	dataJson.current=1;
	dataJson.pagesize=10;
	if(undefined!=currentPage && currentPage!=""){
		dataJson.current=currentPage
	}
	
	if(undefined!=id && id!=""){
		dataJson.id=id;
	}
	
	$.getJSON( global_Path+ "/preferential/pageInnerFree.json",dataJson, function( data ) {
		 var html="";
		 if(!data.rows){
			 $(".pagingWrap").empty();
			 $("#innerfree-table tbody").html(html);
			 return;
		 }
		 $.each(data.rows,function(i,ele){
			 var odd;
			 if(i % 2 == 0){
				 odd = "odd";
			 }else{
				 odd = "";
			 }
			 if(isbranch=="N"){
				 html+=" <tr class='"+odd+"'>"
				 +"  <td>"+(i+1)+"</td> "
				 +"  	<td>"+ele.company_name+"</td> "
				 +"  	<td>"+ele.discount+"折</td> "
				 +"  	<td>"+( ele.can_credit?"是":"否")+"</td> "
				 +"  	<td>"+ (new Date( parseInt(ele.starttime) )).format("YYYY-MM-dd")+" 至 "+(new Date( parseInt(ele.endtime) )).format("YYYY-MM-dd")+"</td> "
				 +"  	<td class='td-last'> "
				 +"  		<div class='operate'> "
				 +"  			<a class='editBtn' href='javascript:void(0)' onclick=\"editInnerFree('"+ele.id+"')\" >修改</a> "
				 +"  			<a class='deleteBtn' href='javascript:void(0)' onclick=\"deleteInnerFree('"+ele.id+"', '"+ele.company_name+"')\" >删除</a> "
				 +"  		</div> "
				 +"  	</td> "
				 +"  </tr>";
			 }else if(isbranch=="Y"){
				 html+=" <tr class='"+odd+"'>"
				 +"  <td>"+(i+1)+"</td> "
				 +"  	<td>"+ele.company_name+"</td> "
				 +"  	<td>"+ele.discount+"折</td> "
				 +"  	<td>"+( ele.can_credit?"是":"否")+"</td> "
				 +"  	<td>"+ (new Date( parseInt(ele.starttime) )).format("YYYY-MM-dd")+" 至 "+(new Date( parseInt(ele.endtime) )).format("YYYY-MM-dd")+"</td> "
				 +"  	<td class='td-last'> "
				 +"  		<div class='operate'> "
				 +"  		</div> "
				 +"  	</td> "
				 +"  </tr>";
			 }
			
		 });
		
		 if(data.total > 10){
			 $(".pagingWrap").html('<ul class="paging clearfix">');
     		 $(".paging").twbsPagination({
					totalPages: data.pageCount,
	     			visiblePages: 5,
	     			prev : '＜',
	     			next : '＞',
			        last: '... ',
			        startPage:dataJson.current,
			        onPageClick: function (event, page){
			        	loadInnerFree( page );
			        }
			 });
		 }else{
			 $(".pagingWrap").empty();
		 }
		 $("#innerfree-table tbody").html(html);
		 
//		 refreshBranchRole();
	});
}

function editInnerFree(id){
	$("#innerfree-add-dialog").modal("show");
	//设置标题为 添加内部优免
	$("#innerfree-add-dialog").find(".modal-title span").html("编辑");
	$("#innerfree-add-dialog form")[0].reset();
	$.getJSON( global_Path+ "/preferential/findInnerFree/"+id+".json",function(data){
		$("#innerfree-add-dialog #id").val(data.id);
		$("#innerfree-add-dialog #preferential").val(data.preferential);
		$("#innerfree-add-dialog #company_name").val(data.company_name);
		$("#innerfree-add-dialog #discount").val(data.discount);
		$("#innerfree-add-dialog #can_credit").val(data.can_credit?"1":"0");
		if( undefined !=data.starttime){
			$("#innerfree-add-dialog #starttime").val((new Date( parseInt(data.starttime) )).format("YYYY-MM-dd"));
		}
		if( undefined != data.endtime ){
			$("#innerfree-add-dialog #endtime").val((new Date( parseInt(data.endtime) )).format("YYYY-MM-dd"));
		}
		
	});
	
}

function deleteInnerFree(id, name){
	$("#deleteInnerComfirm").modal("show");
	$("#showInnerName").text(name).attr("data-id", id);
	
	
}
function doDelInner() {
	var id = $("#showInnerName").attr("data-id");
	$.getJSON( global_Path+ "/preferential/deleteInnerFree/"+id+".json",function(data){
		$("#deleteInnerComfirm").modal("hide");
		if(data.isSuccess){
			loadInnerFree();
			$("#promptMsg").html("删除成功");
			$("#successPrompt").modal("show");
			window.setTimeout(function(){
				$("#successPrompt").modal("hide");
			}, 1000);
		}else{
			$("#promptMsg").html("删除失败");
			$("#successPrompt").modal("show");
			window.setTimeout(function(){
				$("#successPrompt").modal("hide");
			}, 1000);
		}
	});
}

