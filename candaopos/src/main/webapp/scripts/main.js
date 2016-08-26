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