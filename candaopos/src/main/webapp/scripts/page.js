var obj;
var listNum = 14;//ÿҳ��ʾ����
var currPage = 0;//��ǰҳ
var pagesLen = 1;//��ҳ��
var pageNum = 0;
var totleNums;//������
loadPage = function(options){
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
	
	$obj = $(settings.obj);
	listNum = settings.listNum;
	currPage = settings.currPage;
	totleNums = settings.totleNums;
	pagesLen = Math.ceil(totleNums/listNum);
	//���ݱ任  
	for (var i=0;i<totleNums;i++){  
		$($obj[i]).addClass("hide");
	}  
	for (var i=currPage*listNum;i<(currPage+1)*listNum;i++){ 
		if($obj[i])
			$($obj[i]).removeClass("hide");
	}
	pageNum = pagesLen==0?0:(currPage+1);
	$(settings.curPageObj).text(pageNum);
	$(settings.pagesLenObj).text(pagesLen);
	if(pageNum == 1 || pageNum == 0){
		$(settings.prevBtnObj).addClass("disabled");
	}else{
		$(settings.prevBtnObj).removeClass("disabled");
	}
	if(pageNum < pagesLen){
		$(settings.nextBtnObj).removeClass("disabled");
	}else{
		$(settings.nextBtnObj).addClass("disabled");
	}
	if(settings.callback)
		settings.callback();
	return currPage;
};