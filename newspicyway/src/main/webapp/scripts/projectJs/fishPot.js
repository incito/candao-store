var groupfishList=[];
var grouppotList=[];
$(document).ready(function(){
	//valicate通过name来获取并验证信息
	$("#dishes-fishpot-dialog-form").validate({
		submitHandler : function(form) {
			
			var vcheck = true;
			if ($("#fishpotselectDishTypeName").val().trim() == "") {
				$("#fishpotselectDishTypeName_tip").text("必填信息");
				vcheck = false;
			} 
		
			if ($("#fishul li").length== 0) {
				$("#showTypeName").text("请添加鱼");
				$("#notNullDialog").css("z-index","1042");
				$("div.modal-backdrop").css("z-index","1041");
				$("#notNullDialog").modal("show");
				window.setTimeout(function(){
				$("#notNullDialog").modal("hide");
				$("div.modal-backdrop").css("z-index","1030");
				}, 1000);
				
				vcheck = false;
			} 
			if ($("#potul li").length== 0) {
				$("#showTypeName").text("请添加鱼锅");
				$("#notNullDialog").css("z-index","1042");
				$("div.modal-backdrop").css("z-index","1041");
				$("#notNullDialog").modal("show");
				window.setTimeout(function(){
				$("#notNullDialog").modal("hide");
				$("div.modal-backdrop").css("z-index","1030");
				}, 1000);
				
				vcheck = false;
			}
			if (vcheck) {
				if(check_same_dishName(3)){
				savefishpotDish();
				}else{
				}
			}
		}
});
	$.ajaxSetup ({
	    cache: false //关闭AJAX相应的缓存
	});
//	$("#mutfishpotdishes-detailDel-dialog").modal("show");
	$(".btn-cancel").click(function(){
		$("div.modal-backdrop").css("z-index", "1030");
	});
	$(".btn-save").click(function(){
		$("div.modal-backdrop").css("z-index", "1030");
	});
	/**
	 * 添加鱼锅
	 */
	$("#dishes-other-tab2").click(function(){
		initfishpotdish();
		$(".dishes-other-right").addClass("hidden");
		$("#dishes-hot-dialog").modal("show");
	
		$.ajax({
			url : global_Path + "/dish/getInitData.json",
			type : "post",
			datatype : "json",
			contentType : "application/json; charset=utf-8",
			success : function(result) {
				
				$.each(result.listType,function(index,item){
					if($("#nav-dishes .active").attr("id")==item.id){
						
						$("#fishpotdishTypeSelect").append('<span class="select-content-detail"><label style="font-weight:normal;">'+item.itemdesc+'</label><input type="checkbox" onclick="selectFishpotDishType()" checked=true id="'+item.id+'"></span><br/>');
					}else{
						$("#fishpotdishTypeSelect").append('<span class="select-content-detail"><label style="font-weight:normal;">'+item.itemdesc+'</label><input type="checkbox" onclick="selectFishpotDishType()" id="'+item.id+'"></span><br/>');
					}
//						$("#fishpotdishTypeSelect").append('<span class="select-content-detail"><label> style="font-weight:normal;'+item.itemdesc+'</label><input type="checkbox" onclick="selectFishpotDishType()" id="'+item.id+'"></span><br/>');
				});
				var text = $("#nav-dishes .active").children(":first").text();
				//val()中不能放入包含标签的text()
				$("#fishpotselectDishTypeName").val(text);
				$("#fishpotcolumnid").val($("#nav-dishes .active").attr("id"));
				
				
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert(errorThrown);
			}

		});

	});
	$("#fishpotdishTypeShow").click(function(){
		//toggleClass() 对设置或移除被选元素的一个或多个类进行切换。
		$("#fishpotdishTypeSelect").toggleClass("hidden");
	});
	/*菜品标签弹出框显示*/
	$("#fishpotdishes-label-add").click(function(){
		
//		$("div.modal-backdrop").css("z-index","1041");
		initDishTaste(2);
		$("#dishes-labelAdd-dialog").modal("show");
		$("#dishes-labelAdd-dialog-flag").val("1");//火锅中的菜品标签标示
		// 二次弹框遮罩效果0305
		$("#dishes-labelAdd-dialog").css("z-index","1042");
		$("div.modal-backdrop").css("z-index","1041");
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
	/*菜品口味弹出框显示*/
	$("#fishpotdishes-taste-add").click(function(){
		initDishTaste(1);
		$("#dishes-tasteAdd-dialog").modal("show");
		$("#dishes-tasteAdd-dialog-flag").val("1");//火锅中的菜品标签标示
		// 二次弹框遮罩效果0305
		$("#dishes-tasteAdd-dialog").css("z-index","1042");
		$("div.modal-backdrop").css("z-index","1041");
		
		$.ajax({
			url:global_Path+"/datadictionary/getDatasByType/SPECIAL.json",
			type : "post",
			datatype : "json",
			contentType : "application/json; charset=utf-8",
			success : function(result) {
				if(result.length!=0){
					TotalDishTasteArrayB=[];
				TotalDishTasteArray=result;
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
 	$("#fishpotintroduction").on('keyup', function() {
 	    var len = this.value.length;
 	     $("#fishpotcount").html(maxCount-len);
 	});
});
function selectFishpotDishType(){
	var text = '';
	var id='';
	$("#fishpotdishTypeSelect").find("input[type='checkbox']").each(function(){
		if($(this).is(":checked"))
		{
			text = text+","+$(this).parent().text();
			id = id+","+$(this).attr('id');
		}
	});
	text = text.substr(1,text.length-1);
	id = id.substr(1,id.length-1);
	$("#fishpotselectDishTypeName").val(text);
	$("#fishpotcolumnid").val(id);
	$("#fishpotdishTypeSelect").toggleClass("hidden");
}
function initfishpotdish(){
	$("#title").val("");
	$("#combotitle").val("");
	$("#fishpottitle").val("");
	$("#dishes-fishpot-dialog-form :text,#add-form :hidden:not('option')").val("");
	$("#fishpotdishes-label-add").text("");
	$("#fishpotdishes-label-add").append('<i class="icon-plus-sign"></i>');
	$("#fishpotdishes-taste-add").text("");
	$("#fishpotdishes-taste-add").append('<i class="icon-plus-sign"></i>');
	$("#uploadpic3").attr("src","../images/combo-fileup.png");
	$("#fishpotdishTypeShow input").val("");
	$("#fishpotdishTypeSelect .select-content-detail").remove();
	$("#fishpotdishTypeSelect br").remove();
	$("#fishpotintroduction").val("");
	$("#selectFishDetail p").show();
	$("#selectHotDetail p").show();
	$("#potul li").remove();
	$("#fishul li").remove();
	 DishTasteArray = [];//已选择的菜品口味
	 TotalDishTasteArray = [];//总的菜品口味
	 TotalDishTasteArrayB=[];
	 DishLabelArray = [];//已选择的菜品标签
	 TotalDishLabelArray = [];//总的菜品标签
	 TotalDishLabelArrayB = [];
	 groupfishList=[];
	 grouppotList=[];
	 $("#fishpotdishno").val("");
	 $("#fishpotCreateTime").val("");
	 $("#fishpotdishid").val("");
	 $(".error").text("");
	 $("#fishpottitle").removeClass("error");
	 $("#uploadpic3").attr("src","../images/upload-img.png");
	 imagePath="";
	 dishArrBefore=[];
	 $("#recommendFish").prop("checked","");
}
//0 添加鱼  1添加锅
function addfishandhot(flag){
	
	$("#fishpotdish-select-dialog").css("z-index","1045");
	$("div.modal-backdrop").css("z-index","1041");
	$("#fishpotdish-select-dialog").modal("show");
	$("#fishpotdish-select-dialog").load(global_Path+"/dish/loadfishpotDishSelect",function(){
		$("#flag").val(flag);
		
		
	});
}
function buildfishpotDishListInView(flag){
	 var columns=$("#fishpotdish-select-dialog #fishpotaccordion .panel").find("img");
	 
	 if(columns.length){
		 //在循环外部清空，初始化
		 if(flag==0){
			 groupfishList=[];
		 }else{
			 grouppotList=[];
		 }
		 $.each(columns,function(index,item){
			 if($(this).attr("alt")=="1"||$(this).attr("alt")=="2"){
				 var group={};
				 var columnid=$(this).parent().parent().attr("id").substr(12);
				 var columnname=$(this).parent().parent().find("span:first").text();
				 group.columnid=columnid;
				 group.columnname=columnname;
				 group.startnum=0;
				 group.endnum=0;
				 var groupDetailList=[];
				 
				 $.each($("#fishpotcollapseFour_"+columnid).find("input[type=checkbox]:checked"),function(){
					 
					 var groupDetai={};
					 groupDetai.contactdishid=$(this).val();
					 groupDetai.contactdishname=$(this).parent().text();
					 groupDetai.dishunitid=$(this).attr("unit");
					 groupDetai.unitflag=$(this).attr("unitflag");
					 groupDetai.dishtype=$(this).attr("dishtype");
					 groupDetai.price=$(this).attr("price");
					 groupDetai.dishnum=1;
					 groupDetai.status=0;
					 groupDetai.ispot=flag;
//					 if(!groupDetailList.indexOf($(this).val())==-1){
						 groupDetailList.push(groupDetai);
						 
//					 }
					 
//					 if(flag==0){
//						 groupDetailfishList.push(groupDetai);
//					 }else{
//						 groupDetailpotList.push(groupDetai);
//					 }
				 });
//				 console.log(JSON.stringify(groupDetailList));
				 group.groupDetailList=groupDetailList;
				 if(flag==0){
					 groupfishList.push(group);
				 }else{
					 grouppotList.push(group);
				 }
			 }
		 });
	 }
	 showfishpotDetail();
}
function showfishpotDetail(){
	$("#selectFishDetail p").show();
	$("#selectHotDetail p").show();
	$("#potul li").remove();
	$("#fishul li").remove();
	var fishLiIndex = 1;
	if(groupfishList!=null&&groupfishList.length>0){
		
		var html="";
		$("#selectFishDetail p").hide();
		
		$.each(groupfishList,function(fishLiNum,obj){
			 $.each(obj.groupDetailList,function(index,item){
				 
				 html+="<li>"+item.contactdishname+"   "+item.price+"元/"+item.dishunitid+"</li>";
				 console.log(fishLiIndex);
				 if(fishLiIndex==4){
					 return false;
				 }
				 fishLiIndex++;
			 });
			 if(fishLiIndex==4){
				 return false;
			 }
			 
		});
		 $("#fishul").append(html);
	}else{
		
		$("#fishul li").remove();
		$("#selectFishDetail p").css({"display":"block"});
		
	}
	if(grouppotList!=null&&grouppotList.length>0){
		var html="";
		$("#selectHotDetail p").hide();
		$.each(grouppotList,function(i,obj){
			$.each(obj.groupDetailList,function(index,item){
				html+="<li>"+item.contactdishname+"   "+item.price+"元/"+item.dishunitid+"</li>";
			});
		});
		$("#potul").append(html);
	}else{
		$("#potul li").remove();
		$("#selectHotDetail p").css({"display":"block"});
	
	}
}
function delfishandhot(flag){
//	if(flag==1){
//		$("#mutfishpotdishes-detailDel-dialog").css("z-index","1042");
//	}else{
//		
//		$("#fishpotdishes-detailDel-dialog").css("z-index","1042");
//	}
	

	if(flag==1&&grouppotList.length>0){
		$("#fishpotdishes-detailDel-dialog").css("z-index","1042");
		$("#fishpotflag").val(flag);
		$("#fishpotdishes-detailDel-dialog").modal("show");
		$("#showpotName").text(grouppotList[0].groupDetailList[0].contactdishname);
	}
	if(flag==0&&groupfishList.length>0){
		
		var num=0;
		$.each(groupfishList,function(i,obj){
			num+=obj.groupDetailList.length;
		});
		if(num==1){
			$("#fishpotdishes-detailDel-dialog").css("z-index","1042");
			$("#fishpotflag").val(flag);
			$("#fishpotdishes-detailDel-dialog").modal("show");
			$("#showpotName").text(groupfishList[0].groupDetailList[0].contactdishname);
		}else{
			$("#mutfishpotdishes-detailDel-dialog").css("z-index","1042");
			$("#mutfishpotdishes-detailDel-dialog").modal("show");
			$(".fishpot-dish-title-del li").remove();
			var html="";
			$.each(groupfishList,function(i,obj){
				$.each(obj.groupDetailList,function(index,item){
					html+="<li><span>"+(index+1)+"、"+item.contactdishname+item.price+"/"+item.dishunitid+"</span>" +
							" <button  onclick='delthis(this)' columnid='"+obj.columnid+"' unit='"+item.dishunitid+"' class='button-del' type='button' id='"+item.contactdishid+"'>删除</button></li>";
				});
			});
			$(".fishpot-dish-title-del").append(html);
		}
		
	}
	$("div.modal-backdrop").css("z-index","1041");
}
function confirmDelfishpot(multiflag){
	$("div.modal-backdrop").css("z-index", "1030");
	if(multiflag==1){
		var flag=$("#fishpotflag").val();
		if(flag==0){
			groupfishList=[];
		}else{
			grouppotList=[];
		}
		showfishpotDetail();
		$("#fishpotdishes-detailDel-dialog").modal("hide");	
	}
	if(multiflag==2){
		var groupDetailfishListtemp=[];
		$.each(groupfishList,function(ind,ite){
			var groupDetailListTempDel=[];
			$.each(ite.groupDetailList,function(index,item){
				var flag=true;
				$.each($(".fishpot-dish-title-del .delchecked"),function(i,obj){
					if(item.contactdishid==obj.id&&$(obj).attr("unit")==item.dishunitid&&$(obj).attr("columnid")==ite.columnid){
						flag=false;
						return false;
					}
				});
				if(flag){
					groupDetailListTempDel.push(item);
					groupDetailfishListtemp.push(item);
				}
			});
			ite.groupDetailList=groupDetailListTempDel;
		});
		groupDetailfishList=groupDetailfishListtemp;
		showfishpotDetail();
		var tempLi = $("#fishul li").length;
		if(tempLi>0){
			$("#selectFishDetail p").hide();
		}else{
			$("#selectFishDetail p").show();
		}
		$(".fishpot-dish-title-del li").remove();
		$("#mutfishpotdishes-detailDel-dialog").modal("hide");		
	}
	
}
function delthis(e){
	$(e).toggleClass("delchecked");
}
function savefishpotDish(){
	  var name = $("#fishpottitle").val();
	  $.ajaxFileUpload({
		    fileElementId: ['main_img3'],  
		    url: global_Path+'/combodish/savefishpot',  
		    dataType: 'json',
		    contentType:'application/json;charset=UTF-8',
		    data: { json:getFishpotJson() },
		    success: function (data, textStatus) {
//		    	initfishpotdish();
		    	$("#dishes-hot-dialog").modal('hide');
		    	var arr = $("#fishpotcolumnid").val().split(',');
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
		    	
		    	if(arr.indexOf($("li.active").attr('id'))!=-1){
		    	if($("#fishpotdishid").val()==""){
		    		var temp = '<div class="dishes-detail-box" onmouseover="delDisplay(this)" id="'+data.tdish.dishid+'" data-dishtype="1" onmouseout="delHidden(this)">'+
		    		'<p class="dishes-detail-name">'+name+'</p>'+
		    		'<i class="icon-remove hidden" onclick="delDishesDetail(\''+data.tdish.dishid+'\',\''+data.tdish.title+'\',event)"></i></div>';
		    		$("#dishDetailList").before(temp);
		    		$('#'+data.tdish.dishid).dblclick(function(){
		    			initfishpotdish();
						$("#dishes-hot-dialog").modal("show");
						$("#dishes-hot-dialog").find(".dishes-combo-title").text("编辑鱼锅");
						viewAndEditfishpotDish($(this).attr('id'));
					});
//		    		$("#nav-dishes .active ").find("span").eq(1).text("("+($(".nav-dishes-tab div ").length-1)+")");
		    		selectPrinterDish(data,arr);
		    	}else{
		    		var id=$("#fishpotdishid").val();
		    		$("#"+id+" .dishes-detail-name").text(name.replace("\"","“").replace("\"","”"));
		    	}
		    	}else{
		    		if($("#fishpotdishid").val()!=""){
		    			var id=$("#fishpotdishid").val();
		    			$("#"+id).remove();
		    		}
		    	}
		    },  
		    complete: function (XMLHttpRequest, textStatus) {  
		    } 
	    });
}
function getFishpotJson(){
	 var recommendFish=checkStatus('recommendFish');
	 var image;

	  if($("#main_img3").val()==''){
	    	image=imagePath;
	    }else{
	    	image="";
	  }
	var TcomboDishGroupList={};
	var dish={};
	
	dish.dishid=$("#fishpotdishid").val();
	if($("#fishpotdishno").val()==""){
		dish.dishno="1";
	}else{
		dish.dishno = $("#fishpotdishno").val();
	}
	dish.recommend = recommendFish;
	dish.title=$("#fishpottitle").val().replace("\"","“").replace("\"","”");
	
	dish.dishtype='1';
	dish.abbrdesc=DishLabelArray.join(',');
	dish.columnid=$("#fishpotcolumnid").val();
	dish.introduction=$("#fishpotintroduction").val().replace("\"","“").replace("\"","”");
	dish.imagetitle=DishTasteArray.join(',');
	dish.image=image;
	dish.headsort=1;
	dish.createtime = $("#fishpotCreateTime").val();
	TcomboDishGroupList.dish=dish;
	TcomboDishGroupList.groupList=groupfishList.concat(grouppotList);
	console.log(JSON.stringify(TcomboDishGroupList));
	return JSON.stringify(TcomboDishGroupList);
}
/* 双击修改查看套餐*/
function viewAndEditfishpotDish(id){
	$.ajax({
		url : global_Path + "/combodish/findById/"+id+".json",
		type : "post",
		datatype : "json",
		contentType : "application/json; charset=utf-8",
		success : function(result) {
			$("#fishpotdishid").val(result.tdish.dishid);				
			$("#fishpotdishno").val(result.tdish.dishno);				
			$("#fishpottitle").val(result.tdish.title);
			$("#fishpotintroduction").val(result.tdish.introduction);
			$("#fishpotcount").html(maxCount-result.tdish.introduction.length);
			$("#fishpotCreateTime").val(result.tdish.createtime);
			$.each(result.listType,function(index,item){
				$("#fishpotdishTypeSelect").append('<span class="select-content-detail"><label>'+item.itemdesc+'</label><input type="checkbox" onclick="selectFishpotDishType()" id="'+item.id+'"></span><br/>');
			});
			var  columnidList=result.tdish.columnid.split(',');
			dishArrBefore = columnidList;
			var text='';
			var id='';
			if(!jQuery.isEmptyObject(columnidList)){
			$("#fishpotdishTypeSelect").find("input[type='checkbox']").each(function(){
				if(columnidList.indexOf($(this).attr('id'))!=-1){
					text = text+","+$(this).prev("label").html();
					id = id+","+$(this).attr('id');
					$(this).attr("checked",true);
				}
			});
			}
			text = text.substr(1,text.length-1);
			id = id.substr(1,id.length-1);
			$("#fishpotselectDishTypeName").val(text);
			$("#fishpotcolumnid").val(id);
			
			if(result.tdish.imagetitle!=""){
				$("#fishpotdishes-taste-add").text(result.tdish.imagetitle);
				DishTasteArray=result.tdish.imagetitle.split(",");
			}
			if(result.tdish.abbrdesc!=""){
				$("#fishpotdishes-label-add").text(result.tdish.abbrdesc);
				DishLabelArray=result.tdish.abbrdesc.split(",");
			}
			if(result.tdish.image!=""&&typeof(result.tdish.image) != "undefined"){
				imagePath=result.tdish.image;
				$("#uploadpic3").attr("src",img_Path + imagePath);
			}
			if(result.tdish.recommend!=""&&typeof(result.tdish.recommend) != "undefined"&&result.tdish.recommend=="1"){
				  $("#recommendFish").prop("checked","checked");
			}else{
			  $("#recommendFish").prop("checked",""); 
			}
			
			groupList=result.groupList;
			groupfishList=[];
			grouppotList=[];
			$.each(groupList,function(index,item){
				if(item!=null&&item.groupDetailList.length>0){
					if(item.groupDetailList[0].ispot==0){
						groupfishList.push(item);
					}else{
						grouppotList.push(item);
					}
				}
			});
			showfishpotDetail();
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}

	});
} 
