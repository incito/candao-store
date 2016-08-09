<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
		<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>Examples</title>
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
<div class="modal-dialog" id="fishpotdish-select-dialog" data-backdrop="static">
<input type="hidden" value="" id="flag"/> 
	<div class="modal-content">
		<div class="modal-body">
			<div class="dialog-sm-header" style="border: 0px;">				  
				 <img src="<%=request.getContextPath()%>/images/close-sm.png" class="img-close" data-dismiss="modal" style="top: -4%;">
			 </div>
			<div class="dishes-combo-title">
				<span>选择鱼锅明细<font >(已选<font id="dish-count">0</font>个菜品 )</font></span>
				<span class="radio ky-addDishes-empty">
					<label><input type="radio" name="dish-radio-uncheck" id="dish-radio-uncheck" value="" >全不选</label>
				</span>
			</div>
			<hr class="ky-hr">
			<div class="ky-addDishes-content" style="height: 400px;overflow: auto;">
				<div class="panel-group" id="fishpotaccordion" role="tablist" aria-multiselectable="true">
				</div>
			</div>
			<div class="ky-addDishes-footer">
				<div class="btn-operate">
					<button class="btn btn-cancel in-btn135"  onclick="closeModal('fishpotdish-select-dialog')">取消</button>
					<div  class="btn-division"></div>
					<button class="btn btn-save in-btn135 addDishes-btn-bgcolor" id="dish-select-confirm" onclick="closeModal('fishpotdish-select-dialog')">确认</button>
				</div>
			</div>
		</div>
	</div>
	<!-- /.modal-content -->
</div>
<!-- /.modal-dialog -->
<script type="text/javascript">
$(document).ready(function() {
	$("img.img-close").click(function(){
		$("div.modal-backdrop").css("z-index","1030");
	});
	$("#fishpotdish-select-dialog #fishpotaccordion").html("数据正在加载中......");
	
	//解析获取到的菜品/菜品分类，然后显示在 弹出层中。
	$.getJSON(global_Path+"/dish/getTypeAndDishList/3.json", function(json){
		var html="";
		var tmpJson={};
		$.each(json, function(index,item) {
		$.each(item, function(key,obj) {
			tmpJson=JSON.parse(key);
			html +="<div class='panel panel-default'>      "
					+"	<div class='panel-heading clearfix' role='tab' id='headingFour_"+tmpJson.id+"'>      "
					+"		<div style='width:18px;float:left'><img alt='' src='' panelCheckedStatus='' style='width:15px;height:15px'></div>"
					+"		<div class='panel-title' style='width:460px;float:left' data-toggle='collapse' data-parent='#fishpotaccordion' href='#fishpotcollapseFour_"+tmpJson.id+"' aria-expanded='true' aria-controls='collapseFour'>      "
					+"			<span>"+ tmpJson.itemdesc+"</span>      "
					+			"<span class='dish-label'></span>"
					+"			<a  class='pull-right'>      "
					+"			 <i class='glyphicon glyphicon-chevron-down'></i>      "
					+"			</a>      "
					+"		</div>      "
					+"	</div>      "
					+"	<div id='fishpotcollapseFour_"+tmpJson.id+"' class='panel-collapse collapse' role='tabpanel'  aria-labelledby='headingFour_"+tmpJson.id+"'>      "
					+"		<div class='panel-body'>      ";
			//计算该菜品分类是否存在菜品。如果存在，则遍历，并显示。
			var unitI = 0;
			if( obj.length>0){
				$.each(obj,function(i,dishObj){
					var checkboxId;
					var checkboxContent;
					if(dishObj.unitflag==0&&dishObj.unit){
						checkboxId = dishObj.dishid+unitI;
						checkboxContent = dishObj.title+"("+dishObj.unit+")";
						unitI++;
					} else {
						checkboxId = dishObj.dishid;
						checkboxContent = dishObj.title;
					}
					if(getStrLength(checkboxContent)>12){
						html += "<label class='checkbox-inline col-xs-3' id=label"+checkboxId+" text="+checkboxContent+"> <input  data-placement='bottom' rel='popover'  data-html='true' type='checkbox' id='dish_"
							+checkboxId+"' value='"+dishObj.dishid+"' data-title='"+dishObj.title
							+"' unit='"+dishObj.unit+"' unitflag='"+dishObj.unitflag+"' dishtype='"+dishObj.dishtype+"' price='"+dishObj.price+"' >" +substrControl(checkboxContent,12)+"</label>";
						
					}else{
						html += "<label class='checkbox-inline col-xs-3'> <input type='checkbox' id='dish_"
							+checkboxId+"' value='"+dishObj.dishid+"' data-title='"+dishObj.title
							+"' unit='"+dishObj.unit+"' unitflag='"+dishObj.unitflag+"' dishtype='"+dishObj.dishtype+"' price='"+dishObj.price+"' >" +checkboxContent+"</label>";
					}
// 					html += "<label class='checkbox-inline col-xs-3'> <input type='checkbox' id='dish_"	+checkboxId+"' value='"+dishObj.dishid+"' data-title='"+dishObj.title
// 							+"' unit='"+dishObj.unit+"' unitflag='"+dishObj.unitflag+"' dishtype='"+dishObj.dishtype+"' price='"+dishObj.price+"' >" +checkboxContent+"</label>";
				});
			}
			html+=  "		</div>      "
					+"	</div>      "
					+"</div>      ";
		});
		});
		
		
		$("#fishpotdish-select-dialog #fishpotaccordion").html(html);
		$("#fishpotdish-select-dialog").find(".panel-title").eq(0).click();
		$("#fishpotdish-select-dialog .ky-addDishes-content .panel-collapse ").find("label").each(function(){
			console.log("#"+$(this).attr("id"));
			showFishSelectStoreDiv($(this).attr("text"),"#"+$(this).attr("id"));
		});
		//绑定反选功能
		$("#fishpotdish-select-dialog #dish-radio-uncheck").click(function(){
			$("#fishpotdish-select-dialog #fishpotaccordion").find("input[type=checkbox]").prop("checked",false);
			$("#fishpotdish-select-dialog  #dish-count").text($("#fishpotdish-select-dialog #fishpotaccordion").find("input[type='checkbox']:checked").length );
			generalDishTitle();
		});
		
		//在菜品分类绑定选择框点击事件
		$("#fishpotdish-select-dialog  .panel").find("img").click(function(){
			var panelCheckedStatus = $(this).attr("alt");
			if(panelCheckedStatus == 0 || panelCheckedStatus == 1){
				$(this).parent().parent().parent().find("input[type=checkbox]").prop("checked", true);
			} else {
				$(this).parent().parent().parent().find("input[type=checkbox]").prop("checked", false);
			}
			generalDishTitle();
			$("#fishpotdish-select-dialog  #dish-count").text($("#fishpotdish-select-dialog input[type='checkbox']:checked").length);
		});
		
		$("#fishpotdish-select-dialog input[type='checkbox']").click(function(){
			var selectedDish = parseInt($("#fishpotdish-select-dialog #dish-count").text());
			if($(this).prop("checked")){
				$("#fishpotdish-select-dialog #dish-radio-uncheck").prop("checked", false);
				selectedDish++;
// 				$("#fishpotdish-select-dialog").find("input[type='checkbox'][id='"+$(this).attr("id")+"']").prop("checked", true);
			} else {
				selectedDish--;
// 				$("#fishpotdish-select-dialog").find("input[type='checkbox'][id='"+$(this).attr("id")+"']").prop("checked", false);
			}
			$("#fishpotdish-select-dialog  #dish-count").text(selectedDish);
			//$("#dish-count").text($("#fishpotdish-select-dialog").find("input[type='checkbox']:checked").length);
			generalDishTitle();
		});
		
		//绑定确定按钮
		$("#fishpotdish-select-dialog #dish-select-confirm").click(function(){
			var checkedDishs=$("#fishpotdish-select-dialog #fishpotaccordion").find("input[type=checkbox]:checked");
			if(checkedDishs.length>1&&$("#flag").val()==1){
				alert("锅底只能选择一个");
				return false;
			}
			var selectedDishs=[];

			
// 				$("#selecedDishes").css("display","block");
				$.each(checkedDishs,function(i,obj){
					var d={};
					d.dish=$(obj).val();
					d.dish_title=$(obj).attr("data-title");
					d.unit=$(obj).attr("unit");
					//防止不同分类中的同一菜品重复添加。
					var hasSelected = false;
					$.each(selectedDishs, function(j, dish){
						if(dish.dish==d.dish && dish.unit==d.unit){
							hasSelected = true;
							return false;
						}
					});
					if(!hasSelected){
						selectedDishs.push(d);
					}
					
				});
				//使用选择的菜品，构建一个显示在页面表单中的列表。
				buildfishpotDishListInView($("#flag").val());
			
		
			
			$("#fishpotdish-select-dialog").modal("hide");
		});
		
		if($("#flag").val()==0){
			groupList=groupfishList;
		}else{
			groupList=grouppotList;
		}
// 		alert("groupList.length"+"     "+groupList.length);
		$.each(groupList, function(index, item){
			$.each(item.groupDetailList,function(i,obj){
				var checkboxId;
				if(obj.unitflag==0&&obj.dishunitid){
					checkboxId = obj.contactdishid+"("+obj.dishunitid+")";
				} else {
					checkboxId = obj.contactdishid;
				}
				var dishCheck = $("#fishpotdish-select-dialog #fishpotcollapseFour_"+item.columnid).find("input[type='checkbox'][id='dish_"+checkboxId+"']");
				dishCheck.prop("checked",true);
			});
		});
		$("#fishpotdish-select-dialog  #dish-count").text($("#fishpotdish-select-dialog #fishpotaccordion").find("input[type='checkbox']:checked").length );
		generalDishTitle();
		$("#fishpotdish-select-dialog").modal("show");
	});
	
});

/**
 * 生成菜品选择分类的显示内容
 */
function generalDishTitle(){
	$.each( $("#fishpotdish-select-dialog #fishpotaccordion").find(".panel"), function(i,panel){
		var dish_array=[];
		var dishs= $(this).find("input[type=checkbox]:checked");
		if(dishs.length > 0){
			var panelTitle = "";
			$.each(dishs, function(i){
				panelTitle += $(this).parent().text();
				if(panelTitle.length >= 15){
					panelTitle = panelTitle.substring(0,15)+"...";
					return false;
				}
				if(i < dishs.length -1){
					panelTitle +="、";
				}
				
			});
			panelTitle = "(" + panelTitle + ")";
			$(this).find(".dish-label").html(panelTitle);
			if(dishs.length < $(this).find("input[type=checkbox]").length){
				$(this).find("img").attr("alt", "1");
				$(this).find("img").attr("src", global_Path+ "/images/sub_select.png");
			} else {
				$(this).find("img").attr("alt", "2");
				$(this).find("img").attr("src", global_Path+ "/images/all_select.png");
			}
			
		} else {
			$(this).find("img").attr("alt", "0");
			$(this).find("img").attr("src", global_Path+ "/images/none_select.png");
			$(this).find(".dish-label").html("");
		}
		
	});
}
//鼠标停留按钮显示已选择div
function showFishSelectStoreDiv(checkboxContent,selectType){
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