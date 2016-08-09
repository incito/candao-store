$(document).ready(function(){
	$("img.img-close").hover(function(){
	 	$(this).attr("src", global_Path+"/images/close-active.png");	 
	},function(){
		$(this).attr("src", global_Path+"/images/close-sm.png");
	});
});
/**
 * 切换查询类型的时候，为searchType赋值
 * @param type
 * @param o
 */
function changeDataType(type, o){
	searchType = type;
	gethiddenId(type, o);
}
function showLoading(){
	$("#prompt-dialog").modal("show");
}
function hideLoading(){
	$("#prompt-dialog").modal("hide");
}

/**
 * 初始化datatable
 */
function initDatatableConfig(){
	oTable = $("#waiter-assess-tb").dataTable( {
		"bAutoWidth":false,
    	"bFilter":false,
    	"bInfo":false,
    	"bPaginate": false,
    	"bSort": true,
    	"aaSorting": [[0,'asc']],
    	"aoColumnDefs": [
    	   { "bSortable": false, "aTargets": [ 1 ] }],
    	"oLanguage": {
    		"sEmptyTable": "无数据"
		},
    	"bRetrieve": false,
    	"bDestroy": true
	});
	$(".row-fluid").addClass("hide");
}

/*******************会员储值统计表 START********************************/
/**
 * 获取数据
 */
function getMemberValueReportData(){
	if(oTable !=null){
		oTable.fnClearTable(false);
	}
	$.get(global_Path+"/memberReport/total.json", {
		beginTime: $("#beginTime").val(),
		endTime: $("#endTime").val(),
		shiftid: $("#shiftid").val(),
//		type: $("#payType").val(),
		cardno: $("#cardno").val()
	}, function(result){
		console.log(result);
		if(result.flag == 1){
			var data = result.data;
			var htm = '';
			if(data != null && data.length>0){
				$.each(data, function(i, item){
					htm += '<tr ondblclick="showMemberValueReportSecPage()">'
						+ '<td>'+item.timeslot+'</td>'
						+ '<td>'+item.stored+'</td>'
						+ '<td>'+item.deposit+'</td>'
						+ '<td>'+item.total+'</td>'
						+ '<td>'+item.present+'</td>'
						+ '<td>'+item.numbers+'</td></tr>';
				});
				$("#waiter-assess-tb tbody").html(htm);
				initDatatableConfig();
			}else{
				htm += '<tr><td colspan="6">无数据</td></tr>';
				$("#waiter-assess-tb tbody").html(htm);
			}
		}else{
			alert(result.desc);
		}
	},'json');
}

/**
 * 列表中的二次弹窗
 */
function showMemberValueReportSecPage(){
	$("#MemberValue2-assess-dialog").modal("show");
	getMemberValueDetails();
}
/**
 * 列表中的二次弹窗 数据加载
 */
function getMemberValueDetails(){
	$.get(global_Path+"/memberReport/total.json", {
		beginTime: $("#beginTime").val(),
		endTime: $("#endTime").val(),
		shiftid: $("#shiftid").val(),
//		type: $("#payType").val(),
		cardno: $("#cardno").val(),
		day: 1
	}, function(result){
		if(result.flag == 1){
			var data = result.data;
			var htm = '';
			if(data != null && data.length>0){
				$.each(data, function(i, item){
					htm += '<tr ondblclick="showMemberValueReport3Page()">'
						+ '<td>'+item.day+'</td>'
						+ '<td>'+item.stored+'</td>'
						+ '<td>'+item.deposit+'</td>'
						+ '<td>'+item.total+'</td>'
						+ '<td>'+item.present+'</td>'
						+ '<td>'+item.numbers+'</td></tr>';
				});
			}else{
				htm = '<tr><td colspan="6">无数据</td></tr>';
			}
			$("#waiterassess-details-tb2 tbody").html(htm);
		}else{
			alert(result.desc);
		}
	},'json');
}

/**
 * 列表中的三次弹窗
 */
function showMemberValueReport3Page(){
	$("#MemberValue3-assess-dialog").modal("show");
	getMemberValue3Details();
}

/**
 * 列表中三次弹窗 按照时间段查询 弹窗 数据加载
 */
function getMemberValue3Details(){
	$.get(global_Path+"/memberReport/queryMemberDealInfosToTime.json", {
		beginTime: $("#beginTime").val(),
		endTime: $("#endTime").val(),
		shiftid: $("#shiftid").val(),
//		type: $("#payType").val()
		cardno: $("#cardno").val()
	}, function(result){
		if(result.flag == 1){
			var data = result.data;
			var htm = '';
			if(data != null && data.length>0){
				$.each(data, function(i, item){
					htm += '<tr>'
						+ '<td>'+item.dealTime+'</td>'
						+ '<td>'+item.cardno+'</td>'
						+ '<td>'+item.mobile+'</td>'
						+ '<td>'+item.name+'</td>'
						+ '<td>'+item.level+'</td>'
						+ '<td>'+item.dealType+'</td>'
						+ '<td>'+item.amount+'</td>'
						+ '<td>'+item.value+'</td>'
						+ '<td>'+item.actual_value+'</td>'
						+ '<td>'+item.deal_user+'</td></tr>';
				});
				$("#MemberValue3-assess-tb3 tbody").html(htm);
				initDatatableConfig();
			}else{
				htm += '<tr><td colspan="9">无数据</td></tr>';
				$("#MemberValue3-assess-tb3 tbody").html(htm);
			}
		}else{
			alert(result.desc);
		}
	},'json');
}

/*******************会员储值统计表 END********************************/



