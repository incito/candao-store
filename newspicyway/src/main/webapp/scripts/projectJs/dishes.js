var tbPrinterDetailList=[];
var DishTasteArray = [];//已选择的菜品口味
var TotalDishTasteArray = [];//总的菜品口味
var TotalDishTasteArrayB = [];
var DishLabelArray = [];//已选择的菜品标签
var TotalDishLabelArray = [];//总的菜品标签
var TotalDishLabelArrayB = [];
var maxCount = 100;  // 菜品介绍可以输入的最大值
var imagePath='';
var TimeFn = null;
var flag_prev =0;
var dishArrBefore=[];
$(document).ready(function(){
	/*菜品分类鼠标滚动*/	
	var dom =$("#nav-dishes")[0];
    var user_agent = navigator.userAgent;
    if(user_agent.indexOf("Firefox")!=-1){// Firefox
            dom.addEventListener("DOMMouseScroll",addEvent_D,!1);

    } else if(user_agent.indexOf("MSIE")!=-1){// Firefox
             dom.attachEvent("onmousewheel",addEvent_D,!1);

    }else{
             dom.addEventListener("mousewheel",addEvent_D,!1);

    }
    
	Array.prototype.indexOf = function(val) {
        for (var i = 0; i < this.length; i++) {
            if (this[i] == val) return i;
        }
        return -1;
    };
    Array.prototype.remove = function(val) {
        var index = this.indexOf(val);
        if (index > -1) {
            this.splice(index, 1);
        }
    };


//	$("#cannotDeleteDialog").modal("show");
	isExistDishMenu();
	$("#add-form-dishes").validate({
		submitHandler : function(form) {
			var vcheck = true;

			if ($("#selectDishTypeName").val().trim() == "") {

				$("#selectDishTypeName_tip").text("必填信息");
				vcheck = false;
			} 
			
			if (vcheck) {
				if(check_same_dishName(1)){
				clickFormAdd();
				}else{
				}
			}
		}
	});
	findPrinterDish();
	$("img.img-close").hover(function(){
	 	$(this).attr("src",global_Path+"/images/close-active.png");	 
	},function(){
			$(this).attr("src",global_Path+"/images/close-sm.png");
	});
	showAndHidden();

	$("body").click(function(){
		$(".dishes-right-tab").addClass("hidden");
		if(dishesAdd_flag){
			
			$(".dishes-other-right").toggleClass("hidden");
			dishesAdd_flag=false;
		}else{
			$(".dishes-other-right").addClass("hidden");
		}
		
		if(unitHistory_flag){
			$("#Select"+unitHistory_id).toggleClass("hidden");
			unitHistory_flag=false;
		}else{
			$("#Select"+unitHistory_id).addClass("hidden");
		}
	});
	/*菜品分类向左向右按钮*/
	
	$(".nav-dishes-next").click(function(){
		var count = $(".nav-dishes").find("li.nav-dishes-type").length;
		if(flag_prev<count-10){
				$(".nav-dishes").find("li.nav-dishes-type").eq(flag_prev).css("margin-left","-10%");

				flag_prev++;
		}

	});
	$(".nav-dishes-prev").click(function(){
		console.log("inner"+flag_prev);
		if(flag_prev>=1){	
			$(".nav-dishes").find("li.nav-dishes-type").eq(flag_prev-1).css("margin-left","0");			

			flag_prev--;
		}
	});
	
	$("#dishes-type-cancle").click(function(){
		initDishType();
	});
	/*添加菜品分类弹出框*/
	$("#dishes-type-add").click(function(){
		initDishType();
		$("#dishes-add-dialog").modal("show");
		$("#dishes-add-dialog").find("div.modal-title").text("添加分类");
		$("#dishTypeAddType").val("1");
	});
	
	$("img.img-close").click(function(){
		$("div.modal-backdrop").css("z-index","1030");
	});
//	$(".btn-cancel").click(function(){
//		$("img.img-close").click();
//	});
	//点击菜品弹出框中确定按钮增加菜品
	var flagA=true;
	$("#dishes-type-save").click(function(){
		
		$("#warntip").hide();
		$("#emptywarntip").hide();
		if($("#dish-type").val().trim() == ""){
//			$(".error").addClass("hidden");
			$("#emptywarntip").show();
			return false;
		}
		
		var itemsort="";
		if($('#dish-type-id').val()==''){
			itemsort=$(".nav-dishes li").length+1;
		}
		$.ajax({
			url:global_Path+"/dishtype/judgeName.json",
			type : "post",
			datatype : "json",
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify({
				"itemdesc" : $('#dish-type').val(),
				"id" : $('#dish-type-id').val(),
				"itemsort" : itemsort
			}),
			success : function(data) {
				
				if(data.flag=="0"){
					$("#warntip").show();
				}
				if($('#dish-type-id').val()!=""&&data.flag!="0"){
					var id=$("#dish-type-id").val();
		    		$("#"+id).children(":first").text($('#dish-type').val());
		    		initDishType();
				}else{
					if(data.flag=="1"){
						
						var text = $("#dish-type").val();
						var temp = '<li class="nav-dishes-type " onclick="oneclickDishType(this.id)" ondrop="drop(event)"  ondragover="allowDrop(event)"  onmouseover="delDisplay(this)" onmouseout="delHidden(this)" draggable="true" ondragstart="drag(event)" id="'+data.id+'">';
						temp = temp+'<span>'+text+'</span><span>(0)</span><i class="icon-remove hidden" onclick="delDishType(\''+data.id+'\',\''+text+'\',event)"></i></li>';
						if(	$("#dishTypeAddType").val()=="1"){
							$(".nav-dishes").append(temp);
						}else if($("#dishTypeAddType").val()=="2"){
							$(".nav-dishes .active").after(temp);
							updateDishTypeOrder();
						}
						$("#"+data.id).dblclick(function(){
							$("#dishes-add-dialog").modal("show");
							$("#dishes-add-dialog").find("div.modal-title").text("编辑分类");
							$("#dish-type").val($(this).children(":first").text().trim());
							$("#dish-type-id").val($(this).attr('id'));
						});
					
						initDishType();
						showAndHidden();
						onMouseDown();
						$("#"+data.id).click();
						var navDishesCount=$(".nav-dishes .active").index();
						
						if(navDishesCount>10){
							$.each($(".nav-dishes").find("li"),function(index,item){
								$(".nav-dishes-next").click();
//								$(".nav-dishes-next").click();
								navDishesCount--;

								if(navDishesCount==9){
									return false;
								}
							});
//						});
						}
						else if(navDishesCount==10){
							$(".nav-dishes-next").click();
//							$(".nav-dishes").children(":first").css({"margin-left": "-10%"});
						}
//						$.each($(".nav-dishes").find("li"),function(index,item){
//							$(".nav-dishes-next").click();
//							if(index==count-10){
//								return false;
//							}
//						});
						if($(".nav-dishes li").length==1){
							$(".nav-dishes li").attr("class","active");
						}
					}
			}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert(errorThrown);
			}
		});
						
	});
	/*菜品分类右键弹出框*/
	onMouseDown();
	/*右键添加菜品分类*/
	$("li#dishes-right-tab1").click(function(){
		$("#dish-type").val("");
		$("#dish-type-id").val("");
		$("#dishes-add-dialog").modal("show");
		$(".dishes-right-tab").addClass("hidden");
		$("#dishTypeAddType").val("2");

	});
	/*右键编辑菜品分类*/
	$("li#dishes-right-tab2").click(function(){
		$("#dishes-add-dialog").modal("show");
		$("#dishes-add-dialog").find("div.modal-title").text("编辑分类");
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
		initDish();
		trflag=1;
		$("#dishes-detailAdd-dialog").modal("show");
		$("#dishes-detailAdd-dialog").find("div.modal-title").text("添加菜品");
		$.ajax({
			url : global_Path + "/dish/getInitData.json",
			type : "post",
			datatype : "json",
			contentType : "application/json; charset=utf-8",
			success : function(result) {
				$.each(result.listType,function(index,item){
					if($("#nav-dishes .active").attr("id")==item.id){
						
						$("#dishTypeSelect").append('<span class="select-content-detail"><label style="font-weight:normal;">'+item.itemdesc+'</label><input type="checkbox" onclick="selectDishType()" checked=true id="'+item.id+'"></span><br/>');
					}else{
						$("#dishTypeSelect").append('<span class="select-content-detail"><label style="font-weight:normal;">'+item.itemdesc+'</label><input type="checkbox" onclick="selectDishType()" id="'+item.id+'"></span><br/>');
					}
					
				});
				selectDishType();
				$("#dishTypeSelect").addClass("hidden");
				Unitarray=result.UnitHistorylist;
				addUnitHistoryToDiv("unit"+trflag,Unitarray);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert(errorThrown);
			}

		});

	});
	var dishesAdd_flag=false;
	$("#dishes-detailOther-Add").click(function(e){
		
		if(dishesAdd_flag==true){
			dishesAdd_flag=false;
		}else{
			dishesAdd_flag=true;
		}
	});
		/*select */
	$("#dishTypeShow").click(function(){
		//toggleClass() 对设置或移除被选元素的一个或多个类进行切换。
		$("#dishTypeSelect").toggleClass("hidden");
	});
	
	/*双击菜品名称弹出菜品编辑框*/
	$(".dishes-detail-box").dblclick(function(){
	    // 取消上次延时未执行的方法
	    clearTimeout(TimeFn);
	    //双击事件的执行代码
		if($(this).attr('data-dishtype')==0){
			initDish();
			$("#dishes-detailAdd-dialog").modal("show");
			$("#dishes-detailAdd-dialog").find("div.modal-title").text("编辑菜品");
			viewAndEdit($(this).attr('id'));
		}
		if($(this).attr('data-dishtype')==2){
			initcombodish();
			$("#dishes-combo-dialog").modal("show");
			$("#dishes-combo-dialog").find(".dishes-combo-title").text("编辑套餐");
			viewAndEditComboDish($(this).attr('id'));
		}
		if($(this).attr('data-dishtype')==1){
			initfishpotdish();
			$("#dishes-hot-dialog").modal("show");
			$("#dishes-hot-dialog").find(".dishes-combo-title").text("编辑鱼锅");
			viewAndEditfishpotDish($(this).attr('id'));
		}
		
	});

	/*菜品口味,菜品标签中添加按钮*/	
	$(".tagAdd-btn button").click(function(){
		
		var id = $(this).parent().attr("id");
		var num =id.substr(id.length-1,1);
		var text =$("#tagName"+num).val();
		$("#warntip" + num).hide();
		if(text == ""){
			$("#emptywarntip"+num).show();
			return false;
		}
		$("#emptywarntip"+num).hide();
		
		
		if (num == 1) {
			if(TotalDishTasteArrayB.length != 0){
		
				if (TotalDishTasteArrayB.indexOf(text) == -1) {
					
					addTasteAndLabel(num, text,$("#dishes-tag-table1 li").length+1);
					TotalDishTasteArrayB.push(text);
					$("#dishes-tag-table1").children(":last").click();
					$("#warntip" + num).hide();
				} else {
					$("#warntip" + num).show();
				}
			
			}else {
				addTasteAndLabel(num, text,$("#dishes-tag-table1 li").length+1);
				TotalDishTasteArrayB.push(text);
				$("#dishes-tag-table1").children(":last").click();
			}
		}
		if (num == 2) {
			if(TotalDishLabelArrayB.length != 0){
			
				if (TotalDishLabelArrayB.indexOf(text) == -1) {
					addTasteAndLabel(num, text,$("#dishes-tag-table2 li").length+1);
					TotalDishLabelArrayB.push(text);
					$("#dishes-tag-table2").children(":last").click();
				} else {
					$("#warntip" + num).show();
				}
			
		}else {
			addTasteAndLabel(num, text,$("#dishes-tag-table2 li").length+1);
			TotalDishLabelArrayB.push(text);
			$("#dishes-tag-table2").children(":last").click();
		}
		}
		showAndHidden2(num);
//		addTag(e);
		
	});
	/*菜品口味弹出框显示*/
	$("#dishes-taste-add").click(function(){
		initDishTaste(1);
		$("#dishes-tasteAdd-dialog").modal("show");
		// 二次弹框遮罩效果0305
		$("#dishes-tasteAdd-dialog").css("z-index","1042");
		$("div.modal-backdrop").css("z-index","1041");
		$("#dishes-tasteAdd-dialog-flag").val("0");//单品中的菜品标签标示
		
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
				if(result.length!=0){
				TotalDishTasteArray=result;
				TotalDishTasteArrayB=[];
				$.each(result,function(index,item){
					
					TotalDishTasteArrayB.push(item.itemDesc);
					tempB = '<li  id=\''+item.id+'\'  onclick="addTag(this)"  onmouseout="delHidden(this)" onmouseover="delDisplay(this)" class="btn btn-default noClick dishTasteUl" >'+item.itemDesc+'<i class="icon-remove hidden" onclick="delDishTasteAndLabel(1,\''+item.id+'\',\''+item.itemDesc+'\',event)"></i></li>';
					tempC = '<li  id=\''+item.id+'\'  onclick="addTag(this)"  onmouseout="delHidden(this)" onmouseover="delDisplay(this)" class="btn btn-default canClick dishTasteUl" >'+item.itemDesc+'<i class="icon-remove hidden" onclick="delDishTasteAndLabel(1,\''+item.id+'\',\''+item.itemDesc+'\',event)"></i></li>';
					if(DishTasteArray.indexOf(item.itemDesc) != -1){
						$("#dishes-tag-table1").append(tempB);
						
					}else{
						$("#dishes-tag-table1").append(tempC);
					}
					if(DishTasteArray.indexOf(item.itemDesc) != -1){
//						 var temp = '<button class="btn btn-default" type="button" onmouseover="delDisplay(this)" onmouseout="delHidden(this)" onclick="removeTag(this)">'+item.itemDesc+'<i  class="icon-remove hidden"></i></button>';
						var temp ='<li      onmouseout="delHidden(this)" onmouseover="delDisplay(this)" class="btn btn-default canClick dishTasteUl" >'+item.itemDesc+'<i class="icon-remove hidden" onclick="removeTag(this)"></i></li>';
						$("#dishes-tag-select1").append(temp);
						 
					 }
					
				});
				$(".noClick").attr("disabled",true);
				showAndHidden2(1);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert(errorThrown);
			}
		});
		
	});
	/*菜品标签弹出框显示*/
	$("#dishes-label-add").click(function(){
		initDishTaste(2);
		$("#dishes-labelAdd-dialog").modal("show");
		// 二次弹框遮罩效果
		$("#dishes-labelAdd-dialog").css("z-index","1042");
		$("div.modal-backdrop").css("z-index","1041");
		$("#dishes-labelAdd-dialog-flag").val("0");//单品中的菜品标签标示
		$.ajax({
			url:global_Path+"/datadictionary/getDatasByType/DISHLABEL.json",
			type : "post",
			datatype : "json",
			contentType : "application/json; charset=utf-8",
			success : function(result) {
				TotalDishLabelArrayB=[];
				TotalDishLabelArray.push(result);
				$.each(result,function(index,item){
					TotalDishLabelArrayB.push(item.itemDesc);
					tempB = '<li  id=\''+item.id+'\'  onclick="addTag(this)"  onmouseout="delHidden(this)" onmouseover="delDisplay(this)" class="btn btn-default noClick dishTasteUl" >'+item.itemDesc+'<i class="icon-remove hidden" onclick="delDishTasteAndLabel(2,\''+item.id+'\',\''+item.itemDesc+'\',event)"></i></li>';
					tempC = '<li  id=\''+item.id+'\'  onclick="addTag(this)"  onmouseout="delHidden(this)" onmouseover="delDisplay(this)" class="btn btn-default canClick dishTasteUl" >'+item.itemDesc+'<i class="icon-remove hidden" onclick="delDishTasteAndLabel(2,\''+item.id+'\',\''+item.itemDesc+'\',event)"></i></li>';
					if(DishLabelArray.indexOf(item.itemDesc) != -1){
						$("#dishes-tag-table2").append(tempB);
						
					}else{
						$("#dishes-tag-table2").append(tempC);
					}
					
					if(DishLabelArray.indexOf(item.itemDesc) != -1){
						var temp = '<li      onmouseout="delHidden(this)" onmouseover="delDisplay(this)" class="btn btn-default canClick dishTasteUl" >'+item.itemDesc+'<i class="icon-remove hidden" onclick="removeTag(this)"></i></li>';
						 $("#dishes-tag-select2").append(temp);
	 
					 }
					
				});
				$(".noClick").attr("disabled",true);
				showAndHidden2(2);
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
		$(".dishes-tasteAdd-dialog .dishes-tag-select").find("li").each(function(){
//			 text =text+$(this).text()+",";
			DishTasteArray.push($(this).text());
		});
//		text = text.substr(0,text.length-1);
		//dishes-tasteAdd-dialog-flag
		if($("#dishes-tasteAdd-dialog-flag").val()=="0"){
			if(DishTasteArray.length != 0){
				$("#dishes-taste-add").text(DishTasteArray.join(','));
			}else{
				$("#dishes-taste-add").text("");
				$("#dishes-taste-add").append('<i class="icon-plus-sign"></i>');
			}
		}
		if($("#dishes-tasteAdd-dialog-flag").val()=="1"){
			if(DishTasteArray.length != 0){
				$("#fishpotdishes-taste-add").text(DishTasteArray.join(','));
			}else{
				$("#fishpotdishes-taste-add").text("");
				$("#fishpotdishes-taste-add").append('<i class="icon-plus-sign"></i>');
			}
		}
	

	});	
	/*菜品标签确定按钮*/
	$("#dishes-label-save").click(function(){
		DishLabelArray=[];
		var text = "";
		$(".dishes-labelAdd-dialog .dishes-tag-select").find("li").each(function(){
//			 text =text+$(this).text()+",";
			DishLabelArray.push($(this).text());
		});
//		text = text.substr(0,text.length-1);
		if($("#dishes-labelAdd-dialog-flag").val()=="0"){
		if(DishLabelArray.length != 0){
			$("#dishes-label-add").text(DishLabelArray.join(','));
		}else{
			$("#dishes-label-add").text("");
			$("#dishes-label-add").append('<i class="icon-plus-sign"></i>');
		}
		}
		if($("#dishes-labelAdd-dialog-flag").val()=="2"){
			if(DishLabelArray.length != 0){
				$("#combodishes-label-add").text(DishLabelArray.join(','));
			}else{
				$("#combodishes-label-add").text("");
				$("#combodishes-label-add").append('<i class="icon-plus-sign"></i>');
			}
			}
		if($("#dishes-labelAdd-dialog-flag").val()=="1"){
			if(DishLabelArray.length != 0){
				$("#fishpotdishes-label-add").text(DishLabelArray.join(','));
			}else{
				$("#fishpotdishes-label-add").text("");
				$("#fishpotdishes-label-add").append('<i class="icon-plus-sign"></i>');
			}
			}
		initDishTaste(2);

	});
	/*添加一组计价单位*/
	$(".charge-unit-addBtn").click(function(){
		addotherUnit();
		unitShowAndHidden();
	});
	/*添加已有菜品至该分类*/
	$("#dishes-other-tab3").click(function(){
		$(".dishes-other-right").addClass("hidden");
		$("#dish-select-dialog").modal("show");
		$("#dish-select-dialog").load(global_Path+"/dish/loadDishSelect");
		
			
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
 	
 	    var len = this.value.length;
 	     $("#count").html(maxCount-len);
 	});
 	

//	$("#preferential-add div.col-xs-4").click(function(){
//		$(parent.document.all("detail")).attr("src","<%=request.getContextPath()%>/preferential/"+$(this).attr("data-type"));
//	});
	$(parent.document.all("allSearch")).css("visibility","visible");
	findData(null, 1, null);
 	//自动补全
 	var dishidSearch= "";
 	$(parent.document.all("allSearch")).find("input:text").autoComplete({
 		
 		delay: 300,
 		showCount: 6,
 		url : global_Path + "/dish/pageSearch.json",
 		onTextChange : function(e, id, type){
 			$("#nav-dishes").children("li").addClass("active").siblings().removeClass("active");
 			findData(null, 1, id);
 		},
 		searchBtn : $(parent.document.all("allSearch")).find("#basic-addon1"),
 		onBtnAction : function(){
 			$("#nav-dishes").children("li").addClass("active").siblings().removeClass("active");
 			findData(null, 1, null);
 		}
 	});
 	//查询数据
 	function findData(type, subPage, id){
 		
 		var dataJson = '{"page":"'+subPage+'","rows":"10"';
 		if(type && type != ""){
 			dataJson += ',"type":"'+type+'"';
 		}
 		if(id && id != ""){
 			dataJson += ',"dishid":"'+id+'"';
 		}
 		dataJson += '}';
 		$.ajax({
 			url : global_Path + "/dish/pageSearch.json",
 			data:$.parseJSON(dataJson),
 			type:"post",
 			dataType:"json",
 			success:function(data){
// 				var table = $("table.preferential-table");
// 				var dataBlock = table.find("tbody");
// 				var str, classToggle, starttime, endtime;
// 				$.each(data.rows, function(i, v){
// 					classToggle = i % 2 == 0 ? "odd" : "";
// 					if(v.type == "06"){
// 						starttime = endtime= "";
// 					}else{
// 						starttime = timestampformat(parseInt(v.starttime));
// 						endtime = timestampformat(parseInt(v.endtime));
// 					}
// 					str += '<tr class="'+classToggle+'" tr-id="tr'+i+'">\
// 								<td>'+((parseInt(subPage)-1)*10 + (1 + i))+'</td>\
// 								<td>'+v.code+'</td>\
// 								<td class="data_name">'+v.name+'</td>\
// 								<td>'+v.typeName+'</td>\
// 								<td><div class="td-color" style="background-color:'+v.color+'"></div></td>\
// 								<td>'+starttime+' - '+endtime+'</td>\
// 								<td  class="td-last">\
// 									<div class="operate">\
// 										<a href="javascript:void(0)" onclick="operaPreferntial(\''+v.id+'\',\''+v.type+'\',\''+v.subType+'\',\'look\')">查看</a>\
// 										<a href="javascript:void(0)" onclick="operaPreferntial(\''+v.id+'\',\''+v.type+'\',\''+v.subType+'\',\'modify\')">修改</a>\
// 										<a href="javascript:void(0)" class="deleteBtn" onclick="deletePreferntial(\''+v.id+'\',\''+i+'\',\''+v.name+'\')">删除</a>\
// 									</div>\
// 								</td>\
// 							</tr>'
// 				});
// 				dataBlock.empty().append(str);
// 				if(data.total > 10 && (subPage==null||subPage==1)){
// 					$(".pagingWrap").html('<ul class="paging clearfix">');
// 	        		$(".paging").twbsPagination({
// 						totalPages: data.pageCount,
// 	        			visiblePages: 7,
// 	        			first: '...',
// 	        			prev : '<',
// 	        			next : '>',
// 				        last: '...',
// 				        onPageClick: function (event, page){
// 				        	findData(type, page, null);
// 				        }
// 					});
// 				}else if(data.total <= 10){
// 					$(".pagingWrap").empty();
// 				}
 			}
 		});
 	}
 	
 	//进入菜谱管理页面
 	$("#dishes-menu-ctrl").click(function(){
 		$(parent.document.all("detail")).attr("src",global_Path + "/menu/menucontrol");
		$("#allSearch").css("visibility","hidden");
 	});
});
//--------------------------------------------------------------------------------------------------------
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
    document.querySelector(".dishes-right-tab").oncontextmenu=function(){
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
	
	var id = $(e).parent().parent().attr("id");
	
	var num =id.substr(id.length-1,1);
	var DishTasteOrLabelIndex = '';
	if(num=='1'){
		DishTasteOrLabelIndex = TotalDishTasteArrayB.indexOf($(e).parent().text());
//		alert(TotalDishTasteArrayB+"   "+TotalDishTasteArrayB.length+"   "+DishTasteOrLabelIndex);
	}else if(num=='2'){
		DishTasteOrLabelIndex = TotalDishLabelArrayB.indexOf($(e).parent().text());
	}
	var text = $(e).parent().text();
	var id = $("#dishes-tag-table"+num+" li ").eq(DishTasteOrLabelIndex).attr("id");
	var temp = '<li  id=\''+id+'\'  onclick="addTag(this)"  onmouseout="delHidden(this)" onmouseover="delDisplay(this)" class="btn btn-default canClick dishTasteUl" >'+text+'<i class="icon-remove hidden" onclick="delDishTasteAndLabel(\''+num+'\',\''+id+'\',\''+text+'\',event)"></i></li>';
	$("#dishes-tag-table"+num+" li ").eq(DishTasteOrLabelIndex).after(temp);
	$("#dishes-tag-table"+num+" li ").eq(DishTasteOrLabelIndex).remove();
	$(e).parent().remove();
}
function addTag(e){
	var id = $(e).parent().attr("id");
	var num =id.substr(id.length-1,1);
	var count = $("#dishes-tag-select"+num).find("li").length;

	if(count<4){
		var text = $(e).text();
		var temp ='<li      onmouseout="delHidden(this)" onmouseover="delDisplay(this)" class="btn btn-default canClick dishTasteUl" >'+text+'<i class="icon-remove hidden" onclick="removeTag(this)"></i></li>';
		$("#dishes-tag-select"+num).append(temp);

		$(e).attr("disabled",true);
		//removeTag(e);
	}
//	$(e).children("i").hide();
}

/*菜品详情删除*/
function delDishesDetail(dishid,title,e)
{
	stoppro(e);
	$.ajax({
		type:"post",
		async:false,
		url : global_Path+'/dish/comfirmDelDish/'+dishid+'.json',
		contentType:'application/json;charset=UTF-8',
		dataType : "json",
		success : function(result) {
	
			if(result==""){
				ifDishesDetail(dishid,title,e);
			}else{
				$("#showCannotDeleteName").text('菜品"'+$("#"+dishid).text()+'"正在被菜谱使用，删除失败！');
				$("#cannotDeleteDialog").modal("show");
			}
		}
	});
	
	stoppro(e);
}
function ifDishesDetail(dishid,title,e)
{
	stoppro(e);
	$.ajax({
		type:"post",
		async:false,
		url : global_Path+'/combodish/ifDishesDetail/'+dishid+'.json',
		contentType:'application/json;charset=UTF-8',
		dataType : "json",
		success : function(result) {
	
			if(result==""){
				comfirmDelDishesDetail(dishid,title,e);
			}else{
				$("#showCannotDeleteName").text('菜品"'+$("#"+dishid).text()+'"正在被其他菜品引用，删除失败！');
				$("#cannotDeleteDialog").modal("show");
			}
		}
	});
	
	stoppro(e);
}

function comfirmDelDishesDetail(dishid,title,e){
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
	stoppro(e);
}
/*菜品分类详情删除*/
function delDishType(id,itemDesc,e)
{
	$.ajax({
		type:"post",
		async:false,
		url : global_Path+'/dish/comfirmDelDishType.json',
//		contentType:'application/json;charset=UTF-8',
		dataType : "json",
		data:{ "dishType":id},
		success : function(result) {
			
			if(result=="0"){
				ifDelDishType(id,itemDesc,e);
			}else if(result=="1"){
				$("#showCannotDeleteName").text('"'+$("#"+id).children(":first").text()+'"中的菜品正在被菜谱使用，删除失败！');
				$("#cannotDeleteDialog").modal("show");
			}
		}
	});
	
	stoppro(e);
}
function ifDelDishType(id,itemDesc,e)
{
	$.ajax({
		type:"post",
		async:false,
		url : global_Path+'/combodish/ifDelDishType.json',
//		contentType:'application/json;charset=UTF-8',
		dataType : "json",
		data:{ "dishType":id},
		success : function(result) {
			
			if(result=="0"){
				comfirmDelDishType(id,itemDesc,e);
			}else if(result=="1"){
				$("#showCannotDeleteName").text('"'+$("#"+id).children(":first").text()+'"中的菜品正在被其他菜品引用，删除失败！');
				$("#cannotDeleteDialog").modal("show");
			}
		}
	});
	
	stoppro(e);
}
function comfirmDelDishType(id,itemDesc,e){
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
	var drag_text =document.getElementById(data);
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
 *  菜品标签顺序固定
 * 
 */
function updateDishTagOrder(num){

	var idList=[];
	$("#dishes-tag-table"+num+" li").each(function(index,item){
		 var temp={};
		  temp.dictid=$(this).attr('id');
		  temp.itemSort=index+1;
		  idList.push(temp);
	});
	$.ajax({
		type:"post",
		async:false,
		url : global_Path+'/dish/updateDishTagListOrder.json',
		contentType:'application/json;charset=UTF-8',
	    data:JSON.stringify(idList), 
		dataType : "json",
		success : function(result) {
			if(result=="success"){
				
			}
		}
	});
}
/**
 * 更新菜品分类的顺序
 */
function updateDishTypeOrder(){
	var idList=[];
	$("#nav-dishes li").each(function(index,item){
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
	var count = $(".nav-dishes").find("li.nav-dishes-type").length;
	$(".nav-dishes li").removeClass("active");
	$("#"+id).addClass("active");
	$.ajax({
		url : global_Path + "/dish/getDishesByDishType/"+id+".json",
		type : "post",
		datatype : "json",
		contentType : "application/json; charset=utf-8",
		success : function(result) {
			$(".nav-dishes-tab .dishes-detail-box").remove();
			var resultLength = result.length;
			var dishTitle = '';
			$.each(result,function(index,item){
				dishTitle = substrControl(item.title,14);
				$('#dishDetailList').before("<div class='dishes-detail-box' id='"+item.dishid+"'  onmouseover='delDisplay(this)' data-dishtype='"+item.dishtype+"' onmouseout='delHidden(this)'>"+
				"<p class='dishes-detail-name'>"+dishTitle+"</p>"+
				"<i class='icon-remove hidden' onclick=\"delDishesDetail('"+item.dishid+"','"+item.title+"',event)\"></i></div>");
			
				
				$('#'+item.dishid).dblclick(function(){
					  // 取消上次延时未执行的方法
				    clearTimeout(TimeFn);
				    //双击事件的执行代码
					if($(this).attr('data-dishtype')==0){
						initDish();
						$("#dishes-detailAdd-dialog").modal("show");
						$("#dishes-detailAdd-dialog").find("div.modal-title").text("编辑菜品");
						viewAndEdit($(this).attr('id'));
					}
					if($(this).attr('data-dishtype')==2){
						initcombodish();
						$("#dishes-combo-dialog").modal("show");
						$("#dishes-combo-dialog").find(".dishes-combo-title").text("编辑套餐");
						viewAndEditComboDish($(this).attr('id'));
					}
					if($(this).attr('data-dishtype')==1){
						initfishpotdish();
						$("#dishes-hot-dialog").modal("show");
						$("#dishes-hot-dialog").find(".dishes-combo-title").text("编辑鱼锅");
						viewAndEditfishpotDish($(this).attr('id'));
					}
				});
				$('#'+item.dishid).click(function(){
					var id =$(this).attr('id');
					 // 取消上次延时未执行的方法
				    clearTimeout(TimeFn);
				    //执行延时
				    TimeFn = setTimeout(function(){
				        //do function在此处写单击事件要执行的代码
				    	editMenuView(id);
				    },500);
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
			console.log(result);
			$("#dishid").val(result.tdishGroup.tdish.dishid);				
			$("#dishno").val(result.tdishGroup.tdish.dishno);				
			$("#title").val(result.tdishGroup.tdish.title);
			$("#introduction").val(result.tdishGroup.tdish.introduction);
			$("#count").html(maxCount-result.tdishGroup.tdish.introduction.length);
			$.each(result.listType,function(index,item){
				$("#dishTypeSelect").append('<span class="select-content-detail"><label>'+item.itemdesc+'</label><input type="checkbox" onclick="selectDishType()" id="'+item.id+'"></span><br/>');
			});
			var  columnidList=result.tdishGroup.tdish.columnid.split(',');
			dishArrBefore = columnidList;
			var text='';
			var id='';
			if(!jQuery.isEmptyObject(columnidList)){
			$("#dishTypeSelect").find("input[type='checkbox']").each(function(){
				if(columnidList.indexOf($(this).attr('id'))!=-1){
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
			$("#unit1").parents(".dishChargeUnit").remove();
			if(result.tdishGroup.list!=""&&typeof(result.tdishGroup.list) != "undefined"){
				$.each(result.tdishGroup.list,function(index,item){
					trflag=index+1;
						addotherUnit();
						$("#unit"+trflag).val(item.unit);
						$("#price"+trflag).val(item.price);
	 					$("#vipprice"+trflag).val(item.vipprice);
					
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
//			if(result.tdishGroup.tdish.image!=""&&typeof(result.tdishGroup.tdish.image) != "undefined"){
//				imagePath=result.tdishGroup.tdish.image;
//				$("#uploadpic").attr("src",result.tdishGroup.tdish.image);
//			}
			if(result.tdishGroup.tdish.image!=""&&typeof(result.tdishGroup.tdish.image) != "undefined"){
				imagePath=result.tdishGroup.tdish.image;
				$("#uploadpic").attr("src",img_Path+imagePath);
			}
			
			 if(result.tdishGroup.tdish.recommend!=""&&typeof(result.tdishGroup.tdish.recommend) != "undefined"&&result.tdishGroup.tdish.recommend=="1"){
				  $("#recommend").prop("checked","checked");
			 }else{
			  $("#recommend").prop("checked",""); 
			 }
			 if(result.tdishGroup.tdish.weigh!=""&&typeof(result.tdishGroup.tdish.weigh) != "undefined"&&result.tdishGroup.tdish.weigh=="1"){
			  $("#weigh").prop("checked","checked");
			 }else{
			  $("#weigh").prop("checked",""); 
			}
			unitShowAndHidden();
			
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
	$("#dishes-tag-select"+id+" li").remove();
	$("#dishes-tag-table"+id+" li").remove();
	$("#tagName"+id).val('');
	$("#warntip"+id).hide();
	$("#emptywarntip"+id).hide();
}
/**
 * 点击展示菜品单位历史数据
 */
var unitHistory_flag= false;
var unitHistory_id = "";
function showUnitHistory(id){
//	$("#Select"+id).toggleClass("hidden");
	unitHistory_id = id;
	
	if(unitHistory_flag==true){
		unitHistory_flag=false;
	}else{
		unitHistory_flag=true;
	}
}
/**
 * 添加一组计量单位
 */
var Unitarray = new Array();
var trflag=1;

function addotherUnit(){
	if($("#unit1").attr("id")=="unit1"){
		trflag=trflag+1;
	}
	else{
		trflag++;
	}
	var temp = '<div class="form-group dishChargeUnit" name="adddiv" onmouseover="removeStyleUnitR(this)" onmouseout="removeStyleUnitT(this)">';
	temp = temp+'<label class="col-xs-2 control-label"  onmouseover="removeStyle1(this)" onmouseout="removeStyle2(this)"><span class="required-span">*</span>计价单位：</label><div class="col-xs-2" onmouseover="removeStyle1(this)" onmouseout="removeStyle2(this)">';
	
	temp=temp+'<div onclick="showUnitHistory(\'unit'+trflag+'\')"  > <input type="text" 	aria-describedby="basic-addon1" class="form-control required" id="unit'+trflag+'" name="unit'+trflag+'" AUTOCOMPLETE="off" placeholder="只能输入汉字和英文"  onkeyup="value=value.replace(&#47;[&#94;&#92;a&#45;&#92;z&#92;A&#45;&#92;Z&#92;u4E00&#45;\\u9FA5]&#47;g,&#39;&#39;)" >';
	temp = temp+'</div><div class="select-content hidden select-multi" id="Selectunit'+trflag+'"></div> </div>';
	temp = temp+'<label class="col-xs-2 control-label" onmouseover="removeStyle1(this)" onmouseout="removeStyle2(this)" ><span class="required-span">*</span>价格：</label><div class="col-xs-2" onmouseover="removeStyle1(this)" onmouseout="removeStyle2(this)" ><input type="text" value=""  class="form-control required" id="price'+trflag+'" name="price'+trflag+'"  placeholder="最多两位小数的数字" onkeyup="this.value= this.value.match(&#47;\\d+(\\.\\d{0,2})?&#47;) ? this.value.match(&#47;\\d+(\\.\\d{0,2})?&#47;)[0] : &#39;&#39;"></div>';
	temp =temp+'<label class="col-xs-2 control-label " onmouseover="removeStyle1(this)" onmouseout="removeStyle2(this)" >会员价：</label><div class="col-xs-2 input-widthl" onmouseover="removeStyle1(this)" onmouseout="removeStyle2(this)"><input  type="text" value="" class="form-control " id="vipprice'+trflag+'" name="vipprice'+trflag+'"  placeholder="最多两位小数的数字" onkeyup="this.value= this.value.match(/\\d+(\\.\\d{0,2})?/) ? this.value.match(/\\d+(\\.\\d{0,2})?/)[0] : &#39;&#39;"></div><i class="icon-remove hidden iclass" onclick="removeOtherUnit(this)"  style="display:block;padding-top:8px;"  onmouseover="removeStyle11(this)" onmouseout="removeStyle22(this)" id="hiddenidi"></i></div>';
	$(".dishes-charge-unit hr").before(temp);
	addUnitHistoryToDiv("unit"+trflag,Unitarray);
	
}
function addotherUnit1(){
	trflag=1;
	var temp = '<div class="form-group dishChargeUnit" name="adddiv" onmouseover="removeStyleUnitR(this)" onmouseout="removeStyleUnitT(this)">';
	temp = temp+'<label class="col-xs-2 control-label"  onmouseover="removeStyle1(this)" onmouseout="removeStyle2(this)"><span class="required-span">*</span>计价单位：</label><div class="col-xs-2" onmouseover="removeStyle1(this)" onmouseout="removeStyle2(this)">';
	
	temp=temp+'<div onclick="showUnitHistory(\'unit'+trflag+'\')"  > <input type="text" 	aria-describedby="basic-addon1" class="form-control required" id="unit'+trflag+'" name="unit'+trflag+'" AUTOCOMPLETE="off" placeholder="只能输入汉字和英文"  onkeyup="value=value.replace(&#47;[&#94;&#92;a&#45;&#92;z&#92;A&#45;&#92;Z&#92;u4E00&#45;\\u9FA5]&#47;g,&#39;&#39;)" >';
	temp = temp+'</div><div class="select-content hidden select-multi" id="Selectunit'+trflag+'"></div> </div>';
	temp = temp+'<label class="col-xs-2 control-label" onmouseover="removeStyle1(this)" onmouseout="removeStyle2(this)" ><span class="required-span">*</span>价格：</label><div class="col-xs-2" onmouseover="removeStyle1(this)" onmouseout="removeStyle2(this)" ><input type="text" value=""  class="form-control required" id="price'+trflag+'" name="price'+trflag+'"  placeholder="最多两位小数的数字" onkeyup="this.value= this.value.match(&#47;\\d+(\\.\\d{0,2})?&#47;) ? this.value.match(&#47;\\d+(\\.\\d{0,2})?&#47;)[0] : &#39;&#39;"></div>';
	temp =temp+'<label class="col-xs-2 control-label " onmouseover="removeStyle1(this)" onmouseout="removeStyle2(this)" >会员价：</label><div class="col-xs-2 input-widthl" onmouseover="removeStyle1(this)" onmouseout="removeStyle2(this)"><input  type="text" value="" class="form-control " id="vipprice'+trflag+'" name="vipprice'+trflag+'"  placeholder="最多两位小数的数字" onkeyup="this.value= this.value.match(/\\d+(\\.\\d{0,2})?/) ? this.value.match(/\\d+(\\.\\d{0,2})?/)[0] : &#39;&#39;"></div><i class="icon-remove hidden iclass" onclick="removeOtherUnit(this)"  style="display:block;padding-top:8px;"  onmouseover="removeStyle11(this)" onmouseout="removeStyle22(this)" id="hiddenidi"></i></div>';
	$(".dishes-charge-unit hr").before(temp);
	
 
}
/*删除添加的计量单位*/
function removeOtherUnit(e){
	
	var j=$('.iclass').size();

	if(j==2){
		$('.iclass').addClass("hidden");
		$(e).parent().remove();
	}else{
		$(e).parent().remove();
	}
	
}

function removeStyle1(e){
//	$(e).siblings("i").css("display","block");
	stoppro(e);
}
function removeStyle2(e){
//	$(e).siblings("i").fadeOut(3000);
	stoppro(e); 
}
function removeStyleUnitR(e){
	var unitCount=$('.iclass').size();
	if(unitCount>1){
		$(e).find('.iclass').removeClass("hidden");

	}
	
}
function removeStyleUnitT(e){
	$(e).find('.iclass').addClass("hidden");
//	$(e).siblings("i").fadeOut(3000);
}

function removeStyle11(e){
	$(e).siblings("i").css("display","block");
	$(e).removeClass("icon-remove");
	$(e).addClass("icon-remove-sign");
	//e.stopPropagation();  

	$(e).find("i").css("display","none");
}
function removeStyle22(e){
	$(e).siblings("i").fadeOut(3000);
	$(e).addClass("icon-remove");
	$(e).removeClass("icon-remove-sign");
	//e.stopPropagation();   
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
			menuShowAndHidden();
			var id=$("#showDishTypeId").val();
			$("#"+id).remove();
			$("#dishtype-detailDel-dialog").modal("hide");
			showAndHidden();
			
			if($("#nav-dishes .active").attr("id")==null){
				$(".nav-dishes-tab .dishes-detail-box").remove();
				$("#nav-dishes").children(":first").click();
				flag_prev=0;
				$("#nav-dishes li").each(function(){
					
					$(this).css({
						"margin-left": "0"
					});
				});
				$("#nav-dishes .active").click();
			}
			else{
				$(".nav-dishes-prev").click();
			}
			
			updateDishTypeOrder();
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
				$("#nav-dishes .active ").find("span").eq(1).text("("+($(".nav-dishes-tab div ").length-2)+")");
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert(errorThrown);
			}

		});
	}
	if(flag==2){
		var num=$('input:radio[name=del]:checked').val();
		if(num==1){
			deleteDishInAllColumn($("#showmulDishId").val());
			$.ajax({
				url : global_Path + "/dish/delete.json",
				type : "post",
				datatype : "json",
				data:{ "dishid":$("#showmulDishId").val()},
//				contentType : "application/json; charset=utf-8",
				success : function(result) {
					var id=$("#showmulDishId").val();
					$("#mutdishes-detailDel-dialog").modal("hide");
						$("#"+id).remove();
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
					$("#nav-dishes .active ").find("span").eq(1).text("("+($(".nav-dishes-tab div ").length-2)+")");
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
	$("#combotitle").val("");
	$("#fishpottitle").val("");
	$("#combodishid").val("");
	$("#fishpotdishid").val("");
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
	$("#main_img").val("");
	 DishTasteArray = [];//已选择的菜品口味
	 TotalDishTasteArray = [];//总的菜品口味
	 TotalDishTasteArrayB=[];
	 DishLabelArray = [];//已选择的菜品标签
	 TotalDishLabelArray = [];//总的菜品标签
	 TotalDishLabelArrayB = [];
	$("#dishTypeSelect").addClass("hidden"); 
	$(".error").text("");
	$("#combodishid").val("");
	$("input.error").removeClass("error");
	$("#unit1").parents(".dishChargeUnit").remove();
	addotherUnit1();
	imagePath="";
	//现场称重,参与人气推荐都变成否
	$("#recommend").prop("checked","");
	$("#weigh").prop("checked","");
	dishArrBefore=[];
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
 if(strsrc!=null){
	 $("#uploadpic").attr("src",strsrc);
	
	 judgePicType();
 }
}
/**
 * 判断图片是否正确
 */
var allowExt = ['jpg', 'gif', 'bmp', 'png', 'jpeg'];
function judgePicType(){
	var name=$("#main_img").val();
	var ext = name.substr(name.lastIndexOf(".")+1, name.length);
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
	datadictionaryObject.itemSort=$("#dishes-tag-table"+id+" li").length+1;
	return JSON.stringify(datadictionaryObject);
}
/**
 * 获取菜品数据
 * @returns
 */
//function getDishJson(){
//	var unitList=getvaluesList('unit',1);
//	var vippriceList=getvaluesList('vipprice',0);
//	var priceList=getvaluesList('price',0);
//	 var image;
//	  if($("#main_img").val()==''){
//	    	image=imagePath;
//	    }else{
//	    	image="";
//	  }
//	var tdishGroup={};
//	var tdish={};
//	var list=[];
//	tdish.dishid=$("#dishid").val();
//	tdish.dishno=$("#dishno").val();
//	tdish.title=$("#title").val().replace("\"","“").replace("\"","”");
//	tdish.dishtype='0';
//	tdish.abbrdesc=DishLabelArray.join(',');
//	tdish.columnid=$("#selectDishTypeId").val();
//	tdish.unit=unitList;
//	tdish.introduction=$("#introduction").val().replace("\"","“").replace("\"","”");
//	tdish.vipprice=vippriceList;
//	tdish.price=priceList;
//	tdish.image=image;
//	tdish.imagetitle=DishTasteArray.join(',');
//	tdish.headsort=$("#unitflag").val();
//	 $.each($("input[id^='price']"),function(index,item){
//		var object=this;
//		var i=object.id.substring(5);
//		if(i!=''){			
//		  temp={};
//		  temp.dishid=$("#dishid").val();
//		  temp.unit=$("#unit"+i).val();
//		  temp.price=$("#price"+i).val();
//		  temp.vipprice=$("#vipprice"+i).val();
//		  temp.status=0;
//		  temp.ordernum=0;
//		  list.push(temp);
//		}
//	 });
//	 tdishGroup.tdish=tdish;
//	 tdishGroup.list=list;
//	 return JSON.stringify(tdishGroup);
//}
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
function closdAddDish(e){
	$(e).parents('.parent1')
	$(e).modal("hide");
//	initDish();
}
//判断是否是多计量单位的菜品
//多计量单位标识  标识这个菜是否是多计量的   0有多计量   1没有多计量
function validDishUnit(){
	if($(".dishChargeUnit").length==1){
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
function addTasteAndLabel(num,text,itemSort){
	var json=getDataDictionaryJson(num,text);
	$.ajax({
		type:"post",
		async:false,
		url : global_Path+'/datadictionary/save.json',
		contentType:'application/json;charset=UTF-8',
	    data:json, 
		dataType : "json",
		success : function(result) {	
			var temp ='<li  id=\''+result.id+'\'  onclick="addTag(this)"  onmouseout="delHidden(this)" onmouseover="delDisplay(this)" class="btn btn-default canClick dishTasteUl" >'+text+'<i class="icon-remove hidden" onclick="delDishTasteAndLabel(\''+num+'\',\''+result.id+'\',\''+text+'\',event)"></i></li>';
			$("#dishes-tag-table"+num).append(temp);
			$("#tagName"+num).val('');
		}
	});
}
function hideDialog(){
	
	$(".img-close").click();
}
function hideDialogB(){
	$("div.modal-backdrop").css("z-index", "1030");
}

function hideDialogTasteAdd(){
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
		
		$(".dishes-content-hr").css({"display":"block"});
		$("#dishDetailList").css({"display":"inline-block"});
		$(".dishes-content-title span").text("最新菜品");
//		$(".menu-enter").css({"display":"block"});
	}else{
		$(".nav-dishes-tab").css({"border":"0px"});
		$(".dishes-content-hr").css({"display":"none"});
		$("#dishDetailList").css({"display":"none"});
		$(".dishes-content-title span").html("&nbsp;");
//		$(".menu-enter").css({"display":"none"});
		
	}
	if(count>1){
		$("#dishes-other-tab3").css({"display":"block"});
	}else{
		$("#dishes-other-tab3").css({"display":"none"});
	}
	if(count>10){
		$("#dishTypeSelect").css({"overflow": "auto","height":"305px"});
	}else{
		$("#dishTypeSelect").css({"overflow": "","height":""});
	}
}
function menuShowAndHidden(){
	$("#nav-dishes li").each(function(i,item){
	if($(this).find("span").eq(1).text().split("(")[1].split(")")[0]>0){
		$(".menu-enter").css({"display":"block"});
		return false;
	}else{
		$(".menu-enter").css({"display":"none"});
	}
});
}
function unitShowAndHidden(){
//	var unitCount=$('.iclass').size();
//	if(unitCount>1){
//	 $('.iclass').removeClass("hidden");
//	}
}
function showAndHidden2(num){
	var count = $("#dishes-tag-table"+num).find("li").length;

	if(count>15){
		$("#dishes-tag-table"+num).css({"overflow": "auto","height":"100px"});
	}else{
		$("#dishes-tag-table"+num).css({"overflow": "","height":""});
	}
	
	
}

 function changeActive(){
	 $("li.nav-dishes-type").click(function(){
			$(".nav-dishes li").removeClass("active");
			$(this).addClass("active");
			$(".dishes-right-tab").addClass("hidden");

		});
 }
 function onMouseDown(){
	 $("li.nav-dishes-type").on("mousedown",function(e){
			if(e.button == 2)
			{  
				$(this).click();
				var num =0;
				$(this).prevAll().each(function(){
					if($(this).css("margin-left") =='0px')
					 	num++;
				});
				var left = num*10*0.99+'%';
				var top = '70px';
				$(".dishes-right-tab").removeClass("hidden");
				$(".dishes-right-tab").css("left",left);
				$(".dishes-right-tab").css("top",top);
				var dishes_name = $(this).children(":first").text();
				$("li#dishes-right-tab2 span").text("编辑"+"\""+dishes_name+"\"");
				$("li#dishes-right-tab3 span").text("删除"+"\""+dishes_name+"\"");
				$("#dish-type").val(dishes_name.trim());
				$("#dish-type-id").val($(this).attr('id'));
				
			}
		});
 }
 function checkStatus(name){
	 var flag=0;
	  var isCheck = $("#"+name).prop("checked");
	  if(isCheck){
		  flag=1;
	  }else{
		  flag=0;
	  }
	  return flag;
 }
 
 function clickFormAdd(){
		var name = $("#title").val();
		var code = $("#dishno").val();
		var unitList=getvaluesList('unit',1);
		var vippriceList=getvaluesList('vipprice',0);
		var priceList=getvaluesList('price',0);
		var weigh=checkStatus('weigh');
		var recommend=checkStatus('recommend');
		 var image;
		  if($("#main_img").val()==''){
		    	image=imagePath;
		    }else{
		    	image="";
		  }
		
		validDishUnit();
		  $.ajaxFileUpload({
			    fileElementId: ['main_img'],  
			    url: global_Path+"/dish/save",  
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
			    	 headsort:$("#unitflag").val(),
			    	 weigh:weigh,
			    	 recommend:recommend
			    },
			    success: function (data, textStatus) {
			    	var arr = $("#selectDishTypeId").val().split(',');
			    	var dishArrAdd=[];
			    	var dishArrDel=[];
			    	$.each(arr,function(i,item){
			    		if(dishArrBefore.indexOf(item)==-1){
			    			
			    			dishArrAdd.push(item);
			    		}
			    			
			    		
			    	});
			    	$.each(dishArrBefore,function(i,item){
			    		if(arr.indexOf(item)==-1){
			    			
			    			dishArrDel.push(item);
			    		}
			    			
			    		
			    	});
			    	dishNumChange(dishArrAdd,dishArrDel);
			    	if(arr.indexOf($("#nav-dishes li.active").attr('id'))!=-1){
			    	if($("#dishid").val()==""){
			    	
			    		var temp = '<div class="dishes-detail-box" onmouseover="delDisplay(this)" id="'+data.tdish.dishid+'" data-dishtype="0" onmouseout="delHidden(this)">'+
			    		'<p class="dishes-detail-name">'+substrControl(name,14)+'</p>'+
			    		'<i class="icon-remove hidden" onclick="delDishesDetail(\''+data.tdish.dishid+'\',\''+data.tdish.title+'\',event)"></i></div>';
			    		$("#dishDetailList").before(temp);
			    		
//			    		$("#nav-dishes .active ").find("span").eq(1).text("("+($(".nav-dishes-tab div ").length-2)+")");
			    		$('#'+data.tdish.dishid).dblclick(function(){
							initDish();
							$("#dishes-detailAdd-dialog").modal("show");
							$("#dishes-detailAdd-dialog").find("div.modal-title").text("编辑菜品");
							viewAndEdit(data.tdish.dishid);
							
						});
			    		selectPrinterDish(data,arr);
			    	}else{
			    		var id=$("#dishid").val();
			    		$("#"+id+" .dishes-detail-name").text(name.replace("\"","“").replace("\"","”"));
//			    		$("#"+id+" .dishes-detail-code").text($("#dishno").val());
			    	}
			    	}else{
			    		if($("#dishid").val()!=""){
			    			var id=$("#dishid").val();
			    			$("#"+id).remove();
			    		}
			    	}
			    	
			    	saveDishUnit(data.tdish.dishid);
			    	
			    	
			    	$("#dishes-detailAdd-dialog").modal("hide");
			    	
			    	
			    },  
			    complete: function (XMLHttpRequest, textStatus) {  
			    } 
		    });
	}
 
function check_same_dishName(operate){

	var title = '';
	var dishid = '';
	if(operate==1){
		title =$("#title").val(); 
		dishid = $("#dishid").val();
	}else if(operate==2){
		title =$("#combotitle").val();
		dishid = $("#combodishid").val();
	}else if(operate==3){
		title =$("#fishpottitle").val(); 
		dishid = $("#fishpotdishid").val();
	}
	/*if(combotitle!=""){
		title = combotitle;
		dishid =combodishid;
	}
	if(fishpottitle!=""){
		title = fishpottitle;
		dishid = fishpotdishid;
	}*/
	var flag=false;
	$.ajax({
		type : "post",
		async : false,
		data:{
			dishid:dishid,
	    	title:title,
		},
		url : global_Path+"/dish/validateDish.json",
		dataType : "json",
		success : function(result) {
			if(result.message=='菜品名称不能重复'){
			
			$("#title_tip").text(result.messageDetail);
			$("#title").focus();
			
			$("#combotitle_tip").text(result.messageDetail);
			$("#combotitle").focus();
			$("#fishpottitle_tip").text(result.messageDetail);
			$("#fishpottitle").focus();
			
			flag=false;
						
			}else{
			flag=true;
			}
			
		}
			
	});
	return flag;
} 
function editTableTypeShow(e){
	$("#dishes-add-dialog").modal("show");
	$("#dishes-add-dialog").find("div.modal-title").text("编辑分类");
	$("#dish-type").val($(e).children(":first").text().trim());
	$("#dish-type-id").val($(e).attr('id'));
}
var dishTitleLength="";
function substrControl(dishTitle,titleLength){
	dishTitleLength="";
	dishTitleLength = getStrLength(dishTitle);
	if(dishTitleLength<=titleLength){
		return dishTitle;
	}
	for(var i=titleLength/2;i<titleLength;i++){
		dishTitleLength = getStrLength(dishTitle.substr(0,i));
		if(dishTitleLength>titleLength)	{
			return dishTitle.substr(0,i-1)+"...";
		}
	}
	
	
}
function closeModal(id){
	$("#"+id).find(".img-close").click();
}
function delDishTasteAndLabel(num,id,itemDesc,e){

	$("#dishTasteType-detailDel-dialog").modal("show");
	$("#dishTasteType-detailDel-dialog").css("z-index","1043");
	$("div.modal-backdrop").css("z-index","1042");
	$("#showDishTasteName").html(itemDesc);
	$("#showDishTasteId").val(id);
	$("#showDishTasteId").attr("title",num);
	stoppro(e);
	
}
function confirmDelDishTasteAndLabel(){
	
	
	$.ajax({
		url:global_Path+"/datadictionary/delDishTaste/"+$("#showDishTasteId").val()+".json",
		type : "post",
		datatype : "json",
		contentType : "application/json; charset=utf-8",

		success : function(result) {
			var flagA = false;
			var flagB = false;
			var id=$("#showDishTasteId").val();
			$("#"+id).remove();
			console.log($("#showDishTasteId").attr("title"));
			if($("#showDishTasteId").attr("title")==1){
				updateDishTagOrder(1);
				TotalDishTasteArrayB.remove($("#showDishTasteName").html());
				if(DishTasteArray.indexOf($("#showDishTasteName").html()) != -1){
					DishTasteArray.remove($("#showDishTasteName").html());
					flagA = true;
				}
				//alert(TotalDishTasteArrayB+"   "+TotalDishTasteArrayB.length);

			}else if($("#showDishTasteId").attr("title")==2){
				updateDishTagOrder(2);
				TotalDishLabelArrayB.remove($("#showDishTasteName").html());
				if(DishLabelArray.indexOf($("#showDishTasteName").html()) != -1){
					DishLabelArray.remove($("#showDishTasteName").html());
					flagB = true;
				}
			
			}
			

			$("#dishTasteType-detailDel-dialog").find(".img-close").click();
			
			$("#dishTasteType-detailDel-dialog").modal("hide");
			
			showAndHidden2(1);
			showAndHidden2(2);
			console.log(flagA);
			if(flagA){
				console.log(DishTasteArray);
				if($("#dishes-tasteAdd-dialog-flag").val()=="0"){
				if(DishTasteArray.length != 0){
					$("#dishes-taste-add").text(DishTasteArray.join(','));
				}else{
					$("#dishes-taste-add").text("");
					$("#dishes-taste-add").append('<i class="icon-plus-sign"></i>');
				}
			}
			if($("#dishes-tasteAdd-dialog-flag").val()=="1"){
				if(DishTasteArray.length != 0){
					$("#fishpotdishes-taste-add").text(DishTasteArray.join(','));
				}else{
					$("#fishpotdishes-taste-add").text("");
					$("#fishpotdishes-taste-add").append('<i class="icon-plus-sign"></i>');
				}
			}

			}
			if(flagB){
				if($("#dishes-labelAdd-dialog-flag").val()=="0"){
					if(DishLabelArray.length != 0){
						$("#dishes-label-add").text(DishLabelArray.join(','));
					}else{
						$("#dishes-label-add").text("");
						$("#dishes-label-add").append('<i class="icon-plus-sign"></i>');
					}
					}
					if($("#dishes-labelAdd-dialog-flag").val()=="2"){
						if(DishLabelArray.length != 0){
							$("#combodishes-label-add").text(DishLabelArray.join(','));
						}else{
							$("#combodishes-label-add").text("");
							$("#combodishes-label-add").append('<i class="icon-plus-sign"></i>');
						}
						}
					if($("#dishes-labelAdd-dialog-flag").val()=="1"){
						if(DishLabelArray.length != 0){
							$("#fishpotdishes-label-add").text(DishLabelArray.join(','));
						}else{
							$("#fishpotdishes-label-add").text("");
							$("#fishpotdishes-label-add").append('<i class="icon-plus-sign"></i>');
						}
					}
			
			}
			
		}
	});
}
function addPrinterDish(list){
	$.ajax({
		type:"post",
		async:false,
		url : global_Path+"/printerManager/addOnePrinterDish.json",
		contentType:'application/json;charset=UTF-8',
		dataType : "json",
		data:JSON.stringify(list), 
		success : function(result) {	
			
			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}
	});
}
function findPrinterDish(){
	$.ajax({
		type:"post",
		async:false,
		url : global_Path+"/printerManager/findPrinterDish.json",
		dataType : "json",
		data:{
    	    status:1,
    	    
		},
		success : function(result) {	
			tbPrinterDetailList=result.tbPrinterDetailList;

		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}
	});
}
function selectPrinterDish(result,columnidList){
	var printeridHave=[];
	var printerHave=[];
	$.each(columnidList,function(j,itemC){
		$.each(tbPrinterDetailList,function(i,item){
			if(item.columnid==itemC&&printeridHave.indexOf(item.printerid) == -1){
				item.dishid=result.tdish.dishid;
				printeridHave.push(item.printerid);
				printerHave.push(item);
			}
		});
		
	});
	if(printerHave!=""){
		addPrinterDish(printerHave);
	}
	
}

function isExistDishMenu(){
	$.post(global_Path+"/menu/getMenuList.json", function(menulist){
		if(menulist.length > 0){
			$("#dishes-menu-ctrl").removeClass("hidden");
		}else{
			$("#dishes-menu-ctrl").addClass("hidden");
		}
	});
}
function dishNumChange(dishArrAdd,dishArrDel){
	
	if(dishArrAdd==""&&dishArrDel==""){
		$("#nav-dishes .active ").find("span").eq(1).text("("+($(".nav-dishes-tab div ").length-2)+")");
	}else{
		$.each(dishArrAdd,function(i,item){
			var count=0;
			countText = $("#"+item).find("span").eq(1).text();
			count = countText.substr(1,countText.length-2);
			count++;
			$("#"+item).find("span").eq(1).text("("+(count)+")");
		});
		$.each(dishArrDel,function(i,item){
			var count=0;
			countText = $("#"+item).find("span").eq(1).text();
			count = countText.substr(1,countText.length-2);
			count--;
			$("#"+item).find("span").eq(1).text("("+(count)+")");
		});
	}
}

function deleteDishInAllColumn(dishid){
	$.ajax({
		url : global_Path + "/dish/findById/"+dishid+".json",
		type : "post",
		datatype : "json",
		contentType : "application/json; charset=utf-8",
		success : function(result) {
			var dishArrAdd=[];
			var  dishArrDel=result.tdishGroup.tdish.columnid.split(',');
			
			dishNumChange(dishArrAdd,dishArrDel);
		}
	});
}

function addEvent_D(event) {      
    event=event || window.event;
    
    var type = event.type;
    if (type == 'DOMMouseScroll' || type == 'mousewheel') {
        event.delta = (event.wheelDelta) ? event.wheelDelta / 120 : -(event.detail || 0) / 3;
    }

    var count = $("#nav-dishes").children("li").length;		 
	if(event.delta >0){
		if(count-flag_prev>10)
		{
			$("#nav-dishes").find("li").eq(flag_prev).css("margin-left","-14%");
			flag_prev++;
		}

	}else{ 
		if(flag_prev>=1){	
			$("#nav-dishes").find("li").eq(flag_prev-1).css("margin-left","0");						
			flag_prev--;
		}
	}

	if(document.all){
    	event.cancelBubble = false;
    	return false;
    }else{
    	event.preventDefault();
    }

}