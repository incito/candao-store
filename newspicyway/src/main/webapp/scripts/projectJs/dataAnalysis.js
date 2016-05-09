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
		} else if (num == 1) {
			// 获取上月的开始时间
			$("#beginTime").val(getLastMonthStartDate().split(" ")[0]);
			$("#endTime").val(getLastMonthEndDate().split(" ")[0]);
		} else if (num == 2) {
			// 获得本月的开始时间
			$("#beginTime").val(getMonthStartDate().split(" ")[0]);
			$("#endTime").val(getNowDate1().split(" ")[0]);
		}
		$("#wdate").hide();
	}
}
/**
 * 导出
 */
function exportReports() {
	if(compareBeginEndTime()){
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
	beginTime = $("#beginTime").val();
	endTime = $("#endTime").val();
	shiftid = $("#shiftid").val();
	dataType = $("#dataType").val() == null ? "1" : $("#dataType").val();
	areasel = $("#areaSel").val()==null?"-1":$("#areaSel").val();
	initDataStat_new();
}
function initDataStat_new() {
	if(compareBeginEndTime()){
		$.post(global_Path + "/dataDetailController/findDataStatistics.json", {
			beginTime : beginTime,
			endTime : endTime,
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