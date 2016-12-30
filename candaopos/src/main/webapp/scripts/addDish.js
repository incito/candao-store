
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
	guadanDialog: $("#guadan-dialog"),
	lscDialog: $('#lsc-dialog')
};

var consts = {
	orderid: $('input[name=orderid]').val(),
	tableno: $('input[name=tableno]').val(),
	personnum: $('input[name=personnum]').val(),
	DISHES: JSON.parse(utils.storage.getter('DISHES'))[0],
	DISHES2: JSON.parse(utils.storage.getter('DISHES2'))[0]
};

var AddDish = {

	init: function () {
		dishCartMap = new utils.HashMap();
		dishesMap = new utils.HashMap();

		focusIpt = $('input[type=search]');

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

	},

	bindEvent: function () {
		var that = this;

		/*忌口*/
		dom.doc.delegate('.avoid,.freason', 'click', function(){
			$(this).toggleClass('active');
		});
		/*已点菜品按名称排序 dishType: 0为升序，1为降序*/
		/*$('#sel-dish-table-title').click(function () {
		 if(dishCartMap.values().length<1){
		 return false
		 }
		 var dishType=$(this).attr('dishType'),
		 abc=null;
		 if(dishType=='0'){
		 var a=dishCartMap.values()
		 for(var i=0;i<a.length;i++){
		 var FirstLetter=pinyinUtil.getFirstLetter(a[i].title)
		 a[i]['FirstLetter']=FirstLetter
		 }
		 abc=_.sortByOrder(a, ['FirstLetter'], ['asc']);
		 $(this).attr('dishType','1')
		 }
		 else {
		 abc=_.sortByOrder(dishCartMap.values(), ['py'], ['desc'])
		 $(this).attr('dishType','0')
		 }
		 that.initComboDishModal(abc)
		 })
		 /!*已点菜品按数量排序*!/
		 $('#sel-dish-table-dishnum').click(function () {
		 if(dishCartMap.values().length<1){
		 return false
		 }
		 var dishType=$(this).attr('dishType'),
		 abc=null;
		 if(dishType=='0'){
		 abc=_.sortByOrder(dishCartMap.values(), ['dishnum'], ['asc']);
		 $(this).attr('dishType','1')
		 }
		 else {
		 abc=_.sortByOrder(dishCartMap.values(), ['dishnum'], ['desc']);
		 $(this).attr('dishType','0')
		 }
		 $(this).attr('dishType',dishType)

		 that.initComboDishModal(abc)
		 })
		 /!*已点菜品按价格排序*!/
		 $('#sel-dish-table-price').click(function () {
		 if(dishCartMap.values().length<1){
		 return false
		 }
		 var dishType=$(this).attr('dishType'),
		 abc=null;
		 if(dishType=='0'){
		 abc=_.sortByOrder(dishCartMap.values(), ['price'], ['asc']);
		 $(this).attr('dishType','1')
		 }
		 else {
		 abc=_.sortByOrder(dishCartMap.values(), ['price'], ['desc']);
		 $(this).attr('dishType','0')
		 }
		 $(this).attr('dishType',dishType)

		 that.initComboDishModal(abc)
		 })*/


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
			if(dom.searchBtn.val().length === 0) {
				that.renderDishes(me.attr('itemid'));
			}
		});

		//菜品分类向左向右按钮
		$(".nav-dishtype-next").click(function(){
			var count = $dishType.find( "li.nav-dish-type").length;

			if (flag_prev < count - 6) {
				$dishType.find("li.nav-dish-type").eq(flag_prev).css("margin-left", "-16.66%");
				if(parseInt($dishType.find("li.nav-dish-type.active").css('margin-left'))  < 0) {
					$dishType.find("li.nav-dish-type").eq(flag_prev+1).click();
				}
				flag_prev++;
				$(".nav-dishtype-prev").removeClass('disabled');
				console.log(flag_prev);
				if(flag_prev === (count - 6)) {
					$(this).addClass('disabled');
				}
			}

		});
		$(".nav-dishtype-prev").click(function(){
			if(flag_prev>0){
				$dishType.find("li.nav-dish-type").eq(flag_prev-1).css("margin-left","0");

				if($dishType.find("li.nav-dish-type.active").index() ===  (flag_prev + 5)) {
					$dishType.find("li.nav-dish-type").eq($dishType.find("li.active").index() - 1).click();
				}
				flag_prev--;
				$(".nav-dishtype-next").removeClass('disabled');
				if(flag_prev === 0) {
					$(this).addClass('disabled');
				}
			}
		});

		/**
		 * 菜品搜索事件
		 */
		dom.searchBtn.bind('input propertychange focus', function(){
			var val = $.trim($(this).val());
			if(val.length > 0) {
				that.renderDishes('-1', val);
			} else {
				that.renderDishes($('.nav-dish-types li.active').attr('itemid'));
			}

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
			Log.send(2, '点菜');
			Log.send(2, '查询菜品状态:' + JSON.stringify({
					dishUnit: dish.unit
				}));
			$.ajax({
				url: _config.interfaceUrl.GetDishStatus + '/' + dish.dishid + '/',
				method: 'POST',
				contentType: "application/json",
				dataType:'text',
				global: false,
				data: JSON.stringify({
					dishUnit: dish.unit
				})
			}).then(function(res){
				var result = JSON.parse(res.substring(12, res.length-3));
				Log.send(2, '查询菜品状态返回:' + JSON.stringify(res));
				if(result.Data === '1') {//接口返回成功
					if(result.Info === '0') {//菜品状态正常
						if (dish.dishtype == "0") {
							//多口味菜品
							if(dish.imagetitle.length > 0) {
								Log.send(2, '多口味菜');
								that.initNoteDialog(2, dish);
								return false;
							}

							//临时菜 在POS专用菜品分类下，已临时菜开头的菜品名字才算临时菜
							if($('li[itemid="' + dish.itemid + '"] span').text() === "pos专用菜品"  && dish.title.substring(0,3).indexOf('临时菜') >-1) {
								Log.send(2, '临时菜');
								dish.temporary = '1';
								dom.lscDialog.find('input').val('');
								dom.lscDialog.off('click','.J-btn-submit').on('click','.J-btn-submit', function(){
									var  price = $.trim(dom.lscDialog.find('.price').val());
									var  dishnum = $.trim(dom.lscDialog.find('.dishnum').val());
									if($.trim(dom.lscDialog.find('.note-name').val()).length === 0) {
										widget.modal.alert({
											content:'<strong>菜名不能为空,请检查!</strong>',
											btnOkTxt: '',
											btnCancelTxt: '确定'
										});
										return false;
									}
									if(price.length === 0 || parseFloat(price) === 0) {
										widget.modal.alert({
											content:'<strong>价格(元)不能为空或者为零,请检查!</strong>',
											btnOkTxt: '',
											btnCancelTxt: '确定'
										});
										return false;
									}
									if(!(/^[0-9]{1,4}$/g.test(price) || /^[0-9]{1,4}\.[0-9]{1,2}$/g.test(price) || /^[0-9]{1,4}\.$/g.test(price))){
										widget.modal.alert({
											content:'<strong>请输入正确的价格,请检查!</strong>',
											btnOkTxt: '',
											btnCancelTxt: '确定'
										});
										return false;
									}
									if(dishnum.length === 0 || parseFloat(dishnum) === 0) {
										widget.modal.alert({
											content:'<strong>数量(份)不能为空或者为零,请检查!</strong>',
											btnOkTxt: '',
											btnCancelTxt: '确定'
										});
										return false;
									}
									if(!(/^[0-9]{1,2}$/g.test(dishnum) || /^[0-9]{1,2}\.[0-9]{1,2}$/g.test(dishnum) || /^[0-9]{1,2}\.$/g.test(dishnum))){
										widget.modal.alert({
											content:'<strong>请输入正确的数量,请检查!</strong>',
											btnOkTxt: '',
											btnCancelTxt: '确定'
										});
										return false;
									}

									dish.dishnum =  dom.lscDialog.find('.dishnum').val();
									dish.orderprice = dish.orignalprice =  dom.lscDialog.find('.price').val();
									dish.price = parseFloat(parseFloat(dish.dishnum) * parseFloat(dish.orderprice)).toFixed(2);
									dish.taste = dish.orignalprice =  $.trim(dom.lscDialog.find('.note-name').val());

									that.addDish(dish);

									Log.send(2, '临时菜:' + JSON.stringify(dish));
									dom.lscDialog.modal('hide');
								});
								dom.lscDialog.modal('show');
								return  false;
							}

							//普通菜品
							var findCid = that.isExit(dish);

							if (findCid.length > 0) {
								//已存在该菜品
								findCid = findCid[0];
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
								Log.send(2, '菜品已经存在,修改菜品数量:' + JSON.stringify(seldish))
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
							Log.send(2, '鱼锅数据查询:' + _config.interfaceUrl.GetFishPotDish + '/' + dish.dishid + '/')
							$.ajax({
								url: _config.interfaceUrl.GetFishPotDish + '/' + dish.dishid + '/',
								method: 'GET',
								contentType: "application/json",
								dataType:'text'
							}).then(function(res1){
								var res1 = JSON.parse(res1.substring(12, res1.length-3));
								Log.send(2, '鱼锅数据返回:' + JSON.stringify(res1));
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
							Log.send(2, '套餐数据查询:' + JSON.stringify({
									menuid: dish.menuid,
									dishides: dish.dishid
								}));
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
								Log.send(2, '套餐数据返回:' + JSON.stringify(res1));
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
						}
					} else {
						Log.send(2, '菜品沽清');
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
					Log.send(2, '获取菜品状态失败');
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
			if($("#sel-dish-table tbody tr.selected").length > 0){
				$(".main-oper-btns .btn").removeClass("disabled");
				$(".oper-div .btns .btn").not(".prev-btn").not(".next-btn").removeClass("disabled");
			}else{
				$(".main-oper-btns .btn").addClass("disabled");
				$(".oper-div .btns .btn").not(".prev-btn").not(".next-btn").addClass("disabled");
			}
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
			str = '<div class="form-group avoids-certainList" >';

			$.each(JSON.parse(utils.storage.getter('JI_KOU_SPECIAL')),function(k,v){
				if(v.itemDesc.length>7){
					str += '<div class="avoid" style="padding-top: 0px">' + v.itemDesc + '</div>'
				}else {
					str += '<div class="avoid">' + v.itemDesc + '</div>'
				}

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
			//var numInpStatus = (function(){
			//  var status = false;
			//  $('.fishpot-dialog .num-inp').each(function(){
			//      if(parseInt($(this).val(), 10) > 0) {
			//          status = true;
			//          return false;
			//      }
			//  });
			//
			//  return  status;
			//})();
			if($('.fishpot-dialog .taste li').length > 0) {
				if($('.fishpot-dialog .taste li.active').length > 0) {
					btn.removeClass('disabled');
				} else {
					btn.addClass('disabled');
				}
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

	currentCoboDish: null,
	initComboDishModal:  function(data){
		var that = this;
		var pid = data.pid;
		var dish = dishesMap.get(pid);
		var comboStr = '';
		var onlyStr = '';
		that.currentCoboDish = data;

		var _setSubmitStatus = function(){
			var btn = dom.combodishialog.find('.btn-save');
			if(dom.combodishialog.find('.group-title').length === 0 || (dom.combodishialog.find('.group-title').length === dom.combodishialog.find('.fited').length) ) {
				btn.removeClass('disabled');
			} else {
				btn.addClass('disabled');
			}
		};
		dom.combodishialog.find('.J-note').val('');
		dom.combodishialog.find('.dishname').text(dish.title.split('#')[0]);

		$.each(data.rows[0].combo, function(k, v){
			comboStr += '<h4 class="group-title"  combokey="' + k+ '" groupid="' + v.id + '" startnum="' + v.startnum + '" endnum="' + v.endnum + '">' + v.columnname + '（' + v.startnum + '选' + v.endnum + '）已选<span class="selnum">0</span></h4>';
			$.each(v.alldishes, function(key, value){
				comboStr += '<div class="form-group" disheskey="' + key + '" groupid="' + value.groupid + '">'+
					'<div class="col-xs-7">' + value.contactdishname + '(<span class="unit">' + value.dishunitid + '</span>)</div>'+
					'<div class="col-xs-5"><input class="form-control num-inp"  type="text" price="' + value.price + '" dishid="' + value.contactdishid + '" dishtype="' + value.dishtype + '"  unit="' + value.dishunitid +  '" contactdishname="' + value.contactdishname + '"></div>'+
					'</div>';
			});
		});
		dom.combodishialog.find('.combo-group').html(comboStr);

		$.each(data.rows[0].only, function(k, v){
			onlyStr += '<div class="form-group"><div class="col-xs-7">' + v.contactdishname + '(<span class="unit">' + v.dishunitid + ')</span></div></div>';
			if(v.dishtype !== '0') {
				$.each(v.dishes, function(key,value){
					onlyStr += '<div class="form-group"><div class="col-xs-7">' + value.contactdishname + '(<span class="unit">' + value.dishunitid + ')</span></div></div>';
				})
			}
		});
		dom.combodishialog.find('.only-group').html(onlyStr);

		//添加忌口
		dom.combodishialog.find('.avoid-box').html((function(){
			var str = '';
			$.each(JSON.parse(utils.storage.getter('JI_KOU_SPECIAL')),function(k,v){
				if(v.itemDesc.length>7){
					str += '<div class="avoid" style="padding-top: 0px">' + v.itemDesc + '</div>'
				}else {
					str += '<div class="avoid">' + v.itemDesc + '</div>'
				}

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
				selnum = 0;
				$('div[groupid=' + groupid + ']').each(function(){
					if($(this).find('.num-inp').val() !== '') {
						selnum += parseInt($(this).find('.num-inp').val(), 10);
					}
				});
			});


			_getSelectedNum();

			if(endnum === 1) {
				me.val('1');
				me.parents('.form-group').siblings('.form-group[groupid=' + groupid + ']').each(function(){
					$(this).find('.num-inp').val('')
				});
				$selnum.text('1');
				$title.addClass('fited');
				_setSubmitStatus();
				return false;
			}

			if(val > endnum) {
				me.val(endnum);
				me.parents('.form-group').siblings('.form-group[groupid="' + groupid + '"]').find('.num-inp').val('');
				_getSelectedNum();
				//return false;
			}

			if(selnum > endnum ){
				me.parents('.form-group').siblings('.form-group[groupid="' + groupid + '"]').find('.num-inp').val('');
				_getSelectedNum();
			}

			if(selnum === endnum) {
				$title.addClass('fited')
			} else {
				$title.removeClass('fited')
			}

			$selnum.text(selnum);

			_setSubmitStatus();
		});

		dom.combodishialog.undelegate('.btn-save','click').delegate('.btn-save','click',data, function(){
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
			$.each(dom.combodishialog.find('.num-inp'), function(){
				var me = $(this);
				var numIpt = parseInt(me.val(), 10);
				if(numIpt > 0) {
					var parent = me.parents('.form-group');
					var dishtype = me.attr('dishtype');
					var groupid = parent.attr('groupid')
					var combokey = me.parents('.combo-group').find('h4[groupid=' + groupid + ']').attr('combokey')
					var disheskey = parent.attr('disheskey');//与 getUuid()设置的id不一样
					var subDishes = AddDish.currentCoboDish.rows[0].combo[combokey].alldishes[disheskey].dishes
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
						dishes: (function(){
							var ret = [];
							if(parseInt(dishtype, 10) === 0) {
								return null;
							} else {
								$.each(subDishes, function(k,v){
									ret.push(
										{
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
											ispot: v.ispot === undefined ? '0' : v.ispot,
											weight: v.weight,
											groupid: groupid,
										}
									)
								})
								return ret;
							}
						})(),
					});
				}
			});

			//添only
			$.each(data.rows[0].only, function(key, value){
				row.dishes.push({
					printtype: "0",
					pricetype: "0",
					dishid: value.contactdishid,
					dishname: value.contactdishname,
					dishunit: value.dishunitid,
					orignalprice: value.price,
					price: 0,
					dishtype: value.dishtype,
					dishnum: value.dishnum,
					dishnote: "",
					ispot: value.ispot === undefined ? '0' : value.ispot,
					weight: value.weight,
					groupid: groupid,
					dishes: (function(){
						var ret = [];
						if(parseInt(value.dishtype, 10) === 0) {
							return null;
						} else {
							$.each(value.dishes, function(k,v){
								ret.push(
									{
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
										ispot: value.ispot === undefined ? '0' : value.ispot,
										weight: v.weight,
										groupid: groupid,
									}
								)
							})
							return ret;
						}
					})(),
				});
			});
			that.addDish(row);
			dom.combodishialog.modal('hide');
		});

		dom.combodishialog.modal('show');

		_setSubmitStatus();
	},

	//获取菜品分类
	renderDishType: function () {
		var that = this;
		$.ajax({
			url: _config.interfaceUrl.GetDishGroupInfos,
			method: 'POST',
			contentType: "application/json",
			dataType:'json',
			success: function(res){
				Log.send(2, '获取菜品分类:' + JSON.stringify(res));
				if(res.code === '0') {
					var htm = '';
					$.each(res.data, function(k,v){
						var cla = "";
						if (k == 0) {
							cla = "active";
							that.renderDishes(v.itemid);
						}

						htm += '<li class="nav-dish-type ' + cla + '" itemid="' + v.itemid + '"><b></b><span>' + v.itemdesc.split('#')[0] + '</span></li>';
					});
					$(".nav-dish-types").html(htm);
					if($(".nav-dish-types").find( "li.nav-dish-type").length > 6) {
						$(".nav-dishtype-prev").addClass('disabled');
					} else {
						$(".nav-dishtype-prev, .nav-dishtype-next").addClass('disabled');
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
			}
		});
	},

	/**
	 * 渲染菜品信息
	 * @param id    菜品分类id (-1, 全部分类)
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
					var idCondition = (function(){
						if(id === '-1') {
							return true
						}  else {
							return v.source === id
						}
					})();

					if(idCondition && (v.py.indexOf(searchKey.toUpperCase()) !== -1)) {
						oBuffer.append('<div class="dish-info" pid="' + key + '">'
							+ '<div class="dish-name">' + v.title.split('#')[0]
							+ '</div>'
							+ '<hr>'
							+ '<div class="dish-price">' + (v.price === undefined ? '' : v.price) + '/' + v.unit.split('#')[0] + '</div>'
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
					Log.send(2, '获取菜品数据:' + JSON.stringify(res));
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
							content:'<strong>' + result.Info + '</strong>',
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
		var dish = $.extend(true,{}, dish);
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
		var showname = utils.string.cutString(dishname.split('#')[0],14);

		if(taste != null && taste != ""){
			if(dish.temporary === '1') {
				showname = "("+taste+")" + showname;
			} else {
				showname = showname+"("+taste+")";
			}

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
			//临时菜
			if(dish.temporary === '1') {
				tr = "<tr class='selected' cid='" + cid + "'>"
					+ "<td class='dishname' name='"+dishname+"' >"
					+ showname
					+ "</td><td class='num'>"+dishnum+"</td><td class='price'>"
					+ price
					+ "</td></tr>";
			} else {
				tr = "<tr class='selected' cid='" + cid + "'>"
					+ "<td class='dishname' name='"+dishname+"' >"
					+ showname
					+ "</td><td class='num'>"+dishnum+"</td><td class='price'>"
					+ price
					+ "</td></tr>";
			}
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
				tr += "<tr cid='" + cid + "' ispot='" + v.ispot + "' dishid='" +  v.dishid + "'  groupid='" +  dish.groupid + "'>"
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
				if(parseInt(v.dishtype, 10) !== 0) {
					$.each(v.dishes, function(key,value){
						tr += "<tr cid='" + cid + "' groupid='" +  dish.groupid + "'>"
							+ "<td class='dishname' name='"+ value.dishname+"' >"
							+ value.dishname
							+ "</td><td class='num'>"+ value.dishnum+"</td><td class='price'>"
							+ (parseFloat(value.price)*parseFloat(value.dishnum)).toFixed(2)
							+ "</td></tr>";
					})
				}

			});
		}

		Log.send(2, 'addDish:' + JSON.stringify(dish));
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
			tasts = dish.imagetitle;
			dom.noteDialog.find('.btn-save').attr('disabled', 'disabled');
		}


		if(dish !== undefined) {
			//菜品信息
			ret.push('<div class="dish-info"><p class="p1"><span id="note-dishname">' + ((dish.title && dish.title.split('#')[0]) || (dish.dishname && dish.dishname.split('#')[0])) + '</span> <span id="note-price" style="margin-left: 100px;">' + dish.price + '</span>元</p></div>');
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
			} else {
				dom.noteDialog.find('.btn-save').removeAttr('disabled');
			}
		}

		//忌口
		ret.push('<h6 style="font-weight: bold; font-size: 16px">忌口</h6>');
		ret.push((function(){
			var str = '';
			str = '<div class="form-group avoidsList">';

			$.each(JSON.parse(utils.storage.getter('JI_KOU_SPECIAL')),function(k,v){
				if(v.itemDesc.length>7){
					str += '<div class="avoid" style="padding-top: 0px">' + v.itemDesc + '</div>'
				}else {
					str += '<div class="avoid">' + v.itemDesc + '</div>'
				}

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
			dom.noteDialog.find('.btn-save').removeAttr('disabled');

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
				var dish = $.extend({},dishCartMap.get(cid));
				var dishname = dish.title || dish.dishname;

				var showname = utils.string.cutString(dishname.split('#')[0],14);
				var taste = $("#note-dialog .taste ul li.active").text().trim();
				if(dish.temporary === '1') {
					taste = dish.taste;
				}
				if(taste.length > 0){
					dish.taste = taste;
					if(dish.temporary === '1') {
						showname = "("+taste+")" + showname;
					} else {
						showname = showname+"("+taste+")";
					}
				}
				if(note1.length > 0) {
					showname += "("+note1+")";
				}
				$("#sel-dish-table tbody tr.selected").find("td.dishname").html(showname);
				$("#sel-dish-table tbody tr.selected").attr('note',note1);
				dish.dishnote = note;
				dish.dish_avoids = dish_avoids;
				dish.sperequire=note1;//添加忌口
				Log.send(2, '确认备注:' + JSON.stringify(dish));
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
					findCid = findCid[0];
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
					Log.send(2, '确认备注:' + JSON.stringify(dish));
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
	 * @param type 0:普通下单, 1:赠菜下单 2:挂单(挂账下单) 3:外卖下单
	 */
	doOrder: function(type,cb){
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
				var rows = [];
				var user = utils.storage.getter('aUserid');
				var freeuser = null;
				var freeauthorize = null;
				var freereason = null;
				var pricetype = '0'  //0：普通 1：赠菜;
				var url = _config.interfaceUrl.OrderDish;
				var isLogin = res.data.userOrderInfo.memberno.length > 0;


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

				if(type === 2 || type === 3) {
					url = _config.interfaceUrl.OrderDishCf;
				}

				$.each(dishCartMap.values(), function(k, v){
					var dish = v;
					var row = {
						"printtype": '0',
						"pricetype": pricetype,//0：普通 1：赠菜
						"orderprice": (function(){
							if(type === 1) {
								return '0';
							} else {
								if(isLogin && dish.vipprice !== undefined) {
									return dish.vipprice
								} else {
									return dish.price;
								}
							}

						})(),
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

					if(dish.temporary === '1') {
						console.log(dish);
						row = $.extend(row,{
							"dishnum":  parseFloat(parseFloat(dish.dishnum) * parseFloat(dish.orderprice)).toFixed(2),
							"orderprice": type === 1 ? '0' : '1',//菜品价格
							"orignalprice": '1'
						});
					}
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
							//"orderprice": dish.price,//菜品价格
							//"orignalprice": dish.price,//菜品单价
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
									"dishes": (function(){
										var ret = [];
										if(parseInt(value.dishtype, 10) === 0) {
											return null;
										} else {
											$.each(value.dishes, function(k,v){
												ret.push(
													{
														"printtype": '0',
														"pricetype": pricetype,//0：普通 1：赠菜
														"orderprice": v.price,//菜品价格
														"orignalprice": v.orignalprice,//菜品单价
														"dishid": v.dishid,
														"userName": user,
														"dishunit": v.dishunit,
														"orderid": consts.orderid,
														"dishtype": v.dishtype,//0：单品 1：鱼锅 2：套餐
														"orderseq": "1",
														"dishnum": v.dishnum,
														"sperequire": v.sperequire === undefined ? '' : v.sperequire,  //忌口信息
														"primarykey": getUuid(),
														"dishstatus": v.weigh === undefined ? '0' : v.weigh,//0：已称重或者不称重 1:未称重
														"ispot": v.ispot === undefined ? '0' : v.ispot, //0:非锅底 1:锅底
														"taste": v.taste === undefined ? '' : v.taste,// 点菜口味/临时菜的菜名
														"dishes": null,
														"freeuser": freeuser, // 赠菜人/收银员工号
														"freeauthorize": freeauthorize, //赠菜授权人工号
														"freereason": freereason, //赠菜原因
													}
												)
											})
										}
										return ret;
									})(),
									"freeuser": freeuser, // 赠菜人/收银员工号
									"freeauthorize": freeauthorize, //赠菜授权人工号
									"freereason": freereason, //赠菜原因
								}
							)
						})
					}
					rows.push(row);
				});
				Log.send(2, '下单');
				Log.send(2, '获取订单信息:' + JSON.stringify(res));
				//首次点菜 && 餐具设置收费 && 堂食 && pad设置免餐具费
				if(res.data.rows.length === 0 && consts.DISHES2.status === '1'  && g_eatType === 'in' && res.data.userOrderInfo.isFree === '0') {
					Log.send(2, '首次点菜 && 餐具设置收费 && 堂食 && pad设置免餐具费');
					rows.push({
						"printtype": "0",
						"pricetype": 0,//0：普通 1：赠菜
						"orderprice": consts.DISHES.price,
						"orignalprice": consts.DISHES.price,
						"dishid": consts.DISHES.dishid,
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
				//debugger;
				//return false;

				Log.send(2, '下单:' + url);
				Log.send(2, '下单参数:' + JSON.stringify({
						"currenttableid": consts.tableno,
						"globalsperequire": $('#order-note').text(),
						"orderid": consts.orderid,
						"operationType": 1,
						"sequence": 1,
						"rows": rows
					}));
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
						"sequence": 94,
						"rows": rows
					})
				}).then(function(res){
					Log.send(2, '下单返回:' + JSON.stringify(res));
					if(res.code === '0') {
						cb && cb();

						if(type === 2) {
							var putOrderUrl = _config.interfaceUrl.SetTakeoutOrderOnAccount + consts.tableno + '/' + consts.orderid + '/' + dom.guadanDialog.find('.payment-unit').attr('preferential') + '/' + dom.guadanDialog.find('.payment-unit').val() + '/' + dom.guadanDialog.find('.contact').val() + '/' + dom.guadanDialog.find('.tel').val() + '/';
							Log.send(2, '外卖挂单: ' + putOrderUrl);
							$.ajax({
								url: putOrderUrl,
								method: 'get',
								dataType:'text',
							}).then(function(data){
								dom.guadanDialog.modal('hide');
								var  data=JSON.parse(data.substring(12, data.length - 3));
								Log.send(2, '外卖挂单: ' + JSON.stringify(data));
								if(data.Data === '1'){
									var modalIns = widget.modal.alert({
										content:'<strong>设置外卖挂单成功,挂单单号:[' + consts.orderid  + ']</strong>',
										btnCancelTxt: '',
										btnOkCb: function(){
											window.location.href = "../views/main.jsp"
										}
									});
									$('#' + modalIns.id).find('.close').click(function(){
										window.location = "../views/main.jsp";
									})
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
				AddDish.doOrder(g_eatType === 'in' ? 0 : 3);
			}
		});
	},

	/**
	 * 控制操作按钮是否可点击
	 * @param loadPager (true:重新加载分页)
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
			listNum : 12,
			currPage : 0,
			totleNums : $("#sel-dish-table tbody tr").length,
			curPageObj : "#adddish #curr-page",
			pagesLenObj : "#adddish #pages-len",
			prevBtnObj : "#adddish .oper-div .prev-btn",
			nextBtnObj : "#adddish .oper-div .next-btn",
			callback : function() {
			}
		});
	},

	/**
	 * 修改菜品数量对话框
	 */
	updateNum: function (){
		if($("#sel-dish-table tbody tr.selected").length>0){
			var $target = $("#sel-dish-table tbody tr.selected");
			var cid = $target.attr("cid");
			var dishname = dishCartMap.get(cid).dishname || dishCartMap.get(cid).title;
			var dishtype = dishCartMap.get(cid).dishtype;
			dom.numDialog.attr("cid",cid);
			dom.numDialog.find('.dish-name').text(dishname);
			dom.numDialog.find('.J-num').val('');
			dom.numDialog.find('.J-num').off('input propertychange focus').on('input propertychange focus', function() {
				var me = $(this);
				var val = me.val();
				if(!(/^[0-9]{1,3}$/g.test(me.val()) || /^[0-9]{1,3}\.[0-9]{1,2}$/g.test(me.val()) || /^[0-9]{1,3}\.$/g.test(me.val()))) {
					me.val(val.substr(0, me.val().length-1))
				}
				if($.trim(me.val()).length > 0) {
					dom.numDialog.find('.btn-save').removeAttr('disabled');
				} else {
					dom.numDialog.find('.btn-save').attr('disabled', 'disabled');
				}
			});
			focusIpt = dom.numDialog.find('.J-num');
			if(dishtype === 2 || (dishtype === 1 && $target.attr('ispot') === '1' ) || (dishtype === 1 && $target.hasClass('main-pot')))  {
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
		var that=this;
		if($tr.length===0){
			that.updateTotalAmount();
			return false
		}
		var dish = dishCartMap.get($tr.attr("cid"));
		var totalNum =dish.dishnum;
		var dishtype = dish.dishtype;
		var price = dish.price;
		if(dishtype === 1 && $tr.attr('ispot') === '0') {
			$.each(dish.dishes, function(k, v){
				if(v.dishid === $tr.attr('dishid')) {
					totalNum = v.dishnum;
					price = v.price;
				}
			});
		}
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

		if(dishtype === 1 && $tr.attr('ispot') === '0') {
			$.each(dish.dishes, function(k, v){
				if(v.dishid === $tr.attr('dishid')) {
					num = parseFloat(v.dishnum);
				}
			});
		}

		//修改数量
		if(type === 0) {
			num = val;
		} else if(type === 1) {
			if(dish.temporary==='1'){
				if(num>=99){
					return false
				}
			}else {
				num = parseFloat(num + 1);
			}
			//num = parseFloat(num + 1).toFixed(2);
			num = parseFloat(num + 1);
		} else if(type ===2 ){
			//num = parseFloat(num - 1).toFixed(2);
			num = parseFloat(num - 1);
		}

		//if(dishtype === 2 || dishtype === 1) {
		//  if(num > 0) {
		//      if(dishtype === 2 || (dishtype === 1 && $tr.attr('ispot') === '1' ) || (dishtype === 1 && $tr.hasClass('main-pot'))) {
		//          widget.modal.alert({
		//              content:'<strong>套餐和鱼锅不能直接修改数量</strong>',
		//              btnOkTxt: '',
		//              btnCancelTxt: '确定'
		//          });
		//          return false;
		//      } else {
		//          //可以修改的为鱼锅的鱼
		//          $tr.find("td.num").text(num);
		//          $.each(dish.dishes, function(k, v){
		//              console.log(k);
		//              if(v.dishid === $tr.attr('dishid')) {
		//                  v.dishnum = num;
		//              }
		//          });
		//          Log.send(2, '修改菜品数量:' + JSON.stringify(dish));
		//          dishCartMap.put(cid, dish);
		//      }
		//  } else {
		//      if((dishtype === 2 && $tr.hasClass('main-combo')) || (dishtype === 1 && $tr.hasClass('main-pot'))) {
		//          dom.selDishable.find('[cid=' + cid  +']').remove();
		//          dishCartMap.remove(cid);
		//          dom.selDishable.find("tbody tr").eq(0).addClass('selected');
		//      } else {
		//          widget.modal.alert({
		//              content:'<strong>套餐和鱼锅不能直接修改数量</strong>',
		//              btnOkTxt: '',
		//              btnCancelTxt: '确定'
		//          });
		//          return false;
		//      }
		//  }
		//} else
		if(dishtype === 1) {
			if($tr.attr('ispot') === '1' || $tr.hasClass('main-pot')) {
				if(num > 0) {
					widget.modal.alert({
						content:'<strong>鱼锅不能直接修改数量</strong>',
						btnOkTxt: '',
						btnCancelTxt: '确定'
					});
					return false;
				} else {
					if($tr.hasClass('main-pot')) {
						dom.selDishable.find('[cid=' + cid  +']').remove();
						dishCartMap.remove(cid);
						dom.selDishable.find("tbody tr").eq(0).addClass('selected');
					} else {
						widget.modal.alert({
							content:'<strong>鱼锅不能直接修改数量</strong>',
							btnOkTxt: '',
							btnCancelTxt: '确定'
						});
						return false;
					}
				}
			} else {
				if(num > 0) {
					$tr.find("td.num").text(num);
					$.each(dish.dishes, function(k, v){
						if(v.dishid === $tr.attr('dishid')) {
							v.dishnum = num;
						}
					});
					Log.send(2, '修改菜品数量:' + JSON.stringify(dish));
					dishCartMap.put(cid, dish);
				} else {
					$tr.remove();
					$.each(dish.dishes, function(k, v){
						if(v.dishid === $tr.attr('dishid')) {
							dish.dishes.splice(k,1)
						}
					});
					dom.selDishable.find("tbody tr").eq(0).addClass('selected');
				}
			}
		} else if(dishtype === 2) {
			if($tr.hasClass('main-combo')) {
				if(num > 0) {
					widget.modal.alert({
						content:'<strong>套餐不能直接修改数量</strong>',
						btnOkTxt: '',
						btnCancelTxt: '确定'
					});
					return false;
				} else {
					dom.selDishable.find('[cid=' + cid  +']').remove();
					dishCartMap.remove(cid);
					dom.selDishable.find("tbody tr").eq(0).addClass('selected');
				}
			} else {
				widget.modal.alert({
					content:'<strong>套餐不能直接修改数量</strong>',
					btnOkTxt: '',
					btnCancelTxt: '确定'
				});
				return false;
			}
		} else {
			if(parseFloat(num) <= 0) {
				$tr.remove();
				dishCartMap.remove(cid);
				dom.selDishable.find("tbody tr").eq(0).addClass('selected');

			} else {
				$tr.find("td.num").text(num);
				dish.dishnum = num;
				Log.send(2, '修改菜品数量:' + JSON.stringify(dish));
				dishCartMap.put(cid, dish);

			}
		}

		this.controlBtns();
		this.updateTotalPrice();
		this.updateTotalAmount();
		dom.numDialog.modal("hide");
	},

	//清空已菜品
	clearSelected: function(){
		Log.send(2, '清空已菜品');
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
		var str = '';
		$.each(JSON.parse(utils.storage.getter('GIFT_REASON')), function(k,v){
			if(v.itemDesc.length>7){
				str += '<div class="freason" style="padding-top: 0px">' + v.itemDesc + '</div>';
			}
			else{
				str += '<div class="freason">' + v.itemDesc + '</div>';
			}

		})
		$("#givefood-dialog").find('.freasons').html(str);
		$("#givefood-dialog").modal("show");
	},


	/**
	 * 赠菜操作  调接口
	 */
	doGiveRight: function(){
		Log.send(2, '赠菜');
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
		var  f = [];
		var cartDish = null;
		for(var i = 0, len = dishCartMap.values().length; i < len; i++){
			cartDish = dishCartMap.values()[i];
			var cid = dishCartMap.values()[i].cid;
			if(cartDish.pid === dish.pid){
				//多口味菜
				if(dish.isMultiTast === '1') {
					if(cartDish.taste === dish.taste && cartDish.dishnote === dish.dishnote &&  cartDish.dish_avoids.join('') === dish.dish_avoids.join('')) {
						return f.push(cid);
					}
				} else {
					if(cartDish.dish_avoids === 'unde' && cartDish.dish_avoids.length === 0) {
						f.push(cid)
					}

					if(cartDish.dish_avoids === undefined) {
						f.push(cid)
					} else if (cartDish.dish_avoids && cartDish.dish_avoids.length === 0) {
						f.push(cid)
					}
				}
			}
		}
		return f;
	},

	/**
	 * 挂单
	 */
	guadan: function(){
		Log.send(2,'挂单')
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

	/*点菜排序后更新*/
	dishesSort:function (abc) {
		console.log(abc)
		var abc=JSON.parse(abc),
			that=this,
			str=''
		$("#sel-dish-table tbody").html('');
		for( var i=0;i<abc.length;i++){
			str+='<tr class="" cid="'+abc[i].cid+'">' +
				'<td class="dishname" name="'+abc[i].title+'">'+abc[i].title.split('#')[0]+'</td>' +
				'<td class="num">'+abc[i].dishnum+'</td>' +
				'<td class="price">'+abc[i].price+'</td>' +
				'</tr>'
		}
		$("#sel-dish-table tbody").html(str);
		$("#sel-dish-table tr").eq(1).trigger("click")//默认选中第一行
		that.controlBtns()

	}

};



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
	var url = "../views/order.jsp?orderid=" + consts.orderid + '&personnum=' + consts.personnum + '&tableno=' + encodeURIComponent(encodeURIComponent(consts.tableno )) + '&type=' + g_eatType;
	if(tips !== undefined && tips.length > 0) {
		url += '&tips=' + encodeURIComponent(encodeURIComponent(tips));
	}
	window.location.href = url;
}

$(document).ready(function(){
	AddDish.init();
});
