String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {
 if (!RegExp.prototype.isPrototypeOf(reallyDo)) {
	  return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi": "g")), replaceWith);
	} else {
	   return this.replace(reallyDo, replaceWith);
	}
} 
Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1,
		"d+" : this.getDate(),
		"h+" : this.getHours(),
		"m+" : this.getMinutes(),
		"s+" : this.getSeconds(),
		"q+" : Math.floor((this.getMonth() + 3) / 3),
		"S" : this.getMilliseconds()
	};
	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	}
	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
};

function json2str(obj) {
	var S = [];
	for ( var i in obj) {
		obj[i] = typeof obj[i] == 'string' ? '"' + obj[i] + '"'
				: (typeof obj[i] == 'object' ? json2str(obj[i]) : obj[i]);
		S.push(i + ':' + obj[i]);
	}
	return '{' + S.join(',') + '}';
}

function validateEmpty(obj) {
	if (obj == '' || obj == null || obj == 'undefined' || obj == undefined
			|| typeof (obj) == "undefined") {
		return '';
	}
	return obj;
}
//保留两位小数
 function changeTwoDecimal_f(floatvar) {
	 if(floatvar!=""){
	var f_x = parseFloat(floatvar);
	if (isNaN(f_x)) {
		alert('价格输入错误');
		return false;
	}
	var f_x = Math.round( f_x * 100) / 100;
	var s_x = f_x.toString();
	var pos_decimal = s_x.indexOf('.');
	if (pos_decimal < 0) {
		pos_decimal = s_x.length;
		s_x += '.';
	}
	while (s_x.length <= pos_decimal + 2) {
		s_x += '0';
	}
	return s_x;
	}else{
		return "";
	}
}
 
 
 /**
  * num 为要减少的天数
  * @param num
  * @returns {String}
  */
 function reduceByTransDate( num) {   
	  
	    var  dateString = "", monthString = "", dayString = "";   
//	    translateDate = dateParameter.replace("-", "/").replace("-", "/");;   
	  
	    var newDate = new Date();   
	    newDate = newDate.valueOf();   
	    newDate = newDate - num * 24 * 60 * 60 * 1000;   
	    newDate = new Date(newDate);   
	  
	    //如果月份长度少于2，则前加 0 补位   
	    if ((newDate.getMonth() + 1).toString().length == 1) {   
	  
	        monthString = 0 + "" + (newDate.getMonth() + 1).toString();   
	    } else {   
	  
	        monthString = (newDate.getMonth() + 1).toString();   
	    }   
	  
	    //如果天数长度少于2，则前加 0 补位   
	    if (newDate.getDate().toString().length == 1) {   
	        dayString = 0 + "" + newDate.getDate().toString();   
	    } else {   
	        dayString = newDate.getDate().toString();   
	    }   
	  
	    dateString = newDate.getFullYear() + "-" + monthString + "-" + dayString+" "+newDate.getHours() +":"+ newDate.getMinutes() +":"+newDate.getSeconds();   
	    return dateString; 
	} 
 function getNowDate1() {   
	  
        var dayString ,hourString,minituesString,secondString,dateString;
	  
	    var newDate = new Date();   
	    newDate = newDate.valueOf();   
	    newDate = new Date(newDate);   
	    //如果月份长度少于2，则前加 0 补位   
	    if ((newDate.getMonth() + 1).toString().length == 1) {   
	        monthString = 0 + "" + (newDate.getMonth() + 1).toString();   
	    } else {   
	        monthString = (newDate.getMonth() + 1).toString();   
	    }   
	  
	    //如果天数长度少于2，则前加 0 补位   
	    if (newDate.getDate().toString().length == 1) {   
	        dayString = 0 + "" + newDate.getDate().toString();   
	    } else {   
	        dayString = newDate.getDate().toString();   
	    }   
	  
	     //hourString  小时的处理
	    if (newDate.getHours().toString().length == 1) {   
	    	hourString = 0 + "" + newDate.getHours().toString();   
	    } else {   
	    	hourString = newDate.getHours().toString();   
	    }   
	    
	    //minituesString 分钟的处理
	    if (newDate.getMinutes().toString().length == 1) {   
	    	minituesString = 0 + "" + newDate.getMinutes().toString();   
	    } else {   
	    	minituesString = newDate.getMinutes().toString();   
	    }   
	    
	    //secondString 秒的处理
	    if (newDate.getSeconds().toString().length == 1) {   
	    	secondString = 0 + "" + newDate.getSeconds().toString();   
	    } else {   
	    	secondString = newDate.getSeconds().toString();   
	    }   
	    
//	    dateString = newDate.getFullYear() + "-" + monthString + "-" + dayString+" "+hourString +":"+ minituesString +":"+secondString;   
	    dateString = newDate.getFullYear() + "-" + monthString + "-" + dayString+" 23:59:59";   
        return dateString; 
	} 
 function getNowDate() {   
	  
     var dayString ,hourString,minituesString,secondString,dateString;
	  
	    var newDate = new Date();   
	    newDate = newDate.valueOf();   
	    newDate = new Date(newDate);   
	    //如果月份长度少于2，则前加 0 补位   
	    if ((newDate.getMonth() + 1).toString().length == 1) {   
	        monthString = 0 + "" + (newDate.getMonth() + 1).toString();   
	    } else {   
	        monthString = (newDate.getMonth() + 1).toString();   
	    }   
	  
	    //如果天数长度少于2，则前加 0 补位   
	    if (newDate.getDate().toString().length == 1) {   
	        dayString = 0 + "" + newDate.getDate().toString();   
	    } else {   
	        dayString = newDate.getDate().toString();   
	    }   
	  
	     //hourString  小时的处理
	    if (newDate.getHours().toString().length == 1) {   
	    	hourString = 0 + "" + newDate.getHours().toString();   
	    } else {   
	    	hourString = newDate.getHours().toString();   
	    }   
	    
	    //minituesString 分钟的处理
	    if (newDate.getMinutes().toString().length == 1) {   
	    	minituesString = 0 + "" + newDate.getMinutes().toString();   
	    } else {   
	    	minituesString = newDate.getMinutes().toString();   
	    }   
	    
	    //secondString 秒的处理
	    if (newDate.getSeconds().toString().length == 1) {   
	    	secondString = 0 + "" + newDate.getSeconds().toString();   
	    } else {   
	    	secondString = newDate.getSeconds().toString();   
	    }   
	    
	    //dateString = newDate.getFullYear() + "-" + monthString + "-" + dayString+" "+hourString +":"+ minituesString;// +":"+secondString;   
	    dateString = newDate.getFullYear() + "-" + monthString + "-" + dayString+" 00:00:00";// +":"+secondString;   
     return dateString; 
	} 
 
 /**  
  * 获取本周、本季度、本月、上月的开始日期、结束日期  
  */  
 var now = new Date(); //当前日期   
 var nowDayOfWeek = now.getDay(); //今天本周的第几天   
 var nowDay = now.getDate(); //当前日   
 var nowMonth = now.getMonth(); //当前月   
 var nowYear = now.getYear(); //当前年   
 nowYear += (nowYear < 2000) ? 1900 : 0; //   
 var lastMonthDate = new Date(); //上月日期   
 lastMonthDate.setDate(1);   
 lastMonthDate.setMonth(lastMonthDate.getMonth() - 1);   
 var lastYear = lastMonthDate.getYear();   
 var lastMonth = lastMonthDate.getMonth();   
 //格式化日期：yyyy-MM-dd   
 function formatDate(date) {   
     var myyear = date.getFullYear();   
     var mymonth = date.getMonth() + 1;   
     var myweekday = date.getDate();   
     if (mymonth < 10) {   
         mymonth = "0" + mymonth;   
     }   
     if (myweekday < 10) {   
         myweekday = "0" + myweekday;   
     }   
     return (myyear + "-" + mymonth + "-" + myweekday +" "+date.getHours() +":"+ date.getMinutes() +":"+date.getSeconds() );   
 }
 /**
  * 格式化最后一天日期 后面加上23:59:59
  * @param date
  * @returns {String}
  */
 function formatEndDate(date) {   
     var myyear = date.getFullYear();   
     var mymonth = date.getMonth() + 1;   
     var myweekday = date.getDate();   
     if (mymonth < 10) {   
         mymonth = "0" + mymonth;   
     }   
     if (myweekday < 10) {   
         myweekday = "0" + myweekday;   
     }   
     return (myyear + "-" + mymonth + "-" + myweekday +" 23:59:59");   
 }   
 //获得某月的天数   
 function getMonthDays(myMonth) {   
     var monthStartDate = new Date(nowYear, myMonth, 1);   
     var monthEndDate = new Date(nowYear, myMonth + 1, 1);   
     var days = (monthEndDate - monthStartDate) / (1000 * 60 * 60 * 24);   
     return days;   
 }   
 //获得本季度的开始月份   
 function getQuarterStartMonth() {   
     var quarterStartMonth = 0;   
     if (nowMonth < 3) {   
         quarterStartMonth = 0;   
     }   
     if (2 < nowMonth && nowMonth < 6) {   
         quarterStartMonth = 3;   
     }   
     if (5 < nowMonth && nowMonth < 9) {   
         quarterStartMonth = 6;   
     }   
     if (nowMonth > 8) {   
         quarterStartMonth = 9;   
     }   
     return quarterStartMonth;   
 }   
 //获得本周的开始日期   
 function getWeekStartDate() {   
     var weekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek);   
     return formatDate(weekStartDate);   
 }   
 //获得本周的结束日期   
 function getWeekEndDate() {   
     var weekEndDate = new Date(nowYear, nowMonth, nowDay + (6 - nowDayOfWeek));   
     return formatDate(weekEndDate);   
 }   
 //获得上周的开始日期   
 function getLastWeekStartDate() {   
     var weekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek - 7);   
     return formatDate(weekStartDate);   
 }   
 //获得上周的结束日期   
 function getLastWeekEndDate() {   
     var weekEndDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek - 1);   
     return formatDate(weekEndDate);   
 }   
 //获得本月的开始日期   
 function getMonthStartDate() {   
     var monthStartDate = new Date(nowYear, nowMonth, 1);   
     return formatDate(monthStartDate);   
 }   
 //获得本月的结束日期   
 function getMonthEndDate() {   
     var monthEndDate = new Date(nowYear, nowMonth, getMonthDays(nowMonth));   
     return formatDate(monthEndDate);   
 }   
 //获得上月开始时间   
 function getLastMonthStartDate() {
	 /******add by fandan*********/
		//若当前日期为本年第一天（2015.1.1），则上月的年份为上一年
	 var year = nowYear;
	 if(nowMonth == 0){
		 year = nowYear-1;
	 }
	 /*******end********/
     var lastMonthStartDate = new Date(year, lastMonth, 1);//year由nowYear改的 
     return formatDate(lastMonthStartDate);   
 }   
 //获得上月结束时间   
 function getLastMonthEndDate() {
	 /******add by fandan*********/
	//若当前日期为本年第一天（2015.1.1），则上月的年份为上一年
	 var year = nowYear;
	 if(nowMonth == 0){
		 year = nowYear-1;
	 }
	 /*******end********/
     var lastMonthEndDate = new Date(year, lastMonth, getMonthDays(lastMonth));//year由nowYear改的
     return formatEndDate(lastMonthEndDate);
 }   
 //获得本季度的开始日期   
 function getQuarterStartDate() {   
     var quarterStartDate = new Date(nowYear, getQuarterStartMonth(), 1);   
     return formatDate(quarterStartDate);   
 }   
 //或的本季度的结束日期   
 function getQuarterEndDate() {   
     var quarterEndMonth = getQuarterStartMonth() + 2;   
     var quarterStartDate = new Date(nowYear, quarterEndMonth,   
             getMonthDays(quarterEndMonth));   
     return formatDate(quarterStartDate);   
 }
 
 function gethiddenId(num,obj){
	  if(num==3){
		  $("#beginTime").val(getNowDate());
		  $("#endTime").val(getNowDate1());
		  $("#wdate").show();
		}else{
			if(num == 0){
				//获得今日的开始时间
				$("#beginTime").val(getNowDate());
				$("#endTime").val(getNowDate1());
				//$("#wdate").hide();
			}else if(num == 1){
				//获取上月的开始时间
				$("#beginTime").val(getLastMonthStartDate());
				$("#endTime").val(getLastMonthEndDate());
				//$("#wdate").hide();
			}else if(num == 2){
				//获得本月的开始时间
				$("#beginTime").val(getMonthStartDate());
				$("#endTime").val(getNowDate1());
				//$("#wdate").hide();
			}
			//$("#endTime").val(getNowDate1());
			$("#wdate").hide();
		} 
	  //$("#wdate").hide();
	  //change color  FF313E
	 // $(obj).parent().children().css("background-color","#FFFFFF");
	  //$(obj).css("background-color","#9a9691");
	}
 
	function setshiftid(shiftid,obj){
		$("#shiftid").val(shiftid)  ;
		 //$(obj).parent().children().css("background-color","#FFFFFF");
		 //$(obj).css("background-color","#FF313E");
	}
	
	/*
	 * 根据用户输入的Email跳转到相应的电子邮箱首页
	 */
	function getEmailUrl(mail) {
	    var mailArray = mail.split('@');
	    if(mailArray.length==1){
	    	return null;
	    }
	    t = mailArray[1].toLowerCase();
	    if (t == '163.com') {
	        return 'mail.163.com';
	    } else if (t == 'vip.163.com') {
	        return 'vip.163.com';
	    } else if (t == '126.com') {
	        return 'mail.126.com';
	    } else if (t == 'qq.com' || t == 'vip.qq.com' || t == 'foxmail.com') {
	        return 'mail.qq.com';
	    } else if (t == 'gmail.com') {
	        return 'mail.google.com';
	    } else if (t == 'sohu.com') {
	        return 'mail.sohu.com';
	    } else if (t == 'tom.com') {
	        return 'mail.tom.com';
	    } else if (t == 'vip.sina.com') {
	        return 'vip.sina.com';
	    } else if (t == 'sina.com.cn' || t == 'sina.com') {
	        return 'mail.sina.com.cn';
	    } else if (t == 'tom.com') {
	        return 'mail.tom.com';
	    } else if (t == 'yahoo.com.cn' || t == 'yahoo.cn') {
	        return 'mail.cn.yahoo.com';
	    } else if (t == 'tom.com') {
	        return 'mail.tom.com';
	    } else if (t == 'yeah.net') {
	        return 'www.yeah.net';
	    } else if (t == '21cn.com') {
	        return 'mail.21cn.com';
	    } else if (t == 'hotmail.com') {
	        return 'www.hotmail.com';
	    } else if (t == 'sogou.com') {
	        return 'mail.sogou.com';
	    } else if (t == '188.com') {
	        return 'www.188.com';
	    } else if (t == '139.com') {
	        return 'mail.10086.cn';
	    } else if (t == '189.cn') {
	        return 'webmail15.189.cn/webmail';
	    } else if (t == 'wo.com.cn') {
	        return 'mail.wo.com.cn/smsmail';
	    } else if (t == '139.com') {
	        return 'mail.10086.cn';
	    } else if (t == 'incito.com.cn') {
	        return 'mail.incito.com.cn';
	    } else if (t == 'candaochina.com') {
	        return 'mail.candaochina.com';
	    } else {
	        return null;
	    }
	};
	/**
	 * 隐藏字符串核心部分
	 * @param fromStr 需要处理的字符串，字符串
	 * @param place 需要显示的位数,数字
	 * @param c 需要分隔的字符串(字符串，如@)
	 */
	function hideStringCorePart(fromStr,place,c){
		if(fromStr==null||fromStr==''){
			return '';
		}
		fromStr = new String(fromStr);
		var str,part,other='';
		if(c!=null){
			if(fromStr.indexOf(c)!=-1){
				str = fromStr.substr(0,fromStr.indexOf(c));
				other = fromStr.substr(fromStr.indexOf(c));
			}
		}else{
			str = fromStr;
		}
		var count = str.length;
		if(count<=place){
			part = str;
		}else{
			var leftPlace = (count-place)/2;
			var leftStr = str.substr(0, leftPlace);
			var rightStr = str.substr(leftPlace+place);
			part = leftStr;
			for(var i=0;i<place;i++){
				part += "*";
			}
			part += rightStr;
		}
		if(c!=null){
			part += other;
		}
		return part;
	}
