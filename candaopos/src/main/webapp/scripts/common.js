$(document).ready(function(){
	// Copyright 2014-2015 Twitter, Inc.
	// Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
	if (navigator.userAgent.match(/IEMobile\/10\.0/)) {
	  var msViewportStyle = document.createElement('style');
	  msViewportStyle.appendChild(
	    document.createTextNode(
	      '@-ms-viewport{width:auto!important}'
	    )
	  );
	  document.querySelector('head').appendChild(msViewportStyle);
	}
});



function goBack(){
	window.history.back(-1);
}


//Array.prototype.indexOf = function(val) {
//    for (var i = 0; i < this.length; i++) {
//        if (this[i] == val) return i;
//    }
//    return -1;
//};
//Array.prototype.remove = function(val) {
//    var index = this.indexOf(val);
//    if (index > -1) {
//        this.splice(index, 1);
//    }
//};
/************
 * 配置项
 * basePath
 * InterfaceUrl  接口地址
 ************/

var _config = _config || {};
_config.projectName = 'pos';
_config.basePath = '/newspicyway/pos/';
_config.interfaceUrl = {
	AuthorizeLogin: "/newspicyway/padinterface/login.json", <!--授权登录-->
	GetUserRight: "/newspicyway/padinterface/userrights.json", <!--获取用户权限-->
	GetAllTableInfos: "/newspicyway/padinterface/querytables.json", <!--获取所有餐桌信息-->
	GetSystemSetData: "/newspicyway/padinterface/getSystemSetData.json", <!--获取系统设置-->
	VerifyUser: "/newspicyway/padinterface/verifyuser.json", <!--验证用户是否存在-->
	OpenTable: "/newspicyway/padinterface/setorder.json", <!--餐桌开台-->
	GetOrderInvoice: "/newspicyway/padinterface/findInvoiceByOrderid.json", <!--获取订单发票-->
	UpdateOrderInvoice: "/newspicyway/padinterface/updateInvoice.json", <!--更新发票信息-->
	EndWorkSyncData: "/newspicyway/padinterface/jdesyndata.json", <!--结业后通知后台同步数据-->
	GetDishGroupInfos: "/newspicyway/padinterface/getMenuColumn.json", <!--获取菜品分类信息-->
	GetCouponInfos: "/newspicyway/padinterface/getPreferentialList.json", <!--获取优惠券信息-->
	CalcDiscountAmount: "/newspicyway/padinterface/usePreferentialItem.json", <!--使用优惠券-->
	DelPreferential: "/newspicyway/padinterface/delPreferentialItem.json", <!--删除优惠券-->
	GetOrderInfo: "/newspicyway/padinterface/getOrderInfo.json", <!--获取餐台账单明细（新接口）-->
	OrderDish: "/newspicyway/padinterface/bookorderList.json", <!--菜品下单-->
	OrderDishCf: "/newspicyway/padinterface/placeOrder.json", <!--菜品下单（咖啡）-->
	GetMenuComboDish: "/newspicyway/padinterface/getMenuCombodish.json", <!--获取套餐内菜品-->
	PayTheBill: "/newspicyway/padinterface/settleorder.json", <!--结账-->
	PayTheBillCf: "/newspicyway/padinterface/checkout.json", <!--咖啡模式结账-->
	GetAllBankInfo: "/newspicyway/bankinterface/getallbank.json", <!--获取所有银行-->
	GetAllOnCpyAccountInfo: "/newspicyway/padinterface/getCooperationUnit.json", <!--获取所有可挂账单位-->
	GetTradeTime: "/newspicyway/padinterface/getOpenEndTime.json", <!--获取营业时间-->
	CheckTheLastEndWork: "/newspicyway/padinterface/isYesterdayEndWork.json", <!--检测上一次是否结业-->
	GetAllUnclearnPosInfoes: "/newspicyway/padinterface/findUncleanPosList.json", <!--获取所有未结业POS信息-->
	GetOrderMemberInfo: "/newspicyway/member/GetOrderMember.json", <!--获取订单会员信息-->
	GetReportTipInfo: "/newspicyway/tip/tipList.json", <!--获取小费统计信息-->
	GetReportDishInfo: "/newspicyway/padinterface/getItemSellDetail.json", <!--获取品项销售统计信息-->
	GetBranchInfo: "/newspicyway/padinterface/getbranchinfo.json", <!--获取分店店铺信息-->
	MemberLogin: "/newspicyway/member/MemberLogin.json", <!--餐道会员登入-->
	MemberLogout: "/newspicyway/member/MemberLogout.json", <!--餐道会员登出-->
	AddMemberSaleInfo: "/newspicyway/member/AddOrderMember.json", <!--添加会员消费信息-->
	ClearTable: "/newspicyway/padinterface/cleantable.json", <!--清台-->
	ClearTableCf: "/newspicyway/padinterface/cleanTableSimply.json", <!--咖啡模式的清台-->
	AntiSettlementOrder: "/newspicyway/padinterface/rebacksettleorder.json", <!--反结算账单-->
	BackDish: "/newspicyway/padinterface/discarddish.json", <!--退菜-->
	UpdateDishWeigh: "/newspicyway/padinterface/updateDishWeight.json", <!--更新菜品重量-->
	TipBill: "/newspicyway/tip/tipBilling.json", <!--小费结算-->
	SetCouponFavor: "/newspicyway/padinterface/setPreferentialFavor.json", <!--设置不常用优惠-->
	GetTableInfoByTableType: "/newspicyway/padinterface/getTableByType.json", <!--根据餐桌类型获取餐桌信息-->
	GetPrinterList: "/newspicyway/pos/printerlist.json", <!--获取打印机状态列表-->
	GetItemForList: "/newspicyway/itemDetail/getItemForList.json", <!-- 获取营业明细（品类、金额）-->
	GetGrouponForList: "/newspicyway/preferentialAnalysisCharts/findPreferential.json", <!--获取营业明细(团购券)-->
	GetDayReportList: "/newspicyway/daliyReports/getDayReportList.json", <!-- 获取营业明细（其它）-->
	GetGzdwForList: "/newspicyway/gisterBill/getBillCount.json", <!--获取营业明细（获取挂账单位）-->
	GetTipMoney: "/newspicyway/tip/tipListByTime.json", <!--获取小费总额-->
	RestaurantOpened: "/newspicyway/datasnap/rest/TServerMethods1/OpenUp/", <!--店铺开业，分查询是否开业和开业-->
	PettyCashInput: "/newspicyway/datasnap/rest/TServerMethods1/InputTellerCash/", <!--零找金，分查询是否输入过零找金和输入零找金-->
	GetTableDishInfos: "/newspicyway/datasnap/rest/TServerMethods1/GetOrder/", <!--获取餐桌菜单上的所有菜品信息-->
	GetTableDishInfoByOrderId: "/newspicyway/datasnap/rest/TServerMethods1/GetOrderByOrderid/", <!--获取餐桌菜单上的所有菜品信息-->
	GetDinnerWareInfo: "/newspicyway/datasnap/rest/TServerMethods1/getCJFood/", <!--获取餐具的数据-->
	GetAllDishInfos: "/newspicyway/datasnap/rest/TServerMethods1/getAllWmFood/", <!--获取所有菜品信息-->
	GetDishStatus: "/newspicyway/datasnap/rest/TServerMethods1/getFoodStatus/", <!--获取菜品的状态，是否估清-->
	GetFavorable: "/newspicyway/datasnap/rest/TServerMethods1/getFavorale/", <!--获取优惠信息-->
	Clearner: "/newspicyway/datasnap/rest/TServerMethods1/clearMachine/", <!--清机-->
	EndWork: "/newspicyway/datasnap/rest/TServerMethods1/endWork/", <!--结业-->
	SaveCouponInfo: "/newspicyway/datasnap/rest/TServerMethods1/saveOrderPreferential/", <!--保存优惠券信息-->
	GetSavedCouponInfo: "/newspicyway/datasnap/rest/TServerMethods1/GetOrderCouponList/", <!--获取保存的优惠券信息-->
	BroadcastMsg: "/newspicyway/datasnap/rest/TServerMethods1/broadcastmsg/", <!--广播消息-->
	GetBackDishInfo: "/newspicyway/datasnap/rest/TServerMethods1/getBackDishInfo/", <!--退菜-->
	GetPrintOrderInfo: "/newspicyway/datasnap/rest/TServerMethods1/getOrderInfo/", <!--获取打印用订单信息-->
	GetClearPosInfo: "/newspicyway/datasnap/rest/TServerMethods1/getClearMachineData/", <!--获取打印用清机单信息-->
	CheckAntiSettleOrder: "/newspicyway/datasnap/rest/TServerMethods1/rebackorder/", <!--检测是否允许反结算账单-->
	SetMemberPrice: "/newspicyway/datasnap/rest/TServerMethods1/setMemberPrice/", <!--设置成会员价-->
	SetNormalPrice: "/newspicyway/datasnap/rest/TServerMethods1/setMemberPrice2/", <!--设置成正常价-->
	GetMemberPrintPayInfo: "/newspicyway/datasnap/rest/TServerMethods1/getMemberSaleInfo/", <!--获取会员消费打印信息-->
	OpenCash: "/newspicyway/datasnap/rest/TServerMethods1/OpenCash/", <!--开钱箱-->
	QueryOrderInfo: "/newspicyway/datasnap/rest/TServerMethods1/getAllOrderInfo2/", <!--账单查询-->
	GetFishPotDish: "/newspicyway/datasnap/rest/TServerMethods1/getGroupDetail/", <!--获取鱼锅信息-->
	CancelOrder: "/newspicyway/datasnap/rest/TServerMethods1/cancelOrder/", <!--外卖取消账单-->
	SetTakeoutOrderOnAccount: "/newspicyway/datasnap/rest/TServerMethods1/putOrder/", <!--外卖挂单-->
	SetOrderTakeout: "/newspicyway/datasnap/rest/TServerMethods1/wmOrder/", <!--设置订单为外卖单。-->

	PrintPay: "/newspicyway/print4POS/getOrderInfo", <!--打印预结，结算，客用-->
	PrintClearMachine: "/newspicyway/print4POS/getClearMachineData", <!--打印清机单-->
	PrintMemberSale: "/newspicyway/print4POS/getMemberSaleInfo", <!-- 打印会员消费-->
	PrintMemberStore: "/newspicyway/print4POS/StoreCardToNewPos.json", <!--打印会员储值-->
	PrintItemSell: "/newspicyway/print4POS/getItemSellDetail.json", <!--打印品项销售统计报表-->
	PrintTip: "/newspicyway/print4POS/tipList.json", <!--打印小费统计报表-->
	PrintBusinessDetail: "/newspicyway/print4POS/printBusinessDetail.json", <!--打印营业报表明细-->
	PrintInvoice: "/newspicyway/print4POS/printInvoice.json", <!--打印发票单-->
	<!--云接口-->
	QueryCanDao: "/member/memberManager/findByParams", <!--餐道会员查询-->
	StorageCanDao: "/member/deal/StoreCardToNewPos.json", <!--餐道会员储值-->
	ModifyPwdCanDao: "/member/memberManager/MemberEdit.json", <!--修改密码-->
	SendVerifyCode: "/member/memberManager/sendAccountByMobile", <!--发送验证码-->
	MobileRepeatCheck: "/member/memberManager/validateTbMemberManager", <!--验证手机号是否重复-->
	RegistCanDao: "/member/memberManager/save.json", <!--餐道会员会员注册-->
	ReportLossCanDao: "/member/deal/CardLose.json", <!--餐道会员会员挂失-->
	CancelCanDao: "/member/memberManager/delete.json", <!--餐道会员会员注销-->
	SaleCanDao: "/member/deal/MemberSale.json", <!--餐道会员会员消费-->
	VoidSaleCanDao: "/member/deal/VoidSale.json", <!--餐道会员取消消费-->

	VipChangeCardNum: "/member/rest/memberCardService/cardNoByModify", <!--修改会员卡号-->
	VipChangeInfo: "/member/rest/memberService/memberEditService", <!--修改会员基本信息-->
	VipQuery: "/member/rest/memberCardService/getCardNoByMobile", <!--会员查询(一户多卡)-->
	VipInsertCard: "/member/rest/memberCardService/bindingCardService", <!--新增会员实体卡号-->
	VipCheckCard: "/member/memberManager/byUserTouse.json", <!--判断会员实体卡-->
	VipChangePsw: "/member/memberManager/MemberEdit.json", <!--会员密码修改（新）-->
	GetCouponList: "/member/preferential/posPreferentialList", <!--获取优惠列表-->
};



/************
 * 组件类
 ************/

var widget = widget || {};

/*
 * 公共模态框
 * ***参数***
 *
 *  - `content` {String} 填充内容
 *  - `title` {String} 模态框标题
 *  - `width` {Number} 默认500
 *  - `height` {Number} 默认auto
 *  - `vertical` {Boolean} 默认true
 *  - `cls` {String} 为modal添加样式,多个用空格分开
 *  - `hasBtns` {Boolean} 是否有按钮 默认为true
 *  - `btnOkTxt` {String} ok按钮文字 为''时,不显示
 *  - `btnCancelTxt` {String} cancle按钮文字 为''时,不显示
 *  - `btnOkCb` {Function} ok按钮回调
 *  - `btnCancelCb` {Function} cancel按钮回调 默认带有关闭事件
 *  - `onReady` {Function} 创建完成后回调
 * ***方法***
 *
 * - `close()` 销毁modal
 * - `hide()`  隐藏modal
 * - `show()`  对hide的modal 显示
 * */
widget.modal = function () {
	var _tplHtml =
		'<div class="modal dialog-normal bg-gray created-modal [Cls]" id="[Id]">' +
		'<div class="modal-content" style="width:[Width]px;margin: 30px auto;">' +
		'<div class="modal-header">' +
		'<span class="close" data-dismiss="modal">×</span>' +
		'<h5 class="modal-title"><i class="icon-exclamation-sign"></i> [Title]</h5>' +
		'</div>' +
		'<div class="modal-body">' +
		'[Content]' +
		'</div>' +
		'<div class="modal-footer" >' +
		'<button type="button" class="btn-base btn-base-sm cancel" data-dismiss="modal">[BtnCancelTxt]</button>' +
		'<button type="button" class="btn-base btn-yellow btn-base-sm ok">[BtnOkTxt]</button>' +
		'</div>' +
		'</div>';
	'</div>';


	var reg = new RegExp("\\[([^\\[\\]]*?)\\]", 'igm');

	var _alert = function (options) {
		var id = _dialog(options);
		var modal = $('#' + id);
		modal.addClass('modal-alert');

		return {
			id: id,
			hide: function(){
				modal.modal('hide');
			},
			close: function(){
				_close(id);
			},
			show:function(){
				modal.modal('show');
			}
		};
	};

	var _confirm = function (options) {
		var id = _dialog(options);
		var modal = $('#' + id);
		modal.addClass('modal-confirm');
		return {
			id: id,
			hide: function(){
				modal.modal('hide');
			},
			close: function(){
				_close(id);
			},
			show:function(){
				modal.modal('show');
			}
		};
	};


	var _getId = function () {
		var date = new Date();
		return 'mdl' + date.valueOf();
	};

	var _close = function(id){
		var $modal = $('#' + id);
		$modal.next().remove();
		$modal.remove();
	};

	var _dialog = function (options) {
		var ops = {
			content: "提示内容",
			title: "操作提示",
			width: 500,
			height: 'auto',
			auto: false,
			cls:'',
			hasBtns: true,
			btnCancelTxt: '取消',
			btnOkTxt: '确定',
			vertical: true,
			onReady: null,
		};

		ops = $.extend({},ops, options);

		var modalId = _getId();

		var html = _tplHtml.replace(reg, function (node, key) {
			return {
				Id: modalId,
				Title: ops.title,
				Cls: ops.cls,
				Width: ops.width,
				Content: ops.content,
				BtnCancelTxt: ops.btnCancelTxt,
				BtnOkTxt: ops.btnOkTxt,
			}[key];
		});


		$('body').append(html);

		var $modal = $('#' + modalId);

		//按钮逻辑
		if(!ops.hasBtns) {
			$modal.find('.modal-footer').remove();
		} else {

			if(ops.btnOkTxt === '') {
				$modal.find('.ok').remove();
			} else {
				$modal.find('.ok').bind('click', function(){
					ops.btnOkCb && ops.btnOkCb.call(this);
				})
			}

			if(ops.btnCancelTxt === ''){
				$modal.find('.cancel').remove();
			} else {
				$modal.find('.cancel').bind('click', function(){
					ops.btnCancelCb && ops.btnCancelCb.call(this);
					_close(modalId);

				})
			}
		}


		ops.onReady && ops.onReady.call(this);

		//关闭按钮
		$modal.find('.close').bind('click', function(){
			_close(modalId);
		});

		if(ops.vertical) {
			$modal.css({
				'display': 'flex',
				'align-items': 'center'
			});
		}

		$modal.next('.modal-backdrop').addClass('.modal-backdrop-' + modalId);
		$modal.modal({
			width: ops.width,
			backdrop: 'static'
		});

		return modalId;
	};
	return {
		alert: _alert,
		confirm: _confirm
	}
}();

/**
 * 键盘组件
 * @param opts
 * @returns {*}
 */
widget.keyboard = function(opts){
	var defautlopts = {
		target: '.virtual-keyboard'
	};
	var doc = $(document);
	var opts = $.extend({},defautlopts, opts);
	var focusIpt;

	function _init(){
	 	focusIpt = null;
		_bindEvent();
	}

	function _bindEvent (){
		doc.delegate(opts.target + ' li','click', function(){

			if(focusIpt === null) return false;

			var focusVal = focusIpt.val();
			var keyVal = $(this).text();

			if(keyVal == "←"){
				if(focusVal.length>0){
					focusIpt.focus();
					focusIpt.val(focusVal.substring(0,focusVal.length-1));
				}
			}else{
				focusVal += keyVal;
				focusIpt.val(focusVal);
				focusIpt.focus();
			}
		});

		doc.delegate('input[type=text],input[type=password]','focus', function(){
			focusIpt = $(this);
		});
	};
	return _init()
};


/************
 * 工具类
 ************/
var utils = utils || {};
utils.array = {
	indexOf : function(val) {
		for (var i = 0; i < this.length; i++) {
			if (this[i] == val) return i;
		}
		return -1;
	},

	remove: function(val) {
		var index = this.indexOf(val);
		if (index > -1) {
			this.splice(index, 1);
		}
	}
};
/**
 * Map
 */
utils.HashMap = function(){
	/** 存放键的数组(遍历用到) */
	this.keys = new Array();
	//创建一个对象
	var obj = new Object();
	/**
	 * 判断Map是否为空
	 */
	this.isEmpty = function(){
		return this.keys.length == 0;
	};

	/**
	 * 判断对象中是否包含给定Key
	 */
	this.containsKey=function(key){
		return (key in obj);
	};

	/**
	 * 判断对象中是否包含给定的Value
	 */
	this.containsValue=function(value){
		for(var key in obj){
			if(obj[key] == value){
				return true;
			}
		}
		return false;
	};

	/**
	 *向map中添加数据
	 */
	this.put=function(key,value){
		if(!this.containsKey(key)){
			this.keys.push(key);
		}
		obj[key] = value;
	};

	/**
	 * 根据给定的Key获得Value
	 */
	this.get=function(key){
		return this.containsKey(key)?obj[key]:null;
	};

	/**
	 * 根据给定的Key删除一个值
	 */
	this.remove=function(key){
		if(this.containsKey(key)&&(delete obj[key])){
			this.keys.remove(key);
		}
	};

	/**
	 * 获得Map中的所有Value
	 */
	this.values=function(){
		var _values= new Array();
		for(var key in obj){
			_values.push(obj[key]);
		}
		return _values;
	};

	/**
	 * 获得Map中的所有Key
	 */
	this.keySet=function(){
		var _keys = new Array();
		for(var key in obj){
			_keys.push(key);
		}
		return _keys;
	};

	/**
	 * 获得Map的长度
	 */
	this.size = function(){
		return this.keys.length;
	};

	/**
	 * 清空Map
	 */
	this.clear = function(){
		this.keys = new Array();
		obj = new Object();
	};

	/**
	 * 遍历Map,执行处理函数
	 *
	 * @param {Function}
	 *            回调函数 function(key,value,index){..}
	 */
	this.each = function(fn) {
		if (typeof fn != 'function') {
			return;
		}
		var len =this.keys.length;
		for (var i = 0; i < len; i++) {
			var k = this.keys[i];
			fn(k, this.get(k), i);
		}
	};
};
/**
 * localStorage
 */
utils.storage = {
	getter: function (key) {
		return localStorage.getItem( _config.projectName + '_' + key);
	},
	setter: function (key, val) {
		return localStorage.setItem(_config.projectName + '_' + key, val);
	},
	remove: function(key){
		return localStorage.removeItem(_config.projectName + '_' + key);
	}
};
/**
 * date
 */
utils.date = {
	current: function() {
		var date = new Date();
		var seperator1 = "-";
		var seperator2 = ":";
		var year = date.getFullYear();
		var month = date.getMonth() + 1;
		var strDate = date.getDate();
		if (month >= 1 && month <= 9) {
			month = "0" + month;
		}
		if (strDate >= 0 && strDate <= 9) {
			strDate = "0" + strDate;
		}
		var currentdate = year + seperator1 + month + seperator1 + strDate
			+ " " + date.getHours() + seperator2 + date.getMinutes()
			+ seperator2 + date.getSeconds();
		return currentdate;
	},


}



