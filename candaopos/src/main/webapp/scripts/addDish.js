var nowPage4 = 0;// 已选择菜品分页
var nowPage5 = 0;// 待选择菜品分页

var activeinputele;
var flag_prev = 0;
var dishMap = null;//添加进购物车的菜品

var tastDish = {};
$(document).ready(function(){
	dishMap = new HashMap();
	$("img.img-close").hover(function(){
	 	$(this).attr("src",global_path+"/images/close-active.png");	 
	},function(){
		$(this).attr("src",global_path+"/images/close-sm.png");
	});
	if(g_eatType == "TAKE-OUT"){
		//外卖
		$(".give-dish").addClass("hide");
		$(".gua-dan").removeClass("hide");
	}else if(g_eatType == "EAT-IN"){
		//堂食
		$(".gua-dan").addClass("hide");
		$(".give-dish").removeClass("hide");
	}
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
			$("#sel-dish-table tbody tr").removeClass("selected");
			$("#sel-dish-table tbody tr").not(".hide").eq(0).addClass("selected");
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
	for (var i = 0; i < 25; i++) {
		var name = "";
		var price = 49;
		var unit = "份";
		var type = 0;
		if(i<5){
			type = 0;
			name = "无口味菜品"+i;
		}else if(i<10){
			type = 1;
			name = "多口味菜品"+i;
		}else if(i<15){
			type = 2;
			name="套餐类菜品"+i;
			unit = "套";
		}else{
			type = 3;
			name="鱼锅类菜品"+i;
		}
		htm += '<div class="dish-info" dishtype='+type+' dishid="dish-id-' + i + '" dishname="'+name+'" price="'+price+'" unit="'+unit+'">' 
			+ '<div class="dish-name">' + name
			+ '</div>' 
			+ '<hr>' 
			+ '<div class="dish-price">'+price+'/'+unit+'</div>'
			+ '</div>';
	}
	$(".main-div .dishes-content").html(htm);
	page5(nowPage5);
	$(".dishes-content .dish-info").click(function() {
		var dishid = $(this).attr("dishid");
		var dishname = $(this).attr("dishname");
		var price = $(this).attr("price");
		var dishtype = $(this).attr("dishtype");
		var unit = $(this).attr("unit");
		var dish = {
				dishid: dishid,
				dishname:dishname,
				unit:unit,
				price:price,
				dishtype:dishtype,
				dishnum:1,
				dishnote:""
		};
		if(dishtype == "0"){
			//普通菜品
			var f = isExist(dishid);
			if(f){
				//已存在该菜品
				var $tr = null;
				$("#sel-dish-table tbody tr").each(function(){
					var dishId = $(this).attr("dishid");
					if(dishId == dishid){
						$tr = $(this);
						return;
					}
				});
				var seldish = dishMap.get(dishid);
				var num = seldish.dishnum;
				num = parseFloat(num)+1;
				$tr.find("td.num").text(num);
				var totalPrice = calTotalPrice(num, dish.price);
				$tr.find("td.price").text(totalPrice);
				seldish.dishnum = num;
				dishMap.put(dishid, seldish);
				//更新总消费金额
				updateTotalAmount();
			}else{
				addDish(dish);
			}
		}else if(dishtype == "1"){
			//多口味菜品
			tastDish = dish;
			initNoteDialog(2);
		}else if(dishtype == "2"){
			//套餐
			$("#combodish-dialog").modal("show");
			$("#combodish-dialog .num-btns .num-btn").unbind("click").on("click", function(){
				
			});
			
			$("#combodish-dialog .avoid").unbind("click").on("click", function(){
				if($(this).hasClass("active")){
					$(this).removeClass("active");
				}else{
					$(this).addClass("active");
				}
			});
		}else if(dishtype == "3"){
			//鱼锅
			$("#fishpotdish-dialog").modal("show");
			$("#fishpotdish-dialog .avoid").unbind("click").on("click", function(){
				if($(this).hasClass("active")){
					$(this).removeClass("active");
				}else{
					$(this).addClass("active");
				}
			});
		}
		
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
	dishMap = new HashMap();
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