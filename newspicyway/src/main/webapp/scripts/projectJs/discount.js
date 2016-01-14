var isDishLoaded = false;
var isBranchLoaded = false;
$(document).ready(function() {
	
	$("#addstore").click(function(){
		if(!isBranchLoaded){
			loadBranch();
			isBranchLoaded = true;
		} else {
			loadSelectStore();
			$("#store-select-dialog").modal("show");
		}
		
	});
	
	$("#dishesName").dblclick(function(){
		$(".add-dish-select").trigger("click");
	});
	
	if(preferentialId != ""){
		$("#starttime").val(timestampformat(parseInt($("#starttime").val())));
		$("#endtime").val(timestampformat(parseInt($("#endtime").val())));
		$("#color-input").css("background-color",$("#color").val());
		buildDishListInView(noDiscountDish);
		if(applyAllShop){
			$("input[type='radio'][name=applyAll][value='1']").attr("checked",true);
		} else {
			$("input[type='radio'][name=applyAll][value='0']").attr("checked",true);
			$("div.store-select").removeClass("hidden");
			createBranch();
			
			var selectedId = "";
			if(selectShops.length > 0){
				var ul = $("<ul/>").addClass("storesDiv");
				$.each(selectShops, function(i, shop){
					var storeName = shop.branch_name;
					selectedId += shop.branch;
					ul.append("<li>"+storeName+"</li>")
					if(i < selectShops.length -1){
						selectedId +=",";
					}
				});
				$("#selectBranchs").val(selectedId);
				var top = ileft = iwidth ="";
				if(selectShops.length >= 3){
					iwidth = "460px";
					ileft = "-155px";
					
				}
				var div = $("<div>").addClass("popover fade bottom in").css({
					width : iwidth,
					top : "38px",
					left: ileft
				}).append('<div class="arrow" style="left: 50%;"></div>');
				div.append(ul);
				$("#store-select").append(div);
				$("#addstore").text("已选中"+selectShops.length + "家店").addClass("selectBranch");
			}else{
				$("#addstore").html('<i class="icon-plus"></i>选择门店 ').removeClass("selectBranch").next(".popover").remove();
			}
			
		}
	} else {
		//$("#selecedDishes").css("display","none");
	}
	$("#selecedDishes").hover(function(){
		$(this).find(".popover").show();
	}, function(){
		$(this).find(".popover").hide();
	});
});

function cancel(){
	$("#discount-form").reset();
}

var discountTicket ={};
function save(){
	if(!$("#discount-form").valid()){
		return false;
	}
	discountTicket.preferentialActivity={};
	discountTicket.preferentialActivity.id=$("#id").val();
	discountTicket.preferentialActivity.name=$("#name").val();
	discountTicket.preferentialActivity.color=$("#color").val() == "" ? "#9cc05b" : $("#color").val();
	discountTicket.preferentialActivity.applyAll=$("input[name=applyAll]:checked").val()=="1"?true:false;
	discountTicket.discount=$("#discount").val();
	if(!discountTicket.preferentialActivity.applyAll){
		discountTicket.branchs=[];
		$.each($("table.store-select-content").find("input:checked[type='checkbox']"),function(i,obj){
			var branch = {};
			branch.branch=$(this).val();
			branch.branch_name=$(this).next().html();
			discountTicket.branchs.push(branch);
		});
	}
	discountTicket.preferentialActivity.starttime=$("#starttime").val();
	discountTicket.preferentialActivity.endtime=$("#endtime").val();
	discountTicket.preferentialActivity.activityIntroduction=$("#activityIntroduction").val();
	discountTicket.preferentialActivity.useNotice=$("#useNotice").val();
	var url = global_Path+ "/preferential/addDiscountTicket.json";;
	if(isModify){
		url = global_Path+ "/preferential/saveDiscountTicket.json";
	}
	$.ajax({
		url : url,
		type : "POST",
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(discountTicket),
		success : function(data) {
			if(data.isSuccess){
				$("#promptMsg").html("保存成功");
				$("#successPrompt").modal("show");
				window.setTimeout(function(){
					$("#successPrompt").modal("hide");
					window.location.href=global_Path+ "/preferential";
				}, 1000);
				
			}else {
				var str = '<label for="name" class="popover-errorTips" style="display: block;">'+data.message+'</label>';
				$("#name").addClass("popover-errorTips").after(str);
//				if(data.message){
//					$("#promptMsg").html(data.message);
//				} else{
//					$("#promptMsg").html("保存失败");
//				}
//				$("#successPrompt").modal("show");
//				window.setTimeout(function(){
//					$("#successPrompt").modal("hide");
//				}, 1000);
			}
		},
		error : function(XMLHttpRequest, textStatus,errorThrown) {
			alert(errorThrown);
		}
	});
	
}

/**
*@param selectedDishs 代表选中的 菜品。数组中，存放json,用json的方式传递菜品数据
* [
*	{dish:"id",dish_title:"菜品名称"},
*	{....}
* ]
**/
function buildDishListInView(selectedDishs){
	discountTicket.noDiscountDish = selectedDishs;
	var names = "" ;
	var selectItem = [];
	var selectedName = [];
	var selectedTitle = [];
	if(selectedDishs.length > 0){
		$.each(selectedDishs, function(i,dish){
			if(dish.unit && (dish.unitflag == "false" || dish.unitflag == false)){
				selectedTitle.push(dish.dish_title + "("+dish.unit+")");
			} else {
				selectedTitle.push(dish.dish_title);
			}
			/*//如果某一分类菜品全部选择时，只显示分类名
			if(dish.item_select){
				var hasAdded = false;
				for(var j = 0; j < selectItem.length; j++){
					if(selectItem[j] == dish.itemtype){
						hasAdded = true;
						break;
					}
				}
				if(!hasAdded){
					selectItem[selectItem.length] = dish.itemtype;
					selectedName.splice(0,0,dish.itemdesc);
				}
			} else {
				if(dish.unit && (dish.unitflag == "false" || dish.unitflag == false)){
					selectedName.push(dish.dish_title + "("+dish.unit+")");
				} else {
					selectedName.push(dish.dish_title);
				}
			}
			*/
		});
		/*names = selectedName.join();
		if(names.length > 12){
			$("#dishesName").val(names.substr(0,12)+"...("+selectedDishs.length+")");
		} else {
			$("#dishesName").val(names+"("+selectedDishs.length+")");
		}
		$("#dishesName").attr("title", selectedTitle.join());
		$(".add-dish-select").css("display", "none");*/
		
		var ul = $("<ul/>").addClass("storesDiv");
		$.each(selectedTitle, function(i, dish){
			var dishName = dish;
			ul.append("<li>"+dishName+"</li>")
		});
		var top = ileft = iwidth ="";
		if(selectedTitle.length >= 3){
			iwidth = "460px";
			ileft = "0";
			
		}
		var div = $("<div>").addClass("popover fade bottom in").css({
			top : "38px",
			left: ileft,
			width:iwidth
		}).append('<div class="arrow" style="left: 50%;"></div>');
		div.append(ul);
		$("#selecedDishes").find(".popover").remove();
		$("#selecedDishes").append(div);
		div.css("left", (400 - div.width())/2 + "px");
		$(".add-dish-select").text("已选中"+selectedTitle.length + "菜品");
	}else{
		$(".add-dish-select").html('<img alt="" src="'+global_Path+'/images/add.png" />').next(".popover").remove();
		$("#dishesName").val("");
		$("#dishesName").attr("title", "");
		//$("#selecedDishes").css("display","none");
		$(".add-dish-select").css("display", "block");
	}
}

function loadBranch(){
	createBranch();
	$("#store-select-dialog").modal("show");
}

function createBranch(){
	//从数据库加载所有门面
	$.getJSON(global_Path+"/shopMg/getall.json", function(json){
		$("table.store-select-content").html();
		var html="<tr>";
		$.each(json, function(i, obj) {
			html=html+" <td>"
				+"<input type='checkbox' value="+obj.branchid+"><span>"+obj.branchname+"</span>"
				+"</td>";
			if( (i+1)%4==0){//因为计数从0开始，所以要加一个才能显示正常
				html+="</tr><tr>"
			}
		});
		html=html+"</tr>"
		$("table.store-select-content").html(html);
		num = 0;
		loadSelectStore();
		//重新初始化 门店点击事件
		$("table.store-select-content input[type='checkbox']").click(function(){
			uploadStoreCount();
		});
		//选中门店名字，也可以发生变化
		// 点击文字选中
		$("table.store-select-content span").click(function(){
			$(this).prev("input[type='checkbox']").trigger("click");
		});
		
		//绑定确定按钮。当点击确定的时候，保存选择的门店。
		$("#store-select-confirm").click(function(){
			var selectedStore=$("table.store-select-content").find("input[type='checkbox']:checked");
			var ids=new Array();
			selectedStore.each(function(){
				ids.push( $(this).val() );
			});
			$("#selectBranchs").val( ids.join(",") );
		});
		
	});
	/*点击全选与非全选按钮*/
	$("input[name='checkAll']").click(function(){
		var num;
		if($(this).val()=='1'){
			num = $("table.store-select-content td").length;
			$("table.store-select-content").find("input[type='checkbox']").each(function(i,ch){
				ch.checked = true;
			});
			uploadStoreCount();
		}else{
			num=0;
			$("table.store-select-content").find("input[type='checkbox']").each(function(i,ch){
				ch.checked = false;
			});
			uploadStoreCount( );
		}
	});
	
}

//将弹出的选择门店的层中，更新选择的门店数量的方法抽象出来。当选择的门店为0个的时候，标题显示“选择门店”，否则显示“ 选择门店（已选1家店）”
function uploadStoreCount(){
	var count=$("table.store-select-content").find("input[type='checkbox']:checked").length;
	if( count !=0){
		$("#store-count").parent().html("选择门店（已选<font id='store-count'>"+count+"</font>家店）");
	}else{
		$("#store-count").parent().html("选择门店<font id='store-count'></font>");
	}
	//更新全选与全不选的按钮状态
	var branchLen = $("table.store-select-content").find("input[type='checkbox']:checked").length;
	if(branchLen == 0){
		$("input[name='checkAll'][value='0']").prop("checked", true);
		$("input[name='checkAll'][value='1']").prop("checked", false);
	} else if(branchLen == $("table.store-select-content").find("input[type='checkbox']").length ){
		$("input[name='checkAll'][value='0']").prop("checked", false);
		$("input[name='checkAll'][value='1']").prop("checked", true);
	} else {
		$("input[name='checkAll'][value='0']").prop("checked", false);
		$("input[name='checkAll'][value='1']").prop("checked", false);
	}
	
}

function loadSelectStore(){
	//清除选中状态/重置状态
	$("table.store-select-content input[type='checkbox']").prop("checked", false);
	//如果当前已经有选择的门店，需要将选择的门店，重新在页面显示为选中的状态
	if( $("#selectBranchs").val() != ""){
		$.each( $("#selectBranchs").val().split(","),function(i,obj){
			num++;
			$("table.store-select-content input[type='checkbox'][value='"+obj+"']").prop("checked", true);
		});
	}
	uploadStoreCount();
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
