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
	if(preferentialId != ""){
		if( $("#starttime").val() !=""){
			$("#starttime").val(timestampformat(parseInt($("#starttime").val())));
		}
		
		if( $("#endtime").val() !="" ){
			$("#endtime").val(timestampformat(parseInt($("#endtime").val())));
		}
		
		$("#color-input").css("background-color",$("#color").val());
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
					ul.append("<li>"+storeName+"</li>")
					if(i < selectShops.length -1){
						selectedId +=",";
					}
				});
				$("#selectBranchs").val(selectedId);
				var top = ileft = iwidth ="";
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
});

function cancel(){
	$("#discount-form").reset();
}

var voucher ={};
function save(){
	voucher.preferentialActivity={};
	voucher.preferentialActivity.id=$("#id").val();
	voucher.preferentialActivity.name=$("#name").val();
	voucher.preferentialActivity.color=$("#color").val() == "" ? "#9cc05b" : $("#color").val();
	voucher.preferentialActivity.applyAll=$("input[name=applyAll]:checked").val()=="1"?true:false;
	voucher.amount=$("#amount").val();
	if(!voucher.preferentialActivity.applyAll){
		voucher.branchs=[];
		$.each($("table.store-select-content").find("input:checked[type='checkbox']"),function(i,obj){
			var branch = {};
			branch.branch=$(this).val();
			branch.branch_name=$(this).next().html();
			voucher.branchs.push(branch);
		});
	}
	voucher.preferentialActivity.starttime=$("#starttime").val();
	voucher.preferentialActivity.endtime=$("#endtime").val();
	voucher.preferentialActivity.activityIntroduction=$("#activityIntroduction").val();
	voucher.preferentialActivity.useNotice=$("#useNotice").val();
	var url = global_Path+ "/preferential/addVoucher.json";
	if(isModify){
		url = global_Path+ "/preferential/saveVoucher.json";
	}
	$.ajax({
		url : url,
		type : "POST",
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(voucher),
		success : function(data) {
			if(data.isSuccess){
				$("#promptMsg").html("保存成功");
				$("#successPrompt").modal("show");
				window.setTimeout(function(){
					$("#successPrompt").modal("hide");
					window.location.href=global_Path+ "/preferential";
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


function loadBranch(){
	createBranch();
	$("#store-select-dialog").modal("show");
}

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
				html+="</tr><tr>"
			}
		});
		html=html+"</tr>"
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
			uploadStoreCount();
		}
	});
	//绑定确定按钮。当点击确定的时候，保存选择的门店。
	$("#store-select-confirm").click(function(){
		var selectedStore=$("table.store-select-content").find("input[type='checkbox']:checked");
		var ids=new Array();
		selectedStore.each(function(){
			ids.push( $(this).val() );
		});
		$("#selectBranchs").val( ids.join(",") );
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

