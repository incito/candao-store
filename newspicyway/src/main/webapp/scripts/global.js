/**
 * 樊丹
 * 添加自定义滚动条
 */
function customScrollbar(exp){
    if($(exp).hasClass('mCustomScrollbar')){
        if($(exp).children().hasClass('mCustomScrollBox')){
            $(exp).mCustomScrollbar('update');
            return;
        }else{
            $(exp).mCustomScrollbar('destroy');
        }
    }
    $(exp).mCustomScrollbar({
        scrollInertia:400,
        advanced:{
            updateOnContentResize: true,
            autoScrollOnFocus: false
        }
    });
}
function hoverEventScroll(exp){
	$(exp).hover(function(){
		 //这里是放在上面
		 customScrollbar(this);
	    },function(){
	    	//这里是鼠标移走
	    	$(this).mCustomScrollbar('destroy');
	});
}
function clearErrorMsg(selector){
	$(selector).html("");
}
/**
 * 判断数组中是否存在某元素
 * @param array
 * @param obj
 * @returns {Boolean}
 */
function contains(array, obj) {
	var i = array.length;
	while (i--) {
		if (array[i] === obj) {
			return true;
		}
	}
	return false;
}
/**
 * 计算两日期之间的天数
 * 
 * @param sDate1
 * @param sDate2
 * @returns
 */
function dateDiff(sDate1, sDate2, type) { // sDate1和sDate2是2006-12-18格式
	var number = 0;
	if (type == 0) {
		var aDate, bDate, oDate1, oDate2;
		aDate = sDate1.split("-");
//		oDate1 = new Date(aDate[1] + '-' + aDate[2] + '-' + aDate[0]); // 转换为12-18-2006格式
		oDate1 = new Date(aDate[0], aDate[1], aDate[2]);
		bDate = sDate2.split("-");
//		oDate2 = new Date(aDate[1] + '-' + aDate[2] + '-' + aDate[0]);
		oDate2 = new Date(bDate[0], bDate[1], bDate[2]);
		number = parseInt(Math.abs(oDate1 - oDate2) / 1000 / 60 / 60 / 24); // 把相差的毫秒数转换为天数
		var arr = ['01','03','05','07','08','10','12'];
		if(contains(arr, aDate[1])){
/*			if(bDate[1] != aDate[1]){
				number = number + 1;
			}*/
		}else{
			if(bDate[1] != aDate[1]){
				number = number - 1;
			}
		}
	} else if (type == 1) {
		sDate1 = sDate1.replace("-", "/").replace("-", "/");
		sDate2 = sDate2.replace("-", "/").replace("-", "/");
		var sDate1_arr = sDate1.split("/");
		var sDate2_arr = sDate2.split("/");
		var startDate = new Date(sDate1_arr[0], sDate1_arr[1]);
		var endDate = new Date(sDate2_arr[0], sDate2_arr[1]);
		var yearToMonth = (endDate.getFullYear() - startDate.getFullYear()) * 12;
		number += yearToMonth;
		monthToMonth = endDate.getMonth() - startDate.getMonth();
		number += monthToMonth;
		number = number+1;
	}else{
		number = parseInt(sDate2)-parseInt(sDate1);
		number = number+1;
	}
	return number;
}    


function HashMap(){ 
	/** 存放键的数组(遍历用到) */
	this.keys = new Array();
    //创建一个对象  
    var obj = new Object();  
  
    /** 
    * 判断Map是否为空 
    */  
    this.isEmpty = function(){  
        return this.keys.length == 0;  
    };  
  
    /** 
    * 判断对象中是否包含给定Key 
    */  
    this.containsKey=function(key){  
        return (key in obj);  
    };  
  
    /** 
    * 判断对象中是否包含给定的Value 
    */  
    this.containsValue=function(value){  
        for(var key in obj){  
            if(obj[key] == value){  
                return true;  
            }  
        }  
        return false;  
    };  
  
    /** 
    *向map中添加数据 
    */  
    this.put=function(key,value){  
        if(!this.containsKey(key)){
        	this.keys.push(key);
        }  
        obj[key] = value;  
    };  

    /** 
    * 根据给定的Key获得Value 
    */  
    this.get=function(key){  
        return this.containsKey(key)?obj[key]:null;  
    };  
  
    /** 
    * 根据给定的Key删除一个值 
    */  
    this.remove=function(key){  
        if(this.containsKey(key)&&(delete obj[key])){  
        	this.keys.remove(key);
        }  
    };  
  
    /** 
    * 获得Map中的所有Value 
    */  
    this.values=function(){  
        var _values= new Array();  
        for(var key in obj){  
            _values.push(obj[key]);  
        }  
        return _values;  
    };  
  
    /** 
    * 获得Map中的所有Key 
    */  
    this.keySet=function(){  
        var _keys = new Array();  
        for(var key in obj){  
            _keys.push(key);  
        }  
        return _keys;  
    };  
  
    /** 
    * 获得Map的长度 
    */  
    this.size = function(){  
        return this.keys.length;  
    };  
  
    /** 
    * 清空Map 
    */  
    this.clear = function(){  
    	this.keys = new Array(); 
        obj = new Object();  
    };  
    
	/**
	 * 遍历Map,执行处理函数
	 * 
	 * @param {Function}
	 *            回调函数 function(key,value,index){..}
	 */
	this.each = function(fn) {
		if (typeof fn != 'function') {
			return;
		}
		var len =this.keys.length;
		for (var i = 0; i < len; i++) {
			var k = this.keys[i];
			fn(k, this.get(k), i);
		}
	};
}

/*******************以下是页面跳转***********************/
//营业报表分析
function toAnalysis(){
	$(parent.document.all("detail")).attr("src",global_Path+"/daliyReports/reportAnalysis");
	$("#allSearch").css("visibility","hidden");
}

//详细数据统计表
function toDatastatistics(){
	$(parent.document.all("detail")).attr("src",global_Path+"/daliyReports/Datastatistics");
	$("#allSearch").css("visibility","hidden");
}
 //报表的 4个界面
//营业数据报表
function toSaleRept(beginTime,endTime, dateType){
	if(beginTime == null || beginTime == ""){
		$(parent.document.all("detail")).attr("src", global_Path+"/daliyReports/daysalerept");
	}else{
		$(parent.document.all("detail")).attr("src", global_Path+"/daliyReports/daysalerept?beginTime="+beginTime+"&endTime="+endTime+"&dateType="+dateType);
	}
	
	$("#allSearch").css("visibility","hidden");
}
 
function toSettleDetailRept(){
	$(parent.document.all("detail")).attr("src", global_Path+"/daliyReports/paywayrept");
	$("#allSearch").css("visibility","hidden");
}
 
function toDishSaleRept(beginTime,endTime, dateType){
	$("#title_p").html("品项销售明细表");
	if(beginTime == null || beginTime == ""){
		$(parent.document.all("detail")).attr("src", global_Path+"/daliyReports/dishsalerept");
	}else{
		$(parent.document.all("detail")).attr("src", global_Path+"/daliyReports/dishsalerept?beginTime="+beginTime+"&endTime="+endTime+"&dateType="+dateType);
	}
	$("#allSearch").css("visibility","hidden");
}
 
function toCouponsRept(beginTime,endTime, dateType){
	$("#title_p").html("优惠活动明细表");
	if(beginTime == null || beginTime == ""){
		$(parent.document.all("detail")).attr("src", global_Path+"/daliyReports/couponsrept");
	}else{
		$(parent.document.all("detail")).attr("src", global_Path+"/daliyReports/couponsrept?beginTime="+beginTime+"&endTime="+endTime+"&dateType="+dateType);
	}
	$("#allSearch").css("visibility","hidden");
}
/**
 * 排班参考报表
 */
function toScheduleReport(){
	$(parent.document.all("detail")).attr("src", global_Path+"/daliyReports/topage?path=billDetails/scheduleReport");
	$("#allSearch").css("visibility","hidden");
}
/**
 * 服务员考核报表
 */
function toWaiterAssess(){
	$(parent.document.all("detail")).attr("src", global_Path+"/daliyReports/topage?path=billDetails/waiterAssessment");
	$("#allSearch").css("visibility","hidden");
}
/**
 * 跳转到租户管理
 */
function toTenant(){
	$(parent.document.all("detail")).attr("src",global_Path+"/tenant/list");
	$("#allSearch").css("visibility","visible");
}
/**
 * 跳转到员工管理
 */
function toEmployees(){
	//$(parent.document.all("detail")).attr("src",global_Path+"/t_employeeUser/list");
	$(parent.document.all("detail")).attr("src",global_Path+"/skipCloud.jsp?url=t_employeeUser/list");
	$("#allSearch").css("visibility","hidden");
}
/**
 * 跳转到账户管理
 */
function toNormal(){
	$(parent.document.all("detail")).attr("src",global_Path+"/t_headuser/account");
	$("#allSearch").css("visibility","visible");
	$(parent.document).find("#searchText").val("");
	$("#allSearch .autoComplete").remove();
}

/**
 * 跳转到支付管理
 */
function toPayment(){
	$(parent.document.all("detail")).attr("src", global_Path+"/daliyReports/topage?path=payment/paymentlist");
	$("#allSearch").css("visibility","hidden");
}
/**
 * 跳转到我的账户
 */
function toMyAccount(){
	if(global_isbranch=='Y'){//Y是分店
		$(parent.document.all("detail")).attr("src",global_Path+"/skipCloud.jsp?url=t_user/myAccount");
	}else{
		$(parent.document.all("detail")).attr("src",global_Path+"/t_user/myAccount");
	}
	//修改上方显示文本
	$(".ky-container-iframe").find(".ky-title p#title_p").html("我的账户");
	$("#allSearch").css("visibility","hidden");
}
/**
 * 跳转到优惠管理
 */
function toPreferterial(){
	$(parent.document.all("detail")).attr("src",global_Path+"/preferential");
	$("#allSearch").css("visibility","visible");
	localStorage.removeItem("currentType");
	localStorage.removeItem("currentPage");
	$(parent.document).find("#searchText").val("");
}
//以上是报表的4个界面
/*******************以上是页面跳转***********************/
//系统设置
function toSetup(){
	$(parent.document.all("detail")).attr("src",global_Path+"/system/systemSet");
	$("#allSearch").css("visibility","hidden");
}

//退菜明细表
function toAskedForARefund(){
	
	$(parent.document.all("detail")).attr("src",global_Path+"/returnDish/askedForARefund");
	$("#allSearch").css("visibility","hidden");
}
/**
 * 详细数据统计表
 */
function toDataStatistics(){
	$(parent.document.all("detail")).attr("src",global_Path+"/daliyReports/dataStatistics");
	$("#allSearch").css("visibility","hidden");
}
/**
 * 反结算统计表
 */
function toTheSettlementStatistics(){
	$(parent.document.all("detail")).attr("src",global_Path+"/daliyReports/theSettlement");
	$("#allSearch").css("visibility","hidden");
}
/**
 * 交接班统计表
 */
function toPresentStatistics(){
	$(parent.document.all("detail")).attr("src",global_Path+"/daliyReports/presentStatistics");
	$("#allSearch").css("visibility","hidden");
}
/**
 * 营业报表
 */
function toBusinessReport(){
	$(parent.document.all("detail")).attr("src",global_Path+"/daliyReports/businessReportFirst");
	$("#allSearch").css("visibility","hidden");
}
/**
 * 财务报表——结算方式信息
 */
function toPaywayInfo(){
	$(parent.document.all("detail")).attr("src", global_Path+"/daliyReports/topage?path=billDetails/paywayInfoReports");
	$("#allSearch").css("visibility","hidden");
}
/**
 * 财务报表——营业汇总表
 */
function toBusinessSum(){
	$(parent.document.all("detail")).attr("src", global_Path+"/daliyReports/topage?path=billDetails/businessSumReports");
	$("#allSearch").css("visibility","hidden");
}
/**
 * 设置iframe高度自适应屏幕
 * 暂时没有用
 */
function iFrameHeight() {
	var ifm = document.getElementById("detail");
	var subWeb = document.frames ? document.frames["detail"].document : ifm.contentDocument;
	if (ifm != null && subWeb != null) {
		ifm.height = subWeb.body.scrollHeight+120;
	}
}
/**
 * easyui loading 添加
 */
function ajaxLoading(){ 
    $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body"); 
    $("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候。。。").appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2}); 
}
/**
 * easyui loading 取消
 */
function ajaxLoadEnd(){
	$(".datagrid-mask").remove();
	$(".datagrid-mask-msg").remove();
} 
function isNullObj(obj){
    for(var i in obj){
        if(obj.hasOwnProperty(i)){
            return false;
        }
    }
    return true;
}
/**
 * 清空计时器
 * @param intervalProcess
 */
function clearIntervalProcess(intervalProcess){
	if(intervalProcess != null )
		clearInterval(intervalProcess);
}