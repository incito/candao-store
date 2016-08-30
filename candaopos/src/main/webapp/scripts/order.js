var nowPage1 = 0;// 已选择菜品分页
var nowPage2 = 0;// 已选择优惠分页
var nowPage3 = 0;// 待选优惠分页
var pref_prev = 0;
$(document).ready(function(){
	$("img.img-close").hover(function(){
	 	$(this).attr("src",global_path+"/images/close-active.png");	 
	},function(){
		$(this).attr("src",global_path+"/images/close-sm.png");
	});
	//未开过台的先开台
	if(!isopened){
		$("#open-dialog").modal("show");
	}
	//点击加菜
	$("#add-dish").click(function(){
		$("#adddish-dialog").load(global_path+"/views/orderdish.jsp");
		$("#adddish-dialog").modal("show");
	});
	
	//已点菜上一页
	$(".dish-oper-btns .prev-btn").click(function() {
		if ($(this).hasClass("disabled")) {
			return false;
		}
		page1(nowPage1 - 1);
	});
	//已点菜下一页
	$(".dish-oper-btns .next-btn").click(function() {
		if ($(this).hasClass("disabled")) {
			return false;
		}
		page1(nowPage1 + 1);
	});
	//已选优惠上一页
	$(".preferential-oper-btns .prev-btn").click(
			function() {
				if ($(this).hasClass("disabled")) {
					return false;
				}
				page2(nowPage2 - 1);
			});
	//已选优惠下一页
	$(".preferential-oper-btns .next-btn").click(
			function() {
				if ($(this).hasClass("disabled")) {
					return false;
				}
				page2(nowPage2 + 1);
			});
	//优惠上一页
	$(".main-div .prev-btn").click(function() {
		if ($(this).hasClass("disabled")) {
			return false;
		}
		page3(nowPage3 - 1);
	});
	//优惠下一页
	$(".main-div .next-btn").click(function() {
		if ($(this).hasClass("disabled")) {
			return false;
		}
		page3(nowPage3 + 1);
	});
	
	$(".tab-payment ul li").click(function(){
		$(".tab-payment ul li").removeClass("active");
		$(this).addClass("active");
		
		$(".paytype-input").addClass("hide");
		var targetId = $(this).attr("target");
		$(targetId).removeClass("hide");
	});
	
	//选择年龄段
	$(".age-type div").click(function(){
		if($(this).hasClass("active")){
			$(this).removeClass("active");
		}else{
			$(this).addClass("active");
		}
	});
	/*优惠分类向左向右按钮*/
	$(".nav-pretype-next").click(function(){
		var count = $(".nav-pref-types").find( "li.nav-pref-type").length;
		if (pref_prev < count - 6) {
			$(".nav-pref-types").find("li.nav-pref-type").eq(pref_prev).css("margin-left", "-16.66%");
			$(".nav-pref-types").find("li.nav-pref-type").eq(pref_prev+1).click();
			pref_prev++;
		}
	});
	$(".nav-pretype-prev").click(function(){
		if(pref_prev>=1){	
			$(".nav-pref-types").find("li.nav-pref-type").eq(pref_prev-1).css("margin-left","0");	
			$(".nav-pref-types").find("li.nav-pref-type").eq(pref_prev-1).click();
			pref_prev--;
		}
	});
	initPreferentialType();
	initOrderDish();
});
// 已点菜品分页
function page1(currPage) {
	nowPage1 = loadPage({
		obj : "#order-dish-table tbody tr",
		listNum : 6,
		currPage : currPage,
		totleNums : $("#order-dish-table tbody tr").length,
		curPageObj : "#order-modal #curr-page1",
		pagesLenObj : "#order-modal #pages-len1",
		prevBtnObj : "#order-modal .dish-oper-btns .prev-btn",
		nextBtnObj : "#order-modal .dish-oper-btns .next-btn",
		callback : function() {
			$("#order-dish-table tbody tr").not(".hide").eq(0).addClass(
					"selected");
		}
	});
}
// 已选优惠分页
function page2(currPage) {
	nowPage2 = loadPage({
		obj : "#sel-preferential-table tbody tr",
		listNum : 6,
		currPage : currPage,
		totleNums : $("#sel-preferential-table tbody tr").length,
		curPageObj : "#order-modal #curr-page2",
		pagesLenObj : "#order-modal #pages-len2",
		prevBtnObj : "#order-modal .preferential-oper-btns .prev-btn",
		nextBtnObj : "#order-modal .preferential-oper-btns .next-btn"
	});
}
// 优惠分页
function page3(currPage) {
	nowPage3 = loadPage({
		obj : ".preferentials-content .preferential-info",
		listNum : 16,
		currPage : currPage,
		totleNums : $(".preferentials-content .preferential-info").length,
		curPageObj : "#order-modal #curr-page3",
		pagesLenObj : "#order-modal #pages-len3",
		prevBtnObj : "#order-modal .main-div .prev-btn",
		nextBtnObj : "#order-modal .main-div .next-btn"
	});
}
function initOrderDish() {
	for (var i = 0; i < 14; i++) {
		var tr = "";
		tr = "<tr><td>菜品" + i + "</td><td>1</td><td>49</td></tr>";

		$("#order-dish-table tbody").prepend(tr);
	}
	page1(0);
}
// 优惠分类
function initPreferentialType() {
	var htm = '';
	for (var i = 0; i < 20; i++) {
		var cla = "";
		if (i == 0)
			cla = "active";
		htm += '<li class="nav-pref-type ' + cla + '">优惠' + i + '</li>';
	}
	$(".nav-pref-types").html(htm);
	initPreferential();
	$(".nav-pref-types li.nav-pref-type").click(function() {
		$(".nav-pref-types li.nav-pref-type").removeClass("active");
		$(this).addClass("active");
		initPreferential();
	});
}
// 通过分类获取菜品信息
function initPreferential() {
	var htm = '';
	for (var i = 0; i < 20; i++) {
		htm += '<div class="preferential-info" dishid="dish-id-' + i
				+ '" dishname="菜品' + i + '" price="49">'
				+ '<div class="dish-name">优惠活动' + i + '</div>' + '</div>';
	}
	$(".main-div .preferentials-content").html(htm);
	page3(nowPage3);
	$(".preferentials-content .preferential-info").click(
			function() {
				var tr = "";
				var dishname = $(this).attr("dishname");
				var price = $(this).attr("price");
				tr = "<tr><td>" + dishname + "</td><td>1</td><td>" + price
						+ "</td></tr>";

				$("#sel-preferential-table tbody").prepend(tr);
				$("#sel-preferential-table tbody tr").removeClass("selected");
				page2(nowPage2);

				// 选中已选优惠
				$(".sel-preferential-table tbody tr").click(
						function() {
							$(".sel-preferential-table tbody tr").removeClass(
									"selected");
							$(this).addClass("selected");
						});
			});
}
// 切换小键盘
function changeKeyboard(type) {
	if (type == "num") {
		$("#num-keyboard").removeClass("hide");
		$("#letter-keyboard").addClass("hide");
	} else if (type == "letter") {
		$("#num-keyboard").addClass("hide");
		$("#letter-keyboard").removeClass("hide");
	}
}
// 点菜
function takeOrder() {
	$("#adddish-dialog").load(global_path+"/views/orderdish.jsp");
	$("#adddish-dialog").modal("show");
}
// 确认开台
function confirmOpen() {
	$("#open-dialog").modal("hide");
	$("#adddish-dialog").load(global_path+"/views/orderdish.jsp");
	$("#adddish-dialog").modal("show");
}