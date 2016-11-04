
var dishCartMap = null;//添加进购物车的菜品
var dishesMap = null;//菜品

var g_eatType = utils.getUrl.get('type');


var dom = {
	doc: $(document),
	addDishModal : $("#adddish"),
	searchBtn: $("#adddish").find('input[type=search]'),
	selDishable : $("#sel-dish-table"),
	numDialog: $("#updatenum-dialog"),//菜品数量对话框
	noteDialog: $('#note-dialog'),//备注对话框
	combodishialog: $('#combodish-dialog'),//套餐,
	selCompanyDialog: $('#selCompany-dialog'),
	guadanDialog: $("#guadan-dialog")
};

var consts = {
	orderid: $('input[name=orderid]').val(),
	tableno: $('input[name=tableno]').val(),
	personnum: $('input[name=personnum]').val(),
	DISHES: JSON.parse(utils.storage.getter('DISHES'))[0]
};

var AddDish = {

	init: function () {

		dishCartMap = new utils.HashMap();
		dishesMap = new utils.HashMap();

		if(g_eatType === "out"){
			//外卖
			$(".give-dish").addClass("hide");
			$(".gua-dan").removeClass("hide");

			this.renderPayCompany();
		} else {
			//堂食
			$(".gua-dan").addClass("hide");
			$(".give-dish").removeClass("hide");
		}

		//设置全局订单信息
		$(".order-info").find('.J-order-id').text(consts.orderid)
			.end().find('.J-table-no').text(consts.tableno)
			.end().find('.J-person-no').text(consts.personnum);


		widget.keyboard();

		widget.keyboard({
			target: '.num-btns',
			chirdSelector: 'div'
		});

		widget.keyboard({
			target: '.search-btns',
			chirdSelector: 'div'
		});

		this.renderDishType();
		this.bindEvent();
		SetBotoomIfon.init();

		/*菜品数量格式现在*/
		$('.dish-amount').on('input propertychange focus', function() {
			var _thisVal=$.trim($(this).val()),pattern = /^\+?[1-9][0-9]*$/;//只能输入非0开始的数字
			if(_thisVal!=''){
				if(pattern.test(_thisVal)===false){
					utils.printError.alert('菜品数量，只能输入非0开始的数字');
					$(this).val(_thisVal.substring(0,_thisVal.length-1));
					return false
				}
			}

		});
	},

	bindEvent: function () {
		var that = this;

		/*忌口*/
		dom.doc.delegate('.avoid,.freason', 'click', function(){
			$(this).toggleClass('active');
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
			var pid = me.attr("pid");
			var dish = dishesMap.get(pid);

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
							//多口味菜品
							if(dish.imagetitle.length > 0) {
								that.initNoteDialog(2, dish);
								return false;
							}

							//普通菜品
							var findCid = that.isExit(dish);

							if (findCid.length > 0) {
								//已存在该菜品
								var $tr = null;
								dom.selDishable.find('tbody tr').each(function () {
									//dishid相同,且不是鱼锅
									if ($(this).attr("cid") == findCid) {
										$tr = $(this);
										return;
									}
								});
								var seldish = dishCartMap.get(findCid);
								var num = seldish.dishnum;
								num = parseFloat(num) + 1;
								$tr.find("td.num").text(num);
								var totalPrice = that.calTotalPrice(num, dish.price).toFixed(2);
								$tr.find("td.price").text(totalPrice);
								seldish.dishnum = num;
								dishCartMap.put(findCid, seldish);

								//更新总消费金额
								that.updateTotalAmount();

							} else {
								dish.dishnum = 1;
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
									that.initFishPotModal(dish, res1.OrderJson);
								} else {
									widget.modal.alert({
										content: '<strong>' + (res1.Info === '' ? '请求鱼锅数据接口错误' : res1.Info) + '</strong>',
										btnOkTxt: '',
										btnCancelTxt: '确定'
									});
								}
							});
						} else if (dish.dishtype == "2") {
							//套餐
							$.ajax({
								url: _config.interfaceUrl.GetMenuComboDish,
								method: 'POST',
								contentType: "application/json",
								dataType:'json',
								data: JSON.stringify({
									menuid: dish.menuid,
									dishides: dish.dishid
								})
							}).then(function(res1){
								if(res1.code === '0') {
									res1.data.pid = pid;
									that.initComboDishModal(res1.data);
								} else {
									widget.modal.alert({
										content: '<strong>' + (res1.msg === '' ? '请求套餐数据接口错误' : res1.msg) + '</strong>',
										btnOkTxt: '',
										btnCancelTxt: '确定'
									});
								}

							});

							//$("#combodish-dialog").modal("show");
							//$("#combodish-dialog .num-btns .num-btn").unbind("click").on("click", function () {
							//
							//});
							//
							//$("#combodish-dialog .avoid").unbind("click").on("click", function () {
							//	if ($(this).hasClass("active")) {
							//		$(this).removeClass("active");
							//	} else {
							//		$(this).addClass("active");
							//	}
							//});
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

		/**
		 * 合作单位 挂单
		 */

		dom.guadanDialog.find('.payment-unit').bind('input propertychange focus', function () {

		});

		dom.selCompanyDialog.find('[type=search]').bind('input propertychange focus', function () {
			that.renderPayCompany($.trim($(this).val()));
		});

		dom.selCompanyDialog.find('.btn-clear').click(function () {
			dom.selCompanyDialog.find('[type=search]').val('');
			that.renderPayCompany();

		});

		$('.J-selCompany').click(function () {
			dom.selCompanyDialog.modal('show');
		});

		dom.selCompanyDialog.delegate('li', 'click', function () {
			var me = $(this);
			me.addClass('selected').siblings().removeClass('selected');
		});

		dom.selCompanyDialog.find('.btn-save').click(function () {
			var $target = dom.selCompanyDialog.find('li.selected');

			if ($target.length === 0) {
				widget.modal.alert({
					content: '<strong>请选择挂账单位</strong>',
					btnOkTxt: '',
					btnCancelTxt: '确定'
				});
				return;
			}
			$('.payment-unit').val($target.text());
			$('.payment-unit').attr('preferential',$target.attr('preferential'));

			if($target.text().length > 0) {
				dom.guadanDialog.find('.btn-save').removeAttr('disabled');
			} else {
				dom.guadanDialog.find('.btn-save').attr('disabled','disabled');
			}
			dom.selCompanyDialog.modal('hide');
		});
	},

	/**
	 *
	 * @param dishid 鱼锅dishid
	 * @param data 鱼锅下子菜品
	 * @param itemid 菜品分类id
	 */
	initFishPotModal: function(dish, data){
		var that = this;
		var ret = [];
		var groupid = getUuid();

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
		ret.push((function(){
			var str = '';
			str = '<div class="form-group">';

			$.each(JSON.parse(utils.storage.getter('JI_KOU_SPECIAL')),function(k,v){
				str += '<div class="avoid">' + v.itemDesc + '</div>'
			});

			str += '</div>'
			return str;
		})());
		ret.push('<div class="form-group avoids" ><span class="inpt-span">其他忌口：</span><input type="text" class="form-control padding-left J-note" onclick="widget.textAreaModal({ target: $(this), note: $(this).val() }).show();"></div>');

		//绑定事件
		dom.doc.undelegate('.fishpot-dialog .num-oper-btn', 'click');
		dom.doc.delegate('.fishpot-dialog .num-oper-btn', 'click', function(){
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

		dom.doc.undelegate('.fishpot-dialog .taste li', 'click');
		dom.doc.delegate('.fishpot-dialog .taste li', 'click', function(){
			var me = $(this);
			me.addClass('active').siblings().removeClass('active');
			_setSubmitStatus();
		});

		dom.doc.undelegate('.fishpot-dialog .num-inp', 'input propertychange focus');
		dom.doc.delegate('.fishpot-dialog .num-inp', 'input propertychange focus', function(){
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

				var row = {};
				//添加鱼锅总菜品信息 价格为0
				row = {
					dishid: dish.dishid,
					dishname: dish.title,
					unit: dish.unit,
					price: 0,
					dishtype: dish.dishtype,
					dishnum: 1,
					imagetitle: dish.imagetitle === undefined ? '' : dish.imagetitle,
					dishnote: $('.fishpot-dialog .J-note').val(),
					itemid: dish.itemid,
					taste: $('.fishpot-dialog .taste li.active').text(),
					dish_avoids: (function(){
						var tmp = [];
						$('.fishpot-dialog .avoid.active').each(function(){
							var me = $(this);
							tmp.push(me.text())
						})
						//tmp.push($('.fishpot-dialog .avoids input').val())
						return tmp
					})(),
					dishes: [],
					groupid: groupid
				};

				//添加锅
				for(var i = 0; i < data.length ; i++) {
					if(data[i].ispot === 1) {
						row.dishes.push({
							dishid: data[i].dishid,
							dishname: data[i].title,
							unit: data[i].unit,
							price: data[i].price,
							dishtype: data[i].dishtype,
							dishnum: 1,
							dishnote: "",
							ispot: 1,
							groupid: groupid
						})
						break;
					}
				}

				//添加鱼
				$('.fishpot-dialog .fish').each(function(){
					var me = $(this);
					var numIpt = parseInt(me.next().find('.num-inp').val(), 10);
					if(numIpt > 0) {
						row.dishes.push({
							dishid: me.attr('dishid'),
							dishname: me.attr('dishname'),
							unit: me.attr('unit'),
							price: me.attr('price'),
							dishtype: me.attr('dishtype'),
							dishnum: numIpt,
							dishnote: "",
							ispot: 0,
							groupid: groupid
						});
					}
				});

				that.addDish(row);
				modalIns.close();
			},
			onReady: function(){
				_setSubmitStatus();
			}
		});


	},

	initComboDishModal:  function(data){
		var that = this;
		var pid = data.pid;
		var dish = dishesMap.get(pid);
		var comboStr = '';
		var onlyStr = '';

		var _setSubmitStatus = function(){
			var btn = dom.combodishialog.find('.btn-save');
			if(dom.combodishialog.find('.group-title').length === dom.combodishialog.find('.fited').length ) {
				btn.removeClass('disabled');
			} else {
				btn.addClass('disabled');
			}
		};

		dom.combodishialog.find('.J-note').val('');
		dom.combodishialog.find('.dishname').text(dish.title);

		$.each(data.rows[0].combo, function(k, v){
			comboStr += '<h4 class="group-title" groupid="' + v.id + '" startnum="' + v.startnum + '" endnum="' + v.endnum + '">凉菜（' + v.startnum + '选' + v.endnum + '）已选<span class="selnum">0</span></h4>';
			$.each(v.alldishes, function(key, value){
				comboStr += '<div class="form-group" groupid="' + value.groupid + '">'+
					'<div class="col-xs-7">' + value.contactdishname + '(<span class="unit">' + value.dishunitid + '</span>)</div>'+
					'<div class="col-xs-5"><input class="form-control num-inp" type="text" price="' + value.price + '" dishid="' + value.contactdishid + '" dishtype="' + value.dishtype + '"  unit="' + value.dishunitid +  '" contactdishname="' + value.contactdishname + '"></div>'+
					'</div>';
			});
		});
		dom.combodishialog.find('.combo-group').html(comboStr);

		$.each(data.rows[0].only, function(k, v){
			onlyStr += '<div class="form-group"><div class="col-xs-7">' + v.contactdishname + '(<span class="unit">' + v.dishunitid + ')</span></div></div>';
		});
		dom.combodishialog.find('.only-group').html(onlyStr);

		//添加忌口
		dom.combodishialog.find('.avoid-box').html((function(){
			var str = '';
			$.each(JSON.parse(utils.storage.getter('JI_KOU_SPECIAL')),function(k,v){
				str += '<div class="avoid">' + v.itemDesc + '</div>'
			});
			return str;
		})());

		dom.combodishialog.undelegate('.num-inp','input propertychange focus').delegate('.num-inp','input propertychange focus', function(){
			var me = $(this);
			var val = parseInt(me.val() === '' ? 0 : me.val(), 10);
			var groupid = me.parents('.form-group').attr('groupid');
			var $title = $('h4[groupid=' + groupid + ']');
			var startnum = parseInt($title.attr('startnum'), 10);
			var endnum = parseInt($title.attr('endnum'), 10);
			var $selnum = $title.find('.selnum');
			var selnum = 0;

			//只能输入数字
			me.val(me.val().replace(/\D/g,''));

			var _getSelectedNum = (function(){
				selnum = 0
				$('div[groupid=' + groupid + ']').each(function(){
					if($(this).find('.num-inp').val() !== '') {
						selnum += parseInt($(this).find('.num-inp').val(), 10);
					}
				});
			});

			_getSelectedNum();

			if(val > endnum) {
				me.val('');
				_getSelectedNum();
				$selnum.text(selnum);
				return false;
			}

			if(selnum > endnum ){
				me.parents('.form-group').siblings('.form-group[groupid="' + groupid + '"]').find('.num-inp').val('');
				_getSelectedNum();
				$selnum.text(selnum);
				return false;
			}

			if(selnum === endnum) {
				$title.addClass('fited')
			} else {
				$title.removeClass('fited')
			}

			$selnum.text(selnum);

			_setSubmitStatus();
		});

		dom.combodishialog.undelegate('.btn-save','click').delegate('.btn-save','click', function(){
			var row = {};
			var groupid = getUuid();

			//添加套餐总菜品信息, 其他菜品价格为0
			row = {
				pid: pid,
				dishid: dish.dishid,
				dishname: dish.title,
				unit: dish.unit,
				price: dish.price,
				dishtype: dish.dishtype,
				dishnum: 1,
				dishnote: dom.combodishialog.find('.J-note').val(),
				itemid: dish.itemid,
				imagetitle: dish.imagetitle === undefined ? '' : dish.imagetitle,
				taste: dom.combodishialog.find('.taste li.active').text(),
				dish_avoids: (function(){
					var tmp = [];
					dom.combodishialog.find('.avoid.active').each(function(){
						var me = $(this);
						tmp.push(me.text())
					})
					//tmp.push($('.fishpot-dialog .avoids input').val())
					return tmp
				})(),
				dishes: [],
				groupid: groupid
			};


			//添combo
			dom.combodishialog.find('.num-inp').each(function(){
				var me = $(this);
				var numIpt = parseInt(me.val(), 10);
				if(numIpt > 0) {
					row.dishes.push({
						"printtype": "0",
						"pricetype": "0",
						"orignalprice": me.attr('price'),
						"price": 0,
						dishid: me.attr('dishid'),
						dishnum: numIpt,
						dishname: me.attr('contactdishname'),
						groupid: groupid,
						dishunit: me.attr('unit'),
						dishtype: me.attr('dishtype'),
					});
				}
			});

			//添only
			$.each(data.rows[0].only, function(k, v){
				row.dishes.push({
					printtype: "0",
					pricetype: "0",
					dishid: v.contactdishid,
					dishname: v.contactdishname,
					dishunit: v.dishunitid,
					orignalprice: v.price,
					price: 0,
					dishtype: v.dishtype,
					dishnum: v.dishnum,
					dishnote: "",
					ispot: 0,
					weight: v.weight,
					groupid: groupid
				});
			});
			that.addDish(row);
			dom.combodishialog.modal('hide');
		});

		dom.combodishialog.modal('show');
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
			var oBuffer = new utils.string.buffer();
			var keys = dishesMap.keySet();
			if(keys != null && keys.length>0){
				$.each(keys, function(i, key){
					var v = dishesMap.get(key);
					if(v.source === id && (v.py.indexOf(searchKey.toUpperCase()) !== -1)) {

						oBuffer.append('<div class="dish-info" pid="' + key + '">'
							+ '<div class="dish-name">' + v.title
							+ '</div>'
							+ '<hr>'
							+ '<div class="dish-price">' + (v.price === undefined ? '' : v.price) + '/' + v.unit + '</div>'
							+ '</div>');
					}
				});
			}

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
							var pid = getUuid();
							v.pid = pid;
							v.itemid = v.source;
							dishesMap.put(pid, v);
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
	 */
	addDish: function (dish){
		var that = this;
		var dishname = dish.title || dish.dishname;
		var dishtype = dish.dishtype;
		var price = dish.price;
		var dishnum = dish.dishnum;
		var dishnote = dish.dishnote;
		var groupid = dish.groupid;
		var tr = '';
		var cid = getUuid();
		dish.cid = cid;

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

		dish.sperequire = avoidStr;

		$("#sel-dish-table tbody tr").removeClass("selected");
		//单品
		if(dishtype === 0) {

			tr = "<tr class='selected' cid='" + cid + "'>"
				+ "<td class='dishname' name='"+dishname+"' >"
				+ showname
				+ "</td><td class='num'>"+dishnum+"</td><td class='price'>"
				+ price
				+ "</td></tr>";
		}
		//鱼锅
		else if(dishtype === 1) {
			tr = "<tr class='selected main-pot' cid='" + cid + "' groupid='" +  dish.groupid + "'>"
				+ "<td class='dishname' name='"+dishname+"' >"
				+ showname
				+ "</td><td class='num'>"+dishnum+"</td><td class='price'>"
				+ price
				+ "</td></tr>";
			$.each(dish.dishes, function(k, v){
				tr += "<tr cid='" + cid + "' groupid='" +  dish.groupid + "'>"
					+ "<td class='dishname' name='"+ v.dishname+"' >"
					+ v.dishname
					+ "</td><td class='num'>"+ v.dishnum+"</td><td class='price'>"
					+ (parseFloat(v.price)*parseFloat(v.dishnum)).toFixed(2)
					+ "</td></tr>";
			});
		} else if(dishtype === 2) {
			tr = "<tr class='selected main-combo' cid='" + cid + "' groupid='" +  dish.groupid + "'>"
				+ "<td class='dishname' name='"+dishname+"' >"
				+ showname
				+ "</td><td class='num'>"+dishnum+"</td><td class='price'>"
				+ price
				+ "</td></tr>";
			$.each(dish.dishes, function(k, v){
				tr += "<tr cid='" + cid + "' groupid='" +  dish.groupid + "'>"
					+ "<td class='dishname' name='"+ v.dishname+"' >"
					+ v.dishname
					+ "</td><td class='num'>"+ v.dishnum+"</td><td class='price'>"
					+ (parseFloat(v.price)*parseFloat(v.dishnum)).toFixed(2)
					+ "</td></tr>";
			});
		}

		dishCartMap.put(cid, dish);
		$("#sel-dish-table tbody").prepend(tr);

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
					var totalNum = parseInt(dish.dishnum, 10);
					var itemid = dish.itemid;
					var dishtype = dish.dishtype;

					if(dishtype === 1) {
						$.each(dish.dishes, function(k,v){
							total_amount += parseFloat(that.calTotalPrice(v.dishnum, v.price));
						});
					} else {
						total_amount += parseFloat(that.calTotalPrice(totalNum, price));
					}

					if(dishids[itemid] === undefined) {
						dishids[itemid] = totalNum;
					} else {
						dishids[itemid] += totalNum;
					}
				});
			}
		}
		console.log(dishids);
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

		$("#total-amount").text(total_amount.toFixed(2));
	},

	/**
	 * 初始化备注dialog
	 * @param type:0 单品备注； 1：全单备注； 2：多口味菜品添加
	 */
	initNoteDialog: function(type, dish){
		var dish_avoids = [];//忌口
		var dishnote = "";
		var tasts = '';
		var taste = '';
		var cid = "";
		var ret = [];

		dom.noteDialog.attr({'notetype':type});
		dom.noteDialog.removeAttr('pid');//多口味用字段
		if(type === 0) {
			//单品备注
			var $tr = $("#sel-dish-table tbody tr.selected");
			cid = $tr.attr("cid");
			var dish = dishCartMap.get(cid);
			var dishtype = dish.dishtype;
			dishnote = dish.dishnote;
			dish_avoids = dish.dish_avoids;
			tasts = dish.imagetitle;
			taste = dish.taste;

			dom.noteDialog.attr({'dishtype':dishtype});
			dom.noteDialog.attr({'cid':cid});

			//如果是鱼锅中的单品备注
			if(dishtype === 1 && !$tr.hasClass('main-pot')) {
				widget.modal.alert({
					content:'<strong>请选择鱼锅主体进行备注</strong>',
					btnOkTxt: '',
					btnCancelTxt: '确定'
				});
				return false;
			}
			//如果是套餐中的单品备注
			if(dishtype === 2 && !$tr.hasClass('main-combo')){
				widget.modal.alert({
					content:'<strong>请选择鱼锅主体进行备注</strong>',
					btnOkTxt: '',
					btnCancelTxt: '确定'
				});
				return false;
			}
		}

		if(type === 1){

		}

		if(type === 2) {
			tasts = dish.imagetitle
		}


		if(dish !== undefined) {
			//菜品信息
			ret.push('<div class="dish-info"><p class="p1"><span id="note-dishname">' + (dish.title || dish.dishname) + '</span> <span id="note-price" style="margin-left: 100px;">' + dish.price + '</span>元</p></div>');
			//口味
			if(tasts.length > 0) {
				ret.push('<div class="taste"><h6 style="font-weight: bold; font-size: 16px">选择口味</h6><ul>');
				ret.push((function(){
					var result = '';
					$.each(tasts.split(','), function(k,v){
						result += '<li class="' + (taste === v ? 'active' : '') + '">' + v +'</li>';
					});
					return result;
				})());
				ret.push('</ul></div>');
			}
		}

		//忌口
		ret.push('<h6 style="font-weight: bold; font-size: 16px">忌口</h6>');
		ret.push((function(){
			var str = '';
			str = '<div class="form-group">';

			$.each(JSON.parse(utils.storage.getter('JI_KOU_SPECIAL')),function(k,v){
				str += '<div class="avoid">' + v.itemDesc + '</div>'
			});

			str += '</div>'
			return str;
		})());
		ret.push('<div class="form-group avoids" ><span class="inpt-span">其他忌口：</span><input type="text" id="dish-note" class="form-control padding-left" val="' + tasts + '" onclick="widget.textAreaModal({ target: $(this), note: $(this).val() }).show();"></div>');

		dom.noteDialog.find('.dialog-sm-info').html(ret.join(''));

		dom.noteDialog.undelegate('.taste li', 'click');
		dom.noteDialog.delegate('.taste li', 'click', function(){
			var me = $(this);
			me.addClass('active').siblings().removeClass('active');
		});

		if(type == 1){
			//全单备注
			dishnote = $("#order-note").attr("note");
			$("#dish-note").val(dishnote);
			var sel_note = $("#order-note").attr("sel-note");
			if(sel_note!= null && sel_note!=""){
				var arr = sel_note.split("|");
				$("#note-dialog .avoid").each(function(){
					var text = $(this).text().trim();
					var f = false;
					$.each(arr, function(i, v){
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
			if(type == 0){

			}else if(type == 2){
				//多口味菜添加
				dom.noteDialog.attr({'dish-type':dish.dishtype});
				dom.noteDialog.attr({'pid':dish.pid});
			}

		}

		//
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

		$("#note-dialog #dish-note").val(dishnote);

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
		var $orderNote= $("#order-note");
			$("#note-dialog .avoid.active").each(function(){
				var avoid = $(this).text();
				dish_avoids.push(avoid);
				n1 += avoid+";";
				n2 += avoid+"|";
			});
		var note1 = n1+note;
		//note1 = note1.substring(0, note1.length-1);
		var type = dom.noteDialog.attr('notetype');
		var dishtype = dom.noteDialog.attr('dishtype');
		if(type == 1){
			//全单
			$orderNote.attr("note", note);
			$orderNote.attr("sel-note", n2);
			$orderNote.text(note1);
			if(note1.length > 0) {
				$('.note-div').removeClass('hide');
			} else {
				$('.note-div').addClass('hide');
			}

		}else {
			if(type == 0){
				//单品备注
				var cid = dom.noteDialog.attr('cid');
				var dish = dishCartMap.get(cid);
				var dishname = dish.title || dish.dishname;
				var showname = dishname;
				var taste = $("#note-dialog .taste ul li.active").text().trim();
				if(taste.length > 0){
					showname += "("+taste+")";
				}
				if(note1.length > 0) {
					showname += "("+note1+")";
				}
				$("#sel-dish-table tbody tr.selected").find("td.dishname").html(showname);
				$("#sel-dish-table tbody tr.selected").attr('note',note1);
				dish.dishnote = note;
				dish.dish_avoids = dish_avoids;
				dish.sperequire=note1;//添加忌口
				dishCartMap.put(cid, dish);

			}
			else if(type == 2){
				//多口味菜品选择
				var pid = dom.noteDialog.attr('pid');
				var tastedish = $.extend({}, dishesMap.get(pid));
				tastedish.dishnote = note;
				tastedish.taste = $("#note-dialog .taste ul li.active").text().trim();
				tastedish.dish_avoids = dish_avoids;
				tastedish.isMultiTast = '1';

				var findCid = that.isExit(tastedish);
				if(findCid.length > 0) {
					var $tr = null;
					dom.selDishable.find('tbody tr').each(function () {
						//dishid相同,且不是鱼锅
						if ($(this).attr("cid") == findCid) {
							$tr = $(this);
							return;
						}
					});
					var seldish = dishCartMap.get(findCid);
					var num = seldish.dishnum;
					num = parseFloat(num) + 1;
					$tr.find("td.num").text(num);
					var totalPrice = that.calTotalPrice(num, tastedish.price).toFixed(2);
					$tr.find("td.price").text(totalPrice);
					seldish.dishnum = num;
					dishCartMap.put(findCid, seldish);
				} else {
					tastedish.dishnum = '1';
					that.addDish(tastedish);
				}

			}
		}
		$("#note-dialog").modal("hide");
	},

	/**
	 * 下单
	 * @param type 0:普通下单, 1:赠菜下单 2:挂单(挂账下单)
	 */
	doOrder: function(type,cb){
		var rows = [];
		var user = utils.storage.getter('aUserid');
		var freeuser = null;
		var freeauthorize = null;
		var freereason = null;
		var pricetype = '0'  //0：普通 1：赠菜;
		var url = _config.interfaceUrl.OrderDish;

		//如果是赠菜
		if(type === 1){
			freeuser = user;
			freeauthorize =  $('#user').val();
			freereason = (function(){
				var reason = [];
				var val = $("#givefood-reason").val();
				$('#givefood-dialog .freason.active').each(function(){
					reason.push($(this).text());
				});
				if(val.length > 0) {
					reason.push(val);
				}
				return reason.join(';')
			})();
			pricetype = '1';
		}

		if(type === 2) {
			url = _config.interfaceUrl.OrderDishCf;
		}


		$.each(dishCartMap.values(), function(k, v){
			var dish = v;
			var row = {
				"printtype": '0',
				"pricetype": pricetype,//0：普通 1：赠菜
				"orderprice": type === 1 ? '0' : dish.price,//菜品价格
				"orignalprice": dish.price,//菜品单价
				"dishid": dish.dishid,
				"userName": user,
				"dishunit": dish.unit,
				"orderid": consts.orderid,
				"dishtype": dish.dishtype,//0：单品 1：鱼锅 2：套餐
				"orderseq": "1",
				"dishnum": dish.dishnum,
				"sperequire": dish.sperequire,  //忌口信息
				"primarykey": getUuid(),
				"dishstatus": dish.weigh === undefined ? '0' : dish.weigh,//0：已称重或者不称重 1:未称重
				"ispot": 0, //0:非锅底 1:锅底
				"taste": dish.taste === undefined ? '' : dish.taste,// 点菜口味/临时菜的菜名
				"dishes": null,
				"freeuser": freeuser, // 赠菜人/收银员工号
				"freeauthorize": freeauthorize, //赠菜授权人工号
				"freereason": freereason, //赠菜原因
			};
			//如果是鱼锅
			if(dish.dishtype === 1) {
				row = $.extend(row,{
					"orderprice": 0.0,//菜品价格
					"orignalprice": 0.0,//菜品单价
					"dishes": []
				});

				$.each(dish.dishes, function(key, value){
					row.dishes.push(
						{
							"printtype": '0',
							"pricetype": pricetype,//0：普通 1：赠菜
							"orderprice": type === 1 ? '0' : value.price,//菜品价格
							"orignalprice": value.price,//菜品单价
							"dishid": value.dishid,
							"userName": user,
							"dishunit": value.unit,
							"orderid": consts.orderid,
							"dishtype": value.dishtype,//0：单品 1：鱼锅 2：套餐
							"orderseq": "1",
							"dishnum": value.dishnum,
							"sperequire": value.sperequire === undefined ? '' : value.sperequire,  //忌口信息
							"primarykey": getUuid(),
							"dishstatus": value.weigh === undefined ? '0' : value.weigh,//0：已称重或者不称重 1:未称重
							"ispot": value.ispot, //0:非锅底 1:锅底
							"taste": value.taste === undefined ? '' : value.taste,// 点菜口味/临时菜的菜名
							"dishes": null,
							"freeuser": freeuser, // 赠菜人/收银员工号
							"freeauthorize": freeauthorize, //赠菜授权人工号
							"freereason": freereason, //赠菜原因
						}
					)
				})
			}

			//如果是套餐
			if(dish.dishtype === 2) {
				row = $.extend(row,{
					"orderprice": dish.price,//菜品价格
					"orignalprice": dish.price,//菜品单价
					"dishes": []
				});

				$.each(dish.dishes, function(key, value){
					row.dishes.push(
						{
							"printtype": '0',
							"pricetype": pricetype,//0：普通 1：赠菜
							"orderprice": value.price,//菜品价格
							"orignalprice": value.orignalprice,//菜品单价
							"dishid": value.dishid,
							"userName": user,
							"dishunit": value.dishunit,
							"orderid": consts.orderid,
							"dishtype": value.dishtype,//0：单品 1：鱼锅 2：套餐
							"orderseq": "1",
							"dishnum": value.dishnum,
							"sperequire": value.sperequire === undefined ? '' : value.sperequire,  //忌口信息
							"primarykey": getUuid(),
							"dishstatus": value.weigh === undefined ? '0' : value.weigh,//0：已称重或者不称重 1:未称重
							"ispot": value.ispot === undefined ? '0' : value.ispot, //0:非锅底 1:锅底
							"taste": value.taste === undefined ? '' : value.taste,// 点菜口味/临时菜的菜名
							"dishes": null,
							"freeuser": freeuser, // 赠菜人/收银员工号
							"freeauthorize": freeauthorize, //赠菜授权人工号
							"freereason": freereason, //赠菜原因
							"dishstatus": value.weigh === undefined ? '0' : value.weigh,
						}
					)
				})
			}
			rows.push(row);
		});

		$.ajax({
			url: _config.interfaceUrl.GetOrderInfo,
			method: 'POST',
			contentType: "application/json",
			data: JSON.stringify({
				orderid: consts.orderid
			}),
			dataType: 'json',
		}).then(function(res){
			if (res.code === '0') {
				//首次点菜 && 餐具设置收费 && 堂食
				if(res.data.rows.length === 0 && consts.DISHES.status === '1'  && g_eatType === 'in') {
					rows.push({
						"printtype": "0",
						"pricetype": pricetype,//0：普通 1：赠菜
						"orderprice": 2.0,
						"orignalprice": 2.0,
						"dishid": consts.DISHES.id,
						"userName": null,
						"dishunit": "份",
						"orderid": null,
						"dishtype": 0,
						"orderseq": "1",
						"dishnum": consts.personnum,
						"sperequire": "",
						"primarykey": getUuid(),
						"dishstatus": "0",
						"ispot": 0,
						"taste": "",
						"freeuser": freeuser, // 赠菜人/收银员工号
						"freeauthorize": freeauthorize, //赠菜授权人工号
						"freereason": freereason, //赠菜原因
						"dishes": null
					});
				}
				$.ajax({
					url: url,
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
					})
				}).then(function(res){
					if(res.code === '0') {
						cb && cb();

						if(type === 2) {
							var putOrderUrl = _config.interfaceUrl.SetTakeoutOrderOnAccount + consts.tableno + '/' + consts.orderid + '/' + dom.guadanDialog.find('.payment-unit').attr('preferential') + '/' + dom.guadanDialog.find('.payment-unit').val() + '/' + dom.guadanDialog.find('.contact').val() + '/' + dom.guadanDialog.find('.tel').val() + '/';
							$.ajax({
								url: putOrderUrl,
								method: 'get',
								dataType:'text',
							}).then(function(data){
								dom.guadanDialog.modal('hide');
								var  data=JSON.parse(data.substring(12, data.length - 3));
								if(data.Data === '1'){
									widget.modal.alert({
										content:'<strong>设置外卖挂单成功,挂单单号:[' + consts.orderid  + ']</strong>',
										btnOkCb: function(){
											window.location.href = "../views/main.jsp"
										}
									});
								} else {
									widget.modal.alert({
										content:'<strong>设置外卖挂单失败,挂单单号:[' + consts.orderid  + ']</strong>',
										btnOkTxt: '',
										btnCancelTxt: '确定'
									});
								}
							})
						} else {
							goToOrder('下单成功');
						}

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

				});
			} else {
				widget.modal.alert({
					cls: 'fade in',
					content: '<strong>' + res.msg + '</strong>',
					width: 500,
					height: 500,
					btnOkTxt: '',
					btnCancelTxt: '确定'
				});
			}
		})
	},

	/**
	 * 下单
	 */
	placeOrder: function () {
		var ins = widget.modal.alert({
			cls: 'fade in',
			content: '餐台【' + consts.tableno + '】确定下单吗？',
			width: 500,
			height: 500,
			btnOkCb:function () {
				ins.close();
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
			var cid = $("#sel-dish-table tbody tr.selected").attr("cid");
			var dishname = dishCartMap.get(cid).dishname || dishCartMap.get(cid).title;
			var dishtype = dishCartMap.get(cid).dishtype;
			dom.numDialog.attr("cid",cid);
			dom.numDialog.find('.dish-name').text(dishname);
			dom.numDialog.find('.J-num').val('');
			if(dishtype === 2 || dishtype === 1 ) {
				widget.modal.alert({
					content:'<strong>套餐和鱼锅不能直接修改数量</strong>',
					btnOkTxt: '',
					btnCancelTxt: '确定'
				});
				return false
			}
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
		var dish = dishCartMap.get($tr.attr("cid"));
		var totalNum =dish.dishnum;
		var price = dish.price;
		var totalPrice = this.calTotalPrice(totalNum, price);
		$tr.find("td.price").text(totalPrice.toFixed(2));
	},

	/**
	 * 修改菜品数量
	 * @param type 0:通过对话框修改 1:add 2:reduce
	 * @returns {boolean}
     */
	doUpdateNum: function(type){
		var $tr = dom.selDishable.find("tbody tr.selected");
		var cid = $tr.attr("cid");
		var dish = dishCartMap.get(cid);
		var dishtype = dish.dishtype;
		var num = parseFloat(dish.dishnum);
		var val = dom.numDialog.find('.J-num').val() === '' ? 0 : parseFloat(dom.numDialog.find('.J-num').val());

		//修改数量
		if(type === 0) {
			num = val;
		} else if(type === 1) {
			num++;
 		} else if(type ===2 ){
			num--;
		}

		if(dishtype === 2 || dishtype === 1 ) {
			if(num > 0) {
				widget.modal.alert({
					content:'<strong>套餐和鱼锅不能直接修改数量</strong>',
					btnOkTxt: '',
					btnCancelTxt: '确定'
				});
			} else {
				dom.selDishable.find('[cid=' + cid  +']').remove();
				dishCartMap.remove(cid);
				dom.selDishable.find("tbody tr").eq(0).addClass('selected');
				this.controlBtns();
			}
			this.updateTotalAmount();
			return false
		}

		if(parseFloat(num) <= 0) {
			$tr.remove();
			dishCartMap.remove(cid);
			dom.selDishable.find("tbody tr").eq(0).addClass('selected');
			this.controlBtns();
		} else {
			$tr.find("td.num").text(num);
			dish.dishnum = num;
			dishCartMap.put(cid, dish);
			this.updateTotalPrice();
		}
		this.updateTotalAmount();
		dom.numDialog.modal("hide");
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
		if( utils.storage.getter('config') !== null) {
			var str = '';
			$.each(JSON.parse(utils.storage.getter('config')).DishGiftReasons.split(';'), function(k,v){
				str += '<div class="freason">' + v + '</div>';
			})
			$("#givefood-dialog").find('.freasons').html(str);
		}
		$("#givefood-dialog").modal("show");
	},


	/**
	 * 赠菜操作  调接口
	 */
	doGiveRight: function(){
		$("#givefood-dialog").modal('hide');
		$('#givefood-right').load("./check/impower.jsp",{"title" : "赠菜授权","userRightNo":"030207","cbd":"AddDish.doOrder(1)"});
		$('#givefood-right').modal('show');
	},

	/**
	 * 菜品是否存在购物车 如果存在 返回购物车商品id(cid)
	 * @param dish
	 * @returns {string}
     */
	isExit: function(dish){
		var  f = '';
		var cartDish = null;
		for(var i = 0, len = dishCartMap.values().length; i < len; i++){
			cartDish = dishCartMap.values()[i];
			if(cartDish.pid === dish.pid){
				//多口味菜
				if(dish.isMultiTast === '1') {
					if(cartDish.taste === dish.taste && cartDish.dishnote === dish.dishnote &&  cartDish.dish_avoids.join('') === dish.dish_avoids.join('')) {
						f = dishCartMap.values()[i].cid;
						return f;
					}
				} else {
					f = dishCartMap.values()[i].cid;
					return f;
				}

			};
		}

		return f;
	},

	/**
	 * 挂单
	 */
	guadan: function(){
		$("#guadan-dialog").modal("show");
	},

	//挂账单位
	renderPayCompany: function (key) {
		var payCompany = utils.storage.getter('payCompany');
		var ret = [];
		var searchKey = !key ? $.trim(dom.selCompanyDialog.find('[type=search]').val()) : key;
		if (payCompany !== null) {
			$.each(JSON.parse(payCompany), function (k, v) {
				if (v.name_first_letter.indexOf(searchKey.toUpperCase()) !== -1) {
					ret.push('<li py="' + v.name_first_letter + '" preferential="' + v.preferential + '">' + v.name + '</li>');
				}
			});

			if (ret.length < 11) {
				$('#J-company-pager').html('');
				$('.campany-icon').html(ret.join(''));
			} else {
				$('#J-company-pager').pagination({
					dataSource: ret,
					pageSize: 10,
					showPageNumbers: false,
					showNavigator: true,
					callback: function (data) {
						$('.campany-icon').html(data.join(''));
					}
				});
			}
		}
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

function goToOrder(tips){
	var url = "../views/order.jsp?orderid=" + consts.orderid + '&personnum=' + consts.personnum + '&tableno=' + consts.tableno  + '&type=' + g_eatType;
	if(tips !== undefined && tips.length > 0) {
		url += '&tips=' + tips;
	}
	window.location.href = encodeURI(encodeURI(url));
}

$(document).ready(function(){
	AddDish.init();
});
