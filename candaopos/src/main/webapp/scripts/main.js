var MainPage = {
	init: function(){

		this.bindEvent();

		this.setSysInfo();
	},

	bindEvent: function(){

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