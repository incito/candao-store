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
		input[type="checkbox"].checkAll{
			margin-top: 0px;
  			margin-right: 5px;
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
				<span style="font-size:16px">添加已有菜品为互动礼物（最多可选择10项）<font id="select-font"></font></span>
				<div class="col-xs-2 pull-right">
					<label class="radio-inline">
						<input type="checkbox" name="dish-radio-uncheck" value="0" class="checkAll">全不选
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
var allselected = 0;
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
//					+"		<div style='width:18px;float:left;margin:-2px 5px 0 -1px;'><img alt='' src='' panelCheckedStatus='' style='width:15px;height:15px'></div>"
					+"		<div class='panel-title' style='width:96%;float:left' data-toggle='collapse' data-parent='#accordion' href='#collapseFour_"+tmpJson.id+"' aria-expanded='true' aria-controls='collapseFour'>"
					+"			<span itemtype='"+tmpJson.id+"'>"+ tmpJson.itemdesc+"</span>      "
					+			"<span class='dish-label'></span>"
					+"			<a  class='pull-right'>      "
					+"			 <i class='glyphicon glyphicon-chevron-down'></i>      "
					+"			</a>      "
					+"		</div>      "
					+"	</div>      ";
					if(i == 0){
						html +=	"	<div id='collapseFour_"+tmpJson.id+"' class='panel-collapse in' role='tabpanel' aria-labelledby='headingFour_"+tmpJson.id+"' style='height:auto;'> ";
					}else{
						
						html += "	<div id='collapseFour_"+tmpJson.id+"' class='panel-collapse collapse' role='tabpanel' aria-labelledby='headingFour_"+tmpJson.id+"'>";
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
					var checked = "";
					if(haveSelected != null && haveSelected.length>0){
						$.each(haveSelected, function(n, gift){
							if(gift.dish == dishObj.dishid && gift.dish_title == dishObj.title && gift.unit==dishObj.unit){
								checked = "checked";
								return;
							}
						});
					}
					console.log("here");
					html += "<label class='checkbox-inline col-xs-3'> <input type='checkbox' id='dish_"
							+checkboxId+"' value='"+dishObj.dishid+"' data-title='"+dishObj.title
							+"' unit='"+dishObj.unit+"' dishtype='"+dishObj.dishtype+"' price='"+dishObj.price
							+"' vipprice='"+dishObj.vipprice+"' unitflag='"+(dishObj.unitflag==1)+"' "+checked+">" +checkboxContent+"</label>";
				});
			}
			html+=  "		</div>      "
					+"	</div>      "
					+"</div>      ";
			i ++;
		});
		$("#dish-select-dialog #accordion").html(html);
		
		//点击全不选按钮
		$(":checkbox.checkAll").click(function(){
			$("#dish-select-dialog #accordion").find("input[type=checkbox]").prop("checked",false);
			generalDishTitle();
		});
		/*
		//在菜品分类绑定选择框点击事件
		$(".panel").find("img").click(function(){
			var panelCheckedStatus = $(this).attr("alt");
			//其它分类下的相同菜品需要同时选中和取消
			if(panelCheckedStatus == 0 || panelCheckedStatus == 1){
				$(this).parent().parent().parent().find("input[type=checkbox]").prop("checked", false);
				$.each($(this).parent().parent().parent().find("input[type=checkbox]"),function(i,obj){
					console.log(allselected);
					console.log(i);
					if((allselected + i) >= 10){
						alert("最多选择10项");
						return false;
					}else{
						$(obj).prop("checked", true);
						$("#dish-select-dialog").find("input[type='checkbox'][id='"+$(obj).attr("id")+"']").prop("checked", true);
					}
				});
			} else {
				$.each($(this).parent().parent().parent().find("input[type=checkbox]"), function(i, obj){
					$(obj).prop("checked", false);
					$("#dish-select-dialog").find("input[type='checkbox'][id='"+$(obj).attr("id")+"']").prop("checked", false);
				});
			}
			generalDishTitle();
		});
		*/
		/**
		* 点击选择某个菜品
		*/
		$("#dish-select-dialog input[type='checkbox']").not(".checkAll").click(function(){
			if($(this).prop("checked")){
				$("#dish-select-dialog #dish-radio-uncheck").prop("checked", false);
				console.log(allselected);
				if(allselected ==10){
					alert("只能选择10项");
					return false;
				}else{
					$("#dish-select-dialog").find("input[type='checkbox'][id='"+$(this).attr("id")+"']").prop("checked", true);
				}
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
					d.dishtype=$(obj).attr("dishtype");
					
					d.price = $(obj).attr("price");
					d.vipprice = $(obj).attr("vipprice");
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
			console.log(selectedDishs);
			//显示在系统设置页面中
			saveSelectedGifts(selectedDishs);
			$("#dish-select-dialog").modal("hide");
		});//end 提交
		
		generalDishTitle();
		$("#dish-select-dialog").modal("show");
	});
	
});
/**
 * 显示已选择的礼物在系统设置页面中
 */
function saveSelectedGifts(selectedDishs){
	if(selectedDishs!=null && selectedDishs.length>0){
		saveGifts(selectedDishs);
	}
}
/**
 * 保存礼物
 */
function saveGifts(selectedDishs){
	$.post(global_Path+"/social/saveGift", {data: JSON.stringify(selectedDishs)}, function(result){
		console.log(result);
		if(result.flag == 1){
			haveSelected = new Array();
			var htm = "";
			$.each(selectedDishs, function(i, item){
				haveSelected.push(item);
				var giftname = (i+1)+"、"+item.dish_title+"("+item.unit+")";
				console.log(giftname);
				htm += "<div class='one-gift'>"+giftname+"</div>";
			});
			$("#gifts-div").html(htm);
		}else{
			
		}
	},'json');
}
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
	if(selectedDishs.length <= 0){
		allselected = 0;
		$("#select-font").text("");
	} else {
		allselected = selectedDishs.length;
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
	$("input[type='checkbox'][name='dish-radio-uncheck']").prop("checked",false);
	if( checkedDishs.length == 0 ){
		//当前为不全选。
		$("input[type='checkbox'][name='dish-radio-uncheck'][value='0']").prop("checked",true); 
	}else if( checkedDishs.length == $("#dish-select-dialog #accordion").find("input[type='checkbox']").length ){
		//
	}
	
}
</script>
</body>
</html>