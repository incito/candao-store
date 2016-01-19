// 新增或编辑标识
var win_flag = 'add';// edit
$(document).ready(
		function() {
			$(".btn-second").click(function() {
				$("#table-add").modal("show");
				$("#add-btn").show();
				$("#btnsave").removeAttr("disabled" );//?
				clearAddFrom();
				win_flag = 'add';
			});

			$(".btn-first").click(function() {
				$("#table-add").modal("show");
				$("#add-btn").show();
				$("#btnsave").removeAttr("disabled" );
				clearAddFrom();
				win_flag = 'add';
			});

			$("#add-form").validate(
					{
						submitHandler : function(form) {
							var vcheck = true;
							if ($("#tableid").val() == "") {
								$("#tableNo_tip").html("餐台编号不能为空");
								vcheck = false;
							} 
								

							if (vcheck) {
								save_table();
							}
						}
					});
			 
//			$("#personNum  option[value='8'] ").attr("selected",true);
			getTableTag();
			getPrinterTag();
//			$("#personNum option:selected").attr("selected","");
			
		});
//-------------------------------------------------------------------------------------

function getTableTag(){
	
	
	$.ajax({
		type : "post",
		async : false,
		url:global_Path + '/table/getTableTag.json',
		dataType : "json",
		success : function(result) {
			
			
			$.each(result, function(i,val){  
				if(val.areaname==null){
					val.areaname="";
				}	
				$(".areaid").append("<option value ="+val.areaid+"  class='form-control myInfo-select-addrW'>"+val.areaname+"</option>");
				
				});
					

		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}

	});
}
function getPrinterTag(){
	$.ajax({
		type : "post",
		async : false,
		url:global_Path + '/table/getPrinterTag.json',
		dataType : "json",
		success : function(result) {

			$.each(result, function(i,val){  
				
				$(".printerid").append("<option value ="+val.printerid+"  class='form-control myInfo-select-addrW'>"+val.printername+"</option>");
			});
		
					

		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}

	});
}	

function init_object(){
// 	tableid,tableNo,tabletype,tableName,personNum,areaid,minprice,fixprice
	$("#tabletype").val("");
	$("#tableName").val("");
	$("#personNum").val("");
	$("#areaid").val("");
	$("#printerid").val("");
	$("#fixprice").val("");
	$("#tableid").val("");
    $("#tableNo").val("");
	$("#minprice").val("");
	$("#tableid").val("");
//	$("#personNum option:selected").attr("selected",false);
}
function hideDialog() {
	$("#table-add").modal("hide");
}
// 编辑
function doEdit(id, flag) {
	init_object();
	win_flag = 'edit';
	clearAddFrom();//清空弹出框，先。
	if (flag == "detail") {// 表示查看详情
		$("#add-btn").hide();
		$("#btnsave").attr("disabled",true);
	} else {
		$("#add-btn").show();
		$("#btnsave").removeAttr("disabled");
	}
	$("#table-add").modal("show");
	$.ajax({
		type : "post",
		async : false,
		url : global_Path+"/table/findById/"+id+".json",
		dataType : "json",
		success : function(result) {
//			alert(result.tableid);
			$("#tableid").val(result.tableid);					
			$("#tableNo").val(result.tableNo);		
			$("#tabletype  option[value="+result.tabletype+"] ").attr("selected",true);
			$("#personNum  option[value="+result.personNum+"] ").attr("selected",true);
			$("#areaid  option[value="+result.areaid+"] ").attr("selected",true);
//			$("#areaid").val("");
//			$("#areaid option:selected").attr("selected",false);
			$("#printerid  option[value="+result.custPrinter+"] ").attr("selected",true);
			$("#tableName").val(result.tableName);
			$("#minprice").val(result.minprice);
			$("#fixprice").val(result.fixprice);
//			$("#tabletype  option[value='8'] ").attr("selected",true);
			
		},
		

		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}

	});
}

function data2Form(data) {
	for ( var key in data) {
		// console.log("key:"+key+"-value:"+data[key]);
		$("#add-form input[name='" + key + "']").val(data[key]);
	}
	// 编辑餐台时，初始化地址信息。
	if (win_flag == 'edit') {
		initAddressInfo();
	}
}

// 保存餐台信息
function save_table() {
	var tableInfo = {};
	
	$("#add-form :text,#id,#add-form :hidden").each(function(index) {
		// console.log("each:"+$(this).attr("name"));
//		alert($(this).attr("id"));
		tableInfo["" + $(this).attr("name") + ""] = $(this).val();
	});
//	alert(2);
	$("#tabletype option:selected,#personNum option:selected,#areaid option:selected,#printerid option:selected").each(function(index) {
		
//		alert($(this).parent().attr("id"));
		// console.log("each:"+$(this).attr("name"));
		tableInfo["" + $(this).parent().attr("name") + ""] = $(this).val();
	});
	$.ajax({
		type:"post",
		async:false,
		url : global_Path+'/table/save',
		contentType:'application/json;charset=UTF-8',
	    data:JSON.stringify(tableInfo),
		dataType : "json",
		
		success : function(result) {	
			
			$("#query").submit();
//			window.location.reload();
			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}

	});
}

function del(id) {
	if (confirm("确定删除该餐台？")) {
		$.ajax({
			type : "post",
			async : false,
			url : global_Path+"/table/delete/"+id+".json",
			dataType : "json",
			success : function(result) {	
				$("#query").submit();
//				window.location.reload();
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert(errorThrown);
			}

		});
	}

}

function jumpPage(cur) {
	$("#current").val(cur);
	$("form:first").submit();
}

function doQuery() {
	$("#current").val("1");
}

function clearAddFrom(){
	$("#add-form :text,#add-form :hidden:not('option')").val("");
	$("#city").empty();
	$("#region").empty();
}
