$(document).ready(function(){
	$("img.img-close").hover(function(){
	 	$(this).attr("src", global_Path+"/images/close-active.png");
	},function(){
		$(this).attr("src", global_Path+"/images/close-sm.png");
	});

});
/**
 * 去掉字符串前后空格
 * @param str
 * @returns
 */
function dellrTrim(str){
	return str.replace(/(^\s*)|(\s*$)/g, "");
}
/**
 * 切换查询类型的时候，为searchType赋值
 * @param type
 * @param o
 */
function changeDataType(type, o){
	searchType = type;
	gethiddenId(type, o);
}
function showLoading(){
	$("#prompt-dialog").modal("show");
}
function hideLoading(){
	$("#prompt-dialog").modal("hide");
}
/** *****************营业数据统计 START ******************************************** */
function initBussinessData() {
	showLoading();
	$.post(global_Path + "/daliyReports/getBusinessReport.json", {
		beginTime : beginTime,
		endTime : endTime,
		Datetype : dateType == 0 ? "D" : (dateType == 1 ? "M" : "Y")
	}, function(result) {
		var tbody = "";
		var series_data1 = [];
		var series_data2 = [];
		var series_data3 = [];
		var xAxis_data = [];

		var legend = [];
		var markPoint_data = [];
		if (result != null && result.length > 0) {
			xAxis_data = getDataByType();
			var m = new HashMap();
			$.each(result, function(i, item) {
				m.put(item.statistictime, item);
				tbody += '<tr><td>' + item.statistictime + '</td><td>'
						+ item.shouldamount + '</td><td>' + item.paidinamount
						+ '</td><td>' + item.discountamount + '</td><td>'+item.personPercent+'</td><td>'+item.tablenum+'</td></tr>';
			});
			for (var j = 0; j < xAxis_data.length; j++) {
				var xValue = xAxis_data[j];
				if (m.containsKey(xValue)) {
					var obj = m.get(xValue);
					series_data1.push(parseFloat(strToFloat(obj.shouldamount).toFixed(2)));
					series_data2.push(parseFloat(strToFloat(obj.paidinamount).toFixed(2)));
					series_data3.push(parseFloat(strToFloat(obj.discountamount).toFixed(2)));
				} else {
					series_data1.push(0);
					series_data2.push(0);
					series_data3.push(0);
				}
			}
			legend = [ "应收总额", "实收总额", "折扣总额" ];
			markPoint_data = [ {
				type : 'max',
				name : '最大值'
			}/*, {
				type : 'min',
				name : '最小值'
			} */];

		}
		$("#tb_d tbody").html(tbody);
		chartData(xAxis_data, legend, series_data1, series_data2, series_data3,
				markPoint_data);
		hideLoading();
	});
}
/**
 * 营业数据统计 柱形图
 *
 * @param xAxis_data
 * @param legend_datav
 * @param series_data1
 * @param series_data2
 * @param series_data3
 * @param markPoint_data
 */
function chartData(xAxis_data, legend_datav, series_data1, series_data2,
		series_data3, markPoint_data) {
	var domMain = document.getElementById('main');
	var option = {
		title : {
			text : '营业数据统计表',
		},
		tooltip : {
			trigger : 'axis'
		},
		legend : {
			data : legend_datav
		},
		calculable : true,

		xAxis : [ {
			type : 'category',
			data : xAxis_data
		} ],
		yAxis : [ {
			type : 'value'
		} ],
		series : [ {
			name : legend_datav[0],
			type : 'bar',
			data : series_data1,
			markPoint : {
				data : markPoint_data
			}
		}, {
			name : legend_datav[1],
			type : 'bar',
			data : series_data2,
			markPoint : {
				data : markPoint_data
			}
		}, {
			name : legend_datav[2],
			type : 'bar',
			data : series_data3,
			markPoint : {
				data : markPoint_data
			}
		} ]
	};
	launchExample(domMain, option);
}

/**
 * 营业数据分析表 导出
 */
function exportReportAnalysisDaliy() {
	if(compareBeginEndTime()){
		if (beginTime == null || "" == beginTime) {
			var d = new Date();
			var month = d.getMonth() + 1;
			if (d.getMonth() + 1 < 10) {
				month = "0" + month;
			}
			var day = d.getDate();
			if (d.getDate() < 10) {
				day = "0" + day;
			}
			beginTime = d.getFullYear() + '-' + month + '-' + day
					+ ' 00:00:00';
		}

		if (endTime == null || "" == endTime) {
			var d = new Date();
			var month = d.getMonth() + 1;
			if (d.getMonth() + 1 < 10) {
				month = "0" + month;
			}
			var day = d.getDate();
			if (d.getDate() < 10) {
				day = "0" + day;
			}
			var day = d.getDate();
			if (d.getDate() < 10) {
				day = "0" + day;
			}
			var hours = d.getHours();
			if (d.getHours() < 10) {
				hours = "0" + hours;
			}
			var minutes = d.getMinutes();
			if (d.getMinutes() < 10) {
				minutes = "0" + minutes;
			}

			var second = d.getSeconds();
			if (d.getSeconds() < 10) {
				second = "0" + second;
			}

			endTime = d.getFullYear() + '-' + month + '-' + day + ' '
					+ hours + ":" + minutes + ":" + second;
		}
		$("#_beginTime").val(beginTime);
		$("#_endTime").val(endTime);
		$("#_Datetype").val(dateType == 0 ? "D" : (dateType == 1 ? "M" : "Y"));
		$("#daliyReportsForm").attr("action", global_Path +"/daliyReports/exportBusinessReport.json");
		$("#daliyReportsForm").submit();
	}
}
/** *****************营业数据统计 END ******************************************** */
/** ******************品项销售统计 START **************************************** */
/**
 * 品项类别点击切换
 */
function scrollClick(){
	$(".item-type").hover(function(){
		var _this =$(this).find(".nav-dish-type");
		if(_this.children().length>10){
			_this.prev().css("visibility","visible");
			_this.next().css("visibility","visible");
		}
	},function(){
		var _this =$(this).find(".nav-dish-type");
		_this.prev().css("visibility","hidden");
		_this.next().css("visibility","hidden");
	});

	$("#nav-types-prev1").click(function(event){

		if(up_num1>=1){
			$(this).next(".nav-dish-type").find("li").eq(up_num1-1).css("margin-left","0");
			up_num1--;
		}
	});
	$("#nav-types-next1").click(function(){
		var count = $(this).prev(".nav-dish-type").find("li").length;
		if(up_num1<count-10){
			$(this).prev(".nav-dish-type").find("li").eq(up_num1).css("margin-left","-10%");
			up_num1++;
			console.info(up_num1);
		}
	});
	$("#nav-types-prev2").click(function(event){
		if(up_num2>=1){
			$(this).next(".nav-dish-type").find("li").eq(up_num2-1).css("margin-left","0");
			up_num2--;
		}
	});
	$("#nav-types-next2").click(function(){
		var count = $(this).prev(".nav-dish-type").find("li").length;
		if(up_num2<count-10){
			$(this).prev(".nav-dish-type").find("li").eq(up_num2).css("margin-left","-10%");
			up_num2++;
		}
	});
	$("#nav-types-prev3").click(function(event){
		if(up_num3>=1){
			$(this).next(".nav-dish-type").find("li").eq(up_num3-1).css("margin-left","0");
			up_num3--;
		}
	});
	$("#nav-types-next3").click(function(){
		var count = $(this).prev(".nav-dish-type").find("li").length;
		if(up_num3<count-10){
			$(this).prev(".nav-dish-type").find("li").eq(up_num3).css("margin-left","-10%");
			up_num3++;
		}
	});
	/*鼠标滚动*/
     var user_agent = navigator.userAgent;
	 var dom1 =$("#dish-type-first")[0];
     if(user_agent.indexOf("Firefox")!=-1){// Firefox
    	 dom1.addEventListener("DOMMouseScroll",addEvent_1,!1);
     } else if(user_agent.indexOf("MSIE")!=-1){// Firefox
         dom1.attachEvent("onmousewheel",addEvent_1,!1);
     }else{
    	 dom1.addEventListener("mousewheel",addEvent_1,!1);
     }

     var dom2 =$("#dish-type-sec")[0];
     if(user_agent.indexOf("Firefox")!=-1){// Firefox
    	 dom2.addEventListener("DOMMouseScroll",addEvent_2,!1);
     } else if(user_agent.indexOf("MSIE")!=-1){// Firefox
    	 dom2.attachEvent("onmousewheel",addEvent_2,!1);
     }else{
    	 dom2.addEventListener("mousewheel",addEvent_2,!1);
     }
     var dom3 =$("#dish-type-third")[0];
     if(user_agent.indexOf("Firefox")!=-1){// Firefox
    	 dom3.addEventListener("DOMMouseScroll",addEvent_3,!1);
     } else if(user_agent.indexOf("MSIE")!=-1){// Firefox
    	 dom3.attachEvent("onmousewheel",addEvent_3,!1);
     }else{
    	 dom3.addEventListener("mousewheel",addEvent_3,!1);
     }
}
function addEvent_1(event) {
	event = event || window.event;
	var type = event.type;
	if (type == 'DOMMouseScroll' || type == 'mousewheel') {
		event.delta = (event.wheelDelta) ? event.wheelDelta / 120
				: -(event.detail || 0) / 3;
	}
	/*菜品分类*/
	var count = $("#dish-type-first").children("li").length;
	if (event.delta > 0) {
		if (count - up_num1 > 10) {
			$("#dish-type-first").find("li").eq(up_num1).css("margin-left", "-10%");
			up_num1++;
		}
	} else {
		if (up_num1 >= 1) {
			$("#dish-type-first").find("li").eq(up_num1 - 1).css("margin-left", "0");
			up_num1--;
		}
	}
	if (document.all) {
		event.cancelBubble = false;
		return false;
	} else {
		event.preventDefault();
	}
}
function addEvent_2(event) {
	event = event || window.event;
	var type = event.type;
	if (type == 'DOMMouseScroll' || type == 'mousewheel') {
		event.delta = (event.wheelDelta) ? event.wheelDelta / 120
				: -(event.detail || 0) / 3;
	}
	/*菜品分类*/
	var count = $("#dish-type-sec").children("li").length;
	if (event.delta > 0) {
		if (count - up_num2 > 10) {
			$("#dish-type-sec").find("li").eq(up_num2).css("margin-left", "-10%");
			up_num2++;
		}
	} else {
		if (up_num2 >= 1) {
			$("#dish-type-sec").find("li").eq(up_num2 - 1).css("margin-left", "0");
			up_num2--;
		}
	}
	if (document.all) {
		event.cancelBubble = false;
		return false;
	} else {
		event.preventDefault();
	}
}
function addEvent_3(event) {
	event = event || window.event;
	var type = event.type;
	if (type == 'DOMMouseScroll' || type == 'mousewheel') {
		event.delta = (event.wheelDelta) ? event.wheelDelta / 120
				: -(event.detail || 0) / 3;
	}
	/*菜品分类*/
	var count = $("#dish-type-third").children("li").length;
	if (event.delta > 0) {
		if (count - up_num3 > 10) {
			$("#dish-type-third").find("li").eq(up_num3).css("margin-left", "-10%");
			up_num3++;
		}
	} else {
		if (up_num3 >= 1) {
			$("#dish-type-third").find("li").eq(up_num3 - 1).css("margin-left", "0");
			up_num3--;
		}
	}
	if (document.all) {
		event.cancelBubble = false;
		return false;
	} else {
		event.preventDefault();
	}
}
/**
 * 获取品项类型
 */
function getItemsType() {
	$.get(global_Path + "/itemDetail/getItemTypeListForPx.json", function(result) {
		var li1 = '';
		var li2 = '';
		var li3 = '';
		$.each( result, function(i, item) {
			var cla = "";
			if (selItemType1 == null
					|| selItemType1 == "") {
				if (i == 0) {
					cla = "active";
					selItemType1 = item.codeId;
				}
			} else {
				if (item.codeId == selItemType1) {
					cla = "active";
				}
			}
			li1 += '<li class="'+cla+'" value="'+item.codeId+'">'
					+ item.codeDesc
					+ '</li>';
			$("#dish-type-first").html(
					li1);
			var cla2 = "";
			if (selItemType2 == null
					|| selItemType2 == "") {
				if (i == 0) {
					cla2 = "active";
					selItemType2 = item.codeId;
				}
			} else {
				if (item.codeId == selItemType2) {
					cla2 = "active";
				}
			}
			li2 += '<li class="'+ cla2 +'" value="'+item.codeId+'">'
					+ item.codeDesc
					+ '</li>';
			$("#dish-type-sec").html(
					li2);
			var cla3 = "";
			if (selItemType3 == null
					|| selItemType3 == "") {
				if (i == 0) {
					cla3 = "active";
					selItemType3 = item.codeId;
				}
			} else {
				if (item.codeId == selItemType3) {
					cla3 = "active";
				}
			}
			li3 += '<li class="'+cla3+'" value="'+item.codeId+'">'
					+ item.codeDesc
					+ '</li>';
		});

		$("#dish-type-third").html(li3);

		for(var i=0; i<up_num1; i++){
			$("#dish-type-first").find("li").eq(i).css("margin-left", "-10%");
		}
		for(var i=0; i<up_num2; i++){
			$("#dish-type-sec").find("li").eq(i).css("margin-left", "-10%");
		}
		for(var i=0; i<up_num3; i++){
			$("#dish-type-third").find("li").eq(i).css("margin-left", "-10%");
		}

		$("ul#dish-type-first li").click(function() {
			$("ul#dish-type-first li").removeClass("active");
			$(this).addClass("active");
			selItemType1 = $(this).attr("value");
			itemCount();
		});
		$("ul#dish-type-sec li").click(
				function() {
					$("ul#dish-type-sec li")
							.removeClass("active");
					$(this).addClass("active");
					selItemType2 = $(this)
							.attr("value");
					itemAmount();
				});
		$("ul#dish-type-third li").click(
				function() {
					$("ul#dish-type-third li")
							.removeClass("active");
					$(this).addClass("active");
					selItemType3 = $(this)
							.attr("value");
					itemThousandstimes();
				});
		itemCount();//份数
		itemAmount();//金额
		itemThousandstimes();//千次
	});
}
/**
 * 品项售卖份数Top10
 */
function itemCount(){
	doitemReportPost(0, function(result){
		// 售卖份数
		var yAxis_data = [];
		var series_data = [];
		if(result.ItemReportList.length>0){
			for(var i=result.ItemReportList.length-1; i>=0 ;i--){
				var item = result.ItemReportList[i];
				yAxis_data.push(item.itemdesc);
				series_data.push(item.num);
			}
		}
		saleCountTop(yAxis_data, series_data);
		// 售卖份数趋势
		saleCountTrend(result.ItemNumQushiReport);
	});
}
/**
 * 品项售卖金额Top10
 */
function itemAmount(){
	doitemReportPost(1, function(result){
		// 售卖金额
		var amount_yAxis_data = [];
		var amount_series_data = [];
		if(result.ItemSharezhuzhuangReport.length>0){
			for(var i=result.ItemSharezhuzhuangReport.length-1; i>=0 ;i--){
				var item = result.ItemSharezhuzhuangReport[i];
				amount_yAxis_data.push(item.itemdesc);
				amount_series_data.push(strToFloat(item.share));
			}
		}
		saleAmountTop(amount_yAxis_data, amount_series_data);
		// 售卖金额趋势
		saleAmountTrend(result.ItemShareQushiReport);
	});
}
/**
 * 千次
 */
function itemThousandstimes(){
	$.post(global_Path+"/itemAnalysisCharts/getColumnItemThousandsTimesReportForView.json", {
		beginTime : beginTime,
		endTime : endTime,
		Datetype : dateType == 0 ? "D" : (dateType == 1 ? "M" : "Y"),
		columnid: selItemType3
	}, function(result){
		thousandstimes(result);
	},'json');
}
/**
 * 发送请求
 */
function doitemReportPost(type, callback){
	showLoading();
	$.post(global_Path+"/itemAnalysisCharts/getColumnItemReport.json", {
		beginTime : beginTime,
		endTime : endTime,
		Datetype : dateType == 0 ? "D" : (dateType == 1 ? "M" : "Y"),
		type: type,//0:份数；1：金额
		columnid: type ==0 ? selItemType1 : selItemType2
	}, function(result){
		callback(result);
		hideLoading();
	}, 'json');
}
/**
 * 销售份数top10
 */
function saleCountTop(yAxis_data, series_data) {
	var domMain = document.getElementById('sale_count_top_main');
	barChart("售卖份数TOP10", domMain, yAxis_data, series_data);
}
/**
 * 售卖金额Top10
 */
function saleAmountTop(yAxis_data, series_data) {
	var domMain = document.getElementById('saleamount_top_main');
	barChart("售卖金额TOP10", domMain, yAxis_data, series_data);
}
/**
 * 销售份数趋势
 */
function saleCountTrend(arr) {
	var domMain = document.getElementById('sale_count_main');
	var o = getTrendDataObj(arr, 'num');
	lineChart("售卖份数趋势", domMain, o.legend_data, o.xAxis_data, o.series);
}
/**
 * 销售金额趋势
 */
function saleAmountTrend(arr) {
	var domMain = document.getElementById('saleamount_trend_main');
	var o = getTrendDataObj(arr, 'share');
	lineChart("售卖金额趋势", domMain, o.legend_data, o.xAxis_data, o.series);
}
/**
 * 千次
 */
function thousandstimes(arr) {
	var domMain = document.getElementById('thousand_times_main');
	var o = getThousandstimesObj(arr);
	lineChart("千次", domMain, o.legend_data, o.xAxis_data, o.series);
}
/**
 * 千次组数据
 * @param arr
 * @param type
 * @returns {___anonymous8291_8372}
 */
function getThousandstimesObj(arr) {
	var xAxis_data = [];
	var series = [];
	if (arr != null && arr.length > 0) {
		xAxis_data = getDataByType();
		var obj = {
				name : '千次',
				type : 'line'
			};
		obj.data = getData(arr, xAxis_data, "times");
		series.push(obj);
	} else {
		var obj = {
			name : "",
			type : 'line',
			data : []
		};
		series.push(obj);
	}
	return {
		legend_data : ["千次"],
		xAxis_data : xAxis_data,
		series : series
	};
}
/**
 * 组数据
 */
function getTrendDataObj(arr, type) {
	var legend_data = [];
	var xAxis_data = [];
	var series = [];
	if (arr != null && arr.length > 0) {
		xAxis_data = getDataByType();
		$.each(arr, function(i, item) {
			//			var dishname = item.name + "("+item.list[0].dishunit+")";
			var dishname = item.name;//暂不显示单位
			legend_data.push(dishname);
			var obj = {
				name : dishname,
				type : 'line'
			};
			obj.data = getData(item.list, xAxis_data, type);
			series.push(obj);
		});
	} else {
		var obj = {
			name : "",
			type : 'line',
			data : []
		};
		series.push(obj);
	}
	return {
		legend_data : legend_data,
		xAxis_data : xAxis_data,
		series : series
	};
}
function getData(list, xAxis_data, type) {
	var data = [];
	var m = new HashMap();
	$.each(list, function(j, o) {
		if(type == "times"){
			m.put(o.time, o);
		}else{
			m.put(o.statistictime, o);
		}
	});
	for (var j = 0; j < xAxis_data.length; j++) {
		var xValue = xAxis_data[j];
		if (m.containsKey(xValue)) {
			var o = m.get(xValue);
			if (type == "num")
				data.push(o.num);
			else if (type == "share")
				data.push(strToFloat(o.share));
			else if(type == "times")
				data.push(strToFloat(o.thousandstimes));
		} else {
			data.push(0);
		}
	}
	return data;
}
/** ******************品项销售统计 END **************************************** */

/** ***************common**************** */
/**
 * 根据选择日、月、年 获取时间轴
 */
function getDataByType() {
	var xAxis_data = [];
	var begin = beginTime;
	var end = endTime;
	var num = dateDiff(begin, end, dateType);
	var date1 = getNewDate(begin, dateType);
	for (var j = 0; j <= num; j++) {
		var d = getNewDate(begin, dateType);
		var value = "";
		if (dateType == 0) {
			// 日
			var dd = date1.getDate();
			d.setDate(dd + j);
			var month = d.getMonth() + 1;
			if (month < 10) {
				month = "0" + month;
			}
			var day = d.getDate();
			if (day < 10) {
				day = "0" + day;
			}
			value = d.getFullYear() + "-" + month + "-" + day;
		} else if (dateType == 1) {
			// 月
			if(j == 0){
				j = 1;
			}
			var month = date1.getMonth();
			d.setMonth(month + j, 0);
			month = d.getMonth() +1;
			if (month < 10) {
				month = "0" + month;
			}
			value = d.getFullYear() + "-" + month;
		} else {
			// 年
			if(j == 0){
				j = 1;
			}
			d.setFullYear(date1.getFullYear() + j);
			value = d.getFullYear();
		}
		xAxis_data.push(value);
	}
	return xAxis_data;
}
/**
 * 通过字符串获取日期
 * @param dateStr
 * @returns {Date}
 */
function getNewDate(dateStr, dateType){
	var year = "";
	var mon = "";
	var day = "";
	if (dateType == 0) {//日
		var arr = dateStr.split("-");
		year = arr[0];
		mon = arr[1]-1;
		day = arr[2];
	}else if (dateType == 1) {//月
		var arr = dateStr.split("-");
		year = arr[0];
		mon = arr[1];
	}else if (dateType == 2) {//年
		year = dateStr;
	}
	return new Date(year, mon, day," "," "," ");
}
/**
 * 查看明细
 *
 * @param target
 */
function toDetail(target) {
	var title = "";
	var submenu = "";
	if (target == 0) {
		title = "营业数据明细表";
		submenu = ".sub_bussi_menu";
		toSaleRept(beginTime ,endTime, dateType);
	} else if (target == 1) {
		title = "品项销售明细表";
		submenu = ".sub_item_menu";
		toDishSaleRept(beginTime ,endTime, dateType);
	} else if (target == 2) {
		title = "优惠活动明细表";
		submenu = ".sub_coup_menu";
		toCouponsRept(beginTime ,endTime, dateType);
	}
	$(window.parent.document).find("#title_p").text(title);
	$(window.parent.document).find('.ky-menu-success').removeClass("ky-menu-active");
	$(window.parent.document).find(submenu).addClass("ky-menu-active");
}
/**
 * 判断开始时间与结束时间
 * @returns {Boolean}
 */
function compareBeginEndTime(){
	beginT = $("#beginTime").val();
	endT = $("#endTime").val();
	if(beginT == null || beginT == ""){
		alert("开始时间不能为空！");
		$("#beginTime").val(getNowDate());
		$("#beginTime").focus();
		return false;
	}
	if(endT == null || endT == ""){
		alert("结束时间不能为空！");
		$("#endTime").val(getNowDate1());
		$("#endTime").focus();
		return false;
	}
	if(beginT > endT){
		alert("结束时间不能小于开始时间！");
		$("#endTime").val(getNowDate1());
		$("#endTime").focus();
		return false;
	}
	return true;
}
/**
 * 将字符串转换为浮点型数值
 * @param str
 * @returns
 */
function strToFloat(str){
	if(str == null || str == ""){
		return 0;
	}else{
		return parseFloat(str);
	}
}
/** ***************common END**************** */

/** *******************营业数据明细报表 START***************************************** */
function initDaliyData() {
	if(compareBeginEndTime()){
		$.post(global_Path + "/daliyReports/getDayReportList.json", {
			beginTime : beginTime,
			endTime : endTime,
			shiftid : shiftid
		}, function(result) {
			incomeStatistics(result);
			paidInAmount(result);
			discountAmount(result);
			businessDataTb(result);
			meberDataTb(result);
			billDataTb(result);
		});
	}
}
/**
 * 账单信息统计表
 * @param result
 */
function billDataTb(result){
	var tb = "";
	if (result != null && result.length > 0) {
		var item = result[0];
		tb += '<tr><td>'
			+ item.closedordermums+'</td><td>'
			+ item.closedordershouldamount+'</td><td>'
			+ item.closedorderpersonnums + '</td><td>'
			+ item.nobillnums + '</td><td>'
			+ item.nobillshouldamount + '</td><td>'
			+ item.nopersonnums + '</td><td>'
			+ item.billnums + '</td><td>'
			+ item.billshouldamount + '</td><td>'
			+ item.personnums + '</td><td>'
			+ item.zaitaishu + '</td><td>'
			+ item.kaitaishu + '</td></tr>';
	}
	$("#bill_data_tb tbody").html(tb);
}
/**
 * 营业数据统计
 * @param result
 */
function businessDataTb(result) {
	var tb = "";
	var takeoutTb = "";
	if (result != null && result.length > 0) {
		var item = result[0];
		tb = '<tr><td>' + item.tablenum + '</td><td>' + item.tableconsumption
				+ '</td><td>' + item.settlementnum + '</td><td>'
				+ item.shouldaverage + '</td><td>' + item.paidinaverage
				+ '</td><td>' + item.attendance + '</td><td>' + item.overtaiwan
				+ '</td><td>' + item.avgconsumtime + '</td><td>' + item.shouldamountNormal + '</td></tr>';

		takeoutTb = '<tr><td>' + item.shouldamountTakeout
				+ '</td><td>' + item.paidinamountTakeout
				+ '</td><td>' + item.ordercountTakeout
				+ '</td><td>' + item.avgpriceTakeout
				+ '</td></tr>';
	}
	$("#business_data_tb tbody").html(tb);
	$("#takeout_data_tb tbody").html(takeoutTb);
}
/**
 * 会员数据统计
 * @param result
 */
function meberDataTb(result) {
	var tb = "";
	if (result != null && result.length > 0) {
		var item = result[0];
		tb = '<tr><td>'+item.vipordercount+'</td><td>'
			+item.viporderpercent+'</td><td>'
			+ item.meberTicket + '</td><td>'
			+ item.integralconsum + '</td><td>'
			+ item.merbervaluenet + '</td><td>'
			+ item.mebervalueadd + '</td><td>'
			+ item.total + '</td></tr>';
	}
	$("#meber_data_tb tbody").html(tb);
}
/**
 * 营业收入统计
 * @param result
 */
function incomeStatistics(result) {
	var series_data = [];
	var yAxis_data = [];
	var tb = "";
	if (result != null && result.length > 0) {
		var item = result[0];
		var shouldamount = strToFloat(item.shouldamount).toFixed(2);
		var paidinamount = strToFloat(item.paidinamount).toFixed(2);
		var discountamount = strToFloat(item.discountamount).toFixed(2);
		series_data.push(shouldamount);
		series_data.push(paidinamount);
		series_data.push(discountamount);

		yAxis_data = [ '应收总额', '实收总额 ', '折扣总额' ];
		tb = '<tr><td>' + shouldamount + '</td><td>' + paidinamount
				+ '</td><td>' + discountamount + '</td></tr>';
	}
	$("#income_tb tbody").html(tb);

	// 营业收入统计图表
	var domMain = document.getElementById('daliy_yingye_main');
	barChart('营业收入统计栏', domMain, yAxis_data, series_data);
}

/**
 * 实收总额统计
 * @param result
 */
function paidInAmount(result) {
	var legend_data = [];
	var series_data = [];
	var tb = "";
	var tr = "";
	if (result != null && result.length > 0) {
		var item = result[0];
		legend_data = item.settlementDescList;//[ '会员储值消费净值', '刷他行卡', '刷工行卡', '支付宝', '微信', '挂账', '现金' ];
		tr += '<tr id="settlementDesc">';
		$.each(legend_data,function(i,data){
			tr += '<th width="14%">'+data+'</th>';
		});
		tr += '</tr>';
		$("#settlementDesc").replaceWith(tr);
		tb += '<tr>';
		$.each(item.settlements,function(i,data){
			series_data.push({
				value : data,
				name : legend_data[i]
			});
			tb += '<td>'+data+'</td>';
		});
		tb += '</tr>';
	}
	$("#paidIn_tb tbody").html(tb);

	// 实收总额统计图表
	var domMain = document.getElementById('daliy_shishou_main');
	_pieChart('实收总额统计', domMain, legend_data, series_data, 0);
}
/*function paidInAmount(result) {
	var legend_data = [];
	var series_data = [];
	var tb = "";
	if (result != null && result.length > 0) {
		var item = result[0];
		legend_data = [ '会员储值消费净值', '刷他行卡', '刷工行卡', '支付宝', '微信', '挂账', '现金' ];
		series_data.push({
			value : strToFloat(item.merbervaluenet),
			name : legend_data[0]
		});
		series_data.push({
			value : strToFloat(item.otherbank),
			name : legend_data[1]
		});
		series_data.push({
			value : strToFloat(item.icbc),
			name : legend_data[2]
		});
		series_data.push({
			value : strToFloat(item.zhifubao),
			name : legend_data[3]
		});
		series_data.push({
			value : strToFloat(item.weixin),
			name : legend_data[4]
		});
		series_data.push({
			value : strToFloat(item.card),
			name : legend_data[5]
		});
		series_data.push({
			value : strToFloat(item.money),
			name : legend_data[6]
		});
		tb = '<tr><td>' + item.money + '</td><td>' //现金
			+ item.card + '</td><td>'//挂账
			+ item.weixin+'</td><td>'//微信
			+ item.zhifubao+'</td><td>'//支付宝
			+ item.icbc + '</td><td>'//刷工行卡
			+ item.otherbank + '</td><td>'//刷他行卡
			+ item.merbervaluenet + '</td></tr>';
	}
	$("#paidIn_tb tbody").html(tb);

	// 实收总额统计图表
	var domMain = document.getElementById('daliy_shishou_main');
	_pieChart('实收总额统计', domMain, legend_data, series_data, 0);
}*/
/**
 * 折扣总额
 * @param result
 */
function discountAmount(result) {
	var legend_data = [];
	var series_data = [];
	var tb = "";
	if (result != null && result.length > 0) {
		var item = result[0];
		legend_data = [ '优免', '会员积分消费', '会员券消费', '折扣优惠', '赠送金额',
				'会员储值消费虚增' ];
		series_data.push({
			value : strToFloat(item.bastfree),
			name : legend_data[0]
		});
		series_data.push({
			value : strToFloat(item.integralconsum),
			name : legend_data[1]
		});
		series_data.push({
			value : strToFloat(item.meberTicket),
			name : legend_data[2]
		});
		series_data.push({
			value : strToFloat(item.discountmoney),
			name : legend_data[3]
		});
		series_data.push({
			value : strToFloat(item.give),
			name : legend_data[4]
		});
		series_data.push({
			value : strToFloat(item.mebervalueadd),
			name : legend_data[5]
		});
		if(item.handerWay != null && item.handerWay != ""){
			legend_data.push(item.handerWay);
			series_data.push({
				value : Math.abs(strToFloat(item.handervalue)),
				name : item.handerWay
			});
		}
		tb = '<tr><td>' + item.bastfree + '</td><td>' + item.integralconsum
				+ '</td><td>' + item.meberTicket + '</td><td>'
				+ item.discountmoney + '</td>';
		if(item.handerWay != null && item.handerWay != ""){
			$("#dynamic-col").text(item.handerWay);
			tb += '<td>' + item.handervalue + '</td>';
		}else{
			$("#dynamic-col").css("display", "none");
		}
		tb += '<td>'+strToFloat(item.give)+'</td><td>' + item.mebervalueadd + '</td></tr>';
	}
	$("#discount_tb tbody").html(tb);

	// 折扣总额统计图表
	var domMain = document.getElementById('daliy_zhekou_main');
	_pieChart('折扣总额统计', domMain, legend_data, series_data, 0);
}
/**
 * 营业数据明细报表 导出
 */
function exportReportDaliy() {
	if(compareBeginEndTime()){
		if (beginTime == null || "" == beginTime) {
			var d = new Date();
			var month = d.getMonth() + 1;
			if (d.getMonth() + 1 < 10) {
				month = "0" + month;
			}
			var day = d.getDate();
			if (d.getDate() < 10) {
				day = "0" + day;
			}
			beginTime = d.getFullYear() + '-' + month + '-' + day
					+ ' 00:00:00';
		}

		if (endTime == null || "" == endTime) {
			var d = new Date();
			var month = d.getMonth() + 1;
			if (d.getMonth() + 1 < 10) {
				month = "0" + month;
			}
			var day = d.getDate();
			if (d.getDate() < 10) {
				day = "0" + day;
			}
			var day = d.getDate();
			if (d.getDate() < 10) {
				day = "0" + day;
			}
			var hours = d.getHours();
			if (d.getHours() < 10) {
				hours = "0" + hours;
			}
			var minutes = d.getMinutes();
			if (d.getMinutes() < 10) {
				minutes = "0" + minutes;
			}

			var second = d.getSeconds();
			if (d.getSeconds() < 10) {
				second = "0" + second;
			}

			endTime = d.getFullYear() + '-' + month + '-' + day + ' '
					+ hours + ":" + minutes + ":" + second;
		}
		if(shiftid == ""){
			shiftid = null;
		}
		location.href = global_Path + "/daliyReports/exprotReport/"
							+ beginTime + "/" + endTime + "/" + shiftid + "/"+searchType+".json";
	}
}
/** *******************营业数据明细报表 END***************************************** */
/** *******************结算方式明细报表 START*************************************** */
function initPaywayData() {
	$(".legend-custom-div").remove();
	if(compareBeginEndTime()){
		var tb = "";
		$.post(global_Path + "/settlementOption/settlementOptionList.json", {
			beginTime : beginTime,
			endTime : endTime,
			shiftid : shiftid
		}, function(result) {
			var legend_data = [];
			var num_series_data = [];
			var price_series_data = [];
			if (result != null && result.length > 0) {
				$.each(result, function(i, item) {
					legend_data.push(item.payway);
					num_series_data.push({
						value : item.nums,
						name : item.payway
					});
					price_series_data.push({
						value : Math.abs(strToFloat(item.prices)),
						name : item.payway
					});

					tb += '<tr ondblclick="showPaywaySub(\''+item.payway+'\', \''+item.nums+'\', \''+item.prices+'\', \''+item.membercardno+'\', \''+item.itemid+'\')"><td>' + item.payway + '</td><td>' + item.nums
							+ '</td><td>' + item.prices + '<i class="icon-chevron-right" style="color: #000000;float: right;"></i></td></tr>';
				});
			}
			$("#payway_data tbody").html(tb);
			paywayNums(legend_data, num_series_data);
			paywayPrices(legend_data, price_series_data);
		});
	}
}
/**
 * 获取结算方式详情数据
 * @param payway
 * @param nums
 * @param amount
 */
function showPaywaySub(payway, nums, amount, membercardno, itemid){
	$("#p-payway").val(payway);
	$("#membercardno").val(membercardno);
	$("#itemid").val(itemid);

	$("#payway-name").text(payway);
	$("#payway-nums").text(nums);
	$("#payway-amount").text(amount);
	$("#payway-details-dialog").modal("show");
	$("#payway_sub_tb tbody").html("");
	$.post(global_Path+"/settlementDetailChild/getSettDetChildList.json", {
		beginTime : beginTime,
		endTime : endTime,
		shiftid : shiftid,
		payWay : transcoding(payway),
		membercardno : membercardno,
		itemid : itemid
	}, function(result){
		if(result.flag == 1){
			initPaywaySubTb(result.data);
		}
	},'json');
}
/**
 * 结算方式详情表格
 * @param data
 */
function initPaywaySubTb(data){
	if(data != null && data.length>0){
		var htm = "";
		$.each(data, function(i, item){
			htm += '<tr ondblclick="showReckon(\''+item.orderid+'\')"><td>'+item.insertTime+'</td>'
				+ '<td>'+item.orderid+'</td>'
				+ '<td>'+item.payAmount+'</td></tr>';
		});
		$("#payway_sub_tb tbody").html(htm);
	}
}
/**
 * 结算方式笔数占比
 * @param legend_data
 * @param series_data
 */
function paywayNums(legend_data, series_data) {
	var domMain = document.getElementById('payway_num_main');
	_pieChart('结算方式笔数占比', domMain, legend_data, series_data, 1);
}
/**
 * 结算方式金额占比
 * @param legend_data
 * @param series_data
 */
function paywayPrices(legend_data, series_data) {
	var domMain = document.getElementById('payway_money_main');
	_pieChart('结算方式金额占比', domMain, legend_data, series_data, 1);
}
/**
 * 结算方式导出
 */
function exportxlsC(f) {
	if(compareBeginEndTime()){
		if (beginTime == null || "" == beginTime) {
			var d = new Date();
			var month = d.getMonth() + 1;
			if (d.getMonth() + 1 < 10) {
				month = "0" + month;
			}
			var day = d.getDate();
			if (d.getDate() < 10) {
				day = "0" + day;
			}
			beginTime = d.getFullYear() + '-' + month + '-' + day
					+ ' 00:00:00';
		}

		if (endTime == null || "" == endTime) {
			var d = new Date();
			var month = d.getMonth() + 1;
			if (d.getMonth() + 1 < 10) {
				month = "0" + month;
			}
			var day = d.getDate();
			if (d.getDate() < 10) {
				day = "0" + day;
			}
			var day = d.getDate();
			if (d.getDate() < 10) {
				day = "0" + day;
			}
			var hours = d.getHours();
			if (d.getHours() < 10) {
				hours = "0" + hours;
			}
			var minutes = d.getMinutes();
			if (d.getMinutes() < 10) {
				minutes = "0" + minutes;
			}
			var second = d.getSeconds();
			if (d.getSeconds() < 10) {
				second = "0" + second;
			}

			endTime = d.getFullYear() + '-' + month + '-' + day + ' '
					+ hours + ":" + minutes + ":" + second;
		}
		if(f == 1){
			var payway = encodeURI(encodeURI($("#p-payway").val()));
			var itemid = $("#itemid").val();
			var membercardno = $("#membercardno").val();
			if(membercardno == null || membercardno == ""){
				membercardno = 0;
			}
			//详情
			location.href = global_Path + "/settlementDetailChild/exportSettDetailChildList/"
				+beginTime + "/" + endTime +"/"+payway+"/" + shiftid+"/"+searchType+"/"+itemid+"/"+membercardno;
		}else{
			location.href = global_Path + "/settlementOption/exportXls?beginTime="
				+ beginTime + "&endTime=" + endTime + "&shiftid=" + shiftid+"&searchType="+searchType;
		}
	}
}
/** ********************结算方式明细报表 END*************************************** */

/***********************赠菜明细表 START *******************************************/
function initFreeDishData() {
	if(compareBeginEndTime()){
		beginTime = $("#beginTime").val();
		endTime = $("#endTime").val();
		shiftid = $("#shiftid").val();
		page = 0;
		doFreePost(function(result){
			initFreeTb(result, true);
		});
	}
}
function initFreeTb(result, isFirst){
	var tb = "";
	var len = result.length;
	if (result != null && result.length > 0) {
		page ++;
		if(isFirst){
			var more = '<tr><td id="show-more" class="show-more" colspan="8">加载更多</td></tr>';
			$("#free_dish_data tbody").html(more);
		}
		$.each(result, function(i, item) {
			console.info(item.accreditWaiter);
			tb += '<tr ondblclick="showReckon(\''+item.orderid+'\')"><td>' + item.beginTime + '</td><td>' + item.orderid + '</td><td>' + item.title
				+ '</td><td>'+ item.num + '</td><td>' + item.amount + '</td><td>' + (item.waiter === undefined ? '' : item.waiter)
				+ '</td><td>' + (item.accreditWaiter === undefined ? '' : item.accreditWaiter) + '</td><td onclick="showReckon(\''+item.orderid+'\')">'
				+ item.presentReason
				+ '<i class="icon-chevron-right" style="color: #000000;float: right;"></i>'
				+ '</td></tr>';
		});
		$("#show-more").parent().before(tb);
		if(len < 20){
			$("#show-more").parent().remove();
		}
	}else{
		if(isFirst){
			var nodata = '<tr><td colspan="8">没有数据</td></tr>';
			$("#free_dish_data tbody").html(nodata);
		}else{
			alert("没有更多数据");
		}
	}
	$("#show-more").unbind("click").click(function(){
		doFreePost(initFreeTb);
	});
}
function doFreePost(callback){
	$.post(global_Path + "/presentDish/getPreDishDetailList.json", {
		beginTime : beginTime,
		endTime : endTime,
		shiftid : shiftid,
		currPage: page,//当前页数
		pageNums:20//每页显示条数
	}, function(result) {
		callback(result);
	});
}
/**
 * 赠菜明细表 导出
 */
function exportFreeDishxls() {
	if(compareBeginEndTime()){
		if (beginTime == null || "" == beginTime) {
			var d = new Date();
			var month = d.getMonth() + 1;
			if (d.getMonth() + 1 < 10) {
				month = "0" + month;
			}
			var day = d.getDate();
			if (d.getDate() < 10) {
				day = "0" + day;
			}
			beginTime = d.getFullYear() + '-' + month + '-' + day
				+ ' 00:00:00';
		}

		if (endTime == null || "" == endTime) {
			var d = new Date();
			var month = d.getMonth() + 1;
			if (d.getMonth() + 1 < 10) {
				month = "0" + month;
			}
			var day = d.getDate();
			if (d.getDate() < 10) {
				day = "0" + day;
			}
			var day = d.getDate();
			if (d.getDate() < 10) {
				day = "0" + day;
			}
			var hours = d.getHours();
			if (d.getHours() < 10) {
				hours = "0" + hours;
			}
			var minutes = d.getMinutes();
			if (d.getMinutes() < 10) {
				minutes = "0" + minutes;
			}
			var second = d.getSeconds();
			if (d.getSeconds() < 10) {
				second = "0" + second;
			}

			endTime = d.getFullYear() + '-' + month + '-' + day + ' '
				+ hours + ":" + minutes + ":" + second;
		}
		location.href = global_Path + "/presentDish/exportXls?beginTime="
			+ beginTime + "&endTime=" + endTime + "&shiftid=" + shiftid+"&currPage=-1&pageNums=20&searchType="+searchType;
	}
}
/***********************赠菜明细表 START *******************************************/

/***********************退菜明细表 START *******************************************/
var page = 0;
function initReturnDishData() {
	if(compareBeginEndTime()){
		beginTime = $("#beginTime").val();
		endTime = $("#endTime").val();
		shiftid = $("#shiftid").val();
		page = 0;
		doReturnPost(function(result){
			initReturnTb(result, true);
		});
	}
}
function initReturnTb(result, isFirst){
	var tb = "";
	var len = result.length;
	if (result != null && result.length > 0) {
		page ++;
		if(isFirst){
			var more = '<tr><td id="show-more" class="show-more" colspan="8">加载更多</td></tr>';
			$("#return_dish_data tbody").html(more);
		}
		$.each(result, function(i, item) {
			tb += '<tr ondblclick="showReckon(\''+item.orderid+'\')"><td>' + item.beginTime + '</td><td>' + item.orderid + '</td><td>' + item.title
				+ '</td><td>'+ item.num + '</td><td>' + item.amount + '</td><td>' + item.waiter
			    + '</td><td >' + item.discardusername + '</td><td onclick="showReckon(\''+item.orderid+'\')">'
			    + item.discardreason
			    + '<i class="icon-chevron-right" style="color: #000000;float: right;"></i>'
			    + '</td></tr>';
		});
		$("#show-more").parent().before(tb);
		if(len < 20){
			$("#show-more").parent().remove();
		}
	}else{
		if(isFirst){
			var nodata = '<tr><td colspan="8">没有数据</td></tr>';
			$("#return_dish_data tbody").html(nodata);
		}else{
			alert("没有更多数据");
		}
	}
	$("#show-more").unbind("click").click(function(){
		doReturnPost(initReturnTb);
	});
}
function doReturnPost(callback){
	$.post(global_Path + "/returnDish/getReturnDishList.json", {
		beginTime : beginTime,
		endTime : endTime,
		shiftid : shiftid,
		currPage: page,//当前页数
		pageNums: 20//每页显示条数
	}, function(result) {
		callback(result);
	});
}
/**
 * 退菜明细表 导出
 */
function exportReturnDishxls() {
	if(compareBeginEndTime()){
		if (beginTime == null || "" == beginTime) {
			var d = new Date();
			var month = d.getMonth() + 1;
			if (d.getMonth() + 1 < 10) {
				month = "0" + month;
			}
			var day = d.getDate();
			if (d.getDate() < 10) {
				day = "0" + day;
			}
			beginTime = d.getFullYear() + '-' + month + '-' + day
					+ ' 00:00:00';
		}

		if (endTime == null || "" == endTime) {
			var d = new Date();
			var month = d.getMonth() + 1;
			if (d.getMonth() + 1 < 10) {
				month = "0" + month;
			}
			var day = d.getDate();
			if (d.getDate() < 10) {
				day = "0" + day;
			}
			var day = d.getDate();
			if (d.getDate() < 10) {
				day = "0" + day;
			}
			var hours = d.getHours();
			if (d.getHours() < 10) {
				hours = "0" + hours;
			}
			var minutes = d.getMinutes();
			if (d.getMinutes() < 10) {
				minutes = "0" + minutes;
			}
			var second = d.getSeconds();
			if (d.getSeconds() < 10) {
				second = "0" + second;
			}

			endTime = d.getFullYear() + '-' + month + '-' + day + ' '
					+ hours + ":" + minutes + ":" + second;
		}
		location.href = global_Path + "/returnDish/exportXls?beginTime="
			+ beginTime + "&endTime=" + endTime + "&shiftid=" + shiftid+"&currPage=-1&pageNums=20&searchType="+searchType;
	}
}
/***********************退菜明细表 END *******************************************/
/***********************优惠活动明细表 START***************************************/
//转码
function transcoding(name){
	return encodeURIComponent(name, "utf-8");
//	return name;
}
function searchData() {
	initCouponData();
}
//调api获取数据
function initCouponData(){
	if(compareBeginEndTime()){
		beginTime = $("#beginTime").val();
		endTime = $("#endTime").val();
		shiftid = $("#shiftid").val();
		settlementWay = $("#settlementWay").val()==null?"-1":$("#settlementWay").val();
		bankcardno = transcoding($("#bankcardno").val()==null?"-1":$("#bankcardno").val());
		type = $("#type").val()==null?"-1":$("#type").val();
		doCouponPost(initTb);
	}
}
/**
 * 优惠活动明细
 * 发送请求，请求返回后执行回调函数
 * @param callback
 */
function doCouponPost(callback){
	$.post(global_Path + "/preferentialAnalysisCharts/findPreferential.json", {
		beginTime : beginTime,
		endTime : endTime,
		shiftid : shiftid,
		bankcardno: bankcardno,
		settlementWay: settlementWay,
		type: type
	}, function(result){
		callback(result);
	});
}
//初始化列表
function initTb(datalist) {
	var tHtml = "";
	if(datalist!=null && datalist!=""){
		$.each(datalist, function(i, obj) {
			tHtml += '<tr ondblclick ="showsubTb(\''
				+ obj.pname
				+ '\',\''
				+ obj.payway
				+ '\',\''
				+ obj.pname
				+ '\',\''
				+ obj.paywaydesc
				+ '\',\''
				+ obj.ptype
				+ '\')">'
				+ '<td width="10%">'
				+ obj.pname
				+ '</td>'
				+ '<td width="10%">'
				+ obj.ptypename
				+ '</td>'
				+ '<td width="10%">'
				+ obj.paywaydesc
				+ '</td>'
				+ '<td width="10%">'
				+ obj.singular
				+ '</td>'
				+ '<td width="10%">'
				+ obj.couponNum
				+ '</td>'
				+ '<td width="10%">'
				+ obj.payamount
				+ '</td>'
				+ '<td width="10%">'
				+ obj.shouldamount
				+ '</td>'
				+ '<td width="10%">'
				+ obj.paidinamount
				+ '</td>'
				+ '<td width="10%">'
				+ obj.perCapita
				+ '<i class="icon-chevron-right" style="color: #000000;float: right;"></i></td>'
				+ '</tr>';
		});
	}else{
		tHtml += '<tr><td colspan="9">没有数据</td></tr>';
	}
	$("#coupon_tb tbody").html(tHtml);
}
/**
* 显示子表
*/
function showsubTb(code, payway, name, paywaydesc, ptype) {
	$("#p-coupon-id").val(code);
	$("#p-coupon-payway").val(payway);
	$("#p-type-id").val(ptype);
	$("#coupon-name").text(name);
	$("#payway-name").text(paywaydesc);
	$("#coupon-details-dialog").modal("show");
	page = 0;
	doCouponSubPost(code, payway, ptype, function(result){
		initSubTb(code, payway, ptype, result, true);
	});
}
/**
 * 发送请求
 * @param code
 * @param payway
 * @param callback
 */
function doCouponSubPost(code, payway, ptype, callback){
	$.post(global_Path + "/preferentialAnalysisCharts/findPreferentialDetail.json", {
		couponsname: transcoding(code),
		payway : payway,
		beginTime : beginTime,
		endTime : endTime,
		shiftid : shiftid,
		currPage: page,//当前页数
		pageNums: 20,//每页显示条数
		type : ptype
	}, function(result){
		callback(result);
	});
}
//初始化子表数据
function initSubTb(code, payway, ptype, result, isFirst) {
	var subTbody = "";
	var len = result.length;
	if(result != null && result != "" && result.length>0){
		page ++;
		if(isFirst){
			var more = '<tr><td id="show-more" class="show-more" colspan="7">加载更多</td></tr>';
			$("#coupon_sub_tb tbody").html(more);
		}
		$.each(result, function(i, item){
			subTbody += "<tr ondblclick='showReckon(\""+item.orderid+"\")'>";
			subTbody += "<td>" + item.begintime + "</td>";
			subTbody += "<td>" + item.orderid + "</td>";
			subTbody += "<td>" + item.price + "</td>";
			subTbody += "<td>" + item.couponNum + "</td>";
			subTbody += "<td>" + item.payamount + "</td>";
			subTbody += "<td>" + item.shouldamount + "</td>";
			subTbody += "<td>" + item.paidinamount + "</td>";
			subTbody += "</tr>";
		});
		$("#show-more").parent().before(subTbody);
		if(len < 20){
			$("#show-more").parent().remove();
		}
	}else{
		if(isFirst){
			var nodata = '<tr><td colspan="7">没有数据</td></tr>';
			$("#coupon_sub_tb tbody").html(nodata);
		}else{
			alert("没有更多数据！");
		}
	}
	$("#show-more").unbind("click").click(function(){
		doCouponSubPost(code, payway, ptype, function(result){
			initSubTb(code, payway, ptype, result, false);
		});
	});
}
/**
 * 活动名称
 */
function getActiviy() {
	$.get(global_Path+"/daliyReports/getActivityNameList.json",
	function(result){
		var option = '<option value="-1">全部</option>';
		$.each(result, function(i, item){
			option += '<option value="'+item.codeDesc+'">'+item.codeDesc+'</option>';
		});
		$("#bankcardno").html(option);
	});
}
/**
 * 结算方式
 */
function getPayway() {
	$.get(global_Path+"/daliyReports/getpaywayList.json", function(result){
		var option = '<option value="-1">全部</option>';
		$.each(result, function(i, item){
			if(item.itemid == 5 || item.itemid == 6){// || item.itemid == 12
				option += '<option value="'+item.itemid+'">'+item.itemDesc+'</option>';
			}
		});
		$("#settlementWay").html(option);
	});
}
/**
 * 活动类别api
 * @param callback
 */
function doGetActiviyType(callback){
	$.get(global_Path+"/daliyReports/getTypeDictList.json", function(result){
		callback(result);
	});
}
/**
 * 活动类别
 */
function getActiviyType() {
	doGetActiviyType(function(result){
		var option = '<option value="-1">全部</option>';
		$.each(result, function(i, item){
			option += '<option value="'+item.codeId+'">'+item.codeDesc+'</option>';
		});
		$("#type").html(option);
	});

}
/**
 * 优惠活动明细表 导出
 * @param f
 */
function exportReportsCou(f) {
	if(compareBeginEndTime()){
		if (beginTime == null || "" == beginTime) {
			var d = new Date();
			var month = d.getMonth() + 1;
			if (d.getMonth() + 1 < 10) {
				month = "0" + month;
			}
			var day = d.getDate();
			if (d.getDate() < 10) {
				day = "0" + day;
			}
			beginTime = d.getFullYear() + '-' + month + '-' + day + ' 00:00:00';
		}

		if (endTime == null || "" == endTime) {
			var d = new Date();
			var month = d.getMonth() + 1;
			if (d.getMonth() + 1 < 10) {
				month = "0" + month;
			}
			var day = d.getDate();
			if (d.getDate() < 10) {
				day = "0" + day;
			}
			var day = d.getDate();
			if (d.getDate() < 10) {
				day = "0" + day;
			}
			var hours = d.getHours();
			if (d.getHours() < 10) {
				hours = "0" + hours;
			}
			var minutes = d.getMinutes();
			if (d.getMinutes() < 10) {
				minutes = "0" + minutes;
			}

			var second = d.getSeconds();
			if (d.getSeconds() < 10) {
				second = "0" + second;
			}

			endTime = d.getFullYear() + '-' + month + '-' + day + ' ' + hours
					+ ":" + minutes + ":" + second;
		}

		var payway = "";
		var ptype = "";
		var pname = "";
		var action_Path = "";

		if(f == 1){
			pname = $("#p-coupon-id").val();
			payway = $("#p-coupon-payway").val();
			ptype = $("#p-type-id").val();
			action_Path = global_Path + "/preferentialAnalysisCharts/exportReportCouDetailSub.json";// 子表
		}else{
			action_Path = global_Path + "/preferentialAnalysisCharts/exportReportCouDetail.json";// 总表
		}
		$("#_beginTime").val(beginTime);
		$("#_endTime").val(endTime);
		$("#_settlementWay").val(settlementWay);
		$("#_shiftid").val(shiftid);
		$("#_bankcardno").val(bankcardno);
		$("#_type").val(type);
		$("#_payway").val(payway);
		$("#_ptype").val(ptype);
		$("#_pname").val(pname);
		$("#_searchType").val(searchType);
		$("#couponForm").attr("action", action_Path);
		$("#couponForm").submit();
	}
}
/***********************优惠活动明细表 END****************************************/
/***********************品项销售明细表 START**************************************/
//调api获取数据
function initItemDetailsData() {
	if(compareBeginEndTime()){
		beginTime = $("#beginTime").val();
		endTime = $("#endTime").val();
		shiftid = $("#shiftid").val();
		itemId = $("#itemID").val()==null?"-1":$("#itemID").val();
		_dishType = $("#dishType").val();
		doItemDetailsPost(initItemTb);
	}
}
function doItemDetailsPost(callback){
	$.post(global_Path + "/itemDetail/getItemForList.json", {
		beginTime : beginTime,
		endTime : endTime,
		shiftid : shiftid,
		id : itemId,
		dishType : _dishType
	}, function(result) {
		callback(result);
	},'json');
}
//初始化列表
function initItemTb(datalist) {
	var tHtml = "";
	if (datalist != null && datalist != "") {
		$.each(datalist, function(i, obj) {
			var share = parseFloat(obj.share);
			var turnover = parseFloat(obj.turnover);
			var dishType = obj.dishtype;
			var typedesc = dishType == 0 ? "单品" : (dishType == 1 ? "鱼锅" : "套餐");
			tHtml += '<tr itemid="'+obj.id+'" dishType="'+dishType+'" ondblclick="showItemSubTb(\''
					+ obj.id
					+ '\',\''
					+ dishType
					+ '\',\''
					+ obj.itemDesc
					+ '\',\''
					+ typedesc
					+ '\')">'
					+ '<td width="20%">'
					+ obj.itemDesc
					+ '</td>'
					+ '<td width="15%">'
					+ typedesc
					+ '</td>'
					+ '<td width="15%">' + obj.number
					+ '</td><td>'+obj.thousandstimes+'</td><td>'+obj.orignalprice
 					+'</td>'
					+ '<td width="15%">'
					+ turnover.toFixed(2)
					+ '<i class="icon-chevron-right" style="color: #000000;float: right;"></i></td>'
					+ '</tr>';
		});
	}else{
		tHtml += '<tr><td colspan="6">没有数据</td></tr>';
	}

	$("#items_tb tbody").html(tHtml);
}
/**
 * 显示子表
 */
function showItemSubTb(id, dishType, itemdesc, typedesc) {
	$("#p-item-id").val(id);
	$("#p-dish-type").val(dishType);
	$("#item-desc").text(itemdesc);
	$("#dish-type-desc").text(typedesc);
	$("#item-details-dialog").modal("show");
	initItemSubTb(id, dishType);

}
//初始化子表数据
function initItemSubTb(id, dishType) {
	$.post(global_Path + "/itemDetail/getItemDetailForList.json", {
		id : id,
		dishType : dishType,
		beginTime : beginTime,
		endTime : endTime,
		shiftid : shiftid
	}, function(result) {
		var subTbody = "";
		$.each(result, function(i, item) {
			var share = parseFloat(item.share);
			var turnover = parseFloat(item.turnover);
			subTbody += "<tr>";
			subTbody += "<td>" + item.title + "</td>";
			subTbody += "<td>" + item.dishNo + "</td>";
			subTbody += "<td>" + item.price + "</td>";
			subTbody += "<td>" + item.unit + "</td>";
			subTbody += "<td>" + item.number + "</td>";
			subTbody += "<td>" + item.thousandstimes+"</td><td>"+item.orignalprice+"</td>";
			subTbody += "<td>" + turnover.toFixed(2) + "</td>";//
			subTbody += "</tr>";
		});
		$("#item_sub_tb tbody").html(subTbody);
	},'json');
}
/**
 * 品类api
 * @param callback
 */
function doGetItemType(callback){
	$.get(global_Path + "/itemDetail/getItemTypeList.json", function(result) {
		callback(result);
	});
}
/**
 * 品类
 */
function getItemType() {
	doGetItemType(function(result) {
		var option = '<option value="-1">全部</option>';
		$.each(result, function(i, item) {
			option += '<option value="'+item.codeId+'">'
					+ item.codeDesc + '</option>';
		});
		$("#itemID").html(option);
	});
}
/**
 * 品项销售明细 导出
 * @param f
 */
function exportReportsItem(f) {
	if(compareBeginEndTime()){
		if (beginTime == null || "" == beginTime) {
			var d = new Date();
			var month = d.getMonth() + 1;
			if (d.getMonth() + 1 < 10) {
				month = "0" + month;
			}
			var day = d.getDate();
			if (d.getDate() < 10) {
				day = "0" + day;
			}
			beginTime = d.getFullYear() + '-' + month + '-' + day
					+ ' 00:00:00';
		}

		if (endTime == null || "" == endTime) {
			var d = new Date();
			var month = d.getMonth() + 1;
			if (d.getMonth() + 1 < 10) {
				month = "0" + month;
			}
			var day = d.getDate();
			if (d.getDate() < 10) {
				day = "0" + day;
			}
			var day = d.getDate();
			if (d.getDate() < 10) {
				day = "0" + day;
			}
			var hours = d.getHours();
			if (d.getHours() < 10) {
				hours = "0" + hours;
			}
			var minutes = d.getMinutes();
			if (d.getMinutes() < 10) {
				minutes = "0" + minutes;
			}

			var second = d.getSeconds();
			if (d.getSeconds() < 10) {
				second = "0" + second;
			}

			endTime = d.getFullYear() + '-' + month + '-' + day + ' '
					+ hours + ":" + minutes + ":" + second;
		}
		if(shiftid == ""){
			shiftid = "null";
		}
		var id = itemId;
		if(id == null || id == ""){
			id = "null";
		}
		if(_dishType == null || _dishType == ""){
			_dishType = "null";
		}

		var itemids = "";
		if(f == 1){
			var itemid = $("#p-item-id").val();
			var dishtype1 = $("#p-dish-type").val();
			itemids = itemid+","+dishtype1+"|";
		}else{
			itemids = "null";
		}

	//	location.href = global_Path + "/itemDetail/exportxlsA/"
	//			+ beginTime + "/" + endTime + "/" + shiftid + "/" + id + "/"+ _dishType + "/"+itemids+"/"+searchType+".json";
		$("#_beginTime").val(beginTime);
		$("#_endTime").val(endTime);
		$("#_shiftid").val(shiftid);
		$("#_id").val(id);
		$("#dish_type").val(_dishType);
		$("#_itemids").val(itemids);
		$("#_searchType").val(searchType);
		$("#itemDetailForm").attr("action", global_Path + "/itemDetail/exportxlsA.json");
		$("#itemDetailForm").submit();
	}
}
/***********************品项销售明细表 END***************************************/
/***********************服务员考核 START**************************/
/**
 * 导出
 */
function exportWaiterAssess(type){
	var userid = $("#p_userid").val();
	if(type == 0){
		location.href = global_Path + "/waiter/shift/"+beginTime+"/"+endTime+"/"+shiftid+".json";
	}else{
		location.href = global_Path + "/waiter/shiftorders/"+beginTime+"/"+endTime+"/"+shiftid+"/"+userid+".json";
	}
}
/**
 * 初始化datatable
 */
function initDatatableConfig(){
	oTable = $("#waiter-assess-tb").dataTable( {
		"bAutoWidth":false,
    	"bFilter":false,
    	"bInfo":false,
    	"bPaginate": false,
    	"bSort": true,
    	"aaSorting": [[0,'asc']],
    	"aoColumnDefs": [
    	   { "bSortable": false, "aTargets": [ 1 ] }],
    	"oLanguage": {
    		"sEmptyTable": "无数据"
		},
    	"bRetrieve": false,
    	"bDestroy": true
	});
	$(".row-fluid").addClass("hide");
}
/**
 * 获取数据
 */
/*function getWaiterAssessData(){
	if(oTable !=null){
		oTable.fnClearTable(false);
	}
	beginTime = $("#beginTime").val();
	endTime = $("#endTime").val();
	shiftid = $("#shiftid").val();
	$.get(global_Path+"/waiter/shift.json", {
		beginTime: beginTime,
		endTime: endTime,
		shiftid: shiftid
	}, function(result){
		console.log(result);
		if(result.flag == 1){
			var data = result.data;
			var htm = '';
			if(data != null && data.length>0){
				$.each(data, function(i, item){
					htm += '<tr ondblclick="showWaiterSecPage(\''+item.userid+'\')">'
						+ '<td>'+item.userid+'</td>'
						+ '<td>'+item.username+'</td>'
						+ '<td>'+item.ordernum+'</td>'
						+ '<td>'+item.custnum+'</td>'
						+ '<td>'+item.shouldamount+'</td>'
						+ '<td>'+item.paidinamount+'</td>'
						+ '<td>'+item.shouldpre+'</td>'
						+ '<td>'+item.paidinpre+'</td>'

						+ '<td>'+item.xjamount+'</td>'
						+ '<td>'+item.yhkamount+'</td>'
						//+ '<td>'+item.mlamount+'</td>'
						+ '<td>'+item.hykxfamount+'</td>'
						//+ '<td>'+item.hyjfxfamount+'</td>'
						+ '<td>'+item.gz2amount+'</td>'
						+ '<td>'+item.wxzfamount+'</td>'
						+ '<td>'+item.zfbzfamount+'</td></tr>';
				});
				$("#waiter-assess-tb tbody").html(htm);
				initDatatableConfig();
			}else{
				htm += '<tr><td colspan="16">无数据</td></tr>';
				$("#waiter-assess-tb tbody").html(htm);
			}
		}else{
			alert(result.desc);
		}
	},'json');
}*/

function getWaiterAssessData(){
	if(oTable !=null){
		oTable.fnClearTable(false);
	}
	beginTime = $("#beginTime").val();
	endTime = $("#endTime").val();
	shiftid = $("#shiftid").val();
	$.get(global_Path+"/waiter/shift.json", {
		beginTime: beginTime,
		endTime: endTime,
		shiftid: shiftid
	}, function(result){
		console.log(result);
		if(result.flag == 1){
			var data = result.data.td;
			var tr = result.data.tr;
			var htm = '';
			$.each(tr,function(i,item){
				htm += '<th class="ss">实收/'+dellrTrim(item)+'</th>';
			});
			$(".ss").remove();
			$("#waiter-assess-tb thead tr").append(htm);
			htm = '';
			if(data != null && data.length>0){
				$.each(data, function(i, item){
					htm += '<tr ondblclick="showWaiterSecPage(\''+item.waiterId+'\')">'
						+ '<td>'+item.waiterId+'</td>'
						+ '<td>'+item.waiterName+'</td>'
						+ '<td>'+item.tableNum+'</td>'
						+ '<td>'+item.custNum+'</td>'
						+ '<td>'+item.shouldAmount+'</td>'
						+ '<td>'+item.actualAmountTotal+'</td>'
						+ '<td>'+item.shouldPre+'</td>'
						+ '<td>'+item.actualPre+'</td>';
					$.each(item.settlements,function(i,item){
						htm += '<td>'+item+'</td>';
					});
					htm += '</tr>';
				});
				$("#waiter-assess-tb tbody").html(htm);
				initDatatableConfig();
			}else{
				htm += '<tr><td colspan="16">无数据</td></tr>';
				$("#waiter-assess-tb tbody").html(htm);
			}
		}else{
			alert(result.desc);
		}
	},'json');
}
function showWaiterSecPage(userid){
	$("#p_userid").val(userid);
	$("#waiter-assess-dialog").modal("show");
	getWaiterDetails();
}
function getWaiterDetails(){
	$("#waiterassess-details-tb tbody").html('');
	$.get(global_Path+"/waiter/shiftorders.json", {
		beginTime: beginTime,
		endTime: endTime,
		shiftid: shiftid,
		userid: $("#p_userid").val()
	}, function(result){
		if(result.flag == 1){
			var data = result.data.td;
			var tr = result.data.tr;
			var htm = '';
			$.each(tr,function(i,item){
				htm += '<th class="ssd">实收/'+dellrTrim(item)+'</th>';
			});
			$(".ssd").remove();
			$("#waiterassess-details-tb thead tr").append(htm);
			htm = '';
			if(data != null && data.length>0){
				$.each(data, function(i, item){
					htm += '<tr><td>'+item.orderId+'</td>'
						+ '<td>'+item.tableNo+'</td>'
						+ '<td>'+item.custNum+'</td>'
						+ '<td>'+item.shouldAmount+'</td>'
						+ '<td>'+item.actualAmountTotal+'</td>';
					$.each(item.settlements,function(i,item){
						htm += '<td>'+item+'</td>';
					});
					htm += '</tr>';
				});
			}else{
				htm = '<tr><td colspan="5">无数据</td></tr>';
			}
			$("#waiterassess-details-tb tbody").html(htm);
		}else{
			alert(result.desc);
		}
	},'json');
}
/***********************服务员考核 END****************************/
/***********************排班参考报表 start************************/
function getScheduleReport(){
	showLoading();
	beginTime = $("#beginTime").val();
	endTime = $("#endTime").val();
	shiftid = "-1";
	dateinterval = $("#dateinterval").val();
	week  = '-1';
	$("input[name='weeky']:checked").each(function(i, o){
		if(i == 0){
			week = $(this).val();
		}else{
			week += ','+$(this).val();
		}
	});
	$.post(global_Path+"/scheduling/schedulingReport.json",{
		beginTime: beginTime,
		endTime: endTime,
		shiftid: shiftid,
		week: week,
		dateinterval: dateinterval
	}, function(result){
		console.log(result);
		initScheduleTb(result);
		hideLoading();
	},'json');
}
function initScheduleTb(result){
	if(result == null || result.length == 0){
		$("#nodataPrompt").modal("show");

		setInterval(function(){
			$("#nodataPrompt").modal("hide");
		},3000);
	}
	var frozenTh = '';
	var frozenTd = '';
	var scheduleThs  ='';//日期标题
	var scheduleThs_sec = '<tr>';//下面的副标题
	var scheduleTds = '';//内容

	var avgThs = '';
	if(result != null && result.length > 0){
		$.each(result, function(i, item){
			console.log(item);
			var time = item.time;
			frozenTd += '<tr><td>'+time+'</td></tr>';

			var stores = item.stores;
			if(stores!=null && stores.length>0){
				var avgTds = '';
				var otherTds = '';
				$.each(stores, function(j, store){
					var dateTh = store.date;
					if(i == 0){
						if(dateTh == "avg"){
							avgThs = '<th colspan="7" class="wight-bg">平均值</th>';
						}else{
							scheduleThs += '<th colspan="7" class="wight-bg">'+dateTh+'</th>';
						}
						scheduleThs_sec += '<th nowrap="nowrap">开台数</th>'
							+ '<th nowrap="nowrap">上客数</th>'
							+ '<th nowrap="nowrap">已点餐金额</th>'
							+ '<th nowrap="nowrap">已结账台数</th>'
							+ '<th nowrap="nowrap">结账金额</th>'
							+ '<th nowrap="nowrap">未结账台数</th>'
							+ '<th nowrap="nowrap">在台数</th>';
					}
					var values = store.values;
					if(dateTh == "avg"){
						avgTds += '<td>'+values.openNum+'</td>'
							+ '<td>'+values.guestNum+'</td>'
							+ '<td>'+values.orderamount+'</td>'
							+ '<td>'+values.alreadycheckNum+'</td>'
							+ '<td>'+values.checkamount+'</td>'
							+ '<td>'+values.notcheckNum+'</td>'
							+ '<td>'+values.intheNum+'</td>';
					}else{
						otherTds += '<td>'+values.openNum+'</td>'
							+ '<td>'+values.guestNum+'</td>'
							+ '<td>'+values.orderamount+'</td>'
							+ '<td>'+values.alreadycheckNum+'</td>'
							+ '<td>'+values.checkamount+'</td>'
							+ '<td>'+values.notcheckNum+'</td>'
							+ '<td>'+values.intheNum+'</td>';
					}
				});
				scheduleTds += '<tr>'+avgTds+otherTds+'</tr>';
			}
		});
		scheduleThs = '<tr>'+avgThs+scheduleThs+'</tr>';
		scheduleThs_sec += '</tr>';
		frozenTh = '<tr><th nowrap="nowrap" rowspan="2" class="wight-bg" style="vertical-align: middle;height: 66px;">时间段</th></tr>';
	}

	$("#frozen-schedule-Tb thead").html(frozenTh);
	$("#frozen-schedule-Tb tbody").html(frozenTd);
	$("#schedule-tb thead").html(scheduleThs+scheduleThs_sec);
	$("#schedule-tb tbody").html(scheduleTds);
}
/**
 * 排班导出
 */
function exportScheduleReport(){
	location.href = global_Path + "/scheduling/exportSchedulingData/"+beginTime+"/"+endTime+"/"+shiftid+"/"+week+"/"+dateinterval+".json";
}
/***********************反结算统计表 START***************************************/
/**
 * 反结算导出
 */
function exportSettlement(){
	if(compareBeginEndTime()){
		location.href = global_Path + "/dishtypeSettlement/exportSettDetailChildList/"+ beginTime + "/" + endTime + "/" + searchType + ".json";
	}
}
/**
 * 反结算查询
 */
function initSettlementData(){
	if(compareBeginEndTime()){
		beginTime = $("#beginTime").val();
		endTime = $("#endTime").val();
		doSettlementPost(initSettlementTb);
	}
}
/**
 * 发送请求
 * @param callback
 */
function doSettlementPost(callback){
	$.post(global_Path+"/dishtypeSettlement/getRethinkSettlementList.json", {
		beginTime : beginTime,
		endTime : endTime
	}, function(result){
		if(result.flag == 1){
			callback(result.data);
		}else{
			alert(result.desc);
		}
	},'json');
}
/**
 * 组合数据表格
 * @param data
 */
function initSettlementTb(data){
	if(data!=null && data.length>0){
		var htm = "";
		$.each(data, function(i, item){
			htm += "<tr ondblclick='showReckon(\""+item.orderid+"\")'><td>"+item.orderid+"</td>"
				+"<td>"+item.before_cleartime+"</td>"
				+"<td>"+item.before_shouldamount+"</td>"
				+"<td>"+item.before_paidamount+"</td>"
				+"<td>"+item.after_cleartime+"</td>"
				+"<td>"+item.after_shouldamount+"</td>"
				+"<td>"+item.after_paidamount+"</td>"
				+"<td>"+item.timedifference+"</td>"
				+"<td>"+item.paidindifference+"</td>"
				+"<td>"+item.waiter+"</td>"
				+"<td>"+item.cashier+"</td>"
				+"<td>"+item.authorized+"<i class='icon-chevron-right' style='color: #000000;float: right;'></i></td>"
				+"</tr>";
		});
	}else{
		htm = "<tr><td colspan='12'>无数据</td></tr>";
	}
	$("#the_settlement_data tbody").html(htm);
}
/************************反结算统计表 END****************************************/
/***********************交接班统计表 START***************************************/
/**
 * 交接班导出
 */
function exportPresent(){
	if(compareBeginEndTime()){
		location.href = global_Path + "/biz/exportinfos/"+ beginTime + "/" + endTime + ".json";
	}
}
/**
 * 交接班查询
 */
function initPresentData(){
	if(compareBeginEndTime()){
		beginTime = $("#beginTime").val();
		endTime = $("#endTime").val();
		doPresentPost(initPresentTb);
	}
}
/**
 * 发送请求
 * @param callback
 */
function doPresentPost(callback){
	$.post(global_Path+"/biz/infos.json", {
		beginTime : beginTime,
		endTime : endTime
	}, function(result){
		if(result.flag == 1){
			callback(result.data);
		}else{
			alert(result.desc);
		}
	}, 'json');
}
/**
 * 组合数据表格
 * @param data
 */
function initPresentTb(data){
	var tbody = "";
	if(data != null && data.length>0){
		$.each(data, function(i, item){
			tbody += "<tr ondblclick='showPresentSubDialog(\""+item.opentime+"\", \""+item.completiontime+"\")'><td>"+item.opentime+"</td>"
				+"<td>"+item.openauthorized+"</td>"
				+"<td>"+item.completiontime+"</td>"
				+"<td>"+item.completionauthorized
				+"<i class='icon-chevron-right' style='color: #000000;float: right;'></i>"
				+"</td></tr>";
		});
	}else{
		tbody = "<tr><td colspan='4'>无数据</td></tr>";
	}
	$("#present_statistics_data tbody").html(tbody);
}
/**
 * 打开明细dialog
 */
function showPresentSubDialog(opentime, completiontime){
	$("#present-details-dialog").modal("show");
	initPresentDetails(opentime, completiontime);
}
/**
 * 明细列表展示
 */
function initPresentDetails(opentime, completiontime){
	$.post(global_Path+"/biz/infosDetail.json", {
		beginTime : opentime,
		endTime : completiontime
	}, function(result){
		if(result.flag == 1){
			var data = result.data;
			var tbody = "";
			if(data != null && data.length>0){
				$.each(data, function(i, item){
					tbody += "<tr><td>"+item.cashier+"</td><td>"+item.starttime+"</td><td>"+item.endtime+"</td><td>"+item.endauthorized+"</td></tr>";
				});
			}else{
				tbody += "<tr><td colspan='4'>无数据</td></tr>";
			}
			$("#present_sub_tb tbody").html(tbody);
		}else{
			alert(result.desc);
		}
	},'json');
}
/************************交接班统计表 END*****************************************/
/************************营业报表 start******************************************/
function clearCurrtStorage(){
	localStorage.removeItem("branchId");
	localStorage.removeItem("shiftId");
	localStorage.removeItem("beginTime");
	localStorage.removeItem("endTime");
	localStorage.removeItem("currDate");
	selBranchId = "";
}
/**
 *
 */
function doSearchBtn(){
	clearCurrtStorage();
	initBusinessReport();
}
/**
 * 营业报表请求
 */
function initBusinessReport() {
	localStorage.setItem("beginTime", $("#beginTime").val());
	localStorage.setItem("endTime", $("#endTime").val());
	localStorage.setItem("shiftId", $("#shiftid").val());

	branchId = localStorage.getItem("currentStore");
	if(compareBeginEndTime()){
		$.post(global_Path + "/branchbusiness/infos.json", {
			beginTime : $("#beginTime").val(),
			endTime : $("#endTime").val(),
			type : $("#shiftid").val(),
			day : 0
		}, function(result) {
			if(result.flag == 1){
				initBusinessTb(result.data);
			}else{
				alert(result.desc);
			}
		},'json');
	}
}
/**
 * 初始化营业报表数据表
 * @param data
 */
function initBusinessTb(data){
	var tbody = '';
	if(data!= null && data.length>0){
		$.each(data, function(i, item){
			var cla = "";
			if(item.branchId == selBranchId){
				cla = "select_tr";
			}
			tbody += '<tr title="双击查看详情" ondblclick="showSecPage(this)" branchid="'+item.branchId+'" class="'+cla+'"><td>'+item.branchName+'</td>'
				+ '<td>'+item.branchId+'</td>'
				+ '<td>'+item.jdeNo+'</td>'
				+ '<td>'+(item.shouldamount==0?"0.00":item.shouldamount)+'</td>'
				+ '<td>'+(item.paidinamount==0?"0.00":item.paidinamount)+'</td>'
				+ '<td>'+(item.discountamount==0?"0.00":item.discountamount)+'</td>'
				+ '<td>'+(item.cash==0?"0.00":item.cash)+'</td>'
				+ '<td>'+(item.card==0?"0.00":item.card)+'</td>'
				+ '<td>'+(item.othercard==0?"0.00":item.othercard)+'</td>'
				+ '<td>'+(item.weixin==0?"0.00":item.weixin)+'</td>'
				+ '<td>'+(item.zhifubao==0?"0.00":item.zhifubao)+'</td>'
				+ '<td>'+(item.credit==0?"0.00":item.credit)+'</td>'
				+ '<td>'+(item.merbervaluenet==0?"0.00":item.merbervaluenet)+'</td>'
				+ '<td>'+(item.mebervalueadd==0?"0.00":item.mebervalueadd)+'</td>'
				+ '<td>'+(item.integralconsum==0?"0.00":item.integralconsum)+'</td>'
				+ '<td>'+(item.meberTicket==0?"0.00":item.meberTicket)
				+ '<i class="icon-chevron-right" style="color: #000000;float: right;"></i>'
				+'</td>'
				+ '</tr>';
		});
	}else{
		tbody = '<tr><td colspan="16">无数据</td></tr>';
	}
	$("#business_report_data tbody").html(tbody);
}
/**
 * 显示二级页面
 * @param obj
 */
function showSecPage(obj){
	$("#business_report_data tbody tr").removeClass("select_tr");
	$(obj).addClass("select_tr");
	var branchid = $(obj).attr("branchid");
	localStorage.setItem("branchId", branchid);
	$(parent.document.all("detail")).attr("src",global_Path+"/daliyReports/businessReportSec");
	$("#allSearch").css("visibility","hidden");
}
/**
 * 初始化二级营业报表
 */
function initBusinessReportSec(){
	$.post(global_Path + "/branchbusiness/infos.json", {
		beginTime : beginTime,
		endTime : endTime,
		type : shiftId,
		day : 1
	}, function(result) {
		if(result.flag == 1){
			initBusinessReportSecTb(result.data);
		}else{
			alert(result.desc);
		}
	},'json');
}
function initBusinessReportSecTb(data){
	var tbody = '';
	if(data!= null && data.length>0){
		$.each(data, function(i, item){
			var cla = "";
			if(currDate == item.date){
				cla = "select_tr";
			}
			tbody += '<tr title="双击查看详情" ondblclick="showThirdPage(this)" class="'+cla+'" branchid="'+item.branchId+'" date="'+item.date+'"><td>'+item.date+'</td>'
				+ '<td>'+item.branchName+'</td>'
				+ '<td>'+(item.shouldamount==0?"0.00":item.shouldamount)+'</td>'
				+ '<td>'+(item.paidinamount==0?"0.00":item.paidinamount)+'</td>'
				+ '<td>'+(item.discountamount==0?"0.00":item.discountamount)+'</td>'
				+ '<td>'+(item.cash==0?"0.00":item.cash)+'</td>'
				+ '<td>'+(item.card==0?"0.00":item.card)+'</td>'
				+ '<td>'+(item.othercard==0?"0.00":item.othercard)+'</td>'
				+ '<td>'+(item.weixin==0?"0.00":item.weixin)+'</td>'
				+ '<td>'+(item.zhifubao==0?"0.00":item.zhifubao)+'</td>'
				+ '<td>'+(item.credit==0?"0.00":item.credit)+'</td>'
				+ '<td>'+(item.merbervaluenet==0?"0.00":item.merbervaluenet)+'</td>'
				+ '<td>'+(item.mebervalueadd==0?"0.00":item.mebervalueadd)+'</td>'
				+ '<td>'+(item.integralconsum==0?"0.00":item.integralconsum)+'</td>'
				+ '<td>'+(item.meberTicket==0?"0.00":item.meberTicket)
				+ '<i class="icon-chevron-right" style="color: #000000;float: right;"></i>'
				+'</td>'
				+ '</tr>';
		});
	}else{
		tbody = '<tr><td colspan="15">无数据</td></tr>';
	}
	$("#business_sec_report_data tbody").html(tbody);
}
/**
 * 显示三级页面
 * @param obj
 */
function showThirdPage(obj){
	$("#business_sec_report_data tbody tr").removeClass("select_tr");
	$(obj).addClass("select_tr");
	var branchid = $(obj).attr("branchid");
	var date = $(obj).attr("date");
	localStorage.setItem("branchId", branchid);
	localStorage.setItem("currDate", date);
	$(parent.document.all("detail")).attr("src",global_Path+"/daliyReports/businessReportThird");
	$("#allSearch").css("visibility","hidden");
}
/**
 * 营业报表 一级、二级数据导出
 * @param day：0=一级；1=二级
 */
function exportBusinessReportData(day){
	if(day == 0){
		shiftId = $("#shiftid").val();
		beginTime = $("#beginTime").val();
		endTime = $("#endTime").val();
	}
	location.href = global_Path + "/branchbusiness/exportReportInfos/"+ beginTime + "/" + endTime + "/" + shiftId + "/" + day +".json";
}
/**
 * 营业报表 三级orders导出
 */
function exportBusinessReportOrders(){
	location.href = global_Path + "/branchbusiness/exportReportDayOrders/"+ currDate + "/" + shiftId +".json";
}
/**
 * 初始化三级营业报表
 */
function initBusinessReportThird(){
	$.post(global_Path+"/branchbusiness/dayorders.json", {
		type : shiftId,
		date : currDate
	}, function(result){
		if(result.flag == 1){
			initBusinessReportThirdTb(result.data);
		}else{
			alert(result.desc);
		}
	},'json');
}
function initBusinessReportThirdTb(data){
	var tbody = '';
	if(data!= null && data.length>0){
		$.each(data, function(i, item){
			tbody += '<tr title="双击查看结账单" orderid="'+item.orderId+'" ondblclick="showOrderPage(this)"><td>'+item.orderId+'</td>'
				+ '<td>'+item.beginTime+'</td>'
				+ '<td>'+item.endTime+'</td>'
				+ '<td>'+item.shouldamount+'</td>'
				+ '<td>'+item.paidinamount+'</td>'
				+ '<td>'+item.discountamount+'</td>'
				+ '<td>'+item.cash+'</td>'
				+ '<td>'+item.card+'</td>'
				+ '<td>'+item.othercard+'</td>'
				+ '<td>'+item.weixin+'</td>'
				+ '<td>'+item.zhifubao+'</td>'
				+ '<td>'+item.credit+'</td>'
				+ '<td>'+item.merbervaluenet+'</td>'
				+ '<td>'+item.mebervalueadd+'</td>'
				+ '<td>'+item.integralconsum+'</td>'
				+ '<td>'+item.meberTicket
				+ '<i class="icon-chevron-right" style="color: #000000;float: right;"></i>'
				+'</td>'
				+ '</tr>';
		});
	}else{
		tbody = '<tr><td colspan="16">无数据</td></tr>';
	}
	$("#business_third_report_data tbody").html(tbody);
}
/**
 * 显示结账单
 * @param orderId
 */
function showReckon(orderId){
	$("#orderid_param").val(orderId);
	initReckonData(function(){
		$("#reckoning-dialog").modal("show");
	});
}
/**
 * 选择三级某一条记录
 * @param obj
 */
function showOrderPage(obj){
	$("#business_third_report_data tbody tr").removeClass("select_tr");
	$(obj).addClass("select_tr");
	showReckon($(obj).attr("orderid"));
}
/*******************营业报表 END********************************/


/*******************服务员销售统计表START**************************/
/**
function getWaiterSaleData(){
	showLoading();
	localStorage.setItem("beginTime", $("#beginTime").val());
	localStorage.setItem("endTime", $("#endTime").val());
	branchId = localStorage.getItem("currentStore");
	var waiterName = $(obj).attr("waiterName");
	var dishName = $(obj).attr("dishName");
	var num = $(obj).attr("num");
	localStorage.setItem("waiterName", waiterName);
	localStorage.setItem("dishName", dishName);
	localStorage.setItem("num",num);
	if(compareBeginEndTime()){
		$.post(global_Path + "/waiterSale/getWaiterSaleList.json", {
			beginTime : $("#beginTime").val(),
			endTime : $("#endTime").val(),
			waiterName : $("#waiterName").val(),
			dishName : $("#dishName").val(),
			page:1,
			rows:100
		}, function(result) {
			hideLoading();
			if(result.flag == 1){
				initWaiterSaleTb(result.data);
			}else{
				alert(result.desc);
			}
		},'json');
	}
}

//初始化列表
function initWaiterSaleTb(datalist) {
	var tHtml = "";
	if (datalist != null && datalist != "") {
		$.each(datalist, function(i, obj) {
			var name = obj.NAME;
			var title = obj.title;
			var num = parseFloat(obj.num);
			var userid = obj.userid;
			var dishid = obj.dishid;
			var dishunit = obj.dishunit;
			var dishtype = obj.dishtype;
			tHtml += '<tr dishid="'+dishid+'" userid="'+userid+'"num="'+num.toFixed(0)+'"name="'+name+'"title="'+title+'"dishunit="'+dishunit+'"dishtype="'+dishtype+'" ondblclick="showWaiterSaleSubTb(\''
					+ userid
					+ '\',\''
					+ dishid
					+ '\',\''
					+ num.toFixed(0)
					+ '\',\''
					+ name
					+ '\',\''
					+ title
					+ '\',\''
					+ dishunit
					+ '\',\''
					+ dishtype
					+ '\')">'
					+ '<td width="25%">'
					+ name
					+ '</td>'
					+ '<td width="25%">'
					+ title
					+ '</td>'
					+ '<td width="25%">'
					+ dishunit
					+ '</td>'
					+ '<td width="25%">'
					+ num.toFixed(0)
					+ '</td>'
					+ '</tr>';
		});
	}else{
		tHtml += '<tr><td colspan="4">没有数据</td></tr>';
	}
	$("#waiter-sale-tb tbody").html(tHtml);
} */

//查询服务员销售统计总表
function getWaiterSaleData() {
	if(compareBeginEndTime()){
		page = 0;
		showLoading();
		doWaiterSalePost(function(result){
			hideLoading();
			if(result.flag == 1){
				initWaiterSaleTb(result,true);
			}else{
				alert(result.desc);
			}
		});
	}
}

//查询服务员销售统计总表
function doWaiterSalePost(callback){
	beginTime = $("#beginTime").val();
	endTime = $("#endTime").val();
	waiterName = $("#waiterName").val();
	dishName = $("#dishName").val();
	$.post(global_Path + "/waiterSale/getWaiterSaleList.json", {
		beginTime : beginTime,
		endTime : endTime,
		waiterName : waiterName,
		dishName : dishName,
		page: page,//当前页数
		rows: 20//每页显示条数
	}, function(result) {
		callback(result);
	});
}

//初始化服务员销售统计总表数据
function initWaiterSaleTb(datalist,isFirst){
	var tHtml = "";
	var len = datalist.data.length;
	if (datalist.data != null && datalist.data != "") {
	    page ++;
		if(isFirst){
			var more = '<tr><td id="show-more" class="show-more" colspan="8">加载更多</td></tr>';
			$("#waiter-sale-tb tbody").html(more);
		}
		$.each(datalist.data, function(i, obj) {
			var name = obj.NAME;
			var title = obj.title;
			var num = parseFloat(obj.num);
			var userid = obj.userid;
			var dishid = obj.dishid;
			var dishunit = obj.dishunit;
			var dishtype = obj.dishtype;
			var present = parseFloat(obj.present);
			var discount = parseFloat(obj.discount);
			var currdate = obj.currdate;
			tHtml += '<tr dishid="'+dishid+'" userid="'+userid+'"num="'+num.toFixed(0)+'"name="'+name+'"title="'+title+'"dishunit="'+dishunit+'"dishtype="'+dishtype+'" ondblclick="showWaiterSaleSubTb(\''
					+ userid
					+ '\',\''
					+ dishid
					+ '\',\''
					+ num.toFixed(0)
					+ '\',\''
					+ name
					+ '\',\''
					+ title
					+ '\',\''
					+ dishunit
					+ '\',\''
					+ dishtype
					+ '\')">'
					+ '<td width="12.5%">'
					+ currdate
					+ '</td>'
					+ '<td width="12.5%">'
					+ name
					+ '</td>'
					+ '<td width="12.5%">'
					+ title
					+ '</td>'
					+ '<td width="12.5%">'
					+ dishunit
					+ '</td>'
					+ '<td width="12.5%">'
					+ present.toFixed(0)
					+ '</td>'
					+ '<td width="12.5%">'
					+ discount.toFixed(0)
					+ '</td>'
					+ '<td width="12.5%">'
					+ num.toFixed(0)
					+ '</td>'
					+ '</tr>';
		});
		$("#show-more").parent().before(tHtml);
		if(len < 20){
			$("#show-more").parent().remove();
		}
	}else{
		if(isFirst){
			tHtml += '<tr><td colspan="8">没有数据</td></tr>';
			$("#waiter-sale-tb tbody").html(tHtml);
	    }else{
			alert("没有更多数据");
	    }
	}
	$("#show-more").unbind("click").click(function(){
		doWaiterSalePost(initWaiterSaleTb);
	});
}

/** 显示二级弹出层 */
function showWaiterSaleSubTb(userid,dishid,num,name,title,dishunit,dishtype){
	$("#p_userid").val(userid);
	$("#p_dishid").val(dishid);
	$("#p_num").val(num);
	$("#p_name").val(name);
	$("#p_title").val(title);
	$("#p_dishunit").val(dishunit);
	$("#p_dishtype").val(dishtype);
	$("#waiter-name").text(name);
	$("#dish-name").text(title);
	$("#dish-num").text(num);
	$("#dish-unit").text(dishunit);
	$("#waiter-sale-dialog").modal("show");
	getWaiterSaleDetails();
}

/** 二级弹出层拼数据 */
function getWaiterSaleDetails(){
	var dishunit = $("#p_dishunit").val();
	$.post(global_Path+"/waiterSale/getWaiterSaleDetail.json", {
		beginTime: beginTime,
		endTime: endTime,
		userid: $("#p_userid").val(),
		dishid: $("#p_dishid").val(),
		num: $("#p_num").val(),
		dishunit : encodeURI(encodeURI(dishunit)),
		dishtype : $("#p_dishtype").val()
	}, function(result){
		if(result.flag == 1){
			var data = result.data;
			var htm = '';
			if(data != null && data.length>0){
				$.each(data, function(i, item){
					var dishnum = parseFloat(item.dishnum);
					htm += '<tr ondblclick="showReckon(\''+item.orderid+'\')">'
					    + '<td>'+item.begintime+'</td>'
						+ '<td>'+item.orderid+'</td>'
						+ '<td>'+dishnum.toFixed(0)+'</td></tr>';
				});
			}else{
				htm = '<tr><td colspan="5">无数据</td></tr>';
			}
			$("#waitersale-details-tb tbody").html(htm);
		}else{
			alert(result.desc);
		}
	},'json');
}

/** 导出 */
function exportWaiterSale(type){
	var userid = $("#p_userid").val();
	var dishid = $("#p_dishid").val();
	var dishtype = $("#p_dishtype").val();
	var dishunit = $("#p_dishunit").val();
	$("#_beginTime").val(beginTime);
	$("#_endTime").val(endTime);
	$("#_dishtype").val(dishtype);
	$("#_dishunit").val(dishunit);
	if(type == 0){
		$("#_waiterName").val(waiterName);
		$("#_dishName").val(dishName);
		$("#_searchType").val(searchType);
		$("#waiterSaleForm").attr("action", global_Path + "/waiterSale/exportWaiterSaleMainReport");
		$("#waiterSaleForm").submit();
	}else{
		$("#_userid").val(userid);
		$("#_dishid").val(dishid);
		$("#_searchType").val(searchType);
		$("#waiterSaleForm").attr("action", global_Path + "/waiterSale/exportWaiterSaleChildReport");
		$("#waiterSaleForm").submit();
	}
}
/*******************服务员销售统计表END****************************/
