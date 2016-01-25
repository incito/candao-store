var myChart;
var curTheme = "macarons";

require.config({
	paths : {
		echarts : global_Path + '/tools/echarts/js'
	}
});

function launchExample(domMain, option) {
	// 按需加载
	require([ 'echarts',
	// 'theme/' + hash.replace('-en', ''),
	'echarts/chart/line', 
	'echarts/chart/bar', 
//	'echarts/chart/scatter',
//	'echarts/chart/k', 
	'echarts/chart/pie'
//	'echarts/chart/radar',
//	'echarts/chart/force', 
//	'echarts/chart/chord',
//	'echarts/chart/gauge', 
//	'echarts/chart/funnel',
//	'echarts/chart/eventRiver', 
//	'echarts/chart/venn',
//	'echarts/chart/treemap'
	// needMap() ? 'echarts/chart/map' : 'echarts'
	], function(ec) {
		myChart = ec.init(domMain, curTheme);
		myChart.setOption(option, true);
		echarts = ec;
	});
}
/**
 * 标准环形图
 * 
 * @param title
 * @param domMain
 * @param domMessage
 * @param legend_data
 * @param series_data
 */
function pieCharts(title, domMain, legend_data, series_data) {
	var option = {
		title : {
			text : title
		},
		tooltip : {
			trigger : 'item',
			formatter : "{a} <br/>{b} : {c} ({d}%)"
		},
		legend : {
			orient : 'horizontal',
			y : 'bottom',
			formatter: function (series) {
				if(series.length > 5){
					return series.substring(0, 5)+'...';
				}else{
					return series;
				}
            },
			data : legend_data
		},
		calculable : true,
		series : [ {
			name : type,
			type : 'pie',
			radius : [ '50%', '70%' ],
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
						show : false,
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
	launchExample(domMain, option);
}
/**
 * 堆积折线图/面积图
 * 
 * @param title
 * @param domMain
 * @param legend_data
 * @param xAxis_data
 * @param series
 */
function lineChart(title, domMain, legend_data, xAxis_data, series) {
	var option = {
		title : {
			text : title,
		},
		tooltip : {
//			trigger : 'axis',
		},
		legend : {
			orient: 'vertical',
			x : 'right',
			formatter: function (series) {
				if(series.length > 5){
					return series.substring(0, 5)+'...';
				}else{
					return series;
				}
            },
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
		grid: {
	        x2:120
	    },
		series : series
	};
	launchExample(domMain, option);
}
/**
 * 标准条形图
 * 
 * @param title
 * @param domMain
 * @param yAxis_data
 * @param series_data
 */
function barChart(title, domMain, yAxis_data, series_data) {
	var option = {
		title : {
			text : title,
		},
		tooltip : {
			trigger : 'axis',
			axisPointer : { // 坐标轴指示器，坐标轴触发有效
				type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
			}
		},
		calculable : true,
		xAxis : [ {
			type : 'value'
		} ],
		yAxis : [ {
			type : 'category',
			axisLabel : { 
	        	formatter: function(value){
	            	if(value.length > 5){
	                  return value.substring(0, 5)+"...";
	                }else{
	                	return value;
	                }
	            }
	        },
			data : yAxis_data
		} ],
		series : [ {
			name : '总量',
			type : 'bar',
			itemStyle : {
				normal : {
					label : {
						show : true,
						position : 'insideRight'
					}
				}
			},
			barMinHeight: 50,
			data : series_data
		} ]
	};
	launchExample(domMain, option);
}
/**
 * 标准饼状图
 * @param title
 * @param domMain
 * @param legend_data
 * @param series_data
 */
function _pieChart(title, domMain, legend_data, series_data, flag) {
	var option = {
		title : {
			text : title
		},
		tooltip : {
			trigger : 'item',
			formatter : "{a} <br/>{b} : {c} ({d}%)"
		},
		legend : {
			orient : 'vertical',
			x : "right",
			data : legend_data
		},
		calculable : true,
		series : [ {
			type : 'pie',
			radius : '50%',
			center : [ '50%', '50%' ],
			data : series_data
		} ]
	};
	if(flag == 1){
		//title居中，图例放在图表下面
		option.title = {text: title, x: "center"};
		option.legend = {show: false, orient : 'horizontal', y : "bottom", data : legend_data};
		launchExample(domMain, option);
		g_legend_custom(domMain, option, "pie");
	}else{
		launchExample(domMain, option);
	}
}
/**
 * 自定义legend
 * @param domMain
 * @param option
 * @param type
 */
function g_legend_custom(domMain, option, type ){
	var myPreChart = echarts.init(domMain, curTheme);
	myPreChart.setOption(option);
	//divLegends
	var divLegends = $('<div class="legend-custom-div" style="width:98%;margin-top: 5px;text-align:center;float:left;"></div>').appendTo($(domMain).parent());
	var legend = null;
	if(myPreChart.chart[type] != "" && myPreChart.chart[type] != null && myPreChart.chart[type] != undefined){
		legend = myPreChart.chart[type].component.legend;
	}
	if(legend != null){
		$(option.legend.data).each(function(i, l){
			var color = legend.getColor(l);
			var labelLegend = $('<label class="legend">' +
					'<span class="label" style="background-color:'+color+'"></span>'+l+'</label>');
			
			var hoveColor = g_changeColor(color);
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
	}
}
function g_changeColor(color){
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