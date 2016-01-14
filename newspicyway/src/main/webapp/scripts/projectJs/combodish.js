var groupList=[];
$(document).ready(function(){
	$("img.img-close").click(function(){
		$("div.modal-backdrop").css("z-index","1030");
	});
	$(".fishpot-addfish").hover(function(){
		 //这里是放在上面
		var tempLi = $("#fishul li").length;
		if(tempLi>0){
			$(".fishpot-addfish div").css("display","block");
            $("#fishidUl li").eq(0).css("display","none");
            $("#fishidUl li").eq(1).css("display","block");
            $("#fishidUl li").eq(2).css("display","block");
		}else{
			$(".fishpot-addfish div").css("display","none");
			$("#fishidUl li").eq(1).css("display","none");
			$("#fishidUl li").eq(0).css("display","block");
	        $("#fishidUl li").eq(2).css("display","block");
		}
	    },function(){
	    	//这里是鼠标移走
	    	$(".fishpot-addfish div").css("display","none");
	});
	$(".fishpot-addhot").hover(function(){
		 //这里是放在上面
		var tempLi = $("#potul li").length;
		if(tempLi>0){
			$(".fishpot-addhot div").css("display","block");
			$("#potopidUl li").eq(0).css("display","none");
			$("#potopidUl li").eq(1).css("display","block");
			$("#potopidUl li").eq(2).css("display","block");
		}else{
			$(".fishpot-addhot div").css("display","none");
			$("#potopidUl li").eq(1).css("display","none");
			$("#potopidUl li").eq(0).css("display","block");
			$("#potopidUl li").eq(2).css("display","block");
		}
	    },function(){
	    	//这里是鼠标移走
	    	$(".fishpot-addhot div").css("display","none");
	});
	//valicate通过name来获取并验证信息
	$("#dishes-combo-dialog-form").validate({
		submitHandler : function(form) {
			
			var vcheck = true;

			if ($("#comboselectDishTypeName").val().trim() == "") {
				$("#comboselectDishTypeName_tip").text("必填信息");
				vcheck = false;
			} 
			
			if ($("#initComboDishDetail").css("display") != "none") {
				$("#showTypeName").text("请添加套餐明细");
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
				if(check_same_dishName(2)){
				saveComboDish();
				}else{
				}
			}
		}
});
	$.ajaxSetup ({
	    cache: false //关闭AJAX相应的缓存
	});
	/**
	 * 添加套餐
	 */
	$("#dishes-other-tab1").click(function(){
		$(".dishes-other-right").addClass("hidden");
		$("#dishes-combo-dialog").modal("show");
		var selectDish = $("#selectDishShow li").length;  
			if(selectDish>0){
				$("#taocanDetailid").css("display","inline-block");
			}else{
				$("#taocanDetailid").css("display","none");
	    }
		$.ajax({
			url : global_Path + "/dish/getInitData.json",
			type : "post",
			datatype : "json",
			contentType : "application/json; charset=utf-8",
			success : function(result) {
				$.each(result.listType,function(index,item){
					if($("#nav-dishes .active").attr("id")==item.id){
						
//						$("#dishTypeSelect").append('<span class="select-content-detail"><label style="font-weight:normal;">'+item.itemdesc+'</label><input type="checkbox" onclick="selectDishType()" checked=true id="'+item.id+'"></span><br/>');
						$("#combodishTypeSelect").append('<span class="select-content-detail"><label style="font-weight:normal;">'+item.itemdesc+'</label><input type="checkbox" onclick="selectComboDishType()" checked=true id="'+item.id+'"></span><br/>');
					}else{
//						$("#dishTypeSelect").append('<span class="select-content-detail"><label style="font-weight:normal;">'+item.itemdesc+'</label><input type="checkbox" onclick="selectDishType()" id="'+item.id+'"></span><br/>');
						$("#combodishTypeSelect").append('<span class="select-content-detail"><label style="font-weight:normal;">'+item.itemdesc+'</label><input type="checkbox" onclick="selectComboDishType()" id="'+item.id+'"></span><br/>');
					}
						
				});
				
				var text = $("#nav-dishes .active").children(":first").text();
				//val()中不能放入包含标签的text()
				$("#comboselectDishTypeName").val(text);
				$("#combocolumnid").val($("#nav-dishes .active").attr("id"));
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert(errorThrown);
			}

		});
		initcombodish();
			
	});
	$("#combodishTypeShow").click(function(){
		//toggleClass() 对设置或移除被选元素的一个或多个类进行切换。
		$("#combodishTypeSelect").toggleClass("hidden");
	});
	/*菜品标签弹出框显示*/
	$("#combodishes-label-add").click(function(){
		$("#dishes-labelAdd-dialog").css("z-index","1042");
//		$("div.modal-backdrop").css("z-index","1041");
		initDishTaste(2);
		$("#dishes-labelAdd-dialog").modal("show");
		$("#dishes-labelAdd-dialog-flag").val("2");//套餐中的菜品标签标示
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
 	$("#combointroduction").on('keyup', function() {
 	    var len = this.value.length;
 	     $("#combocount").html(maxCount-len);
 	});
 	/*菜谱入口*/
	 $(".menu-enter img").mouseover(function(){
	 	var src = $(this).attr("src");
		if(src.match("active") == null){
		src =  src.substr(0,src.lastIndexOf('.'))+'-active.png';
		$(this).attr("src",src);}

	 });
	 $(".menu-enter img").mouseout(function(){
		var src = $(this).attr("src");
		if(src.match("active")){
			src =  src.substr(0,src.lastIndexOf('-'))+'.png';
			$(this).attr("src",src);
		}

	 });
	 $(".menu-enter img").click(function(){
		 $(parent.document.all("detail")).attr("src",global_Path+"/menu/createmenu");
//		 $(parent.document.all("detail")).attr("src",global_Path+"/menu/viewmenu?menuId=null");
		 $("#allSearch").css("visibility","hidden");
	 });
});
//初始化套餐
function initcombodish(){
	$("#title").val("");
	$("#combotitle").val("");
	$("#fishpottitle").val("");
	$("#dishes-combo-dialog-form :text,#add-form :hidden:not('option')").val("");
	$("#combodishes-label-add").text("");
	$("#combodishes-label-add").append('<i class="icon-plus-sign"></i>');
	$("#uploadpic").attr("src","../images/combo-fileup.png");
	$("#combodishTypeShow input").val("");
	$("#combodishTypeSelect .select-content-detail").remove();
	$("#combodishTypeSelect br").remove();
	$("#combointroduction").val("");
	$("#initComboDishDetail").show();
	$("#selectDishShow").html('');
	groupList=[];
	$("#comboleft").hide();
	$("#comboright").hide();
	 DishTasteArray = [];//已选择的菜品口味
	 TotalDishTasteArray = [];//总的菜品口味
	 TotalDishTasteArrayB=[];
	 DishLabelArray = [];//已选择的菜品标签
	 TotalDishLabelArray = [];//总的菜品标签
	 TotalDishLabelArrayB = [];
	 $("#combodishid").val("");
	 $("#dishid").val("");
	 $("#combodishno").val("");
	 $("#combodishCreateTime").val("");
	 $(".error").text("");
	 $("#combotitle").removeClass("error");
	 $("#comboprice").removeClass("error");
	 $("#uploadpic2").attr("src","../images/upload-img.png");
	 imagePath="";
	 dishArrBefore=[];
	 $("#recommendCombo").prop("checked","");
}
function selectComboDishType(){
	var text = '';
	var id='';
	$("#combodishTypeSelect").find("input[type='checkbox']").each(function(){
		if($(this).is(":checked"))
		{
			text = text+","+$(this).parent().text();
			id = id+","+$(this).attr('id');
		}
	});
	text = text.substr(1,text.length-1);
	id = id.substr(1,id.length-1);
	$("#comboselectDishTypeName").val(text);
	$("#combocolumnid").val(id);
	$("#combodishTypeSelect").toggleClass("hidden");
}
/**
 * 点击图片触发input事件，选择图片
 */
function combotempClick(id){
	$("#main_img"+id).click();
}
/**
 * 展示缩略图
 */
function comboshowpic(id){
 var strsrc=getObjectURL(document.getElementById("main_img"+id).files[0]);
 if(strsrc!=null){
	 $("#uploadpic"+id).attr("src",strsrc);
	 judgePicType(id);
 }
}
/**
 * 判断图片是否正确
 */
var comboallowExt = ['jpg', 'gif', 'bmp', 'png', 'jpeg'];
function judgePicType(id){
	var name=$("#main_img"+id).val();
	var ext = name.substr(name.lastIndexOf(".")+1, name.length);
	 if(comboallowExt.indexOf(ext) == -1){
		 alert("图片格式错误！");
	 }
}
function showcombodishselect(flag){
	$("#combodish-select-dialog").css("z-index","1045");
	$("div.modal-backdrop").css("z-index","1041");
//	 $("div.modal-backdrop").css("z-index", "1030");

	$("#combodish-select-dialog").modal("show");

	$("#combodish-select-dialog").load(global_Path+"/dish/loadcomboDishSelect",function(){
		if(flag==1){
			$("#combodish-select-dialog #editcombodishtitle").text("编辑");
		}else{
			$("#combodish-select-dialog #editcombodishtitle").text("添加");
		}
	});
	
	
}
function buildComboDishListInView(){
	groupList=[];
	 var columns=$("#combodish-select-dialog #comboaccordion .panel").find("img");
	 if(columns.length){
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
				 $.each($("#combocollapseFour_"+columnid).find("input[type=checkbox]:checked"),function(){
					 var groupDetai={};
					 groupDetai.contactdishid=$(this).val();
					 groupDetai.contactdishname=$(this).parent().find("input").attr("data-title");
					 groupDetai.dishunitid=$(this).attr("unit");
					 groupDetai.unitflag=$(this).attr("unitflag");
					 groupDetai.dishtype=$(this).attr("dishtype");
					 groupDetai.price=$(this).attr("price");
					 groupDetai.dishnum=1;
					 groupDetai.status=0;
//					 groupDetai.columnid=columnid;
					 if($(this).attr("dishtype")==1){
						 groupDetai.list=getfishpotDetail($(this).val());
					 }
					 groupDetailList.push(groupDetai);
				 });
				 group.groupDetailList=groupDetailList;
				 groupList.push(group);
				 
			 }
		 });
//		 console.log(JSON.stringify(groupList));
	 }
	 $("#initComboDishDetail").hide();
	 showdetail();
	 console.log("groupList:"+JSON.stringify(groupList));
}
function getfishpotDetail(id){
	var list=[];
	$.ajax({
		url : global_Path + "/combodish/findfishpotDetailById/"+id+".json",
		type : "post",
		datatype : "json",
		contentType : "application/json; charset=utf-8",
		success : function(result) {
			$.each(result,function(index,item){
				var groupDetai={};
				 groupDetai.contactdishid=item.contactdishid;
				 groupDetai.contactdishname=item.contactdishname;
				 groupDetai.dishunitid=item.dishunitid;
				 groupDetai.unitflag=item.unitflag;
				 groupDetai.dishtype=item.dishtype;
				 groupDetai.price=item.price;
				 groupDetai.dishnum=1;
				 groupDetai.status=0;
				 groupDetai.ispot=item.ispot;
				 list.push(groupDetai);
			});
		}});
	return list;
}
function showdetail(){
	 var html="";
	 $.each(groupList,function(index,object){
		 html+=   "<div class='dishes-comboList' style='margin-left: 4px;' id='combo-"+object.columnid+"'>"+
				"<div class='dishes-comboList-title'>"+object.columnname+"("+object.groupDetailList.length+")</div>"+
				"<ul class='dishes-comboList-content'>";
		 var j=0;
		 $.each(object.groupDetailList,function(i,item){
			 
			 //class="dishes-conboList-stip"
			 if(item.status=='1'&&object.startnum!=0&&object.endnum!=0){
				
				
				 html+="<li class='dishes-conboList-stip'>"+(j+1)+".<span title="+item.contactdishname+">"+substrControl(item.contactdishname,10)+"</span>" ;
				 html+="<span class='pull-right dishes-conboList-tip' style='font-size:10px;'>"+object.startnum+"选"+object.endnum+"</span></li>";
				 j++;
			 }
		 });
		 $.each(object.groupDetailList,function(i,item){
			 
			 //class="dishes-conboList-stip"
			 if(!(item.status=='1'&&object.startnum!=0&&object.endnum!=0)){
				
				 html+="<li>"+(j+1)+".<span title="+item.contactdishname+">"+substrControl(item.contactdishname,10)+"</span>" ;
				 html+="<span class='pull-right'>"+item.dishnum+"</span></li>";
				 j++;
			 }
		 });
			html+="</ul>"+
				"<div class='dishes-comboList-bottom' onclick='modifyrule(this)' style='cursor: pointer;' " +
				"id='rule-"+object.columnid+"' alt='"+object.columnname+"'><span><img alt='0' src='"+global_Path+ "/images/set.png' panelCheckedStatus=''>设置规则</span></div>"+
				"</div>";
	 });
	 $("#selectDishShow").html(html);
	 if(groupList.length>4){
		 $("#comboleft").show();
		 $("#comboright").show();
	 }else{
		 $("#comboleft").hide();
		 $("#comboright").hide();
	 }
	 hoverEventScroll('ul.dishes-comboList-content');
	 var selectDish = $("#selectDishShow li").length;   
		if(selectDish>0){
			$("#taocanDetailid").css("display","inline-block");
		}else{
			$("#taocanDetailid").css("display","none");
		}
		
		
}

/**
 * 设置规则
 * @param event
 */
function modifyrule(event){
	$("#dishes-comboEdit-dialog").css("z-index","1042");
	$("div.modal-backdrop").css("z-index","1041");
	$("#dishes-comboEdit-dialog").modal('show');
	var columnid=$(event).attr('id').substr(5);
	$("#dishes-comboEdit-dialog #modify-columnname").text($(event).attr('alt'));
	$("#dishes-comboEdit-dialog #modify-columnid").val(columnid);
	showmodal(columnid);
	console.log(JSON.stringify(groupList));
}
function showmodal(columnid){
	$("#dishes-comboEdit-box-list-selected").html('');
	$("#dishes-comboEdit-box-list-unselected").html('');
	$(".dishes-combined").hide();
	$("#optionalbutton button").remove();
	$("#startnum").text('');
	$("#endnum").text('');
	$.each(groupList,function(i,group){
		if(group.columnid==columnid){
			$.each(group.groupDetailList,function(index,groupDetai){
				var html="";
				if(groupDetai.status=='0'||group.startnum==0||group.endnum==0){//必选的几个菜
					if(groupDetai.dishtype=='0'){//单品
						html=singledishHtml(index,groupDetai.contactdishid,groupDetai.contactdishname,groupDetai.dishunitid,groupDetai.price,groupDetai.dishnum,groupDetai.dishtype,0);
					}
					if(groupDetai.dishtype=='1'){//火锅
						html=combodishHtml(index,groupDetai,0);
					}
					$("#dishes-comboEdit-box-list-unselected").append(html);
				}else{//可供选择的几个菜
					if(groupDetai.dishtype=='0'){//单品
						html=singledishHtml(index,groupDetai.contactdishid,groupDetai.contactdishname,groupDetai.dishunitid,groupDetai.price,groupDetai.dishnum,groupDetai.dishtype,groupDetai.status);
					}
					if(groupDetai.dishtype=='1'){//火锅
						html=combodishHtml(index,groupDetai,1);
					}
					$("#dishes-comboEdit-box-list-selected").append(html);
				}
			});
			if(group.startnum!=0&&group.endnum!=0){
				$(".dishes-combined").show();
				$("#startnum").text(Numeric(group.startnum));
				$("#endnum").text(Numeric(group.endnum));
				$("#dishes-comboEdit-box-list-unselected .dishes-comboEdit-box").removeAttr("onclick");
				$("#dishes-comboEdit-box-list-selected .dishes-comboEdit-box").removeAttr("onclick");
			}
		}
	});
}
//var temClickArr=[];//暂时保存单击的菜皮id
//array.splice(start,delCount)
var selectCombodish=function selectCombodish(e){
	$(e).toggleClass("dishes-checked");
	var temClickArr=$("#dishes-comboEdit-box-list-unselected .dishes-checked");
	$("#optionalbutton button").remove();
	if(temClickArr.length>1){
	for(var i=1;i<temClickArr.length;i++){
		$("#optionalbutton").append("<button type='button' startnum='"+temClickArr.length+"' endnum='"+i+"' onclick='selectgroup(this)' class='btn btn-comboS'>"+Numeric(temClickArr.length)+"选"+Numeric(i)+"</button>");
	}
	}
};
var num =[ "零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十","十一","十二","十三","十四","十五","十六","十七","十八","十九","二十" ];
function Numeric(index)
{
    return num[index];
}
/**
 * 选择了几选几 onclick事件
 */
function selectgroup(e){
	$(".dishes-combined").show();
	var temClickArr=$("#dishes-comboEdit-box-list-unselected .dishes-checked");
	$.each(temClickArr,function(index,item){
		$("#dishes-comboEdit-box-list-selected").append($(this).clone());
		$(this).remove();
	});
	$("#optionalbutton button").remove();
	$("#dishes-comboEdit-box-list-unselected .dishes-comboEdit-box").removeAttr("onclick");
	$("#dishes-comboEdit-box-list-selected .dishes-comboEdit-box").removeAttr("onclick");
	$("#startnum").text(Numeric($(e).attr('startnum')));
	$("#endnum").text(Numeric($(e).attr('endnum')));
}
/**
 * 重新组合
 */
function restComboDishGroup(){
	var columnid=$("#dishes-comboEdit-dialog #modify-columnid").val();
	$("#dishes-comboEdit-box-list-selected").html('');
	$("#dishes-comboEdit-box-list-unselected").html('');
	$(".dishes-combined").hide();
	$("#startnum").text('');
	$("#endnum").text('');
	$.each(groupList,function(i,group){
		if(group.columnid==columnid){
			group.startnum=0;
			group.endnum=0;
			$.each(group.groupDetailList,function(index,groupDetai){
				var html='';
					if(groupDetai.dishtype=='0'){//单品
						html=singledishHtml(index,groupDetai.contactdishid,groupDetai.contactdishname,groupDetai.dishunitid,groupDetai.price,groupDetai.dishnum,groupDetai.dishtype,0);
					}
					if(groupDetai.dishtype=='1'){//火锅
						html=combodishHtml(index,groupDetai,0);
					}
					$("#dishes-comboEdit-box-list-unselected").append(html);
			});
		}
	});
}
//组合菜html拼接
function combodishHtml(index,groupDetai,flag){
	var html="";
	if(flag=='0'){//必选
		html+="<div class='dishes-comboEdit-box'";
	}else{//可选
		html+="<div class='dishes-comboEdit-box dishes-checked'";
	}
	html+=" alt='"+groupDetai.dishtype+"' id='singledish_"+groupDetai.contactdishid+"' onclick='selectCombodish(this)' dishes-unit='"+groupDetai.dishunitid+"'>" +
			"<div class='checkbox' style='padding-left:0px;'>" +
			"<label><span class='dishes-comboEdit-boxi'>"+(index+1)+".</span><span>"+groupDetai.contactdishname+"</span></label></div>";
	$.each(groupDetai.list,function(index,item){
		html+=	"<div class='dishes-panBottom' id='singledish_"+item.contactdishid+"' dishes-unit='"+item.dishunitid+"'>"+
		"<span class='dishes-panBottom-name'>"+item.contactdishname+"</span>"+
		"<span class='dishes-panBottom-num'><input type='text' 	class='form-control'  min='1' onclick='stoppro(event)'  value='"+item.dishnum+"'>"+
		"<span class='dishes-unit'>"+item.dishunitid+"</span></span> " +
		"<span class='dishes-panBottom-price'>"+item.price+"元</span></div>";
	});
	html+="</div>";
	return html;
}
//单个菜品html拼接
function singledishHtml(index,contactdishid,contactdishname,unit,price,dishnum,dishtype,flag){
	var html="";
	if(flag=='0'){//必选
		html+="<div class='dishes-comboEdit-box dishes-combo-single'";
	}else{//可选
		html+="<div class='dishes-comboEdit-box dishes-combo-single dishes-checked'";
	}
	html+=" alt='"+dishtype+"'  id='singledish_"+contactdishid+"' onclick='selectCombodish(this)' dishes-unit='"+unit+"'>" +
			"<div class='checkbox' style='padding-left:0px;'>" +
			"<label><span class='dishes-comboEdit-boxi'>"+(index+1)+".</span><span>"+contactdishname+"</span></label>" +
			" <span class='dishes-panBottom-num'><input type='text' class='form-control' onclick='stoppro(event)' value='"+dishnum+"' min='1' ><span class='dishes-unit'>"+unit+"</span></span>" +
			"<span class='dishes-panBottom-price'><span>"+price+"</span>元</span></div></div>";
	return html;
}
function savegroupselected(){
	var columnid=$("#dishes-comboEdit-dialog #modify-columnid").val();
	$.each(groupList,function(i,group){
		if(group.columnid==columnid){
			if($("#startnum").text()!=''){
				group.startnum=num.indexOf($("#startnum").text());
			}
			if($("#endnum").text()!=''){
				group.endnum=num.indexOf($("#endnum").text());
			}
			$.each($("#dishes-comboEdit-dialog .dishes-comboEdit-box"),function(index,item){
				var id=$(this).attr("id");
				var dishnum=$(this).find("input").val();
				var status=0;
				var dishunitid=$(this).attr("dishes-unit");
				 if($(this).hasClass("dishes-checked")&&$("#startnum").text()!=''&&$("#endnum").text()!=''){
					 status=1;
				 }else{
					 status=0;
				 }
				 var listTemp=[];
				 if($(this).attr("alt")==1){
					 $.each($(this).find(".dishes-panBottom"),function(i,ite){
						 var detailTemp={};
						 detailTemp.id=$(this).attr("id");
						 detailTemp.dishnum=$(this).find("input").val();
						 detailTemp.status=status;
						 detailTemp.dishunitid=$(this).attr("dishes-unit");
						 listTemp.push(detailTemp);
					 });
				 }
				$.each(group.groupDetailList,function(index,groupDetai){
					if(id.substr(id.indexOf('_')+1)==groupDetai.contactdishid&&dishunitid==groupDetai.dishunitid){
						groupDetai.dishnum=dishnum;
						groupDetai.status=status;
					}
					if(groupDetai.dishtype==1){
						$.each(groupDetai.list,function(i,detail){
							$.each(listTemp,function(k,obj){
								if(obj.id.substr(obj.id.indexOf('_')+1)==detail.contactdishid&&obj.dishunitid==detail.dishunitid){
									detail.dishnum=obj.dishnum;
									detail.status=obj.status;
								}
							});
						});
					}
				});
			});
		}
	});
	console.log(JSON.stringify(groupList));
	$("#dishes-comboEdit-dialog").modal('hide');
	showdetail();
	var marginl = -147.5*(flag_combo)+'px';
	$('.dishes-comboList:eq(0)').css('margin-left',marginl);
}
function saveComboDish(){

	  var name = $("#combotitle").val();
	  $.ajaxFileUpload({
		    fileElementId: ['main_img2'],  
		    url: global_Path+'/combodish/savedishset',  
		    dataType: 'json',
		    contentType:'application/json;charset=UTF-8',
		    data: { json:getCombodishJson() },
		    success: function (data, textStatus) {
		    	
		    	$("#dishes-combo-dialog").modal('hide');
		    	var arr = $("#combocolumnid").val().split(',');
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
		    		
		    	if($("#combodishid").val()==""){
		    		var temp = '<div class="dishes-detail-box" onmouseover="delDisplay(this)" id="'+data.tdish.dishid+'" data-dishtype="2" onmouseout="delHidden(this)">'+
		    		'<p class="dishes-detail-name">'+substrControl(name,14)+'</p>'+
		    		'<i class="icon-remove hidden" onclick="delDishesDetail(\''+data.tdish.dishid+'\',\''+data.tdish.title+'\',event)"></i></div>';
		    		$("#dishDetailList").before(temp);
		    		$('#'+data.tdish.dishid).dblclick(function(){
						initcombodish();
						$("#dishes-combo-dialog").modal("show");
						$("#dishes-combo-dialog").find(".dishes-combo-title").text("编辑套餐");
						viewAndEditComboDish($(this).attr('id'));
						
					});
//		    		$("#nav-dishes .active ").find("span").eq(1).text("("+($(".nav-dishes-tab div ").length-1)+")");
//		    		selectPrinterDish(data,arr);
		    	}else{
		    		var id=$("#combodishid").val();
		    		$("#"+id+" .dishes-detail-name").text(name.replace("\"","“").replace("\"","”"));
		    	}
		    	}else{
		    		if($("#combodishid").val()!=""){
		    			var id=$("#combodishid").val();
		    			$("#"+id).remove();
		    		}
		    	}
		    },  
		    complete: function (XMLHttpRequest, textStatus) {  
		    } 
	    });
}
function getCombodishJson(){
	var recommendCombo=checkStatus('recommendCombo');
	 var image;
	  if($("#main_img2").val()==''){
	    	image=imagePath;
	    }else{
	    	image="";
	  }
	  
	var TcomboDishGroupList={};
	var dish={};
	
	dish.dishid=$("#combodishid").val();
	if($("#combodishno").val()==""){
		dish.dishno="0";
	}else{
		dish.dishno = $("#combodishno").val();
	}
	dish.recommend = recommendCombo;
	dish.title=$("#combotitle").val().replace("\"","“").replace("\"","”");
	dish.dishtype='2';
	dish.abbrdesc=DishLabelArray.join(',');
	dish.columnid=$("#combocolumnid").val();
	dish.introduction=$("#combointroduction").val().replace("\"","“").replace("\"","”");
	dish.vipprice=$("#combovipprice").val();
	dish.price=$("#comboprice").val();
	dish.image=image;
	dish.headsort=1;
	dish.createtime=$("#combodishCreateTime").val();
	TcomboDishGroupList.dish=dish;
	TcomboDishGroupList.groupList=groupList;
	return JSON.stringify(TcomboDishGroupList);
}
/* 双击修改查看套餐*/
function viewAndEditComboDish(id){
	$.ajax({
		url : global_Path + "/combodish/findById/"+id+".json",
		type : "post",
		datatype : "json",
		contentType : "application/json; charset=utf-8",
		success : function(result) {
			$("#combodishid").val(result.tdish.dishid);				
			$("#combodishno").val(result.tdish.dishno);				
			$("#combotitle").val(result.tdish.title);
			$("#combointroduction").val(result.tdish.introduction);
			$("#comboprice").val(result.tdish.price);
			$("#combovipprice").val(result.tdish.vipprice);
			$("#combocount").html(maxCount-result.tdish.introduction.length);
			$("#combodishCreateTime").val(result.tdish.createtime);
			$.each(result.listType,function(index,item){
				$("#combodishTypeSelect").append('<span class="select-content-detail"><label>'+item.itemdesc+'</label><input type="checkbox" onclick="selectComboDishType()" id="'+item.id+'"></span><br/>');
			});
			var  columnidList=result.tdish.columnid.split(',');
			dishArrBefore = columnidList;
			var text='';
			var id='';
			if(!jQuery.isEmptyObject(columnidList)){
			$("#combodishTypeSelect").find("input[type='checkbox']").each(function(){
				if(columnidList.indexOf($(this).attr('id'))!=-1){
					text = text+","+$(this).prev("label").html();
					id = id+","+$(this).attr('id');
					$(this).attr("checked",true);
				}
			});
			}
			text = text.substr(1,text.length-1);
			id = id.substr(1,id.length-1);
			$("#comboselectDishTypeName").val(text);
			$("#combocolumnid").val(id);
			if(result.tdish.abbrdesc!=""){
				$("#combodishes-label-add").text(result.tdish.abbrdesc);
				DishLabelArray=result.tdish.abbrdesc.split(",");
			}
			if(result.tdish.image!=""&&typeof(result.tdish.image) != "undefined"){
				imagePath=result.tdish.image;
				$("#uploadpic2").attr("src",img_Path + imagePath);
			}
			groupList=result.groupList;
			console.log(JSON.stringify(groupList));
			if(groupList!=null&&groupList.length>0){
				$("#initComboDishDetail").hide();
			}
			if(result.tdish.recommend!=""&&typeof(result.tdish.recommend) != "undefined"&&result.tdish.recommend=="1"){
				  $("#recommendCombo").prop("checked","checked");
			}else{
			  $("#recommendCombo").prop("checked",""); 
			}
			showdetail();
			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}

	});
} 
