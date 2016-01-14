/**
 * 不带时分秒的日期格式
 * 
 * @param num
 * @param obj
 */
function gethiddenId_nohms(num, obj) {
	if (num == 3) {
		$("#beginTime").val(getNowDate().split(" ")[0]);
		$("#endTime").val(getNowDate1().split(" ")[0]);
		$("#wdate").show();
	} else {
		if (num == 0) {
			// 获得今日的开始时间
			$("#beginTime").val(getNowDate().split(" ")[0]);
			$("#endTime").val(getNowDate1().split(" ")[0]);
			// $("#wdate").hide();
		} else if (num == 1) {
			// 获取上月的开始时间
			$("#beginTime").val(getLastMonthStartDate().split(" ")[0]);
			$("#endTime").val(getLastMonthEndDate().split(" ")[0]);
			// $("#wdate").hide();
		} else if (num == 2) {
			// 获得本月的开始时间
			$("#beginTime").val(getMonthStartDate().split(" ")[0]);
			$("#endTime").val(getNowDate1().split(" ")[0]);
			// $("#wdate").hide();
		}
		// $("#endTime").val(getNowDate1());
		$("#wdate").hide();
	}
	// $("#wdate").hide();
	// change color FF313E
	// $(obj).parent().children().css("background-color","#FFFFFF");
	// $(obj).css("background-color","#9a9691");
}
/**
 * 导出
 */
function exportReports() {
	if(compareBeginEndTime()){
		var beginTime = $("#beginTime").val();
		var endTime = $("#endTime").val();
//		var shiftid = $("#shiftid").val();
//		var dataType = $("#dataType").val();
		var areaid = areasel;
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
	
			endTime = d.getFullYear() + '-' + month + '-' + day + ' ' + hours + ":"
					+ minutes + ":" + second;
		}
		if (shiftid == "") {
			shiftid = null;
	}
	if (dataType == ""){
		dataType = null;
	}
	if(areaid == "")
		areaid = null;
	location.href = global_Path + "/daliyReports/exportxlsG/" + beginTime + "/"
			+ endTime + "/" + shiftid + "/" + areaid + "/" + dataType + ".json";
	}
}


var map = null;
var areamap = null;
var rows = 0;
/**
 * 查询、初始化数据
 */
function searchData() {
	shiftid = $("#shiftid").val();
	dataType = $("#dataType").val() == null ? "1" : $("#dataType").val();
	areasel = $("#areaSel").val()==null?"-1":$("#areaSel").val();
	initDataStat_new();
}
function initDataStat_new() {
	if(compareBeginEndTime()){
		$.post(global_Path + "/dataDetailController/findDataStatistics.json", {
			beginTime : $("#beginTime").val(),
			endTime : $("#endTime").val(),
			shiftId : shiftid,
			dataType : dataType,
			areaid : areasel
		}, function(result) {
			console.log(result);
			var thHtm = "<thead><tr>";//"<th>区域</th><th>桌号/时间</th>";
			var tbodyHtm = "";
			var frozenTbody = "";
			if(result!=null && result.length>0){
				$.each(result, function(i, data){
					var trHtm='';
					var td_tableid = '';
					var td_area = '';
					var td_value = '';
					var keys = Object.keys(data);
					keys = keys.sort();
					$.each(keys, function(j, key){
						if(i == 0){
							//取表头
							if(key != 'areaname' && key != 'tableid'){
								thHtm += '<th>'+key+'</th>';
							}
						}
						var td = '<td>'+data[key]+'</td>';
						if(key == 'areaname'){
							td_area = td;
						}else if(key == 'tableid'){
							td_tableid = td;
						}else{
							if(dataType == 3 || dataType == 4){
								td = '<td>'+Math.round(data[key])+'</td>';
							}
							td_value += td;
						}
						
					});
					trHtm = '<tr>'+td_value+'</tr>';//td_area+td_tableid+
					frozenTbody += '<tr>'+td_area+td_tableid+'</tr>';
					tbodyHtm += trHtm;
				});
				thHtm += '</tr></thead>';
				$("#frozenDiv").css("width", "15%");
			}else{
//				tbodyHtm = '<tr><td colspan="2">没有数据</td></tr>';
				frozenTbody = '<tr><td colspan="2">没有数据</td></tr>';
				$("#frozenDiv").css("width", "100%");
			}
			$("#frozenTb tbody").html(frozenTbody);
			$("#dataStat_tb").html(thHtm+tbodyHtm);
		},'json');
	}
}
/**
 * 查询类型
 */
function getDataType() {
	$.get(global_Path + "/daliyReports/getCodeList.json", function(result) {
		var option = "";
		$.each(result, function(i, code) {
			option += '<option value="' + code.codeId + '">' + code.codeDesc
					+ '</option>';
		});
		$("#dataType").html(option);
	});
}

var areaMap = null;
/**
 * 区域
 */
function getArea() {
	$.get(global_Path + "/daliyReports/getAreaList.json", function(result) {
		var option = '<option value="-1">全部</option>';
		$.each(result, function(i, item) {
			option += '<option value="' + item.areaNo + '">' + item.areaname
					+ '</option>';
		});
		$("#areaSel").html(option);
	});
}

/******************以下是原来代码 暂时不用***************************/
/**
 * 初始化全部区域、桌号
 * @param callback
 */
function initTableNo(callback){
	map = new HashMap();
	areamap = new HashMap();
	var tablelist = [];
	$.get(global_Path + "/daliyReports/getTableNoList.json", function(result) {
		rows = result.length;
		$.each(result, function(i, code){
			var area = code.codeDesc;
			var aredId = code.aredId;
			if(map.containsKey(aredId)){
				tablelist = map.get(aredId);
			}else{
				tablelist = [];
			}
			tablelist.push(code.codeId);
			map.put(aredId, tablelist);
			areamap.put(aredId, area);
		});
		callback(map, rows);
	});
}

/**
 * 显示api获取数据
 * @param map
 * @param rows
 */
function initDataStat(map, rows) {
	if(compareBeginEndTime()){
		var begin = $("#beginTime").val();
		var end = $("#endTime").val();
		var tThead = '<thead><tr>' + '<th>区域</th>' + '<th>桌号/时间</th>';
		var thValue = getThDate(begin, end);
		console.log(thValue);
		var thHtm = '';
		$.each(thValue, function(i, th) {
			thHtm += '<th>' + th + '</th>';
		});
		$("#dataStat_tb tbody").remove();
		tThead = tThead + thHtm +'</tr></thead>';
		var tbody = "";
		var areaSelId = $("#areaSel").val();
		$.post(global_Path + "/daliyReports/getDatastatistics.json", {
			beginTime : $("#beginTime").val(),
			endTime : $("#endTime").val(),
			shiftId : $("#shiftid").val(),
			dataType : $("#dataType").val() == null ? "1" : $("#dataType").val(),
			areaid : areaSelId
		}, function(result) {
			console.log(result);
			var len = 0;
			map.each(function(areaid, values, num){
				console.log(11);
				if (areaSelId == null
						|| areaSelId == "" || areaSelId == "-1"
						|| (areaSelId != null
								&& areaSelId != "" && areaSelId != "-1" && areaSelId == areaid)) {
				len = values.length;
				var area = areamap.get(areaid);
				console.log(area);
				$.each(values, function(k, tableno){
					tbody += '<tr>';
					if(k == 0){
						tbody += '<td rowspan="'+len+'">' + area + '</td>';
					}
					tbody += '<td>' + tableno + "</td>";
					$.each(thValue, function(m, time){
						tbody += '<td>';
						$.each(result, function(i, data) {
							var arealist = data.list;
							$.each(arealist, function(j, areaData) {
								var area_id = areaData.id;
								if(area_id == areaid){
									var tblist = areaData.list;
									$.each(tblist, function(n, tbs) {
										var tableid = tbs.name;
										if(tableid == tableno){
											var datalist = tbs.list;
											$.each(datalist, function(h, item) {
												var dateTime = item.dateTime;
												var arr = dateTime.split("-");
												dateTime = arr[0]+"/"+arr[1]+"/"+arr[2];
												if(time == dateTime){
													var value = item.values;
													tbody += value;
												}
											});
										}
									});
								}
							});
						});
						tbody += '</td>';
					});//end thtimes.each
					tbody += '</tr>';
				});//end values.each (table)
				}
			});//end map.each
			var htm = tThead + '<tbody>' + tbody +'</tbody>';
			console.log(htm);
			$("#dataStat_tb").html(htm);
		});
	}
}
/**
 * 初始化动态表头
 * @param begin
 * @param end
 * @returns {Array}
 */
function getThDate(begin, end) {
	if (begin != null && begin != "") {
		begin = begin.split(" ")[0];
	}
	if (end != null && end != "") {
		end = end.split(" ")[0];
	}
	var num = dateDiff(begin, end, 0);
	var date1 = getNewDate(begin, 0);
	var thValue = [];
	for (var j = 0; j <= num; j++) {
		var d = getNewDate(begin, 0);
		var value = "";
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
		value = d.getFullYear() + "/" + month + "/" + day;
		thValue.push(value);
	}
	return thValue;
}