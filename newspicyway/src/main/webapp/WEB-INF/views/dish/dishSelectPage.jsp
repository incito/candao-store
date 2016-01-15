<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
		<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

	<style type="text/css">
		.selected-dish-list div{
			background-color:#F4F4F4;
			margin-bottom: 1px;
		}
		
		.selected-dish-list .p_0{
			padding-top: 0px;
			padding-bottom: 0px;
			height:auto;
		}
		
		.ky-addDishes-empty{
			float: right;
			margin-top: 0;
			margin-bottom: 0;
		}
		.ky-addDishes-content .checkbox-inline{
			margin:0;
		}
		.ky-addDishes-content .panel-heading{
			padding-top: 5px;
			padding-bottom: 5px;
		}
		.ky-addDishes-content .panel-title{
			font-size: 14px;
		}
	</style>
</head>
<body>
<div class="modal-dialog" id="dish-select-dialog" data-backdrop="static" >
	<div class="modal-content">
		<div class="modal-body">
			<div class="dialog-sm-header">				  
				        
				 <img src="<%=request.getContextPath()%>/images/close-sm.png" class="img-close" data-dismiss="modal">
			 </div>
			<div class="dishes-combo-title">
				<span>添加已有菜品至该分类 <font >(已选<font id="dish-count">0</font>个菜品 )</font></span>
				<span class="radio ky-addDishes-empty">
					<label><input type="radio" name="dish-radio-uncheck" id="dish-radio-uncheck" value="" >全不选</label>
				</span>
			</div>
			<hr class="ky-hr">
			<div class="ky-addDishes-content" style="height: 400px;overflow: auto;">
				<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
				</div>
			</div>
			<div class="ky-addDishes-footer">
				<div class="btn-operate">
					<button class="btn btn-cancel in-btn135" data-dismiss="modal">取消</button>
					<div  class="btn-division"></div>
					<button class="btn btn-save in-btn135 addDishes-btn-bgcolor btn-saves" id="dish-select-confirm">确认</button>
				</div>
			</div>
		</div>
	</div>
	<!-- /.modal-content -->
</div>
<!-- /.modal-dialog -->
<script type="text/javascript">
$(document).ready(function() {
	$("img.img-close").hover(function(){
	 	$(this).attr("src",global_Path+"/images/close-active.png");	 
	},function(){
			$(this).attr("src",global_Path+"/images/close-sm.png");
	});
	$("#dish-select-dialog #accordion").html("数据正在加载中......");
	
	//解析获取到的菜品/菜品分类，然后显示在 弹出层中。
	$.getJSON(global_Path+"/dish/getTypeAndDishMap.json", function(json){
		var html="";
		var tmpJson={};
		var haveDishId=[];
		$(".nav-dishes-tab .dishes-detail-box").each(function(){
// 			alert($(this).attr("id"));
			haveDishId.push($(this).attr("id"));
		});
		$.each(json, function(index,item) {
		$.each(item, function(key,obj) {
			tmpJson=JSON.parse(key);
			if($("li.nav-dishes-type.active").attr('id')!=tmpJson.id){
			html +="<div class='panel panel-default'>      "
					+"	<div class='panel-heading clearfix' role='tab' id='headingFour_"+tmpJson.id+"'>      "
					+"		<div style='width:18px;float:left'><img alt='0' src='"+global_Path+ "/images/none_select.png' panelCheckedStatus='' style='width:15px;height:15px'></div>"
					+"		<div class='panel-title' style='width:470px;float:left' data-toggle='collapse' data-parent='#accordion' href='#collapseFour_"+tmpJson.id+"' aria-expanded='true' aria-controls='collapseFour'>      "
					+"			<span>"+ tmpJson.itemdesc+"</span>      "
					+			"<span class='dish-label'></span>"
					+"			<a  class='pull-right'>      "
					+"			 <i class='glyphicon glyphicon-chevron-down'></i>      "
					+"			</a>      "
					+"		</div>      "
					+"	</div>      "
					+"	<div id='collapseFour_"+tmpJson.id+"' class='panel-collapse collapse' role='tabpanel' aria-labelledby='headingFour_"+tmpJson.id+"'>      "
					+"		<div class='panel-body'>      ";
			//计算该菜品分类是否存在菜品。如果存在，则遍历，并显示。
			if( obj.length>0){
				$.each(obj,function(i,dishObj){
					var checkboxId;
					var checkboxContent;
					if(jQuery.isEmptyObject(haveDishId)){
						checkboxId = dishObj.dishid;
						checkboxContent = dishObj.title;
					html += "<label class='checkbox-inline col-xs-3'><input type='checkbox' id='dish_"
							+checkboxId+"' value='"+dishObj.dishid+"' data-title='"+dishObj.title
							+"' code='"+dishObj.dishno+"'>"+substrControl(checkboxContent,12)+"</label>";
// 							checkboxContent.substr(0,7)
// 							substrControl(checkboxContent,14)
					}else{
						if(haveDishId.indexOf(dishObj.dishid)==-1){
								checkboxId = dishObj.dishid;
								checkboxContent = dishObj.title;
							if(getStrLength(checkboxContent)>12){
								html += "<label class='checkbox-inline col-xs-3' id=label"+checkboxId+" text="+checkboxContent+"> <input  data-placement='bottom' rel='popover'  data-html='true' type='checkbox' id='dish_"
									+checkboxId+"' value='"+dishObj.dishid+"' data-title='"+dishObj.title
									+"' dishtype='"+dishObj.dishtype+"' price='"+dishObj.price+"' >" +substrControl(checkboxContent,12)+"</label>";
								
							}else{
								html += "<label class='checkbox-inline col-xs-3'> <input type='checkbox' id='dish_"
									+checkboxId+"' value='"+dishObj.dishid+"' data-title='"+dishObj.title
									+"' dishtype='"+dishObj.dishtype+"' price='"+dishObj.price+"' >" +checkboxContent+"</label>";
							}
// 							html += "<label class='checkbox-inline col-xs-3'><input type='checkbox' id='dish_"
// 									+checkboxId+"' value='"+dishObj.dishid+"' data-title='"+dishObj.title
// 									+"' code='"+dishObj.dishno+"'>"+substrControl(checkboxContent,12)+"</label>";
						}
					}
				});
			}
			html+=  "		</div>      "
					+"	</div>      "
					+"</div>      ";
			}		
		});
		});
		
		
		$("#dish-select-dialog #accordion").html(html);
		$("#dish-select-dialog #accordion").children(":first").find(".panel-title").click();
		$("#dish-select-dialog .ky-addDishes-content .panel-collapse ").find("label").each(function(){
			console.log("#"+$(this).attr("id"));
			showDishSelectStoreDiv($(this).attr("text"),"#"+$(this).attr("id"));
		});
		//绑定反选功能
		$("#dish-select-dialog #dish-radio-uncheck").click(function(){
			$("#dish-select-dialog #accordion").find("input[type=checkbox]").prop("checked",false);
			$("#dish-select-dialog #accordion").find("img").attr("alt", "0");
			$("#dish-select-dialog #accordion").find("img").attr("src", global_Path+ "/images/none_select.png");
			$("#dish-select-dialog #accordion").find(".dish-label").html('');
			$("#dish-count").text($("#dish-select-dialog #accordion").find("input[type='checkbox']:checked").length );
		});
		
		//在菜品分类绑定选择框点击事件
		$(".panel").find("img").click(function(){
			var panelCheckedStatus = $(this).attr("alt");
			if(panelCheckedStatus == 0 || panelCheckedStatus == 1){
				$(this).parent().parent().parent().find("input[type=checkbox]").prop("checked", true);
			} else {
				$(this).parent().parent().parent().find("input[type=checkbox]").prop("checked", false);
			}
			$("#dish-select-dialog #dish-radio-uncheck").attr("checked",false);
			generalDishTitle();
			$("#dish-count").text($("#dish-select-dialog input[type='checkbox']:checked").length);
		});
		
		$("#dish-select-dialog input[type='checkbox']").click(function(){
			var selectedDish = parseInt($("#dish-count").text());
			if($(this).prop("checked")){
				selectedDish++;
				$("#dish-select-dialog").find("input[type='checkbox'][id='"+$(this).attr("id")+"']").prop("checked", true);
			} else {
				selectedDish--;
				$("#dish-select-dialog").find("input[type='checkbox'][id='"+$(this).attr("id")+"']").prop("checked", false);
			}
			$("#dish-count").text(selectedDish);
			$("#dish-select-dialog #dish-radio-uncheck").attr("checked",false);
			//$("#dish-count").text($("#dish-select-dialog").find("input[type='checkbox']:checked").length);
			generalDishTitle();
		});
		
		//绑定确定按钮
		$("#dish-select-dialog #dish-select-confirm").click(function(){
			var checkedDishs=$("#dish-select-dialog #accordion").find("input[type=checkbox]:checked");
			var selectedDishs=[];
			$.each(checkedDishs,function(i,obj){
				var d={};
				d.dishid=$(obj).val();
				d.dish_title=$(obj).attr("data-title");
				d.dish_dishno=$(obj).attr("code");
				d.columnid=$("li.nav-dishes-type.active").attr('id');
				//防止不同分类中的同一菜品重复添加。
				var hasSelected = false;
				$.each(selectedDishs, function(j, dish){
					if(dish.dishid==d.dishid){
						hasSelected = true;
						return false;
					}
				});
				if(!hasSelected){
					selectedDishs.push(d);
				}
				
			});
			//使用选择的菜品，构建一个显示在页面表单中的列表。
			addDishToDiv(selectedDishs);

			var printeridHave=[];
			var printerHave=[];
			$.each(tbPrinterDetailList,function(i,item){
				if(item.columnid==$("#nav-dishes .active").attr("id")&&printeridHave.indexOf(item.printerid) == -1){
					$.each(selectedDishs, function(j, itemB){
						item.dishid=itemB.dishid;
//						addPrinterArea(item);
						printeridHave.push(item.printerid);
						printerHave.push(item);
					});
					
				}
			});
// 			addPrinterDish(printerHave);
			
			$("#dish-select-dialog").modal("hide");
			dishNumChange("","");
		});
		
	});
});
function addDishToDiv(selectedDishs){
	if(!jQuery.isEmptyObject(selectedDishs)){
	$.each(selectedDishs, function(i,dish){
		var temp = '<div class="dishes-detail-box" onmouseover="delDisplay(this)" id="'+dish.dishid+'" data-dishtype="0" onmouseout="delHidden(this)">'+
		'<p class="dishes-detail-name">'+substrControl(dish.dish_title,14)+'</p>'+
		'<i class="icon-remove hidden" onclick="delDishesDetail(\''+dish.dishid+'\',\''+dish.dish_title+'\',event)"></i></div>';
// 		var temp = '<div class="dishes-detail-box" onmouseover="delDisplay(this)" id="'+dish.dishid+'" onmouseout="delHidden(this)">'+
// 		'<p class="dishes-detail-name">'+dish.dish_title+'</p>'+
// 		'<p class="dishes-detail-code">'+dish.dish_dishno+'</p>'+
// 		'<i class="icon-remove hidden" onclick="delDishesDetail(\''+dish.dishid+'\',\''+dish.dish_title+'\',event)"></i></div>';
		$("#dishDetailList").before(temp);
		$('#'+dish.dishid).dblclick(function(){
			initDish();
			$("#dishes-detailAdd-dialog").modal("show");
			$("#dishes-detailAdd-dialog").find("div.modal-title").text("编辑菜品");
			viewAndEdit(dish.dishid);
			
		});
		
	});
// 	ajax插入数据库
	$.ajax({
		type:"post",
		async:false,
		url : global_Path+'/dish/addTdishDishType.json',
		contentType:'application/json;charset=UTF-8',
	    data:JSON.stringify(selectedDishs), 
		dataType : "json",
		success : function(result) {
		}
	});
	
	
	}
}

/**
 * 生成菜品选择分类的显示内容
 */
function generalDishTitle(){
	$.each( $("#dish-select-dialog #accordion").find(".panel"), function(i,panel){
		var dishs= $(this).find("input[type=checkbox]:checked");
		if(dishs.length > 0){
			if(dishs.length < $(this).find("input[type=checkbox]").length){
				$(this).find("img").attr("alt", "1");
				$(this).find("img").attr("src", global_Path+ "/images/sub_select.png");
			} else {
				$(this).find("img").attr("alt", "2");
				$(this).find("img").attr("src", global_Path+ "/images/all_select.png");
			}
			var panelTitle ='';
			$.each(dishs, function(i){
// 				alert($(this).parent().text());
// 				alert($(this).parent().text().length);
				panelTitle += $(this).parent().text();
				if(panelTitle.length >= 15){
					panelTitle = panelTitle.substring(0,15)+"...";
					return false;
				}
				if(i < dishs.length -1){
					panelTitle +=",";
				}
				
			});
			panelTitle = "(" + panelTitle + ")";
			$(this).find(".dish-label").html(panelTitle);
		} else {
			$(this).find("img").attr("alt", "0");
			$(this).find("img").attr("src", global_Path+ "/images/none_select.png");
			$(this).find(".dish-label").html("");
		}
		
	});
}
//鼠标停留按钮显示已选择div
function showDishSelectStoreDiv(checkboxContent,selectType){
	console.log(selectType);
	console.log($(selectType));
	console.log($(selectType).attr("id"));
		if(checkboxContent!=null){
			$(selectType).find("div.popover").remove();
			var ul = $("<ul/>").addClass("storesDiv");
			ul.append("<li>"+checkboxContent+"</li>");
			var  iwidth ="";
			var div = $("<div>").addClass("popover fade bottom in").css({
				width : iwidth,
				left:"10px",
				top: "12px",
				}).append('<div class="arrow" style="left: 50%;"></div>');
			div.append(ul);
			$(selectType).append(div);
			
		}else{
			$(selectType).find(".popover").remove();
		}

	$(selectType).hover(function(){
		$(this).find(".popover").show();
	}, function(){
		$(this).find(".popover").hide();
	});
	
}
</script>
</body>
</html>