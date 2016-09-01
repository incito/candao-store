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
				var dishid = $(this).attr("dishid");
				var dishname = $(this).attr("dishname");
				var price = $(this).attr("price");
				
				var arr = isExist(dishid);
				if(arr[0]){
					var num = arr[1].find("td.num").text().trim();
					num = parseFloat(num)+1;
					var price1 = arr[1].find("td.price").text().trim();
					price1 =  parseFloat(price1)+parseFloat(price);
					
					arr[1].find("td.num").text(num);
					arr[1].find("td.price").text(price1);
				}else{
					tr = "<tr dishid='"+dishid+"' price="+price+"><td class='dishname'>" + dishname + "</td><td class='num'>1</td><td class='price'>" + price
						+ "</td></tr>";

					$("#sel-dish-table tbody").prepend(tr);
				}
				$("#sel-dish-table tbody tr").removeClass("selected");
				page4(nowPage4);
				//更新总消费金额
				updateTotalAmount();
				
				// 选中已点菜品
				$("#sel-dish-table tbody tr").click(function() {
					$("#sel-dish-table tbody tr").removeClass("selected");
					$(this).addClass("selected");
				});
			});
}
/**
 * 修改菜品数量
 */
function updateNum(){
	var name = $("#sel-dish-table tbody tr.selected").find("td.dishname").text().trim();
	$("#updatenum-dialog #dish-name").text(name);
	$("#updatenum-dialog").modal("show");
}
/**
 * 加菜
 */
function add(){
	var $tr = $("#sel-dish-table tbody tr.selected");
	var totalNum = $tr.find("td.num").text().trim();
	var totalPrice = $tr.find("td.price").text().trim();
	var price = $tr.attr("price");
	totalNum = parseFloat(totalNum) + 1;
	totalPrice = parseFloat(totalPrice) + parseFloat(price);
	$tr.find("td.num").text(totalNum);
	$tr.find("td.price").text(totalPrice);
	updateTotalAmount();
	page4(nowPage4);
}
/**
 * 减菜
 */
function reduct(){
	var $tr = $("#sel-dish-table tbody tr.selected");
	var totalNum = $tr.find("td.num").text().trim();
	var totalPrice = $tr.find("td.price").text().trim();
	var price = $tr.attr("price");
	if(parseFloat(totalNum) <= 1){
		//如果数量小于1，则删除本条
		$tr.remove();
		$("#sel-dish-table tbody tr").not(".hide").eq(0).addClass("selected");
	}else{
		totalNum = parseFloat(totalNum) - 1;
		totalPrice = parseFloat(totalPrice) - parseFloat(price);
		$tr.find("td.num").text(totalNum);
		$tr.find("td.price").text(totalPrice);
	}
	updateTotalAmount();
	page4(nowPage4);
}
/**
 * 更新总消费额
 */
function updateTotalAmount(){
	var total_amount = 0;
	if($("#sel-dish-table tbody tr").length >0 ){
		$("#sel-dish-table tbody tr").each(function(){
			var price = $(this).find("td.price").text().trim();
			total_amount += parseFloat(price);
		});
	}
	$("#total-amount").text(total_amount);
}
/**
 * 是否已存在，若已存在，只更新数量和金额
 */
function isExist(dishid){
	var $tr = null;
	var f = false;
	$("#sel-dish-table tbody tr").each(function(){
		if(dishid == $(this).attr("dishid")){
			$tr = $(this);
			f = true;
			return;
		}
	});
	return [f, $tr];
}
// 清空已选菜品
function clearSelected() {
	$("#clear-confirm-dialog").modal("show");
}
function doClear() {
	closeConfirm("clear-confirm-dialog");
	$("#sel-dish-table tbody").html("");
	updateTotalAmount();
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