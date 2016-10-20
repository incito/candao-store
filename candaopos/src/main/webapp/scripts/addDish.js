
var dishCartMap = null;//添加进购物车的菜品
var dishesMap = null;//菜品

var g_eatType = "EAT-IN";

var tastDish = {};

var dom = {
	addDishModal : $("#adddish"),
	searchBtn: $("#adddish").find('input[type=search]'),
	selDishable : $("#sel-dish-table"),
	numDialog: $("#updatenum-dialog"),//菜品数量对话框
	noteDialog: $('#note-dialog')//备注对话框
};

var consts = {
	orderid: $('input[name=orderid]').val(),
	tableno: $('input[name=tableno]').val(),
	personnum: $('input[name=personnum]').val(),
};

var AddDish = {

	init: function () {

		dishCartMap = new utils.HashMap();
		dishesMap = new utils.HashMap();

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
				dishnote: "",
				itemid: me.attr('itemid'),
				level: me.attr('level')
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
							//是否已存在，若已存在，只更新数量和金额
							var isExist = (function(dishid){
								var  f= false;
								if(dishCartMap!= null && dishCartMap.size()>0){
									f = dishCartMap.containsKey(dishid);
								}
								return f;
							})(dish.dishid)
							if (isExist) {
								//已存在该菜品
								var $tr = null;
								dom.selDishable.find('tbody tr').each(function () {
									var dishId = $(this).attr("dishid");
									if (dishId == dish.dishid) {
										$tr = $(this);
										return;
									}
								});
								var seldish = dishCartMap.get(dish.dishid);
								var num = seldish.dishnum;
								num = parseFloat(num) + 1;
								$tr.find("td.num").text(num);
								var totalPrice = that.calTotalPrice(num, dish.price);
								$tr.find("td.price").text(totalPrice);
								seldish.dishnum = num;
								dishCartMap.put(dish.dishid, seldish);

								//更新总消费金额
								that.updateTotalAmount();

							} else {
								that.addDish(dish);
							}
						} else if (dish.dishtype == "1") {
							//pos 不支持双拼锅
							if(dish.level === '1') {
								widget.modal.alert({
									content:'<strong>POS暂不支持双拼鱼锅点餐,请通过PAD点餐</strong>',
									btnOkTxt: '',
									btnCancelTxt: '确定'
								});
								return false
							}

							$.ajax({
								url: _config.interfaceUrl.GetFishPotDish + '/' + dish.dishid + '/',
								method: 'GET',
								contentType: "application/json",
								dataType:'text'
							}).then(function(res1){
								var res1 = JSON.parse(res1.substring(12, res1.length-3));
								if(res1.Data === '1') {
									that.initFishPotModal(dish.dishid, res1.OrderJson, me.attr('itemid'));
								} else {
									widget.modal.alert({
										content: '<strong>' + (res1.Info === '' ? '请求鱼锅数据接口错误' : res1.Info) + '</strong>',
										btnOkTxt: '',
										btnCancelTxt: '确定'
									});
								}
							});

							//鱼锅

							//$("#fishpotdish-dialog").modal("show");
							//$("#fishpotdish-dialog .avoid").unbind("click").on("click", function () {
							//	if ($(this).hasClass("active")) {
							//		$(this).removeClass("active");
							//	} else {
							//		$(this).addClass("active");
							//	}
							//});

							//多口味菜品
							//tastDish = dish;
							//initNoteDialog(2);
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
			that.controlBtns();
		});
	},

	/**
	 *
	 * @param dishid 鱼锅dishid
	 * @param data 鱼锅下子菜品
	 * @param itemid 菜品分类id
     */
	initFishPotModal: function(dishid, data, itemid){
		var that = this;
		var ret = [];
		var dish = dishesMap.get(dishid);
		var doc = $(document);

		//有口味
		if(dish.imagetitle.length > 0) {
			ret.push('<div class="taste"><h6 style="font-weight: bold; font-size: 16px">选择口味</h6><ul>');
			ret.push((function(){
				var result = '';
				$.each(dish.imagetitle.split(','), function(k,v){
					result += '<li>' + v +'</li>';
				});
				return result;
			})());
			ret.push('</ul></div>');
		}


		//添加锅底和鱼
		ret.push('<div class="fishpot">');
		$.each(data, function(k, v){
			if(v.ispot) {
				ret.push('<h6 style="font-weight: bold; font-size: 16px">锅底</h6>');
				ret.push('<div class="form-group"><div class="col-xs-7 pot" dishid="' + v.dishid + '">' + v.title + '</div></div>');
				ret.push('<h6 style="font-weight: bold; font-size: 16px">鱼</h6>');
			} else {
				ret.push('<div class="form-group">' +
					'<div class="col-xs-7 fish" dishid="' + v.dishid + '"  dishname="' + v.title + '" unit="' + v.unit + '" price="' + v.price + '" dishtype="' + v.dishtype + '">' + v.title  +'</div> <div class="num-oper col-xs-5"> <button class="btn btn-default num-oper-btn plus">-</button> <input type="text" class="form-control num-inp" min="0" value="0"> <button class="btn btn-default num-oper-btn add">+</button> </div> </div>');
			}
		});
		ret.push('</div>');

		//忌口
		ret.push('<h6 style="font-weight: bold; font-size: 16px">忌口</h6>');
		ret.push('<div class="form-group avoids" ><span class="inpt-span">其他忌口：</span><input type="text" class="form-control padding-left" onclick="widget.textAreaModal({ target: $(this), note: $(this).val() }).show();"></div>');

		//绑定事件
		doc.undelegate('.fishpot-dialog .num-oper-btn', 'click');
		doc.delegate('.fishpot-dialog .num-oper-btn', 'click', function(){
			var me = $(this);
			var target = me.siblings('input');
			var val = parseInt(target.val(), 10);
			if(me.hasClass('add')){
				target.val(parseInt(target.val(), 10) + 1);
			} else {
				if(val > 0) {
					target.val(parseInt(target.val(), 10) - 1);
				} else {
					target.val(0);
				}
			}
			_setSubmitStatus();
		});

		doc.undelegate('.fishpot-dialog .taste li', 'click');
		doc.delegate('.fishpot-dialog .taste li', 'click', function(){
			var me = $(this);
			me.addClass('active').siblings().removeClass('active');
			_setSubmitStatus();
		});

		doc.undelegate('.fishpot-dialog .num-inp', 'input propertychange focus');
		doc.delegate('.fishpot-dialog .num-inp', 'input propertychange focus', function(){
			var me = $(this);
			me.val(me.val().replace(/[^\d]/g,''));
			_setSubmitStatus();
		});

		var _setSubmitStatus = function(){
			var btn = $('.fishpot-dialog .btn-base.ok');
			var numInpStatus = (function(){
				var status = false;
				$('.fishpot-dialog .num-inp').each(function(){
					if(parseInt($(this).val(), 10) > 0) {
						status = true;
						return false;
					}
				});

				return  status;
			})();
			if($('.fishpot-dialog .taste li.active').length > 0 && numInpStatus) {
				btn.removeClass('disabled');
			} else {
				btn.addClass('disabled');
			}
		};

		var modalIns = widget.modal.alert({
			cls: 'default-dialog fishpot-dialog',
			content: ret.join(''),
			width: 600,
			title: dish.title,
			//添加鱼锅到购物车
			btnOkCb: function(){

				var dishPotRandomId = getUuid();

				//添加锅
				for(var i = 0; i < data.length ; i++) {
					if(data[i].ispot === 1) {
						that.addDish({
							dishid: data[i].dishid,
							dishname: data[i].title,
							unit: data[i].unit,
							price: data[i].price,
							dishtype: data[i].dishtype,
							dishnum: 1,
							dishnote: "",
						}, dishPotRandomId);
					}
				}

				//添加鱼
				$('.fishpot-dialog .fish').each(function(){
					var me = $(this);
					var numIpt = parseInt(me.next().find('.num-inp').val(), 10);
					if(numIpt > 0) {
						that.addDish({
							dishid: me.attr('dishid'),
							dishname: me.attr('dishname'),
							unit: me.attr('unit'),
							price: me.attr('price'),
							dishtype: me.attr('dishtype'),
							dishnum: numIpt,
							dishnote: ""
						}, dishPotRandomId);
					}
				});

				//添加鱼锅总菜品信息 价格为0
				that.addDish({
					dishid: dishid,
					dishname: dish.title,
					unit: dish.unit,
					price: 0,
					dishtype: dish.dishtype,
					dishnum: 1,
					dishnote: "",
					itemid: itemid,
					taste: $('.fishpot-dialog .taste li.active').text(),
					dish_avoids: [$('.fishpot-dialog .avoids input').val()],
				}, dishPotRandomId);

				modalIns.close();
			},
			onReady: function(){
				_setSubmitStatus();
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
						var cla = "";
						if (k == 0) {
							cla = "active";
							that.renderDishes(v.itemid);
						}

						htm += '<li class="nav-dish-type ' + cla + '" itemid="' + v.itemid + '"><b></b>' + v.itemdesc + '</li>';
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

		if(dishesMap != null && dishesMap.size()>0){
			var keys = dishesMap.keySet();
			var oBuffer = new utils.string.buffer();
			$.each(keys, function(i, key){
				var v = dishesMap.get(key);
				if(v.source === id && (v.py.indexOf(searchKey.toUpperCase()) !== -1)) {
					oBuffer.append('<div class="dish-info" dishtype=' + v.dishtype + ' dishid="' + v.dishid + '" py="' + v.py + '" dishname="' + v.title + '" price="' + (v.price === undefined ? '' : v.price) + '" unit="' + v.unit + '" level="' + (v.level === undefined ? '' : v.level) + '"  itemid="' + id + '" image>'
						+ '<div class="dish-name">' + v.title
						+ '</div>'
						+ '<hr>'
						+ '<div class="dish-price">' + (v.price === undefined ? '' : v.price) + '/' + v.unit + '</div>'
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
					var result = JSON.parse(res.substring(12, res.length-3));

					if(result.Data === '1') {
						$.each(result.OrderJson , function(k,v){
							dishesMap.put(v.dishid, v);
						});
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
	 * @randomId 鱼锅和套餐 需传入randomId
	 */
	addDish: function (dish, randomId){
		var that = this;
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

		$("#sel-dish-table tbody tr").removeClass("selected");

		var tr = "<tr class='selected' dishid='"+dishid+"' unit='"+unit+"' dishtype='"+dishtype+"' price="+price+" randomId='" +  (randomId === undefined ? '' : randomId) + "'>"
			+ "<td class='dishname' name='"+dishname+"' >"
			+ showname
			+ "</td><td class='num'>"+dishnum+"</td><td class='price'>"
			+ price
			+ "</td></tr>";

		$("#sel-dish-table tbody").prepend(tr);

		dishCartMap.put(dishid, dish);

		//更新总消费金额
		that.updateTotalAmount();
		that.controlBtns();
	},

	/**
	 * 更新总消费额
	 */
	updateTotalAmount: function(){
		var total_amount = 0;
		var that = this;
		var dishids = {};//菜品分类下面已经选择的菜品数量;
		if(dishCartMap != null && dishCartMap.size()>0){
			var keys = dishCartMap.keySet();
			if(keys != null && keys.length>0){
				$.each(keys, function(i, key){
					var dish = dishCartMap.get(key);
					var price = dish.price;
					var totalNum = dish.dishnum;
					var itemid = dish.itemid;
					total_amount += parseFloat(that.calTotalPrice(totalNum, price));

					if(dishids[itemid] === undefined) {
						dishids[itemid] = totalNum;
					} else {
						dishids[itemid] += totalNum;
					}
				});
			}
		}

		$('.nav-dish-type').each(function(){
			var me = $(this);
			var itemid = me.attr('itemid');
			if(dishids[itemid] > 0){
				me.find('b').text(dishids[itemid]);
				me.find('b').show();
			} else {
				me.find('b').text('');
				me.find('b').hide();
			}
		});

		$("#total-amount").text(total_amount);
	},

	/**
	 * 初始化备注dialog
	 * @param type:0单品备注；1：全单备注；2：多口味菜品添加
	 */
	initNoteDialog: function(type){
		var dish_avoids = [];//忌口
		var dishnote = "";
		var dishid = "";

		dom.noteDialog.find('.avoid').removeClass("active")
		dom.noteDialog.attr({'notetype':type});
		$("#dish-note").val("");

		if(type == 1){
			//全单备注
			dom.noteDialog.find('.taste,.dish-info').addClass("hide")
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
				var dish = dishCartMap.get(dishid);
				var dishtype = dish.dishtype;
				dom.noteDialog.attr({'dishtype':dishtype});
				dom.noteDialog.attr({'dishid':dishid});

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
				dom.noteDialog.attr({'dish-type':tastDish.dishtype});
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
	},

	/**
	 * 确认备注
	 */
	doNote: function (){
		var that = this;
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
		var type = dom.noteDialog.attr('notetype');
		var dishtype = dom.noteDialog.attr('dishtype');
		if(type == 1){
			//全单
			$(".note-div").removeClass("hide");
			$("#order-note").attr("note", note1);
			//$("#order-note").attr("sel-note", n2);
			$("#order-note").text(note1);
		}else {
			if(type == 0){
				//单品备注
				var dishid = dom.noteDialog.attr('dishid');
				var dish = dishCartMap.get(dishid);
				var dishname = dish.dishname;

				$("#sel-dish-table tbody tr.selected").find("td.dishname").html(dishname+"("+note1+")");
				$("#sel-dish-table tbody tr.selected").attr('note',note1);
				dish.dishnote = note;
				dish.dish_avoids = dish_avoids;
				dishCartMap.put(dishid, dish);
			}else if(type == 2){
				console.log(tastDish);
				//多口味菜品选择
				tastDish.dishnote = note;
				tastDish.taste = $("#note-dialog #taste ul li.active").text().trim();
				tastDish.dish_avoids = dish_avoids;
				that.addDish(tastDish);
			}
		}
		$("#note-dialog").modal("hide");
	},

	/**
	 * 下单
	 * @param type 0:普通下单, 1:赠菜下单
	 */
	doOrder: function(type,cb){
		var $dishes = $('#sel-dish-table tbody tr');
		var rows = [];

		//先处理鱼锅 套餐
		$dishes.each(function(){
			var me = $(this);
			var dishid = me.attr('dishid');
			var dish = dishCartMap.get(dishid);

			//var ret = [1,2,3];
			//for(var i = 0; i < ret.length + 1; i ++) {
			//	console.log(i);
			//	console.log(ret);
			//	ret.splice(k, 1)
			//}
		});

		//处理单品
		$dishes.each(function(){
			var me = $(this);
			var price = parseInt(me.attr('price'), 10).toFixed(1);
			var dishtype = parseInt(me.attr('dishtype'), 10);
			var dishid = parseInt(me.attr('dishtype'), 10);
			var row = {
				"printtype": '0',
				"pricetype": '0',//0：普通 1：赠菜
				"orderprice": price,//菜品价格
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
			};

			if(type === 0) {
				row = $.extend(row,{
					"freeuser": null, // 赠菜人/收银员工号
					"freeauthorize": null, //赠菜授权人工号
					"freereason": null, //赠菜原因
					"dishes": null //套餐中的子菜品
				})
			} else {
				row = $.extend(row,{
					"freeuser": utils.storage.getter('pos_aUserid'),
					"freeauthorize": $('#user').val(),
					"freereason": $("#givefood-reason").val(''),
				})
			}

			rows.push(row);
		});

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
	},

	/**
	 * 下单
	 */
	placeOrder: function () {
		widget.modal.alert({
			cls: 'fade in',
			content: '餐台【' + consts.tableno + '】确定下单吗？',
			width: 500,
			height: 500,
			btnOkCb:function () {
				AddDish.doOrder(0)
			}
		});
	},

	/**
	 * 控制操作按钮是否可点击
	 */
	controlBtns: function(){

		if($("#sel-dish-table tbody tr.selected").length > 0){
			$(".main-oper-btns .btn").removeClass("disabled");
			$(".oper-div .btns .btn").not(".prev-btn").not(".next-btn").removeClass("disabled");
		}else{
			$(".main-oper-btns .btn").addClass("disabled");
			$(".oper-div .btns .btn").not(".prev-btn").not(".next-btn").addClass("disabled");
		}

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
				//$("#sel-dish-table tbody tr").removeClass("selected");
				//$("#sel-dish-table tbody tr").not(".hide").eq(0).addClass("selected");
			}
		});

	},

	/**
	 * 修改菜品数量对话框
	 */
	updateNum: function (){
		if($("#sel-dish-table tbody tr.selected").length>0){
			var dishid = $("#sel-dish-table tbody tr.selected").attr("dishid");
			var dishname = dishCartMap.get(dishid).dishname;

			dom.numDialog.attr("dishid",dishid);
			dom.numDialog.find('.dish-name').text(dishname)
			dom.numDialog.find('.num').val('')
			dom.numDialog.modal("show");
		}
	},

	/**
	 * 计算菜品总价
	 * @param totalNum
	 * @param price
	 */
	calTotalPrice: function (totalNum, price){
		var totalPrice = parseFloat(totalNum) * parseFloat(price);
		return totalPrice;
	},

	/**
	 * 更新每个菜品的总价格
	 */
	updateTotalPrice: function (){
		var $tr = dom.selDishable.find("tbody tr.selected");
		var dish = dishCartMap.get($tr.attr("dishid"));
		var totalNum =dish.dishnum;
		var price = dish.price;
		var totalPrice = this.calTotalPrice(totalNum, price);
		$tr.find("td.price").text(totalPrice);
	},

	/**
	 * 修改菜品数量
	 */
	doUpdateNum: function(){
		var num = dom.numDialog.find('.J-num').val();
		var dishid = dom.numDialog.attr("dishid");
		var dish = dishCartMap.get(dishid);
		dom.selDishable.find("tbody tr.selected").find("td.num").text(num);
		dish.dishnum = num;
		dishCartMap.put(dishid, dish);

		this.updateTotalPrice();
		this.updateTotalAmount();
		dom.numDialog.modal("hide");
	},

	/**
	 * 加菜
	 */
	addByBtn: function (){
		var $tr = $("#sel-dish-table tbody tr.selected");
		var dishid = $tr.attr("dishid");
		var dishtype = $tr.attr("dishtype");
		var dish = dishCartMap.get(dishid);
		var totalNum = dish.dishnum;
		if(dishtype === '1' || dishtype === '2' ) {
			widget.modal.alert({
				content:'<strong>套餐和鱼锅不能直接修改</strong>',
				btnOkTxt: '',
				btnCancelTxt: '确定'
			});
			return false
		}

		totalNum = parseFloat(totalNum) + 1;
		$tr.find("td.num").text(totalNum);
		dish.dishnum = totalNum;
		dishCartMap.put(dishid, dish);
		this.updateTotalPrice();
		this.updateTotalAmount();
	},

	/**
	 * 减菜
	 */
	reductByBtn: function(){
		var $tr = $("#sel-dish-table tbody tr.selected");
		var dishid = $tr.attr("dishid");
		var dish = dishCartMap.get(dishid);
		var dishtype = $tr.attr("dishtype");
		var totalNum = dish.dishnum;
		if(dishtype === '1' || dishtype === '2' ) {
			$("#sel-dish-table tbody tr[randomid'" + $tr.attr('randomid') + "']").remove();
			return false
		}
		if(parseFloat(totalNum) <= 1){
			//如果数量小于1，则删除本条
			$tr.remove();
			dishCartMap.remove(dishid);
			this.controlBtns();
		}else{
			totalNum = parseFloat(totalNum) - 1;
			$tr.find("td.num").text(totalNum);
			dish.dishnum = totalNum;
			dishCartMap.put(dishid, dish);
			this.updateTotalPrice();
		}
		this.updateTotalAmount();
	},

	//清空已菜品
	clearSelected: function(){
		var modalIns = widget.modal.alert({
			content:'<strong>确定要清空已选菜品吗？</strong>',
			btnOkCb:function () {
				$("#sel-dish-table tbody").html("");
				dishCartMap = new utils.HashMap();
				AddDish.updateTotalAmount();
				AddDish.controlBtns();
				$(".note-div").addClass("hide");
				$("#order-note").attr("note", "");
				$("#order-note").attr("sel-note", "");
				$("#order-note").text("");
				modalIns.close();
			}
		});
	},

	/**
	 * 赠菜
	 */
	giveFood: function () {
		$("#givefood-reason").val('');
		$("#givefood-dialog").modal("show");
	},


	/**
	 * 赠菜操作  调接口
	 */
	doGiveRight: function(){
		$("#givefood-dialog").modal('hide');
		$('#givefood-right').load("./check/impower.jsp",{"title" : "赠菜授权","userRightNo":"030207","cbd":"doGive()"});
		$('#givefood-right').modal('show');
	},

	inputReason :function (){
		widget.textAreaModal({
			target: $("#givefood-reason"),
			note: $("#givefood-reason").val(),
			cb: function(){
				AddDish.doOrder(1);
			}
		}).show();
	},
};

//
///**
// * 刷新订单
// */
//function refreshOrder(){
//	if(dishCartMap != null && dishCartMap.size()>0){
//		var keys = dishCartMap.keySet();
//		$.each(keys, function(i, key){
//			var dish = dishCartMap.get(key);
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

$(document).ready(function(){


	AddDish.init();

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
