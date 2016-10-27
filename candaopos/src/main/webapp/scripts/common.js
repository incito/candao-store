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
    $(document).bind("ajaxSend", function () {
			utils.loading.open('正在加载…')

    }).bind("ajaxComplete", function () {
      utils.loading.remove();
    }).bind('ajaxError',function () {
    	if($('.errorAlert').length<1){
			widget.modal.alert({
				cls: 'fade in errorAlert',
				content:'<strong>数据加载失败，请稍后重试</strong>',
				width:500,
				height:500,
				btnOkTxt: '',
				btnCancelTxt: '确定'
			});
		}

    });

	if(utils.getUrl.get('tips')) {
		$.each(decodeURI(utils.getUrl.get('tips')).split('|'), function(k,v){
			rightBottomPop.alert({
				content: v
			});
		})
	}


});

javascript:window.history.forward(1);
function goBack(){
	window.history.back(-1);
}

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
	GivePrefer: "/newspicyway/padinterface/givePrefer.json", <!--获取已经使用的赠菜-->
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
	AddMemberSaleInfo: "/newspicyway/member/AddOrderMember.json", <!--添加会员消op费信息-->
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
	ConsumInfo: "/newspicyway/padinterface/consumInfo.json", <!-- 统计信息 -->
	GetTableDishInfos: "/newspicyway/datasnap/rest/TServerMethods1/GetOrder/", <!--获取餐桌菜单上的所有菜品信息-->
	GetTableDishInfoByOrderId: "/newspicyway/datasnap/rest/TServerMethods1/GetOrderByOrderid/", <!--获取餐桌菜单上的所有菜品信息-->
	GetDinnerWareInfo: "/newspicyway/datasnap/rest/TServerMethods1/getCJFood/", <!--获取餐具的数据-->
	GetAllDishInfos: "/newspicyway/datasnap/rest/TServerMethods1/getAllWmFood/", <!--获取所有菜品信息-->
	GetDishStatus: "/newspicyway/datasnap/rest/TServerMethods1/getFoodStatus/", <!--获取菜品的状态，是否估清-->
	GetFavorable: "/newspicyway/datasnap/rest/TServerMethods1/getFavorale/", <!--获取优惠信息-->
	Clearner: "/newspicyway/datasnap/rest/TServerMethods1/clearMachine/", <!--清机-->
	EndWork: "/newspicyway/datasnap/rest/TServerMethods1/endWork/", <!--结业不需要传递参数-->
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
	QueryCanDao: "/member/memberManager/findByParams.json", <!--餐道会员查询-->
	StorageCanDao: "/member/deal/StoreCardToNewPos.json", <!--餐道会员储值-->
	ModifyPwdCanDao: "/member/memberManager/MemberEdit.json", <!--修改密码-->
	SendVerifyCode: "/member/memberManager/sendAccountByMobile", <!--发送验证码-->
	MobileRepeatCheck: "/member/memberManager/validateTbMemberManager", <!--验证手机号是否重复-->
	RegistCanDao: "/member/memberManager/save.json", <!--餐道会员会员注册-->
	ReportLossCanDao: "/member/deal/CardLose.json", <!--餐道会员会员挂失-->
	CancelCanDao: "/member/memberManager/delete.json", <!--餐道会员会员注销-->
	SaleCanDao: "/member/deal/MemberSale.json", <!--餐道会员会员消费-->
	VoidSaleCanDao: "/member/deal/VoidSale.json", <!--餐道会员取消消费-->

	CancelLossCanDao: "/member/deal/UnCardLose", <!--餐道会员会员挂失-->
	VipChangeCardNum: "/member/rest/memberCardService/cardNoByModify", <!--修改会员卡号-->
	VipChangeInfo: "/member/rest/memberService/memberEditService", <!--修改会员基本信息-->
	VipQuery: "/member/rest/memberCardService/getCardNoByMobile", <!--会员查询(一户多卡)-->
	VipInsertCard: "/member/rest/memberCardService/bindingCardService", <!--新增会员实体卡号-->
	VipCheckCard: "/member/memberManager/byUserTouse.json", <!--判断会员实体卡-->
	VipChangePsw: "/member/memberManager/MemberEdit.json", <!--会员密码修改（新）-->
	GetCouponList: "/member/preferential/posPreferentialList", <!--获取优惠列表-->
    GetMemberAddress: "/newspicyway/padinterface/getconfiginfos", <!--请求会员地址-->


    /*雅座接口*/
	Yafindmember:'/datasnap/rest/TServerMethods1/QueryBalance/',<!--雅座会员查询-->
	Yarecharge:'/datasnap/rest/TServerMethods1/StoreCardDeposit/',<!--雅座会员储值-->
	YaCardActive:'/datasnap/rest/TServerMethods1/CardActive/',<!--雅座会员激活-->

	/*pos配置*/
	Config:'/newspicyway/pos/scripts/config.json',<!--雅座会员激活-->
};
//优惠分类
_config.preferential = {
	'05': '团购',
	'01': '特价',
	'02': '折扣',
	'03': '代金券',
	'04': '礼品券',
	'88': '会员',
	'00': '其他优惠',
	'08': '合作单位',
	'-1': '不常用'
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
		var modalId = _getId();
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
			hasFooter: true,
			btnOkCb: function(){
				_close(modalId);
			},
			btnCancelCb:function(){
				_close(modalId);
			}

		};

		ops = $.extend({},ops, options);

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
					if($(this).hasClass('disabled')) return false;
					ops.btnOkCb && ops.btnOkCb.call(this);
				})
			}


			if(!ops.hasFooter) {
				$modal.find('.modal-footer').remove();
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
/*
 * pagination.js 2.0.7
 * A jQuery plugin to provide simple yet fully customisable pagination
 * https://github.com/superRaytin/paginationjs

 * Homepage: http://paginationjs.com
 *
 * Copyright 2014-2100, superRaytin
 * Released under the MIT license.
 */
!function(a,b){function c(a){throw new Error("Pagination: "+a)}function d(a){a.dataSource||c('"dataSource" is required.'),"string"==typeof a.dataSource?"undefined"==typeof a.totalNumber?c('"totalNumber" is required.'):b.isNumeric(a.totalNumber)||c('"totalNumber" is incorrect. (Number)'):j.isObject(a.dataSource)&&("undefined"==typeof a.locator?c('"dataSource" is an Object, please specify "locator".'):"string"==typeof a.locator||b.isFunction(a.locator)||c(""+a.locator+" is incorrect. (String | Function)"))}function e(a){var c=["go","previous","next","disable","enable","refresh","show","hide","destroy"];b.each(c,function(b,c){a.off(i+c)}),a.data("pagination",{}),b(".paginationjs",a).remove()}function f(a,b){return("object"==(b=typeof a)?null==a&&"null"||Object.prototype.toString.call(a).slice(8,-1):b).toLowerCase()}"undefined"==typeof b&&c("Pagination requires jQuery.");var g="pagination",h="addHook",i="__pagination-";b.fn.pagination&&(g="pagination2"),b.fn[g]=function(f){if("undefined"==typeof f)return this;var g=b(this),h={initialize:function(){var a=this;if(g.data("pagination")||g.data("pagination",{}),a.callHook("beforeInit")!==!1){g.data("pagination").initialized&&b(".paginationjs",g).remove(),a.disabled=!!l.disabled;var c=a.model={pageRange:l.pageRange,pageSize:l.pageSize};a.parseDataSource(l.dataSource,function(b){if(a.sync=j.isArray(b),a.sync&&(c.totalNumber=l.totalNumber=b.length),c.totalPage=a.getTotalPage(),!(l.hideWhenLessThanOnePage&&c.totalPage<=1)){var d=a.render(!0);l.className&&d.addClass(l.className),c.el=d,g["bottom"===l.position?"append":"prepend"](d),a.observer(),g.data("pagination").initialized=!0,a.callHook("afterInit",d)}})}},render:function(a){var c=this,d=c.model,e=d.el||b('<div class="paginationjs"></div>'),f=a!==!0;c.callHook("beforeRender",f);var g=d.pageNumber||l.pageNumber,h=l.pageRange,i=d.totalPage,j=g-h,k=g+h;return k>i&&(k=i,j=i-2*h,j=1>j?1:j),1>=j&&(j=1,k=Math.min(2*h+1,i)),e.html(c.createTemplate({currentPage:g,pageRange:h,totalPage:i,rangeStart:j,rangeEnd:k})),c.callHook("afterRender",f),e},createTemplate:function(a){var c,d,e=this,f=a.currentPage,g=a.totalPage,h=a.rangeStart,i=a.rangeEnd,j=l.totalNumber,k=l.showPrevious,m=l.showNext,n=l.showPageNumbers,o=l.showNavigator,p=l.showGoInput,q=l.showGoButton,r=l.pageLink,s=l.prevText,t=l.nextText,u=l.ellipsisText,v=l.goButtonText,w=l.classPrefix,x=l.activeClassName,y=l.disableClassName,z=l.ulClassName,A=b.isFunction(l.formatNavigator)?l.formatNavigator():l.formatNavigator,B=b.isFunction(l.formatGoInput)?l.formatGoInput():l.formatGoInput,C=b.isFunction(l.formatGoButton)?l.formatGoButton():l.formatGoButton,D=b.isFunction(l.autoHidePrevious)?l.autoHidePrevious():l.autoHidePrevious,E=b.isFunction(l.autoHideNext)?l.autoHideNext():l.autoHideNext,F=b.isFunction(l.header)?l.header():l.header,G=b.isFunction(l.footer)?l.footer():l.footer,H="",I='<input type="text" class="J-paginationjs-go-pagenumber">',J='<input type="button" class="J-paginationjs-go-button" value="'+v+'">';if(F&&(c=e.replaceVariables(F,{currentPage:f,totalPage:g,totalNumber:j}),H+=c),k||n||m){if(H+='<div class="paginationjs-pages">',H+=z?'<ul class="'+z+'">':"<ul>",k&&(1===f?D||(H+='<li class="'+w+"-prev "+y+'"><a>'+s+"</a></li>"):H+='<li class="'+w+'-prev J-paginationjs-previous" data-num="'+(f-1)+'" title="Previous page"><a href="'+r+'">'+s+"</a></li>"),n){if(3>=h)for(d=1;h>d;d++)H+=d==f?'<li class="'+w+"-page J-paginationjs-page "+x+'" data-num="'+d+'"><a>'+d+"</a></li>":'<li class="'+w+'-page J-paginationjs-page" data-num="'+d+'"><a href="'+r+'">'+d+"</a></li>";else l.showFirstOnEllipsisShow&&(H+='<li class="'+w+"-page "+w+'-first J-paginationjs-page" data-num="1"><a href="'+r+'">1</a></li>'),H+='<li class="'+w+"-ellipsis "+y+'"><a>'+u+"</a></li>";for(d=h;i>=d;d++)H+=d==f?'<li class="'+w+"-page J-paginationjs-page "+x+'" data-num="'+d+'"><a>'+d+"</a></li>":'<li class="'+w+'-page J-paginationjs-page" data-num="'+d+'"><a href="'+r+'">'+d+"</a></li>";if(i>=g-2)for(d=i+1;g>=d;d++)H+='<li class="'+w+'-page J-paginationjs-page" data-num="'+d+'"><a href="'+r+'">'+d+"</a></li>";else H+='<li class="'+w+"-ellipsis "+y+'"><a>'+u+"</a></li>",l.showLastOnEllipsisShow&&(H+='<li class="'+w+"-page "+w+'-last J-paginationjs-page" data-num="'+g+'"><a href="'+r+'">'+g+"</a></li>")}m&&(f==g?E||(H+='<li class="'+w+"-next "+y+'"><a>'+t+"</a></li>"):H+='<li class="'+w+'-next J-paginationjs-next" data-num="'+(f+1)+'" title="Next page"><a href="'+r+'">'+t+"</a></li>"),H+="</ul></div>"}return o&&A&&(c=e.replaceVariables(A,{currentPage:f,totalPage:g,totalNumber:j}),H+='<div class="'+w+'-nav J-paginationjs-nav">'+c+"</div>"),p&&B&&(c=e.replaceVariables(B,{currentPage:f,totalPage:g,totalNumber:j,input:I}),H+='<div class="'+w+'-go-input">'+c+"</div>"),q&&C&&(c=e.replaceVariables(C,{currentPage:f,totalPage:g,totalNumber:j,button:J}),H+='<div class="'+w+'-go-button">'+c+"</div>"),G&&(c=e.replaceVariables(G,{currentPage:f,totalPage:g,totalNumber:j}),H+=c),H},go:function(a,c){function d(a){if(e.callHook("beforePaging",h)===!1)return!1;if(f.direction="undefined"==typeof f.pageNumber?0:h>f.pageNumber?1:-1,f.pageNumber=h,e.render(),e.disabled&&!e.sync&&e.enable(),g.data("pagination").model=f,b.isFunction(l.formatResult)){var d=b.extend(!0,[],a);j.isArray(a=l.formatResult(d))||(a=d)}g.data("pagination").currentPageData=a,e.doCallback(a,c),e.callHook("afterPaging",h),1==h&&e.callHook("afterIsFirstPage"),h==f.totalPage&&e.callHook("afterIsLastPage")}var e=this,f=e.model;if(!e.disabled){var h=a,i=l.pageSize,k=f.totalPage;if(h=parseInt(h),!(!h||1>h||h>k)){if(e.sync)return void d(e.getDataSegment(h));var m={},n=l.alias||{};m[n.pageSize?n.pageSize:"pageSize"]=i,m[n.pageNumber?n.pageNumber:"pageNumber"]=h;var o={type:"get",cache:!1,data:{},contentType:"application/x-www-form-urlencoded; charset=UTF-8",dataType:"json",async:!0};b.extend(!0,o,l.ajax),b.extend(o.data||{},m),o.url=l.dataSource,o.success=function(a){d(e.filterDataByLocator(a))},o.error=function(a,b,c){l.formatAjaxError&&l.formatAjaxError(a,b,c),e.enable()},e.disable(),b.ajax(o)}}},doCallback:function(a,c){var d=this,e=d.model;b.isFunction(c)?c(a,e):b.isFunction(l.callback)&&l.callback(a,e)},destroy:function(){this.callHook("beforeDestroy")!==!1&&(this.model.el.remove(),g.off(),b("#paginationjs-style").remove(),this.callHook("afterDestroy"))},previous:function(a){this.go(this.model.pageNumber-1,a)},next:function(a){this.go(this.model.pageNumber+1,a)},disable:function(){var a=this,b=a.sync?"sync":"async";a.callHook("beforeDisable",b)!==!1&&(a.disabled=!0,a.model.disabled=!0,a.callHook("afterDisable",b))},enable:function(){var a=this,b=a.sync?"sync":"async";a.callHook("beforeEnable",b)!==!1&&(a.disabled=!1,a.model.disabled=!1,a.callHook("afterEnable",b))},refresh:function(a){this.go(this.model.pageNumber,a)},show:function(){var a=this;a.model.el.is(":visible")||a.model.el.show()},hide:function(){var a=this;a.model.el.is(":visible")&&a.model.el.hide()},replaceVariables:function(a,b){var c;for(var d in b){var e=b[d],f=new RegExp("<%=\\s*"+d+"\\s*%>","img");c=(c||a).replace(f,e)}return c},getDataSegment:function(a){var b=l.pageSize,c=l.dataSource,d=l.totalNumber,e=b*(a-1)+1,f=Math.min(a*b,d);return c.slice(e-1,f)},getTotalPage:function(){return Math.ceil(l.totalNumber/l.pageSize)},getLocator:function(a){var d;return"string"==typeof a?d=a:b.isFunction(a)?d=a():c('"locator" is incorrect. (String | Function)'),d},filterDataByLocator:function(a){var d,e=this.getLocator(l.locator);if(j.isObject(a)){try{b.each(e.split("."),function(b,c){d=(d?d:a)[c]})}catch(f){}d?j.isArray(d)||c("dataSource."+e+" must be an Array."):c("dataSource."+e+" is undefined.")}return d||a},parseDataSource:function(a,d){var e=this,f=arguments;j.isObject(a)?d(l.dataSource=e.filterDataByLocator(a)):j.isArray(a)?d(l.dataSource=a):b.isFunction(a)?l.dataSource(function(a){b.isFunction(a)&&c('Unexpect parameter of the "done" Function.'),f.callee.call(e,a,d)}):"string"==typeof a?(/^https?|file:/.test(a)&&(l.ajaxDataType="jsonp"),d(a)):c('Unexpect data type of the "dataSource".')},callHook:function(c){var d,e=g.data("pagination"),f=Array.prototype.slice.apply(arguments);return f.shift(),l[c]&&b.isFunction(l[c])&&l[c].apply(a,f)===!1&&(d=!1),e.hooks&&e.hooks[c]&&b.each(e.hooks[c],function(b,c){c.apply(a,f)===!1&&(d=!1)}),d!==!1},observer:function(){var a=this,d=a.model.el;g.on(i+"go",function(d,e,f){e=parseInt(b.trim(e)),e&&(b.isNumeric(e)||c('"pageNumber" is incorrect. (Number)'),a.go(e,f))}),d.delegate(".J-paginationjs-page","click",function(c){var d=b(c.currentTarget),e=b.trim(d.attr("data-num"));return!e||d.hasClass(l.disableClassName)||d.hasClass(l.activeClassName)?void 0:a.callHook("beforePageOnClick",c,e)===!1?!1:(a.go(e),a.callHook("afterPageOnClick",c,e),l.pageLink?void 0:!1)}),d.delegate(".J-paginationjs-previous","click",function(c){var d=b(c.currentTarget),e=b.trim(d.attr("data-num"));return e&&!d.hasClass(l.disableClassName)?a.callHook("beforePreviousOnClick",c,e)===!1?!1:(a.go(e),a.callHook("afterPreviousOnClick",c,e),l.pageLink?void 0:!1):void 0}),d.delegate(".J-paginationjs-next","click",function(c){var d=b(c.currentTarget),e=b.trim(d.attr("data-num"));return e&&!d.hasClass(l.disableClassName)?a.callHook("beforeNextOnClick",c,e)===!1?!1:(a.go(e),a.callHook("afterNextOnClick",c,e),l.pageLink?void 0:!1):void 0}),d.delegate(".J-paginationjs-go-button","click",function(){var c=b(".J-paginationjs-go-pagenumber",d).val();return a.callHook("beforeGoButtonOnClick",event,c)===!1?!1:(g.trigger(i+"go",c),void a.callHook("afterGoButtonOnClick",event,c))}),d.delegate(".J-paginationjs-go-pagenumber","keyup",function(c){if(13===c.which){var e=b(c.currentTarget).val();if(a.callHook("beforeGoInputOnEnter",c,e)===!1)return!1;g.trigger(i+"go",e),b(".J-paginationjs-go-pagenumber",d).focus(),a.callHook("afterGoInputOnEnter",c,e)}}),g.on(i+"previous",function(b,c){a.previous(c)}),g.on(i+"next",function(b,c){a.next(c)}),g.on(i+"disable",function(){a.disable()}),g.on(i+"enable",function(){a.enable()}),g.on(i+"refresh",function(b,c){a.refresh(c)}),g.on(i+"show",function(){a.show()}),g.on(i+"hide",function(){a.hide()}),g.on(i+"destroy",function(){a.destroy()}),l.triggerPagingOnInit&&g.trigger(i+"go",Math.min(l.pageNumber,a.model.totalPage))}};if(g.data("pagination")&&g.data("pagination").initialized===!0){if(b.isNumeric(f))return g.trigger.call(this,i+"go",f,arguments[1]),this;if("string"==typeof f){var k=Array.prototype.slice.apply(arguments);switch(k[0]=i+k[0],f){case"previous":case"next":case"go":case"disable":case"enable":case"refresh":case"show":case"hide":case"destroy":g.trigger.apply(this,k);break;case"getSelectedPageNum":return g.data("pagination").model?g.data("pagination").model.pageNumber:g.data("pagination").attributes.pageNumber;case"getTotalPage":return g.data("pagination").model.totalPage;case"getSelectedPageData":return g.data("pagination").currentPageData;case"isDisabled":return g.data("pagination").model.disabled===!0;default:c("Pagination do not provide action: "+f)}return this}e(g)}else j.isObject(f)||c("Illegal options");var l=b.extend({},arguments.callee.defaults,f);return d(l),h.initialize(),this},b.fn[g].defaults={totalNumber:1,pageNumber:1,pageSize:10,pageRange:2,showPrevious:!0,showNext:!0,showPageNumbers:!0,showNavigator:!1,showGoInput:!1,showGoButton:!1,pageLink:"",prevText:"&laquo;",nextText:"&raquo;",ellipsisText:"...",goButtonText:"Go",classPrefix:"paginationjs",activeClassName:"active",disableClassName:"disabled",inlineStyle:!0,formatNavigator:"<%= currentPage %> / <%= totalPage %>",formatGoInput:"<%= input %>",formatGoButton:"<%= button %>",position:"bottom",autoHidePrevious:!1,autoHideNext:!1,triggerPagingOnInit:!0,hideWhenLessThanOnePage:!1,showFirstOnEllipsisShow:!0,showLastOnEllipsisShow:!0,callback:function(){}},b.fn[h]=function(a,d){arguments.length<2&&c("Missing argument."),b.isFunction(d)||c("callback must be a function.");var e=b(this),f=e.data("pagination");f||(e.data("pagination",{}),f=e.data("pagination")),!f.hooks&&(f.hooks={}),f.hooks[a]=f.hooks[a]||[],f.hooks[a].push(d)},b[g]=function(a,d){arguments.length<2&&c("Requires two parameters.");var e;return e="string"!=typeof a&&a instanceof jQuery?a:b(a),e.length?(e.pagination(d),e):void 0};var j={};b.each(["Object","Array"],function(a,b){j["is"+b]=function(a){return f(a)===b.toLowerCase()}}),"function"==typeof define&&define.amd&&define(function(){return b})}(this,window.jQuery);

/**
 * loadPage mini分页
 */
widget.loadPage = function(options){
	var settings = $.extend({
		obj: null,
		listNum: 14,
		currPage: 0,
		totleNums: 0,
		curPageObj: null,
		pagesLenObj: null,
		prevBtnObj: null,
		nextBtnObj: null,
		callback: null
	}, options);

	var doc = $(document);
	var $obj = $(settings.obj);
	var listNum = settings.listNum;
	var currPage = settings.currPage;
	var totleNums = settings.totleNums;
	var pagesLen = Math.ceil(totleNums / listNum);



	var goToPage = function(currPage){
		for (var i = 0; i < totleNums; i++) {
			$($obj[i]).addClass("hide");
		}
		for (var i = currPage * listNum; i < (currPage + 1) * listNum; i++) {
			if ($obj[i]) $($obj[i]).removeClass("hide");
		}
		pageNum = pagesLen == 0 ? 0 : (currPage + 1);

		$(settings.curPageObj).text(pageNum);
		$(settings.pagesLenObj).text(pagesLen);



		if (pageNum == 1 || pageNum == 0) {
			$(settings.prevBtnObj).addClass("disabled");
		} else {
			$(settings.prevBtnObj).removeClass("disabled");
		}
		if (pageNum < pagesLen) {
			$(settings.nextBtnObj).removeClass("disabled");
		} else {
			$(settings.nextBtnObj).addClass("disabled");
		}
	};

	goToPage(currPage);


	doc.undelegate(settings.prevBtnObj, 'click');
	doc.undelegate(settings.nextBtnObj, 'click');
	doc.delegate(settings.prevBtnObj, 'click', function(){
		if($(this).hasClass("disabled")){
			return false;
		}
		goToPage(--currPage)
	});

	doc.delegate(settings.nextBtnObj, 'click', function(){
		if($(this).hasClass("disabled")){
			return false;
		}
		goToPage(++currPage)
	});

	settings.callback && settings.callback();

	return currPage;
};

/**
 * 键盘组件
 * @param opts
 * @returns {*}
 */

var focusIpt;
widget.keyboard = function(opts){
	var defautlopts = {
		target: '.virtual-keyboard',
		chirdSelector: 'li',
		cb: $.noop
	};
	var doc = $(document);
	var opts = $.extend({},defautlopts, opts);

	var $target = $(opts.target);


	function _init(){
		$target.attr('keyboard-type', opts.type);
		_bindEvent();
	}

	function _bindEvent (){
		doc.undelegate(opts.target + ' ' + opts.chirdSelector,'click');
		doc.delegate(opts.target + ' ' + opts.chirdSelector,'click', function(){
			var me = $(this);
			if(focusIpt === null || me.hasClass('btn-action')) return false;

			var focusVal = focusIpt.val();
			var keyVal = $(this).text();

			if(keyVal == "←"){
				if(focusVal.length>0){
					focusIpt.focus();
					focusIpt.val(focusVal.substring(0,focusVal.length-1));
				}
			}else if(keyVal == "C"){
				focusIpt.val("");
				focusIpt.focus();
			}else{
				focusVal += keyVal;
				focusIpt.val(focusVal);
				focusIpt.focus();
			}

			opts.cb && opts.cb();
		});
		doc.undelegate('input[type=text],input[type=password],input[type=search]','focus');
		doc.delegate('input[type=text],input[type=password],input[type=search]','focus', function(){
			focusIpt = $(this);
		});

	}
	return _init()
};

/**
 * textarea输入modal
 * @returns {{show}}
 */
widget.textAreaModal = function(opts){
	var modalIns = null;
	var note_count = 20;
	var doc = $(document);

	var defautlopts = {
		title: '请输入原因',
		cls: 'textareaModal default-dialog',
		note: '',
		taregt: null,
		cb: null
	};
	var opts = $.extend({},defautlopts, opts);

	doc.undelegate('.textareaModal .btn-save','click');
	doc.delegate('.textareaModal .btn-save','click', function(){
		opts.target.val($('.textareaModal .J-textarea').val());
		modalIns.close();
	});

	doc.undelegate('.textareaModal .J-textarea','keyup');
	doc.delegate('.textareaModal .J-textarea','keyup', function(){
		var me = $(this);
		var value = me.val();
		var c = note_count;
		if(value != null && value != ""){
			c = note_count-value.length;
		}
		if(c <=0){
			c = 0;
		}
		$(".J-count").text(c);
	});

	doc.undelegate('.textareaModal .J-clear','click');
	doc.delegate('.textareaModal .J-clear','click', function(){
		$('.textareaModal .J-textarea').val('');
		$('.J-count').text(note_count);
	});

	doc.delegate('.textareaModal .btn-cancel ','click', function(){
		modalIns.close();
	});

	var html = '<div class="fl ">其他退菜原因：</div>' +
		'<div class="fr">还可以输入<span class="J-count">20</span>字</div>' +
		'<textarea class="form-control J-textarea" maxlength="20" rows="5" cols="80">' + opts.note + '</textarea>' +
		'<div class="btn-operate  ">' +
		'<button class="btn in-btn135 clear-btn J-clear" style="float: left;" type="button">清空</button>' +
			'<div style="text-align: right;">' +
				'<button class="btn btn-cancel in-btn135" type="button" >取消</button>' +
				'<button class="btn btn-save in-btn135 ml5" type="button">确认</button>' +
			'</div>' +
		'</div>';

	return {
		show: function(){
			modalIns = widget.modal.alert({
				cls: opts.cls,
				content:html,
				width:600,
				height:500,
				btnOkTxt: '',
				hasFooter: false,
				btnCancelTxt: ''
			});
		}
	}
}


/**
 * 设置底部Info
 * @param opts
 * @returns {*}
 */
var SetBotoomIfon = {
	init: function(){
		this.setSysInfo();
	},
	setSysInfo: function(){
		$('.J-sys-info')
			.find('.branch-num').text(utils.storage.getter('branch_branchcode'))
			.end().find('.user-info').text(utils.storage.getter('fullname') + ' ' + utils.storage.getter('aUserid'));

		setInterval(function(){
			$('.J-sys-info').find('.time').text(utils.date.current());
		},1000)
	}
}
/**
 * 右下角弹窗
 title:"提示信息",
 content:" ",内容
 width: 320,宽度
 height: 200,高度
 right:5,右边
 */
var rightBottomPop ={
	alert:function (opts) {
		var that=this;
		//取当前浏览器窗口大小
		var  windowWidth=$(document).width();
		var windowHeight=$(document).height();
		//参数和默认值
		var defaults = {
			title:"提示信息",
			content:" ",
			width: 320,
			height: 200,
			right:5,
		};
		var opts = $.extend({},defaults, opts);
		//弹窗的大小
		var html="<div class='rightBottomPop' style='width:"+opts.width+"px;height:"+opts.height+"px;bottom:"+(-opts.height)+"px;right:"+opts.right+"px;'>"+
			"<div class='rightBottomPop-title'>" +
			"<div class='rightBottomPop-title_left' ><b>"+opts.title+"</b></div>" +
			"<div class='rightBottomPop-title_right' onclick='rightBottomPop.close(this)'>×</div>" +
			"<div style='clear:both'></div>"+
			"</div>" +
			"<div class='rightBottomPop-content'>" + opts.content+ "</div>"+
			"</div>"
		$("body").append(html);
		var lengthCon=$(".rightBottomPop").length
		for( var i=0;i<lengthCon;i++){
			$(".rightBottomPop").eq(i).animate({bottom:i*opts.height},1500);
			$(".rightBottomPop").eq(i).css("bottom",i*opts.height);
		}
		setTimeout(function() {
			$('.rightBottomPop').remove()
		}, 10000);
	},
	close:function (a) {
		$(a).parent().parent().remove();
		var lengthCon=$(".rightBottomPop").length;
		if(lengthCon>0){
			var height=$(".rightBottomPop").css("height");
			height=height.substring(0,height.length-2)
			for( var i=0;i<lengthCon;i++){
				$(".rightBottomPop").eq(i).animate({bottom:i*height},1500);
				$(".rightBottomPop").eq(i).css("bottom",i*height)
			}
		}
	}

}
/************
 * 工具类
 ************/
var utils = utils || {};
utils.array = {
	indexOf : function(ret,val) {
		for (var i = 0; i < ret.length; i++) {
			if (ret[i] == val) return i;
		}
		return -1;
	},

	remove: function(ret, val) {
		var index = ret.indexOf(val);
		if (index > -1) {
			ret.splice(index, 1);
		}
	},

	unique:  function(ret) {
		var n = []; //一个新的临时数组
		for(var i = 0; i < ret.length; i++) //遍历当前数组
		{
			//如果当前数组的第i已经保存进了临时数组，那么跳过，
			//否则把当前项push到临时数组里面
			if (n.indexOf(ret[i]) == -1) n.push(ret[i]);
		}
		return n;
	}
};
utils.object = {
	isEmptyObject: function(obj){
		for (var key in obj) {
			return false
		}
		return true
	}
};
utils.string = {
	buffer: function(){
		function StringBuffer() {
			this.__strings__ = new Array();
		}
		StringBuffer.prototype.append = function (str) {
			this.__strings__.push(str);
			return this;    //方便链式操作
		};
		StringBuffer.prototype.toString = function () {
			return this.__strings__.join("");
		};
		return new StringBuffer();
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
			length--;
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
		var hours = date.getHours();
		var mins = date.getMinutes();
		var secs = date.getSeconds();
		if (month >= 1 && month <= 9) {
			month = "0" + month;
		}
		if (strDate >= 0 && strDate <= 9) {
			strDate = "0" + strDate;
		}
		if (hours >= 0 && hours <= 9) {
			hours = "0" + hours;
		}
		if (secs >= 0 && secs <= 9) {
			secs = "0" + secs;
		}if (mins >= 0 && mins <= 9) {
			mins = "0" + mins;
		}

		var currentdate = year + seperator1 + month + seperator1 + strDate
			+ " " + hours + seperator2 + mins
			+ seperator2 + secs;
		return currentdate;
	},


}
/**
 * utils.userRight.get(username,key)传入参数:用户名称，权限名称;返回 true false
 */
utils.userRight={
     get:function (username, key) {
		var aUserid=utils.storage.getter('aUserid'),user_rights;//获取缓存用户名称
     	if(username==aUserid){
     		user_rights=JSON.parse(utils.storage.getter('user_rights'));
			if(user_rights[key]=="1"){
				return true
			}
			else {
				return false
			}
		}
		else {//从服务器获取
			var result = null;
			$.ajax({
				url: _config.interfaceUrl.GetUserRight,
				method: 'POST',
				contentType: "application/json",
				data: JSON.stringify({
					username: username
				}),
				dataType:'json',
				async: false,
				success: function(res){
					if(res.result === '0') {
						user_rights=res.rights;
						if(user_rights[key]=="1"){
							result=true;
						}
						else {
							result=false
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
			return result
		}
	 }
}
/**
 * utils.getUrl.get(name)传入参数:要获取参数的名称
 */
utils.getUrl={//获取浏览器参数
	get:function (name) {
		var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if(r!=null)return  unescape(r[2]); return null;
	}
}
/**
 * utils.getUrl.get()//打印清机单
 */
utils.reprintClear={//打印清机单
	get:function () {
		var posId=utils.storage.getter('posid'),jsorder=" ";
		$.ajax({
			url:_config.interfaceUrl.PrintClearMachine+'/'+ utils.storage.getter('aUserid') + '+/'+jsorder+'/'+utils.storage.getter('posid')+'/',
			type: "get",
			success: function (data) {
                if(data.result=='0'){
                    rightBottomPop.alert({
                        content:"清机单打印完成",
                    })
                }
                else {
                    utils.printError.alert('清机单打印失败，请稍后重试')
                }
			}
		});
	}
}
utils.clearLocalStorage={
	clear:function (key) {//清除传递的指定
		if(key==undefined){
			localStorage.clear();
		}
		else {
			for(var i in key){
				utils.storage.remove(key[i])
			}
		}

	},
	clearSelect:function () {//清除固定的缓存
		var clearLocal={
				'aUserid':'aUserid',
				'branch_branchcode':'branch_branchcode',
				'branch_branchid':'branch_branchid',
				'branch_branchaddress':'branch_branchaddress',
			    'branch_branchname':'branch_branchname',
			    'branch_id':'branch_id',
			    'branch_insertime':'branch_insertime',
			    'branch_managerid':'branch_managerid',
			    'branch_managername':'branch_managername',
			    'branch_managertel':'branch_managertel',
			    'branch_padversion':'branch_padversion',
			    'branch_serverversion':'branch_serverversion',
			    'branch_tenantid':'branch_tenantid',
			    'branch_updatetime':'branch_updatetime',
			    'checkout_fullname':'checkout_fullname',
			    'fullname':'fullname',
			    'loginTime':'loginTime',
			    'user_rights':'user_rights',
			    'setTentimes':'setTentimes',
			    'tenTimes':'tenTimes'
			}
		for(var i in clearLocal){
			utils.storage.remove(clearLocal[i])
		}
	}
}
utils.loading ={
	open:function (msg) {
			var str='<div class="lading-shade" ></div><div class="spinner">'+
				'<div class="rect1"></div>'+
				'<div class="rect2"></div>'+
				'<div class="rect3"></div>'+
				'<div class="rect4"></div>'+
				'<div class="rect5"></div>'+
				'<p>'+msg+'</p>'
			'</div>';
		if($('.spinner').length<1){
			$('body').append(str);
		}
	},
	remove:function () {
		$('.lading-shade,.spinner').remove();
	}
}
utils.printError={//打印失败
    alert:function (msg) {
        widget.modal.alert({
            cls: 'fade in',
            content:'<strong>'+msg+'</strong>',
            width:500,
            height:500,
            btnOkTxt: '',
            btnCancelTxt: '确定'
        });
    }
}
/*打印机异常*/
utils.printAbnormal={
	int:function () {
		this.get();
	},
	get:function () {//获取打印机列表
		var that = this,timedata='';
		$.ajax({
			url:'/newspicyway/pos/printerlist.json',
			type: "get",
			dataType: "json",
			async:false,
			global: false,
			success: function (data) {
				var arry=[];
				for( var i=0;i<data.data.length;i++) {
					if(data.data[i].status!='1'){
						arry.push(data.data[i])
					}
				};
				if(arry.length>0){
					var str='<div id="printAbnormal">检测到'+arry.length+'个打印机异常，请到"系统">"打印机列表"查看并修复<br><br><br>'
						str +='<label><input  type="checkbox" value="true" class="printAbnormalinput" /><span style="padding-right: 5px"></span>10分钟以内不在提醒</label></div>'
					if($("#printAbnormal").length<1){
						widget.modal.alert({
							cls: 'fade in printAbnormal',
							content:str,
							title:'',
							width:300,
							height:400,
							btnOkTxt: '',
							btnCancelTxt: '确定',
							btnCancelCb:function () {
								if($('.printAbnormalinput').prop("checked")==true){
									that.set(10*60*1000)
								}
								else {
									that.set(60*1000)
								}
							}
						});
					}
					$('.printAbnormal .modal-header span').hide()
				}

			},
		});

	},
	set:function (time) {
		var that=this,timeLeft=time;//这里设定的时间是;
		setTentimes=time
	}

}
var setTentimes=utils.storage.getter('setTentimes')
if(setTentimes==null||setTentimes==0){
	setTentimes=60*1000
}
/*定时任务检查打印是否异常*/
setInterval(function () {
	if(setTentimes>0){
		setTentimes =setTentimes-1000
		utils.storage.setter('setTentimes',setTentimes);
		//console.log(utils.storage.getter('setTentimes'))
	}
	if(setTentimes==0 && $("#printAbnormal").length<1){
		    setTentimes=60*1000
			var hrefLink=document.location.href;
			if(hrefLink.indexOf('login.jsp')>-1 || hrefLink.indexOf('openpage.jsp')>-1){

			}else {
				utils.printAbnormal.int();
			}
	}
},1000);
/*多张会员卡选择
* 传入参数为对象
* {'data':data,传入卡号列表['卡号一','卡号二']
* 'callback':回调方法，如果回调方法需要再次调用查询会员卡号，回调方法的参数名必须为'chooseNo'}
* */
utils.chooseMember={
	choose:function (msg) {
		var chooseNo=null
		var str=' <div class="coupon-cnt">';
		for(var i=0 ;i<msg.data.length;i++){
			str+='<div class="memberChoose-item">'+msg.data[i]+'</div>';
		}
		str+='</div>';
		widget.modal.alert({
			cls: 'fade in memberChoose',
			content:str,
			width:520,
			height:500,
			title:'请选择会员卡',
			btnOkTxt: '确定',
			btnCancelTxt: '取消',
			btnOkCb:function () {
				if($('.memberChoose-active').length<1){
					utils.printError.alert('请选择会员卡号')
				}
				else {
					$(".modal-alert:last,.modal-backdrop:last").remove();
					eval(msg.callback)
				}

			}
		});
		$('body').on('click','.memberChoose-item',function () {
			$(this).addClass('memberChoose-active').siblings('.memberChoose-item').removeClass('memberChoose-active');
			chooseNo= $(this).text()
		});
	}
}
/*弹钱箱
* 只有utils.storage.getter('cashbox')=='1'时才开启*/
utils.openCash=function () {
	if(utils.storage.getter('cashbox')=='1'){
		$.ajax({
			url: _config.interfaceUrl.OpenCash + utils.storage.getter('ipaddress'),
			method: 'POST',
			contentType: "application/json",
			dataType: 'json',
			success: function (res) {
				if (res.result[0] === '1') {//成功

				}
				widget.modal.alert({
					cls: 'fade in',
					content: '<strong>' + (res.Info === undefined ? '打开钱箱成功' : res.Info) + '</strong>',
					width: 500,
					height: 500,
					btnOkTxt: '',
					btnCancelTxt: '确定'
				});
			}
		});
	}
}



