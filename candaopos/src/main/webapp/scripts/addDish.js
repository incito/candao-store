var nowPage4 = 0;// 已选择菜品分页
var nowPage5 = 0;// 待选择菜品分页


var dishMap = null;//添加进购物车的菜品

var tastDish = {};

var AddDish = {

	init: function () {

		//设置全局订单信息
		$("#order-dialog").find('.J-order-id').text(MainPage.orderInfo.orderId)
			.end().find('.J-table-no').text(MainPage.orderInfo.tableNo)
			.end().find('.J-person-no').text(MainPage.orderInfo.personNo);

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
		 * 搜索事件
		 */
		$(".J-search .btn-clear").click(function () {
			$(".search input[type='search']").val("");
		});

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
			that.renderDishes();
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
						console.log(v);
						var cla = "";
						if (k == 0)
							cla = "active";
						htm += '<li class="nav-dish-type ' + cla + '" itemid="' + v.itemid + '"  itemsort="' + v.itemsort + '">' + v.itemdesc + '</li>';
					});
					$(".nav-dish-types").html(htm);
					that.renderDishes();
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
	// 通过分类获取菜品信息
	renderDishes: function () {
		var that = this
		$.ajax({
			url: _config.interfaceUrl.GetAllDishInfos + '/' + utils.storage.getter('aUserid') + '/',
			method: 'GET',
			contentType: "application/json",
			dataType:'json',
			success: function(res){
				console.log(res);
			}
		});
	}
}



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

	page4(0);
	//renderDishType();
	
	$("#updatenum-dialog input").focus(function(){
		activeinputele = $(this);
	});
	//
	$("#updatenum-dialog .virtual-keyboard li").click(function(){
		var keytext = $(this).text();
		if(activeinputele != null && activeinputele != undefined){
			if(keytext == "←"){
				activeinputele.focus();
				backspace();
			}else{
				var val = activeinputele.val();
				val = val + keytext;
				activeinputele.val(val);
				activeinputele.focus();
			}
		}
	});
});
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
// 已点菜品分页
function page4(currPage) {
	nowPage4 = widget.loadPage({
		obj : "#sel-dish-table tbody tr",
		listNum : 16,
		currPage : currPage,
		totleNums : $("#sel-dish-table tbody tr").length,
		curPageObj : "#adddish-modal #curr-page",
		pagesLenObj : "#adddish-modal #pages-len",
		prevBtnObj : "#adddish-modal .oper-div .prev-btn",
		nextBtnObj : "#adddish-modal .oper-div .next-btn",
		callback : function() {
			$("#sel-dish-table tbody tr").removeClass("selected");
			$("#sel-dish-table tbody tr").not(".hide").eq(0).addClass("selected");
		}
	});
}
// 菜品分页
function page5(currPage) {
	nowPage5 = widget.loadPage({
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

/**
 * 购物车添加菜品
 * @param dish
 */
function addDish(dish){
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
	page4(nowPage4);
	//更新总消费金额
	updateTotalAmount();
	controlBtns();
	// 选中已点菜品
	$("#sel-dish-table tbody tr").click(function() {
		$("#sel-dish-table tbody tr").removeClass("selected");
		$(this).addClass("selected");
	});
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
	var type = $("#note-type").val();
	var dishtype = $("#note-dialog #dish-type").val();
	if(type == 1){
		//全单
		$(".note-div").removeClass("hide");
		$("#order-note").attr("note", note);
		$("#order-note").attr("sel-note", n2);
		$("#order-note").text(note1);
	}else {
		if(type == 0){
			//单品备注
			var dishid = $("#note-dialog #dish-id").val();
			var dish = dishMap.get(dishid);
			var dishname = dish.dishname;
			
			$("#sel-dish-table tbody tr.selected").find("td.dishname").html(dishname+"("+note1+")");
			dish.dishnote = note;
			dish.dish_avoids = dish_avoids;
			dishMap.put(dishid, dish);
		}else if(type == 2){
			console.log(tastDish);
			//多口味菜品选择
			tastDish.dishnote = note;
			tastDish.taste = $("#note-dialog #taste ul li.active").text().trim();
			tastDish.dish_avoids = dish_avoids;
			addDish(tastDish);
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
		page4(nowPage4);
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
 * 更新总消费额
 */
function updateTotalAmount(){
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
	$("#placeorder-confirm-dialog").modal("show");
}
/**
 * 确认下单
 */
function doPlaceOrder() {
	//......调下单的接口
	
	refreshOrder();
	$("#tips-dialog #tips-msg").text("下单成功！");
	$("#tips-dialog").modal("show");
}
/**
 * 刷新订单
 */
function refreshOrder(){
	if(dishMap != null && dishMap.size()>0){
		var keys = dishMap.keySet();
		$.each(keys, function(i, key){
			var dish = dishMap.get(key);
			var totalPrice = calTotalPrice(dish.dishnum, dish.price);
			var tr = "<tr dishid='"+dish.dishid+"' price="+dish.price+">"
				+ "<td class='dishname' name='"+dish.dishname+"' note='"+dish.dishnote+"'>"+dish.dishname+"</td>"
				+ "<td class='num'>"+dish.dishnum+"</td>"
				+ "<td class='price'>"+totalPrice+"</td>"
				+ "</tr>";
			$("#order-dish-table tbody").append(tr);
		});
		page1(nowPage1);
		trClickEvent();
	}
}
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
	$("#givefood-dialog").modal("show");
}
/**
 * 赠菜操作  调接口
 */
function doGive(){
	// ....调赠菜接口
	$("#givefood-dialog").modal("hide");
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