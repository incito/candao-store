var pref_prev = 0;

var Order = {

	init: function () {

		this.initPreferentialType();

		this.updateOrder();

		this.bindEvent();
	},


	bindEvent: function () {

		var that = this;
		var dom = {
			orderDialog : $("#order-dialog"),
			openDialog : $("#open-dialog"),//开台权限验证弹窗
			addDishDialog : $("#adddish-dialog"),//点菜弹窗
			giveDishDialog : $("#givedish-dialog"),//赠菜弹窗
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
						tableNo: $('input[name=tableno]').val(),
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

							$('input[name=tableno]').val(res.data.orderid)
							//设置订单信息
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


		//退菜
		$("#back-dish").click(function(){
			backFood(0);
		});
		$('#backfood-dialog .btn-save').click(function(){
			$('#backfoodnum-dialog').modal('show');
		});

		$('#backfoodnum-dialog .btn-save').click(function(){
			$('#backfood-right').load("./check/impower.jsp",{"title" : "退菜权限","userRightNo":"030102","cbd":"checkOrder.rebackOrder.jumpfjs(user)"});
			$('#backfood-right').modal('show');
		});

		//验证退菜权限


		//称重
		$("#weigh-dish").click(function(){
			$("#weight-dialog").modal("show");
		});

		/**
		 * 优惠操作
		 */
		dom.orderDialog.delegate(".nav-pref-types li.nav-pref-type",'click', function () {
			var me = $(this);
			me.siblings().removeClass("active").end().addClass("active");
			that.initPreferential(me.attr('preid'));
		});

		//优惠-赠菜
		dom.giveDishDialog.delegate('li', 'click', function(){
			var me = $(this);
			var num = parseInt(me.attr('num'), 10);
			var sel = parseInt(me.attr('sel') || 0, 10);
			if(sel === num) {
				me.removeClass('selected');
				sel = 0;
			} else {
				me.addClass('selected');
				sel++;
			}
			me.attr('sel',sel);
			me.find('.sel').text(sel);
		});

		//选中已选优惠
		dom.orderDialog.delegate("#sel-preferential-table tbody tr, #order-dish-table tbody tr", "click", function () {
			var me = $(this);
			me.siblings().removeClass("selected").end().addClass("selected");
		});

		//添加优惠
		dom.orderDialog.delegate(".preferential-info",'click', function () {
			var me = $(this);
			var name = me.attr('name');
			var type = me.attr('type');
			var sub_type = me.attr('sub_type');
			var discount = me.attr('discount');
			var free_reason = me.attr('free_reason')
			var $coupnumDialog = $('#coupnum-dialog');
			var $givedishDialog = $('#givedish-dialog');

			that.manageUsePref.set({
				"preferentialid": me.attr('preferential'),
				"disrate": discount === '' ? '0.0' : discount,//折扣
				"type": type,//类型
				"sub_type": sub_type === '' ? null : sub_type,//子类型  优惠券信息

				//"preferentialAmt": "0.00",//总优惠
				//"toalFreeAmount": "0.00",//优免
				//"toalDebitAmount": "0.00",//挂账
				//"toalDebitAmountMany": "0.00",//挂账多收
				//"adjAmout": "0.00",//优免调整

				"preferentialNum": "1",//优惠卷张数 “xx,xx,xx”,
				"preferentialAmout": "0",//手动输入优惠金额
				"isCustom": "0",//是否是用户自己输入

				"dishid": "",//赠菜卷“xx,xx,xx”,
				"unit": "" //“xx,xx,xx”,
			});

			//根据不同优惠券,弹出不同modal
			if(type === '05' || type=== '03'){
				//团购 || 代金券, 输入数量
				$coupnumDialog.addClass('coupnum-num');
				$coupnumDialog.find('.coupname').text(name);
				$coupnumDialog.find('.inpt-span').text('使用数量');
				$coupnumDialog.modal('show');

			} else if(type === '07' && free_reason === '1'){
				//手工 输入折扣率
				$coupnumDialog.addClass('coupnum-cus-discount');
				$coupnumDialog.find('.inpt-span').text('折扣率');
				$coupnumDialog.modal('show');
			} else if(type === '07' && free_reason === '2') {
				//手工 输入金额(优免)
				$coupnumDialog.addClass('coupnum-cus-free');
				$coupnumDialog.find('.inpt-span').text('优免');
				$coupnumDialog.modal('show');
			} else if(type === '07' && free_reason === '0') {
				//手工 选择赠菜
				$givedishDialog.addClass('coupum-cus-give');

				//更新可选赠菜信息
				that.updateGiveDishInfo(function(){
					$givedishDialog.modal('show');
				});
			} else {
				//确认框
				var alert = widget.modal.alert({
					cls: 'fade in',
					content:'<strong>确定使用' + name + '?</strong>',
					width:500,
					height:500,
					btnOkCb: function(){
						alert.close();
						that.addPref(this);
					}
				});
			}

			return false;

		});

		//删除或清空优惠
		$('#del-pref,#clear-pref').click(function(){
			var me = $(this);
			var target = $('#sel-preferential-table tbody tr.selected');
			var isClear = me.attr('id') === 'clear-pref';
			if(me.hasClass('disabled')) return false;
			$.ajax({
				url: _config.interfaceUrl.DelPreferential,
				method: 'POST',
				async: true,
				contentType: "application/json",
				dataType:'json',
				data: JSON.stringify({
					clear: isClear ? '1' : '0',
					DetalPreferentiald : target.attr('preid'),
					orderid: target.attr('orderid')
				}),
				success: function(res){
					if(res.code === '0') {

						//更新结算信息
						that.updateTotal(res.data.preferentialInfo);

						//更新已选优惠
						that.updateSelectedPref(res.data.detailPreferentials);

						if(isClear) {//如果是清空
							target.parents('tbody tr').remove();
						} else {
							me.remove();
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
			})
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
		var addDishDialog = $("#adddish-dialog")
		addDishDialog.load("../views/orderdish.jsp");
		addDishDialog.modal("show");
	},

	/**
	 * 优惠
	 */

	//加载优惠分类
	initPreferentialType: function () {
		var ret = [];
		var that = this;
		$.each(_config.preferential, function(k,v){
			var cla = "";
			if (k  === '05') {
				cla = "active";
			}
			ret.push('<li class="nav-pref-type ' + cla + '" preid="'+ k + '">' + v + '</li>');
		});

		$(".nav-pref-types").html(ret.join(''));
		that.initPreferential();
	},

	//通过分类获取优惠券信息
	initPreferential: function (id) {
		if(arguments.length < 1) {
			id = '05';
		}

		$.ajax({
			url: _config.interfaceUrl.GetCouponInfos,
			method: 'POST',
			async: true,
			contentType: "application/json",
			dataType:'json',
			data: JSON.stringify({
				machineno:"96121CBC21EF02256E9C5F2E602C5441",
				userid: utils.storage.getter('aUserid'),
				orderid:'0',
				typeid: id
			}),
			success: function(res) {
				console.log(res);
				var htm = '';
				$.each(res, function(k, v){
					htm += '<div class="preferential-info"' +
						' type="' + v.type + '"' +
						' free_reason="' + v.free_reason + '"' +
						' preferential="' + v.preferential + '"' +
						' name="' + v.name + '"' +
						' discount="' + v.discount + '"' +
						' sub_type="' + v.sub_type + '"' +
						' type_name="' + v.type_name + '" >'
						+ '<div class="dish-name">' + v.name + '</div>' + '</div>';
				});
				$(".main-div .preferentials-content").html(htm);

				widget.loadPage({
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
		})
	},

	/**
	 * 添加优惠
	 */
	addPref: function (target) {
		var that = this;
		//var updateGiveData = [];

		//debugger;
		if(arguments.length > 0) {
			var targetModal = $(target).closest('.modal');
			var val = targetModal.find('.J-pref-ipt').val();

			if(targetModal.hasClass('coupnum-num')) {
				//输入优惠券数量
				that.manageUsePref.set({
					preferentialNum:val
				});
			} else if(targetModal.hasClass('coupnum-cus-discount')) {
				//手动输入折扣
				that.manageUsePref.set({
					isCustom: '1',
					disrate: val
				});
			} else if(targetModal.hasClass('coupnum-cus-free')) {
				//手动输入优免
				that.manageUsePref.set({
					isCustom: '1',
					preferentialAmout: val
				});
			} else if(targetModal.hasClass('coupnum-cus-give')) {
				var $giveDishDialog = $('#givedish-dialog');
				var $li = $giveDishDialog.find('li');
				var preferentialNum = [];
				var dishid = [];
				var unit = [];


				$.each($li, function(){
					var me = $(this);
					var sel = parseInt(me.attr('sel'), 10);
					var num = parseInt(me.attr('num'), 10);

					if(me.hasClass('selected')) {
						preferentialNum.push(sel);
						dishid.push(me.attr('dishid'));
						unit.push(me.attr('unit'));
					}

					//如果当前还有可以赠送的菜
					//if(num != sel) {
					//	updateGiveData.push({
					//		dishnum:  num - sel,
					//		dishid: me.attr('dishid'),
					//		dishname: me.find('.dishname').text(),
					//		dishunit: me.attr('unit'),
					//	})
					//}
				});

				//手动输入赠菜
				that.manageUsePref.set({
					isCustom: '1',
					"preferentialNum": preferentialNum.join(','),//优惠卷张数 “xx,xx,xx”,
					"dishid": dishid.join(','),//赠菜卷“xx,xx,xx”,
					"unit": unit.join(',') //“xx,xx,xx”,
				});
			}
		}
		$.ajax({
			url: _config.interfaceUrl.CalcDiscountAmount,
			method: 'POST',
			async: true,
			contentType: "application/json",
			dataType:'json',
			data: JSON.stringify(that.manageUsePref.get()),
			success: function(res){
				if(res.code === '0') {
					console.log(res);


					that.updateTotal(res.data);
					//更新已选优惠
					that.updateSelectedPref(res.data.detailPreferentials);

					$("#coupnum-dialog,#givedish-dialog").modal("hide");

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

	/**
	 * 更新赠菜信息
	 */
	updateGiveDishInfo: function(cb){
		$.when(
			$.ajax({
				url: _config.interfaceUrl.GetOrderInfo,
				method: 'POST',
				contentType: "application/json",
				data: JSON.stringify({
					orderid: $('input[name=orderid]').val()
				}),
				dataType:'json'
			}),
			$.ajax({
				url: _config.interfaceUrl.GivePrefer,
				method: 'POST',
				async: true,
				contentType: "application/json",
				dataType:'json',
				data: JSON.stringify({
					orderId: $('input[name=orderid]').val()
				})
			})
		).then(function(res1, res2){
			var data = res1[0].data.rows;
			//debugger;
			if(res1[0].code === '0' && res2[0].code === '0') {
				var htm = '';
				$.each(data, function(k,v){
					var dishnum = parseInt(v.dishnum, 10);
					$.each(res2[0].data, function(key, value){
						var left = dishnum - parseInt(value.count, 10);
						if(value.dishid === v.dishid) {
							v.dishnum = left;
						}
					});
				});
				console.log(data);
				$.each(data, function(k,v){
					var cls = v.dishnum > 0 ? '' : 'display:none';
					htm += "<li style='" + cls + "' dishid='" + v.dishid + "' unit='" + v.dishunit + "' num='" + v.dishnum + "'>" +
						"<span class='dishname'>" + v.dishname + "</span>" +
						"<span class='info'><span class='sel'>0</span>/<span class='num'>" + v.dishnum + "</span></span>" +
						"</li>";
				});

				$('#givedish-dialog .give-dish-list').html(htm);

				cb && cb();
			}
			else {
				widget.modal.alert({
					cls: 'fade in',
					content:'<strong>' + res1.msg + ' ' + res2.msg + '</strong>',
					width:500,
					height:500,
					btnOkTxt: '',
					btnCancelTxt: '确定'
				});
			}
		},function(){
			widget.modal.alert({
				cls: 'fade in',
				content:'<strong>接口错误</strong>',
				width:500,
				height:500,
				btnOkTxt: '',
				btnCancelTxt: '确定'
			});
		});
	},

	/**
	 * 更新已选优惠
	 */
	updateSelectedPref: function(data){
		var tr = '';
		var $body = $("#sel-preferential-table tbody");
		//设置优惠回传值
		$.each(data, function(k, v){
			tr += "<tr preid='" + v.id + "' orderid='" + v.orderid + "'><td>" + v.activity.name + "</td><td>" + 1 + "</td><td>" + v.deAmount + "</td></tr>";
		});
		$("#sel-preferential-table tbody").prepend(tr);
		widget.loadPage({
			obj : "#sel-preferential-table tbody tr",
			listNum : 6,
			currPage : currPage,
			totleNums : $body.find('tr').length,
			curPageObj : ".preferential-oper-btns .page-info span:first",
			pagesLenObj : ".preferential-oper-btns .page-info span:last",
			prevBtnObj : ".preferential-oper-btns .prev-btn",
			nextBtnObj : ".preferential-oper-btns .next-btn",
			callback : function() {
				$body.find('tr').removeClass("selected");
				$body.find('tr').not(".hide").eq(0).addClass(
					"selected");
			}
		});

		/**
		 * 优惠购物车按钮
		 */
		if ($body.find("tr.selected").length > 0) {
			$("#del-pref").removeClass("disabled");
			$("#clear-pref").removeClass("disabled");
		} else {
			$("#del-pref").addClass("disabled");
			$("#clear-pref").addClass("disabled");
		}
	},

	/**
	 * 更新统计信息
	 * @param data
     */
	updateTotal: function(data){
		var that = this;
		var toalFreeAmount = data.toalFreeAmount;
		var toalDebitAmount = data.toalDebitAmount;
		var toalDebitAmountMany = data.toalDebitAmountMany;
		var adjAmout = data.adjAmout;
		var payamount = data.payamount;
		var originalOrderAmount = data.originalOrderAmount;
		var amount = data.amount;


		//设置统计
		$('#discount-amount').text(amount);
		$('#amount').text(originalOrderAmount);
		$('#should-amount').text(payamount);
		$('#cash input').val(payamount);
		var totalHtml = '<ul class="pay-total"> ' +
			'<li style="' + (parseInt(toalDebitAmount, 10) === 0 && 'display:none;')  + '">挂账:' + toalDebitAmount + '</li> ' +
			'<li style="' + (parseInt(toalFreeAmount, 10)  === 0 && 'display:none;')  + '">优免:' + toalFreeAmount + '</li> ' +
			'<li style="' + (parseInt(adjAmout, 10)  === 0 && 'display:none;')  + '">优免调整:' + adjAmout + '</li> ' +
			'<li style="' + (parseInt(toalDebitAmountMany, 10)  === 0 && 'display:none;')  + '">挂账多收:' + toalDebitAmountMany + '</span></li> ' +
			'<li style="' + (parseInt(payamount, 10)  === 0 && 'display:none;')  + '">现金:' + payamount + '</li> ' +
			'</ul>';
		$('.pay-div').after(totalHtml);

		//设置优惠回传值
		that.manageUsePref.set({
			"preferentialAmt": amount,//总优惠
			"toalFreeAmount": toalFreeAmount,//优免
			"toalDebitAmount": toalDebitAmount,//挂账
			"toalDebitAmountMany": toalDebitAmountMany,//挂账多收
			"adjAmout": adjAmout,//优免调整
		});
	},

	/**
	* 管理每次使用优惠信息接口
	*/
	manageUsePref: (function(){
		var orderId = $('input[name=orderid]').val();
		var prefInfoCache = {
			"orderid": orderId
		}

		var _init = function(obj){
			_set(obj);
		};

		var _set = function(obj){
			prefInfoCache = $.extend({}, prefInfoCache, obj);
		};

		var _get = function(key){
			if(arguments.length > 0) {
				return prefInfoCache[key]
			} else {
				return prefInfoCache;
			}
		};
		return {
			set: _set,
			get: _get,
			init: _init
		}
	})(),

	/**
	 * 更新订单信息
	 */
	updateOrder: function () {
		var that = this;
		$.ajax({
			url: _config.interfaceUrl.GetOrderInfo,
			method: 'POST',
			contentType: "application/json",
			data: JSON.stringify({
				orderid: $('input[name=orderid]').val()
			}),
			dataType:'json',
			async: false,
			success: function(res){
				if(res.code === '0') {

					that.updateTotal(res.data.preferentialInfo);

					//已经选择菜品
					var tr = '';
					var $body = $("#order-dish-table tbody");

					$.each(res.data.rows, function(k,v){
						tr += "<tr dishid='" + v.dishid + "' unit='" + v.dishunit + "'><td class='dishname'>" + v.dishname + "</td><td class='unit'>" + v.dishnum +"</td><td class='num'>" + v.dishunit +"</td><td class='orderprice'>" + v.orderprice*v.dishnum + "</td></tr>";
					});
					$body.prepend(tr);

					widget.loadPage({
						obj : "#order-dish-table tbody tr",
						listNum : 6,
						currPage : currPage,
						totleNums : $body.find('tr').length,
						curPageObj : "#order-modal #curr-page1",
						pagesLenObj : "#order-modal #pages-len1",
						prevBtnObj : "#order-modal .dish-oper-btns .prev-btn",
						nextBtnObj : "#order-modal .dish-oper-btns .next-btn",
						callback : function() {
							$body.find('tr').removeClass("selected");
							$body.find('tr').not(".hide").eq(0).addClass(
								"selected");
						}
					});

					//初始化已经使用的优惠
					if(res.data.preferentialInfo.detailPreferentials.length > 0) {
						that.updateSelectedPref(res.data.preferentialInfo.detailPreferentials);
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

	////已点菜上一页
	//$(".dish-oper-btns .prev-btn").click(function() {
	//	if ($(this).hasClass("disabled")) {
	//		return false;
	//	}
	//	page1(nowPage1 - 1);
	//});
	////已点菜下一页
	//$(".dish-oper-btns .next-btn").click(function() {
	//	if ($(this).hasClass("disabled")) {
	//		return false;
	//	}
	//	page1(nowPage1 + 1);
	//});


	//已选优惠上一页
	//$(".preferential-oper-btns .prev-btn").click(
	//		function() {
	//			if ($(this).hasClass("disabled")) {
	//				return false;
	//			}
	//			page2(nowPage2 - 1);
	//		});
	////已选优惠下一页
	//$(".preferential-oper-btns .next-btn").click(
	//		function() {
	//			if ($(this).hasClass("disabled")) {
	//				return false;
	//			}
	//			page2(nowPage2 + 1);
	//		});
	////优惠上一页
	//$(".main-div .prev-btn").click(function() {
	//	if ($(this).hasClass("disabled")) {
	//		return false;
	//	}
	//	page3(nowPage3 - 1);
	//});
	////优惠下一页
	//$(".main-div .next-btn").click(function() {
	//	if ($(this).hasClass("disabled")) {
	//		return false;
	//	}
	//	page3(nowPage3 + 1);
	//});

	$(".tab-payment ul li").click(function(){
		$(".tab-payment ul li").removeClass("active");
		$(this).addClass("active");

		$(".paytype-input").addClass("hide");
		var targetId = $(this).attr("target");
		$(targetId).removeClass("hide");
	});

	//initPreferentialType();


	$(document).click(function(e){
		$(".more-oper").addClass("hide");
		e.stopPropagation();
	});
	$(".show-more").click(function(e){
		$(".more-oper").removeClass("hide");
		e.stopPropagation();
	});

	//清空购物车优惠
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
//function page1(currPage) {
//	nowPage1 = widget.loadPage({
//		obj : "#order-dish-table tbody tr",
//		listNum : 6,
//		currPage : currPage,
//		totleNums : $("#order-dish-table tbody tr").length,
//		curPageObj : "#order-modal #curr-page1",
//		pagesLenObj : "#order-modal #pages-len1",
//		prevBtnObj : "#order-modal .dish-oper-btns .prev-btn",
//		nextBtnObj : "#order-modal .dish-oper-btns .next-btn",
//		callback : function() {
//			$("#order-dish-table tbody tr").removeClass("selected");
//			$("#order-dish-table tbody tr").not(".hide").eq(0).addClass(
//					"selected");
//		}
//	});
//}
// 已选优惠分页
//function page2(currPage) {
//	nowPage2 = widget.loadPage({
//		obj : "#sel-preferential-table tbody tr",
//		listNum : 6,
//		currPage : currPage,
//		totleNums : $("#sel-preferential-table tbody tr").length,
//		curPageObj : "#order-modal #curr-page2",
//		pagesLenObj : "#order-modal #pages-len2",
//		prevBtnObj : "#order-modal .preferential-oper-btns .prev-btn",
//		nextBtnObj : "#order-modal .preferential-oper-btns .next-btn",
//		callback : function() {
//			$("#sel-preferential-table tbody tr").removeClass("selected");
//			$("#sel-preferential-table tbody tr").not(".hide").eq(0).addClass(
//					"selected");
//		}
//	});
//	Order.controlOperBtns();
//}
// 优惠分页
//function page3(currPage) {
//	nowPage3 = widget.loadPage({
//		obj : ".preferentials-content .preferential-info",
//		listNum : 16,
//		currPage : currPage,
//		totleNums : $(".preferentials-content .preferential-info").length,
//		curPageObj : "#order-modal #curr-page3",
//		pagesLenObj : "#order-modal #pages-len3",
//		prevBtnObj : "#order-modal .main-div .prev-btn",
//		nextBtnObj : "#order-modal .main-div .next-btn"
//	});
//}

//function trClickEvent(){
//	// 选中已点菜品
//	$("#order-dish-table tbody tr").click(function() {
//		$("#order-dish-table tbody tr").removeClass("selected");
//		$(this).addClass("selected");
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