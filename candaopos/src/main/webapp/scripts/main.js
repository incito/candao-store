var pay_nowPage = 0;//挂账单位
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

/**
 * 选择挂账单位
 */
function selPayCompany(){
	pay_nowPage = 0;
	payPage(0);
	$("#select-paycompany-dialog").modal("show");
	$(".paycompany-content ul li").unbind("click").on("click", function(){
		$(".paycompany-content ul li").removeClass("active");
		$(this).addClass("active");
	});
	
	//已点菜上一页
	$("#select-paycompany-dialog .prev-btn").unbind("click").on("click", function(){
		if ($(this).hasClass("disabled")) {
			return false;
		}
		payPage(pay_nowPage - 1);
	});
	//已点菜下一页
	$("#select-paycompany-dialog .next-btn").unbind("click").on("click", function(){
		if ($(this).hasClass("disabled")) {
			return false;
		}
		payPage(pay_nowPage + 1);
	});
}
//挂账单位选择页面分页
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