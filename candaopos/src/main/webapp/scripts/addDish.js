var nowPage4 = 0;// 已选择菜品分页
var nowPage5 = 0;// 待选择菜品分页

var activeinputele;
var flag_prev = 0;
$(document).ready(function(){
	$("img.img-close").hover(function(){
	 	$(this).attr("src",global_path+"/images/close-active.png");	 
	},function(){
		$(this).attr("src",global_path+"/images/close-sm.png");
	});
	$(".search input[type='search']").focus(function(event){
        activeinputele = $(this);
	});
	//删除搜索条件
	$(".search .delsearch-btn").click(function(){
		$(".search input[type='search']").val("");
	});
	//搜索条件输入
	$(".search-btns div").click(function(){
		var keytext = $(this).text();
		if(activeinputele != null && activeinputele != undefined){
			var val = activeinputele.val();
			val = val + keytext;
			activeinputele.val(val);
			activeinputele.focus();
		}
	});
	//上一页
	$(".oper-div .prev-btn").click(function(){
		if($(this).hasClass("disabled")){
			return false;
		}
		page4(nowPage4-1);
	});
	//下一页
	$(".oper-div .next-btn").click(function(){
		if($(this).hasClass("disabled")){
			return false;
		}
		page4(nowPage4+1);
	});
	//上一页
	$(".main-div .prev-btn").click(function(){
		if($(this).hasClass("disabled")){
			return false;
		}
		page5(nowPage5-1);
	});
	//下一页
	$(".main-div .next-btn").click(function(){
		if($(this).hasClass("disabled")){
			return false;
		}
		page5(nowPage5+1);
	});
	/*菜品分类向左向右按钮*/
	$(".nav-dishtype-next").click(function(){
		var count = $(".nav-dish-types").find( "li.nav-dish-type").length;
		if (flag_prev < count - 6) {
			$(".nav-dish-types").find("li.nav-dish-type").eq(flag_prev).css("margin-left", "-16.66%");
			$(".nav-dish-types").find("li.nav-dish-type").eq(flag_prev+1).click();
			flag_prev++;
		}
	});
	$(".nav-dishtype-prev").click(function(){
		if(flag_prev>=1){	
			$(".nav-dish-types").find("li.nav-dish-type").eq(flag_prev-1).css("margin-left","0");
			$(".nav-dish-types").find("li.nav-dish-type").eq(flag_prev-1).click();
			flag_prev--;
		}
	});
	page4(0);
	initDishType();
});
// 已点菜品分页
function page4(currPage) {
	nowPage4 = loadPage({
		obj : "#sel-dish-table tbody tr",
		listNum : 16,
		currPage : currPage,
		totleNums : $("#sel-dish-table tbody tr").length,
		curPageObj : "#adddish-modal #curr-page",
		pagesLenObj : "#adddish-modal #pages-len",
		prevBtnObj : "#adddish-modal .oper-div .prev-btn",
		nextBtnObj : "#adddish-modal .oper-div .next-btn",
		callback : function() {
			$("#sel-dish-table tbody tr").not(".hide").eq(0).addClass(
					"selected");
		}
	});
}
// 菜品分页
function page5(currPage) {
	nowPage5 = loadPage({
		obj : ".dishes-content .dish-info",
		listNum : 20,
		currPage : currPage,
		totleNums : $(".dishes-content .dish-info").length,
		curPageObj : "#adddish-modal #curr-page1",
		pagesLenObj : "#adddish-modal #pages-len1",
		prevBtnObj : "#adddish-modal .main-div .prev-btn",
		nextBtnObj : "#adddish-modal .main-div .next-btn"
	});
}
// 菜品分类
function initDishType() {
	var htm = '';
	for (var i = 0; i < 20; i++) {
		var cla = "";
		if (i == 0)
			cla = "active";
		htm += '<li class="nav-dish-type ' + cla + '">分类' + i + '</li>';
	}
	$(".nav-dish-types").html(htm);
	initDishes();
	$(".nav-dish-types li.nav-dish-type").click(function() {
		$(".nav-dish-types li.nav-dish-type").removeClass("active");
		$(this).addClass("active");
		initDishes();
	});
}
// 通过分类获取菜品信息
function initDishes() {
	var htm = '';
	for (var i = 0; i < 45; i++) {
		htm += '<div class="dish-info" dishid="dish-id-' + i + '" dishname="菜品'
				+ i + '" price="49">' + '<div class="dish-name">菜品' + i
				+ '</div>' + '<hr>' + '<div class="dish-price">20元~999元</div>'
				+ '</div>';
	}
	$(".main-div .dishes-content").html(htm);
	page5(nowPage5);
	$(".dishes-content .dish-info").click(
			function() {
				var tr = "";
				var dishname = $(this).attr("dishname");
				var price = $(this).attr("price");
				tr = "<tr><td>" + dishname + "</td><td>1</td><td>" + price
						+ "</td></tr>";

				$("#sel-dish-table tbody").prepend(tr);
				$("#sel-dish-table tbody tr").removeClass("selected");
				page4(nowPage4);

				// 选中已点菜品
				$("#sel-dish-table tbody tr").click(function() {
					$("#sel-dish-table tbody tr").removeClass("selected");
					$(this).addClass("selected");
				});
			});
}
// 清空已选菜品
function clearSelected() {
	$("#clear-confirm-dialog").modal("show");
}
function doClear() {
	closeConfirm("clear-confirm-dialog");
	$("#sel-dish-table tbody").html("");
	page4(0);
}
// 下单
function placeOrder() {
	if (checkIsSeldish()) {
		// xiadan
		$("#placeorder-confirm-dialog").modal("show");
	}
}
// 确认下单
function doPlaceOrder() {
	console.log(111111);
}
// 全单备注
function allNote() {

}
// 赠菜
function giveFood() {
	if (checkIsSeldish()) {
		// zengcai
	}
}
// 关闭dialog
function closeConfirm(dialogId) {
	$("#" + dialogId).modal("hide");
}

// 判断是否选择菜品
function checkIsSeldish() {
	var len = $("#sel-dish-table tbody tr").length;
	if (len <= 0) {
		$("#nodish-confirm-dialog").modal("show");
		return false;
	}
	return true;
}