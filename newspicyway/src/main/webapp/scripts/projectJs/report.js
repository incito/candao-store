$(document).ready(function(){
	$("img.img-close").hover(function(){
	 	$(this).attr("src", global_Path+"/images/close-active.png");	 
	},function(){
		$(this).attr("src", global_Path+"/images/close-sm.png");
	});
});
/**
 * 切换查询类型的时候，为searchType赋值
 * @param type
 * @param o
 */
function changeDataType(type, o){
	searchType = type;
	gethiddenId(type, o);
}
/** *****************营业数据统计 START ******************************************** */
function initBussinessData() {
	$.post(global_Path + "/daliyReports/getBusinessReport.json", {
		beginTime : beginTime,
		endTime : endTime,
		Datetype : dateType == 0 ? "D" : (dateType == 1 ? "M" : "Y")
	}, function(result) {
		console.log("营业数据统计");
		console.log(result);
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
						+ '</td><td>' + item.discountamount + '</td></tr>';
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
/** *****************营业数据统计 END ******************************************** */
/** ******************品项销售统计 START **************************************** */
function initItemData() {
	$.post(global_Path + "/itemAnalysisCharts/getItemReport.json", {
		beginTime : beginTime,
		endTime : endTime,
		Datetype : dateType == 0 ? "D" : (dateType == 1 ? "M" : "Y")
	}, function(result) {
		console.log("品项销售统计");
		console.log(result);
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

		// 售卖份数趋势
		saleCountTrend(result.ItemNumQushiReport);
		// 售卖金额趋势
		saleAmountTrend(result.ItemShareQushiReport);
	});
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
		m.put(o.statistictime, o);
	});
	for (var j = 0; j < xAxis_data.length; j++) {
		var xValue = xAxis_data[j];
		if (m.containsKey(xValue)) {
			var o = m.get(xValue);
			if (type == "num")
				data.push(o.num);
			else if (type == "share")
				data.push(strToFloat(o.share));
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
			beginTime : $("#beginTime").val(),
			endTime : $("#endTime").val(),
			shiftid : $("#shiftid").val()
		}, function(result) {
			console.log("营业数据明细报表");
			console.log(result);
			incomeStatistics(result);
			paidInAmount(result);
			discountAmount(result);
			businessDataTb(result);
			meberDataTb(result);
		});
	}
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
}
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
		var beginTime = $("#beginTime").val();
		var endTime = $("#endTime").val();
//		var shiftid = $("#shiftid").val();
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
	if(compareBeginEndTime()){
		var tb = "";
		$.post(global_Path + "/settlementOption/settlementOptionList.json", {
			beginTime : $("#beginTime").val(),
			endTime : $("#endTime").val(),
			shiftid : $("#shiftid").val()
		}, function(result) {
			console.log("结算方式明细报表");
			console.log(result);
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
	
					tb += '<tr><td>' + item.payway + '</td><td>' + item.nums
							+ '</td><td>' + item.prices + '</td></tr>';
				});
			}
			$("#payway_data tbody").html(tb);
			paywayNums(legend_data, num_series_data);
			paywayPrices(legend_data, price_series_data);
		});
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
function exportxlsC() {
	if(compareBeginEndTime()){
		//var settlementWay=$("#settlementWay").combobox('getValue');
		var beginTime = $("#beginTime").val();
		var endTime = $("#endTime").val();
//		var shiftid = $("#shiftid").val();
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
		location.href = global_Path + "/settlementOption/exportXls?beginTime="
				+ beginTime + "&endTime=" + endTime + "&shiftid=" + shiftid+"&searchType="+searchType;
	}
}
/** ********************结算方式明细报表 END*************************************** */

/***********************退菜明细表 START *******************************************/
var page = 0;
function initReturnDishData() {
	if(compareBeginEndTime()){
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
			tb += '<tr><td>' + item.beginTime + '</td><td>' + item.orderid + '</td><td>' + item.title 
			+ '</td><td>'+ item.num + '</td><td>' + item.amount + '</td><td>' + item.waiter 
		    + '</td><td>' + item.discardusername + '</td><td>' + item.discardreason + '</td></tr>';
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
		beginTime : $("#beginTime").val(),
		endTime : $("#endTime").val(),
		shiftid : $("#shiftid").val(),
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
		var beginTime = $("#beginTime").val();
		var endTime = $("#endTime").val();
//		var shiftid = $("#shiftid").val();
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
		/*
		location.href = global_Path + "/daliyReports/exportReturnDishxls?beginTime="
				+ beginTime + "&endTime=" + endTime + "&shiftid=" + shiftid;*/
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
		beginTime : $("#beginTime").val(),
		endTime : $("#endTime").val(),
		shiftid : $("#shiftid").val(),
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
			tHtml += '<tr onclick="showsubTb(\''
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
				+ '<td width="20%">'
				+ obj.pname
				+ '</td>'
				+ '<td width="15%">'
				+ obj.ptypename
				+ '</td>'
				+ '<td width="15%">'
				+ obj.paywaydesc
				+ '</td>'
				+ '<td width="10%">'
				+ obj.couponNum
				+ '</td>'
				+ '<td width="10%">'
				+ obj.payamount
				+ '</td>'
				+ '<td width="15%">'
				+ obj.shouldamount
				+ '</td>'
				+ '<td width="15%">'
				+ obj.paidinamount 
				+ '<i class="icon-chevron-right" style="color: #000000;float: right;"></i></td>' 
				+ '</tr>';
		});
	}else{
		tHtml += '<tr><td colspan="7">没有数据</td></tr>';
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
		beginTime : $("#beginTime").val(),
		endTime : $("#endTime").val(),
		shiftid : $("#shiftid").val(),
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
			subTbody += "<tr>";
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
	$.get(global_Path+"/daliyReports/getActivityNameList.json", function(result){
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
 * 活动类别
 */
function getActiviyType() {
	$.get(global_Path+"/daliyReports/getTypeDictList.json", function(result){
		var option = '<option value="-1">全部</option>';
		$.each(result, function(i, item){
			option += '<option value="'+item.codeId+'">'+item.codeDesc+'</option>';
		});
		$("#type").html(option);
	});
	
}
/***********************优惠活动明细表 END****************************************/
/***********************品项销售明细表 START**************************************/
//调api获取数据
function initItemDetailsData() {
	if(compareBeginEndTime()){
		shiftid = $("#shiftid").val();
		itemId = $("#itemID").val()==null?"-1":$("#itemID").val();
		dishtype = $("#dishType").val();
		doItemDetailsPost(initItemTb);
	}
}
function doItemDetailsPost(callback){
	$.post(global_Path + "/itemDetail/getItemForList.json", {
		beginTime : $("#beginTime").val(),
		endTime : $("#endTime").val(),
		shiftid : $("#shiftid").val(),
		id : itemId,
		dishType : dishtype
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
			var dishType = obj.dishtype;
			var typedesc = dishType == 0 ? "单品" : (dishType == 1 ? "鱼锅" : "套餐");
			tHtml += '<tr itemid="'+obj.id+'" dishType="'+dishType+'" onclick="showItemSubTb(\''
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
					+ '<td width="15%">' + Math.round(obj.number)
					+ '</td>' + '<td width="15%">'
					+ share.toFixed(2) + '<i class="icon-chevron-right" style="color: #000000;float: right;"></i></td>'
					+ '</tr>';
		});
	}else{
		tHtml += '<tr><td colspan="4">没有数据</td></tr>';
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
		beginTime : $("#beginTime").val(),
		endTime : $("#endTime").val(),
		shiftid : $("#shiftid").val()
	}, function(result) {
		var subTbody = "";
		$.each(result, function(i, item) {
			var share = parseFloat(item.share);
			subTbody += "<tr>";
			subTbody += "<td>" + item.title + "</td>";
			subTbody += "<td>" + item.dishNo + "</td>";
			subTbody += "<td>" + item.price + "</td>";
			subTbody += "<td>" + item.unit + "</td>";
			subTbody += "<td>" + Math.round(item.number) + "</td>";
			subTbody += "<td>" + share.toFixed(2) + "</td>";
			subTbody += "</tr>";
		});
		$("#item_sub_tb tbody").html(subTbody);
	},'json');
}
/**
 * 品类
 */
function getItemType() {
	$.get(global_Path + "/itemDetail/getItemTypeList.json", function(
			result) {
		var option = '<option value="-1">全部</option>';
		$.each(result, function(i, item) {
			option += '<option value="'+item.codeId+'">'
					+ item.codeDesc + '</option>';
		});
		$("#itemID").html(option);
	});
}
/***********************品项销售明细表 END***************************************/