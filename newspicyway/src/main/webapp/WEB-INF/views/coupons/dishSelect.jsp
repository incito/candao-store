<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
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
		#accordion .panel {
			border-radius:0;
		}
		#accordion .panel-body {
			border:none;
		}
		.modal-body {
			padding-top:10px;
		}
		
	</style>
</head>
<body>
<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header addDelicon">				  
	        <img data-dismiss="modal" class="img-close" src="/newspicyway/images/close.png">
	    </div>
		<div class="modal-body">
			<div class="ky-addDishes-title">
				<span style="font-size:16px">不参与折扣菜品<font id="select-font"></font></span>
				<!-- <span class="radio ky-addDishes-empty">
					<label><input type="radio" name="dish-radio-uncheck"  value="" >全选</label>
					<label><input type="radio" name="dish-radio-uncheck" id="dish-radio-uncheck" value="" >全不选</label>
				</span> -->
				<div class="col-xs-3 pull-right">
					<label class="radio-inline">
						<input type="radio" name="dish-radio-uncheck" value="1" class="checkAll">全选
					</label>
					<label class="radio-inline">
						<input type="radio" name="dish-radio-uncheck" value="0" class="checkAll">全不选
					</label>
				</div>
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
					<button class="btn btn-save in-btn135 addDishes-btn-bgcolor" id="dish-select-confirm">确认</button>
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
	$.getJSON(global_Path+"/preferential/getTypeAndDishList.json", function(json){
		var html="";
		var tmpJson={};
		var i = 0;
		$.each(json, function(key,obj) {
			tmpJson=JSON.parse(key);
			html +="<div class='panel panel-default'>      "
					+"	<div class='panel-heading clearfix' role='tab' id='headingFour_"+tmpJson.id+"'>      "
					+"		<div style='width:18px;float:left;margin:-2px 5px 0 -1px;'><img alt='' src='' panelCheckedStatus='' style='width:15px;height:15px'></div>"
					+"		<div class='panel-title' style='width:96%;float:left' data-toggle='collapse' data-parent='#accordion' href='#collapseFour_"+tmpJson.id+"' aria-expanded='true' aria-controls='collapseFour'>      "
					+"			<span itemtype='"+tmpJson.id+"'>"+ tmpJson.itemdesc+"</span>      "
					+			"<span class='dish-label'></span>"
					+"			<a  class='pull-right'>      "
					+"			 <i class='glyphicon glyphicon-chevron-down'></i>      "
					+"			</a>      "
					+"		</div>      "
					+"	</div>      "
					if(i == 0){
						html +=	"	<div id='collapseFour_"+tmpJson.id+"' class='panel-collapse in' role='tabpanel' aria-labelledby='headingFour_"+tmpJson.id+"' style='height:auto;'>      "
					}else{
						
						html += "	<div id='collapseFour_"+tmpJson.id+"' class='panel-collapse collapse' role='tabpanel' aria-labelledby='headingFour_"+tmpJson.id+"'>      "
					}
						html += "		<div class='panel-body'>      ";
			//计算该菜品分类是否存在菜品。如果存在，则遍历，并显示。
			if( obj.length>0){
				$.each(obj,function(i,dishObj){
					var checkboxId;
					var checkboxContent;
					if(dishObj.unit && (dishObj.unitflag == "false" || dishObj.unitflag == false)){
						checkboxId = dishObj.dishid+"("+dishObj.unit+")";
						checkboxContent = dishObj.title+"("+dishObj.unit+")";
					} else {
						checkboxId = dishObj.dishid;
						checkboxContent = dishObj.title;
					}
					html += "<label class='checkbox-inline col-xs-3'> <input type='checkbox' id='dish_"
							+checkboxId+"' value='"+dishObj.dishid+"' data-title='"+dishObj.title
							+"' unit='"+dishObj.unit+"' unitflag='"+(dishObj.unitflag==1)+"'>" +checkboxContent+"</label>";
				});
			}
			html+=  "		</div>      "
					+"	</div>      "
					+"</div>      ";
			i ++;
		});
		
		
		$("#dish-select-dialog #accordion").html(html);
		//绑定反选功能
		/* $("#dish-select-dialog #dish-radio-uncheck").click(function(){
			$("#dish-select-dialog #accordion").find("input[type=checkbox]").prop("checked",false);
			generalDishTitle();
		}); */
		
		$(":radio.checkAll").click(function(){
			if($(this).val() == "1"){
				$("#dish-select-dialog #accordion").find("input[type=checkbox]").prop("checked",true);
			}else{
				$("#dish-select-dialog #accordion").find("input[type=checkbox]").prop("checked",false);
			}
			generalDishTitle();
		});
		
		//在菜品分类绑定选择框点击事件
		$(".panel").find("img").click(function(){
			var panelCheckedStatus = $(this).attr("alt");
			//其它分类下的相同菜品需要同时选中和取消
			if(panelCheckedStatus == 0 || panelCheckedStatus == 1){
				$.each($(this).parent().parent().parent().find("input[type=checkbox]"),function(i,obj){
					$(obj).prop("checked", true);
					$("#dish-select-dialog").find("input[type='checkbox'][id='"+$(obj).attr("id")+"']").prop("checked", true);
				});
			} else {
				$.each($(this).parent().parent().parent().find("input[type=checkbox]"), function(i, obj){
					$(obj).prop("checked", false);
					$("#dish-select-dialog").find("input[type='checkbox'][id='"+$(obj).attr("id")+"']").prop("checked", false);
				});
			}
			generalDishTitle();
		});
		
		$("#dish-select-dialog input[type='checkbox']").click(function(){
			var selectedDishs=$("#dish-select-dialog #accordion").find("input[type='checkbox']");
			if($(this).prop("checked")){
				$("#dish-select-dialog #dish-radio-uncheck").prop("checked", false);
				$("#dish-select-dialog").find("input[type='checkbox'][id='"+$(this).attr("id")+"']").prop("checked", true);
			} else {
				$("#dish-select-dialog").find("input[type='checkbox'][id='"+$(this).attr("id")+"']").prop("checked", false);
			}
			generalDishTitle(); 
		});
		
		//绑定确定按钮
		$("#dish-select-dialog #dish-select-confirm").click(function(){
			var checkedDishs=$("#dish-select-dialog #accordion").find("input[type=checkbox]:checked");
			var selectedDishs=[];
			if(checkedDishs.length > 0){
				$("#selecedDishes").css("display","block");
				$.each(checkedDishs,function(i,obj){
					var d={};
					d.dish=$(obj).val();
					d.dish_title=$(obj).attr("data-title");
					d.unit=$(obj).attr("unit");
					d.unitflag=$(obj).attr("unitflag");
					if($(obj).attr("item_select")){
						d.itemtype=$(obj).attr("itemtype");
						d.itemdesc=$(obj).attr("itemdesc");
						d.item_select=$(obj).attr("item_select");
					}
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
			}
			//使用选择的菜品，构建一个显示在页面表单中的列表。
			buildDishListInView(selectedDishs);
			$("#dish-select-dialog").modal("hide");
		});
		
		$.each(noDiscountDish, function(i, dish){
			var checkboxId;
			if(dish.unit && dish.unitflag == false){
				checkboxId = dish.dish+"("+dish.unit+")";
			} else {
				checkboxId = dish.dish;
			}
			var dishCheck = $("#dish-select-dialog").find("input[type='checkbox'][id='dish_"+checkboxId+"']");
			dishCheck.prop("checked",true);
		});
		generalDishTitle();
		$("#dish-select-dialog").modal("show");
	});
	
});

/**
 * 生成菜品选择分类的显示内容
 */
function generalDishTitle(){
	var checkedDishs=$("#dish-select-dialog #accordion").find("input[type='checkbox']:checked");
	var selectedDishs=[];
	if(checkedDishs.length > 0){
		$.each(checkedDishs,function(i,obj){
			//防止不同分类中的同一菜品重复添加。
			var d={};
			d.dish=$(obj).val();
			d.unit=$(obj).attr("unit");
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
	}
	$("#dish-count").text(selectedDishs.length);
	if(selectedDishs.length <= 0){
		$("#select-font").text("");
	} else {
		$("#select-font").text("(已选"+selectedDishs.length+"种菜品)");
	} 
	$.each( $("#dish-select-dialog #accordion").find(".panel"), function(i,panel){
		var dish_array=[];
		var dishs= $(this).find("input[type=checkbox]:checked");
		if(dishs.length > 0){
			if(dishs.length < $(this).find("input[type=checkbox]").length){
				$(this).find("img").attr("alt", "1");
				$(this).find("img").attr("src", global_Path+ "/images/sub_select.png");
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
				dishs.attr("itemtype","");
				dishs.attr("itemdesc","");
				dishs.attr("item_select", "");
			} else {
				$(this).find("img").attr("alt", "2");
				$(this).find("img").attr("src", global_Path+ "/images/all_select.png");
				$(this).find(".dish-label").html("");
				dishs.attr("itemtype",$(this).find("span").attr("itemtype"));
				dishs.attr("itemdesc",$(this).find("span").text());
				dishs.attr("item_select", true);
			}
		} else {
			$(this).find("img").attr("alt", "0");
			$(this).find("img").attr("src", global_Path+ "/images/none_select.png");
			$(this).find(".dish-label").html("");
		}
	});
	
	//更新全选与全不选按钮的状态。当不是全选或者全不选的时候，清除这两个单选按钮的选中状态。
	$("input[type='radio'][name='dish-radio-uncheck']").prop("checked",false);
	if( checkedDishs.length == $("#dish-select-dialog #accordion").find("input[type='checkbox']").length ){
		//当前为全选。
		$("input[type='radio'][name='dish-radio-uncheck'][value='1']").prop("checked",true); 
	}else if( checkedDishs.length == 0 ){
		//当前为不全选。
		$("input[type='radio'][name='dish-radio-uncheck'][value='0']").prop("checked",true); 
	}
	
}

/**
 *获取已经选择的，并且显示在页面中的 菜品id，用于重新选择菜品的时候，还原应选择的菜品.
 @return Array
**/
function getHaveSelectDishsInView(){
	var dishListDiv=$("#dish-selected-list");
	var dishs=[];
	var dishList=dishListDiv.find("input");
	$.each(dishList, function(i, obj) {
		dishs.push( $(obj).attr("data-dish") );
	})
	return dishs;
}
</script>
</body>
</html>