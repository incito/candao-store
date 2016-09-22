

var MainPage = {
	init: function(){

		this.setTables();

		this.setSysInfo();

		this.bindEvent();

		//加载虚拟键盘组件
		widget.keyboard();
	},

	bindEvent: function(){
		var that = this;
		var dom = {
			standardTables : $("#standard-tables"),
			orderDialog : $("#order-dialog"),
			openDialog : $("#open-dialog"),//开台权限验证弹窗
		};

		/**
		 * 标准台事件
		 */
		dom.standardTables.on('click','li', function(){
			var cla = $(this).attr("class");
			if(cla == "opened"){
				dom.orderDialog.load("../views/order.jsp");
				dom.orderDialog.modal("show");
			}else{
				dom.openDialog.modal("show");
			}
		});

		dom.openDialog.on('click','.J-btn-submit', function(){
			$.ajax({
				url: _config.interfaceUrl.VerifyUser,
				method: 'POST',
				contentType: "application/json",
				data: JSON.stringify({
					loginType: '030101',
					username: $.trim(dom.openDialog.find('.serverName').val())
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
								dom.openDialog.modal('hide');
								alertModal.close();

								dom.orderDialog.load("../views/order.jsp");
								dom.orderDialog.modal("show");
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
		});

		//退出系统
		$(".exit-sys").click(function(){
			window.location = "../views/login.jsp";
		});

		//
		$(".menu-tab ul li").click(function(){
			nowPage = 0;
			var olddiv = $(".menu-tab ul li.active").attr("loaddiv");
			$(olddiv).addClass("hide");
			$(".menu-tab ul li").removeClass("active");
			$(this).addClass("active");
			var loaddiv = $(this).attr("loaddiv");
			$(loaddiv).removeClass("hide");
			doPage(nowPage);
		});

		/*分区分类向左向右按钮*/
		var roomtype_prev = 0;
		$(".rooms-type .nav-types li").click(function() {
			var me = $(this);
			me.find('li').removeClass("active");
			me.addClass("active");
		});
		$(".rooms-type .nav-type-next").click(function(){
			var count = $(".rooms-type .nav-types").find( "li").length;
			if (roomtype_prev < count - 10) {
				$(".rooms-type .nav-types").find("li").eq(roomtype_prev).css("margin-left", "-10%");
				$(".rooms-type .nav-types").find("li").eq(roomtype_prev+1).click();
				roomtype_prev++;
			}
		});
		$(".rooms-type .nav-type-prev").click(function(){
			if(roomtype_prev>=1){
				$(".rooms-type .nav-types").find("li").eq(roomtype_prev-1).css("margin-left","0");
				$(".rooms-type .nav-types").find("li").eq(roomtype_prev-1).click();
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
				$("#sys-dialog").load("../views/reporting/reporting.jsp");
				$("#sys-dialog").modal("show");
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

		//餐桌数量筛选
		$(".table-nums>div").click(function(){
			$(".table-nums>div").removeClass("active");
			$(this).addClass("active");
		});
	},

	setTables: function(){
		$.ajax({
			url: _config.interfaceUrl.GetTableInfoByTableType,
			method: 'POST',
			contentType: "application/json",
			data: JSON.stringify({
				tableType: [1,0,5]
			}),
			dataType:'json',
			success: function(res){
				var ret = [];
				var isOpend = false;
				$.each(res, function(key,val){
					isOpend = val.status === '0' ? false : true;
					if(isOpend) {
						ret.push('<li class="opened" areaid="' + val.areaid + '">'+ val.tableNo +
						'<div class="tb-info tb-status">' + val.fixprice + '</div>' +
						'<div class="tb-info meal-time">' + val.begintime + '</div> ' +
						'<div class="tb-info tb-person">' + val.personNum + '</div>' +
						' </li>')
					} else {
						ret.push('<li areaid="' + val.areaid + '">'+ val.tableNo +
						'<div class="tb-info tb-person">' + val.personNum + '</div>' +
						' </li>')
					}

				});
				$("#standard-tables").html(ret.join(''));

			}
		})
	},

	setSysInfo: function(){
		$('.J-sys-info')
			.find('.branch-num').text(utils.storage.getter('branch_branchcode'))
			.end().find('.user-info').text(utils.storage.getter('fullname') + ' ' + utils.storage.getter('aUserid'));

		setInterval(function(){
			$('.J-sys-info').find('.time').text(utils.date.current());
		},1000)
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