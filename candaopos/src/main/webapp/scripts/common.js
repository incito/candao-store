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

	var $obj = $(settings.obj);
	var listNum = settings.listNum;
	var currPage = settings.currPage;
	var totleNums = settings.totleNums;
	var pagesLen = Math.ceil(totleNums / listNum);

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

	settings.callback && settings.callback();

	return currPage;
};

/**
 * 键盘组件
 * @param opts
 * @returns {*}
 */
widget.keyboard = function(opts){
	var defautlopts = {
		target: '.virtual-keyboard',
		chirdSelector: 'li'
	};
	var doc = $(document);
	var opts = $.extend({},defautlopts, opts);
	var focusIpt;
	var $target = $(opts.target);


	function _init(){
	 	focusIpt = null;
		$target.attr('keyboard-type', opts.type);
		_bindEvent();
	}

	function _bindEvent (){
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
			}else{
				focusVal += keyVal;
				focusIpt.val(focusVal);
				focusIpt.focus();
			}
		});

		doc.delegate('input[type=text],input[type=password],input[type=search]','focus', function(){
			focusIpt = $(this);
		});

	};
	return _init()
};
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



