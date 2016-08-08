var DishTasteArray = [];//已选择的菜品口味
var TotalDishTasteArray = [];//总的菜品口味
var DishLabelArray = [];//已选择的菜品标签
var TotalDishLabelArray = [];//总的菜品标签
var maxCount = 100;  // 菜品介绍可以输入的最大值
var imagePath='';
$(document).ready(function(){
	
//	$("#dish-select-dialog").modal("show");
/*	$("#dish-select-dialog").load(global_Path+"/dish/loadDishSelect");
*/	/*菜品分类向左向右按钮*/
	var flag_prev =0;
	$(".nav-dishes-prev").click(function(){
		var count = $(".nav-dishes").find("li.nav-dishes-type").length;
		if(flag_prev<count-10){
				$(".nav-dishes").find("li.nav-dishes-type").eq(flag_prev).css("margin-left","-10%");
				flag_prev++;
		}

	});
	$(".nav-dishes-next").click(function(){
		if(flag_prev>=1){	
					
			$(".nav-dishes").find("li.nav-dishes-type").eq(flag_prev-1).css("margin-left","0px");
			flag_prev--;
		}
	});
	
	
	$("#dishes-type-cancle").click(function(){
		initDishType();
	});
	/*添加菜品分类弹出框*/
	$("#dishes-type-add").click(function(){
		$("#dishes-add-dialog").modal("show");
		$("#dishes-add-dialog").find("h4.modal-title").text("添加分类");

	});
	
	$("img.img-close").click(function(){
		$("div.modal-backdrop").css("z-index","1030");
	});
	//点击菜品弹出框中确定按钮增加菜品
	$("#dishes-type-save").click(function(){
		if($("#dish-type").val() == ""){
			$("#emptywarntip").show();
			return false;
		}
		$("#emptywarntip").hide();
		$.ajax({
			url:global_Path+"/dishtype/judgeName.json",
			type : "post",
			datatype : "json",
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify({
				"itemdesc" : $('#dish-type').val(),
				"id" : $('#dish-type-id').val(),
			}),
			success : function(data) {
				if(data.flag=="0"){
					$("#warntip").show();
				}
				if($('#dish-type-id').val()!=""){
					var id=$("#dish-type-id").val();
		    		$("#"+id).text($('#dish-type').val());
		    		initDishType();
				}else{
				if(data.flag=="1"){
//					alert(data.message);
					var text = $("#dish-type").val();
//					var temp = '<li class="nav-dishes-type">';
//					temp = temp +'<button class="btn btn-default dishes-type" ondrop="drop(event)" ondragover="allowDrop(event)"  onmouseover="delDisplay(this)" onmouseout="delHidden(this)" draggable="true" ondragstart="drag(event)" id="'+data.id+'">';
//					temp = temp+'<span>'+text+'</span><i class="icon-remove hidden"></i></button></li>';
					
					/* add 20150325*/
					var temp = '<li class="nav-dishes-type" onclick="oneclickDishType(this.id)" ondrop="drop(event)" ondragover="allowDrop(event)"  onmouseover="delDisplay(this)" onmouseout="delHidden(this)" draggable="true" ondragstart="drag(event)" id="'+data.id+'" >';
					temp = temp+text+'<i class="icon-remove hidden" onclick="delDishType(\''+data.id+'\',\''+text+'\',event)"></i></li>';

					$(".nav-dishes").append(temp);
					
					$("li.nav-dishes-type").click(function(){
						$(".nav-dishes li").removeClass("active");
						//$(this).parent().addClass("active");
						$(this).addClass("active");
						$(".dishes-right-tab").addClass("hidden");

					});
					initDishType();
					showAndHidden();
				}
			}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert(errorThrown);
			}
		});
						
	});
/*changed 20150327*/
	$("li.nav-dishes-type").click(function(){
		$(".nav-dishes li").removeClass("active");
		$(this).addClass("active");
		$(".dishes-right-tab").addClass("hidden");
	});
	/*菜品分类右键弹出框*/
	$("li.nav-dishes-type").on("mousedown",function(e){
		if(e.button == 2)
		{  
			var num =0;
			$(this).prevAll().each(function(){
				if($(this).css("margin-left") =='0px')
				 	num++;
			});
			var left = num*10+6+'%';		
			$(".dishes-right-tab").removeClass("hidden");
			$(".dishes-right-tab").css("left",left);
			var dishes_name = $(this).text();
			$("li#dishes-right-tab2 span").text("编辑"+"\""+dishes_name+"\"");
			$("li#dishes-right-tab3 span").text("删除"+"\""+dishes_name+"\"");
			$("#dish-type").val(dishes_name.trim());
			$("#dish-type-id").val($(this).attr('id'));
		}
	});
	/*右键添加菜品分类*/
	$("li#dishes-right-tab1").click(function(){
		$("#dish-type").val("");
		$("#dish-type-id").val("");
		$("#dishes-add-dialog").modal("show");
		$(".dishes-right-tab").addClass("hidden");

	});
	/*右键编辑菜品分类*/
	$("li#dishes-right-tab2").click(function(){
		$("#dishes-add-dialog").modal("show");
		$("#dishes-add-dialog").find("h4.modal-title").text("编辑分类");
		$(".dishes-right-tab").addClass("hidden");
	});
	$("li#dishes-right-tab3").click(function(e){
		delDishType($("#dish-type-id").val(),$("#dish-type").val(),e);
		$("#dish-type").val("");
		$("#dish-type-id").val("");
		$(".dishes-right-tab").addClass("hidden");
	});


	/*菜品分类下具体菜品添加*/
	$("#dishes-detailMain-Add").click(function(){
		trflag=1;
		$("#dishes-detailAdd-dialog").modal("show");
		$("#dishes-detailAdd-dialog").find("h4.modal-title").text("添加菜品");
		$.ajax({
			url : global_Path + "/dish/getInitData.json",
			type : "post",
			datatype : "json",
			contentType : "application/json; charset=utf-8",
			success : function(result) {
				$.each(result.listType,function(index,item){
					$("#dishTypeSelect").append('<span class="select-content-detail"><label>'+item.itemdesc+'</label><input type="checkbox" onclick="selectDishType()" id="'+item.id+'"></span><br/>');
				});
				Unitarray=result.UnitHistorylist;
				addUnitHistoryToDiv("unit"+trflag,Unitarray);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert(errorThrown);
			}

		});

	});
	$("#dishes-detailOther-Add").click(function(e){
		$(".dishes-other-right").toggleClass("hidden");
	});
		/*select */
	$("#dishTypeShow").click(function(){
		//toggleClass() 对设置或移除被选元素的一个或多个类进行切换。
		$("#dishTypeSelect").toggleClass("hidden");
	});
	$("#dishes-detail-save").click(function(){
		var name = $("#title").val();
		var code = $("#dishno").val();
		var unitList=getvaluesList('unit',1);
		var vippriceList=getvaluesList('vipprice',0);
		var priceList=getvaluesList('price',0);
		 var image;
		  if($("#main_img").val()=='../images/upload-img.png'){
		    	image=imagePath;
		    }else{
		    	image="";
		  }
		validDishUnit();
		  $.ajaxFileUpload({
			    fileElementId: ['main_img'],  
			    url: global_Path+'/dish/save',  
			    dataType: 'json',
			    contentType:'application/json;charset=UTF-8',
			    data: {
			    	 dishid:$("#dishid").val(),
			    	 dishno:$("#dishno").val(),
			    	 title:$("#title").val().replace("\"","“").replace("\"","”"),
			    	 dishtype:'0',
			    	 abbrdesc:DishLabelArray.join(','),
			    	 columnid:$("#selectDishTypeId").val(),
			    	 unit:unitList,
			    	 introduction:$("#introduction").val().replace("\"","“").replace("\"","”"), 
			    	 vipprice:vippriceList,
			    	 price:priceList,
			    	 image:image,
			    	 imagetitle:DishTasteArray.join(','),
			    	 headsort:$("#unitflag").val()
			    },
			    success: function (data, textStatus) {
			    	var arr = $("#selectDishTypeId").val().split(',');
			    	if(arr.indexOf($("li.active").attr('id'))!=-1){
			    	if($("#dishid").val()==""){
			    		var temp = '<div class="dishes-detail-box" onmouseover="delDisplay(this)" id="'+data.tdish.dishid+'" onmouseout="delHidden(this)">'+
			    		'<p class="dishes-detail-name">'+name+'</p>'+
			    		'<p class="dishes-detail-code">'+code+'</p>'+
			    		'<i class="icon-remove hidden" onclick="delDishesDetail(\''+data.tdish.dishid+'\',\''+data.tdish.title+'\',event)"></i></div>';
			    		$(".dishes-detail-add").before(temp);
			    	}else{
			    		var id=$("#dishid").val();
			    		$("#"+id+" .dishes-detail-name").text($("#title").val().replace("\"","“").replace("\"","”"));
			    		$("#"+id+" .dishes-detail-code").text($("#dishno").val());
			    	}
			    	}else{
			    		if($("#dishid").val()!=""){
			    			var id=$("#dishid").val();
			    			$("#"+id).remove();
			    		}
			    	}
			    	
			    	saveDishUnit(data.tdish.dishid);
			    },  
			    complete: function (XMLHttpRequest, textStatus) {  
			    } 
		    });
	});
	/*双击菜品名称弹出菜品编辑框*/
	$(".dishes-detail-box").dblclick(function(){
//		alert($(this).attr('id'));
		$("#dishes-detailAdd-dialog").modal("show");
		$("#dishes-detailAdd-dialog").find("h4.modal-title").text("编辑菜品");
		viewAndEdit($(this).attr('id'));
	});

	/*菜品口味,菜品标签中添加按钮*/	
	$(".tagAdd-btn button").click(function(){
		var id = $(this).parent().attr("id");
		var num =id.substr(id.length-1,1);
		var text =$("#tagName"+num).val();
		if(text == ""){
			$("#emptywarntip"+num).show();
			return false;
		}
		$("#emptywarntip"+num).hide();
		if (num == 1) {
			if(!jQuery.isEmptyObject(TotalDishTasteArray)){
			if (TotalDishTasteArray.length != 0) {
				if (TotalDishTasteArray.indexOf(text) == -1) {
					addTasteAndLabel(num, text);
					TotalDishTasteArray.push(text);
				} else {
					$("#warntip" + num).show();
				}
			} 
			}else {
				addTasteAndLabel(num, text);
				TotalDishTasteArray.push(text);
			}
		}
		if (num == 2) {
			if(!jQuery.isEmptyObject(TotalDishLabelArray)){
			if (TotalDishLabelArray.length != 0) {
				if (TotalDishLabelArray.indexOf(text) == -1) {
					addTasteAndLabel(num, text);
					TotalDishLabelArray.push(text);
				} else {
					$("#warntip" + num).show();
				}
			} 
		}else {
			addTasteAndLabel(num, text);
			TotalDishLabelArray.push(text);
		}
		}
	});
	/*菜品口味弹出框显示*/
	$("#dishes-taste-add").click(function(){
		$("#dishes-tasteAdd-dialog").modal("show");
		// 二次弹框遮罩效果0305
		$("#dishes-tasteAdd-dialog").css("z-index","1042");
		$("div.modal-backdrop").css("z-index","1041");
		
		$.ajax({
			url:global_Path+"/datadictionary/getDatasByType/SPECIAL.json",
			type : "post",
			datatype : "json",
			contentType : "application/json; charset=utf-8",
//			data : JSON.stringify({
//				"itemdesc" : $('#dish-type').val(),
//				"id" : $('#dish-type-id').val(),
//			}),
			success : function(result) {
				if(result.legth!=0){
				TotalDishTasteArray=result;
				$.each(result,function(index,item){
					if(DishTasteArray.indexOf(item.itemDesc) == -1){
						$("#dishes-tag-table1").append('<div><button  onclick="addTag(this)" class="btn btn-default" type="button">'+item.itemDesc+'</button></div>');
					 }else{
						 var temp = '<button class="btn btn-default" type="button" onmouseover="delDisplay(this)" onmouseout="delHidden(this)">'+item.itemDesc+'<i onclick="removeSelectTag(this)" class="icon-remove hidden"></i></button>';
						 $("#dishes-tag-select1").append(temp);
					 }
					
				});
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert(errorThrown);
			}
		});
		
	});
	/*菜品标签弹出框显示*/
	$("#dishes-label-add").click(function(){
		$("#dishes-labelAdd-dialog").modal("show");
		$.ajax({
			url:global_Path+"/datadictionary/getDatasByType/DISHLABEL.json",
			type : "post",
			datatype : "json",
			contentType : "application/json; charset=utf-8",
			success : function(result) {
				TotalDishLabelArray.push(result);
				$.each(result,function(index,item){
					if(DishLabelArray.indexOf(item.itemDesc) == -1){
						$("#dishes-tag-table2").append('<div><button  onclick="addTag(this)" class="btn btn-default" type="button">'+item.itemDesc+'</button></div>');
					 }else{
						 var temp = '<button class="btn btn-default" type="button" onmouseover="delDisplay(this)" onmouseout="delHidden(this)">'+item.itemDesc+'<i onclick="removeSelectTag(this)" class="icon-remove hidden"></i></button>';
						 $("#dishes-tag-select2").append(temp);
					 }
					
				});
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert(errorThrown);
			}
		});
	});
	/*菜品口味确定按钮*/
	$("#dishes-taste-save").click(function(){
		DishTasteArray=[];
		var text = "";
		$(".dishes-tasteAdd-dialog .dishes-tag-select").find("button").each(function(){
//			 text =text+$(this).text()+",";
			DishTasteArray.push($(this).text());
		});
//		text = text.substr(0,text.length-1);
		if(DishTasteArray.length != 0){
			$("#dishes-taste-add").text(DishTasteArray.join(','));
		}else{
			$("#dishes-taste-add").text("");
			$("#dishes-taste-add").append('<i class="icon-plus-sign"></i>');
		}
	

	});	
	/*菜品标签确定按钮*/
	$("#dishes-label-save").click(function(){
		DishLabelArray=[];
		var text = "";
		$(".dishes-labelAdd-dialog .dishes-tag-select").find("button").each(function(){
//			 text =text+$(this).text()+",";
			DishLabelArray.push($(this).text());
		});
//		text = text.substr(0,text.length-1);
		if(DishLabelArray.length != 0){
			$("#dishes-label-add").text(DishLabelArray.join(','));
		}else{
			$("#dishes-label-add").text("");
			$("#dishes-label-add").append('<i class="icon-plus-sign"></i>');
		}
		initDishTaste(2);
//		$(".dishes-labelAdd-dialog .dishes-tag-select").find("button").each(function(){
//			var temp = '<label class="checkbox-inline"><input type="checkbox" value="">'+$(this).text()+'</label>';
//			$(".dishes-checkbox").append(temp);
//		});
//		$("#dishes-labelAdd-dialog").modal("hide");

	});
	/*添加一组计价单位*/
	$(".charge-unit-addBtn").click(function(){
		addotherUnit();
	});
	/*添加已有菜品至该分类*/
	$("#dishes-other-tab3").click(function(){
		$(".dishes-other-right").addClass("hidden");
		$("#dish-select-dialog").modal("show");
		$("#dish-select-dialog").load(global_Path+"/dish/loadDishSelect");
			
	});
	$('.glyphicon').click(function(){
 		if($(this).hasClass('glyphicon-chevron-up')){
 			$(this).removeClass('glyphicon-chevron-up').addClass('glyphicon-chevron-down');
 		}else if($(this).hasClass('glyphicon-chevron-down')){
 			$(this).removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-up');
 		}
 	});
 	$("#dishes-existAdd-dialog input[type='checkbox']").click(function(){

 		var existText = '';
 		var panelId = $(this).parent().parent().parent().attr("id");
 		var obj = $("#"+panelId).prev().find(".panel-title span");
 		$(this).parent().parent().find("label.checkbox-inline").find("input[type='checkbox']").each(function(){
 			if($(this).is(":checked"))
 			{
 				existText = existText+","+$.trim($(this).parent().text());
 			}
 		});
 		if(obj.next()[0].nodeName == 'SPAN')
 		{
 			obj.next().remove();
 		}
 		var temp = '<span class="dishes-existAdd-checked">('+existText.substr(1,existText.length-1)+')</span>';
 		obj.after(temp);

 	});
 	$("#introduction").on('keyup', function() {
 	    var len = getStrLength(this.value);
 	     $("#count").html(maxCount-len);
 	});
 	showAndHidden();
});
/*禁止浏览器自带的鼠标右键功能*/
window.onload = function ()
{
    var temp = document.getElementById('nav-dishes');
 	temp.oncontextmenu = function ()
    {
        return false;
    };
	var temp1 = document.getElementById("dishes-detailOther-Add");
    temp1.oncontextmenu = function ()
    {
        return false;
    };
};

function delDisplay(e){	
	$(e).find("i.icon-remove").removeClass("hidden");

}
function delHidden(e){
	$(e).find("i.icon-remove").addClass("hidden");

}

function removeTag(e){
	$(e).remove();
}
//function removeSelectTag(e){
//	var id = $(e).parent().parent().attr("id");
//	var num =id.substr(id.length-1,1);
//	var text = $(e).text();
//	var temp = '<div><button class="btn btn-default" onclick="addTag(this)" type="button">' + $(e).parent().text() + '</button></div>';
//	$("#dishes-tag-table"+num).append(temp);
//	removeTag(e);
//}
function addTag(e){
	var id = $(e).parent().parent().attr("id");
	var num =id.substr(id.length-1,1);
	var text = $(e).text();
	var temp = '<button class="btn btn-default" type="button" onclick="removeTag(this)" onmouseover="delDisplay(this)" onmouseout="delHidden(this)" onclick="removeTag(this)">'+text+'<i   class="icon-remove hidden"></i></button>';
	$("#dishes-tag-select"+num).append(temp);
	//removeTag(e);
}
/*菜品详情删除*/
function delDishesDetail(dishid,title,e)
{
	$.ajax({
		type:"post",
		async:false,
		url : global_Path+'/dish/getdishCol/'+dishid+'.json',
		contentType:'application/json;charset=UTF-8',
		dataType : "json",
		success : function(colArray) {
			if(colArray.length==1){
				$("#dishes-detailDel-dialog").modal("show");
				$("#showDishId").val(dishid);
				$("#showDishName").html(title);
			}else{
				$("#mutdishes-detailDel-dialog").modal("show");
				$("#showmulDishId").val(dishid);
				$("#selectall").next("span").text(title);
				$("#onlyone").next().next().text(title);
				$("#onlyone").next().text($("li.active").text());
			}
		}
	});
	
}
/*菜品详情删除*/
function delDishType(id,itemDesc,e)
{
	$("#dishtype-detailDel-dialog").modal("show");
	$("#showDishTypeName").html(itemDesc);
	$("#showDishTypeId").val(id);
	stoppro(e);
}
/**
 * 在这个删除事件后面调下这个方法，就不会调用按钮事件
 * @param evt
 */
function stoppro(evt){
	var e=evt?evt:window.event; //判断浏览器的类型，在基于ie内核的浏览器中的使用cancelBubble  
	if (window.event) {  
		e.cancelBubble=true;  
	} else {  
	    e.preventDefault(); //在基于firefox内核的浏览器中支持做法stopPropagation  
		e.stopPropagation();  
	}  
}
/*菜品分类拖曳*/
function allowDrop(ev)
{
	ev.preventDefault();
}

function drag(ev)
{
	ev.dataTransfer.setData("Text",ev.target.id);
}
/*分类拖动*/
function drop(ev)
{
	ev.preventDefault();
	ev.stopPropagation();
	var data=ev.dataTransfer.getData("Text");
	var drag_text =$("#"+data);
	var drag_num = $("#"+data).prevAll().length;
	var drop_text = ev.target;
	var drop_num = $("#"+ev.target.id).prevAll().length;

	if(drag_num>drop_num)
	{
		$(drop_text).before(drag_text);

	}else{
		$(drop_text).after(drag_text);
	}

	updateDishTypeOrder();
}
/**
 * 更新菜品分类的顺序
 */
function updateDishTypeOrder(){
	var idList=[];
	$("#nav-dishes li").each(function(index,item){
//		alert($(this).attr('id'));
		 var temp={};
		  temp.id=$(this).attr('id');
		  temp.itemsort=index;
		  idList.push(temp);
	});
	$.ajax({
		type:"post",
		async:false,
		url : global_Path+'/dishtype/updateListOrder.json',
		contentType:'application/json;charset=UTF-8',
	    data:JSON.stringify(idList), 
		dataType : "json",
		success : function(result) {
		}
	});
}
/**
 * 单击分类事件
 */
function  oneclickDishType(id){
	$.ajax({
		url : global_Path + "/dish/getDishesByDishType/"+id+".json",
		type : "post",
		datatype : "json",
		contentType : "application/json; charset=utf-8",
		success : function(result) {
			$(".nav-dishes-tab .dishes-detail-box").remove();
			$.each(result,function(index,item){
				$('#dishDetailList').before("<div class='dishes-detail-box' id='"+item.dishid+"' onmouseover='delDisplay(this)' onmouseout='delHidden(this)'>"+
				"<p class='dishes-detail-name'>"+item.title+"</p>"+
				"<p class='dishes-detail-code'>"+item.dishno+"</p>"+
				"<i class='icon-remove hidden' onclick=\"delDishesDetail('"+item.dishid+"','"+item.title+"',event)\"></i></div>");
				$('#'+item.dishid).dblclick(function(){
					$("#dishes-detailAdd-dialog").modal("show");
					$("#dishes-detailAdd-dialog").find("h4.modal-title").text("编辑菜品");
					viewAndEdit(item.dishid);
				});
			});
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}

	});
}

/* 双击修改查看菜品*/
function viewAndEdit(id){
	trflag=1;
	$.ajax({
		url : global_Path + "/dish/findById/"+id+".json",
		type : "post",
		datatype : "json",
		contentType : "application/json; charset=utf-8",
		success : function(result) {
			$("#dishid").val(result.tdishGroup.tdish.dishid);				
			$("#dishno").val(result.tdishGroup.tdish.dishno);				
			$("#title").val(result.tdishGroup.tdish.title);
			$("#introduction").val(result.tdishGroup.tdish.introduction);
			$("#count").html(maxCount-getStrLength(result.tdishGroup.tdish.introduction));
			$.each(result.listType,function(index,item){
				$("#dishTypeSelect").append('<span class="select-content-detail"><label>'+item.itemdesc+'</label><input type="checkbox" onclick="selectDishType()" id="'+item.id+'"></span><br/>');
			});
			var  columnidList=result.tdishGroup.tdish.columnid.split(',');
			var text='';
			var id='';
			if(!jQuery.isEmptyObject(columnidList)){
			$("#dishTypeSelect").find("input[type='checkbox']").each(function(){
				if(columnidList.indexOf($(this).attr('id'))!=-1){
//					$("#selectDishTypeName").val($(this).prev("label").html());
//					$("#selectDishTypeId").val($(this).attr('id'));
					text = text+","+$(this).prev("label").html();
					id = id+","+$(this).attr('id');
					$(this).attr("checked",true);
				}
			});
			}
			text = text.substr(1,text.length-1);
			id = id.substr(1,id.length-1);
			$("#selectDishTypeName").val(text);
			$("#selectDishTypeId").val(id);
			Unitarray=result.UnitHistorylist;
			addUnitHistoryToDiv("unit"+trflag,Unitarray);
			if(result.tdishGroup.list!=""&&typeof(result.tdishGroup.list) != "undefined"){
				$.each(result.tdishGroup.list,function(index,item){
					trflag=index+1;
					if(index==0){
						$("#unit1").val(item.unit);
						$("#price1").val(item.price);
	 					$("#vipprice1").val(item.vipprice);
					}else{
						addotherUnit();
						$("#unit"+trflag).val(item.unit);
						$("#price"+trflag).val(item.price);
	 					$("#vipprice"+trflag).val(item.vipprice);
					}
				});
			}
			if(result.tdishGroup.tdish.imagetitle!=""){
				$("#dishes-taste-add").text(result.tdishGroup.tdish.imagetitle);
				DishTasteArray=result.tdishGroup.tdish.imagetitle.split(",");
			}
			if(result.tdishGroup.tdish.abbrdesc!=""){
				$("#dishes-label-add").text(result.tdishGroup.tdish.abbrdesc);
				DishLabelArray=result.tdishGroup.tdish.abbrdesc.split(",");
			}
			if(result.tdishGroup.tdish.image!=""&&typeof(result.tdishGroup.tdish.image) != "undefined"){
				imagePath=result.tdishGroup.tdish.image;
				$("#uploadpic").attr("src",global_Path+'/'+result.tdishGroup.tdish.image);
			}
			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}

	});
} 
/**
 * 初始化菜品分类model
 */
function initDishType(){
	$('#dish-type-id').val("");
	$('#dish-type').val("");
	$("#warntip").hide();
	$("#emptywarntip").hide();
	$("#dishes-add-dialog").modal("hide");
}
/**
 * 初始化菜品口味model
 */
function initDishTaste(id){
	if(id=='1'){
		$("#dishes-tasteAdd-dialog").modal("hide");
	}
	if(id=='2'){
		$("#dishes-labelAdd-dialog").modal("hide");
	}
	$("#dishes-tag-select"+id+" button").remove();
	$("#dishes-tag-table"+id+" div").remove();
	$("#tagName"+id).val('');
	$("#warntip"+id).hide();
	$("#emptywarntip"+id).hide();
}
/**
 * 点击展示菜品单位历史数据
 */
function showUnitHistory(id){
	$("#Select"+id).toggleClass("hidden");
}
/**
 * 添加一组计量单位
 */
var Unitarray = new Array();
var trflag=1;
function addotherUnit(){
	trflag=trflag+1;
	var temp = '<div class="form-group" name="adddiv">';
	temp = temp+'<label class="col-xs-2 control-label">计价单位：</label><div class="col-xs-2">';
	temp=temp+'<div onclick="showUnitHistory(\'unit'+trflag+'\')"> <input type="text"	aria-describedby="basic-addon1" class="form-control" id="unit'+trflag+'">';
	temp = temp+'</div><div class="select-content hidden select-multi" id="Selectunit'+trflag+'"></div> </div>';
	temp = temp+'<label class="col-xs-2 control-label">价格：</label><div class="col-xs-2"><input type="text" value="" class="form-control" id="price'+trflag+'"></div>';
	temp =temp+'<label class="col-xs-2 control-label ">会员价：</label><div class="col-xs-2 input-widthl"><input type="text" value="" class="form-control" id="vipprice'+trflag+'"></div><i class="icon-remove" onclick="removeOtherUnit(this)" onmouseover="removeStyle1(this)" onmouseout="removeStyle2(this)"></i></div>';
	$(".dishes-charge-unit hr").before(temp);
	addUnitHistoryToDiv("unit"+trflag,Unitarray);
}
/*删除添加的计量单位*/
function removeOtherUnit(e){
	$(e).parent().remove();
}
function removeStyle1(e){
	$(e).removeClass("icon-remove");
	$(e).addClass("icon-remove-sign");
}
function removeStyle2(e){
	$(e).removeClass("icon-remove-sign");
	$(e).addClass("icon-remove");
}
/**
 * 将历史单位添加到Div中
 * 
 */
function addUnitHistoryToDiv(id,arrayobj){
	$.each(arrayobj,function(index,item){
		$("#Select"+id).append('<span class="select-content-detail" onclick="showSelectUnit(\''+id+'\',\''+item+'\')">'+item +'</span><br/>');
	});
} 
/*确认删除菜品分类*/
function confirmDelDishType(){
	$.ajax({
		url : global_Path + "/dishtype/delete/"+$("#showDishTypeId").val()+".json",
		type : "post",
		datatype : "json",
		contentType : "application/json; charset=utf-8",
		success : function(result) {
			var id=$("#showDishTypeId").val();
			$("#"+id).remove();
			$("#dishtype-detailDel-dialog").modal("hide");
			showAndHidden();
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}

	});
}
/*确认删除菜品*/
function confirmDelDish(flag){
	if(flag==1){
		$.ajax({
			url : global_Path + "/dish/delete.json",
			type : "post",
			datatype : "json",
			data:{ "dishid":$("#showDishId").val()},
//			contentType : "application/json; charset=utf-8",
			success : function(result) {
				var id=$("#showDishId").val();
				$("#"+id).remove();
				$("#dishes-detailDel-dialog").modal("hide");
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert(errorThrown);
			}

		});
	}
	if(flag==2){
		var num=$('input:radio[name=del]:checked').val();
		if(num==1){
			$.ajax({
				url : global_Path + "/dish/delete.json",
				type : "post",
				datatype : "json",
				data:{ "dishid":$("#showmulDishId").val()},
//				contentType : "application/json; charset=utf-8",
				success : function(result) {
					var id=$("#showmulDishId").val();
					$("#"+id).remove();
					$("#mutdishes-detailDel-dialog").modal("hide");
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					alert(errorThrown);
				}

			});
		}else{
			$.ajax({
				url : global_Path + "/dish/delete.json",
				type : "post",
				datatype : "json",
				data:{ "dishid":$("#showmulDishId").val(),
					"columnid":$("li.active").attr('id')},
//				contentType : "application/json; charset=utf-8",
				success : function(result) {
					var id=$("#showmulDishId").val();
					$("#"+id).remove();
					$("#mutdishes-detailDel-dialog").modal("hide");
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					alert(errorThrown);
				}

			});
		 }
		}
	
}
/**
 * 选择菜品分类
 */
function selectDishType(){
	var text = '';
	var id='';
	$("#dishTypeSelect").find("input[type='checkbox']").each(function(){
		if($(this).is(":checked"))
		{
			text = text+","+$(this).parent().text();
			id = id+","+$(this).attr('id');
		}
	});
	text = text.substr(1,text.length-1);
	id = id.substr(1,id.length-1);
	$("#selectDishTypeName").val(text);
	$("#selectDishTypeId").val(id);
	$("#dishTypeSelect").toggleClass("hidden");
}
/**
 * 初始化菜品添加页面
 */
function initDish(){
	$("#dishid").val("");				
	$("#dishno").val("");				
	$("#title").val("");
	$("#unit1").val("");
	$("#price1").val("");
	$("#vipprice1").val("");
	$("#dishTypeShow input").val("");
	$("#dishTypeSelect .select-content-detail").remove();
	$("#dishTypeSelect br").remove();
	$("#Selectunit1 .select-content-detail").remove();
	$("#Selectunit1 br").remove();
	$("div[name='adddiv']").remove();	
	$("#dishes-taste-add").text("");
	$("#dishes-taste-add").append('<i class="icon-plus-sign"></i>');
	$("#dishes-label-add").text("");
	$("#dishes-label-add").append('<i class="icon-plus-sign"></i>');
	$("#introduction").val("");
	$("#uploadpic").attr("src","../images/upload-img.png");
	 DishTasteArray = [];//已选择的菜品口味
	 TotalDishTasteArray = [];//总的菜品口味
	 DishLabelArray = [];//已选择的菜品标签
	 TotalDishLabelArray = [];//总的菜品标签
}
/**
 * 展示所选择的单位
 */
function showSelectUnit(id,item){
	$("#"+id).val(item);
	showUnitHistory(id);
}
/**
 * 点击图片触发input事件，选择图片
 */
function tempClick(){
	$("#main_img").click();
}
/**
 * 获取路径
 * @param file
 * @returns
 */
function getObjectURL(file) {
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
/**
 * 展示缩略图
 */
function showpic(){
 var strsrc=getObjectURL(document.getElementById("main_img").files[0]);
 $("#uploadpic").attr("src",strsrc);
 judgePicType();
}
/**
 * 判断图片是否正确
 */
var allowExt = ['jpg', 'gif', 'bmp', 'png', 'jpeg'];
function judgePicType(){
	var name=$("#main_img").val();
	var ext = name.substr(name.lastIndexOf(".")+1, name.length);
	alert(name);
	alert(ext);
	 if(allowExt.indexOf(ext) == -1){
		 alert("图片格式错误！");
	 }
}
/**
 * 保存口味和菜品标签
 * @returns
 */
function getDataDictionaryJson(id,text){
	var datadictionaryObject={};
	datadictionaryObject.id='';
	//口味
	if(id=='1'){
		datadictionaryObject.type='SPECIAL';
		datadictionaryObject.typename='忌口分类';
	}
	//菜品标签
	if(id=='2'){
		datadictionaryObject.type='DISHLABEL';
		datadictionaryObject.typename='菜品标签';
	}
	datadictionaryObject.itemDesc=text;
	datadictionaryObject.itemid='';
	datadictionaryObject.itemSort='';
	return JSON.stringify(datadictionaryObject);
}
/**
 * 获取菜品数据
 * @returns
 */
function getDishJson(){
	var unitList=getvaluesList('unit',1);
	var vippriceList=getvaluesList('vipprice',0);
	var priceList=getvaluesList('price',0);
	 var image;
	  if($("#main_img").val()=='../images/upload-img.png'){
	    	image=imagePath;
	    }else{
	    	image="";
	  }
	var tdishGroup={};
	var tdish={};
	var list=[];
	tdish.dishid=$("#dishid").val();
	tdish.dishno=$("#dishno").val();
	tdish.title=$("#title").val().replace("\"","“").replace("\"","”");
	tdish.dishtype='0';
	tdish.abbrdesc=DishLabelArray.join(',');
	tdish.columnid=$("#selectDishTypeId").val();
	tdish.unit=unitList;
	tdish.introduction=$("#introduction").val().replace("\"","“").replace("\"","”");
	tdish.vipprice=vippriceList;
	tdish.price=priceList;
	tdish.image=image;
	tdish.imagetitle=DishTasteArray.join(',');
	tdish.headsort=$("#unitflag").val();
	 $.each($("input[id^='price']"),function(index,item){
		var object=this;
		var i=object.id.substring(5);
		if(i!=''){			
		  temp={};
		  temp.dishid=$("#dishid").val();
		  temp.unit=$("#unit"+i).val();
		  temp.price=$("#price"+i).val();
		  temp.vipprice=$("#vipprice"+i).val();
		  temp.status=0;
		  temp.ordernum=0;
		  list.push(temp);
		}
	 });
	 tdishGroup.tdish=tdish;
	 tdishGroup.list=list;
	 return JSON.stringify(tdishGroup);
}
// 中文字符判断
function getStrLength(str) { 
    var len = str.length; 
    var reLen = 0; 
    for (var i = 0; i < len; i++) {        
        if (str.charCodeAt(i) < 27 || str.charCodeAt(i) > 126) { 
            // 全角    
            reLen += 2; 
        } else { 
            reLen++; 
        } 
    } 
    return reLen;    
}
/**
 * 关闭菜品页面
 */
function closdAddDish(){
	$("#dishes-detailAdd-dialog").modal("hide");
	initDish();
}
//判断是否是多计量单位的菜品
//多计量单位标识  标识这个菜是否是多计量的   0有多计量   1没有多计量
function validDishUnit(){
	if(trflag==1){
		$("#unitflag").val("1");
	}else{
		$("#unitflag").val("0");
	}
}
//获取多计量单位值的字符串 flag=0（  input框）   flag=1（ combobox框）
function getvaluesList(name,flag){
	var valueList=[];
	 if(flag==0){
		 $.each($("input[id^='"+name+"']"),function(index,item){
	     valueList.push(changeTwoDecimal_f($(this).val()));
		 });
	 }else{
		 $.each($("input[id^='"+name+"']"),function(index,item){
	     var object=this;
	     if(object.id!='unitflag'){
	  	  valueList.push($("#"+object.id).val()); 
	     }
	   });
	 }
	 return valueList.join("/");
}
//保留两位小数
function changeTwoDecimal_f(floatvar) {
	 if(floatvar!=""){
	var fx = parseFloat(floatvar);
	if (isNaN(fx)) {
		alert('价格输入错误');
		return false;
	}
	var fx = Math.round(fx*100) / 100;
	var s_x = fx.toString();
	var pos_decimal = s_x.indexOf('.');
	if (pos_decimal < 0) {
		pos_decimal = s_x.length;
		s_x += '.';
	}
	while (s_x.length <= pos_decimal + 2) {
		s_x += '0';
	}
	return s_x;
	}else{
		return "";
	}
}
/**
 * 保存多计量单位
 * @param thisdishid
 */
function saveDishUnit(thisdishid){
	var json=getUnitvalues(thisdishid);
	$.ajax({
		type:"post",
		async:false,
		url : global_Path+'/dishunit/save.json',
		contentType:'application/json;charset=UTF-8',
	    data:json, 
		dataType : "json",
		success : function(result) {
			closdAddDish();
		}
	});
}
//获取多计量单位的值
function getUnitvalues(thisdishid){
	var unitList=[];
	 $.each($("input[id^='price']"),function(index,item){
		var object=this;
		var i=object.id.substring(5);
		if(i!=''){			
		  temp={};
		  temp.dishid=thisdishid;
		  temp.unit=$("#unit"+i).val();
		  temp.price=$("#price"+i).val();
		  temp.vipprice=$("#vipprice"+i).val();
		  temp.status=0;
		  temp.ordernum=i;
		  unitList.push(temp);
		}
	 });
	  return JSON.stringify(unitList); 
}
/**
 * 添加菜品口味和标签
 */
function addTasteAndLabel(num,text){
	var json=getDataDictionaryJson(num,text);
	$.ajax({
		type:"post",
		async:false,
		url : global_Path+'/datadictionary/save',
		contentType:'application/json;charset=UTF-8',
	    data:json, 
		dataType : "json",
		success : function(result) {								
			var temp = '<div><button class="btn btn-default" onclick="addTag(this)" type="button">'+text+'</button></div>';
			$("#dishes-tag-table"+num).append(temp);
			$("#tagName"+num).val('');
		}
	});
}
function hideDialog(){
	$(".img-close").click();
}
function hideDialogB(){
	$("#dishes-tasteAdd-dialog .img-close").click();
}
function showAndHidden(){
	var count = $(".nav-dishes").find("li.nav-dishes-type").length;
	
	
	if(count>10){
		
		$(".nav-dishes-prev").css({"display":"inline"});
		$(".nav-dishes-next").css({"display":"inline"});
	}else{
		$(".nav-dishes-prev").css({"display":"none"});
		$(".nav-dishes-next").css({"display":"none"});
	}
	
	if(count>0){
		$("#dishDetailList").css({"display":"inline"});
		$(".dishes-content-title span").text("最新菜品");
	}else{
		$("#dishDetailList").css({"display":"none"});
		$(".dishes-content-title span").html("&nbsp;");
	}
	
}
