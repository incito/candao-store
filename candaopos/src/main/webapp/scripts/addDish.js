
var dishMap = null;//添加进购物车的菜品

var g_eatType = "EAT-IN";

var tastDish = {};

var dom = {
	addDishModal : $("#adddish"),
	searchBtn: $("#adddish").find('input[type=search]'),
	selDishable : $("#sel-dish-table")
};

var consts = {
	orderid: $('input[name=orderid]').val(),
	tableno: $('input[name=tableno]').val(),
	personnum: $('input[name=personnum]').val(),
};

var AddDish = {

	//所有菜品缓存数据
	dishesData: [],

	init: function () {
		//设置全局订单信息
		$(".order-info").find('.J-order-id').text(consts.orderid)
			.end().find('.J-table-no').text(consts.tableno)
			.end().find('.J-person-no').text(consts.personnum);

		//搜索键盘初始化
		widget.keyboard({
			target: '.search-btns',
			chirdSelector: 'div'
		});

		this.renderDishType();

		this.bindEvent();
	},

	bindEvent: function () {
		var that = this;

		/**
		 * 菜品分类事件
		 * @type {*|jQuery|HTMLElement}
         */
		var $dishType = $('.dish-type');
		var $navDishTypes = $(".nav-dish-types");
		var flag_prev = 0;
		$navDishTypes.delegate('li.nav-dish-type', 'click', function () {
			var me = $(this);
			me.siblings().removeClass("active").end().addClass("active");
			that.renderDishes(me.attr('itemid'));
		});

		//菜品分类向左向右按钮
		$(".nav-dishtype-next").click(function(){
			var count = $dishType.find( "li.nav-dish-type").length;
			if (flag_prev < count - 6) {
				$dishType .find("li.nav-dish-type").eq(flag_prev).css("margin-left", "-16.66%");
				$dishType .find("li.nav-dish-type").eq(flag_prev+1).click();
				flag_prev++;
			}
		});
		$(".nav-dishtype-prev").click(function(){
			if(flag_prev>=1){
				$dishType.find("li.nav-dish-type").eq(flag_prev-1).css("margin-left","0");
				$dishType.find("li.nav-dish-type").eq(flag_prev-1).click();
				flag_prev--;
			}
		});

		/**
		 * 菜品搜索事件
		 */
		dom.searchBtn.bind('input propertychange focus', function(){
			that.renderDishes($('.nav-dish-types li.active').attr('itemid'), $(this).val());
		});

		dom.addDishModal.find('.btn-clear').click(function(){
			dom.searchBtn.val('');
			that.renderDishes($('.nav-dish-types li.active').attr('itemid'));
		});

		/**
		 * 菜品选择
		 */
		dom.addDishModal.delegate((".dishes-content .dish-info"), 'click', function () {
			var me = $(this);
			var dish = {
				dishid: me.attr("dishid"),
				dishname: me.attr("dishname"),
				unit: me.attr("unit"),
				price: me.attr("price"),
				dishtype: me.attr("dishtype"),
				dishnum: 1,
				dishnote: ""
			};

			$.ajax({
				url: _config.interfaceUrl.GetDishStatus + '/' + dish.dishid + '/',
				method: 'POST',
				contentType: "application/json",
				dataType:'text',
				data: JSON.stringify({
					dishUnit: dish.unit
				})
			}).then(function(res){
				var result = JSON.parse(res.substring(12, res.length-3));

				if(result.Data === '1') {//接口返回成功
					if(result.Info === '0') {//菜品状态正常
						if (dish.dishtype == "0") {
							//普通菜品
							var f = isExist(dish.dishid);
							if (f) {
								//已存在该菜品
								var $tr = null;
								dom.selDishable.find('tbody tr').each(function () {
									var dishId = $(this).attr("dishid");
									if (dishId == dish.dishid) {
										$tr = $(this);
										return;
									}
								});
								var seldish = dishMap.get(dish.dishid);
								var num = seldish.dishnum;
								num = parseFloat(num) + 1;
								$tr.find("td.num").text(num);
								var totalPrice = calTotalPrice(num, dish.price);
								$tr.find("td.price").text(totalPrice);
								seldish.dishnum = num;
								dishMap.put(dish.dishid, seldish);

								//更新总消费金额
								updateTotalAmount();

							} else {
								that.addDish(dish);
							}
						} else if (dish.dishtype == "1") {
							//多口味菜品
							tastDish = dish;
							initNoteDialog(2);
						} else if (dish.dishtype == "2") {
							//套餐
							$("#combodish-dialog").modal("show");
							$("#combodish-dialog .num-btns .num-btn").unbind("click").on("click", function () {

							});

							$("#combodish-dialog .avoid").unbind("click").on("click", function () {
								if ($(this).hasClass("active")) {
									$(this).removeClass("active");
								} else {
									$(this).addClass("active");
								}
							});
						} else if (dish.dishtype == "3") {
							//鱼锅
							$("#fishpotdish-dialog").modal("show");
							$("#fishpotdish-dialog .avoid").unbind("click").on("click", function () {
								if ($(this).hasClass("active")) {
									$(this).removeClass("active");
								} else {
									$(this).addClass("active");
								}
							});
						}
					} else {
						//菜品沽清
						widget.modal.alert({
							cls: 'fade in',
							content:'<strong>菜品沽清</strong>',
							width:500,
							height:500,
							btnOkTxt: '',
							btnCancelTxt: '确定'
						});
					}
				} else {
					widget.modal.alert({
						cls: 'fade in',
						content:'<strong>获取菜品状态失败!</strong>',
						width:500,
						height:500,
						btnOkTxt: '',
						btnCancelTxt: '确定'
					});
				}
			});
		});

		// 选中已点菜品
		dom.addDishModal.delegate("#sel-dish-table tbody tr",'click', function() {
			$(this).siblings().removeClass("selected").end().addClass("selected");
		});
	},

	//获取菜品分类
	renderDishType: function () {
		var that = this
		$.ajax({
			url: _config.interfaceUrl.GetDishGroupInfos,
			method: 'POST',
			contentType: "application/json",
			dataType:'json',
			success: function(res){

				if(res.code === '0') {
					var htm = '';
					$.each(res.data, function(k,v){
						var cla = "";
						if (k == 0) {
							cla = "active";
							that.renderDishes(v.itemid);
						}

						htm += '<li class="nav-dish-type ' + cla + '" itemid="' + v.itemid + '"  itemsort="' + v.itemsort + '">' + v.itemdesc + '</li>';
					});
					$(".nav-dish-types").html(htm);

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
		});
	},

	/**
	 * 渲染菜品信息
	 * @param id	菜品分类id
	 * @param key 搜索字符串(不传时,需直接从dom中获取key)
     */
	renderDishes: function (id, key) {
		var that = this;
		var searchKey = !key ? $.trim(dom.searchBtn.val()) : key;

		if(that.dishesData.length > 0) {//从缓存中获取数据
			var oBuffer = new utils.string.buffer();
			$.each(that.dishesData, function(k, v){
				if(v.source === id && (v.py.indexOf(searchKey.toUpperCase()) !== -1)) {
					oBuffer.append('<div class="dish-info" dishtype=' + v.dishtype + ' dishid="' + v.dishid + '" py="' + v.py + '" dishname="' + v.title + '" price="' + v.price + '" unit="' + v.unit + '">'
						+ '<div class="dish-name">' + v.title
						+ '</div>'
						+ '<hr>'
						+ '<div class="dish-price">' + v.price + '/' + v.unit + '</div>'
						+ '</div>');
				}

			});

			$(".main-div .dishes-content").html(oBuffer.toString());

			widget.loadPage({
				obj : ".dishes-content .dish-info",
				listNum : 20,
				currPage : 0,
				totleNums : $(".dishes-content .dish-info").length,
				curPageObj : "#adddish #curr-page1",
				pagesLenObj : "#adddish #pages-len1",
				prevBtnObj : "#adddish .main-div .prev-btn",
				nextBtnObj : "#adddish .main-div .next-btn"
			});
		} else {//同步获取数据(打开点菜界面时)

			$.ajax({
				url: _config.interfaceUrl.GetAllDishInfos + '/' + utils.storage.getter('aUserid') + '/',
				method: 'GET',
				contentType: "application/json",
				dataType:'text',
				success: function(res){
					var htm = '';
					var result = JSON.parse(res.substring(12, res.length-3));

					if(result.Data === '1') {

						that.dishesData = result.OrderJson;
						that.renderDishes(id);

					} else {
						widget.modal.alert({
							cls: 'fade in',
							content:'<strong>' + res.Info + '</strong>',
							width:500,
							height:500,
							btnOkTxt: '',
							btnCancelTxt: '确定'
						});
					}
				}
			});
		}

	},

	/**
	 * 购物车添加菜品
	 * @param dish
	 */
	addDish: function (dish){
		var dishid = dish.dishid;
		var dishname = dish.dishname;
		var unit = dish.unit;
		var dishtype = dish.dishtype;
		var price = dish.price;
		var dishnum = dish.dishnum;
		var dishnote = dish.dishnote;

		var dish_avoids = dish.dish_avoids;
		var taste = dish.taste;

		var showname = dishname;
		if(taste != null && taste != ""){
			showname = showname+"("+taste+")";
		}
		var avoidStr = "";
		if(dish_avoids != null && dish_avoids !=""){
			$.each(dish_avoids, function(i, avoid){
				avoidStr += avoid+";";
			});
		}

		if(dishnote != null && dishnote != ""){
			avoidStr += dishnote;
		}

		if(avoidStr != null && avoidStr != ""){
			showname = showname + "("+avoidStr+")";
		}
		var tr = "<tr dishid='"+dishid+"' unit='"+unit+"' dishtype='"+dishtype+"' price="+price+">"
			+ "<td class='dishname' name='"+dishname+"'>"
			+ showname
			+ "</td><td class='num'>"+dishnum+"</td><td class='price'>"
			+ price
			+ "</td></tr>";

		$("#sel-dish-table tbody").prepend(tr);

		dishMap.put(dishid, dish);

		$("#sel-dish-table tbody tr").removeClass("selected");


		widget.loadPage({
			obj : "#sel-dish-table tbody tr",
			listNum : 16,
			currPage : 0,
			totleNums : $("#sel-dish-table tbody tr").length,
			curPageObj : "#adddish #curr-page",
			pagesLenObj : "#adddish #pages-len",
			prevBtnObj : "#adddish .oper-div .prev-btn",
			nextBtnObj : "#adddish .oper-div .next-btn",
			callback : function() {
				$("#sel-dish-table tbody tr").removeClass("selected");
				$("#sel-dish-table tbody tr").not(".hide").eq(0).addClass("selected");
			}
		});

		//更新总消费金额
		updateTotalAmount();
		controlBtns();
	},

};



$(document).ready(function(){


	AddDish.init();

	dishMap = new utils.HashMap();

	if(g_eatType == "TAKE-OUT"){
		//外卖
		$(".give-dish").addClass("hide");
		$(".gua-dan").removeClass("hide");
	}else if(g_eatType == "EAT-IN"){
		//堂食
		$(".gua-dan").addClass("hide");
		$(".give-dish").removeClass("hide");
	}
});

/**
 * 更新总消费额
 */
function updateTotalAmount (){
	var total_amount = 0;
	if(dishMap != null && dishMap.size()>0){
		var keys = dishMap.keySet();
		if(keys != null && keys.length>0){
			$.each(keys, function(i, key){
				var dish = dishMap.get(key);
				var price = dish.price;
				var totalNum = dish.dishnum;
				total_amount += parseFloat(calTotalPrice(totalNum, price));
			});
		}
	}
	$("#total-amount").text(total_amount);
}

/**
 * 控制操作按钮是否可点击
 */
function controlBtns(){
	if($("#sel-dish-table tbody tr.selected").length > 0){
		$(".main-oper-btns .btn").removeClass("disabled");
		$(".oper-div .btns .btn").not(".prev-btn").not(".next-btn").removeClass("disabled");
	}else{
		$(".main-oper-btns .btn").addClass("disabled");
		$(".oper-div .btns .btn").not(".prev-btn").not(".next-btn").addClass("disabled");
	}

}
/**
 * 全单备注
 */
function allNote() {
	initNoteDialog(1);
}
/**
 * 单个菜品备注
 */
function singleNote(){
	initNoteDialog(0);
}
/**
 * 初始化备注dialog
 * @param type:0单品备注；1：全单备注；2：多口味菜品添加
 */
function initNoteDialog(type){
	$("#note-dialog .avoid").removeClass("active");
	$("#note-dialog #dish-note").val("");
	$("#note-dialog #note-type").val(type);
	var dish_avoids = [];//忌口
	var dishnote = "";
	var dishid = "";
	if(type == 1){
		//全单备注
		$("#note-dialog #taste").addClass("hide");
		$("#note-dialog #dish-info").addClass("hide");

		dishnote = $("#order-note").attr("note");
		var sel_note = $("#order-note").attr("sel-note");
		if(sel_note!= null && sel_note!=""){
			var arr = sel_note.split("|");
			$("#note-dialog .avoid").each(function(){
				var text = $(this).text().trim();
				var f = false;
				$.each(arr, function(i, v){
					console.log(v);
					if(text == v){
						f = true;
						return;
					}
				});
				if(f){
					$(this).addClass("active");
				}
			});
		}
	}else{
		$("#note-dialog #dish-info").removeClass("hide");
		if(type == 0){
			dishid = $("#sel-dish-table tbody tr.selected").attr("dishid");
			var dish = dishMap.get(dishid);
			var dishtype = dish.dishtype;
			$("#note-dialog #dish-type").val(dishtype);
			if(dishtype == "1"){
				//多口味
				$("#note-dialog #taste").removeClass("hide");
			}else{
				$("#note-dialog #taste").addClass("hide");
			}
			//单品备注
			var dishname = dish.dishname;
			var price = dish.price;
			$("#note-dishname").text(dishname);
			$("#note-price").text(price);

			dishnote = dish.dishnote;
			dish_avoids = dish.dish_avoids;
		}else if(type == 2){
			dishid = tastDish.dishid;
			//多口味菜添加
			$("#note-dialog #dish-type").val(tastDish.dishtype);
			$("#note-dialog #taste").removeClass("hide");
			$("#note-dishname").text(tastDish.dishname);
			$("#note-price").text(tastDish.price);
		}
		$("#note-dialog #dish-id").val(dishid);
		if(dish_avoids != null && dish_avoids.length >0){
			$.each(dish_avoids, function(i, avoid){
				$("#note-dialog .avoid").each(function(){
					var text = $(this).text().trim();
					if(avoid == text){
						$(this).addClass("active");
					}
				});
			});
		}
	}
	$("#note-dialog #dish-note").val(dishnote);

	$("#note-dialog .avoid").unbind("click").on("click",function(){
		if($(this).hasClass("active")){
			$(this).removeClass("active");
		}else{
			$(this).addClass("active");
		}
	});
	$("#note-dialog .taste ul li").unbind("click").on("click",function(){
		$("#note-dialog .taste ul li").removeClass("active");
		$(this).addClass("active");
	});
	$("#note-dialog").modal("show");
}
/**
 * 确认备注
 */
function doNote(){
	var note = $("#dish-note").val();
	var n1 = "";
	var n2 = "";
	var dish_avoids = [];
	$("#note-dialog .avoid.active").each(function(){
		var avoid = $(this).text();
		dish_avoids.push(avoid);
		n1 += avoid+";";
		n2 += avoid+"|";
	});
	var note1 = n1+note;
	note1 = note1.substring(0, note1.length-1);
	var type = $("#note-type").val();
	var dishtype = $("#note-dialog #dish-type").val();
	if(type == 1){
		//全单
		$(".note-div").removeClass("hide");
		$("#order-note").attr("note", note1);
		//$("#order-note").attr("sel-note", n2);
		$("#order-note").text(note1);
	}else {
		if(type == 0){
			//单品备注
			var dishid = $("#note-dialog #dish-id").val();
			var dish = dishMap.get(dishid);
			var dishname = dish.dishname;

			$("#sel-dish-table tbody tr.selected").find("td.dishname").html(dishname+"("+note1+")");
			$("#sel-dish-table tbody tr.selected").attr('note',note1);
			dish.dishnote = note;
			dish.dish_avoids = dish_avoids;
			dishMap.put(dishid, dish);
		}else if(type == 2){
			console.log(tastDish);
			//多口味菜品选择
			tastDish.dishnote = note;
			tastDish.taste = $("#note-dialog #taste ul li.active").text().trim();
			tastDish.dish_avoids = dish_avoids;
			AddDish.addDish(tastDish);
		}
	}
	$("#note-dialog").modal("hide");
}
var note_count = 20;
function inputNote(){
	var note = $("#dish-note").val();
	$("#note-inp").val(note);
	$("#noteinput-dialog").modal("show");
	$("#note-inp").focus();
}
function changeCount(){
	var value = $("#note-inp").val();
	var c = note_count;
	if(value != null && value != ""){
		c = note_count-value.length;
	}
	if(c <=0){
		c = 0;
	}
	$("#note-count").text(c);
	contrlClearBtn(value);
}
function changeNote(){
	var note = $("#note-inp").val();
	$("#dish-note").val(note);
	$("#noteinput-dialog").modal("hide");
}
/**
 * 清空输入的备注
 */
function clearNoteInput(){
	$("#note-inp").val("");
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

/**
 * 修改菜品数量对话框
 */
function updateNum(){
	if($("#sel-dish-table tbody tr.selected").length>0){
		var dishid = $("#sel-dish-table tbody tr.selected").attr("dishid");
		$("#updatenum-dialog #dish-id").val(dishid);
		var dishname = dishMap.get(dishid).dishname;
		$("#updatenum-dialog #dish-name").text(dishname);
		$("#updatenum-dialog #num").val("");
		$("#updatenum-dialog").modal("show");
	}
}
/**
 * 修改菜品数量
 */
function doUpdateNum(){
	var num = $("#updatenum-dialog #num").val();
	$("#sel-dish-table tbody tr.selected").find("td.num").text(num);
	var dishid = $("#updatenum-dialog #dish-id").val();
	var dish = dishMap.get(dishid);
	console.log(dishid);
	console.log(dish);
	dish.dishnum = num;
	dishMap.put(dishid, dish);
	updateTotalPrice();
	updateTotalAmount();
	$("#updatenum-dialog").modal("hide");
}
/**
 * 加菜
 */
function add(){
	var $tr = $("#sel-dish-table tbody tr.selected");
	var dishid = $tr.attr("dishid");
	var dish = dishMap.get(dishid);
	var totalNum = dish.dishnum;
	totalNum = parseFloat(totalNum) + 1;
	$tr.find("td.num").text(totalNum);
	dish.dishnum = totalNum;
	dishMap.put(dishid, dish);
	updateTotalPrice();
	updateTotalAmount();
}
/**
 * 减菜
 */
function reduct(){
	var $tr = $("#sel-dish-table tbody tr.selected");
	var dishid = $tr.attr("dishid");
	var dish = dishMap.get(dishid);
	var totalNum = dish.dishnum;
	if(parseFloat(totalNum) <= 1){
		//如果数量小于1，则删除本条
		$tr.remove();
		dishMap.remove(dishid);
		//page4(nowPage4);
		controlBtns();
	}else{
		totalNum = parseFloat(totalNum) - 1;
		$tr.find("td.num").text(totalNum);
		dish.dishnum = totalNum;
		dishMap.put(dishid, dish);
		updateTotalPrice();
	}
	updateTotalAmount();
}
/**
 * 计算菜品总价
 * @param totalNum
 * @param price
 */
function calTotalPrice(totalNum, price){
	var totalPrice = parseFloat(totalNum) * parseFloat(price);
	return totalPrice;
}
/**
 * 更新每个菜品的总价格
 */
function updateTotalPrice(){
	var $tr = $("#sel-dish-table tbody tr.selected");
	var dish = dishMap.get($tr.attr("dishid"));
	var totalNum =dish.dishnum;
	var price = dish.price;
	var totalPrice = calTotalPrice(totalNum, price);
	$tr.find("td.price").text(totalPrice);
}

/**
 * 是否已存在，若已存在，只更新数量和金额
 */
function isExist(dishid){
	var f = false;
	if(dishMap!= null && dishMap.size()>0){
		f = dishMap.containsKey(dishid);
	}
	return f;
}
/**
 * 清空已选菜品
 */
function clearSelected() {
	$("#clear-confirm-dialog").modal("show");
}
function doClear() {
	closeConfirm("clear-confirm-dialog");
	$("#sel-dish-table tbody").html("");
	dishMap = new utils.HashMap();
	updateTotalAmount();
	page4(0);
	controlBtns();
	$(".note-div").addClass("hide");
	$("#order-note").attr("note", "");
	$("#order-note").attr("sel-note", "");
	$("#order-note").text("");
}
// 下单
function placeOrder() {
	var modal = $("#placeorder-confirm-dialog");
	modal.find('.tableno').text(consts.tableno);
	$("#placeorder-confirm-dialog").modal("show");
}
/**
 * 确认下单
 */
function doPlaceOrder() {

	doOrder(0)
}

/**
 * 下单
 * @param type 0:普通下单, 1:赠菜下单
 */
function doOrder(type,cb){
	var $dishes = $('#sel-dish-table tbody tr');
	var rows = [];

	if(type === 0) {
		$dishes.each(function(){
			var me = $(this);
			var price = parseInt(me.attr('price'), 10).toFixed(1);
			var dishtype = parseInt(me.attr('dishtype'), 10);
			var dishid = parseInt(me.attr('dishtype'), 10);
			rows.push(
				{
					"printtype": '0',
					"pricetype": '0',//0：普通 1：赠菜
					"orderprice": price, //菜品价格
					"orignalprice": price,//菜品单价
					"dishid": me.attr('dishid'),
					"userName": utils.storage.getter('pos_aUserid'),
					"dishunit": me.attr('unit'),
					"orderid": consts.orderid,
					"dishtype": dishtype,//0：单品 1：鱼锅 2：套餐
					"orderseq": "1",
					"dishnum": parseInt(me.find('.num').text(), 10).toFixed(1),
					"sperequire": me.attr('note'), //忌口信息
					"primarykey": getUuid(),
					"dishstatus": "0",//0：已称重或者不称重 1:未称重
					"ispot": 0, //0:非锅底 1:锅底
					"taste": null,// 点菜口味/临时菜的菜名
					"freeuser": null, // 赠菜人/收银员工号
					"freeauthorize": null, //赠菜授权人工号
					"freereason": null, //赠菜原因
					"dishes": null //套餐中的子菜品
				}
			)
		});

	} else {
		$dishes.each(function(){
			var me = $(this);
			var price = parseInt(me.attr('price'), 10).toFixed(1);
			var dishtype = parseInt(me.attr('dishtype'), 10);
			rows.push(
				{
					"printtype": '0',
					"pricetype": '0',
					"orderprice": 0.0,
					"orignalprice": price,
					"dishid": me.attr('dishid'),
					"userName": utils.storage.getter('pos_aUserid'),
					"dishunit": me.attr('unit'),
					"orderid": consts.orderid,
					"dishtype": dishtype,//0：单品 1：鱼锅 2：套餐
					"orderseq": "1",
					"dishnum": parseInt(me.find('.num').text(), 10).toFixed(1),
					"sperequire": me.attr('note'),
					"primarykey": getUuid(),
					"dishstatus": "0",//0：已称重或者不称重 1:未称重
					"ispot": 0,
					"taste": null,
					"freeuser": utils.storage.getter('pos_aUserid'),
					"freeauthorize": $('#user').val(),
					"freereason": $("#givefood-reason").val(''),
					"dishes": null
				}
			)
		});
	}

	debugger;

	$.ajax({
		url: _config.interfaceUrl.OrderDish,
		method: 'POST',
		contentType: "application/json",
		dataType:'json',
		data:JSON.stringify({
			"currenttableid": consts.tableno,
			"globalsperequire": $('#order-note').text(),
			"orderid": consts.orderid,
			"operationType": 1,
			"sequence": 1,
			"rows": rows
		}),
		success: function(res){
			if(res.code === '0') {
				cb && cb();
				goToOrder();
			}
			widget.modal.alert({
				cls: 'fade in',
				content:'<strong>' + res.msg + '</strong>',
				width:500,
				height:500,
				btnOkTxt: '',
				btnCancelTxt: '确定'
			});
		}
	});
}
//
///**
// * 刷新订单
// */
//function refreshOrder(){
//	if(dishMap != null && dishMap.size()>0){
//		var keys = dishMap.keySet();
//		$.each(keys, function(i, key){
//			var dish = dishMap.get(key);
//			var totalPrice = calTotalPrice(dish.dishnum, dish.price);
//			var tr = "<tr dishid='"+dish.dishid+"' price="+dish.price+">"
//				+ "<td class='dishname' name='"+dish.dishname+"' note='"+dish.dishnote+"'>"+dish.dishname+"</td>"
//				+ "<td class='num'>"+dish.dishnum+"</td>"
//				+ "<td class='price'>"+totalPrice+"</td>"
//				+ "</tr>";
//			$("#order-dish-table tbody").append(tr);
//		});
//		page1(nowPage1);
//		trClickEvent();
//	}
//}

/**
 * 挂单
 */
function guadan(){
	$("#guadan-dialog").modal("show");
}
/**
 * 赠菜
 */
function giveFood() {
	$("#givefood-reason").val('');
	$("#givefood-dialog").modal("show");
}
/**
 * 赠菜操作  调接口
 */
function doGiveRight(){
	$("#givefood-dialog").modal('hide');
	$('#givefood-right').load("./check/impower.jsp",{"title" : "赠菜授权","userRightNo":"030207","cbd":"doGive()"});
	$('#givefood-right').modal('show');
}

function doGive(){
	doOrder(1);
}

var count = 20;
function inputReason(){
	var reason = $("#givefood-reason").val();
	$("#reason-inp").val(reason);
	$("#reasoninput-dialog").modal("show");
	$("#reason-inp").focus();
}
function changeReaCount(){
	var value = $("#reason-inp").val();
	var c = count;
	if(value != null && value != ""){
		c = count-value.length;
	}
	if(c <=0){
		c = 0;
	}
	$("#reason-count").text(c);
	contrlClearBtn(value);
}
function changeReason(){
	var reason = $("#reason-inp").val();
	$("#givefood-reason").val(reason);
	$("#reasoninput-dialog").modal("hide");
}
/**
 * 清空输入的赠菜原因
 */
function clearReasonInput(){
	$("#reason-inp").val("");
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



// 已点菜品分页
function page4(currPage) {
	nowPage4 = widget.loadPage({
		obj : "#sel-dish-table tbody tr",
		listNum : 16,
		currPage : currPage,
		totleNums : $("#sel-dish-table tbody tr").length,
		curPageObj : "#adddish #curr-page",
		pagesLenObj : "#adddish #pages-len",
		prevBtnObj : "#adddish .oper-div .prev-btn",
		nextBtnObj : "#adddish .oper-div .next-btn",
		callback : function() {
			$("#sel-dish-table tbody tr").removeClass("selected");
			$("#sel-dish-table tbody tr").not(".hide").eq(0).addClass("selected");
		}
	});
}

//关闭dialog
function closeConfirm(dialogId) {
	$("#" + dialogId).modal("hide");
}


function getUuid(){
	var len=32;//32长度
	var radix=16;//16进制
	var chars='0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');var uuid=[],i;radix=radix||chars.length;if(len){for(i=0;i<len;i++)uuid[i]=chars[0|Math.random()*radix];}else{var r;uuid[8]=uuid[13]=uuid[18]=uuid[23]='-';uuid[14]='4';for(i=0;i<36;i++){if(!uuid[i]){r=0|Math.random()*16;uuid[i]=chars[(i==19)?(r&0x3)|0x8:r];}}}
	return uuid.join('');
}

function goToOrder(){
	var url = "../views/order.jsp?orderid=" + $('input[name=orderid]').val() + '&personnum=' + $('input[name=personnum]').val() + '&tableno=' + $('input[name=tableno]').val();
	window.location.href = encodeURI(encodeURI(url));
}
