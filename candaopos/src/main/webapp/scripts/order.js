var nowPage1 = 0;// 已选择菜品分页
var nowPage2 = 0;// 已选择优惠分页
var nowPage3 = 0;// 待选优惠分页
var pref_prev = 0;


var Order = {

	init: function () {

		this.initPreferentialType();
		this.bindEvent();

		$("#order-dialog").find('.J-order-id').text(MainPage.orderInfo.orderId)
			.end().find('.J-table-no').text(MainPage.orderInfo.tableNo)
			.end().find('.J-person-no').text(MainPage.orderInfo.personNo);
	},


	bindEvent: function () {

		var that = this;
		var dom = {
			orderDialog : $("#order-dialog"),
			openDialog : $("#open-dialog"),//开台权限验证弹窗
			addDishDialog : $("#adddish-dialog"),//点菜弹窗
		};

		/**
		 * 开启服务员权限验证
		 */
		dom.openDialog.on('click', '.age-type>div', function(){
			var me = $(this);
			me.toggleClass('active');
		});

		dom.openDialog.on('change', '.J-male-num, .J-female-num', function(){
			var maleNum = dom.openDialog.find('.J-male-num').val() === '' ? '0' : dom.openDialog.find('.J-male-num').val();
			var femailNum = dom.openDialog.find('.J-female-num').val() === '' ? '0' : dom.openDialog.find('.J-female-num').val();
			$('.J-tableware-num').val(parseInt(maleNum,10) + parseInt(femailNum,10))
		});

		dom.openDialog.on('click','.J-btn-submit', function(){
			var serverName = dom.openDialog.find('.J-server-name').val();
			var maleNum = dom.openDialog.find('.J-male-num').val() === '' ? '0' : dom.openDialog.find('.J-male-num').val();
			var femailNum = dom.openDialog.find('.J-female-num').val() === '' ? '0' : dom.openDialog.find('.J-female-num').val();

			if(parseInt(maleNum,10) + parseInt(femailNum,10) < 1) {
				widget.modal.alert({
					cls: 'fade in',
					content:'<strong>请输入就餐人数</strong>',
					width:500,
					height:500,
					btnOkTxt: '确定',
					btnCancelTxt: ''
				});
				return false;
			}

			if(serverName === undefined || serverName === '') {
				widget.modal.alert({
					cls: 'fade in',
					content:'<strong>请输入服务员编号</strong>',
					width:500,
					height:500,
					btnOkTxt: '确定',
					btnCancelTxt: ''
				});
				return false;
			}

			//验证用户权限,然后开台
			that.verifyUser(serverName, function(){
				$.ajax({
					url: _config.interfaceUrl.OpenTable,
					method: 'POST',
					contentType: "application/json",
					data: JSON.stringify({
						tableNo: MainPage.orderInfo.tableNo,
						ageperiod: (function(){
							var str = '';
							dom.openDialog.find('.age-type>div').each(function(){
								var me = $(this);
								if(me.hasClass('active')){
									str += me.index()+1;
								}
							});
							return str;
						})(),
						username: serverName,
						manNum: maleNum,
						womanNum: femailNum
					}),
					dataType:'json',
					success: function(res){
						if(res.code === '0') {
							console.log(res.data);
							dom.openDialog.modal('hide');

							//设置订单信息
							MainPage.orderInfo.orderId = res.data.orderid;

							that.takeOrder();

						} else {
							widget.modal.alert({
								cls: 'fade in',
								content:'<strong>' + res.msg + '</strong>',
								width:500,
								height:500,
								btnOkTxt: '',
								btnCancelTxt: '确定'
							});
						}
					}
				})

			});
		});

		/**
		 * 优惠操作
		 */
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

	},

	//切换小键盘
	changeKeyboard: function (type) {
		if (type == "num") {
			$("#num-keyboard").removeClass("hide");
			$("#letter-keyboard").addClass("hide");
		} else if (type == "letter") {
			$("#num-keyboard").addClass("hide");
			$("#letter-keyboard").removeClass("hide");
		}
	},

	//验证用户
	verifyUser: function(serverName, cb){
		$.ajax({
			url: _config.interfaceUrl.VerifyUser,
			method: 'POST',
			contentType: "application/json",
			data: JSON.stringify({
				loginType: '030101',
				username: serverName
			}),
			dataType:'json',
			success: function(res){
				if(res.code === '0') {
					var alertModal = widget.modal.alert({
						cls: 'fade in',
						content:'<strong>' + '确认开台' + '</strong>',
						width:500,
						height:500,
						btnOkCb: function(){
							alertModal.close();
							cb && cb();
						}
					});
				} else {
					widget.modal.alert({
						cls: 'fade in',
						content:'<strong>' + res.msg + '</strong>',
						width:500,
						height:500,
						btnOkTxt: '',
						btnCancelTxt: '确定'
					});
				}
			}
		})
	},

	//点菜
	takeOrder: function(){
		var addDishDialog = $("#order-dialog")
		addDishDialog.load("../views/orderdish.jsp");
		addDishDialog.modal("show");
	},

	/**
	 * 优惠
	 */

	//加载优惠分类
	initPreferentialType: function () {
		var that = this;


		//$.ajax({
		//	url: _config.interfaceUrl.GetCouponInfos,
		//	method: 'POST',
		//	contentType: "application/json",
		//	data: JSON.stringify({
		//		tableType: [1,0,5]
		//	}),
		//	dataType:'json',
		//	success: function(res){
        //
		//		var tables = _getTablesArr(res);
		//		var navRoomTypesArr = [];
		//		var navRoomTypes = $('#nav-room-types');
        //
		//		//设置餐桌统计
		//		$('.J-table-nums').find('.all .num').text(tables.all.length)
		//			.end().find('.free .num').text(tables.free.length)
		//			.end().find('.opened .num').text(tables.opened.length);
        //
		//		//设置区域
		//		if(navRoomTypes.attr('inited') !== 'true'){
		//			navRoomTypesArr.push('<li class="active" areaid="-1">全部</li>')
		//			$.each(res, function(key, val){
		//				navRoomTypesArr.push('<li areaid="' + val.areaid  + '">' + val.areaname  + '</li>');
		//			});
		//			navRoomTypes.attr('inited', 'true');
		//			navRoomTypes.html(utils.array.unique(navRoomTypesArr).join(''));
		//		}
        //
		//		if(tables[type].length == 0) {
		//			$('#J-table-pager').html('');
		//			$("#standard-tables").html('');
		//		} else {
		//			//初始化分页
		//			TablePager = $('#J-table-pager').pagination({
		//				dataSource: tables[type],
		//				pageSize: 40,
		//				showPageNumbers: false,
		//				showNavigator: true,
		//				callback: function(data) {
		//					$("#standard-tables").html(data.join(''));
		//				}
		//			});
		//		}
        //
		//	}
		//});


		var htm = '';
		for (var i = 0; i < 20; i++) {
			var cla = "";
			if (i == 0)
				cla = "active";
			htm += '<li class="nav-pref-type ' + cla + '">优惠' + i + '</li>';
		}
		$(".nav-pref-types").html(htm);
		that.initPreferential();
		$(".nav-pref-types li.nav-pref-type").click(function () {
			$(".nav-pref-types li.nav-pref-type").removeClass("active");
			$(this).addClass("active");
			that.initPreferential();
		});
	},
	//通过分类获取优惠券信息
	initPreferential: function () {
		var htm = '';
		for (var i = 0; i < 20; i++) {
			htm += '<div class="preferential-info" dishid="dish-id-' + i
				+ '" prefname="优惠活动' + i + '" price="49">'
				+ '<div class="dish-name">优惠活动' + i + '</div>' + '</div>';
		}
		$(".main-div .preferentials-content").html(htm);
		page3(nowPage3);
		$(".preferentials-content .preferential-info").click(function () {
			$("#pref-name").val($(this).attr("prefname"));
			$("#pref-price").val($(this).attr("price"));
			$("#coupnum-dialog").modal("show");
		});
	},
	/**
	 * 添加优惠
	 */
	addPref: function () {
		var tr = "";
		var name = $("#pref-name").val();
		var price = $("#pref-price").val();
		var num = $("#pref-num").val();
		if (num == null || num == "") {
			num = 1;
		}
		tr = "<tr><td>" + name + "</td><td>" + num + "</td><td>" + price
			+ "</td></tr>";

		$("#sel-preferential-table tbody").prepend(tr);
		page2(nowPage2);
		$("#coupnum-dialog").modal("hide");
		// 选中已选优惠
		$("#sel-preferential-table tbody tr").unbind("click").on("click", function () {
			$("#sel-preferential-table tbody tr").removeClass("selected");
			$(this).addClass("selected");
		});
	},
	/**
	 * 优惠购物车操作
	 */
	controlOperBtns: function () {
		if ($("#sel-preferential-table tbody tr.selected").length > 0) {
			$("#del-pref").removeClass("disabled");
			$("#clear-pref").removeClass("disabled");
		} else {
			$("#del-pref").addClass("disabled");
			$("#clear-pref").addClass("disabled");
		}

	}
};



$(document).ready(function(){

	Order.init();

	$("img.img-close").hover(function(){
	 	$(this).attr("src","../images/close-active.png");
	},function(){
		$(this).attr("src","../images/close-sm.png");
	});
	//未开过台的先开台
	//if(!g_isopened){
	//	$("#open-dialog").modal("show");
	//}

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

	//initPreferentialType();
	initOrderDish();

	$(document).click(function(e){
		$(".more-oper").addClass("hide");
		e.stopPropagation();
	});
	$(".show-more").click(function(e){
		$(".more-oper").removeClass("hide");
		e.stopPropagation();
	});
	//退菜
	$("#back-dish").click(function(){
		backFood(0);
	});
	//称重
	$("#weigh-dish").click(function(){
		$("#weight-dialog").modal("show");
	});
	//删除购物车优惠
	$("#del-pref").click(function(){
		$("#sel-preferential-table tbody tr.selected").remove();
		page2(nowPage2);
	});
	//清空购物车优惠
	$("#clear-pref").click(function(){
		$("#sel-preferential-table tbody tr").remove();
		page2(0);
	});
});
/**
 * 选择银行
 */
function selectBank(){
	$("#select-bank-dialog").modal("show");
	$(".bank-icon img").unbind("click").on("click", function(){
		$(".bank-icon img").removeClass("active");
		$(this).addClass("active");
	});
}
/**
 * 重印客用单
 */
function reprint(){
	$("#tips-dialog #tips-msg").text("客用单打印完成！");
	$("#tips-dialog").modal("show");
}
/**
 * 取消订单
 */
function cancelOrder(){
	$("#tips-dialog #tips-msg").text("只能取消空账单！");
	$("#tips-dialog").modal("show");
}
/**
 * 退菜
 * @param type： 0：单个退菜；1:整单退菜
 */
function backFood(type){
	$("#backfood-dialog").modal("show");
	$("#backfood-dialog .avoid").unbind("click").on("click", function(){
		if($(this).hasClass("active")){
			$(this).removeClass("active");
		}else{
			$(this).addClass("active");
		}
	});
}
// 已点菜品分页
function page1(currPage) {
	nowPage1 = widget.loadPage({
		obj : "#order-dish-table tbody tr",
		listNum : 6,
		currPage : currPage,
		totleNums : $("#order-dish-table tbody tr").length,
		curPageObj : "#order-modal #curr-page1",
		pagesLenObj : "#order-modal #pages-len1",
		prevBtnObj : "#order-modal .dish-oper-btns .prev-btn",
		nextBtnObj : "#order-modal .dish-oper-btns .next-btn",
		callback : function() {
			$("#order-dish-table tbody tr").removeClass("selected");
			$("#order-dish-table tbody tr").not(".hide").eq(0).addClass(
					"selected");
		}
	});
}
// 已选优惠分页
function page2(currPage) {
	nowPage2 = widget.loadPage({
		obj : "#sel-preferential-table tbody tr",
		listNum : 6,
		currPage : currPage,
		totleNums : $("#sel-preferential-table tbody tr").length,
		curPageObj : "#order-modal #curr-page2",
		pagesLenObj : "#order-modal #pages-len2",
		prevBtnObj : "#order-modal .preferential-oper-btns .prev-btn",
		nextBtnObj : "#order-modal .preferential-oper-btns .next-btn",
		callback : function() {
			$("#sel-preferential-table tbody tr").removeClass("selected");
			$("#sel-preferential-table tbody tr").not(".hide").eq(0).addClass(
					"selected");
		}
	});
	Order.controlOperBtns();
}
// 优惠分页
function page3(currPage) {
	nowPage3 = widget.loadPage({
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

function trClickEvent(){
	// 选中已点菜品
	$("#order-dish-table tbody tr").click(function() {
		$("#order-dish-table tbody tr").removeClass("selected");
		$(this).addClass("selected");
	});
}
function initOrderDish() {
	for (var i = 0; i < 3; i++) {
		var tr = "";
		tr = "<tr dishid='dish-id-"+i+"' price=49><td class='dishname'>菜品" + i + "</td><td class='num'>1</td><td class='price'>49</td></tr>";

		$("#order-dish-table tbody").prepend(tr);
	}
	page1(0);
	trClickEvent();
}
// 优惠分类
//function initPreferentialType() {
//	var htm = '';
//	for (var i = 0; i < 20; i++) {
//		var cla = "";
//		if (i == 0)
//			cla = "active";
//		htm += '<li class="nav-pref-type ' + cla + '">优惠' + i + '</li>';
//	}
//	$(".nav-pref-types").html(htm);
//	initPreferential();
//	$(".nav-pref-types li.nav-pref-type").click(function() {
//		$(".nav-pref-types li.nav-pref-type").removeClass("active");
//		$(this).addClass("active");
//		initPreferential();
//	});
//}
// 通过分类获取优惠券信息
//function initPreferential() {
//	var htm = '';
//	for (var i = 0; i < 20; i++) {
//		htm += '<div class="preferential-info" dishid="dish-id-' + i
//				+ '" prefname="优惠活动' + i + '" price="49">'
//				+ '<div class="dish-name">优惠活动' + i + '</div>' + '</div>';
//	}
//	$(".main-div .preferentials-content").html(htm);
//	page3(nowPage3);
//	$(".preferentials-content .preferential-info").click( function() {
//		$("#pref-name").val($(this).attr("prefname"));
//		$("#pref-price").val($(this).attr("price"));
//		$("#coupnum-dialog").modal("show");
//	});
//}
///**
// * 添加优惠
// */
//function addPref(){
//	var tr = "";
//	var name = $("#pref-name").val();
//	var price = $("#pref-price").val();
//	var num = $("#pref-num").val();
//	if(num == null || num== ""){
//		num = 1;
//	}
//	tr = "<tr><td>" + name + "</td><td>"+num+"</td><td>" + price
//			+ "</td></tr>";
//
//	$("#sel-preferential-table tbody").prepend(tr);
//	page2(nowPage2);
//	$("#coupnum-dialog").modal("hide");
//	// 选中已选优惠
//	$("#sel-preferential-table tbody tr").unbind("click").on("click", function(){
//		$("#sel-preferential-table tbody tr").removeClass( "selected");
//		$(this).addClass("selected");
//	});
//}
///**
// * 优惠购物车操作
// */
//function controlOperBtns(){
//	if($("#sel-preferential-table tbody tr.selected").length > 0){
//		$("#del-pref").removeClass("disabled");
//		$("#clear-pref").removeClass("disabled");
//	}else{
//		$("#del-pref").addClass("disabled");
//		$("#clear-pref").addClass("disabled");
//	}
//
//}


// 确认开台
function confirmOpen() {
	$("#open-dialog").modal("hide");
	$("#adddish-dialog").load("../views/orderdish.jsp");
	$("#adddish-dialog").modal("show");
}
/**
 * 退菜
 */
function inputBackReason(){
	$("#backreasoninput-dialog").modal("show");
}
var count = 20;
function inputBackReason(){
	var reason = $("#backfood-reason").val();
	$("#backreason-inp").val(reason);
	$("#backreasoninput-dialog").modal("show");
	$("#backreason-inp").focus();
}
function changeBackReaCount(){
	var value = $("#backreason-inp").val();
	var c = count;
	if(value != null && value != ""){
		c = count-value.length;
	}
	if(c <=0){
		c = 0;
	}
	$("#backreason-count").text(c);
	contrlClearBtn(value);
}
function changeBackReason(){
	var reason = $("#backreason-inp").val();
	$("#backfood-reason").val(reason);
	$("#backreasoninput-dialog").modal("hide");
}
/**
 * 清空输入的退菜原因
 */
function clearBackReasonInput(){
	$("#backreason-inp").val("");
	contrlClearBtn("");
}
/**
 * 控制清空按钮
 * @param value
 */
function contrlClearBtn(value){
	if(value != null && value != ""){
		$(".clear-btn").removeClass("disabled");
	}else{
		$(".clear-btn").addClass("disabled");
	}
}
//关闭dialog
function closeConfirm(dialogId) {
	$("#" + dialogId).modal("hide");
}
function stoppro(evt){
	var e=evt?evt:window.event; //判断浏览器的类型，在基于ie内核的浏览器中的使用cancelBubble
	if (window.event) {
		e.cancelBubble=true;
	} else {
	    e.preventDefault(); //在基于firefox内核的浏览器中支持做法stopPropagation
		e.stopPropagation();
	}
}