

var MainPage = {

	CurrentTalbeType: 'all',
	CurrentArea: '-1',//默认为全部

	init: function(){

		this.setTables();

		SetBotoomIfon.init();

		this.bindEvent();

		//加载虚拟键盘组件
		widget.keyboard();
	},

	bindEvent: function(){

		var that = this;
		var dom = {
			standardTables : $("#standard-tables"),
			orderDialog : $("#order-dialog"),
			openDialog : $("#open-dialog"),//开台权限验证弹窗,
			roomTypeNav: $(".rooms-type"),//餐台分类导航
		};
		/**
		 * 标准台事件
		 */
		dom.standardTables.on('click','li', function(){
			var me = $(this);
			var cla = me.attr("class");
			var data = {
				orderid: me.attr('orderid'),
				personnum: me.attr('personnum'),
				tableno: me.attr('tableno')
			};
			dom.orderDialog.load("../views/order.jsp",data,function(){
				dom.orderDialog.modal('show');
				if(cla !== "opened"){
					$("#open-dialog").modal("show");
				}
			});

		});

		//退出系统
		$(".exit-sys").click(function(){
			window.location = "../views/login.jsp";
		});

		//标准台和咖啡台切换
		$(".menu-tab ul li").click(function(){
			var olddiv = $(".menu-tab ul li.active").attr("loaddiv");
			$(olddiv).addClass("hide");
			$(".menu-tab ul li").removeClass("active");
			$(this).addClass("active");
			var loaddiv = $(this).attr("loaddiv");
			$(loaddiv).removeClass("hide");
		});

		/*餐台分类事件*/
		var roomtype_prev = 0;
		var navRoomTypes = $("#nav-room-types");

		dom.roomTypeNav.delegate('li', 'click', function() {
			var me = $(this);
			me.siblings().removeClass("active").end().addClass('active');
			me.addClass("active");
			that.CurrentArea = me.attr('areaid');
			that.setTables(that.CurrentTalbeType,me.attr('areaid'));
		});

		dom.roomTypeNav.delegate('.nav-type-next', 'click', function() {
			var count = navRoomTypes.find( "li").length;
			if (roomtype_prev < count - 10) {
				navRoomTypes.find("li").eq(roomtype_prev).css("margin-left", "-10%");
				navRoomTypes.find("li").eq(roomtype_prev+1).click();
				roomtype_prev++;
			}
		});

		dom.roomTypeNav.delegate('.nav-type-prev', 'click', function() {
			if(roomtype_prev>=1){
				navRoomTypes.find("li").eq(roomtype_prev-1).css("margin-left","0");
				navRoomTypes.find("li").eq(roomtype_prev-1).click();
				roomtype_prev--;
			}
		});

		//底部菜单事件绑定
		$(".foot-menu li").click(function(e){
			var me = $(this);
			if(me.hasClass("J-btn-takeout")){
				$("#J-takeout-dialog").modal("show");
				$(".take-out-list li").unbind("click").on("click",  function(){
					$(".take-out-list li").removeClass("active");
					$(this).addClass("active");
				});
			}
			if(me.hasClass("member-btns")){
				//会员
				$(".m-member.popover").toggle();
				e.stopPropagation();
			}
			if(me.hasClass('J-btn-sys')) {
				$("#sys-dialog").load("../views/sys.jsp");
				$("#sys-dialog").modal("show");
			}
			if(me.hasClass('J-btn-rep')) {
				window.location.href="../views/reporting/reporting.jsp";
			}
			if(me.hasClass('J-btn-check')) {
				window.location.href="../views/check/check.jsp";

			}
			if(me.hasClass('J-btn-checkout')) {
				var str ='<strong>确定要结业吗？</strong>';

				var alertModal = widget.modal.alert({
					cls: 'fade in',
					content:str,
					width:400,
					height:500,
					title: "结业提醒",
					btnOkTxt: '确定',
					btnOkCb: function(){
						$(".modal-alert").modal("hide");
						$("#J-btn-checkout-dialog").load("../views/check/impower.jsp",{"title" : "结业授权"});
						$("#J-btn-checkout-dialog").modal("show");
					},
					btnCancelCb: function(){
					}
				})

			}
			if(me.hasClass('J-btn-clear')) {
				var str ='<strong>请选择倒班或结业：</strong>'+
					'<div id="cleardata" class="form-group form-group-base" style="margin-top: 20px">'+
					'<button id="clearAll" class="btn-default btn-lg btn-base btn-base-flex2 clearAll" style="margin-right: 5px">倒班</button>'+
					'<button id="completion" class="btn-default btn-lg btn-base btn-base-flex2 clearCompletion" >结业</button>'+
					'</div>'+
					'<div class="glyphicon glyphicon-info-sign" style="color: #8c8c8c;">还有未结账的餐台不能结业</div>'

				var alertModal = widget.modal.alert({
					cls: 'fade in',
					content:str,
					width:400,
					height:500,
					title: "清机提醒",
					hasBtns:false,
				});
				$("#cleardata button").click(function () {
					var _this = $(this);
					if(_this.hasClass("clearAll")){
						$(".modal-alert").modal("hide");
						$("#J-btn-clear-dialog").load("../views/check/impower.jsp",{"title" : "清机授权","clearType":"倒班"});
						$("#J-btn-clear-dialog").modal("show");
					}
					if(_this.hasClass("clearCompletion")){
						$(".modal-alert").modal("hide");
						$("#J-btn-clear-dialog").load("../views/check/impower.jsp",{"title" : "清机授权","clearType":"结业"});
						$("#J-btn-clear-dialog").modal("show");
					}

				});

			}

			if(me.hasClass('J-btn-register')) {
				$("#register-dialog").load("../views/member/register.jsp");
				$("#register-dialog").modal("show");
			}

			if(me.hasClass('J-btn-storge')) {
				window.location.href = '../views/member/storge.jsp';
			}

			if(me.hasClass('J-btn-memberView')) {
				window.location.href = './member/view.jsp';
			}

		});

		$(document).click(function(e){
			$(".m-member.popover").hide();
			e.stopPropagation();
		});

		//设置餐桌统计
		$(".J-table-nums>div").click(function(){
			var me = $(this);
			me.siblings().removeClass("active").end().addClass('active');
			if(me.hasClass('all')) {
				that.setTables('all', that.CurrentArea);
				that.CurrentTalbeType = 'all';
			} else if(me.hasClass('opened')) {
				that.setTables('opened', that.CurrentArea);
				that.CurrentTalbeType = 'opened';
			} else {
				that.setTables('free', that.CurrentArea);
				that.CurrentTalbeType = 'free';
			}
		});
	},

	/**
	 * 设置餐桌
	 * @param type [opened, free, all]
	 * @param areaid
	 */
	setTables: function(type,areaid){

		var type = type || this.CurrentTalbeType;
		var areaid = areaid || this.CurrentArea;

		function _getTablesArr(res){

			var isOpend = false;
			var tablesAll = [];
			var tablesFree = [];
			var tablesOpened = [];

			$.each(res, function(key,val){
				var tmp = '';
				isOpend = val.status === '0' ? false : true;

				if(areaid === val.areaid || areaid === '-1') {
					if(isOpend) {
						tmp = '<li class="opened" orderid="'+ val.orderid  + '" personNum="'+ val.personNum  + '" tableno="' + val.tableNo + '" areaid="' + val.areaid + '">'+ val.tableNo +
							'<div class="tb-info tb-status">' + val.fixprice + '</div>' +
							'<div class="tb-info meal-time">' + val.begintime + '</div> ' +
							'<div class="tb-info tb-person">' + val.personNum + '</div>' +
							' </li>';

						tablesOpened.push(tmp);
					} else {
						tmp = '<li orderid="'+ val.orderid  + '" personNum="'+ val.personNum  + '" tableno="' + val.tableNo + '" areaid="' + val.areaid + '">'+ val.tableNo +
							'<div class="tb-info tb-person">' + val.personNum + '</div>' +
							' </li>'
						tablesFree.push(tmp);
					}
					tablesAll.push(tmp);
				}
			});

			return {
				all : tablesAll,
				free : tablesFree,
				opened : tablesOpened
			}
		}

		$.ajax({
			url: _config.interfaceUrl.GetTableInfoByTableType,
			method: 'POST',
			contentType: "application/json",
			data: JSON.stringify({
				tableType: [1,0,5]
			}),
			dataType:'json',
			success: function(res){

				var tables = _getTablesArr(res);
				var navRoomTypesArr = [];
				var navRoomTypes = $('#nav-room-types');

				//设置餐桌统计
				$('.J-table-nums').find('.all .num').text(tables.all.length)
					.end().find('.free .num').text(tables.free.length)
					.end().find('.opened .num').text(tables.opened.length);




				//设置区域
				if(navRoomTypes.attr('inited') !== 'true'){
					navRoomTypesArr.push('<li class="active" areaid="-1">全部</li>')
					$.each(res, function(key, val){
						navRoomTypesArr.push('<li areaid="' + val.areaid  + '">' + val.areaname  + '</li>');
					});
					navRoomTypes.attr('inited', 'true');
					navRoomTypes.html(utils.array.unique(navRoomTypesArr).join(''));
				}

				if(tables[type].length == 0) {
					$('#J-table-pager').html('');
					$("#standard-tables").html('');
				} else {
					//初始化分页
					$('#J-table-pager').pagination({
						dataSource: tables[type],
						pageSize: 40,
						showPageNumbers: false,
						showNavigator: true,
						callback: function(data) {
							$("#standard-tables").html(data.join(''));
							//$("#standard-tables").find('li').eq(0).trigger('click');
						}
					});
				}

			}
		})
	}
};

$(function(){
	MainPage.init();
});

var pay_nowPage = 0;
function paging(currPage) {
	var o = $(".tab-div ul li.active").attr("loaddiv") + " li";
	nowPage = loadPage({
		obj : o,
		listNum : 40,
		currPage : currPage,
		totleNums : $(o).length,
		curPageObj : "#curr-page",
		pagesLenObj : "#pages-len",
		prevBtnObj : ".page .prev-btn",
		nextBtnObj : ".page .next-btn"
	});
}

function page(options) {
	return loadPage(options);
}
function selPayCompany(){
	pay_nowPage = 0;
	payPage(0);
	$("#select-paycompany-dialog").modal("show");
	$(".paycompany-content ul li").unbind("click").on("click", function(){
		$(".paycompany-content ul li").removeClass("active");
		$(this).addClass("active");
	});

	//�ѵ����һҳ
	$("#select-paycompany-dialog .prev-btn").unbind("click").on("click", function(){
		if ($(this).hasClass("disabled")) {
			return false;
		}
		payPage(pay_nowPage - 1);
	});
	//�ѵ����һҳ
	$("#select-paycompany-dialog .next-btn").unbind("click").on("click", function(){
		if ($(this).hasClass("disabled")) {
			return false;
		}
		payPage(pay_nowPage + 1);
	});
}
function payPage(currPage) {
	pay_nowPage = loadPage({
		obj : ".paycompany-content ul li",
		listNum : 8,
		currPage : currPage,
		totleNums : $(".paycompany-content ul li").length,
		curPageObj : "#select-paycompany-dialog #pay-curr-page",
		pagesLenObj : "#select-paycompany-dialog #pay-pages-len",
		prevBtnObj : "#select-paycompany-dialog .prev-btn",
		nextBtnObj : "#select-paycompany-dialog .next-btn",
		callback : function() {
		}
	});
}