$(document).ready(function(){

	/*添加餐台分区弹出框*/
	$("#counter-type-add").click(function(){
		$("#counter-add-dialog").modal("show");
		$("#counter-add-dialog").find(".counter-dialog-header >span").text("添加分区");

	});

	$(".nav-counter li").click(function(){
		$(".nav-counter li").removeClass("active");
		$(this).addClass("active");
	});

	//点击餐台分区中确认按钮
	$("#counter-type-save").click(function(){
		var count = $(".nav-counter").find("li").length;	
		var text = $("input[name='counter-type']").val();
		var temp = '<li ondblclick="editCounterType(this)"  onmouseover="delDisplay(this)"  onmouseout="delHidden(this)" >';
		temp = temp+text+'<i class="icon-remove hidden"  onclick="delCounterType(this)"></i></li>';
		$(".nav-counter").append(temp);
		$("#counter-add-dialog").modal("hide");
						
	});
	/*餐台分区向左向右按钮*/
	var flag_prev =0;
	$(".nav-counter-prev").click(function(){
		var count = $(".nav-counter").find("li").length;
		if(flag_prev<count-10){
				$(".nav-counter").find("li").eq(flag_prev).css("margin-left","-10%");
				flag_prev++;
		}

	});
	$(".nav-counter-next").click(function(){
		if(flag_prev>=1){	
			$(".nav-counter").find("li").eq(flag_prev-1).css("margin-left","0px");
			flag_prev--;
		}
	});
	/*删除分区成功*/
	$("#counter-typeDel-dialog .btn-save").click(function(){
		$(".counterDel-success").removeClass("hidden");
		setTimeout(function(){
				$("#counter-typeDel-dialog").modal("hide");
				$(".counterDel-success").addClass("hidden");
			}, 3000);

	});

	/*餐台添加弹出框显示*/
	$("#counter-table-add").click(function(){
		$("#counter-table-dialog").modal("show");
		$("#counter-table-dialog").find(".counter-dialog-header >span").text("编辑餐台");

		$(".disabled-input input").attr("readonly","true");

	});
	/*checkbox选中与否*/
	$("input[name='enableCheck']").click(function(){
		var obj =$(this).parent().parent();
		if($(this).is(":checked"))
		{
			obj.removeClass("disabled-input");
			obj.find("input").removeAttr("readonly");
		}else{
			obj.addClass("disabled-input");
			obj.find("input").attr("readonly","readonly");
			obj.find("input").val(" ");

		}
	});
	//餐台添加中确认按钮
	$("#counter-table-save").click(function(){
		var name = $("#counter-name").val();
		var code = $("#counter-sits").val();
		var temp = '<div class="counter-detail-box" onmouseover="delDisplay(this)" ondblClick="editCounterTable(this)" onmouseout="delHidden(this)"><p>'+name+'</p><p>('+code+'）</p><i class="icon-remove hidden" onclick="delCounterTable(this)"></i></div>';
		$("#counter-table-add").before(temp);
		$("#counter-table-dialog").modal("hide");
	});
	/**/
	/*删除餐台成功*/
	$("#counter-tableDel-dialog .btn-save").click(function(){
		$(".counterDel-success").removeClass("hidden");
		setTimeout(function(){
			$("#counter-tableDel-dialog").modal("hide");
			$(".counterDel-success").addClass("hidden");
		}, 3000);

	});
});

function delDisplay(e){	
	$(e).find("i.icon-remove").removeClass("hidden");

}
function delHidden(e){
	$(e).find("i.icon-remove").addClass("hidden");

}
function delCounterType(e){
	$("#counter-typeDel-dialog").modal("show");
}
function editCounterType(e){
	$("#counter-add-dialog").modal("show");
	$("#counter-add-dialog").find(".counter-dialog-header >span").text("编辑分区");
}
function delCounterTable(e){
	$("#counter-tableDel-dialog").modal("show");
}
function editCounterTable(e){
	$("#counter-table-dialog").modal("show");	
	$("#counter-table-dialog").find(".counter-dialog-header >span").text("编辑餐台");

}
 
