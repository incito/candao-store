var curTheme = "macarons";

require.config({
	paths : {
		echarts : global_Path + '/tools/echarts/js'
	}
});
plaunchExample();

function plaunchExample() {
	// 按需加载
	require([ 'echarts',
	'echarts/chart/line', 
	'echarts/chart/pie'
	], function(ec) {
		echarts = ec;
	});
}
/** *****************优惠活动统计 START ****************************************** */
/**
 * 切换查询方式
 */
/*
function changeActivy(p) {
	activiyType = p;
	if (p == 0) {
		type = "活动名称";
	} else {
		type = "活动类别";
	}
}
*/
/**
 * 初始化、并切换活动类型
 */
function getPreferentialType(){
	doGetActiviyType(function(result){
		var li = '';
		$.each(result, function(i, item){
			var cla = "";
			if(selPreftType == null || selPreftType == ""){
				if(i == 0){
					cla = "active";
					selPreftType = item.codeId;
				}
			}else{
				if(selPreftType == item.codeId){
					cla = "active";
				}
			}
			li += '<li class="'+cla+'" value="'+item.codeId+'">'+item.codeDesc+'</li>';
		});
		$("#preferential-type-first").html(li);
		$("ul#preferential-type-first li").click(function(){
			$("ul#preferential-type-first li").removeClass("active");
			$(this).addClass("active");
			selPreftType = $(this).val();
			initPreferentialData(selPreftType);
		});
		initPreferentialData(selPreftType);
	});
}
/**
 * 调api 获取数据
 */
function initPreferentialData(selPreftType) {
	showLoading();
	$.post(global_Path + "/preferentialAnalysisCharts/findPreferentialView.json", {
		beginTime : beginTime,
		endTime : endTime,
		dateType : dateType,
		dataType : 0,//按名称查询
		preftType : selPreftType
	}, function(result) {
		console.log(result);
		toActivyNamePie(result);
		toActivyNameLine(result);
		hideLoading();
	});
}
/**
 * 趋势
 * 
 * @param result
 */
function toActivyNameLine(result) {
	var dataArr = result.mapCouDtalTime;
	spendtoNumsTrend(dataArr);//发生笔数
	spendtoMoneyTrend(dataArr);//发生金额
	shouldAmountTrend(dataArr);//应收
	paidinAmountTrend(dataArr);//实收
}
function getActiveData(dataArr, type) {
	var legend_data = [];
	var xAxis_data = [];
	var series = [];
	if (dataArr != null && dataArr.length > 0) {
		xAxis_data = getDataByType();
		$.each(dataArr, function(i, item) {
			if(item.code == type){
				legend_data.push(item.name);
				var obj = {
						name : item.name,
						type : 'line',
						itemStyle : {
							normal : {
								areaStyle : {
									type : 'default'
								}
							}
						},
						stack : '总量'
				};
				obj.data = getData1(item.list, xAxis_data);
				series.push(obj);
			}
		});
	} else {
		var obj = {
			name : "",
			type : 'line',
			itemStyle : {
				normal : {
					areaStyle : {
						type : 'default'
					}
				}
			},
			stack : '总量',
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
/**
 * 
 * @param list
 * @param xAxis_data
 * @param type
 * @returns {Array}
 */
function getData1(list, xAxis_data) {
	var data = [];
	var m = new HashMap();
	if (list != null && list != undefined) {
		$.each(list, function(j, o) {
			m.put(o.insertTime, o);
		});
		for (var j = 0; j < xAxis_data.length; j++) {
			var xValue = xAxis_data[j];
			if (m.containsKey(xValue)) {
				var o = m.get(xValue);
				data.push(o.total);
			} else {
				data.push(0);
			}
		}
	}
	return data;
}
/**
 * 发生笔数 面积
 * 
 * @param legend_data
 * @param series_data
 */
function spendtoNumsTrend(dataArr) {
	var domMain = document.getElementById('nums_trend_main');
	var o = getActiveData(dataArr, 0);
	lineChart_custom("发生笔数", domMain, o.legend_data, o.xAxis_data, o.series);
}

/**
 * 发生金额 面积
 * 
 * @param legend_data
 * @param series_data
 */
function spendtoMoneyTrend(dataArr) {
	var domMain = document.getElementById('amount_trend_main');
	var o = getActiveData(dataArr, 1);
	lineChart_custom("发生金额", domMain, o.legend_data, o.xAxis_data, o.series);
}

/**
 * 拉动应收 面积
 * 
 * @param legend_data
 * @param series_data
 */
function shouldAmountTrend(dataArr) {
	var domMain = document.getElementById('should_amount_trend_main');
	var o = getActiveData(dataArr, 2);
	lineChart_custom("拉动应收", domMain, o.legend_data, o.xAxis_data, o.series);
}
/**
 * 拉动实收 面积
 * 
 * @param legend_data
 * @param series_data
 */
function paidinAmountTrend(dataArr) {
	var domMain = document.getElementById('paidin_amount_trend_main');
	var o = getActiveData(dataArr, 3);
	lineChart_custom("拉动实收", domMain, o.legend_data, o.xAxis_data, o.series);
}
/**
 * 占比
 * 
 * @param result
 */
function toActivyNamePie(result) {
	var legend_data_num = [];
	var legend_data_money = [];
	var legend_data_should = [];
	var legend_data_paidin = [];
	var numArr = [];
	var moneyArr = [];
	var shouldArr = [];
	var paidinArr = [];
	var dataArr = result.findCouponsReptList;
	$.each(dataArr, function(i, item) {
		var name = item.couponsname;
		if(item.code == 0){
			legend_data_num.push(name);
			var datejson = {};
			datejson.value = item.total;
			datejson.name = name;
			numArr.push(datejson);
		}else if(item.code == 1){
			legend_data_money.push(name);
			var moneyObj = {};
			moneyObj.value = strToFloat(item.total);
			moneyObj.name = name;
			moneyArr.push(moneyObj);
		}else if(item.code == 2){
			legend_data_should.push(name);
			var shouldObj = {};
			shouldObj.value = strToFloat(item.total);
			shouldObj.name = name;
			shouldArr.push(shouldObj);
		}else if(item.code == 3){
			legend_data_paidin.push(name);
			var paidinObj = {};
			paidinObj.value = strToFloat(item.total);
			paidinObj.name = name;
			paidinArr.push(paidinObj);
		}
		
	});
	spendtoNums(legend_data_num, numArr);//发生笔数
	spendtoMoney(legend_data_money, moneyArr);//发生金额
	shouldAmount(legend_data_should, shouldArr);//应收
	paidinAmount(legend_data_paidin, paidinArr);//实收
}
/**
 * 发生笔数
 * 
 * @param legend_data
 * @param series_data
 */
function spendtoNums(legend_data, series_data) {
	var domMain = document.getElementById('nums_main');
	pieCharts_custom("发生笔数", domMain, legend_data, series_data);
}
/**
 * 发生金额
 * 
 * @param legend_data
 * @param series_data
 */
function spendtoMoney(legend_data, series_data) {
	var domMain = document.getElementById('amount_main');
	pieCharts_custom("发生金额", domMain, legend_data, series_data);
}

/**
 * 拉动应收
 * 
 * @param legend_data
 * @param series_data
 */
function shouldAmount(legend_data, series_data) {
	var domMain = document.getElementById('should_amount_main');
	pieCharts_custom("拉动应收", domMain, legend_data, series_data);
}
/**
 * 拉动实收
 * 
 * @param legend_data
 * @param series_data
 */
function paidinAmount(legend_data, series_data) {
	var domMain = document.getElementById('paidin_amount_main');
	pieCharts_custom("拉动实收", domMain, legend_data, series_data);
}
/**
 * 饼图
 * 自定义legend
 * @param title
 * @param domMain
 * @param legend_data
 * @param series_data
 */
function pieCharts_custom(title, domMain, legend_data, series_data) {
	var option = {
			title : {
				text : title
			},
			tooltip : {
				trigger : 'item',
				formatter : "{a} <br/>{b} : {c} ({d}%)"
			},
			legend : {
				show : true,
				orient : 'horizontal',
				y : 'bottom',
				data : legend_data
			},
			calculable : true,
			series : [ {
				name : type,
				type : 'pie',
				radius : [ '50%', '75%' ],
				itemStyle : {
					normal : {
						label : {
							show : false
						},
						labelLine : {
							show : false
						}
					},
					emphasis : {
						label : {
							show : true,
							position : 'center',
							textStyle : {
								fontSize : '14',
								fontWeight : 'normal'
							}
						}
					}
				},
				data : series_data
			} ]
		};
	legend_custom(domMain, option, 'pie');
}
/**
 * 堆积面积图
 * 自定义legend
 * @param title
 * @param domMain
 * @param legend_data
 * @param xAxis_data
 * @param series
 */
function lineChart_custom(title, domMain, legend_data, xAxis_data, series) {
	var option = {
		title : {
			text : title,
		},
		tooltip : {
//			trigger : 'axis',
		},
		legend : {
			show :  true,
			orient : 'horizontal',
			y : 'bottom',
			data : legend_data
		},
		calculable : true,
		xAxis : [ {
			type : 'category',
			boundaryGap : false,
			data : xAxis_data
		} ],
		yAxis : [ {
			type : 'value'
		} ],
		series : series
	};
	legend_custom(domMain, option, 'line');
}
/**
 * 自定义legend
 * @param domMain
 * @param option
 * @param type
 */
function legend_custom(domMain, option, type ){
	var myPreChart = echarts.init(domMain, curTheme);
	myPreChart.setOption(option);
	//divLegends
	/*
	var divLegends = $('<div style="width:98%;text-align:center;"></div>').appendTo($(domMain).parent());
	var legend = null;
	if(myPreChart.chart[type] != "" && myPreChart.chart[type] != null && myPreChart.chart[type] != undefined){
		legend = myPreChart.chart[type].component.legend;
	}
	if(legend != null){
		$(option.legend.data).each(function(i, l){
			var color = legend.getColor(l);
			var labelLegend = $('<label class="legend">' +
					'<span class="label" style="background-color:'+color+'"></span>'+l+'</label>');
			
			var hoveColor = changeColor(color);
			labelLegend.mouseover(function(){
				labelLegend.css('color', color).css('font-weight', 'bold');
				if(type == 'line'){
					option.series[i].itemStyle.normal.areaStyle.color = hoveColor;
					myPreChart.setOption(option);
				}else{
					legend.setColor(l, hoveColor);
				}
				myPreChart.refresh();
			}).mouseout(function(){
				labelLegend.css('color', '#333').css('font-weight', 'normal');
				if(type == 'line'){
					option.series[i].itemStyle.normal.areaStyle.color = '';
					myPreChart.setOption(option);
				}else{
					legend.setColor(l, color);
				}
				myPreChart.refresh();
			}).click(function(){
				labelLegend.toggleClass('disabled');
				legend.setSelected(l, !labelLegend.hasClass('disabled'));
			});
			divLegends.append(labelLegend);
		});
	}*/
}
function changeColor(color){
	var rgbColor = '';
	if (color.charAt(0) == '#')
		rgbColor = color.colorRgb();
	else
		rgbColor = color;
	
	var s1 = rgbColor.split("(")[1];
	var s2 = s1.split(")")[0];
	var hoveColor = "rgba("+s2+",0.7)";
	return hoveColor;
}
/** *****************优惠活动统计 END ****************************************** */